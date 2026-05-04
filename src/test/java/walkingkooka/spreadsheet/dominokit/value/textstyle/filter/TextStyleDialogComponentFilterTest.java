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

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextStyleDialogComponentFilterTest implements ClassTesting<TextStyleDialogComponentFilter>,
    ToStringTesting<TextStyleDialogComponentFilter> {

    // with.............................................................................................................

    @Test
    public void testWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> TextStyleDialogComponentFilter.with(null)
        );
    }

    // testTextStylePropertyComponent...................................................................................

    private final static String ID_PREFIX = "TestIdPrefix123-";

    @Test
    public void testTestComponentWithName() {
        this.testComponentAndCheck(
            TextStyleDialogComponentFilter.with("CITY"),
            OpacityComponent.with(
                ID_PREFIX
            ),
            true
        );
    }

    @Test
    public void testTestComponentWithValue() {
        this.testComponentAndCheck(
            TextStyleDialogComponentFilter.with("50"),
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
            TextStyleDialogComponentFilter.with("Bad"),
            OpacityComponent.with(
                ID_PREFIX
            ).setStringValue(
                Optional.of("50")
            ),
            false
        );
    }

    private void testComponentAndCheck(final TextStyleDialogComponentFilter filter,
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
            TextStyleDialogComponentFilter.with("COL"),
            TextStylePropertyName.BACKGROUND_COLOR,
            true
        );
    }

    @Test
    public void testTestNameMismatch() {
        this.testNameAndCheck(
            TextStyleDialogComponentFilter.with("MIS"),
            TextStylePropertyName.BACKGROUND_COLOR,
            false
        );
    }

    private void testNameAndCheck(final TextStyleDialogComponentFilter filter,
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
            TextStyleDialogComponentFilter.with("Lo"),
            Optional.of("Hello"),
            true
        );
    }

    @Test
    public void testTestValueMismatch() {
        this.testValueAndCheck(
            TextStyleDialogComponentFilter.with("NO"),
            Optional.of("Hello"),
            false
        );
    }

    @Test
    public void testTestValueWithNoValue() {
        this.testValueAndCheck(
            TextStyleDialogComponentFilter.with("NO"),
            Optional.empty(),
            false
        );
    }

    private void testValueAndCheck(final TextStyleDialogComponentFilter filter,
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
            TextStyleDialogComponentFilter.with("Cle"),
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
            TextStyleDialogComponentFilter.with("LEft"),
            Lists.of(
                TextAlign.values()
            ),
            true
        );
    }

    @Test
    public void testTestEnumsMismatch() {
        this.testEnumsAndCheck(
            TextStyleDialogComponentFilter.with("NO"),
            Lists.of(
                TextAlign.values()
            ),
            false
        );
    }

    private void testEnumsAndCheck(final TextStyleDialogComponentFilter filter,
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
            TextStyleDialogComponentFilter.with("LEft"),
            TextAlign.LEFT,
            true
        );
    }

    @Test
    public void testTestEnumMismatch() {
        this.testEnumAndCheck(
            TextStyleDialogComponentFilter.with("NO"),
            TextAlign.RIGHT,
            false
        );
    }

    private void testEnumAndCheck(final TextStyleDialogComponentFilter filter,
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
            TextStyleDialogComponentFilter.with("Times New Roman"),
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
            TextStyleDialogComponentFilter.with("Not"),
            Lists.of(
                FontFamily.with("Courier"),
                FontFamily.with("Times New Roman")
            ),
            false
        );
    }

    private void testFontFamiliesAndCheck(final TextStyleDialogComponentFilter filter,
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
            TextStyleDialogComponentFilter.with("Times New Roman"),
            FontFamily.with("Times New Roman"),
            true
        );
    }

    @Test
    public void testTestFontFamilyMismatch() {
        this.testFontFamilyAndCheck(
            TextStyleDialogComponentFilter.with("Not"),
            FontFamily.with("Courier"),
            false
        );
    }

    private void testFontFamilyAndCheck(final TextStyleDialogComponentFilter filter,
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
            TextStyleDialogComponentFilter.with("Apple"),
            "APPle",
            true
        );
    }

    @Test
    public void testTestMismatch() {
        this.testAndCheck(
            TextStyleDialogComponentFilter.with("Not"),
            "Banana",
            false
        );
    }

    private void testAndCheck(final TextStyleDialogComponentFilter filter,
                              final String text,
                              final boolean expected) {
        this.checkEquals(
            expected,
            filter.test(text),
            () -> filter + " " + CharSequences.quoteAndEscape(text)
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            TextStyleDialogComponentFilter.with("Filter123"),
            "Filter123"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TextStyleDialogComponentFilter> type() {
        return TextStyleDialogComponentFilter.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
