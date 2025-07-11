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

package walkingkooka.spreadsheet.dominokit.pluginaliassetlike;

import org.junit.jupiter.api.Test;
import walkingkooka.convert.provider.ConverterAlias;
import walkingkooka.convert.provider.ConverterAliasSet;
import walkingkooka.convert.provider.ConverterInfo;
import walkingkooka.convert.provider.ConverterInfoSet;
import walkingkooka.convert.provider.ConverterName;
import walkingkooka.convert.provider.ConverterSelector;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

public final class AppContextPluginAliasSetLikeDialogComponentContextConverterAliasSetConvertersTest implements PluginAliasSetLikeDialogComponentContextTesting<AppContextPluginAliasSetLikeDialogComponentContextConverterAliasSetConverters,
    ConverterName,
    ConverterInfo,
    ConverterInfoSet,
    ConverterSelector,
    ConverterAlias,
    ConverterAliasSet> {

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
    public AppContextPluginAliasSetLikeDialogComponentContextConverterAliasSetConverters createContext() {
        return AppContextPluginAliasSetLikeDialogComponentContextConverterAliasSetConverters.with(AppContexts.fake());
    }

    @Override
    public SpreadsheetMetadataPropertyName<ConverterAliasSet> metadataPropertyName() {
        return SpreadsheetMetadataPropertyName.CONVERTERS;
    }

    // Class............................................................................................................

    @Override
    public Class<AppContextPluginAliasSetLikeDialogComponentContextConverterAliasSetConverters> type() {
        return AppContextPluginAliasSetLikeDialogComponentContextConverterAliasSetConverters.class;
    }
}
