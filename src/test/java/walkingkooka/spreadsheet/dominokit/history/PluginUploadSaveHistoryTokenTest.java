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
import walkingkooka.spreadsheet.dominokit.file.BrowserFile;

public final class PluginUploadSaveHistoryTokenTest extends PluginHistoryTokenTestCase<PluginUploadSaveHistoryToken> {

    // parse............................................................................................................

    @Test
    public void testParseSaveMissingType() {
        this.parseAndCheck(
                "/plugin-upload/save/",
                HistoryToken.pluginUploadSelect()
        );
    }

    @Test
    public void testParseNotBase64() {
        this.parseAndCheck(
                "/plugin-upload/save/???/Filename123",
                HistoryToken.pluginUploadSelect()
        );
    }

    @Test
    public void testParseWithoutFileContent() {
        this.parseAndCheck(
                "/plugin-upload/save/base64/Filename123",
                PluginUploadSaveHistoryToken.with(
                        BrowserFile.base64("Filename123", "")
                )
        );
    }

    @Test
    public void testParseWithEmptyFileContent() {
        this.parseAndCheck(
                "/plugin-upload/save/base64/Filename123/",
                PluginUploadSaveHistoryToken.with(
                        BrowserFile.base64("Filename123", "")
                )
        );
    }

    @Test
    public void testParseWithFileContent() {
        this.parseAndCheck(
                "/plugin-upload/save/base64/Filename123/FileContent456",
                PluginUploadSaveHistoryToken.with(
                        BrowserFile.base64("Filename123", "FileContent456")
                )
        );
    }

    // urlFragment......................................................................................................

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck("/plugin-upload/save/base64/Filename123/FileContent456");
    }

    @Test
    public void testUrlFragmentWithEmptyFileContent() {
        this.urlFragmentAndCheck(
                PluginUploadSaveHistoryToken.with(
                        BrowserFile.base64("Filename123", "")
                ),
                "/plugin-upload/save/base64/Filename123");
    }

    // clearAction.....................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
                this.createHistoryToken(),
                HistoryToken.pluginUploadSelect()
        );
    }

    @Test
    public void testClose() {
        this.closeAndCheck(
                HistoryToken.pluginUploadSelect()
        );
    }

    @Override
    PluginUploadSaveHistoryToken createHistoryToken() {
        return PluginUploadSaveHistoryToken.with(
                BrowserFile.base64(
                        "Filename123",
                        "FileContent456"
                )
        );
    }

    // class............................................................................................................

    @Override
    public Class<PluginUploadSaveHistoryToken> type() {
        return PluginUploadSaveHistoryToken.class;
    }
}
