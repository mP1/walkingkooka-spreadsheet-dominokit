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

package walkingkooka.spreadsheet.dominokit.ui.columnorrowcomparatornames.columnrow;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.dominokit.ui.ValueComponent;
import walkingkooka.spreadsheet.dominokit.ui.parsertextbox.ParserSpreadsheetTextBox;

import java.util.Objects;
import java.util.Optional;

/**
 * A text box that accepts entry and validates it as a {@link SpreadsheetColumnOrRowSpreadsheetComparatorNamesList}.
 */
public final class SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent implements ValueComponent<HTMLFieldSetElement, SpreadsheetColumnOrRowSpreadsheetComparatorNamesList, SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent> {

    public static SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent empty() {
        return new SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent();
    }

    private SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent() {
        this.textBox = ParserSpreadsheetTextBox.with(SpreadsheetColumnOrRowSpreadsheetComparatorNamesList::parse);
    }

    @Override
    public SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent setId(final String id) {
        this.textBox.setId(id);
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent setLabel(final String label) {
        this.textBox.setLabel(label);
        return this;
    }

    @Override
    public boolean isDisabled() {
        return this.textBox.isDisabled();
    }

    @Override
    public SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent setDisabled(final boolean disabled) {
        this.textBox.setDisabled(disabled);
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent optional() {
        this.textBox.optional();
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent required() {
        this.textBox.required();
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent validate() {
        this.textBox.validate();
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent focus() {
        this.textBox.focus();
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent alwaysShowHelperText() {
        this.textBox.alwaysShowHelperText();
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent setHelperText(final Optional<String> text) {
        this.textBox.setHelperText(text);
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent hideMarginBottom() {
        this.textBox.hideMarginBottom();
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent removeBorders() {
        this.textBox.removeBorders();
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent addChangeListener(final ChangeListener<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNamesList>> listener) {
        this.textBox.addChangeListener(listener);
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent addFocusListener(final EventListener listener) {
        this.textBox.addFocusListener(listener);
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent addKeydownListener(final EventListener listener) {
        this.textBox.addKeydownListener(listener);
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent addKeyupListener(final EventListener listener) {
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
    public SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent setValue(final Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNamesList> comparatorNames) {
        Objects.requireNonNull(comparatorNames, "comparatorNames");

        this.textBox.setValue(comparatorNames);
        return this;
    }

    @Override //
    public Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNamesList> value() {
        return this.textBox.value();
    }

    public Optional<String> stringValue() {
        return this.textBox.stringValue();
    }

    public SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent setStringValue(final Optional<String> stringValue) {
        this.textBox.setStringValue(stringValue);
        return this;
    }


    private final ParserSpreadsheetTextBox<SpreadsheetColumnOrRowSpreadsheetComparatorNamesList> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}