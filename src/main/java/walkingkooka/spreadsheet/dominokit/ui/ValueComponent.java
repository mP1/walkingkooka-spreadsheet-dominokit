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

package walkingkooka.spreadsheet.dominokit.ui;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.Value;

import java.util.Optional;

/**
 * A component that supports entering or selecting a value.
 */
public interface ValueComponent<E extends HTMLElement, V, C extends ValueComponent<E, V, C>>
        extends HtmlElementComponent<E, C>,
        Value<Optional<V>> {

    C setId(final String id);

    String id();

    C setLabel(final String label);

    String label();

    C setValue(final Optional<V> value);

    default C clearValue() {
        return this.setValue(Optional.empty());
    }

    C focus();

    C optional();

    C required();

    C validate();

    boolean isDisabled();

    C setDisabled(final boolean disabled);

    C addChangeListener(final ChangeListener<Optional<V>> listener);

    C addFocusListener(final EventListener listener);

    C addKeydownListener(final EventListener listener);

    C addKeyupListener(final EventListener listener);

    /**
     * The normal domino-kit behaviour is to only show helper text where validation error text appears, as necessary.
     * When a component has no helper text to show the helper text space is auto hidden.
     */
    C alwaysShowHelperText();

    /**
     * This setter may be used to set a (error) message.
     */
    C setHelperText(final Optional<String> text);

    /**
     * Clears any present helper text.
     */
    default C clearHelperText() {
        return this.setHelperText(Optional.empty());
    }

    /**
     * Constant height for containers holding helper text.
     */
    String HELPER_TEXT_HEIGHT = "4em";

    C hideMarginBottom();

    C removeBorders();
}
