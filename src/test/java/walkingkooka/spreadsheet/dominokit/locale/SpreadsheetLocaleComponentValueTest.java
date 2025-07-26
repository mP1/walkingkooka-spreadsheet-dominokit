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

public final class SpreadsheetLocaleComponentValueTest implements ComparableTesting2<SpreadsheetLocaleComponentValue>,
    ClassTesting<SpreadsheetLocaleComponentValue> {

    // Comparable.......................................................................................................

    @Test
    public void testComparableSort() {
        final Set<SpreadsheetLocaleComponentValue> values = SortedSets.tree();

        final SpreadsheetLocaleComponentValue english = this.createComparable();

        final SpreadsheetLocaleComponentValue au = SpreadsheetLocaleComponentValue.with(
            Locale.ENGLISH,
            "English australia"
        );

        final SpreadsheetLocaleComponentValue nz = SpreadsheetLocaleComponentValue.with(
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
    public SpreadsheetLocaleComponentValue createComparable() {
        return SpreadsheetLocaleComponentValue.with(
            Locale.ENGLISH,
            "English"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetLocaleComponentValue> type() {
        return SpreadsheetLocaleComponentValue.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
