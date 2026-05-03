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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.text.FontFamily;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A filter that accepts user query and matches {@link TextStylePropertyComponent}. This is used by {@link walkingkooka.spreadsheet.dominokit.value.textstyle.color.TextStyleColorComponent
 * to support filtering all available {@link TextStylePropertyComponent}.
 */
public final class TextStyleDialogComponentFilter {

    public static TextStyleDialogComponentFilter with(final String text) {
        return new TextStyleDialogComponentFilter(
            Objects.requireNonNull(text, "text")
                .trim()
        );
    }

    private TextStyleDialogComponentFilter(final String text) {
        super();
        this.text = text;
    }

    public boolean testComponent(final TextStylePropertyComponent<?, ?, ?> component) {
        return this.testName(component.name()) ||
            this.testValue(component.value());
    }

    public boolean testName(final TextStylePropertyName<?> propertyName) {
        return this.test(
            propertyName.value()
        );
    }

    public boolean testEnums(final Collection<? extends Enum<?>> enumValues) {
        return enumValues.stream()
            .anyMatch(
                (Enum<?> enumValue) ->
                    null != enumValue ?
                        this.testEnum(enumValue) :
                        this.test("Clear")
            );
    }

    public boolean testEnum(final Enum<?> enumValue) {
        return this.test(enumValue.name());
    }

    public boolean testFontFamilies(final List<FontFamily> fontFamilies) {
        return fontFamilies.stream()
            .anyMatch(this::testFontFamily);
    }

    public boolean testFontFamily(final FontFamily fontFamily) {
        return this.test(
            fontFamily.text()
        );
    }

    /**
     * Tests if the given value matches the filter.
     */
    public boolean testValue(final Optional<?> value) {
        return value.isPresent() &&
            this.test(
                value.get()
                    .toString()
            );
    }

    /**
     * Tests if the given text matches the filter text.
     */
    public boolean test(final String value) {
        final String text = this.text;
        return text.isEmpty() || CASE_SENSITIVITY.contains(
            value,
            text
        );
    }

    private final String text;

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.text;
    }
}
