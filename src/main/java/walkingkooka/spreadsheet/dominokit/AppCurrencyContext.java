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

import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.ImmutableSortedSet;
import walkingkooka.collect.set.Sets;
import walkingkooka.currency.CurrencyContext;
import walkingkooka.currency.CurrencyContextDelegator;
import walkingkooka.currency.CurrencyContexts;
import walkingkooka.spreadsheet.dominokit.currency.CurrencyComponent;
import walkingkooka.spreadsheet.dominokit.fetcher.CurrencyFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasCurrencyFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.server.currency.CurrencyCode;
import walkingkooka.spreadsheet.server.currency.CurrencyHateosResource;
import walkingkooka.spreadsheet.server.currency.CurrencyHateosResourceSet;

import java.util.Currency;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

final class AppCurrencyContext implements CurrencyContextDelegator,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher,
    CurrencyFetcherWatcher {

    static AppCurrencyContext with(final HasCurrencyFetcherWatchers hasCurrencyFetcherWatchers,
                                   final CurrencyContext currencyContext) {
        return new AppCurrencyContext(
            Objects.requireNonNull(hasCurrencyFetcherWatchers, "hasCurrencyFetcherWatchers"),
            Objects.requireNonNull(currencyContext, "currencyContext")
        );
    }

    private AppCurrencyContext(final HasCurrencyFetcherWatchers watchers,
                               final CurrencyContext currencyContext) {
        super();
        watchers.addCurrencyFetcherWatcher(this);

        this.currencyContext = currencyContext;
    }

    // CurrencyContext....................................................................................................

    @Override
    public Set<Currency> findByCurrencyText(final String text,
                                            final int offset,
                                            final int count) {
        Objects.requireNonNull(text, "text");
        if (offset < 0) {
            throw new IllegalArgumentException("Invalid offset < 0");
        }
        if (count < 0) {
            throw new IllegalArgumentException("Invalid count < 0");
        }

        return this.currencyToText.entrySet()
            .stream()
            .filter(currencyAndText -> {
                final String currencyText = currencyAndText.getValue();
                return false == currencyText.isEmpty() &&
                    (CurrencyContexts.CASE_SENSITIVITY.equals(text, currencyText) || CurrencyContexts.CASE_SENSITIVITY.startsWith(currencyText, text));
            }).skip(offset)
            .limit(count)
            .map(Entry::getKey)
            .collect(
                ImmutableSortedSet.collector(CurrencyContexts.CURRENCY_CODE_COMPARATOR)
            );
    }

    @Override
    public Optional<String> currencyText(final Currency currency) {
        Objects.requireNonNull(currency, "currency");

        return Optional.ofNullable(
            this.currencyToText.get(currency)
        );
    }

    /**
     * This may be updated externally.
     */
    // @VisibleForTesting
    Map<Currency, String> currencyToText = Maps.empty();

    // CurrencyContext....................................................................................................

    @Override
    public CurrencyContext currencyContext() {
        return this.currencyContext;
    }

    private CurrencyContext currencyContext;

    // CurrencyFetcherWatcher.............................................................................................

    @Override
    public void onCurrencyHateosResource(final CurrencyCode currencyCode,
                                         final CurrencyHateosResource currency) {
        // NOP
    }

    /**
     * Save the loaded currencys. These will appear in the {@link CurrencyComponent}.
     */
    @Override
    public void onCurrencyHateosResourceSet(final CurrencyHateosResourceSet currencys) {
        final Set<Currency> availableCurrencys = Sets.hash();
        final Map<Currency, String> currencyToText = Maps.sorted(CurrencyContexts.CURRENCY_CODE_COMPARATOR);

        for (final CurrencyHateosResource currencyHateosResource : currencys) {
            final Currency currency = this.currencyForCurrencyCode(
                currencyHateosResource.value()
                    .value() // currencyCode
            ).orElse(null);

            if (null != currency) {
                availableCurrencys.add(currency);

                currencyToText.put(
                    currency,
                    currencyHateosResource.text()
                );
            }
        }

        this.currencyToText = currencyToText;
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.currencyContext.toString();
    }
}
