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

package walkingkooka.spreadsheet.dominokit.format;

import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.selector.AppendPluginSelectorTextComponentContext;
import walkingkooka.spreadsheet.dominokit.selector.RemoveOrReplacePluginSelectorTextComponentContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterName;

final class SpreadsheetFormatterSelectorAppendPluginSelectorTextComponentContextRemoveOrReplacePluginSelectorTextComponentContext implements AppendPluginSelectorTextComponentContext,
        RemoveOrReplacePluginSelectorTextComponentContext {

    static SpreadsheetFormatterSelectorAppendPluginSelectorTextComponentContextRemoveOrReplacePluginSelectorTextComponentContext with(final SpreadsheetFormatterName name,
                                                                                                                                      final HistoryTokenContext context) {
        return new SpreadsheetFormatterSelectorAppendPluginSelectorTextComponentContextRemoveOrReplacePluginSelectorTextComponentContext(
                name,
                context
        );
    }

    private SpreadsheetFormatterSelectorAppendPluginSelectorTextComponentContextRemoveOrReplacePluginSelectorTextComponentContext(final SpreadsheetFormatterName name,
                                                                                                                                  final HistoryTokenContext context) {
        this.name = name;
        this.context = context;
    }

    @Override
    public String saveText(final String text) {
        final SpreadsheetFormatterName name = this.name;

        return null == name ?
                text :
                name.setText(text)
                        .toString();
    }

    private final SpreadsheetFormatterName name;

    // HistoryTokenContext..............................................................................................

    @Override
    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
        return this.context.addHistoryTokenWatcher(watcher);
    }

    @Override
    public Runnable addHistoryTokenWatcherOnce(final HistoryTokenWatcher watcher) {
        return this.context.addHistoryTokenWatcherOnce(watcher);
    }

    @Override
    public HistoryToken historyToken() {
        return this.context.historyToken();
    }

    @Override
    public void pushHistoryToken(final HistoryToken token) {
        this.context.pushHistoryToken(token);
    }

    @Override
    public void fireCurrentHistoryToken() {
        this.context.fireCurrentHistoryToken();
    }

    private final HistoryTokenContext context;
}
