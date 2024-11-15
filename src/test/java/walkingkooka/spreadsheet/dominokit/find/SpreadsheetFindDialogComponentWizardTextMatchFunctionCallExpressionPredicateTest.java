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

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.predicate.PredicateTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.engine.SpreadsheetCellQuery;
import walkingkooka.spreadsheet.expression.function.SpreadsheetExpressionFunctions;
import walkingkooka.tree.expression.Expression;

public final class SpreadsheetFindDialogComponentWizardTextMatchFunctionCallExpressionPredicateTest implements PredicateTesting,
        ToStringTesting<SpreadsheetFindDialogComponentWizardTextMatchFunctionCallExpressionPredicate>,
        ClassTesting<SpreadsheetFindDialogComponentWizardTextMatchFunctionCallExpressionPredicate> {

    @Test
    public void testTestNonCallExpression() {
        this.testFalse(
                SpreadsheetFindDialogComponentWizardTextMatchFunctionCallExpressionPredicate.with(SpreadsheetExpressionFunctions.CELL_FORMULA),
                Expression.value("string-literal-123")
        );
    }

    @Test
    public void testTestCallExpression() {
        this.testFalse(
                SpreadsheetFindDialogComponentWizardTextMatchFunctionCallExpressionPredicate.with(SpreadsheetExpressionFunctions.CELL_FORMULA),
                expression("dummy()")
        );
    }

    @Test
    public void testTestCallExpressionTextMatchMissingCellGetter() {
        this.testFalse(
                SpreadsheetFindDialogComponentWizardTextMatchFunctionCallExpressionPredicate.with(SpreadsheetExpressionFunctions.CELL_FORMULA),
                expression("textMatch(\"hello\")")
        );
    }

    @Test
    public void testTestCallExpressionTextMatchDifferentCellGetter() {
        this.testFalse(
                SpreadsheetFindDialogComponentWizardTextMatchFunctionCallExpressionPredicate.with(SpreadsheetExpressionFunctions.CELL_FORMULA),
                expression("textMatch(\"hello\", cellFormatter())")
        );
    }

    @Test
    public void testTestCallExpressionTextMatchCellGetter() {
        this.testTrue(
                SpreadsheetFindDialogComponentWizardTextMatchFunctionCallExpressionPredicate.with(SpreadsheetExpressionFunctions.CELL_FORMULA),
                expression("textMatch(\"hello\", cellFormula())")
        );
    }

    @Test
    public void testTestCallExpressionTextMatchCellGetterFunctionNameDifferentCase1() {
        this.testTrue(
                SpreadsheetFindDialogComponentWizardTextMatchFunctionCallExpressionPredicate.with(SpreadsheetExpressionFunctions.CELL_FORMULA),
                expression("TEXTMATCH(\"hello\", cellFormula())")
        );
    }

    @Test
    public void testTestCallExpressionTextMatchCellGetterFunctionNameDifferentCase2() {
        this.testTrue(
                SpreadsheetFindDialogComponentWizardTextMatchFunctionCallExpressionPredicate.with(SpreadsheetExpressionFunctions.CELL_FORMULA),
                expression("textMatch(\"hello\", CELLFORMULA())")
        );
    }

    private Expression expression(final String text) {
        return SpreadsheetCellQuery.parse(text)
                .expression();
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                SpreadsheetFindDialogComponentWizardTextMatchFunctionCallExpressionPredicate.with(SpreadsheetExpressionFunctions.CELL_FORMULA),
                "textMatch(\"\", cellFormula())"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetFindDialogComponentWizardTextMatchFunctionCallExpressionPredicate> type() {
        return SpreadsheetFindDialogComponentWizardTextMatchFunctionCallExpressionPredicate.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
