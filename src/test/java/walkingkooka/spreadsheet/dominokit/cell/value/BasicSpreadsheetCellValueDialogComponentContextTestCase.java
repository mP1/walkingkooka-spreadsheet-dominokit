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

package walkingkooka.spreadsheet.dominokit.cell.value;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContexts;
import walkingkooka.spreadsheet.dominokit.viewport.FakeSpreadsheetViewportCacheContext;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class BasicSpreadsheetCellValueDialogComponentContextTestCase<C extends BasicSpreadsheetCellValueDialogComponentContext<V>, V> implements SpreadsheetCellValueDialogComponentContextTesting<V, C> {

    BasicSpreadsheetCellValueDialogComponentContextTestCase() {
        super();
    }

    final static SpreadsheetDeltaFetcherWatchers DELTA_FETCHER_WATCHERS = SpreadsheetDeltaFetcherWatchers.empty();
    final static HistoryContext HISTORY_CONTEXT = HistoryContexts.fake();
    final static LoggingContext LOGGING_CONTEXT = LoggingContexts.fake();

    // with.............................................................................................................

    @Test
    public final void testWithNullSpreadsheetViewportCacheFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext(
                null,
                DELTA_FETCHER_WATCHERS,
                HISTORY_CONTEXT,
                LOGGING_CONTEXT
            )
        );
    }

    @Test
    public final void testWithNullSpreadsheetDeltaFetcherWatchersFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext(
                this.spreadsheetViewportCache(),
                null,
                HISTORY_CONTEXT,
                LOGGING_CONTEXT
            )
        );
    }

    @Test
    public final void testWithNullHistoryContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext(
                this.spreadsheetViewportCache(),
                DELTA_FETCHER_WATCHERS,
                null,
                LOGGING_CONTEXT
            )
        );
    }

    @Test
    public final void testWithNullLoggingContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext(
                this.spreadsheetViewportCache(),
                DELTA_FETCHER_WATCHERS,
                HISTORY_CONTEXT,
                null
            )
        );
    }

    @Override
    public final void testAddHistoryTokenWatcherOnceWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void testAddHistoryTokenWatcherWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void testPushHistoryTokenWithNullFails() {
        throw new UnsupportedOperationException();
    }

    final void idAndCheck(final String expected) {
        this.checkEquals(
            expected,
            this.createContext().id()
        );
    }

    @Override
    public final C createContext() {
        return this.createContext(
            this.spreadsheetViewportCache(),
            DELTA_FETCHER_WATCHERS,
            HISTORY_CONTEXT,
            LOGGING_CONTEXT
        );
    }

    abstract C createContext(final SpreadsheetViewportCache viewportCache,
                             final HasSpreadsheetDeltaFetcherWatchers deltaFetcherWatchers,
                             final HistoryContext historyContext,
                             final LoggingContext loggingContext);

    final SpreadsheetViewportCache spreadsheetViewportCache() {
        return SpreadsheetViewportCache.empty(
            new FakeSpreadsheetViewportCacheContext() {
                @Override
                public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                    return null;
                }

                @Override
                public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
                    return null;
                }

                @Override
                public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                    return null;
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
