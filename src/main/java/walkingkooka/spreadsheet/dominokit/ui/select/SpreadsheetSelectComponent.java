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

package walkingkooka.spreadsheet.dominokit.ui.select;

import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.forms.suggest.Select;
import org.dominokit.domino.ui.forms.suggest.SelectOption;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.Value;

import java.util.Objects;
import java.util.Optional;

/**
 * A select component with a few helpers to assist with build and working with values.
 */
public class SpreadsheetSelectComponent<T> implements IsElement<HTMLFieldSetElement>,
        Value<Optional<T>> {

    public static SpreadsheetSelectComponent empty() {
        return new SpreadsheetSelectComponent();
    }

    private SpreadsheetSelectComponent() {
        this.select = Select.create();
        this.select.setAutoValidation(true);
        this.select.addValidator(SpreadsheetSelectComponentValidator.with(this));
    }

    public SpreadsheetSelectComponent setId(final String id) {
        this.select.setId(id);
        return this;
    }

    public SpreadsheetSelectComponent setLabel(final String label) {
        this.select.setLabel(label);
        return this;
    }

    public SpreadsheetSelectComponent<T> optional() {
        this.required = false;
        return this;
    }

    public SpreadsheetSelectComponent<T> required() {
        this.required = true;
        return this;
    }

    boolean required;

    public void focus() {
        this.select.focus();
    }

    public SpreadsheetSelectComponent addChangeListener(final ChangeListener<Optional<T>> listener) {
        Objects.requireNonNull(listener, "listener");

        this.select.addChangeListener(
                (oldValue, newValue) -> listener.onValueChanged(
                        Optional.ofNullable(oldValue),
                        Optional.ofNullable(newValue)
                )
        );
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.select.element();
    }

    // Value............................................................................................................

    /**
     * Appends a new value to the drop down.
     */
    public SpreadsheetSelectComponent<T> appendValue(final String text,
                                                     final T value) {
        checkText(text);
        checkValue(value);

        return this.appendValue(
                text,
                Optional.of(value)
        );
    }

    /**
     * Appends a new value to the drop down.
     */
    public SpreadsheetSelectComponent<T> appendValue(final String text,
                                                     final Optional<T> value) {
        checkText(text);
        checkValue(value);

        this.select.appendChild(
                SelectOption.create(
                        value.orElse(null),
                        text
                )
        );
        return this;
    }

    public void setValue(final Optional<T> value) {
        checkValue(value);

        this.select.setValue(
                value.orElse(null)
        );
    }

    @Override //
    public Optional<T> value() {
        return Optional.ofNullable(
                this.select.getValue()
        );
    }

    private final Select<T> select;

    private static String checkText(final String text) {
        return Objects.requireNonNull(text, "text");
    }

    private static <T> T checkValue(final T value) {
        return Objects.requireNonNull(value, "value");
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.select.toString();
    }
}
