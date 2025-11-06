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

package walkingkooka.spreadsheet.dominokit.label;

import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.RefreshContextDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

abstract class AppContextSpreadsheetLabelMappingListDialogComponentContext implements SpreadsheetLabelMappingListDialogComponentContext,
    HasSpreadsheetDeltaFetcherWatchersDelegator,
    RefreshContextDelegator {

    AppContextSpreadsheetLabelMappingListDialogComponentContext(final AppContext context) {
        this.context = Objects.requireNonNull(context, "context");
    }

    @Override
    public final Set<SpreadsheetExpressionReference> cellReferences(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
        return this.context.spreadsheetViewportCache()
            .cellReferences(spreadsheetExpressionReference);
    }

    @Override
    public final Optional<String> formulaText(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
        return this.context.spreadsheetViewportCache()
            .formulaText(spreadsheetExpressionReference);
    }

    @Override
    public final Optional<SpreadsheetCell> labelCell(final SpreadsheetLabelName label) {
        return this.context.spreadsheetViewportCache()
            .cell(label);
    }

    @Override
    public final Optional<SpreadsheetSelection> resolveLabel(final SpreadsheetLabelName spreadsheetLabelName) {
        return this.context.spreadsheetViewportCache()
            .resolveLabel(spreadsheetLabelName);
    }

    // HasSpreadsheetDeltaFetcherWatchersDelegator......................................................................

    @Override
    public final HasSpreadsheetDeltaFetcherWatchers hasSpreadsheetDeltaFetcherWatchers() {
        return this.context;
    }

    // ComponentLifecycleMatcher........................................................................................

    @Override
    public final boolean shouldIgnore(final HistoryToken token) {
        return false;
    }

    // RefreshContextDelegator..........................................................................................

    @Override
    public RefreshContext refreshContext() {
        return this.context;
    }

    final AppContext context;

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.context.toString();
    }
}
