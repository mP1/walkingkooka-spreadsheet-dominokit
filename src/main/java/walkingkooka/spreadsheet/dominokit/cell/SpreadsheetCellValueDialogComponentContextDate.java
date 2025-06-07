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

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

final class SpreadsheetCellValueDialogComponentContextDate implements SpreadsheetCellValueDialogComponentContext<LocalDate>,
    HistoryContextDelegator,
    LoggingContextDelegator {

    static SpreadsheetCellValueDialogComponentContextDate with(final SpreadsheetViewportCache viewportCache,
                                                               final JsonNodeMarshallContext marshallContext,
                                                               final HistoryContext historyContext,
                                                               final LoggingContext loggingContext) {
        return new SpreadsheetCellValueDialogComponentContextDate(
            Objects.requireNonNull(viewportCache, "viewportCache"),
            Objects.requireNonNull(marshallContext, "marshallContext"),
            Objects.requireNonNull(historyContext, "historyContext"),
            Objects.requireNonNull(loggingContext, "loggingContext")
        );
    }

    private SpreadsheetCellValueDialogComponentContextDate(final SpreadsheetViewportCache viewportCache,
                                                           final JsonNodeMarshallContext marshallContext,
                                                           final HistoryContext historyContext,
                                                           final LoggingContext loggingContext) {
        this.viewportCache = viewportCache;
        this.marshallContext = marshallContext;
        this.historyContext = historyContext;
        this.loggingContext = loggingContext;
    }

    @Override
    public String id() {
        return ID;
    }

    private final static String ID = "cellValueDate";

    @Override
    public String dialogTitle() {
        return "Date";
    }

    @Override
    public boolean isMatch(final ValidationValueTypeName valueType) {
        return ValidationValueTypeName.DATE.equals(valueType);
    }

    @Override
    public Optional<LocalDate> value() {
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
    public String prepareSaveValue(final Optional<LocalDate> value) {
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

    // toString.........................................................................................................

    @Override
    public String toString() {
        return this.historyContext.toString();
    }
}
