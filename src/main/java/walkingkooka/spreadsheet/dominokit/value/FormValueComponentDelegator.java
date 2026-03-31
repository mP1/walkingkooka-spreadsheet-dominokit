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

import elemental2.dom.HTMLElement;

import java.util.List;
import java.util.Optional;

public interface FormValueComponentDelegator<E extends HTMLElement, V, C extends FormValueComponent<E, V, C>> extends FormValueComponent<E, V, C>,
    ValueComponentDelegator<E, V, C> {

    @Override
    default String label() {
        return this.formValueComponent()
            .label();
    }

    @Override
    default C setLabel(final String label) {
        this.formValueComponent()
            .setLabel(label);
        return (C) this;
    }

    @Override
    default C optional() {
        this.formValueComponent()
            .optional();
        return (C) this;
    }

    @Override
    default C required() {
        this.formValueComponent()
            .required();
        return (C) this;
    }

    @Override
    default boolean isRequired() {
        return this.formValueComponent()
            .isRequired();
    }

    @Override
    default C alwaysShowHelperText() {
        this.formValueComponent()
            .alwaysShowHelperText();
        return (C) this;
    }

    @Override
    default C validate() {
        this.formValueComponent()
            .validate();
        return (C) this;
    }

    @Override
    default Optional<String> helperText() {
        return this.formValueComponent()
            .helperText();
    }

    @Override
    default C setHelperText(final Optional<String> text) {
        this.formValueComponent()
            .setHelperText(text);
        return (C) this;
    }

    @Override
    default List<String> errors() {
        return this.formValueComponent()
            .errors();
    }

    @Override
    default C setErrors(final List<String> errors) {
        this.formValueComponent()
            .setErrors(errors);
        return (C) this;
    }

    FormValueComponent<E, V, ?> formValueComponent();

    // ValueComponentDelegator..........................................................................................

    @Override
    default ValueComponent<E, V, ?> valueComponent() {
        return this.formValueComponent();
    }
}
