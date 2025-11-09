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

import elemental2.dom.Headers;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.ExpressionFunctionFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.FetcherRequestBody;
import walkingkooka.spreadsheet.dominokit.function.ExpressionFunctionAliasSetComponent;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAlias;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAliasSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfo;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfoSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionSelector;

import java.util.Optional;
import java.util.function.Consumer;

abstract class AppContextPluginAliasSetLikeDialogComponentContextExpressionFunctionAliasSet extends AppContextPluginAliasSetLikeDialogComponentContext<ExpressionFunctionName,
    ExpressionFunctionInfo,
    ExpressionFunctionInfoSet,
    ExpressionFunctionSelector,
    ExpressionFunctionAlias,
    ExpressionFunctionAliasSet> {

    AppContextPluginAliasSetLikeDialogComponentContextExpressionFunctionAliasSet(final AppContext context) {
        super(context);
    }

    // PluginAliasSetLikeDialogComponentContext..............................................................................

    @Override
    public final ExpressionFunctionAliasSetComponent textBox() {
        return ExpressionFunctionAliasSetComponent.empty();
    }

    @Override
    public final ExpressionFunctionAliasSet emptyAliasSetLike() {
        return SpreadsheetExpressionFunctions.EMPTY_ALIAS_SET;
    }

    @Override
    public final void loadPluginInfoSetLike() {
        this.context.expressionFunctionFetcher()
            .getInfoSet();
    }

    @Override
    public final Runnable addProviderFetcherWatcher(final Consumer<ExpressionFunctionAliasSet> set) {
        return this.context.addExpressionFunctionFetcherWatcher(
            new ExpressionFunctionFetcherWatcher() {
                @Override
                public void onExpressionFunctionInfoSet(final ExpressionFunctionInfoSet infos) {
                    set.accept(infos.aliasSet());
                }

                @Override
                public void onBegin(final HttpMethod method,
                                    final Url url,
                                    final Optional<FetcherRequestBody<?>> body) {
                    // nop
                }

                @Override
                public void onFailure(final HttpMethod method,
                                      final AbsoluteOrRelativeUrl url,
                                      final HttpStatus status,
                                      final Headers headers,
                                      final String body) {
                    // nop
                }

                @Override
                public void onError(final Object cause) {
                    // nop
                }

                @Override
                public void onEmptyResponse() {
                    // nop
                }
            }
        );
    }

    /**
     * This takes the {@link ExpressionFunctionInfoSet} filtered by {@link SpreadsheetMetadataPropertyName#FUNCTIONS},
     * providing a view of the effective {@link ExpressionFunctionAliasSet aliase}.
     */
    final ExpressionFunctionAliasSet providerAliasSetLikeAndFunctions() {
        final AppContext context = this.context;

        return context.spreadsheetMetadata()
            .get(SpreadsheetMetadataPropertyName.FUNCTIONS)
            .orElse(SpreadsheetExpressionFunctions.EMPTY_ALIAS_SET)
            .keepAliasOrNameAll(
                context.systemSpreadsheetProvider()
                    .expressionFunctionInfos()
                    .names()
            );
    }
}
