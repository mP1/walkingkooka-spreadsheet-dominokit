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
import walkingkooka.ToStringTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContexts;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelNameResolver;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelNameResolvers;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicSpreadsheetDeltaLabelsTableComponentContextTest implements SpreadsheetDeltaLabelsTableComponentContextTesting<BasicSpreadsheetDeltaLabelsTableComponentContext>,
    ToStringTesting<BasicSpreadsheetDeltaLabelsTableComponentContext> {

    private final static Function<SpreadsheetExpressionReference, Optional<String>> CELL_TO_FORMULA_TEXT = (SpreadsheetExpressionReference reference) -> {
        Objects.requireNonNull(reference, "reference");

        if (reference.equals(SpreadsheetSelection.A1)) {
            return Optional.of("=1+2+3000");
        }

        throw new UnsupportedOperationException();
    };

    private final static Function<SpreadsheetExpressionReference, Set<SpreadsheetExpressionReference>> CELL_TO_REFERENCES = (ser) -> {
        throw new UnsupportedOperationException();
    };

    private final static Function<SpreadsheetLabelName, Optional<SpreadsheetCell>> LABEL_TO_CELL = (sln) -> {
        throw new UnsupportedOperationException();
    };

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

    private final static SpreadsheetLabelNameResolver SPREADSHEET_LABEL_NAME_RESOLVER = SpreadsheetLabelNameResolvers.fake();

    private final static HistoryContext HISTORY_CONTEXT = HistoryContexts.fake();

    // with.............................................................................................................

    @Test
    public void testWithNullCellToReferencesFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetDeltaLabelsTableComponentContext.with(
                CELL_TO_FORMULA_TEXT,
                null,
                LABEL_TO_CELL,
                HAS_SPREADSHEET_DELTA_FETCHER_WATCHERS,
                SPREADSHEET_LABEL_NAME_RESOLVER,
                HISTORY_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullLabelToCellFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetDeltaLabelsTableComponentContext.with(
                CELL_TO_FORMULA_TEXT,
                CELL_TO_REFERENCES,
                null,
                HAS_SPREADSHEET_DELTA_FETCHER_WATCHERS,
                SPREADSHEET_LABEL_NAME_RESOLVER,
                HISTORY_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullHasSpreadsheetDeltaFetcherWatchersFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetDeltaLabelsTableComponentContext.with(
                CELL_TO_FORMULA_TEXT,
                CELL_TO_REFERENCES,
                LABEL_TO_CELL,
                null,
                SPREADSHEET_LABEL_NAME_RESOLVER,
                HISTORY_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullSpreadsheetLabelNameResolverFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetDeltaLabelsTableComponentContext.with(
                CELL_TO_FORMULA_TEXT,
                CELL_TO_REFERENCES,
                LABEL_TO_CELL,
                HAS_SPREADSHEET_DELTA_FETCHER_WATCHERS,
                null,
                HISTORY_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullHistoryContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetDeltaLabelsTableComponentContext.with(
                CELL_TO_FORMULA_TEXT,
                CELL_TO_REFERENCES,
                LABEL_TO_CELL,
                HAS_SPREADSHEET_DELTA_FETCHER_WATCHERS,
                SPREADSHEET_LABEL_NAME_RESOLVER,
                null
            )
        );
    }

    @Override
    public BasicSpreadsheetDeltaLabelsTableComponentContext createContext() {
        return BasicSpreadsheetDeltaLabelsTableComponentContext.with(
            CELL_TO_FORMULA_TEXT,
            CELL_TO_REFERENCES,
            LABEL_TO_CELL,
            HAS_SPREADSHEET_DELTA_FETCHER_WATCHERS,
            SPREADSHEET_LABEL_NAME_RESOLVER,
            HISTORY_CONTEXT
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
    public void testCellReferencesWithNullSpreadsheetExpressionReferenceFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testPushHistoryTokenWithNullFails() {
        throw new UnsupportedOperationException();
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            BasicSpreadsheetDeltaLabelsTableComponentContext.with(
                CELL_TO_FORMULA_TEXT,
                CELL_TO_REFERENCES,
                LABEL_TO_CELL,
                HAS_SPREADSHEET_DELTA_FETCHER_WATCHERS,
                SPREADSHEET_LABEL_NAME_RESOLVER,
                HISTORY_CONTEXT
            ),
            CELL_TO_FORMULA_TEXT +
                " " +
                CELL_TO_REFERENCES +
                " " +
                LABEL_TO_CELL +
                " " +
                HAS_SPREADSHEET_DELTA_FETCHER_WATCHERS +
                " " +
                HISTORY_CONTEXT
        );
    }

    // class............................................................................................................

    @Override
    public Class<BasicSpreadsheetDeltaLabelsTableComponentContext> type() {
        return BasicSpreadsheetDeltaLabelsTableComponentContext.class;
    }

    @Override
    public String typeNameSuffix() {
        return SpreadsheetDeltaLabelsTableComponentContext.class.getSimpleName();
    }
}
