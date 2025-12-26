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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class SpreadsheetNameHistoryTokenTestCase<T extends SpreadsheetNameHistoryToken> extends SpreadsheetIdHistoryTokenTestCase<T> {

    SpreadsheetNameHistoryTokenTestCase() {
        super();
    }

    @Test
    public final void testSetAnchoredSelectionDifferentCell() {
        final AnchoredSpreadsheetSelection different = SpreadsheetSelection.parseCell("B2")
            .setDefaultAnchor();

        this.setAnchoredSelectionAndCheck(
            this.createHistoryToken(),
            different,
            HistoryToken.cellSelect(
                ID,
                NAME,
                different
            )
        );
    }

    @Test
    public void testSetAnchoredSelectionDifferentColumn() {
        final AnchoredSpreadsheetSelection different = SpreadsheetSelection.parseColumn("B")
            .setDefaultAnchor();

        this.setAnchoredSelectionAndCheck(
            this.createHistoryToken(),
            different,
            HistoryToken.columnSelect(
                ID,
                NAME,
                different
            )
        );
    }

    @Test
    public void testSetAnchoredSelectionDifferentRow() {
        final AnchoredSpreadsheetSelection different = SpreadsheetSelection.parseRow("2")
            .setDefaultAnchor();

        this.setAnchoredSelectionAndCheck(
            this.createHistoryToken(),
            different,
            HistoryToken.rowSelect(
                ID,
                NAME,
                different
            )
        );
    }

    @Test
    public final void testSetIdName() {
        this.setIdAndNameAndCheck(
            ID,
            NAME,
            this.createHistoryToken(ID, NAME)
        );
    }

    @Test
    public final void testSetIdNameDifferentId() {
        final SpreadsheetId differentId = SpreadsheetId.with(9999);

        this.setIdAndNameAndCheck(
            differentId,
            NAME,
            this.createHistoryToken(differentId, NAME)
        );
    }

    @Test
    public final void testSetIdNameDifferentName() {
        final SpreadsheetName differentName = SpreadsheetName.with("Different");

        this.setIdAndNameAndCheck(
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
            () -> this.createHistoryToken()
                .menu(null)
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
            "Got " + selection + ", expected cell, column or row",
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

    final void menuAndCheck(final SpreadsheetAnchoredSelectionHistoryToken token,
                            final SpreadsheetSelection selection) {
        this.menuAndCheck(
            token,
            selection,
            token
        );
    }

    final void menuAndCheck(final SpreadsheetNameHistoryToken before,
                            final SpreadsheetSelection selection,
                            final SpreadsheetAnchoredSelectionHistoryToken expected) {
        this.checkEquals(
            expected,
            before.menu(selection),
            () -> before + " menu " + selection
        );
    }

    // setName..........................................................................................................

    @Test
    public final void testSetName() {
        this.setNameAndCheck(
            NAME,
            this.createHistoryToken(ID, NAME)
        );
    }

    @Test
    public final void testSetNameDifferentName() {
        final SpreadsheetName differentName = SpreadsheetName.with("Different");

        this.setNameAndCheck(
            differentName,
            this.createHistoryToken(
                ID,
                differentName
            )
        );
    }

    // setValueName.....................................................................................................

    @Test
    public final void testSetValueNameWithTextStylePropertyName() {
        final SpreadsheetMetadataPropertyName<?> metadataPropertyName = SpreadsheetMetadataPropertyName.DEFAULT_YEAR;

        this.setValueNameAndCheck(
            this.createHistoryToken(),
            metadataPropertyName,
            HistoryToken.metadataPropertySelect(
                ID,
                NAME,
                metadataPropertyName
            )
        );
    }

    @Test
    public final void testSetValueNameWithTextStylePropertyName2() {
        final SpreadsheetMetadataPropertyName<?> metadataPropertyName = SpreadsheetMetadataPropertyName.ROUNDING_MODE;

        this.setValueNameAndCheck(
            this.createHistoryToken(),
            metadataPropertyName,
            HistoryToken.metadataPropertySelect(
                ID,
                NAME,
                metadataPropertyName
            )
        );
    }

    // helpers.........................................................................................................

    @Override //
    final T createHistoryToken(final SpreadsheetId id) {
        return this.createHistoryToken(
            id,
            NAME
        );
    }

    abstract T createHistoryToken(final SpreadsheetId id,
                                  final SpreadsheetName name);
}
