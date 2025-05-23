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

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;

import java.util.Objects;
import java.util.Optional;

final class AppContextSpreadsheetFormulaSelectAnchorComponentContext implements SpreadsheetFormulaSelectAnchorComponentContext,
    HistoryContextDelegator {

    static AppContextSpreadsheetFormulaSelectAnchorComponentContext with(final AppContext context) {
        return new AppContextSpreadsheetFormulaSelectAnchorComponentContext(
            Objects.requireNonNull(context, "context")
        );
    }

    private AppContextSpreadsheetFormulaSelectAnchorComponentContext(final AppContext context) {
        this.context = context;
    }

    @Override
    public HistoryContext historyContext() {
        return this.context;
    }

    private final AppContext context;

    @Override
    public Optional<String> formulaText(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
        return this.context.spreadsheetViewportCache()
            .formulaText(spreadsheetExpressionReference);
    }

    @Override
    public String toString() {
        return this.context.toString();
    }
}
