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

package walkingkooka.spreadsheet.dominokit.plugin;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentTesting;
import walkingkooka.spreadsheet.dominokit.file.BrowserFile;

import java.util.Optional;

public final class PluginUploadSaveAnchorComponentTest implements AnchorComponentTesting<PluginUploadSaveAnchorComponent, BrowserFile> {

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            PluginUploadSaveAnchorComponent.empty("lost")
                .setId("Upload123")
                .setTextContent("Upload123!"),
            "\"Upload123!\" DISABLED id=Upload123"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            PluginUploadSaveAnchorComponent.empty("lost")
                .setId("Upload456")
                .setTextContent("Upload456!")
                .setValue(
                    Optional.of(
                        BrowserFile.base64(
                            "Filename123",
                            "Filecontent456"
                        )
                    )
                ),
            "\"Upload456!\" [#/plugin-upload/save/base64/Filename123/Filecontent456] id=Upload456"
        );
    }

    @Test
    public void testSetValueClearValue() {
        this.treePrintAndCheck(
            PluginUploadSaveAnchorComponent.empty("lost")
                .setId("Upload789")
                .setTextContent("Upload789!")
                .setValue(
                    Optional.of(
                        BrowserFile.base64(
                            "Filename123",
                            "Filecontent456"
                        )
                    )
                ).clearValue(),
            "\"Upload789!\" DISABLED id=Upload789"
        );
    }

    @Override
    public PluginUploadSaveAnchorComponent createComponent() {
        return PluginUploadSaveAnchorComponent.empty("test-999");
    }

    // class............................................................................................................

    @Override
    public Class<PluginUploadSaveAnchorComponent> type() {
        return PluginUploadSaveAnchorComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
