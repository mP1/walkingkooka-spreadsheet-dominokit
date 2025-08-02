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

package walkingkooka.spreadsheet.dominokit.datetimesymbols;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;

abstract class AppContextDateTimeSymbolsDialogComponentContext implements DateTimeSymbolsDialogComponentContext,
    HistoryContextDelegator,
    LoggingContextDelegator {

    AppContextDateTimeSymbolsDialogComponentContext(final AppContext context) {
        this.context = context;
    }

    @Override
    public final Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.context.addSpreadsheetDeltaFetcherWatcher(watcher);
    }

    @Override
    public final Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.context.addSpreadsheetMetadataFetcherWatcher(watcher);
    }

    // HistoryContextDelegator..........................................................................................

    @Override
    public final HistoryContext historyContext() {
        return this.context;
    }

    // LoggingContextDelegator..........................................................................................

    @Override
    public final LoggingContext loggingContext() {
        return this.context;
    }

    final AppContext context;

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.context.toString();
    }
}
