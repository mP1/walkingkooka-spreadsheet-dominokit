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

package walkingkooka.spreadsheet.dominokit.value.plugin.pluginaliassetlike;

import org.junit.jupiter.api.Test;
import walkingkooka.currency.provider.CurrencyExchangeRaterAlias;
import walkingkooka.currency.provider.CurrencyExchangeRaterAliasSet;
import walkingkooka.currency.provider.CurrencyExchangeRaterInfo;
import walkingkooka.currency.provider.CurrencyExchangeRaterInfoSet;
import walkingkooka.currency.provider.CurrencyExchangeRaterName;
import walkingkooka.currency.provider.CurrencyExchangeRaterSelector;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

public final class AppContextPluginAliasSetLikeDialogComponentContextCurrencyExchangeRaterAliasSetCurrencyExchangeRatersTest implements PluginAliasSetLikeDialogComponentContextTesting<AppContextPluginAliasSetLikeDialogComponentContextCurrencyExchangeRaterAliasSetCurrencyExchangeRaters,
    CurrencyExchangeRaterName,
    CurrencyExchangeRaterInfo,
    CurrencyExchangeRaterInfoSet,
    CurrencyExchangeRaterSelector,
    CurrencyExchangeRaterAlias,
    CurrencyExchangeRaterAliasSet> {

    // isMatch..........................................................................................................

    @Test
    public void testIsMatchWithSpreadsheetMetadataPropertySelectHistoryTokenFunctions() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.FUNCTIONS
            ),
            false
        );
    }

    @Override
    public AppContextPluginAliasSetLikeDialogComponentContextCurrencyExchangeRaterAliasSetCurrencyExchangeRaters createContext() {
        return AppContextPluginAliasSetLikeDialogComponentContextCurrencyExchangeRaterAliasSetCurrencyExchangeRaters.with(AppContexts.fake());
    }

    @Override
    public SpreadsheetMetadataPropertyName<CurrencyExchangeRaterAliasSet> metadataPropertyName() {
        return SpreadsheetMetadataPropertyName.CURRENCY_EXCHANGE_RATERS;
    }

    // Class............................................................................................................

    @Override
    public Class<AppContextPluginAliasSetLikeDialogComponentContextCurrencyExchangeRaterAliasSetCurrencyExchangeRaters> type() {
        return AppContextPluginAliasSetLikeDialogComponentContextCurrencyExchangeRaterAliasSetCurrencyExchangeRaters.class;
    }
}
