
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

package walkingkooka.spreadsheet.dominokit.ui.row;

import walkingkooka.spreadsheet.dominokit.ui.parsertextbox.ParserSpreadsheetTextBox;
import walkingkooka.spreadsheet.dominokit.ui.parsertextbox.ParserSpreadsheetTextBoxWrapper;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

/**
 * A text box that accepts entry and validates it as a {@link SpreadsheetRowReference}.
 */
public final class SpreadsheetRowReferenceComponent implements ParserSpreadsheetTextBoxWrapper<SpreadsheetRowReferenceComponent, SpreadsheetRowReference> {

    public static SpreadsheetRowReferenceComponent empty() {
        return new SpreadsheetRowReferenceComponent();
    }

    private SpreadsheetRowReferenceComponent() {
        this.textBox = ParserSpreadsheetTextBox.with(SpreadsheetSelection::parseRow);
    }

    // ParserSpreadsheetTextBoxWrapper..................................................................................

    @Override
    public ParserSpreadsheetTextBox<SpreadsheetRowReference> parserSpreadsheetTextBox() {
        return this.textBox;
    }

    private final ParserSpreadsheetTextBox<SpreadsheetRowReference> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}