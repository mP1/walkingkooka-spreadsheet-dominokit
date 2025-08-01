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

public final class PluginListReloadHistoryTokenTest extends PluginListHistoryTokenTestCase<PluginListReloadHistoryToken> {

    // count............................................................................................................

    @Test
    public void testCount() {
        this.countAndCheck(
            PluginListReloadHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY
            )
        );
    }

    @Test
    public void testCount2() {
        final int count = 123;

        this.countAndCheck(
            PluginListReloadHistoryToken.with(
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
        final PluginListReloadHistoryToken historyToken = this.createHistoryToken();

        assertSame(
            historyToken,
            historyToken.setCount(historyToken.count())
        );
    }

    @Test
    public void testSetCountWithDifferent() {
        final OptionalInt count = OptionalInt.of(123);

        this.setCountAndCheck(
            PluginListReloadHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY
            ),
            count,
            PluginListReloadHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY.setCount(count)
            )
        );
    }

    // offset............................................................................................................

    @Test
    public void testOffset() {
        this.offsetAndCheck(
            PluginListReloadHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY
            )
        );
    }

    @Test
    public void testOffset2() {
        final int offset = 456;

        this.offsetAndCheck(
            PluginListReloadHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY.setOffset(
                    OptionalInt.of(offset)
                )
            ),
            offset
        );
    }

    // setOffset.........................................................................................................

    @Test
    public void testSetOffsetWithSame() {
        final PluginListReloadHistoryToken historyToken = this.createHistoryToken();

        assertSame(
            historyToken,
            historyToken.setOffset(historyToken.offset())
        );
    }

    @Test
    public void testSetOffsetWithDifferent() {
        final OptionalInt offset = OptionalInt.of(456);

        this.setOffsetAndCheck(
            PluginListReloadHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY
            ),
            offset,
            PluginListReloadHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY.setOffset(offset)
            )
        );
    }

    // parse............................................................................................................

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

    // urlFragment......................................................................................................

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

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.pluginListSelect(OFFSET_AND_COUNT)
        );
    }

    // reload...........................................................................................................

    @Test
    public void testReload() {
        this.reloadAndCheck(
            this.createHistoryToken(),
            HistoryToken.pluginListReload(OFFSET_AND_COUNT)
        );
    }

    // helpers..........................................................................................................

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
