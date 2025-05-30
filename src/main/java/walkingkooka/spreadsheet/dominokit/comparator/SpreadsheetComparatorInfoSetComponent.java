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

import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfoSet;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBox;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBoxWrapper;

public final class SpreadsheetComparatorInfoSetComponent implements ValueSpreadsheetTextBoxWrapper<SpreadsheetComparatorInfoSetComponent, SpreadsheetComparatorInfoSet> {

    public static SpreadsheetComparatorInfoSetComponent empty() {
        return new SpreadsheetComparatorInfoSetComponent();
    }

    private SpreadsheetComparatorInfoSetComponent() {
        this.textBox = ValueSpreadsheetTextBox.with(
            SpreadsheetComparatorInfoSet::parse,
            SpreadsheetComparatorInfoSet::text
        );
    }

    // ValueSpreadsheetTextBoxWrapper..................................................................................

    @Override
    public ValueSpreadsheetTextBox<SpreadsheetComparatorInfoSet> valueSpreadsheetTextBox() {
        return this.textBox;
    }

    private final ValueSpreadsheetTextBox<SpreadsheetComparatorInfoSet> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}