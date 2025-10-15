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
import walkingkooka.net.UrlPath;
import walkingkooka.plugin.PluginName;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PluginDownloadTest implements ClassTesting<PluginDownload> {

    @Test
    public void testExtractWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> PluginDownload.extract(null)
        );
    }

    @Test
    public void testExtract() {
        this.checkEquals(
            PluginDownload.with(
                PluginName.with("test-plugin-123"),
                Optional.of(
                    JarEntryInfoName.MANIFEST_MF
                )
            ),
            PluginDownload.extract(
                UrlPath.parse("/api/plugin/test-plugin-123/download/META-INF/MANIFEST.MF")
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<PluginDownload> type() {
        return PluginDownload.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
