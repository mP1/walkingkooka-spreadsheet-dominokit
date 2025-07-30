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

package walkingkooka.spreadsheet.dominokit.plugin;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.HasPluginFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasPluginFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;

import java.util.Locale;
import java.util.Objects;

final class AppContextPluginSetDialogComponentContext implements PluginSetDialogComponentContext,
    HistoryContextDelegator,
    LoggingContextDelegator,
    HasPluginFetcherWatchersDelegator {

    static AppContextPluginSetDialogComponentContext with(final AppContext context) {
        return new AppContextPluginSetDialogComponentContext(
            Objects.requireNonNull(context, "context")
        );
    }

    private AppContextPluginSetDialogComponentContext(final AppContext context) {
        this.context = context;
    }

    // HasPluginFetcherWatchersDelegator................................................................................

    @Override
    public HasPluginFetcherWatchers hasPluginFetcherWatchers() {
        return this.context;
    }

    // HasLocale........................................................................................................

    @Override
    public Locale locale() {
        return this.context.locale();
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

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }

    private final AppContext context;
}
