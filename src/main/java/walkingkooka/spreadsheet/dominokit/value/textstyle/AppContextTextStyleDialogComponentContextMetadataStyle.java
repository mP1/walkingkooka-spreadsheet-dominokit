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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertyStyleSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertyStyleSelectHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

/**
 * A {@link TextStyleDialogComponentContext} that only matches:
 * <pre>
 * // without {@link TextStylePropertyName}
 *
 * /1/SpreadsheetName1/metadata/style
 * /1/SpreadsheetName1/metadata/style/*
 * /1/SpreadsheetName1/metadata/style/{@link TextStylePropertyName}
 * </pre>
 */
final class AppContextTextStyleDialogComponentContextMetadataStyle extends AppContextTextStyleDialogComponentContext
    implements HasSpreadsheetMetadataFetcherWatchersDelegator {

    static AppContextTextStyleDialogComponentContextMetadataStyle with(final AppContext context) {
        return new AppContextTextStyleDialogComponentContextMetadataStyle(context);
    }

    private AppContextTextStyleDialogComponentContextMetadataStyle(final AppContext context) {
        super(context);
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertyStyleSaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertyStyleSelectHistoryToken;
    }

    @Override
    public Optional<TextStyle> undo() {
        return this.context.spreadsheetMetadata()
            .getIgnoringDefaults(SpreadsheetMetadataPropertyName.STYLE);
    }

    @Override
    public String dialogTitle() {
        return this.spreadsheetMetadataPropertyNameDialogTitle(
            SpreadsheetMetadataPropertyName.STYLE
        );
    }

    // HasSpreadsheetDeltaFetcherWatchers...............................................................................

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        return null;
    }

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcherOnce(final SpreadsheetDeltaFetcherWatcher watcher) {
        return null;
    }

    // HasSpreadsheetMetadataFetcherWatchersDelegator...................................................................

    @Override
    public HasSpreadsheetMetadataFetcherWatchers hasSpreadsheetMetadataFetcherWatchers() {
        return this.context;
    }
}
