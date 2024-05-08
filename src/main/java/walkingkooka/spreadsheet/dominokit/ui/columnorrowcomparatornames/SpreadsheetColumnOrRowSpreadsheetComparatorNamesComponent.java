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

package walkingkooka.spreadsheet.dominokit.ui.columnorrowcomparatornames;

import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNames;
import walkingkooka.spreadsheet.dominokit.ui.parsertextbox.ParserSpreadsheetTextBox;
import walkingkooka.spreadsheet.dominokit.ui.parsertextbox.ParserSpreadsheetTextBoxWrapper;

/**
 * A text box that accepts entry and validates it as a {@link SpreadsheetColumnOrRowSpreadsheetComparatorNames}.
 */
public final class SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent implements ParserSpreadsheetTextBoxWrapper<SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent, SpreadsheetColumnOrRowSpreadsheetComparatorNames> {

    public static SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent empty() {
        return new SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent();
    }

    private SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent() {
        this.textBox = ParserSpreadsheetTextBox.with(SpreadsheetColumnOrRowSpreadsheetComparatorNames::parse);
    }

    // ParserSpreadsheetTextBoxWrapper..................................................................................

    @Override
    public ParserSpreadsheetTextBox<SpreadsheetColumnOrRowSpreadsheetComparatorNames> parserSpreadsheetTextBox() {
        return this.textBox;
    }

    private final ParserSpreadsheetTextBox<SpreadsheetColumnOrRowSpreadsheetComparatorNames> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}