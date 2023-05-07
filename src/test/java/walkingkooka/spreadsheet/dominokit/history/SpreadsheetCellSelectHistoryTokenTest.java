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
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public final class SpreadsheetCellSelectHistoryTokenTest extends SpreadsheetCellHistoryTokenTestCase<SpreadsheetCellSelectHistoryToken> {

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/B2:C3/top-left"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/123/SpreadsheetName456/cell/Label123"
        );
    }

    // freezeOrEmpty....................................................................................................

    @Test
    public void testFreezeOrEmptyCellInvalid() {
        this.freezeOrEmptyAndCheck(
                SpreadsheetSelection.parseCell("B2")
                        .setDefaultAnchor()
        );
    }

    @Test
    public void testFreezeOrEmptyCellRangeInvalid() {
        this.freezeOrEmptyAndCheck(
                SpreadsheetSelection.parseCellRange("C3:D4")
                        .setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM_RIGHT)
        );
    }

    // setMenu1(Selection)..................................................................................................

    @Test
    public void testMenuWithCell() {
        this.menuWithCellAndCheck();
    }

    // unfreezeOrEmpty..................................................................................................

    @Test
    public void testUnfreezeOrEmptyCellInvalid() {
        this.unfreezeOrEmptyAndCheck(
                SpreadsheetSelection.parseCell("B2")
                        .setDefaultAnchor()
        );
    }

    @Test
    public void testUnfreezeOrEmptyCellRangeInvalid() {
        this.unfreezeOrEmptyAndCheck(
                SpreadsheetSelection.parseCellRange("C3:D4")
                        .setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM_RIGHT)
        );
    }

    @Override
    SpreadsheetCellSelectHistoryToken createHistoryToken(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellSelectHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    @Override
    public Class<SpreadsheetCellSelectHistoryToken> type() {
        return SpreadsheetCellSelectHistoryToken.class;
    }
}
