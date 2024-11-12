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

import walkingkooka.collect.set.Sets;
import walkingkooka.spreadsheet.expression.function.SpreadsheetExpressionFunctions;
import walkingkooka.tree.expression.CallExpression;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionFunctionName;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A predicate that matches a testMatch function that includes a cell component getter function such as
 * <pre>
 * textMatch( textBoxValue, callFormula())
 * textMatch( textBoxValue, callFormatter())
 * textMatch( textBoxValue, callParser())
 * textMatch( textBoxValue, callStyle())
 * textMatch( textBoxValue, callFormattedValue())     
 * </pre>
 */
final class SpreadsheetFindDialogComponentCallExpressionPredicate implements Predicate<Expression> {
    @Override
    public boolean test(final Expression expression) {
        boolean test = false;

        if(expression instanceof CallExpression) {
            final CallExpression callExpression = (CallExpression) expression;

            // textMatch( textBoxValue, callFormula())
            // textMatch( textBoxValue, callFormatter())
            // textMatch( textBoxValue, callParser())
            // textMatch( textBoxValue, callStyle())
            // textMatch( textBoxValue, callFormattedValue())
            if(SpreadsheetExpressionFunctions.TEXT_MATCH.equals(callExpression.name())) {
                final List<Expression> parameters = callExpression.value();
                switch(parameters.size()) {
                    case 2:
                        test = isCellGetterFunction(parameters.get(0)) ||
                                isCellGetterFunction(parameters.get(1));
                        break;
                    default:
                        break;
                }
            }
        }

        return test;
    }

    private boolean isCellGetterFunction(final Expression expression) {
        boolean test = false;

        if(expression instanceof CallExpression) {
            final CallExpression callExpression = (CallExpression) expression;
            final ExpressionFunctionName functionName = callExpression.name();

            // callFormula()
            // callFormatter()
            // callParser()
            // callStyle()
            // callFormattedValue()
            if(GETTER_FUNCTIONS.contains(functionName)) {
                final List<Expression> parameters = callExpression.value();
                switch(parameters.size()) {
                    case 0:
                        test = true;
                        break;
                    default:
                        break;
                }
            }
        }

        return test;
    }

    private final static Set<ExpressionFunctionName> GETTER_FUNCTIONS = Sets.of(
            SpreadsheetExpressionFunctions.CELL_FORMULA,
            SpreadsheetExpressionFunctions.CELL_FORMATTER,
            SpreadsheetExpressionFunctions.CELL_PARSER,
            SpreadsheetExpressionFunctions.CELL_STYLE,
            SpreadsheetExpressionFunctions.CELL_FORMATTED_VALUE
    );
}
