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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Objects;
import java.util.Optional;

public class AppContextSpreadsheetMetadataPropertyDateTimeSymbolsDialogComponentContext implements DateTimeSymbolsDialogComponentContext,
    HistoryContextDelegator,
    LoggingContextDelegator {

    static AppContextSpreadsheetMetadataPropertyDateTimeSymbolsDialogComponentContext with(final AppContext context) {
        return new AppContextSpreadsheetMetadataPropertyDateTimeSymbolsDialogComponentContext(
            Objects.requireNonNull(context, "context")
        );
    }

    private AppContextSpreadsheetMetadataPropertyDateTimeSymbolsDialogComponentContext(final AppContext context) {
        super();
        this.context = context;
    }

    @Override
    public String dialogTitle() {
        return SpreadsheetDialogComponentContext.spreadsheetMetadataPropertyNameDialogTitle(SpreadsheetMetadataPropertyName.DATE_TIME_SYMBOLS);
    }

    /**
     * There is no source to copy when editing the {@link SpreadsheetMetadata}.
     */
    // TODO support copy values using SpreadsheetMetadataPropertyName#LOCALE
    @Override
    public Optional<DateTimeSymbols> copyDateTimeSymbols() {
        return Optional.empty();
    }

    @Override
    public Optional<DateTimeSymbols> loadDateTimeSymbols() {
        return this.context.spreadsheetMetadata()
            .get(SpreadsheetMetadataPropertyName.DATE_TIME_SYMBOLS);
    }

    @Override
    public void save(final Optional<DateTimeSymbols> symbols) {
        final AppContext context = this.context;
        context.spreadsheetMetadataFetcher()
            .patchMetadata(
                context.spreadsheetMetadata()
                    .getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_ID),
                SpreadsheetMetadata.EMPTY.setOrRemove(
                    SpreadsheetMetadataPropertyName.DATE_TIME_SYMBOLS,
                    symbols.orElse(null)
                )
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
        return token instanceof SpreadsheetMetadataPropertySaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySelectHistoryToken &&
            token.cast(SpreadsheetMetadataPropertySelectHistoryToken.class).propertyName() == SpreadsheetMetadataPropertyName.DATE_TIME_SYMBOLS;
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
