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

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAliasSet;

final class PluginAliasSetLikeDialogComponentContextBasicExpressionFunctionAliasesFormulaFunctions extends PluginAliasSetLikeDialogComponentContextBasicExpressionFunctionAliases {

    static PluginAliasSetLikeDialogComponentContextBasicExpressionFunctionAliasesFormulaFunctions with(final AppContext context) {
        return new PluginAliasSetLikeDialogComponentContextBasicExpressionFunctionAliasesFormulaFunctions(context);
    }

    private PluginAliasSetLikeDialogComponentContextBasicExpressionFunctionAliasesFormulaFunctions(final AppContext context) {
        super(context);
    }

    // PluginAliasSetLikeDialogComponentContext.........................................................................

    @Override
    SpreadsheetMetadataPropertyName<ExpressionFunctionAliasSet> metadataPropertyName() {
        return SpreadsheetMetadataPropertyName.FORMULA_FUNCTIONS;
    }

    @Override
    public ExpressionFunctionAliasSet providerAliasSetLike() {
        return this.providerAliasSetLikeAndFunctions();
    }
}
