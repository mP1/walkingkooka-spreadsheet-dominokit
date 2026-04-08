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

package walkingkooka.spreadsheet.dominokit.textstyle;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellStyleHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellStyleSaveHistoryToken;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

/**
 * A {@link TextStyleDialogComponentContext} that only matches:
 * <pre>
 * // without {@link TextStylePropertyName}
 *
 * /1/SpreadsheetName1/cell/A1/style
 * </pre>
 */
final class AppContextTextStyleDialogComponentContextCellStyle extends AppContextTextStyleDialogComponentContext
    implements HasSpreadsheetDeltaFetcherWatchersDelegator {

    static AppContextTextStyleDialogComponentContextCellStyle with(final AppContext context) {
        return new AppContextTextStyleDialogComponentContextCellStyle(context);
    }

    private AppContextTextStyleDialogComponentContextCellStyle(final AppContext context) {
        super(context);
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetCellStyleSaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellStyleHistoryToken && token.stylePropertyName().isEmpty();
    }

    @Override
    public Optional<TextStyle> undo() {
        return this.context.spreadsheetViewportCache()
            .historyTokenCell()
            .map(SpreadsheetCell::style);
    }

    @Override
    public String dialogTitle() {
        return this.selectionTextStylePropertyDialogTitle(
            TextStylePropertyName.WILDCARD
        );
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
}
