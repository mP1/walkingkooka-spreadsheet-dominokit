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

public final class SpreadsheetListSelectHistoryTokenTest extends SpreadsheetListHistoryTokenTestCase<SpreadsheetListSelectHistoryToken> {

    @Test
    public void testParseInvalidOffset() {
        this.parseAndCheck(
            "/*/offset/X",
            SpreadsheetListSelectHistoryToken.with(HistoryTokenOffsetAndCount.EMPTY)
        );
    }

    @Test
    public void testParseOffset() {
        this.parseAndCheck(
            "/*/offset/10",
            SpreadsheetListSelectHistoryToken.with(
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
            "/*/count/20",
            SpreadsheetListSelectHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY.setCount(
                    OptionalInt.of(20)
                )
            )
        );
    }

    @Test
    public void testParseOffsetAndCount() {
        this.parseAndCheck(
            "/*/offset/10/count/20",
            SpreadsheetListSelectHistoryToken.with(
                HistoryTokenOffsetAndCount.with(
                    OptionalInt.of(10), // offset
                    OptionalInt.of(20) // count
                )
            )
        );
    }

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck("/*/offset/1/count/23");
    }

    @Test
    public void testUrlFragmentOffset() {
        this.urlFragmentAndCheck(
            SpreadsheetListSelectHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY.setOffset(
                    OptionalInt.of(10)
                )
            ),
            "/*/offset/10"
        );
    }

    @Test
    public void testUrlFragmentOffsetAndCount() {
        this.urlFragmentAndCheck(
            SpreadsheetListSelectHistoryToken.with(
                HistoryTokenOffsetAndCount.with(
                    OptionalInt.of(10), // offset
                    OptionalInt.of(20) // count
                )
            ),
            "/*/offset/10/count/20"
        );
    }

    @Test
    public void testClearAction() {
        this.clearActionAndCheck();
    }

    @Test
    public void testReload() {
        this.reloadAndCheck(
            this.createHistoryToken(),
            HistoryToken.spreadsheetListReload(OFFSET_AND_COUNT)
        );
    }

    @Override
    SpreadsheetListSelectHistoryToken createHistoryToken(final HistoryTokenOffsetAndCount offsetAndCount) {
        return SpreadsheetListSelectHistoryToken.with(offsetAndCount);
    }

    @Override
    public Class<SpreadsheetListSelectHistoryToken> type() {
        return SpreadsheetListSelectHistoryToken.class;
    }
}
