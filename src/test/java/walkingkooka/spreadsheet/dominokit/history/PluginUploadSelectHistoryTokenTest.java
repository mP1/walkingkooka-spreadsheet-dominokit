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

public final class PluginUploadSelectHistoryTokenTest extends PluginHistoryTokenTestCase<PluginUploadSelectHistoryToken> {

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck("/plugin-upload");
    }

    // clearAction.....................................................................................................

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

    @Test
    public void testClose() {
        this.closeAndCheck(
            HistoryToken.pluginListSelect(
                OptionalInt.empty(), // offset
                OptionalInt.empty() // count
            )
        );
    }

    @Override
    PluginUploadSelectHistoryToken createHistoryToken() {
        return PluginUploadSelectHistoryToken.INSTANCE;
    }

    // class............................................................................................................

    @Override
    public Class<PluginUploadSelectHistoryToken> type() {
        return PluginUploadSelectHistoryToken.class;
    }
}
