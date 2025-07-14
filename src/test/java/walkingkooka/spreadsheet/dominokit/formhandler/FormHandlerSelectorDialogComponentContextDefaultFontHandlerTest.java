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

package walkingkooka.spreadsheet.dominokit.formhandler;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class FormHandlerSelectorDialogComponentContextDefaultFontHandlerTest implements FormHandlerSelectorDialogComponentContextTesting<FormHandlerSelectorDialogComponentContextDefaultFormHandler> {

    @Test
    public void testWithNullAppContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> FormHandlerSelectorDialogComponentContextDefaultFormHandler.with(null)
        );
    }

    // isMatch..........................................................................................................

    @Test
    public void testIsMatchWithSpreadsheetMetadataPropertySelectHistoryTokenDefaultFormHandler() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.DEFAULT_FORM_HANDLER
            ),
            true
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetMetadataPropertySelectHistoryTokenComparators() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.COMPARATORS
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetMetadataPropertySelectHistoryTokenFunctions() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetMetadataPropertyName.FUNCTIONS
            ),
            false
        );
    }

    @Override
    public FormHandlerSelectorDialogComponentContextDefaultFormHandler createContext() {
        return FormHandlerSelectorDialogComponentContextDefaultFormHandler.with(AppContexts.fake());
    }

    @Override
    public String typeNameSuffix() {
        return "DefaultFormHandler";
    }

    // class............................................................................................................

    @Override
    public Class<FormHandlerSelectorDialogComponentContextDefaultFormHandler> type() {
        return FormHandlerSelectorDialogComponentContextDefaultFormHandler.class;
    }
}
