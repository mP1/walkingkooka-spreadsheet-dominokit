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

package walkingkooka.spreadsheet.dominokit.find;

import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContextDelegator;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContextDelegator;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchersDelegator;
import walkingkooka.spreadsheet.engine.SpreadsheetCellFindQuery;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.SpreadsheetParserProvider;
import walkingkooka.spreadsheet.parser.SpreadsheetParserProviderDelegator;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

final class AppContextSpreadsheetFindDialogComponentContext implements SpreadsheetFindDialogComponentContext,
    HasSpreadsheetDeltaFetcherWatchersDelegator,
    SpreadsheetDialogComponentContextDelegator,
    SpreadsheetParserProviderDelegator,
    ProviderContextDelegator {

    static AppContextSpreadsheetFindDialogComponentContext with(final AppContext context) {
        return new AppContextSpreadsheetFindDialogComponentContext(
            Objects.requireNonNull(context, "context")
        );
    }

    private AppContextSpreadsheetFindDialogComponentContext(final AppContext context) {
        this.context = context;
    }

    @Override
    public String dialogTitle() {
        return this.selectionDialogTitle("Find");
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

    // SpreadsheetFormulaSelectAnchorComponentContext...................................................................

    @Override
    public Optional<String> formulaText(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
        return this.context.spreadsheetViewportCache()
            .formulaText(spreadsheetExpressionReference);
    }

    // SpreadsheetCellValueAnchorComponentContext.......................................................................

    @Override
    public Optional<SpreadsheetCell> cell(final SpreadsheetSelection selection) {
        return this.context.spreadsheetViewportCache()
            .cell(selection);
    }

    // HasNow...........................................................................................................

    @Override
    public LocalDateTime now() {
        return this.context.now();
    }

    // HasSpreadsheetDeltaFetcherWatcher................................................................................

    @Override
    public HasSpreadsheetDeltaFetcherWatchers hasSpreadsheetDeltaFetcherWatchers() {
        return this.context;
    }

    @Override
    public void findCells(final SpreadsheetId id,
                          final SpreadsheetCellRangeReference cells,
                          final SpreadsheetCellFindQuery find) {
        this.context.spreadsheetDeltaFetcher()
            .getFindCells(
                id,
                cells,
                find
            );
    }

    // HasSpreadsheetMetadata...........................................................................................

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        return this.context.spreadsheetMetadata();
    }

    // SpreadsheetParserProvider........................................................................................

    @Override
    public SpreadsheetParserProvider spreadsheetParserProvider() {
        return this.context;
    }

    // ProviderContext..................................................................................................

    @Override
    public ProviderContext providerContext() {
        return this.context;
    }

    // SpreadsheetDialogComponentContext................................................................................

    @Override
    public SpreadsheetDialogComponentContext spreadsheetDialogComponentContext() {
        return SpreadsheetDialogComponentContexts.basic(
            this.context,
            this.context
        );
    }

    private final AppContext context;

    @Override
    public String toString() {
        return this.context.toString();
    }
}
