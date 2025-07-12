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
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySelectHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

final class AppContextSpreadsheetLocaleDialogComponentContextSpreadsheetMetadataLocale extends AppContextSpreadsheetLocaleDialogComponentContext
    implements HasSpreadsheetMetadataFetcherWatchersDelegator {

    static AppContextSpreadsheetLocaleDialogComponentContextSpreadsheetMetadataLocale with(final AppContext context) {
        return new AppContextSpreadsheetLocaleDialogComponentContextSpreadsheetMetadataLocale(
            Objects.requireNonNull(context, "context")
        );
    }

    private AppContextSpreadsheetLocaleDialogComponentContextSpreadsheetMetadataLocale(final AppContext context) {
        super(context);
    }

    @Override
    public String dialogTitle() {
        return "Spreadsheet Locale";
    }

    @Override
    public Optional<Locale> undoLocale() {
        return this.context.spreadsheetMetadata()
            .get(SpreadsheetMetadataPropertyName.LOCALE);
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

    // ComponentLifecycleMatcher........................................................................................

    // /spreadsheet/1/SpreadsheetName/metadata/RoundingMode
    // /spreadsheet/1/SpreadsheetName/metadata/Locale/save/Locale
    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return (
            token instanceof SpreadsheetMetadataPropertySaveHistoryToken &&
                token.cast(SpreadsheetMetadataPropertySelectHistoryToken.class)
                    .propertyName()
                    .equals(SpreadsheetMetadataPropertyName.LOCALE)
        ) ||
            token instanceof SpreadsheetMetadataPropertySaveHistoryToken;
    }

    // /spreadsheet/1/SpreadsheetName/metadata/Locale/save
    @Override
    public boolean isMatch(HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySelectHistoryToken &&
            token.cast(SpreadsheetMetadataPropertySelectHistoryToken.class)
                .propertyName()
                .equals(SpreadsheetMetadataPropertyName.LOCALE);
    }
}
