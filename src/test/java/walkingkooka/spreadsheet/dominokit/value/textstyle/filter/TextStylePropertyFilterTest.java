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

package walkingkooka.spreadsheet.dominokit.value.textstyle.filter;

import org.junit.jupiter.api.Test;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.opacity.OpacityComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.FontFamily;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextStylePropertyFilterTest implements HashCodeEqualsDefinedTesting2<TextStylePropertyFilter>,
    ToStringTesting<TextStylePropertyFilter>,
    ClassTesting<TextStylePropertyFilter> {

    // with.............................................................................................................

    @Test
    public void testWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> TextStylePropertyFilter.with(null)
        );
    }

    @Test
    public void testWithEmpty() {
        assertSame(
            TextStylePropertyFilter.ALL,
            TextStylePropertyFilter.with("")
        );
    }

    @Test
    public void testWithWhitespaceOnly() {
        assertSame(
            TextStylePropertyFilter.ALL,
            TextStylePropertyFilter.with("   ")
        );
    }

    // add..............................................................................................................

    @Test
    public void testAddWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> TextStylePropertyFilter.ALL.add(null)
        );
    }

    @Test
    public void testAddWithNew() {
        this.addAndCheck(
            "",
            TextStylePropertyFilterKind.TEXT,
            "TEXT"
        );
    }

    @Test
    public void testAddWithNewNotEmpty() {
        this.addAndCheck(
            "hello",
            TextStylePropertyFilterKind.TEXT,
            "hello TEXT"
        );
    }

    @Test
    public void testAddWithNewNotEmptyEndsWithSpace() {
        this.addAndCheck(
            "hello ",
            TextStylePropertyFilterKind.TEXT,
            "hello TEXT"
        );
    }

    @Test
    public void testAddWithOldDifferentCase() {
        this.addAndCheck(
            "text",
            TextStylePropertyFilterKind.TEXT,
            "text"
        );
    }

    @Test
    public void testAddWithOld() {
        this.addAndCheck(
            "TEXT",
            TextStylePropertyFilterKind.TEXT,
            "TEXT"
        );
    }

    @Test
    public void testAddWithOld2() {
        this.addAndCheck(
            "TEXT hello",
            TextStylePropertyFilterKind.TEXT,
            "TEXT hello"
        );
    }

    private void addAndCheck(final String filter,
                             final TextStylePropertyFilterKind kind,
                             final String expected) {
        this.addAndCheck(
            TextStylePropertyFilter.with(filter),
            kind,
            TextStylePropertyFilter.with(expected)
        );
    }

    private void addAndCheck(final TextStylePropertyFilter filter,
                             final TextStylePropertyFilterKind kind,
                             final TextStylePropertyFilter expected) {
        if(filter.equals(expected)) {
            assertSame(
                expected,
                expected.add(kind)
            );
        } else {
            this.checkEquals(
                expected,
                filter.add(kind)
            );
        }
    }

    // contains.........................................................................................................

    @Test
    public void testContainsAll() {
        for (final TextStylePropertyFilterKind kind : TextStylePropertyFilterKind.values()) {
            this.containsAndCheck(
                TextStylePropertyFilter.ALL,
                kind,
                false
            );
        }
    }

    @Test
    public void testContainsOnlyLowerCase() {
        this.containsAndCheck(
            "text",
            TextStylePropertyFilterKind.TEXT,
            true
        );
    }

    @Test
    public void testContainsOnlyUpperCase() {
        this.containsAndCheck(
            "TEXT",
            TextStylePropertyFilterKind.TEXT,
            true
        );
    }

    @Test
    public void testContainsMultipleTokens() {
        this.containsAndCheck(
            "1 TEXT",
            TextStylePropertyFilterKind.TEXT,
            true
        );
    }

    private void containsAndCheck(final String filter,
                                  final TextStylePropertyFilterKind kind,
                                  final boolean expected) {
        this.containsAndCheck(
            TextStylePropertyFilter.with(filter),
            kind,
            expected
        );
    }

    private void containsAndCheck(final TextStylePropertyFilter filter,
                                  final TextStylePropertyFilterKind kind,
                                  final boolean expected) {
        this.checkEquals(
            expected,
            filter.contains(kind),
            () -> kind + " contains " + kind
        );
    }

    // testTextStylePropertyComponent...................................................................................

    private final static String ID_PREFIX = "TestIdPrefix123-";

    @Test
    public void testTestComponentWithName() {
        this.testComponentAndCheck(
            TextStylePropertyFilter.with("CITY"),
            OpacityComponent.with(
                ID_PREFIX
            ),
            true
        );
    }

    @Test
    public void testTestComponentWithName2ndToken() {
        this.testComponentAndCheck(
            TextStylePropertyFilter.with("FIRST CITY"),
            OpacityComponent.with(
                ID_PREFIX
            ),
            true
        );
    }

    @Test
    public void testTestComponentWithValue() {
        this.testComponentAndCheck(
            TextStylePropertyFilter.with("50"),
            OpacityComponent.with(
                ID_PREFIX
            ).setStringValue(
                Optional.of("75")
            ),
            false
        );
    }

    @Test
    public void testTestComponentFalse() {
        this.testComponentAndCheck(
            TextStylePropertyFilter.with("Bad"),
            OpacityComponent.with(
                ID_PREFIX
            ).setStringValue(
                Optional.of("50")
            ),
            false
        );
    }

    @Test
    public void testTestAll() {
        this.testComponentAndCheck(
            TextStylePropertyFilter.with("   "),
            OpacityComponent.with(
                ID_PREFIX
            ),
            true
        );
    }

    private void testComponentAndCheck(final TextStylePropertyFilter filter,
                                       final TextStylePropertyComponent<?, ?, ?> component,
                                       final boolean expected) {
        this.checkEquals(
            expected,
            filter.testComponent(component),
            () -> filter + " " + component
        );
    }

    // testName.........................................................................................................

    @Test
    public void testTestNameMatch() {
        this.testNameAndCheck(
            TextStylePropertyFilter.with("COL"),
            TextStylePropertyName.BACKGROUND_COLOR,
            true
        );
    }

    @Test
    public void testTestNameMismatch() {
        this.testNameAndCheck(
            TextStylePropertyFilter.with("MIS"),
            TextStylePropertyName.BACKGROUND_COLOR,
            false
        );
    }

    private void testNameAndCheck(final TextStylePropertyFilter filter,
                                  final TextStylePropertyName<?> name,
                                  final boolean expected) {
        this.checkEquals(
            expected,
            filter.testName(name),
            () -> filter + " " + name
        );
    }

    // testValue........................................................................................................

    @Test
    public void testTestValueMatch() {
        this.testValueAndCheck(
            TextStylePropertyFilter.with("Lo"),
            Optional.of("Hello"),
            true
        );
    }

    @Test
    public void testTestValueMismatch() {
        this.testValueAndCheck(
            TextStylePropertyFilter.with("NO"),
            Optional.of("Hello"),
            false
        );
    }

    @Test
    public void testTestValueWithNoValue() {
        this.testValueAndCheck(
            TextStylePropertyFilter.with("NO"),
            Optional.empty(),
            false
        );
    }

    private void testValueAndCheck(final TextStylePropertyFilter filter,
                                   final Optional<?> value,
                                   final boolean expected) {
        this.checkEquals(
            expected,
            filter.testValue(value),
            () -> filter + " " + value
        );
    }

    // testEnums........................................................................................................

    @Test
    public void testTestEnumsIncludesNull() {
        this.testEnumsAndCheck(
            TextStylePropertyFilter.with("Cle"),
            Lists.of(
                null,
                TextAlign.LEFT
            ),
            true
        );
    }

    @Test
    public void testTestEnumsMatch() {
        this.testEnumsAndCheck(
            TextStylePropertyFilter.with("LEft"),
            Lists.of(
                TextAlign.values()
            ),
            true
        );
    }

    @Test
    public void testTestEnumsMismatch() {
        this.testEnumsAndCheck(
            TextStylePropertyFilter.with("NO"),
            Lists.of(
                TextAlign.values()
            ),
            false
        );
    }

    private void testEnumsAndCheck(final TextStylePropertyFilter filter,
                                   final Collection<Enum<?>> enumValues,
                                   final boolean expected) {
        this.checkEquals(
            expected,
            filter.testEnums(enumValues),
            () -> filter + " " + enumValues
        );
    }

    // testEnum.........................................................................................................

    @Test
    public void testTestEnumMatch() {
        this.testEnumAndCheck(
            TextStylePropertyFilter.with("LEft"),
            TextAlign.LEFT,
            true
        );
    }

    @Test
    public void testTestEnumMismatch() {
        this.testEnumAndCheck(
            TextStylePropertyFilter.with("NO"),
            TextAlign.RIGHT,
            false
        );
    }

    private void testEnumAndCheck(final TextStylePropertyFilter filter,
                                  final Enum<?> enumValue,
                                  final boolean expected) {
        this.checkEquals(
            expected,
            filter.testEnum(enumValue),
            () -> filter + " " + enumValue
        );
    }

    // testFontFamilies.................................................................................................

    @Test
    public void testTestFontFamiliesMatch() {
        this.testFontFamiliesAndCheck(
            TextStylePropertyFilter.with("Times New Roman"),
            Lists.of(
                FontFamily.with("Courier"),
                FontFamily.with("Times New Roman")
            ),
            true
        );
    }

    @Test
    public void testTestFontFamiliesMismatch() {
        this.testFontFamiliesAndCheck(
            TextStylePropertyFilter.with("Not"),
            Lists.of(
                FontFamily.with("Courier"),
                FontFamily.with("Times New Roman")
            ),
            false
        );
    }

    private void testFontFamiliesAndCheck(final TextStylePropertyFilter filter,
                                          final List<FontFamily> fontFamilies,
                                          final boolean expected) {
        this.checkEquals(
            expected,
            filter.testFontFamilies(fontFamilies),
            () -> filter + " " + fontFamilies
        );
    }

    // testFontFamily...................................................................................................

    @Test
    public void testTestFontFamilyMatch() {
        this.testFontFamilyAndCheck(
            TextStylePropertyFilter.with("Times New Roman"),
            FontFamily.with("Times New Roman"),
            true
        );
    }

    @Test
    public void testTestFontFamilyMismatch() {
        this.testFontFamilyAndCheck(
            TextStylePropertyFilter.with("Not"),
            FontFamily.with("Courier"),
            false
        );
    }

    private void testFontFamilyAndCheck(final TextStylePropertyFilter filter,
                                        final FontFamily fontFamily,
                                        final boolean expected) {
        this.checkEquals(
            expected,
            filter.testFontFamily(fontFamily),
            () -> filter + " " + fontFamily
        );
    }

    // test.............................................................................................................

    @Test
    public void testTestMatch() {
        this.testAndCheck(
            TextStylePropertyFilter.with("Apple"),
            "APPle",
            true
        );
    }

    @Test
    public void testTestMatchSecondToken() {
        this.testAndCheck(
            TextStylePropertyFilter.with("Apple Bana"),
            "Banana",
            true
        );
    }

    @Test
    public void testTestMatchSecondTokenExtraWhitespace() {
        this.testAndCheck(
            TextStylePropertyFilter.with(" Apple  nana "),
            "Banana",
            true
        );
    }

    @Test
    public void testTestMismatch() {
        this.testAndCheck(
            TextStylePropertyFilter.with("Not"),
            "Banana",
            false
        );
    }

    private void testAndCheck(final TextStylePropertyFilter filter,
                              final String text,
                              final boolean expected) {
        this.checkEquals(
            expected,
            filter.test(text),
            () -> filter + " " + CharSequences.quoteAndEscape(text)
        );
    }

    // hashCode/equals..................................................................................................

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(
            TextStylePropertyFilter.with("DifferentFilter")
        );
    }

    @Test
    public void testEqualsDifferentCase() {
        this.checkEquals(
            TextStylePropertyFilter.with(FILTER_TEXT.toUpperCase())
        );
    }

    @Test
    public void testEqualsExtraWhitespace() {
        this.checkNotEquals(
            TextStylePropertyFilter.with(FILTER_TEXT + " ")
        );
    }

    private final static String FILTER_TEXT = "Filter123";

    @Override
    public TextStylePropertyFilter createObject() {
        return TextStylePropertyFilter.with(FILTER_TEXT);
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            TextStylePropertyFilter.with("Filter123"),
            "Filter123"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TextStylePropertyFilter> type() {
        return TextStylePropertyFilter.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
