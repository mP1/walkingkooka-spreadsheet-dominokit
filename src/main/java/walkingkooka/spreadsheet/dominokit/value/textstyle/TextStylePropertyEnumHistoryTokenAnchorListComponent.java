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
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorListComponent;
import walkingkooka.spreadsheet.dominokit.value.FormElement;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final class TextStylePropertyEnumHistoryTokenAnchorListComponent<T extends Enum<T>> extends TextStylePropertyEnumHistoryTokenAnchorListComponentLike<T> {

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

        this.formElement = FormElement.with(
            this.list
        );

        this.setLabel(
            propertyNameToLabel(propertyName)
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
        return this.formElement.getLabel();
    }

    @Override
    public TextStylePropertyEnumHistoryTokenAnchorListComponent<T> setLabel(final String label) {
        this.formElement.setLabel(label);
        return this;
    }

    @Override
    public Optional<String> helperText() {
        final String helperText = this.formElement.getHelperText();

        return Optional.ofNullable(
            CharSequences.isNullOrEmpty(helperText) ?
                null :
                helperText
        );
    }

    @Override
    public TextStylePropertyEnumHistoryTokenAnchorListComponent<T> setHelperText(final Optional<String> text) {
        Objects.requireNonNull(text, "text");

        this.formElement.setHelperText(
            text.orElse(null)
        );
        return this;
    }

    @Override
    public List<String> errors() {
        return this.formElement.getErrors();
    }

    @Override
    public TextStylePropertyEnumHistoryTokenAnchorListComponent<T> setErrors(final List<String> errors) {
        this.formElement.invalidate(errors);
        return this;
    }

    // id...............................................................................................................

    @Override
    public String id() {
        return this.formElement.getId();
    }

    @Override
    public TextStylePropertyEnumHistoryTokenAnchorListComponent<T> setId(final String id) {
        this.formElement.setId(id);
        return this;
    }

    // width............................................................................................................

    @Override
    public int width() {
        return this.formElement.element()
            .offsetWidth;
    }

    // height...........................................................................................................

    @Override
    public int height() {
        return this.formElement.element()
            .offsetHeight;
    }

    // cssXXX...........................................................................................................

    @Override
    public TextStylePropertyEnumHistoryTokenAnchorListComponent<T> setCssText(final String css) {
        this.formElement.element()
            .style
            .cssText = css;
        return this;
    }

    @Override
    public TextStylePropertyEnumHistoryTokenAnchorListComponent<T> setCssProperty(final String name,
                                                                                  final String value) {
        this.formElement.element()
            .style
            .setProperty(
                name,
                value
            );
        return this;
    }

    @Override
    public TextStylePropertyEnumHistoryTokenAnchorListComponent<T> removeCssProperty(final String name) {
        this.formElement.element()
            .style
            .removeProperty(name);
        return this;
    }

    // Component........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.formElement.element();
    }

    private final FormElement<T, HTMLDivElement, AnchorListComponent> formElement;
}
