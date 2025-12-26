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
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportHomeNavigationList;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportNavigationList;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetNavigateHistoryTokenTest extends SpreadsheetNameHistoryTokenTestCase<SpreadsheetNavigateHistoryToken> {

    private final static SpreadsheetViewportHomeNavigationList NAVIGATIONS = SpreadsheetViewportHomeNavigationList.with(
        SpreadsheetSelection.parseCell("Z99")
    ).setNavigations(
        SpreadsheetViewportNavigationList.parse("scroll right 555px")
    );

    @Test
    public void testWithNullNavigationFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetNavigateHistoryToken.with(
                ID,
                NAME,
                null
            )
        );
    }

    // urlFragment......................................................................................................

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/navigate/Z99/scroll right 555px");
    }

    // parse............................................................................................................

    @Test
    public void testParseInvalidHome() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/navigate/!invalid",
            HistoryToken.spreadsheetSelect(
                ID,
                NAME
            )
        );
    }

    @Test
    public void testParseInvalidNavigation() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/navigate/Z9/!invalid",
            HistoryToken.spreadsheetSelect(
                ID,
                NAME
            )
        );
    }

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.spreadsheetSelect(
                ID,
                NAME
            )
        );
    }

    // close............................................................................................................

    @Test
    public void testClose() {
        this.closeAndCheck(
            this.createHistoryToken(),
            HistoryToken.spreadsheetSelect(
                ID,
                NAME
            )
        );
    }

    // delete...........................................................................................................

    @Test
    public void testDelete() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.spreadsheetSelect(
                ID,
                NAME
            )
        );
    }

    // navigation.......................................................................................................

    @Test
    public void testNavigation() {
        this.navigationAndCheck(
            this.createHistoryToken(),
            NAVIGATIONS
        );
    }

    @Override
    SpreadsheetNavigateHistoryToken createHistoryToken(final SpreadsheetId id,
                                                       final SpreadsheetName name) {
        return SpreadsheetNavigateHistoryToken.with(
            id,
            name,
            Optional.of(NAVIGATIONS)
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetNavigateHistoryToken> type() {
        return SpreadsheetNavigateHistoryToken.class;
    }
}
