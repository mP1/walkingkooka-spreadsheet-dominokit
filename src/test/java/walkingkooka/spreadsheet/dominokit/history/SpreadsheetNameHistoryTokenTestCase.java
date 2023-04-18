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
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class SpreadsheetNameHistoryTokenTestCase<T extends SpreadsheetNameHistoryToken> extends SpreadsheetIdHistoryTokenTestCase<T> {

    SpreadsheetNameHistoryTokenTestCase() {
        super();
    }

    @Test
    public final void testIdName() {
        this.idNameAndCheck(
                ID,
                NAME,
                this.createHistoryToken(ID, NAME)
        );
    }

    @Test
    public final void testIdNameDifferentId() {
        final SpreadsheetId differentId = SpreadsheetId.with(9999);

        this.idNameAndCheck(
                differentId,
                NAME,
                this.createHistoryToken(differentId, NAME)
        );
    }

    @Test
    public final void testIdNameDifferentName() {
        final SpreadsheetName differentName = SpreadsheetName.with("Different");

        this.idNameAndCheck(
                ID,
                differentName,
                this.createHistoryToken(ID, differentName)
        );
    }

    // menu(Selection)..................................................................................................

    @Test
    public void testMenuWithNullSelectionFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createHistoryToken().menu(null)
        );
    }

    @Test
    public void testMenuWithCellRangeFails() {
        this.menuWithSelectionFails(
                SpreadsheetSelection.parseCellRange("A1:B2")
        );
    }

    @Test
    public void testMenuWithColumnRangeFails() {
        this.menuWithSelectionFails(
                SpreadsheetSelection.parseColumnRange("C:D")
        );
    }

    @Test
    public void testMenuWithRowRangeFails() {
        this.menuWithSelectionFails(
                SpreadsheetSelection.parseRowRange("5:6")
        );
    }

    private void menuWithSelectionFails(final SpreadsheetSelection selection) {
        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> this.createHistoryToken()
                        .menu(selection)
        );

        this.checkEquals(
                "Expected cell, column or row but got " + selection,
                thrown.getMessage(),
                () -> "menu " + selection
        );
    }

    final void menuWithCellAndCheck() {
        this.menuWithCellAndCheck(
                SpreadsheetSelection.parseCell("Z99")
        );
    }

    final void menuWithCellAndCheck(final SpreadsheetCellReference cell) {
        this.menuAndCheck(
                HistoryToken.cellMenu(
                        ID,
                        NAME,
                        cell.setDefaultAnchor()
                ),
                cell
        );
    }
    
    final void menuWithColumnAndCheck() {
        this.menuWithColumnAndCheck(
                SpreadsheetSelection.parseColumn("C")
        );
    }

    final void menuWithColumnAndCheck(final SpreadsheetColumnReference column) {
        this.menuAndCheck(
                HistoryToken.columnMenu(
                        ID,
                        NAME,
                        column.setDefaultAnchor()
                ),
                column
        );
    }

    final void menuWithRowAndCheck() {
        this.menuWithRowAndCheck(
                SpreadsheetSelection.parseRow("4")
        );
    }

    final void menuWithRowAndCheck(final SpreadsheetRowReference row) {
        this.menuAndCheck(
                HistoryToken.rowMenu(
                        ID,
                        NAME,
                        row.setDefaultAnchor()
                ),
                row
        );
    }

    final void menuAndCheck(final SpreadsheetViewportSelectionHistoryToken token,
                            final SpreadsheetSelection selection) {
        this.menuAndCheck(
                token,
                selection,
                token
        );
    }

    final void menuAndCheck(final SpreadsheetNameHistoryToken before,
                            final SpreadsheetSelection selection,
                            final SpreadsheetViewportSelectionHistoryToken expected) {
        this.checkEquals(
                expected,
                before.menu(selection),
                () -> before + " menu " + selection
        );
    }

    @Override final T createHistoryToken() {
        return this.createHistoryToken(
                ID,
                NAME
        );
    }

    abstract T createHistoryToken(final SpreadsheetId id,
                                  final SpreadsheetName name);
}
