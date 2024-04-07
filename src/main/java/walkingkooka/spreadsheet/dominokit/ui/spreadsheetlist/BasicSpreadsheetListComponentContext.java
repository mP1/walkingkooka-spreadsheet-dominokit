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

package walkingkooka.spreadsheet.dominokit.ui.spreadsheetlist;

import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatchers;

public final class BasicSpreadsheetListComponentContext implements SpreadsheetListComponentContext {

    static BasicSpreadsheetListComponentContext with(final HistoryTokenContext historyTokenContext,
                                                     final SpreadsheetMetadataFetcher metadataFetcher,
                                                     final SpreadsheetMetadataFetcherWatchers metadataFetcherWatchers) {
        return new BasicSpreadsheetListComponentContext(
                historyTokenContext,
                metadataFetcher,
                metadataFetcherWatchers
        );
    }

    private BasicSpreadsheetListComponentContext(final HistoryTokenContext historyTokenContext,
                                                 final SpreadsheetMetadataFetcher metadataFetcher,
                                                 final SpreadsheetMetadataFetcherWatchers metadataFetcherWatchers) {
        this.historyTokenContext = historyTokenContext;
        this.metadataFetcher = metadataFetcher;
        this.metadataFetcherWatchers = metadataFetcherWatchers;
    }

    @Override
    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
        return this.historyTokenContext.addHistoryTokenWatcher(watcher);
    }

    @Override
    public Runnable addHistoryTokenWatcherOnce(final HistoryTokenWatcher watcher) {
        return this.historyTokenContext.addHistoryTokenWatcherOnce(watcher);
    }

    @Override
    public HistoryToken historyToken() {
        return this.historyTokenContext.historyToken();
    }

    @Override
    public void pushHistoryToken(final HistoryToken token) {
        this.historyTokenContext.pushHistoryToken(token);
    }

    @Override
    public void fireCurrentHistoryToken() {
        this.historyTokenContext.fireCurrentHistoryToken();
    }

    private final HistoryTokenContext historyTokenContext;

    @Override
    public Runnable addSpreadsheetMetadataWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.metadataFetcherWatchers.add(watcher);
    }

    @Override
    public Runnable addSpreadsheetMetadataWatcherOnce(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.metadataFetcherWatchers.addOnce(watcher);
    }

    private final SpreadsheetMetadataFetcherWatchers metadataFetcherWatchers;

    @Override
    public SpreadsheetMetadataFetcher spreadsheetMetadataFetcher() {
        return this.metadataFetcher;
    }

    private final SpreadsheetMetadataFetcher metadataFetcher;
}
