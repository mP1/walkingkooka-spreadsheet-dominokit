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
                "/reload/offset/X",
                SpreadsheetListReloadHistoryToken.with(
                        OptionalInt.empty(), // offset
                        OptionalInt.empty() // count
                )
        );
    }

    @Test
    public void testParseReload() {
        this.parseAndCheck(
                "/reload",
                SpreadsheetListReloadHistoryToken.with(
                        OptionalInt.empty(), // offset
                        OptionalInt.empty() // count
                )
        );
    }

    @Test
    public void testParseOffset() {
        this.parseAndCheck(
                "/reload/offset/10",
                SpreadsheetListReloadHistoryToken.with(
                        OptionalInt.of(10), // offset
                        OptionalInt.empty() // count
                )
        );
    }

    @Test
    public void testParseCount() {
        this.parseAndCheck(
                "/reload/count/20",
                SpreadsheetListReloadHistoryToken.with(
                        OptionalInt.empty(), // offset
                        OptionalInt.of(20) // count
                )
        );
    }

    @Test
    public void testParseOffsetAndCount() {
        this.parseAndCheck(
                "/reload/offset/10/count/20",
                SpreadsheetListReloadHistoryToken.with(
                        OptionalInt.of(10), // offset
                        OptionalInt.of(20) // count
                )
        );
    }

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck("/reload/offset/1/count/23");
    }

    @Test
    public void testUrlFragmentOffset() {
        this.urlFragmentAndCheck(
                SpreadsheetListReloadHistoryToken.with(
                        OptionalInt.of(10), // offset
                        OptionalInt.empty() // count
                ),
                "/reload/offset/10"
        );
    }

    @Test
    public void testUrlFragmentOffsetAndCount() {
        this.urlFragmentAndCheck(
                SpreadsheetListReloadHistoryToken.with(
                        OptionalInt.of(10), // offset
                        OptionalInt.of(20) // count
                ),
                "/reload/offset/10/count/20"
        );
    }

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
                this.createHistoryToken(),
                HistoryToken.spreadsheetListSelect(
                        OFFSET,
                        COUNT
                )
        );
    }

    @Test
    public void testReload() {
        this.setReloadAndCheck(
                this.createHistoryToken(),
                HistoryToken.spreadsheetListReload(
                        OFFSET,
                        COUNT
                )
        );
    }

    @Override
    SpreadsheetListReloadHistoryToken createHistoryToken(final OptionalInt offset,
                                                         final OptionalInt count) {
        return SpreadsheetListReloadHistoryToken.with(
                offset,
                count
        );
    }

    @Override
    public Class<SpreadsheetListReloadHistoryToken> type() {
        return SpreadsheetListReloadHistoryToken.class;
    }
}
