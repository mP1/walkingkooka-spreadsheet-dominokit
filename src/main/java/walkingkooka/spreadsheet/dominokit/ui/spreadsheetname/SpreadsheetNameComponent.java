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

package walkingkooka.spreadsheet.dominokit.ui.spreadsheetname;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.ui.ValueComponent;
import walkingkooka.spreadsheet.dominokit.ui.parsertextbox.ParserSpreadsheetTextBox;

import java.util.Objects;
import java.util.Optional;

/**
 * A text box that accepts entry and validates it as a {@link SpreadsheetName}.
 */
public final class SpreadsheetNameComponent implements ValueComponent<HTMLFieldSetElement, SpreadsheetName> {

    public static SpreadsheetNameComponent empty() {
        return new SpreadsheetNameComponent();
    }

    private SpreadsheetNameComponent() {
        this.textBox = ParserSpreadsheetTextBox.with(SpreadsheetName::with);
    }

    @Override
    public SpreadsheetNameComponent setId(final String id) {
        this.textBox.setId(id);
        return this;
    }

    @Override
    public SpreadsheetNameComponent setLabel(final String label) {
        this.textBox.setLabel(label);
        return this;
    }

    @Override
    public SpreadsheetNameComponent focus() {
        this.textBox.focus();
        return this;
    }

    @Override
    public SpreadsheetNameComponent alwaysShowHelperText() {
        this.textBox.alwaysShowHelperText();
        return this;
    }

    @Override
    public SpreadsheetNameComponent hideMarginBottom() {
        this.textBox.hideMarginBottom();
        return this;
    }

    @Override
    public SpreadsheetNameComponent removeBorders() {
        this.textBox.removeBorders();
        return this;
    }

    @Override
    public SpreadsheetNameComponent addChangeListener(final ChangeListener<Optional<SpreadsheetName>> listener) {
        this.textBox.addChangeListener(listener);
        return this;
    }

    @Override
    public SpreadsheetNameComponent addFocusListener(final EventListener listener) {
        this.textBox.addFocusListener(listener);
        return this;
    }

    @Override
    public SpreadsheetNameComponent addKeydownListener(final EventListener listener) {
        this.textBox.addKeydownListener(listener);
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.textBox.element();
    }

    // Value............................................................................................................

    @Override
    public SpreadsheetNameComponent setValue(final Optional<SpreadsheetName> spreadsheetName) {
        Objects.requireNonNull(spreadsheetName, "spreadsheetName");

        this.textBox.setValue(spreadsheetName);
        return this;
    }

    @Override //
    public Optional<SpreadsheetName> value() {
        return this.textBox.value();
    }

    public Optional<String> stringValue() {
        return this.textBox.stringValue();
    }

    public SpreadsheetNameComponent setStringValue(final Optional<String> stringValue) {
        this.textBox.setStringValue(stringValue);
        return this;
    }

    private final ParserSpreadsheetTextBox<SpreadsheetName> textBox;

    @Override
    public boolean isDisabled() {
        return this.textBox.isDisabled();
    }

    @Override
    public SpreadsheetNameComponent setDisabled(final boolean disabled) {
        this.textBox.setDisabled(disabled);
        return this;
    }

    @Override
    public SpreadsheetNameComponent required() {
        return this;
    }

    @Override
    public SpreadsheetNameComponent optional() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetNameComponent validate() {
        this.textBox.validate();
        return this;
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}
