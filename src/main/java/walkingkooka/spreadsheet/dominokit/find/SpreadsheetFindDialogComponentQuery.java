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
import walkingkooka.spreadsheet.engine.SpreadsheetCellQuery;
import walkingkooka.spreadsheet.expression.SpreadsheetFunctionName;
import walkingkooka.spreadsheet.expression.function.SpreadsheetExpressionFunctions;
import walkingkooka.spreadsheet.expression.function.TextMatch;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.formula.parser.ConditionRightSpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.formula.parser.ConditionSpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.formula.parser.FunctionSpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.formula.parser.NamedFunctionSpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.formula.parser.SpreadsheetFormulaParserToken;
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
                                              final Optional<TextMatch> dateTimeSymbols,
                                              final Optional<TextMatch> decimalNumberSymbols,
                                              final Optional<TextMatch> formatter,
                                              final Optional<TextMatch> parser,
                                              final Optional<TextMatch> style,
                                              final Optional<ConditionRightSpreadsheetFormulaParserToken> value,
                                              final Optional<TextMatch> validator,
                                              final Optional<TextMatch> formattedValue) {
        ParserToken token = query.map(
            SpreadsheetCellQuery::parserToken
        ).orElse(null);

        final List<SpreadsheetFormulaParserToken> or = Lists.array();

        token = replaceTextMatchOr(
            token, // old
            formula,
            SpreadsheetExpressionFunctions.CELL_FORMULA,
            or
        );

        token = replaceTextMatchOr(
            token, // old
            dateTimeSymbols,
            SpreadsheetExpressionFunctions.CELL_DATE_TIME_SYMBOLS,
            or
        );

        token = replaceTextMatchOr(
            token, // old
            decimalNumberSymbols,
            SpreadsheetExpressionFunctions.CELL_DECIMAL_NUMBER_SYMBOLS,
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
            validator,
            SpreadsheetExpressionFunctions.CELL_VALIDATOR,
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
                        token.cast(SpreadsheetFormulaParserToken.class)
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
                                                  final List<SpreadsheetFormulaParserToken> or) {
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
                                                   final List<SpreadsheetFormulaParserToken> or) {
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
            if (null != textMatch) {
                // EXACT replace try replace any previous textMatch(component.value, cellXXX())
                token = old.replaceIf(
                    SpreadsheetFindDialogComponentQueryTextMatchFunctionParserTokenPredicate.with(cellPropertyGetter), // predicate
                    (e) -> textMatch(
                        textMatch,
                        cellPropertyGetter
                    )  // mapper
                );
            } else {
                // three replaceIfs
                // first - find OR ( cellPropertyGetter, OTHER) -> OTHER
                token = old.replaceIf(
                    SpreadsheetFindDialogComponentQueryOrTextMatchFunctionParserTokenPredicate.with(
                        0,
                        cellPropertyGetter
                    ), // predicate
                    (e) -> e.cast(NamedFunctionSpreadsheetFormulaParserToken.class)
                        .parameters()
                        .parameters()
                        .get(1)
                );

                if (old.equals(token)) {
                    // second -> find OR ( OTHER, cellPropertyGetter) -> OTHER
                    token = old.replaceIf(
                        SpreadsheetFindDialogComponentQueryOrTextMatchFunctionParserTokenPredicate.with(
                            1,
                            cellPropertyGetter
                        ), // predicate
                        (e) -> e.cast(NamedFunctionSpreadsheetFormulaParserToken.class)
                            .parameters()
                            .parameters()
                            .get(0)
                    );

                    if (old.equals(token)) {
                        // third -> remove cellPropertyGetter
                        token = old.removeIf(
                            SpreadsheetFindDialogComponentQueryTextMatchFunctionParserTokenPredicate.with(cellPropertyGetter)
                        ).orElse(null);
                    }
                }
            }

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
     * Factory that creates an {@link SpreadsheetFormulaParserToken} holding a textMatch function call.
     * <pre>
     * textMatch(
     *   value,
     *   cellXXX()
     * )
     * </pre>
     */
    private static SpreadsheetFormulaParserToken textMatch(final TextMatch textMatch,
                                                           final ExpressionFunctionName cellPropertyGetter) {
        final String textMatchString = textMatch.text();
        final String quotedTextMatch = CharSequences.escape(
            textMatchString
        ).toString();

        final String cellPropertyGetterName = cellPropertyGetter.value();
        final String textMatchFunction = TEXT_MATCH_FUNCTION_NAME + "(\"" + quotedTextMatch + "\"," + cellPropertyGetterName + "())";

        return SpreadsheetFormulaParserToken.namedFunction(
            Lists.of(
                TEXT_MATCH_FUNCTION_NAME,
                SpreadsheetFormulaParserToken.functionParameters(
                    Lists.of(
                        SpreadsheetFormulaParserToken.text(
                            Lists.of(
                                DOUBLE_QUOTE_SYMBOL,
                                SpreadsheetFormulaParserToken.textLiteral(
                                    quotedTextMatch,
                                    textMatchString
                                ),
                                DOUBLE_QUOTE_SYMBOL
                            ),
                            "" + '"' + quotedTextMatch + '"'
                        ),
                        SpreadsheetFormulaParserToken.namedFunction(
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

    private final static SpreadsheetFormulaParserToken TEXT_MATCH_FUNCTION_NAME = functionName(
        SpreadsheetExpressionFunctions.TEXT_MATCH
    );

    /**
     * Attempts to replace an old cellValue call with a new or updates the expression with an OR.
     */
    private static ParserToken replaceCellValueOr(final ParserToken old,
                                                  final Optional<ConditionRightSpreadsheetFormulaParserToken> conditionRight,
                                                  final List<SpreadsheetFormulaParserToken> or) {
        return replaceCellValueOr0(
            old,
            conditionRight.orElse(null),
            or
        );
    }

    private static ParserToken replaceCellValueOr0(final ParserToken old,
                                                   final ConditionRightSpreadsheetFormulaParserToken conditionRight,
                                                   final List<SpreadsheetFormulaParserToken> or) {
        ParserToken token = old;
        if (null == old) {
            if (null != conditionRight) {
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
     * Factory that creates an {@link SpreadsheetFormulaParserToken} with the given condition as the right half.
     * <pre>
     * cellValue() < 10
     * </pre>
     */
    private static ConditionSpreadsheetFormulaParserToken cellValue(final ConditionRightSpreadsheetFormulaParserToken conditionRight) {
        return conditionRight.setConditionLeft(
            CELL_VALUE_FUNCTION
        );
    }

    private final static SpreadsheetFormulaParserToken CELL_VALUE_FUNCTION_NAME = functionName(
        SpreadsheetExpressionFunctions.CELL_VALUE
    );

    private static SpreadsheetFormulaParserToken functionName(final ExpressionFunctionName name) {
        final String text = name.text();

        return SpreadsheetFormulaParserToken.functionName(
            SpreadsheetFunctionName.with(text),
            text
        );
    }

    private final static SpreadsheetFormulaParserToken DOUBLE_QUOTE_SYMBOL = SpreadsheetFormulaParserToken.doubleQuoteSymbol("\"", "\"");

    private final static SpreadsheetFormulaParserToken EMPTY_PARAMETER_LIST = SpreadsheetFormulaParserToken.functionParameters(
        Lists.<ParserToken>of(
            SpreadsheetFormulaParserToken.parenthesisOpenSymbol("(", "("),
            SpreadsheetFormulaParserToken.parenthesisCloseSymbol(")", ")")
        ),
        "()"
    );

    private final static FunctionSpreadsheetFormulaParserToken CELL_VALUE_FUNCTION = SpreadsheetFormulaParserToken.namedFunction(
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
                                  final List<SpreadsheetFormulaParserToken> ors) {
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

    private final static String OR = SpreadsheetExpressionFunctions.OR.value();

    /**
     * Creates a call to the OR function with the two given {@link ParserToken}
     */
    private static ParserToken or(final ParserToken left,
                                  final ParserToken right) {
        return SpreadsheetFormulaParserToken.namedFunction(
            Lists.of(
                OR_FUNCTION_NAME,
                SpreadsheetFormulaParserToken.functionParameters(
                    Lists.of(
                        left,
                        COMMA,
                        right
                    ),
                    left + "," + right
                )
            ),
            OR + "(" + left + "," + right + ")"
        );
    }

    private final static SpreadsheetFormulaParserToken OR_FUNCTION_NAME = SpreadsheetFormulaParserToken.functionName(
        SpreadsheetFunctionName.with(OR),
        OR
    );

    private final static SpreadsheetFormulaParserToken COMMA = SpreadsheetFormulaParserToken.valueSeparatorSymbol(
        ",",
        ","
    );

    /**
     * Stop creation
     */
    private SpreadsheetFindDialogComponentQuery() {
        throw new UnsupportedOperationException();
    }
}
