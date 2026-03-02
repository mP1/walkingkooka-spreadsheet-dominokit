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

import walkingkooka.spreadsheet.dominokit.dialog.FakeDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class FakeCurrencyDialogComponentContext extends FakeDialogComponentContext implements CurrencyDialogComponentContext {

    public FakeCurrencyDialogComponentContext() {
        super();
    }

    @Override
    public String dialogTitle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Currency> undoCurrency() {
        throw new UnsupportedOperationException();
    }

    // HasSpreadsheetDeltaWatcher.......................................................................................

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcherOnce(final SpreadsheetDeltaFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    // HasSpreadsheetMetadataFetcherWatchers............................................................................

    @Override
    public Runnable addSpreadsheetMetadataFetcherWatcherOnce(final SpreadsheetMetadataFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    // CurrencyContext..................................................................................................

    @Override
    public Currency currency() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCurrency(final Currency currency) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Currency> availableCurrencies() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Currency> currencyForCurrencyCode(final String currencyCode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<String> currencyText(final Currency currency) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Currency> currencyForLocale(final Locale locale) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Locale> localesForCurrencyCode(final String currencyCode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Currency> findByCurrencyText(final String text,
                                            final int offset,
                                            final int count) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Number exchangeRate(final Currency from,
                               final Currency to,
                               final Optional<LocalDateTime> dateTime) {
        throw new UnsupportedOperationException();
    }

    // LifeCycleMatcher.................................................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isMatch(HistoryToken token) {
        throw new UnsupportedOperationException();
    }
}
