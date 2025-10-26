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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.compare.ComparableTesting2;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.server.datetimesymbols.DateTimeSymbolsHateosResource;
import walkingkooka.spreadsheet.server.decimalnumbersymbols.DecimalNumberSymbolsHateosResource;
import walkingkooka.text.HasTextTesting;

import java.text.DateFormatSymbols;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class LocaleComponentSuggestionsValueTest implements ComparableTesting2<LocaleComponentSuggestionsValue<Integer>>,
    ClassTesting<LocaleComponentSuggestionsValue<Integer>>,
    HasTextTesting {

    private final static Locale LOCALE = Locale.ENGLISH;
    private final static String TEXT = "English";
    private final static Integer VALUE = 123;

    // fromDateTimeSymbolsHateosResource................................................................................

    @Test
    public void testFromDateTimeSymbolsHateosResourceWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> LocaleComponentSuggestionsValue.fromDateTimeSymbolsHateosResource(null)
        );
    }

    @Test
    public void testFromDateTimeSymbolsHateosResource() {
        final Locale locale = Locale.forLanguageTag("en-AU");
        final DateTimeSymbols symbols = DateTimeSymbols.fromDateFormatSymbols(
            new DateFormatSymbols(locale)
        );

        final LocaleComponentSuggestionsValue<DateTimeSymbols> value = LocaleComponentSuggestionsValue.fromDateTimeSymbolsHateosResource(
            DateTimeSymbolsHateosResource.fromLocale(locale)
        );
        this.checkEquals(
            locale,
            value.locale(),
            "locale"
        );
        this.textAndCheck(
            value,
            "English (Australia)"
        );
        this.checkEquals(
            symbols,
            value.value(),
            "value"
        );
    }

    // fromDecimalNumberSymbolsHateosResource...........................................................................

    @Test
    public void testFromDecimalNumberSymbolsHateosResourceWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> LocaleComponentSuggestionsValue.fromDecimalNumberSymbolsHateosResource(null)
        );
    }

    @Test
    public void testFromDecimalNumberSymbolsHateosResource() {
        final Locale locale = Locale.forLanguageTag("en-AU");
        final DecimalNumberSymbols symbols = DecimalNumberSymbols.fromDecimalFormatSymbols(
            '+',
            new DecimalFormatSymbols(locale)
        );

        final LocaleComponentSuggestionsValue<DecimalNumberSymbols> value = LocaleComponentSuggestionsValue.fromDecimalNumberSymbolsHateosResource(
            DecimalNumberSymbolsHateosResource.fromLocale(locale)
        );
        this.checkEquals(
            locale,
            value.locale(),
            "locale"
        );
        this.textAndCheck(
            value,
            "English (Australia)"
        );
        this.checkEquals(
            symbols,
            value.value(),
            "value"
        );
    }

    // with.............................................................................................................

    @Test
    public void testWithNullLocaleFails() {
        assertThrows(
            NullPointerException.class,
            () -> LocaleComponentSuggestionsValue.with(
                null,
                TEXT,
                VALUE
            )
        );
    }

    @Test
    public void testWithNullTextFails() {
        assertThrows(
            NullPointerException.class,
            () -> LocaleComponentSuggestionsValue.with(
                LOCALE,
                null,
                VALUE
            )
        );
    }

    @Test
    public void testWithEmptyTextFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> LocaleComponentSuggestionsValue.with(
                LOCALE,
                "",
                VALUE
            )
        );
    }

    @Test
    public void testWithNullValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> LocaleComponentSuggestionsValue.with(
                LOCALE,
                TEXT,
                null
            )
        );
    }
    
    // Comparable.......................................................................................................

    @Test
    public void testComparableSort() {
        final Set<LocaleComponentSuggestionsValue<Integer>> values = SortedSets.tree();

        final LocaleComponentSuggestionsValue<Integer> english = this.createComparable();

        final LocaleComponentSuggestionsValue<Integer> au = LocaleComponentSuggestionsValue.with(
            LOCALE,
            "English australia",
            VALUE
        );

        final LocaleComponentSuggestionsValue<Integer> nz = LocaleComponentSuggestionsValue.with(
            Locale.forLanguageTag("en-NZ"),
            "English NEW ZEALAND",
            VALUE
        );

        values.add(english);
        values.add(au);
        values.add(nz);

        this.checkEquals(
            Lists.of(
                english,
                au,
                nz
            ),
            values.stream()
                .collect(Collectors.toList())
        );
    }

    @Override
    public LocaleComponentSuggestionsValue<Integer> createComparable() {
        return LocaleComponentSuggestionsValue.with(
            LOCALE,
            TEXT,
            VALUE
        );
    }

    // class............................................................................................................

    @Override
    public Class<LocaleComponentSuggestionsValue<Integer>> type() {
        return Cast.to(LocaleComponentSuggestionsValue.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
