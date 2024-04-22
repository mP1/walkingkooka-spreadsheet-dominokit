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

package walkingkooka.spreadsheet.dominokit.ui.columnrow;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.dominokit.ui.ValueComponent;
import walkingkooka.spreadsheet.dominokit.ui.parsertextbox.ParserSpreadsheetTextBox;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;

/**
 * A text box that accepts entry and validates it as a {@link SpreadsheetColumnOrRowReference}.
 */
public final class SpreadsheetColumnOrRowReferenceComponent implements ValueComponent<HTMLFieldSetElement, SpreadsheetColumnOrRowReference, SpreadsheetColumnOrRowReferenceComponent> {

    public static SpreadsheetColumnOrRowReferenceComponent empty() {
        return new SpreadsheetColumnOrRowReferenceComponent();
    }

    private SpreadsheetColumnOrRowReferenceComponent() {
        this.textBox = ParserSpreadsheetTextBox.with(SpreadsheetSelection::parseColumn);
    }

    @Override
    public SpreadsheetColumnOrRowReferenceComponent setId(final String id) {
        this.textBox.setId(id);
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowReferenceComponent setLabel(final String label) {
        this.textBox.setLabel(label);
        return this;
    }

    @Override
    public boolean isDisabled() {
        return this.textBox.isDisabled();
    }

    @Override
    public SpreadsheetColumnOrRowReferenceComponent setDisabled(final boolean disabled) {
        this.textBox.setDisabled(disabled);
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowReferenceComponent optional() {
        this.textBox.optional();
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowReferenceComponent required() {
        this.textBox.required();
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowReferenceComponent validate() {
        this.textBox.validate();
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowReferenceComponent focus() {
        this.textBox.focus();
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowReferenceComponent alwaysShowHelperText() {
        this.textBox.alwaysShowHelperText();
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowReferenceComponent setHelperText(final Optional<String> text) {
        this.textBox.setHelperText(text);
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowReferenceComponent hideMarginBottom() {
        this.textBox.hideMarginBottom();
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowReferenceComponent removeBorders() {
        this.textBox.removeBorders();
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowReferenceComponent addChangeListener(final ChangeListener<Optional<SpreadsheetColumnOrRowReference>> listener) {
        this.textBox.addChangeListener(listener);
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowReferenceComponent addFocusListener(final EventListener listener) {
        this.textBox.addFocusListener(listener);
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowReferenceComponent addKeydownListener(final EventListener listener) {
        this.textBox.addKeydownListener(listener);
        return this;
    }

    @Override
    public SpreadsheetColumnOrRowReferenceComponent addKeyupListener(final EventListener listener) {
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
    public SpreadsheetColumnOrRowReferenceComponent setValue(final Optional<SpreadsheetColumnOrRowReference> column) {
        Objects.requireNonNull(column, "column");

        this.textBox.setValue(column);
        return this;
    }

    @Override //
    public Optional<SpreadsheetColumnOrRowReference> value() {
        return this.textBox.value();
    }

    public Optional<String> stringValue() {
        return this.textBox.stringValue();
    }

    public SpreadsheetColumnOrRowReferenceComponent setStringValue(final Optional<String> stringValue) {
        this.textBox.setStringValue(stringValue);
        return this;
    }


    private final ParserSpreadsheetTextBox<SpreadsheetColumnOrRowReference> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}