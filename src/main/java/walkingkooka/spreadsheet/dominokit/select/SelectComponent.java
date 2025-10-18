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
import org.dominokit.domino.ui.menu.MenuItem;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTreePrintable;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A select component with a few helpers to assist with build and working with values.
 */
public final class SelectComponent<T> implements FormValueComponent<HTMLFieldSetElement, T, SelectComponent<T>>,
    FormValueComponentTreePrintable<HTMLFieldSetElement, SelectComponent<T>, T> {

    public static <T> SelectComponent<T> empty(final Function<Optional<T>, SelectOption<T>> selectCreator) {
        return new SelectComponent<>(
            Objects.requireNonNull(selectCreator, "selectCreator")
        );
    }

    private SelectComponent(final Function<Optional<T>, SelectOption<T>> selectCreator) {
        this.select = Select.create();
        this.select.setAutoValidation(true);
        this.select.addValidator(SelectComponentValidator.with(this));

        this.selectCreator = selectCreator;
    }

    // id...............................................................................................................

    @Override
    public SelectComponent<T> setId(final String id) {
        this.select.setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.select.getId();
    }

    // label............................................................................................................

    @Override
    public SelectComponent<T> setLabel(final String label) {
        this.select.setLabel(label);
        return this;
    }

    @Override
    public String label() {
        return this.select.getLabel();
    }

    // helperText.......................................................................................................

    @Override
    public SelectComponent<T> alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SelectComponent<T> setHelperText(final Optional<String> text) {
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
    public SelectComponent<T> appendOption(final Optional<T> value) {
        this.select.appendChild(
            this.selectCreator.apply(
                Objects.requireNonNull(value, "value")
            )
        );

        return this;
    }

    /**
     * Factory that creates the {@link MenuItem}, for each option.
     */
    private final Function<Optional<T>, SelectOption<T>> selectCreator;

    public SelectComponent<T> clearOptions() {
        this.select.removeAllOptions();
        return this;
    }

    @Override
    public SelectComponent<T> setValue(final Optional<T> value) {
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
    public SelectComponent<T> optional() {
        this.required = false;
        return this;
    }

    @Override
    public SelectComponent<T> required() {
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
    public SelectComponent<T> setDisabled(final boolean disabled) {
        this.select.setDisabled(disabled);
        return this;
    }

    @Override
    public SelectComponent<T> validate() {
        this.select.validate();
        return this;
    }

    @Override
    public List<String> errors() {
        return this.select.getErrors();
    }

    @Override
    public SelectComponent<T> setErrors(final List<String> errors) {
        Objects.requireNonNull(errors, "errors");

        this.select.invalidate(
            Lists.immutable(errors)
        );
        return this;
    }

    // events...........................................................................................................

    @Override
    public SelectComponent<T> addBlurListener(final EventListener listener) {
        return this.addEventListener(
            EventType.blur,
            listener
        );
    }

    @Override
    public SelectComponent<T> addChangeListener(final ChangeListener<Optional<T>> listener) {
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
    public SelectComponent<T> addClickListener(final EventListener listener) {
        return this.addEventListener(
            EventType.click,
            listener
        );
    }

    @Override
    public SelectComponent<T> addContextMenuListener(final EventListener listener) {
        return this.addEventListener(
            EventType.contextmenu,
            listener
        );
    }

    @Override
    public SelectComponent<T> addFocusListener(final EventListener listener) {
        return this.addEventListener(
            EventType.focus,
            listener
        );
    }

    @Override
    public SelectComponent<T> addKeyDownListener(final EventListener listener) {
        return this.addEventListener(
            EventType.keydown,
            listener
        );
    }

    @Override
    public SelectComponent<T> addKeyUpListener(final EventListener listener) {
        return this.addEventListener(
            EventType.keyup,
            listener
        );
    }

    private SelectComponent<T> addEventListener(final EventType type,
                                                final EventListener listener) {
        this.select.addEventListener(
            type,
            Objects.requireNonNull(listener, "listener")
        );
        return this;
    }

    // focus............................................................................................................

    @Override
    public SelectComponent<T> focus() {
        this.select.focus();
        return this;
    }

    // styling..........................................................................................................

    @Override
    public SelectComponent<T> hideMarginBottom() {
        this.select.setMarginBottom("");
        return this;
    }

    @Override
    public SelectComponent<T> removeBorders() {
        this.select.getInputElement()
            .parent()
            .setBorder("0")
            .setCssProperty("border-radius", 0);
        return this;
    }

    @Override
    public SelectComponent<T> removePadding() {
        this.select.getInputElement()
            .parent()
            .setPadding("0");
        return this;
    }

    // HtmlComponent....................................................................................................

    @Override
    public int width() {
        return this.element()
            .offsetWidth;
    }

    @Override
    public int height() {
        return this.element()
            .offsetHeight;
    }

    // setCssText.......................................................................................................

    @Override
    public SelectComponent<T> setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.select.cssText(css);
        return this;
    }

    // setCssText.......................................................................................................

    @Override
    public SelectComponent<T> setCssProperty(final String name,
                                             final String value) {
        this.select.setCssProperty(
            name,
            value
        );
        return this;
    }

    // removeCssProperty................................................................................................

    @Override
    public SelectComponent<T> removeCssProperty(final String name) {
        this.select.removeCssProperty(name);
        return this;
    }

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        final Select<T> select = this.select;

        return select.isExpanded() ||
            HtmlComponent.hasFocus(
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

    // FormValueComponentTreePrintable..................................................................................

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
