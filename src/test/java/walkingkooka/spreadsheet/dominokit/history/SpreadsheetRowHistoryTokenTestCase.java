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
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReferenceRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

import static org.junit.jupiter.api.Assertions.assertSame;

public abstract class SpreadsheetRowHistoryTokenTestCase<T extends SpreadsheetRowHistoryToken> extends SpreadsheetViewportSelectionHistoryTokenTestCase<T> {

    final static SpreadsheetRowReference ROW = SpreadsheetSelection.parseRow("1");

    final static SpreadsheetRowReferenceRange ROW_RANGE = SpreadsheetSelection.parseRowRange("2:3");

    private final static SpreadsheetViewportSelection VIEWPORT_SELECTION = ROW.setDefaultAnchor();

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

    // freezeOrEmpty....................................................................................................

    @Test
    public final void testFreezeOrEmptyRow() {
        final SpreadsheetViewportSelection viewportSelection = ROW.setDefaultAnchor();

        this.freezeOrEmptyAndCheck(
                viewportSelection,
                HistoryToken.rowFreeze(
                        ID,
                        NAME,
                        viewportSelection
                )
        );
    }

    @Test
    public final void testFreezeOrEmptyRowRange() {
        final SpreadsheetViewportSelection viewportSelection = SpreadsheetSelection.parseRowRange("1:2")
                .setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM);

        this.freezeOrEmptyAndCheck(
                viewportSelection,
                HistoryToken.rowFreeze(
                        ID,
                        NAME,
                        viewportSelection
                )
        );
    }

    // setViewportSelection.............................................................................................

    @Test
    public final void testSetViewportSelectionWithSameColumn() {
        this.setViewportSelectionAndCheck(
                ROW.setDefaultAnchor()
        );
    }

    @Test
    public final void testSetViewportSelectionWithSameColumnRange() {
        this.setViewportSelectionAndCheck(
                SpreadsheetSelection.parseRowRange("1:2")
                        .setAnchor(SpreadsheetViewportSelectionAnchor.TOP)
        );
    }

    @Test
    public final void testSetViewportSelectionWithSameColumnRange2() {
        this.setViewportSelectionAndCheck(
                SpreadsheetSelection.parseRowRange("1:3")
                        .setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM)
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
        final SpreadsheetRowReferenceRange range = SpreadsheetSelection.parseRowRange("1:3");

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

    // setSave..........................................................................................................

    @Test
    public final void testSetSave() {
        final T historyToken = this.createHistoryToken();

        assertSame(
                historyToken.setSave("=1"),
                historyToken
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

    final void urlFragmentAndCheck(final SpreadsheetRowReferenceRange reference,
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
                VIEWPORT_SELECTION
        );
    }
}
