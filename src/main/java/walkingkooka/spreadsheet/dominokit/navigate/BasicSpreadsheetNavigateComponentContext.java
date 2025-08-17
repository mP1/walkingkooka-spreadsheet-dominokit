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

import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;

import java.util.Objects;

abstract class BasicSpreadsheetNavigateComponentContext implements SpreadsheetNavigateComponentContext,
    HistoryContextDelegator,
    LoggingContextDelegator {

    BasicSpreadsheetNavigateComponentContext(final HistoryContext historyContext,
                                             final LoggingContext loggingContext) {
        super();
        this.historyContext = Objects.requireNonNull(historyContext, "historyContext");
        this.loggingContext = Objects.requireNonNull(loggingContext, "loggingContext");
    }

    // ComponentLifecycleMatcher........................................................................................

    @Override
    public final boolean shouldIgnore(final HistoryToken token) {
        return token.isSave();
    }

    // HistoryContextDelegator..........................................................................................

    @Override
    public final HistoryContext historyContext() {
        return this.historyContext;
    }

    private final HistoryContext historyContext;

    // LoggingContextDelegator..........................................................................................

    @Override
    public final LoggingContext loggingContext() {
        return this.loggingContext;
    }

    private final LoggingContext loggingContext;

    // toString.........................................................................................................

    @Override
    public String toString() {
        return this.historyContext + " " + this.loggingContext;
    }
}
