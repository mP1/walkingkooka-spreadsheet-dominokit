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
import walkingkooka.collect.set.Sets;
import walkingkooka.color.Color;
import walkingkooka.net.UrlFragment;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.ui.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReferenceRange;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReferenceRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;
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
            Optional.of(LABEL)
    );

    private final static SpreadsheetColumnReference COLUMN = SpreadsheetSelection.parseColumn("AA");

    private final static SpreadsheetColumnReferenceRange COLUMN_RANGE = SpreadsheetSelection.parseColumnRange("BB:CC");

    private final static SpreadsheetRowReference ROW = SpreadsheetSelection.parseRow("11");

    private final static SpreadsheetRowReferenceRange ROW_RANGE = SpreadsheetSelection.parseRowRange("22:33");

    // nonLabelSelection................................................................................................

    @Test
    public void testNonLabelSelectionNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> HistoryToken.unknown(UrlFragment.SLASH)
                        .nonLabelSelection(null)
        );
    }

    @Test
    public void testNonLabelSelectionCell() {
        this.nonLabelSelectionAndCheck(CELL);
    }

    @Test
    public void testNonLabelSelectionCellRange() {
        this.nonLabelSelectionAndCheck(CELL_RANGE);
    }

    @Test
    public void testNonLabelSelectionLabel() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();
        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY.setLabels(
                        Sets.of(
                                LABEL.mapping(CELL)
                        )
                ),
                new FakeAppContext()
        );

        this.nonLabelSelectionAndCheck(
                HistoryToken.cell(
                        ID,
                        NAME,
                        LABEL.setDefaultAnchor()
                ),
                cache,
                CELL
        );
    }

    @Test
    public void testNonLabelSelectionUnknownLabel() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        this.nonLabelSelectionAndCheck(
                HistoryToken.cell(
                        ID,
                        NAME,
                        LABEL.setDefaultAnchor()
                ),
                cache
        );
    }

    @Test
    public void testNonLabelSelectionColumn() {
        this.nonLabelSelectionAndCheck(COLUMN);
    }

    @Test
    public void testNonLabelSelectionColumnRange() {
        this.nonLabelSelectionAndCheck(COLUMN_RANGE);
    }

    @Test
    public void testNonLabelSelectionRow() {
        this.nonLabelSelectionAndCheck(ROW);
    }

    @Test
    public void testNonLabelSelectionRowRange() {
        this.nonLabelSelectionAndCheck(ROW_RANGE);
    }

    private void nonLabelSelectionAndCheck(final SpreadsheetSelection selection) {
        this.nonLabelSelectionAndCheck(
                HistoryToken.selection(
                        ID,
                        NAME,
                        selection.setDefaultAnchor()
                ),
                SpreadsheetViewportCache.empty(),
                selection
        );
    }

    private void nonLabelSelectionAndCheck(final HistoryToken token,
                                           final SpreadsheetViewportCache viewportCache) {
        this.nonLabelSelectionAndCheck(
                token,
                viewportCache,
                Optional.empty()
        );
    }

    private void nonLabelSelectionAndCheck(final HistoryToken token,
                                           final SpreadsheetViewportCache viewportCache,
                                           final SpreadsheetSelection expected) {
        this.nonLabelSelectionAndCheck(
                token,
                viewportCache,
                Optional.of(expected)
        );
    }

    private void nonLabelSelectionAndCheck(final HistoryToken token,
                                           final SpreadsheetViewportCache viewportCache,
                                           final Optional<SpreadsheetSelection> expected) {
        this.checkEquals(
                expected,
                token.nonLabelSelection(viewportCache)
        );
    }
    // setAnchor........................................................................................................

    @Test
    public void testSetAnchorNotSpreadsheetViewportHistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
                historyToken.setAnchor(SpreadsheetViewportAnchor.BOTTOM),
                historyToken
        );
    }

    @Test
    public void testSetAnchor() {
        this.checkEquals(
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN_RANGE.setAnchor(SpreadsheetViewportAnchor.LEFT)
                ).setAnchor(SpreadsheetViewportAnchor.RIGHT),
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN_RANGE.setAnchor(SpreadsheetViewportAnchor.RIGHT)
                )
        );
    }

    // setCell..........................................................................................................

    @Test
    public void testSetCellWithNotSpreadsheetNameHistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
                historyToken.setCell(CELL),
                historyToken
        );
    }

    @Test
    public void testSetCellWithColumnFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> HistoryToken.spreadsheetSelect(ID, NAME).setCell(COLUMN)
        );
    }

    @Test
    public void testSetCell() {
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

    // isCellFormatPattern..............................................................................................

    @Test
    public void testIsCellFormatPattern() {
        this.isCellFormatPatternAndCheck(
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                ),
                false
        );
    }

    @Test
    public void testIsCellFormatPatternWhenCell() {
        this.isCellFormatPatternAndCheck(
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                ),
                false
        );
    }

    @Test
    public void testIsCellFormatPatternWhenCellFormatPattern() {
        this.isCellFormatPatternAndCheck(
                HistoryToken.cellPattern(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetPatternKind.DATE_FORMAT_PATTERN
                ),
                true
        );
    }

    @Test
    public void testIsCellFormatPatternWhenCellParsePattern() {
        this.isCellFormatPatternAndCheck(
                HistoryToken.cellPattern(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetPatternKind.DATE_PARSE_PATTERN
                ),
                false
        );
    }

    @Test
    public void testIsCellFormatPatternWhenMetadataFormatPattern() {
        this.isCellFormatPatternAndCheck(
                HistoryToken.metadataPropertySelect(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.DATE_FORMAT_PATTERN
                ),
                false
        );
    }

    @Test
    public void testIsCellFormatPatternWhenMetadataParsePattern() {
        this.isCellFormatPatternAndCheck(
                HistoryToken.metadataPropertySelect(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.DATE_PARSE_PATTERN
                ),
                false
        );
    }

    private void isCellFormatPatternAndCheck(final HistoryToken token,
                                             final boolean expected) {
        this.checkEquals(
                expected,
                token.isCellFormatPattern(),
                token::toString
        );
    }

    // isCellParsePattern..............................................................................................

    @Test
    public void testIsCellParsePattern() {
        this.isCellParsePatternAndCheck(
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                ),
                false
        );
    }

    @Test
    public void testIsCellParsePatternWhenCell() {
        this.isCellParsePatternAndCheck(
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                ),
                false
        );
    }

    @Test
    public void testIsCellParsePatternWhenCellFormatPattern() {
        this.isCellParsePatternAndCheck(
                HistoryToken.cellPattern(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetPatternKind.DATE_FORMAT_PATTERN
                ),
                false
        );
    }

    @Test
    public void testIsCellParsePatternWhenCellParsePattern() {
        this.isCellParsePatternAndCheck(
                HistoryToken.cellPattern(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetPatternKind.DATE_PARSE_PATTERN
                ),
                true
        );
    }

    @Test
    public void testIsCellParsePatternWhenMetadataFormatPattern() {
        this.isCellParsePatternAndCheck(
                HistoryToken.metadataPropertySelect(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.DATE_FORMAT_PATTERN
                ),
                false
        );
    }

    @Test
    public void testIsCellParsePatternWhenMetadataParsePattern() {
        this.isCellParsePatternAndCheck(
                HistoryToken.metadataPropertySelect(
                        ID,
                        NAME,
                        SpreadsheetMetadataPropertyName.DATE_PARSE_PATTERN
                ),
                false
        );
    }

    private void isCellParsePatternAndCheck(final HistoryToken token,
                                            final boolean expected) {
        this.checkEquals(
                expected,
                token.isCellParsePattern(),
                token::toString
        );
    }

    // setClear..........................................................................................................

    @Test
    public void testSetClearWithNotSpreadsheetNameHistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
                historyToken.setClear(),
                historyToken
        );
    }

    @Test
    public void testSetClearCell() {
        final AnchoredSpreadsheetSelection selection = CELL.setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.cell(ID, NAME, selection);

        this.checkEquals(
                historyToken.setClear(),
                HistoryToken.cellClear(
                        ID,
                        NAME,
                        selection
                )
        );
    }

    @Test
    public void testSetClearColumn() {
        final AnchoredSpreadsheetSelection selection = COLUMN.setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.column(ID, NAME, selection);

        this.checkEquals(
                historyToken.setClear(),
                HistoryToken.columnClear(
                        ID,
                        NAME,
                        selection
                )
        );
    }

    @Test
    public void testSetClearRow() {
        final AnchoredSpreadsheetSelection selection = ROW.setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.row(ID, NAME, selection);

        this.checkEquals(
                historyToken.setClear(),
                HistoryToken.rowClear(
                        ID,
                        NAME,
                        selection
                )
        );
    }

    // setColumn........................................................................................................

    @Test
    public void testSetColumnWithNotSpreadsheetNameHistoryTokenSubclass() {
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
    public void testSetColumn() {
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

    // setDelete..........................................................................................................

    @Test
    public void testSetDeleteWithNotSpreadsheetNameHistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
                historyToken.setDelete(),
                historyToken
        );
    }

    @Test
    public void testSetDeleteCell() {
        final AnchoredSpreadsheetSelection viewport = CELL.setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.cell(ID, NAME, viewport);

        this.checkEquals(
                historyToken.setDelete(),
                HistoryToken.cellDelete(
                        ID,
                        NAME,
                        viewport
                )
        );
    }

    @Test
    public void testSetDeleteColumn() {
        final AnchoredSpreadsheetSelection viewport = COLUMN.setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.column(ID, NAME, viewport);

        this.checkEquals(
                historyToken.setDelete(),
                HistoryToken.columnDelete(
                        ID,
                        NAME,
                        viewport
                )
        );
    }

    @Test
    public void testSetDeleteRow() {
        final AnchoredSpreadsheetSelection viewport = ROW.setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.row(ID, NAME, viewport);

        this.checkEquals(
                historyToken.setDelete(),
                HistoryToken.rowDelete(
                        ID,
                        NAME,
                        viewport
                )
        );
    }

    // setFreeze........................................................................................................

    @Test
    public void testSetFreezeNotSpreadsheetNameHistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
                historyToken.setFreeze(),
                historyToken
        );
    }

    @Test
    public void testSetFreezeCell() {
        final AnchoredSpreadsheetSelection viewport = SpreadsheetSelection.A1.setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.cell(ID, NAME, viewport);

        this.checkEquals(
                historyToken.setFreeze(),
                HistoryToken.cellFreeze(
                        ID,
                        NAME,
                        viewport
                )
        );
    }

    @Test
    public void testSetFreezeColumn() {
        final AnchoredSpreadsheetSelection viewport = SpreadsheetSelection.parseColumn("A")
                .setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.column(ID, NAME, viewport);

        this.checkEquals(
                historyToken.setFreeze(),
                HistoryToken.columnFreeze(
                        ID,
                        NAME,
                        viewport
                )
        );
    }

    @Test
    public void testSetFreezeRow() {
        final AnchoredSpreadsheetSelection viewport = SpreadsheetSelection.parseRow("1")
                .setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.row(ID, NAME, viewport);

        this.checkEquals(
                historyToken.setFreeze(),
                HistoryToken.rowFreeze(
                        ID,
                        NAME,
                        viewport
                )
        );
    }

    // setLabelMapping..................................................................................................

    @Test
    public void testSetLabelNameWhenNotSpreadsheetNameHistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
                historyToken.setLabelName(
                        Optional.of(LABEL)
                ),
                historyToken
        );
    }

    @Test
    public void testSetLabelName() {
        final HistoryToken historyToken = HistoryToken.spreadsheetSelect(ID, NAME);

        this.checkEquals(
                historyToken.setLabelName(
                        Optional.of(LABEL)
                ),
                HistoryToken.labelMapping(
                        ID,
                        NAME,
                        Optional.of(LABEL)
                )
        );
    }

    // clearSelection...................................................................................................

    @Test
    public void testClearSelection() {
        this.checkEquals(
                HistoryToken.spreadsheetSelect(
                        ID,
                        NAME
                ),
                HistoryToken.cell(
                        ID,
                        NAME,
                        SpreadsheetSelection.A1.setDefaultAnchor()
                ).clearSelection()
        );
    }

    // setMenu..........................................................................................................

    @Test
    public void testSetMenuWithNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> HistoryToken.unknown(UrlFragment.EMPTY)
                        .setMenu(null)
        );
    }

    // setMenu with empty...............................................................................................

    @Test
    public void testSetMenuCellWithoutSelection() {
        final AnchoredSpreadsheetSelection cell = CELL.setDefaultAnchor();

        this.setMenuAndCheck(
                HistoryToken.cell(
                        ID,
                        NAME,
                        cell
                ),
                HistoryToken.cellMenu(
                        ID,
                        NAME,
                        cell
                )
        );
    }

    @Test
    public void testSetMenuCellRangeWithoutSelection() {
        final AnchoredSpreadsheetSelection range = CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT);

        this.setMenuAndCheck(
                HistoryToken.cell(
                        ID,
                        NAME,
                        range
                ),
                HistoryToken.cellMenu(
                        ID,
                        NAME,
                        range
                )
        );
    }

    @Test
    public void testSetMenuCellRangeWithoutSelection2() {
        final AnchoredSpreadsheetSelection range = CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.BOTTOM_RIGHT);

        this.setMenuAndCheck(
                HistoryToken.cell(
                        ID,
                        NAME,
                        range
                ),
                HistoryToken.cellMenu(
                        ID,
                        NAME,
                        range
                )
        );
    }

    @Test
    public void testSetMenuLabelWithoutSelection() {
        final AnchoredSpreadsheetSelection cell = LABEL.setDefaultAnchor();

        this.setMenuAndCheck(
                HistoryToken.cell(
                        ID,
                        NAME,
                        cell
                ),
                HistoryToken.cellMenu(
                        ID,
                        NAME,
                        cell
                )
        );
    }

    @Test
    public void testSetMenuColumnWithoutSelection() {
        final AnchoredSpreadsheetSelection column = COLUMN.setDefaultAnchor();

        this.setMenuAndCheck(
                HistoryToken.column(
                        ID,
                        NAME,
                        column
                ),
                HistoryToken.columnMenu(
                        ID,
                        NAME,
                        column
                )
        );
    }

    @Test
    public void testSetMenuColumnRangeWithoutSelection() {
        final AnchoredSpreadsheetSelection range = COLUMN_RANGE.setAnchor(SpreadsheetViewportAnchor.LEFT);

        this.setMenuAndCheck(
                HistoryToken.column(
                        ID,
                        NAME,
                        range
                ),
                HistoryToken.columnMenu(
                        ID,
                        NAME,
                        range
                )
        );
    }

    @Test
    public void testSetMenuColumnRangeWithoutSelection2() {
        final AnchoredSpreadsheetSelection range = COLUMN_RANGE.setAnchor(SpreadsheetViewportAnchor.RIGHT);

        this.setMenuAndCheck(
                HistoryToken.column(
                        ID,
                        NAME,
                        range
                ),
                HistoryToken.columnMenu(
                        ID,
                        NAME,
                        range
                )
        );
    }

    @Test
    public void testSetMenuRowWithoutSelection() {
        final AnchoredSpreadsheetSelection row = ROW.setDefaultAnchor();

        this.setMenuAndCheck(
                HistoryToken.row(
                        ID,
                        NAME,
                        row
                ),
                HistoryToken.rowMenu(
                        ID,
                        NAME,
                        row
                )
        );
    }

    @Test
    public void testSetMenuRowRangeWithoutSelection() {
        final AnchoredSpreadsheetSelection range = ROW_RANGE.setAnchor(SpreadsheetViewportAnchor.TOP);

        this.setMenuAndCheck(
                HistoryToken.row(
                        ID,
                        NAME,
                        range
                ),
                HistoryToken.rowMenu(
                        ID,
                        NAME,
                        range
                )
        );
    }

    @Test
    public void testSetMenuRowRangeWithoutSelection2() {
        final AnchoredSpreadsheetSelection range = ROW_RANGE.setAnchor(SpreadsheetViewportAnchor.BOTTOM);

        this.setMenuAndCheck(
                HistoryToken.row(
                        ID,
                        NAME,
                        range
                ),
                HistoryToken.rowMenu(
                        ID,
                        NAME,
                        range
                )
        );
    }

    private void setMenuAndCheck(final HistoryToken token,
                                 final HistoryToken expected) {
        this.setMenuAndCheck(
                token,
                Optional.empty(),
                expected
        );
    }

    // setMenu cell.....................................................................................................

    @Test
    public void testSetMenuWithMissingSelection() {
        this.setMenuAndCheck(
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
    public void testSetMenuWithoutCellSelectAndCellSelection() {
        this.setMenuAndCheck(
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
    public void testSetMenuWithColumnRangeSelectAndCellSelection() {
        this.setMenuAndCheck(
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN_RANGE.setAnchor(SpreadsheetViewportAnchor.RIGHT)
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
    public void testSetMenuWithCellSelectAndSameCellSelection() {
        this.setMenuAndCheck(
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
    public void testSetMenuWithCellSelectAndDifferentCellSelection() {
        final SpreadsheetCellReference differentCell = SpreadsheetSelection.parseCell("B2");

        this.setMenuAndCheck(
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
    public void testSetMenuWithCellRangeSelectAndInsideCellSelection() {
        final AnchoredSpreadsheetSelection range = SpreadsheetSelection.parseCellRange("A1:C3")
                .setAnchor(SpreadsheetViewportAnchor.BOTTOM_RIGHT);

        this.setMenuAndCheck(
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
    public void testSetMenuWithCellRangeSelectAndOutsideCellSelection() {
        final SpreadsheetCellReference differentCell = SpreadsheetSelection.parseCell("C3");

        this.setMenuAndCheck(
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

    // setMenu column...................................................................................................

    @Test
    public void testSetMenuWithoutColumnSelectAndColumnSelection() {
        this.setMenuAndCheck(
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
    public void testSetMenuWithRowRangeSelectAndColumnSelection() {
        this.setMenuAndCheck(
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW_RANGE.setAnchor(SpreadsheetViewportAnchor.BOTTOM)
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
    public void testSetMenuWithColumnSelectAndSameColumnSelection() {
        this.setMenuAndCheck(
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
    public void testSetMenuWithColumnSelectAndDifferentColumnSelection() {
        final SpreadsheetColumnReference differentColumn = SpreadsheetSelection.parseColumn("B");

        this.setMenuAndCheck(
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
    public void testSetMenuWithColumnRangeSelectAndInsideColumnSelection() {
        final AnchoredSpreadsheetSelection range = SpreadsheetSelection.parseColumnRange("A:C")
                .setAnchor(SpreadsheetViewportAnchor.RIGHT);

        this.setMenuAndCheck(
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
    public void testSetMenuWithColumnRangeSelectAndOutsideColumnSelection() {
        final SpreadsheetColumnReference differentColumn = SpreadsheetSelection.parseColumn("C");

        this.setMenuAndCheck(
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

    // setMenu row......................................................................................................

    @Test
    public void testSetMenuWithoutRowSelectAndRowSelection() {
        this.setMenuAndCheck(
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
    public void testSetMenuWithCellSelectAndRowSelection() {
        this.setMenuAndCheck(
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
    public void testSetMenuWithRowSelectAndSameRowSelection() {
        this.setMenuAndCheck(
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
    public void testSetMenuWithRowSelectAndDifferentRowSelection() {
        final SpreadsheetRowReference differentRow = SpreadsheetSelection.parseRow("2");

        this.setMenuAndCheck(
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
    public void testSetMenuWithRowRangeSelectAndInsideRowSelection() {
        final AnchoredSpreadsheetSelection range = SpreadsheetSelection.parseRowRange("1:3")
                .setAnchor(SpreadsheetViewportAnchor.BOTTOM);

        this.setMenuAndCheck(
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
    public void testSetMenuWithRowRangeSelectAndOutsideRowSelection() {
        final SpreadsheetRowReference differentRow = SpreadsheetSelection.parseRow("3");

        this.setMenuAndCheck(
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

    private void setMenuAndCheck(final HistoryToken token,
                                 final SpreadsheetSelection selection,
                                 final HistoryToken expected) {
        this.setMenuAndCheck(
                token,
                Optional.of(selection),
                expected
        );
    }

    private void setMenuAndCheck(final HistoryToken token,
                                 final Optional<SpreadsheetSelection> selection,
                                 final HistoryToken expected) {
        this.checkEquals(
                expected,
                token.setMenu(selection),
                () -> token + " setMenu " + selection
        );
    }

    // setPattern........................................................................................................

    @Test
    public void testSetPatternWithNotSpreadsheetNameHistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
                historyToken.setPattern(SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN),
                historyToken
        );
    }

    @Test
    public void testSetPatternCell() {
        final AnchoredSpreadsheetSelection viewport = CELL.setDefaultAnchor();
        final SpreadsheetPattern pattern = SpreadsheetPattern.parseTextFormatPattern("@@@");
        final HistoryToken historyToken = HistoryToken.cell(ID, NAME, viewport);

        this.checkEquals(
                HistoryToken.cellPatternSave(
                        ID,
                        NAME,
                        viewport,
                        pattern.kind(),
                        Optional.of(
                                pattern
                        )
                ),
                historyToken.setPattern(pattern),
                () -> historyToken + " setPattern " + pattern
        );
    }

    // setPatternKind..........................................................................................................

    @Test
    public void testSetPatternKindWithNotSpreadsheetNameHistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
                historyToken.setPatternKind(
                        Optional.of(
                                SpreadsheetPatternKind.TIME_PARSE_PATTERN)
                ),
                historyToken
        );
    }

    @Test
    public void testSetPatternKindCell() {
        final AnchoredSpreadsheetSelection viewport = CELL.setDefaultAnchor();
        final SpreadsheetPatternKind kind = SpreadsheetPatternKind.DATE_FORMAT_PATTERN;
        final HistoryToken historyToken = HistoryToken.cell(ID, NAME, viewport);

        this.checkEquals(
                historyToken.setPatternKind(
                        Optional.of(kind)
                ),
                HistoryToken.cellPattern(
                        ID,
                        NAME,
                        viewport,
                        kind
                )
        );
    }

    @Test
    public void testSetPatternKindCell2() {
        final AnchoredSpreadsheetSelection viewport = CELL.setDefaultAnchor();
        final SpreadsheetPatternKind kind = SpreadsheetPatternKind.DATE_TIME_PARSE_PATTERN;
        final HistoryToken historyToken = HistoryToken.cell(ID, NAME, viewport);

        this.checkEquals(
                historyToken.setPatternKind(
                        Optional.of(kind)
                ),
                HistoryToken.cellPattern(
                        ID,
                        NAME,
                        viewport,
                        kind
                )
        );
    }

    @Test
    public void testSetPatternKindColumn() {
        final AnchoredSpreadsheetSelection viewport = COLUMN.setDefaultAnchor();
        final SpreadsheetPatternKind kind = SpreadsheetPatternKind.DATE_FORMAT_PATTERN;
        final HistoryToken historyToken = HistoryToken.column(ID, NAME, viewport);

        assertSame(
                historyToken.setPatternKind(
                        Optional.of(kind)
                ),
                historyToken
        );
    }

    @Test
    public void testSetPatternKindRow() {
        final AnchoredSpreadsheetSelection viewport = ROW.setDefaultAnchor();
        final SpreadsheetPatternKind kind = SpreadsheetPatternKind.DATE_FORMAT_PATTERN;
        final HistoryToken historyToken = HistoryToken.row(ID, NAME, viewport);

        assertSame(
                historyToken.setPatternKind(
                        Optional.of(kind)
                ),
                historyToken
        );
    }

    // setSave..........................................................................................................

    @Test
    public void testSetSaveWithNotSpreadsheetNameHistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
                historyToken.setSave("save-value"),
                historyToken
        );
    }

    // setStyle..........................................................................................................

    @Test
    public void testSetStyleWithNotSpreadsheetNameHistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
                historyToken.setStyle(TextStylePropertyName.COLOR),
                historyToken
        );
    }

    @Test
    public void testSetStyleCell() {
        final AnchoredSpreadsheetSelection viewport = CELL.setDefaultAnchor();
        final TextStylePropertyName<Color> property = TextStylePropertyName.COLOR;
        final HistoryToken historyToken = HistoryToken.cell(ID, NAME, viewport);

        this.checkEquals(
                historyToken.setStyle(property),
                HistoryToken.cellStyle(
                        ID,
                        NAME,
                        viewport,
                        property
                )
        );
    }

    @Test
    public void testSetStyleCell2() {
        final AnchoredSpreadsheetSelection viewport = CELL.setDefaultAnchor();
        final TextStylePropertyName<Color> property = TextStylePropertyName.OUTLINE_COLOR;
        final HistoryToken historyToken = HistoryToken.cell(ID, NAME, viewport);

        this.checkEquals(
                historyToken.setStyle(property),
                HistoryToken.cellStyle(
                        ID,
                        NAME,
                        viewport,
                        property
                )
        );
    }

    @Test
    public void testSetStyleColumn() {
        final AnchoredSpreadsheetSelection viewport = COLUMN.setDefaultAnchor();
        final TextStylePropertyName<Color> property = TextStylePropertyName.COLOR;
        final HistoryToken historyToken = HistoryToken.column(ID, NAME, viewport);

        assertSame(
                historyToken.setStyle(property),
                historyToken
        );
    }

    @Test
    public void testSetStyleRow() {
        final AnchoredSpreadsheetSelection viewport = ROW.setDefaultAnchor();
        final TextStylePropertyName<Color> property = TextStylePropertyName.COLOR;
        final HistoryToken historyToken = HistoryToken.row(ID, NAME, viewport);

        assertSame(
                historyToken.setStyle(property),
                historyToken
        );
    }

    // setUnfreeze........................................................................................................

    @Test
    public void testSetUnfreezeNotSpreadsheetNameHistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
                historyToken.setUnfreeze(),
                historyToken
        );
    }

    @Test
    public void testSetUnfreezeCell() {
        final AnchoredSpreadsheetSelection viewport = SpreadsheetSelection.A1.setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.cell(ID, NAME, viewport);

        this.checkEquals(
                historyToken.setUnfreeze(),
                HistoryToken.cellUnfreeze(
                        ID,
                        NAME,
                        viewport
                )
        );
    }

    @Test
    public void testSetUnfreezeColumn() {
        final AnchoredSpreadsheetSelection viewport = SpreadsheetSelection.parseColumn("A")
                .setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.column(ID, NAME, viewport);

        this.checkEquals(
                historyToken.setUnfreeze(),
                HistoryToken.columnUnfreeze(
                        ID,
                        NAME,
                        viewport
                )
        );
    }

    @Test
    public void testSetUnfreezeRow() {
        final AnchoredSpreadsheetSelection viewport = SpreadsheetSelection.parseRow("1")
                .setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.row(ID, NAME, viewport);

        this.checkEquals(
                historyToken.setUnfreeze(),
                HistoryToken.rowUnfreeze(
                        ID,
                        NAME,
                        viewport
                )
        );
    }

    // viewport................................................................................................

    @Test
    public void testViewportCell() {
        final AnchoredSpreadsheetSelection cell = CELL.setDefaultAnchor();

        final AnchoredSpreadsheetSelectionHistoryToken historyToken = HistoryToken.selection(
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
    public void testViewportCellRange() {
        final AnchoredSpreadsheetSelection cell = CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.BOTTOM_RIGHT);

        final AnchoredSpreadsheetSelectionHistoryToken historyToken = HistoryToken.selection(
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
    public void testViewportLabel() {
        final AnchoredSpreadsheetSelection cell = LABEL.setAnchor(SpreadsheetViewportAnchor.BOTTOM_RIGHT);

        final AnchoredSpreadsheetSelectionHistoryToken historyToken = HistoryToken.selection(
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
    public void testViewportColumn() {
        final AnchoredSpreadsheetSelection column = COLUMN.setDefaultAnchor();

        final AnchoredSpreadsheetSelectionHistoryToken historyToken = HistoryToken.selection(
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
    public void testViewportColumnRange() {
        final AnchoredSpreadsheetSelection column = COLUMN_RANGE.setAnchor(SpreadsheetViewportAnchor.RIGHT);

        final AnchoredSpreadsheetSelectionHistoryToken historyToken = HistoryToken.selection(
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
    public void testViewportRow() {
        final AnchoredSpreadsheetSelection row = ROW.setDefaultAnchor();

        final AnchoredSpreadsheetSelectionHistoryToken historyToken = HistoryToken.selection(
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
    public void testViewportRowRange() {
        final AnchoredSpreadsheetSelection row = ROW_RANGE.setAnchor(SpreadsheetViewportAnchor.BOTTOM);

        final AnchoredSpreadsheetSelectionHistoryToken historyToken = HistoryToken.selection(
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

    // anchoredSelectionHistoryTokenOrEmpty.............................................................................

    @Test
    public void testAnchoredSelectionHistoryTokenOrEmptyNot() {
        this.anchoredSelectionHistoryTokenOrEmptyAndCheck(
                HistoryToken.unknown(UrlFragment.parse("/something-else")),
                Optional.empty()
        );
    }

    @Test
    public void testAnchoredSelectionHistoryTokenOrEmptyCell() {
        this.anchoredSelectionHistoryTokenOrEmptyAndCheck(
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testAnchoredSelectionHistoryTokenOrEmptyCellRange() {
        this.anchoredSelectionHistoryTokenOrEmptyAndCheck(
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.BOTTOM_RIGHT)
                )
        );
    }

    @Test
    public void testAnchoredSelectionHistoryTokenOrEmptyLabel() {
        this.anchoredSelectionHistoryTokenOrEmptyAndCheck(
                HistoryToken.cell(
                        ID,
                        NAME,
                        LABEL.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testAnchoredSelectionHistoryTokenOrEmptyWhenCellClear() {
        final AnchoredSpreadsheetSelection viewport = CELL.setDefaultAnchor();

        this.anchoredSelectionHistoryTokenOrEmptyAndCheck(
                HistoryToken.cellClear(
                        ID,
                        NAME,
                        viewport
                ),
                HistoryToken.cell(
                        ID,
                        NAME,
                        viewport
                )
        );
    }

    @Test
    public void testAnchoredSelectionHistoryTokenOrEmptyWhenCellFormulaSave() {
        final AnchoredSpreadsheetSelection viewport = CELL.setDefaultAnchor();

        this.anchoredSelectionHistoryTokenOrEmptyAndCheck(
                HistoryToken.formulaSave(
                        ID,
                        NAME,
                        viewport,
                        "=1"
                ),
                HistoryToken.cell(
                        ID,
                        NAME,
                        viewport
                )
        );
    }

    @Test
    public void testAnchoredSelectionHistoryTokenOrEmptyColumn() {
        this.anchoredSelectionHistoryTokenOrEmptyAndCheck(
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testAnchoredSelectionHistoryTokenOrEmptyWhenColumnClear() {
        final AnchoredSpreadsheetSelection viewport = COLUMN.setDefaultAnchor();

        this.anchoredSelectionHistoryTokenOrEmptyAndCheck(
                HistoryToken.columnClear(
                        ID,
                        NAME,
                        viewport
                ),
                HistoryToken.column(
                        ID,
                        NAME,
                        viewport
                )
        );
    }

    @Test
    public void testAnchoredSelectionHistoryTokenOrEmptyColumnRange() {
        this.anchoredSelectionHistoryTokenOrEmptyAndCheck(
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN_RANGE.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testAnchoredSelectionHistoryTokenOrEmptyRow() {
        this.anchoredSelectionHistoryTokenOrEmptyAndCheck(
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testAnchoredSelectionHistoryTokenOrEmptyWhenRowClear() {
        final AnchoredSpreadsheetSelection viewport = ROW.setDefaultAnchor();

        this.anchoredSelectionHistoryTokenOrEmptyAndCheck(
                HistoryToken.rowClear(
                        ID,
                        NAME,
                        viewport
                ),
                HistoryToken.row(
                        ID,
                        NAME,
                        viewport
                )
        );
    }

    @Test
    public void testAnchoredSelectionHistoryTokenOrEmptyRowRange() {
        this.anchoredSelectionHistoryTokenOrEmptyAndCheck(
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW_RANGE.setDefaultAnchor()
                )
        );
    }

    private void anchoredSelectionHistoryTokenOrEmptyAndCheck(final HistoryToken token) {
        assertSame(
                token,
                token.anchoredSelectionHistoryTokenOrEmpty()
                        .orElse(null),
                () -> token + " anchoredSelectionHistoryTokenOrEmpty"
        );
    }

    private void anchoredSelectionHistoryTokenOrEmptyAndCheck(final HistoryToken token,
                                                              final HistoryToken expected) {
        this.anchoredSelectionHistoryTokenOrEmptyAndCheck(
                token,
                Optional.of(expected)
        );
    }

    private void anchoredSelectionHistoryTokenOrEmptyAndCheck(final HistoryToken token,
                                                              final Optional<HistoryToken> expected) {
        this.checkEquals(
                expected,
                token.anchoredSelectionHistoryTokenOrEmpty(),
                () -> token + " anchoredSelectionHistoryTokenOrEmpty"
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
                        CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.BOTTOM_RIGHT)
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
                        CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT)
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
                        CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_RIGHT)
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
                        CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.BOTTOM_RIGHT)
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
                        CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.BOTTOM_RIGHT)
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
                        CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_RIGHT)
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
                        "=1+2"
                )
        );
    }

    // cell/highlight.....................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellHighlight() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/highlight",
                HistoryToken.cellHighlightSelect(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellHighlightSaveDisable() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/highlight/save/disable",
                HistoryToken.cellHighlightSave(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        false
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellHighlightSaveEnable() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/highlight/save/enable",
                HistoryToken.cellHighlightSave(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        true
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellHighlightSaveInvalid() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/highlight/save/!invalid",
                HistoryToken.cellHighlightSelect(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    // cell/pattern.......................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellFormatPatternMissingPatternKind() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/format-pattern",
                HistoryToken.cellFormatPatternToolbar(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellParsePatternMissingPatternKind() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/parse-pattern",
                HistoryToken.cellParsePatternToolbar(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellFormatPatternInvalidPatternKind() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/format-pattern/!invalid",
                HistoryToken.cellFormatPatternToolbar(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellPatternPatternKind() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/format-pattern/date",
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
                "/123/SpreadsheetName456/cell/A1/format-pattern/date/save/",
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
                "/123/SpreadsheetName456/cell/A1/format-pattern/date/save/" + pattern,
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
                "/123/SpreadsheetName456/cell/A1/parse-pattern/time/save/" + pattern,
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
                        COLUMN_RANGE.setAnchor(SpreadsheetViewportAnchor.RIGHT)
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
                        COLUMN_RANGE.setAnchor(SpreadsheetViewportAnchor.LEFT)
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
                        COLUMN_RANGE.setAnchor(SpreadsheetViewportAnchor.RIGHT)
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
                        COLUMN_RANGE.setAnchor(SpreadsheetViewportAnchor.RIGHT)
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
                                .setAnchor(SpreadsheetViewportAnchor.RIGHT)
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
                                .setAnchor(SpreadsheetViewportAnchor.RIGHT)
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
                "/123/SpreadsheetName456/column/AA/format-pattern/date/yymmdd",
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
                        ROW_RANGE.setAnchor(SpreadsheetViewportAnchor.BOTTOM)
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
                        ROW_RANGE.setAnchor(SpreadsheetViewportAnchor.TOP)
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
                        ROW_RANGE.setAnchor(SpreadsheetViewportAnchor.BOTTOM)
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
                        ROW_RANGE.setAnchor(SpreadsheetViewportAnchor.BOTTOM)
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
                "/123/SpreadsheetName456/row/11/format-pattern/date/yymmdd",
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
                HistoryToken.labelMapping(
                        ID,
                        NAME,
                        Optional.empty()
                )
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
                        Optional.of(LABEL)
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
                        Optional.of(LABEL)
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
                "/123/SpreadsheetName456/label/Label123/format-pattern/date/yymmdd",
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
