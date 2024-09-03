
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

package walkingkooka.spreadsheet.dominokit.format;


import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBox;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBoxWrapper;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterName;

/**
 * A text box that accepts entry and validates it as a {@link SpreadsheetFormatterName}.
 */
public final class SpreadsheetFormatterNameComponent implements ValueSpreadsheetTextBoxWrapper<SpreadsheetFormatterNameComponent, SpreadsheetFormatterName> {

    public static SpreadsheetFormatterNameComponent empty() {
        return new SpreadsheetFormatterNameComponent();
    }

    private SpreadsheetFormatterNameComponent() {
        this.textBox = ValueSpreadsheetTextBox.with(
                SpreadsheetFormatterName::with,
                SpreadsheetFormatterName::toString
        );
    }

    // ValueSpreadsheetTextBoxWrapper..................................................................................

    @Override
    public ValueSpreadsheetTextBox<SpreadsheetFormatterName> parserSpreadsheetTextBox() {
        return this.textBox;
    }

    private final ValueSpreadsheetTextBox<SpreadsheetFormatterName> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}