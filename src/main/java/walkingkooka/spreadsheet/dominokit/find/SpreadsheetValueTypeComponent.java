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

package walkingkooka.spreadsheet.dominokit.find;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.SpreadsheetValueType;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.SpreadsheetSelectComponent;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.validation.ValidationValueTypeName;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A drop down that supports picking an optional {@link String spreadsheet value type}.
 */
final class SpreadsheetValueTypeComponent implements FormValueComponent<HTMLFieldSetElement, ValidationValueTypeName, SpreadsheetValueTypeComponent>,
    TreePrintable {

    static SpreadsheetValueTypeComponent empty() {
        return new SpreadsheetValueTypeComponent();
    }

    private SpreadsheetValueTypeComponent() {
        final SpreadsheetSelectComponent<ValidationValueTypeName> select = SpreadsheetSelectComponent.empty();

        select.appendValue("Any", SpreadsheetValueType.ANY);
        select.appendValue("Boolean", SpreadsheetValueType.BOOLEAN);
        select.appendValue("Date", SpreadsheetValueType.DATE);
        select.appendValue("Error", SpreadsheetValueType.ERROR);
        select.appendValue("DateTime", SpreadsheetValueType.DATE_TIME);
        select.appendValue("Number", SpreadsheetValueType.NUMBER);
        select.appendValue("Text", SpreadsheetValueType.TEXT);
        select.appendValue("Time", SpreadsheetValueType.TIME);

        this.select = select;
    }

    @Override
    public SpreadsheetValueTypeComponent setId(final String id) {
        this.select.setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.select.id();
    }

    @Override
    public SpreadsheetValueTypeComponent setLabel(final String label) {
        this.select.setLabel(label);
        return this;
    }

    @Override
    public String label() {
        return this.select.label();
    }

    @Override
    public SpreadsheetValueTypeComponent focus() {
        this.select.focus();
        return this;
    }

    @Override
    public SpreadsheetValueTypeComponent alwaysShowHelperText() {
        this.select.alwaysShowHelperText();
        return this;
    }

    @Override
    public SpreadsheetValueTypeComponent setHelperText(final Optional<String> text) {
        this.select.setHelperText(text);
        return this;
    }

    @Override
    public Optional<String> helperText() {
        return this.select.helperText();
    }

    @Override
    public List<String> errors() {
        return this.select.errors();
    }

    @Override
    public SpreadsheetValueTypeComponent setErrors(final List<String> errors) {
        this.select.setErrors(errors);
        return this;
    }

    @Override
    public SpreadsheetValueTypeComponent hideMarginBottom() {
        this.select.hideMarginBottom();
        return this;
    }

    @Override
    public SpreadsheetValueTypeComponent removeBorders() {
        this.select.removeBorders();
        return this;
    }

    @Override
    public boolean isDisabled() {
        return this.select.isDisabled();
    }

    @Override
    public SpreadsheetValueTypeComponent setDisabled(final boolean disabled) {
        this.select.setDisabled(disabled);
        return this;
    }

    @Override
    public SpreadsheetValueTypeComponent required() {
        this.select.required();
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.select.isRequired();
    }

    @Override
    public SpreadsheetValueTypeComponent optional() {
        this.select.optional();
        return this;
    }

    @Override
    public SpreadsheetValueTypeComponent validate() {
        this.select.validate();
        return this;
    }

    @Override
    public SpreadsheetValueTypeComponent addChangeListener(final ChangeListener<Optional<ValidationValueTypeName>> listener) {
        this.select.addChangeListener(listener);
        return this;
    }

    @Override
    public SpreadsheetValueTypeComponent addClickListener(final EventListener listener) {
        this.select.addClickListener(
            listener
        );
        return this;
    }
    
    @Override
    public SpreadsheetValueTypeComponent addFocusListener(final EventListener listener) {
        this.select.addFocusListener(
            listener
        );
        return this;
    }

    @Override
    public SpreadsheetValueTypeComponent addKeydownListener(final EventListener listener) {
        this.select.addKeydownListener(listener);
        return this;
    }

    @Override
    public SpreadsheetValueTypeComponent addKeyupListener(final EventListener listener) {
        this.select.addKeyupListener(listener);
        return this;
    }

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetValueTypeComponent setCssText(final String css) {
        this.select.setCssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public SpreadsheetValueTypeComponent setCssProperty(final String name,
                                                        final String value) {
        this.select.setCssProperty(
            name,
            value
        );
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.select.element();
    }

    // Value............................................................................................................

    @Override
    public SpreadsheetValueTypeComponent setValue(final Optional<ValidationValueTypeName> valueType) {
        Objects.requireNonNull(valueType, "valueType");

        this.select.setValue(valueType);
        return this;
    }

    @Override //
    public Optional<ValidationValueTypeName> value() {
        return this.select.value();
    }

    private final SpreadsheetSelectComponent<ValidationValueTypeName> select;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.select.toString();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.select.printTree(printer);
        }
        printer.outdent();
    }
}