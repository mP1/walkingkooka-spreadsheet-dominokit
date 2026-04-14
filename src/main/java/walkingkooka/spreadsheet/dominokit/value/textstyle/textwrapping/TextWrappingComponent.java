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

package walkingkooka.spreadsheet.dominokit.value.textstyle.textwrapping;

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyNameEnumHistoryTokenAnchorListComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegator;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.TextWrapping;

public final class TextWrappingComponent implements TextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegator<TextWrapping, TextWrappingComponent> {

    public static TextWrappingComponent with(final String idPrefix,
                                             final TextWrappingComponentContext context) {
        return new TextWrappingComponent(
            idPrefix,
            context
        );
    }

    private TextWrappingComponent(final String idPrefix,
                                  final TextWrappingComponentContext context) {
        super();
        this.component = TextStylePropertyNameEnumHistoryTokenAnchorListComponent.with(
            idPrefix,
            TextStylePropertyName.TEXT_WRAPPING,
            Lists.of(
                null,
                TextWrapping.OVERFLOW,
                TextWrapping.WRAP,
                TextWrapping.CLIP
            ),
            TextStylePropertyNameEnumHistoryTokenAnchorListComponent.valueToText(),
            TextStylePropertyNameEnumHistoryTokenAnchorListComponent.noIcons(),
            context // TextStylePropertyNameEnumHistoryTokenAnchorListComponentContext
        );
    }

    @Override
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponent<TextWrapping> textStylePropertyNameEnumHistoryTokenAnchorListComponent() {
        return this.component;
    }

    private final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<TextWrapping> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
