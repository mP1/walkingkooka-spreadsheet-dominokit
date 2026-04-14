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
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyNameEnumHistoryTokenAnchorListComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegator;
import walkingkooka.tree.text.FontStretch;
import walkingkooka.tree.text.TextStylePropertyName;

public final class FontStretchComponent implements TextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegator<FontStretch, FontStretchComponent> {

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
        this.component = TextStylePropertyNameEnumHistoryTokenAnchorListComponent.with(
            idPrefix,
            TextStylePropertyName.FONT_STRETCH,
            Lists.of(
                null,
                FontStretch.ULTRA_CONDENSED,
                FontStretch.EXTRA_CONDENSED,
                FontStretch.CONDENSED,
                FontStretch.SEMI_CONDENSED,
                FontStretch.NORMAL,
                FontStretch.EXPANDED,
                FontStretch.EXTRA_EXPANDED,
                FontStretch.ULTRA_EXPANDED
            ),
            TextStylePropertyNameEnumHistoryTokenAnchorListComponent.valueToText(),
            TextStylePropertyNameEnumHistoryTokenAnchorListComponent.noIcons(),
            context // TextStylePropertyNameEnumHistoryTokenAnchorListComponentContext
        );
    }

    @Override
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponent<FontStretch> textStylePropertyNameEnumHistoryTokenAnchorListComponent() {
        return this.component;
    }

    private final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<FontStretch> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
