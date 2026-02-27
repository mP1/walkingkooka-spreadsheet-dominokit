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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Currency;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AppContextCurrencyDialogComponentContextMetadataCurrencyTest extends AppContextCurrencyDialogComponentContextTestCase<AppContextCurrencyDialogComponentContextMetadataCurrency> {

    @Test
    public void testWithNullAppContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> AppContextCurrencyDialogComponentContextMetadataCurrency.with(null)
        );
    }

    // DialogTitle......................................................................................................

    @Test
    public void testDialogTitle() {
        this.dialogTitleAndCheck(
            this.createContext(),
            "Spreadsheet: Currency (currency)"
        );
    }

    // undoCurrency.......................................................................................................

    private final static Currency CURRENCY = Currency.getInstance("AUD");

    @Test
    public void testUndoCurrency() {
        this.undoCurrencyAndCheck(
            this.createContext(),
            CURRENCY
        );
    }

    // IsMatch..........................................................................................................

    @Test
    public void testIsMatchWithSpreadsheetMetadataSelectNotCurrency() {
        for (final SpreadsheetMetadataPropertyName<?> propertyName : SpreadsheetMetadataPropertyName.ALL) {
            if (SpreadsheetMetadataPropertyName.CURRENCY.equals(propertyName)) {
                continue;
            }

            this.isMatchAndCheck(
                this.createContext(),
                HistoryToken.metadataPropertySelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    propertyName
                ),
                false
            );
        }
    }

    @Test
    public void testIsMatchWithSpreadsheetMetadataSelectWithCurrency() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.CURRENCY
            ),
            true
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetMetadataSaveWithCurrency() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.CURRENCY,
                Optional.of(CURRENCY)
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetMetadataSelectWithFormulaConverter() {
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
    public void testIsMatchWithCellCurrencySelect() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.cellCurrencySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            false
        );
    }

    // shouldIgnore.....................................................................................................

    @Test
    public void testShouldIgnoreSpreadsheetMetadataPropertyNameWithNotCurrency() {
        for (final SpreadsheetMetadataPropertyName<?> propertyName : SpreadsheetMetadataPropertyName.ALL) {
            if (SpreadsheetMetadataPropertyName.CURRENCY.equals(propertyName)) {
                continue;
            }

            this.shouldIgnoreAndCheck(
                this.createContext(),
                HistoryToken.metadataPropertySelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    propertyName
                ),
                true
            );
        }
    }

    @Test
    public void testShouldIgnoreSpreadsheetMetadataPropertyNameWithCurrency() {
        this.shouldIgnoreAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.CURRENCY
            ),
            false
        );
    }

    @Override
    public AppContextCurrencyDialogComponentContextMetadataCurrency createContext() {
        return AppContextCurrencyDialogComponentContextMetadataCurrency.with(
            new FakeAppContext() {

                @Override
                public SpreadsheetMetadata spreadsheetMetadata() {
                    return SpreadsheetMetadata.EMPTY.set(
                        SpreadsheetMetadataPropertyName.CURRENCY,
                        AppContextCurrencyDialogComponentContextMetadataCurrencyTest.CURRENCY
                    );
                }
            }
        );
    }

    @Override
    public Class<AppContextCurrencyDialogComponentContextMetadataCurrency> type() {
        return AppContextCurrencyDialogComponentContextMetadataCurrency.class;
    }
}
