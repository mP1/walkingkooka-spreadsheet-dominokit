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
import walkingkooka.tree.expression.ExpressionVisitor;
import walkingkooka.visit.Visiting;

import java.util.List;

final class SpreadsheetFindDialogComponentTextMatchExpressionVisitor extends ExpressionVisitor {

    static CallExpression find(final CallExpression cellGetter,
                               final Expression root) {
        final SpreadsheetFindDialogComponentTextMatchExpressionVisitor visitor = new SpreadsheetFindDialogComponentTextMatchExpressionVisitor(cellGetter);
        visitor.accept(root);

        return visitor.testMatch;
    }

    SpreadsheetFindDialogComponentTextMatchExpressionVisitor(final CallExpression cellGetter) {
        this.cellGetter = cellGetter;
        this.testMatch = null;
    }

    // textMatch("pattern", cellFormula())

    @Override
    protected Visiting startVisit(final CallExpression expression) {
        Visiting visiting = Visiting.CONTINUE;

        if (expression.name().equals(SpreadsheetExpressionFunctions.TEXT_MATCH)) {
            CallExpression function = null;

            final List<Expression> parameters = expression.value();
            switch (parameters.size()) {
                case 2:

                    break;
                default:
                    function = null;
                    break;
            }
        }

        return visiting;
    }

    private final CallExpression cellGetter;

    CallExpression testMatch;
}
