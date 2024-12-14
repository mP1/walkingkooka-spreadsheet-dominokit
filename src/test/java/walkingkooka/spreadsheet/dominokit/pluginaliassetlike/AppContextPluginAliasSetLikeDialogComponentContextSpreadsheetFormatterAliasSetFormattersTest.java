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
import walkingkooka.spreadsheet.format.SpreadsheetFormatterAlias;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterAliasSet;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterInfo;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterInfoSet;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;

public final class AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetFormatterAliasSetFormattersTest implements PluginAliasSetLikeDialogComponentContextTesting<AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetFormatterAliasSetFormatters,
        SpreadsheetFormatterName,
        SpreadsheetFormatterInfo,
        SpreadsheetFormatterInfoSet,
        SpreadsheetFormatterSelector,
        SpreadsheetFormatterAlias,
        SpreadsheetFormatterAliasSet> {
    @Override
    public AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetFormatterAliasSetFormatters createContext() {
        return AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetFormatterAliasSetFormatters.with(AppContexts.fake());
    }

    // Context..........................................................................................................

    @Override
    public String typeNameSuffix() {
        return SpreadsheetFormatterAliasSet.class.getSimpleName() + "Formatters";
    }

    // Class............................................................................................................

    @Override
    public Class<AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetFormatterAliasSetFormatters> type() {
        return AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetFormatterAliasSetFormatters.class;
    }
}