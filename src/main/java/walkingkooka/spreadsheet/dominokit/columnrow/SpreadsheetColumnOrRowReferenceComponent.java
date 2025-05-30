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

package walkingkooka.spreadsheet.dominokit.columnrow;

import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBox;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBoxWrapper;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReferenceOrRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.HasText;

/**
 * A text box that accepts entry of {@link walkingkooka.spreadsheet.reference.SpreadsheetColumnReference} or {@link walkingkooka.spreadsheet.reference.SpreadsheetRowReference}.
 * Entering other selection types like {@link walkingkooka.spreadsheet.reference.SpreadsheetColumnRangeReference},
 * {@link walkingkooka.spreadsheet.reference.SpreadsheetRowRangeReference} or {@link walkingkooka.spreadsheet.reference.SpreadsheetCellReference} will
 * fail.
 */
public final class SpreadsheetColumnOrRowReferenceComponent implements ValueSpreadsheetTextBoxWrapper<SpreadsheetColumnOrRowReferenceComponent, SpreadsheetColumnOrRowReferenceOrRange> {

    public static SpreadsheetColumnOrRowReferenceComponent empty() {
        return new SpreadsheetColumnOrRowReferenceComponent();
    }

    private SpreadsheetColumnOrRowReferenceComponent() {
        this.textBox = ValueSpreadsheetTextBox.with(
            SpreadsheetSelection::parseColumnOrRow,
            HasText::text
        );
    }

    // ValueSpreadsheetTextBoxWrapper..................................................................................

    @Override
    public ValueSpreadsheetTextBox<SpreadsheetColumnOrRowReferenceOrRange> valueSpreadsheetTextBox() {
        return this.textBox;
    }

    private final ValueSpreadsheetTextBox<SpreadsheetColumnOrRowReferenceOrRange> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}