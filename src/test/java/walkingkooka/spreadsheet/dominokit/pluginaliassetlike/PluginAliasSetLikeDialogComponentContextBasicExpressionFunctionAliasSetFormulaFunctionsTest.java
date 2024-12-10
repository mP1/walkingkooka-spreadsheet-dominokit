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
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAlias;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAliasSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfo;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfoSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionSelector;

public final class PluginAliasSetLikeDialogComponentContextBasicExpressionFunctionAliasSetFormulaFunctionsTest implements PluginAliasSetLikeDialogComponentContextTesting<PluginAliasSetLikeDialogComponentContextBasicExpressionFunctionAliasSetFormulaFunctions,
        ExpressionFunctionName,
        ExpressionFunctionInfo,
        ExpressionFunctionInfoSet,
        ExpressionFunctionSelector,
        ExpressionFunctionAlias,
        ExpressionFunctionAliasSet> {
    @Override
    public PluginAliasSetLikeDialogComponentContextBasicExpressionFunctionAliasSetFormulaFunctions createContext() {
        return PluginAliasSetLikeDialogComponentContextBasicExpressionFunctionAliasSetFormulaFunctions.with(AppContexts.fake());
    }

    // Context..........................................................................................................

    @Override
    public String typeNameSuffix() {
        return ExpressionFunctionAliasSet.class.getSimpleName() + "FormulaFunctions";
    }

    // Class............................................................................................................

    @Override
    public Class<PluginAliasSetLikeDialogComponentContextBasicExpressionFunctionAliasSetFormulaFunctions> type() {
        return PluginAliasSetLikeDialogComponentContextBasicExpressionFunctionAliasSetFormulaFunctions.class;
    }
}
