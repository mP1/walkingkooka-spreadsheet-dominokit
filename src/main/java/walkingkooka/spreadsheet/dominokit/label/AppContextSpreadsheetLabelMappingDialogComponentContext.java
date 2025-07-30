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

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContextDelegator;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetIdHistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;

import java.util.Objects;
import java.util.OptionalInt;

/**
 * A basic implementation of {@link SpreadsheetLabelMappingDialogComponentContext}.
 */
final class AppContextSpreadsheetLabelMappingDialogComponentContext implements SpreadsheetLabelMappingDialogComponentContext,
    SpreadsheetDialogComponentContextDelegator {

    static AppContextSpreadsheetLabelMappingDialogComponentContext with(final AppContext context) {
        Objects.requireNonNull(context, "context");
        return new AppContextSpreadsheetLabelMappingDialogComponentContext(context);
    }

    private AppContextSpreadsheetLabelMappingDialogComponentContext(final AppContext context) {
        this.context = context;
    }

    @Override
    public String dialogTitle() {
        return this.spreadsheetDialogTitle("Label");
    }

    @Override
    public void loadLabel(final SpreadsheetLabelName name) {
        final AppContext context = this.context;
        context.spreadsheetDeltaFetcher()
            .getLabelMapping(
                context.historyToken()
                    .cast(SpreadsheetIdHistoryToken.class)
                    .id(),
                name
            );
    }

    // AddSpreadsheetDeltaFetcherWatchers...............................................................................

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.context.addSpreadsheetDeltaFetcherWatcher(watcher);
    }

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcherOnce(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.context.addSpreadsheetDeltaFetcherWatcherOnce(watcher);
    }

    // SpreadsheetLabelComponentContext.................................................................................

    @Override
    public void findLabelByName(final String text,
                                final OptionalInt offset,
                                final OptionalInt count) {
        final AppContext context = this.context;

        context.spreadsheetDeltaFetcher()
            .getLabelMappingsFindByName(
                context.historyToken()
                    .cast(SpreadsheetIdHistoryToken.class)
                    .id(),
                text,
                offset,
                count
            );
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
