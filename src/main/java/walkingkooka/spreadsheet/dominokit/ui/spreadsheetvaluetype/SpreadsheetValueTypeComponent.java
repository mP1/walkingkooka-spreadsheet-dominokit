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

package walkingkooka.spreadsheet.dominokit.ui.spreadsheetvaluetype;

import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.Value;
import walkingkooka.spreadsheet.SpreadsheetValueType;
import walkingkooka.spreadsheet.dominokit.ui.select.SpreadsheetSelectComponent;

import java.util.Objects;
import java.util.Optional;

/**
 * A drop down that supports picking an optional {@link String spreadsheet value type}.
 */
public class SpreadsheetValueTypeComponent implements IsElement<HTMLFieldSetElement>,
        Value<Optional<String>> {

    public static SpreadsheetValueTypeComponent empty() {
        return new SpreadsheetValueTypeComponent();
    }

    private SpreadsheetValueTypeComponent() {
        final SpreadsheetSelectComponent<String> select = SpreadsheetSelectComponent.empty();

        select.appendValue("Any", SpreadsheetValueType.ANY);
        select.appendValue("Boolean", SpreadsheetValueType.BOOLEAN);
        select.appendValue("Date", SpreadsheetValueType.DATE);
        select.appendValue("Error", SpreadsheetValueType.ERROR);
        select.appendValue("DateTime", SpreadsheetValueType.DATE_TIME);
        select.appendValue("Number", SpreadsheetValueType.NUMBER);
        select.appendValue("Text", SpreadsheetValueType.TEXT);
        select.appendValue("Time", SpreadsheetValueType.TIME);

        this.select = select;
    }

    public SpreadsheetValueTypeComponent setId(final String id) {
        this.select.setId(id);
        return this;
    }

    public SpreadsheetValueTypeComponent setLabel(final String label) {
        this.select.setLabel(label);
        return this;
    }

    public void focus() {
        this.select.focus();
    }

    public SpreadsheetValueTypeComponent addChangeListener(final ChangeListener<Optional<String>> listener) {
        this.select.addChangeListener(listener);
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.select.element();
    }

    // Value............................................................................................................

    public void setValue(final Optional<String> valueType) {
        Objects.requireNonNull(valueType, "valueType");

        this.select.setValue(valueType);
    }

    @Override //
    public Optional<String> value() {
        return this.select.value();
    }

    private final SpreadsheetSelectComponent<String> select;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.select.toString();
    }
}