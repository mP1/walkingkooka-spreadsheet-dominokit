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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;

import java.util.Map;

/**
 * This {@link HistoryToken} is used by to paste a formula over a {@link walkingkooka.spreadsheet.reference.SpreadsheetCellRange}.
 */
public final class SpreadsheetCellSaveFormulaHistoryToken extends SpreadsheetCellSaveHistoryToken<String> {

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
    Class<String> valueType() {
        return String.class;
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
    UrlFragment cellSaveUrlFragment() {
        return FORMULA;
    }

    private final static UrlFragment FORMULA = UrlFragment.parse("formula");

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // TODO PATCH cell formula text
    }
}
