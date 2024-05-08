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

package walkingkooka.spreadsheet.dominokit.ui.formula;

import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.dominokit.ui.parsertextbox.ParserSpreadsheetTextBox;
import walkingkooka.spreadsheet.dominokit.ui.parsertextbox.ParserSpreadsheetTextBoxWrapper;
import walkingkooka.tree.expression.Expression;

import java.util.function.Function;

/**
 * A text box that accepts entry and validates it as a {@link Expression}.
 */
public final class SpreadsheetFormulaComponent implements ParserSpreadsheetTextBoxWrapper<SpreadsheetFormulaComponent, SpreadsheetFormula> {

    public static SpreadsheetFormulaComponent empty(final Function<String, SpreadsheetFormula> parser) {
        return new SpreadsheetFormulaComponent(parser);
    }

    private SpreadsheetFormulaComponent(final Function<String, SpreadsheetFormula> parser) {
        this.textBox = ParserSpreadsheetTextBox.with(parser)
                .setValidator(SpreadsheetFormulaComponentValidator.with(parser));
    }

    // ParserSpreadsheetTextBoxWrapper..................................................................................

    @Override
    public ParserSpreadsheetTextBox<SpreadsheetFormula> parserSpreadsheetTextBox() {
        return this.textBox;
    }

    private final ParserSpreadsheetTextBox<SpreadsheetFormula> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}
