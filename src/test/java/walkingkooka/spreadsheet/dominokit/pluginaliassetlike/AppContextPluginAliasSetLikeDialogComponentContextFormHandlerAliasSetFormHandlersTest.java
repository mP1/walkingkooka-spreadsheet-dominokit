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
import walkingkooka.validation.form.provider.FormHandlerAlias;
import walkingkooka.validation.form.provider.FormHandlerAliasSet;
import walkingkooka.validation.form.provider.FormHandlerInfo;
import walkingkooka.validation.form.provider.FormHandlerInfoSet;
import walkingkooka.validation.form.provider.FormHandlerName;
import walkingkooka.validation.form.provider.FormHandlerSelector;

public final class AppContextPluginAliasSetLikeDialogComponentContextFormHandlerAliasSetFormHandlersTest implements PluginAliasSetLikeDialogComponentContextTesting<AppContextPluginAliasSetLikeDialogComponentContextFormHandlerAliasSetFormHandlers,
    FormHandlerName,
    FormHandlerInfo,
    FormHandlerInfoSet,
    FormHandlerSelector,
    FormHandlerAlias,
    FormHandlerAliasSet> {

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
    public AppContextPluginAliasSetLikeDialogComponentContextFormHandlerAliasSetFormHandlers createContext() {
        return AppContextPluginAliasSetLikeDialogComponentContextFormHandlerAliasSetFormHandlers.with(AppContexts.fake());
    }

    @Override
    public SpreadsheetMetadataPropertyName<FormHandlerAliasSet> metadataPropertyName() {
        return SpreadsheetMetadataPropertyName.FORM_HANDLERS;
    }

    // Class............................................................................................................

    @Override
    public Class<AppContextPluginAliasSetLikeDialogComponentContextFormHandlerAliasSetFormHandlers> type() {
        return AppContextPluginAliasSetLikeDialogComponentContextFormHandlerAliasSetFormHandlers.class;
    }
}
