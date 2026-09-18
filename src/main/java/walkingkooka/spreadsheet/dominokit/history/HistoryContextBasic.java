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

package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.spreadsheet.dominokit.AppContext;

import java.util.Objects;

final class HistoryContextBasic implements HistoryContext {

    static HistoryContextBasic with(final HistoryToken historyToken,
                                    final AppContext context) {
        return new HistoryContextBasic(
            Objects.requireNonNull(historyToken, "historyToken"),
            Objects.requireNonNull(context, "context")
        );
    }

    private HistoryContextBasic(final HistoryToken historyToken,
                                final AppContext context) {
        super();

        this.historyToken = historyToken;
        this.context = context;
    }

    @Override
    public HistoryToken historyToken() {
        return this.historyToken;
    }

    private HistoryToken historyToken;

    @Override
    public void pushHistoryToken(final HistoryToken token) {
        Objects.requireNonNull(token, "token");

        final HistoryToken prev = this.historyToken;
        this.historyToken = token;

        this.watchers.onHistoryTokenChange(
            prev,
            this.context
        );
    }

    @Override
    public Runnable addHistoryWatcher(HistoryWatcher watcher) {
        return this.watchers.add(watcher);
    }

    @Override
    public Runnable addHistoryWatcherOnce(final HistoryWatcher watcher) {
        return this.watchers.addOnce(watcher);
    }

    @Override
    public void fireCurrentHistoryToken() {
        this.watchers.onHistoryTokenChange(
            this.historyToken,
            this.context
        );
    }

    private final HistoryWatchers watchers = HistoryWatchers.empty();

    private final AppContext context;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.historyToken.toString();
    }
}
