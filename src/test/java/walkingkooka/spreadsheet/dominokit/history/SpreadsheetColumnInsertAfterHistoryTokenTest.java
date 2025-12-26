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
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportAnchor;

import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class SpreadsheetColumnInsertAfterHistoryTokenTest extends SpreadsheetColumnHistoryTokenTestCase<SpreadsheetColumnInsertAfterHistoryToken> {

    private final static OptionalInt COUNT = OptionalInt.of(1);

    // count............................................................................................................

    @Test
    public void testCount() {
        this.countAndCheck(
            SpreadsheetColumnInsertAfterHistoryToken.with(
                ID,
                NAME,
                COLUMN.setDefaultAnchor(),
                OptionalInt.empty()
            )
        );
    }

    @Test
    public void testCount2() {
        final int count = 123;

        this.countAndCheck(
            SpreadsheetColumnInsertAfterHistoryToken.with(
                ID,
                NAME,
                COLUMN.setDefaultAnchor(),
                OptionalInt.of(count)
            ),
            count
        );
    }

    // setCount.........................................................................................................

    @Test
    public void testSetCountWithSame() {
        final SpreadsheetColumnInsertAfterHistoryToken historyToken = this.createHistoryToken();

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
            SpreadsheetColumnInsertAfterHistoryToken.with(
                ID,
                NAME,
                COLUMN.setDefaultAnchor(),
                count
            )
        );
    }

    // UrlFragment......................................................................................................

    @Test
    public void testUrlFragmentColumn1() {
        this.urlFragmentAndCheck(
            COLUMN,
            "/123/SpreadsheetName456/column/A/insertAfter/1");
    }

    @Test
    public void testUrlFragmentColumnEmptyCount() {
        this.urlFragmentAndCheck(
            SpreadsheetColumnInsertAfterHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseColumn("BC").setDefaultAnchor(),
                OptionalInt.empty()
            ),
            "/123/SpreadsheetName456/column/BC/insertAfter");
    }

    @Test
    public void testUrlFragmentColumn23() {
        this.urlFragmentAndCheck(
            SpreadsheetColumnInsertAfterHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseColumn("BC").setDefaultAnchor(),
                OptionalInt.of(
                    23
                )
            ),
            "/123/SpreadsheetName456/column/BC/insertAfter/23");
    }

    @Test
    public void testUrlFragmentColumnRange1() {
        this.urlFragmentAndCheck(
            SpreadsheetSelection.parseColumnRange("A:B").setAnchor(SpreadsheetViewportAnchor.RIGHT),
            "/123/SpreadsheetName456/column/A:B/right/insertAfter/1"
        );
    }

    @Test
    public void testUrlFragmentColumnRangeStar() {
        this.urlFragmentAndCheck(
            SpreadsheetSelection.ALL_COLUMNS.setAnchor(SpreadsheetViewportAnchor.RIGHT),
            "/123/SpreadsheetName456/column/*/right/insertAfter/1"
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseColumnMissingCount() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/column/B",
            HistoryToken.columnSelect(
                ID,
                NAME,
                SpreadsheetSelection.parseColumn("B").setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseColumnZeroCount() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/column/C/insertAfter/0",
            HistoryToken.columnSelect(
                ID,
                NAME,
                SpreadsheetSelection.parseColumn("C").setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseColumnWithCount() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/column/B/insertAfter/1",
            SpreadsheetColumnInsertAfterHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseColumn("B")
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
    SpreadsheetColumnInsertAfterHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                final SpreadsheetName name,
                                                                final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetColumnInsertAfterHistoryToken.with(
            id,
            name,
            selection,
            COUNT
        );
    }

    @Override
    public Class<SpreadsheetColumnInsertAfterHistoryToken> type() {
        return SpreadsheetColumnInsertAfterHistoryToken.class;
    }
}
