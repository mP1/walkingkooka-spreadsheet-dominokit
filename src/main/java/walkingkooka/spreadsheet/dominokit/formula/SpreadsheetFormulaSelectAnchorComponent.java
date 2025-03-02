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

package walkingkooka.spreadsheet.dominokit.formula;

import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponent;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellFormulaSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.value.ValueHistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;

import java.util.Objects;
import java.util.Optional;

/**
 * Creates a {@link SpreadsheetFormulaSelectAnchorComponent}.
 * The value which must be a {@link SpreadsheetExpressionReference} will be used to select the cell and edit the formula.
 * The link text will contain the {@link SpreadsheetExpressionReference}.
 */
public final class SpreadsheetFormulaSelectAnchorComponent implements AnchorComponentDelegator<SpreadsheetFormulaSelectAnchorComponent, SpreadsheetExpressionReference> {

    public static SpreadsheetFormulaSelectAnchorComponent with(final String id,
                                                               final HistoryContext context) {
        return new SpreadsheetFormulaSelectAnchorComponent(
            id,
            context
        );
    }

    private SpreadsheetFormulaSelectAnchorComponent(final String id,
                                                    final HistoryContext context) {
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
            .map(t -> t.cast(SpreadsheetCellSelectHistoryToken.class)
                .anchoredSelection()
                .selection()
                .toExpressionReference()
            );
    }

    private void setter(final Optional<SpreadsheetExpressionReference> value,
                        final HistoryTokenAnchorComponent anchor) {
        String text = null;
        HistoryToken historyToken = null;

        if (value.isPresent()) {
            historyToken = this.context.historyToken()
                .setSelection(value)
                .formula();
            if (false == (historyToken instanceof SpreadsheetCellFormulaSelectHistoryToken)) {
                historyToken = null;
            }
            text = value.get()
                .text();
        } else {
            text = "Formula";
        }

        anchor.setHistoryToken(
            Optional.ofNullable(historyToken)
        ).setTextContent(text);
    }

    @Override
    public Optional<SpreadsheetExpressionReference> value() {
        return this.component.value();
    }

    @Override
    public SpreadsheetFormulaSelectAnchorComponent setValue(final Optional<SpreadsheetExpressionReference> value) {
        this.component.setValue(value);
        return this;
    }

    // AnchorComponentDelegator.........................................................................................

    @Override
    public AnchorComponent<?, SpreadsheetExpressionReference> anchorComponent() {
        return this.component;
    }

    // @VisibleForTesting
    final ValueHistoryTokenAnchorComponent<SpreadsheetExpressionReference> component;

    final HistoryContext context;

    // toString.........................................................................................................

    @Override
    public String toString() {
        return this.component.toString();
    }
}
