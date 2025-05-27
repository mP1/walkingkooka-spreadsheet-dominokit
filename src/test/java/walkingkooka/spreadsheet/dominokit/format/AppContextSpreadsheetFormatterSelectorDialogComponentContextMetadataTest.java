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

package walkingkooka.spreadsheet.dominokit.format;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AppContextSpreadsheetFormatterSelectorDialogComponentContextMetadataTest implements SpreadsheetFormatterSelectorDialogComponentContextTesting<AppContextSpreadsheetFormatterSelectorDialogComponentContextMetadata> {

    @Test
    public void testWithNullAppContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> AppContextSpreadsheetFormatterSelectorDialogComponentContextMetadata.with(null)
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
    public void testIsMatchWithSpreadsheetMetadataPropertyNameSelectHistoryTokenFormatters() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.DATE_FORMATTER
            ),
            true
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetMetadataPropertyNameSaveHistoryTokenFormatters() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.DATE_FORMATTER,
                Optional.empty()
            ),
            false
        );
    }

    @Override
    public AppContextSpreadsheetFormatterSelectorDialogComponentContextMetadata createContext() {
        return AppContextSpreadsheetFormatterSelectorDialogComponentContextMetadata.with(AppContexts.fake());
    }

    @Override
    public String typeNameSuffix() {
        return "Metadata";
    }

    // class............................................................................................................

    @Override
    public Class<AppContextSpreadsheetFormatterSelectorDialogComponentContextMetadata> type() {
        return AppContextSpreadsheetFormatterSelectorDialogComponentContextMetadata.class;
    }
}
