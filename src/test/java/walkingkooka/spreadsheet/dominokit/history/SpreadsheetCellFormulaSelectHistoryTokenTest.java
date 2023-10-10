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

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public final class SpreadsheetCellFormulaSelectHistoryTokenTest extends SpreadsheetCellFormulaHistoryTokenTestCase<SpreadsheetCellFormulaSelectHistoryToken> {

    // setSave.........................................................................................................

    @Test
    public void testSetSave() {
        final SpreadsheetViewport viewport = CELL.setDefaultAnchor();
        final String formulaText = "=1";
        final HistoryToken historyToken = HistoryToken.formula(ID, NAME, viewport);

        this.checkEquals(
                historyToken.setSave(formulaText),
                HistoryToken.formulaSave(
                        ID,
                        NAME,
                        viewport,
                        SpreadsheetFormula.EMPTY.setText(formulaText)
                )
        );
    }

    // urlFragment.....................................................................................................

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1/formula");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/B2:C3/top-left/formula"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/123/SpreadsheetName456/cell/Label123/formula"
        );
    }

    @Override
    SpreadsheetCellFormulaSelectHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                final SpreadsheetName name,
                                                                final SpreadsheetViewport viewport) {
        return SpreadsheetCellFormulaSelectHistoryToken.with(
                id,
                name,
                viewport
        );
    }

    @Override
    public Class<SpreadsheetCellFormulaSelectHistoryToken> type() {
        return SpreadsheetCellFormulaSelectHistoryToken.class;
    }
}
