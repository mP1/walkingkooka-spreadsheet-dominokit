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

public final class SpreadsheetCellHighlightSelectHistoryTokenTest extends SpreadsheetCellHighlightHistoryTokenTestCase<SpreadsheetCellHighlightSelectHistoryToken> {

    // setSave.........................................................................................................

    @Test
    public void testSetSaveDisable() {
        final AnchoredSpreadsheetSelection selection = CELL.setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.cellHighlightSelect(ID, NAME, selection);

        this.checkEquals(
                historyToken.setSave("disable"),
                HistoryToken.cellHighlightSave(
                        ID,
                        NAME,
                        selection,
                        false
                )
        );
    }

    @Test
    public void testSetSaveEnable() {
        final AnchoredSpreadsheetSelection selection = CELL.setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.cellHighlightSelect(ID, NAME, selection);

        this.checkEquals(
                historyToken.setSave("enable"),
                HistoryToken.cellHighlightSave(
                        ID,
                        NAME,
                        selection,
                        true
                )
        );
    }

    // urlFragment.....................................................................................................

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1/highlight");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/B2:C3/top-left/highlight"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/123/SpreadsheetName456/cell/Label123/highlight"
        );
    }

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck();
    }

    // setAnchoredSelection.............................................................................................
    
    @Test
    public void testSetAnchoredSelectionDifferentCell() {
        final AnchoredSpreadsheetSelection different = SpreadsheetSelection.parseCell("B2")
                .setDefaultAnchor();

        this.setAnchoredSelectionAndCheck(
                this.createHistoryToken(),
                different,
                HistoryToken.cellHighlightSelect(
                        ID,
                        NAME,
                        different
                )
        );
    }

    // helpers..........................................................................................................

    @Override
    SpreadsheetCellHighlightSelectHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                  final SpreadsheetName name,
                                                                  final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetCellHighlightSelectHistoryToken.with(
                id,
                name,
                selection
        );
    }

    @Override
    public Class<SpreadsheetCellHighlightSelectHistoryToken> type() {
        return SpreadsheetCellHighlightSelectHistoryToken.class;
    }
}
