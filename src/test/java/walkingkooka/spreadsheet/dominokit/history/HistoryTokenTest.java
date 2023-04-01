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
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReferenceRange;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReferenceRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class HistoryTokenTest implements ClassTesting<HistoryToken>, ParseStringTesting<Optional<HistoryToken>> {

    private final static SpreadsheetId ID = SpreadsheetId.parse("123");

    private final static SpreadsheetName NAME = SpreadsheetName.with("SpreadsheetName456");

    private static final SpreadsheetSelectHistoryToken SPREADSHEET_ID_SPREADSHEET_NAME_HHT = SpreadsheetHistoryToken.spreadsheetSelect(
            ID,
            NAME
    );

    private final static SpreadsheetCellReference CELL = SpreadsheetSelection.parseCell("A1");

    private static final SpreadsheetCellSelectHistoryToken CELL_HHT = SpreadsheetHistoryToken.cell(
            ID,
            NAME,
            CELL.setDefaultAnchor()
    );

    private final static SpreadsheetCellRange CELL_RANGE = SpreadsheetSelection.parseCellRange("B2:C3");

    private final static SpreadsheetLabelName LABEL = SpreadsheetSelection.labelName("Label123");

    private static final SpreadsheetLabelMappingSelectHistoryToken LABEL_MAPPING_HHT = SpreadsheetHistoryToken.labelMapping(
            ID,
            NAME,
            LABEL
    );

    private final static SpreadsheetColumnReference COLUMN = SpreadsheetSelection.parseColumn("AA");

    private final static SpreadsheetColumnReferenceRange COLUMN_RANGE = SpreadsheetSelection.parseColumnRange("BB:CC");

    private final static SpreadsheetRowReference ROW = SpreadsheetSelection.parseRow("11");

    private final static SpreadsheetRowReferenceRange ROW_RANGE = SpreadsheetSelection.parseRowRange("22:33");

    // parse............................................................................................................

    @Test
    public void testParseEmpty() {
        this.parseStringAndCheck(
                "",
                SpreadsheetHistoryToken.spreadsheetCreate()
        );
    }

    @Test
    public void testParseSlash() {
        this.parseStringAndCheck(
                "/",
                SpreadsheetHistoryToken.spreadsheetCreate()
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
                SpreadsheetHistoryToken.spreadsheetCreate()
        );
    }

    @Test
    public void testParseSpreadsheetId() {
        this.parseStringAndCheck(
                "/123",
                SpreadsheetHistoryToken.spreadsheetLoad(
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
                SpreadsheetHistoryToken.cell(
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
                SpreadsheetHistoryToken.cell(
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
                SpreadsheetHistoryToken.cell(
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
                SpreadsheetHistoryToken.cell(
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
                SpreadsheetHistoryToken.cell(
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
                SpreadsheetHistoryToken.cellClear(
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
                SpreadsheetHistoryToken.cellDelete(
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
                SpreadsheetHistoryToken.cell(
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
                SpreadsheetHistoryToken.cell(
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
                SpreadsheetHistoryToken.cellFreeze(
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
                SpreadsheetHistoryToken.cell(
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
                SpreadsheetHistoryToken.cell(
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
                SpreadsheetHistoryToken.cellFreeze(
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
                SpreadsheetHistoryToken.cellMenu(
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
                SpreadsheetHistoryToken.cell(
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
                SpreadsheetHistoryToken.cell(
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
                SpreadsheetHistoryToken.cellUnfreeze(
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
                SpreadsheetHistoryToken.cell(
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
                SpreadsheetHistoryToken.cell(
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
                SpreadsheetHistoryToken.cellUnfreeze(
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
                SpreadsheetHistoryToken.cellUnfreeze(
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
                SpreadsheetHistoryToken.cellClear(
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
                SpreadsheetHistoryToken.cellClear(
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
                SpreadsheetHistoryToken.formula(
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
                SpreadsheetHistoryToken.formulaSave(
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
                SpreadsheetHistoryToken.cellPattern(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetPatternKind.DATE_FORMAT_PATTERN
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellPatternSaveDateFormat() {
        final String pattern = "yyyymmdd";

        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/pattern/date-format/save/" + pattern,
                SpreadsheetHistoryToken.cellPatternSave(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetPattern.parseDateFormatPattern("yyyymmdd")
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellPatternSaveTimeParse() {
        final String pattern = "hh:mm:ss";

        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/pattern/time-parse/save/" + pattern,
                SpreadsheetHistoryToken.cellPatternSave(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetPattern.parseTimeParsePattern(pattern)
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
                SpreadsheetHistoryToken.cellStyle(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        TextStylePropertyName.COLOR
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellStyleSave() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/style/color/save/#123456",
                SpreadsheetHistoryToken.cellStyleSave(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        TextStylePropertyName.COLOR,
                        Color.parse("#123456")
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
                SpreadsheetHistoryToken.column(
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
                SpreadsheetHistoryToken.column(
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
                SpreadsheetHistoryToken.column(
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
                SpreadsheetHistoryToken.column(
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
                SpreadsheetHistoryToken.column(
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
                SpreadsheetHistoryToken.column(
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
                SpreadsheetHistoryToken.column(
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
                SpreadsheetHistoryToken.columnClear(
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
                SpreadsheetHistoryToken.columnDelete(
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
                SpreadsheetHistoryToken.column(
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
                SpreadsheetHistoryToken.columnFreeze(
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
                SpreadsheetHistoryToken.column(
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
                SpreadsheetHistoryToken.columnFreeze(
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
                SpreadsheetHistoryToken.columnMenu(
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
                SpreadsheetHistoryToken.column(
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
                SpreadsheetHistoryToken.columnUnfreeze(
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
                SpreadsheetHistoryToken.columnUnfreeze(
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
                SpreadsheetHistoryToken.column(
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
                SpreadsheetHistoryToken.column(
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
                SpreadsheetHistoryToken.column(
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
                SpreadsheetHistoryToken.row(
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
                SpreadsheetHistoryToken.row(
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
                SpreadsheetHistoryToken.row(
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
                SpreadsheetHistoryToken.row(
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
                SpreadsheetHistoryToken.row(
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
                SpreadsheetHistoryToken.row(
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
                SpreadsheetHistoryToken.row(
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
                SpreadsheetHistoryToken.rowClear(
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
                SpreadsheetHistoryToken.rowDelete(
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
                SpreadsheetHistoryToken.row(
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
                SpreadsheetHistoryToken.rowFreeze(
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
                SpreadsheetHistoryToken.row(
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
                SpreadsheetHistoryToken.rowFreeze(
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
                SpreadsheetHistoryToken.rowMenu(
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
                SpreadsheetHistoryToken.row(
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
                SpreadsheetHistoryToken.rowUnfreeze(
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
                SpreadsheetHistoryToken.row(
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
                SpreadsheetHistoryToken.rowUnfreeze(
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
                SpreadsheetHistoryToken.rowUnfreeze(
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
                SpreadsheetHistoryToken.row(
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
                SpreadsheetHistoryToken.row(
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
                SpreadsheetHistoryToken.row(
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
                SpreadsheetHistoryToken.labelMappingDelete(
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
                SpreadsheetHistoryToken.labelMappingDelete(
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
                SpreadsheetHistoryToken.labelMapping(
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
                SpreadsheetHistoryToken.labelMapping(
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
                SpreadsheetHistoryToken.labelMappingSave(
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
                SpreadsheetHistoryToken.labelMappingSave(
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
                SpreadsheetHistoryToken.labelMappingSave(
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

    // parse helpers....................................................................................................

    private void parseStringAndCheck(final String urlFragment) {
        this.parseStringAndCheck(
                urlFragment,
                Optional.empty()
        );
    }

    private void parseStringAndCheck(final String urlFragment,
                                     final HistoryToken expected) {
        this.parseStringAndCheck(
                urlFragment,
                Optional.of(expected)
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
    public Optional<HistoryToken> parseString(final String urlFragment) {
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
