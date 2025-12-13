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

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellLocaleSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellLocaleSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellLocaleUnselectHistoryToken;
import walkingkooka.spreadsheet.value.SpreadsheetCell;

import java.util.Locale;
import java.util.Optional;

final class AppContextLocaleDialogComponentContextCellLocale extends AppContextLocaleDialogComponentContext
    implements HasSpreadsheetDeltaFetcherWatchersDelegator {

    static AppContextLocaleDialogComponentContextCellLocale with(final AppContext context) {
        return new AppContextLocaleDialogComponentContextCellLocale(context);
    }

    private AppContextLocaleDialogComponentContextCellLocale(final AppContext context) {
        super(context);
    }

    @Override
    public String dialogTitle() {
        return this.selectionValueDialogTitle(Locale.class);
    }

    @Override
    public Optional<Locale> undoLocale() {
        return this.context.spreadsheetViewportCache()
            .historyTokenCell()
            .flatMap(SpreadsheetCell::locale);
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

    // /spreadsheet/1/SpreadsheetName/cell/A1/Locale/toolbar
    // /spreadsheet/1/SpreadsheetName/cell/A1/Locale/save/Locale
    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetCellLocaleSaveHistoryToken ||
            token instanceof SpreadsheetCellLocaleUnselectHistoryToken;
    }

    // /spreadsheet/1/SpreadsheetName/cell/A1/Locale/save
    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellLocaleSelectHistoryToken;
    }
}
