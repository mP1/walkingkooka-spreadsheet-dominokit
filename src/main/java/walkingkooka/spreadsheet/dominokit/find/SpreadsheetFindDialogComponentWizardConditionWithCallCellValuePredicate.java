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

import walkingkooka.spreadsheet.expression.function.SpreadsheetExpressionFunctions;
import walkingkooka.tree.expression.CallExpression;
import walkingkooka.tree.expression.CompareExpression;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.NamedFunctionExpression;

import java.util.List;
import java.util.function.Predicate;

/**
 * A {@link Predicate} that tests if a comparison is a condition involving the function cellValue()
 */
final class SpreadsheetFindDialogComponentWizardConditionWithCallCellValuePredicate implements Predicate<Expression> {

    /**
     * Singleton
     */
    final static SpreadsheetFindDialogComponentWizardConditionWithCallCellValuePredicate INSTANCE = new SpreadsheetFindDialogComponentWizardConditionWithCallCellValuePredicate();

    private SpreadsheetFindDialogComponentWizardConditionWithCallCellValuePredicate() {
    }

    @Override
    public boolean test(final Expression expression) {
        return expression.isCompare() &&
                this.isCompareWithCellValue((CompareExpression) expression);
    }

    // valueGetter() < x
    // x > valueGetter()
    private boolean isCompareWithCellValue(final CompareExpression expression) {
        return this.isCellValueFunction(
                expression.left()
        ) ||
                this.isCellValueFunction(
                        expression.right()
                );
    }

    private boolean isCellValueFunction(final Expression expression) {
        boolean test = false;

        if (expression.isCall()) {
            final CallExpression callExpression = (CallExpression) expression;

            final Expression callable = callExpression.callable();
            if(callable.isNamedFunction()) {
                final NamedFunctionExpression namedFunctionExpression = (NamedFunctionExpression) callable;

                final ExpressionFunctionName functionName = namedFunctionExpression.value();

                // callValue()
                if (SpreadsheetExpressionFunctions.CELL_VALUE.equals(functionName)) {
                    final List<Expression> parameters = namedFunctionExpression.children();
                    switch (parameters.size()) {
                        case 0:
                            test = true;
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        return test;
    }

    @Override
    public String toString() {
        return "cellValue() <";
    }
}
