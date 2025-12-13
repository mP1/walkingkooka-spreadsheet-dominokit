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

package walkingkooka.spreadsheet.dominokit.cell.value;

import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.RefreshContextDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.text.CaseKind;
import walkingkooka.validation.ValueType;

import java.util.Objects;
import java.util.Optional;

final class BasicSpreadsheetCellValueDialogComponentContext<T> implements SpreadsheetCellValueDialogComponentContext<T>,
    RefreshContextDelegator {

    static <T> BasicSpreadsheetCellValueDialogComponentContext<T> with(final ValueType valueType,
                                                                       final SpreadsheetViewportCache viewportCache,
                                                                       final HasSpreadsheetDeltaFetcherWatchers deltaFetcherWatchers,
                                                                       final RefreshContext refreshContext) {
        return new BasicSpreadsheetCellValueDialogComponentContext<>(
            Objects.requireNonNull(valueType, "valueType"),
            Objects.requireNonNull(viewportCache, "viewportCache"),
            Objects.requireNonNull(deltaFetcherWatchers, "deltaFetcherWatchers"),
            Objects.requireNonNull(refreshContext, "refreshContext")
        );
    }

    private BasicSpreadsheetCellValueDialogComponentContext(final ValueType valueType,
                                                            final SpreadsheetViewportCache viewportCache,
                                                            final HasSpreadsheetDeltaFetcherWatchers deltaFetcherWatchers,
                                                            final RefreshContext refreshContext) {
        super();

        this.valueType = valueType;
        this.viewportCache = viewportCache;
        this.deltaFetcherWatchers = deltaFetcherWatchers;
        this.refreshContext = refreshContext;
    }

    @Override
    public String id() {
        return SpreadsheetCell.class.getSimpleName() + "Value";
    }

    @Override
    public String dialogTitle() {
        return this.selectionDialogTitle(
            CaseKind.CAMEL.change(
                this.valueType()
                    .text(),
                CaseKind.TITLE
            )
        );
    }

    @Override
    public Optional<SpreadsheetCell> cell() {
        return this.viewportCache.historyTokenCell();
    }

    private final SpreadsheetViewportCache viewportCache;

    @Override
    public ValueType valueType() {
        return this.valueType;
    }

    private final ValueType valueType;

    // RefreshContextDelegator..........................................................................................

    @Override
    public RefreshContext refreshContext() {
        return this.refreshContext;
    }

    private final RefreshContext refreshContext;

    // SpreadsheetDeltaFetcherWatchers..................................................................................

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.deltaFetcherWatchers.addSpreadsheetDeltaFetcherWatcher(watcher);
    }

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcherOnce(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.deltaFetcherWatchers.addSpreadsheetDeltaFetcherWatcherOnce(watcher);
    }

    private final HasSpreadsheetDeltaFetcherWatchers deltaFetcherWatchers;

    // toString.........................................................................................................

    @Override
    public String toString() {
        return this.refreshContext.toString();
    }
}
