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
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAlias;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAliasSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfo;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfoSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionSelector;

public final class AppContextPluginAliasSetLikeDialogComponentContextExpressionFunctionAliasSetFindFunctionsTest implements PluginAliasSetLikeDialogComponentContextTesting<AppContextPluginAliasSetLikeDialogComponentContextExpressionFunctionAliasSetFindFunctions,
    ExpressionFunctionName,
    ExpressionFunctionInfo,
    ExpressionFunctionInfoSet,
    ExpressionFunctionSelector,
    ExpressionFunctionAlias,
    ExpressionFunctionAliasSet> {
    @Override
    public AppContextPluginAliasSetLikeDialogComponentContextExpressionFunctionAliasSetFindFunctions createContext() {
        return AppContextPluginAliasSetLikeDialogComponentContextExpressionFunctionAliasSetFindFunctions.with(AppContexts.fake());
    }

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
    public SpreadsheetMetadataPropertyName<ExpressionFunctionAliasSet> metadataPropertyName() {
        return SpreadsheetMetadataPropertyName.FIND_FUNCTIONS;
    }

    // Class............................................................................................................

    @Override
    public Class<AppContextPluginAliasSetLikeDialogComponentContextExpressionFunctionAliasSetFindFunctions> type() {
        return AppContextPluginAliasSetLikeDialogComponentContextExpressionFunctionAliasSetFindFunctions.class;
    }
}
