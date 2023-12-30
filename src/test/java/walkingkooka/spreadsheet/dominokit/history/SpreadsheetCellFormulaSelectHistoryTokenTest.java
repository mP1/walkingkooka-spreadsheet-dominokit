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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;

public final class SpreadsheetCellFormulaSelectHistoryTokenTest extends SpreadsheetCellFormulaHistoryTokenTestCase<SpreadsheetCellFormulaSelectHistoryToken> {

    // setSave.........................................................................................................

    @Test
    public void testSetSave() {
        final AnchoredSpreadsheetSelection selection = CELL.setDefaultAnchor();
        final String formulaText = "=1";
        final HistoryToken historyToken = HistoryToken.formula(ID, NAME, selection);

        this.checkEquals(
                historyToken.setSave(formulaText),
                HistoryToken.formulaSave(
                        ID,
                        NAME,
                        selection,
                        formulaText
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
                RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
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

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck();
    }

    // setSelection.....................................................................................................

    @Test
    public void testSetSelectionDifferentCell() {
        final AnchoredSpreadsheetSelection different = SpreadsheetSelection.parseCell("B2")
                .setDefaultAnchor();

        this.setSelectionAndCheck(
                this.createHistoryToken(),
                different,
                HistoryToken.formula(
                        ID,
                        NAME,
                        different
                )
        );
    }

    // helpers..........................................................................................................

    @Override
    SpreadsheetCellFormulaSelectHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                final SpreadsheetName name,
                                                                final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetCellFormulaSelectHistoryToken.with(
                id,
                name,
                selection
        );
    }

    @Override
    public Class<SpreadsheetCellFormulaSelectHistoryToken> type() {
        return SpreadsheetCellFormulaSelectHistoryToken.class;
    }
}
