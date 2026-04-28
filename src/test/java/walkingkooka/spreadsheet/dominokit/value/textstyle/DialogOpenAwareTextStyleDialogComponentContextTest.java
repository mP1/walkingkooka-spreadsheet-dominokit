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
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;

public final class DialogOpenAwareTextStyleDialogComponentContextTest implements TextStyleDialogComponentContextTesting<DialogOpenAwareTextStyleDialogComponentContext> {

    @Test
    public void testAddHistoryTokenWatcherOnHistoryTokenDialogOpen() {
        this.fired = false;

        final DialogOpenAwareTextStyleDialogComponentContext context = this.createContext(true);
        context.addHistoryTokenWatcher(
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
    public void testAddHistoryTokenWatcherOnHistoryTokenDialogClose() {
        final DialogOpenAwareTextStyleDialogComponentContext context = this.createContext(false);
        context.addHistoryTokenWatcher(
            (final HistoryToken previous,
             final AppContext appContext) -> {
                throw new UnsupportedOperationException();
            }
        );
        context.fireCurrentHistoryToken();
    }

    @Test
    public void testAddHistoryTokenWatcherOnceOnHistoryTokenDialogOpen() {
        this.fired = false;

        final DialogOpenAwareTextStyleDialogComponentContext context = this.createContext(true);
        context.addHistoryTokenWatcherOnce(
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
    public void testAddHistoryTokenWatcherOnceOnHistoryTokenDialogClose() {
        final DialogOpenAwareTextStyleDialogComponentContext context = this.createContext(false);
        context.addHistoryTokenWatcherOnce(
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
                public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                    return this.watcher.add(watcher);
                }

                @Override
                public Runnable addHistoryTokenWatcherOnce(final HistoryTokenWatcher watcher) {
                    return this.watcher.addOnce(watcher);
                }

                private final HistoryTokenWatchers watcher = HistoryTokenWatchers.empty();

                @Override
                public void fireCurrentHistoryToken() {
                    this.watcher.onHistoryTokenChange(
                        HistoryToken.spreadsheetSelect(
                            SpreadsheetId.with(1),
                            SpreadsheetName.with("SpreadsheetName1")
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
