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
import walkingkooka.spreadsheet.dominokit.export.SpreadsheetExporterAliasSetComponent;
import walkingkooka.spreadsheet.dominokit.fetcher.FetcherRequestBody;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetExporterFetcherWatcher;
import walkingkooka.spreadsheet.export.SpreadsheetExporterAlias;
import walkingkooka.spreadsheet.export.SpreadsheetExporterAliasSet;
import walkingkooka.spreadsheet.export.SpreadsheetExporterInfo;
import walkingkooka.spreadsheet.export.SpreadsheetExporterInfoSet;
import walkingkooka.spreadsheet.export.SpreadsheetExporterName;
import walkingkooka.spreadsheet.export.SpreadsheetExporterSelector;

import java.util.Optional;
import java.util.function.Consumer;

abstract class AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetExporterAliasSet extends AppContextPluginAliasSetLikeDialogComponentContext<SpreadsheetExporterName,
    SpreadsheetExporterInfo,
    SpreadsheetExporterInfoSet,
    SpreadsheetExporterSelector,
    SpreadsheetExporterAlias,
    SpreadsheetExporterAliasSet> {

    AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetExporterAliasSet(final AppContext context) {
        super(context);
    }

    // PluginAliasSetLikeDialogComponentContext..............................................................................

    @Override
    public final SpreadsheetExporterAliasSetComponent textBox() {
        return SpreadsheetExporterAliasSetComponent.empty();
    }

    @Override
    public final SpreadsheetExporterAliasSet emptyAliasSetLike() {
        return SpreadsheetExporterAliasSet.EMPTY;
    }

    @Override
    public final void loadPluginInfoSetLike() {
        this.context.spreadsheetExporterFetcher()
            .getInfoSet();
    }

    @Override
    public final Runnable addProviderFetcherWatcher(final Consumer<SpreadsheetExporterAliasSet> set) {
        return this.context.addSpreadsheetExporterFetcherWatcher(
            new SpreadsheetExporterFetcherWatcher() {
                @Override
                public void onSpreadsheetExporterInfoSet(final SpreadsheetExporterInfoSet infos,
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
