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

import walkingkooka.Value;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.locale.LocaleContexts;
import walkingkooka.spreadsheet.server.datetimesymbols.DateTimeSymbolsHateosResource;
import walkingkooka.text.CharSequences;
import walkingkooka.text.HasText;
import walkingkooka.util.HasLocale;

import java.util.Locale;
import java.util.Objects;

public final class SpreadsheetLocaleComponentSuggestionsValue<T> implements HasLocale,
    HasText,
    Value<T>,
    Comparable<SpreadsheetLocaleComponentSuggestionsValue<T>> {

    public static SpreadsheetLocaleComponentSuggestionsValue<DateTimeSymbols> fromDateTimeSymbolsHateosResource(final DateTimeSymbolsHateosResource resource) {
        Objects.requireNonNull(resource, "resource");
        return with(
            Locale.forLanguageTag(
                resource.hateosLinkId()
            ),
            resource.text(),
            resource.value()
        );
    }

    static <T> SpreadsheetLocaleComponentSuggestionsValue<T> with(final Locale locale,
                                                                  final String text,
                                                                  final T value) {
        return new SpreadsheetLocaleComponentSuggestionsValue(
            Objects.requireNonNull(locale, "locale"),
            CharSequences.failIfNullOrEmpty(text, "text"),
            Objects.requireNonNull(value, "value")
        );
    }

    private SpreadsheetLocaleComponentSuggestionsValue(final Locale locale,
                                                       final String text,
                                                       final T value) {
        this.locale = locale;
        this.text = text;
        this.value = value;
    }

    @Override
    public Locale locale() {
        return this.locale;
    }

    private final Locale locale;

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
            this.locale,
            this.text,
            this.value
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            (other instanceof SpreadsheetLocaleComponentSuggestionsValue && this.equals0((SpreadsheetLocaleComponentSuggestionsValue) other));
    }

    private boolean equals0(final SpreadsheetLocaleComponentSuggestionsValue other) {
        return this.locale.equals(other.locale) &&
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
    public int compareTo(final SpreadsheetLocaleComponentSuggestionsValue other) {
        return LocaleContexts.CASE_SENSITIVITY.comparator()
            .compare(
                this.text,
                other.text
            );
    }
}
