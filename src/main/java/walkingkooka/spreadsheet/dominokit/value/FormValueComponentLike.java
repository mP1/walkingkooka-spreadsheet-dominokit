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
 * Adds form support such as a label, helper text and errors to a {@link ValueComponentLike}.
 */
public interface FormValueComponentLike<E extends HTMLElement, C extends FormValueComponentLike<E, C>>
    extends ValueComponentLike<E, C>,
    ComponentWithErrors<C> {

    C setLabel(final String label);

    String label();

    /**
     * Getter that returns the current helper text.
     */
    Optional<String> helperText();

    /**
     * The normal domino-kit behaviour is to only show helper text where validation error text appears, as necessary.
     * When a component has no helper text to show the helper text space is auto hidden.
     */
    C alwaysShowHelperText();

    /**
     * This setter may be used to set the helper text. Note helper text is not the same as error messages.
     */
    C setHelperText(final Optional<String> text);

    /**
     * Clears any present helper text.
     */
    default C clearHelperText() {
        return this.setHelperText(Optional.empty());
    }

    C validate();

    C optional();

    C required();

    boolean isRequired();

    /**
     * Clears the value, helper text and errors. Useful when resetting a component to look empty.
     */
    @Override
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
