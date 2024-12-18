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

package walkingkooka.spreadsheet.dominokit.spreadsheet;

import walkingkooka.locale.HasLocale;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContextDelegator;

import java.util.Locale;

public final class BasicSpreadsheetListComponentContext implements SpreadsheetListComponentContext,
        HistoryTokenContextDelegator {

    static BasicSpreadsheetListComponentContext with(final HistoryTokenContext historyTokenContext,
                                                     final SpreadsheetMetadataFetcherWatchers metadataFetcherWatchers,
                                                     final HasLocale hasLocale) {
        return new BasicSpreadsheetListComponentContext(
                historyTokenContext,
                metadataFetcherWatchers,
                hasLocale
        );
    }

    private BasicSpreadsheetListComponentContext(final HistoryTokenContext historyTokenContext,
                                                 final SpreadsheetMetadataFetcherWatchers metadataFetcherWatchers,
                                                 final HasLocale hasLocale) {
        this.historyTokenContext = historyTokenContext;
        this.metadataFetcherWatchers = metadataFetcherWatchers;
        this.hasLocale = hasLocale;
    }

    @Override
    public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.metadataFetcherWatchers.add(watcher);
    }

    @Override
    public Runnable addSpreadsheetMetadataFetcherWatcherOnce(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.metadataFetcherWatchers.addOnce(watcher);
    }

    private final SpreadsheetMetadataFetcherWatchers metadataFetcherWatchers;

    @Override
    public Locale locale() {
        return this.hasLocale.locale();
    }

    private final HasLocale hasLocale;

    // HistoryTokenContext..............................................................................................

    @Override
    public HistoryTokenContext historyTokenContext() {
        return this.historyTokenContext;
    }

    private final HistoryTokenContext historyTokenContext;
}
