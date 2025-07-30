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

import walkingkooka.plugin.PluginName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContextDelegator;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.fetcher.HasPluginFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasPluginFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.history.PluginSelectHistoryToken;

import java.util.Locale;
import java.util.Objects;

final class AppContextJarEntryInfoListDialogComponentContext implements JarEntryInfoListDialogComponentContext,
    HasPluginFetcherWatchersDelegator,
    SpreadsheetDialogComponentContextDelegator {

    static AppContextJarEntryInfoListDialogComponentContext with(final AppContext context) {
        return new AppContextJarEntryInfoListDialogComponentContext(
            Objects.requireNonNull(context, "context")
        );
    }

    private AppContextJarEntryInfoListDialogComponentContext(final AppContext context) {
        this.context = context;
    }

    @Override
    public String dialogTitle() {
        return this.pluginDialogTitle("File list");
    }

    @Override
    public PluginName pluginName() {
        return this.historyToken()
            .cast(PluginSelectHistoryToken.class)
            .name();
    }

    @Override
    public Locale locale() {
        return this.context.locale();
    }

    @Override
    public void listJarEntries(final PluginName pluginName) {
        this.context.pluginFetcher()
            .getPluginList(pluginName);
    }

    // HasPluginFetcherWatchersDelegator................................................................................

    @Override
    public HasPluginFetcherWatchers hasPluginFetcherWatchers() {
        return this.context;
    }

    // SpreadsheetDialogComponentContextDelegator.......................................................................

    @Override
    public SpreadsheetDialogComponentContext spreadsheetDialogComponentContext() {
        return SpreadsheetDialogComponentContexts.basic(
            this.context,
            this.context
        );
    }

    private final AppContext context;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }
}

