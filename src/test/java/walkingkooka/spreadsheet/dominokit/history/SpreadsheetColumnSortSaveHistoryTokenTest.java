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

public final class SpreadsheetColumnSortSaveHistoryTokenTest extends SpreadsheetColumnSortHistoryTokenTestCase<SpreadsheetColumnSortSaveHistoryToken> {

    @Test
    public void testWithNullComparatorNamesFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetColumnSortSaveHistoryToken.with(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor(),
                        null
                )
        );
    }

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck(
                this.createHistoryToken(),
                "/123/SpreadsheetName456/column/A/sort/save/" + COMPARATOR_NAMES_LIST_STRING
        );
    }

    @Test
    public void testParseInvalidComparator() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/column/A/sort/save/!invalid",
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseInvalidComparatorColumn() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/column/A/sort/save/Z=text",
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParse2() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/column/A/sort/save/" + COMPARATOR_NAMES_LIST_STRING,
                this.createHistoryToken()
        );
    }

    @Test
    public void testParseColumnRange() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/column/A:C/right/sort/save/" + COMPARATOR_NAMES_LIST_STRING2,
                SpreadsheetColumnSortSaveHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseColumnRange("A:C").setDefaultAnchor(),
                        COMPARATOR_NAMES_LIST2
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

    @Override
    SpreadsheetColumnSortSaveHistoryToken createHistoryToken(final SpreadsheetId id,
                                                             final SpreadsheetName name,
                                                             final AnchoredSpreadsheetSelection anchoredSelection) {
        return this.createHistoryToken(
                id,
                name,
                anchoredSelection,
                COMPARATOR_NAMES_LIST
        );
    }

    private SpreadsheetColumnSortSaveHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                     final SpreadsheetName name,
                                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                                     final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames) {
        return SpreadsheetColumnSortSaveHistoryToken.with(
                id,
                name,
                anchoredSelection,
                comparatorNames
        );
    }

    @Override
    public Class<SpreadsheetColumnSortSaveHistoryToken> type() {
        return SpreadsheetColumnSortSaveHistoryToken.class;
    }
}
