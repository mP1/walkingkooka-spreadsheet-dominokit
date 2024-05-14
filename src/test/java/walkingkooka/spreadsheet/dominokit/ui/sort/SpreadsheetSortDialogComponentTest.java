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

package walkingkooka.spreadsheet.dominokit.ui.sort;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.compare.SpreadsheetComparator;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorProvider;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorProviders;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.ui.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Set;

public final class SpreadsheetSortDialogComponentTest implements SpreadsheetDialogComponentLifecycleTesting<SpreadsheetSortDialogComponent> {

    private final static SpreadsheetId ID = SpreadsheetId.with(1);

    private final static SpreadsheetName NAME = SpreadsheetName.with("spreadsheetName123");

    // should have one *EMPTY* SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent
    @Test
    public void testCellEmpty() {
        this.onHistoryTokenChangeAndCheck(
                this.cellAppContext(
                        "B2:C3",
                        ""
                ),
                "SpreadsheetSortDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Sort\n" +
                        "    id=sort includeClose=true\n" +
                        "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                        "        ParserSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [] id=sort-columnOrRowComparatorNamesList-TextBox\n" +
                        "            Errors\n" +
                        "              text is empty\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [] id=sort-comparatorNames-0-TextBox\n" +
                        "              Errors\n" +
                        "                text is empty\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        \"Sort\" DISABLED id=sort-sort-Link\n" +
                        "        \"Close\" [#/1/spreadsheetName123/cell/B2:C3/bottom-right] id=sort-close-Link\n"
        );
    }

    // The first column is invalid so should include an ERROR MESSAGE
    @Test
    public void testCellInvalidColumn() {
        this.onHistoryTokenChangeAndCheck(
                this.cellAppContext(
                        "B2:C3",
                        "Z=text"
                ),
                "SpreadsheetSortDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Sort\n" +
                        "    id=sort includeClose=true\n" +
                        "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                        "        ParserSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [Z=text] id=sort-columnOrRowComparatorNamesList-TextBox\n" +
                        "            Errors\n" +
                        "              Some sort columns/rows are not within B2:C3 got Z\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [Z=text] id=sort-comparatorNames-0-TextBox\n" +
                        "              Errors\n" +
                        "                Invalid Column Z is not within B2:C3\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [] id=sort-comparatorNames-1-TextBox\n" +
                        "              Errors\n" +
                        "                text is empty\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        \"Sort\" DISABLED id=sort-sort-Link\n" +
                        "        \"Close\" [#/1/spreadsheetName123/cell/B2:C3/bottom-right] id=sort-close-Link\n"
        );
    }

    // The first column is invalid so should include an ERROR MESSAGE
    @Test
    public void testCellInvalidRow() {
        this.onHistoryTokenChangeAndCheck(
                this.cellAppContext(
                        "B2:C3",
                        "99=text"
                ),
                "SpreadsheetSortDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Sort\n" +
                        "    id=sort includeClose=true\n" +
                        "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                        "        ParserSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [99=text] id=sort-columnOrRowComparatorNamesList-TextBox\n" +
                        "            Errors\n" +
                        "              Some sort columns/rows are not within B2:C3 got 99\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [99=text] id=sort-comparatorNames-0-TextBox\n" +
                        "              Errors\n" +
                        "                Invalid Row 99 is not within B2:C3\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [] id=sort-comparatorNames-1-TextBox\n" +
                        "              Errors\n" +
                        "                text is empty\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        \"Sort\" DISABLED id=sort-sort-Link\n" +
                        "        \"Close\" [#/1/spreadsheetName123/cell/B2:C3/bottom-right] id=sort-close-Link\n"
        );
    }

    // should have two SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent
    // the first with "B" and an empty second.
    @Test
    public void testCellColumn() {
        this.onHistoryTokenChangeAndCheck(
                this.cellAppContext(
                        "B2:C3",
                        "B"
                ),
                "SpreadsheetSortDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Sort\n" +
                        "    id=sort includeClose=true\n" +
                        "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                        "        ParserSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [B] id=sort-columnOrRowComparatorNamesList-TextBox\n" +
                        "            Errors\n" +
                        "              Missing '='\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [B] id=sort-comparatorNames-0-TextBox\n" +
                        "              Errors\n" +
                        "                Missing '='\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [] id=sort-comparatorNames-1-TextBox\n" +
                        "              Errors\n" +
                        "                text is empty\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        \"Sort\" DISABLED id=sort-sort-Link\n" +
                        "        \"Close\" [#/1/spreadsheetName123/cell/B2:C3/bottom-right] id=sort-close-Link\n"
        );
    }

