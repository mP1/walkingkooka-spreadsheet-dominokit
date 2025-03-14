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

package walkingkooka.spreadsheet.dominokit.delta;

import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;

import java.util.Objects;

final class BasicSpreadsheetDeltaCellsTableComponentContext implements SpreadsheetDeltaCellsTableComponentContext,
    HistoryContextDelegator,
    HasSpreadsheetDeltaFetcherWatchers {

    static BasicSpreadsheetDeltaCellsTableComponentContext with(final HistoryContext historyContext,
                                                                final HasSpreadsheetDeltaFetcherWatchers hasSpreadsheetDeltaFetcherWatchers) {
        return new BasicSpreadsheetDeltaCellsTableComponentContext(
            Objects.requireNonNull(historyContext, "historyContext"),
            Objects.requireNonNull(hasSpreadsheetDeltaFetcherWatchers, "hasSpreadsheetDeltaFetcherWatchers")
        );
    }

    public BasicSpreadsheetDeltaCellsTableComponentContext(final HistoryContext historyContext,
                                                           final HasSpreadsheetDeltaFetcherWatchers hasSpreadsheetDeltaFetcherWatchers) {
        this.historyContext = historyContext;
        this.hasSpreadsheetDeltaFetcherWatchers = hasSpreadsheetDeltaFetcherWatchers;
    }

    @Override
    public HistoryContext historyContext() {
        return this.historyContext;
    }

    private final HistoryContext historyContext;

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.hasSpreadsheetDeltaFetcherWatchers.addSpreadsheetDeltaFetcherWatcher(watcher);
    }

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcherOnce(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.hasSpreadsheetDeltaFetcherWatchers.addSpreadsheetDeltaFetcherWatcherOnce(watcher);
    }

    private final HasSpreadsheetDeltaFetcherWatchers hasSpreadsheetDeltaFetcherWatchers;
}
