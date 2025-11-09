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
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetImporterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.importer.SpreadsheetImporterAliasSetComponent;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterAlias;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterAliasSet;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterInfo;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterInfoSet;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterName;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterSelector;

import java.util.Optional;
import java.util.function.Consumer;

abstract class AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetImporterAliasSet extends AppContextPluginAliasSetLikeDialogComponentContext<SpreadsheetImporterName,
    SpreadsheetImporterInfo,
    SpreadsheetImporterInfoSet,
    SpreadsheetImporterSelector,
    SpreadsheetImporterAlias,
    SpreadsheetImporterAliasSet> {

    AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetImporterAliasSet(final AppContext context) {
        super(context);
    }

    // PluginAliasSetLikeDialogComponentContext..............................................................................

    @Override
    public final SpreadsheetImporterAliasSetComponent textBox() {
        return SpreadsheetImporterAliasSetComponent.empty();
    }

    @Override
    public final SpreadsheetImporterAliasSet emptyAliasSetLike() {
        return SpreadsheetImporterAliasSet.EMPTY;
    }

    @Override
    public final void loadPluginInfoSetLike() {
        this.context.spreadsheetImporterFetcher()
            .getInfoSet();
    }

    @Override
    public final Runnable addProviderFetcherWatcher(final Consumer<SpreadsheetImporterAliasSet> set) {
        return this.context.addSpreadsheetImporterFetcherWatcher(
            new SpreadsheetImporterFetcherWatcher() {
                @Override
                public void onSpreadsheetImporterInfoSet(final SpreadsheetImporterInfoSet infos) {
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
}
