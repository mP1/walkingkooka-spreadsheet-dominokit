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

package walkingkooka.spreadsheet.dominokit.ui.label;

import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.elements.SpanElement;
import org.dominokit.domino.ui.forms.suggest.SuggestBox;
import org.dominokit.domino.ui.forms.suggest.SuggestOption;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.Context;
import walkingkooka.spreadsheet.dominokit.ui.ValueComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;

/**
 * A text box component that includes support for finding a label.
 */
public final class SpreadsheetLabelComponent implements ValueComponent<HTMLFieldSetElement, SpreadsheetLabelName> {

    public static SpreadsheetLabelComponent with(final Context context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetLabelComponent(context);
    }

    private SpreadsheetLabelComponent(final Context context) {
        final SuggestBox<String, SpanElement, SuggestOption<String>> suggestBox = SuggestBox.create(
                SpreadsheetLabelComponentSuggestStore.with(context)
        );
        this.suggestBox = suggestBox;
        suggestBox.setAutoValidation(true);
        suggestBox.addValidator(SpreadsheetLabelComponentValidator.with(this));
    }

    @Override
    public SpreadsheetLabelComponent setId(final String id) {
        this.suggestBox.getInputElement()
                .setId(id);
        return this;
    }

    @Override
    public SpreadsheetLabelComponent setLabel(final String label) {
        this.suggestBox.setLabel(label);
        return this;
    }

    @Override
    public SpreadsheetLabelComponent optional() {
        this.required = false;
        return this;
    }

    @Override
    public SpreadsheetLabelComponent required() {
        this.required = true;
        return this;
    }

    boolean required;

    @Override
    public SpreadsheetLabelComponent focus() {
        this.suggestBox.focus();
        return this;
    }

    @Override
    public SpreadsheetLabelComponent hideMarginBottom() {
        this.suggestBox.setMarginBottom("");
        return this;
    }

    @Override
    public SpreadsheetLabelComponent removeBorders() {
        this.suggestBox.getInputElement()
                .parent()
                .parent()
                .setBorder("0")
                .setCssProperty("border-radius", 0);
        return this;
    }

    @Override
    public SpreadsheetLabelComponent addChangeListener(final ChangeListener<Optional<SpreadsheetLabelName>> listener) {
        Objects.requireNonNull(listener, "listener");

        this.suggestBox.addChangeListener(
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
        return this.suggestBox.element();
    }

    // Value............................................................................................................

    @Override
    public SpreadsheetLabelComponent setValue(final Optional<SpreadsheetLabelName> label) {
        Objects.requireNonNull(label, "label");

        this.suggestBox.setValue(
                label.map(SpreadsheetLabelName::text)
                        .orElse("")
        );
        return this;
    }

    @Override //
    public Optional<SpreadsheetLabelName> value() {
        return tryParse(
                this.suggestBox.getStringValue()
        );
    }

    private Optional<SpreadsheetLabelName> tryParse(final String value) {
        SpreadsheetLabelName label;

        try {
            label = SpreadsheetSelection.labelName(value);
        } catch (final Exception ignore) {
            label = null;
        }

        return Optional.ofNullable(label);
    }

    private final SuggestBox<String, SpanElement, SuggestOption<String>> suggestBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.element()
                .toString();
    }
}
