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
import walkingkooka.spreadsheet.engine.SpreadsheetCellFindQuery;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;

public abstract class SpreadsheetCellHistoryTokenTestCase<T extends SpreadsheetCellHistoryToken> extends SpreadsheetAnchoredSelectionHistoryTokenTestCase<T> {

    final static SpreadsheetCellReference CELL = SpreadsheetSelection.A1;

    final static SpreadsheetCellRangeReference RANGE = SpreadsheetSelection.parseCellRange("B2:C3");

    final static AnchoredSpreadsheetSelection SELECTION = CELL.setDefaultAnchor();

    SpreadsheetCellHistoryTokenTestCase() {
        super();
    }

    @Test
    public final void testWithColumnFails() {
        this.createHistoryTokenFails(
            SpreadsheetSelection.parseColumn("A").setDefaultAnchor(),
            "Got A expected cell, cell-range or label"
        );
    }

    @Test
    public final void testWithColumnRangeFails() {
        this.createHistoryTokenFails(
            SpreadsheetSelection.parseColumnRange("B:C").setDefaultAnchor(),
            "Got B:C expected cell, cell-range or label"
        );
    }

    @Test
    public final void testWithRowFails() {
        this.createHistoryTokenFails(
            SpreadsheetSelection.parseRow("1").setDefaultAnchor(),
            "Got 1 expected cell, cell-range or label"
        );
    }

    @Test
    public final void testWithRowRangeFails() {
        this.createHistoryTokenFails(
            SpreadsheetSelection.parseRowRange("1:2").setDefaultAnchor(),
            "Got 1:2 expected cell, cell-range or label"
        );
    }

    // freezeOrEmpty....................................................................................................

    @Test
    public final void testFreezeOrEmptyCell() {
        final AnchoredSpreadsheetSelection selection = CELL.setDefaultAnchor();

        this.freezeOrEmptyAndCheck(
            selection,
            HistoryToken.cellFreeze(
                ID,
                NAME,
                selection
            )
        );
    }

    @Test
    public final void testFreezeOrEmptyCellRange() {
        final AnchoredSpreadsheetSelection selection = SpreadsheetSelection.parseCellRange("A1:B2")
            .setAnchor(SpreadsheetViewportAnchor.BOTTOM_RIGHT);

        this.freezeOrEmptyAndCheck(
            selection,
            HistoryToken.cellFreeze(
                ID,
                NAME,
                selection
            )
        );
    }

    // setFind..........................................................................................................

    @Test
    public final void testSetFind() {
        this.setFindAndCheck(
            this.createHistoryToken(),
            SpreadsheetCellFindQuery.empty(), // query
            HistoryToken.cellFind(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                SpreadsheetCellFindQuery.empty()
            )
        );
    }

    // formula..........................................................................................................

    @Test
    public final void testFormula() {
        final T token = this.createHistoryToken();

        this.checkEquals(
            HistoryToken.cellFormula(
                ID,
                NAME,
                SELECTION
            ),
            token.formula()
        );
    }

    // setLabelMappingReference............................................................................................

    @Test
    public void testSetLabelMappingReferenceWhenNotSpreadsheetCellLabel() {
        final HistoryToken historyToken = this.createHistoryToken();
        if(false == historyToken instanceof SpreadsheetCellLabelHistoryToken) {
            // nop
            this.setLabelMappingReferenceAndCheck(
                historyToken,
                SpreadsheetSelection.parseCell("Z9")
            );
        }
    }

    // menu with selection..............................................................................................

    @Test
    public final void testMenuCellMenuWithSameCell() {
        final SpreadsheetCellReference cell = SpreadsheetSelection.A1;

        this.menuAndCheck(
            this.createHistoryToken(
                cell.setDefaultAnchor()
            ),
            cell,
            HistoryToken.cellMenu(
                ID,
                NAME,
                cell.setDefaultAnchor()
            )
        );
    }

    @Test
    public final void testMenuCellMenuWithDifferentCell() {
        final SpreadsheetCellReference cell = SpreadsheetSelection.parseCell("B2");

        this.menuAndCheck(
            this.createHistoryToken(
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            cell,
            HistoryToken.cellMenu(
                ID,
                NAME,
                cell.setDefaultAnchor()
            )
        );
    }

    @Test
    public final void testMenuCellRangeMenuWithCellInside() {
        final AnchoredSpreadsheetSelection selection = SpreadsheetSelection.parseCellRange("A1:C3")
            .setAnchor(SpreadsheetViewportAnchor.BOTTOM_RIGHT);

        this.menuAndCheck(
            this.createHistoryToken(selection),
            SpreadsheetSelection.parseCell("B2"),
            HistoryToken.cellMenu(
                ID,
                NAME,
                selection
            )
        );
    }

    @Test
    public final void testMenuCellRangeMenuWithCellOutside() {
        final SpreadsheetCellReference cell = SpreadsheetSelection.parseCell("Z99");

        this.menuAndCheck(
            this.createHistoryToken(
                SpreadsheetSelection.parseCellRange("A1:B2").setDefaultAnchor()
            ),
            cell,
            HistoryToken.cellMenu(
                ID,
                NAME,
                cell.setDefaultAnchor()
            )
        );
    }

    @Test
    public final void testMenuWithColumn() {
        this.menuWithColumnAndCheck();
    }

    @Test
    public final void testMenuWithRow() {
        this.menuWithRowAndCheck();
    }

    final void urlFragmentAndCheck(final SpreadsheetExpressionReference reference,
                                   final String expected) {
        this.urlFragmentAndCheck(
            this.createHistoryToken(
                reference.setDefaultAnchor()
            ),
            expected
        );
    }

    @Override final T createHistoryToken(final SpreadsheetId id,
                                         final SpreadsheetName name) {
        return this.createHistoryToken(
            id,
            name,
            CELL.setDefaultAnchor()
        );
    }
}
