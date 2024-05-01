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

public final class SpreadsheetRowSortEditHistoryTokenTest extends SpreadsheetRowSortHistoryTokenTestCase<SpreadsheetRowSortEditHistoryToken> {

    @Test
    public void testWithNullComparatorNamesFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetRowSortEditHistoryToken.with(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor(),
                        null
                )
        );
    }

    @Test
    public void testWithEmptyComparatorNames() {
        SpreadsheetRowSortEditHistoryToken.with(
                ID,
                NAME,
                ANCHORED_ROW,
                ""
        );
    }

    @Test
    public void testWithInvalidComparatorNames() {
        SpreadsheetRowSortEditHistoryToken.with(
                ID,
                NAME,
                ANCHORED_ROW,
                "!Invalid"
        );
    }

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck(
                this.createHistoryToken(),
                "/123/SpreadsheetName456/row/1/sort/edit/" + COMPARATOR_NAMES_LIST_STRING
        );
    }

    @Test
    public void testUrlFragmentWithEmptyComparatorNames() {
        this.urlFragmentAndCheck(
                SpreadsheetRowSortEditHistoryToken.with(
                        ID,
                        NAME,
                        ANCHORED_ROW,
                        ""
                ),
                "/123/SpreadsheetName456/row/1:3/bottom/sort/edit"
        );
    }

    @Test
    public void testParseInvalidComparatorNames() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/row/1:3/sort/edit/!invalid",
                SpreadsheetRowSortEditHistoryToken.with(
                        ID,
                        NAME,
                        ANCHORED_ROW,
                        "!invalid"
                )
        );
    }

    @Test
    public void testParseMissingComparatorNames() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/row/1/sort/edit",
                SpreadsheetRowSortEditHistoryToken.with(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor(),
                        ""
                )
        );
    }

    @Test
    public void testParseEmptyComparatorNames() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/row/1/sort/edit/",
                SpreadsheetRowSortEditHistoryToken.with(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor(),
                        ""
                )
        );
    }

    @Test
    public void testParseInvalidComparatorColumn() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/row/1/sort/edit/Z=text",
                SpreadsheetRowSortEditHistoryToken.with(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor(),
                        "Z=text"
                )
        );
    }

    @Test
    public void testParseInvalidComparatorRow() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/row/1/sort/edit/99=text",
                SpreadsheetRowSortEditHistoryToken.with(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor(),
                        "99=text"
                )
        );
    }

    @Test
    public void testParse2() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/row/1/sort/edit/" + COMPARATOR_NAMES_LIST_STRING,
                this.createHistoryToken()
        );
    }

    @Test
    public void testParseColumnRange() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/row/1:3/bottom/sort/edit/" + COMPARATOR_NAMES_LIST_STRING2,
                SpreadsheetRowSortEditHistoryToken.with(
                        ID,
                        NAME,
                        ANCHORED_ROW,
                        COMPARATOR_NAMES_LIST_STRING2
                )
        );
    }

    @Test
    public void testSetSave() {
        final String saveText = "1=text";

        this.setSaveAndCheck(
                this.createHistoryToken(),
                saveText,
                SpreadsheetRowSortSaveHistoryToken.with(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor(),
                        SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse(saveText)
                )
        );
    }

    @Test
    public void testSetSaveRowRange() {
        final String saveText = "1=text;2=text";
        final AnchoredSpreadsheetSelection anchored = SpreadsheetSelection.parseRowRange("1:2")
                .setDefaultAnchor();

        this.setSaveAndCheck(
                SpreadsheetRowSortEditHistoryToken.with(
                        ID,
                        NAME,
                        anchored,
                        saveText
                ),
                saveText,
                SpreadsheetRowSortSaveHistoryToken.with(
                        ID,
                        NAME,
                        anchored,
                        SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse(saveText)
                )
        );
    }

    @Override
    SpreadsheetRowSortEditHistoryToken createHistoryToken(final SpreadsheetId id,
                                                          final SpreadsheetName name,
                                                          final AnchoredSpreadsheetSelection anchoredSelection) {
        return this.createHistoryToken(
                id,
                name,
                anchoredSelection,
                COMPARATOR_NAMES_LIST_STRING
        );
    }

    private SpreadsheetRowSortEditHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                  final SpreadsheetName name,
                                                                  final AnchoredSpreadsheetSelection anchoredSelection,
                                                                  final String comparatorNames) {
        return SpreadsheetRowSortEditHistoryToken.with(
                id,
                name,
                anchoredSelection,
                comparatorNames
        );
    }

    @Override
    public Class<SpreadsheetRowSortEditHistoryToken> type() {
        return SpreadsheetRowSortEditHistoryToken.class;
    }
}
