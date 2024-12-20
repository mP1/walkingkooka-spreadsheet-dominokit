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

package walkingkooka.spreadsheet.dominokit.sort;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorProviders;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContexts;
import walkingkooka.spreadsheet.dominokit.log.LoggingContexts;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicSpreadsheetSortDialogComponentContextTest implements SpreadsheetSortDialogComponentContextTesting<BasicSpreadsheetSortDialogComponentContext> {

    @Test
    public void testWithNullSpreadsheetComparatorProviderFails() {
        assertThrows(
                NullPointerException.class,
                () -> BasicSpreadsheetSortDialogComponentContext.with(
                        null,
                        HistoryTokenContexts.fake(),
                        LoggingContexts.fake()
                )
        );
    }

    @Test
    public void testWithNullHistoryTokenContextFails() {
        assertThrows(
                NullPointerException.class,
                () -> BasicSpreadsheetSortDialogComponentContext.with(
                        SpreadsheetComparatorProviders.fake(),
                        null,
                        LoggingContexts.fake()
                )
        );
    }

    @Test
    public void testWithNullLoggingContextFails() {
        assertThrows(
                NullPointerException.class,
                () -> BasicSpreadsheetSortDialogComponentContext.with(
                        SpreadsheetComparatorProviders.fake(),
                        HistoryTokenContexts.fake(),
                        null
                )
        );
    }

    // context..........................................................................................................

    @Override
    public BasicSpreadsheetSortDialogComponentContext createContext() {
        return BasicSpreadsheetSortDialogComponentContext.with(
                SpreadsheetComparatorProviders.fake(),
                HistoryTokenContexts.fake(),
                LoggingContexts.fake()
        );
    }

    // class............................................................................................................

    @Override
    public String typeNameSuffix() {
        return SpreadsheetSortDialogComponentContext.class.getSimpleName();
    }

    @Override
    public Class<BasicSpreadsheetSortDialogComponentContext> type() {
        return BasicSpreadsheetSortDialogComponentContext.class;
    }
}
