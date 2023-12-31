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

package walkingkooka.spreadsheet.dominokit.ui.spreadsheetexpressionreference;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.dominokit.ui.ValueComponent;
import walkingkooka.spreadsheet.dominokit.ui.parsertextbox.ParserSpreadsheetTextBox;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;

/**
 * A text box that accepts entry and validates it as a {@link SpreadsheetExpressionReference}.
 */
public final class SpreadsheetExpressionReferenceComponent implements ValueComponent<HTMLFieldSetElement, SpreadsheetExpressionReference> {

    public static SpreadsheetExpressionReferenceComponent empty() {
        return new SpreadsheetExpressionReferenceComponent();
    }

    private SpreadsheetExpressionReferenceComponent() {
        this.textBox = ParserSpreadsheetTextBox.with(
                SpreadsheetSelection::parseExpressionReference
        );
    }

    @Override
    public SpreadsheetExpressionReferenceComponent setId(final String id) {
        this.textBox.setId(id);
        return this;
    }

    @Override
    public SpreadsheetExpressionReferenceComponent setLabel(final String label) {
        this.textBox.setLabel(label);
        return this;
    }

    @Override
    public SpreadsheetExpressionReferenceComponent optional() {
        this.textBox.optional();
        return this;
    }

    @Override
    public SpreadsheetExpressionReferenceComponent required() {
        this.textBox.required();
        return this;
    }

    @Override
    public SpreadsheetExpressionReferenceComponent focus() {
        this.textBox.focus();
        return this;
    }

    @Override
    public SpreadsheetExpressionReferenceComponent hideMarginBottom() {
        this.textBox.hideMarginBottom();
        return this;
    }

    @Override
    public SpreadsheetExpressionReferenceComponent removeBorders() {
        this.textBox.removeBorders();
        return this;
    }

    @Override
    public SpreadsheetExpressionReferenceComponent addChangeListener(final ChangeListener<Optional<SpreadsheetExpressionReference>> listener) {
        this.textBox.addChangeListener(listener);
        return this;
    }

    @Override
    public SpreadsheetExpressionReferenceComponent addFocusListener(final EventListener listener) {
        this.textBox.addFocusListener(listener);
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.textBox.element();
    }

    // Value............................................................................................................

    @Override
    public SpreadsheetExpressionReferenceComponent setValue(final Optional<SpreadsheetExpressionReference> spreadsheetExpressionReference) {
        Objects.requireNonNull(spreadsheetExpressionReference, "spreadsheetExpressionReference");

        this.textBox.setValue(spreadsheetExpressionReference);
        return this;
    }

    @Override //
    public Optional<SpreadsheetExpressionReference> value() {
        return this.textBox.value();
    }

    private final ParserSpreadsheetTextBox<SpreadsheetExpressionReference> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}