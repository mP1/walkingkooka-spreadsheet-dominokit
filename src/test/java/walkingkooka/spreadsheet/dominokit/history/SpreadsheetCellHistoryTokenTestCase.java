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
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public abstract class SpreadsheetCellHistoryTokenTestCase<T extends SpreadsheetCellHistoryToken> extends SpreadsheetViewportHistoryTokenTestCase<T> {

    final static SpreadsheetCellReference CELL = SpreadsheetSelection.A1;

    final static SpreadsheetCellRange RANGE = SpreadsheetSelection.parseCellRange("B2:C3");

    final static SpreadsheetLabelName LABEL = SpreadsheetSelection.labelName("Label123");

    final static SpreadsheetViewport VIEWPORT_SELECTION = CELL.setDefaultAnchor();

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
        final SpreadsheetViewport viewport = CELL.setDefaultAnchor();

        this.freezeOrEmptyAndCheck(
                viewport,
                HistoryToken.cellFreeze(
                        ID,
                        NAME,
                        viewport
                )
        );
    }

    @Test
    public final void testFreezeOrEmptyCellRange() {
        final SpreadsheetViewport viewport = SpreadsheetSelection.parseCellRange("A1:B2")
                .setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM_RIGHT);

        this.freezeOrEmptyAndCheck(
                viewport,
                HistoryToken.cellFreeze(
                        ID,
                        NAME,
                        viewport
                )
        );
    }

    // setFormula.......................................................................................................

    @Test
    public final void testSetFormula() {
        final T token = this.createHistoryToken();

        this.checkEquals(
                HistoryToken.formula(
                        ID,
                        NAME,
                        VIEWPORT_SELECTION
                ),
                token.setFormula()
        );
    }

    // setViewport.............................................................................................

    @Test
    public final void testSetViewportWithSameCell() {
        this.setViewportAndCheck(
                CELL.setDefaultAnchor()
        );
    }

    @Test
    public final void testSetViewportWithSameCellRange() {
        this.setViewportAndCheck(
                SpreadsheetSelection.parseCellRange("A1:B2")
                        .setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM_RIGHT)
        );
    }

    @Test
    public final void testSetViewportWithSameCellRange2() {
        this.setViewportAndCheck(
                SpreadsheetSelection.parseCellRange("A1:C3")
                        .setAnchor(SpreadsheetViewportSelectionAnchor.TOP_LEFT)
        );
    }

    @Test
    public final void testSetViewportWithSameLabel() {
        this.setViewportAndCheck(
                LABEL.setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM_RIGHT)
        );
    }

    // setMenu1 with selection..............................................................................................

    @Test
    public final void testSetMenuCellMenuWithSameCell() {
        final SpreadsheetCellReference cell = SpreadsheetSelection.A1;

        this.setMenuAndCheck(
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
    public final void testSetMenuCellMenuWithDifferentCell() {
        final SpreadsheetCellReference cell = SpreadsheetSelection.parseCell("B2");

        this.setMenuAndCheck(
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
    public final void testSetMenuCellRangeMenuWithCellInside() {
        final SpreadsheetViewport viewport = SpreadsheetSelection.parseCellRange("A1:C3")
                .setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM_RIGHT);

        this.setMenuAndCheck(
                this.createHistoryToken(viewport),
                SpreadsheetSelection.parseCell("B2"),
                HistoryToken.cellMenu(
                        ID,
                        NAME,
                        viewport
                )
        );
    }

    @Test
    public final void testSetMenuCellRangeMenuWithCellOutside() {
        final SpreadsheetCellReference cell = SpreadsheetSelection.parseCell("Z99");

        this.setMenuAndCheck(
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
    public final void testSetMenuWithColumn() {
        this.setMenuWithColumnAndCheck();
    }

    @Test
    public final void testSetMenuWithRow() {
        this.setMenuWithRowAndCheck();
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

    final T createHistoryToken(final SpreadsheetId id,
                               final SpreadsheetName name) {
        return this.createHistoryToken(
                id,
                name,
                CELL.setDefaultAnchor()
        );
    }
}
