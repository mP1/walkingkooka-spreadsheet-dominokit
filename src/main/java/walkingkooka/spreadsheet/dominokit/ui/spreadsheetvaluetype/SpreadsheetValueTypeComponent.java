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
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.SpreadsheetValueType;
import walkingkooka.spreadsheet.dominokit.ui.ValueComponent;
import walkingkooka.spreadsheet.dominokit.ui.select.SpreadsheetSelectComponent;

import java.util.Objects;
import java.util.Optional;

/**
 * A drop down that supports picking an optional {@link String spreadsheet value type}.
 */
public final class SpreadsheetValueTypeComponent implements ValueComponent<HTMLFieldSetElement, String> {

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

    @Override
    public SpreadsheetValueTypeComponent setId(final String id) {
        this.select.setId(id);
        return this;
    }

    @Override
    public SpreadsheetValueTypeComponent setLabel(final String label) {
        this.select.setLabel(label);
        return this;
    }

    @Override
    public SpreadsheetValueTypeComponent focus() {
        this.select.focus();
        return this;
    }

    @Override
    public SpreadsheetValueTypeComponent required() {
        this.select.required();
        return this;
    }

    @Override
    public SpreadsheetValueTypeComponent optional() {
        this.select.optional();
        return this;
    }

    @Override
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

    @Override
    public SpreadsheetValueTypeComponent setValue(final Optional<String> valueType) {
        Objects.requireNonNull(valueType, "valueType");

        this.select.setValue(valueType);
        return this;
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