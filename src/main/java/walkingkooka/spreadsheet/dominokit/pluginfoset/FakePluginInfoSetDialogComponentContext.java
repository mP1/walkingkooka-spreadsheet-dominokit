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

package walkingkooka.spreadsheet.dominokit.pluginfoset;

import walkingkooka.naming.Name;
import walkingkooka.plugin.PluginInfoLike;
import walkingkooka.plugin.PluginInfoSetLike;
import walkingkooka.spreadsheet.dominokit.history.FakeHistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBoxWrapper;

import java.util.function.Consumer;

public class FakePluginInfoSetDialogComponentContext<N extends Name & Comparable<N>, I extends PluginInfoLike<I, N>, S extends PluginInfoSetLike<S, I, N>> extends FakeHistoryTokenContext
        implements PluginInfoSetDialogComponentContext<N, I, S> {
    @Override
    public String dialogTitle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValueSpreadsheetTextBoxWrapper textBox() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addProviderFetcherWatcher(final Consumer<S> set) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void loadProviderInfoSet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    public S parse(final String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public S emptyInfoSet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public S metadataInfoSet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public S providerInfoSet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void giveFocus(final Runnable focus) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void debug(final Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void error(final Object... values) {
        throw new UnsupportedOperationException();
    }
}
