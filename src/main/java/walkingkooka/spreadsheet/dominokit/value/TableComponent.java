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
 * Base interface for all table {@link ValueComponent}.
 */
public interface TableComponent<E extends HTMLElement, V, C extends TableComponent<E, V, C>> extends ValueComponent<E, V, C> {

    @Override
    default String id() {
        throw new UnsupportedOperationException();
    }

    @Override
    default C setId(final String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean isDisabled() {
        return false;
    }

    @Override
    default C setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    default C addBlurListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    default C addChangeListener(final ChangeListener<Optional<V>> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    default C addClickListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    default C addContextMenuListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    default C addFocusListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    default C addKeyDownListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    default C addKeyUpListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    default C hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    default C removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    default C removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean isEditing() {
        return false; // query individual components
    }
}
