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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorListComponent;
import walkingkooka.spreadsheet.dominokit.value.FormElementComponent;
import walkingkooka.spreadsheet.dominokit.value.FormElementComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilter;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public final class TextStylePropertyEnumHistoryTokenAnchorListComponent<T extends Enum<T>> extends TextStylePropertyEnumHistoryTokenAnchorListComponentShared<T>
    implements FormElementComponentDelegator<T, TextStylePropertyEnumHistoryTokenAnchorListComponent<T>> {

    public static <T extends Enum<T>> TextStylePropertyEnumHistoryTokenAnchorListComponent<T> with(final String idPrefix,
                                                                                                   final TextStylePropertyName<T> propertyName,
                                                                                                   final List<T> values,
                                                                                                   final Function<Optional<T>, String> valueToText,
                                                                                                   final Function<Optional<T>, Optional<Icon<?>>> valueToIcon,
                                                                                                   final TextStylePropertyEnumHistoryTokenAnchorListComponentContext context) {
        return new TextStylePropertyEnumHistoryTokenAnchorListComponent<>(
            idPrefix,
            propertyName,
            values,
            valueToText,
            valueToIcon,
            context
        );
    }

    private TextStylePropertyEnumHistoryTokenAnchorListComponent(final String idPrefix,
                                                                 final TextStylePropertyName<T> propertyName,
                                                                 final List<T> values,
                                                                 final Function<Optional<T>, String> valueToText,
                                                                 final Function<Optional<T>, Optional<Icon<?>>> valueToIcon,
                                                                 final TextStylePropertyEnumHistoryTokenAnchorListComponentContext context) {
        super(
            idPrefix,
            propertyName,
            values,
            valueToText,
            valueToIcon,
            context
        );

        this.formElementComponent = FormElementComponent.with(
            this.list
        );

        this.setHelperText(
            Optional.empty()
        );
        this.setErrors(
            Lists.empty()
        );
    }

    // TextStylePropertyComponentDelegator..............................................................................

    @Override
    public boolean filterTest(final TextStylePropertyFilter filter) {
        return filter.testComponent(this) ||
            filter.testEnums(
                Cast.to(this.values)
            );
    }

    // FormElementComponentDelegator.............................................................................................

    @Override
    public FormElementComponent<T, ?, ?> formElementComponent() {
        return this.formElementComponent;
    }

    private final FormElementComponent<T, HTMLDivElement, AnchorListComponent> formElementComponent;
}
