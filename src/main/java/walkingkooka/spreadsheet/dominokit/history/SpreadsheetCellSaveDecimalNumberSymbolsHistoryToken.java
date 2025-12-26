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

import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToDecimalNumberSymbolsMap;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.text.cursor.TextCursor;

import java.util.Map;
import java.util.Optional;

/**
 * This {@link HistoryToken} is used by to paste a {@link DecimalNumberSymbols} for many cells over another range.
 * <pre>
 * /123/SpreadsheetName456/cell/A1/save/decimalNumberSymbols/XYZ
 *
 * /spreadsheet-id/spreadsheet-name/cell/cell or cell-range or label/save/decimalNumberSymbols/{@link DecimalNumberSymbols} for each selected cell.
 * </pre>
 */
public final class SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken extends SpreadsheetCellSaveMapHistoryToken<Optional<DecimalNumberSymbols>> {

    static SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken with(final SpreadsheetId id,
                                                                    final SpreadsheetName name,
                                                                    final AnchoredSpreadsheetSelection anchoredSelection,
                                                                    final Map<SpreadsheetCellReference, Optional<DecimalNumberSymbols>> value) {
        return new SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken(
            id,
            name,
            anchoredSelection,
            SpreadsheetCellReferenceToDecimalNumberSymbolsMap.with(value)
        );
    }

    private SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken(final SpreadsheetId id,
                                                                final SpreadsheetName name,
                                                                final AnchoredSpreadsheetSelection anchoredSelection,
                                                                final SpreadsheetCellReferenceToDecimalNumberSymbolsMap value) {
        super(
            id,
            name,
            anchoredSelection,
            value
        );
    }

    @Override //
    SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken replace(final SpreadsheetId id,
                                                                final SpreadsheetName name,
                                                                final AnchoredSpreadsheetSelection anchoredSelection,
                                                                final Map<SpreadsheetCellReference, Optional<DecimalNumberSymbols>> value) {
        return new SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken(
            id,
            name,
            anchoredSelection,
            SpreadsheetCellReferenceToDecimalNumberSymbolsMap.with(value)
        );
    }

    @Override
    SpreadsheetCellReferenceToDecimalNumberSymbolsMap parseSaveValue(final TextCursor cursor) {
        return parseJson(
            cursor,
            SpreadsheetCellReferenceToDecimalNumberSymbolsMap.class
        );
    }

    // HasUrlFragment..................................................................................................

    @Override
    UrlFragment urlFragmentSaveEntity() {
        return DECIMAL_NUMBER_SYMBOLS;
    }

    // HistoryTokenWatcher..............................................................................................

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.spreadsheetDeltaFetcher()
            .patchCellsDecimalNumberSymbols(
                this.id,
                this.anchoredSelection().selection(),
                this.value()
            );
        context.pushHistoryToken(previous);
    }
}
