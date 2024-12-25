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

import walkingkooka.plugin.PluginName;
import walkingkooka.spreadsheet.dominokit.dialog.FakeSpreadsheetDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcherWatcher;

import java.util.Locale;

public class FakeJarEntryInfoListDialogComponentContext extends FakeSpreadsheetDialogComponentContext implements JarEntryInfoListDialogComponentContext {

    @Override
    public Runnable addPluginFetcherWatcher(final PluginFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addPluginFetcherWatcherOnce(final PluginFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void listJarEntries(final PluginName pluginName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PluginName pluginName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Locale locale() {
        throw new UnsupportedOperationException();
    }
}