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
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

public final class SpreadsheetLabelMappingSelectHistoryTokenTest extends SpreadsheetLabelMappingHistoryTokenTestCase<SpreadsheetLabelMappingSelectHistoryToken> {

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck(
                LABEL,
                "/123/SpreadsheetName456/label/Label123"
        );
    }

    @Test
    public void testClose() {
        final SpreadsheetLabelMappingHistoryToken token = this.createHistoryToken();

        this.checkEquals(
                HistoryToken.spreadsheetSelect(
                        token.id(),
                        token.name()
                ),
                this.createHistoryToken()
                        .close()
        );
    }

    @Test
    public void testSetDelete() {
        final SpreadsheetLabelMappingHistoryToken token = this.createHistoryToken();
        final SpreadsheetLabelName labelName = token.labelName();

        this.checkEquals(
                HistoryToken.labelMappingDelete(
                        token.id(),
                        token.name(),
                        labelName
                ),
                this.createHistoryToken()
                        .setDelete()
        );
    }

    @Test
    public void testSetSaveCell() {
        final SpreadsheetLabelMappingHistoryToken token = this.createHistoryToken();
        final SpreadsheetLabelName labelName = token.labelName();
        final SpreadsheetCellReference cell = SpreadsheetSelection.parseCell("B2");

        this.checkEquals(
                HistoryToken.labelMappingSave(
                        token.id(),
                        token.name(),
                        labelName.mapping(cell)
                ),
                this.createHistoryToken()
                        .setSave(
                                cell.toString()
                        )
        );
    }

    @Test
    public void testSetSaveCellRange() {
        final SpreadsheetLabelMappingHistoryToken token = this.createHistoryToken();
        final SpreadsheetLabelName labelName = token.labelName();
        final SpreadsheetCellRange cells = SpreadsheetSelection.parseCellRange("C3:D4");

        this.checkEquals(
                HistoryToken.labelMappingSave(
                        token.id(),
                        token.name(),
                        labelName.mapping(cells)
                ),
                this.createHistoryToken()
                        .setSave(
                                cells.toString()
                        )
        );
    }

    @Override
    SpreadsheetLabelMappingSelectHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                 final SpreadsheetName name,
                                                                 final SpreadsheetLabelName label) {
        return SpreadsheetLabelMappingSelectHistoryToken.with(
                id,
                name,
                label
        );
    }

    @Override
    public Class<SpreadsheetLabelMappingSelectHistoryToken> type() {
        return SpreadsheetLabelMappingSelectHistoryToken.class;
    }
}
