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

package walkingkooka.spreadsheet.dominokit.pluginfosetlink;

import elemental2.dom.Headers;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.function.ExpressionFunctionInfoSetComponent;
import walkingkooka.spreadsheet.dominokit.net.ExpressionFunctionFetcherWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.provider.SpreadsheetProvider;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAlias;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAliasSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfo;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfoSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionSelector;

import java.util.Optional;
import java.util.function.Consumer;

final class PluginInfoSetLikeDialogComponentContextBasicSpreadsheetExpressionFunctions extends PluginInfoSetLikeDialogComponentContextBasic<ExpressionFunctionName,
        ExpressionFunctionInfo,
        ExpressionFunctionInfoSet,
        ExpressionFunctionSelector,
        ExpressionFunctionAlias,
        ExpressionFunctionAliasSet> {

    static PluginInfoSetLikeDialogComponentContextBasicSpreadsheetExpressionFunctions with(final AppContext context) {
        return new PluginInfoSetLikeDialogComponentContextBasicSpreadsheetExpressionFunctions(context);
    }

    private PluginInfoSetLikeDialogComponentContextBasicSpreadsheetExpressionFunctions(final AppContext context) {
        super(context);
    }

    // PluginInfoSetLikeDialogComponentContext..............................................................................

    @Override
    public ExpressionFunctionInfoSetComponent textBox() {
        return ExpressionFunctionInfoSetComponent.empty();
    }

    @Override
    SpreadsheetMetadataPropertyName<ExpressionFunctionInfoSet> metadataPropertyName() {
        return SpreadsheetMetadataPropertyName.FUNCTIONS;
    }

    @Override
    public ExpressionFunctionInfoSet emptyInfoSetLike() {
        return ExpressionFunctionInfoSet.EMPTY;
    }

    @Override
    void loadPluginInfoSet0(final SpreadsheetId id) {
        this.context.expressionFunctionFetcher()
                .infoSet(id);
    }

    @Override
    public Runnable addProviderFetcherWatcher(final Consumer<ExpressionFunctionInfoSet> set) {
        return this.context.addExpressionFunctionFetcherWatcher(
                new ExpressionFunctionFetcherWatcher() {
                    @Override
                    public void onExpressionFunctionInfoSet(final SpreadsheetId id,
                                                            final ExpressionFunctionInfoSet infos,
                                                            final AppContext context) {
                        set.accept(infos);
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

    @Override
    ExpressionFunctionInfoSet providerInfoSet0(final SpreadsheetProvider spreadsheetProvider) {
        return spreadsheetProvider.expressionFunctionInfos();
    }
}
