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
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.CanBeEmpty;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.dom.HasEventListeners;
import walkingkooka.spreadsheet.dominokit.text.TextBoxComponent;
import walkingkooka.spreadsheet.dominokit.validator.SpreadsheetValidators;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A text box that supports a typed value using a {@link Function} as a parser. Any thrown exception messages become
 * the validation fail messages. it is possible to replace the default validator mentioned above using {@link #setValidator(Validator)}.
 */
public final class ValueTextBoxComponent<T> implements FormValueComponent<HTMLFieldSetElement, T, ValueTextBoxComponent<T>>,
    HtmlComponentDelegator<HTMLFieldSetElement, ValueTextBoxComponent<T>>,
    HasEventListeners<T, ValueTextBoxComponent<T>> {

    /**
     * Creates a new {@link ValueTextBoxComponent}.
     */
    public static <T> ValueTextBoxComponent<T> with(final Function<String, T> parser,
                                                    final Function<T, String> formatter) {
        return new ValueTextBoxComponent<>(
            Objects.requireNonNull(parser, "parser"),
            Objects.requireNonNull(formatter, "formatter")
        );
    }

    private ValueTextBoxComponent(final Function<String, T> parser,
                                  final Function<T, String> formatter) {
        this.textBox = TextBoxComponent.empty()
            .autocompleteOff()
            .clearIcon()
            .disableSpellcheck()
            .enterFiresValueChange();
        this.setParser(parser);
        this.setFormatter(formatter);
        this.required();
    }

    /**
     * Sets a new {@link Function} will be used to parse String into values.
     */
    private ValueTextBoxComponent<T> setParser(final Function<String, T> parser) {
        Objects.requireNonNull(parser, "parser");

        this.parser = parser;
        this.setValidator(
            SpreadsheetValidators.tryCatch(parser::apply)
        );
        this.validate();
        return this;
    }

    private Function<String, T> parser;


    /**
     * Sets a new {@link Function} will be used to format values into text for editing
     */
    private ValueTextBoxComponent<T> setFormatter(final Function<T, String> formatter) {
        Objects.requireNonNull(formatter, "formatter");

        this.formatter = formatter;
        return this;
    }

    private Function<T, String> formatter;

    @Override
    public ValueTextBoxComponent<T> setId(final String id) {
        this.textBox.setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.textBox.id();
    }

    @Override
    public ValueTextBoxComponent<T> setLabel(final String label) {
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
    public ValueTextBoxComponent<T> setDisabled(final boolean disabled) {
        this.textBox.setDisabled(disabled);
        return this;
    }

    @Override
    public ValueTextBoxComponent<T> optional() {
        this.textBox.setValidator(
            SpreadsheetValidators.optional(this.validator)
        );
        this.required = false;
        return this;
    }

    @Override
    public ValueTextBoxComponent<T> required() {
        this.textBox.setValidator(this.validator);
        this.required = true;
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.required;
    }

    private boolean required;

    public Validator<Optional<String>> validator() {
        return this.validator;
    }

    public ValueTextBoxComponent<T> setValidator(final Validator<Optional<String>> validator) {
        this.textBox.setValidator(validator);
        this.validator = validator;
        return this;
    }

    private Validator<Optional<String>> validator;

    @Override
    public ValueTextBoxComponent<T> validate() {
        this.textBox.validate();
        return this;
    }

    @Override
    public List<String> errors() {
        return this.textBox.errors();
    }

    @Override
    public ValueTextBoxComponent<T> setErrors(final List<String> errors) {
        this.textBox.setErrors(errors);
        return this;
    }

    @Override
    public ValueTextBoxComponent<T> focus() {
        this.textBox.focus();
        return this;
    }

    @Override
    public ValueTextBoxComponent<T> blur() {
        this.textBox.blur();
        return this;
    }

    @Override
    public boolean isEditing() {
        return this.textBox.isEditing();
    }

    @Override
    public ValueTextBoxComponent<T> alwaysShowHelperText() {
        this.textBox.alwaysShowHelperText();
        return this;
    }

    @Override
    public ValueTextBoxComponent<T> setHelperText(final Optional<String> text) {
        this.textBox.setHelperText(text);
        return this;
    }

    @Override
    public Optional<String> helperText() {
        return this.textBox.helperText();
    }

    @Override
    public ValueTextBoxComponent<T> hideMarginBottom() {
        this.textBox.hideMarginBottom();
        return this;
    }

    @Override
    public ValueTextBoxComponent<T> removeBorders() {
        this.textBox.removeBorders();
        return this;
    }

    @Override
    public ValueTextBoxComponent<T> removePadding() {
        this.textBox.removePadding();
        return this;
    }

    @Override
    public ValueTextBoxComponent<T> addBlurListener(final EventListener listener) {
        this.textBox.addBlurListener(listener);
        return this;
    }

    @Override
    public ValueTextBoxComponent<T> addChangeListener(final ChangeListener<Optional<T>> listener) {
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
    public ValueTextBoxComponent<T> addClickListener(final EventListener listener) {
        this.textBox.addClickListener(listener);
        return this;
    }

    @Override
    public ValueTextBoxComponent<T> addContextMenuListener(final EventListener listener) {
        this.textBox.addContextMenuListener(listener);
        return this;
    }

    @Override
    public ValueTextBoxComponent<T> addFocusListener(final EventListener listener) {
        this.textBox.addFocusListener(listener);
        return this;
    }

    @Override
    public ValueTextBoxComponent<T> addInputListener(final EventListener listener) {
        this.textBox.addInputListener(listener);
        return this;
    }

    @Override
    public ValueTextBoxComponent<T> addKeyDownListener(final EventListener listener) {
        this.textBox.addKeyDownListener(listener);
        return this;
    }

    @Override
    public ValueTextBoxComponent<T> addKeyUpListener(final EventListener listener) {
        this.textBox.addKeyUpListener(listener);
        return this;
    }

    @Override
    public Runnable addValueWatcher(final ValueWatcher<T> watcher) {
        Objects.requireNonNull(watcher, "watcher");

        return this.textBox.addValueWatcher(
            (text) -> watcher.onValue(this.value())
        );
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLFieldSetElement, ?> htmlComponent() {
        return this.textBox;
    }

    // Value............................................................................................................

    @Override
    public ValueTextBoxComponent<T> setValue(final Optional<T> value) {
        Objects.requireNonNull(value, "value");

        this.textBox.setValue(
            value.map(this.formatter::apply)
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
        } catch (final UnsupportedOperationException rethrow) {
            throw rethrow;
        } catch (final Exception ignore) {
            parsed = null;
        }
        return Optional.ofNullable(
            null == parsed ||
                "".equals(parsed) ||
                isEmpty(parsed) ?
                null :
                parsed
        );
    }

    private static boolean isEmpty(final Object value) {
        final boolean empty;

        if (value instanceof CanBeEmpty) {
            final CanBeEmpty canBeEmpty = (CanBeEmpty) value;
            empty = canBeEmpty.isEmpty();
        } else {
            empty = false;
        }

        return empty;
    }

    /**
     * Sets the given {@link String value} on the wrapped {@link org.dominokit.domino.ui.forms.TextBox} skipping any
     * validation or conversion.
     */
    public ValueTextBoxComponent<T> setStringValue(final Optional<String> value) {
        this.textBox.setValue(value);
        return this;
    }

    /**
     * Returns the original entered text.
     */
    public Optional<String> stringValue() {
        return this.textBox.value();
    }

    private final TextBoxComponent textBox;

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
