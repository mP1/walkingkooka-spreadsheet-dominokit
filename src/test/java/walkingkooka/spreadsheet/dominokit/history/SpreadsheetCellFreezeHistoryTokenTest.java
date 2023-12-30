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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyValueException;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellFreezeHistoryTokenTest extends SpreadsheetCellHistoryTokenTestCase<SpreadsheetCellFreezeHistoryToken> {

    @Test
    public void testUrlFragmentColumnRangeInvalidFails() {
        assertThrows(
                SpreadsheetMetadataPropertyValueException.class,
                () -> this.createHistoryToken(
                        SpreadsheetSelection.parseCellRange("B1:C3")
                                .setDefaultAnchor()
                )
        );
    }

    @Test
    public void testUrlFragmentRowRangeInvalidFails() {
        assertThrows(
                SpreadsheetMetadataPropertyValueException.class,
                () -> this.createHistoryToken(
                        SpreadsheetSelection.parseCellRange("A2:C3")
                                .setDefaultAnchor()
                )
        );
    }

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck(
                SpreadsheetSelection.A1.setDefaultAnchor(),
                "/123/SpreadsheetName456/cell/A1/freeze"
        );
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                SpreadsheetSelection.parseCellRange("A1:B2").setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/A1:B2/top-left/freeze"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/123/SpreadsheetName456/cell/Label123/freeze"
        );
    }

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
                this.createHistoryToken(),
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    // setMenu1(Selection)..................................................................................................

    @Test
    public void testSetMenuWithCell() {
        this.setMenuWithCellAndCheck();
    }

    // setSelection.....................................................................................................

    @Test
    public void testSetSelectionDifferentCell() {
        final AnchoredSpreadsheetSelection different = SpreadsheetSelection.A1
                .setDefaultAnchor();

        this.setSelectionAndCheck(
                this.createHistoryToken(),
                different,
                HistoryToken.cellFreeze(
                        ID,
                        NAME,
                        different
                )
        );
    }

    @Test
    public void testSetSelectionDifferentColumn() {
        final AnchoredSpreadsheetSelection different = SpreadsheetSelection.parseColumn("A")
                .setDefaultAnchor();

        this.setSelectionAndCheck(
                this.createHistoryToken(),
                different,
                HistoryToken.columnFreeze(
                        ID,
                        NAME,
                        different
                )
        );
    }

    @Test
    public void testSetSelectionDifferentRow() {
        final AnchoredSpreadsheetSelection different = SpreadsheetSelection.parseRow("1")
                .setDefaultAnchor();

        this.setSelectionAndCheck(
                this.createHistoryToken(),
                different,
                HistoryToken.rowFreeze(
                        ID,
                        NAME,
                        different
                )
        );
    }

    // ClassTesting....................................................................................................

    @Override
    SpreadsheetCellFreezeHistoryToken createHistoryToken(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetCellFreezeHistoryToken.with(
                id,
                name,
                selection
        );
    }

    @Override
    public Class<SpreadsheetCellFreezeHistoryToken> type() {
        return SpreadsheetCellFreezeHistoryToken.class;
    }
}
