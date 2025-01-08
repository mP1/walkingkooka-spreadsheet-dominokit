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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.parser.SpreadsheetParserToken;
import walkingkooka.spreadsheet.parser.SpreadsheetParsers;
import walkingkooka.text.cursor.parser.ParserToken;

public final class SpreadsheetFindDialogComponentQueryConditionCellValueFunctionParserTokenPredicateTest implements PredicateTesting2<SpreadsheetFindDialogComponentQueryConditionCellValueFunctionParserTokenPredicate, ParserToken>,
    ToStringTesting<SpreadsheetFindDialogComponentQueryConditionCellValueFunctionParserTokenPredicate>,
    ClassTesting<SpreadsheetFindDialogComponentQueryConditionCellValueFunctionParserTokenPredicate>,
    SpreadsheetMetadataTesting {

    @Override
    public void testTestNullFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testTestNonFunctionName() {
        this.testFalse(
            token("\"string-literal-123\"")
        );
    }

    @Test
    public void testTestFunctionName() {
        this.testFalse(
            token("dummy()")
        );
    }

    @Test
    public void testTestFunctionNameCellValueMissingCondition() {
        this.testFalse(
            token("cellValue()")
        );
    }

    @Test
    public void testTestFunctionNameCellValueWithLessThanConditionRight() {
        this.testTrue(
            token("cellValue() < 1")
        );
    }

    @Test
    public void testTestFunctionNameCellValueWithLessThanEqualsConditionRight() {
        this.testTrue(
            token("cellValue() <= 1")
        );
    }

    @Test
    public void testTestFunctionNameCellValueWithGreaterThanConditionRight() {
        this.testTrue(
            token("cellValue() > 1")
        );
    }

    @Test
    public void testTestFunctionNameCellValueWithGreaterThanEqualsConditionRight() {
        this.testTrue(
            token("cellValue() >= 1")
        );
    }

    @Test
    public void testTestFunctionNameCellValueWithEqualsConditionRight() {
        this.testTrue(
            token("cellValue() = 1")
        );
    }

    @Test
    public void testTestFunctionNameCellValueWithNotEqualsConditionRight() {
        this.testTrue(
            token("cellValue() <> 1")
        );
    }

    @Test
    public void testTestFunctionNameCellValueWithExpressionRight() {
        this.testTrue(
            token("cellValue() < 1+2+dummy()")
        );
    }

    @Test
    public void testTestFunctionNameCellValueWithConditionRightDifferentCase() {
        this.testTrue(
            token("CELLVALUE() < 1")
        );
    }

    @Test
    public void testTestFunctionNameCellValueExtraParameter() {
        this.testFalse(
            token("cellValue(1) < 2")
        );
    }

    // cellValue on the right not supported.
    @Test
    public void testTestFunctionNameCellValueWithNotEqualsConditionLeft() {
        this.testFalse(
            token("1 <> cellValue()")
        );
    }

    private SpreadsheetParserToken token(final String text) {
        return SpreadsheetParsers.expression()
            .parseText(
                text,
                SPREADSHEET_PARSER_CONTEXT
            ).cast(SpreadsheetParserToken.class);
    }


    @Override
    public SpreadsheetFindDialogComponentQueryConditionCellValueFunctionParserTokenPredicate createPredicate() {
        return SpreadsheetFindDialogComponentQueryConditionCellValueFunctionParserTokenPredicate.INSTANCE;
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            SpreadsheetFindDialogComponentQueryConditionCellValueFunctionParserTokenPredicate.INSTANCE,
            "cellValue()"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetFindDialogComponentQueryConditionCellValueFunctionParserTokenPredicate> type() {
        return SpreadsheetFindDialogComponentQueryConditionCellValueFunctionParserTokenPredicate.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