    @Test
    public void testCellColumnEqualsSign() {
        this.onHistoryTokenChangeAndCheck(
                this.cellAppContext(
                        "B2:C3",
                        "B="
                ),
                "SpreadsheetSortDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Sort\n" +
                        "    id=sort includeClose=true\n" +
                        "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                        "        ParserSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [B=] id=sort-columnOrRowComparatorNamesList-TextBox\n" +
                        "            Errors\n" +
                        "              Missing comparator name\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [B=] id=sort-comparatorNames-0-TextBox\n" +
                        "              Errors\n" +
                        "                Missing comparator name\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [] id=sort-comparatorNames-1-TextBox\n" +
                        "              Errors\n" +
                        "                text is empty\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        \"Sort\" DISABLED id=sort-sort-Link\n" +
                        "        \"Close\" [#/1/spreadsheetName123/cell/B2:C3/bottom-right] id=sort-close-Link\n"
        );
    }

    // should only have 2x SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent because there are only
    // 2 columns in the range.
    @Test
    public void testCellColumnComparatorName() {
        this.onHistoryTokenChangeAndCheck(
                this.cellAppContext(
                        "B2:C3",
                        "B=text"
                ),
                "SpreadsheetSortDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Sort\n" +
                        "    id=sort includeClose=true\n" +
                        "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                        "        ParserSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [B=text] id=sort-columnOrRowComparatorNamesList-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [B=text] id=sort-comparatorNames-0-TextBox\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [] id=sort-comparatorNames-1-TextBox\n" +
                        "              Errors\n" +
                        "                text is empty\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        \"Sort\" [#/1/spreadsheetName123/cell/B2:C3/bottom-right/sort/save/B%3Dtext] id=sort-sort-Link\n" +
                        "        \"Close\" [#/1/spreadsheetName123/cell/B2:C3/bottom-right] id=sort-close-Link\n"
        );
    }

    @Test
    public void testCellColumnComparatorNameComparatorName() {
        this.onHistoryTokenChangeAndCheck(
                this.cellAppContext(
                        "B2:C3",
                        "B=text,text2"
                ),
                "SpreadsheetSortDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Sort\n" +
                        "    id=sort includeClose=true\n" +
                        "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                        "        ParserSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [B=text,text2] id=sort-columnOrRowComparatorNamesList-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [B=text,text2] id=sort-comparatorNames-0-TextBox\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [] id=sort-comparatorNames-1-TextBox\n" +
                        "              Errors\n" +
                        "                text is empty\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        \"Sort\" [#/1/spreadsheetName123/cell/B2:C3/bottom-right/sort/save/B%3Dtext%2Ctext2] id=sort-sort-Link\n" +
                        "        \"Close\" [#/1/spreadsheetName123/cell/B2:C3/bottom-right] id=sort-close-Link\n"
        );
    }

    @Test
    public void testCellColumnComparatorNameColumnComparatorName() {
        this.onHistoryTokenChangeAndCheck(
                this.cellAppContext(
                        "B2:C3",
                        "B=text;C=text2"
                ),
                "SpreadsheetSortDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Sort\n" +
                        "    id=sort includeClose=true\n" +
                        "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                        "        ParserSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [B=text;C=text2] id=sort-columnOrRowComparatorNamesList-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [B=text] id=sort-comparatorNames-0-TextBox\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [C=text2] id=sort-comparatorNames-1-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        \"Sort\" [#/1/spreadsheetName123/cell/B2:C3/bottom-right/sort/save/B%3Dtext%3BC%3Dtext2] id=sort-sort-Link\n" +
                        "        \"Close\" [#/1/spreadsheetName123/cell/B2:C3/bottom-right] id=sort-close-Link\n"
        );
    }

    // contains a duplicate COLUMN in the expression so the 2nd SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent
    // should have an error
    @Test
    public void testCellWithDuplicateColumn() {
        this.onHistoryTokenChangeAndCheck(
                this.cellAppContext(
                        "B2:C3",
                        "B=text;B=text2"
                ),
                "SpreadsheetSortDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Sort\n" +
                        "    id=sort includeClose=true\n" +
                        "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                        "        ParserSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [B=text;B=text2] id=sort-columnOrRowComparatorNamesList-TextBox\n" +
                        "            Errors\n" +
                        "              Duplicate column B\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [B=text] id=sort-comparatorNames-0-TextBox\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [B=text2] id=sort-comparatorNames-1-TextBox\n" +
                        "              Errors\n" +
                        "                Duplicate Column B\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        \"Sort\" DISABLED id=sort-sort-Link\n" +
                        "        \"Close\" [#/1/spreadsheetName123/cell/B2:C3/bottom-right] id=sort-close-Link\n"
        );
    }

