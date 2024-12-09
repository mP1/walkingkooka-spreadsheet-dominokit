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
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetParserFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.parser.SpreadsheetParserAliasSetComponent;
import walkingkooka.spreadsheet.parser.SpreadsheetParserAlias;
import walkingkooka.spreadsheet.parser.SpreadsheetParserAliasSet;
import walkingkooka.spreadsheet.parser.SpreadsheetParserInfo;
import walkingkooka.spreadsheet.parser.SpreadsheetParserInfoSet;
import walkingkooka.spreadsheet.parser.SpreadsheetParserName;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.server.parser.SpreadsheetParserSelectorEdit;

import java.util.Optional;
import java.util.function.Consumer;

abstract class PluginAliasSetLikeDialogComponentContextBasicSpreadsheetParserAliasSet extends PluginAliasSetLikeDialogComponentContextBasic<SpreadsheetParserName,
        SpreadsheetParserInfo,
        SpreadsheetParserInfoSet,
        SpreadsheetParserSelector,
        SpreadsheetParserAlias,
        SpreadsheetParserAliasSet> {

    PluginAliasSetLikeDialogComponentContextBasicSpreadsheetParserAliasSet(final AppContext context) {
        super(context);
    }

    // PluginAliasSetLikeDialogComponentContext..............................................................................

    @Override
    public final SpreadsheetParserAliasSetComponent textBox() {
        return SpreadsheetParserAliasSetComponent.empty();
    }

    @Override
    public final SpreadsheetParserAliasSet emptyAliasSetLike() {
        return SpreadsheetParserAliasSet.EMPTY;
    }

    @Override final void loadPluginInfoSetLike0(final SpreadsheetId id) {
        this.context.spreadsheetParserFetcher()
                .infoSet(id);
    }

    @Override
    public final Runnable addProviderFetcherWatcher(final Consumer<SpreadsheetParserAliasSet> set) {
        return this.context.addSpreadsheetParserFetcherWatcher(
                new SpreadsheetParserFetcherWatcher() {
                    @Override
                    public void onSpreadsheetParserInfoSet(final SpreadsheetId id,
                                                           final SpreadsheetParserInfoSet infos,
                                                           final AppContext context) {
                        set.accept(infos.aliasSet());
                    }

                    @Override
                    public void onSpreadsheetParserSelectorEdit(final SpreadsheetId id,
                                                                final SpreadsheetParserSelectorEdit edit,
                                                                final AppContext context) {
                        // nop
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
}
