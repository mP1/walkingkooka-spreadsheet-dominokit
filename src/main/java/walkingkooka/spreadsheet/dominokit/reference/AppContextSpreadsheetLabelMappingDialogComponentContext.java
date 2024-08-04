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

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContextDelegator;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetIdHistoryToken;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelFetcherWatcher;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;

import java.util.Objects;

/**
 * A basic implementation of {@link SpreadsheetLabelMappingDialogComponentContext}.
 */
final class AppContextSpreadsheetLabelMappingDialogComponentContext implements SpreadsheetLabelMappingDialogComponentContext,
        HistoryTokenContextDelegator,
        LoggingContextDelegator {

    static AppContextSpreadsheetLabelMappingDialogComponentContext with(final AppContext context) {
        Objects.requireNonNull(context, "context");
        return new AppContextSpreadsheetLabelMappingDialogComponentContext(context);
    }

    private AppContextSpreadsheetLabelMappingDialogComponentContext(final AppContext context) {
        this.context = context;
    }

    @Override
    public void addLabelMappingWatcher(final SpreadsheetLabelFetcherWatcher watcher) {
        this.context.addSpreadsheetLabelFetcherWatcher(watcher);
    }

    @Override
    public void loadLabel(final SpreadsheetLabelName name) {
        final AppContext context = this.context;
        context.spreadsheetLabelFetcher()
                .loadLabelMapping(
                        context.historyToken()
                                .cast(SpreadsheetIdHistoryToken.class)
                                .id(),
                        name
                );
    }

    // HistoryTokenContext..............................................................................................

    @Override
    public HistoryTokenContext historyTokenContext() {
        return this.context;
    }

    // LoggingContext...................................................................................................

    @Override
    public LoggingContext loggingContext() {
        return this.context;
    }

    private final AppContext context;

    @Override
    public String toString() {
        return this.context.toString();
    }
}
