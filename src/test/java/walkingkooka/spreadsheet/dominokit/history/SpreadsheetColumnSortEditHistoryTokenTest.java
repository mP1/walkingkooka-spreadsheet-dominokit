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

public final class SpreadsheetColumnSortEditHistoryTokenTest extends SpreadsheetColumnSortHistoryTokenTestCase<SpreadsheetColumnSortEditHistoryToken> {

    @Test
    public void testWithNullComparatorNamesFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetColumnSortEditHistoryToken.with(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor(),
                        null
                )
        );
    }

    @Test
    public void testWithEmptyComparatorNames() {
        SpreadsheetColumnSortEditHistoryToken.with(
                ID,
                NAME,
                ANCHORED_COLUMN,
                ""
        );
    }

    @Test
    public void testWithInvalidComparatorNames() {
        SpreadsheetColumnSortEditHistoryToken.with(
                ID,
                NAME,
                ANCHORED_COLUMN,
                "!Invalid"
        );
    }

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck(
                this.createHistoryToken(),
                "/123/SpreadsheetName456/column/A/sort/edit/" + COMPARATOR_NAMES_LIST_STRING
        );
    }

    @Test
    public void testUrlFragmentWithEmptyComparatorNames() {
        this.urlFragmentAndCheck(
                SpreadsheetColumnSortEditHistoryToken.with(
                        ID,
                        NAME,
                        ANCHORED_COLUMN,
                        ""
                ),
                "/123/SpreadsheetName456/column/A:C/right/sort/edit"
        );
    }

    @Test
    public void testParseInvalidComparatorNames() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/column/A:C/sort/edit/!invalid",
                SpreadsheetColumnSortEditHistoryToken.with(
                        ID,
                        NAME,
                        ANCHORED_COLUMN,
                        "!invalid"
                )
        );
    }

    @Test
    public void testParseMissingComparatorNames() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/column/A/sort/edit",
                SpreadsheetColumnSortEditHistoryToken.with(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor(),
                        ""
                )
        );
    }

    @Test
    public void testParseEmptyComparatorNames() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/column/A/sort/edit/",
                SpreadsheetColumnSortEditHistoryToken.with(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor(),
                        ""
                )
        );
    }

    @Test
    public void testParseInvalidComparatorColumn() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/column/A/sort/edit/Z=text",
                SpreadsheetColumnSortEditHistoryToken.with(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor(),
                        "Z=text"
                )
        );
    }

    @Test
    public void testParseInvalidComparatorRow() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/column/A/sort/edit/99=text",
                SpreadsheetColumnSortEditHistoryToken.with(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor(),
                        "99=text"
                )
        );
    }

    @Test
    public void testParse2() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/column/A/sort/edit/" + COMPARATOR_NAMES_LIST_STRING,
                this.createHistoryToken()
        );
    }

    @Test
    public void testParseColumnRange() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/column/A:C/bottom-right/sort/edit/" + COMPARATOR_NAMES_LIST_STRING2,
                SpreadsheetColumnSortEditHistoryToken.with(
                        ID,
                        NAME,
                        ANCHORED_COLUMN,
                        COMPARATOR_NAMES_LIST_STRING2
                )
        );
    }

    @Test
    public void testSetSave() {
        final String saveText = "A=text";

        this.setSaveAndCheck(
                this.createHistoryToken(),
                saveText,
                SpreadsheetColumnSortSaveHistoryToken.with(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor(),
                        SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse(saveText)
                )
        );
    }

    @Test
    public void testSetSaveColumnRange() {
        final String saveText = "A=text;B=text";
        final AnchoredSpreadsheetSelection anchored = SpreadsheetSelection.parseColumnRange("A:B")
                .setDefaultAnchor();

        this.setSaveAndCheck(
                SpreadsheetColumnSortEditHistoryToken.with(
                        ID,
                        NAME,
                        anchored,
                        saveText
                ),
                saveText,
                SpreadsheetColumnSortSaveHistoryToken.with(
                        ID,
                        NAME,
                        anchored,
                        SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse(saveText)
                )
        );
    }

    @Override
    SpreadsheetColumnSortEditHistoryToken createHistoryToken(final SpreadsheetId id,
                                                             final SpreadsheetName name,
                                                             final AnchoredSpreadsheetSelection anchoredSelection) {
        return this.createHistoryToken(
                id,
                name,
                anchoredSelection,
                COMPARATOR_NAMES_LIST_STRING
        );
    }

    private SpreadsheetColumnSortEditHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                     final SpreadsheetName name,
                                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                                     final String comparatorNames) {
        return SpreadsheetColumnSortEditHistoryToken.with(
                id,
                name,
                anchoredSelection,
                comparatorNames
        );
    }

    @Override
    public Class<SpreadsheetColumnSortEditHistoryToken> type() {
        return SpreadsheetColumnSortEditHistoryToken.class;
    }
}
