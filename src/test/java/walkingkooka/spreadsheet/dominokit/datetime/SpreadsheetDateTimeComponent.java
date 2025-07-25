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

package walkingkooka.spreadsheet.dominokit.datetime;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ValidatorHelper;
import walkingkooka.spreadsheet.dominokit.text.SpreadsheetTextBoxTreePrintable;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A mock of main/SpreadsheetTextBox with the same public interface and a helpful {@link TreePrintable}. This will be useful for unit tests to verify the rough apperance of a component that includes
 * {@linkSpreadsheetDateTimeComponent}.
 */
public final class SpreadsheetDateTimeComponent implements FormValueComponent<HTMLFieldSetElement, LocalDateTime,SpreadsheetDateTimeComponent>,
    SpreadsheetTextBoxTreePrintable<SpreadsheetDateTimeComponent, LocalDateTime>,
    TestHtmlElementComponent<HTMLFieldSetElement,SpreadsheetDateTimeComponent>,
    ValidatorHelper {

    public  static SpreadsheetDateTimeComponent empty(final String id,
                                                                                                 final Supplier<LocalDateTime> clearValue) {
        return new SpreadsheetDateTimeComponent(
            id,
            clearValue
        );
    }

    private SpreadsheetDateTimeComponent(final String id,
                                         final Supplier<LocalDateTime> clearValue) {
        this.setId(id);
        this.clearValue = clearValue;
    }

    @Override
     public SpreadsheetDateTimeComponent setId(final String id) {
        CharSequences.failIfNullOrEmpty(id, "id");

        this.id = id;
        return this;
    }

    @Override
    public String id() {
        return this.id;
    }

    private String id;

    @Override
     public SpreadsheetDateTimeComponent setLabel(final String label) {
        this.label = label;
        return this;
    }

    @Override
    public String label() {
        return this.label;
    }

    private String label;

    @Override
     public SpreadsheetDateTimeComponent setValue(final Optional<LocalDateTime> value) {
        Objects.requireNonNull(value, "value");
        this.value = value.isPresent() ?
            value :
            Optional.of(this.clearValue.get());

        return validate();
    }

    private final Supplier<LocalDateTime> clearValue;

    @Override
    public Optional<LocalDateTime> value() {
        return this.value;
    }

    private Optional<LocalDateTime> value = Optional.empty();

    @Override
     public SpreadsheetDateTimeComponent focus() {
        return this;
    }

    @Override
     public SpreadsheetDateTimeComponent optional() {
        this.required = false;
        return this;
    }

    @Override
     public SpreadsheetDateTimeComponent required() {
        this.required = true;
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.required;
    }

    private boolean required;

     public SpreadsheetDateTimeComponent setValidator(final Validator<Optional<LocalDateTime>> validator) {
        this.validator = validator;
        return this;
    }

    private Validator<Optional<LocalDateTime>> validator;

    @Override
     public SpreadsheetDateTimeComponent validate() {
        this.setErrors(
            this.validateAndGetErrors(
                this.value,
                Optional.ofNullable(this.validator)
            )
        );
        return this;
    }

    @Override
    public List<String> errors() {
        return this.errors;
    }

    @Override
     public SpreadsheetDateTimeComponent setErrors(final List<String> errors) {
        Objects.requireNonNull(errors, "errors");
        this.errors = Lists.immutable(errors);
        return this;
    }

    private List<String> errors = Lists.empty();

    @Override
    public boolean isDisabled() {
        return this.disabled;
    }

    @Override
     public SpreadsheetDateTimeComponent setDisabled(final boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    private boolean disabled;

     public SpreadsheetDateTimeComponent resetView() {
        return this;
    }

    @Override
     public SpreadsheetDateTimeComponent addChangeListener(final ChangeListener<Optional<LocalDateTime>> listener) {
        return this;
    }

    @Override
     public SpreadsheetDateTimeComponent addClickListener(final EventListener listener) {
        return this;
    }

    @Override
     public SpreadsheetDateTimeComponent addFocusListener(final EventListener listener) {
        return this;
    }

    @Override
     public SpreadsheetDateTimeComponent addKeydownListener(final EventListener listener) {
        return this;
    }

    @Override
     public SpreadsheetDateTimeComponent addKeyupListener(final EventListener listener) {
        return this;
    }

    @Override
     public SpreadsheetDateTimeComponent alwaysShowHelperText() {
        return this;
    }

    @Override
     public SpreadsheetDateTimeComponent setHelperText(final Optional<String> text) {
        Objects.requireNonNull(text, "text");
        this.helperText = text;
        return this;
    }

    @Override public Optional<String> helperText() {
        return this.helperText;
    }

    private Optional<String> helperText = Optional.empty();

    @Override
     public SpreadsheetDateTimeComponent hideMarginBottom() {
        return this;
    }

    @Override
     public SpreadsheetDateTimeComponent removeBorders() {
        return this;
    }

     public SpreadsheetDateTimeComponent autocompleteOff() {
        return this;
    }

     public SpreadsheetDateTimeComponent clearIcon() {
        return this;
    }

     public SpreadsheetDateTimeComponent disableSpellcheck() {
        return this;
    }

     public SpreadsheetDateTimeComponent enterFiresValueChange() {
        return this;
    }

     public SpreadsheetDateTimeComponent magnifyingGlassIcon() {
        return this;
    }

    // FIXES
    //
    // java.lang.NoSuchMethodError: walkingkooka.spreadsheet.dominokit.value.SpreadsheetTextBox.setCssText(Ljava/lang/String;)Lwalkingkooka/spreadsheet/dominokit/ui/textbox/SpreadsheetTextBox;
    @Override
     public SpreadsheetDateTimeComponent setCssText(final String css) {
        Objects.requireNonNull(css, "css");
        return this;
    }

    // SpreadsheetTextBoxTreePrintable..................................................................................

    @Override
    public void treePrintAlternateValues(final IndentingPrinter printer) {
        // NOP
    }
}
