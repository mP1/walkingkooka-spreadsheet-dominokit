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

import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelNameResolver;
import walkingkooka.spreadsheet.value.SpreadsheetCell;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public final class SpreadsheetDeltaLabelsTableComponentContexts implements PublicStaticHelper {

    /**
     * {@see BasicSpreadsheetDeltaLabelsTableComponentContext}
     */
    public static SpreadsheetDeltaLabelsTableComponentContext basic(final Function<SpreadsheetExpressionReference, Optional<String>> cellToFormulaText,
                                                                    final Function<SpreadsheetExpressionReference, Set<SpreadsheetExpressionReference>> cellToReferences,
                                                                    final Function<SpreadsheetLabelName, Optional<SpreadsheetCell>> labelToCell,
                                                                    final HasSpreadsheetDeltaFetcherWatchers hasSpreadsheetDeltaFetcherWatchers,
                                                                    final SpreadsheetLabelNameResolver spreadsheetLabelNameResolver,
                                                                    final HistoryContext historyContext) {
        return BasicSpreadsheetDeltaLabelsTableComponentContext.with(
            cellToFormulaText,
            cellToReferences,
            labelToCell,
            hasSpreadsheetDeltaFetcherWatchers,
            spreadsheetLabelNameResolver,
            historyContext
        );
    }

    /**
     * {@see FakeSpreadsheetDeltaLabelsTableComponentContext}
     */
    public static FakeSpreadsheetDeltaLabelsTableComponentContext fake() {
        return new FakeSpreadsheetDeltaLabelsTableComponentContext();
    }

    /**
     * Stop creation
     */
    private SpreadsheetDeltaLabelsTableComponentContexts() {
        throw new UnsupportedOperationException();
    }
}
