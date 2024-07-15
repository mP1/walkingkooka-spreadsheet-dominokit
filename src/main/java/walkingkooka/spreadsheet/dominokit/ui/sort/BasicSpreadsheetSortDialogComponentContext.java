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

package walkingkooka.spreadsheet.dominokit.ui.sort;

import walkingkooka.spreadsheet.compare.SpreadsheetComparator;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorProvider;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;

import java.util.Objects;
import java.util.Set;

final class BasicSpreadsheetSortDialogComponentContext implements SpreadsheetSortDialogComponentContext {

    static BasicSpreadsheetSortDialogComponentContext with(final SpreadsheetComparatorProvider spreadsheetComparatorProvider,
                                                           final HistoryTokenContext historyTokenContext) {
        Objects.requireNonNull(spreadsheetComparatorProvider, "spreadsheetComparatorProvider");
        Objects.requireNonNull(historyTokenContext, "historyTokenContext");

        return new BasicSpreadsheetSortDialogComponentContext(
                spreadsheetComparatorProvider,
                historyTokenContext
        );
    }

    private BasicSpreadsheetSortDialogComponentContext(final SpreadsheetComparatorProvider spreadsheetComparatorProvider,
                                                       final HistoryTokenContext historyTokenContext) {
        this.spreadsheetComparatorProvider = spreadsheetComparatorProvider;
        this.historyTokenContext = historyTokenContext;
    }

    // SpreadsheetSortDialogComponentContext............................................................................

    @Override
    public SpreadsheetComparator<?> spreadsheetComparator(final SpreadsheetComparatorName spreadsheetComparatorName) {
        return this.spreadsheetComparatorProvider.spreadsheetComparator(spreadsheetComparatorName);
    }

    @Override
    public Set<SpreadsheetComparatorInfo> spreadsheetComparatorInfos() {
        return this.spreadsheetComparatorProvider.spreadsheetComparatorInfos();
    }

    private final SpreadsheetComparatorProvider spreadsheetComparatorProvider;

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

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.historyTokenContext.toString();
    }
}
