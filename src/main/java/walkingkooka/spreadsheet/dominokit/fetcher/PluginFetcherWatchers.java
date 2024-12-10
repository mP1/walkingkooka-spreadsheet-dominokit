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
import walkingkooka.plugin.store.PluginSet;
import walkingkooka.spreadsheet.dominokit.AppContext;

import java.util.Optional;

public final class PluginFetcherWatchers extends FetcherWatchers<PluginFetcherWatcher>
        implements PluginFetcherWatcher {

    private PluginFetcherWatchers() {
        super();
    }

    public static PluginFetcherWatchers empty() {
        return new PluginFetcherWatchers();
    }

    @Override
    public void onPlugin(final PluginName name,
                         final Optional<Plugin> plugin,
                         final AppContext context) {
        this.fire(
                PluginFetcherWatchersPluginEvent.with(
                        name,
                        plugin,
                        context
                )
        );
    }

    @Override
    public void onPluginSet(final PluginSet plugins,
                            final AppContext context) {
        this.fire(
                PluginFetcherWatchersPluginSetEvent.with(
                        plugins,
                        context
                )
        );
    }
}
