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
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PluginFileViewHistoryTokenTest extends PluginNameHistoryTokenTestCase<PluginFileViewHistoryToken> {

    private final static Optional<JarEntryInfoName> FILE = Optional.of(
        JarEntryInfoName.with("/dir1/file2.txt")
    );

    // with.............................................................................................................

    @Test
    public void testWithNullFileFails() {
        assertThrows(
            NullPointerException.class,
            () -> PluginFileViewHistoryToken.with(
                PLUGIN_NAME,
                null
            )
        );
    }

    // parse............................................................................................................
    @Test
    public void testParseWithFile() {
        this.parseAndCheck(
            "/plugin/test-plugin-name-123/file/dir1/file2.txt",
            this.createHistoryToken()
        );
    }

    @Test
    public void testParseWithoutFile() {
        this.parseAndCheck(
            "/plugin/test-plugin-name-123/file",
            PluginFileViewHistoryToken.with(
                PLUGIN_NAME,
                Optional.empty()
            )
        );
    }

    // UrlFragment......................................................................................................

    @Test
    public void testUrlFragmentWithFile() {
        this.urlFragmentAndCheck(
            "/plugin/test-plugin-name-123/file/dir1/file2.txt"
        );
    }

    @Test
    public void testUrlFragmentWithoutFile() {
        this.urlFragmentAndCheck(
            PluginFileViewHistoryToken.with(
                PLUGIN_NAME,
                Optional.empty()
            ),
            "/plugin/test-plugin-name-123/file");
    }

    // clearAction.....................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.pluginSelect(PLUGIN_NAME)
        );
    }

    @Test
    public void testClose() {
        this.closeAndCheck(
            HistoryToken.pluginSelect(PLUGIN_NAME)
        );
    }

    @Override
    PluginFileViewHistoryToken createHistoryToken(final PluginName name) {
        return PluginFileViewHistoryToken.with(
            name,
            FILE
        );
    }

    // class............................................................................................................

    @Override
    public Class<PluginFileViewHistoryToken> type() {
        return PluginFileViewHistoryToken.class;
    }
}
