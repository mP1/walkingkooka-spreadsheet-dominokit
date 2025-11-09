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

import walkingkooka.plugin.PluginName;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;

import java.util.Optional;

/**
 * The event payload used by {@link PluginFetcherWatchers}.
 */
final class PluginFetcherWatchersJarEntryInfoNameEvent extends FetcherWatchersEvent<PluginFetcherWatcher> {

    static PluginFetcherWatchersJarEntryInfoNameEvent with(final PluginName pluginName,
                                                           final Optional<JarEntryInfoName> filename,
                                                           final Optional<String> body) {
        return new PluginFetcherWatchersJarEntryInfoNameEvent(
            pluginName,
            filename,
            body
        );
    }

    private PluginFetcherWatchersJarEntryInfoNameEvent(final PluginName pluginName,
                                                       final Optional<JarEntryInfoName> filename,
                                                       final Optional<String> body) {
        super();
        this.pluginName = pluginName;
        this.filename = filename;
        this.body = body;
    }

    @Override
    void fire(final PluginFetcherWatcher watcher) {
        watcher.onJarEntryInfoName(
            this.pluginName,
            this.filename,
            this.body
        );
    }

    private final PluginName pluginName;

    private final Optional<JarEntryInfoName> filename;

    private final Optional<String> body;

    @Override
    public String toString() {
        return this.pluginName + " " + this.filename + " " + this.body.orElse("");
    }
}
