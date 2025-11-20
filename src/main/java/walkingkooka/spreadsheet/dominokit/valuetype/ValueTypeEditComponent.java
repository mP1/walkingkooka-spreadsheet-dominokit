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

package walkingkooka.spreadsheet.dominokit.valuetype;

import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.spreadsheet.SpreadsheetValueType;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.select.SelectComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.text.CaseKind;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.validation.ValueType;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A drop down that supports picking an optional {@link ValueType}.
 */
public final class ValueTypeEditComponent implements FormValueComponent<HTMLFieldSetElement, ValueType, ValueTypeEditComponent>,
    HtmlComponentDelegator<HTMLFieldSetElement, ValueTypeEditComponent>,
    TreePrintable {

    public static ValueTypeEditComponent empty(final String id,
                                               final ValueTypeEditComponentContext context) {
        return new ValueTypeEditComponent(
            id,
            context
        );
    }

    private ValueTypeEditComponent(final String id,
                                   final ValueTypeEditComponentContext context) {
        final SelectComponent<ValueType> select = SelectComponent.<ValueType>empty(
            (v) -> {
                final ValueType n = v.orElseThrow(() -> new IllegalArgumentException("Missing ValueType"));
                final String nameText = n.text();

                return context.selectOption(
                    id + nameText + SpreadsheetElementIds.OPTION, // id
                    n.isAny() ?
                        "Any" :
                        CaseKind.KEBAB.change(
                            nameText,
                            CaseKind.TITLE
                        ), // text
                    v, // value
                    Optional.empty() // HistoryToken
                );
            }
        );

        select.appendOption(
            Optional.of(SpreadsheetValueType.ANY)
        );

        for (final ValueType typeName : SpreadsheetValueType.ALL_CELL_VALUE_TYPES) {
            select.appendOption(
                Optional.of(typeName)
            );
        }

        this.select = select;
        this.setId(id);
    }

    @Override
    public ValueTypeEditComponent setId(final String id) {
        this.select.setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.select.id();
    }

    @Override
    public ValueTypeEditComponent setLabel(final String label) {
        this.select.setLabel(label);
        return this;
    }

    @Override
    public String label() {
        return this.select.label();
    }

    @Override
    public ValueTypeEditComponent focus() {
        this.select.focus();
        return this;
    }

    @Override
    public ValueTypeEditComponent blur() {
        this.select.blur();
        return this;
    }

    @Override
    public boolean isEditing() {
        return this.select.isEditing();
    }

    @Override
    public ValueTypeEditComponent alwaysShowHelperText() {
        this.select.alwaysShowHelperText();
        return this;
    }

    @Override
    public ValueTypeEditComponent setHelperText(final Optional<String> text) {
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
    public ValueTypeEditComponent setErrors(final List<String> errors) {
        this.select.setErrors(errors);
        return this;
    }

    @Override
    public ValueTypeEditComponent hideMarginBottom() {
        this.select.hideMarginBottom();
        return this;
    }

    @Override
    public ValueTypeEditComponent removeBorders() {
        this.select.removeBorders();
        return this;
    }

    @Override
    public ValueTypeEditComponent removePadding() {
        this.select.removePadding();
        return this;
    }

    @Override
    public boolean isDisabled() {
        return this.select.isDisabled();
    }

    @Override
    public ValueTypeEditComponent setDisabled(final boolean disabled) {
        this.select.setDisabled(disabled);
        return this;
    }

    @Override
    public ValueTypeEditComponent required() {
        this.select.required();
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.select.isRequired();
    }

    @Override
    public ValueTypeEditComponent optional() {
        this.select.optional();
        return this;
    }

    @Override
    public ValueTypeEditComponent validate() {
        this.select.validate();
        return this;
    }

    // Value............................................................................................................

    @Override
    public ValueTypeEditComponent setValue(final Optional<ValueType> valueType) {
        Objects.requireNonNull(valueType, "valueType");

        this.select.setValue(valueType);
        return this;
    }

    @Override //
    public Optional<ValueType> value() {
        return this.select.value();
    }

    @Override
    public Runnable addValueWatcher(final ValueWatcher<ValueType> watcher) {
        return this.select.addValueWatcher(watcher);
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLFieldSetElement, ?> htmlComponent() {
        return this.select;
    }

    private final SelectComponent<ValueType> select;

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