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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.clipboard.SpreadsheetCellClipboardKind;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.ui.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelNameResolvers;
import walkingkooka.spreadsheet.reference.SpreadsheetRowRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;
import java.util.OptionalInt;

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

    private final static SpreadsheetCellRangeReference CELL_RANGE = SpreadsheetSelection.parseCellRange("B2:C3");

    private final static SpreadsheetLabelName LABEL = SpreadsheetSelection.labelName("Label123");

    private static final SpreadsheetLabelMappingSelectHistoryToken LABEL_MAPPING_HHT = HistoryToken.labelMapping(
            ID,
            NAME,
            Optional.of(LABEL)
    );

    private final static SpreadsheetColumnReference COLUMN = SpreadsheetSelection.parseColumn("AA");

    private final static SpreadsheetColumnRangeReference COLUMN_RANGE = SpreadsheetSelection.parseColumnRange("BB:CC");

    private final static SpreadsheetRowReference ROW = SpreadsheetSelection.parseRow("11");

    private final static SpreadsheetRowRangeReference ROW_RANGE = SpreadsheetSelection.parseRowRange("22:33");

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

    // setCount.........................................................................................................

    @Test
    public void testSetCountOnSpreadsheetLoad() {
        this.setCountAndCheck(
                HistoryToken.spreadsheetLoad(
                        ID
                ),
                OptionalInt.of(1)
        );
    }

    @Test
    public void testSetCountOnSpreadsheetCellSelect() {
        this.setCountAndCheck(
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                ),
                OptionalInt.of(1)
        );
    }

    @Test
    public void testSetCountSpreadsheetColumnInsertAfterSame() {
        final OptionalInt count = OptionalInt.of(123);

        this.setCountAndCheck(
                HistoryToken.columnInsertAfter(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor(),
                        count
                ),
                count
        );
    }

    @Test
    public void testSetCountSpreadsheetColumnInsertAfterDifferent() {
        final OptionalInt count = OptionalInt.of(123);

        this.setCountAndCheck(
                HistoryToken.columnInsertAfter(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor(),
                        OptionalInt.empty()
                ),
                count,
                HistoryToken.columnInsertAfter(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor(),
                        count
                )
        );
    }

    @Test
    public void testSetCountSpreadsheetColumnInsertBeforeSame() {
        final OptionalInt count = OptionalInt.of(123);

        this.setCountAndCheck(
                HistoryToken.columnInsertBefore(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor(),
                        count
                ),
                count
        );
    }

    @Test
    public void testSetCountSpreadsheetColumnInsertBeforeDifferent() {
        final OptionalInt count = OptionalInt.of(123);

        this.setCountAndCheck(
                HistoryToken.columnInsertBefore(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor(),
                        OptionalInt.empty()
                ),
                count,
                HistoryToken.columnInsertBefore(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor(),
                        count
                )
        );
    }

    @Test
    public void testSetCountSpreadsheetRowInsertAfterSame() {
        final OptionalInt count = OptionalInt.of(123);

        this.setCountAndCheck(
                HistoryToken.rowInsertAfter(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor(),
                        count
                ),
                count
        );
    }

    @Test
    public void testSetCountSpreadsheetRowInsertAfterDifferent() {
        final OptionalInt count = OptionalInt.of(123);

        this.setCountAndCheck(
                HistoryToken.rowInsertAfter(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor(),
                        OptionalInt.empty()
                ),
                count,
                HistoryToken.rowInsertAfter(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor(),
                        count
                )
        );
    }

    @Test
    public void testSetCountSpreadsheetRowInsertBeforeSame() {
        final OptionalInt count = OptionalInt.of(123);

        this.setCountAndCheck(
                HistoryToken.rowInsertBefore(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor(),
                        count
                ),
                count
        );
    }

    @Test
    public void testSetCountSpreadsheetRowInsertBeforeDifferent() {
        final OptionalInt count = OptionalInt.of(123);

        this.setCountAndCheck(
                HistoryToken.rowInsertBefore(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor(),
                        OptionalInt.empty()
                ),
                count,
                HistoryToken.rowInsertBefore(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor(),
                        count
                )
        );
    }

    private void setCountAndCheck(final HistoryToken token,
                                  final OptionalInt count) {
        assertSame(
                token,
                token.setCount(count),
                () -> "setCount " + count
        );
    }


    private void setCountAndCheck(final HistoryToken token,
                                  final OptionalInt count,
                                  final HistoryToken expected) {
        this.checkEquals(
                expected,
                token.setCount(count),
                () -> "setCount " + count
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
        final HistoryToken historyToken = HistoryToken.cell(
                ID,
                NAME,
                CELL.setDefaultAnchor()
        );

        assertSame(
                historyToken.setClear(),
                historyToken
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

    // setCellCopy..........................................................................................................

    @Test
    public void testSetCellCopyWithNullKindFails() {
        assertThrows(
                NullPointerException.class,
                () -> HistoryToken.unknown(UrlFragment.SLASH).setCellCopy(null)
        );
    }

    @Test
    public void testSetCellCopyWithNonCellHistoryToken() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
                historyToken.setCellCopy(SpreadsheetCellClipboardKind.CELL),
                historyToken
        );
    }

    @Test
    public void testSetCellCopyWithCellHistoryToken() {
        final HistoryToken historyToken = HistoryToken.cell(
                ID,
                NAME,
                CELL.setDefaultAnchor()
        );

        this.checkEquals(
                HistoryToken.cellClipboardCopy(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetCellClipboardKind.CELL
                ),
                historyToken.setCellCopy(SpreadsheetCellClipboardKind.CELL)
        );
    }

    @Test
    public void testSetCellCopyWithCellHistoryToken2() {
        final AnchoredSpreadsheetSelection cell = SpreadsheetSelection.parseCellRange("A1:B2")
                .setAnchor(SpreadsheetViewportAnchor.TOP_LEFT);

        final HistoryToken historyToken = HistoryToken.cell(
                ID,
                NAME,
                cell
        );

        for (final SpreadsheetCellClipboardKind kind : SpreadsheetCellClipboardKind.values()) {
            this.checkEquals(
                    HistoryToken.cellClipboardCopy(
                            ID,
                            NAME,
                            cell,
                            kind
                    ),
                    historyToken.setCellCopy(kind)
            );
        }
    }

    @Test
    public void testSetCellCopyWithColumnHistoryToken() {
        final HistoryToken historyToken = HistoryToken.column(
                ID,
                NAME,
                COLUMN.setDefaultAnchor()
        );

        assertSame(
                historyToken.setCellCopy(SpreadsheetCellClipboardKind.CELL),
                historyToken
        );
    }

    @Test
    public void testSetCellCopyWithRowHistoryToken() {
        final HistoryToken historyToken = HistoryToken.row(
                ID,
                NAME,
                ROW.setDefaultAnchor()
        );

        assertSame(
                historyToken.setCellCopy(SpreadsheetCellClipboardKind.CELL),
                historyToken
        );
    }

    // setCellCut...........................................................................................................

    @Test
    public void testSetCellCutWithNullKindFails() {
        assertThrows(
                NullPointerException.class,
                () -> HistoryToken.unknown(UrlFragment.SLASH).setCellCut(null)
        );
    }

    @Test
    public void testSetCellCutWithNonCellHistoryToken() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
                historyToken.setCellCut(SpreadsheetCellClipboardKind.CELL),
                historyToken
        );
    }

    @Test
    public void testSetCellCutWithCellHistoryToken() {
        final HistoryToken historyToken = HistoryToken.cell(
                ID,
                NAME,
                CELL.setDefaultAnchor()
        );

        this.checkEquals(
                HistoryToken.cellClipboardCut(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetCellClipboardKind.CELL
                ),
                historyToken.setCellCut(SpreadsheetCellClipboardKind.CELL)
        );
    }

    @Test
    public void testSetCellCutWithCellHistoryToken2() {
        final AnchoredSpreadsheetSelection cell = SpreadsheetSelection.parseCellRange("A1:B2")
                .setAnchor(SpreadsheetViewportAnchor.TOP_LEFT);

        final HistoryToken historyToken = HistoryToken.cell(
                ID,
                NAME,
                cell
        );

        for (final SpreadsheetCellClipboardKind kind : SpreadsheetCellClipboardKind.values()) {
            this.checkEquals(
                    HistoryToken.cellClipboardCut(
                            ID,
                            NAME,
                            cell,
                            kind
                    ),
                    historyToken.setCellCut(kind)
            );
        }
    }

    @Test
    public void testSetCellCutWithColumnHistoryToken() {
        final HistoryToken historyToken = HistoryToken.column(
                ID,
                NAME,
                COLUMN.setDefaultAnchor()
        );

        assertSame(
                historyToken.setCellCut(SpreadsheetCellClipboardKind.CELL),
                historyToken
        );
    }

    @Test
    public void testSetCellCutWithRowHistoryToken() {
        final HistoryToken historyToken = HistoryToken.row(
                ID,
                NAME,
                ROW.setDefaultAnchor()
        );

        assertSame(
                historyToken.setCellCut(SpreadsheetCellClipboardKind.CELL),
                historyToken
        );
    }

    // setCellPaste.....................................................................................................

    @Test
    public void testSetCellPasteWithNullKindFails() {
        assertThrows(
                NullPointerException.class,
                () -> HistoryToken.unknown(UrlFragment.SLASH).setCellPaste(null)
        );
    }

    @Test
    public void testSetCellPasteWithNonCellHistoryToken() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
                historyToken.setCellPaste(SpreadsheetCellClipboardKind.CELL),
                historyToken
        );
    }

    @Test
    public void testSetCellPasteWithCellHistoryToken() {
        final HistoryToken historyToken = HistoryToken.cell(
                ID,
                NAME,
                CELL.setDefaultAnchor()
        );

        this.checkEquals(
                HistoryToken.cellClipboardPaste(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetCellClipboardKind.CELL
                ),
                historyToken.setCellPaste(SpreadsheetCellClipboardKind.CELL)
        );
    }

    @Test
    public void testSetCellPasteWithCellHistoryToken2() {
        final AnchoredSpreadsheetSelection cell = SpreadsheetSelection.parseCellRange("A1:B2")
                .setAnchor(SpreadsheetViewportAnchor.TOP_LEFT);

        final HistoryToken historyToken = HistoryToken.cell(
                ID,
                NAME,
                cell
        );

        for (final SpreadsheetCellClipboardKind kind : SpreadsheetCellClipboardKind.values()) {
            this.checkEquals(
                    HistoryToken.cellClipboardPaste(
                            ID,
                            NAME,
                            cell,
                            kind
                    ),
                    historyToken.setCellPaste(kind)
            );
        }
    }

    @Test
    public void testSetCellPasteWithColumnHistoryToken() {
        final HistoryToken historyToken = HistoryToken.column(
                ID,
                NAME,
                COLUMN.setDefaultAnchor()
        );

        assertSame(
                historyToken.setCellPaste(SpreadsheetCellClipboardKind.CELL),
                historyToken
        );
    }

    @Test
    public void testSetCellPasteWithRowHistoryToken() {
        final HistoryToken historyToken = HistoryToken.row(
                ID,
                NAME,
                ROW.setDefaultAnchor()
        );

        assertSame(
                historyToken.setCellPaste(SpreadsheetCellClipboardKind.CELL),
                historyToken
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
    public void testSetMenuWithNullSelectionFails() {
        assertThrows(
                NullPointerException.class,
                () -> HistoryToken.unknown(UrlFragment.EMPTY)
                        .setMenu(
                                null,
                                SpreadsheetLabelNameResolvers.fake()
                        )
        );
    }

    @Test
    public void testSetMenuWithNullLabelNameResolverFails() {
        assertThrows(
                NullPointerException.class,
                () -> HistoryToken.unknown(UrlFragment.EMPTY)
                        .setMenu(
                                Optional.of(SpreadsheetSelection.A1),
                                null
                        )
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
                token.setMenu(
                        selection,
                        SpreadsheetLabelNameResolvers.fake()
                ),
                () -> token + " setMenu " + selection
        );
    }

    // setMetadataPropertyName..........................................................................................

    @Test
    public void testSetMetadataPropertyName() {
        final SpreadsheetMetadataPropertyName<?> metadataProperty = SpreadsheetMetadataPropertyName.GROUP_SEPARATOR;

        this.checkEquals(
                HistoryToken.metadataPropertySelect(
                        ID,
                        NAME,
                        metadataProperty
                ),
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                ).setMetadataPropertyName(metadataProperty)
        );
    }

    // HasSpreadsheetPatternKind........................................................................................

    @Test
    public void testPatternKindWithSpreadsheetCreateHistoryToken() {
        this.patternKindAndCheck(
                HistoryToken.spreadsheetCreate()
        );
    }

    @Test
    public void testPatternKindWithSpreadsheetLoadHistoryToken() {
        this.patternKindAndCheck(
                HistoryToken.spreadsheetLoad(
                        SpreadsheetId.with(1)
                )
        );
    }

    private void patternKindAndCheck(final HistoryToken token) {
        this.checkEquals(
                Optional.empty(),
                token.patternKind(),
                token::toString
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

    // clearPatternKind.................................................................................................

    @Test
    public void testClearPatternKindOnCellTextFormatPattern() {
        this.checkEquals(
                HistoryToken.cellPattern(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetPatternKind.TEXT_FORMAT_PATTERN
                ).clearPatternKind(),
                HistoryToken.cellFormatPattern(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    // setReload........................................................................................................

    @Test
    public void testSetReloadWithNotSpreadsheetNameHistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
                historyToken.setReload(),
                historyToken
        );
    }

    @Test
    public void testSetReloadWithSpreadsheetNameHistoryToken() {
        final HistoryToken historyToken = HistoryToken.spreadsheetSelect(
                ID,
                NAME
        );

        this.checkEquals(
                historyToken.setReload(),
                HistoryToken.spreadsheetReload(
                        ID,
                        NAME
                )
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

    // setSortEdit......................................................................................................

    @Test
    public void testSetSortEditNotAnchoredSelection() {
        final String comparatorNames = "A=text UP";

        final HistoryToken load = HistoryToken.spreadsheetLoad(ID);

        assertSame(
                load,
                load.setSortEdit(comparatorNames)
        );
    }

    @Test
    public void testSetSortEditCell() {
        final String comparatorNames = "A=text UP";

        this.setSortEdit(
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                ),
                comparatorNames,
                HistoryToken.cellSortEdit(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        comparatorNames
                )
        );
    }

    @Test
    public void testSetSortEditCellRange() {
        final String comparatorNames = "A=text UP";

        this.setSortEdit(
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL_RANGE.setDefaultAnchor()
                ),
                comparatorNames,
                HistoryToken.cellSortEdit(
                        ID,
                        NAME,
                        CELL_RANGE.setDefaultAnchor(),
                        comparatorNames
                )
        );
    }

    @Test
    public void testSetSortEditColumn() {
        final String comparatorNames = "A=text UP";

        this.setSortEdit(
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                ),
                comparatorNames,
                HistoryToken.columnSortEdit(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor(),
                        comparatorNames
                )
        );
    }

    @Test
    public void testSetSortEditColumnRange() {
        final String comparatorNames = "A=text UP";

        this.setSortEdit(
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN_RANGE.setDefaultAnchor()
                ),
                comparatorNames,
                HistoryToken.columnSortEdit(
                        ID,
                        NAME,
                        COLUMN_RANGE.setDefaultAnchor(),
                        comparatorNames
                )
        );
    }

    @Test
    public void testSetSortEditRow() {
        final String comparatorNames = "1=text UP";

        this.setSortEdit(
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor()
                ),
                comparatorNames,
                HistoryToken.rowSortEdit(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor(),
                        comparatorNames
                )
        );
    }

    @Test
    public void testSetSortEditRowRange() {
        final String comparatorNames = "2=text UP";

        this.setSortEdit(
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW_RANGE.setDefaultAnchor()
                ),
                comparatorNames,
                HistoryToken.rowSortEdit(
                        ID,
                        NAME,
                        ROW_RANGE.setDefaultAnchor(),
                        comparatorNames
                )
        );
    }

    private void setSortEdit(final HistoryToken historyToken,
                             final String comparatorNames,
                             final HistoryToken expected) {
        this.checkEquals(
                expected,
                historyToken.setSortEdit(comparatorNames),
                () -> historyToken + " setSortEdit " + comparatorNames
        );
    }

    // setSortSave......................................................................................................

    @Test
    public void testSetSortSaveNotAnchoredSelection() {
        final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames = SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse("A=text UP");

        final HistoryToken load = HistoryToken.spreadsheetLoad(ID);

        assertSame(
                load,
                load.setSortSave(comparatorNames)
        );
    }

    @Test
    public void testSetSortSaveCell() {
        final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames = SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse("A=text UP");

        this.setSortSave(
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                ),
                comparatorNames,
                HistoryToken.cellSortSave(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        comparatorNames
                )
        );
    }

    @Test
    public void testSetSortSaveCellRange() {
        final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames = SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse("B=text UP;C=text DOWN");

        this.setSortSave(
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL_RANGE.setDefaultAnchor()
                ),
                comparatorNames,
                HistoryToken.cellSortSave(
                        ID,
                        NAME,
                        CELL_RANGE.setDefaultAnchor(),
                        comparatorNames
                )
        );
    }

    @Test
    public void testSetSortSaveColumn() {
        final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames = SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse("AA=text UP");

        this.setSortSave(
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor()
                ),
                comparatorNames,
                HistoryToken.columnSortSave(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor(),
                        comparatorNames
                )
        );
    }

    @Test
    public void testSetSortSaveColumnRange() {
        final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames = SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse("BB=text UP;CC=text DOWN");

        this.setSortSave(
                HistoryToken.column(
                        ID,
                        NAME,
                        COLUMN_RANGE.setDefaultAnchor()
                ),
                comparatorNames,
                HistoryToken.columnSortSave(
                        ID,
                        NAME,
                        COLUMN_RANGE.setDefaultAnchor(),
                        comparatorNames
                )
        );
    }

    @Test
    public void testSetSortSaveRow() {
        final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames = SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse("11=text UP");

        this.setSortSave(
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor()
                ),
                comparatorNames,
                HistoryToken.rowSortSave(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor(),
                        comparatorNames
                )
        );
    }

    @Test
    public void testSetSortSaveRowRange() {
        final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames = SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse("22=text UP;33=text DOWN");

        this.setSortSave(
                HistoryToken.row(
                        ID,
                        NAME,
                        ROW_RANGE.setDefaultAnchor()
                ),
                comparatorNames,
                HistoryToken.rowSortSave(
                        ID,
                        NAME,
                        ROW_RANGE.setDefaultAnchor(),
                        comparatorNames
                )
        );
    }

    private void setSortSave(final HistoryToken historyToken,
                             final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames,
                             final HistoryToken expected) {
        this.checkEquals(
                expected,
                historyToken.setSortSave(comparatorNames),
                () -> historyToken + " setSortSave " + comparatorNames
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

        final SpreadsheetAnchoredSelectionHistoryToken historyToken = HistoryToken.selection(
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

        final SpreadsheetAnchoredSelectionHistoryToken historyToken = HistoryToken.selection(
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

        final SpreadsheetAnchoredSelectionHistoryToken historyToken = HistoryToken.selection(
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

        final SpreadsheetAnchoredSelectionHistoryToken historyToken = HistoryToken.selection(
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

        final SpreadsheetAnchoredSelectionHistoryToken historyToken = HistoryToken.selection(
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

        final SpreadsheetAnchoredSelectionHistoryToken historyToken = HistoryToken.selection(
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

        final SpreadsheetAnchoredSelectionHistoryToken historyToken = HistoryToken.selection(
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
                HistoryToken.spreadsheetListSelect(
                        OptionalInt.empty(), // from
                        OptionalInt.empty() // count
                )
        );
    }

    @Test
    public void testParseSlash() {
        this.parseStringAndCheck(
                "/",
                HistoryToken.spreadsheetListSelect(
                        OptionalInt.empty(), // from
                        OptionalInt.empty() // count
                )
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
                HistoryToken.unknown(
                        UrlFragment.with("/XYZ")
                )
        );
    }

    @Test
    public void testParseDeleteSpreadsheetId() {
        this.parseStringAndCheck(
                "/delete/123",
                HistoryToken.spreadsheetListDelete(
                        ID
                )
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
    public void testParseSpreadsheetIdSpreadsheetNameReload() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/reload",
                HistoryToken.spreadsheetReload(
                        ID,
                        NAME
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameReloadSave() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/reload/save",
                HistoryToken.spreadsheetReload(
                        ID,
                        NAME
                )
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
                HistoryToken.cell(
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
    public void testParseSpreadsheetIdSpreadsheetNameCellSort() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/sort",
                HistoryToken.cell(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellSortInvalid() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/cell/A1/sort/invalid",
                HistoryToken.cell(
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
                HistoryToken.cell(
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
                HistoryToken.cell(
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
                HistoryToken.cellFormatPattern(
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
                HistoryToken.cellParsePattern(
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
                HistoryToken.cellFormatPattern(
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
    public void testParseSpreadsheetIdSpreadsheetNameColumnInsertAfterMissingCount() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/AA/insertAfter",
                HistoryToken.columnInsertAfter(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor(),
                        OptionalInt.empty()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnInsertAfterEmptyCount() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/AA/insertAfter/",
                HistoryToken.columnInsertAfter(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor(),
                        OptionalInt.empty()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnInsertAfter123() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/AA/insertAfter/123",
                HistoryToken.columnInsertAfter(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor(),
                        OptionalInt.of(
                                123
                        )
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnInsertBeforeMissingCount() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/AA/insertBefore",
                HistoryToken.columnInsertBefore(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor(),
                        OptionalInt.empty()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnInsertBeforeEmptyCount() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/AA/insertBefore/",
                HistoryToken.columnInsertBefore(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor(),
                        OptionalInt.empty()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnInsertBefore123() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/column/AA/insertBefore/123",
                HistoryToken.columnInsertBefore(
                        ID,
                        NAME,
                        COLUMN.setDefaultAnchor(),
                        OptionalInt.of(
                                123
                        )
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
    public void testParseSpreadsheetIdSpreadsheetNameRowInsertAfterMissingCount() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/11/insertAfter",
                HistoryToken.rowInsertAfter(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor(),
                        OptionalInt.empty()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowInsertAfterEmptyCount() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/11/insertAfter/",
                HistoryToken.rowInsertAfter(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor(),
                        OptionalInt.empty()
                )
        );
    }


    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowInsertAfter123() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/11/insertAfter/123",
                HistoryToken.rowInsertAfter(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor(),
                        OptionalInt.of(
                                123
                        )
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowInsertBeforeMissingCount() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/11/insertBefore",
                HistoryToken.rowInsertBefore(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor(),
                        OptionalInt.empty()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowInsertBeforeEmptyCount() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/11/insertBefore/",
                HistoryToken.rowInsertBefore(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor(),
                        OptionalInt.empty()
                )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowInsertBefore123() {
        this.parseStringAndCheck(
                "/123/SpreadsheetName456/row/11/insertBefore/123",
                HistoryToken.rowInsertBefore(
                        ID,
                        NAME,
                        ROW.setDefaultAnchor(),
                        OptionalInt.of(
                                123
                        )
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

    private AppContext appContext() {
        return new FakeAppContext() {
            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addSpreadsheetDeltaWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addSpreadsheetMetadataWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                return null;
            }

            @Override
            public SpreadsheetViewportCache spreadsheetViewportCache() {
                return this.viewportCache;
            }

            private final SpreadsheetViewportCache viewportCache = SpreadsheetViewportCache.empty(this);

            @Override
            public HistoryToken historyToken() {
                return HistoryToken.spreadsheetSelect(
                        ID,
                        NAME
                );
            }

            @Override
            public void debug(final Object... values) {
                // nop
            }
        };
    }
}
