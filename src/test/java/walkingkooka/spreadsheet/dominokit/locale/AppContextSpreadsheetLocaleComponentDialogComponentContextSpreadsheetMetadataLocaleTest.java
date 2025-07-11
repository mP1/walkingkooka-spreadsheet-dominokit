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

package walkingkooka.spreadsheet.dominokit.locale;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AppContextSpreadsheetLocaleComponentDialogComponentContextSpreadsheetMetadataLocaleTest extends AppContextSpreadsheetLocaleComponentDialogComponentContextTestCase<AppContextSpreadsheetLocaleComponentDialogComponentContextSpreadsheetMetadataLocale> {

    @Test
    public void testWithNullAppContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> AppContextSpreadsheetLocaleComponentDialogComponentContextSpreadsheetMetadataLocale.with(null)
        );
    }

    // DialogTitle......................................................................................................

    @Test
    public void testDialogTitle() {
        this.dialogTitleAndCheck(
            this.createContext(),
            "Spreadsheet Locale"
        );
    }

    // IsMatch..........................................................................................................

    @Test
    public void testisMatchWithSpreadsheetMetadataSelectWithLocale() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.LOCALE
            ),
            true
        );
    }

    @Test
    public void testisMatchWithSpreadsheetMetadataSaveWithLocale() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.LOCALE,
                Optional.of(Locale.ENGLISH)
            ),
            false
        );
    }

    @Test
    public void testisMatchWithSpreadsheetMetadataSelectWithFormulaConverter() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.FORMULA_CONVERTER
            ),
            false
        );
    }

    @Test
    public void testisMatchWithCellLocaleSelect() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.cellLocaleSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            false
        );
    }

    @Override
    public AppContextSpreadsheetLocaleComponentDialogComponentContextSpreadsheetMetadataLocale createContext() {
        return AppContextSpreadsheetLocaleComponentDialogComponentContextSpreadsheetMetadataLocale.with(
            new FakeAppContext()
        );
    }

    @Override
    public Class<AppContextSpreadsheetLocaleComponentDialogComponentContextSpreadsheetMetadataLocale> type() {
        return AppContextSpreadsheetLocaleComponentDialogComponentContextSpreadsheetMetadataLocale.class;
    }
}
