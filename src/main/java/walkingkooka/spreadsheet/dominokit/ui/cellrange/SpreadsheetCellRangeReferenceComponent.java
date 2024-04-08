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

package walkingkooka.spreadsheet.dominokit.ui.cellrange;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.dominokit.ui.ValueComponent;
import walkingkooka.spreadsheet.dominokit.ui.parsertextbox.ParserSpreadsheetTextBox;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;

/**
 * A text box that accepts entry and validates it as a {@link SpreadsheetCellRangeReference}.
 */
public final class SpreadsheetCellRangeReferenceComponent implements ValueComponent<HTMLFieldSetElement, SpreadsheetCellRangeReference, SpreadsheetCellRangeReferenceComponent> {

    public static SpreadsheetCellRangeReferenceComponent empty() {
        return new SpreadsheetCellRangeReferenceComponent();
    }

    private SpreadsheetCellRangeReferenceComponent() {
        this.textBox = ParserSpreadsheetTextBox.with(SpreadsheetSelection::parseCellRange);
    }

    @Override
    public SpreadsheetCellRangeReferenceComponent setId(final String id) {
        this.textBox.setId(id);
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferenceComponent setLabel(final String label) {
        this.textBox.setLabel(label);
        return this;
    }

    @Override
    public boolean isDisabled() {
        return this.textBox.isDisabled();
    }

    @Override
    public SpreadsheetCellRangeReferenceComponent setDisabled(final boolean disabled) {
        this.textBox.setDisabled(disabled);
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferenceComponent optional() {
        this.textBox.optional();
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferenceComponent required() {
        this.textBox.required();
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferenceComponent validate() {
        this.textBox.validate();
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferenceComponent focus() {
        this.textBox.focus();
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferenceComponent alwaysShowHelperText() {
        this.textBox.alwaysShowHelperText();
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferenceComponent setHelperText(final Optional<String> text) {
        this.textBox.setHelperText(text);
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferenceComponent hideMarginBottom() {
        this.textBox.hideMarginBottom();
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferenceComponent removeBorders() {
        this.textBox.removeBorders();
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferenceComponent addChangeListener(final ChangeListener<Optional<SpreadsheetCellRangeReference>> listener) {
        this.textBox.addChangeListener(listener);
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferenceComponent addFocusListener(final EventListener listener) {
        this.textBox.addFocusListener(listener);
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferenceComponent addKeydownListener(final EventListener listener) {
        this.textBox.addKeydownListener(listener);
        return this;
    }

    @Override
    public SpreadsheetCellRangeReferenceComponent addKeyupListener(final EventListener listener) {
        this.textBox.addKeyupListener(listener);
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.textBox.element();
    }

    // Value............................................................................................................

    @Override
    public SpreadsheetCellRangeReferenceComponent setValue(final Optional<SpreadsheetCellRangeReference> cellRange) {
        Objects.requireNonNull(cellRange, "cellRange");

        this.textBox.setValue(cellRange);
        return this;
    }

    @Override //
    public Optional<SpreadsheetCellRangeReference> value() {
        return this.textBox.value();
    }

    public Optional<String> stringValue() {
        return this.textBox.stringValue();
    }

    public SpreadsheetCellRangeReferenceComponent setStringValue(final Optional<String> stringValue) {
        this.textBox.setStringValue(stringValue);
        return this;
    }


    private final ParserSpreadsheetTextBox<SpreadsheetCellRangeReference> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}