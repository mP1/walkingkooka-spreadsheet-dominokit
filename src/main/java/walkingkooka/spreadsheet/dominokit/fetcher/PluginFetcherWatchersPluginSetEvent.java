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

import walkingkooka.plugin.store.PluginSet;

/**
 * The event payload used by {@link PluginFetcherWatchers}.
 */
final class PluginFetcherWatchersPluginSetEvent extends FetcherWatchersEvent<PluginFetcherWatcher> {

    static PluginFetcherWatchersPluginSetEvent with(final PluginSet plugins) {
        return new PluginFetcherWatchersPluginSetEvent(plugins);
    }

    private PluginFetcherWatchersPluginSetEvent(final PluginSet plugins) {
        super();
        this.plugins = plugins;
    }

    @Override
    void fire(final PluginFetcherWatcher watcher) {
        watcher.onPluginSet(this.plugins);
    }

    private final PluginSet plugins;

    @Override
    public String toString() {
        return this.plugins.toString();
    }
}
