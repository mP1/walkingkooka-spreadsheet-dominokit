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

package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.meta.SpreadsheetId;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetListRenameSaveHistoryTokenTest extends SpreadsheetListRenameHistoryTokenTestCase<SpreadsheetListRenameSaveHistoryToken> {

    @Test
    public void testWithNullValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetListRenameSaveHistoryToken.with(
                SPREADSHEET_ID,
                null
            )
        );
    }

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck(
            this.createHistoryToken(),
            "/rename/123/save/" + SPREADSHEET_NAME
        );
    }

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.spreadsheetListRenameSelect(SPREADSHEET_ID)
        );
    }

    // setSaveStringValue...............................................................................................

    @Test
    public void testSetSaveStringValueWithNotEmptyString() {
        this.setSaveStringValueAndCheck(
            this.createHistoryToken(),
            DIFFERENT_SPREADSHEET_NAME.toString(),
            HistoryToken.spreadsheetListRenameSave(
                SPREADSHEET_ID,
                DIFFERENT_SPREADSHEET_NAME
            )
        );
    }

    @Test
    public void testSetSaveStringValueWithEmptyString() {
        this.setSaveStringValueAndCheck(
            this.createHistoryToken(),
            "",
            HistoryToken.spreadsheetListRenameSelect(SPREADSHEET_ID)
        );
    }

    // setSaveValue.....................................................................................................

    @Test
    public void testSetSaveValueWithSpreadsheetName() {
        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(DIFFERENT_SPREADSHEET_NAME),
            HistoryToken.spreadsheetListRenameSave(
                SPREADSHEET_ID,
                DIFFERENT_SPREADSHEET_NAME
            )
        );
    }

    @Test
    public void testSetSaveValueWithEmptyOptional() {
        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.empty(),
            HistoryToken.spreadsheetListRenameSelect(SPREADSHEET_ID)
        );
    }

    @Override
    SpreadsheetListRenameSaveHistoryToken createHistoryToken(final SpreadsheetId id) {
        return SpreadsheetListRenameSaveHistoryToken.with(
            id,
            SPREADSHEET_NAME
        );
    }

    @Override
    public Class<SpreadsheetListRenameSaveHistoryToken> type() {
        return SpreadsheetListRenameSaveHistoryToken.class;
    }
}
