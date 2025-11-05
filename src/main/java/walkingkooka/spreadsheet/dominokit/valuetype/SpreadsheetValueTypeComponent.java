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

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.SpreadsheetValueType;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.select.SelectComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.text.CaseKind;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.validation.ValueTypeName;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A drop down that supports picking an optional {@link ValueTypeName}.
 */
public final class SpreadsheetValueTypeComponent implements FormValueComponent<HTMLFieldSetElement, ValueTypeName, SpreadsheetValueTypeComponent>,
    HtmlComponentDelegator<HTMLFieldSetElement, SpreadsheetValueTypeComponent>,
    TreePrintable {

    public static SpreadsheetValueTypeComponent empty(final String id,
                                                      final SpreadsheetValueTypeComponentContext context) {
        return new SpreadsheetValueTypeComponent(
            id,
            context
        );
    }

    private SpreadsheetValueTypeComponent(final String id,
                                          final SpreadsheetValueTypeComponentContext context) {
        final SelectComponent<ValueTypeName> select = SelectComponent.<ValueTypeName>empty(
            (v) -> {
                final ValueTypeName n = v.orElseThrow(() -> new IllegalArgumentException("Missing ValueTypeName"));
                final String nameText = n.text();

                return context.selectOption(
                    id + nameText + SpreadsheetElementIds.OPTION, // id
                    n.isAny() ?
                        "Any" :
                        CaseKind.PASCAL.change(
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

        for (final ValueTypeName typeName : SpreadsheetValueType.ALL_CELL_VALUE_TYPES) {
            select.appendOption(
                Optional.of(typeName)
            );
        }

        this.select = select;
        this.setId(id);
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
    public boolean isEditing() {
        return this.select.isEditing();
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
    public SpreadsheetValueTypeComponent removePadding() {
        this.select.removePadding();
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
    public SpreadsheetValueTypeComponent addBlurListener(final EventListener listener) {
        this.select.addBlurListener(
            listener
        );
        return this;
    }
    
    @Override
    public SpreadsheetValueTypeComponent addChangeListener(final ChangeListener<Optional<ValueTypeName>> listener) {
        this.select.addChangeListener(listener);
        return this;
    }

    @Override
    public SpreadsheetValueTypeComponent addContextMenuListener(final EventListener listener) {
        this.select.addContextMenuListener(
            listener
        );
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
    public SpreadsheetValueTypeComponent addInputListener(final EventListener listener) {
        this.select.addInputListener(
            listener
        );
        return this;
    }

    @Override
    public SpreadsheetValueTypeComponent addKeyDownListener(final EventListener listener) {
        this.select.addKeyDownListener(listener);
        return this;
    }

    @Override
    public SpreadsheetValueTypeComponent addKeyUpListener(final EventListener listener) {
        this.select.addKeyUpListener(listener);
        return this;
    }

    // Value............................................................................................................

    @Override
    public SpreadsheetValueTypeComponent setValue(final Optional<ValueTypeName> valueType) {
        Objects.requireNonNull(valueType, "valueType");

        this.select.setValue(valueType);
        return this;
    }

    @Override //
    public Optional<ValueTypeName> value() {
        return this.select.value();
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLFieldSetElement, ?> htmlComponent() {
        return this.select;
    }

    private final SelectComponent<ValueTypeName> select;

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