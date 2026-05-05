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
import walkingkooka.tree.text.OverflowWrap;
import walkingkooka.tree.text.TextStylePropertyName;

public final class OverflowWrapComponent implements TextStylePropertyEnumComponentDelegator<OverflowWrap, OverflowWrapComponent> {

    public static OverflowWrapComponent with(final String idPrefix,
                                             final OverflowWrapComponentContext context) {
        return new OverflowWrapComponent(
            idPrefix,
            context
        );
    }

    private OverflowWrapComponent(final String idPrefix,
                                  final OverflowWrapComponentContext context) {
        super();
        this.component = TextStylePropertyEnumComponent.with(
            idPrefix,
            TextStylePropertyName.OVERFLOW_WRAP,
            Lists.of(
                null,
                OverflowWrap.NORMAL,
                OverflowWrap.ANYWHERE,
                OverflowWrap.BREAK_WORD
            ),
            TextStylePropertyEnumComponent.valueToText(),
            TextStylePropertyEnumComponent.noIcons(),
            TextStylePropertyComponent.TEXT_STYLE_PROPERTY_FILTER_KINDS_OVERFLOW,
            context // TextStylePropertyEnumComponentContext
        );
    }

    @Override
    public TextStylePropertyEnumComponent<OverflowWrap> textStylePropertyEnumComponent() {
        return this.component;
    }

    private final TextStylePropertyEnumComponent<OverflowWrap> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
