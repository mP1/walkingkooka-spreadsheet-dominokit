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

package walkingkooka.spreadsheet.dominokit.sort;

import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorProvider;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorProviderDelegator;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;

import java.util.Objects;

final class AppContextSpreadsheetCellSortDialogComponentContext implements SpreadsheetCellSortDialogComponentContext,
    HistoryContextDelegator,
    LoggingContextDelegator,
    SpreadsheetComparatorProviderDelegator {

    static AppContextSpreadsheetCellSortDialogComponentContext with(final AppContext context) {
        Objects.requireNonNull(context, "context");

        return new AppContextSpreadsheetCellSortDialogComponentContext(context);
    }

    private AppContextSpreadsheetCellSortDialogComponentContext(final AppContext context) {
        this.context = context;
    }

    @Override
    public String dialogTitle() {
        return this.spreadsheetDialogTitle("Sort");
    }

    // SpreadsheetViewportCache.........................................................................................

    @Override
    public SpreadsheetViewportCache spreadsheetViewportCache() {
        return this.context.spreadsheetViewportCache();
    }

    // SpreadsheetCellSortDialogComponentContext............................................................................

    @Override
    public SpreadsheetComparatorProvider spreadsheetComparatorProvider() {
        return this.context;
    }

    // CanGiveFocus.....................................................................................................

    @Override
    public final void giveFocus(final Runnable focus) {
        this.context.giveFocus(focus);
    }

    // DialogComponentContextDelegator..................................................................................

    @Override
    public HistoryContext historyContext() {
        return this.context;
    }

    // LoggingContext...................................................................................................

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
