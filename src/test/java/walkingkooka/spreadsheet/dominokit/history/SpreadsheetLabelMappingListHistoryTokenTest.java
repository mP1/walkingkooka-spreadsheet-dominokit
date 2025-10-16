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

import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class SpreadsheetLabelMappingListHistoryTokenTest extends SpreadsheetLabelMappingHistoryTokenTestCase<SpreadsheetLabelMappingListHistoryToken> {

    // offset...........................................................................................................

    @Test
    public void testOffset() {
        final SpreadsheetLabelMappingListHistoryToken historyToken = this.createHistoryToken();
        this.checkEquals(
            OptionalInt.empty(),
            historyToken.offset()
        );
    }

    @Test
    public void testOffset2() {
        final int offset = 123;

        final SpreadsheetLabelMappingListHistoryToken historyToken = SpreadsheetLabelMappingListHistoryToken.with(
            ID,
            NAME,
            HistoryTokenOffsetAndCount.EMPTY.setOffset(
                OptionalInt.of(offset)
            )
        );
        this.checkEquals(
            OptionalInt.of(offset),
            historyToken.offset()
        );
    }

    // setOffset........................................................................................................

    @Test
    public void testSetOffsetWithSame() {
        final SpreadsheetLabelMappingListHistoryToken historyToken = this.createHistoryToken();

        assertSame(
            historyToken,
            historyToken.setOffset(historyToken.offset())
        );
    }

    @Test
    public void testSetOffsetWithDifferent() {
        final OptionalInt offset = OptionalInt.of(999);

        this.setOffsetAndCheck(
            SpreadsheetLabelMappingListHistoryToken.with(
                ID,
                NAME,
                HistoryTokenOffsetAndCount.EMPTY
            ),
            offset,
            SpreadsheetLabelMappingListHistoryToken.with(
                ID,
                NAME,
                HistoryTokenOffsetAndCount.EMPTY.setOffset(offset)
            )
        );
    }

    // count............................................................................................................

    @Test
    public void testCount() {
        this.countAndCheck(
            this.createHistoryToken()
        );
    }

    @Test
    public void testCount2() {
        final int count = 123;

        this.countAndCheck(
            SpreadsheetLabelMappingListHistoryToken.with(
                ID,
                NAME,
                HistoryTokenOffsetAndCount.EMPTY.setCount(
                    OptionalInt.of(count)
                )
            ),
            count
        );
    }

    // setCount.........................................................................................................

    @Test
    public void testSetCountWithSame() {
        final SpreadsheetLabelMappingListHistoryToken historyToken = this.createHistoryToken();

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
            SpreadsheetLabelMappingListHistoryToken.with(
                ID,
                NAME,
                HistoryTokenOffsetAndCount.EMPTY.setCount(count)
            )
        );
    }

    // setList..........................................................................................................

    @Test
    public void testList() {
        this.listAndCheck(
            this.createHistoryToken()
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseWithoutOffsetAndCount() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/label/*",
            SpreadsheetLabelMappingListHistoryToken.with(
                ID,
                NAME,
                HistoryTokenOffsetAndCount.EMPTY
            )
        );
    }

    @Test
    public void testParseWithOffset() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/label/*/offset/123",
            SpreadsheetLabelMappingListHistoryToken.with(
                ID,
                NAME,
                HistoryTokenOffsetAndCount.EMPTY.setOffset(
                    OptionalInt.of(123)
                )
            )
        );
    }

    @Test
    public void testParseWithCount() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/label/*/count/123",
            SpreadsheetLabelMappingListHistoryToken.with(
                ID,
                NAME,
                HistoryTokenOffsetAndCount.EMPTY.setCount(
                    OptionalInt.of(123)
                )
            )
        );
    }

    @Test
    public void testParseWithOffsetAndCount() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/label/*/offset/123/count/456",
            SpreadsheetLabelMappingListHistoryToken.with(
                ID,
                NAME,
                HistoryTokenOffsetAndCount.EMPTY.setOffset(
                    OptionalInt.of(123)
                ).setCount(
                    OptionalInt.of(456)
                )
            )
        );
    }

    // setLabelMappingReference.........................................................................................

    @Test
    public void testSetLabelMappingReferenceWithCell() {
        this.setLabelMappingReferenceAndCheck();
    }

    // UrlFragment......................................................................................................

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck(
            SpreadsheetLabelMappingListHistoryToken.with(
                ID,
                NAME,
                HistoryTokenOffsetAndCount.EMPTY
            ),
            "/123/SpreadsheetName456/label"
        );
    }

    @Test
    public void testUrlFragmentWithOffset() {
        this.urlFragmentAndCheck(
            SpreadsheetLabelMappingListHistoryToken.with(
                ID,
                NAME,
                HistoryTokenOffsetAndCount.EMPTY.setOffset(
                    OptionalInt.of(123)
                )
            ),
            "/123/SpreadsheetName456/label/*/offset/123"
        );
    }

    @Test
    public void testUrlFragmentWithOffsetAndCount() {
        this.urlFragmentAndCheck(
            SpreadsheetLabelMappingListHistoryToken.with(
                ID,
                NAME,
                HistoryTokenOffsetAndCount.EMPTY.setOffset(
                    OptionalInt.of(123)
                ).setCount(
                    OptionalInt.of(456)
                )
            ),
            "/123/SpreadsheetName456/label/*/offset/123/count/456"
        );
    }

    @Test
    public void testUrlFragmentWithCount() {
        this.urlFragmentAndCheck(
            SpreadsheetLabelMappingListHistoryToken.with(
                ID,
                NAME,
                HistoryTokenOffsetAndCount.EMPTY.setCount(
                    OptionalInt.of(456)
                )
            ),
            "/123/SpreadsheetName456/label/*/count/456"
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

    // helpers..........................................................................................................

    @Override
    SpreadsheetLabelMappingListHistoryToken createHistoryToken(final SpreadsheetId id,
                                                               final SpreadsheetName name) {
        return SpreadsheetLabelMappingListHistoryToken.with(
            id,
            name,
            HistoryTokenOffsetAndCount.EMPTY
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetLabelMappingListHistoryToken> type() {
        return SpreadsheetLabelMappingListHistoryToken.class;
    }
}
