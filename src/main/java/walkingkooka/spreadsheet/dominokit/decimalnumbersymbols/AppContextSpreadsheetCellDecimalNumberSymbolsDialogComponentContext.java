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

package walkingkooka.spreadsheet.dominokit.decimalnumbersymbols;

import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellDecimalNumberSymbolsHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellDecimalNumberSymbolsSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellDecimalNumberSymbolsSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Objects;
import java.util.Optional;

final class AppContextSpreadsheetCellDecimalNumberSymbolsDialogComponentContext implements DecimalNumberSymbolsDialogComponentContext,
    HistoryContextDelegator,
    LoggingContextDelegator {

    static AppContextSpreadsheetCellDecimalNumberSymbolsDialogComponentContext with(final AppContext context) {
        return new AppContextSpreadsheetCellDecimalNumberSymbolsDialogComponentContext(
            Objects.requireNonNull(context, "context")
        );
    }

    private AppContextSpreadsheetCellDecimalNumberSymbolsDialogComponentContext(final AppContext context) {
        super();
        this.context = context;
    }

    @Override
    public String dialogTitle() {
        return DecimalNumberSymbols.class.getSimpleName();
    }

    @Override
    public Optional<DecimalNumberSymbols> copyDecimalNumberSymbols() {
        return this.context.spreadsheetMetadata()
            .get(SpreadsheetMetadataPropertyName.DECIMAL_NUMBER_SYMBOLS);
    }

    @Override
    public Optional<DecimalNumberSymbols> loadDecimalNumberSymbols() {
        final AppContext context = this.context;

        return context.spreadsheetViewportCache()
            .cell(
                context.historyToken()
                    .selection()
                    .get()
            ).flatMap(SpreadsheetCell::decimalNumberSymbols);
    }

    @Override
    public void save(final Optional<DecimalNumberSymbols> symbols) {
        final AppContext context = this.context;
        context.spreadsheetDeltaFetcher()
            .patchDecimalNumberSymbols(
                context.spreadsheetMetadata()
                    .getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_ID),
                context.historyToken()
                    .cast(SpreadsheetCellDecimalNumberSymbolsHistoryToken.class)
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
        return token instanceof SpreadsheetCellDecimalNumberSymbolsSaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellDecimalNumberSymbolsSelectHistoryToken;
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
