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
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportAnchor;

public final class SpreadsheetColumnKeyboardHistoryTokenTest extends SpreadsheetColumnHistoryTokenTestCase<SpreadsheetColumnKeyboardHistoryToken> {

    @Test
    public void testUrlFragmentColumn() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/column/A/keyboard");
    }

    @Test
    public void testUrlFragmentColumnRangeStar() {
        this.urlFragmentAndCheck(
            SpreadsheetSelection.ALL_COLUMNS.setAnchor(SpreadsheetViewportAnchor.LEFT),
            "/123/SpreadsheetName456/column/*/left/keyboard"
        );
    }

    @Test
    public void testUrlFragmentColumnRange() {
        this.urlFragmentAndCheck(
            COLUMN_RANGE.setAnchor(SpreadsheetViewportAnchor.LEFT),
            "/123/SpreadsheetName456/column/B:C/left/keyboard"
        );
    }

    // close............................................................................................................

    @Test
    public void testClose() {
        this.closeAndCheck(
            this.createHistoryToken(),
            HistoryToken.columnSelect(
                ID,
                NAME,
                COLUMN.setDefaultAnchor()
            )
        );
    }

    // navigation.......................................................................................................

    @Test
    public void testNavigation() {
        this.navigationAndCheck(
            this.createHistoryToken()
        );
    }

    @Override
    SpreadsheetColumnKeyboardHistoryToken createHistoryToken(final SpreadsheetId id,
                                                             final SpreadsheetName name,
                                                             final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetColumnKeyboardHistoryToken.with(
            id,
            name,
            selection
        );
    }

    // Class............................................................................................................

    @Override
    public Class<SpreadsheetColumnKeyboardHistoryToken> type() {
        return SpreadsheetColumnKeyboardHistoryToken.class;
    }
}
