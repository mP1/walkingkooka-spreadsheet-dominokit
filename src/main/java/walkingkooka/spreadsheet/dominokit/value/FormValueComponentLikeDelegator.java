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

public interface FormValueComponentLikeDelegator<E extends HTMLElement, C extends FormValueComponentLike<E, C>> extends FormValueComponentLike<E, C>,
    ValueComponentLikeDelegator<E, C> {

    @Override
    default String label() {
        return this.formValueComponentLike()
            .label();
    }

    @Override
    default C setLabel(final String label) {
        this.formValueComponentLike()
            .setLabel(label);
        return (C) this;
    }

    @Override
    default C optional() {
        this.formValueComponentLike()
            .optional();
        return (C) this;
    }

    @Override
    default C required() {
        this.formValueComponentLike()
            .required();
        return (C) this;
    }

    @Override
    default boolean isRequired() {
        return this.formValueComponentLike()
            .isRequired();
    }

    @Override
    default C alwaysShowHelperText() {
        this.formValueComponentLike()
            .alwaysShowHelperText();
        return (C) this;
    }

    @Override
    default C validate() {
        this.formValueComponentLike()
            .validate();
        return (C) this;
    }

    @Override
    default Optional<String> helperText() {
        return this.formValueComponentLike()
            .helperText();
    }

    @Override
    default C setHelperText(final Optional<String> text) {
        this.formValueComponentLike()
            .setHelperText(text);
        return (C) this;
    }

    @Override
    default List<String> errors() {
        return this.formValueComponentLike()
            .errors();
    }

    @Override
    default C setErrors(final List<String> errors) {
        this.formValueComponentLike()
            .setErrors(errors);
        return (C) this;
    }

    FormValueComponentLike<E, ?> formValueComponentLike();

    // ValueComponentLikeDelegator......................................................................................

    @Override
    default ValueComponentLike<E, ?> valueComponentLike() {
        return this.formValueComponentLike();
    }
}
