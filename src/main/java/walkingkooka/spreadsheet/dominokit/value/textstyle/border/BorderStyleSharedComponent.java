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

package walkingkooka.spreadsheet.dominokit.value.textstyle.border;

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponentDelegator;
import walkingkooka.tree.text.BorderStyle;

abstract class BorderStyleSharedComponent<C extends BorderStyleSharedComponent<C>> implements TextStylePropertyEnumComponentDelegator<BorderStyle, C> {

    BorderStyleSharedComponent(final String idPrefix,
                               final BorderStyleSharedComponentContext context) {
        super();
        this.component = TextStylePropertyEnumComponent.with(
            idPrefix,
            this.name(),
            Lists.of(
                null,
                BorderStyle.NONE,
                BorderStyle.HIDDEN,
                BorderStyle.DOTTED,
                BorderStyle.DASHED,
                BorderStyle.SOLID,
                BorderStyle.DOUBLE,
                BorderStyle.GROOVE,
                BorderStyle.RIDGE,
                BorderStyle.INSET,
                BorderStyle.OUTSET
            ),
            TextStylePropertyEnumComponent.valueToText(),
            TextStylePropertyEnumComponent.noIcons(),
            TEXT_STYLE_PROPERTY_FILTER_KINDS_BORDER,
            context // TextStylePropertyEnumComponentContext
        );
    }

    @Override
    public final TextStylePropertyEnumComponent<BorderStyle> textStylePropertyEnumComponent() {
        return this.component;
    }

    private final TextStylePropertyEnumComponent<BorderStyle> component;

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.component.toString();
    }
}
