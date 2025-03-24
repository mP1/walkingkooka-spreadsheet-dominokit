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

package walkingkooka.spreadsheet.dominokit.delta;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContexts;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicSpreadsheetDeltaCellsTableComponentContextTest implements SpreadsheetDeltaCellsTableComponentContextTesting<BasicSpreadsheetDeltaCellsTableComponentContext> {

    private final static HistoryContext HISTORY_CONTEXT = HistoryContexts.fake();

    private final static HasSpreadsheetDeltaFetcherWatchers HAS_SPREADSHEET_DELTA_FETCHER_WATCHERS = new HasSpreadsheetDeltaFetcherWatchers() {

        @Override
        public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Runnable addSpreadsheetDeltaFetcherWatcherOnce(final SpreadsheetDeltaFetcherWatcher watcher) {
            throw new UnsupportedOperationException();
        }
    };

    private final static Function<SpreadsheetExpressionReference, Set<SpreadsheetLabelName>> CELL_LABELS = (SpreadsheetExpressionReference reference) -> {
        Objects.requireNonNull(reference, "reference");
        throw new UnsupportedOperationException();
    };

    private final static Function<SpreadsheetExpressionReference, Set<SpreadsheetExpressionReference>> CELL_REFERENCES = (SpreadsheetExpressionReference reference) -> {
        Objects.requireNonNull(reference, "reference");
        throw new UnsupportedOperationException();
    };

    private final static Function<SpreadsheetExpressionReference, Optional<String>> FORMULA_TEXT = (SpreadsheetExpressionReference reference) -> {
        Objects.requireNonNull(reference, "reference");

        if(reference.equals(SpreadsheetSelection.A1)) {
            return Optional.of("=1+2+3000");
        }

        throw new UnsupportedOperationException();
    };

    // with.............................................................................................................

    @Test
    public void testWithNullHistoryContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetDeltaCellsTableComponentContext.with(
                null,
                HAS_SPREADSHEET_DELTA_FETCHER_WATCHERS,
                CELL_LABELS,
                CELL_REFERENCES,
                FORMULA_TEXT
            )
        );
    }

    @Test
    public void testWithNullHasSpreadsheetDeltaFetcherWatchersFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetDeltaCellsTableComponentContext.with(
                HISTORY_CONTEXT,
                null,
                CELL_LABELS,
                CELL_REFERENCES,
                FORMULA_TEXT
            )
        );
    }

    @Test
    public void testWithNullCellLabelsFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetDeltaCellsTableComponentContext.with(
                HISTORY_CONTEXT,
                HAS_SPREADSHEET_DELTA_FETCHER_WATCHERS,
                null,
                CELL_REFERENCES,
                FORMULA_TEXT
            )
        );
    }

    @Test
    public void testWithNullCellReferencesFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetDeltaCellsTableComponentContext.with(
                HISTORY_CONTEXT,
                HAS_SPREADSHEET_DELTA_FETCHER_WATCHERS,
                CELL_LABELS,
                null,
                FORMULA_TEXT
            )
        );
    }

    @Test
    public void testWithNullFormulaTextFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetDeltaCellsTableComponentContext.with(
                HISTORY_CONTEXT,
                HAS_SPREADSHEET_DELTA_FETCHER_WATCHERS,
                CELL_LABELS,
                CELL_REFERENCES,
                null
            )
        );
    }

    @Override
    public void testAddHistoryTokenWatcherOnceWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testAddHistoryTokenWatcherWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testPushHistoryTokenWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testFormulaText() {
        this.formulaTextAndCheck(
            this.createContext(),
            SpreadsheetSelection.A1,
            "=1+2+3000"
        );
    }

    @Override
    public BasicSpreadsheetDeltaCellsTableComponentContext createContext() {
        return BasicSpreadsheetDeltaCellsTableComponentContext.with(
            HISTORY_CONTEXT,
            HAS_SPREADSHEET_DELTA_FETCHER_WATCHERS,
            CELL_LABELS,
            CELL_REFERENCES,
            FORMULA_TEXT
        );
    }

    // class............................................................................................................

    @Override
    public String typeNameSuffix() {
        return SpreadsheetDeltaCellsTableComponentContext.class.getSimpleName();
    }

    @Override
    public Class<BasicSpreadsheetDeltaCellsTableComponentContext> type() {
        return BasicSpreadsheetDeltaCellsTableComponentContext.class;
    }
}
