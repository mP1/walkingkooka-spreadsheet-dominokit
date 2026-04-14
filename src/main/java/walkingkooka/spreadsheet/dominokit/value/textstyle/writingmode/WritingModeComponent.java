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

package walkingkooka.spreadsheet.dominokit.value.textstyle.writingmode;

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyNameEnumHistoryTokenAnchorListComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegator;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.WritingMode;

public final class WritingModeComponent implements TextStylePropertyNameEnumHistoryTokenAnchorListComponentDelegator<WritingMode, WritingModeComponent> {

    public static WritingModeComponent with(final String idPrefix,
                                            final WritingModeComponentContext context) {
        return new WritingModeComponent(
            idPrefix,
            context
        );
    }

    private WritingModeComponent(final String idPrefix,
                                 final WritingModeComponentContext context) {
        super();
        this.component = TextStylePropertyNameEnumHistoryTokenAnchorListComponent.with(
            idPrefix,
            TextStylePropertyName.WRITING_MODE,
            Lists.of(
                null,
                WritingMode.HORIZONTAL_TB,
                WritingMode.VERTICAL_LR,
                WritingMode.VERTICAL_RL
            ),
            TextStylePropertyNameEnumHistoryTokenAnchorListComponent.valueToText(),
            TextStylePropertyNameEnumHistoryTokenAnchorListComponent.noIcons(),
            context // TextStylePropertyNameEnumHistoryTokenAnchorListComponentContext
        );
    }

    @Override
    public TextStylePropertyNameEnumHistoryTokenAnchorListComponent<WritingMode> textStylePropertyNameEnumHistoryTokenAnchorListComponent() {
        return this.component;
    }

    private final TextStylePropertyNameEnumHistoryTokenAnchorListComponent<WritingMode> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
