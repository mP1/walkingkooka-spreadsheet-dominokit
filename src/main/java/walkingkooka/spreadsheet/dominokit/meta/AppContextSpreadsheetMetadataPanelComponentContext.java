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

package walkingkooka.spreadsheet.dominokit.meta;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.RefreshContextDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

import java.util.Locale;
import java.util.Objects;

final class AppContextSpreadsheetMetadataPanelComponentContext implements SpreadsheetMetadataPanelComponentContext,
    RefreshContextDelegator {

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
    public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.context.addSpreadsheetMetadataFetcherWatcher(watcher);
    }

    // RefreshContextDelegator..........................................................................................

    @Override
    public RefreshContext refreshContext() {
        return this.context;
    }

    private final AppContext context;
}
