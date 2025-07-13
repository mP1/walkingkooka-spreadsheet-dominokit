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
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.elements.SpanElement;
import org.dominokit.domino.ui.forms.suggest.SuggestOption;
import org.dominokit.domino.ui.forms.suggest.SuggestionsStore;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ValidatorHelper;
import walkingkooka.spreadsheet.dominokit.validator.SpreadsheetValidators;
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
    TestHtmlElementComponent<HTMLFieldSetElement, SpreadsheetSuggestBoxComponent<T>>,
    ValidatorHelper {

    public static <T extends HasText> SpreadsheetSuggestBoxComponent<T> with(final Function<String, T> parser,
                                                                                                                           final SuggestionsStore<String, SpanElement, SuggestOption<String>> suggestionsStore) {
        Objects.requireNonNull(parser, "parser");
        Objects.requireNonNull(suggestionsStore, "suggestionsStore");

        return new SpreadsheetSuggestBoxComponent<>(
            parser,
            suggestionsStore
        );
    }

    private SpreadsheetSuggestBoxComponent(final Function<String, T> parser,
                                           final SuggestionsStore<String, SpanElement, SuggestOption<String>> suggestionsStore) {
        this.helperText = Optional.empty();

        this.value = Optional.empty();
        this.errors = Lists.empty();

        this.validator = null;
        this.required();
        this.validate();
    }

    // id...............................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> setId(final String id) {
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
    public SpreadsheetSuggestBoxComponent<T> setLabel(final String label) {
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
    public SpreadsheetSuggestBoxComponent<T> alwaysShowHelperText() {
        return this;
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> setHelperText(final Optional<String> text) {
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
    public SpreadsheetSuggestBoxComponent<T> setStringValue(final Optional<String> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<String> stringValue() {
        return this.value.map(HasText::text);
    }

    // Value............................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> setValue(final Optional<T> value) {
        Objects.requireNonNull(value, "value");

        this.value = value;
        this.validate();
        return this;
    }

    @Override //
    public Optional<T> value() {
        return this.value;
    }

    private Optional<T> value;

    // isDisabled.......................................................................................................

    @Override
    public boolean isDisabled() {
        return this.disabled;
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> setDisabled(final boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    private boolean disabled;

    // validation.......................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> optional() {
        this.required = false;
        return this.setValidator(
            SpreadsheetValidators.optional(
                SpreadsheetValidators.required()
            )
        );
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> required() {
        this.required = true;
        return this.setValidator(
            SpreadsheetValidators.required()
        );
    }

    private SpreadsheetSuggestBoxComponent<T> setValidator(final Validator<Optional<T>> validator) {
        this.validator = validator;
        return this.validate();
    }

    private Validator<Optional<T>> validator;

    @Override
    public boolean isRequired() {
        return this.required;
    }

    boolean required;

    @Override
    public SpreadsheetSuggestBoxComponent<T> validate() {
        this.setErrors(
            this.validateAndGetErrors(
                this.value,
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
    public SpreadsheetSuggestBoxComponent<T> setErrors(final List<String> errors) {
        this.errors = Lists.immutable(errors);
        return this;
    }

    private List<String> errors;

    // events...........................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> addChangeListener(final ChangeListener<Optional<T>> listener) {
        Objects.requireNonNull(listener, "listener");

        return this;
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> addClickListener(final EventListener listener) {
        Objects.requireNonNull(listener, "listener");

        return this;
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> addFocusListener(final EventListener listener) {

        return this;
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> addKeydownListener(final EventListener listener) {
        Objects.requireNonNull(listener, "listener");

        return this;
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> addKeyupListener(final EventListener listener) {
        Objects.requireNonNull(listener, "listener");

        return this;
    }

    // focus............................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> focus() {
        return this;
    }

    // styling..........................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> hideMarginBottom() {
        return this;
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> removeBorders() {
        return this;
    }

    // SpreadsheetTextBoxTreePrintable..................................................................................

    @Override
    public void treePrintAlternateValues(final IndentingPrinter printer) {
        // NOP
    }
}
