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
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetRowRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportAnchor;

import java.util.OptionalInt;

public abstract class SpreadsheetRowHistoryTokenTestCase<T extends SpreadsheetRowHistoryToken> extends SpreadsheetAnchoredSelectionHistoryTokenTestCase<T> {

    final static SpreadsheetRowReference ROW = SpreadsheetSelection.parseRow("1");

    final static SpreadsheetRowRangeReference ROW_RANGE = SpreadsheetSelection.parseRowRange("2:3");

    private final static AnchoredSpreadsheetSelection SELECTION = ROW.setDefaultAnchor();

    SpreadsheetRowHistoryTokenTestCase() {
        super();
    }

    @Test
    public final void testWithCellFails() {
        this.createHistoryTokenFails(
            SpreadsheetSelection.A1.setDefaultAnchor(),
            "Got A1 expected row or row-range"
        );
    }

    @Test
    public final void testWithCellRangeFails() {
        this.createHistoryTokenFails(
            SpreadsheetSelection.parseCellRange("A1:A2").setDefaultAnchor(),
            "Got A1:A2 expected row or row-range"
        );
    }

    @Test
    public final void testWithLabelFails() {
        this.createHistoryTokenFails(
            SpreadsheetSelection.labelName("Label123").setDefaultAnchor(),
            "Got Label123 expected row or row-range"
        );
    }

    @Test
    public final void testWithColumnFails() {
        this.createHistoryTokenFails(
            SpreadsheetSelection.parseColumn("A").setDefaultAnchor(),
            "Got A expected row or row-range"
        );
    }

    @Test
    public final void testWithColumnRangeFails() {
        this.createHistoryTokenFails(
            SpreadsheetSelection.parseColumnRange("A:B").setDefaultAnchor(),
            "Got A:B expected row or row-range"
        );
    }

    // clearAction......................................................................................................

