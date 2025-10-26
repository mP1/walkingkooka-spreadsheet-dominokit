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

package walkingkooka.spreadsheet.dominokit.cell;

import walkingkooka.net.email.EmailAddress;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.validation.ValueTypeName;

final class BasicSpreadsheetCellValueDialogComponentContextEmail extends BasicSpreadsheetCellValueDialogComponentContext<EmailAddress> {

    static BasicSpreadsheetCellValueDialogComponentContextEmail with(final SpreadsheetViewportCache viewportCache,
                                                                     final HasSpreadsheetDeltaFetcherWatchers deltaFetcherWatchers,
                                                                     final HistoryContext historyContext,
                                                                     final LoggingContext loggingContext) {
        return new BasicSpreadsheetCellValueDialogComponentContextEmail(
            viewportCache,
            deltaFetcherWatchers,
            historyContext,
            loggingContext
        );
    }

    private BasicSpreadsheetCellValueDialogComponentContextEmail(final SpreadsheetViewportCache viewportCache,
                                                                 final HasSpreadsheetDeltaFetcherWatchers deltaFetcherWatchers,
                                                                 final HistoryContext historyContext,
                                                                 final LoggingContext loggingContext) {
        super(
            viewportCache,
            deltaFetcherWatchers,
            historyContext,
            loggingContext
        );
    }

    @Override
    public String id() {
        return ID;
    }

    private final static String ID = "cellValueEmail";

    @Override
    public ValueTypeName valueType() {
        return ValueTypeName.EMAIL;
    }
}
