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
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoList;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;

import java.util.Optional;

/**
 * A watcher that receives all {@link PluginSet} response events.
 */
public interface PluginFetcherWatcher extends FetcherWatcher {

    void onJarEntryInfoList(final PluginName name,
                            final Optional<JarEntryInfoList> list,
                            final AppContext context);

    void onJarEntryInfoName(final PluginName pluginName,
                            final Optional<JarEntryInfoName> filename,
                            final Optional<String> body,
                            final AppContext context);

    void onPlugin(final PluginName name,
                  final Optional<Plugin> plugin,
                  final AppContext context);


    void onPluginSet(final PluginSet plugins,
                     final AppContext context);
}
