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

package walkingkooka.spreadsheet.dominokit.value.textstyle.outline;

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponentDelegator;
import walkingkooka.tree.text.OutlineStyle;
import walkingkooka.tree.text.TextStylePropertyName;

public final class OutlineStyleComponent implements TextStylePropertyEnumComponentDelegator<OutlineStyle, OutlineStyleComponent> {

    public static OutlineStyleComponent with(final String idPrefix,
                                             final OutlineStyleComponentContext context) {
        return new OutlineStyleComponent(
            idPrefix,
            context
        );
    }

    private OutlineStyleComponent(final String idPrefix,
                                  final OutlineStyleComponentContext context) {
        super();
        this.component = TextStylePropertyEnumComponent.with(
            idPrefix,
            TextStylePropertyName.OUTLINE_STYLE,
            Lists.of(
                null,
                OutlineStyle.NONE,
                OutlineStyle.HIDDEN,
                OutlineStyle.DOTTED,
                OutlineStyle.DASHED,
                OutlineStyle.SOLID,
                OutlineStyle.DOUBLE,
                OutlineStyle.GROOVE,
                OutlineStyle.RIDGE,
                OutlineStyle.INSET,
                OutlineStyle.OUTSET
            ),
            TextStylePropertyEnumComponent.valueToText(),
            TextStylePropertyEnumComponent.noIcons(),
            TEXT_STYLE_PROPERTY_FILTER_KINDS_TEXT,
            context // TextStylePropertyEnumComponentContext
        );
    }

    @Override
    public TextStylePropertyEnumComponent<OutlineStyle> textStylePropertyEnumComponent() {
        return this.component;
    }

    private final TextStylePropertyEnumComponent<OutlineStyle> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
