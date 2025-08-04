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

package walkingkooka.spreadsheet.dominokit.formula;

import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBoxWrapper;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.text.HasText;

import java.util.function.Function;

/**
 * A text box that accepts entry and validates it as a syntactically correct expression with the
 * {@link SpreadsheetFormula#token()} being populated
 */
public final class SpreadsheetFormulaComponent implements ValueSpreadsheetTextBoxWrapper<SpreadsheetFormulaComponent, SpreadsheetFormula> {

    public static SpreadsheetFormulaComponent empty(final Function<String, SpreadsheetFormula> parser) {
        return new SpreadsheetFormulaComponent(parser);
    }

    private SpreadsheetFormulaComponent(final Function<String, SpreadsheetFormula> parser) {
        this.textBox = ValueTextBoxComponent.with(
            parser,
            HasText::text
        ).setValidator(SpreadsheetFormulaComponentValidator.with(parser));
    }

    // ValueSpreadsheetTextBoxWrapper..................................................................................

    @Override
    public ValueTextBoxComponent<SpreadsheetFormula> valueTextBoxComponent() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<SpreadsheetFormula> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}
