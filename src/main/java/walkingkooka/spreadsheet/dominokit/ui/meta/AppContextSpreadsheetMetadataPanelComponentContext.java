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

package walkingkooka.spreadsheet.dominokit.ui.meta;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

import java.util.Locale;
import java.util.Objects;

final class AppContextSpreadsheetMetadataPanelComponentContext implements SpreadsheetMetadataPanelComponentContext {

    static AppContextSpreadsheetMetadataPanelComponentContext with(final AppContext context) {
        Objects.requireNonNull(context, "context");

        return new AppContextSpreadsheetMetadataPanelComponentContext(context);
    }

    private AppContextSpreadsheetMetadataPanelComponentContext(final AppContext context) {
        super();
        this.context = context;
    }

    // HasLocale........................................................................................................

    @Override
    public Locale locale() {
        return this.context.locale();
    }

    // HasSpreadsheetMetadata...........................................................................................

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        return this.context.spreadsheetMetadata();
    }

    // SpreadsheetMetadataFetcherWatcher.......................................................................................

    @Override
    public Runnable addSpreadsheetMetadataWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.context.addSpreadsheetMetadataWatcher(watcher);
    }

    // HistoryContext...................................................................................................

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
}
