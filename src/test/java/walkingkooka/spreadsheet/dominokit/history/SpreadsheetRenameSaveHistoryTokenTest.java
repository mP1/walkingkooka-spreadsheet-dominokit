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

import java.util.Optional;

public final class SpreadsheetRenameSaveHistoryTokenTest extends SpreadsheetRenameHistoryTokenTestCase<SpreadsheetRenameSaveHistoryToken> {

    private final static SpreadsheetName RENAME_TO = SpreadsheetName.with("RenameToSpreadsheetName567");

    @Test
    public void testParseMissingName() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/rename/save",
            HistoryToken.spreadsheetRenameSelect(
                ID,
                NAME
            )
        );
    }

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/rename/save/RenameToSpreadsheetName567");
    }

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.spreadsheetRenameSelect(
                ID,
                NAME
            )
        );
    }

    // setSaveValue.....................................................................................................

    @Test
    public void testSetSaveValueWithNotEmptyString() {
        final SpreadsheetName renameTo = SpreadsheetName.with("RenameToSpreadsheetName567");

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            renameTo.toString()
        );
    }

    @Test
    public void testSetSaveValueWithEmptyString() {
        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            "",
            HistoryToken.spreadsheetRenameSelect(
                ID,
                NAME
            )
        );
    }

    @Test
    public void testSetSaveValueWithSpreadsheetName() {
        final SpreadsheetName renameTo = SpreadsheetName.with("RenameToSpreadsheetName567");

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(renameTo),
            HistoryToken.spreadsheetRenameSave(
                ID,
                NAME,
                renameTo
            )
        );
    }

    @Test
    public void testSetSaveValueWithEmptyOptional() {
        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.empty(),
            HistoryToken.spreadsheetRenameSelect(
                ID,
                NAME
            )
        );
    }

    @Override
    SpreadsheetRenameSaveHistoryToken createHistoryToken(final SpreadsheetId id,
                                                         final SpreadsheetName name) {
        return SpreadsheetRenameSaveHistoryToken.with(
            id,
            name,
            RENAME_TO
        );
    }

    @Override
    public Class<SpreadsheetRenameSaveHistoryToken> type() {
        return SpreadsheetRenameSaveHistoryToken.class;
    }
}
