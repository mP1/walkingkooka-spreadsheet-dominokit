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
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTreePrintable;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A mock of main/TextBoxComponent with the same public interface and a helpful {@link TreePrintable}. This will be useful for unit tests to verify the rough apperance of a component that includes
 * {@link DateComponent}.
 */
public final class DateComponent implements FormValueComponent<HTMLFieldSetElement, LocalDate, DateComponent>,
    FormValueComponentTreePrintable<HTMLFieldSetElement, DateComponent, LocalDate>,
    TestHtmlElementComponent<HTMLFieldSetElement, DateComponent>,
    ValidatorHelper {

    public static DateComponent empty(final String id,
                                      final Supplier<LocalDate> clearValue) {
        return new DateComponent(
            id,
            clearValue
        );
    }

    private DateComponent(final String id,
                          final Supplier<LocalDate> clearValue) {
        this.setId(id);
        this.clearValue = clearValue;
    }

    @Override
    public DateComponent setId(final String id) {
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
    public DateComponent setLabel(final String label) {
        this.label = label;
        return this;
    }

    @Override
    public String label() {
        return this.label;
    }

    private String label;

    @Override
    public DateComponent setValue(final Optional<LocalDate> value) {
        Objects.requireNonNull(value, "value");
        this.value = value.isPresent() ?
            value :
            Optional.of(this.clearValue.get());

        return validate();
    }

    private final Supplier<LocalDate> clearValue;

    @Override
    public Optional<LocalDate> value() {
        return this.value;
    }

    private Optional<LocalDate> value = Optional.empty();

    @Override
    public DateComponent focus() {
        return this;
    }

    @Override
    public DateComponent optional() {
        this.required = false;
        return this;
    }

    @Override
    public DateComponent required() {
        this.required = true;
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.required;
    }

    private boolean required;

    public DateComponent setValidator(final Validator<Optional<LocalDate>> validator) {
        this.validator = validator;
        return this;
    }

    private Validator<Optional<LocalDate>> validator;

    @Override
    public DateComponent validate() {
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
    public DateComponent setErrors(final List<String> errors) {
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
    public DateComponent setDisabled(final boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    private boolean disabled;

    public DateComponent resetView() {
        return this;
    }

    @Override
    public DateComponent addChangeListener(final ChangeListener<Optional<LocalDate>> listener) {
        return this;
    }

    @Override
    public DateComponent addClickListener(final EventListener listener) {
        return this;
    }

    @Override
    public DateComponent addContextMenuListener(final EventListener listener) {
        return this;
    }

    @Override
    public DateComponent addFocusListener(final EventListener listener) {
        return this;
    }

    @Override
    public DateComponent addKeydownListener(final EventListener listener) {
        return this;
    }

    @Override
    public DateComponent addKeyupListener(final EventListener listener) {
        return this;
    }

    @Override
    public DateComponent alwaysShowHelperText() {
        return this;
    }

    @Override
    public DateComponent setHelperText(final Optional<String> text) {
        Objects.requireNonNull(text, "text");
        this.helperText = text;
        return this;
    }

    @Override public Optional<String> helperText() {
        return this.helperText;
    }

    private Optional<String> helperText = Optional.empty();

    @Override
    public DateComponent hideMarginBottom() {
        return this;
    }

    @Override
    public DateComponent removeBorders() {
        return this;
    }

    public DateComponent autocompleteOff() {
        return this;
    }

    public DateComponent clearIcon() {
        return this;
    }

    public DateComponent disableSpellcheck() {
        return this;
    }

    public DateComponent enterFiresValueChange() {
        return this;
    }

    public DateComponent magnifyingGlassIcon() {
        return this;
    }

    // FIXES
    //
    // java.lang.NoSuchMethodError: walkingkooka.spreadsheet.dominokit.value.TextBoxComponent.setCssText(Ljava/lang/String;)Lwalkingkooka/spreadsheet/dominokit/ui/textbox/TextBoxComponent;
    @Override
    public DateComponent setCssText(final String css) {
        Objects.requireNonNull(css, "css");
        return this;
    }

    // FormValueComponentTreePrintable..................................................................................

    @Override
    public void treePrintAlternateValues(final IndentingPrinter printer) {
        // NOP
    }
}
