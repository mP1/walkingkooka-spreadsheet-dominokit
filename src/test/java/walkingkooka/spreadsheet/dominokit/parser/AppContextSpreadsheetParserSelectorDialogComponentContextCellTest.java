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

package walkingkooka.spreadsheet.dominokit.parser;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AppContextSpreadsheetParserSelectorDialogComponentContextCellTest implements SpreadsheetParserSelectorDialogComponentContextTesting<AppContextSpreadsheetParserSelectorDialogComponentContextCell> {

    @Test
    public void testWithNullAppContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> AppContextSpreadsheetParserSelectorDialogComponentContextCell.with(null)
        );
    }

    // isMatch..........................................................................................................

    @Test
    public void testIsMatchWithSpreadsheetCellSelectHistoryToken() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetCellParserSelectHistoryToken() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.cellParserSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            true
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetCellParserSaveHistoryToken() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.cellParserSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Optional.empty()
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetCellFormatterSelectHistoryToken() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.cellFormatterSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetMetadataSelectPropertyNameHistoryTokenDateParser() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.DATE_PARSER
            ),
            false
        );
    }

    @Override
    public AppContextSpreadsheetParserSelectorDialogComponentContextCell createContext() {
        return AppContextSpreadsheetParserSelectorDialogComponentContextCell.with(AppContexts.fake());
    }

    @Override
    public String typeNameSuffix() {
        return "Cell";
    }

    // class............................................................................................................

    @Override
    public Class<AppContextSpreadsheetParserSelectorDialogComponentContextCell> type() {
        return AppContextSpreadsheetParserSelectorDialogComponentContextCell.class;
    }
}
