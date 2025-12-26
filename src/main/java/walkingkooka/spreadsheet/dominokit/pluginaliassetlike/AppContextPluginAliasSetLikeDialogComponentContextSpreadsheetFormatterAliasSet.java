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
import walkingkooka.spreadsheet.dominokit.fetcher.FetcherRequestBody;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetFormatterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.format.SpreadsheetFormatterAliasSetComponent;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterAlias;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterAliasSet;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterInfo;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterInfoSet;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterMenuList;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorEdit;

import java.util.Optional;
import java.util.function.Consumer;

abstract class AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetFormatterAliasSet extends AppContextPluginAliasSetLikeDialogComponentContext<SpreadsheetFormatterName,
    SpreadsheetFormatterInfo,
    SpreadsheetFormatterInfoSet,
    SpreadsheetFormatterSelector,
    SpreadsheetFormatterAlias,
    SpreadsheetFormatterAliasSet> {

    AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetFormatterAliasSet(final AppContext context) {
        super(context);
    }

    // PluginAliasSetLikeDialogComponentContext..............................................................................

    @Override
    public final SpreadsheetFormatterAliasSetComponent textBox() {
        return SpreadsheetFormatterAliasSetComponent.empty();
    }

    @Override
    public final SpreadsheetFormatterAliasSet emptyAliasSetLike() {
        return SpreadsheetFormatterAliasSet.EMPTY;
    }

    @Override
    public final void loadPluginInfoSetLike() {
        this.context.spreadsheetFormatterFetcher()
            .getInfoSet();
    }

    @Override
    public final Runnable addProviderFetcherWatcher(final Consumer<SpreadsheetFormatterAliasSet> set) {
        return this.context.addSpreadsheetFormatterFetcherWatcher(
            new SpreadsheetFormatterFetcherWatcher() {
                @Override
                public void onSpreadsheetFormatterInfoSet(final SpreadsheetFormatterInfoSet infos) {
                    set.accept(infos.aliasSet());
                }

                @Override
                public void onSpreadsheetFormatterSelectorEdit(final SpreadsheetId id,
                                                               final Optional<SpreadsheetExpressionReference> cellOrLabel,
                                                               final SpreadsheetFormatterSelectorEdit edit) {
                    // nop
                }

                @Override
                public void onSpreadsheetFormatterMenuList(final SpreadsheetId id,
                                                           final SpreadsheetExpressionReference cellOrLabel,
                                                           final SpreadsheetFormatterMenuList menu) {
                    // nop
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
}
