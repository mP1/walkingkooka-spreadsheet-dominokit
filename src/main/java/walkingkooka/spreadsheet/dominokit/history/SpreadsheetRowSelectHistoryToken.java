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

public class SpreadsheetRowSelectHistoryToken extends SpreadsheetRowHistoryToken {

    static SpreadsheetRowSelectHistoryToken with(final SpreadsheetId id,
                                                 final SpreadsheetName name,
                                                 final AnchoredSpreadsheetSelection selection) {
        return new SpreadsheetRowSelectHistoryToken(
                id,
                name,
                selection
        );
    }

    private SpreadsheetRowSelectHistoryToken(final SpreadsheetId id,
                                             final SpreadsheetName name,
                                             final AnchoredSpreadsheetSelection selection) {
        super(
                id,
                name,
                selection
        );
    }

    @Override
    UrlFragment rowUrlFragment() {
        return SELECT;
    }

    @Override //
    HistoryToken setDifferentSelection(final AnchoredSpreadsheetSelection selection) {
        return selection(
                this.id(),
                this.name(),
                selection
        );
    }

    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return with(
                id,
                name,
                this.selection()
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // SpreadsheetViewportComponent will give focus to row
    }
}
