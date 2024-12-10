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
import walkingkooka.plugin.store.Plugin;
import walkingkooka.spreadsheet.dominokit.AppContext;

import java.util.Optional;

/**
 * The event payload used by {@link PluginFetcherWatchers}.
 */
final class PluginFetcherWatchersPluginEvent extends FetcherWatchersEvent<PluginFetcherWatcher> {

    static PluginFetcherWatchersPluginEvent with(final PluginName name,
                                                 final Optional<Plugin> plugin,
                                                 final AppContext context) {
        return new PluginFetcherWatchersPluginEvent(
                name,
                plugin,
                context
        );
    }

    private PluginFetcherWatchersPluginEvent(final PluginName name,
                                             final Optional<Plugin> plugin,
                                             final AppContext context) {
        super(context);
        this.name = name;
        this.plugin = plugin;
    }

    @Override
    void fire(final PluginFetcherWatcher watcher) {
        watcher.onPlugin(
                this.name,
                this.plugin,
                this.context
        );
    }

    private final PluginName name;

    private final Optional<Plugin> plugin;

    @Override
    public String toString() {
        return this.plugin + " " + this.context;
    }
}
