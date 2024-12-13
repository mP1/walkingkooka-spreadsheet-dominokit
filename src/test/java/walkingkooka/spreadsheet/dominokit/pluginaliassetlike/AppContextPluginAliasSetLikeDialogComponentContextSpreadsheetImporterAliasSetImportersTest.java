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
import walkingkooka.spreadsheet.importer.SpreadsheetImporterAlias;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterAliasSet;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterInfo;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterInfoSet;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterName;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterSelector;

public final class AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetImporterAliasSetImportersTest implements PluginAliasSetLikeDialogComponentContextTesting<AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetImporterAliasSetImporters,
        SpreadsheetImporterName,
        SpreadsheetImporterInfo,
        SpreadsheetImporterInfoSet,
        SpreadsheetImporterSelector,
        SpreadsheetImporterAlias,
        SpreadsheetImporterAliasSet> {
    @Override
    public AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetImporterAliasSetImporters createContext() {
        return AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetImporterAliasSetImporters.with(AppContexts.fake());
    }

    // Context..........................................................................................................

    @Override
    public String typeNameSuffix() {
        return SpreadsheetImporterAliasSet.class.getSimpleName() + "Importers";
    }

    // Class............................................................................................................

    @Override
    public Class<AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetImporterAliasSetImporters> type() {
        return AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetImporterAliasSetImporters.class;
    }
}
