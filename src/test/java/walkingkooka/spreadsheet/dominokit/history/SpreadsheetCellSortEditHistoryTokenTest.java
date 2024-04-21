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
import walkingkooka.spreadsheet.compare.SpreadsheetCellSpreadsheetComparatorNames;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.List;

public final class SpreadsheetCellSortEditHistoryTokenTest extends SpreadsheetCellSortHistoryTokenTestCase<SpreadsheetCellSortEditHistoryToken> {

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck(
                this.createHistoryToken(),
                "/123/SpreadsheetName456/cell/A1/sort/edit/" + COMPARATOR_NAMES_LIST_STRING
        );
    }

    @Test
    public void testParseInvalidComparator() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1/sort/edit/!invalid",
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseInvalidComparatorColumn() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1/sort/edit/Z=text",
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseInvalidComparatorRow() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1/sort/edit/99=text",
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParse2() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1/sort/edit/" + COMPARATOR_NAMES_LIST_STRING,
                this.createHistoryToken()
        );
    }

    @Test
    public void testParseCellRange() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1:C3/bottom-right/sort/edit/" + COMPARATOR_NAMES_LIST_STRING2,
                SpreadsheetCellSortEditHistoryToken.with(
                        ID,
                        NAME,
                        ANCHORED_CELL,
                        COMPARATOR_NAMES_LIST2
                )
        );
    }

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
                this.createHistoryToken(),
                HistoryToken.cell(
                        ID,
                        NAME,
                        SELECTION
                )
        );
    }

    @Test
    public void testSetSave() {
        final String saveText = "A=text";

        this.setSaveAndCheck(
                this.createHistoryToken(),
                saveText,
                SpreadsheetCellSortSaveHistoryToken.with(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetCellSpreadsheetComparatorNames.parseList(saveText)
                )
        );
    }

    @Test
    public void testSetSaveCellRange() {
        final String saveText = "A=text;B=text";
        final AnchoredSpreadsheetSelection anchored = SpreadsheetSelection.parseCellRange("A1:B2")
                .setDefaultAnchor();

        this.setSaveAndCheck(
                SpreadsheetCellSortEditHistoryToken.with(
                        ID,
                        NAME,
                        anchored,
                        SpreadsheetCellSpreadsheetComparatorNames.parseList(saveText)
                ),
                saveText,
                SpreadsheetCellSortSaveHistoryToken.with(
                        ID,
                        NAME,
                        anchored,
                        SpreadsheetCellSpreadsheetComparatorNames.parseList(saveText)
                )
        );
    }

    @Override
    SpreadsheetCellSortEditHistoryToken createHistoryToken(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final AnchoredSpreadsheetSelection anchoredSelection,
                                                           final List<SpreadsheetCellSpreadsheetComparatorNames> comparatorNames) {
        return SpreadsheetCellSortEditHistoryToken.with(
                id,
                name,
                anchoredSelection,
                comparatorNames
        );
    }

    @Override
    public Class<SpreadsheetCellSortEditHistoryToken> type() {
        return SpreadsheetCellSortEditHistoryToken.class;
    }
}
