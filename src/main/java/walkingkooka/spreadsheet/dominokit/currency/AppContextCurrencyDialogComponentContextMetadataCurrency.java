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

package walkingkooka.spreadsheet.dominokit.currency;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySelectHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Currency;
import java.util.Optional;

final class AppContextCurrencyDialogComponentContextMetadataCurrency extends AppContextCurrencyDialogComponentContext
    implements HasSpreadsheetMetadataFetcherWatchersDelegator {

    static AppContextCurrencyDialogComponentContextMetadataCurrency with(final AppContext context) {
        return new AppContextCurrencyDialogComponentContextMetadataCurrency(context);
    }

    private AppContextCurrencyDialogComponentContextMetadataCurrency(final AppContext context) {
        super(context);
    }

    private final static SpreadsheetMetadataPropertyName<Currency> PROPERTY_NAME = SpreadsheetMetadataPropertyName.CURRENCY;

    @Override
    public String dialogTitle() {
        return this.spreadsheetMetadataPropertyNameDialogTitle(SpreadsheetMetadataPropertyName.CURRENCY);
    }

    @Override
    public Optional<Currency> undoCurrency() {
        return this.context.spreadsheetMetadata()
            .get(PROPERTY_NAME);
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
    // /spreadsheet/1/SpreadsheetName/metadata/Currency/save/Currency
    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return (
            token instanceof SpreadsheetMetadataPropertySelectHistoryToken &&
                false == PROPERTY_NAME.equals(
                    token.metadataPropertyName()
                        .orElse(null)
                )
        ) ||
            token instanceof SpreadsheetMetadataPropertySaveHistoryToken;
    }

    // /spreadsheet/1/SpreadsheetName/metadata/Currency/save
    @Override
    public boolean isMatch(HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySelectHistoryToken &&
            PROPERTY_NAME.equals(
                token.metadataPropertyName()
                    .orElse(null)
                );
    }
}
