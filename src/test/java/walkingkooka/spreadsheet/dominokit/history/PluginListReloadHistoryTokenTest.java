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

public final class PluginListReloadHistoryTokenTest extends PluginListHistoryTokenTestCase<PluginListReloadHistoryToken> {

    @Test
    public void testParseInvalidOffset() {
        this.parseAndCheck(
            "/plugin/*/offset/X/reload",
            HistoryToken.pluginListSelect(
                HistoryTokenOffsetAndCount.EMPTY
            )
        );
    }

    @Test
    public void testParseReload() {
        this.parseAndCheck(
            "/plugin/*/reload",
            PluginListReloadHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY
            )
        );
    }

    @Test
    public void testParseOffset() {
        this.parseAndCheck(
            "/plugin/*/offset/10/reload",
            PluginListReloadHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY.setOffset(
                    OptionalInt.of(10)
                )
            )
        );
    }

    @Test
    public void testParseCount() {
        this.parseAndCheck(
            "/plugin/*/count/20/reload",
            PluginListReloadHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY.setCount(
                    OptionalInt.of(20)
                )
            )
        );
    }

    @Test
    public void testParseOffsetAndCount() {
        this.parseAndCheck(
            "/plugin/*/offset/10/count/20/reload",
            PluginListReloadHistoryToken.with(
                HistoryTokenOffsetAndCount.with(
                    OptionalInt.of(10), // offset
                    OptionalInt.of(20) // count
                )
            )
        );
    }

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck("/plugin/*/offset/1/count/23/reload");
    }

    @Test
    public void testUrlFragmentOffset() {
        this.urlFragmentAndCheck(
            PluginListReloadHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY.setOffset(
                    OptionalInt.of(10)
                )
            ),
            "/plugin/*/offset/10/reload"
        );
    }

    @Test
    public void testUrlFragmentOffsetAndCount() {
        this.urlFragmentAndCheck(
            PluginListReloadHistoryToken.with(
                HistoryTokenOffsetAndCount.with(
                    OptionalInt.of(10), // offset
                    OptionalInt.of(20) // count
                )
            ),
            "/plugin/*/offset/10/count/20/reload"
        );
    }

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.pluginListSelect(OFFSET_AND_COUNT)
        );
    }

    @Test
    public void testReload() {
        this.reloadAndCheck(
            this.createHistoryToken(),
            HistoryToken.pluginListReload(OFFSET_AND_COUNT)
        );
    }

    @Override
    PluginListReloadHistoryToken createHistoryToken(final HistoryTokenOffsetAndCount offsetAndCount) {
        return PluginListReloadHistoryToken.with(offsetAndCount);
    }

    // class............................................................................................................

    @Override
    public Class<PluginListReloadHistoryToken> type() {
        return PluginListReloadHistoryToken.class;
    }
}
