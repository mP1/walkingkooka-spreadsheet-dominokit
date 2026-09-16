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

package walkingkooka.spreadsheet.dominokit.value.plugin.currencyexchangerater;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AppContextCurrencyExchangeRaterSelectorDialogComponentContextMetadataTest implements CurrencyExchangeRaterSelectorDialogComponentContextTesting<AppContextCurrencyExchangeRaterSelectorDialogComponentContextMetadata> {

    @Test
    public void testWithNullAppContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> AppContextCurrencyExchangeRaterSelectorDialogComponentContextMetadata.with(null)
        );
    }

    // isMatch..........................................................................................................

    @Test
    public void testIsMatchWithSpreadsheetCellSelectHistoryToken() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetCellParserSelectHistoryToken() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.cellParserSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetMetadataPropertyNameSelectHistoryTokenFormattingConverter() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.FORMATTING_CONVERTER
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetMetadataPropertyNameSelectHistoryTokenFormattingCurrencyExchangeRater() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.FORMATTING_CURRENCY_EXCHANGE_RATER
            ),
            true
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetMetadataPropertyNameSelectHistoryTokenScriptingCurrencyExchangeRater() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.SCRIPTING_CURRENCY_EXCHANGE_RATER
            ),
            true
        );
    }

    @Override
    public AppContextCurrencyExchangeRaterSelectorDialogComponentContextMetadata createContext() {
        return AppContextCurrencyExchangeRaterSelectorDialogComponentContextMetadata.with(AppContexts.fake());
    }

    @Override
    public String typeNameSuffix() {
        return "Metadata";
    }

    // class............................................................................................................

    @Override
    public Class<AppContextCurrencyExchangeRaterSelectorDialogComponentContextMetadata> type() {
        return AppContextCurrencyExchangeRaterSelectorDialogComponentContextMetadata.class;
    }
}
