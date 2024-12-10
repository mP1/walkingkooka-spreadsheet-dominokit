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

import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.export.SpreadsheetExporterAlias;
import walkingkooka.spreadsheet.export.SpreadsheetExporterAliasSet;
import walkingkooka.spreadsheet.export.SpreadsheetExporterInfo;
import walkingkooka.spreadsheet.export.SpreadsheetExporterInfoSet;
import walkingkooka.spreadsheet.export.SpreadsheetExporterName;
import walkingkooka.spreadsheet.export.SpreadsheetExporterSelector;

public final class PluginAliasSetLikeDialogComponentContextBasicSpreadsheetExporterAliasSetExportersTest implements PluginAliasSetLikeDialogComponentContextTesting<PluginAliasSetLikeDialogComponentContextBasicSpreadsheetExporterAliasSetExporters,
        SpreadsheetExporterName,
        SpreadsheetExporterInfo,
        SpreadsheetExporterInfoSet,
        SpreadsheetExporterSelector,
        SpreadsheetExporterAlias,
        SpreadsheetExporterAliasSet> {
    @Override
    public PluginAliasSetLikeDialogComponentContextBasicSpreadsheetExporterAliasSetExporters createContext() {
        return PluginAliasSetLikeDialogComponentContextBasicSpreadsheetExporterAliasSetExporters.with(AppContexts.fake());
    }

    // Context..........................................................................................................

    @Override
    public String typeNameSuffix() {
        return SpreadsheetExporterAliasSet.class.getSimpleName() + "Exporters";
    }

    // Class............................................................................................................

    @Override
    public Class<PluginAliasSetLikeDialogComponentContextBasicSpreadsheetExporterAliasSetExporters> type() {
        return PluginAliasSetLikeDialogComponentContextBasicSpreadsheetExporterAliasSetExporters.class;
    }
}
