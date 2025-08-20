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

package walkingkooka.spreadsheet.dominokit.viewport;

import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportNavigation;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportNavigationContext;

final class SpreadsheetViewportComponentSpreadsheetViewportComponentTableContext implements SpreadsheetViewportComponentTableContext,
    HistoryContextDelegator,
    LoggingContextDelegator,
    SpreadsheetViewportContextDelegator {


    static SpreadsheetViewportComponentSpreadsheetViewportComponentTableContext with(final SpreadsheetViewportComponent spreadsheetViewportComponent,
                                                                                     final SpreadsheetViewportContext context) {
        return new SpreadsheetViewportComponentSpreadsheetViewportComponentTableContext(
            spreadsheetViewportComponent,
            context
        );
    }

    private SpreadsheetViewportComponentSpreadsheetViewportComponentTableContext(final SpreadsheetViewportComponent spreadsheetViewportComponent,
                                                                                 final SpreadsheetViewportContext context) {
        this.spreadsheetViewportComponent = spreadsheetViewportComponent;
        this.navigationContext = SpreadsheetViewportComponentSpreadsheetViewportComponentTableContextSpreadsheetViewportNavigationContext.with(
            spreadsheetViewportComponent.spreadsheetViewportCache()
        );

        this.context = context;
    }

    @Override
    public int viewportGridWidth() {
        return this.spreadsheetViewportComponent.viewportGridWidth;
    }

    @Override
    public int viewportGridHeight() {
        return this.spreadsheetViewportComponent.viewportGridHeight;
    }

    @Override
    public SpreadsheetViewportCache spreadsheetViewportCache() {
        return this.spreadsheetViewportComponent.spreadsheetViewportCache();
    }

    @Override
    public boolean autoHideScrollbars() {
        return this.spreadsheetViewportComponent.autoHideScrollbars;
    }

    @Override
    public boolean shouldHideZeroValues() {
        return this.spreadsheetViewportComponent.shouldHideZeroValues;
    }

    @Override
    public boolean shouldShowFormulas() {
        return this.spreadsheetViewportComponent.shouldShowFormulas;
    }

    @Override
    public boolean shouldShowHeaders() {
        return this.spreadsheetViewportComponent.shouldShowHeaders;
    }

    @Override
    public boolean mustRefresh() {
        return this.spreadsheetViewportComponent.mustRefresh;
    }

    @Override
    public boolean isShiftKeyDown() {
        return this.spreadsheetViewportComponent.shiftKeyDown;
    }

    @Override
    public AnchoredSpreadsheetSelection extendColumn(final SpreadsheetColumnReference column) {
        return this.update(
            SpreadsheetViewportNavigation.extendColumn(column)
        );
    }

    @Override
    public AnchoredSpreadsheetSelection extendRow(final SpreadsheetRowReference row) {
        return this.update(
            SpreadsheetViewportNavigation.extendRow(row)
        );
    }

    private AnchoredSpreadsheetSelection update(final SpreadsheetViewportNavigation navigation) {
        return navigation.update(
                this.spreadsheetViewportComponent.viewport(),
                this.navigationContext
            ).anchoredSelection()
            .get();
    }

    @Override
    public void pushNavigation(final SpreadsheetViewportNavigation navigation) {
        this.spreadsheetViewportComponent.onNavigation(navigation);
    }

    private final SpreadsheetViewportComponent spreadsheetViewportComponent;

    private final SpreadsheetViewportNavigationContext navigationContext;

    // SpreadsheetViewportContextDelegator..............................................................................

    @Override
    public SpreadsheetViewportContext spreadsheetViewportContext() {
        return this.context;
    }

    private final SpreadsheetViewportContext context;

    // HistoryContext...................................................................................................

    @Override
    public HistoryContext historyContext() {
        return this.spreadsheetViewportComponent.context;
    }

    // LoggingContext...................................................................................................

    @Override
    public LoggingContext loggingContext() {
        return this.spreadsheetViewportComponent.context;
    }

    // toString.........................................................................................................

    @Override
    public String toString() {
        return this.spreadsheetViewportComponent.toString();
    }
}
