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

package walkingkooka.spreadsheet.dominokit.find;

import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContextDelegator;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContextDelegator;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.SpreadsheetParserProvider;
import walkingkooka.spreadsheet.parser.SpreadsheetParserProviderDelegator;

import java.time.LocalDateTime;

final class AppContextSpreadsheetFindDialogComponentContext implements SpreadsheetFindDialogComponentContext,
        HistoryTokenContextDelegator,
        LoggingContextDelegator,
        SpreadsheetParserProviderDelegator,
        ProviderContextDelegator {

    static AppContextSpreadsheetFindDialogComponentContext with(final AppContext context) {
        return new AppContextSpreadsheetFindDialogComponentContext(context);
    }

    private AppContextSpreadsheetFindDialogComponentContext(final AppContext context) {
        this.context = context;
    }

    // HistoryTokenContext..............................................................................................

    @Override
    public HistoryTokenContext historyTokenContext() {
        return this.context;
    }

    // HasNow...........................................................................................................

    @Override
    public LocalDateTime now() {
        return this.context.now();
    }

    // HasSpreadsheetDeltaWatcher.......................................................................................

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.context.addSpreadsheetDeltaFetcherWatcher(watcher);
    }

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcherOnce(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.context.addSpreadsheetDeltaFetcherWatcherOnce(watcher);
    }

    @Override
    public SpreadsheetDeltaFetcher spreadsheetDeltaFetcher() {
        return this.context.spreadsheetDeltaFetcher();
    }

    // HasSpreadsheetMetadata...........................................................................................

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        return this.context.spreadsheetMetadata();
    }

    // SpreadsheetParserProvider........................................................................................

    @Override
    public SpreadsheetParserProvider spreadsheetParserProvider() {
        return this.context;
    }

    // ProviderContext..................................................................................................

    @Override
    public ProviderContext providerContext() {
        return this.context;
    }

    // LoggingContext...................................................................................................

    @Override
    public LoggingContext loggingContext() {
        return this.context;
    }

    private final AppContext context;

    @Override
    public String toString() {
        return this.context.toString();
    }
}
