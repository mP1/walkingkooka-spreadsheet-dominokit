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

package walkingkooka.spreadsheet.dominokit.ui.spreadsheetname;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;

abstract class BasicSpreadsheetNameDialogComponentContext implements SpreadsheetNameDialogComponentContext {

    BasicSpreadsheetNameDialogComponentContext(final AppContext context) {
        this.context = context;
    }

    @Override
    public final Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
        return this.context.addHistoryTokenWatcher(watcher);
    }

    @Override
    public final Runnable addHistoryTokenWatcherOnce(final HistoryTokenWatcher watcher) {
        return this.context.addHistoryTokenWatcherOnce(watcher);
    }

    @Override
    public final HistoryToken historyToken() {
        return this.context.historyToken();
    }

    @Override
    public final void pushHistoryToken(final HistoryToken token) {
        this.context.pushHistoryToken(token);
    }

    @Override
    public final void fireCurrentHistoryToken() {
        this.context.fireCurrentHistoryToken();
    }

    @Override
    public final void debug(final Object... values) {
        this.context.debug(values);
    }

    @Override
    public final void error(final Object... values) {
        this.context.error(values);
    }

    private final AppContext context;
}
