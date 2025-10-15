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
import walkingkooka.plugin.PluginName;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorComponentTesting;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;

import java.util.Optional;

public final class PluginDownloadAnchorComponentTest implements AnchorComponentTesting<PluginDownloadAnchorComponent, PluginDownload> {

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            PluginDownloadAnchorComponent.empty("Download123")
                .setTextContent("Download123!"),
            "\"Download123!\" DISABLED id=Download123"
        );
    }

    @Test
    public void testSetValueMissingFilename() {
        this.treePrintAndCheck(
            PluginDownloadAnchorComponent.empty("Download456")
                .setTextContent("Download456!")
                .setValue(
                    Optional.of(
                        PluginDownload.with(
                            PluginName.with("plugin-name-456"),
                            Optional.empty()
                        )
                    )
                ),
            "\"Download456!\" [/api/plugin/plugin-name-456/download] id=Download456"
        );
    }

    @Test
    public void testSetValueWithFilename() {
        this.treePrintAndCheck(
            PluginDownloadAnchorComponent.empty("Download789")
                .setTextContent("Download789!")
                .setValue(
                    Optional.of(
                        PluginDownload.with(
                            PluginName.with("plugin-name-789"),
                            Optional.of(
                                JarEntryInfoName.MANIFEST_MF
                            )
                        )
                    )
                ),
            "\"Download789!\" [/api/plugin/plugin-name-789/download/META-INF/MANIFEST.MF] id=Download789"
        );
    }

    @Override
    public PluginDownloadAnchorComponent createComponent() {
        return PluginDownloadAnchorComponent.empty("Test999");
    }

    // class............................................................................................................

    @Override
    public Class<PluginDownloadAnchorComponent> type() {
        return PluginDownloadAnchorComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
