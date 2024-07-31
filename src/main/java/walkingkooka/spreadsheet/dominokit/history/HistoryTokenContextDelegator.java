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

public interface HistoryTokenContextDelegator extends HistoryTokenContext {

    @Override
    default Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
        return this.historyTokenContext()
                .addHistoryTokenWatcher(watcher);
    }

    @Override
    default Runnable addHistoryTokenWatcherOnce(final HistoryTokenWatcher watcher) {
        return this.historyTokenContext()
                .addHistoryTokenWatcherOnce(watcher);
    }

    @Override
    default HistoryToken historyToken() {
        return this.historyTokenContext()
                .historyToken();
    }

    @Override
    default void pushHistoryToken(final HistoryToken token) {
        this.historyTokenContext()
                .pushHistoryToken(token);
    }

    @Override
    default void fireCurrentHistoryToken() {
        this.historyTokenContext()
                .fireCurrentHistoryToken();
    }

    HistoryTokenContext historyTokenContext();
}
