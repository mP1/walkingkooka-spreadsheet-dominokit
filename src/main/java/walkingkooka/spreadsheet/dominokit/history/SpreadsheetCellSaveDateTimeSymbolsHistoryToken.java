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

package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToDateTimeSymbolsMap;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.text.cursor.TextCursor;

import java.util.Map;
import java.util.Optional;

/**
 * This {@link HistoryToken} is used by to paste a {@link DateTimeSymbols} for many cells over another range.
 * <pre>
 * /123/SpreadsheetName456/cell/A1/save/dateTimeSymbols/XYZ
 *
 * /spreadsheet-id/spreadsheet-name/cell/cell or cell-range or label/save/dateTimeSymbols/{@link DateTimeSymbols} for each selected cell.
 * </pre>
 */
public final class SpreadsheetCellSaveDateTimeSymbolsHistoryToken extends SpreadsheetCellSaveMapHistoryToken<Optional<DateTimeSymbols>> {

    static SpreadsheetCellSaveDateTimeSymbolsHistoryToken with(final SpreadsheetId id,
                                                               final SpreadsheetName name,
                                                               final AnchoredSpreadsheetSelection anchoredSelection,
                                                               final Map<SpreadsheetCellReference, Optional<DateTimeSymbols>> value) {
        return new SpreadsheetCellSaveDateTimeSymbolsHistoryToken(
            id,
            name,
            anchoredSelection,
            SpreadsheetCellReferenceToDateTimeSymbolsMap.with(value)
        );
    }

    private SpreadsheetCellSaveDateTimeSymbolsHistoryToken(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final AnchoredSpreadsheetSelection anchoredSelection,
                                                           final SpreadsheetCellReferenceToDateTimeSymbolsMap value) {
        super(
            id,
            name,
            anchoredSelection,
            value
        );
    }

    @Override //
    SpreadsheetCellSaveDateTimeSymbolsHistoryToken replace(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final AnchoredSpreadsheetSelection anchoredSelection,
                                                           final Map<SpreadsheetCellReference, Optional<DateTimeSymbols>> value) {
        return new SpreadsheetCellSaveDateTimeSymbolsHistoryToken(
            id,
            name,
            anchoredSelection,
            SpreadsheetCellReferenceToDateTimeSymbolsMap.with(value)
        );
    }

    @Override
    SpreadsheetCellReferenceToDateTimeSymbolsMap parseSaveValue(final TextCursor cursor) {
        return parseJson(
            cursor,
            SpreadsheetCellReferenceToDateTimeSymbolsMap.class
        );
    }

    // HasUrlFragment..................................................................................................

    @Override
    UrlFragment urlFragmentSaveEntity() {
        return DATE_TIME_SYMBOLS;
    }

    // HistoryTokenWatcher..............................................................................................

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.spreadsheetDeltaFetcher()
            .patchCellsDateTimeSymbols(
                this.id,
                this.anchoredSelection().selection(),
                this.value()
            );
        context.pushHistoryToken(previous);
    }
}
