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
import walkingkooka.spreadsheet.reference.SpreadsheetColumnRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;

import java.util.Optional;

public abstract class SpreadsheetColumnHistoryTokenTestCase<T extends SpreadsheetColumnHistoryToken> extends SpreadsheetAnchoredSelectionHistoryTokenTestCase<T> {

    final static SpreadsheetColumnReference COLUMN = SpreadsheetSelection.parseColumn("A");

    final static SpreadsheetColumnRangeReference COLUMN_RANGE = SpreadsheetSelection.parseColumnRange("B:C");

    final static AnchoredSpreadsheetSelection SELECTION = COLUMN.setDefaultAnchor();

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

    // clearAction......................................................................................................

    @Test
    public final void testClearAction() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.columnSelect(
                ID,
                NAME,
                SELECTION
            )
        );
    }

    // freezeOrEmpty....................................................................................................

    @Test
    public final void testFreezeOrEmptyColumn() {
        final AnchoredSpreadsheetSelection selection = COLUMN.setDefaultAnchor();

        this.freezeOrEmptyAndCheck(
            selection,
            HistoryToken.columnFreeze(
                ID,
                NAME,
                selection
            )
        );
    }

    @Test
    public final void testFreezeOrEmptyColumnRange() {
        final AnchoredSpreadsheetSelection selection = SpreadsheetSelection.parseColumnRange("A:B")
            .setAnchor(SpreadsheetViewportAnchor.RIGHT);

        this.freezeOrEmptyAndCheck(
            selection,
            HistoryToken.columnFreeze(
                ID,
                NAME,
                selection
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
                Optional.of(LABEL)
            )
        );
    }

    // menu(Selection)..................................................................................................

    @Test
    public final void testMenuWithColumn() {
        this.menuWithColumnAndCheck();
    }

    @Test
    public final void testMenuColumnWithSameColumn() {
        final SpreadsheetColumnReference column = COLUMN;

        this.menuAndCheck(
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
    public final void testMenuColumnWithDifferentColumn() {
        final SpreadsheetColumnReference column = COLUMN.add(1);

        this.menuAndCheck(
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
    public final void testMenuColumnRangeWithColumnInside() {
        final AnchoredSpreadsheetSelection selection = SpreadsheetSelection.parseColumnRange("A:C")
            .setAnchor(SpreadsheetViewportAnchor.RIGHT);

        this.menuAndCheck(
            this.createHistoryToken(selection),
            SpreadsheetSelection.parseColumn("B"),
            HistoryToken.columnMenu(
                ID,
                NAME,
                selection
            )
        );
    }

    @Test
    public final void testMenuColumnRangeWithColumnOutside() {
        final SpreadsheetColumnReference column = SpreadsheetSelection.parseColumn("Z");

        this.menuAndCheck(
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
    public final void testMenuWithRow() {
        this.menuWithRowAndCheck();
    }

    // patternKind......................................................................................................

    @Test
    public final void testPatternKind() {
        this.patternKindAndCheck(
            this.createHistoryToken()
        );
    }

    // helpers..............................................................................................

    final void urlFragmentAndCheck(final SpreadsheetColumnReference reference,
                                   final String expected) {
        this.urlFragmentAndCheck(
            this.createHistoryToken(
                reference.setDefaultAnchor()
            ),
            expected
        );
    }

    final void urlFragmentAndCheck(final SpreadsheetColumnRangeReference reference,
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
