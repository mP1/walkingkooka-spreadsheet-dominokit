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

package walkingkooka.spreadsheet.dominokit.value.textstyle.overflow;

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponentDelegator;
import walkingkooka.tree.text.Overflow;
import walkingkooka.tree.text.TextStylePropertyName;

public final class OverflowComponent implements TextStylePropertyEnumComponentDelegator<Overflow, OverflowComponent> {

    public static OverflowComponent overflowX(final String idPrefix,
                                              final OverflowComponentContext context) {
        return new OverflowComponent(
            idPrefix,
            TextStylePropertyName.OVERFLOW_X,
            context
        );
    }

    public static OverflowComponent overflowY(final String idPrefix,
                                              final OverflowComponentContext context) {
        return new OverflowComponent(
            idPrefix,
            TextStylePropertyName.OVERFLOW_Y,
            context
        );
    }

    private static OverflowComponent with(final String idPrefix,
                                          final TextStylePropertyName<Overflow> propertyName,
                                          final OverflowComponentContext context) {
        return new OverflowComponent(
            idPrefix,
            propertyName,
            context
        );
    }

    private OverflowComponent(final String idPrefix,
                              final TextStylePropertyName<Overflow> propertyName,
                              final OverflowComponentContext context) {
        super();
        this.component = TextStylePropertyEnumComponent.with(
            idPrefix,
            propertyName,
            Lists.of(
                null,
                Overflow.VISIBLE,
                Overflow.HIDDEN,
                Overflow.SCROLL,
                Overflow.AUTO
            ),
            TextStylePropertyEnumComponent.valueToText(),
            TextStylePropertyEnumComponent.noIcons(),
            TextStylePropertyComponent.TEXT_STYLE_PROPERTY_FILTER_KINDS_OVERFLOW,
            context // TextStylePropertyEnumComponentContext
        );
    }

    @Override
    public TextStylePropertyEnumComponent<Overflow> textStylePropertyNameEnumHistoryTokenAnchorListComponent() {
        return this.component;
    }

    private final TextStylePropertyEnumComponent<Overflow> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
