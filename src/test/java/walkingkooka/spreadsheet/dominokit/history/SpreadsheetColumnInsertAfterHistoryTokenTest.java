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

public final class SpreadsheetColumnInsertAfterHistoryTokenTest extends SpreadsheetColumnHistoryTokenTestCase<SpreadsheetColumnInsertAfterHistoryToken> {

    private final static OptionalInt COUNT = OptionalInt.of(1);

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
                HistoryToken.column(
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
                HistoryToken.column(
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
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                )
        );
    }

    // setMenu1(Selection)..................................................................................................

    @Test
    public void testSetMenuWithColumn() {
        this.setMenuWithColumnAndCheck();
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
