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

import walkingkooka.ToStringBuilder;
import walkingkooka.net.UrlPath;
import walkingkooka.plugin.PluginName;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;

import java.util.Objects;
import java.util.Optional;

public final class PluginDownload {

    public static PluginDownload extract(final UrlPath path) {
        Objects.requireNonNull(path, "path");

        return PluginDownload.with(
                PluginName.with(
                        path.namesList()
                                .get(3)
                                .value()
                ),
                JarEntryInfoName.pluginDownloadPathExtract(path)
        );
    }

    public static PluginDownload with(final PluginName pluginName,
                                      final Optional<JarEntryInfoName> filename) {
        return new PluginDownload(
                Objects.requireNonNull(pluginName, "pluginName"),
                Objects.requireNonNull(filename, "filename")
        );
    }

    private PluginDownload(final PluginName pluginName,
                           final Optional<JarEntryInfoName> filename) {
        this.pluginName = pluginName;
        this.filename = filename;
    }

    public PluginName pluginName() {
        return this.pluginName;
    }

    private final PluginName pluginName;

    public Optional<JarEntryInfoName> filename() {
        return this.filename;
    }

    private final Optional<JarEntryInfoName> filename;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
                this.pluginName,
                this.filename
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof PluginDownload && this.equals0((PluginDownload) other);
    }

    private boolean equals0(final PluginDownload other) {
        return this.pluginName.equals(other.pluginName) &&
                this.filename.equals(other.filename);
    }

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .value(this.pluginName)
                .value(this.filename)
                .build();
    }
}
