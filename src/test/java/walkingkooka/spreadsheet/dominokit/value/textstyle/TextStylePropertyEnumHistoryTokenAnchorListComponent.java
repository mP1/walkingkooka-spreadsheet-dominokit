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

import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final class TextStylePropertyEnumHistoryTokenAnchorListComponent<T extends Enum<T>> extends TextStylePropertyEnumHistoryTokenAnchorListComponentLike<T>
    implements TestHtmlElementComponent<HTMLFieldSetElement, TextStylePropertyEnumHistoryTokenAnchorListComponent<T>> {

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

        this.setHelperText(
            Optional.empty()
        );
        this.setErrors(
            Lists.empty()
        );
    }

    @Override
    public String label() {
        return this.label;
    }

    @Override
    public TextStylePropertyEnumHistoryTokenAnchorListComponent<T> setLabel(final String label) {
        this.label = Objects.requireNonNull(label, "label");
        return this;
    }

    private String label;

    @Override
    public Optional<String> helperText() {
        return this.helperText;
    }

    @Override
    public TextStylePropertyEnumHistoryTokenAnchorListComponent<T> setHelperText(final Optional<String> helperText) {
        Objects.requireNonNull(helperText, "helperText");

        this.helperText = helperText;
        return this;
    }

    private Optional<String> helperText;

    @Override
    public List<String> errors() {
        return this.errors;
    }

    @Override
    public TextStylePropertyEnumHistoryTokenAnchorListComponent<T> setErrors(final List<String> errors) {
        this.errors = Lists.immutable(
            Objects.requireNonNull(errors, "errors")
        );
        return this;
    }

    private List<String> errors;
}
