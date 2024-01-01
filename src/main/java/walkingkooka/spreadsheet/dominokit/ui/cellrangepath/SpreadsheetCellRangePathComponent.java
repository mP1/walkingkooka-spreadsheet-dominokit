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

package walkingkooka.spreadsheet.dominokit.ui.cellrangepath;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.dominokit.ui.ValueComponent;
import walkingkooka.spreadsheet.dominokit.ui.select.SpreadsheetSelectComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangePath;

import java.util.Objects;
import java.util.Optional;

/**
 * A drop down that supports picking an optional {@link SpreadsheetCellRangePath}.
 */
public final class SpreadsheetCellRangePathComponent implements ValueComponent<HTMLFieldSetElement, SpreadsheetCellRangePath> {

    public static SpreadsheetCellRangePathComponent empty() {
        return new SpreadsheetCellRangePathComponent();
    }

    private SpreadsheetCellRangePathComponent() {
        this.select = SpreadsheetSelectComponent.empty();

        for (final SpreadsheetCellRangePath path : SpreadsheetCellRangePath.values()) {
            this.select.appendValue(
                    path.labelText(), // text
                    path // value
            );
        }
    }

    @Override
    public SpreadsheetCellRangePathComponent setId(final String id) {
        this.select.setId(id);
        return this;
    }

    @Override
    public SpreadsheetCellRangePathComponent setLabel(final String label) {
        this.select.setLabel(label);
        return this;
    }

    @Override
    public boolean isDisabled() {
        return this.select.isDisabled();
    }

    @Override
    public SpreadsheetCellRangePathComponent setDisabled(final boolean disabled) {
        this.select.setDisabled(disabled);
        return this;
    }

    @Override
    public SpreadsheetCellRangePathComponent optional() {
        this.select.optional();
        return this;
    }

    @Override
    public SpreadsheetCellRangePathComponent required() {
        this.select.required();
        return this;
    }

    @Override
    public SpreadsheetCellRangePathComponent validate() {
        this.select.validate();
        return this;
    }

    @Override
    public SpreadsheetCellRangePathComponent focus() {
        this.select.focus();
        return this;
    }

    @Override
    public SpreadsheetCellRangePathComponent alwaysShowHelperText() {
        this.select.alwaysShowHelperText();
        return this;
    }

    @Override
    public SpreadsheetCellRangePathComponent hideMarginBottom() {
        this.select.hideMarginBottom();
        return this;
    }

    @Override
    public SpreadsheetCellRangePathComponent removeBorders() {
        this.select.removeBorders();
        return this;
    }

    @Override
    public SpreadsheetCellRangePathComponent addChangeListener(final ChangeListener<Optional<SpreadsheetCellRangePath>> listener) {
        this.select.addChangeListener(listener);
        return this;
    }

    @Override
    public SpreadsheetCellRangePathComponent addFocusListener(final EventListener listener) {
        this.select.addFocusListener(listener);
        return this;
    }

    @Override
    public SpreadsheetCellRangePathComponent addKeydownListener(final EventListener listener) {
        this.select.addKeydownListener(listener);
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.select.element();
    }

    // Value............................................................................................................

    @Override
    public SpreadsheetCellRangePathComponent setValue(final Optional<SpreadsheetCellRangePath> spreadsheetCellRangePath) {
        Objects.requireNonNull(spreadsheetCellRangePath, "spreadsheetCellRangePath");

        this.select.setValue(spreadsheetCellRangePath);
        return this;
    }

    @Override //
    public Optional<SpreadsheetCellRangePath> value() {
        return this.select.value();
    }

    private final SpreadsheetSelectComponent<SpreadsheetCellRangePath> select;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.select.toString();
    }
}