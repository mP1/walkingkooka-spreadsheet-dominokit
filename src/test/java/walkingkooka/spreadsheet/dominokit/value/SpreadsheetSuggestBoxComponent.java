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
import org.dominokit.domino.ui.elements.SpanElement;
import org.dominokit.domino.ui.forms.suggest.SuggestOption;
import org.dominokit.domino.ui.forms.suggest.SuggestionsStore;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.ValidatorHelper;
import walkingkooka.text.HasText;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A text box component that includes support for suggestions using a {@link SuggestionsStore}.
 */
public final class SpreadsheetSuggestBoxComponent<T extends HasText> implements SpreadsheetSuggestBoxComponentLike<T>,
        TestHtmlElementComponent<HTMLFieldSetElement, walkingkooka.spreadsheet.dominokit.value.SpreadsheetSuggestBoxComponent<T>>,
        ValidatorHelper {

    public static <T extends HasText> walkingkooka.spreadsheet.dominokit.value.SpreadsheetSuggestBoxComponent<T> with(final Function<String, T> parser,
                                                                                                                      final SuggestionsStore<String, SpanElement, SuggestOption<String>> suggestionsStore) {
        Objects.requireNonNull(parser, "parser");
        Objects.requireNonNull(suggestionsStore, "suggestionsStore");

        return new walkingkooka.spreadsheet.dominokit.value.SpreadsheetSuggestBoxComponent<>(
                parser,
                suggestionsStore
        );
    }

    private SpreadsheetSuggestBoxComponent(final Function<String, T> parser,
                                           final SuggestionsStore<String, SpanElement, SuggestOption<String>> suggestionsStore) {
        this.helperText = Optional.empty();

        this.parser = parser;
        this.stringValue = Optional.empty();
        this.errors = Lists.empty();

        this.suggestionsStore = suggestionsStore;
        this.validator = null;
        this.required();
        this.validate();
    }

    // id...............................................................................................................

    @Override
    public walkingkooka.spreadsheet.dominokit.value.SpreadsheetSuggestBoxComponent<T> setId(final String id) {
        this.id = id;
        return this;
    }

    @Override
    public String id() {
        return this.id;
    }

    private String id;

    // label............................................................................................................

    @Override
    public walkingkooka.spreadsheet.dominokit.value.SpreadsheetSuggestBoxComponent<T> setLabel(final String label) {
        this.label = label;
        return this;
    }

    @Override
    public String label() {
        return this.label;
    }

    private String label;

    // helperText.......................................................................................................

    @Override
    public walkingkooka.spreadsheet.dominokit.value.SpreadsheetSuggestBoxComponent<T> alwaysShowHelperText() {
        return this;
    }

    @Override
    public walkingkooka.spreadsheet.dominokit.value.SpreadsheetSuggestBoxComponent<T> setHelperText(final Optional<String> text) {
        Objects.requireNonNull(text, "text");

        this.helperText = text;
        return this;
    }

    @Override
    public Optional<String> helperText() {
        return this.helperText;
    }

    private Optional<String> helperText;

    // StringValue......................................................................................................

    @Override
    public walkingkooka.spreadsheet.dominokit.value.SpreadsheetSuggestBoxComponent<T> setStringValue(final Optional<String> value) {
        Objects.requireNonNull(value, "value");

        this.stringValue = value;
        this.validate();
        return this;
    }

    public Optional<String> stringValue() {
        return this.stringValue;
    }

    private Optional<String> stringValue;

    // Value............................................................................................................

    @Override
    public walkingkooka.spreadsheet.dominokit.value.SpreadsheetSuggestBoxComponent<T> setValue(final Optional<T> value) {
        Objects.requireNonNull(value, "value");

        return this.setStringValue(
                value.map(HasText::text)
        );
    }

    @Override //
    public Optional<T> value() {
        return tryParse(
                this.stringValue.orElse("")
        );
    }

    private Optional<T> tryParse(final String string) {
        T value;

        try {
            value = this.parser.apply(string);
        } catch (final Exception ignore) {
            value = null;
        }

        return Optional.ofNullable(value);
    }

    private final Function<String, T> parser;

    // isDisabled.......................................................................................................

    @Override
    public boolean isDisabled() {
        return this.disabled;
    }

    @Override
    public walkingkooka.spreadsheet.dominokit.value.SpreadsheetSuggestBoxComponent<T> setDisabled(final boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    private boolean disabled;

    // validation.......................................................................................................

    @Override
    public walkingkooka.spreadsheet.dominokit.value.SpreadsheetSuggestBoxComponent<T> optional() {
        this.required = false;
        return this.setValidator(
                SpreadsheetValidators.optional(
                        SpreadsheetValidators.tryCatch(
                                this.parser::apply
                        )
                )
        );
    }

    @Override
    public walkingkooka.spreadsheet.dominokit.value.SpreadsheetSuggestBoxComponent<T> required() {
        this.required = true;
        return this.setValidator(
                SpreadsheetValidators.tryCatch(
                        this.parser::apply
                )
        );
    }

    private walkingkooka.spreadsheet.dominokit.value.SpreadsheetSuggestBoxComponent<T> setValidator(final Validator<Optional<String>> validator) {
        this.validator = validator;
        return this.validate();
    }

    private Validator<Optional<String>> validator;

    @Override
    public boolean isRequired() {
        return this.required;
    }

    boolean required;

    @Override
    public walkingkooka.spreadsheet.dominokit.value.SpreadsheetSuggestBoxComponent<T> validate() {
        this.setErrors(
                this.validateAndGetErrors(
                        this.stringValue,
                        Optional.ofNullable(
                                this.validator
                        )
                )
        );
        return this;
    }

    @Override
    public List<String> errors() {
        return this.errors;
    }

    @Override
    public walkingkooka.spreadsheet.dominokit.value.SpreadsheetSuggestBoxComponent<T> setErrors(final List<String> errors) {
        this.errors = Lists.immutable(errors);
        return this;
    }

    private List<String> errors;

    private final SuggestionsStore<String, SpanElement, SuggestOption<String>> suggestionsStore;

    // events...........................................................................................................

    @Override
    public walkingkooka.spreadsheet.dominokit.value.SpreadsheetSuggestBoxComponent<T> addChangeListener(final ChangeListener<Optional<T>> listener) {
        Objects.requireNonNull(listener, "listener");

        return this;
    }

    @Override
    public walkingkooka.spreadsheet.dominokit.value.SpreadsheetSuggestBoxComponent<T> addFocusListener(final EventListener listener) {

        return this;
    }

    @Override
    public walkingkooka.spreadsheet.dominokit.value.SpreadsheetSuggestBoxComponent<T> addKeydownListener(final EventListener listener) {
        Objects.requireNonNull(listener, "listener");

        return this;
    }

    @Override
    public walkingkooka.spreadsheet.dominokit.value.SpreadsheetSuggestBoxComponent<T> addKeyupListener(final EventListener listener) {
        Objects.requireNonNull(listener, "listener");

        return this;
    }

    // focus............................................................................................................

    @Override
    public walkingkooka.spreadsheet.dominokit.value.SpreadsheetSuggestBoxComponent<T> focus() {
        return this;
    }

    // styling..........................................................................................................

    @Override
    public walkingkooka.spreadsheet.dominokit.value.SpreadsheetSuggestBoxComponent<T> hideMarginBottom() {
        return this;
    }

    @Override
    public walkingkooka.spreadsheet.dominokit.value.SpreadsheetSuggestBoxComponent<T> removeBorders() {
        return this;
    }

    // SpreadsheetTextBoxTreePrintable..................................................................................

    @Override
    public void treePrintAlternateValues(final IndentingPrinter printer) {
        // NOP
    }
}
