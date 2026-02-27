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

import walkingkooka.Value;
import walkingkooka.currency.CurrencyContexts;
import walkingkooka.currency.HasCurrency;
import walkingkooka.text.CharSequences;
import walkingkooka.text.HasText;

import java.util.Currency;
import java.util.Objects;

/**
 * Represents a single {@link Currency} that will appear within a {@link walkingkooka.spreadsheet.dominokit.suggestbox.SuggestBoxComponent}.
 */
public final class CurrencyComponentSuggestionsValue<T> implements HasCurrency,
    HasText,
    Value<T>,
    Comparable<CurrencyComponentSuggestionsValue<T>> {

    static <T> CurrencyComponentSuggestionsValue<T> with(final Currency currency,
                                                         final String text,
                                                         final T value) {
        return new CurrencyComponentSuggestionsValue<>(
            Objects.requireNonNull(currency, "currency"),
            CharSequences.failIfNullOrEmpty(text, "text"),
            Objects.requireNonNull(value, "value")
        );
    }

    private CurrencyComponentSuggestionsValue(final Currency currency,
                                              final String text,
                                              final T value) {
        this.currency = currency;
        this.text = text;
        this.value = value;
    }

    // HasCurrency........................................................................................................

    @Override
    public Currency currency() {
        return this.currency;
    }

    private final Currency currency;

    // HasText..........................................................................................................

    @Override
    public String text() {
        return this.text;
    }

    // Value............................................................................................................

    @Override
    public T value() {
        return this.value;
    }

    private final T value;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
            this.currency,
            this.text,
            this.value
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            (other instanceof CurrencyComponentSuggestionsValue && this.equals0((CurrencyComponentSuggestionsValue<?>) other));
    }

    private boolean equals0(final CurrencyComponentSuggestionsValue<?> other) {
        return this.currency.equals(other.currency) &&
            this.text.equals(other.text) &&
            this.value.equals(other.value);
    }

    @Override
    public String toString() {
        return this.text;
    }

    private final String text;

    // Comparable.......................................................................................................

    @Override
    public int compareTo(final CurrencyComponentSuggestionsValue other) {
        return CurrencyContexts.CASE_SENSITIVITY.comparator()
            .compare(
                this.text,
                other.text
            );
    }
}
