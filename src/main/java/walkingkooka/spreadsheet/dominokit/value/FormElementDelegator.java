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

package walkingkooka.spreadsheet.dominokit.value;

import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface FormElementDelegator<V, C extends FormElementDelegator<V, C>> extends FormValueComponent<HTMLFieldSetElement, V, C> {

    static String propertyNameToLabel(final TextStylePropertyName<?> propertyName) {
        Objects.requireNonNull(propertyName, "propertyName");

        return CaseKind.KEBAB.change(
            propertyName.value(),
            CaseKind.TITLE
        );
    }

    @Override
    default String label() {
        return this.formElement()
            .getLabel();
    }

    @Override
    default C setLabel(final String label) {
        this.formElement()
            .setLabel(label);
        return (C) this;
    }

    @Override
    default Optional<String> helperText() {
        final String helperText = this.formElement()
            .getHelperText();

        return Optional.ofNullable(
            CharSequences.isNullOrEmpty(helperText) ?
                null :
                helperText
        );
    }

    @Override
    default C setHelperText(final Optional<String> text) {
        Objects.requireNonNull(text, "text");

        this.formElement().setHelperText(
            text.orElse(null)
        );
        return (C) this;
    }

    @Override
    default List<String> errors() {
        return this.formElement().getErrors();
    }

    @Override
    default C setErrors(final List<String> errors) {
        this.formElement()
            .invalidate(errors);
        return (C) this;
    }

    // id...............................................................................................................

    @Override
    default String id() {
        return this.formElement()
            .getId();
    }

    @Override
    default C setId(final String id) {
        this.formElement()
            .setId(id);
        return (C) this;
    }

    // width............................................................................................................

    @Override
    default int width() {
        return this.formElement()
            .element()
            .offsetWidth;
    }

    // height...........................................................................................................

    @Override
    default int height() {
        return this.formElement()
            .element()
            .offsetHeight;
    }

    // cssXXX...........................................................................................................

    @Override
    default C setCssText(final String css) {
        this.formElement()
            .element()
            .style
            .cssText = css;
        return (C) this;
    }

    @Override
    default C setCssProperty(final String name,
                             final String value) {
        this.formElement()
            .element()
            .style
            .setProperty(
                name,
                value
            );
        return (C) this;
    }

    @Override
    default C removeCssProperty(final String name) {
        this.formElement().element()
            .style
            .removeProperty(name);
        return (C) this;
    }

    FormElement<V, ?, ?> formElement();

    // Component........................................................................................................

    @Override
    default HTMLFieldSetElement element() {
        return this.formElement()
            .element();
    }
}