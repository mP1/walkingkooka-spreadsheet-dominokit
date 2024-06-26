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
import elemental2.dom.Node;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.spreadsheet.dominokit.ui.ValueComponent;
import walkingkooka.spreadsheet.dominokit.ui.textbox.SpreadsheetTextBox;
import walkingkooka.spreadsheet.dominokit.ui.validator.SpreadsheetValidators;
import walkingkooka.text.HasText;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A text box that supports a typed value using a {@link Function} as a parser. Any thrown exception messages become
 * the validation fail messages. it is possible to replace the default validator mentioned above using {@link #setValidator(Validator)}.
 */
public final class ParserSpreadsheetTextBox<T extends HasText> implements ValueComponent<HTMLFieldSetElement, T, ParserSpreadsheetTextBox<T>> {

    /**
     * Creates a new {@link ParserSpreadsheetTextBox}.
     */
    public static <T extends HasText> ParserSpreadsheetTextBox<T> with(final Function<String, T> parser) {
        return new ParserSpreadsheetTextBox<>(parser);
    }

    private ParserSpreadsheetTextBox(final Function<String, T> parser) {
        this.textBox = SpreadsheetTextBox.empty()
                .clearIcon()
                .disableSpellcheck()
                .enterFiresValueChange();
        this.setParser(parser);
        this.required();
    }

    /**
     * Sets a new {@link Function} will be used to parse String into values.
     */
    public ParserSpreadsheetTextBox<T> setParser(final Function<String, T> parser) {
        Objects.requireNonNull(parser, "parser");

        this.parser = parser;
        this.setValidator(
                SpreadsheetValidators.tryCatch(parser::apply)
        );
        this.validate();
        return this;
    }

    private Function<String, T> parser;

    @Override
    public ParserSpreadsheetTextBox<T> setId(final String id) {
        this.textBox.setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.textBox.id();
    }

    @Override
    public ParserSpreadsheetTextBox<T> setLabel(final String label) {
        this.textBox.setLabel(label);
        return this;
    }

    @Override
    public String label() {
        return this.textBox.label();
    }

    @Override
    public boolean isDisabled() {
        return this.textBox.isDisabled();
    }

    @Override
    public ParserSpreadsheetTextBox<T> setDisabled(final boolean disabled) {
        this.textBox.setDisabled(disabled);
        return this;
    }

    @Override
    public ParserSpreadsheetTextBox<T> optional() {
        this.textBox.setValidator(
                SpreadsheetValidators.optional(this.validator)
        );
        this.required = false;
        return this;
    }

    @Override
    public ParserSpreadsheetTextBox<T> required() {
        this.textBox.setValidator(this.validator);
        this.required = true;
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.required;
    }

    private boolean required;

    public ParserSpreadsheetTextBox<T> setValidator(final Validator<Optional<String>> validator) {
        this.textBox.setValidator(validator);
        this.validator = validator;
        return this;
    }

    private Validator<Optional<String>> validator;

    @Override
    public ParserSpreadsheetTextBox<T> validate() {
        this.textBox.validate();
        return this;
    }

    @Override
    public List<String> errors() {
        return this.textBox.errors();
    }

    @Override
    public ParserSpreadsheetTextBox<T> setErrors(final List<String> errors) {
        this.textBox.setErrors(errors);
        return this;
    }
    @Override
    public ParserSpreadsheetTextBox<T> focus() {
        this.textBox.focus();
        return this;
    }

    @Override
    public ParserSpreadsheetTextBox<T> alwaysShowHelperText() {
        this.textBox.alwaysShowHelperText();
        return this;
    }

    @Override
    public ParserSpreadsheetTextBox<T> setHelperText(final Optional<String> text) {
        this.textBox.setHelperText(text);
        return this;
    }

    @Override
    public Optional<String> helperText() {
        return this.textBox.helperText();
    }

    @Override
    public ParserSpreadsheetTextBox<T> hideMarginBottom() {
        this.textBox.hideMarginBottom();
        return this;
    }

    @Override
    public ParserSpreadsheetTextBox<T> removeBorders() {
        this.textBox.removeBorders();
        return this;
    }

    @Override
    public ParserSpreadsheetTextBox<T> addChangeListener(final ChangeListener<Optional<T>> listener) {
        Objects.requireNonNull(listener, "listener");

        this.textBox.addChangeListener(
                (final Optional<String> oldValue,
                 final Optional<String> newValue) ->
                        listener.onValueChanged(
                                tryParse(oldValue),
                                tryParse(newValue)
                        )
        );

        return this;
    }

    @Override
    public ParserSpreadsheetTextBox<T> addFocusListener(final EventListener listener) {
        this.textBox.addFocusListener(listener);
        return this;
    }

    @Override
    public ParserSpreadsheetTextBox<T> addKeydownListener(final EventListener listener) {
        this.textBox.addKeydownListener(listener);
        return this;
    }

    @Override
    public ParserSpreadsheetTextBox<T> addKeyupListener(final EventListener listener) {
        this.textBox.addKeyupListener(listener);
        return this;
    }

    // setCssText.......................................................................................................

    @Override
    public ParserSpreadsheetTextBox<T> setCssText(final String css) {
        this.textBox.setCssText(css);
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.textBox.element();
    }

    // Node.............................................................................................................

    @Override
    public Node node() {
        return this.textBox.node();
    }

    // Value............................................................................................................

    @Override
    public ParserSpreadsheetTextBox<T> setValue(final Optional<T> value) {
        Objects.requireNonNull(value, "value");

        this.textBox.setValue(
                value.map(HasText::text)
        );
        return this;
    }

    @Override //
    public Optional<T> value() {
        return tryParse(
                this.textBox.value()
        );
    }

    private Optional<T> tryParse(final Optional<String> text) {
        T parsed;
        try {
            parsed = this.parser.apply(
                    text.orElse("")
            );
        } catch (final Exception ignore) {
            parsed = null;
        }
        return Optional.ofNullable(parsed);
    }

    /**
     * Sets the given {@link String value} on the wrapped {@link org.dominokit.domino.ui.forms.TextBox} skipping any
     * validation or conversion.
     */
    public ParserSpreadsheetTextBox<T> setStringValue(final Optional<String> value) {
        this.textBox.setValue(value);
        return this;
    }

    /**
     * Returns the original entered text.
     */
    public Optional<String> stringValue() {
        return this.textBox.value();
    }

    private final SpreadsheetTextBox textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.textBox.printTree(printer);
        }
        printer.outdent();
    }
}
