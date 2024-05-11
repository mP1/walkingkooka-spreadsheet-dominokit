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

package walkingkooka.spreadsheet.dominokit.ui.parsertextbox;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.dominokit.ui.ValueComponent;
import walkingkooka.text.HasText;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Optional;

/**
 * A helper interface that implements delegator methods for a wrapped {@link ParserSpreadsheetTextBox}.
 */
public interface ParserSpreadsheetTextBoxWrapper<C extends ParserSpreadsheetTextBoxWrapper<C, T>, T extends HasText>
        extends ValueComponent<HTMLFieldSetElement, T, C>,
        TreePrintable {

    @Override
    default C setId(final String id) {
        this.parserSpreadsheetTextBox()
                .setId(id);
        return (C) this;
    }

    @Override
    default String id() {
        return this.parserSpreadsheetTextBox()
                .id();
    }

    @Override
    default C setLabel(final String label) {
        this.parserSpreadsheetTextBox()
                .setLabel(label);
        return (C) this;
    }

    @Override
    default String label() {
        return this.parserSpreadsheetTextBox()
                .label();
    }

    @Override
    default C setValue(final Optional<T> value) {
        this.parserSpreadsheetTextBox()
                .setValue(value);
        return (C) this;
    }

    @Override
    default C focus() {
        this.parserSpreadsheetTextBox()
                .focus();
        return (C) this;
    }

    @Override
    default C optional() {
        this.parserSpreadsheetTextBox()
                .optional();
        return (C) this;
    }

    @Override
    default C required() {
        this.parserSpreadsheetTextBox()
                .required();
        return (C) this;
    }

    @Override
    default boolean isRequired() {
        return this.parserSpreadsheetTextBox()
                .isRequired();
    }

    @Override
    default C validate() {
        this.parserSpreadsheetTextBox()
                .validate();
        return (C) this;
    }

    @Override
    default List<String> errors() {
        return this.parserSpreadsheetTextBox()
                .errors();
    }

    @Override
    default boolean isDisabled() {
        return this.parserSpreadsheetTextBox()
                .isDisabled();
    }

    @Override
    default C setDisabled(final boolean disabled) {
        this.parserSpreadsheetTextBox()
                .setDisabled(disabled);
        return (C) this;
    }

    @Override
    default C addChangeListener(final ChangeListener<Optional<T>> listener) {
        this.parserSpreadsheetTextBox()
                .addChangeListener(listener);
        return (C) this;
    }

    @Override
    default C addFocusListener(final EventListener listener) {
        this.parserSpreadsheetTextBox()
                .addFocusListener(listener);
        return (C) this;
    }

    @Override
    default C addKeydownListener(final EventListener listener) {
        this.parserSpreadsheetTextBox()
                .addKeydownListener(listener);
        return (C) this;
    }

    @Override
    default C addKeyupListener(final EventListener listener) {
        this.parserSpreadsheetTextBox()
                .addKeyupListener(listener);
        return (C) this;
    }

    @Override
    default C alwaysShowHelperText() {
        this.parserSpreadsheetTextBox()
                .alwaysShowHelperText();
        return (C) this;
    }

    @Override
    default C setHelperText(final Optional<String> text) {
        this.parserSpreadsheetTextBox()
                .setHelperText(text);
        return (C) this;
    }

    @Override
    default Optional<String> helperText() {
        return this.parserSpreadsheetTextBox()
                .helperText();
    }

    @Override
    default C hideMarginBottom() {
        this.parserSpreadsheetTextBox()
                .hideMarginBottom();
        return (C) this;
    }

    @Override
    default C removeBorders() {
        this.parserSpreadsheetTextBox()
                .removeBorders();
        return (C) this;
    }

    @Override
    default Optional<T> value() {
        return this.parserSpreadsheetTextBox()
                .value();
    }

    default Optional<String> stringValue() {
        return this.parserSpreadsheetTextBox()
                .stringValue();
    }

    default C setStringValue(final Optional<String> stringValue) {
        this.parserSpreadsheetTextBox()
                .setStringValue(stringValue);
        return (C) this;
    }

    @Override
    default HTMLFieldSetElement element() {
        return this.parserSpreadsheetTextBox()
                .element();
    }

    /**
     * The wrapped {@link ParserSpreadsheetTextBox}, which is the target of all delegated methods.
     */
    ParserSpreadsheetTextBox<T> parserSpreadsheetTextBox();

    // TreePrintable....................................................................................................

    @Override
    default void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.parserSpreadsheetTextBox()
                    .printTree(printer);
        }
        printer.outdent();
    }
}
