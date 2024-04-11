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
    public void testParseInvalidFrom() {
        this.parseAndCheck(
                "/reload/from/X",
                SpreadsheetListReloadHistoryToken.with(
                        OptionalInt.empty(), // from
                        OptionalInt.empty() // count
                )
        );
    }

    @Test
    public void testParseReload() {
        this.parseAndCheck(
                "/reload",
                SpreadsheetListReloadHistoryToken.with(
                        OptionalInt.empty(), // from
                        OptionalInt.empty() // count
                )
        );
    }

    @Test
    public void testParseFrom() {
        this.parseAndCheck(
                "/reload/from/10",
                SpreadsheetListReloadHistoryToken.with(
                        OptionalInt.of(10), // from
                        OptionalInt.empty() // count
                )
        );
    }

    @Test
    public void testParseCount() {
        this.parseAndCheck(
                "/reload/count/20",
                SpreadsheetListReloadHistoryToken.with(
                        OptionalInt.empty(), // from
                        OptionalInt.of(20) // count
                )
        );
    }

    @Test
    public void testParseFromAndCount() {
        this.parseAndCheck(
                "/reload/from/10/count/20",
                SpreadsheetListReloadHistoryToken.with(
                        OptionalInt.of(10), // from
                        OptionalInt.of(20) // count
                )
        );
    }

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck("/reload/from/1/count/23");
    }

    @Test
    public void testUrlFragmentFrom() {
        this.urlFragmentAndCheck(
                SpreadsheetListReloadHistoryToken.with(
                        OptionalInt.of(10), // from
                        OptionalInt.empty() // count
                ),
                "/reload/from/10"
        );
    }

    @Test
    public void testUrlFragmentFromAndCount() {
        this.urlFragmentAndCheck(
                SpreadsheetListReloadHistoryToken.with(
                        OptionalInt.of(10), // from
                        OptionalInt.of(20) // count
                ),
                "/reload/from/10/count/20"
        );
    }

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
                this.createHistoryToken(),
                HistoryToken.spreadsheetListSelect(
                        FROM,
                        COUNT
                )
        );
    }

    @Test
    public void testReload() {
        this.reloadAndCheck(
                this.createHistoryToken(),
                HistoryToken.spreadsheetListReload(
                        FROM,
                        COUNT
                )
        );
    }

    @Override
    SpreadsheetListReloadHistoryToken createHistoryToken(final OptionalInt from,
                                                         final OptionalInt count) {
        return SpreadsheetListReloadHistoryToken.with(
                from,
                count
        );
    }

    @Override
    public Class<SpreadsheetListReloadHistoryToken> type() {
        return SpreadsheetListReloadHistoryToken.class;
    }
}
