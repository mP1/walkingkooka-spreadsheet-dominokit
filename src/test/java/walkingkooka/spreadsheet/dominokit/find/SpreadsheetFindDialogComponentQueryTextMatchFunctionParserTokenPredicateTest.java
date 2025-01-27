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
import walkingkooka.spreadsheet.expression.function.SpreadsheetExpressionFunctions;
import walkingkooka.spreadsheet.formula.SpreadsheetParserToken;
import walkingkooka.spreadsheet.formula.SpreadsheetParsers;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

public final class SpreadsheetFindDialogComponentQueryTextMatchFunctionParserTokenPredicateTest implements PredicateTesting,
    ToStringTesting<SpreadsheetFindDialogComponentQueryTextMatchFunctionParserTokenPredicate>,
    ClassTesting<SpreadsheetFindDialogComponentQueryTextMatchFunctionParserTokenPredicate>,
    SpreadsheetMetadataTesting {

    @Test
    public void testTestNonCallExpression() {
        this.testFalse(
            SpreadsheetFindDialogComponentQueryTextMatchFunctionParserTokenPredicate.with(SpreadsheetExpressionFunctions.CELL_FORMULA),
            token("string-literal-123")
        );
    }

    @Test
    public void testTestCallExpression() {
        this.testFalse(
            SpreadsheetFindDialogComponentQueryTextMatchFunctionParserTokenPredicate.with(SpreadsheetExpressionFunctions.CELL_FORMULA),
            token("dummy()")
        );
    }

    @Test
    public void testTestCallExpressionTextMatchMissingCellGetter() {
        this.testFalse(
            SpreadsheetFindDialogComponentQueryTextMatchFunctionParserTokenPredicate.with(SpreadsheetExpressionFunctions.CELL_FORMULA),
            token("textMatch(\"hello\")")
        );
    }

    @Test
    public void testTestCallExpressionTextMatchDifferentCellGetter() {
        this.testFalse(
            SpreadsheetFindDialogComponentQueryTextMatchFunctionParserTokenPredicate.with(SpreadsheetExpressionFunctions.CELL_FORMULA),
            token("textMatch(\"hello\", cellFormatter())")
        );
    }

    @Test
    public void testTestCallExpressionTextMatchCellGetter() {
        this.testTrue(
            SpreadsheetFindDialogComponentQueryTextMatchFunctionParserTokenPredicate.with(SpreadsheetExpressionFunctions.CELL_FORMULA),
            token("textMatch(\"hello\", cellFormula())")
        );
    }

    @Test
    public void testTestCallExpressionTextMatchCellGetterFunctionNameDifferentCase1() {
        this.testTrue(
            SpreadsheetFindDialogComponentQueryTextMatchFunctionParserTokenPredicate.with(SpreadsheetExpressionFunctions.CELL_FORMULA),
            token("TEXTMATCH(\"hello\", cellFormula())")
        );
    }

    @Test
    public void testTestCallExpressionTextMatchCellGetterFunctionNameDifferentCase2() {
        this.testTrue(
            SpreadsheetFindDialogComponentQueryTextMatchFunctionParserTokenPredicate.with(SpreadsheetExpressionFunctions.CELL_FORMULA),
            token("textMatch(\"hello\", CELLFORMULA())")
        );
    }

    private SpreadsheetParserToken token(final String text) {
        return SpreadsheetParsers.expression()
            .parseText(
                text,
                SPREADSHEET_PARSER_CONTEXT
            ).cast(SpreadsheetParserToken.class);
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            SpreadsheetFindDialogComponentQueryTextMatchFunctionParserTokenPredicate.with(SpreadsheetExpressionFunctions.CELL_FORMULA),
            "textMatch(\"\", cellFormula())"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetFindDialogComponentQueryTextMatchFunctionParserTokenPredicate> type() {
        return SpreadsheetFindDialogComponentQueryTextMatchFunctionParserTokenPredicate.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
