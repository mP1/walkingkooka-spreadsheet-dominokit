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

package walkingkooka.spreadsheet.dominokit.cell;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContexts;
import walkingkooka.spreadsheet.dominokit.viewport.FakeSpreadsheetViewportCacheContext;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellValueDialogComponentContextDateTest implements SpreadsheetCellValueDialogComponentContextTesting<SpreadsheetCellValueDialogComponentContextDate> {

    private final static JsonNodeMarshallContext MARSHALL_CONTEXT = JsonNodeMarshallContexts.basic();
    private final static HistoryContext HISTORY_CONTEXT = HistoryContexts.fake();
    private final static LoggingContext LOGGING_CONTEXT = LoggingContexts.fake();

    // with.............................................................................................................

    @Test
    public void testWithNullSpreadsheetViewportCacheFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetCellValueDialogComponentContextDate.with(
                null,
                MARSHALL_CONTEXT,
                HISTORY_CONTEXT,
                LOGGING_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullMarshallContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetCellValueDialogComponentContextDate.with(
                this.spreadsheetViewportCache(),
                null,
                HISTORY_CONTEXT,
                LOGGING_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullHistoryContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetCellValueDialogComponentContextDate.with(
                this.spreadsheetViewportCache(),
                MARSHALL_CONTEXT,
                null,
                LOGGING_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullLoggingContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetCellValueDialogComponentContextDate.with(
                this.spreadsheetViewportCache(),
                MARSHALL_CONTEXT,
                HISTORY_CONTEXT,
                null
            )
        );
    }

    @Override
    public void testAddHistoryTokenWatcherOnceWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testAddHistoryTokenWatcherWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testPushHistoryTokenWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testPrepareSaveValueWithEmpty() {
        this.prepareSaveValueAndCheck(
            Optional.empty(),
            ""
        );
    }

    @Test
    public void testPrepareSaveValueWithNotEmpty() {
        this.prepareSaveValueAndCheck(
            Optional.of(
                LocalDate.of(
                    1999,
                    12,
                    31
                )
            ),
            "\"1999-12-31\""
        );
    }

    @Override
    public SpreadsheetCellValueDialogComponentContextDate createContext() {
        return SpreadsheetCellValueDialogComponentContextDate.with(
            this.spreadsheetViewportCache(),
            MARSHALL_CONTEXT,
            HISTORY_CONTEXT,
            LOGGING_CONTEXT
        );
    }

    private SpreadsheetViewportCache spreadsheetViewportCache() {
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

    @Override
    public Class<SpreadsheetCellValueDialogComponentContextDate> type() {
        return SpreadsheetCellValueDialogComponentContextDate.class;
    }
}
