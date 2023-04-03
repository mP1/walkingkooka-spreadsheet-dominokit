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
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public final class SpreadsheetRowMenuHistoryTokenTest extends SpreadsheetRowHistoryTokenTestCase<SpreadsheetRowMenuHistoryToken> {

    @Test
    public void testUrlFragmentRow() {
        this.urlFragmentAndCheck(
                ROW,
                "/123/SpreadsheetName456/row/1/menu"
        );
    }

    @Test
    public void testUrlFragmentRowRange() {
        this.urlFragmentAndCheck(
                ROW_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM),
                "/123/SpreadsheetName456/row/2:3/bottom/menu"
        );
    }

    // menu(Selection)..................................................................................................

    @Test
    public void testRowMenuWithSameRow() {
        final SpreadsheetRowReference row = SpreadsheetSelection.parseRow("2");

        this.menuAndCheck(
                this.createHistoryToken(
                        row.setDefaultAnchor()
                ),
                row,
                SpreadsheetHistoryToken.rowMenu(
                        ID,
                        NAME,
                        row.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testRowMenuWithDifferentRow() {
        final SpreadsheetRowReference row = SpreadsheetSelection.parseRow("2");

        this.menuAndCheck(
                this.createHistoryToken(
                        SpreadsheetSelection.parseRow("1").setDefaultAnchor()
                ),
                row,
                SpreadsheetHistoryToken.rowMenu(
                        ID,
                        NAME,
                        row.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testRowRangeMenuWithRowInside() {
        this.menuAndCheck(
                this.createHistoryToken(
                        SpreadsheetSelection.parseRowRange("1:3").setDefaultAnchor()
                ),
                SpreadsheetSelection.parseRow("2")
        );
    }

    @Test
    public void testRowRangeMenuWithRowOutside() {
        final SpreadsheetRowReference row = SpreadsheetSelection.parseRow("99");

        this.menuAndCheck(
                this.createHistoryToken(
                        SpreadsheetSelection.parseRowRange("1:3").setDefaultAnchor()
                ),
                row,
                SpreadsheetHistoryToken.rowMenu(
                        ID,
                        NAME,
                        row.setDefaultAnchor()
                )
        );
    }

    @Override
    SpreadsheetRowMenuHistoryToken createHistoryToken(final SpreadsheetId id,
                                                      final SpreadsheetName name,
                                                      final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetRowMenuHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    @Override
    public Class<SpreadsheetRowMenuHistoryToken> type() {
        return SpreadsheetRowMenuHistoryToken.class;
    }
}
