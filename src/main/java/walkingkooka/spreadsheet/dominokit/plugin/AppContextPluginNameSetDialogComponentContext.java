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

import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContextDelegator;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.fetcher.HasPluginFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasPluginFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcherWatchersDelegator;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

import java.util.Objects;
import java.util.OptionalInt;

final class AppContextPluginNameSetDialogComponentContext implements PluginNameSetDialogComponentContext,
    HasPluginFetcherWatchersDelegator,
    HasSpreadsheetMetadataFetcherWatchersDelegator,
    SpreadsheetDialogComponentContextDelegator {

    static AppContextPluginNameSetDialogComponentContext with(final AppContext context) {
        return new AppContextPluginNameSetDialogComponentContext(
            Objects.requireNonNull(context, "context")
        );
    }

    private AppContextPluginNameSetDialogComponentContext(final AppContext context) {
        this.context = context;
    }

    @Override
    public String dialogTitle() {
        return "Plugins";
    }

    // HasPluginFetcher.................................................................................................

    @Override
    public void pluginFilter(final String query,
                             final OptionalInt offset,
                             final OptionalInt count) {
        this.context.pluginFetcher()
            .getPluginFilter(
                query,
                offset,
                count
            );
    }

    @Override
    public HasPluginFetcherWatchers hasPluginFetcherWatchers() {
        return this.context;
    }

    // HasSpreadsheetMetadataFetcherWatcher.............................................................................

    @Override
    public void loadSpreadsheetMetadata(final SpreadsheetId id) {
        this.context.spreadsheetMetadataFetcher()
            .getSpreadsheetMetadata(id);
    }

    @Override
    public HasSpreadsheetMetadataFetcherWatchers hasSpreadsheetMetadataFetcherWatchers() {
        return this.context;
    }

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        return this.context.spreadsheetMetadata();
    }

    // SpreadsheetDialogComponentContext................................................................................

    @Override
    public SpreadsheetDialogComponentContext spreadsheetDialogComponentContext() {
        return SpreadsheetDialogComponentContexts.basic(
            this.context,
            this.context
        );
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }

    private final AppContext context;
}
