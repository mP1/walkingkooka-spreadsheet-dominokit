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
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReferenceRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public abstract class SpreadsheetColumnHistoryTokenTestCase<T extends SpreadsheetColumnHistoryToken> extends SpreadsheetViewportHistoryTokenTestCase<T> {

    final static SpreadsheetColumnReference COLUMN = SpreadsheetSelection.parseColumn("A");

    final static SpreadsheetColumnReferenceRange COLUMN_RANGE = SpreadsheetSelection.parseColumnRange("B:C");

    final static SpreadsheetViewport VIEWPORT_SELECTION = COLUMN.setDefaultAnchor();

    SpreadsheetColumnHistoryTokenTestCase() {
        super();
    }

    @Test
    public final void testWithCellFails() {
        this.createHistoryTokenFails(
                SpreadsheetSelection.A1.setDefaultAnchor(),
                "Got A1 expected column or column-range"
        );
    }

    @Test
    public final void testWithCellRangeFails() {
        this.createHistoryTokenFails(
                SpreadsheetSelection.parseCellRange("A1:A2").setDefaultAnchor(),
                "Got A1:A2 expected column or column-range"
        );
    }

    @Test
    public final void testWithLabelFails() {
        this.createHistoryTokenFails(
                SpreadsheetSelection.labelName("Label123").setDefaultAnchor(),
                "Got Label123 expected column or column-range"
        );
    }

    @Test
    public final void testWithRowFails() {
        this.createHistoryTokenFails(
                SpreadsheetSelection.parseRow("1").setDefaultAnchor(),
                "Got 1 expected column or column-range"
        );
    }

    @Test
    public final void testWithRowRangeFails() {
        this.createHistoryTokenFails(
                SpreadsheetSelection.parseRowRange("1:2").setDefaultAnchor(),
                "Got 1:2 expected column or column-range"
        );
    }

    // freezeOrEmpty....................................................................................................

    @Test
    public final void testFreezeOrEmptyColumn() {
        final SpreadsheetViewport viewport = COLUMN.setDefaultAnchor();

        this.freezeOrEmptyAndCheck(
                viewport,
                HistoryToken.columnFreeze(
                        ID,
                        NAME,
                        viewport
                )
        );
    }

    @Test
    public final void testFreezeOrEmptyColumnRange() {
        final SpreadsheetViewport viewport = SpreadsheetSelection.parseColumnRange("A:B")
                .setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT);

        this.freezeOrEmptyAndCheck(
                viewport,
                HistoryToken.columnFreeze(
                        ID,
                        NAME,
                        viewport
                )
        );
    }

    // setViewport.............................................................................................

    @Test
    public final void testSetViewportWithSameColumn() {
        this.setViewportAndCheck(
                COLUMN.setDefaultAnchor()
        );
    }

    @Test
    public final void testSetViewportWithSameColumnRange() {
        this.setViewportAndCheck(
                SpreadsheetSelection.parseColumnRange("A:B")
                        .setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT)
        );
    }

    @Test
    public final void testSetViewportWithSameColumnRange2() {
        this.setViewportAndCheck(
                SpreadsheetSelection.parseColumnRange("A:C")
                        .setAnchor(SpreadsheetViewportSelectionAnchor.LEFT)
        );
    }

    // setMenu1(Selection)..................................................................................................

    @Test
    public final void testSetMenuWithCell() {
        this.setMenuWithCellAndCheck();
    }

    @Test
    public final void testColumnMenuWithSameColumn() {
        final SpreadsheetColumnReference column = COLUMN;

        this.setMenuAndCheck(
                this.createHistoryToken(
                        column.setDefaultAnchor()
                ),
                column,
                HistoryToken.columnMenu(
                        ID,
                        NAME,
                        column.setDefaultAnchor()
                )
        );
    }

    @Test
    public final void testColumnMenuWithDifferentColumn() {
        final SpreadsheetColumnReference column = COLUMN.add(1);

        this.setMenuAndCheck(
                this.createHistoryToken(
                        COLUMN.setDefaultAnchor()
                ),
                column,
                HistoryToken.columnMenu(
                        ID,
                        NAME,
                        column.setDefaultAnchor()
                )
        );
    }


    @Test
    public final void testColumnRangeMenuWithColumnInside() {
        final SpreadsheetViewport viewport = SpreadsheetSelection.parseColumnRange("A:C")
                .setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT);

        this.setMenuAndCheck(
                this.createHistoryToken(viewport),
                SpreadsheetSelection.parseColumn("B"),
                HistoryToken.columnMenu(
                        ID,
                        NAME,
                        viewport
                )
        );
    }

    @Test
    public final void testColumnRangeMenuWithColumnOutside() {
        final SpreadsheetColumnReference column = SpreadsheetSelection.parseColumn("Z");

        this.setMenuAndCheck(
                this.createHistoryToken(
                        SpreadsheetSelection.parseColumnRange("A:C").setDefaultAnchor()
                ),
                column,
                HistoryToken.columnMenu(
                        ID,
                        NAME,
                        column.setDefaultAnchor()
                )
        );
    }

    @Test
    public final void testSetMenuWithRow() {
        this.setMenuWithRowAndCheck();
    }

    final void urlFragmentAndCheck(final SpreadsheetColumnReference reference,
                                   final String expected) {
        this.urlFragmentAndCheck(
                this.createHistoryToken(
                        reference.setDefaultAnchor()
                ),
                expected
        );
    }

    final void urlFragmentAndCheck(final SpreadsheetColumnReferenceRange reference,
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
