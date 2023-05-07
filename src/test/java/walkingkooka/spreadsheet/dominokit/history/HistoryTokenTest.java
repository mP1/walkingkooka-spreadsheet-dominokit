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
import walkingkooka.color.Color;
import walkingkooka.net.UrlFragment;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReferenceRange;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReferenceRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HistoryTokenTest implements ClassTesting<HistoryToken>, ParseStringTesting<HistoryToken> {

    private final static SpreadsheetId ID = SpreadsheetId.parse("123");

    private final static SpreadsheetName NAME = SpreadsheetName.with("SpreadsheetName456");

    private static final SpreadsheetSelectHistoryToken SPREADSHEET_ID_SPREADSHEET_NAME_HHT = HistoryToken.spreadsheetSelect(
            ID,
            NAME
    );

    private final static SpreadsheetCellReference CELL = SpreadsheetSelection.A1;

    private static final SpreadsheetCellSelectHistoryToken CELL_HHT = HistoryToken.cell(
            ID,
            NAME,
            CELL.setDefaultAnchor()
    );

    private final static SpreadsheetCellRange CELL_RANGE = SpreadsheetSelection.parseCellRange("B2:C3");

    private final static SpreadsheetLabelName LABEL = SpreadsheetSelection.labelName("Label123");

    private static final SpreadsheetLabelMappingSelectHistoryToken LABEL_MAPPING_HHT = HistoryToken.labelMapping(
            ID,
            NAME,
            LABEL
    );

    private final static SpreadsheetColumnReference COLUMN = SpreadsheetSelection.parseColumn("AA");

    private final static SpreadsheetColumnReferenceRange COLUMN_RANGE = SpreadsheetSelection.parseColumnRange("BB:CC");

    private final static SpreadsheetRowReference ROW = SpreadsheetSelection.parseRow("11");

    private final static SpreadsheetRowReferenceRange ROW_RANGE = SpreadsheetSelection.parseRowRange("22:33");

    // menuHistoryToken.................................................................................................

    @Test
    public void testMenuHistoryTokenWithNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> HistoryToken.unknown(UrlFragment.EMPTY)
                        .menuHistoryToken(null)
        );
    }

    @Test
    public void testMenuHistoryTokenWithMissingSelection() {
        this.menuHistoryTokenAndCheck(
                HistoryToken.spreadsheetSelect(
                        ID,
                        NAME
                ),
                CELL,
                HistoryToken.cellMenu(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    // menuHistoryToken cell............................................................................................

    @Test
    public void testMenuHistoryTokenWithoutCellSelectAndCellSelection() {
        this.menuHistoryTokenAndCheck(
                HistoryToken.spreadsheetSelect(
                        ID,
                        NAME
                ),
                CELL,
                HistoryToken.cellMenu(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testMenuHistoryTokenWithColumnRangeSelectAndCellSelection() {
        this.menuHistoryTokenAndCheck(
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT)
                ),
                CELL,
                HistoryToken.cellMenu(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testMenuHistoryTokenWithCellSelectAndSameCellSelection() {
        this.menuHistoryTokenAndCheck(
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                ),
                CELL,
                HistoryToken.cellMenu(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testMenuHistoryTokenWithCellSelectAndDifferentCellSelection() {
        final SpreadsheetCellReference differentCell = SpreadsheetSelection.parseCell("B2");

        this.menuHistoryTokenAndCheck(
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                ),
                differentCell,
                HistoryToken.cellMenu(
                        ID,
                        NAME,
                        differentCell.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testMenuHistoryTokenWithCellRangeSelectAndInsideCellSelection() {
        final SpreadsheetViewportSelection range = SpreadsheetSelection.parseCellRange("A1:C3")
                .setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM_RIGHT);

        this.menuHistoryTokenAndCheck(
                HistoryToken.cell(
                        ID,
                        NAME,
                        range
                ),
                SpreadsheetSelection.A1,
                HistoryToken.cellMenu(
                        ID,
                        NAME,
                        range
                )
        );
    }

    @Test
    public void testMenuHistoryTokenWithCellRangeSelectAndOutsideCellSelection() {
        final SpreadsheetCellReference differentCell = SpreadsheetSelection.parseCell("C3");

        this.menuHistoryTokenAndCheck(
                HistoryToken.cell(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCellRange("A1:B2").setDefaultAnchor()
                ),
                differentCell,
                HistoryToken.cellMenu(
                        ID,
                        NAME,
                        differentCell.setDefaultAnchor()
                )
        );
    }

    // menuHistoryToken column..........................................................................................

    @Test
    public void testMenuHistoryTokenWithoutColumnSelectAndColumnSelection() {
        this.menuHistoryTokenAndCheck(
                HistoryToken.spreadsheetSelect(
                        ID,
                        NAME
                ),
                COLUMN,
                HistoryToken.columnMenu(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testMenuHistoryTokenWithRowRangeSelectAndColumnSelection() {
        this.menuHistoryTokenAndCheck(
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM)
                ),
                COLUMN,
                HistoryToken.columnMenu(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testMenuHistoryTokenWithColumnSelectAndSameColumnSelection() {
        this.menuHistoryTokenAndCheck(
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                ),
                COLUMN,
                HistoryToken.columnMenu(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testMenuHistoryTokenWithColumnSelectAndDifferentColumnSelection() {
        final SpreadsheetColumnReference differentColumn = SpreadsheetSelection.parseColumn("B");

        this.menuHistoryTokenAndCheck(
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                ),
                differentColumn,
                HistoryToken.columnMenu(
                        ID,
                        NAME,
                        differentColumn.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testMenuHistoryTokenWithColumnRangeSelectAndInsideColumnSelection() {
        final SpreadsheetViewportSelection range = SpreadsheetSelection.parseColumnRange("A:C")
                .setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT);

        this.menuHistoryTokenAndCheck(
                HistoryToken.column(
                        ID,
                        NAME,
                        range
                ),
                SpreadsheetSelection.A1,
                HistoryToken.columnMenu(
                        ID,
                        NAME,
                        range
                )
        );
    }

    @Test
    public void testMenuHistoryTokenWithColumnRangeSelectAndOutsideColumnSelection() {
        final SpreadsheetColumnReference differentColumn = SpreadsheetSelection.parseColumn("C");

        this.menuHistoryTokenAndCheck(
                HistoryToken.column(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseColumnRange("A:B").setDefaultAnchor()
                ),
                differentColumn,
                HistoryToken.columnMenu(
                        ID,
                        NAME,
                        differentColumn.setDefaultAnchor()
                )
        );
    }

    // menuHistoryToken row.............................................................................................

    @Test
    public void testMenuHistoryTokenWithoutRowSelectAndRowSelection() {
        this.menuHistoryTokenAndCheck(
                HistoryToken.spreadsheetSelect(
                        ID,
                        NAME
                ),
                ROW,
                HistoryToken.rowMenu(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testMenuHistoryTokenWithCellSelectAndRowSelection() {
        this.menuHistoryTokenAndCheck(
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                ),
                ROW,
                HistoryToken.rowMenu(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testMenuHistoryTokenWithRowSelectAndSameRowSelection() {
        this.menuHistoryTokenAndCheck(
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor()
                ),
                ROW,
                HistoryToken.rowMenu(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testMenuHistoryTokenWithRowSelectAndDifferentRowSelection() {
        final SpreadsheetRowReference differentRow = SpreadsheetSelection.parseRow("2");

        this.menuHistoryTokenAndCheck(
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor()
                ),
                differentRow,
                HistoryToken.rowMenu(
                        ID,
                        NAME,
                        differentRow.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testMenuHistoryTokenWithRowRangeSelectAndInsideRowSelection() {
        final SpreadsheetViewportSelection range = SpreadsheetSelection.parseRowRange("1:3")
                .setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM);

        this.menuHistoryTokenAndCheck(
                HistoryToken.row(
                        ID,
                        NAME,
                        range
                ),
                SpreadsheetSelection.A1,
                HistoryToken.rowMenu(
                        ID,
                        NAME,
                        range
                )
        );
    }

    @Test
    public void testMenuHistoryTokenWithRowRangeSelectAndOutsideRowSelection() {
        final SpreadsheetRowReference differentRow = SpreadsheetSelection.parseRow("3");

        this.menuHistoryTokenAndCheck(
                HistoryToken.row(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseRowRange("1:2").setDefaultAnchor()
                ),
                differentRow,
                HistoryToken.rowMenu(
                        ID,
                        NAME,
                        differentRow.setDefaultAnchor()
                )
        );
    }

    private void menuHistoryTokenAndCheck(final HistoryToken token,
                                          final SpreadsheetSelection selection,
                                          final HistoryToken expected) {
        this.checkEquals(
                expected,
                token.menuHistoryToken(selection),
                () -> token + " menuHistoryToken " + selection
        );
    }

    // setAnchor........................................................................................................

    @Test
    public void testSetAnchorNotSpreadsheetViewportSelectionHistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
                historyToken.setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM),
                historyToken
        );
    }

    @Test
    public void testSetAnchor() {
        this.checkEquals(
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.LEFT)
                ).setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT),
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT)
                )
        );
    }

    // setCell..........................................................................................................

    @Test
    public void testSetCellNotSpreadsheetNameistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
                historyToken.setCell(CELL),
                historyToken
        );
    }

    @Test
    public void testSetCellNotCellFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> HistoryToken.spreadsheetSelect(ID, NAME).setCell(COLUMN)
        );
    }

    @Test
    public void testSeCell() {
        final HistoryToken historyToken = HistoryToken.spreadsheetSelect(ID, NAME);

        this.checkEquals(
                historyToken.setCell(CELL),
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    // setColumn........................................................................................................

    @Test
    public void testSetColumnNotSpreadsheetNameHistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
                historyToken.setColumn(COLUMN),
                historyToken
        );
    }

    @Test
    public void testSetColumnWithCellFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> HistoryToken.spreadsheetSelect(ID, NAME).setColumn(CELL)
        );
    }

    @Test
    public void testSeColumn() {
        final HistoryToken historyToken = HistoryToken.spreadsheetSelect(ID, NAME);

        this.checkEquals(
                historyToken.setColumn(COLUMN),
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                )
        );
    }

    // setLabelMapping..................................................................................................

    @Test
    public void testSetLabelMappingNotSpreadsheetNameHistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
                historyToken.setLabelMapping(LABEL),
                historyToken
        );
    }

    @Test
    public void testSeLabelMapping() {
        final HistoryToken historyToken = HistoryToken.spreadsheetSelect(ID, NAME);

        this.checkEquals(
                historyToken.setLabelMapping(LABEL),
                HistoryToken.labelMapping(
                        ID,
                        NAME,
                        LABEL
                )
        );
    }

    // setRow........................................................................................................

    @Test
    public void testSetRowNotSpreadsheetNameHistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
                historyToken.setRow(ROW),
                historyToken
        );
    }

    @Test
    public void testSetRowWithCellFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> HistoryToken.spreadsheetSelect(ID, NAME).setRow(CELL)
        );
    }

    @Test
    public void testSeRow() {
        final HistoryToken historyToken = HistoryToken.spreadsheetSelect(ID, NAME);

        this.checkEquals(
                historyToken.setRow(ROW),
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor()
                )
        );
    }

    // viewportSelection................................................................................................

    @Test
    public void testViewportSelectionCell() {
        final SpreadsheetViewportSelection cell = CELL.setDefaultAnchor();

        final SpreadsheetViewportSelectionHistoryToken historyToken = HistoryToken.viewportSelection(
                ID,
                NAME,
                cell
        );

        this.checkEquals(
                HistoryToken.cell(
                        ID,
                        NAME,
                        cell
                ),
                historyToken
        );
    }

    @Test
    public void testViewportSelectionCellRange() {
        final SpreadsheetViewportSelection cell = CELL_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM_RIGHT);

        final SpreadsheetViewportSelectionHistoryToken historyToken = HistoryToken.viewportSelection(
                ID,
                NAME,
                cell
        );

        this.checkEquals(
                HistoryToken.cell(
                        ID,
                        NAME,
                        cell
                ),
                historyToken
        );
    }

    @Test
    public void testViewportSelectionLabel() {
        final SpreadsheetViewportSelection cell = LABEL.setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM_RIGHT);

        final SpreadsheetViewportSelectionHistoryToken historyToken = HistoryToken.viewportSelection(
                ID,
                NAME,
                cell
        );

        this.checkEquals(
                HistoryToken.cell(
                        ID,
                        NAME,
                        cell
                ),
                historyToken
        );
    }

    @Test
    public void testViewportSelectionColumn() {
        final SpreadsheetViewportSelection column = COLUMN.setDefaultAnchor();

        final SpreadsheetViewportSelectionHistoryToken historyToken = HistoryToken.viewportSelection(
                ID,
                NAME,
                column
        );

        this.checkEquals(
                HistoryToken.column(
                        ID,
                        NAME,
                        column
                ),
                historyToken
        );
    }

    @Test
    public void testViewportSelectionColumnRange() {
        final SpreadsheetViewportSelection column = COLUMN_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT);

        final SpreadsheetViewportSelectionHistoryToken historyToken = HistoryToken.viewportSelection(
                ID,
                NAME,
                column
        );

        this.checkEquals(
                HistoryToken.column(
                        ID,
                        NAME,
                        column
                ),
                historyToken
        );
    }

    @Test
    public void testViewportSelectionRow() {
        final SpreadsheetViewportSelection row = ROW.setDefaultAnchor();

        final SpreadsheetViewportSelectionHistoryToken historyToken = HistoryToken.viewportSelection(
                ID,
                NAME,
                row
        );

        this.checkEquals(
                HistoryToken.row(
                        ID,
                        NAME,
                        row
                ),
                historyToken
        );
    }

    @Test
    public void testViewportSelectionRowRange() {
        final SpreadsheetViewportSelection row = ROW_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM);

        final SpreadsheetViewportSelectionHistoryToken historyToken = HistoryToken.viewportSelection(
                ID,
                NAME,
                row
        );

        this.checkEquals(
                HistoryToken.row(
                        ID,
                        NAME,
                        row
                ),
                historyToken
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseUnknown() {
        this.parseStringAndCheck("hello");
    }

    @Test
    public void testParseEmpty() {
        this.parseStringAndCheck(
                "",
                HistoryToken.spreadsheetCreate()
        );
    }

    @Test
    public void testParseSlash() {
        this.parseStringAndCheck(
                "/",
                HistoryToken.spreadsheetCreate()
        );
    }

    @Override
    public void testParseStringEmptyFails() {
        // nop
    }

    @Test
    public void testParseInvalidSpreadsheetId() {
        this.parseStringAndCheck(
                "/XYZ",
                HistoryToken.spreadsheetCreate()
        );
    }

    @Test
    public void testParseSpreadsheetId() {
        this.parseStringAndCheck(
                "/123",
                HistoryToken.spreadsheetLoad(
                        ID
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetName() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456",
                SPREADSHEET_ID_SPREADSHEET_NAME_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameUnknown() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/Unknown",
                SPREADSHEET_ID_SPREADSHEET_NAME_HHT
        );
    }

    // cell.............................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellMissingReference() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell",
                SPREADSHEET_ID_SPREADSHEET_NAME_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellInvalidReference() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/!!!",
                SPREADSHEET_ID_SPREADSHEET_NAME_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellInvalidReference2() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/!!!/cell/A1",
                SPREADSHEET_ID_SPREADSHEET_NAME_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellCellReference() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1",
                CELL_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellInvalidAnchor() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/bottom-left",
                CELL_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellLabel() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/Label123",
                HistoryToken.cell(
                        ID,
                        NAME,
                        LABEL.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellRangeMissingAnchor() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/B2:C3",
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM_RIGHT)
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellRangeTopLeft() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/B2:C3/top-left",
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.TOP_LEFT)
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellRangeTopRight() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/B2:C3/top-right",
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.TOP_RIGHT)
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellRangeInvalidAnchor() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/B2:C3/left",
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM_RIGHT)
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellInvalidAction() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/!invalid",
                CELL_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellClear() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/clear",
                HistoryToken.cellClear(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellDelete() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/delete",
                HistoryToken.cellDelete(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellFreezeInvalidColumnFails() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/B1/freeze",
                HistoryToken.cell(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCell("B1").setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellFreezeInvalidRowFails() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A2/freeze",
                HistoryToken.cell(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCell("A2").setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellFreeze() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/freeze",
                HistoryToken.cellFreeze(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellRangeFreezeInvalidColumnFails() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/B1:C3/freeze",
                HistoryToken.cell(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCellRange("B1:C3").setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellRangeFreezeInvalidRowFails() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A2:C3/freeze",
                HistoryToken.cell(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCellRange("A2:C3").setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellRangeFreeze() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1:B2/freeze",
                HistoryToken.cellFreeze(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCellRange("A1:B2").setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellMenu() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/menu",
                HistoryToken.cellMenu(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellRangeUnfreezeInvalidColumn() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/B2:C3/unfreeze",
                HistoryToken.cell(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCellRange("B2:C3")
                                .setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellRangeUnfreezeInvalidRow() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A2:C3/unfreeze",
                HistoryToken.cell(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCellRange("A2:C3")
                                .setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellRangeUnfreeze() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1:B2/unfreeze",
                HistoryToken.cellUnfreeze(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCellRange("A1:B2")
                                .setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellUnfreezeInvalidColumn() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/B2/unfreeze",
                HistoryToken.cell(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCell("B2")
                                .setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellUnfreezeInvalidRow() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A2/unfreeze",
                HistoryToken.cell(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCell("A2")
                                .setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellUnfreeze() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/unfreeze",
                HistoryToken.cellUnfreeze(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellUnfreezeExtra() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/unfreeze/extra",
                HistoryToken.cellUnfreeze(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellRangeClear() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/B2:C3/clear",
                HistoryToken.cellClear(
                        ID,
                        NAME,
                        CELL_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM_RIGHT)
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellRangeAnchorClear() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/B2:C3/top-right/clear",
                HistoryToken.cellClear(
                        ID,
                        NAME,
                        CELL_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.TOP_RIGHT)
                )
        );
    }

    // cell/formula.....................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellFormula() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/formula",
                HistoryToken.formula(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellFormulaSave() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/formula/save/=1+2",
                HistoryToken.formulaSave(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetFormula.EMPTY.setText("=1+2")
                )
        );
    }

    // cell/pattern.......................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellPatternMissingPatternKind() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/pattern",
                CELL_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellPatternInvalidPatternKind() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/pattern/!invalid",
                CELL_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellPatternPatternKind() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/pattern/date-format",
                HistoryToken.cellPattern(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetPatternKind.DATE_FORMAT_PATTERN
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellPatternSaveEmptyDateFormat() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/pattern/date-format/save/",
                HistoryToken.cellPatternSave(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetPatternKind.DATE_FORMAT_PATTERN,
                        Optional.empty()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellPatternSaveDateFormat() {
        final String pattern = "yyyymmdd";

        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/pattern/date-format/save/" + pattern,
                HistoryToken.cellPatternSave(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetPatternKind.DATE_FORMAT_PATTERN,
                        Optional.of(
                                SpreadsheetPattern.parseDateFormatPattern("yyyymmdd")
                        )
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellPatternSaveTimeParse() {
        final String pattern = "hh:mm:ss";

        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/pattern/time-parse/save/" + pattern,
                HistoryToken.cellPatternSave(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetPatternKind.TIME_PARSE_PATTERN,
                        Optional.of(
                                SpreadsheetPattern.parseTimeParsePattern(pattern)
                        )
                )
        );
    }

    // cell/style.......................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellStyleMissingStyleProperty() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/style",
                CELL_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellStyleInvalidPropertyName() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/style/!invalid",
                CELL_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellStyleStylePropertyName() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/style/color",
                HistoryToken.cellStyle(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        TextStylePropertyName.COLOR
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellStyleSavWithoutValue() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/style/color/save/",
                HistoryToken.cellStyleSave(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        TextStylePropertyName.COLOR,
                        Optional.empty()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellStyleSave() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/style/color/save/#123456",
                HistoryToken.cellStyleSave(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        TextStylePropertyName.COLOR,
                        Optional.of(
                                Color.parse("#123456")
                        )
                )
        );
    }

    // column.............................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnMissingReference() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column",
                SPREADSHEET_ID_SPREADSHEET_NAME_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnInvalidReference() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/!invalid",
                SPREADSHEET_ID_SPREADSHEET_NAME_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnInvalidReference2() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/!invalid/column/A",
                SPREADSHEET_ID_SPREADSHEET_NAME_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnColumnReference() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/AA",
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnInvalidAnchor() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/AA/bottom-left",
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnRangeMissingAnchor() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/BB:CC",
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT)
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnRangeLeft() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/BB:CC/left",
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.LEFT)
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnRangeRight() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/BB:CC/right",
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT)
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnRangeInvalidAnchor() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/BB:CC/top-left",
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT)
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnInvalidAction() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/AA/!invalid",
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnClear() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/AA/clear",
                HistoryToken.columnClear(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnDelete() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/AA/delete",
                HistoryToken.columnDelete(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnFreezeInvalidRemoved() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/AA/freeze",
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnFreeze() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/A/freeze",
                HistoryToken.columnFreeze(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseColumn("A")
                                .setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnRangeFreezeInvalidRemoved() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/AA:BB/freeze",
                HistoryToken.column(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseColumnRange("AA:BB")
                                .setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT)
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnRangeFreeze() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/A:B/freeze",
                HistoryToken.columnFreeze(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseColumnRange("A:B")
                                .setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT)
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnMenu() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/AA/menu",
                HistoryToken.columnMenu(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnUnfreezeInvalidFails() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/B/unfreeze",
                HistoryToken.column(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseColumn("B").setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnUnfreeze() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/A/unfreeze",
                HistoryToken.columnUnfreeze(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseColumn("A").setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnUnfreezeExtra() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/A/unfreeze/extra",
                HistoryToken.columnUnfreeze(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseColumn("A")
                                .setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnFormula() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/AA",
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnPattern() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/AA/pattern/date-format/yymmdd",
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnStyle() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/AA/style",
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                )
        );
    }

    // row.............................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowMissingReference() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row",
                SPREADSHEET_ID_SPREADSHEET_NAME_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowInvalidReference() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/A1",
                SPREADSHEET_ID_SPREADSHEET_NAME_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowInvalidReference2() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/123456789/row/1",
                SPREADSHEET_ID_SPREADSHEET_NAME_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowRowReference() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/11",
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowInvalidAnchor() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/11/bottom-left",
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowRangeMissingAnchor() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/22:33",
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM)
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowRangeTop() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/22:33/top",
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.TOP)
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowRangeBottom() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/22:33/bottom",
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM)
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowRangeInvalidAnchor() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/22:33/top-left",
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM)
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowInvalidAction() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/11/!invalid",
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowClear() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/11/clear",
                HistoryToken.rowClear(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowDelete() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/11/delete",
                HistoryToken.rowDelete(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowFreezeInvalidFails() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/11/freeze",
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowFreeze() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/1/freeze",
                HistoryToken.rowFreeze(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseRow("1").setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowRangeFreezeInvalidFails() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/22:33/freeze",
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW_RANGE.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowRangeFreeze() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/1:2/freeze",
                HistoryToken.rowFreeze(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseRowRange("1:2").setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowMenu() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/11/menu",
                HistoryToken.rowMenu(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowUnfreezeInvalid() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/11/unfreeze",
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowUnfreeze() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/1/unfreeze",
                HistoryToken.rowUnfreeze(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseRow("1").setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowRangeUnfreezeInvalid() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/22:33/unfreeze",
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW_RANGE.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowRangeUnfreeze() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/1/unfreeze",
                HistoryToken.rowUnfreeze(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseRow("1").setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowUnfreezeExtra() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/1/unfreeze/extra",
                HistoryToken.rowUnfreeze(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseRow("1").setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowFormula() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/11",
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowPattern() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/11/pattern/date-format/yymmdd",
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowStyle() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/11/style",
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor()
                )
        );
    }

    // label............................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelMissingName() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/label",
                SPREADSHEET_ID_SPREADSHEET_NAME_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelInvalid() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/label/!!/cell/A1",
                SPREADSHEET_ID_SPREADSHEET_NAME_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelLabelReference() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/label/Label123",
                LABEL_MAPPING_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelInvalidAction() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/label/Label123/!invalid",
                LABEL_MAPPING_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelClear() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/label/Label123/clear",
                LABEL_MAPPING_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelDelete() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/label/Label123/delete",
                HistoryToken.labelMappingDelete(
                        ID,
                        NAME,
                        LABEL
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelFreeze() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/label/Label123/freeze",
                LABEL_MAPPING_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelMenu() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/label/Label123/menu",
                LABEL_MAPPING_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelUnfreeze() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/label/Label123/unfreeze",
                LABEL_MAPPING_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelDeleteExtra() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/label/Label123/delete/extra",
                HistoryToken.labelMappingDelete(
                        ID,
                        NAME,
                        LABEL
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelSaveMissingReference() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/label/Label123/save",
                HistoryToken.labelMapping(
                        ID,
                        NAME,
                        LABEL
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelSaveInvalidReference() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/label/Label123/save/!invalid",
                HistoryToken.labelMapping(
                        ID,
                        NAME,
                        LABEL
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelSaveCell() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/label/Label123/save/A1",
                HistoryToken.labelMappingSave(
                        ID,
                        NAME,
                        LABEL.mapping(CELL)
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelSaveCellRange() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/label/Label123/save/B2:C3",
                HistoryToken.labelMappingSave(
                        ID,
                        NAME,
                        LABEL.mapping(CELL_RANGE)
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelSaveLabel() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/label/Label123/save/Label456",
                HistoryToken.labelMappingSave(
                        ID,
                        NAME,
                        LABEL.mapping(
                                SpreadsheetSelection.labelName("Label456")
                        )
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelFormula() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/label/Label123",
                LABEL_MAPPING_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelPattern() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/label/Label123/pattern/date-format/yymmdd",
                LABEL_MAPPING_HHT
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelStyle() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/label/Label123/style",
                LABEL_MAPPING_HHT
        );
    }

    // metadata.........................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameMetadata() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/metadata",
                HistoryToken.metadataSelect(
                        ID,
                        NAME
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameMetadataPropertyNameInvalid() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/metadata/!invalid",
                HistoryToken.metadataSelect(
                        ID,
                        NAME
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameMetadataPropertyName() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/metadata/decimal-separator",
                HistoryToken.metadataPropertySelect(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.DECIMAL_SEPARATOR
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameMetadataPropertyNameSaveInvalid() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/metadata/decimal-separator/save/123",
                HistoryToken.metadataPropertySelect(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.DECIMAL_SEPARATOR
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameMetadataPropertyNameSaveWithoutValue() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/metadata/decimal-separator/save/",
                HistoryToken.metadataPropertySave(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.DECIMAL_SEPARATOR,
                        Optional.empty()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameMetadataPropertyNameSave() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/metadata/decimal-separator/save/.",
                HistoryToken.metadataPropertySave(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.DECIMAL_SEPARATOR,
                        Optional.of(
                                '.'
                        )
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameMetadataStylePropertyNameInvalid() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/metadata/style/!invalid",
                HistoryToken.metadataSelect(
                        ID,
                        NAME
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameMetadataStylePropertyName() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/metadata/style/color",
                HistoryToken.metadataPropertyStyle(
                        ID,
                        NAME,
                        TextStylePropertyName.COLOR
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameMetadataStylePropertyNameSaveWithoutValue() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/metadata/style/color/save/",
                HistoryToken.metadataPropertyStyleSave(
                        ID,
                        NAME,
                        TextStylePropertyName.COLOR,
                        Optional.empty()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameMetadataStylePropertyNameSave() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/metadata/style/color/save/#123456",
                HistoryToken.metadataPropertyStyleSave(
                        ID,
                        NAME,
                        TextStylePropertyName.COLOR,
                        Optional.of(
                                Color.parse("#123456")
                        )
                )
        );
    }

    // parse helpers....................................................................................................

    private void parseStringAndCheck(final String urlFragment) {
        this.parseStringAndCheck(
                urlFragment,
                UnknownHistoryToken.with(
                        UrlFragment.parse(urlFragment)
                )
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<HistoryToken> type() {
        return HistoryToken.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // ClassTesting.....................................................................................................

    @Override
    public HistoryToken parseString(final String urlFragment) {
        return HistoryToken.parse(
                UrlFragment.with(urlFragment)
        );
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> thrown) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException thrown) {
        throw new UnsupportedOperationException();
    }
}
