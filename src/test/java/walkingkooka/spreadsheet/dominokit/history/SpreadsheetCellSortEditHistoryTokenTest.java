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
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellSortEditHistoryTokenTest extends SpreadsheetCellSortHistoryTokenTestCase<SpreadsheetCellSortEditHistoryToken> {

    @Test
    public void testWithNullComparatorNamesFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetCellSortEditHistoryToken.with(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        null
                )
        );
    }

    @Test
    public void testWithEmptyComparatorNames() {
        SpreadsheetCellSortEditHistoryToken.with(
                ID,
                NAME,
                ANCHORED_CELL,
                ""
        );
    }

    @Test
    public void testWithInvalidComparatorNames() {
        SpreadsheetCellSortEditHistoryToken.with(
                ID,
                NAME,
                ANCHORED_CELL,
                "!Invalid"
        );
    }

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck(
                this.createHistoryToken(),
                "/123/SpreadsheetName456/cell/A1/sort/edit/" + COMPARATOR_NAMES_LIST_STRING
        );
    }

    @Test
    public void testUrlFragmentWithEmptyComparatorNames() {
        this.urlFragmentAndCheck(
                SpreadsheetCellSortEditHistoryToken.with(
                        ID,
                        NAME,
                        ANCHORED_CELL,
                        ""
                ),
                "/123/SpreadsheetName456/cell/A1:C3/bottom-right/sort/edit"
        );
    }

    @Test
    public void testParseInvalidComparatorNames() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1:C3/sort/edit/!invalid",
                SpreadsheetCellSortEditHistoryToken.with(
                        ID,
                        NAME,
                        ANCHORED_CELL,
                        "!invalid"
                )
        );
    }

    @Test
    public void testParseMissingComparatorNames() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1/sort/edit",
                SpreadsheetCellSortEditHistoryToken.with(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        ""
                )
        );
    }

    @Test
    public void testParseEmptyComparatorNames() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1/sort/edit/",
                SpreadsheetCellSortEditHistoryToken.with(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        ""
                )
        );
    }

    @Test
    public void testParseInvalidComparatorColumn() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1/sort/edit/Z=text",
                SpreadsheetCellSortEditHistoryToken.with(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        "Z=text"
                )
        );
    }

    @Test
    public void testParseInvalidComparatorRow() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1/sort/edit/99=text",
                SpreadsheetCellSortEditHistoryToken.with(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        "99=text"
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
                        COMPARATOR_NAMES_LIST_STRING2
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
                        SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse(saveText)
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
                        saveText
                ),
                saveText,
                SpreadsheetCellSortSaveHistoryToken.with(
                        ID,
                        NAME,
                        anchored,
                        SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse(saveText)
                )
        );
    }

    @Override
    SpreadsheetCellSortEditHistoryToken createHistoryToken(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final AnchoredSpreadsheetSelection anchoredSelection) {
        return this.createHistoryToken(
                id,
                name,
                anchoredSelection,
                COMPARATOR_NAMES_LIST_STRING
        );
    }

    private SpreadsheetCellSortEditHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final AnchoredSpreadsheetSelection anchoredSelection,
                                                                   final String comparatorNames) {
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
