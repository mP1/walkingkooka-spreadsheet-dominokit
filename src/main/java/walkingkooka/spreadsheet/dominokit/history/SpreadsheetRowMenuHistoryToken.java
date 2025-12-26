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
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

/**
 * Opens a context menu with items for the selected row or rows.
 * <pre>
 * /123/SpreadsheetName456/row/1/menu
 * /123/SpreadsheetName456/row/2:3/menu
 *
 * /spreadsheet-id/spreadsheet-name/row/row or row-range
 * </pre>
 */
public class SpreadsheetRowMenuHistoryToken extends SpreadsheetRowHistoryToken {

    static SpreadsheetRowMenuHistoryToken with(final SpreadsheetId id,
                                               final SpreadsheetName name,
                                               final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetRowMenuHistoryToken(
            id,
            name,
            anchoredSelection
        );
    }

    private SpreadsheetRowMenuHistoryToken(final SpreadsheetId id,
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
        return MENU;
    }

    @Override //
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return selection(
            id,
            name,
            anchoredSelection
        ).menu();
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // SpreadsheetViewportComponent will open a row menu
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitRowMenu(
            this.id,
            this.name,
            this.anchoredSelection
        );
    }
}
