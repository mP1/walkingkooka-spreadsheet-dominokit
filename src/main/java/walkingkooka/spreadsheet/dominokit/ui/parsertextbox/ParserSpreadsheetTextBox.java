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

import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.Value;
import walkingkooka.spreadsheet.dominokit.ui.TextBoxStringParserValidator;
import walkingkooka.spreadsheet.dominokit.ui.textbox.SpreadsheetTextBox;
import walkingkooka.text.HasText;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A text box that supports a typed value using a {@link Function} as a parser. Any thrown exception messages become
 * the validation fail messages.
 */
public class ParserSpreadsheetTextBox<T extends HasText> implements IsElement<HTMLFieldSetElement>,
        Value<Optional<T>> {

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
                .enterFiresValueChange()
                .setValidator(TextBoxStringParserValidator.with(parser::apply));
        this.parser = parser;
    }

    public ParserSpreadsheetTextBox setId(final String id) {
        this.textBox.setId(id);
        return this;
    }

    public ParserSpreadsheetTextBox setLabel(final String label) {
        this.textBox.setLabel(label);
        return this;
    }

    public void focus() {
        this.textBox.focus();
    }

    public ParserSpreadsheetTextBox addChangeListener(final ChangeListener<Optional<T>> listener) {
        this.textBox.addChangeListener(
                (final String oldValue,
                 final String newValue) -> {
                    listener.onValueChanged(
                            tryParse(oldValue),
                            tryParse(newValue)
                    );
                }
        );

        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.textBox.element();
    }

    // Value............................................................................................................

    public void setValue(final Optional<T> value) {
        Objects.requireNonNull(value, "value");

        this.textBox.setValue(
                value.map(T::text)
                        .orElse("")
        );
    }

    @Override //
    public Optional<T> value() {
        return tryParse(
                this.textBox.value()
        );
    }

    private Optional<T> tryParse(final String text) {
        T value;

        try {
            value = parser.apply(text);
        } catch (final Exception ignore) {
            value = null;
        }

        return Optional.ofNullable(value);
    }

    private final Function<String, T> parser;

    private final SpreadsheetTextBox textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}
