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
import walkingkooka.spreadsheet.format.SpreadsheetFormatterAliasSet;

public final class SpreadsheetFormatterAliasSetComponent implements ValueSpreadsheetTextBoxWrapper<SpreadsheetFormatterAliasSetComponent, SpreadsheetFormatterAliasSet> {

    public static SpreadsheetFormatterAliasSetComponent empty() {
        return new SpreadsheetFormatterAliasSetComponent();
    }

    private SpreadsheetFormatterAliasSetComponent() {
        this.textBox = ValueSpreadsheetTextBox.with(
            SpreadsheetFormatterAliasSet::parse,
            SpreadsheetFormatterAliasSet::text
        );
    }

    // ValueSpreadsheetTextBoxWrapper..................................................................................

    @Override
    public ValueSpreadsheetTextBox<SpreadsheetFormatterAliasSet> valueSpreadsheetTextBox() {
        return this.textBox;
    }

    private final ValueSpreadsheetTextBox<SpreadsheetFormatterAliasSet> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}