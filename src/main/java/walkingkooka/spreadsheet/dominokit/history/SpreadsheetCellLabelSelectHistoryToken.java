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
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

/**
 * A request to edit a label belonging to a cell selection.
 * <pre>
 * /1/SpreadsheetName/cell/A1/label
 * </pre>
 */
public final class SpreadsheetCellLabelSelectHistoryToken extends SpreadsheetCellLabelHistoryToken {

    static SpreadsheetCellLabelSelectHistoryToken with(final SpreadsheetId id,
                                                       final SpreadsheetName name,
                                                       final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellLabelSelectHistoryToken(
            id,
            name,
            anchoredSelection
        );
    }

    private SpreadsheetCellLabelSelectHistoryToken(final SpreadsheetId id,
                                                   final SpreadsheetName name,
                                                   final AnchoredSpreadsheetSelection anchoredSelection) {
        super(id, name, anchoredSelection);
    }

    // /1/SpreadsheetName/cell/A1/label
    @Override
    UrlFragment cellLabelUrlFragment() {
        return UrlFragment.EMPTY;
    }

    @Override //
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return selection(
            id,
            name,
            anchoredSelection
        ).labelMapping();
    }

    // /1/SpreadsheetName/cell/A1
    @Override
    public HistoryToken clearAction() {
        return this;
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // NOP
    }
}
