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

import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.text.CaseKind;
import walkingkooka.validation.ValueTypeName;

import java.util.Objects;
import java.util.Optional;

final class BasicSpreadsheetCellValueDialogComponentContext<T> implements SpreadsheetCellValueDialogComponentContext<T>,
    HistoryContextDelegator,
    LoggingContextDelegator {

    static <T> BasicSpreadsheetCellValueDialogComponentContext<T> with(final ValueTypeName valueType,
                                                                       final SpreadsheetViewportCache viewportCache,
                                                                       final HasSpreadsheetDeltaFetcherWatchers deltaFetcherWatchers,
                                                                       final HistoryContext historyContext,
                                                                       final LoggingContext loggingContext) {
        return new BasicSpreadsheetCellValueDialogComponentContext<>(
            Objects.requireNonNull(valueType, "valueType"),
            Objects.requireNonNull(viewportCache, "viewportCache"),
            Objects.requireNonNull(deltaFetcherWatchers, "deltaFetcherWatchers"),
            Objects.requireNonNull(historyContext, "historyContext"),
            Objects.requireNonNull(loggingContext, "loggingContext")
        );
    }

    private BasicSpreadsheetCellValueDialogComponentContext(final ValueTypeName valueType,
                                                            final SpreadsheetViewportCache viewportCache,
                                                            final HasSpreadsheetDeltaFetcherWatchers deltaFetcherWatchers,
                                                            final HistoryContext historyContext,
                                                            final LoggingContext loggingContext) {
        super();

        this.valueType = valueType;
        this.viewportCache = viewportCache;
        this.deltaFetcherWatchers = deltaFetcherWatchers;
        this.historyContext = historyContext;
        this.loggingContext = loggingContext;
    }

    @Override
    public String id() {
        return SpreadsheetCell.class.getSimpleName() + "Value";
    }

    @Override
    public String dialogTitle() {
        return this.selectionDialogTitle(
            CaseKind.CAMEL.change(
                this.valueType()
                    .text(),
                CaseKind.TITLE
            )
        );
    }

    @Override
    public Optional<SpreadsheetCell> cell() {
        return this.viewportCache.historyTokenCell();
    }

    private final SpreadsheetViewportCache viewportCache;

    @Override
    public ValueTypeName valueType() {
        return this.valueType;
    }

    private final ValueTypeName valueType;

    // HistoryContextDelegator..........................................................................................

    @Override
    public HistoryContext historyContext() {
        return this.historyContext;
    }

    private final HistoryContext historyContext;

    // LoggingContextDelegator..........................................................................................

    @Override
    public LoggingContext loggingContext() {
        return this.loggingContext;
    }

    private final LoggingContext loggingContext;

    // SpreadsheetDeltaFetcherWatchers..................................................................................

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.deltaFetcherWatchers.addSpreadsheetDeltaFetcherWatcher(watcher);
    }

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcherOnce(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.deltaFetcherWatchers.addSpreadsheetDeltaFetcherWatcherOnce(watcher);
    }

    private final HasSpreadsheetDeltaFetcherWatchers deltaFetcherWatchers;

    // toString.........................................................................................................

    @Override
    public String toString() {
        return this.historyContext.toString();
    }
}
