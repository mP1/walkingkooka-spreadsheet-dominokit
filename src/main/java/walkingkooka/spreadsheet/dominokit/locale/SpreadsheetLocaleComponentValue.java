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

import walkingkooka.locale.LocaleContexts;
import walkingkooka.text.HasText;
import walkingkooka.util.HasLocale;

import java.util.Locale;
import java.util.Objects;

final class SpreadsheetLocaleComponentValue implements HasLocale,
    HasText,
    Comparable<SpreadsheetLocaleComponentValue> {

    static SpreadsheetLocaleComponentValue with(final Locale locale,
                                                final String text) {
        return new SpreadsheetLocaleComponentValue(
            locale,
            text
        );
    }

    private SpreadsheetLocaleComponentValue(final Locale locale,
                                            final String text) {
        this.locale = locale;
        this.text = text;
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

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
            this.locale,
            this.text
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            (other instanceof SpreadsheetLocaleComponentValue && this.equals0((SpreadsheetLocaleComponentValue) other));
    }

    private boolean equals0(final SpreadsheetLocaleComponentValue other) {
        return this.locale.equals(other.locale) ||
            this.text.equals(other.text);
    }

    @Override
    public String toString() {
        return this.text;
    }

    private final String text;

    // Comparable.......................................................................................................

    @Override
    public int compareTo(final SpreadsheetLocaleComponentValue other) {
        return LocaleContexts.LANGUAGE_TAG_COMPARATOR.compare(
            this.locale,
            other.locale
        );
    }
}
