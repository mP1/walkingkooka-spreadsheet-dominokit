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

import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.Value;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.ui.textbox.SpreadsheetTextBox;

import java.util.Objects;
import java.util.Optional;

/**
 * A text box that accepts entry and validates it as a {@link SpreadsheetName}.
 */
public class SpreadsheetNameComponent implements IsElement<HTMLFieldSetElement>,
        Value<Optional<SpreadsheetName>> {

    public static SpreadsheetNameComponent empty() {
        return new SpreadsheetNameComponent();
    }

    private SpreadsheetNameComponent() {
        this.textBox = SpreadsheetTextBox.empty()
                .clearIcon()
                .disableSpellcheck()
                .enterFiresValueChange()
                .setValidator(SpreadsheetNameComponentValidator.INSTANCE);

    }

    public SpreadsheetNameComponent setId(final String id) {
        this.textBox.setId(id);
        return this;
    }

    public SpreadsheetNameComponent setLabel(final String label) {
        this.textBox.setLabel(label);
        return this;
    }

    public void focus() {
        this.textBox.focus();
    }

    public SpreadsheetNameComponent addChangeListener(final ChangeListener<Optional<SpreadsheetName>> listener) {
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

    public void setValue(final Optional<SpreadsheetName> spreadsheetName) {
        Objects.requireNonNull(spreadsheetName, "spreadsheetName");

        this.textBox.setValue(
                spreadsheetName.map(SpreadsheetName::text)
                        .orElse("")
        );
    }

    @Override //
    public Optional<SpreadsheetName> value() {
        return tryParse(
                this.textBox.value()
        );
    }

    private Optional<SpreadsheetName> tryParse(final String value) {
        SpreadsheetName spreadsheetName;

        try {
            spreadsheetName = SpreadsheetName.with(value);
        } catch (final Exception ignore) {
            spreadsheetName = null;
        }

        return Optional.ofNullable(spreadsheetName);
    }

    private final SpreadsheetTextBox textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}
