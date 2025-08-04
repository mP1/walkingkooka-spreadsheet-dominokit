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
import walkingkooka.spreadsheet.dominokit.select.SelectComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReferencePath;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A drop down that supports picking an optional {@link SpreadsheetCellRangeReferencePath}.
 */
public final class SpreadsheetCellRangeReferencePathComponent implements FormValueComponent<HTMLFieldSetElement, SpreadsheetCellRangeReferencePath, SpreadsheetCellRangeReferencePathComponent> {

    public static SpreadsheetCellRangeReferencePathComponent empty() {
        return new SpreadsheetCellRangeReferencePathComponent();
    }

    private SpreadsheetCellRangeReferencePathComponent() {
        this.select = SelectComponent.empty();

        for (final SpreadsheetCellRangeReferencePath path : SpreadsheetCellRangeReferencePath.values()) {
            this.select.appendOption(
                path.labelText(), // text
                path // value
            );
        }
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
    public SpreadsheetCellRangeReferencePathComponent addKeydownListener(final EventListener listener) {
        this.select.addKeydownListener(listener);
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferencePathComponent addKeyupListener(final EventListener listener) {
        this.select.addKeyupListener(listener);
        return this;
    }

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetCellRangeReferencePathComponent setCssText(final String css) {
        this.select.setCssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public SpreadsheetCellRangeReferencePathComponent setCssProperty(final String name,
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
    public SpreadsheetCellRangeReferencePathComponent setValue(final Optional<SpreadsheetCellRangeReferencePath> SpreadsheetCellRangeReferencePath) {
        Objects.requireNonNull(SpreadsheetCellRangeReferencePath, "SpreadsheetCellRangeReferencePath");

        this.select.setValue(SpreadsheetCellRangeReferencePath);
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