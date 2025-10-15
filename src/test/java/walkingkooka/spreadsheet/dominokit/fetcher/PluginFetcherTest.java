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

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PluginFetcherTest implements ClassTesting<PluginFetcher> {

    // url..............................................................................................................

    @Test
    public void testUrlWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> PluginFetcher.url(
                null
            )
        );
    }

    @Test
    public void testUrl() {
        this.checkEquals(
            Url.parseRelative("/api/plugin/test-plugin-name-123"),
            PluginFetcher.url(
                PluginName.with("test-plugin-name-123")
            )
        );
    }

    // downloadUrl................................................................................................

    @Test
    public void testPluginDownloadUrlWithNullPluginNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> PluginFetcher.downloadUrl(
                null,
                Optional.empty()
            )
        );
    }

    @Test
    public void testDownloadUrlWithNullFileNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> PluginFetcher.downloadUrl(
                PluginName.with("test-plugin-123"),
                null
            )
        );
    }

    @Test
    public void testDownloadUrlMissingFile() {
        this.downloadUrlAndCheck(
            "test-plugin-name-123",
            Optional.empty(),
            "/api/plugin/test-plugin-name-123/download"
        );
    }

    @Test
    public void testDownloadUrlWithFilename() {
        this.downloadUrlAndCheck(
            "test-plugin-name-123",
            Optional.of(
                JarEntryInfoName.MANIFEST_MF
            ),
            "/api/plugin/test-plugin-name-123/download/META-INF/MANIFEST.MF"
        );
    }

    @Test
    public void testDownloadUrlWithFilename2() {
        this.downloadUrlAndCheck(
            "test-plugin-name-123",
            Optional.of(
                JarEntryInfoName.with("/dir111/file222.txt")
            ),
            "/api/plugin/test-plugin-name-123/download/dir111/file222.txt"
        );
    }

    private void downloadUrlAndCheck(final String pluginName,
                                     final Optional<JarEntryInfoName> file,
                                     final String expected) {
        this.downloadUrlAndCheck(
            PluginName.with(pluginName),
            file,
            Url.parseRelative(expected)
        );
    }

    private void downloadUrlAndCheck(final PluginName pluginName,
                                     final Optional<JarEntryInfoName> file,
                                     final RelativeUrl expected) {
        this.checkEquals(
            expected,
            PluginFetcher.downloadUrl(
                pluginName,
                file
            ),
            () -> "downloadUrl " + pluginName + " " + file
        );
    }

    // uploadUrl..................................................................................................

    @Test
    public void testUploadUrl() {
        this.checkEquals(
            Url.parseRelative("/api/plugin/*/upload"),
            PluginFetcher.uploadUrl()
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
