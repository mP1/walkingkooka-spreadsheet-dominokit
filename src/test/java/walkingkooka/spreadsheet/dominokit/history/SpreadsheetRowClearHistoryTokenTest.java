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

public final class SpreadsheetRowClearHistoryTokenTest extends SpreadsheetRowHistoryTokenTestCase<SpreadsheetRowClearHistoryToken> {

    @Test
    public void testUrlFragmentRow() {
        this.urlFragmentAndCheck(
                ROW,
                "/123/SpreadsheetName456/row/1/clear"
        );
    }

    @Test
    public void testUrlFragmentRowRange() {
        this.urlFragmentAndCheck(
                ROW_RANGE.setAnchor(SpreadsheetViewportAnchor.BOTTOM),
                "/123/SpreadsheetName456/row/2:3/bottom/clear"
        );
    }

    // setMenu1(Selection)..................................................................................................

    @Test
    public void testSetMenuWithRow() {
        this.setMenuWithRowAndCheck();
    }

    @Override
    SpreadsheetRowClearHistoryToken createHistoryToken(final SpreadsheetId id,
                                                       final SpreadsheetName name,
                                                       final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetRowClearHistoryToken.with(
                id,
                name,
                selection
        );
    }

    @Override
    public Class<SpreadsheetRowClearHistoryToken> type() {
        return SpreadsheetRowClearHistoryToken.class;
    }
}
