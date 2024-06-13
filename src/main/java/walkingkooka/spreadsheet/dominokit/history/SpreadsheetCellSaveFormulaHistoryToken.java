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
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.tree.json.JsonNode;

import java.util.Map;

/**
 * This {@link HistoryToken} is used by to paste a formula over the selected cells.
 * <pre>
 * /123/SpreadsheetName456/cell/A1/save/formula/{"A1":"=1"}
 *
 * /spreadsheet-id/spreadsheet-name/cell/cell or cell-range or label/save/formula/multi formatter patch as json.
 * </pre>
 */
public final class SpreadsheetCellSaveFormulaHistoryToken extends SpreadsheetCellSaveMapHistoryToken<String> {

    static SpreadsheetCellSaveFormulaHistoryToken with(final SpreadsheetId id,
                                                       final SpreadsheetName name,
                                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                                       final Map<SpreadsheetCellReference, String> value) {
        return new SpreadsheetCellSaveFormulaHistoryToken(
                id,
                name,
                anchoredSelection,
                Maps.immutable(value)
        );
    }

    private SpreadsheetCellSaveFormulaHistoryToken(final SpreadsheetId id,
                                                   final SpreadsheetName name,
                                                   final AnchoredSpreadsheetSelection anchoredSelection,
                                                   final Map<SpreadsheetCellReference, String> value) {
        super(
                id,
                name,
                anchoredSelection,
                value
        );
    }

    @Override
    Map<SpreadsheetCellReference, String> parseSaveValue(final TextCursor cursor) {
        return parseMapWithNullableValues(
                cursor,
                String.class
        );
    }

    @Override
    SpreadsheetCellSaveFormulaHistoryToken replace(final SpreadsheetId id,
                                                   final SpreadsheetName name,
                                                   final AnchoredSpreadsheetSelection anchoredSelection,
                                                   final Map<SpreadsheetCellReference, String> value) {
        return new SpreadsheetCellSaveFormulaHistoryToken(
                id,
                name,
                anchoredSelection,
                value
        );
    }

    // HasUrlFragment...................................................................................................

    @Override
    UrlFragment urlFragmentSaveEntity() {
        return FORMULA;
    }

    private final static UrlFragment FORMULA = UrlFragment.parse("formula");

    @Override
    JsonNode saveValueUrlFragmentValueToJson(final String value) {
        return MARSHALL_CONTEXT.marshall(value);
    }

    // history..........................................................................................................

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        final Map<SpreadsheetCellReference, SpreadsheetFormula> cellToFormula = Maps.sorted();

        this.value().forEach(
                (cell, formulaString) -> cellToFormula.put(
                        cell,
                        SpreadsheetFormula.EMPTY.setText(formulaString)
                )
        );

        context.spreadsheetDeltaFetcher()
                .patchCellsFormula(
                        this.id(),
                        this.anchoredSelection().selection(),
                        cellToFormula
                );
        context.pushHistoryToken(previous);
    }
}
