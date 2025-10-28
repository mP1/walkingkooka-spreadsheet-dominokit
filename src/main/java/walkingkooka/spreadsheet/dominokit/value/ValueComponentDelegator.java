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
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;

import java.util.Optional;

/**
 * A delegator for {@link ValueComponent} that may be within a {@link walkingkooka.spreadsheet.dominokit.Component}.
 */
public interface ValueComponentDelegator<E extends HTMLElement, V, C extends ValueComponent<E, V, C>> extends ValueComponent<E, V, C>,
    HtmlComponentDelegator<E, C> {

    @Override
    default Optional<V> value() {
        return this.valueComponent()
            .value();
    }

    @Override
    default C setValue(final Optional<V> value) {
        this.valueComponent()
            .setValue(value);
        return (C) this;
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
    default C addBlurListener(final EventListener listener) {
        this.valueComponent()
            .addBlurListener(listener);
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
    default C addContextMenuListener(final EventListener listener) {
        this.valueComponent()
            .addContextMenuListener(listener);
        return (C) this;
    }

    @Override
    default C addFocusListener(final EventListener listener) {
        this.valueComponent()
            .addFocusListener(listener);
        return (C) this;
    }

    @Override
    default C addKeyDownListener(final EventListener listener) {
        this.valueComponent()
            .addKeyDownListener(listener);
        return (C) this;
    }

    @Override
    default C addKeyUpListener(final EventListener listener) {
        this.valueComponent()
            .addKeyUpListener(listener);
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
    default C removePadding() {
        this.valueComponent()
            .removePadding();
        return (C) this;
    }

    @Override
    default C focus() {
        this.valueComponent()
            .focus();
        return (C) this;
    }

    @Override
    default boolean isEditing() {
        return this.valueComponent()
            .isEditing();
    }

    ValueComponent<E, V, ?> valueComponent();

    // HtmlComponentDelegator...........................................................................................

    @Override
    default HtmlComponent<E, ?> htmlComponent() {
        return this.valueComponent();
    }
}
