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

package walkingkooka.spreadsheet.dominokit.tagbox;

import elemental2.dom.HTMLElement;
import walkingkooka.Cast;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;

import java.util.List;
import java.util.Optional;

/**
 * Delegates some but not all {@link TagBoxComponent} methods.
 */
public interface TagBoxComponentDelegator<E extends HTMLElement, V, C extends FormValueComponent<E, V, C>> extends FormValueComponent<E, V, C>,
    HtmlComponentDelegator<E, C> {

    // id...............................................................................................................

    @Override
    default C setId(final String id) {
        this.tagBoxComponent().setId(id);
        return (C) this;
    }

    @Override
    default String id() {
        return this.tagBoxComponent()
            .id();
    }

    // label............................................................................................................

    @Override
    default C setLabel(final String label) {
        this.tagBoxComponent().
            setLabel(label);
        return (C) this;
    }

    @Override
    default String label() {
        return this.tagBoxComponent().label();
    }

    // helperText.......................................................................................................

    @Override
    default C alwaysShowHelperText() {
        this.tagBoxComponent()
            .alwaysShowHelperText();
        return (C) this;
    }

    @Override
    default C setHelperText(final Optional<String> text) {
        this.tagBoxComponent()
            .setHelperText(text);
        return (C) this;
    }

    @Override
    default Optional<String> helperText() {
        return this.tagBoxComponent()
            .helperText();
    }

    // errors...........................................................................................................

    @Override
    default List<String> errors() {
        return this.tagBoxComponent()
            .errors();
    }

    @Override
    default C setErrors(final List<String> errors) {
        this.tagBoxComponent()
            .setErrors(errors);
        return (C) this;
    }

    @Override
    default C hideMarginBottom() {
        this.tagBoxComponent()
            .hideMarginBottom();
        return (C) this;
    }

    @Override
    default C removeBorders() {
        this.tagBoxComponent()
            .removeBorders();
        return (C) this;
    }

    @Override
    default C removePadding() {
        this.tagBoxComponent()
            .removePadding();
        return (C) this;
    }

    @Override
    default boolean isDisabled() {
        return this.tagBoxComponent()
            .isDisabled();
    }

    @Override
    default C setDisabled(final boolean disabled) {
        this.tagBoxComponent()
            .setDisabled(disabled);
        return (C) this;
    }

    @Override
    default C required() {
        this.tagBoxComponent()
            .required();
        return (C) this;
    }

    @Override
    default boolean isRequired() {
        return this.tagBoxComponent()
            .isRequired();
    }

    @Override
    default C optional() {
        this.tagBoxComponent()
            .optional();
        return (C) this;
    }

    @Override
    default C validate() {
        this.tagBoxComponent()
            .validate();
        return (C) this;
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    default HtmlComponent<E, ?> htmlComponent() {
        return Cast.to(
            this.tagBoxComponent()
        );
    }

    TagBoxComponent<?> tagBoxComponent();
}
