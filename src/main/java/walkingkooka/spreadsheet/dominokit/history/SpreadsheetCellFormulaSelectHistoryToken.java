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
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

public final class SpreadsheetCellFormulaSelectHistoryToken extends SpreadsheetCellFormulaHistoryToken {

    static SpreadsheetCellFormulaSelectHistoryToken with(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final SpreadsheetViewportSelection viewportSelection) {
        return new SpreadsheetCellFormulaSelectHistoryToken(
                id,
                name,
                viewportSelection
        );
    }

    private SpreadsheetCellFormulaSelectHistoryToken(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final SpreadsheetViewportSelection viewportSelection) {
        super(
                id,
                name,
                viewportSelection
        );
    }

    @Override
    UrlFragment formulaUrlFragment() {
        return SELECT;
    }

    @Override
    SpreadsheetNameHistoryToken save(final String formulaText) {
        return formulaSave(
                this.id(),
                this.name(),
                this.viewportSelection(),
                SpreadsheetFormula.EMPTY.setText(formulaText)
        );
    }

    @Override
    SpreadsheetNameHistoryToken formulaHistoryToken() {
        return this;
    }

    @Override
    public void onHashChange(final HistoryToken previous,
                             final AppContext context) {
        // enable formula text box and give focus
    }
}
