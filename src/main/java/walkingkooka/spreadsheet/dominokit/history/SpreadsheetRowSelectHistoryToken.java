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
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

/**
 * Selects the given row or rows.
 * <pre>
 * /123/SpreadsheetName456/row/1
 * /123/SpreadsheetName456/row/2:3/right
 *
 * /spreadsheet-id/spreadsheet-name/row/row or row-range
 * </pre>
 */
public class SpreadsheetRowSelectHistoryToken extends SpreadsheetRowHistoryToken {

    static SpreadsheetRowSelectHistoryToken with(final SpreadsheetId id,
                                                 final SpreadsheetName name,
                                                 final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetRowSelectHistoryToken(
                id,
                name,
                anchoredSelection
        );
    }

    private SpreadsheetRowSelectHistoryToken(final SpreadsheetId id,
                                             final SpreadsheetName name,
                                             final AnchoredSpreadsheetSelection anchoredSelection) {
        super(
                id,
                name,
                anchoredSelection
        );
    }

    @Override
    UrlFragment rowUrlFragment() {
        return SELECT;
    }

    @Override //
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return selection(
                id,
                name,
                anchoredSelection
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // SpreadsheetViewportComponent will give focus to row
    }
}
