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

package walkingkooka.spreadsheet.dominokit.checkbox;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.HasValueWatchers;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.validation.ValidationCheckbox;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A checkbox that uses {@link ValidationCheckbox} values
 */
public final class ValidationCheckboxComponent implements FormValueComponent<HTMLFieldSetElement, Object, ValidationCheckboxComponent>,
    HtmlComponentDelegator<HTMLFieldSetElement, ValidationCheckboxComponent>,
    HasValueWatchers<HTMLFieldSetElement, Object, ValidationCheckboxComponent> {

    public static ValidationCheckboxComponent empty(final String id,
                                                    final ValidationCheckboxComponentContext context) {
        return new ValidationCheckboxComponent(
            CharSequences.failIfNullOrEmpty(id, id),
            Objects.requireNonNull(context, "context")
        );
    }

    private ValidationCheckboxComponent(final String id,
                                        final ValidationCheckboxComponentContext context) {
        this.checkbox = CheckboxComponent.empty();

        this.setId(id);
        this.validationCheckbox = ValidationCheckbox.TRUE_FALSE;
    }

    // CheckboxComponent................................................................................................

    @Override
    public ValidationCheckboxComponent setId(final String id) {
        this.checkbox.setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.checkbox.id();
    }

    @Override
    public ValidationCheckboxComponent setLabel(final String label) {
        this.checkbox.setLabel(label);
        return this;
    }

    @Override
    public String label() {
        return this.checkbox.label();
    }

    @Override
    public boolean isDisabled() {
        return this.checkbox.isDisabled();
    }

    @Override
    public ValidationCheckboxComponent setDisabled(final boolean disabled) {
        this.checkbox.setDisabled(disabled);
        return this;
    }

    @Override
    public ValidationCheckboxComponent optional() {
        this.checkbox.optional();
        return this;
    }

    @Override
    public ValidationCheckboxComponent required() {
        this.checkbox.required();
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.checkbox.isRequired();
    }

    @Override
    public ValidationCheckboxComponent validate() {
        this.checkbox.validate();
        return this;
    }

    @Override
    public List<String> errors() {
        return this.checkbox.errors();
    }

    @Override
    public ValidationCheckboxComponent setErrors(final List<String> errors) {
        this.checkbox.setErrors(errors);
        return this;
    }

    @Override
    public ValidationCheckboxComponent focus() {
        this.checkbox.focus();
        return this;
    }

    @Override
    public ValidationCheckboxComponent alwaysShowHelperText() {
        this.checkbox.alwaysShowHelperText();
        return this;
    }

    @Override
    public ValidationCheckboxComponent setHelperText(final Optional<String> text) {
        this.checkbox.setHelperText(text);
        return this;
    }

    @Override
    public Optional<String> helperText() {
        return this.checkbox.helperText();
    }

    @Override
    public ValidationCheckboxComponent hideMarginBottom() {
        this.checkbox.hideMarginBottom();
        return this;
    }

    @Override
    public ValidationCheckboxComponent removeBorders() {
        this.checkbox.removeBorders();
        return this;
    }

    @Override
    public ValidationCheckboxComponent removePadding() {
        this.checkbox.removePadding();
        return this;
    }

    @Override
    public ValidationCheckboxComponent addBlurListener(final EventListener listener) {
        this.checkbox.addBlurListener(listener);
        return this;
    }

    @Override
    public ValidationCheckboxComponent addClickListener(final EventListener listener) {
        this.checkbox.addClickListener(listener);
        return this;
    }

    @Override
    public ValidationCheckboxComponent addChangeListener(final ChangeListener<Optional<Object>> listener) {
        this.checkbox.addChangeListener(
            (final Optional<Boolean> oldValue,
             final Optional<Boolean> newValue) -> listener.onValueChanged(
                toValue(oldValue),
                toValue(newValue)
            )
        );
        return this;
    }

    private Optional<Object> toValue(final Optional<Boolean> value) {
        return value.orElse(Boolean.FALSE) ?
            this.validationCheckbox.trueValue() :
            this.validationCheckbox.falseValue();
    }

    @Override
    public ValidationCheckboxComponent addContextMenuListener(final EventListener listener) {
        this.checkbox.addContextMenuListener(listener);
        return this;
    }

    @Override
    public ValidationCheckboxComponent addFocusListener(final EventListener listener) {
        this.checkbox.addFocusListener(listener);
        return this;
    }

    @Override
    public ValidationCheckboxComponent addInputListener(final EventListener listener) {
        this.checkbox.addInputListener(listener);
        return this;
    }

    @Override
    public ValidationCheckboxComponent addKeyDownListener(final EventListener listener) {
        this.checkbox.addKeyDownListener(listener);
        return this;
    }

    @Override
    public ValidationCheckboxComponent addKeyUpListener(final EventListener listener) {
        this.checkbox.addKeyUpListener(listener);
        return this;
    }

    // ValidationCheckbox...............................................................................................

    public ValidationCheckbox validationCheckbox() {
        return this.validationCheckbox;
    }

    public ValidationCheckboxComponent setValidationCheckbox(final ValidationCheckbox validationCheckbox) {
        Objects.requireNonNull(validationCheckbox, "validationCheckbox");

        this.validationCheckbox = validationCheckbox;

        return this;
    }

    private ValidationCheckbox validationCheckbox;

    // Value............................................................................................................

    /**
     * Copies the value to the {@link ValidationCheckbox}.
     */
    @Override
    public ValidationCheckboxComponent setValue(final Optional<Object> value) {
        Objects.requireNonNull(value, "value");

        this.checkbox.setValue(
            Optional.of(
                this.validationCheckbox.trueValue()
                    .equals(value)
            )
        );
        return this;
    }

    @Override //
    public Optional<Object> value() {
        final ValidationCheckbox value = this.validationCheckbox;

        return this.checkbox.value()
            .orElse(Boolean.FALSE) ?
            value.trueValue() :
            value.falseValue();
    }

    // HasValueWatchers.................................................................................................

    @Override
    public Runnable addValueWatcher(final ValueWatcher<Object> watcher) {
        Objects.requireNonNull(watcher, "watcher");

        return this.checkbox.addValueWatcher(
            (final Optional<Boolean> value) -> watcher.onValue(
                toValue(value)
            )
        );
    }

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return this.checkbox.isEditing();
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLFieldSetElement, ?> htmlComponent() {
        return this.checkbox;
    }

    private final CheckboxComponent checkbox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.checkbox.toString();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.checkbox.printTree(printer);
        }
        printer.outdent();
    }
}