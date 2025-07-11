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

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A mock of main/SpreadsheetTimeComponent with the same public interface and a helpful {@link TreePrintable}.
 * This will be useful for rendering tests.
 */
public final class SpreadsheetTimeComponent implements FormValueComponent<HTMLFieldSetElement, LocalTime,SpreadsheetTimeComponent>,
    SpreadsheetTextBoxTreePrintable<SpreadsheetTimeComponent, LocalTime>,
    TestHtmlElementComponent<HTMLFieldSetElement,SpreadsheetTimeComponent>,
    ValidatorHelper {

    public  static SpreadsheetTimeComponent empty(final String id,
                                                                                             final Supplier<LocalTime> clearValue) {
        return new SpreadsheetTimeComponent(
            id,
            clearValue
        );
    }

    private SpreadsheetTimeComponent(final String id,
                                     final Supplier<LocalTime> clearValue) {
        this.setId(id);
        this.clearValue = clearValue;
    }

    @Override
     public SpreadsheetTimeComponent setId(final String id) {
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
     public SpreadsheetTimeComponent setLabel(final String label) {
        this.label = label;
        return this;
    }

    @Override
    public String label() {
        return this.label;
    }

    private String label;

    @Override
     public SpreadsheetTimeComponent setValue(final Optional<LocalTime> value) {
        Objects.requireNonNull(value, "value");
        this.value = value.isPresent() ?
            value :
            Optional.of(this.clearValue.get());

        return validate();
    }

    private final Supplier<LocalTime> clearValue;

    @Override
    public Optional<LocalTime> value() {
        return this.value;
    }

    private Optional<LocalTime> value = Optional.empty();

    @Override
     public SpreadsheetTimeComponent focus() {
        return this;
    }

    @Override
     public SpreadsheetTimeComponent optional() {
        this.required = false;
        return this;
    }

    @Override
     public SpreadsheetTimeComponent required() {
        this.required = true;
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.required;
    }

    private boolean required;

     public SpreadsheetTimeComponent setValidator(final Validator<Optional<LocalTime>> validator) {
        this.validator = validator;
        return this;
    }

    private Validator<Optional<LocalTime>> validator;

    @Override
     public SpreadsheetTimeComponent validate() {
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
     public SpreadsheetTimeComponent setErrors(final List<String> errors) {
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
     public SpreadsheetTimeComponent setDisabled(final boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    private boolean disabled;

    @Override
     public SpreadsheetTimeComponent addChangeListener(final ChangeListener<Optional<LocalTime>> listener) {
        return this;
    }

    @Override
     public SpreadsheetTimeComponent addClickListener(final EventListener listener) {
        return this;
    }

    @Override
     public SpreadsheetTimeComponent addFocusListener(final EventListener listener) {
        return this;
    }

    @Override
     public SpreadsheetTimeComponent addKeydownListener(final EventListener listener) {
        return this;
    }

    @Override
     public SpreadsheetTimeComponent addKeyupListener(final EventListener listener) {
        return this;
    }

    @Override
     public SpreadsheetTimeComponent alwaysShowHelperText() {
        return this;
    }

    @Override
     public SpreadsheetTimeComponent setHelperText(final Optional<String> text) {
        Objects.requireNonNull(text, "text");
        this.helperText = text;
        return this;
    }

    @Override public Optional<String> helperText() {
        return this.helperText;
    }

    private Optional<String> helperText = Optional.empty();

    @Override
     public SpreadsheetTimeComponent hideMarginBottom() {
        return this;
    }

    @Override
     public SpreadsheetTimeComponent removeBorders() {
        return this;
    }

     public SpreadsheetTimeComponent autocompleteOff() {
        return this;
    }

     public SpreadsheetTimeComponent clearIcon() {
        return this;
    }

     public SpreadsheetTimeComponent disableSpellcheck() {
        return this;
    }

     public SpreadsheetTimeComponent enterFiresValueChange() {
        return this;
    }

     public SpreadsheetTimeComponent magnifyingGlassIcon() {
        return this;
    }

    @Override
     public SpreadsheetTimeComponent setCssText(final String css) {
        Objects.requireNonNull(css, "css");
        return this;
    }

    // SpreadsheetTextBoxTreePrintable..................................................................................

    @Override
    public void treePrintAlternateValues(final IndentingPrinter printer) {
        // NOP
    }
}
