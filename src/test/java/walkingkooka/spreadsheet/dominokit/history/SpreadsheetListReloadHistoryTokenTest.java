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

public final class SpreadsheetListReloadHistoryTokenTest extends SpreadsheetListHistoryTokenTestCase<SpreadsheetListReloadHistoryToken> {

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

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.spreadsheetListSelect(OFFSET_AND_COUNT)
        );
    }

    @Test
    public void testReload() {
        this.reloadAndCheck(
            this.createHistoryToken(),
            HistoryToken.spreadsheetListReload(OFFSET_AND_COUNT)
        );
    }

    @Override
    SpreadsheetListReloadHistoryToken createHistoryToken(final HistoryTokenOffsetAndCount offsetAndCount) {
        return SpreadsheetListReloadHistoryToken.with(offsetAndCount);
    }

    @Override
    public Class<SpreadsheetListReloadHistoryToken> type() {
        return SpreadsheetListReloadHistoryToken.class;
    }
}
