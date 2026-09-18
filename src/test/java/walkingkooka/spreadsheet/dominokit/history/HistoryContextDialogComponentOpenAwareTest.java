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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;

public final class HistoryContextDialogComponentOpenAwareTest implements HistoryContextTesting<HistoryContextDialogComponentOpenAware> {

    @Test
    public void testAddHistoryWatcherOnHistoryTokenDialogOpen() {
        this.fired = false;

        final HistoryContextDialogComponentOpenAware context = this.createContext(true);
        context.addHistoryWatcher(
            (final HistoryToken previous,
             final AppContext appContext) -> HistoryContextDialogComponentOpenAwareTest.this.fired = true
        );
        context.fireCurrentHistoryToken();

        this.checkEquals(
            true,
            this.fired
        );
    }

    @Test
    public void testAddHistoryWatcherOnHistoryTokenDialogClose() {
        final HistoryContextDialogComponentOpenAware context = this.createContext(false);
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

        final HistoryContextDialogComponentOpenAware context = this.createContext(true);
        context.addHistoryWatcherOnce(
            (final HistoryToken previous,
             final AppContext appContext) -> HistoryContextDialogComponentOpenAwareTest.this.fired = true
        );
        context.fireCurrentHistoryToken();

        this.checkEquals(
            true,
            this.fired
        );
    }

    @Test
    public void testAddHistoryWatcherOnceOnHistoryTokenDialogClose() {
        final HistoryContextDialogComponentOpenAware context = this.createContext(false);
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
    public HistoryContextDialogComponentOpenAware createContext() {
        return this.createContext(true);
    }

    private HistoryContextDialogComponentOpenAware createContext(final boolean isDialogOpen) {
        return HistoryContextDialogComponentOpenAware.with(
            () -> isDialogOpen,
            HistoryContexts.basic(
                HistoryToken.spreadsheetSelect(
                    SpreadsheetId.with(1),
                    SpreadsheetName.with("SpreadsheetName1")
                ),
                AppContexts.fake()
            )
        );
    }

    @Override
    public Class<HistoryContextDialogComponentOpenAware> type() {
        return Cast.to(HistoryContextDialogComponentOpenAware.class);
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
