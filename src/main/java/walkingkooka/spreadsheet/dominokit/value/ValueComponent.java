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
import walkingkooka.Value;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;

import java.util.Optional;

/**
 * A {@link HtmlComponent} that supports mostly displaying a value, but without any label or validation functionality
 * display.
 */
public interface ValueComponent<E extends HTMLElement, V, C extends ValueComponent<E, V, C>>
    extends HtmlComponent<E, C>,
    Value<Optional<V>> {

    C setId(final String id);

    String id();

    C setValue(final Optional<V> value);

    default C clearValue() {
        return this.setValue(Optional.empty());
    }

    boolean isDisabled();

    default C enabled() {
        return this.setEnabled(true);
    }

    default C setEnabled(final boolean enabled) {
        return this.setDisabled(
            false == enabled
        );
    }

    default C disabled() {
        return this.setDisabled(true);
    }

    C setDisabled(final boolean disabled);

    /**
     * Clears the value, helper text and errors if a {@link FormValueComponent}. Useful when resetting a component to look empty.
     */
    default C clear() {
        return this.clearValue();
    }

    C addClickListener(final EventListener listener);

    C addChangeListener(final ChangeListener<Optional<V>> listener);

    C addContextMenuListener(final EventListener listener);

    C addFocusListener(final EventListener listener);

    C addKeydownListener(final EventListener listener);

    C addKeyupListener(final EventListener listener);

    C hideMarginBottom();

    C removeBorders();

    C focus();
}
