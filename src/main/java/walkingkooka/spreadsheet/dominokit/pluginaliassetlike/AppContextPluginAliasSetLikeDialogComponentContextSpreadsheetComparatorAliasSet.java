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
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorAlias;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorAliasSet;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfoSet;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorSelector;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.comparator.SpreadsheetComparatorAliasSetComponent;
import walkingkooka.spreadsheet.dominokit.fetcher.FetcherRequestBody;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetComparatorFetcherWatcher;

import java.util.Optional;
import java.util.function.Consumer;

abstract class AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetComparatorAliasSet extends AppContextPluginAliasSetLikeDialogComponentContext<SpreadsheetComparatorName,
    SpreadsheetComparatorInfo,
    SpreadsheetComparatorInfoSet,
    SpreadsheetComparatorSelector,
    SpreadsheetComparatorAlias,
    SpreadsheetComparatorAliasSet> {

    AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetComparatorAliasSet(final AppContext context) {
        super(context);
    }

    // PluginAliasSetLikeDialogComponentContext..............................................................................

    @Override
    public final SpreadsheetComparatorAliasSetComponent textBox() {
        return SpreadsheetComparatorAliasSetComponent.empty();
    }

    @Override
    public final SpreadsheetComparatorAliasSet emptyAliasSetLike() {
        return SpreadsheetComparatorAliasSet.EMPTY;
    }

    @Override
    final void loadPluginInfoSetLike0(final SpreadsheetId id) {
        this.context.spreadsheetComparatorFetcher()
            .getInfoSet();
    }

    @Override
    public final Runnable addProviderFetcherWatcher(final Consumer<SpreadsheetComparatorAliasSet> set) {
        return this.context.addSpreadsheetComparatorFetcherWatcher(
            new SpreadsheetComparatorFetcherWatcher() {
                @Override
                public void onSpreadsheetComparatorInfoSet(final SpreadsheetComparatorInfoSet infos,
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
