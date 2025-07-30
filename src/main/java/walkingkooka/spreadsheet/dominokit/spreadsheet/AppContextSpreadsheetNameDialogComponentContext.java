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

package walkingkooka.spreadsheet.dominokit.spreadsheet;

import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContextDelegator;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcherWatchersDelegator;

abstract class AppContextSpreadsheetNameDialogComponentContext implements SpreadsheetNameDialogComponentContext,
    SpreadsheetDialogComponentContextDelegator,
    HasSpreadsheetMetadataFetcherWatchersDelegator {

    AppContextSpreadsheetNameDialogComponentContext(final AppContext context) {
        this.context = context;
    }

    @Override
    public final void loadSpreadsheetMetadata(final SpreadsheetId id) {
        this.context.spreadsheetMetadataFetcher()
            .getSpreadsheetMetadata(id);
    }

    @Override
    public final HasSpreadsheetMetadataFetcherWatchers hasSpreadsheetMetadataFetcherWatchers() {
        return this.context;
    }

    // SpreadsheetDialogComponentContext................................................................................

    @Override
    public final SpreadsheetDialogComponentContext spreadsheetDialogComponentContext() {
        return SpreadsheetDialogComponentContexts.basic(
            this.context,
            this.context
        );
    }

    private final AppContext context;

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.context.toString();
    }
}
