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

package walkingkooka.spreadsheet.dominokit.fetcher;

import org.junit.jupiter.api.Test;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.plugin.PluginName;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;

import java.util.Optional;

import static org.junit.Assert.assertThrows;

public final class PluginFetcherTest implements ClassTesting<PluginFetcher> {

    // pluginName.......................................................................................................

    @Test
    public void testPluginNameWithNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> PluginFetcher.pluginName(
                        null
                )
        );
    }

    @Test
    public void testPluginName() {
        this.checkEquals(
                Url.parseRelative("/api/plugin/TestPluginName123"),
                PluginFetcher.pluginName(
                        PluginName.with("TestPluginName123")
                )
        );
    }

    // pluginDownloadUrl................................................................................................

    @Test
    public void testPluginDownloadUrlWithNullPluginNameFails() {
        assertThrows(
                NullPointerException.class,
                () -> PluginFetcher.pluginDownloadUrl(
                        null,
                        Optional.empty()
                )
        );
    }

    @Test
    public void testPluginDownloadUrlWithNullFileNameFails() {
        assertThrows(
                NullPointerException.class,
                () -> PluginFetcher.pluginDownloadUrl(
                        PluginName.with("TestPlugin123"),
                        null
                )
        );
    }

    @Test
    public void testPluginDownloadUrlMissingFile() {
        this.pluginDownloadUrlAndCheck(
                "TestPluginName123",
                Optional.empty(),
                "/api/plugin/TestPluginName123/download"
        );
    }

    @Test
    public void testPluginDownloadUrlWithFilename() {
        this.pluginDownloadUrlAndCheck(
                "TestPluginName123",
                Optional.of(
                        JarEntryInfoName.MANIFEST_MF
                ),
                "/api/plugin/TestPluginName123/download/META-INF/MANIFEST.MF"
        );
    }

    @Test
    public void testPluginDownloadUrlWithFilename2() {
        this.pluginDownloadUrlAndCheck(
                "TestPluginName123",
                Optional.of(
                        JarEntryInfoName.with("/dir111/file222.txt")
                ),
                "/api/plugin/TestPluginName123/download/dir111/file222.txt"
        );
    }

    private void pluginDownloadUrlAndCheck(final String pluginName,
                                           final Optional<JarEntryInfoName> file,
                                           final String expected) {
        this.pluginDownloadUrlAndCheck(
                PluginName.with(pluginName),
                file,
                Url.parseRelative(expected)
        );
    }

    private void pluginDownloadUrlAndCheck(final PluginName pluginName,
                                           final Optional<JarEntryInfoName> file,
                                           final RelativeUrl expected) {
        this.checkEquals(
                expected,
                PluginFetcher.pluginDownloadUrl(
                        pluginName,
                        file
                ),
                () -> "pluginDownloadUrl " + pluginName + " " + file
        );
    }

    // class............................................................................................................

    @Override
    public Class<PluginFetcher> type() {
        return PluginFetcher.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
