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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

public final class SpreadsheetRenameSaveHistoryTokenTest extends SpreadsheetNameHistoryTokenTestCase<SpreadsheetRenameSaveHistoryToken> {

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

    @Test
    public void testSetAnchoredSelection() {
        final SpreadsheetRenameSaveHistoryToken token = this.createHistoryToken();

        this.setAnchoredSelectionAndCheck(
                token,
                token
        );
    }

    @Test
    public void testSetMetadataPropertyName() {
        this.setMetadataPropertyNameAndCheck(
                SpreadsheetMetadataPropertyName.LOCALE,
                HistoryToken.metadataPropertySelect(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.LOCALE
                )
        );
    }

    @Test
    public void testPatternKind() {
        this.patternKindAndCheck(
                this.createHistoryToken()
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
