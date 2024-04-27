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
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellSortSaveHistoryTokenTest extends SpreadsheetCellSortHistoryTokenTestCase<SpreadsheetCellSortSaveHistoryToken> {

    @Test
    public void testWithNullComparatorNamesFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetCellSortSaveHistoryToken.with(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        null
                )
        );
    }

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck(
                this.createHistoryToken(),
                "/123/SpreadsheetName456/cell/A1/sort/save/" + COMPARATOR_NAMES_LIST_STRING
        );
    }

    @Test
    public void testParseInvalidComparator() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1/sort/save/!invalid",
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
                "/123/SpreadsheetName456/cell/A1/sort/save/Z=text",
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
                "/123/SpreadsheetName456/cell/A1/sort/save/99=text",
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
                "/123/SpreadsheetName456/cell/A1/sort/save/" + COMPARATOR_NAMES_LIST_STRING,
                this.createHistoryToken()
        );
    }

    @Test
    public void testParseCellRange() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1:C3/bottom-right/sort/save/" + COMPARATOR_NAMES_LIST_STRING2,
                SpreadsheetCellSortSaveHistoryToken.with(
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
                        CELL.setDefaultAnchor()
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
                        SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse(saveText)
                )
        );
    }

    @Override
    SpreadsheetCellSortSaveHistoryToken createHistoryToken(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final AnchoredSpreadsheetSelection anchoredSelection) {
        return this.createHistoryToken(
                id,
                name,
                anchoredSelection,
                COMPARATOR_NAMES_LIST
        );
    }

    private SpreadsheetCellSortSaveHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final AnchoredSpreadsheetSelection anchoredSelection,
                                                                   final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames) {
        return SpreadsheetCellSortSaveHistoryToken.with(
                id,
                name,
                anchoredSelection,
                comparatorNames
        );
    }

    @Override
    public Class<SpreadsheetCellSortSaveHistoryToken> type() {
        return SpreadsheetCellSortSaveHistoryToken.class;
    }
}
