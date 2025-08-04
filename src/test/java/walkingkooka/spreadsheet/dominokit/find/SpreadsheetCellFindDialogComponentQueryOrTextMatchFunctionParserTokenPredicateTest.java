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
import walkingkooka.spreadsheet.expression.function.SpreadsheetExpressionFunctions;
import walkingkooka.spreadsheet.formula.SpreadsheetFormulaParsers;
import walkingkooka.spreadsheet.formula.parser.SpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.text.cursor.parser.ParserToken;

public final class SpreadsheetCellFindDialogComponentQueryOrTextMatchFunctionParserTokenPredicateTest implements PredicateTesting2<SpreadsheetCellFindDialogComponentQueryOrTextMatchFunctionParserTokenPredicate, ParserToken>,
    ToStringTesting<SpreadsheetCellFindDialogComponentQueryOrTextMatchFunctionParserTokenPredicate>,
    ClassTesting<SpreadsheetCellFindDialogComponentQueryOrTextMatchFunctionParserTokenPredicate>,
    SpreadsheetMetadataTesting {

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
        this.testFalse(
            token("cellValue() < 1")
        );
    }

    @Test
    public void testTestOrTextMatchLeft() {
        this.testTrue(
            token("or(textMatch(\"*\",cellFormula()),1)")
        );
    }

    @Test
    public void testTestOrTextMatchLeft2() {
        this.testTrue(
            token("OR(TEXTMATCH(\"*\",CELLFORMULA()),1)")
        );
    }

    @Test
    public void testTestOrTextMatchLeftWrongFunction() {
        this.testFalse(
            token("OR(textMatch(\"*\",cellWrong()),hello())")
        );
    }

    @Test
    public void testTestOrTextMatchRight() {
        this.testFalse(
            token("OR(0,textMatch(\"*\",cellFormula()))")
        );
    }

    private SpreadsheetFormulaParserToken token(final String text) {
        return SpreadsheetFormulaParsers.expression()
            .parseText(
                text,
                SPREADSHEET_PARSER_CONTEXT
            ).cast(SpreadsheetFormulaParserToken.class);
    }


    @Override
    public SpreadsheetCellFindDialogComponentQueryOrTextMatchFunctionParserTokenPredicate createPredicate() {
        return SpreadsheetCellFindDialogComponentQueryOrTextMatchFunctionParserTokenPredicate.with(
            0,
            SpreadsheetExpressionFunctions.CELL_FORMULA
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToStringWithParameterIndex0() {
        this.toStringAndCheck(
            SpreadsheetCellFindDialogComponentQueryOrTextMatchFunctionParserTokenPredicate.with(
                0,
                SpreadsheetExpressionFunctions.CELL_FORMULA
            ),
            "or(textMatch(\"*\",cellXXX()),1)"
        );
    }

    @Test
    public void testToStringWithParameterIndex1() {
        this.toStringAndCheck(
            SpreadsheetCellFindDialogComponentQueryOrTextMatchFunctionParserTokenPredicate.with(
                1,
                SpreadsheetExpressionFunctions.CELL_FORMATTER
            ),
            "or(0,textMatch(\"*\",cellXXX()))"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetCellFindDialogComponentQueryOrTextMatchFunctionParserTokenPredicate> type() {
        return SpreadsheetCellFindDialogComponentQueryOrTextMatchFunctionParserTokenPredicate.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
