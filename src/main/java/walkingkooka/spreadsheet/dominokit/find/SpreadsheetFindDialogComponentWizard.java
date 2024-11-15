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
import walkingkooka.spreadsheet.expression.function.SpreadsheetExpressionFunctions;
import walkingkooka.spreadsheet.expression.function.TextMatch;
import walkingkooka.spreadsheet.parser.SpreadsheetConditionRightParserToken;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionFunctionName;

import java.util.List;
import java.util.Optional;

/**
 * Helper that provides methods to update the FORM query from other wizard fields,
 * AND (TODO)
 * Update wizard fields from the query
 */
final class SpreadsheetFindDialogComponentWizard implements PublicStaticHelper {


    static Optional<SpreadsheetFormula> query(final Optional<SpreadsheetCellQuery> query,
                                              final Optional<TextMatch> formula,
                                              final Optional<TextMatch> formatter,
                                              final Optional<TextMatch> parser,
                                              final Optional<TextMatch> style,
                                              final Optional<SpreadsheetConditionRightParserToken> value,
                                              final Optional<TextMatch> formattedValue) {
        Expression expression = query.map(
                SpreadsheetCellQuery::expression
        ).orElse(null);

        final List<Expression> or = Lists.array();

        expression = replaceOrOrTextMatch(
                expression, // old
                formula,
                SpreadsheetExpressionFunctions.CELL_FORMULA,
                or
        );

        expression = replaceOrOrTextMatch(
                expression, // old
                formatter,
                SpreadsheetExpressionFunctions.CELL_FORMATTER,
                or
        );

        expression = replaceOrOrTextMatch(
                expression, // old
                parser,
                SpreadsheetExpressionFunctions.CELL_PARSER,
                or
        );

        expression = replaceOrOrTextMatch(
                expression, // old
                style,
                SpreadsheetExpressionFunctions.CELL_STYLE,
                or
        );

        expression = replaceOrOrTextMatch(
                expression, // old
                formattedValue,
                SpreadsheetExpressionFunctions.CELL_FORMATTED_VALUE,
                or
        );

        // append OR expressions
        expression = appendOrExpressions(
                expression,
                or
        );

        return Optional.ofNullable(
                null != expression ?
                        SpreadsheetFormula.EMPTY.setExpression(
                                Optional.ofNullable(expression)
                        ).setText(
                                null != expression ?
                                        expression.text() :
                                        ""
                        ) :
                        null
        );
    }

    /**
     * Attempts to replace an old textMatch call with a new or updates the expression with an OR.
     */
    private static Expression replaceOrOrTextMatch(final Expression old,
                                                   final Optional<TextMatch> textMatch,
                                                   final ExpressionFunctionName cellPropertyGetter,
                                                   final List<Expression> or) {
        return replaceOrOrTextMatch0(
                old,
                textMatch.orElse(null),
                cellPropertyGetter,
                or
        );
    }

    private static Expression replaceOrOrTextMatch0(final Expression old,
                                                    final TextMatch textMatch,
                                                    final ExpressionFunctionName cellPropertyGetter,
                                                    final List<Expression> or) {
        Expression expression = old;

        if (null == old) {
            // only overwrite $expression if textMatch is also not empty
            if (null != textMatch && textMatch.isNotEmpty()) {
                // expression = textMatch(component.value, cellXXX())
                expression = textMatch(
                        textMatch,
                        cellPropertyGetter
                );
            }
        } else {
            // try replace any previous textMatch(component.value, cellXXX())
            expression = old.replaceIf(
                    SpreadsheetFindDialogComponentWizardTextMatchFunctionCallPredicate.with(cellPropertyGetter), // predicate
                    (e) -> textMatch(
                            textMatch,
                            cellPropertyGetter
                    ) // mapper
            );

            // if replace DID NOT happened then create OR expression(old, textMatch(component.value, cellXXX)
            if (old.equals(expression) && null != textMatch) {
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

        return expression;
    }

    /**
     * Factory that creates an {@link Expression} holding a textMatch function call.
     * <pre>
     * textMatch(
     *   value,
     *   cellXXX()
     * )
     * </pre>
     */
    private static Expression textMatch(final TextMatch value,
                                        final ExpressionFunctionName cellPropertyGetter) {
        return Expression.call(
                TEXT_MATCH_FUNCTION,
                Lists.of(
                        Expression.value(
                                value.text()
                        ),
                        Expression.call(
                                Expression.namedFunction(cellPropertyGetter),
                                Expression.NO_CHILDREN
                        )
                )
        );
    }

    private final static Expression TEXT_MATCH_FUNCTION = Expression.namedFunction(SpreadsheetExpressionFunctions.TEXT_MATCH);

    // old
    //
    // 1, 2
    // or(old,or(1, 2))
    //
    // 1, 2, 3
    // or(old, or(1, or(2,3)))
    private static Expression appendOrExpressions(final Expression expression,
                                                  final List<Expression> orExpressions) {
        Expression out = expression;

        System.out.println("\n" + expression);

        if (false == orExpressions.isEmpty()) {
            out = or(
                    expression,
                    appendOrExpressions(
                            orExpressions.remove(0),
                            orExpressions
                    )
            );
        }

        System.out.println("\t\t" + out);


        return out;
    }

    /**
     * Creates a call to the OR function with the two given {@link Expression}
     */
    private static Expression or(final Expression left,
                                 final Expression right) {
        return Expression.call(
                OR_FUNCTION,
                Lists.of(
                        left,
                        right
                )
        );
    }

    private final static Expression OR_FUNCTION = Expression.namedFunction(ExpressionFunctionName.with("OR"));

    /**
     * Stop creation
     */
    private SpreadsheetFindDialogComponentWizard() {
        throw new UnsupportedOperationException();
    }
}
