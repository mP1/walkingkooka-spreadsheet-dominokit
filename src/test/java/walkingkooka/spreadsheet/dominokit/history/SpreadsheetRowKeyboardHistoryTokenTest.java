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

public final class SpreadsheetRowKeyboardHistoryTokenTest extends SpreadsheetRowHistoryTokenTestCase<SpreadsheetRowKeyboardHistoryToken> {

    @Test
    public void testUrlFragmentRow() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/row/1/keyboard");
    }

    @Test
    public void testUrlFragmentRowRangeStar() {
        this.urlFragmentAndCheck(
            SpreadsheetSelection.ALL_ROWS.setAnchor(SpreadsheetViewportAnchor.TOP),
            "/123/SpreadsheetName456/row/*/top/keyboard"
        );
    }

    @Test
    public void testUrlFragmentRowRange() {
        this.urlFragmentAndCheck(
            ROW_RANGE.setAnchor(SpreadsheetViewportAnchor.TOP),
            "/123/SpreadsheetName456/row/2:3/top/keyboard"
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
    SpreadsheetRowKeyboardHistoryToken createHistoryToken(final SpreadsheetId id,
                                                          final SpreadsheetName name,
                                                          final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetRowKeyboardHistoryToken.with(
            id,
            name,
            selection
        );
    }

    // Class............................................................................................................

    @Override
    public Class<SpreadsheetRowKeyboardHistoryToken> type() {
        return SpreadsheetRowKeyboardHistoryToken.class;
    }
}
