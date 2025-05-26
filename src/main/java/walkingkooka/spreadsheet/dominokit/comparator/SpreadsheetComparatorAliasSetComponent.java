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

package walkingkooka.spreadsheet.dominokit.comparator;

import walkingkooka.spreadsheet.compare.SpreadsheetComparatorAliasSet;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBox;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBoxWrapper;

public final class SpreadsheetComparatorAliasSetComponent implements ValueSpreadsheetTextBoxWrapper<SpreadsheetComparatorAliasSetComponent, SpreadsheetComparatorAliasSet> {

    public static SpreadsheetComparatorAliasSetComponent empty() {
        return new SpreadsheetComparatorAliasSetComponent();
    }

    private SpreadsheetComparatorAliasSetComponent() {
        this.textBox = ValueSpreadsheetTextBox.with(
            SpreadsheetComparatorAliasSet::parse,
            SpreadsheetComparatorAliasSet::text
        );
    }

    // ValueSpreadsheetTextBoxWrapper..................................................................................

    @Override
    public ValueSpreadsheetTextBox<SpreadsheetComparatorAliasSet> valueSpreadsheetTextBox() {
        return this.textBox;
    }

    private final ValueSpreadsheetTextBox<SpreadsheetComparatorAliasSet> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}