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
import elemental2.dom.HTMLFieldSetElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Optional;

/**
 * A helper interface that implements delegator methods for a wrapped {@link ValueSpreadsheetTextBox}.
 */
public interface ValueSpreadsheetTextBoxWrapper<C extends ValueSpreadsheetTextBoxWrapper<C, V>, V>
    extends FormValueComponent<HTMLFieldSetElement, V, C> {

    @Override
    default C setId(final String id) {
        this.valueSpreadsheetTextBox()
            .setId(id);
        return (C) this;
    }

    @Override
    default String id() {
        return this.valueSpreadsheetTextBox()
            .id();
    }

    @Override
    default C setLabel(final String label) {
        this.valueSpreadsheetTextBox()
            .setLabel(label);
        return (C) this;
    }

    @Override
    default String label() {
        return this.valueSpreadsheetTextBox()
            .label();
    }

    @Override
    default C setValue(final Optional<V> value) {
        this.valueSpreadsheetTextBox()
            .setValue(value);
        return (C) this;
    }

    @Override
    default C focus() {
        this.valueSpreadsheetTextBox()
            .focus();
        return (C) this;
    }

    @Override
    default boolean isEditing() {
        return this.valueSpreadsheetTextBox()
            .isEditing();
    }

    @Override
    default C optional() {
        this.valueSpreadsheetTextBox()
            .optional();
        return (C) this;
    }

    @Override
    default C required() {
        this.valueSpreadsheetTextBox()
            .required();
        return (C) this;
    }

    @Override
    default boolean isRequired() {
        return this.valueSpreadsheetTextBox()
            .isRequired();
    }

    @Override
    default C validate() {
        this.valueSpreadsheetTextBox()
            .validate();
        return (C) this;
    }

    @Override
    default List<String> errors() {
        return this.valueSpreadsheetTextBox()
            .errors();
    }

    @Override
    default C setErrors(final List<String> errors) {
        this.valueSpreadsheetTextBox()
            .setErrors(errors);
        return (C) this;
    }

    @Override
    default boolean isDisabled() {
        return this.valueSpreadsheetTextBox()
            .isDisabled();
    }

    @Override
    default C setDisabled(final boolean disabled) {
        this.valueSpreadsheetTextBox()
            .setDisabled(disabled);
        return (C) this;
    }

    @Override
    default C addChangeListener(final ChangeListener<Optional<V>> listener) {
        this.valueSpreadsheetTextBox()
            .addChangeListener(listener);
        return (C) this;
    }

    @Override
    default C addClickListener(final EventListener listener) {
        this.valueSpreadsheetTextBox()
            .addClickListener(listener);
        return (C) this;
    }

    @Override
    default C addFocusListener(final EventListener listener) {
        this.valueSpreadsheetTextBox()
            .addFocusListener(listener);
        return (C) this;
    }

    @Override
    default C addKeydownListener(final EventListener listener) {
        this.valueSpreadsheetTextBox()
            .addKeydownListener(listener);
        return (C) this;
    }

    @Override
    default C addKeyupListener(final EventListener listener) {
        this.valueSpreadsheetTextBox()
            .addKeyupListener(listener);
        return (C) this;
    }

    @Override
    default C alwaysShowHelperText() {
        this.valueSpreadsheetTextBox()
            .alwaysShowHelperText();
        return (C) this;
    }

    @Override
    default C setHelperText(final Optional<String> text) {
        this.valueSpreadsheetTextBox()
            .setHelperText(text);
        return (C) this;
    }

    @Override
    default Optional<String> helperText() {
        return this.valueSpreadsheetTextBox()
            .helperText();
    }

    @Override
    default C hideMarginBottom() {
        this.valueSpreadsheetTextBox()
            .hideMarginBottom();
        return (C) this;
    }

    @Override
    default C removeBorders() {
        this.valueSpreadsheetTextBox()
            .removeBorders();
        return (C) this;
    }

    @Override
    default Optional<V> value() {
        return this.valueSpreadsheetTextBox()
            .value();
    }

    default Optional<String> stringValue() {
        return this.valueSpreadsheetTextBox()
            .stringValue();
    }

    default C setStringValue(final Optional<String> stringValue) {
        this.valueSpreadsheetTextBox()
            .setStringValue(stringValue);
        return (C) this;
    }

    @Override
    default HTMLFieldSetElement element() {
        return this.valueSpreadsheetTextBox()
            .element();
    }

    @Override
    default C setCssText(final String css) {
        this.valueSpreadsheetTextBox()
            .setCssText(css);
        return (C) this;
    }

    @Override
    default C setCssProperty(final String name,
                             final String value) {
        this.valueSpreadsheetTextBox()
            .setCssProperty(
                name,
                value
            );
        return (C) this;
    }

    @Override
    default Node node() {
        return this.valueSpreadsheetTextBox()
            .node();
    }

    /**
     * The wrapped {@link ValueSpreadsheetTextBox}, which is the target of all delegated methods.
     */
    ValueSpreadsheetTextBox<V> valueSpreadsheetTextBox();

    // TreePrintable....................................................................................................

    @Override
    default void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.valueSpreadsheetTextBox()
                .printTree(printer);
        }
        printer.outdent();
    }
}
