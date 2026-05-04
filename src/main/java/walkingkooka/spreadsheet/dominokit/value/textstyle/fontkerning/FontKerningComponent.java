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

package walkingkooka.spreadsheet.dominokit.value.textstyle.fontkerning;

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponentDelegator;
import walkingkooka.tree.text.FontKerning;
import walkingkooka.tree.text.TextStylePropertyName;

public final class FontKerningComponent implements TextStylePropertyEnumComponentDelegator<FontKerning, FontKerningComponent> {

    public static FontKerningComponent with(final String idPrefix,
                                            final FontKerningComponentContext context) {
        return new FontKerningComponent(
            idPrefix,
            context
        );
    }

    private FontKerningComponent(final String idPrefix,
                                 final FontKerningComponentContext context) {
        super();
        this.component = TextStylePropertyEnumComponent.with(
            idPrefix,
            TextStylePropertyName.FONT_KERNING,
            Lists.of(
                null,
                FontKerning.AUTO,
                FontKerning.NONE,
                FontKerning.NORMAL
            ),
            TextStylePropertyEnumComponent.valueToText(),
            TextStylePropertyEnumComponent.noIcons(),
            TEXT_STYLE_PROPERTY_FILTER_KINDS_FONT,
            context // TextStylePropertyEnumComponentContext
        );
    }

    @Override
    public TextStylePropertyEnumComponent<FontKerning> textStylePropertyNameEnumHistoryTokenAnchorListComponent() {
        return this.component;
    }

    private final TextStylePropertyEnumComponent<FontKerning> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
