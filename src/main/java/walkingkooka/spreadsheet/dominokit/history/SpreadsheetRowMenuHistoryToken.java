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
    HistoryToken setDifferentAnchoredSelection(final AnchoredSpreadsheetSelection anchoredSelection) {
        return selection(
                this.id(),
                this.name(),
                anchoredSelection
        ).setMenu1();
    }

    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return with(
                id,
                name,
                this.anchoredSelection()
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // SpreadsheetViewportComponent will open a row menu
    }
}
