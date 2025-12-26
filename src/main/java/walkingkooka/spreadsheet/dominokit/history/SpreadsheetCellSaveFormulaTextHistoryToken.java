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

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToFormulaTextMap;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.text.cursor.TextCursor;

import java.util.Map;

/**
 * This {@link HistoryToken} is used by to paste a formula over the selected cells.
 * <pre>
 * /123/SpreadsheetName456/cell/A1/save/formula/{"A1":"=1"}
 *
 * /spreadsheet-id/spreadsheet-name/cell/cell or cell-range or label/save/formula/multi formatter patch as json.
 * </pre>
 */
public final class SpreadsheetCellSaveFormulaTextHistoryToken extends SpreadsheetCellSaveMapHistoryToken<String> {

    static SpreadsheetCellSaveFormulaTextHistoryToken with(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final AnchoredSpreadsheetSelection anchoredSelection,
                                                           final Map<SpreadsheetCellReference, String> value) {
        return new SpreadsheetCellSaveFormulaTextHistoryToken(
            id,
            name,
            anchoredSelection,
            SpreadsheetCellReferenceToFormulaTextMap.with(value)
        );
    }

    private SpreadsheetCellSaveFormulaTextHistoryToken(final SpreadsheetId id,
                                                       final SpreadsheetName name,
                                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                                       final SpreadsheetCellReferenceToFormulaTextMap value) {
        super(
            id,
            name,
            anchoredSelection,
            value
        );
    }

    @Override
    Map<SpreadsheetCellReference, String> parseSaveValue(final TextCursor cursor) {
        return parseJson(
            cursor,
            SpreadsheetCellReferenceToFormulaTextMap.class
        );
    }

    @Override
    SpreadsheetCellSaveFormulaTextHistoryToken replace(final SpreadsheetId id,
                                                       final SpreadsheetName name,
                                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                                       final Map<SpreadsheetCellReference, String> value) {
        return new SpreadsheetCellSaveFormulaTextHistoryToken(
            id,
            name,
            anchoredSelection,
            SpreadsheetCellReferenceToFormulaTextMap.with(value)
        );
    }

    // HasUrlFragment...................................................................................................

    @Override
    UrlFragment urlFragmentSaveEntity() {
        return FORMULA;
    }

    // history..........................................................................................................

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.spreadsheetDeltaFetcher()
            .patchCellsFormulaText(
                this.id,
                this.anchoredSelection().selection(),
                this.value()
            );
        context.pushHistoryToken(previous);
    }
}
