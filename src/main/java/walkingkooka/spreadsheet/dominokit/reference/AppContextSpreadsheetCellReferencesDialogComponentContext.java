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

package walkingkooka.spreadsheet.dominokit.reference;

import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContextDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenOffsetAndCount;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;

import java.util.Objects;
import java.util.Set;

final class AppContextSpreadsheetCellReferencesDialogComponentContext implements SpreadsheetCellReferencesDialogComponentContext,
    HasSpreadsheetDeltaFetcherWatchersDelegator,
    SpreadsheetDialogComponentContextDelegator {

    static AppContextSpreadsheetCellReferencesDialogComponentContext with(final AppContext context) {
        return new AppContextSpreadsheetCellReferencesDialogComponentContext(
            Objects.requireNonNull(context, "context")
        );
    }

    private AppContextSpreadsheetCellReferencesDialogComponentContext(final AppContext context) {
        this.context = context;
    }


    @Override
    public void loadCellReferences(final SpreadsheetId id,
                                   final SpreadsheetCellRangeReference cells,
                                   final HistoryTokenOffsetAndCount offsetAndCount) {
        this.context.spreadsheetDeltaFetcher()
            .loadCellReferences(
                id,
                cells,
                offsetAndCount.offset()
                    .orElse(0),
                offsetAndCount.count()
                    .orElse(REFERENCES_MAX_COUNT)
            );
    }

    // SpreadsheetCellLabelsAnchorComponentContext......................................................................

    @Override
    public Set<SpreadsheetLabelName> cellLabels(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
        Objects.requireNonNull(spreadsheetExpressionReference, "spreadsheetExpressionReference");

        return this.context.spreadsheetViewportCache()
            .cellLabels(spreadsheetExpressionReference);
    }

    // SpreadsheetCellReferencesAnchorComponentContext..................................................................

    @Override
    public Set<SpreadsheetExpressionReference> cellReferences(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
        Objects.requireNonNull(spreadsheetExpressionReference, "spreadsheetExpressionReference");

        return this.context.spreadsheetViewportCache()
            .cellReferences(spreadsheetExpressionReference);
    }

    // HasSpreadsheetDeltaFetcherWatcher................................................................................

    @Override
    public HasSpreadsheetDeltaFetcherWatchers hasSpreadsheetDeltaFetcherWatchers() {
        return this.context;
    }

    // HasSpreadsheetMetadata...........................................................................................

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        return this.context.spreadsheetMetadata();
    }

    // SpreadsheetDialogComponentContext................................................................................

    @Override
    public SpreadsheetDialogComponentContext spreadsheetDialogComponentContext() {
        return this.context;
    }

    private final AppContext context;

    @Override
    public String toString() {
        return this.context.toString();
    }
}
