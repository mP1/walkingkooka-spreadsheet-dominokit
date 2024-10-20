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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.function.ExpressionFunctionAliasSetComponent;
import walkingkooka.spreadsheet.dominokit.net.ExpressionFunctionFetcherWatcher;
import walkingkooka.spreadsheet.provider.SpreadsheetProvider;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAlias;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAliasSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfo;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfoSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionSelector;

import java.util.Optional;
import java.util.function.Consumer;

abstract class PluginAliasSetLikeDialogComponentContextBasicExpressionFunctionAliases extends PluginAliasSetLikeDialogComponentContextBasic<ExpressionFunctionName,
        ExpressionFunctionInfo,
        ExpressionFunctionInfoSet,
        ExpressionFunctionSelector,
        ExpressionFunctionAlias,
        ExpressionFunctionAliasSet> {

    PluginAliasSetLikeDialogComponentContextBasicExpressionFunctionAliases(final AppContext context) {
        super(context);
    }

    // PluginAliasSetLikeDialogComponentContext..............................................................................

    @Override
    public final ExpressionFunctionAliasSetComponent textBox() {
        return ExpressionFunctionAliasSetComponent.empty();
    }

    @Override
    public final ExpressionFunctionAliasSet emptyAliasSetLike() {
        return ExpressionFunctionAliasSet.EMPTY;
    }

    @Override final void loadPluginInfoSetLike0(final SpreadsheetId id) {
        this.context.expressionFunctionFetcher()
                .infoSet(id);
    }

    @Override
    public final Runnable addProviderFetcherWatcher(final Consumer<ExpressionFunctionAliasSet> set) {
        return this.context.addExpressionFunctionFetcherWatcher(
                new ExpressionFunctionFetcherWatcher() {
                    @Override
                    public void onExpressionFunctionInfoSet(final SpreadsheetId id,
                                                            final ExpressionFunctionInfoSet infos,
                                                            final AppContext context) {
                        set.accept(infos.aliasSet());
                    }

                    @Override
                    public void onBegin(final HttpMethod method,
                                        final Url url,
                                        final Optional<String> body,
                                        final AppContext context) {
                        // nop
                    }

                    @Override
                    public void onFailure(final HttpMethod method,
                                          final AbsoluteOrRelativeUrl url,
                                          final HttpStatus status,
                                          final Headers headers,
                                          final String body,
                                          final AppContext context) {
                        // nop
                    }

                    @Override
                    public void onError(final Object cause,
                                        final AppContext context) {
                        // nop
                    }

                    @Override
                    public void onEmptyResponse(final AppContext context) {
                        // nop
                    }
                }
        );
    }

    @Override final ExpressionFunctionAliasSet providerAliasSetLike0(final SpreadsheetProvider spreadsheetProvider) {
        return spreadsheetProvider.expressionFunctionInfos()
                .aliasSet();
    }
}
