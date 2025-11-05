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
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.select.SelectComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReferencePath;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A drop down that supports picking an optional {@link SpreadsheetCellRangeReferencePath}.
 */
public final class SpreadsheetCellRangeReferencePathComponent implements FormValueComponent<HTMLFieldSetElement, SpreadsheetCellRangeReferencePath, SpreadsheetCellRangeReferencePathComponent>,
    HtmlComponentDelegator<HTMLFieldSetElement, SpreadsheetCellRangeReferencePathComponent> {

    public static SpreadsheetCellRangeReferencePathComponent empty(final String id,
                                                                   final SpreadsheetCellRangeReferencePathComponentContext context) {
        return new SpreadsheetCellRangeReferencePathComponent(
            CharSequences.failIfNullOrEmpty(id, "id"),
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetCellRangeReferencePathComponent(final String id,
                                                       final SpreadsheetCellRangeReferencePathComponentContext context) {
        final SelectComponent<SpreadsheetCellRangeReferencePath> select = SelectComponent.<SpreadsheetCellRangeReferencePath>empty(
            (v) -> {
                final SpreadsheetCellRangeReferencePath p = v.orElseThrow(() -> new IllegalArgumentException("Missing SpreadsheetCellRangeReferencePath"));

                return context.selectOption(
                    id + p.name() + SpreadsheetElementIds.OPTION, // id
                    p.labelText(), // text
                    v, // value
                    Optional.empty() // HistoryToken
                );
            }
        );

        for (final SpreadsheetCellRangeReferencePath path : SpreadsheetCellRangeReferencePath.values()) {
            select.appendOption(
                Optional.of(path) // value
            );
        }

        this.select = select;
        this.setId(id);
    }

    @Override
    public SpreadsheetCellRangeReferencePathComponent setId(final String id) {
        this.select.setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.select.id();
    }

    @Override
    public SpreadsheetCellRangeReferencePathComponent setLabel(final String label) {
        this.select.setLabel(label);
        return this;
    }

    @Override
    public String label() {
        return this.select.label();
    }

    @Override
    public boolean isDisabled() {
        return this.select.isDisabled();
    }

    @Override
    public SpreadsheetCellRangeReferencePathComponent setDisabled(final boolean disabled) {
        this.select.setDisabled(disabled);
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferencePathComponent optional() {
        this.select.optional();
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferencePathComponent required() {
        this.select.required();
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.select.isRequired();
    }

    @Override
    public SpreadsheetCellRangeReferencePathComponent validate() {
        this.select.validate();
        return this;
    }

    @Override
    public List<String> errors() {
        return this.select.errors();
    }

    @Override
    public SpreadsheetCellRangeReferencePathComponent setErrors(final List<String> errors) {
        this.select.setErrors(errors);
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferencePathComponent focus() {
        this.select.focus();
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferencePathComponent alwaysShowHelperText() {
        this.select.alwaysShowHelperText();
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferencePathComponent setHelperText(final Optional<String> text) {
        this.select.setHelperText(text);
        return this;
    }

    @Override
    public Optional<String> helperText() {
        return this.select.helperText();
    }

    @Override
    public SpreadsheetCellRangeReferencePathComponent hideMarginBottom() {
        this.select.hideMarginBottom();
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferencePathComponent removeBorders() {
        this.select.removeBorders();
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferencePathComponent removePadding() {
        this.select.removePadding();
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferencePathComponent addBlurListener(final EventListener listener) {
        this.select.addBlurListener(listener);
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferencePathComponent addClickListener(final EventListener listener) {
        this.select.addClickListener(listener);
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferencePathComponent addChangeListener(final ChangeListener<Optional<SpreadsheetCellRangeReferencePath>> listener) {
        this.select.addChangeListener(listener);
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferencePathComponent addContextMenuListener(final EventListener listener) {
        this.select.addContextMenuListener(listener);
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferencePathComponent addFocusListener(final EventListener listener) {
        this.select.addFocusListener(listener);
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferencePathComponent addInputListener(final EventListener listener) {
        this.select.addInputListener(listener);
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferencePathComponent addKeyDownListener(final EventListener listener) {
        this.select.addKeyDownListener(listener);
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferencePathComponent addKeyUpListener(final EventListener listener) {
        this.select.addKeyUpListener(listener);
        return this;
    }

    // Value............................................................................................................

    @Override
    public SpreadsheetCellRangeReferencePathComponent setValue(final Optional<SpreadsheetCellRangeReferencePath> value) {
        Objects.requireNonNull(value, "value");

        this.select.setValue(value);
        return this;
    }

    @Override //
    public Optional<SpreadsheetCellRangeReferencePath> value() {
        return this.select.value();
    }

    @Override
    public boolean isEditing() {
        return this.select.isEditing();
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLFieldSetElement, ?> htmlComponent() {
        return this.select;
    }

    private final SelectComponent<SpreadsheetCellRangeReferencePath> select;

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