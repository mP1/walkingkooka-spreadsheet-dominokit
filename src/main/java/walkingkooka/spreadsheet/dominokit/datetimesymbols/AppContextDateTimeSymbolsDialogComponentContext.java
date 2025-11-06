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
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.RefreshContextDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.DateTimeSymbolsFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;

abstract class AppContextDateTimeSymbolsDialogComponentContext implements DateTimeSymbolsDialogComponentContext,
    RefreshContextDelegator {

    AppContextDateTimeSymbolsDialogComponentContext(final AppContext context) {
        this.context = context;
    }

    @Override
    public final void findDateTimeSymbolsWithLocaleStartsWith(final String startsWith) {
        this.context.dateTimeSymbolsFetcher()
            .getDateTimeSymbolsLocaleStartsWith(startsWith);
    }

    @Override
    public final Runnable addDateTimeSymbolsFetcherWatcher(final DateTimeSymbolsFetcherWatcher watcher) {
        return this.context.addDateTimeSymbolsFetcherWatcher(watcher);
    }

    @Override
    public final Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.context.addSpreadsheetDeltaFetcherWatcher(watcher);
    }

    @Override
    public final Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.context.addSpreadsheetMetadataFetcherWatcher(watcher);
    }

    // RefreshContextDelegator..........................................................................................

    @Override
    public final RefreshContext refreshContext() {
        return this.context;
    }

    final AppContext context;

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.context.toString();
    }
}
