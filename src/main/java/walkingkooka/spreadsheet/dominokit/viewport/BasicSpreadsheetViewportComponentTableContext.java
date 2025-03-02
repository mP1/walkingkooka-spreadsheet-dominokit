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
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportNavigation;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportNavigationContext;
import walkingkooka.tree.text.TextStyle;

final class BasicSpreadsheetViewportComponentTableContext implements SpreadsheetViewportComponentTableContext,
    HistoryContextDelegator,
    LoggingContextDelegator {


    static BasicSpreadsheetViewportComponentTableContext with(final HistoryContext historyContext,
                                                              final SpreadsheetViewportCache viewportCache,
                                                              final boolean hideZeroValues,
                                                              final TextStyle defaultCellStyle,
                                                              final boolean mustRefresh,
                                                              final boolean shiftKeyDown,
                                                              final SpreadsheetViewport spreadsheetViewport,
                                                              final LoggingContext loggingContext) {
        return new BasicSpreadsheetViewportComponentTableContext(
            historyContext,
            viewportCache,
            hideZeroValues,
            defaultCellStyle,
            mustRefresh,
            shiftKeyDown,
            spreadsheetViewport,
            loggingContext
        );
    }

    private BasicSpreadsheetViewportComponentTableContext(final HistoryContext historyContext,
                                                          final SpreadsheetViewportCache viewportCache,
                                                          final boolean hideZeroValues,
                                                          final TextStyle defaultCellStyle,
                                                          final boolean mustRefresh,
                                                          final boolean shiftKeyDown,
                                                          final SpreadsheetViewport spreadsheetViewport,
                                                          final LoggingContext loggingContext) {
        this.historyContext = historyContext;
        this.viewportCache = viewportCache;
        this.hideZeroValues = hideZeroValues;
        this.defaultCellStyle = defaultCellStyle;
        this.mustRefresh = mustRefresh;
        this.shiftKeyDown = shiftKeyDown;

        this.spreadsheetViewport = spreadsheetViewport;
        this.navigationContext = BasicSpreadsheetViewportComponentTableContextSpreadsheetViewportNavigationContext.with(viewportCache);

        this.loggingContext = loggingContext;
    }

    @Override
    public SpreadsheetViewportCache spreadsheetViewportCache() {
        return this.viewportCache;
    }

    private final SpreadsheetViewportCache viewportCache;

    @Override
    public boolean hideZeroValues() {
        return this.hideZeroValues;
    }

    private final boolean hideZeroValues;

    @Override
    public TextStyle defaultCellStyle() {
        return this.defaultCellStyle;
    }

    private final TextStyle defaultCellStyle;

    @Override
    public boolean mustRefresh() {
        return this.mustRefresh;
    }

    private final boolean mustRefresh;

    @Override
    public boolean isShiftKeyDown() {
        return this.shiftKeyDown;
    }

    private final boolean shiftKeyDown;

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
                this.spreadsheetViewport,
                this.navigationContext
            ).anchoredSelection()
            .get();
    }

    private final SpreadsheetViewport spreadsheetViewport;

    private final SpreadsheetViewportNavigationContext navigationContext;

    // HistoryContext..............................................................................................

    @Override
    public HistoryContext historyContext() {
        return this.historyContext;
    }

    private final HistoryContext historyContext;

    // LoggingContext...................................................................................................

    @Override
    public LoggingContext loggingContext() {
        return this.loggingContext;
    }

    private final LoggingContext loggingContext;
}
