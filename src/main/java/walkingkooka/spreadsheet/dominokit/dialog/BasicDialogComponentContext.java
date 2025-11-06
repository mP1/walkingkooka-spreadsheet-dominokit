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

package walkingkooka.spreadsheet.dominokit.dialog;

import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;

import java.util.Objects;

/**
 * An incomplete {@link DialogComponentContext} intended to be target of a {@link DialogComponentContextDelegator}.
 * Note the {@link #dialogTitle()} throws {@link UnsupportedOperationException}.
 */
final class BasicDialogComponentContext implements DialogComponentContext,
    HistoryContextDelegator,
    LoggingContextDelegator {

    static BasicDialogComponentContext with(final HistoryContext historyContext,
                                            final LoggingContext loggingContext) {
        return new BasicDialogComponentContext(
            Objects.requireNonNull(historyContext, "historyContext"),
            Objects.requireNonNull(loggingContext, "loggingContext")
        );
    }

    private BasicDialogComponentContext(final HistoryContext historyContext,
                                        final LoggingContext loggingContext) {
        this.historyContext = historyContext;
        this.loggingContext = loggingContext;
    }

    @Override
    public String dialogTitle() {
        throw new UnsupportedOperationException();
    }

    // CanGiveFocus.....................................................................................................

    @Override
    public void giveFocus(final Runnable focus) {
        throw new UnsupportedOperationException();
    }

    // HistoryContextDelegator..........................................................................................

    @Override
    public HistoryContext historyContext() {
        return this.historyContext;
    }

    private final HistoryContext historyContext;

    // LoggingContextDelegator..........................................................................................

    @Override
    public LoggingContext loggingContext() {
        return this.loggingContext;
    }

    private final LoggingContext loggingContext;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.historyContext + " " + this.loggingContext;
    }
}
