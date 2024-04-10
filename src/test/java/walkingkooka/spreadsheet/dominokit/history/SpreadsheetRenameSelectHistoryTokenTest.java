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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetRenameSelectHistoryTokenTest extends SpreadsheetRenameHistoryTokenTestCase<SpreadsheetRenameSelectHistoryToken> {

    @Test
    public void testWithNullValueFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetRenameSaveHistoryToken.with(
                        ID,
                        NAME,
                        null
                )
        );
    }

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/rename");
    }

    @Test
    public void testClearAction() {
        this.clearActionAndCheck();
    }

    @Test
    public void testClose() {
        this.closeAndCheck(
                this.createHistoryToken(),
                HistoryToken.spreadsheetSelect(
                        ID,
                        NAME
                )
        );
    }

    @Test
    public void testSave() {
        final SpreadsheetName renameTo = SpreadsheetName.with("RenameToSpreadsheetName567");

        this.setSaveAndCheck(
                this.createHistoryToken(),
                renameTo.toString(),
                HistoryToken.spreadsheetRenameSave(
                        ID,
                        NAME,
                        renameTo
                )
        );
    }

    @Override
    SpreadsheetRenameSelectHistoryToken createHistoryToken(final SpreadsheetId id,
                                                           final SpreadsheetName name) {
        return SpreadsheetRenameSelectHistoryToken.with(
                id,
                name
        );
    }

    @Override
    public Class<SpreadsheetRenameSelectHistoryToken> type() {
        return SpreadsheetRenameSelectHistoryToken.class;
    }
}
