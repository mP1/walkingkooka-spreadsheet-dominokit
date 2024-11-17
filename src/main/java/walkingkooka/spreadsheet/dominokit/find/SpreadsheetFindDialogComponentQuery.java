/*
 * Copyright 2023 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.spreadsheet.dominokit.find;

import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.engine.SpreadsheetCellQuery;
import walkingkooka.spreadsheet.expression.SpreadsheetFunctionName;
import walkingkooka.spreadsheet.expression.function.SpreadsheetExpressionFunctions;
import walkingkooka.spreadsheet.expression.function.TextMatch;
import walkingkooka.spreadsheet.parser.SpreadsheetConditionParserToken;
import walkingkooka.spreadsheet.parser.SpreadsheetConditionRightParserToken;
import walkingkooka.spreadsheet.parser.SpreadsheetFunctionParserToken;
import walkingkooka.spreadsheet.parser.SpreadsheetParserToken;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.expression.ExpressionFunctionName;

import java.util.List;
import java.util.Optional;

/**
 * Helper that provides methods to update the FORM query from other wizard fields,
 * AND (TODO)
 * Update wizard fields from the query
 */
final class SpreadsheetFindDialogComponentQuery implements PublicStaticHelper {

    /**
     * Refreshes the {@link SpreadsheetFindDialogComponent#query} from other wizard component fields.
     */

    static Optional<SpreadsheetFormula> query(final Optional<SpreadsheetCellQuery> query,
                                              final Optional<TextMatch> formula,
                                              final Optional<TextMatch> formatter,
                                              final Optional<TextMatch> parser,
                                              final Optional<TextMatch> style,
                                              final Optional<SpreadsheetConditionRightParserToken> value,
                                              final Optional<TextMatch> formattedValue) {
        ParserToken token = query.map(
                q -> q.parserToken()
        ).orElse(null);

        final List<SpreadsheetParserToken> or = Lists.array();

        token = replaceTextMatchOr(
                token, // old
                formula,
                SpreadsheetExpressionFunctions.CELL_FORMULA,
                or
        );

        token = replaceTextMatchOr(
                token, // old
                formatter,
                SpreadsheetExpressionFunctions.CELL_FORMATTER,
                or
        );

        token = replaceTextMatchOr(
                token, // old
                parser,
                SpreadsheetExpressionFunctions.CELL_PARSER,
                or
        );

        token = replaceTextMatchOr(
                token, // old
                style,
                SpreadsheetExpressionFunctions.CELL_STYLE,
                or
        );

        token = replaceCellValueOr(
                token, // old
                value,
                or
        );

        token = replaceTextMatchOr(
                token, // old
                formattedValue,
                SpreadsheetExpressionFunctions.CELL_FORMATTED_VALUE,
                or
        );

        // append OR expressions
        token = or(
                token,
                or
        );

        return Optional.ofNullable(
                null != token ?
                        SpreadsheetFormula.EMPTY.setToken(
                                Optional.ofNullable(
                                        token.cast(SpreadsheetParserToken.class)
                                )
                        ).setText(
                                null != token ?
                                        token.text() :
                                        ""
                        ) :
                        null
        );
    }

    /**
     * Attempts to replace an old textMatch call with a new or updates the expression with an OR.
     */
    private static ParserToken replaceTextMatchOr(final ParserToken old,
                                                  final Optional<TextMatch> textMatch,
                                                  final ExpressionFunctionName cellPropertyGetter,
                                                  final List<SpreadsheetParserToken> or) {
        return replaceTextMatchOr0(
                old,
                textMatch.orElse(null),
                cellPropertyGetter,
                or
        );
    }

    private static ParserToken replaceTextMatchOr0(final ParserToken old,
                                                   final TextMatch textMatch,
                                                   final ExpressionFunctionName cellPropertyGetter,
                                                   final List<SpreadsheetParserToken> or) {
        ParserToken token = old;

        if (null == old) {
            // only overwrite $expression if textMatch is also not empty
            if (null != textMatch && textMatch.isNotEmpty()) {
                // expression = textMatch(component.value, cellXXX())
                token = textMatch(
                        textMatch,
                        cellPropertyGetter
                );
            }
        } else {
            // try replace any previous textMatch(component.value, cellXXX())
            token = old.replaceIf(
                    SpreadsheetFindDialogComponentQueryTextMatchFunctionParserTokenPredicate.with(cellPropertyGetter), // predicate
                    (e) -> textMatch(
                            textMatch,
                            cellPropertyGetter
                    ) // mapper
            );

            // if replace DID NOT happened then create OR expression(old, textMatch(component.value, cellXXX)
            if (old.equals(token) && null != textMatch) {
                // append ors at the end to create expressions like
                //
                // or(1, or(2,3))
                //
                // and NOT
                //
                // or(or(1,or(2,3))
                or.add(
                        textMatch(
                                textMatch,
                                cellPropertyGetter
                        )
                );
            }
        }

        return token;
    }

