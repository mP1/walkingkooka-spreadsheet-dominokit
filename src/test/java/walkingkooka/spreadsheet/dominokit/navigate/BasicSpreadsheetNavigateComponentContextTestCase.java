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

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherTesting;
import walkingkooka.spreadsheet.dominokit.history.FakeHistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContexts;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class BasicSpreadsheetNavigateComponentContextTestCase<C extends BasicSpreadsheetNavigateComponentContext>
    implements SpreadsheetNavigateComponentContextTesting<C>,
    ComponentLifecycleMatcherTesting {

    @Test
    public final void testWithNullHistoryContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext(
                null,
                LoggingContexts.fake()
            )
        );
    }

    @Test
    public final void testWithNullLoggingContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext(
                HistoryContexts.fake(),
                null
            )
        );
    }

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
            },
            LoggingContexts.fake()
        );
    }

    final C createContext(final HistoryContext historyContext) {
        return this.createContext(
            historyContext,
            LoggingContexts.fake()
        );
    }

    abstract C createContext(final HistoryContext historyContext,
                             final LoggingContext loggingContext);

    @Override
    public final String typeNamePrefix() {
        return BasicSpreadsheetNavigateComponentContext.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
