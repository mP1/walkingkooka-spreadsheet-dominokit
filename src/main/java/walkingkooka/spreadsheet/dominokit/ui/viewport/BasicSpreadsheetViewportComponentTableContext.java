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

package walkingkooka.spreadsheet.dominokit.ui.viewport;

import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.tree.text.TextStyle;

final class BasicSpreadsheetViewportComponentTableContext implements SpreadsheetViewportComponentTableContext {


    static BasicSpreadsheetViewportComponentTableContext with(final HistoryTokenContext historyTokenContext,
                                                              final SpreadsheetViewportCache viewportCache,
                                                              final boolean hideZeroValues,
                                                              final TextStyle defaultCellStyle,
                                                              final boolean mustRefresh,
                                                              final boolean shiftKeyDown,
                                                              final LoggingContext loggingContext) {
        return new BasicSpreadsheetViewportComponentTableContext(
                historyTokenContext,
                viewportCache,
                hideZeroValues,
                defaultCellStyle,
                mustRefresh,
                shiftKeyDown,
                loggingContext
        );
    }

    private BasicSpreadsheetViewportComponentTableContext(final HistoryTokenContext historyTokenContext,
                                                          final SpreadsheetViewportCache viewportCache,
                                                          final boolean hideZeroValues,
                                                          final TextStyle defaultCellStyle,
                                                          final boolean mustRefresh,
                                                          final boolean shiftKeyDown,
                                                          final LoggingContext loggingContext) {
        this.historyTokenContext = historyTokenContext;
        this.viewportCache = viewportCache;
        this.hideZeroValues = hideZeroValues;
        this.defaultCellStyle = defaultCellStyle;
        this.mustRefresh = mustRefresh;
        this.shiftKeyDown = shiftKeyDown;
        this.loggingContext = loggingContext;
    }

    @Override
    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
        return this.historyTokenContext.addHistoryTokenWatcher(watcher);
    }

    @Override
    public Runnable addHistoryTokenWatcherOnce(final HistoryTokenWatcher watcher) {
        return this.historyTokenContext.addHistoryTokenWatcherOnce(watcher);
    }

    @Override
    public HistoryToken historyToken() {
        return this.historyTokenContext.historyToken();
    }

    @Override
    public void pushHistoryToken(final HistoryToken token) {
        this.historyTokenContext.pushHistoryToken(token);
    }

    @Override
    public void fireCurrentHistoryToken() {
        this.historyTokenContext.fireCurrentHistoryToken();
    }

    private final HistoryTokenContext historyTokenContext;

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
    public void debug(final Object... values) {
        this.loggingContext.debug(values);
    }

    @Override
    public void error(final Object... values) {
        this.loggingContext.error(values);
    }

    private final LoggingContext loggingContext;
}
