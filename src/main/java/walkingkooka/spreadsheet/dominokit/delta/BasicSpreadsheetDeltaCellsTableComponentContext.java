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
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

final class BasicSpreadsheetDeltaCellsTableComponentContext implements SpreadsheetDeltaCellsTableComponentContext,
    HistoryContextDelegator,
    HasSpreadsheetDeltaFetcherWatchers {

    static BasicSpreadsheetDeltaCellsTableComponentContext with(final HasSpreadsheetDeltaFetcherWatchers hasSpreadsheetDeltaFetcherWatchers,
                                                                final Function<SpreadsheetExpressionReference, Set<SpreadsheetLabelName>> cellToLabels,
                                                                final Function<SpreadsheetExpressionReference, Set<SpreadsheetExpressionReference>> cellToReferences,
                                                                final Function<SpreadsheetExpressionReference, Optional<String>> cellToFormulaText,
                                                                final Function<SpreadsheetSelection, Optional<SpreadsheetCell>> selectionToCell,
                                                                final HistoryContext historyContext) {
        return new BasicSpreadsheetDeltaCellsTableComponentContext(
            Objects.requireNonNull(hasSpreadsheetDeltaFetcherWatchers, "hasSpreadsheetDeltaFetcherWatchers"),
            Objects.requireNonNull(cellToLabels, "cellToLabels"),
            Objects.requireNonNull(cellToReferences, "cellToReferences"),
            Objects.requireNonNull(cellToFormulaText, "cellToFormulaText"),
            Objects.requireNonNull(selectionToCell, "selectionToCell"),
            Objects.requireNonNull(historyContext, "historyContext")
        );
    }

    public BasicSpreadsheetDeltaCellsTableComponentContext(final HasSpreadsheetDeltaFetcherWatchers hasSpreadsheetDeltaFetcherWatchers,
                                                           final Function<SpreadsheetExpressionReference, Set<SpreadsheetLabelName>> cellToLabels,
                                                           final Function<SpreadsheetExpressionReference, Set<SpreadsheetExpressionReference>> cellToReferences,
                                                           final Function<SpreadsheetExpressionReference, Optional<String>> cellToFormulaText,
                                                           final Function<SpreadsheetSelection, Optional<SpreadsheetCell>> selectionToCell,
                                                           final HistoryContext historyContext) {
        this.hasSpreadsheetDeltaFetcherWatchers = hasSpreadsheetDeltaFetcherWatchers;

        this.cellToLabels = cellToLabels;
        this.cellToReferences = cellToReferences;
        this.cellToFormulaText = cellToFormulaText;
        this.selectionToCell = selectionToCell;

        this.historyContext = historyContext;
    }

    @Override
    public HistoryContext historyContext() {
        return this.historyContext;
    }

    private final HistoryContext historyContext;

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.hasSpreadsheetDeltaFetcherWatchers.addSpreadsheetDeltaFetcherWatcher(watcher);
    }

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcherOnce(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.hasSpreadsheetDeltaFetcherWatchers.addSpreadsheetDeltaFetcherWatcherOnce(watcher);
    }

    private final HasSpreadsheetDeltaFetcherWatchers hasSpreadsheetDeltaFetcherWatchers;

    // SpreadsheetCellLabelsAnchorComponentContext......................................................................

    @Override
    public Set<SpreadsheetLabelName> cellLabels(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
        return this.cellToLabels.apply(spreadsheetExpressionReference);
    }

    private final Function<SpreadsheetExpressionReference, Set<SpreadsheetLabelName>> cellToLabels;

    // SpreadsheetCellReferencesAnchorComponentContext..................................................................

    @Override
    public Set<SpreadsheetExpressionReference> cellReferences(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
        return this.cellToReferences.apply(spreadsheetExpressionReference);
    }

    private final Function<SpreadsheetExpressionReference, Set<SpreadsheetExpressionReference>> cellToReferences;


    // SpreadsheetCellValueAnchorComponentContext.......................................................................

    @Override
    public Optional<SpreadsheetCell> cell(final SpreadsheetSelection selection) {
        return this.selectionToCell.apply(selection);
    }

    private final Function<SpreadsheetSelection, Optional<SpreadsheetCell>> selectionToCell;

    // SpreadsheetFormulaSelectAnchorComponentContext...................................................................

    @Override
    public Optional<String> formulaText(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
        return this.cellToFormulaText.apply(spreadsheetExpressionReference);
    }

    private final Function<SpreadsheetExpressionReference, Optional<String>> cellToFormulaText;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return ToStringBuilder.empty()
            .label("historyContext")
            .value(this.historyContext)
            .label("hasSpreadsheetDeltaFetcherWatchers")
            .value(this.hasSpreadsheetDeltaFetcherWatchers)
            .label("cellToLabels")
            .value(this.cellToLabels)
            .label("cellToReferences")
            .value(this.cellToReferences)
            .label("cellToFormulaText")
            .value(this.cellToFormulaText)
            .label("selectionToCell")
            .value(this.selectionToCell)
            .build();
    }
}
