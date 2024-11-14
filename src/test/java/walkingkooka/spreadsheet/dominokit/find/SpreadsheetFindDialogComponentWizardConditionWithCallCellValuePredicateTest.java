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
import walkingkooka.predicate.PredicateTesting2;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.engine.SpreadsheetCellQuery;
import walkingkooka.tree.expression.Expression;

public final class SpreadsheetFindDialogComponentWizardConditionWithCallCellValuePredicateTest implements PredicateTesting2<SpreadsheetFindDialogComponentWizardConditionWithCallCellValuePredicate, Expression>,
        ToStringTesting<SpreadsheetFindDialogComponentWizardConditionWithCallCellValuePredicate>,
        ClassTesting<SpreadsheetFindDialogComponentWizardConditionWithCallCellValuePredicate> {

    @Test
    public void testTestStringLiteral() {
        this.testFalse(
                Expression.value("string-literal-123")
        );
    }

    @Test
    public void testTestCallFunction() {
        this.testFalse(
                expression("dummy()")
        );
    }

    @Test
    public void testTestConditionLeftNonCellValueFunction() {
        this.testFalse(
                expression("dummy() < 1")
        );
    }

    @Test
    public void testTestConditionRightNonCellValueFunction() {
        this.testFalse(
                expression("1 < dummy()")
        );
    }

    @Test
    public void testTestConditionLeftCellValue() {
        this.testTrue(
                expression("cellValue() < 1")
        );
    }

    @Test
    public void testTestConditionLeftCellValueFunctionNameUpperCased() {
        this.testTrue(
                expression("CELLVALUE() < 1")
        );
    }

    @Test
    public void testTestConditionRightCellValue() {
        this.testTrue(
                expression("1 > cellValue()")
        );
    }

    @Test
    public void testTestConditionRightCellValueFunctionNameUpperCased() {
        this.testTrue(
                expression("1 > CELLVALUE()")
        );
    }

    private Expression expression(final String text) {
        return SpreadsheetCellQuery.parse(text)
                .expression();
    }

    @Override
    public SpreadsheetFindDialogComponentWizardConditionWithCallCellValuePredicate createPredicate() {
        return SpreadsheetFindDialogComponentWizardConditionWithCallCellValuePredicate.INSTANCE;
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                SpreadsheetFindDialogComponentWizardConditionWithCallCellValuePredicate.INSTANCE,
                "cellValue() <"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetFindDialogComponentWizardConditionWithCallCellValuePredicate> type() {
        return SpreadsheetFindDialogComponentWizardConditionWithCallCellValuePredicate.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
