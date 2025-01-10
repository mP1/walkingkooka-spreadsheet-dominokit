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

import java.util.Optional;
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

    // setSaveValue.....................................................................................................

    @Test
    public void testSetSaveValueWithInvalidOptionalValueFails() {
        this.setSaveValueFails(
            Optional.of(this)
        );
    }

    @Test
    public void testSetSaveValueWithEmptyFails() {
        this.setSaveValueFails(Optional.empty());
    }

    @Test
    public void testSetSaveValueWithNotEmpty() {
        final BrowserFile file = BrowserFile.base64(
            "Different",
            "FileContent456"
        );

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(file),
            HistoryToken.pluginUploadSave(file)
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
