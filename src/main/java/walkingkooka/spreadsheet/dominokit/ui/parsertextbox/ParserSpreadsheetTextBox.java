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

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A text box that supports a typed value using a {@link Function} as a parser. Any thrown exception messages become
 * the validation fail messages.
 */
public final class ParserSpreadsheetTextBox<T> implements ValueComponent<HTMLFieldSetElement, T> {

    /**
     * Creates a new {@link ParserSpreadsheetTextBox}.
     * A {@link Function textExtractor} is required to convert types that require context such as an {@link walkingkooka.tree.expression.Expression}.
     */
    public static <T> ParserSpreadsheetTextBox<T> with(final Function<String, T> parser,
                                                       final Function<T, String> textExtractor) {
        Objects.requireNonNull(parser, "parser");
        Objects.requireNonNull(textExtractor, "textExtractor");

        return new ParserSpreadsheetTextBox<>(
                parser,
                textExtractor
        );
    }

    private ParserSpreadsheetTextBox(final Function<String, T> parser,
                                     final Function<T, String> textExtractor) {
        this.textBox = SpreadsheetTextBox.empty()
                .clearIcon()
                .disableSpellcheck()
                .enterFiresValueChange();
        this.parser = parser;
        this.textExtractor = textExtractor;
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
                value.map(this.textExtractor::apply)
        );
        return this;
    }

    private final Function<T, String> textExtractor;

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
