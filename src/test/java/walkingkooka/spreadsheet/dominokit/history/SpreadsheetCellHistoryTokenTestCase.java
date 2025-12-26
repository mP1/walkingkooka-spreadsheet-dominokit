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
import walkingkooka.spreadsheet.engine.SpreadsheetCellFindQuery;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportAnchor;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.OptionalInt;

public abstract class SpreadsheetCellHistoryTokenTestCase<T extends SpreadsheetCellHistoryToken> extends SpreadsheetAnchoredSelectionHistoryTokenTestCase<T> {

    final static SpreadsheetCellReference CELL = SpreadsheetSelection.A1;

    final static SpreadsheetCellRangeReference CELL_RANGE = SpreadsheetSelection.parseCellRange("B2:C3");

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

    // label............................................................................................................

    @Test
    public final void testCreateLabel() {
        final T historyToken = this.createHistoryToken();

        this.createLabelAndCheck(
            historyToken,
            HistoryToken.cellLabelSelect(
                ID,
                NAME,
                historyToken.anchoredSelection()
            )
        );
    }

    // setLabelMappingReference............................................................................................

    @Test
    public void testSetLabelMappingReferenceWhenNotSpreadsheetCellLabel() {
        final HistoryToken historyToken = this.createHistoryToken();
        if (false == historyToken instanceof SpreadsheetCellLabelHistoryToken) {
            // nop
            this.setLabelMappingReferenceAndCheck(
                historyToken,
                SpreadsheetSelection.parseCell("Z9")
            );
        }
    }

    // labels...........................................................................................................

    @Test
    public final void testSetLabels() {
        final HistoryTokenOffsetAndCount offsetAndCount = HistoryTokenOffsetAndCount.EMPTY.setCount(
            OptionalInt.of(123)
        );

        this.setLabelsAndCheck(
            this.createHistoryToken(),
            offsetAndCount,
            HistoryToken.cellLabels(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                offsetAndCount
            )
        );
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

    // setSelection.....................................................................................................

    @Test
    public final void testSetSelectionWithColumn() {
        final SpreadsheetSelection selection = SpreadsheetSelection.parseColumn("Z");

        this.setSelectionAndCheck(
            this.createHistoryToken(),
            selection,
            HistoryToken.columnSelect(
                ID,
                NAME,
                selection.setDefaultAnchor()
            )
        );
    }

    @Test
    public final void testSetSelectionWithColumnRange() {
        final SpreadsheetSelection selection = SpreadsheetSelection.parseColumnRange("X:Y");

        this.setSelectionAndCheck(
            this.createHistoryToken(),
            selection,
            HistoryToken.columnSelect(
                ID,
                NAME,
                selection.setDefaultAnchor()
            )
        );
    }

    @Test
    public final void testSetSelectionWithSameCell() {
        this.setSelectionAndCheck(
            this.createHistoryToken(
                CELL.setDefaultAnchor()
            ),
            CELL
        );
    }

    @Test
    public final void testSetSelectionWithDifferentCell() {
        final SpreadsheetSelection selection = SpreadsheetSelection.parseCell("Z99");

        this.setSelectionAndCheck(
            this.createHistoryToken(),
            selection,
            HistoryToken.cellSelect(
                ID,
                NAME,
                selection.setDefaultAnchor()
            )
        );
    }

    @Test
    public final void testSetSelectionWithSameCellRange() {
        final SpreadsheetSelection selection = SpreadsheetSelection.parseCellRange("A1:B2");

        this.setSelectionAndCheck(
            this.createHistoryToken(
                selection.setDefaultAnchor()
            ),
            selection
        );
    }

    @Test
    public final void testSetSelectionWithDifferentCellRange() {
        final SpreadsheetSelection selection = SpreadsheetSelection.parseCellRange("Z1:Z2");

        this.setSelectionAndCheck(
            this.createHistoryToken(),
            selection,
            HistoryToken.cellSelect(
                ID,
                NAME,
                selection.setDefaultAnchor()
            )
        );
    }

    @Test
    public final void testSetSelectionWithSameLabel() {
        this.setSelectionAndCheck(
            this.createHistoryToken(
                LABEL.setDefaultAnchor()
            ),
            LABEL
        );
    }

    @Test
    public final void testSetSelectionWithDifferentLabel() {
        final SpreadsheetSelection selection = SpreadsheetSelection.labelName("Different");

        this.setSelectionAndCheck(
            this.createHistoryToken(),
            selection,
            HistoryToken.cellSelect(
                ID,
                NAME,
                selection.setDefaultAnchor()
            )
        );
    }

    @Test
    public final void testSetSelectionWithRow() {
        final SpreadsheetSelection selection = SpreadsheetSelection.parseRow("1");

        this.setSelectionAndCheck(
            this.createHistoryToken(),
            selection,
            HistoryToken.rowSelect(
                ID,
                NAME,
                selection.setDefaultAnchor()
            )
        );
    }

    @Test
    public final void testSetSelectionWithRowRange() {
        final SpreadsheetSelection selection = SpreadsheetSelection.parseRowRange("2:3");

        this.setSelectionAndCheck(
            this.createHistoryToken(),
            selection,
            HistoryToken.rowSelect(
                ID,
                NAME,
                selection.setDefaultAnchor()
            )
        );
    }

    // setValueName.....................................................................................................

    @Test
    public final void testSetValueNameWithSpreadsheetMetadataPropertyName() {
        final TextStylePropertyName<?> stylePropertyName = TextStylePropertyName.COLOR;

        this.setValueNameAndCheck(
            this.createHistoryToken(),
            stylePropertyName,
            HistoryToken.cellStyle(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                stylePropertyName
            )
        );
    }

    @Test
    public final void testSetValueNameWithSpreadsheetMetadataPropertyName2() {
        final TextStylePropertyName<?> stylePropertyName = TextStylePropertyName.TEXT_ALIGN;

        this.setValueNameAndCheck(
            this.createHistoryToken(),
            stylePropertyName,
            HistoryToken.cellStyle(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                stylePropertyName
            )
        );
    }

    // urlFragment......................................................................................................

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
