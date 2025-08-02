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

package walkingkooka.spreadsheet.dominokit.datetimesymbols;

import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellDateTimeSymbolsHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellDateTimeSymbolsSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellDateTimeSymbolsSelectHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Objects;
import java.util.Optional;

final class AppContextDateTimeSymbolsDialogComponentContextSpreadsheetCell extends AppContextDateTimeSymbolsDialogComponentContext {

    static AppContextDateTimeSymbolsDialogComponentContextSpreadsheetCell with(final AppContext context) {
        return new AppContextDateTimeSymbolsDialogComponentContextSpreadsheetCell(
            Objects.requireNonNull(context, "context")
        );
    }

    private AppContextDateTimeSymbolsDialogComponentContextSpreadsheetCell(final AppContext context) {
        super(context);
    }

    @Override
    public String dialogTitle() {
        return this.selectionValueDialogTitle(DateTimeSymbols.class);
    }

    @Override
    public Optional<DateTimeSymbols> copyDateTimeSymbols() {
        return this.context.spreadsheetMetadata()
            .get(SpreadsheetMetadataPropertyName.DATE_TIME_SYMBOLS);
    }

    @Override
    public Optional<DateTimeSymbols> loadDateTimeSymbols() {
        final AppContext context = this.context;

        return context.spreadsheetViewportCache()
            .cell(
                context.historyToken()
                    .selection()
                    .get()
            ).flatMap(SpreadsheetCell::dateTimeSymbols);
    }

    @Override
    public void save(final Optional<DateTimeSymbols> symbols) {
        final AppContext context = this.context;
        context.spreadsheetDeltaFetcher()
            .patchDateTimeSymbols(
                context.spreadsheetMetadata()
                    .getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_ID),
                context.historyToken()
                    .cast(SpreadsheetCellDateTimeSymbolsHistoryToken.class)
                    .selection()
                    .get(),
                symbols
            );
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetCellDateTimeSymbolsSaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellDateTimeSymbolsSelectHistoryToken;
    }
}
