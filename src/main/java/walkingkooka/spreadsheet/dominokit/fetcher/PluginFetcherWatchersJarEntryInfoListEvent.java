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
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoList;

import java.util.Optional;

/**
 * The event payload used by {@link PluginFetcherWatchers}.
 */
final class PluginFetcherWatchersJarEntryInfoListEvent extends FetcherWatchersEvent<PluginFetcherWatcher> {

    static PluginFetcherWatchersJarEntryInfoListEvent with(final PluginName name,
                                                           final Optional<JarEntryInfoList> list) {
        return new PluginFetcherWatchersJarEntryInfoListEvent(
            name,
            list
        );
    }

    private PluginFetcherWatchersJarEntryInfoListEvent(final PluginName name,
                                                       final Optional<JarEntryInfoList> list) {
        super();
        this.name = name;
        this.list = list;
    }

    @Override
    void fire(final PluginFetcherWatcher watcher) {
        watcher.onJarEntryInfoList(
            this.name,
            this.list
        );
    }

    private final PluginName name;

    private final Optional<JarEntryInfoList> list;

    @Override
    public String toString() {
        return this.name + " " + this.list;
    }
}
