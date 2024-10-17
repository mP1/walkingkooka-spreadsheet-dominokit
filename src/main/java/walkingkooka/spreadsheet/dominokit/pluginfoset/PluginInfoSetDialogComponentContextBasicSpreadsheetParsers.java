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

package walkingkooka.spreadsheet.dominokit.pluginfoset;

import elemental2.dom.Headers;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetParserFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.parser.SpreadsheetParserInfoSetComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.parser.SpreadsheetParserAlias;
import walkingkooka.spreadsheet.parser.SpreadsheetParserAliasSet;
import walkingkooka.spreadsheet.parser.SpreadsheetParserInfo;
import walkingkooka.spreadsheet.parser.SpreadsheetParserInfoSet;
import walkingkooka.spreadsheet.parser.SpreadsheetParserName;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.provider.SpreadsheetProvider;
import walkingkooka.spreadsheet.server.parser.SpreadsheetParserSelectorEdit;

import java.util.Optional;
import java.util.function.Consumer;

final class PluginInfoSetDialogComponentContextBasicSpreadsheetParsers extends PluginInfoSetDialogComponentContextBasic<SpreadsheetParserName,
        SpreadsheetParserInfo,
        SpreadsheetParserInfoSet,
        SpreadsheetParserSelector,
        SpreadsheetParserAlias,
        SpreadsheetParserAliasSet> {

    static PluginInfoSetDialogComponentContextBasicSpreadsheetParsers with(final AppContext context) {
        return new PluginInfoSetDialogComponentContextBasicSpreadsheetParsers(context);
    }

    private PluginInfoSetDialogComponentContextBasicSpreadsheetParsers(final AppContext context) {
        super(context);
    }

    // PluginInfoSetDialogComponentContext..............................................................................

    @Override
    public SpreadsheetParserInfoSetComponent textBox() {
        return SpreadsheetParserInfoSetComponent.empty();
    }

    @Override
    SpreadsheetMetadataPropertyName<SpreadsheetParserInfoSet> metadataPropertyName() {
        return SpreadsheetMetadataPropertyName.PARSERS;
    }

    @Override
    public SpreadsheetParserInfoSet emptyInfoSet() {
        return SpreadsheetParserInfoSet.EMPTY;
    }

    @Override
    void loadPluginInfoSet0(final SpreadsheetId id) {
        this.context.spreadsheetParserFetcher()
                .infoSet(id);
    }

    @Override
    public Runnable addProviderFetcherWatcher(final Consumer<SpreadsheetParserInfoSet> set) {
        return this.context.addSpreadsheetParserFetcherWatcher(
                new SpreadsheetParserFetcherWatcher() {
                    @Override
                    public void onSpreadsheetParserInfoSet(final SpreadsheetId id,
                                                           final SpreadsheetParserInfoSet infos,
                                                           final AppContext context) {
                        set.accept(infos);
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

    @Override
    SpreadsheetParserInfoSet providerInfoSet0(final SpreadsheetProvider spreadsheetProvider) {
        return spreadsheetProvider.spreadsheetParserInfos();
    }
}
