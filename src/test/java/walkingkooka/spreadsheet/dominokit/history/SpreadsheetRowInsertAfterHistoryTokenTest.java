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
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;

public final class SpreadsheetRowInsertAfterHistoryTokenTest extends SpreadsheetRowHistoryTokenTestCase<SpreadsheetRowInsertAfterHistoryToken> {

    private final static int COUNT = 1;

    @Test
    public void testUrlFragmentRow1() {
        this.urlFragmentAndCheck(
                ROW,
                "/123/SpreadsheetName456/row/1/insertAfter/1");
    }

    @Test
    public void testUrlFragmentRow23() {
        this.urlFragmentAndCheck(
                SpreadsheetRowInsertAfterHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseRow("12").setDefaultAnchor(),
                        23
                ),
                "/123/SpreadsheetName456/row/12/insertAfter/23");
    }

    @Test
    public void testUrlFragmentRowRange1() {
        this.urlFragmentAndCheck(
                SpreadsheetSelection.parseRowRange("3:4").setAnchor(SpreadsheetViewportAnchor.BOTTOM),
                "/123/SpreadsheetName456/row/3:4/bottom/insertAfter/1"
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseRowMissingCount() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/row/5",
                HistoryToken.row(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseRow("5").setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseRowZeroCount() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/row/6/insertAfter/0",
                HistoryToken.row(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseRow("6").setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseRowWithCount() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/row/7/insertAfter/1",
                SpreadsheetRowInsertAfterHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseRow("7")
                                .setDefaultAnchor(),
                        COUNT
                )
        );
    }

    // setMenu1(Selection)..................................................................................................

    @Test
    public void testSetMenuWithRow() {
        this.setMenuWithRowAndCheck();
    }

    @Override
    SpreadsheetRowInsertAfterHistoryToken createHistoryToken(final SpreadsheetId id,
                                                             final SpreadsheetName name,
                                                             final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetRowInsertAfterHistoryToken.with(
                id,
                name,
                selection,
                COUNT
        );
    }

    @Override
    public Class<SpreadsheetRowInsertAfterHistoryToken> type() {
        return SpreadsheetRowInsertAfterHistoryToken.class;
    }
}
