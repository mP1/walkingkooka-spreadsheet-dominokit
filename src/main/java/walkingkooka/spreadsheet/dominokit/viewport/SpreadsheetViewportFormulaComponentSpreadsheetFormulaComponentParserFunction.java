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

import walkingkooka.spreadsheet.formula.SpreadsheetFormula;

import java.util.function.Function;

/**
 * A {@link Function} that accepts any text and always returns a {@link SpreadsheetFormula}. This function assumes the
 * parent/enclosing {@link walkingkooka.spreadsheet.dominokit.formula.SpreadsheetFormulaComponent} will call the server,
 * and eventually refresh errors.
 */
final class SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction implements Function<String, SpreadsheetFormula> {

    public static SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction empty() {
        return new SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction();
    }

    private SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction() {
        super();
    }

    @Override
    public SpreadsheetFormula apply(final String text) {
        return SpreadsheetFormula.EMPTY.setText(text); // text is not validated or syntax verified in anyway. This assumes server calls will eventually report errors.
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
