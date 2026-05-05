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

package walkingkooka.spreadsheet.dominokit.value.textstyle.fontvariant;

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponentDelegator;
import walkingkooka.tree.text.FontVariant;
import walkingkooka.tree.text.TextStylePropertyName;

public final class FontVariantComponent implements TextStylePropertyEnumComponentDelegator<FontVariant, FontVariantComponent> {

    public static FontVariantComponent with(final String idPrefix,
                                            final FontVariantComponentContext context) {
        return new FontVariantComponent(
            idPrefix,
            context
        );
    }

    private FontVariantComponent(final String idPrefix,
                                 final FontVariantComponentContext context) {
        super();
        this.component = TextStylePropertyEnumComponent.with(
            idPrefix,
            TextStylePropertyName.FONT_VARIANT,
            Lists.of(
                null,
                FontVariant.INITIAL,
                FontVariant.NORMAL,
                FontVariant.SMALL_CAPS
            ),
            TextStylePropertyEnumComponent.valueToText(),
            TextStylePropertyEnumComponent.noIcons(),
            TextStylePropertyComponent.TEXT_STYLE_PROPERTY_FILTER_KINDS_FONT,
            context // TextStylePropertyEnumComponentContext
        );
    }

    @Override
    public TextStylePropertyEnumComponent<FontVariant> textStylePropertyEnumComponent() {
        return this.component;
    }

    private final TextStylePropertyEnumComponent<FontVariant> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
