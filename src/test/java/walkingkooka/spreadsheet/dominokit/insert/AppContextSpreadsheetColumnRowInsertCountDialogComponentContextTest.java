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

package walkingkooka.spreadsheet.dominokit.insert;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AppContextSpreadsheetColumnRowInsertCountDialogComponentContextTest implements SpreadsheetColumnRowInsertCountDialogComponentContextTesting<AppContextSpreadsheetColumnRowInsertCountDialogComponentContext> {

    @Test
    public void testWithNullAppContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> AppContextSpreadsheetColumnRowInsertCountDialogComponentContext.with(null)
        );
    }

    // isMatch..........................................................................................................

    @Test
    public void testIsMatchWithSpreadsheetColumnInsertAfterHistoryTokenWithCount() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.columnInsertAfter(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.column()
                    .setDefaultAnchor(),
                OptionalInt.of(1)
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetColumnInsertAfterHistoryTokenWithoutCount() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.columnInsertAfter(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.column()
                    .setDefaultAnchor(),
                OptionalInt.empty()
            ),
            true
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetColumnInsertBeforeHistoryTokenWithCount() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.columnInsertBefore(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.column()
                    .setDefaultAnchor(),
                OptionalInt.of(1)
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetColumnInsertBeforeHistoryTokenWithoutCount() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.columnInsertBefore(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.column()
                    .setDefaultAnchor(),
                OptionalInt.empty()
            ),
            true
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetColumnSelectHistoryToken() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.columnSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.column()
                    .setDefaultAnchor()
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetRowInsertAfterHistoryTokenWithCount() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.rowInsertAfter(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.row()
                    .setDefaultAnchor(),
                OptionalInt.of(1)
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetRowInsertAfterHistoryTokenWithoutCount() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.rowInsertAfter(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.row()
                    .setDefaultAnchor(),
                OptionalInt.empty()
            ),
            true
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetRowInsertBeforeHistoryTokenWithCount() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.rowInsertBefore(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.row()
                    .setDefaultAnchor(),
                OptionalInt.of(1)
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetRowInsertBeforeHistoryTokenWithoutCount() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.rowInsertBefore(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.row()
                    .setDefaultAnchor(),
                OptionalInt.empty()
            ),
            true
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetRowSelectHistoryToken() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.rowSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.row()
                    .setDefaultAnchor()
            ),
            false
        );
    }

    @Override
    public AppContextSpreadsheetColumnRowInsertCountDialogComponentContext createContext() {
        return AppContextSpreadsheetColumnRowInsertCountDialogComponentContext.with(AppContexts.fake());
    }

    // class............................................................................................................

    @Override
    public String typeNameSuffix() {
        return SpreadsheetColumnRowInsertCountDialogComponentContext.class.getSimpleName();
    }

    @Override
    public Class<AppContextSpreadsheetColumnRowInsertCountDialogComponentContext> type() {
        return AppContextSpreadsheetColumnRowInsertCountDialogComponentContext.class;
    }
}
