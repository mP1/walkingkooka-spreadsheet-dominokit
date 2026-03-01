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

package walkingkooka.spreadsheet.dominokit;

import org.junit.jupiter.api.Test;
import walkingkooka.currency.CurrencyContextTesting2;
import walkingkooka.currency.CurrencyContexts;
import walkingkooka.locale.LocaleContexts;
import walkingkooka.spreadsheet.dominokit.fetcher.CurrencyFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasCurrencyFetcherWatchers;
import walkingkooka.spreadsheet.server.currency.CurrencyCode;
import walkingkooka.spreadsheet.server.currency.CurrencyHateosResource;
import walkingkooka.spreadsheet.server.currency.CurrencyHateosResourceSet;

import java.util.Currency;
import java.util.Locale;

import static org.junit.Assert.assertThrows;

public final class AppCurrencyContextTest implements CurrencyContextTesting2<AppCurrencyContext> {

    private final static Currency CURRENCY = Currency.getInstance("AUD");

    private final static HasCurrencyFetcherWatchers HAS_CURRENCY_FETCHER_WATCHERS = new HasCurrencyFetcherWatchers() {
        @Override
        public Runnable addCurrencyFetcherWatcher(final CurrencyFetcherWatcher watcher) {
            return () -> {
            };
        }

        @Override
        public Runnable addCurrencyFetcherWatcherOnce(final CurrencyFetcherWatcher watcher) {
            throw new UnsupportedOperationException();
        }
    };

    // with.............................................................................................................

    @Test
    public void testWithNullHasCurrencyFetcherWatchersFails() {
        assertThrows(
            NullPointerException.class,
            () -> AppCurrencyContext.with(
                null,
                CurrencyContexts.fake()
            )
        );
    }

    @Test
    public void testWithNullCurrencyContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> AppCurrencyContext.with(
                HAS_CURRENCY_FETCHER_WATCHERS,
                null
            )
        );
    }

    @Test
    public void testCurrency() {
        this.currencyAndCheck(
            this.createContext(),
            CURRENCY
        );
    }

    @Test
    public void testSetCurrency() {
        final Currency currency = Currency.getInstance("NZD");
        this.checkNotEquals(
            currency,
            CURRENCY
        );

        this.setCurrencyAndCheck(
            this.createContext(),
            currency
        );
    }

    @Test
    public void testCurrencyTextBeforeOnCurrencyHateosResourceSet() {
        this.currencyTextAndCheck(
            this.createContext(),
            CURRENCY
        );
    }

    @Test
    public void testCurrencyTextAfterOnCurrencyHateosResourceSet() {
        final String text = "Custom Australia Currency";

        final AppCurrencyContext context = this.createContext();
        context.onCurrencyHateosResourceSet(
            CurrencyHateosResourceSet.EMPTY.concat(
                CurrencyHateosResource.with(
                    CurrencyCode.fromCurrency(CURRENCY),
                    text
                )
            )
        );

        this.currencyTextAndCheck(
            context,
            CURRENCY,
            text
        );
    }

    @Test
    public void testFindTextAfterOnCurrencyHateosResourceSet() {
        final AppCurrencyContext context = this.createContext();

        final Currency currency = Currency.getInstance("CAD");

        context.onCurrencyHateosResourceSet(
            CurrencyHateosResourceSet.EMPTY.concat(
                CurrencyHateosResource.with(
                    CurrencyCode.fromCurrency(CURRENCY),
                    "Custom Australia Currency"
                )
            ).concat(
                CurrencyHateosResource.with(
                    CurrencyCode.fromCurrency(currency),
                    "Custom Canada Currency"
                )
            ).concat(
                CurrencyHateosResource.with(
                    CurrencyCode.fromCurrency(
                        Currency.getInstance("NZD")
                    ),
                    "Custom New Zealand"
                )
            )
        );

        this.findByCurrencyTextAndCheck(
            context,
            "Custom",
            1,
            1,
            currency
        );
    }

    @Override
    public AppCurrencyContext createContext() {
        final Locale locale = Locale.forLanguageTag("en-AU");

        return AppCurrencyContext.with(
            HAS_CURRENCY_FETCHER_WATCHERS,
            CurrencyContexts.jre(
                Currency.getInstance(locale),
                (Currency from, Currency to) -> {
                    throw new UnsupportedOperationException();
                },
                LocaleContexts.jre(locale)
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<AppCurrencyContext> type() {
        return AppCurrencyContext.class;
    }
}
