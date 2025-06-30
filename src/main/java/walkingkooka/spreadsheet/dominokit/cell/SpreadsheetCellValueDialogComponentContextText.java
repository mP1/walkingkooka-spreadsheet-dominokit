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
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.Printers;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.validation.ValidationValueTypeName;

import java.util.Objects;
import java.util.Optional;

final class SpreadsheetCellValueDialogComponentContextText implements SpreadsheetCellValueDialogComponentContext<String>,
    HistoryContextDelegator,
    LoggingContextDelegator {

    static SpreadsheetCellValueDialogComponentContextText with(final SpreadsheetViewportCache viewportCache,
                                                               final HasSpreadsheetDeltaFetcherWatchers deltaFetcherWatchers,
                                                               final JsonNodeMarshallContext marshallContext,
                                                               final HistoryContext historyContext,
                                                               final LoggingContext loggingContext) {
        return new SpreadsheetCellValueDialogComponentContextText(
            Objects.requireNonNull(viewportCache, "viewportCache"),
            Objects.requireNonNull(deltaFetcherWatchers, "deltaFetcherWatchers"),
            Objects.requireNonNull(marshallContext, "marshallContext"),
            Objects.requireNonNull(historyContext, "historyContext"),
            Objects.requireNonNull(loggingContext, "loggingContext")
        );
    }

    private SpreadsheetCellValueDialogComponentContextText(final SpreadsheetViewportCache viewportCache,
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
    public String id() {
        return ID;
    }

    private final static String ID = "cellValueText";

    @Override
    public String dialogTitle() {
        return "Text";
    }

    @Override
    public ValidationValueTypeName valueType() {
        return ValidationValueTypeName.TEXT;
    }

    @Override
    public Optional<String> value() {
        return this.historyContext.historyToken()
            .selection()
            .flatMap(this.viewportCache::cell)
            .flatMap((SpreadsheetCell cell) -> Cast.to(
                cell.formula()
                    .value())
            );
    }

    private final SpreadsheetViewportCache viewportCache;

    @Override
    public String toHistoryTokenSaveStringValue(final Optional<String> value) {
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

    private final JsonNodeMarshallContext marshallContext;

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
