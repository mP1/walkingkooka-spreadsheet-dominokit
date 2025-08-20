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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportHomeNavigationList;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class AppContextSpreadsheetNavigateDialogComponentContextTestCase<C extends AppContextSpreadsheetNavigateDialogComponentContext>
    implements SpreadsheetNavigateDialogComponentContextTesting<C> {

    final static SpreadsheetViewportHomeNavigationList NAVIGATION_LIST = SpreadsheetViewportHomeNavigationList.with(
        SpreadsheetSelection.parseCell("Z99")
    );

    @Test
    public final void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext(
                (AppContext)null
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

    public final C createContext(final HistoryToken historyToken) {
        return this.createContext(
            new FakeAppContext() {
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

    abstract C createContext(final AppContext context);

    @Override
    public final String typeNamePrefix() {
        return AppContextSpreadsheetNavigateDialogComponentContext.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
