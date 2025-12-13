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

package walkingkooka.spreadsheet.dominokit.cell.value;

import walkingkooka.spreadsheet.dominokit.anchor.ValueHistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.anchor.ValueHistoryTokenAnchorComponentDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellValueHistoryToken;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.spreadsheet.value.SpreadsheetValueType;
import walkingkooka.validation.ValueType;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link SpreadsheetCellValueAnchorComponent} is a link that when clicked results in a picker allowing the user to select
 * from a date picker or something similar, depending on the {@link walkingkooka.validation.ValueType}
 * for the selected {@link walkingkooka.spreadsheet.value.SpreadsheetCell}.
 */
public final class SpreadsheetCellValueAnchorComponent implements ValueHistoryTokenAnchorComponentDelegator<SpreadsheetCellValueAnchorComponent, SpreadsheetExpressionReference> {

    public static SpreadsheetCellValueAnchorComponent with(final String id,
                                                           final SpreadsheetCellValueAnchorComponentContext context) {
        return new SpreadsheetCellValueAnchorComponent(
            id,
            context
        );
    }

    private SpreadsheetCellValueAnchorComponent(final String id,
                                                final SpreadsheetCellValueAnchorComponentContext context) {
        this.component = ValueHistoryTokenAnchorComponent.with(
            HistoryTokenAnchorComponent.empty(),
            this::getter,
            this::setter
        );
        this.setId(id);
        this.context = Objects.requireNonNull(context, "context");
    }

    /**
     * Getter that returns the {@link SpreadsheetExpressionReference} if one is present.
     */
    private Optional<SpreadsheetExpressionReference> getter(final HistoryTokenAnchorComponent anchor) {
        return anchor.historyToken()
            .map(t -> t.cast(SpreadsheetCellValueHistoryToken.class)
                .anchoredSelection()
                .selection()
                .toExpressionReference()
            );
    }

    private void setter(final Optional<SpreadsheetExpressionReference> cellOrLabel,
                        final HistoryTokenAnchorComponent anchor) {
        String text = "";
        HistoryToken historyToken = null;

        if (cellOrLabel.isPresent()) {
            text = cellOrLabel.get()
                .text();

            final SpreadsheetCellValueAnchorComponentContext context = this.context;

            final SpreadsheetCell cell = context.cell(cellOrLabel.get())
                .orElse(null);
            if (null != cell) {
                final SpreadsheetFormula formula = cell.formula();
                Object value = formula.value()
                    .orElse(null);

                // value present use that as the valueType
                Optional<ValueType> valueType = Optional.empty();
                if (formula.text().isEmpty() && null != value) {
                    valueType = SpreadsheetValueType.toValueType(value.getClass());
                }

                if (false == valueType.isPresent()) {
                    valueType = formula.valueType();
                }

                if (valueType.isPresent()) {
                    historyToken = context.historyToken()
                        .setSelection(cellOrLabel)
                        .setValue(valueType);
                    if (false == historyToken instanceof SpreadsheetCellValueHistoryToken) {
                        historyToken = null;
                    }
                }
            }

        } else {
            text = "Value";
        }

        anchor.setHistoryToken(
            Optional.ofNullable(historyToken)
        ).setTextContent(text);
    }

    // AnchorComponentDelegator.........................................................................................

    @Override
    public ValueHistoryTokenAnchorComponent<SpreadsheetExpressionReference> valueHistoryTokenAnchorComponent() {
        return this.component;
    }

    // @VisibleForTesting
    final ValueHistoryTokenAnchorComponent<SpreadsheetExpressionReference> component;

    final SpreadsheetCellValueAnchorComponentContext context;

    // toString.........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
