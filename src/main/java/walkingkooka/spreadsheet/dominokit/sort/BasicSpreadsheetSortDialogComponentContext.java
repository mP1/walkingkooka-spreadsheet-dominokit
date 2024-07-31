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

import java.util.Objects;

final class BasicSpreadsheetSortDialogComponentContext implements SpreadsheetSortDialogComponentContext,
        HistoryTokenContextDelegator,
        SpreadsheetComparatorProviderDelegator {

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
    public SpreadsheetComparatorProvider spreadsheetComparatorProvider() {
        return this.spreadsheetComparatorProvider;
    }

    private final SpreadsheetComparatorProvider spreadsheetComparatorProvider;

    // HistoryTokenContext..............................................................................................

    @Override
    public HistoryTokenContext historyTokenContext() {
        return this.historyTokenContext;
    }

    private final HistoryTokenContext historyTokenContext;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.historyTokenContext.toString();
    }
}
