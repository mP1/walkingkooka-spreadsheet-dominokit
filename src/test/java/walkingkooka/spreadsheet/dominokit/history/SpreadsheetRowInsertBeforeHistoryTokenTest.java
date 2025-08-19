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

import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class SpreadsheetRowInsertBeforeHistoryTokenTest extends SpreadsheetRowHistoryTokenTestCase<SpreadsheetRowInsertBeforeHistoryToken> {

    private final static OptionalInt COUNT = OptionalInt.of(1);

    // count............................................................................................................

    @Test
    public void testCount() {
        this.countAndCheck(
            SpreadsheetRowInsertBeforeHistoryToken.with(
                ID,
                NAME,
                ROW.setDefaultAnchor(),
                OptionalInt.empty()
            )
        );
    }

    @Test
    public void testCount2() {
        final int count = 123;

        this.countAndCheck(
            SpreadsheetRowInsertBeforeHistoryToken.with(
                ID,
                NAME,
                ROW.setDefaultAnchor(),
                OptionalInt.of(count)
            ),
            count
        );
    }

    // setCount.........................................................................................................

    @Test
    public void testSetCountWithSame() {
        final SpreadsheetRowInsertBeforeHistoryToken historyToken = this.createHistoryToken();

        assertSame(
            historyToken,
            historyToken.setCount(historyToken.count())
        );
    }

    @Test
    public void testSetCountWithDifferent() {
        final OptionalInt count = OptionalInt.of(123);

        this.setCountAndCheck(
            this.createHistoryToken(),
            count,
            SpreadsheetRowInsertBeforeHistoryToken.with(
                ID,
                NAME,
                ROW.setDefaultAnchor(),
                count
            )
        );
    }

    // UrlFragment......................................................................................................
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
            HistoryToken.rowSelect(
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
            HistoryToken.rowSelect(
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
            HistoryToken.rowSelect(
                ID,
                NAME,
                ROW.setDefaultAnchor()
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
