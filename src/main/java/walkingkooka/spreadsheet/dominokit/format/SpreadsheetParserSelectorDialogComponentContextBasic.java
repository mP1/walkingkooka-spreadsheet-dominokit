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

package walkingkooka.spreadsheet.dominokit.format;

import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContextDelegator;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContextDelegator;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetIdHistoryToken;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetParserFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetParserFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.util.Throttler;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContextDelegator;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterProvider;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterProviderDelegator;
import walkingkooka.spreadsheet.parser.SpreadsheetParserProvider;
import walkingkooka.spreadsheet.parser.SpreadsheetParserProviderDelegator;

/**
 * A mostly complete {@link SpreadsheetParserSelectorDialogComponent}.
 */
abstract class SpreadsheetParserSelectorDialogComponentContextBasic implements SpreadsheetParserSelectorDialogComponentContext,
        HistoryTokenContextDelegator,
        LoggingContextDelegator,
        SpreadsheetFormatterContextDelegator,
        SpreadsheetFormatterProviderDelegator,
        SpreadsheetParserProviderDelegator,
        ProviderContextDelegator {

    SpreadsheetParserSelectorDialogComponentContextBasic(final AppContext context) {
        super();

        this.throttler = Throttler.empty(Throttler.KEYBOARD_DELAY);
        this.context = context;
    }

    // SpreadsheetFormatterContext......................................................................................

    @Override
    public final SpreadsheetFormatterContext spreadsheetFormatterContext() {
        return this.context;
    }

    // SpreadsheetParserContext.........................................................................................

    @Override
    public char valueSeparator() {
        return this.context.valueSeparator();
    }

    // SpreadsheetFormatterProvider........................................................................................

    @Override
    public final SpreadsheetFormatterProvider spreadsheetFormatterProvider() {
        return this.context;
    }

    // SpreadsheetParserProvider........................................................................................

    @Override
    public final SpreadsheetParserProvider spreadsheetParserProvider() {
        return this.context;
    }

    // HistoryTokenContext..............................................................................................

    @Override
    public final HistoryTokenContext historyTokenContext() {
        return this.context;
    }

    // misc.............................................................................................................

    @Override
    public final void giveFocus(final Runnable focus) {
        this.context.giveFocus(focus);
    }

    @Override
    public final Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.context.addSpreadsheetDeltaFetcherWatcher(watcher);
    }

    @Override
    public final Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.context.addSpreadsheetMetadataFetcherWatcher(watcher);
    }

    // SpreadsheetParserFetcher.........................................................................................

    @Override
    public final SpreadsheetParserFetcher spreadsheetParserFetcher() {
        return this.context.spreadsheetParserFetcher();
    }

    @Override
    public final Runnable addSpreadsheetParserFetcherWatcher(final SpreadsheetParserFetcherWatcher watcher) {
        return this.context.addSpreadsheetParserFetcherWatcher(watcher);
    }

    @Override
    public final Runnable addSpreadsheetParserFetcherWatcherOnce(final SpreadsheetParserFetcherWatcher watcher) {
        return this.context.addSpreadsheetParserFetcherWatcherOnce(watcher);
    }

    @Override
    public final void spreadsheetParsersEdit(final String text) {
        this.throttler.add(
                () -> this.spreadsheetParserFetcher()
                        .edit(
                                context.historyToken()
                                        .cast(SpreadsheetIdHistoryToken.class)
                                        .id(), // id
                                text
                        )
        );
    }

    /**
     * Used to throttle calls to /formatter/STAR/edit
     */
    private final Throttler throttler;

    // ProviderContext..................................................................................................

    @Override
    public final ProviderContext providerContext() {
        return this.context;
    }

    // log..............................................................................................................

    @Override
    public LoggingContext loggingContext() {
        return this.context;
    }

    // Object..........................................................................................................

    @Override
    public final String toString() {
        return this.context.toString();
    }

    final AppContext context;
}
