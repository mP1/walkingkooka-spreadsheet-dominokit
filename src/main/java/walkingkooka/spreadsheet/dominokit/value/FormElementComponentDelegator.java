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
import walkingkooka.spreadsheet.dominokit.dom.HasFocusBlurEventListener;
import walkingkooka.spreadsheet.dominokit.dom.HasFocusBlurEventListenerDelegator;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface FormElementComponentDelegator<V, C extends FormElementComponentDelegator<V, C>> extends FormValueComponent<HTMLFieldSetElement, V, C>,
    HasFocusBlurEventListenerDelegator<C> {

    @Override
    default String label() {
        return this.formElementComponent()
            .getLabel();
    }

    @Override
    default C setLabel(final String label) {
        this.formElementComponent()
            .setLabel(label);
        return (C) this;
    }

    @Override
    default Optional<String> helperText() {
        final String helperText = this.formElementComponent()
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

        this.formElementComponent().setHelperText(
            text.orElse(null)
        );
        return (C) this;
    }

    @Override
    default List<String> errors() {
        return this.formElementComponent().getErrors();
    }

    @Override
    default C setErrors(final List<String> errors) {
        this.formElementComponent()
            .invalidate(errors);
        return (C) this;
    }

    // id...............................................................................................................

    @Override
    default String id() {
        return this.formElementComponent()
            .getId();
    }

    @Override
    default C setId(final String id) {
        this.formElementComponent()
            .setId(id);
        return (C) this;
    }

    // width............................................................................................................

    @Override
    default int width() {
        return this.formElementComponent()
            .element()
            .offsetWidth;
    }

    // height...........................................................................................................

    @Override
    default int height() {
        return this.formElementComponent()
            .element()
            .offsetHeight;
    }

    // cssXXX...........................................................................................................

    @Override
    default C setCssText(final String css) {
        this.formElementComponent()
            .element()
            .style
            .cssText = css;
        return (C) this;
    }

    @Override
    default C setCssProperty(final String name,
                             final String value) {
        this.formElementComponent()
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
        this.formElementComponent().element()
            .style
            .removeProperty(name);
        return (C) this;
    }

    FormElementComponent<V, ?, ?> formElementComponent();

    // HasFocusBlurEventListenerDelegator...............................................................................

    @Override
    default HasFocusBlurEventListener<?> hasFocusBlurEventListener() {
        return this.formElementComponent();
    }

    // Component........................................................................................................

    @Override
    default HTMLFieldSetElement element() {
        return this.formElementComponent()
            .element();
    }
}