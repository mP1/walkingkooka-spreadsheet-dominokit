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

package walkingkooka.spreadsheet.dominokit.currency;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Currency;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AppContextCurrencyDialogComponentContextCellCurrencyTest extends AppContextCurrencyDialogComponentContextTestCase<AppContextCurrencyDialogComponentContextCellCurrency> {

    @Test
    public void testWithNullAppContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> AppContextCurrencyDialogComponentContextCellCurrency.with(null)
        );
    }

    // DialogTitle......................................................................................................

    @Test
    public void testDialogTitle() {
        this.dialogTitleAndCheck(
            this.createContext(),
            "A1: Currency"
        );
    }

    // IsMatch..........................................................................................................

    @Test
    public void testisMatchWithSpreadsheetMetadataSelectWithCurrency() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.CURRENCY
            ),
            false
        );
    }

    @Test
    public void testisMatchWithCellCurrencySave() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.cellCurrencySave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Optional.of(
                    Currency.getInstance("AUD")
                )
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

    // /1/SpreadsheetName/cell/currency
    @Test
    public void testisMatchWithCellCurrencySelect() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.cellCurrencySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            true
        );
    }

    @Override
    public AppContextCurrencyDialogComponentContextCellCurrency createContext() {
        return AppContextCurrencyDialogComponentContextCellCurrency.with(
            new FakeAppContext() {
                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.cellCurrencySelect(
                        AppContextCurrencyDialogComponentContextCellCurrencyTest.SPREADSHEET_ID,
                        SPREADSHEET_NAME,
                        SpreadsheetSelection.A1.setDefaultAnchor()
                    );
                }
            }
        );
    }

    @Override
    public Class<AppContextCurrencyDialogComponentContextCellCurrency> type() {
        return AppContextCurrencyDialogComponentContextCellCurrency.class;
    }
}
