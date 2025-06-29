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
import walkingkooka.convert.provider.ConverterAlias;
import walkingkooka.convert.provider.ConverterAliasSet;
import walkingkooka.convert.provider.ConverterInfo;
import walkingkooka.convert.provider.ConverterInfoSet;
import walkingkooka.convert.provider.ConverterName;
import walkingkooka.convert.provider.ConverterSelector;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.convert.ConverterAliasSetComponent;
import walkingkooka.spreadsheet.dominokit.fetcher.ConverterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.FetcherRequestBody;

import java.util.Optional;
import java.util.function.Consumer;

abstract class AppContextPluginAliasSetLikeDialogComponentContextConverterAliasSet extends AppContextPluginAliasSetLikeDialogComponentContext<ConverterName,
    ConverterInfo,
    ConverterInfoSet,
    ConverterSelector,
    ConverterAlias,
    ConverterAliasSet> {

    AppContextPluginAliasSetLikeDialogComponentContextConverterAliasSet(final AppContext context) {
        super(context);
    }

    // PluginAliasSetLikeDialogComponentContext..............................................................................

    @Override
    public final ConverterAliasSetComponent textBox() {
        return ConverterAliasSetComponent.empty();
    }

    @Override
    public final ConverterAliasSet emptyAliasSetLike() {
        return ConverterAliasSet.EMPTY;
    }

    @Override final void loadPluginInfoSetLike0(final SpreadsheetId id) {
        this.context.converterFetcher()
            .getInfoSet(id);
    }

    @Override
    public final Runnable addProviderFetcherWatcher(final Consumer<ConverterAliasSet> set) {
        return this.context.addConverterFetcherWatcher(
            new ConverterFetcherWatcher() {
                @Override
                public void onConverterInfoSet(final SpreadsheetId id,
                                               final ConverterInfoSet infos,
                                               final AppContext context) {
                    set.accept(infos.aliasSet());
                }

                @Override
                public void onBegin(final HttpMethod method,
                                    final Url url,
                                    final Optional<FetcherRequestBody<?>> body,
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
}
