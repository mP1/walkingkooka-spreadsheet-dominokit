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

import walkingkooka.collect.map.Maps;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.tree.json.JsonNode;

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
            Maps.immutable(value)
        );
    }

    private SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken(final SpreadsheetId id,
                                                                final SpreadsheetName name,
                                                                final AnchoredSpreadsheetSelection anchoredSelection,
                                                                final Map<SpreadsheetCellReference, Optional<DecimalNumberSymbols>> value) {
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
            value
        );
    }

    @Override
    Map<SpreadsheetCellReference, Optional<DecimalNumberSymbols>> parseSaveValue(final TextCursor cursor) {
        return parseMapWithOptionalValues(
            cursor,
            DecimalNumberSymbols.class
        );
    }

    // HasUrlFragment..................................................................................................

    @Override
    UrlFragment urlFragmentSaveEntity() {
        return DECIMAL_NUMBER_SYMBOLS;
    }

    @Override
    JsonNode saveValueUrlFragmentValueToJson(final Optional<DecimalNumberSymbols> value) {
        return MARSHALL_CONTEXT.marshallOptional(value);
    }

    // HistoryTokenWatcher..............................................................................................

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.spreadsheetDeltaFetcher()
            .patchCellsDecimalNumberSymbols(
                this.id(),
                this.anchoredSelection().selection(),
                this.value()
            );
        context.pushHistoryToken(previous);
    }
}
