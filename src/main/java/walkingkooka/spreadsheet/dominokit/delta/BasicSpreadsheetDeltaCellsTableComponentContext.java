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

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

final class BasicSpreadsheetDeltaCellsTableComponentContext implements SpreadsheetDeltaCellsTableComponentContext,
    HistoryContextDelegator,
    HasSpreadsheetDeltaFetcherWatchers {

    static BasicSpreadsheetDeltaCellsTableComponentContext with(final HistoryContext historyContext,
                                                                final HasSpreadsheetDeltaFetcherWatchers hasSpreadsheetDeltaFetcherWatchers,
                                                                final Function<SpreadsheetExpressionReference, Set<SpreadsheetLabelName>> cellLabels,
                                                                final Function<SpreadsheetExpressionReference, Set<SpreadsheetExpressionReference>> cellReferences,
                                                                final Function<SpreadsheetExpressionReference, Optional<String>> formulaText) {
        return new BasicSpreadsheetDeltaCellsTableComponentContext(
            Objects.requireNonNull(historyContext, "historyContext"),
            Objects.requireNonNull(hasSpreadsheetDeltaFetcherWatchers, "hasSpreadsheetDeltaFetcherWatchers"),
            Objects.requireNonNull(cellLabels, "cellLabels"),
            Objects.requireNonNull(cellReferences, "cellReferences"),
            Objects.requireNonNull(formulaText, "formulaText")
        );
    }

    public BasicSpreadsheetDeltaCellsTableComponentContext(final HistoryContext historyContext,
                                                           final HasSpreadsheetDeltaFetcherWatchers hasSpreadsheetDeltaFetcherWatchers,
                                                           final Function<SpreadsheetExpressionReference, Set<SpreadsheetLabelName>> cellLabels,
                                                           final Function<SpreadsheetExpressionReference, Set<SpreadsheetExpressionReference>> cellReferences,
                                                           final Function<SpreadsheetExpressionReference, Optional<String>> formulaText) {
        this.historyContext = historyContext;
        this.hasSpreadsheetDeltaFetcherWatchers = hasSpreadsheetDeltaFetcherWatchers;

        this.cellLabels = cellLabels;
        this.cellReferences = cellReferences;

        this.formulaText = formulaText;
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
        return this.cellLabels.apply(spreadsheetExpressionReference);
    }

    private final Function<SpreadsheetExpressionReference, Set<SpreadsheetLabelName>> cellLabels;

    // SpreadsheetCellReferencesAnchorComponentContext..................................................................

    @Override
    public Set<SpreadsheetExpressionReference> cellReferences(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
        return this.cellReferences.apply(spreadsheetExpressionReference);
    }

    private final Function<SpreadsheetExpressionReference, Set<SpreadsheetExpressionReference>> cellReferences;

    // SpreadsheetFormulaSelectAnchorComponentContext...................................................................

    @Override
    public Optional<String> formulaText(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
        return this.formulaText.apply(spreadsheetExpressionReference);
    }

    private final Function<SpreadsheetExpressionReference, Optional<String>> formulaText;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return ToStringBuilder.empty()
            .label("historyContext")
            .value(this.historyContext)
            .label("hasSpreadsheetDeltaFetcherWatchers")
            .value(this.hasSpreadsheetDeltaFetcherWatchers)
            .label("cellLabels")
            .value(this.cellLabels)
            .label("cellReferences")
            .value(this.cellReferences)
            .label("formulaText")
            .value(this.formulaText)
            .build();
    }
}
