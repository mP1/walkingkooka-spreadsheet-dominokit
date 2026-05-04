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

package walkingkooka.spreadsheet.dominokit.value.textstyle.hyphens;

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumHistoryTokenAnchorListComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumHistoryTokenAnchorListComponentDelegator;
import walkingkooka.tree.text.Hyphens;
import walkingkooka.tree.text.TextStylePropertyName;

public final class HyphensComponent implements TextStylePropertyEnumHistoryTokenAnchorListComponentDelegator<Hyphens, HyphensComponent> {

    public static HyphensComponent with(final String idPrefix,
                                        final HyphensComponentContext context) {
        return new HyphensComponent(
            idPrefix,
            context
        );
    }
    
    private HyphensComponent(final String idPrefix,
                             final HyphensComponentContext context) {
        super();
        this.component = TextStylePropertyEnumHistoryTokenAnchorListComponent.with(
            idPrefix,
            TextStylePropertyName.HYPHENS,
            Lists.of(
                null,
                Hyphens.NONE,
                Hyphens.MANUAL,
                Hyphens.AUTO
            ),
            TextStylePropertyEnumHistoryTokenAnchorListComponent.valueToText(),
            TextStylePropertyEnumHistoryTokenAnchorListComponent.noIcons(),
            TEXT_STYLE_PROPERTY_FILTER_KINDS_BREAK,
            context // TextStylePropertyEnumHistoryTokenAnchorListComponentContext
        );
    }

    @Override
    public TextStylePropertyEnumHistoryTokenAnchorListComponent<Hyphens> textStylePropertyNameEnumHistoryTokenAnchorListComponent() {
        return this.component;
    }

    private final TextStylePropertyEnumHistoryTokenAnchorListComponent<Hyphens> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
