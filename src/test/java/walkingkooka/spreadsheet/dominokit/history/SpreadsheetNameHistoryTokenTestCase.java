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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
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

    // setMenu1(Selection)..................................................................................................

    @Test
    public void testSetMenuWithNullSelectionFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createHistoryToken().setMenu2(null)
        );
    }

    @Test
    public void testSetMenuWithCellRangeFails() {
        this.setMenuWithSelectionFails(
                SpreadsheetSelection.parseCellRange("A1:B2")
        );
    }

    @Test
    public void testSetMenuWithColumnRangeFails() {
        this.setMenuWithSelectionFails(
                SpreadsheetSelection.parseColumnRange("C:D")
        );
    }

    @Test
    public void testSetMenuWithRowRangeFails() {
        this.setMenuWithSelectionFails(
                SpreadsheetSelection.parseRowRange("5:6")
        );
    }

    private void setMenuWithSelectionFails(final SpreadsheetSelection selection) {
        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> this.createHistoryToken()
                        .setMenu2(selection)
        );

        this.checkEquals(
                "Expected cell, column or row but got " + selection,
                thrown.getMessage(),
                () -> "menu " + selection
        );
    }

    final void setMenuWithCellAndCheck() {
        this.setMenuWithCellAndCheck(
                SpreadsheetSelection.parseCell("Z99")
        );
    }

    final void setMenuWithCellAndCheck(final SpreadsheetCellReference cell) {
        this.setMenuAndCheck(
                HistoryToken.cellMenu(
                        ID,
                        NAME,
                        cell.setDefaultAnchor()
                ),
                cell
        );
    }

    final void setMenuWithColumnAndCheck() {
        this.setMenuWithColumnAndCheck(
                SpreadsheetSelection.parseColumn("C")
        );
    }

    final void setMenuWithColumnAndCheck(final SpreadsheetColumnReference column) {
        this.setMenuAndCheck(
                HistoryToken.columnMenu(
                        ID,
                        NAME,
                        column.setDefaultAnchor()
                ),
                column
        );
    }

    final void setMenuWithRowAndCheck() {
        this.setMenuWithRowAndCheck(
                SpreadsheetSelection.parseRow("4")
        );
    }

    final void setMenuWithRowAndCheck(final SpreadsheetRowReference row) {
        this.setMenuAndCheck(
                HistoryToken.rowMenu(
                        ID,
                        NAME,
                        row.setDefaultAnchor()
                ),
                row
        );
    }

    final void setMenuAndCheck(final SpreadsheetViewportSelectionHistoryToken token,
                               final SpreadsheetSelection selection) {
        this.setMenuAndCheck(
                token,
                selection,
                token
        );
    }

    final void setMenuAndCheck(final SpreadsheetNameHistoryToken before,
                               final SpreadsheetSelection selection,
                               final SpreadsheetViewportSelectionHistoryToken expected) {
        this.checkEquals(
                expected,
                before.setMenu2(selection),
                () -> before + " menu " + selection
        );
    }

    // setMetadataPropertyName..........................................................................................

    @Test
    public void testSetMetadataPropertyName() {
        final SpreadsheetMetadataPropertyName<?> propertyName = SpreadsheetMetadataPropertyName.DATE_FORMAT_PATTERN;

        this.setMetadataPropertyNameAndCheck(
                propertyName,
                HistoryToken.metadataPropertySelect(
                        ID,
                        NAME,
                        propertyName
                )
        );
    }

    @Test
    public void testSetMetadataPropertyName2() {
        final SpreadsheetMetadataPropertyName<?> propertyName = SpreadsheetMetadataPropertyName.LOCALE;

        this.setMetadataPropertyNameAndCheck(
                propertyName,
                HistoryToken.metadataPropertySelect(
                        ID,
                        NAME,
                        propertyName
                )
        );
    }

    // helpers.........................................................................................................

    @Override //
    final T createHistoryToken() {
        return this.createHistoryToken(
                ID,
                NAME
        );
    }

    abstract T createHistoryToken(final SpreadsheetId id,
                                  final SpreadsheetName name);
}
