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
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public final class SpreadsheetCellMenuHistoryTokenTest extends SpreadsheetCellHistoryTokenTestCase<SpreadsheetCellMenuHistoryToken> {

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1/menu");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/B2:C3/top-left/menu"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/123/SpreadsheetName456/cell/Label123/menu"
        );
    }

    // menu with selection..............................................................................................

    @Test
    public void testCellMenuWithSameCell() {
        final SpreadsheetCellReference cell = SpreadsheetSelection.parseCell("A1");

        this.menuAndCheck(
                this.createHistoryToken(
                        cell.setDefaultAnchor()
                ),
                cell,
                SpreadsheetHistoryToken.cellMenu(
                        ID,
                        NAME,
                        cell.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testCellMenuWithDifferentCell() {
        final SpreadsheetCellReference cell = SpreadsheetSelection.parseCell("B2");

        this.menuAndCheck(
                this.createHistoryToken(
                        SpreadsheetSelection.parseCell("A1").setDefaultAnchor()
                ),
                cell,
                SpreadsheetHistoryToken.cellMenu(
                        ID,
                        NAME,
                        cell.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testCellRangeMenuWithCellInside() {
        this.menuAndCheck(
                this.createHistoryToken(
                        SpreadsheetSelection.parseCellRange("A1:C3").setDefaultAnchor()
                ),
                SpreadsheetSelection.parseCell("B2")
        );
    }

    @Test
    public void testCellRangeMenuWithCellOutside() {
        final SpreadsheetCellReference cell = SpreadsheetSelection.parseCell("Z99");

        this.menuAndCheck(
                this.createHistoryToken(
                        SpreadsheetSelection.parseCellRange("B2:C4").setDefaultAnchor()
                ),
                cell,
                SpreadsheetHistoryToken.cellMenu(
                        ID,
                        NAME,
                        cell.setDefaultAnchor()
                )
        );
    }

    @Override
    SpreadsheetCellMenuHistoryToken createHistoryToken(final SpreadsheetId id,
                                                       final SpreadsheetName name,
                                                       final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellMenuHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    @Override
    public Class<SpreadsheetCellMenuHistoryToken> type() {
        return SpreadsheetCellMenuHistoryToken.class;
    }
}
