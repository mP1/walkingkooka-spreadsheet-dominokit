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
import walkingkooka.spreadsheet.reference.SpreadsheetRowRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;

import static org.junit.jupiter.api.Assertions.assertSame;

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
                HistoryToken.row(
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

    // setMenu1(Selection)..................................................................................................

    @Test
    public final void testSetMenuWithCell() {
        this.setMenuWithCellAndCheck();
    }

    @Test
    public final void testSetMenuWithColumn() {
        this.setMenuWithColumnAndCheck();
    }

    @Test
    public final void testRowMenuWithSameRow() {
        final SpreadsheetRowReference row = ROW;

        this.setMenuAndCheck(
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
    public final void testRowMenuWithDifferentRow() {
        final SpreadsheetRowReference row = ROW.add(1);

        this.setMenuAndCheck(
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
    public final void testRowRangeMenuWithRowInside() {
        final SpreadsheetRowRangeReference range = SpreadsheetSelection.parseRowRange("1:3");

        this.setMenuAndCheck(
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
    public final void testRowRangeMenuWithRowOutside() {
        final SpreadsheetRowReference row = SpreadsheetSelection.parseRow("99");

        this.setMenuAndCheck(
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

    // patternKind......................................................................................................

    @Test
    public void testPatternKind() {
        this.patternKindAndCheck(
                this.createHistoryToken()
        );
    }

    // setSave..........................................................................................................

    @Test
    public final void testSetSave() {
        final T historyToken = this.createHistoryToken();

        assertSame(
                historyToken.setSave("=1"),
                historyToken
        );
    }

    // setAnchoredSelection.............................................................................................
    @Test
    public final void testSetAnchoredSelectionWithSameColumn() {
        this.setAnchoredSelectionAndCheck(
                ROW.setDefaultAnchor()
        );
    }

    @Test
    public final void testSetAnchoredSelectionWithSameColumnRange() {
        this.setAnchoredSelectionAndCheck(
                SpreadsheetSelection.parseRowRange("1:2")
                        .setAnchor(SpreadsheetViewportAnchor.TOP)
        );
    }

    @Test
    public final void testSetAnchoredSelectionWithSameColumnRange2() {
        this.setAnchoredSelectionAndCheck(
                SpreadsheetSelection.parseRowRange("1:3")
                        .setAnchor(SpreadsheetViewportAnchor.BOTTOM)
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

    final T createHistoryToken(final SpreadsheetId id,
                               final SpreadsheetName name) {
        return this.createHistoryToken(
                id,
                name,
                SELECTION
        );
    }
}
