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

public final class PluginListSelectHistoryTokenTest extends PluginListHistoryTokenTestCase<PluginListSelectHistoryToken> {

    @Test
    public void testParseInvalidOffset() {
        this.parseAndCheck(
            "/plugin/*/offset/X",
            PluginListSelectHistoryToken.with(
                OptionalInt.empty(), // offset
                OptionalInt.empty() // count
            )
        );
    }

    @Test
    public void testParseOffset() {
        this.parseAndCheck(
            "/plugin/*/offset/10",
            PluginListSelectHistoryToken.with(
                OptionalInt.of(10), // offset
                OptionalInt.empty() // count
            )
        );
    }

    @Test
    public void testParseCount() {
        this.parseAndCheck(
            "/plugin/*/count/20",
            PluginListSelectHistoryToken.with(
                OptionalInt.empty(), // offset
                OptionalInt.of(20) // count
            )
        );
    }

    @Test
    public void testParseOffsetAndCount() {
        this.parseAndCheck(
            "/plugin/*/offset/10/count/20",
            PluginListSelectHistoryToken.with(
                OptionalInt.of(10), // offset
                OptionalInt.of(20) // count
            )
        );
    }

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck(
            PluginListSelectHistoryToken.with(
                OptionalInt.empty(), // offset
                OptionalInt.empty() // count
            ),
            "/plugin");
    }

    @Test
    public void testUrlFragmentOffset() {
        this.urlFragmentAndCheck(
            PluginListSelectHistoryToken.with(
                OptionalInt.of(10), // offset
                OptionalInt.empty() // count
            ),
            "/plugin/*/offset/10"
        );
    }

    @Test
    public void testUrlFragmentOffsetAndCount() {
        this.urlFragmentAndCheck(
            PluginListSelectHistoryToken.with(
                OptionalInt.of(10), // offset
                OptionalInt.of(20) // count
            ),
            "/plugin/*/offset/10/count/20"
        );
    }

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.spreadsheetListSelect(
                OptionalInt.empty(), // offset
                OptionalInt.empty() // count
            )
        );
    }

    @Test
    public void testClose() {
        this.closeAndCheck(
            HistoryToken.spreadsheetListSelect(
                OptionalInt.empty(), // offset
                OptionalInt.empty() // count
            )
        );
    }

    @Test
    public void testReload() {
        this.reloadAndCheck(
            this.createHistoryToken(),
            HistoryToken.pluginListReload(
                OFFSET,
                COUNT
            )
        );
    }

    @Override
    PluginListSelectHistoryToken createHistoryToken(final OptionalInt offset,
                                                    final OptionalInt count) {
        return PluginListSelectHistoryToken.with(
            offset,
            count
        );
    }

    @Override
    public Class<PluginListSelectHistoryToken> type() {
        return PluginListSelectHistoryToken.class;
    }
}
