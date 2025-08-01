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

package walkingkooka.spreadsheet.dominokit.suggestbox;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;

import java.util.List;
import java.util.Optional;

/**
 * Delegates some but not all {@link SpreadsheetSuggestBoxComponent} methods.
 */
public interface SpreadsheetSuggestBoxComponentDelegator<E extends HTMLElement, V, C extends FormValueComponent<E, V, C>> extends FormValueComponent<E, V, C> {

    // id...............................................................................................................

    @Override
    default C setId(final String id) {
        this.spreadsheetSuggestBoxComponent().setId(id);
        return (C)this;
    }

    @Override
    default String id() {
        return this.spreadsheetSuggestBoxComponent()
            .id();
    }

    // label............................................................................................................

    @Override
    default C setLabel(final String label) {
        this.spreadsheetSuggestBoxComponent().
            setLabel(label);
        return (C)this;
    }

    @Override
    default String label() {
        return this.spreadsheetSuggestBoxComponent().label();
    }

    // helperText.......................................................................................................

    @Override
    default C alwaysShowHelperText() {
        this.spreadsheetSuggestBoxComponent()
            .alwaysShowHelperText();
        return (C)this;
    }

    @Override
    default C setHelperText(final Optional<String> text) {
        this.spreadsheetSuggestBoxComponent()
            .setHelperText(text);
        return (C)this;
    }

    @Override
    default Optional<String> helperText() {
        return this.spreadsheetSuggestBoxComponent()
            .helperText();
    }

    // errors...........................................................................................................

    @Override
    default List<String> errors() {
        return this.spreadsheetSuggestBoxComponent()
            .errors();
    }

    @Override
    default C setErrors(final List<String> errors) {
        this.spreadsheetSuggestBoxComponent()
            .setErrors(errors);
        return (C) this;
    }

    @Override
    default C hideMarginBottom() {
        this.spreadsheetSuggestBoxComponent()
            .hideMarginBottom();
        return (C)this;
    }

    @Override
    default C removeBorders() {
        this.spreadsheetSuggestBoxComponent()
            .removeBorders();
        return (C)this;
    }

    @Override
    default boolean isDisabled() {
        return this.spreadsheetSuggestBoxComponent()
            .isDisabled();
    }

    @Override
    default C setDisabled(final boolean disabled) {
        this.spreadsheetSuggestBoxComponent()
            .setDisabled(disabled);
        return (C)this;
    }

    @Override
    default C required() {
        this.spreadsheetSuggestBoxComponent()
            .required();
        return (C)this;
    }

    @Override
    default boolean isRequired() {
        return this.spreadsheetSuggestBoxComponent()
            .isRequired();
    }

    @Override
    default C optional() {
        this.spreadsheetSuggestBoxComponent()
            .optional();
        return (C)this;
    }

    @Override
    default C validate() {
        this.spreadsheetSuggestBoxComponent()
            .validate();
        return (C)this;
    }

    // listeners........................................................................................................

    @Override
    C addChangeListener(final ChangeListener<Optional<V>> listener);

    @Override
    default C addClickListener(final EventListener listener) {
        this.spreadsheetSuggestBoxComponent()
            .addClickListener(listener);
        return (C) this;
    }

    @Override
    default C addFocusListener(final EventListener listener) {
        this.spreadsheetSuggestBoxComponent()
            .addFocusListener(listener);
        return (C) this;
    }

    @Override
    default C addKeydownListener(final EventListener listener) {
        this.spreadsheetSuggestBoxComponent()
            .addKeydownListener(listener);
        return (C) this;
    }

    @Override
    default C addKeyupListener(final EventListener listener) {
        this.spreadsheetSuggestBoxComponent()
            .addKeyupListener(listener);
        return (C) this;
    }

    // setCssText.......................................................................................................

    @Override
    default C setCssText(final String css) {
        this.spreadsheetSuggestBoxComponent()
            .setCssText(css);
        return (C) this;
    }

    // setCssProperty...................................................................................................

    @Override
    default C setCssProperty(final String name,
                             final String value) {
        this.spreadsheetSuggestBoxComponent()
            .setCssProperty(
                name, 
                value
            );
        return (C) this;
    }

    SpreadsheetSuggestBoxComponent<?> spreadsheetSuggestBoxComponent();
}
