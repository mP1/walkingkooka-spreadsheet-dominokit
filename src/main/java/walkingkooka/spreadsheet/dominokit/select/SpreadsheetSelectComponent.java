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

package walkingkooka.spreadsheet.dominokit.select;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.forms.suggest.Select;
import org.dominokit.domino.ui.forms.suggest.SelectOption;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.text.SpreadsheetTextBoxTreePrintable;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A select component with a few helpers to assist with build and working with values.
 */
public final class SpreadsheetSelectComponent<T> implements FormValueComponent<HTMLFieldSetElement, T, SpreadsheetSelectComponent<T>>,
    SpreadsheetTextBoxTreePrintable<SpreadsheetSelectComponent<T>, T> {

    public static <T> SpreadsheetSelectComponent<T> empty() {
        return new SpreadsheetSelectComponent<>();
    }

    private SpreadsheetSelectComponent() {
        this.select = Select.create();
        this.select.setAutoValidation(true);
        this.select.addValidator(SpreadsheetSelectComponentValidator.with(this));
    }

    // id...............................................................................................................

    @Override
    public SpreadsheetSelectComponent<T> setId(final String id) {
        this.select.setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.select.getId();
    }

    // label............................................................................................................

    @Override
    public SpreadsheetSelectComponent<T> setLabel(final String label) {
        this.select.setLabel(label);
        return this;
    }

    @Override
    public String label() {
        return this.select.getLabel();
    }

    // helperText.......................................................................................................

    @Override
    public SpreadsheetSelectComponent<T> alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetSelectComponent<T> setHelperText(final Optional<String> text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<String> helperText() {
        return Optional.empty();
    }

    // Value............................................................................................................

    /**
     * Appends a new value to the drop down.
     */
    public SpreadsheetSelectComponent<T> appendOption(final String text,
                                                      final T value) {
        Objects.requireNonNull(text, "text");
        Objects.requireNonNull(value, "value");

        return this.appendOption(
            text,
            Optional.of(value)
        );
    }

    /**
     * Appends a new value to the drop down.
     */
    public SpreadsheetSelectComponent<T> appendOption(final String text,
                                                      final Optional<T> value) {
        Objects.requireNonNull(text, "text");
        Objects.requireNonNull(value, "value");

        this.select.appendChild(
            SelectOption.create(
                value.orElse(null),
                text
            )
        );
        return this;
    }

    public SpreadsheetSelectComponent<T> clearOptions() {
        this.select.removeAllOptions();
        return this;
    }

    @Override
    public SpreadsheetSelectComponent<T> setValue(final Optional<T> value) {
        Objects.requireNonNull(value, "value");

        this.select.setValue(
            value.orElse(null)
        );
        return this;
    }

    @Override //
    public Optional<T> value() {
        return Optional.ofNullable(
            this.select.getValue()
        );
    }

    // validation.......................................................................................................

    @Override
    public SpreadsheetSelectComponent<T> optional() {
        this.required = false;
        return this;
    }

    @Override
    public SpreadsheetSelectComponent<T> required() {
        this.required = true;
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.required;
    }

    boolean required;

    @Override
    public boolean isDisabled() {
        return this.select.isDisabled();
    }

    @Override
    public SpreadsheetSelectComponent<T> setDisabled(final boolean disabled) {
        this.select.setDisabled(disabled);
        return this;
    }

    @Override
    public SpreadsheetSelectComponent<T> validate() {
        this.select.validate();
        return this;
    }

    @Override
    public List<String> errors() {
        return this.select.getErrors();
    }

    @Override
    public SpreadsheetSelectComponent<T> setErrors(final List<String> errors) {
        Objects.requireNonNull(errors, "errors");

        this.select.invalidate(
            Lists.immutable(errors)
        );
        return this;
    }

    // events...........................................................................................................

    @Override
    public SpreadsheetSelectComponent<T> addChangeListener(final ChangeListener<Optional<T>> listener) {
        Objects.requireNonNull(listener, "listener");

        this.select.addChangeListener(
            (oldValue, newValue) -> listener.onValueChanged(
                Optional.ofNullable(oldValue),
                Optional.ofNullable(newValue)
            )
        );
        return this;
    }

    @Override
    public SpreadsheetSelectComponent<T> addClickListener(final EventListener listener) {
        this.select.addEventListener(
            EventType.click,
            listener
        );
        return this;
    }

    @Override
    public SpreadsheetSelectComponent<T> addFocusListener(final EventListener listener) {
        this.select.addEventListener(
            EventType.focus,
            listener
        );
        return this;
    }

    @Override
    public SpreadsheetSelectComponent<T> addKeydownListener(final EventListener listener) {
        Objects.requireNonNull(listener, "listener");

        this.select.addEventListener(
            EventType.keydown,
            listener
        );
        return this;
    }

    @Override
    public SpreadsheetSelectComponent<T> addKeyupListener(final EventListener listener) {
        Objects.requireNonNull(listener, "listener");

        this.select.addEventListener(
            EventType.keyup,
            listener
        );
        return this;
    }

    // focus............................................................................................................

    @Override
    public SpreadsheetSelectComponent<T> focus() {
        this.select.focus();
        return this;
    }

    // styling..........................................................................................................

    @Override
    public SpreadsheetSelectComponent<T> hideMarginBottom() {
        this.select.setMarginBottom("");
        return this;
    }

    @Override
    public SpreadsheetSelectComponent<T> removeBorders() {
        this.select.getInputElement()
            .parent()
            .setBorder("0")
            .setCssProperty("border-radius", 0);
        return this;
    }

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetSelectComponent<T> setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.select.cssText(css);
        return this;
    }

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetSelectComponent<T> setCssProperty(final String name,
                                                        final String value) {
        this.select.setCssProperty(
            name,
            value
        );
        return this;
    }

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        final Select<T> select = this.select;

        return select.isExpanded() ||
            HtmlElementComponent.hasFocus(
                select.element()
            );
    }

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.select.element();
    }

    private final Select<T> select;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.select.toString();
    }

    // SpreadsheetTextBoxTreePrintable..................................................................................

    @Override
    public void treePrintAlternateValues(final IndentingPrinter printer) {
        printer.indent();
        {
            // Not sure what this will print...
            TreePrintable.printTreeOrToString(
                this.select.getOptionsMenu(),
                printer
            );
        }
        printer.outdent();
    }
}
