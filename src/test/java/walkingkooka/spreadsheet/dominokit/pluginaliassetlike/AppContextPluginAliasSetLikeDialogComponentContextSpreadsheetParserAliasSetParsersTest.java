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
import walkingkooka.spreadsheet.parser.SpreadsheetParserAlias;
import walkingkooka.spreadsheet.parser.SpreadsheetParserAliasSet;
import walkingkooka.spreadsheet.parser.SpreadsheetParserInfo;
import walkingkooka.spreadsheet.parser.SpreadsheetParserInfoSet;
import walkingkooka.spreadsheet.parser.SpreadsheetParserName;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;

public final class AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetParserAliasSetParsersTest implements PluginAliasSetLikeDialogComponentContextTesting<AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetParserAliasSetParsers,
    SpreadsheetParserName,
    SpreadsheetParserInfo,
    SpreadsheetParserInfoSet,
    SpreadsheetParserSelector,
    SpreadsheetParserAlias,
    SpreadsheetParserAliasSet> {
    @Override
    public AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetParserAliasSetParsers createContext() {
        return AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetParserAliasSetParsers.with(AppContexts.fake());
    }

    // Context..........................................................................................................

    @Override
    public String typeNameSuffix() {
        return SpreadsheetParserAliasSet.class.getSimpleName() + "Parsers";
    }

    // Class............................................................................................................

    @Override
    public Class<AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetParserAliasSetParsers> type() {
        return AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetParserAliasSetParsers.class;
    }
}
