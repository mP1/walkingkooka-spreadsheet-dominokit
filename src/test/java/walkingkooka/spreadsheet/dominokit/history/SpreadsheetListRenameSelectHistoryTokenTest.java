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

public final class SpreadsheetListRenameSelectHistoryTokenTest extends SpreadsheetListRenameHistoryTokenTestCase<SpreadsheetListRenameSelectHistoryToken> {

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck(
            this.createHistoryToken(),
            "/rename/123"
        );
    }

    @Test
    public void testClearAction() {
        this.clearActionAndCheck();
    }

    // setSaveValue.....................................................................................................

    @Test
    public void testSetSaveValueWithNotEmptyString() {
        final SpreadsheetName renameTo = SpreadsheetName.with("RenameToSpreadsheetName567");

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            renameTo.toString(),
            HistoryToken.spreadsheetListRenameSave(
                ID,
                renameTo
            )
        );
    }

    @Test
    public void testSetSaveValueWithEmptyString() {
        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            ""
        );
    }

    @Test
    public void testSetSaveValueWithSpreadsheetName() {
        final SpreadsheetName renameTo = SpreadsheetName.with("RenameToSpreadsheetName567");

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(renameTo),
            HistoryToken.spreadsheetListRenameSave(
                ID,
                renameTo
            )
        );
    }

    @Test
    public void testSetSaveValueWithEmptyOptional() {
        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.empty()
        );
    }

    @Override
    SpreadsheetListRenameSelectHistoryToken createHistoryToken(final SpreadsheetId id) {
        return SpreadsheetListRenameSelectHistoryToken.with(id);
    }

    @Override
    public Class<SpreadsheetListRenameSelectHistoryToken> type() {
        return SpreadsheetListRenameSelectHistoryToken.class;
    }
}