    @Test
    public final void testClearAction() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.rowSelect(
                ID,
                NAME,
                SELECTION
            )
        );
    }

    // freezeOrEmpty....................................................................................................

    @Test
    public final void testFreezeOrEmptyRow() {
        final AnchoredSpreadsheetSelection selection = ROW.setDefaultAnchor();

        this.freezeOrEmptyAndCheck(
            selection,
            HistoryToken.rowFreeze(
                ID,
                NAME,
                selection
            )
        );
    }

    @Test
    public final void testFreezeOrEmptyRowRange() {
        final AnchoredSpreadsheetSelection selection = SpreadsheetSelection.parseRowRange("1:2")
            .setAnchor(SpreadsheetViewportAnchor.BOTTOM);

        this.freezeOrEmptyAndCheck(
            selection,
            HistoryToken.rowFreeze(
                ID,
                NAME,
                selection
            )
        );
    }

    // label............................................................................................................

    @Test
    public final void testCreateLabel() {
        this.createLabelAndCheck(
            this.createHistoryToken(),
            HistoryToken.labelMappingCreate(
                ID,
                NAME
            )
        );
    }

    // setLabelMappingReference............................................................................................

    @Test
    public final void testSetLabelMappingReference() {
        this.setLabelMappingReferenceAndCheck();
    }

    // label............................................................................................................

    @Test
    public final void testLabelName() {
        this.labelNameAndCheck(
            this.createHistoryToken()
        );
    }

    // setLabel.........................................................................................................

    @Test
    public final void testSetLabelName() {
        this.setLabelNameAndCheck(
            this.createHistoryToken(),
            LABEL,
            HistoryToken.labelMappingSelect(
                ID,
                NAME,
                LABEL
            )
        );
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
            HistoryToken.labelMappingList(
                ID,
                NAME,
                offsetAndCount
            )
        );
    }

    // menu.............................................................................................................

    @Test
    public final void testMenuWithCell() {
        this.menuWithCellAndCheck();
    }

    @Test
    public final void testMenuWithColumn() {
        this.menuWithColumnAndCheck();
    }

    @Test
    public final void testMenuWithRow() {
        this.menuWithRowAndCheck();
    }

    @Test
    public final void testMenuWithSameRow() {
        final SpreadsheetRowReference row = ROW;

        this.menuAndCheck(
            this.createHistoryToken(
                row.setDefaultAnchor()
            ),
            row,
            HistoryToken.rowMenu(
                ID,
                NAME,
                row.setDefaultAnchor()
            )
        );
    }

    @Test
    public final void testMenuWithDifferentRow() {
        final SpreadsheetRowReference row = ROW.add(1);

        this.menuAndCheck(
            this.createHistoryToken(
                ROW.setDefaultAnchor()
            ),
            row,
            HistoryToken.rowMenu(
                ID,
                NAME,
                row.setDefaultAnchor()
            )
        );
    }

    @Test
    public final void testMenuRowRangeMenuWithRowInside() {
        final SpreadsheetRowRangeReference range = SpreadsheetSelection.parseRowRange("1:3");

        this.menuAndCheck(
            this.createHistoryToken(
                range.setDefaultAnchor()
            ),
            SpreadsheetSelection.parseRow("2"),
            HistoryToken.rowMenu(
                ID,
                NAME,
                range.setDefaultAnchor()
            )
        );
    }

    @Test
    public final void testMenuRowRangeMenuWithRowOutside() {
        final SpreadsheetRowReference row = SpreadsheetSelection.parseRow("99");

        this.menuAndCheck(
            this.createHistoryToken(
                SpreadsheetSelection.parseRowRange("1:3").setDefaultAnchor()
            ),
            row,
            HistoryToken.rowMenu(
                ID,
                NAME,
                row.setDefaultAnchor()
            )
        );
    }

    // setSelection.....................................................................................................

    @Test
    public final void testSetSelectionWithColumn() {
        final SpreadsheetSelection selection = SpreadsheetSelection.parseColumn("A");

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
        final SpreadsheetSelection selection = SpreadsheetSelection.parseColumnRange("B:C");

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
    public final void testSetSelectionWithCell() {
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
    public final void testSetSelectionWithCellRange() {
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
    public final void testSetSelectionWithLabel() {
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
    public final void testSetSelectionWithSameRow() {
        this.setSelectionAndCheck(
            this.createHistoryToken(
                ROW.setDefaultAnchor()
            ),
            ROW
        );
    }

    @Test
    public final void testSetSelectionWithDifferentRow() {
        final SpreadsheetSelection different = SpreadsheetSelection.parseRow("123");

        this.setSelectionAndCheck(
            this.createHistoryToken(
                ROW.setDefaultAnchor()
            ),
            different,
            HistoryToken.rowSelect(
                ID,
                NAME,
                different.setDefaultAnchor()
            )
        );
    }

    @Test
    public final void testSetSelectionWithSameRowRange() {
        final SpreadsheetSelection range = SpreadsheetSelection.parseRowRange("1:2");

        this.setSelectionAndCheck(
            this.createHistoryToken(
                range.setDefaultAnchor()
            ),
            range
        );
    }

    @Test
    public final void testSetSelectionWithDifferentRowRange() {
        final SpreadsheetSelection different = SpreadsheetSelection.parseRowRange("2:3");

        this.setSelectionAndCheck(
            this.createHistoryToken(
                SpreadsheetSelection.parseRowRange("1:2")
                    .setDefaultAnchor()
            ),
            different,
            HistoryToken.rowSelect(
                ID,
                NAME,
                different.setDefaultAnchor()
            )
        );
    }


    @Test
    public final void testSetSelectionWithDifferentRowRangeNonDefaultAnchor() {
        final SpreadsheetSelection different = SpreadsheetSelection.parseRowRange("1:3");
        final SpreadsheetViewportAnchor anchor = different.defaultAnchor()
            .opposite();

        this.setSelectionAndCheck(
            this.createHistoryToken(
                SpreadsheetSelection.parseRowRange("1:2")
                    .setAnchor(anchor)
            ),
            different,
            HistoryToken.rowSelect(
                ID,
                NAME,
                different.setAnchor(anchor)
            )
        );
    }

    // urlFragment......................................................................................................

    final void urlFragmentAndCheck(final SpreadsheetRowReference reference,
                                   final String expected) {
        this.urlFragmentAndCheck(
            this.createHistoryToken(
                reference.setDefaultAnchor()
            ),
            expected
        );
    }

    final void urlFragmentAndCheck(final SpreadsheetRowRangeReference reference,
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
            SELECTION
        );
    }
}
