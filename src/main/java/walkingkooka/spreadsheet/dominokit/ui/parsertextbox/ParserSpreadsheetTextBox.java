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
import walkingkooka.spreadsheet.dominokit.ui.textbox.SpreadsheetTextBox;
import walkingkooka.spreadsheet.dominokit.ui.textbox.SpreadsheetTextBoxValidators;
import walkingkooka.text.HasText;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A text box that supports a typed value using a {@link Function} as a parser. Any thrown exception messages become
 * the validation fail messages.
 */
public final class ParserSpreadsheetTextBox<T extends HasText> implements ValueComponent<HTMLFieldSetElement, T> {

    /**
     * Creates a new {@link ParserSpreadsheetTextBox}.
     */
    public static <T extends HasText> ParserSpreadsheetTextBox<T> with(final Function<String, T> parser) {
        Objects.requireNonNull(parser, "parser");

        return new ParserSpreadsheetTextBox<>(parser);
    }

    private ParserSpreadsheetTextBox(final Function<String, T> parser) {
        this.textBox = SpreadsheetTextBox.empty()
                .clearIcon()
                .disableSpellcheck()
                .enterFiresValueChange();
        this.parser = parser;
        this.required();
    }

    @Override
    public ParserSpreadsheetTextBox<T> setId(final String id) {
        this.textBox.setId(id);
        return this;
    }

    @Override
    public ParserSpreadsheetTextBox<T> setLabel(final String label) {
        this.textBox.setLabel(label);
        return this;
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
                SpreadsheetTextBoxValidators.optional(
                        SpreadsheetTextBoxValidators.parser(this.parser::apply)
                )
        );
        return this;
    }

    @Override
    public ParserSpreadsheetTextBox<T> required() {
        this.textBox.setValidator(
                SpreadsheetTextBoxValidators.parser(this.parser::apply)
        );
        return this;
    }

    @Override
    public ParserSpreadsheetTextBox<T> validate() {
        this.textBox.validate();
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

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.textBox.element();
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

    private final Function<String, T> parser;

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
}
