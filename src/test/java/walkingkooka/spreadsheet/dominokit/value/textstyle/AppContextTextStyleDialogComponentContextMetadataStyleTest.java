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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AppContextTextStyleDialogComponentContextMetadataStyleTest extends AppContextTextStyleDialogComponentContextTestCase<AppContextTextStyleDialogComponentContextMetadataStyle> {

    @Test
    public void testWithNullAppContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> AppContextTextStyleDialogComponentContextMetadataStyle.with(null)
        );
    }

    // DialogTitle......................................................................................................

    @Test
    public void testDialogTitle() {
        this.dialogTitleAndCheck(
            this.createContext(),
            "Spreadsheet: Style (style)"
        );
    }

    // IsMatch..........................................................................................................

    @Test
    public void testIsMatchWithSpreadsheetMetadataPropertyStyleWithoutTextStylePropertyName() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertyStyle(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                Optional.empty()
            ),
            true
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetMetadataPropertyStyleWithTextStylePropertyName() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.metadataPropertyStyle(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                Optional.of(
                    TextStylePropertyName.TEXT_ALIGN
                )
            ),
            true
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetCellStyleWithoutTextStylePropertyName() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.cellStyle(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Optional.empty()
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetCellStyleWithTextStylePropertyName() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.cellStyle(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Optional.of(
                    TextStylePropertyName.TEXT_ALIGN
                )
            ),
            false
        );
    }

    @Override
    public AppContextTextStyleDialogComponentContextMetadataStyle createContext() {
        return AppContextTextStyleDialogComponentContextMetadataStyle.with(
            new FakeAppContext() {
                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.metadataPropertyStyle(
                        AppContextTextStyleDialogComponentContextMetadataStyleTest.SPREADSHEET_ID,
                        SPREADSHEET_NAME,
                        Optional.empty()
                    );
                }
            }
        );
    }

    @Override
    public Class<AppContextTextStyleDialogComponentContextMetadataStyle> type() {
        return AppContextTextStyleDialogComponentContextMetadataStyle.class;
    }
}
