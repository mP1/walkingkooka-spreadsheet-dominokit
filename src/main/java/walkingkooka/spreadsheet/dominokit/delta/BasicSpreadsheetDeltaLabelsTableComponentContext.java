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

package walkingkooka.spreadsheet.dominokit.delta;

import walkingkooka.ToStringBuilder;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

final class BasicSpreadsheetDeltaLabelsTableComponentContext implements SpreadsheetDeltaLabelsTableComponentContext,
    HistoryContextDelegator,
    HasSpreadsheetDeltaFetcherWatchersDelegator {

    static BasicSpreadsheetDeltaLabelsTableComponentContext with(final Function<SpreadsheetExpressionReference, Optional<String>> cellToFormulaText,
                                                                 final Function<SpreadsheetExpressionReference, Set<SpreadsheetExpressionReference>> cellToReferences,
                                                                 final Function<SpreadsheetLabelName, Optional<SpreadsheetCell>> labelToCell,
                                                                 final HasSpreadsheetDeltaFetcherWatchers hasSpreadsheetDeltaFetcherWatchers,
                                                                 final HistoryContext historyContext) {
        return new BasicSpreadsheetDeltaLabelsTableComponentContext(
            Objects.requireNonNull(cellToFormulaText, "cellToFormulaText"),
            Objects.requireNonNull(cellToReferences, "cellToReferences"),
            Objects.requireNonNull(labelToCell, "labelToCell"),
            Objects.requireNonNull(hasSpreadsheetDeltaFetcherWatchers, "hasSpreadsheetDeltaFetcherWatchers"),
            Objects.requireNonNull(historyContext, "historyContext")
        );
    }

    private BasicSpreadsheetDeltaLabelsTableComponentContext(final Function<SpreadsheetExpressionReference, Optional<String>> cellToFormulaText,
                                                             final Function<SpreadsheetExpressionReference, Set<SpreadsheetExpressionReference>> cellToReferences,
                                                             final Function<SpreadsheetLabelName, Optional<SpreadsheetCell>> labelToCell,
                                                             final HasSpreadsheetDeltaFetcherWatchers hasSpreadsheetDeltaFetcherWatchers,
                                                             final HistoryContext historyContext) {
        this.cellToFormulaText = cellToFormulaText;
        this.labelToCell = labelToCell;
        this.cellToReferences = cellToReferences;

        this.hasSpreadsheetDeltaFetcherWatchers = hasSpreadsheetDeltaFetcherWatchers;
        this.historyContext = historyContext;
    }

    @Override
    public Optional<SpreadsheetCell> resolveCellForLabel(final SpreadsheetLabelName label) {
        return this.labelToCell.apply(label);
    }

    private final Function<SpreadsheetLabelName, Optional<SpreadsheetCell>> labelToCell;

    @Override
    public Set<SpreadsheetExpressionReference> cellReferences(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
        return this.cellToReferences.apply(spreadsheetExpressionReference);
    }

    private final Function<SpreadsheetExpressionReference, Set<SpreadsheetExpressionReference>> cellToReferences;

    @Override
    public HasSpreadsheetDeltaFetcherWatchers hasSpreadsheetDeltaFetcherWatchers() {
        return this.hasSpreadsheetDeltaFetcherWatchers;
    }

    private final HasSpreadsheetDeltaFetcherWatchers hasSpreadsheetDeltaFetcherWatchers;

    @Override
    public Optional<String> formulaText(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
        return this.cellToFormulaText.apply(spreadsheetExpressionReference);
    }

    private final Function<SpreadsheetExpressionReference, Optional<String>> cellToFormulaText;

    // HistoryContextDelegator..........................................................................................

    @Override
    public HistoryContext historyContext() {
        return this.historyContext;
    }

    private final HistoryContext historyContext;

    // toString.........................................................................................................

    @Override
    public String toString() {
        return ToStringBuilder.empty()
            .value(this.cellToFormulaText)
            .value(this.cellToReferences)
            .value(this.labelToCell)
            .value(this.hasSpreadsheetDeltaFetcherWatchers)
            .value(this.historyContext)
            .build();
    }
}
