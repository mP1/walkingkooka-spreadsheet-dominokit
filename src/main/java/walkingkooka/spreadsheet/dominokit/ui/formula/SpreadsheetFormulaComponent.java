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

package walkingkooka.spreadsheet.dominokit.ui.formula;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.dominokit.ui.ValueComponent;
import walkingkooka.spreadsheet.dominokit.ui.parsertextbox.ParserSpreadsheetTextBox;
import walkingkooka.tree.expression.Expression;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A text box that accepts entry and validates it as a {@link Expression}.
 */
public final class SpreadsheetFormulaComponent implements ValueComponent<HTMLFieldSetElement, SpreadsheetFormula> {

    public static SpreadsheetFormulaComponent empty(final Function<String, SpreadsheetFormula> parser) {
        return new SpreadsheetFormulaComponent(parser);
    }

    private SpreadsheetFormulaComponent(final Function<String, SpreadsheetFormula> parser) {
        this.textBox = ParserSpreadsheetTextBox.with(parser)
                .setValidator(SpreadsheetFormulaComponentValidator.with(parser));
    }

    @Override
    public SpreadsheetFormulaComponent setId(final String id) {
        this.textBox.setId(id);
        return this;
    }

    @Override
    public SpreadsheetFormulaComponent setLabel(final String label) {
        this.textBox.setLabel(label);
        return this;
    }

    @Override
    public boolean isDisabled() {
        return this.textBox.isDisabled();
    }

    @Override
    public SpreadsheetFormulaComponent setDisabled(final boolean disabled) {
        this.textBox.setDisabled(disabled);
        return this;
    }

    @Override
    public SpreadsheetFormulaComponent validate() {
        this.textBox.validate();
        return this;
    }

    @Override
    public SpreadsheetFormulaComponent focus() {
        this.textBox.focus();
        return this;
    }

    @Override
    public SpreadsheetFormulaComponent alwaysShowHelperText() {
        this.textBox.alwaysShowHelperText();
        return this;
    }

    @Override
    public SpreadsheetFormulaComponent setHelperText(final Optional<String> text) {
        this.textBox.setHelperText(text);
        return this;
    }

    @Override
    public SpreadsheetFormulaComponent hideMarginBottom() {
        this.textBox.hideMarginBottom();
        return this;
    }

    @Override
    public SpreadsheetFormulaComponent removeBorders() {
        this.textBox.removeBorders();
        return this;
    }

    @Override
    public SpreadsheetFormulaComponent addChangeListener(final ChangeListener<Optional<SpreadsheetFormula>> listener) {
        this.textBox.addChangeListener(listener);
        return this;
    }

    @Override
    public SpreadsheetFormulaComponent addFocusListener(final EventListener listener) {
        this.textBox.addFocusListener(listener);
        return this;
    }

    @Override
    public SpreadsheetFormulaComponent addKeydownListener(final EventListener listener) {
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
    public SpreadsheetFormulaComponent setValue(final Optional<SpreadsheetFormula> formula) {
        Objects.requireNonNull(formula, "formula");

        this.textBox.setValue(formula);
        return this;
    }

    @Override //
    public Optional<SpreadsheetFormula> value() {
        return this.textBox.value();
    }

    public Optional<String> stringValue() {
        return this.textBox.stringValue();
    }

    public SpreadsheetFormulaComponent setStringValue(final Optional<String> stringValue) {
        this.textBox.setStringValue(stringValue);
        return this;
    }

    private final ParserSpreadsheetTextBox<SpreadsheetFormula> textBox;

    @Override
    public SpreadsheetFormulaComponent required() {
        this.textBox.required();
        return this;
    }

    @Override
    public SpreadsheetFormulaComponent optional() {
        this.textBox.required();
        return this;
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}
