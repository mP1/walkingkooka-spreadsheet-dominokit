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

import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.Value;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * A text box that accepts entry and validates it as a {@link SpreadsheetExpressionReference}.
 */
public class SpreadsheetExpressionReferenceComponent implements IsElement<HTMLFieldSetElement>,
        Value<Optional<SpreadsheetExpressionReference>> {

    public static SpreadsheetExpressionReferenceComponent with(final String label) {
        CharSequences.failIfNullOrEmpty(label, "label");

        return new SpreadsheetExpressionReferenceComponent(label);
    }

    private SpreadsheetExpressionReferenceComponent(final String label) {
        final TextBox textBox = new TextBox(label);
        textBox.setAutoValidation(true);
        textBox.addValidator(SpreadsheetExpressionReferenceComponentValidator.INSTANCE);

        this.textBox = textBox;

    }

    public SpreadsheetExpressionReferenceComponent setId(final String id) {
        this.textBox.getInputElement()
                .setId(id);
        return this;
    }

    public void focus() {
        this.textBox.focus();
    }

    public SpreadsheetExpressionReferenceComponent addChangeListener(final ChangeListener<Optional<SpreadsheetExpressionReference>> listener) {
        Objects.requireNonNull(listener, "listener");

        this.textBox.addChangeListener(
                (final String oldValue,
                 final String newValue) -> {
                    listener.onValueChanged(
                            tryParse(oldValue),
                            tryParse(newValue)
                    );
                }
        );

        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.textBox.element();
    }

    // Value............................................................................................................

    public void setValue(final Optional<SpreadsheetExpressionReference> expressionReference) {
        Objects.requireNonNull(expressionReference, "expressionReference");

        this.textBox.setValue(
                expressionReference.map(SpreadsheetExpressionReference::text)
                        .orElse("")
        );
    }

    @Override //
    public Optional<SpreadsheetExpressionReference> value() {
        return tryParse(
                this.textBox.getStringValue()
        );
    }

    private Optional<SpreadsheetExpressionReference> tryParse(final String value) {
        SpreadsheetExpressionReference expressionReference;

        try {
            expressionReference = SpreadsheetSelection.parseExpressionReference(value);
        } catch (final Exception ignore) {
            expressionReference = null;
        }

        return Optional.ofNullable(expressionReference);
    }

    private final TextBox textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.element()
                .toString();
    }
}
