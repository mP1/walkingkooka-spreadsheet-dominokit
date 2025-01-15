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
import walkingkooka.spreadsheet.dominokit.ComponentWithErrors;

import java.util.Optional;

/**
 * A component that supports entering or selecting a value.
 */
public interface FormValueComponent<E extends HTMLElement, V, C extends FormValueComponent<E, V, C>>
    extends ValueComponent<E, V, C>,
    ComponentWithErrors<C> {

    C setLabel(final String label);

    String label();

    /**
     * Getter that returns the current helper text.
     */
    Optional<String> helperText();

    C validate();

    C optional();

    C required();

    boolean isRequired();

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
     * Clears the value, helper text and errors. Useful when resetting a component to look empty.
     */
    default C clear() {
        return this.clearValue()
            .clearHelperText()
            .clearErrors();
    }

    /**
     * Constant height for containers holding helper text.
     */
    String HELPER_TEXT_HEIGHT = "4em";
}
