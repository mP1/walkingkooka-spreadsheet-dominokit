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

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContexts;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;

import static org.junit.jupiter.api.Assertions.assertThrows;


public final class BasicSpreadsheetDialogComponentContextTest implements SpreadsheetDialogComponentContextTesting<BasicSpreadsheetDialogComponentContext> {

    private final static HistoryContext HISTORY_CONTEXT = HistoryContexts.fake();

    private final static LoggingContext LOGGING_CONTEXT = walkingkooka.spreadsheet.dominokit.log.LoggingContexts.fake();

    @Test
    public void testWithNullHistoryContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetDialogComponentContext.with(
                null,
                LOGGING_CONTEXT
            )
        );
    }

    @Test
    public void testWithNullLoggingContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicSpreadsheetDialogComponentContext.with(
                HISTORY_CONTEXT,
                null
            )
        );
    }

    @Override
    public void testAddHistoryTokenWatcherWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testAddHistoryTokenWatcherOnceWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testPushHistoryTokenWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BasicSpreadsheetDialogComponentContext createContext() {
        return BasicSpreadsheetDialogComponentContext.with(
            HISTORY_CONTEXT,
            LOGGING_CONTEXT
        );
    }

    // class............................................................................................................

    @Override
    public Class<BasicSpreadsheetDialogComponentContext> type() {
        return BasicSpreadsheetDialogComponentContext.class;
    }
}
