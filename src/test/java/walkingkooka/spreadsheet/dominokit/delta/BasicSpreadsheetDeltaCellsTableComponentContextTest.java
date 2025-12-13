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
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.validation.ValueType;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicSpreadsheetDeltaCellsTableComponentContextTest implements SpreadsheetDeltaCellsTableComponentContextTesting<BasicSpreadsheetDeltaCellsTableComponentContext> {

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

    private final static Function<SpreadsheetExpressionReference, Set<SpreadsheetLabelName>> CELL_TO_LABELS = (SpreadsheetExpressionReference reference) -> {
        Objects.requireNonNull(reference, "reference");
        throw new UnsupportedOperationException();
    };

    private final static Function<SpreadsheetExpressionReference, Set<SpreadsheetExpressionReference>> CELL_TO_REFERENCES = (SpreadsheetExpressionReference reference) -> {
        Objects.requireNonNull(reference, "reference");
        throw new UnsupportedOperationException();
    };

    private final static Function<SpreadsheetExpressionReference, Optional<String>> CELL_TO_FORMULA_TEXT = (SpreadsheetExpressionReference reference) -> {
        Objects.requireNonNull(reference, "reference");

        if (reference.equals(SpreadsheetSelection.A1)) {
            return Optional.of("=1+2+3000");
        }

        throw new UnsupportedOperationException();
    };

    private final static Function<SpreadsheetSelection, Optional<SpreadsheetCell>> SELECTION_TO_CELL = (SpreadsheetSelection selection) -> {
        Objects.requireNonNull(selection, "selection");

        if (selection.equals(SpreadsheetSelection.A1)) {
            return Optional.of(
                SpreadsheetSelection.A1.setFormula(
                    SpreadsheetFormula.EMPTY.setValueType(
                        Optional.of(ValueType.TEXT)
                    )
                )
            );
        }

        throw new UnsupportedOperationException();
    };

    private final static HistoryContext HISTORY_CONTEXT = HistoryContexts.fake();

    // with.............................................................................................................

    @Test
    public void testWithNullHasSpreadsheetDeltaFetcherWatchersFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetDeltaCellsTableComponentContext.with(
                null,
                CELL_TO_LABELS,
                CELL_TO_REFERENCES,
                CELL_TO_FORMULA_TEXT,
                SELECTION_TO_CELL,
                HISTORY_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullCellToLabelsFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetDeltaCellsTableComponentContext.with(
                HAS_SPREADSHEET_DELTA_FETCHER_WATCHERS,
                null,
                CELL_TO_REFERENCES,
                CELL_TO_FORMULA_TEXT,
                SELECTION_TO_CELL,
                HISTORY_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullCellToReferencesFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetDeltaCellsTableComponentContext.with(
                HAS_SPREADSHEET_DELTA_FETCHER_WATCHERS,
                CELL_TO_LABELS,
                null,
                CELL_TO_FORMULA_TEXT,
                SELECTION_TO_CELL,
                HISTORY_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullCellToFormulaTextFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetDeltaCellsTableComponentContext.with(
                HAS_SPREADSHEET_DELTA_FETCHER_WATCHERS,
                CELL_TO_LABELS,
                CELL_TO_REFERENCES,
                null,
                SELECTION_TO_CELL,
                HISTORY_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullSelectionToCellFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetDeltaCellsTableComponentContext.with(
                HAS_SPREADSHEET_DELTA_FETCHER_WATCHERS,
                CELL_TO_LABELS,
                CELL_TO_REFERENCES,
                CELL_TO_FORMULA_TEXT,
                null,
                HISTORY_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullHistoryContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetDeltaCellsTableComponentContext.with(
                HAS_SPREADSHEET_DELTA_FETCHER_WATCHERS,
                CELL_TO_LABELS,
                CELL_TO_REFERENCES,
                CELL_TO_FORMULA_TEXT,
                SELECTION_TO_CELL,
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
            HAS_SPREADSHEET_DELTA_FETCHER_WATCHERS,
            CELL_TO_LABELS,
            CELL_TO_REFERENCES,
            CELL_TO_FORMULA_TEXT,
            SELECTION_TO_CELL,
            HISTORY_CONTEXT
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
