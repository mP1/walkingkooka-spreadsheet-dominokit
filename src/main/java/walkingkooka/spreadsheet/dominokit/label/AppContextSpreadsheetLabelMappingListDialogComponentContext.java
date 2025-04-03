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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenOffsetAndCount;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetLabelMappingListHistoryToken;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

final class AppContextSpreadsheetLabelMappingListDialogComponentContext implements SpreadsheetLabelMappingListDialogComponentContext,
    HasSpreadsheetDeltaFetcherWatchersDelegator,
    HistoryContextDelegator,
    LoggingContextDelegator {

    static AppContextSpreadsheetLabelMappingListDialogComponentContext with(final AppContext context) {
        return new AppContextSpreadsheetLabelMappingListDialogComponentContext(
            Objects.requireNonNull(context, "context")
        );
    }

    private AppContextSpreadsheetLabelMappingListDialogComponentContext(final AppContext context) {
        this.context = context;
    }

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        return null;
    }

    @Override
    public Set<SpreadsheetExpressionReference> cellReferences(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
        return this.context.spreadsheetViewportCache()
            .cellReferences(spreadsheetExpressionReference);
    }

    @Override
    public Optional<String> formulaText(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
        return this.context.spreadsheetViewportCache()
            .formulaText(spreadsheetExpressionReference);
    }

    @Override
    public Optional<SpreadsheetCell> labelCell(final SpreadsheetLabelName label) {
        return this.context.spreadsheetViewportCache()
            .cell(label);
    }

    @Override
    public void loadLabelMappings(final SpreadsheetId id,
                                  final HistoryTokenOffsetAndCount offsetAndCount) {
        this.context.spreadsheetDeltaFetcher()
            .loadLabelMappings(
                id,
                offsetAndCount.offset()
                    .orElse(0),
                offsetAndCount.count()
                    .orElse(0)
            );
    }

    @Override
    public Optional<SpreadsheetSelection> resolveLabel(final SpreadsheetLabelName spreadsheetLabelName) {
        return this.context.spreadsheetViewportCache()
            .resolveLabel(spreadsheetLabelName);
    }

    // HasSpreadsheetDeltaFetcherWatchersDelegator......................................................................

    @Override
    public HasSpreadsheetDeltaFetcherWatchers hasSpreadsheetDeltaFetcherWatchers() {
        return this.context;
    }

    // ComponentLifecycleMatcher........................................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return false;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetLabelMappingListHistoryToken;
    }

    // HistoryContextDelegator..........................................................................................

    @Override
    public HistoryContext historyContext() {
        return this.context;
    }

    // LoggingContextDelegator..........................................................................................

    @Override
    public LoggingContext loggingContext() {
        return this.context;
    }

    private final AppContext context;
}
