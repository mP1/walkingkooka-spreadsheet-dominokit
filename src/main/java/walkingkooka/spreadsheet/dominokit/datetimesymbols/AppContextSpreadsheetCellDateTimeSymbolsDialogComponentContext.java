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

package walkingkooka.spreadsheet.dominokit.datetimesymbols;

import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellDateTimeSymbolsHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellDateTimeSymbolsSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellDateTimeSymbolsSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Objects;
import java.util.Optional;

final class AppContextSpreadsheetCellDateTimeSymbolsDialogComponentContext implements DateTimeSymbolsDialogComponentContext,
    HistoryContextDelegator,
    LoggingContextDelegator {

    static AppContextSpreadsheetCellDateTimeSymbolsDialogComponentContext with(final AppContext context) {
        return new AppContextSpreadsheetCellDateTimeSymbolsDialogComponentContext(
            Objects.requireNonNull(context, "context")
        );
    }

    private AppContextSpreadsheetCellDateTimeSymbolsDialogComponentContext(final AppContext context) {
        super();
        this.context = context;
    }

    @Override
    public String dialogTitle() {
        return DateTimeSymbols.class.getSimpleName();
    }

    @Override
    public Optional<DateTimeSymbols> copyDateTimeSymbols() {
        return this.context.spreadsheetMetadata()
            .get(SpreadsheetMetadataPropertyName.DATE_TIME_SYMBOLS);
    }

    @Override
    public Optional<DateTimeSymbols> loadDateTimeSymbols() {
        final AppContext context = this.context;

        return context.spreadsheetViewportCache()
            .cell(
                context.historyToken()
                    .selection()
                    .get()
            ).flatMap(SpreadsheetCell::dateTimeSymbols);
    }

    @Override
    public void save(final Optional<DateTimeSymbols> symbols) {
        final AppContext context = this.context;
        context.spreadsheetDeltaFetcher()
            .patchDateTimeSymbols(
                context.spreadsheetMetadata()
                    .getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_ID),
                context.historyToken()
                    .cast(SpreadsheetCellDateTimeSymbolsHistoryToken.class)
                    .selection()
                    .get(),
                symbols
            );
    }

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.context.addSpreadsheetDeltaFetcherWatcher(watcher);
    }

    @Override
    public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.context.addSpreadsheetMetadataFetcherWatcher(watcher);
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetCellDateTimeSymbolsSaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellDateTimeSymbolsSelectHistoryToken;
    }

    @Override
    public HistoryContext historyContext() {
        return this.context;
    }

    @Override
    public LoggingContext loggingContext() {
        return this.context;
    }

    private final AppContext context;

    @Override
    public String toString() {
        return this.context.toString();
    }
}
