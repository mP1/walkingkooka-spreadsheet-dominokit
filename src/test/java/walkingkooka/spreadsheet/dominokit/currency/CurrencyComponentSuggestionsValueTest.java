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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.compare.ComparableTesting2;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.HasTextTesting;

import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CurrencyComponentSuggestionsValueTest implements ComparableTesting2<CurrencyComponentSuggestionsValue<Integer>>,
    ClassTesting<CurrencyComponentSuggestionsValue<Integer>>,
    HasTextTesting {

    private final static Currency CURRENCY = Currency.getInstance("AUD");
    private final static String TEXT = "English";
    private final static Integer VALUE = 123;

    // with.............................................................................................................

    @Test
    public void testWithNullCurrencyFails() {
        assertThrows(
            NullPointerException.class,
            () -> CurrencyComponentSuggestionsValue.with(
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
            () -> CurrencyComponentSuggestionsValue.with(
                CURRENCY,
                null,
                VALUE
            )
        );
    }

    @Test
    public void testWithEmptyTextFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> CurrencyComponentSuggestionsValue.with(
                CURRENCY,
                "",
                VALUE
            )
        );
    }

    @Test
    public void testWithNullValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> CurrencyComponentSuggestionsValue.with(
                CURRENCY,
                TEXT,
                null
            )
        );
    }
    
    // Comparable.......................................................................................................

    @Test
    public void testComparableSort() {
        final Set<CurrencyComponentSuggestionsValue<Integer>> values = SortedSets.tree();

        final CurrencyComponentSuggestionsValue<Integer> english = this.createComparable();

        final CurrencyComponentSuggestionsValue<Integer> au = CurrencyComponentSuggestionsValue.with(
            CURRENCY,
            "English australia",
            VALUE
        );

        final CurrencyComponentSuggestionsValue<Integer> nz = CurrencyComponentSuggestionsValue.with(
            Currency.getInstance("NZD"),
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
    public CurrencyComponentSuggestionsValue<Integer> createComparable() {
        return CurrencyComponentSuggestionsValue.with(
            CURRENCY,
            TEXT,
            VALUE
        );
    }

    // class............................................................................................................

    @Override
    public Class<CurrencyComponentSuggestionsValue<Integer>> type() {
        return Cast.to(CurrencyComponentSuggestionsValue.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
