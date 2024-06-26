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

package walkingkooka.spreadsheet.dominokit.ui.labelmapping;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetIdHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingFetcherWatcher;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;

import java.util.Objects;

/**
 * A basic implementation of {@link SpreadsheetLabelMappingDialogComponentContext}.
 */
final class AppContextSpreadsheetLabelMappingDialogComponentContext implements SpreadsheetLabelMappingDialogComponentContext {

    static AppContextSpreadsheetLabelMappingDialogComponentContext with(final AppContext context) {
        Objects.requireNonNull(context, "context");
        return new AppContextSpreadsheetLabelMappingDialogComponentContext(context);
    }

    private AppContextSpreadsheetLabelMappingDialogComponentContext(final AppContext context) {
        this.context = context;
    }

    @Override
    public void addLabelMappingWatcher(final SpreadsheetLabelMappingFetcherWatcher watcher) {
        this.context.addSpreadsheetLabelMappingFetcherWatcher(watcher);
    }

    @Override
    public void loadLabel(final SpreadsheetLabelName name) {
        final AppContext context = this.context;
        context.spreadsheetLabelMappingFetcher()
                .loadLabelMapping(
                        context.historyToken()
                                .cast(SpreadsheetIdHistoryToken.class)
                                .id(),
                        name
                );
    }

    @Override
    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
        return this.context.addHistoryTokenWatcher(watcher);
    }

    @Override
    public Runnable addHistoryTokenWatcherOnce(final HistoryTokenWatcher watcher) {
        return this.context.addHistoryTokenWatcherOnce(watcher);
    }

    @Override
    public HistoryToken historyToken() {
        return this.context.historyToken();
    }

    @Override
    public void pushHistoryToken(final HistoryToken token) {
        this.context.pushHistoryToken(token);
    }

    @Override
    public void fireCurrentHistoryToken() {
        this.context.fireCurrentHistoryToken();
    }

    // LoggingContext...................................................................................................

    @Override
    public void debug(final Object... values) {
        this.context.debug(values);
    }

    @Override
    public void error(final Object... values) {
        this.context.error(values);
    }

    private final AppContext context;

    @Override
    public String toString() {
        return this.context.toString();
    }
}
