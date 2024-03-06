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

public class FakeHistoryTokenContext implements HistoryTokenContext {
    @Override
    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addHistoryTokenWatcherOnce(final HistoryTokenWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HistoryToken historyToken() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void pushHistoryToken(final HistoryToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void fireCurrentHistoryToken() {
        throw new UnsupportedOperationException();
    }
}
