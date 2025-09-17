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
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterAlias;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterAliasSet;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterInfo;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterInfoSet;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterName;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

public final class AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetExporterAliasSetExportersTest implements PluginAliasSetLikeDialogComponentContextTesting<AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetExporterAliasSetExporters,
    SpreadsheetExporterName,
    SpreadsheetExporterInfo,
    SpreadsheetExporterInfoSet,
    SpreadsheetExporterSelector,
    SpreadsheetExporterAlias,
    SpreadsheetExporterAliasSet> {

    // isMatch..........................................................................................................

    @Test
    public void testIsMatchWithSpreadsheetMetadataPropertySelectHistoryTokenConverters() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.CONVERTERS
            ),
            false
        );
    }

    @Override
    public AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetExporterAliasSetExporters createContext() {
        return AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetExporterAliasSetExporters.with(AppContexts.fake());
    }

    @Override
    public SpreadsheetMetadataPropertyName<SpreadsheetExporterAliasSet> metadataPropertyName() {
        return SpreadsheetMetadataPropertyName.EXPORTERS;
    }

    // Class............................................................................................................

    @Override
    public Class<AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetExporterAliasSetExporters> type() {
        return AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetExporterAliasSetExporters.class;
    }
}
