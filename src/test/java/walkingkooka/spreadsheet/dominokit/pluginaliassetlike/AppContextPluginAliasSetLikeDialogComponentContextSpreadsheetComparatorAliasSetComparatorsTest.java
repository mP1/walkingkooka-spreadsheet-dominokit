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
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorAlias;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorAliasSet;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfoSet;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorSelector;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

public final class AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetComparatorAliasSetComparatorsTest implements PluginAliasSetLikeDialogComponentContextTesting<AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetComparatorAliasSetComparators,
    SpreadsheetComparatorName,
    SpreadsheetComparatorInfo,
    SpreadsheetComparatorInfoSet,
    SpreadsheetComparatorSelector,
    SpreadsheetComparatorAlias,
    SpreadsheetComparatorAliasSet> {

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
    public AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetComparatorAliasSetComparators createContext() {
        return AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetComparatorAliasSetComparators.with(AppContexts.fake());
    }

    @Override
    public SpreadsheetMetadataPropertyName<SpreadsheetComparatorAliasSet> metadataPropertyName() {
        return SpreadsheetMetadataPropertyName.COMPARATORS;
    }

    // Class............................................................................................................

    @Override
    public Class<AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetComparatorAliasSetComparators> type() {
        return AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetComparatorAliasSetComparators.class;
    }
}
