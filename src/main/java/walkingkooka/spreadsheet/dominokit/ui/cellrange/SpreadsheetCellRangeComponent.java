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

package walkingkooka.spreadsheet.dominokit.ui.cellrange;

import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.Value;
import walkingkooka.spreadsheet.dominokit.ui.parsertextbox.ParserSpreadsheetTextBox;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;

/**
 * A text box that accepts entry and validates it as a {@link SpreadsheetCellRange}.
 */
public class SpreadsheetCellRangeComponent implements IsElement<HTMLFieldSetElement>,
        Value<Optional<SpreadsheetCellRange>> {

    public static SpreadsheetCellRangeComponent empty() {
        return new SpreadsheetCellRangeComponent();
    }

    private SpreadsheetCellRangeComponent() {
        this.textBox = ParserSpreadsheetTextBox.with(
                SpreadsheetSelection::parseCellRange
        );
    }

    public SpreadsheetCellRangeComponent setId(final String id) {
        this.textBox.setId(id);
        return this;
    }

    public SpreadsheetCellRangeComponent setLabel(final String label) {
        this.textBox.setLabel(label);
        return this;
    }

    public SpreadsheetCellRangeComponent optional() {
        this.textBox.optional();
        return this;
    }

    public SpreadsheetCellRangeComponent required() {
        this.textBox.required();
        return this;
    }

    public void focus() {
        this.textBox.focus();
    }

    public SpreadsheetCellRangeComponent addChangeListener(final ChangeListener<Optional<SpreadsheetCellRange>> listener) {
        this.textBox.addChangeListener(listener);
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.textBox.element();
    }

    // Value............................................................................................................

    public void setValue(final Optional<SpreadsheetCellRange> cellRange) {
        Objects.requireNonNull(cellRange, "cellRange");

        this.textBox.setValue(cellRange);
    }

    @Override //
    public Optional<SpreadsheetCellRange> value() {
        return this.textBox.value();
    }

    private final ParserSpreadsheetTextBox<SpreadsheetCellRange> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}