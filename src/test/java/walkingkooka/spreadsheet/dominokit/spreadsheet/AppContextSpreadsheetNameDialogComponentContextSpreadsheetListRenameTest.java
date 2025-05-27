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

package walkingkooka.spreadsheet.dominokit.spreadsheet;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AppContextSpreadsheetNameDialogComponentContextSpreadsheetListRenameTest implements SpreadsheetNameDialogComponentContextTesting<AppContextSpreadsheetNameDialogComponentContextSpreadsheetListRename> {

    @Test
    public void testWithNullAppContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> AppContextSpreadsheetNameDialogComponentContextSpreadsheetListRename.with(null)
        );
    }

    // isMatch..........................................................................................................

    @Test
    public void testIsMatchWithSpreadsheetListRenameSelectHistoryToken() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.spreadsheetListRenameSelect(
                SPREADSHEET_ID
            ),
            true
        );
    }


    @Test
    public void testIsMatchWithSpreadsheetListRenameSaveHistoryToken() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.spreadsheetListRenameSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetRenameSelectHistoryToken() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.spreadsheetRenameSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            false
        );
    }


    @Test
    public void testIsMatchWithSpreadsheetRenameSaveHistoryToken() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.spreadsheetRenameSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SPREADSHEET_NAME
            ),
            false
        );
    }

    @Override
    public AppContextSpreadsheetNameDialogComponentContextSpreadsheetListRename createContext() {
        return AppContextSpreadsheetNameDialogComponentContextSpreadsheetListRename.with(AppContexts.fake());
    }

    @Override
    public String typeNameSuffix() {
        return "SpreadsheetListRename";
    }

    // class............................................................................................................

    @Override
    public Class<AppContextSpreadsheetNameDialogComponentContextSpreadsheetListRename> type() {
        return AppContextSpreadsheetNameDialogComponentContextSpreadsheetListRename.class;
    }
}
