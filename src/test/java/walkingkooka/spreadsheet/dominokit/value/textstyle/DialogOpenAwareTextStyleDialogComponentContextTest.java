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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryWatchers;

public final class DialogOpenAwareTextStyleDialogComponentContextTest implements TextStyleDialogComponentContextTesting<DialogOpenAwareTextStyleDialogComponentContext> {

    @Test
    public void testAddHistoryWatcherOnHistoryTokenDialogOpen() {
        this.fired = false;

        final DialogOpenAwareTextStyleDialogComponentContext context = this.createContext(true);
        context.addHistoryWatcher(
            (final HistoryToken previous,
             final AppContext appContext) -> DialogOpenAwareTextStyleDialogComponentContextTest.this.fired = true
        );
        context.fireCurrentHistoryToken();

        this.checkEquals(
            true,
            this.fired
        );
    }

    @Test
    public void testAddHistoryWatcherOnHistoryTokenDialogClose() {
        final DialogOpenAwareTextStyleDialogComponentContext context = this.createContext(false);
        context.addHistoryWatcher(
            (final HistoryToken previous,
             final AppContext appContext) -> {
                throw new UnsupportedOperationException();
            }
        );
        context.fireCurrentHistoryToken();
    }

    @Test
    public void testAddHistoryWatcherOnceOnHistoryTokenDialogOpen() {
        this.fired = false;

        final DialogOpenAwareTextStyleDialogComponentContext context = this.createContext(true);
        context.addHistoryWatcherOnce(
            (final HistoryToken previous,
             final AppContext appContext) -> DialogOpenAwareTextStyleDialogComponentContextTest.this.fired = true
        );
        context.fireCurrentHistoryToken();

        this.checkEquals(
            true,
            this.fired
        );
    }

    @Test
    public void testAddHistoryWatcherOnceOnHistoryTokenDialogClose() {
        final DialogOpenAwareTextStyleDialogComponentContext context = this.createContext(false);
        context.addHistoryWatcherOnce(
            (final HistoryToken previous,
             final AppContext appContext) -> {
                throw new UnsupportedOperationException();
            }
        );
        context.fireCurrentHistoryToken();
    }

    private boolean fired;

    @Override
    public void testPushHistoryTokenWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DialogOpenAwareTextStyleDialogComponentContext createContext() {
        return this.createContext(true);
    }

    private DialogOpenAwareTextStyleDialogComponentContext createContext(final boolean isDialogOpen) {
        return DialogOpenAwareTextStyleDialogComponentContext.with(
            () -> isDialogOpen,
            new FakeTextStyleDialogComponentContext() {

                @Override
                public Runnable addHistoryWatcher(final HistoryWatcher watcher) {
                    return this.watcher.add(watcher);
                }

                @Override
                public Runnable addHistoryWatcherOnce(final HistoryWatcher watcher) {
                    return this.watcher.addOnce(watcher);
                }

                private final HistoryWatchers watcher = walkingkooka.spreadsheet.dominokit.history.HistoryWatchers.empty();

                @Override
                public void fireCurrentHistoryToken() {
                    this.watcher.onHistoryTokenChange(
                        HistoryToken.spreadsheetSelect(
                            SPREADSHEET_ID,
                            SPREADSHEET_NAME
                        ),
                        AppContexts.fake()
                    );
                }
            }
        );
    }

    @Override
    public Class<DialogOpenAwareTextStyleDialogComponentContext> type() {
        return Cast.to(DialogOpenAwareTextStyleDialogComponentContext.class);
    }
}
