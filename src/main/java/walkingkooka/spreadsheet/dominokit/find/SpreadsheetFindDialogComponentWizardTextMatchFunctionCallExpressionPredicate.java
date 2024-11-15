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
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.NamedFunctionExpression;

import java.util.List;
import java.util.function.Predicate;

/**
 * A {@link Predicate} that matches a textMatch function call that also contains a cellXXX getter function.
 */
final class SpreadsheetFindDialogComponentWizardTextMatchFunctionCallExpressionPredicate implements Predicate<Expression> {

    static SpreadsheetFindDialogComponentWizardTextMatchFunctionCallExpressionPredicate with(final ExpressionFunctionName getter) {
        return new SpreadsheetFindDialogComponentWizardTextMatchFunctionCallExpressionPredicate(getter);
    }

    private SpreadsheetFindDialogComponentWizardTextMatchFunctionCallExpressionPredicate(final ExpressionFunctionName getter) {
        this.getter = getter;
    }

    @Override
    public boolean test(final Expression expression) {
        boolean test = false;

        if (expression.isCall()) {
            final CallExpression callExpression = (CallExpression) expression;

            // textMatch( textBoxValue, callFormula())
            // textMatch( textBoxValue, callFormatter())
            // textMatch( textBoxValue, callParser())
            // textMatch( textBoxValue, callStyle())
            // textMatch( textBoxValue, callFormattedValue())
            final Expression callable = callExpression.callable();
            if(callable.isNamedFunction()) {
                final NamedFunctionExpression namedFunctionExpression = (NamedFunctionExpression) callable;

                if (SpreadsheetExpressionFunctions.TEXT_MATCH.equals(namedFunctionExpression.value())) {
                    final List<Expression> parameters = callExpression.value();
                    switch (parameters.size()) {
                        case 2:
                            test = isCellGetterFunction(parameters.get(0)) ||
                                    isCellGetterFunction(parameters.get(1));
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        return test;
    }

    private boolean isCellGetterFunction(final Expression expression) {
        boolean test = false;

        if (expression.isCall()) {
            final CallExpression callExpression = (CallExpression) expression;

            final Expression callable = callExpression.callable();
            if(callable.isNamedFunction()) {
                final NamedFunctionExpression namedFunctionExpression = (NamedFunctionExpression) callable;

                final ExpressionFunctionName functionName = namedFunctionExpression.value();

                // callFormula()
                // callFormatter()
                // callParser()
                // callStyle()
                // callFormattedValue()
                if (this.getter.equals(functionName)) {
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

    private final ExpressionFunctionName getter;

    @Override
    public String toString() {
        return "textMatch(\"\", " + this.getter + "())";
    }
}
