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

package walkingkooka.spreadsheet.dominokit.csv;

import walkingkooka.collect.list.CsvStringList;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBox;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBoxWrapper;

/**
 * A component that will be used to support entry text entry of lists of days, months etc as a CSV text.
 */
public final class CsvStringListComponent implements ValueSpreadsheetTextBoxWrapper<CsvStringListComponent, CsvStringList> {

    public static CsvStringListComponent empty() {
        return new CsvStringListComponent();
    }

    private CsvStringListComponent() {
        this.textBox = ValueSpreadsheetTextBox.with(
            CsvStringList::parse,
            CsvStringList::text
        );
    }

    // ValueSpreadsheetTextBoxWrapper..................................................................................

    @Override
    public ValueSpreadsheetTextBox<CsvStringList> parserSpreadsheetTextBox() {
        return this.textBox;
    }

    private final ValueSpreadsheetTextBox<CsvStringList> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}