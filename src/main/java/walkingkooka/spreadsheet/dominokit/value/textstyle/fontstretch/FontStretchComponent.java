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

package walkingkooka.spreadsheet.dominokit.value.textstyle.fontstretch;

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponentDelegator;
import walkingkooka.tree.text.FontStretch;
import walkingkooka.tree.text.TextStylePropertyName;

public final class FontStretchComponent implements TextStylePropertyEnumComponentDelegator<FontStretch, FontStretchComponent> {

    public static FontStretchComponent with(final String idPrefix,
                                            final FontStretchComponentContext context) {
        return new FontStretchComponent(
            idPrefix,
            context
        );
    }

    private FontStretchComponent(final String idPrefix,
                                 final FontStretchComponentContext context) {
        super();
        this.component = TextStylePropertyEnumComponent.with(
            idPrefix,
            TextStylePropertyName.FONT_STRETCH,
            Lists.of(
                null,
                FontStretch.ULTRA_CONDENSED,
                FontStretch.EXTRA_CONDENSED,
                FontStretch.CONDENSED,
                FontStretch.SEMI_CONDENSED,
                FontStretch.NORMAL,
                FontStretch.SEMI_EXPANDED,
                FontStretch.EXPANDED,
                FontStretch.EXTRA_EXPANDED,
                FontStretch.ULTRA_EXPANDED
            ),
            TextStylePropertyEnumComponent.valueToText(),
            TextStylePropertyEnumComponent.noIcons(),
            TEXT_STYLE_PROPERTY_FILTER_KINDS_FONT,
            context // TextStylePropertyEnumComponentContext
        );
    }

    @Override
    public TextStylePropertyEnumComponent<FontStretch> textStylePropertyEnumComponent() {
        return this.component;
    }

    private final TextStylePropertyEnumComponent<FontStretch> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
