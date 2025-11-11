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

import walkingkooka.net.UrlFragment;
import walkingkooka.plugin.PluginName;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents the viewing of a file within or the plugin archive.
 */
public final class PluginFileViewHistoryToken extends PluginNameHistoryToken {

    static PluginFileViewHistoryToken with(final PluginName name,
                                           final Optional<JarEntryInfoName> file) {
        return new PluginFileViewHistoryToken(
            Objects.requireNonNull(name, "name"),
            Objects.requireNonNull(file, "file")
        );
    }

    private PluginFileViewHistoryToken(final PluginName name,
                                       final Optional<JarEntryInfoName> file) {
        super(name);
        this.file = file;
    }

    public Optional<JarEntryInfoName> file() {
        return this.file;
    }

    public PluginFileViewHistoryToken setFile(final Optional<JarEntryInfoName> file) {
        Objects.requireNonNull(file, "file");

        return this.file.equals(file) ?
            this :
            new PluginFileViewHistoryToken(
                this.name,
                file
            );
    }

    private final Optional<JarEntryInfoName> file;

    // HistoryToken.....................................................................................................

    //
    // /plugin/PluginName123/file/dir1/file2.txt
    //
    @Override
    UrlFragment pluginNameUrlFragment() {
        return FILE.append(
            this.file.map(f -> UrlFragment.parse(f.value()))
                .orElse(UrlFragment.EMPTY)
        );
    }

    @Override
    public HistoryToken clearAction() {
        return pluginSelect(this.name);
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitPluginFileView(
            this.name,
            this.file
        );
    }
}
