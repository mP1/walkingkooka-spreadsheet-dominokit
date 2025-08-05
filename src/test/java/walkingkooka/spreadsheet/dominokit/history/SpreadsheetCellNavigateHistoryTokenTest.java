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
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportNavigationList;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellNavigateHistoryTokenTest extends SpreadsheetCellHistoryTokenTestCase<SpreadsheetCellNavigateHistoryToken> {

    private final static SpreadsheetViewportNavigationList NAVIGATIONS = SpreadsheetViewportNavigationList.parse("right 100px");

    @Test
    public void testWithNullNavigationFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetCellNavigateHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                null
            )
        );
    }

    // urlFragment......................................................................................................

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1/navigate/right 100px");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
            CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
            "/123/SpreadsheetName456/cell/B2:C3/top-left/navigate/right 100px"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
            LABEL,
            "/123/SpreadsheetName456/cell/Label123/navigate/right 100px"
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseInvalidSpreadsheetViewportNavigation() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/navigate/!invalid",
            HistoryToken.cellSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.cellSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    // delete...........................................................................................................

    @Test
    public void testDelete() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.cellSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    @Override
    SpreadsheetCellNavigateHistoryToken createHistoryToken(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetCellNavigateHistoryToken.with(
            id,
            name,
            selection,
            NAVIGATIONS
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetCellNavigateHistoryToken> type() {
        return SpreadsheetCellNavigateHistoryToken.class;
    }
}
