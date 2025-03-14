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

import elemental2.dom.EventListener;
import elemental2.dom.HTMLElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;

import java.util.Optional;

/**
 * A delegator for {@link ValueComponent} that may be within a {@link walkingkooka.spreadsheet.dominokit.Component}.
 */
public interface ValueComponentDelegator<E extends HTMLElement, V, C extends ValueComponent<E, V, C>> extends ValueComponent<E, V, C> {

    @Override
    default C setId(final String id) {
        this.valueComponent()
            .setId(id);
        return (C) this;
    }

    @Override
    default String id() {
        return this.valueComponent()
            .id();
    }

    @Override
    default Optional<V> value() {
        return this.valueComponent()
            .value();
    }

    @Override
    default C setValue(final Optional<V> value) {
        this.valueComponent()
            .setValue(value);
        return (C)this;
    }

    @Override 
    default boolean isDisabled() {
        return this.valueComponent()
            .isDisabled();
    }

    @Override 
    default C setDisabled(final boolean disabled) {
        this.valueComponent()
            .setDisabled(disabled);
        return (C) this;
    }

    @Override 
    default C addChangeListener(final ChangeListener<Optional<V>> listener) {
        this.valueComponent()
            .addChangeListener(listener);
        return (C) this;
    }

    @Override
    default C addClickListener(final EventListener listener) {
        this.valueComponent()
            .addClickListener(listener);
        return (C) this;
    }
    
    @Override
    default C addFocusListener(final EventListener listener) {
        this.valueComponent()
            .addFocusListener(listener);
        return (C) this;
    }

    @Override
    default C addKeydownListener(final EventListener listener) {
        this.valueComponent()
            .addKeydownListener(listener);
        return (C) this;
    }

    @Override
    default C addKeyupListener(final EventListener listener) {
        this.valueComponent()
            .addKeyupListener(listener);
        return (C) this;
    }

    @Override
    default C hideMarginBottom() {
        this.valueComponent()
            .hideMarginBottom();
        return (C) this;
    }

    @Override
    default C removeBorders() {
        this.valueComponent()
            .removeBorders();
        return (C) this;
    }

    @Override
    default C focus() {
        this.valueComponent()
            .focus();
        return (C) this;
    }

    @Override
    default C setCssText(final String css) {
        this.valueComponent()
            .setCssText(css);
        return (C) this;
    }

    @Override
    default C setCssProperty(final String name,
                             final String value) {
        this.valueComponent()
            .setCssProperty(
                name,
                value
            );
        return (C) this;
    }

    @Override
    default E element() {
        return this.valueComponent()
            .element();
    }

    ValueComponent<E, V, ?> valueComponent();
}
