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

import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class SpreadsheetCellValueDialogComponentContexts implements PublicStaticHelper {

    /**
     * {@see SpreadsheetCellValueDialogComponentContextDate}
     */
    public static SpreadsheetCellValueDialogComponentContext<LocalDate> date(final SpreadsheetViewportCache viewportCache,
                                                                             final HasSpreadsheetDeltaFetcherWatchers deltaFetcherWatchers,
                                                                             final JsonNodeMarshallContext marshallContext,
                                                                             final HistoryContext historyContext,
                                                                             final LoggingContext loggingContext) {
        return SpreadsheetCellValueDialogComponentContextDate.with(
            viewportCache,
            deltaFetcherWatchers,
            marshallContext,
            historyContext,
            loggingContext
        );
    }

    /**
     * {@see SpreadsheetCellValueDialogComponentContextDateTime}
     */
    public static SpreadsheetCellValueDialogComponentContext<LocalDateTime> dateTime(final SpreadsheetViewportCache viewportCache,
                                                                                     final HasSpreadsheetDeltaFetcherWatchers deltaFetcherWatchers,
                                                                                     final JsonNodeMarshallContext marshallContext,
                                                                                     final HistoryContext historyContext,
                                                                                     final LoggingContext loggingContext) {
        return SpreadsheetCellValueDialogComponentContextDateTime.with(
            viewportCache,
            deltaFetcherWatchers,
            marshallContext,
            historyContext,
            loggingContext
        );
    }

    /**
     * {@see SpreadsheetCellValueDialogComponentContextText}
     */
    public static SpreadsheetCellValueDialogComponentContext<String> text(final SpreadsheetViewportCache viewportCache,
                                                                          final HasSpreadsheetDeltaFetcherWatchers deltaFetcherWatchers,
                                                                          final JsonNodeMarshallContext marshallContext,
                                                                          final HistoryContext historyContext,
                                                                          final LoggingContext loggingContext) {
        return SpreadsheetCellValueDialogComponentContextText.with(
            viewportCache,
            deltaFetcherWatchers,
            marshallContext,
            historyContext,
            loggingContext
        );
    }

    /**
     * {@see SpreadsheetCellValueDialogComponentContextTime}
     */
    public static SpreadsheetCellValueDialogComponentContext<LocalTime> time(final SpreadsheetViewportCache viewportCache,
                                                                             final HasSpreadsheetDeltaFetcherWatchers deltaFetcherWatchers,
                                                                             final JsonNodeMarshallContext marshallContext,
                                                                             final HistoryContext historyContext,
                                                                             final LoggingContext loggingContext) {
        return SpreadsheetCellValueDialogComponentContextTime.with(
            viewportCache,
            deltaFetcherWatchers,
            marshallContext,
            historyContext,
            loggingContext
        );
    }

    /**
     * {@see FakeSpreadsheetCellValueDialogComponentContext}
     */
    public static <T> SpreadsheetCellValueDialogComponentContext<T> fake() {
        return new FakeSpreadsheetCellValueDialogComponentContext<>();
    }

    /**
     * Stop creation
     */
    private SpreadsheetCellValueDialogComponentContexts() {
        throw new UnsupportedOperationException();
    }
}