    /**
     * Factory that creates an {@link SpreadsheetParserToken} holding a textMatch function call.
     * <pre>
     * textMatch(
     *   value,
     *   cellXXX()
     * )
     * </pre>
     */
    private static SpreadsheetParserToken textMatch(final TextMatch textMatch,
                                                    final ExpressionFunctionName cellPropertyGetter) {
        final String textMatchString = textMatch.text();
        final String quotedTextMatch = CharSequences.escape(
                textMatchString
        ).toString();

        final String cellPropertyGetterName = cellPropertyGetter.value();
        final String textMatchFunction = TEXT_MATCH_FUNCTION_NAME + "(\"" + quotedTextMatch + "\"," + cellPropertyGetterName + "())";

        return SpreadsheetParserToken.namedFunction(
                Lists.of(
                        TEXT_MATCH_FUNCTION_NAME,
                        SpreadsheetParserToken.functionParameters(
                                Lists.of(
                                        SpreadsheetParserToken.text(
                                                Lists.of(
                                                        DOUBLE_QUOTE_SYMBOL,
                                                        SpreadsheetParserToken.textLiteral(
                                                                quotedTextMatch,
                                                                textMatchString
                                                        ),
                                                        DOUBLE_QUOTE_SYMBOL
                                                ),
                                                "" + '"' + quotedTextMatch + '"'
                                        ),
                                        SpreadsheetParserToken.namedFunction(
                                                Lists.of(
                                                        functionName(cellPropertyGetter),
                                                        EMPTY_PARAMETER_LIST
                                                ),
                                                cellPropertyGetterName + "()"
                                        )
                                ),
                                textMatchFunction
                        )
                ),
                textMatchFunction
        );
    }

    private final static SpreadsheetParserToken TEXT_MATCH_FUNCTION_NAME = functionName(
            SpreadsheetExpressionFunctions.TEXT_MATCH
    );

    /**
     * Attempts to replace an old cellValue call with a new or updates the expression with an OR.
     */
    private static ParserToken replaceCellValueOr(final ParserToken old,
                                                  final Optional<SpreadsheetConditionRightParserToken> conditionRight,
                                                  final List<SpreadsheetParserToken> or) {
        return replaceCellValueOr0(
                old,
                conditionRight.orElse(null),
                or
        );
    }

    private static ParserToken replaceCellValueOr0(final ParserToken old,
                                                   final SpreadsheetConditionRightParserToken conditionRight,
                                                   final List<SpreadsheetParserToken> or) {
        ParserToken token = old;
        if (null == old) {
            if(null != conditionRight) {
                token = cellValue(
                        conditionRight
                );
            }
        } else {
            // try replace any previous cellXXX() conditionRight
            token = old.replaceIf(
                    SpreadsheetFindDialogComponentQueryConditionCellValueFunctionParserTokenPredicate.INSTANCE, // predicate
                    (e) -> cellValue(
                            conditionRight
                    ) // mapper
            );

            // if replace DID NOT happened then create OR expression(old, cellValue(component.value, cellXXX)
            if (old.equals(token) && null != conditionRight) {
                // append ors at the end to create expressions like
                //
                // or(1, or(2,3))
                //
                // and NOT
                //
                // or(or(1,or(2,3))
                or.add(
                        cellValue(
                                conditionRight
                        )
                );
            }
        }

        return token;
    }

    /**
     * Factory that creates an {@link SpreadsheetParserToken} with the given condition as the right half.
     * <pre>
     * cellValue() < 10
     * </pre>
     */
    private static SpreadsheetConditionParserToken cellValue(final SpreadsheetConditionRightParserToken conditionRight) {
        return conditionRight.setConditionLeft(
                CELL_VALUE_FUNCTION
        );
    }
    
    private final static SpreadsheetParserToken CELL_VALUE_FUNCTION_NAME = functionName(
            SpreadsheetExpressionFunctions.CELL_VALUE
    );

    private static SpreadsheetParserToken functionName(final ExpressionFunctionName name) {
        final String text = name.text();

        return SpreadsheetParserToken.functionName(
                SpreadsheetFunctionName.with(text),
                text
        );
    }

    private final static SpreadsheetParserToken DOUBLE_QUOTE_SYMBOL = SpreadsheetParserToken.doubleQuoteSymbol("\"", "\"");

    private final static SpreadsheetParserToken EMPTY_PARAMETER_LIST = SpreadsheetParserToken.functionParameters(
            Lists.of(
                    SpreadsheetParserToken.parenthesisOpenSymbol("(", "("),
                    SpreadsheetParserToken.parenthesisCloseSymbol(")", ")")
            ),
            "()"
    );

    private final static SpreadsheetFunctionParserToken CELL_VALUE_FUNCTION = SpreadsheetParserToken.namedFunction(
            Lists.of(
                    CELL_VALUE_FUNCTION_NAME,
                    EMPTY_PARAMETER_LIST
            ),
            CELL_VALUE_FUNCTION_NAME + EMPTY_PARAMETER_LIST.text()
    );

    // old
    //
    // 1, 2
    // or(old,or(1, 2))
    //
    // 1, 2, 3
    // or(old, or(1, or(2,3)))
    private static ParserToken or(final ParserToken token,
                                  final List<SpreadsheetParserToken> ors) {
        ParserToken out = token;

        if (false == ors.isEmpty()) {
            out = or(
                    token,
                    or(
                            ors.remove(0),
                            ors
                    )
            );
        }

        return out;
    }

    /**
     * Creates a call to the OR function with the two given {@link ParserToken}
     */
    private static ParserToken or(final ParserToken left,
                                  final ParserToken right) {
        return SpreadsheetParserToken.namedFunction(
                Lists.of(
                        OR_FUNCTION_NAME,
                        SpreadsheetParserToken.functionParameters(
                                Lists.of(
                                        left,
                                        COMMA,
                                        right
                                ),
                                left + "," + right
                        )
                ),
                "OR(" + left + "," + right + ")"
        );
    }

    private final static SpreadsheetParserToken OR_FUNCTION_NAME = SpreadsheetParserToken.functionName(
            SpreadsheetFunctionName.with("OR"),
            "OR"
    );

    private final static SpreadsheetParserToken COMMA = SpreadsheetParserToken.valueSeparatorSymbol(",", ",");

    /**
     * Stop creation
     */
    private SpreadsheetFindDialogComponentQuery() {
        throw new UnsupportedOperationException();
    }
}
