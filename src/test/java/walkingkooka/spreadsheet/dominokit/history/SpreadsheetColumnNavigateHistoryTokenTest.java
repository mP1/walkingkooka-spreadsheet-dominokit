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
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportAnchor;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportHomeNavigationList;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportNavigationList;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetColumnNavigateHistoryTokenTest extends SpreadsheetColumnHistoryTokenTestCase<SpreadsheetColumnNavigateHistoryToken> {

    private final static SpreadsheetViewportHomeNavigationList NAVIGATIONS = SpreadsheetViewportHomeNavigationList.with(
        SpreadsheetSelection.parseCell("Z99")
    ).setNavigations(
        SpreadsheetViewportNavigationList.parse("right 555px")
    );

    @Test
    public void testWithNullNavigationFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetColumnNavigateHistoryToken.with(
                ID,
                NAME,
                COLUMN.setDefaultAnchor(),
                null
            )
        );
    }

    // urlFragment......................................................................................................

    @Test
    public void testUrlFragmentColumn() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/column/A/navigate/Z99/right 555px");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
            COLUMN_RANGE.setAnchor(SpreadsheetViewportAnchor.LEFT),
            "/123/SpreadsheetName456/column/B:C/left/navigate/Z99/right 555px"
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseInvalidHome() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/column/A/navigate/home/!invalid",
            HistoryToken.columnSelect(
                ID,
                NAME,
                COLUMN.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseInvalidNavigation() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/column/A/navigate/home/Z9/!invalid",
            HistoryToken.columnSelect(
                ID,
                NAME,
                COLUMN.setDefaultAnchor()
            )
        );
    }

    // delete...........................................................................................................

    @Test
    public void testDelete() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.columnSelect(
                ID,
                NAME,
                COLUMN.setDefaultAnchor()
            )
        );
    }

    @Override
    SpreadsheetColumnNavigateHistoryToken createHistoryToken(final SpreadsheetId id,
                                                             final SpreadsheetName name,
                                                             final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetColumnNavigateHistoryToken.with(
            id,
            name,
            selection,
            NAVIGATIONS
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetColumnNavigateHistoryToken> type() {
        return SpreadsheetColumnNavigateHistoryToken.class;
    }
}
