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
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetLocaleComponentSuggestionsValueTest implements ComparableTesting2<SpreadsheetLocaleComponentSuggestionsValue<Integer>>,
    ClassTesting<SpreadsheetLocaleComponentSuggestionsValue<Integer>> {

    private final static Locale LOCALE = Locale.ENGLISH;
    private final static String TEXT = "English";
    private final static Integer VALUE = 123;
    
    // with.............................................................................................................

    @Test
    public void testWithNullLocaleFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetLocaleComponentSuggestionsValue.with(
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
            () -> SpreadsheetLocaleComponentSuggestionsValue.with(
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
            () -> SpreadsheetLocaleComponentSuggestionsValue.with(
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
            () -> SpreadsheetLocaleComponentSuggestionsValue.with(
                LOCALE,
                TEXT,
                null
            )
        );
    }
    
    // Comparable.......................................................................................................

    @Test
    public void testComparableSort() {
        final Set<SpreadsheetLocaleComponentSuggestionsValue<Integer>> values = SortedSets.tree();

        final SpreadsheetLocaleComponentSuggestionsValue<Integer> english = this.createComparable();

        final SpreadsheetLocaleComponentSuggestionsValue<Integer> au = SpreadsheetLocaleComponentSuggestionsValue.with(
            LOCALE,
            "English australia",
            VALUE
        );

        final SpreadsheetLocaleComponentSuggestionsValue<Integer> nz = SpreadsheetLocaleComponentSuggestionsValue.with(
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
    public SpreadsheetLocaleComponentSuggestionsValue<Integer> createComparable() {
        return SpreadsheetLocaleComponentSuggestionsValue.with(
            LOCALE,
            TEXT,
            VALUE
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetLocaleComponentSuggestionsValue<Integer>> type() {
        return Cast.to(SpreadsheetLocaleComponentSuggestionsValue.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
