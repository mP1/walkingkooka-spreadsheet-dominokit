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

import java.util.Objects;

public final class AppContextPluginUploadDialogComponentContext implements PluginUploadDialogComponentContext,
    HasPluginFetcherWatchersDelegator,
    HistoryContextDelegator,
    LoggingContextDelegator {

    public static AppContextPluginUploadDialogComponentContext with(final AppContext context) {
        return new AppContextPluginUploadDialogComponentContext(
            Objects.requireNonNull(context, "context")
        );
    }

    private AppContextPluginUploadDialogComponentContext(final AppContext context) {
        this.context = context;
    }

    // CanGiveFocus.....................................................................................................

    @Override
    public void giveFocus(final Runnable focus) {
        this.context.giveFocus(focus);
    }

    // HasPluginFetcherWatchersDelegator................................................................................

    @Override
    public HasPluginFetcherWatchers hasPluginFetcherWatchers() {
        return this.context;
    }

    // HistoryContextDelegator.....................................................................................

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

    // toString.........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }
}
