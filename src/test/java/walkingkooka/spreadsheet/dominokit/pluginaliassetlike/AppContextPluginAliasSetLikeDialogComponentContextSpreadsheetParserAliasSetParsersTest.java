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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserAlias;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserAliasSet;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserInfo;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserInfoSet;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserName;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;

public final class AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetParserAliasSetParsersTest implements PluginAliasSetLikeDialogComponentContextTesting<AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetParserAliasSetParsers,
    SpreadsheetParserName,
    SpreadsheetParserInfo,
    SpreadsheetParserInfoSet,
    SpreadsheetParserSelector,
    SpreadsheetParserAlias,
    SpreadsheetParserAliasSet> {

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

    @Test
    public void testIsMatchWithSpreadsheetMetadataPropertySelectHistoryTokenParsers() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.PARSERS
            ),
            true
        );
    }

    @Override
    public AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetParserAliasSetParsers createContext() {
        return AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetParserAliasSetParsers.with(AppContexts.fake());
    }

    @Override
    public SpreadsheetMetadataPropertyName<SpreadsheetParserAliasSet> metadataPropertyName() {
        return SpreadsheetMetadataPropertyName.PARSERS;
    }

    // Class............................................................................................................

    @Override
    public Class<AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetParserAliasSetParsers> type() {
        return AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetParserAliasSetParsers.class;
    }
}
