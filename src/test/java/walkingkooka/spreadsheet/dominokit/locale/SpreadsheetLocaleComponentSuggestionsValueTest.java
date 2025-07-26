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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.compare.ComparableTesting2;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public final class SpreadsheetLocaleComponentSuggestionsValueTest implements ComparableTesting2<SpreadsheetLocaleComponentSuggestionsValue>,
    ClassTesting<SpreadsheetLocaleComponentSuggestionsValue> {

    // Comparable.......................................................................................................

    @Test
    public void testComparableSort() {
        final Set<SpreadsheetLocaleComponentSuggestionsValue> values = SortedSets.tree();

        final SpreadsheetLocaleComponentSuggestionsValue english = this.createComparable();

        final SpreadsheetLocaleComponentSuggestionsValue au = SpreadsheetLocaleComponentSuggestionsValue.with(
            Locale.ENGLISH,
            "English australia"
        );

        final SpreadsheetLocaleComponentSuggestionsValue nz = SpreadsheetLocaleComponentSuggestionsValue.with(
            Locale.forLanguageTag("en-NZ"),
            "English NEW ZEALAND"
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
    public SpreadsheetLocaleComponentSuggestionsValue createComparable() {
        return SpreadsheetLocaleComponentSuggestionsValue.with(
            Locale.ENGLISH,
            "English"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetLocaleComponentSuggestionsValue> type() {
        return SpreadsheetLocaleComponentSuggestionsValue.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
