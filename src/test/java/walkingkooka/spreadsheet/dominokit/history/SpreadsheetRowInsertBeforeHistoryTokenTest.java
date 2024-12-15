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

import java.util.OptionalInt;

public final class SpreadsheetRowInsertBeforeHistoryTokenTest extends SpreadsheetRowHistoryTokenTestCase<SpreadsheetRowInsertBeforeHistoryToken> {

    private final static OptionalInt COUNT = OptionalInt.of(1);

    @Test
    public void testUrlFragmentRow1() {
        this.urlFragmentAndCheck(
                ROW,
                "/123/SpreadsheetName456/row/1/insertBefore/1");
    }

    @Test
    public void testUrlFragmentRowEmptyCount() {
        this.urlFragmentAndCheck(
                SpreadsheetRowInsertBeforeHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseRow("12").setDefaultAnchor(),
                        OptionalInt.empty()
                ),
                "/123/SpreadsheetName456/row/12/insertBefore");
    }

    @Test
    public void testUrlFragmentRow23() {
        this.urlFragmentAndCheck(
                SpreadsheetRowInsertBeforeHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseRow("12").setDefaultAnchor(),
                        OptionalInt.of(
                                23
                        )
                ),
                "/123/SpreadsheetName456/row/12/insertBefore/23");
    }

    @Test
    public void testUrlFragmentRowRange1() {
        this.urlFragmentAndCheck(
                SpreadsheetSelection.parseRowRange("3:4").setAnchor(SpreadsheetViewportAnchor.BOTTOM),
                "/123/SpreadsheetName456/row/3:4/bottom/insertBefore/1"
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
                "/123/SpreadsheetName456/row/6/insertBefore/0",
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
                "/123/SpreadsheetName456/row/7/insertBefore/1",
                SpreadsheetRowInsertBeforeHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseRow("7")
                                .setDefaultAnchor(),
                        COUNT
                )
        );
    }

    // close...........................................................................................................

    @Test
    public void testClose() {
        this.closeAndCheck(
                this.createHistoryToken(),
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor()
                )
        );
    }

    // menu(Selection)..................................................................................................

    @Test
    public void testMenuWithRow() {
        this.menuWithRowAndCheck();
    }

    @Override
    SpreadsheetRowInsertBeforeHistoryToken createHistoryToken(final SpreadsheetId id,
                                                              final SpreadsheetName name,
                                                              final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetRowInsertBeforeHistoryToken.with(
                id,
                name,
                selection,
                COUNT
        );
    }

    @Override
    public Class<SpreadsheetRowInsertBeforeHistoryToken> type() {
        return SpreadsheetRowInsertBeforeHistoryToken.class;
    }
}
