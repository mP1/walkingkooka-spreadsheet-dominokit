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
import walkingkooka.spreadsheet.dominokit.AppContexts;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AppContextSpreadsheetSortDialogComponentContextTest implements SpreadsheetSortDialogComponentContextTesting<AppContextSpreadsheetSortDialogComponentContext> {

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> AppContextSpreadsheetSortDialogComponentContext.with(
                null
            )
        );
    }

    // context..........................................................................................................

    @Override
    public AppContextSpreadsheetSortDialogComponentContext createContext() {
        return AppContextSpreadsheetSortDialogComponentContext.with(
            AppContexts.fake()
        );
    }

    // class............................................................................................................

    @Override
    public String typeNameSuffix() {
        return SpreadsheetSortDialogComponentContext.class.getSimpleName();
    }

    @Override
    public Class<AppContextSpreadsheetSortDialogComponentContext> type() {
        return AppContextSpreadsheetSortDialogComponentContext.class;
    }
}
