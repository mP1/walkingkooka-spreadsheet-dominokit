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

public final class PluginListSelectHistoryTokenTest extends PluginListHistoryTokenTestCase<PluginListSelectHistoryToken> {

    // count............................................................................................................

    @Test
    public void testCount() {
        this.countAndCheck(
            PluginListSelectHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY
            )
        );
    }

    @Test
    public void testCount2() {
        final int count = 123;

        this.countAndCheck(
            PluginListSelectHistoryToken.with(
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
        final PluginListSelectHistoryToken historyToken = this.createHistoryToken();

        assertSame(
            historyToken,
            historyToken.setCount(historyToken.count())
        );
    }

    @Test
    public void testSetCountWithDifferent() {
        final OptionalInt count = OptionalInt.of(123);

        this.setCountAndCheck(
            PluginListSelectHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY
            ),
            count,
            PluginListSelectHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY.setCount(count)
            )
        );
    }

    // offset............................................................................................................

    @Test
    public void testOffset() {
        this.offsetAndCheck(
            PluginListSelectHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY
            )
        );
    }

    @Test
    public void testOffset2() {
        final int offset = 456;

        this.offsetAndCheck(
            PluginListSelectHistoryToken.with(
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
        final PluginListSelectHistoryToken historyToken = this.createHistoryToken();

        assertSame(
            historyToken,
            historyToken.setOffset(historyToken.offset())
        );
    }

    @Test
    public void testSetOffsetWithDifferent() {
        final OptionalInt offset = OptionalInt.of(456);

        this.setOffsetAndCheck(
            PluginListSelectHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY
            ),
            offset,
            PluginListSelectHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY.setOffset(offset)
            )
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseInvalidOffset() {
        this.parseAndCheck(
            "/plugin/*/offset/X",
            PluginListSelectHistoryToken.with(HistoryTokenOffsetAndCount.EMPTY)
        );
    }

    @Test
    public void testParseOffset() {
        this.parseAndCheck(
            "/plugin/*/offset/10",
            PluginListSelectHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY.setOffset(
                    OptionalInt.of(10)
                )
            )
        );
    }

    @Test
    public void testParseCount() {
        this.parseAndCheck(
            "/plugin/*/count/20",
            PluginListSelectHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY.setCount(
                    OptionalInt.of(20)
                )
            )
        );
    }

    @Test
    public void testParseOffsetAndCount() {
        this.parseAndCheck(
            "/plugin/*/offset/10/count/20",
            PluginListSelectHistoryToken.with(
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
        this.urlFragmentAndCheck(
            PluginListSelectHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY
            ),
            "/plugin");
    }

    @Test
    public void testUrlFragmentOffset() {
        this.urlFragmentAndCheck(
            PluginListSelectHistoryToken.with(
                HistoryTokenOffsetAndCount.EMPTY.setOffset(
                    OptionalInt.of(10)
                )
            ),
            "/plugin/*/offset/10"
        );
    }

    @Test
    public void testUrlFragmentOffsetAndCount() {
        this.urlFragmentAndCheck(
            PluginListSelectHistoryToken.with(
                HistoryTokenOffsetAndCount.with(
                    OptionalInt.of(10), // offset
                    OptionalInt.of(20) // count
                )
            ),
            "/plugin/*/offset/10/count/20"
        );
    }

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.spreadsheetListSelect(HistoryTokenOffsetAndCount.EMPTY)
        );
    }

    // close............................................................................................................

    @Test
    public void testClose() {
        this.closeAndCheck(
            HistoryToken.spreadsheetListSelect(HistoryTokenOffsetAndCount.EMPTY)
        );
    }

    // reload...........................................................................................................

    @Test
    public void testReload() {
        this.reloadAndCheck(
            this.createHistoryToken(),
            HistoryToken.pluginListReload(
                HistoryTokenOffsetAndCount.with(
                    OFFSET,
                    COUNT
                )
            )
        );
    }

    // helpers..........................................................................................................

    @Override
    PluginListSelectHistoryToken createHistoryToken(final HistoryTokenOffsetAndCount offsetAndCount) {
        return PluginListSelectHistoryToken.with(offsetAndCount);
    }

    // class............................................................................................................

    @Override
    public Class<PluginListSelectHistoryToken> type() {
        return PluginListSelectHistoryToken.class;
    }
}
