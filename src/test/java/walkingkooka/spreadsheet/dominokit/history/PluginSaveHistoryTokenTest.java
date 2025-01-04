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
import walkingkooka.plugin.PluginName;

import java.util.OptionalInt;

import static org.junit.Assert.assertThrows;

public final class PluginSaveHistoryTokenTest extends PluginNameHistoryTokenTestCase<PluginSaveHistoryToken> {

    // with.............................................................................................................

    @Test
    public void testWithNullSaveFails() {
        assertThrows(
                NullPointerException.class,
                () -> PluginSaveHistoryToken.with(
                        PLUGIN_NAME,
                        null
                )
        );
    }

    // HasUrlFragment...................................................................................................

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck("/plugin/TestPluginName123/save/value123");
    }

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
                this.createHistoryToken(),
                HistoryToken.pluginListSelect(
                        OptionalInt.empty(), // offset
                        OptionalInt.empty() // count
                )
        );
    }

    @Override
    PluginSaveHistoryToken createHistoryToken(final PluginName name) {
        return PluginSaveHistoryToken.with(
                name,
                "value123"
        );
    }

    // class............................................................................................................

    @Override
    public Class<PluginSaveHistoryToken> type() {
        return PluginSaveHistoryToken.class;
    }
}