    // Must have 3 SpreadsheetColumnOrRowSpreadsheetComparatorName: B, C, *EMPTY*
    @Test
    public void testCellWith2ColumnsAndColumnComparatorNameColumnComparatorName() {
        this.onHistoryTokenChangeAndCheck(
                this.cellAppContext(
                        "B2:D4",
                        "B=text;C=text2"
                ),
                "SpreadsheetSortDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Sort\n" +
                        "    id=sort includeClose=true\n" +
                        "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                        "        ParserSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [B=text;C=text2] id=sort-columnOrRowComparatorNamesList-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [B=text] id=sort-comparatorNames-0-TextBox\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [C=text2] id=sort-comparatorNames-1-TextBox\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [] id=sort-comparatorNames-2-TextBox\n" +
                        "              Errors\n" +
                        "                text is empty\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        \"Sort\" [#/1/spreadsheetName123/cell/B2:D4/bottom-right/sort/save/B%3Dtext%3BC%3Dtext2] id=sort-sort-Link\n" +
                        "        \"Close\" [#/1/spreadsheetName123/cell/B2:D4/bottom-right] id=sort-close-Link\n"
        );
    }

    @Test
    public void testColumn() {
        this.onHistoryTokenChangeAndCheck(
                this.appContext(
                        HistoryToken.columnSortEdit(
                                ID,
                                NAME,
                                SpreadsheetSelection.parseColumnRange("B:C")
                                        .setDefaultAnchor(),
                                "B=text"
                        )
                ),
                "SpreadsheetSortDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Sort\n" +
                        "    id=sort includeClose=true\n" +
                        "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                        "        ParserSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [B=text] id=sort-columnOrRowComparatorNamesList-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [B=text] id=sort-comparatorNames-0-TextBox\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [] id=sort-comparatorNames-1-TextBox\n" +
                        "              Errors\n" +
                        "                text is empty\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        \"Sort\" [#/1/spreadsheetName123/column/B:C/right/sort/save/B%3Dtext] id=sort-sort-Link\n" +
                        "        \"Close\" [#/1/spreadsheetName123/column/B:C/right] id=sort-close-Link\n"
        );
    }

    // only the 2nd SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent should have the duplicate Column error
    @Test
    public void testColumnDuplicateColumn() {
        this.onHistoryTokenChangeAndCheck(
                this.appContext(
                        HistoryToken.columnSortEdit(
                                ID,
                                NAME,
                                SpreadsheetSelection.parseColumnRange("B:C")
                                        .setDefaultAnchor(),
                                "B=text;B=text-case-insensitive"
                        )
                ),
                "SpreadsheetSortDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Sort\n" +
                        "    id=sort includeClose=true\n" +
                        "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                        "        ParserSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [B=text;B=text-case-insensitive] id=sort-columnOrRowComparatorNamesList-TextBox\n" +
                        "            Errors\n" +
                        "              Duplicate column B\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [B=text] id=sort-comparatorNames-0-TextBox\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [B=text-case-insensitive] id=sort-comparatorNames-1-TextBox\n" +
                        "              Errors\n" +
                        "                Duplicate Column B\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        \"Sort\" DISABLED id=sort-sort-Link\n" +
                        "        \"Close\" [#/1/spreadsheetName123/column/B:C/right] id=sort-close-Link\n"
        );
    }

    @Test
    public void testColumnGotRow() {
        this.onHistoryTokenChangeAndCheck(
                this.appContext(
                        HistoryToken.columnSortEdit(
                                ID,
                                NAME,
                                SpreadsheetSelection.parseColumnRange("B:C")
                                        .setDefaultAnchor(),
                                "B=text;2=text-case-insensitive"
                        )
                ),
                "SpreadsheetSortDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Sort\n" +
                        "    id=sort includeClose=true\n" +
                        "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                        "        ParserSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [B=text;2=text-case-insensitive] id=sort-columnOrRowComparatorNamesList-TextBox\n" +
                        "            Errors\n" +
                        "              Got Row 2 expected Column\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [B=text] id=sort-comparatorNames-0-TextBox\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [2=text-case-insensitive] id=sort-comparatorNames-1-TextBox\n" +
                        "              Errors\n" +
                        "                Got Row 2 expected Column\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        \"Sort\" DISABLED id=sort-sort-Link\n" +
                        "        \"Close\" [#/1/spreadsheetName123/column/B:C/right] id=sort-close-Link\n"
        );
    }

    @Test
    public void testRow() {
        this.onHistoryTokenChangeAndCheck(
                this.appContext(
                        HistoryToken.rowSortEdit(
                                ID,
                                NAME,
                                SpreadsheetSelection.parseRowRange("3:4")
                                        .setDefaultAnchor(),
                                "3=text"
                        )
                ),
                "SpreadsheetSortDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Sort\n" +
                        "    id=sort includeClose=true\n" +
                        "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                        "        ParserSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [3=text] id=sort-columnOrRowComparatorNamesList-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [3=text] id=sort-comparatorNames-0-TextBox\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [] id=sort-comparatorNames-1-TextBox\n" +
                        "              Errors\n" +
                        "                text is empty\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        \"Sort\" [#/1/spreadsheetName123/row/3:4/bottom/sort/save/3%3Dtext] id=sort-sort-Link\n" +
                        "        \"Close\" [#/1/spreadsheetName123/row/3:4/bottom] id=sort-close-Link\n"
        );
    }

