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

package walkingkooka.spreadsheet.dominokit.navigate;

import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherTesting;
import walkingkooka.spreadsheet.dominokit.history.FakeHistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;

import java.util.Objects;

public abstract class HistoryTokenSpreadsheetNavigateComponentContextTestCase<C extends HistoryTokenSpreadsheetNavigateComponentContext>
    implements SpreadsheetNavigateComponentContextTesting<C>,
    ComponentLifecycleMatcherTesting {

    @Override
    public final C createContext() {
        return this.createContext(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            )
        );
    }

    public C createContext(final HistoryToken historyToken) {
        return this.createContext(
            new FakeHistoryContext() {
                @Override
                public HistoryToken historyToken() {
                    return historyToken;
                }

                @Override
                public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                    Objects.requireNonNull(watcher, "watcher");
                    throw new UnsupportedOperationException();
                }

                @Override
                public Runnable addHistoryTokenWatcherOnce(final HistoryTokenWatcher watcher) {
                    Objects.requireNonNull(watcher, "watcher");
                    throw new UnsupportedOperationException();
                }

                @Override
                public void pushHistoryToken(final HistoryToken token) {
                    Objects.requireNonNull(token, "token");
                    throw new UnsupportedOperationException();
                }
            }
        );
    }

    abstract C createContext(final HistoryContext historyContext);

    @Override
    public final String typeNamePrefix() {
        return HistoryTokenSpreadsheetNavigateComponentContext.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
