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

package walkingkooka.spreadsheet.dominokit.locale;

import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellLocaleSelectHistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

final class AppContextSpreadsheetLocaleDialogComponentContextCellLocale extends AppContextSpreadsheetLocaleDialogComponentContext
    implements HasSpreadsheetDeltaFetcherWatchersDelegator {

    static AppContextSpreadsheetLocaleDialogComponentContextCellLocale with(final AppContext context) {
        return new AppContextSpreadsheetLocaleDialogComponentContextCellLocale(
            Objects.requireNonNull(context, "context")
        );
    }

    private AppContextSpreadsheetLocaleDialogComponentContextCellLocale(final AppContext context) {
        super(context);
    }

    @Override
    public String dialogTitle() {
        return "Cell " + this.historyContext()
            .historyToken()
            .selection()
            .get() +
            " Locale";
    }

    @Override
    public Optional<Locale> undoLocale() {
        Locale locale = null;

        final AppContext context = this.context;
        final SpreadsheetSelection cell = context.historyToken()
            .selection()
            .orElse(null);
        if (null != cell) {
            locale = context.spreadsheetViewportCache()
                .cell(cell)
                .flatMap(SpreadsheetCell::locale)
                .orElse(null);
        }

        return Optional.ofNullable(locale);
    }

    // HasSpreadsheetDelta..............................................................................................

    @Override
    public HasSpreadsheetDeltaFetcher hasSpreadsheetDeltaFetcherWatchers() {
        return this.context;
    }

    // HasSpreadsheetMetadataFetcherWatchers............................................................................

    @Override
    public Runnable addSpreadsheetMetadataFetcherWatcherOnce(final SpreadsheetMetadataFetcherWatcher watcher) {
        // NOP
        return null;
    }

    @Override
    public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        // NOP
        return null;
    }

    // ComponentLifecycleMatcher........................................................................................

    // /spreadsheet/1/SpreadsheetName/cell/A1/Locale/delete
    // /spreadsheet/1/SpreadsheetName/cell/A1/Locale/save/Locale
    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return false == this.isMatch(token);
    }

    // /spreadsheet/1/SpreadsheetName/cell/A1/Locale/save
    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellLocaleSelectHistoryToken;
    }
}
