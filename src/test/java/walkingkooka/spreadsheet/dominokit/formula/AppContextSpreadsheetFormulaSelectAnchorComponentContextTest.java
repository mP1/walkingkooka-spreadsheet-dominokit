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

package walkingkooka.spreadsheet.dominokit.formula;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;

import java.util.Objects;

import static org.junit.Assert.assertThrows;

public final class AppContextSpreadsheetFormulaSelectAnchorComponentContextTest implements SpreadsheetFormulaSelectAnchorComponentContextTesting<AppContextSpreadsheetFormulaSelectAnchorComponentContext> {

    private final static AppContext APP_CONTEXT = new FakeAppContext() {

        @Override
        public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher historyTokenWatcher) {
            Objects.requireNonNull(historyTokenWatcher, "historyTokenWatcher");
            return null;
        }

        @Override
        public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher spreadsheetDeltaFetcherWatcher) {
            return null;
        }

        @Override
        public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher spreadsheetMetadataFetcherWatcher) {
            return null;
        }

        @Override
        public SpreadsheetViewportCache spreadsheetViewportCache() {
            return SpreadsheetViewportCache.empty(this);
        }
    };

    @Test
    public void testWithNullAppContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> AppContextSpreadsheetFormulaSelectAnchorComponentContext.with(null)
        );
    }

    @Override
    public AppContextSpreadsheetFormulaSelectAnchorComponentContext createContext() {
        return AppContextSpreadsheetFormulaSelectAnchorComponentContext.with(APP_CONTEXT);
    }

    // class............................................................................................................

    @Override
    public String typeNameSuffix() {
        return SpreadsheetFormulaSelectAnchorComponentContext.class.getSimpleName();
    }

    @Override
    public Class<AppContextSpreadsheetFormulaSelectAnchorComponentContext> type() {
        return AppContextSpreadsheetFormulaSelectAnchorComponentContext.class;
    }
}
