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

import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.spreadsheet.dominokit.dialog.FakeDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class FakeLocaleDialogComponentContext extends FakeDialogComponentContext implements LocaleDialogComponentContext {

    public FakeLocaleDialogComponentContext() {
        super();
    }

    @Override
    public String dialogTitle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Locale> undoLocale() {
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

    // LocaleContext....................................................................................................

    @Override
    public Set<Locale> availableLocales() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<DateTimeSymbols> dateTimeSymbolsForLocale(final Locale locale) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<DecimalNumberSymbols> decimalNumberSymbolsForLocale(final Locale locale) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Locale> findByLocaleText(final String text,
                                        final int offset,
                                        final int count) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<String> localeText(final Locale locale) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Locale locale() {
        throw new UnsupportedOperationException();
    }

    @Override
    public LocaleDialogComponentContext setLocale(final Locale locale) {
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
