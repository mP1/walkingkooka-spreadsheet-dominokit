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
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;

/**
 * A text box that accepts entry and validates it as a {@link SpreadsheetCellRange}.
 */
public final class SpreadsheetCellRangeComponent implements ValueComponent<HTMLFieldSetElement, SpreadsheetCellRange> {

    public static SpreadsheetCellRangeComponent empty() {
        return new SpreadsheetCellRangeComponent();
    }

    private SpreadsheetCellRangeComponent() {
        this.textBox = ParserSpreadsheetTextBox.with(SpreadsheetSelection::parseCellRange);
    }

    @Override
    public SpreadsheetCellRangeComponent setId(final String id) {
        this.textBox.setId(id);
        return this;
    }

    @Override
    public SpreadsheetCellRangeComponent setLabel(final String label) {
        this.textBox.setLabel(label);
        return this;
    }

    @Override
    public boolean isDisabled() {
        return this.textBox.isDisabled();
    }

    @Override
    public SpreadsheetCellRangeComponent setDisabled(final boolean disabled) {
        this.textBox.setDisabled(disabled);
        return this;
    }

    @Override
    public SpreadsheetCellRangeComponent optional() {
        this.textBox.optional();
        return this;
    }

    @Override
    public SpreadsheetCellRangeComponent required() {
        this.textBox.required();
        return this;
    }

    @Override
    public SpreadsheetCellRangeComponent validate() {
        this.textBox.validate();
        return this;
    }

    @Override
    public SpreadsheetCellRangeComponent focus() {
        this.textBox.focus();
        return this;
    }

    @Override
    public SpreadsheetCellRangeComponent alwaysShowHelperText() {
        this.textBox.alwaysShowHelperText();
        return this;
    }

    @Override
    public SpreadsheetCellRangeComponent setHelperText(final Optional<String> text) {
        this.textBox.setHelperText(text);
        return this;
    }

    @Override
    public SpreadsheetCellRangeComponent hideMarginBottom() {
        this.textBox.hideMarginBottom();
        return this;
    }

    @Override
    public SpreadsheetCellRangeComponent removeBorders() {
        this.textBox.removeBorders();
        return this;
    }

    @Override
    public SpreadsheetCellRangeComponent addChangeListener(final ChangeListener<Optional<SpreadsheetCellRange>> listener) {
        this.textBox.addChangeListener(listener);
        return this;
    }

    @Override
    public SpreadsheetCellRangeComponent addFocusListener(final EventListener listener) {
        this.textBox.addFocusListener(listener);
        return this;
    }

    @Override
    public SpreadsheetCellRangeComponent addKeydownListener(final EventListener listener) {
        this.textBox.addKeydownListener(listener);
        return this;
    }

    @Override
    public SpreadsheetCellRangeComponent addKeyupListener(final EventListener listener) {
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
    public SpreadsheetCellRangeComponent setValue(final Optional<SpreadsheetCellRange> cellRange) {
        Objects.requireNonNull(cellRange, "cellRange");

        this.textBox.setValue(cellRange);
        return this;
    }

    @Override //
    public Optional<SpreadsheetCellRange> value() {
        return this.textBox.value();
    }

    public Optional<String> stringValue() {
        return this.textBox.stringValue();
    }

    public SpreadsheetCellRangeComponent setStringValue(final Optional<String> stringValue) {
        this.textBox.setStringValue(stringValue);
        return this;
    }


    private final ParserSpreadsheetTextBox<SpreadsheetCellRange> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}