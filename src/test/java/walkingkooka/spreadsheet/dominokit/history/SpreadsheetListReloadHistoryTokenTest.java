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

import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class SpreadsheetListReloadHistoryTokenTest extends SpreadsheetListHistoryTokenTestCase<SpreadsheetListReloadHistoryToken> {

    // count............................................................................................................

    @Test
    public void testCount() {
        this.countAndCheck(
            SpreadsheetListReloadHistoryToken.with(HistoryTokenOffsetAndCount.EMPTY)
        );
    }

    @Test
    public void testCount2() {
        final int count = 123;

        this.countAndCheck(
            SpreadsheetListReloadHistoryToken.with(
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
        final SpreadsheetListReloadHistoryToken historyToken = this.createHistoryToken();

        assertSame(
            historyToken,
            historyToken.setCount(historyToken.count())
        );
    }

    @Test
    public void testSetCountWithDifferent() {
        final OptionalInt count = OptionalInt.of(123);

        this.setCountAndCheck(
            SpreadsheetListReloadHistoryToken.with(HistoryTokenOffsetAndCount.EMPTY),
            count,
            SpreadsheetListReloadHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY.setCount(count)
            )
        );
    }

    // offset...........................................................................................................

    @Test
    public void testOffset() {
        this.offsetAndCheck(
            SpreadsheetListReloadHistoryToken.with(HistoryTokenOffsetAndCount.EMPTY)
        );
    }

    @Test
    public void testOffset2() {
        final int offset = 123;

        this.offsetAndCheck(
            SpreadsheetListReloadHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY.setOffset(
                    OptionalInt.of(offset)
                )
            ),
            offset
        );
    }

    // setOffset........................................................................................................

    @Test
    public void testSetOffsetWithSame() {
        final SpreadsheetListReloadHistoryToken historyToken = this.createHistoryToken();

        assertSame(
            historyToken,
            historyToken.setOffset(historyToken.offset())
        );
    }

    @Test
    public void testSetOffsetWithDifferent() {
        final OptionalInt offset = OptionalInt.of(456);

        this.setOffsetAndCheck(
            SpreadsheetListReloadHistoryToken.with(HistoryTokenOffsetAndCount.EMPTY),
            offset,
            SpreadsheetListReloadHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY.setOffset(offset)
            )
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseInvalidOffset() {
        this.parseAndCheck(
            "/*/offset/X/reload/",
            HistoryToken.spreadsheetListSelect(HistoryTokenOffsetAndCount.EMPTY)
        );
    }

    @Test
    public void testParseReload() {
        this.parseAndCheck(
            "/reload",
            HistoryToken.spreadsheetListSelect(HistoryTokenOffsetAndCount.EMPTY)
        );
    }

    @Test
    public void testParseStarReload() {
        this.parseAndCheck(
            "/*/reload",
            SpreadsheetListReloadHistoryToken.with(HistoryTokenOffsetAndCount.EMPTY)
        );
    }

    @Test
    public void testParseOffset() {
        this.parseAndCheck(
            "/*/offset/10/reload",
            SpreadsheetListReloadHistoryToken.with(
                HistoryTokenOffsetAndCount.with(
                    OptionalInt.of(10), // offset
                    OptionalInt.empty() // count
                )
            )
        );
    }

    @Test
    public void testParseCount() {
        this.parseAndCheck(
            "/*/count/20/reload",
            SpreadsheetListReloadHistoryToken.with(
                HistoryTokenOffsetAndCount.with(
                    OptionalInt.empty(), // offset
                    OptionalInt.of(20) // count
                )
            )
        );
    }

    @Test
    public void testParseOffsetAndCount() {
        this.parseAndCheck(
            "/*/offset/10/count/20/reload",
            SpreadsheetListReloadHistoryToken.with(
                HistoryTokenOffsetAndCount.with(
                    OptionalInt.of(10), // offset
                    OptionalInt.of(20) // count
                )
            )
        );
    }

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck("/*/offset/1/count/23/reload");
    }

    @Test
    public void testUrlFragmentOffset() {
        this.urlFragmentAndCheck(
            SpreadsheetListReloadHistoryToken.with(
                HistoryTokenOffsetAndCount.with(
                    OptionalInt.of(10), // offset
                    OptionalInt.empty() // count
                )
            ),
            "/*/offset/10/reload"
        );
    }

    @Test
    public void testUrlFragmentOffsetAndCount() {
        this.urlFragmentAndCheck(
            SpreadsheetListReloadHistoryToken.with(
                HistoryTokenOffsetAndCount.with(
                    OptionalInt.of(10), // offset
                    OptionalInt.of(20) // count
                )
            ),
            "/*/offset/10/count/20/reload"
        );
    }

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.spreadsheetListSelect(OFFSET_AND_COUNT)
        );
    }

    // reload...........................................................................................................

    @Test
    public void testReload() {
        this.reloadAndCheck(
            this.createHistoryToken(),
            HistoryToken.spreadsheetListReload(OFFSET_AND_COUNT)
        );
    }

    // helpers..........................................................................................................

    @Override
    SpreadsheetListReloadHistoryToken createHistoryToken(final HistoryTokenOffsetAndCount offsetAndCount) {
        return SpreadsheetListReloadHistoryToken.with(offsetAndCount);
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetListReloadHistoryToken> type() {
        return SpreadsheetListReloadHistoryToken.class;
    }
}
