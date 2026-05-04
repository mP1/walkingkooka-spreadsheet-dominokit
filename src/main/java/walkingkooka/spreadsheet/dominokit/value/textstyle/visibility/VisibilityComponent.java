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

package walkingkooka.spreadsheet.dominokit.value.textstyle.visibility;

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilterKind;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.Visibility;

import java.util.EnumSet;

public final class VisibilityComponent implements TextStylePropertyEnumComponentDelegator<Visibility, VisibilityComponent> {

    public static VisibilityComponent with(final String idPrefix,
                                           final VisibilityComponentContext context) {
        return new VisibilityComponent(
            idPrefix,
            context
        );
    }

    private VisibilityComponent(final String idPrefix,
                                final VisibilityComponentContext context) {
        super();
        this.component = TextStylePropertyEnumComponent.with(
            idPrefix,
            TextStylePropertyName.VISIBILITY,
            Lists.of(
                null,
                Visibility.VISIBLE,
                Visibility.HIDDEN,
                Visibility.COLLAPSE
            ),
            TextStylePropertyEnumComponent.valueToText(),
            TextStylePropertyEnumComponent.noIcons(),
            EnumSet.noneOf(TextStylePropertyFilterKind.class),
            context // TextStylePropertyEnumComponentContext
        );
    }

    @Override
    public TextStylePropertyEnumComponent<Visibility> textStylePropertyNameEnumHistoryTokenAnchorListComponent() {
        return this.component;
    }

    private final TextStylePropertyEnumComponent<Visibility> component;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
