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

package walkingkooka.spreadsheet.dominokit.viewport;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.util.FunctionTesting;

public final class SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunctionTest implements FunctionTesting<SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction, String, SpreadsheetFormula> {

    @Test
    public void testApplyValidExpression() {
        final String text = "=1+2";

        this.applyAndCheck(
            text,
            SpreadsheetFormula.EMPTY.setText(text)
        );
    }

    @Test
    public void testApplyInvalidExpression() {
        final String text = "=1+";

        this.applyAndCheck(
            text,
            SpreadsheetFormula.EMPTY.setText(text)
        );
    }

    @Override
    public SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction createFunction() {
        return SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction.empty();
    }

    // Class............................................................................................................

    @Override
    public Class<SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction> type() {
        return SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction.class;
    }
}
