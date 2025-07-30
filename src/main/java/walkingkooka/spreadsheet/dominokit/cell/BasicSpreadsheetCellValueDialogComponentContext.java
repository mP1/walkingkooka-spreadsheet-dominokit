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

import walkingkooka.Cast;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.text.CaseKind;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.Printers;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;

import java.util.Objects;
import java.util.Optional;

abstract class BasicSpreadsheetCellValueDialogComponentContext<T> implements SpreadsheetCellValueDialogComponentContext<T>,
    HistoryContextDelegator,
    LoggingContextDelegator {

    BasicSpreadsheetCellValueDialogComponentContext(final SpreadsheetViewportCache viewportCache,
                                                    final HasSpreadsheetDeltaFetcherWatchers deltaFetcherWatchers,
                                                    final JsonNodeMarshallContext marshallContext,
                                                    final HistoryContext historyContext,
                                                    final LoggingContext loggingContext) {
        this.viewportCache = viewportCache;
        this.deltaFetcherWatchers = deltaFetcherWatchers;
        this.marshallContext = marshallContext;
        this.historyContext = historyContext;
        this.loggingContext = loggingContext;
    }

    @Override
    public final String dialogTitle() {
        return this.selectionDialogTitle(
            CaseKind.CAMEL.change(
                this.valueType()
                    .text(),
                CaseKind.TITLE
            )
        );
    }

    @Override
    public final Optional<T> value() {
        return this.historyContext.historyToken()
            .selection()
            .flatMap(this.viewportCache::cell)
            .flatMap((SpreadsheetCell cell) -> Cast.to(
                cell.formula()
                    .value())
            );
    }

    final SpreadsheetViewportCache viewportCache;

    @Override
    public final String toHistoryTokenSaveStringValue(final Optional<T> value) {
        Objects.requireNonNull(value, "value");

        String string = "";

        if (value.isPresent()) {
            final StringBuilder b = new StringBuilder();
            try (final IndentingPrinter printer = Printers.stringBuilder(b, LineEnding.NONE).indenting(Indentation.EMPTY)) {
                this.marshallContext.marshall(
                    value.orElse(null)
                ).printJson(printer);

                printer.flush();
            }

            string = b.toString();
        }

        return string;
    }

    final JsonNodeMarshallContext marshallContext;

    // HistoryContextDelegator..........................................................................................

    @Override
    public final HistoryContext historyContext() {
        return this.historyContext;
    }

    final HistoryContext historyContext;

    // LoggingContextDelegator..........................................................................................

    @Override
    public final LoggingContext loggingContext() {
        return this.loggingContext;
    }

    final LoggingContext loggingContext;

    // SpreadsheetDeltaFetcherWatchers..................................................................................

    @Override
    public final Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.deltaFetcherWatchers.addSpreadsheetDeltaFetcherWatcher(watcher);
    }

    @Override
    public final Runnable addSpreadsheetDeltaFetcherWatcherOnce(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.deltaFetcherWatchers.addSpreadsheetDeltaFetcherWatcherOnce(watcher);
    }

    final HasSpreadsheetDeltaFetcherWatchers deltaFetcherWatchers;

    // toString.........................................................................................................

    @Override
    public final String toString() {
        return this.historyContext.toString();
    }
}
