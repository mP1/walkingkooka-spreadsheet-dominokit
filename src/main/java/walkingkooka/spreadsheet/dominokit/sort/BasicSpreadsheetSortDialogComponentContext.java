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

import walkingkooka.spreadsheet.compare.SpreadsheetComparatorProvider;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorProviderDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContextDelegator;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;

import java.util.Objects;

final class BasicSpreadsheetSortDialogComponentContext implements SpreadsheetSortDialogComponentContext,
        HistoryTokenContextDelegator,
        LoggingContextDelegator,
        SpreadsheetComparatorProviderDelegator {

    static BasicSpreadsheetSortDialogComponentContext with(final SpreadsheetComparatorProvider spreadsheetComparatorProvider,
                                                           final HistoryTokenContext historyTokenContext,
                                                           final LoggingContext loggingContext) {
        Objects.requireNonNull(spreadsheetComparatorProvider, "spreadsheetComparatorProvider");
        Objects.requireNonNull(historyTokenContext, "historyTokenContext");
        Objects.requireNonNull(loggingContext, "loggingContext");

        return new BasicSpreadsheetSortDialogComponentContext(
                spreadsheetComparatorProvider,
                historyTokenContext,
                loggingContext
        );
    }

    private BasicSpreadsheetSortDialogComponentContext(final SpreadsheetComparatorProvider spreadsheetComparatorProvider,
                                                       final HistoryTokenContext historyTokenContext,
                                                       final LoggingContext loggingContext) {
        this.spreadsheetComparatorProvider = spreadsheetComparatorProvider;
        this.historyTokenContext = historyTokenContext;
        this.loggingContext = loggingContext;
    }

    // SpreadsheetSortDialogComponentContext............................................................................

    @Override
    public SpreadsheetComparatorProvider spreadsheetComparatorProvider() {
        return this.spreadsheetComparatorProvider;
    }

    private final SpreadsheetComparatorProvider spreadsheetComparatorProvider;

    // SpreadsheetDialogComponentContextDelegator.......................................................................

    @Override
    public HistoryTokenContext historyTokenContext() {
        return this.historyTokenContext;
    }

    private final HistoryTokenContext historyTokenContext;

    // LoggingContext...................................................................................................

    @Override
    public LoggingContext loggingContext() {
        return this.loggingContext;
    }

    private final LoggingContext loggingContext;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.historyTokenContext.toString();
    }
}
