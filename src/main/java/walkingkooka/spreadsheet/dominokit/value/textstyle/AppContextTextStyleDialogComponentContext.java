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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContextDelegator;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

import java.util.Objects;

abstract class AppContextTextStyleDialogComponentContext implements TextStyleDialogComponentContext,
    DialogComponentContextDelegator,
    ComponentLifecycleMatcher {

    AppContextTextStyleDialogComponentContext(final AppContext context) {
        super();
        this.context = Objects.requireNonNull(context, "context");
    }

    @Override
    public final SpreadsheetMetadata spreadsheetMetadata() {
        return this.context.spreadsheetMetadata();
    }

    @Override
    public final Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.context.addSpreadsheetMetadataFetcherWatcher(watcher);
    }

    // DialogComponentContextDelegator..................................................................................

    @Override
    public final DialogComponentContext dialogComponentContext() {
        return DialogComponentContexts.basic(this.context);
    }

    final AppContext context;

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.context.toString();
    }
}
