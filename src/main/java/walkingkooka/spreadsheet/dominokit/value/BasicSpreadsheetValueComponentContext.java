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

package walkingkooka.spreadsheet.dominokit.value;

import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;

final class BasicSpreadsheetValueComponentContext implements SpreadsheetValueComponentContext,
    HistoryContextDelegator {

    static BasicSpreadsheetValueComponentContext with(final SpreadsheetViewportCache viewportCache,
                                                      final HistoryContext historyContext) {
        return new BasicSpreadsheetValueComponentContext(
            Objects.requireNonNull(viewportCache, "viewportCache"),
            Objects.requireNonNull(historyContext, "historyContext")
        );
    }

    private BasicSpreadsheetValueComponentContext(final SpreadsheetViewportCache viewportCache,
                                                  final HistoryContext historyContext) {
        this.viewportCache = viewportCache;
        this.historyContext = historyContext;
    }

    @Override
    public Optional<SpreadsheetCell> cell() {
        return this.historyContext.historyToken()
            .selection()
            .flatMap((SpreadsheetSelection selection) -> viewportCache.cell(selection));
    }

    private final SpreadsheetViewportCache viewportCache;

    // HistoryContextDelegator..........................................................................................

    @Override
    public HistoryContext historyContext() {
        return this.historyContext;
    }

    private final HistoryContext historyContext;

    // toString.........................................................................................................

    @Override
    public String toString() {
        return this.historyContext.toString();
    }
}
