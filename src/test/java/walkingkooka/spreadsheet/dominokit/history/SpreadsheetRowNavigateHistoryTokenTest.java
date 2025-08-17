/*
 * Copydown 2023 Miroslav Pokorny (github.com/mP1)
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetRowNavigateHistoryTokenTest extends SpreadsheetRowHistoryTokenTestCase<SpreadsheetRowNavigateHistoryToken> {

    private final static SpreadsheetViewportHomeNavigationList NAVIGATIONS = SpreadsheetViewportHomeNavigationList.with(
        SpreadsheetSelection.parseCell("Z99")
    ).setNavigations(
        SpreadsheetViewportNavigationList.parse("down 555px")
    );

    @Test
    public void testWithNullNavigationFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetRowNavigateHistoryToken.with(
                ID,
                NAME,
                ROW.setDefaultAnchor(),
                null
            )
        );
    }

    // urlFragment......................................................................................................

    @Test
    public void testUrlFragmentColumn() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/row/1/navigate/Z99/down 555px");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
            ROW_RANGE.setAnchor(SpreadsheetViewportAnchor.BOTTOM),
            "/123/SpreadsheetName456/row/2:3/bottom/navigate/Z99/down 555px"
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseInvalidHome() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/row/1/navigate/!invalid",
            HistoryToken.rowSelect(
                ID,
                NAME,
                ROW.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseInvalidNavigation() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/row/1/navigate/Z9/!invalid",
            HistoryToken.rowSelect(
                ID,
                NAME,
                ROW.setDefaultAnchor()
            )
        );
    }

    // close............................................................................................................

    @Test
    public void testClose() {
        this.closeAndCheck(
            this.createHistoryToken(),
            HistoryToken.rowSelect(
                ID,
                NAME,
                ROW.setDefaultAnchor()
            )
        );
    }

    // delete...........................................................................................................

    @Test
    public void testDelete() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.rowSelect(
                ID,
                NAME,
                ROW.setDefaultAnchor()
            )
        );
    }

    @Override
    SpreadsheetRowNavigateHistoryToken createHistoryToken(final SpreadsheetId id,
                                                          final SpreadsheetName name,
                                                          final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetRowNavigateHistoryToken.with(
            id,
            name,
            selection,
            Optional.of(NAVIGATIONS)
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetRowNavigateHistoryToken> type() {
        return SpreadsheetRowNavigateHistoryToken.class;
    }
}
