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

package walkingkooka.spreadsheet.dominokit.reference;

import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBox;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBoxWrapper;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.HasText;

/**
 * A text box that accepts entry and validates it as a {@link SpreadsheetColumnOrRowReference}.
 */
public final class SpreadsheetColumnOrRowReferenceComponent implements ValueSpreadsheetTextBoxWrapper<SpreadsheetColumnOrRowReferenceComponent, SpreadsheetColumnOrRowReference> {

    public static SpreadsheetColumnOrRowReferenceComponent empty() {
        return new SpreadsheetColumnOrRowReferenceComponent();
    }

    private SpreadsheetColumnOrRowReferenceComponent() {
        this.textBox = ValueSpreadsheetTextBox.with(
                SpreadsheetSelection::parseColumn,
                HasText::text
        );
    }

    // ValueSpreadsheetTextBoxWrapper..................................................................................

    @Override
    public ValueSpreadsheetTextBox<SpreadsheetColumnOrRowReference> parserSpreadsheetTextBox() {
        return this.textBox;
    }

    private final ValueSpreadsheetTextBox<SpreadsheetColumnOrRowReference> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}