    // only the 3rd SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent should have the duplicate Row error message.
    @Test
    public void testRowDuplicateRow() {
        this.onHistoryTokenChangeAndCheck(
                this.appContext(
                        HistoryToken.rowSortEdit(
                                ID,
                                NAME,
                                SpreadsheetSelection.parseRowRange("3:5")
                                        .setDefaultAnchor(),
                                "3=text;4=text;3=text-case-insensitive"
                        )
                ),
                "SpreadsheetSortDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Sort\n" +
                        "    id=sort includeClose=true\n" +
                        "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                        "        ParserSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [3=text;4=text;3=text-case-insensitive] id=sort-columnOrRowComparatorNamesList-TextBox\n" +
                        "            Errors\n" +
                        "              Duplicate row 3\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [3=text] id=sort-comparatorNames-0-TextBox\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [4=text] id=sort-comparatorNames-1-TextBox\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [3=text-case-insensitive] id=sort-comparatorNames-2-TextBox\n" +
                        "              Errors\n" +
                        "                Duplicate Row 3\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        \"Sort\" DISABLED id=sort-sort-Link\n" +
                        "        \"Close\" [#/1/spreadsheetName123/row/3:5/bottom] id=sort-close-Link\n"
        );
    }

    @Test
    public void testRowGotColumn() {
        this.onHistoryTokenChangeAndCheck(
                this.appContext(
                        HistoryToken.rowSortEdit(
                                ID,
                                NAME,
                                SpreadsheetSelection.parseRowRange("3:4")
                                        .setDefaultAnchor(),
                                "3=text;A=text"
                        )
                ),
                "SpreadsheetSortDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Sort\n" +
                        "    id=sort includeClose=true\n" +
                        "      SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent\n" +
                        "        ParserSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [3=text;A=text] id=sort-columnOrRowComparatorNamesList-TextBox\n" +
                        "            Errors\n" +
                        "              Got Column A expected Row\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [3=text] id=sort-comparatorNames-0-TextBox\n" +
                        "        SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "          ParserSpreadsheetTextBox\n" +
                        "            SpreadsheetTextBox\n" +
                        "              [A=text] id=sort-comparatorNames-1-TextBox\n" +
                        "              Errors\n" +
                        "                Got Column A expected Row\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        \"Sort\" DISABLED id=sort-sort-Link\n" +
                        "        \"Close\" [#/1/spreadsheetName123/row/3:4/bottom] id=sort-close-Link\n"
        );
    }

    private AppContext cellAppContext(final String cellRange,
                                      final String edit) {
        return this.appContext(
                HistoryToken.cellSortEdit(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCellRange(cellRange)
                                .setDefaultAnchor(),
                        edit
                )
        );
    }

    private AppContext appContext(final HistoryToken historyToken) {
        return new FakeAppContext() {

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public HistoryToken historyToken() {
                return historyToken;
            }

            @Override
            public SpreadsheetViewportCache spreadsheetViewportCache() {
                return SpreadsheetViewportCache.empty(this);
            }

            @Override
            public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                return null;
            }

            @Override
            public SpreadsheetComparatorProvider spreadsheetComparatorProvider() {
                return SpreadsheetComparatorProviders.builtIn();
            }
        };
    }

    private SpreadsheetSortDialogComponent dialog(final AppContext context) {
        return SpreadsheetSortDialogComponent.with(
                new FakeSpreadsheetSortDialogComponentContext() {

                    @Override
                    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                        return context.addHistoryTokenWatcher(watcher);
                    }

                    @Override
                    public HistoryToken historyToken() {
                        return context.historyToken();
                    }

                    @Override
                    public SpreadsheetComparator<?> spreadsheetComparator(final SpreadsheetComparatorName spreadsheetComparatorName) {
                        return context.spreadsheetComparatorProvider()
                                .spreadsheetComparator(spreadsheetComparatorName);
                    }

                    @Override
                    public Set<SpreadsheetComparatorInfo> spreadsheetComparatorInfos() {
                        return context.spreadsheetComparatorProvider()
                                .spreadsheetComparatorInfos();
                    }
                }
        );
    }

    private void onHistoryTokenChangeAndCheck(final AppContext context,
                                              final String expected) {
        this.onHistoryTokenChangeAndCheck(
                this.dialog(context),
                context,
                expected
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetSortDialogComponent> type() {
        return SpreadsheetSortDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
