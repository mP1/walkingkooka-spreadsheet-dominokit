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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.net.UrlFragment;
import walkingkooka.plugin.PluginName;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.dominokit.clipboard.SpreadsheetCellClipboardKind;
import walkingkooka.spreadsheet.dominokit.file.BrowserFile;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToValueMap;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelNameResolvers;
import walkingkooka.spreadsheet.reference.SpreadsheetRowRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.validation.form.SpreadsheetForms;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportAnchor;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportHomeNavigationList;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportNavigationList;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.validation.ValidationValueTypeName;
import walkingkooka.validation.form.Form;
import walkingkooka.validation.form.FormName;
import walkingkooka.validation.provider.ValidatorSelector;

import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HistoryTokenTest implements ClassTesting<HistoryToken>,
    ParseStringTesting<HistoryToken>,
    SpreadsheetMetadataTesting {

    private final static SpreadsheetId ID = SpreadsheetId.parse("123");

    private final static SpreadsheetName NAME = SpreadsheetName.with("SpreadsheetName456");

    private static final SpreadsheetSelectHistoryToken SPREADSHEET_ID_SPREADSHEET_NAME_TOKEN = HistoryToken.spreadsheetSelect(
        ID,
        NAME
    );

    private final static SpreadsheetCellReference CELL = SpreadsheetSelection.A1;

    private static final SpreadsheetCellSelectHistoryToken CELL_TOKEN = HistoryToken.cellSelect(
        ID,
        NAME,
        CELL.setDefaultAnchor()
    );

    private final static SpreadsheetCellRangeReference CELL_RANGE = SpreadsheetSelection.parseCellRange("B2:C3");

    private final static SpreadsheetLabelName LABEL = SpreadsheetSelection.labelName("Label123");

    private static final SpreadsheetLabelMappingSelectHistoryToken LABEL_MAPPING_TOKEN = HistoryToken.labelMappingSelect(
        ID,
        NAME,
        LABEL
    );

    private final static SpreadsheetColumnReference COLUMN = SpreadsheetSelection.parseColumn("AA");

    private final static SpreadsheetColumnRangeReference COLUMN_RANGE = SpreadsheetSelection.parseColumnRange("BB:CC");

    private final static SpreadsheetRowReference ROW = SpreadsheetSelection.parseRow("11");

    private final static SpreadsheetRowRangeReference ROW_RANGE = SpreadsheetSelection.parseRowRange("22:33");

    // AnchoredSelection................................................................................................

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
            HistoryToken.cellSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testAnchoredSelectionHistoryTokenOrEmptyCellRange() {
        this.anchoredSelectionHistoryTokenOrEmptyAndCheck(
            HistoryToken.cellSelect(
                ID,
                NAME,
                CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.BOTTOM_RIGHT)
            )
        );
    }

    @Test
    public void testAnchoredSelectionHistoryTokenOrEmptyLabel() {
        this.anchoredSelectionHistoryTokenOrEmptyAndCheck(
            HistoryToken.cellSelect(
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
            HistoryToken.cellFormulaSave(
                ID,
                NAME,
                viewport,
                "=1"
            ),
            HistoryToken.cellSelect(
                ID,
                NAME,
                viewport
            )
        );
    }

    @Test
    public void testAnchoredSelectionHistoryTokenOrEmptyColumn() {
        this.anchoredSelectionHistoryTokenOrEmptyAndCheck(
            HistoryToken.columnSelect(
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
            HistoryToken.columnSelect(
                ID,
                NAME,
                viewport
            )
        );
    }

    @Test
    public void testAnchoredSelectionHistoryTokenOrEmptyColumnRange() {
        this.anchoredSelectionHistoryTokenOrEmptyAndCheck(
            HistoryToken.columnSelect(
                ID,
                NAME,
                COLUMN_RANGE.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testAnchoredSelectionHistoryTokenOrEmptyRow() {
        this.anchoredSelectionHistoryTokenOrEmptyAndCheck(
            HistoryToken.rowSelect(
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
            HistoryToken.rowSelect(
                ID,
                NAME,
                viewport
            )
        );
    }

    @Test
    public void testAnchoredSelectionHistoryTokenOrEmptyRowRange() {
        this.anchoredSelectionHistoryTokenOrEmptyAndCheck(
            HistoryToken.rowSelect(
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

    // setCellCopy......................................................................................................

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
        final HistoryToken historyToken = HistoryToken.cellSelect(
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

        final HistoryToken historyToken = HistoryToken.cellSelect(
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
        final HistoryToken historyToken = HistoryToken.columnSelect(
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
        final HistoryToken historyToken = HistoryToken.rowSelect(
            ID,
            NAME,
            ROW.setDefaultAnchor()
        );

        assertSame(
            historyToken.setCellCopy(SpreadsheetCellClipboardKind.CELL),
            historyToken
        );
    }

    // setCellCut.......................................................................................................

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
        final HistoryToken historyToken = HistoryToken.cellSelect(
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

        final HistoryToken historyToken = HistoryToken.cellSelect(
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
        final HistoryToken historyToken = HistoryToken.columnSelect(
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
        final HistoryToken historyToken = HistoryToken.rowSelect(
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
        final HistoryToken historyToken = HistoryToken.cellSelect(
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

        final HistoryToken historyToken = HistoryToken.cellSelect(
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
        final HistoryToken historyToken = HistoryToken.columnSelect(
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
        final HistoryToken historyToken = HistoryToken.rowSelect(
            ID,
            NAME,
            ROW.setDefaultAnchor()
        );

        assertSame(
            historyToken.setCellPaste(SpreadsheetCellClipboardKind.CELL),
            historyToken
        );
    }

    // clear............................................................................................................

    @Test
    public void testClearWithNotSpreadsheetNameHistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
            historyToken.clear(),
            historyToken
        );
    }

    @Test
    public void testClearCell() {
        final HistoryToken historyToken = HistoryToken.cellSelect(
            ID,
            NAME,
            CELL.setDefaultAnchor()
        );

        assertSame(
            historyToken.clear(),
            historyToken
        );
    }

    @Test
    public void testClearColumn() {
        final AnchoredSpreadsheetSelection selection = COLUMN.setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.columnSelect(ID, NAME, selection);

        this.checkEquals(
            historyToken.clear(),
            HistoryToken.columnClear(
                ID,
                NAME,
                selection
            )
        );
    }

    @Test
    public void testClearRow() {
        final AnchoredSpreadsheetSelection selection = ROW.setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.rowSelect(ID, NAME, selection);

        this.checkEquals(
            historyToken.clear(),
            HistoryToken.rowClear(
                ID,
                NAME,
                selection
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
            HistoryToken.cellSelect(
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

    // delete...........................................................................................................

    @Test
    public void testDeleteWithNotSpreadsheetNameHistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
            historyToken.delete(),
            historyToken
        );
    }

    @Test
    public void testDeleteCell() {
        final AnchoredSpreadsheetSelection viewport = CELL.setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.cellSelect(ID, NAME, viewport);

        this.checkEquals(
            historyToken.delete(),
            HistoryToken.cellDelete(
                ID,
                NAME,
                viewport
            )
        );
    }

    @Test
    public void testDeleteColumn() {
        final AnchoredSpreadsheetSelection viewport = COLUMN.setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.columnSelect(ID, NAME, viewport);

        this.checkEquals(
            historyToken.delete(),
            HistoryToken.columnDelete(
                ID,
                NAME,
                viewport
            )
        );
    }

    @Test
    public void testDeleteRow() {
        final AnchoredSpreadsheetSelection viewport = ROW.setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.rowSelect(ID, NAME, viewport);

        this.checkEquals(
            historyToken.delete(),
            HistoryToken.rowDelete(
                ID,
                NAME,
                viewport
            )
        );
    }

    // setFreeze........................................................................................................

    @Test
    public void testFreezeNotSpreadsheetNameHistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
            historyToken.freeze(),
            historyToken
        );
    }

    @Test
    public void testFreezeCell() {
        final AnchoredSpreadsheetSelection viewport = SpreadsheetSelection.A1.setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.cellSelect(ID, NAME, viewport);

        this.checkEquals(
            historyToken.freeze(),
            HistoryToken.cellFreeze(
                ID,
                NAME,
                viewport
            )
        );
    }

    @Test
    public void testFreezeColumn() {
        final AnchoredSpreadsheetSelection viewport = SpreadsheetSelection.parseColumn("A")
            .setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.columnSelect(ID, NAME, viewport);

        this.checkEquals(
            historyToken.freeze(),
            HistoryToken.columnFreeze(
                ID,
                NAME,
                viewport
            )
        );
    }

    @Test
    public void testFreezeRow() {
        final AnchoredSpreadsheetSelection viewport = SpreadsheetSelection.parseRow("1")
            .setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.rowSelect(ID, NAME, viewport);

        this.checkEquals(
            historyToken.freeze(),
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
            HistoryToken.labelMappingSelect(
                ID,
                NAME,
                LABEL
            )
        );
    }

    // locale...........................................................................................................

    @Test
    public void testLocaleWithSpreadsheetCellSelectHistoryToken() {
        this.checkEquals(
            HistoryToken.cellLocaleSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            ),
            HistoryToken.cellSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            ).locale()
        );
    }

    // menu.............................................................................................................

    @Test
    public void testMenuWithNullSelectionFails() {
        assertThrows(
            NullPointerException.class,
            () -> HistoryToken.unknown(UrlFragment.EMPTY)
                .menu(
                    null,
                    SpreadsheetLabelNameResolvers.fake()
                )
        );
    }

    @Test
    public void testMenuWithNullLabelNameResolverFails() {
        assertThrows(
            NullPointerException.class,
            () -> HistoryToken.unknown(UrlFragment.EMPTY)
                .menu(
                    Optional.of(SpreadsheetSelection.A1),
                    null
                )
        );
    }

    // menu with empty..................................................................................................

    @Test
    public void testMenuCellWithoutSelection() {
        final AnchoredSpreadsheetSelection cell = CELL.setDefaultAnchor();

        this.menuAndCheck(
            HistoryToken.cellSelect(
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
    public void testMenuCellRangeWithoutSelection() {
        final AnchoredSpreadsheetSelection range = CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT);

        this.menuAndCheck(
            HistoryToken.cellSelect(
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
    public void testMenuCellRangeWithoutSelection2() {
        final AnchoredSpreadsheetSelection range = CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.BOTTOM_RIGHT);

        this.menuAndCheck(
            HistoryToken.cellSelect(
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
    public void testMenuLabelWithoutSelection() {
        final AnchoredSpreadsheetSelection cell = LABEL.setDefaultAnchor();

        this.menuAndCheck(
            HistoryToken.cellSelect(
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
    public void testMenuColumnWithoutSelection() {
        final AnchoredSpreadsheetSelection column = COLUMN.setDefaultAnchor();

        this.menuAndCheck(
            HistoryToken.columnSelect(
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
    public void testMenuColumnRangeWithoutSelection() {
        final AnchoredSpreadsheetSelection range = COLUMN_RANGE.setAnchor(SpreadsheetViewportAnchor.LEFT);

        this.menuAndCheck(
            HistoryToken.columnSelect(
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
    public void testMenuColumnRangeWithoutSelection2() {
        final AnchoredSpreadsheetSelection range = COLUMN_RANGE.setAnchor(SpreadsheetViewportAnchor.RIGHT);

        this.menuAndCheck(
            HistoryToken.columnSelect(
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
    public void testMenuRowWithoutSelection() {
        final AnchoredSpreadsheetSelection row = ROW.setDefaultAnchor();

        this.menuAndCheck(
            HistoryToken.rowSelect(
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
    public void testMenuRowRangeWithoutSelection() {
        final AnchoredSpreadsheetSelection range = ROW_RANGE.setAnchor(SpreadsheetViewportAnchor.TOP);

        this.menuAndCheck(
            HistoryToken.rowSelect(
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
    public void testMenuRowRangeWithoutSelection2() {
        final AnchoredSpreadsheetSelection range = ROW_RANGE.setAnchor(SpreadsheetViewportAnchor.BOTTOM);

        this.menuAndCheck(
            HistoryToken.rowSelect(
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

    private void menuAndCheck(final HistoryToken token,
                              final HistoryToken expected) {
        this.menuAndCheck(
            token,
            Optional.empty(),
            expected
        );
    }

    // menu cell........................................................................................................

    @Test
    public void testMenuWithMissingSelection() {
        this.menuAndCheck(
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
    public void testMenuWithoutCellSelectAndCellSelection() {
        this.menuAndCheck(
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
    public void testMenuWithColumnRangeSelectAndCellSelection() {
        this.menuAndCheck(
            HistoryToken.columnSelect(
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
    public void testMenuWithCellSelectAndSameCellSelection() {
        this.menuAndCheck(
            HistoryToken.cellSelect(
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
    public void testMenuWithCellSelectAndDifferentCellSelection() {
        final SpreadsheetCellReference differentCell = SpreadsheetSelection.parseCell("B2");

        this.menuAndCheck(
            HistoryToken.cellSelect(
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
    public void testMenuWithCellRangeSelectAndInsideCellSelection() {
        final AnchoredSpreadsheetSelection range = SpreadsheetSelection.parseCellRange("A1:C3")
            .setAnchor(SpreadsheetViewportAnchor.BOTTOM_RIGHT);

        this.menuAndCheck(
            HistoryToken.cellSelect(
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
    public void testMenuWithCellRangeSelectAndOutsideCellSelection() {
        final SpreadsheetCellReference differentCell = SpreadsheetSelection.parseCell("C3");

        this.menuAndCheck(
            HistoryToken.cellSelect(
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

    // menu column......................................................................................................

    @Test
    public void testMenuWithoutColumnSelectAndColumnSelection() {
        this.menuAndCheck(
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
    public void testMenuWithRowRangeSelectAndColumnSelection() {
        this.menuAndCheck(
            HistoryToken.rowSelect(
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
    public void testMenuWithColumnSelectAndSameColumnSelection() {
        this.menuAndCheck(
            HistoryToken.columnSelect(
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
    public void testMenuWithColumnSelectAndDifferentColumnSelection() {
        final SpreadsheetColumnReference differentColumn = SpreadsheetSelection.parseColumn("B");

        this.menuAndCheck(
            HistoryToken.columnSelect(
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
    public void testMenuWithColumnRangeSelectAndInsideColumnSelection() {
        final AnchoredSpreadsheetSelection range = SpreadsheetSelection.parseColumnRange("A:C")
            .setAnchor(SpreadsheetViewportAnchor.RIGHT);

        this.menuAndCheck(
            HistoryToken.columnSelect(
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
    public void testMenuWithColumnRangeSelectAndOutsideColumnSelection() {
        final SpreadsheetColumnReference differentColumn = SpreadsheetSelection.parseColumn("C");

        this.menuAndCheck(
            HistoryToken.columnSelect(
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

    // menu row.........................................................................................................

    @Test
    public void testMenuWithoutRowSelectAndRowSelection() {
        this.menuAndCheck(
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
    public void testMenuWithCellSelectAndRowSelection() {
        this.menuAndCheck(
            HistoryToken.cellSelect(
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
    public void testMenuWithRowSelectAndSameRowSelection() {
        this.menuAndCheck(
            HistoryToken.rowSelect(
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
    public void testMenuWithRowSelectAndDifferentRowSelection() {
        final SpreadsheetRowReference differentRow = SpreadsheetSelection.parseRow("2");

        this.menuAndCheck(
            HistoryToken.rowSelect(
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
    public void testMenuWithRowRangeSelectAndInsideRowSelection() {
        final AnchoredSpreadsheetSelection range = SpreadsheetSelection.parseRowRange("1:3")
            .setAnchor(SpreadsheetViewportAnchor.BOTTOM);

        this.menuAndCheck(
            HistoryToken.rowSelect(
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
    public void testMenuWithRowRangeSelectAndOutsideRowSelection() {
        final SpreadsheetRowReference differentRow = SpreadsheetSelection.parseRow("3");

        this.menuAndCheck(
            HistoryToken.rowSelect(
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

    private void menuAndCheck(final HistoryToken token,
                              final SpreadsheetSelection selection,
                              final HistoryToken expected) {
        this.menuAndCheck(
            token,
            Optional.of(selection),
            expected
        );
    }

    private void menuAndCheck(final HistoryToken token,
                              final Optional<SpreadsheetSelection> selection,
                              final HistoryToken expected) {
        this.checkEquals(
            expected,
            token.menu(
                selection,
                SpreadsheetLabelNameResolvers.fake()
            ),
            () -> token + " menu " + selection
        );
    }

    // setMetadataPropertyName..........................................................................................

    @Test
    public void testSetMetadataPropertyName() {
        final SpreadsheetMetadataPropertyName<?> metadataProperty = SpreadsheetMetadataPropertyName.ROUNDING_MODE;

        this.checkEquals(
            HistoryToken.metadataPropertySelect(
                ID,
                NAME,
                metadataProperty
            ),
            HistoryToken.cellSelect(
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
        final HistoryToken historyToken = HistoryToken.cellSelect(ID, NAME, viewport);

        assertSame(
            historyToken,
            historyToken.setPatternKind(
                Optional.of(kind)
            )
        );
    }

    // plugin...........................................................................................................

    // reload...........................................................................................................

    @Test
    public void testReloadWithNotSpreadsheetNameHistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
            historyToken.reload(),
            historyToken
        );
    }

    @Test
    public void testReloadWithSpreadsheetNameHistoryToken() {
        final HistoryToken historyToken = HistoryToken.spreadsheetSelect(
            ID,
            NAME
        );

        this.checkEquals(
            historyToken.reload(),
            HistoryToken.spreadsheetReload(
                ID,
                NAME
            )
        );
    }

    @Test
    public void testReloadWithSpreadsheetCellSelectHistoryToken() {
        final HistoryToken historyToken = HistoryToken.cellSelect(
            ID,
            NAME,
            CELL.setDefaultAnchor()
        );

        this.checkEquals(
            historyToken.reload(),
            HistoryToken.cellReload(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    // setSaveStringValue.....................................................................................................

    @Test
    public void testSetSaveValueWithInvalidValueFails() {
        final HistoryToken historyToken = HistoryToken.cellDateTimeSymbolsSelect(
            ID,
            NAME,
            CELL.setDefaultAnchor()
        );

        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> historyToken.setSaveValue(
                Optional.of(this)
            )
        );

        this.checkEquals(
            "Invalid value: got HistoryTokenTest expected DateTimeSymbols",
            thrown.getMessage(),
            "message"
        );
    }

    @Test
    public void testSetSaveValueWithNotSpreadsheetNameHistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
            historyToken.setSaveStringValue("save-value"),
            historyToken
        );
    }

    // SELECTION........................................................................................................

    @Test
    public void testClearSelection() {
        this.checkEquals(
            HistoryToken.spreadsheetSelect(
                ID,
                NAME
            ),
            HistoryToken.cellSelect(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ).clearSelection()
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
            HistoryToken.cellSelect(
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
            HistoryToken.cellSelect(
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
            HistoryToken.columnSelect(
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
            HistoryToken.columnSelect(
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
            HistoryToken.rowSelect(
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
            HistoryToken.rowSelect(
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
            HistoryToken.cellSelect(
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
            HistoryToken.cellSelect(
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
            HistoryToken.columnSelect(
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
            HistoryToken.columnSelect(
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
            HistoryToken.rowSelect(
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
            HistoryToken.rowSelect(
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

    // style............................................................................................................

    @Test
    public void testStyleWithNotSpreadsheetNameHistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
            historyToken.style(TextStylePropertyName.COLOR),
            historyToken
        );
    }

    @Test
    public void testStyleCell() {
        final AnchoredSpreadsheetSelection viewport = CELL.setDefaultAnchor();
        final TextStylePropertyName<Color> property = TextStylePropertyName.COLOR;
        final HistoryToken historyToken = HistoryToken.cellSelect(ID, NAME, viewport);

        this.checkEquals(
            historyToken.style(property),
            HistoryToken.cellStyle(
                ID,
                NAME,
                viewport,
                property
            )
        );
    }

    @Test
    public void testStyleCell2() {
        final AnchoredSpreadsheetSelection viewport = CELL.setDefaultAnchor();
        final TextStylePropertyName<Color> property = TextStylePropertyName.OUTLINE_COLOR;
        final HistoryToken historyToken = HistoryToken.cellSelect(ID, NAME, viewport);

        this.checkEquals(
            historyToken.style(property),
            HistoryToken.cellStyle(
                ID,
                NAME,
                viewport,
                property
            )
        );
    }

    @Test
    public void testStyleColumn() {
        final AnchoredSpreadsheetSelection viewport = COLUMN.setDefaultAnchor();
        final TextStylePropertyName<Color> property = TextStylePropertyName.COLOR;
        final HistoryToken historyToken = HistoryToken.columnSelect(ID, NAME, viewport);

        assertSame(
            historyToken.style(property),
            historyToken
        );
    }

    @Test
    public void testStyleRow() {
        final AnchoredSpreadsheetSelection viewport = ROW.setDefaultAnchor();
        final TextStylePropertyName<Color> property = TextStylePropertyName.COLOR;
        final HistoryToken historyToken = HistoryToken.rowSelect(ID, NAME, viewport);

        assertSame(
            historyToken.style(property),
            historyToken
        );
    }

    // unfreeze.........................................................................................................

    @Test
    public void testUnfreezeNotSpreadsheetNameHistoryTokenSubclass() {
        final HistoryToken historyToken = HistoryToken.unknown(UrlFragment.parse("/something else"));

        assertSame(
            historyToken.unfreeze(),
            historyToken
        );
    }

    @Test
    public void testUnfreezeCell() {
        final AnchoredSpreadsheetSelection viewport = SpreadsheetSelection.A1.setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.cellSelect(ID, NAME, viewport);

        this.checkEquals(
            historyToken.unfreeze(),
            HistoryToken.cellUnfreeze(
                ID,
                NAME,
                viewport
            )
        );
    }

    @Test
    public void testUnfreezeColumn() {
        final AnchoredSpreadsheetSelection viewport = SpreadsheetSelection.parseColumn("A")
            .setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.columnSelect(ID, NAME, viewport);

        this.checkEquals(
            historyToken.unfreeze(),
            HistoryToken.columnUnfreeze(
                ID,
                NAME,
                viewport
            )
        );
    }

    @Test
    public void testUnfreezeRow() {
        final AnchoredSpreadsheetSelection viewport = SpreadsheetSelection.parseRow("1")
            .setDefaultAnchor();
        final HistoryToken historyToken = HistoryToken.rowSelect(ID, NAME, viewport);

        this.checkEquals(
            historyToken.unfreeze(),
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
            HistoryToken.cellSelect(
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
            HistoryToken.cellSelect(
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
            HistoryToken.cellSelect(
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
            HistoryToken.columnSelect(
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
            HistoryToken.columnSelect(
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
            HistoryToken.rowSelect(
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
            HistoryToken.rowSelect(
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
        this.parseStringAndCheck(
            "hello",
            HistoryToken.spreadsheetListSelect(HistoryTokenOffsetAndCount.EMPTY)
        );
    }

    @Test
    public void testParseEmpty() {
        this.parseStringAndCheck(
            "",
            HistoryToken.spreadsheetListSelect(HistoryTokenOffsetAndCount.EMPTY)
        );
    }

    @Test
    public void testParseSlash() {
        this.parseStringAndCheck(
            "/",
            HistoryToken.spreadsheetListSelect(HistoryTokenOffsetAndCount.EMPTY)
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
            HistoryToken.spreadsheetListSelect(HistoryTokenOffsetAndCount.EMPTY)
        );
    }

    // plugin...........................................................................................................

    @Test
    public void testParsePlugin() {
        this.parseStringAndCheck(
            "/plugin",
            HistoryToken.pluginListSelect(HistoryTokenOffsetAndCount.EMPTY)
        );
    }

    @Test
    public void testParsePluginOffset() {
        this.parseStringAndCheck(
            "/plugin/*/offset/123",
            HistoryToken.pluginListSelect(
                HistoryTokenOffsetAndCount.with(
                    OptionalInt.of(123), // offset
                    OptionalInt.empty() // count
                )
            )
        );
    }


    @Test
    public void testParsePluginCount() {
        this.parseStringAndCheck(
            "/plugin/*/count/456",
            HistoryToken.pluginListSelect(
                HistoryTokenOffsetAndCount.with(
                    OptionalInt.empty(), // offset
                    OptionalInt.of(456) // count
                )
            )
        );
    }

    @Test
    public void testParsePluginReload() {
        this.parseStringAndCheck(
            "/plugin/reload",
            HistoryToken.pluginSelect(
                PluginName.with("reload")
            )
        );
    }

    @Test
    public void testParsePluginStarReload() {
        this.parseStringAndCheck(
            "/plugin/*/reload",
            HistoryToken.pluginListReload(
                HistoryTokenOffsetAndCount.with(
                    OptionalInt.empty(), // offset
                    OptionalInt.empty() // count
                )
            )
        );
    }

    @Test
    public void testParsePluginStarOffsetReload() {
        this.parseStringAndCheck(
            "/plugin/*/offset/123/reload",
            HistoryToken.pluginListReload(
                HistoryTokenOffsetAndCount.with(
                    OptionalInt.of(123), // offset
                    OptionalInt.empty() // count
                )
            )
        );
    }

    @Test
    public void testParsePluginPluginNameSave() {
        this.parseStringAndCheck(
            "/plugin/TestPluginName123/save/text123",
            HistoryToken.pluginSave(
                PluginName.with("TestPluginName123"),
                "text123"
            )
        );
    }

    @Test
    public void testParsePluginSlashUpload() {
        this.parseStringAndCheck(
            "/plugin/upload",
            HistoryToken.pluginSelect(
                PluginName.with("upload")
            )
        );
    }

    @Test
    public void testParsePluginUploadSelect() {
        this.parseStringAndCheck(
            "/plugin-upload",
            HistoryToken.pluginUploadSelect()
        );
    }

    @Test
    public void testParsePluginUploadSave() {
        this.parseStringAndCheck(
            "/plugin-upload/save/base64/Filename123/FileContent456",
            HistoryToken.pluginUploadSave(
                BrowserFile.base64(
                    "Filename123",
                    "FileContent456"
                )
            )
        );
    }

    // plugin/name......................................................................................................

    @Test
    public void testParsePluginNameInvalid() {
        this.parseStringAndCheck(
            "/plugin/!TestPluginName123",
            HistoryToken.pluginListSelect(
                HistoryTokenOffsetAndCount.EMPTY
            )
        );
    }

    @Test
    public void testParsePluginName() {
        this.parseStringAndCheck(
            "/plugin/TestPluginName123",
            HistoryToken.pluginSelect(
                PluginName.with("TestPluginName123")
            )
        );
    }

    // spreadsheet......................................................................................................

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
            SPREADSHEET_ID_SPREADSHEET_NAME_TOKEN
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
            SPREADSHEET_ID_SPREADSHEET_NAME_TOKEN
        );
    }

    // cell.............................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellMissingReference() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell",
            SPREADSHEET_ID_SPREADSHEET_NAME_TOKEN
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellInvalidReference() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/!!!",
            SPREADSHEET_ID_SPREADSHEET_NAME_TOKEN
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellInvalidReference2() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/!!!/cell/A1",
            SPREADSHEET_ID_SPREADSHEET_NAME_TOKEN
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellCellReference() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1",
            CELL_TOKEN
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellInvalidAnchor() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/bottom-left",
            CELL_TOKEN
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellLabelName() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/Label123",
            HistoryToken.cellSelect(
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
            HistoryToken.cellSelect(
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
            HistoryToken.cellSelect(
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
            HistoryToken.cellSelect(
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
            HistoryToken.cellSelect(
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
            CELL_TOKEN
        );
    }

    // clear............................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellClear() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/clear",
            HistoryToken.cellSelect(
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
            HistoryToken.cellSelect(
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
            HistoryToken.cellSelect(
                ID,
                NAME,
                CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_RIGHT)
            )
        );
    }

    // cell/dateTimeSymbols.............................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellDateTimeSymbols() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/dateTimeSymbols",
            HistoryToken.cellDateTimeSymbolsSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellDateTimeSymbolsToolbar() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/dateTimeSymbols/toolbar",
            HistoryToken.cellDateTimeSymbolsUnselect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellDateTimeSymbolsSaveEmpty() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/dateTimeSymbols/save/",
            HistoryToken.cellDateTimeSymbolsSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                Optional.empty()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellDateTimeSymbolsSave() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/dateTimeSymbols/save/" + DATE_TIME_SYMBOLS.text(),
            HistoryToken.cellDateTimeSymbolsSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                Optional.of(DATE_TIME_SYMBOLS)
            )
        );
    }

    // cell/decimalNumberSymbols........................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellDecimalNumberSymbols() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/decimalNumberSymbols",
            HistoryToken.cellDecimalNumberSymbolsSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellDecimalNumberSymbolsToolbar() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/decimalNumberSymbols/toolbar",
            HistoryToken.cellDecimalNumberSymbolsUnselect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellDecimalNumberSymbolsSaveEmpty() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/decimalNumberSymbols/save/",
            HistoryToken.cellDecimalNumberSymbolsSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                Optional.empty()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellDecimalNumberSymbolsSave() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/decimalNumberSymbols/save/" + urlEncode(DECIMAL_NUMBER_SYMBOLS.text()),
            HistoryToken.cellDecimalNumberSymbolsSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                Optional.of(DECIMAL_NUMBER_SYMBOLS)
            )
        );
    }

    // cell/delete......................................................................................................

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

    // cell/form/FormName...............................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellForm() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/form",
            HistoryToken.cellSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellFormFormName() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/form/FormName123",
            HistoryToken.cellFormSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                FormName.with("FormName123")
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellFormFormNameSave() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/form/FormName123/save",
            HistoryToken.cellFormSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                FormName.with("FormName123"),
                SpreadsheetCellReferenceToValueMap.EMPTY
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellFormFormNameSaveValue() {
        final Map<SpreadsheetCellReference, Optional<Object>> cellToValues = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(
                ExpressionNumberKind.BIG_DECIMAL.create(12.5)
            ),
            SpreadsheetSelection.parseCell("A2"),
            Optional.of("String222"),
            SpreadsheetSelection.parseCell("A3"),
            Optional.of(
                LocalDate.of(1999, 12, 31)
            )
        );

        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/form/FormName123/save/" + SpreadsheetCellReferenceToValueMap.with(cellToValues).urlFragment(),
            HistoryToken.cellFormSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                FormName.with("FormName123"),
                cellToValues
            )
        );
    }

    // cell/formatter...................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellFormatter() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/formatter",
            HistoryToken.cellFormatterSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellFormatterToolbar() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/formatter/toolbar",
            HistoryToken.cellFormatterUnselect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellFormatterSaveEmptyDateFormat() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/formatter/save/",
            HistoryToken.cellFormatterSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                Optional.empty()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellFormatterSaveTimeFormat() {
        final SpreadsheetFormatterSelector selector = SpreadsheetPattern.parseTimeFormatPattern("hh:mm:ss")
            .spreadsheetFormatterSelector();

        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/formatter/save/" + selector,
            HistoryToken.cellFormatterSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                Optional.of(selector)
            )
        );
    }

    // cell/formula.....................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellFormula() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/formula",
            HistoryToken.cellFormula(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellFormulaMenu() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/formula/menu",
            HistoryToken.cellFormulaMenu(
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
            HistoryToken.cellFormulaSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                "=1+2"
            )
        );
    }

    // cell/freeze......................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellFreezeInvalidColumnFails() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/B1/freeze",
            HistoryToken.cellSelect(
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
            HistoryToken.cellSelect(
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
            HistoryToken.cellSelect(
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
            HistoryToken.cellSelect(
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

    // cell/label.......................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellCellReferenceLabel() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/label",
            HistoryToken.cellLabelSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellCellReferenceLabelSlash() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/label/",
            HistoryToken.cellLabelSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellCellRangeLabel() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/B2:C3/label",
            HistoryToken.cellLabelSelect(
                ID,
                NAME,
                CELL_RANGE.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellCellRangeBottomLeftLabel() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/B2:C3/bottom-left/label",
            HistoryToken.cellLabelSelect(
                ID,
                NAME,
                CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.BOTTOM_LEFT)
            )
        );
    }

    // cell/labels......................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellLabels() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/labels",
            HistoryToken.cellLabels(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                HistoryTokenOffsetAndCount.EMPTY
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellLabelsInvalidOffset() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/labels/offset/-1",
            HistoryToken.cellLabels(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                HistoryTokenOffsetAndCount.EMPTY
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellLabelsInvalidCount() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/labels/count/-1",
            HistoryToken.cellLabels(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                HistoryTokenOffsetAndCount.EMPTY
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellLabelsOffsetCount() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/labels/offset/111/count/222",
            HistoryToken.cellLabels(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                HistoryTokenOffsetAndCount.with(
                    OptionalInt.of(111), // offset
                    OptionalInt.of(222) // count
                )
            )
        );
    }

    // cell/menu........................................................................................................

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

    // cell/navigate....................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellNavigate() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/navigate",
            HistoryToken.cellNavigate(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                Optional.empty()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellNavigateHomeNavigation() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/navigate/Z99/right%20444px",
            HistoryToken.cellNavigate(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                Optional.of(
                    SpreadsheetViewportHomeNavigationList.with(
                        SpreadsheetSelection.parseCell("Z99")
                    ).setNavigations(
                        SpreadsheetViewportNavigationList.parse("right 444px")
                    )
                )
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellRangeNavigateHomeNavitations() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/B2:C3/top-left/navigate/Z99/right%20444px",
            HistoryToken.cellNavigate(
                ID,
                NAME,
                CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
                Optional.of(
                    SpreadsheetViewportHomeNavigationList.with(
                        SpreadsheetSelection.parseCell("Z99")
                    ).setNavigations(
                        SpreadsheetViewportNavigationList.parse("right 444px")
                    )
                )
            )
        );
    }

    // cell/parser......................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellParser() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/parser",
            HistoryToken.cellParserSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellParserToolbar() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/parser/toolbar",
            HistoryToken.cellParserUnselect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellParserSaveTimeParsePattern() {
        final SpreadsheetParserSelector selector = SpreadsheetPattern.parseTimeParsePattern("hh:mm:ss")
            .spreadsheetParserSelector();

        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/parser/save/" + selector,
            HistoryToken.cellParserSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                Optional.of(selector)
            )
        );
    }

    // cell/references..................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellReferences() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/references",
            HistoryToken.cellReferences(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                HistoryTokenOffsetAndCount.EMPTY
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellReferencesInvalidOffset() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/references/offset/-1",
            HistoryToken.cellReferences(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                HistoryTokenOffsetAndCount.EMPTY
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellReferencesInvalidCount() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/references/count/-1",
            HistoryToken.cellReferences(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                HistoryTokenOffsetAndCount.EMPTY
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellReferencesOffsetCount() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/references/offset/111/count/222",
            HistoryToken.cellReferences(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                HistoryTokenOffsetAndCount.with(
                    OptionalInt.of(111), // offset
                    OptionalInt.of(222) // count
                )
            )
        );
    }

    // cell/reload......................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellReload() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/reload",
            HistoryToken.cellReload(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    // cell/save........................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellCellSaveEmptyLabel() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/label/save/",
            HistoryToken.cellLabelSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellCellSaveLabel() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/label/save/Label123",
            HistoryToken.cellLabelSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                LABEL
            )
        );
    }

    // cell/sort........................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellSort() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/sort",
            HistoryToken.cellSelect(
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
            HistoryToken.cellSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    // cell/style.......................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellStyleMissingStyleProperty() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/style",
            CELL_TOKEN
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellStyleInvalidPropertyName() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/style/!invalid",
            CELL_TOKEN
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
    public void testParseSpreadsheetIdSpreadsheetNameCellStyleSaveWithoutValue() {
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

    // cell/unfreeze....................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellRangeUnfreezeInvalidColumn() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/B2:C3/unfreeze",
            HistoryToken.cellSelect(
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
            HistoryToken.cellSelect(
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
            HistoryToken.cellSelect(
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
            HistoryToken.cellSelect(
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

    // cell/validator...................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellValidator() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/validator",
            HistoryToken.cellValidatorSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellValidatorToolbar() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/validator/toolbar",
            HistoryToken.cellValidatorUnselect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellValidatorSaveEmpty() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/validator/save/",
            HistoryToken.cellValidatorSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                Optional.empty()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellValidatorSave() {
        final ValidatorSelector validator = ValidatorSelector.parse("hello-validator");

        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/validator/save/" + urlEncode(validator.toString()),
            HistoryToken.cellValidatorSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                Optional.of(validator)
            )
        );
    }

    // cell/value.......................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellValueMissingValueType() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/value",
            HistoryToken.cellValueUnselect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellValueValueType() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/value/text",
            HistoryToken.cellValueSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValidationValueTypeName.TEXT
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellRangeValueValueType() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1:A2/value/text",
            HistoryToken.cellValueSelect(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A2")
                    .setDefaultAnchor(),
                ValidationValueTypeName.TEXT
            )
        );
    }


    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelValueValueType() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/Label123/value/text",
            HistoryToken.cellValueSelect(
                ID,
                NAME,
                SpreadsheetSelection.labelName("Label123")
                    .setDefaultAnchor(),
                ValidationValueTypeName.TEXT
            )
        );
    }

    // cell/valueType...................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellValueType() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/valueType",
            HistoryToken.cellValueTypeSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellValueTypeToolbar() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/valueType/toolbar",
            HistoryToken.cellValueTypeUnselect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellValueTypeSaveEmpty() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/valueType/save/",
            HistoryToken.cellValueTypeSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                Optional.empty()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCellValueTypeSave() {
        final ValidationValueTypeName valueType = ValidationValueTypeName.with("hello-value-type");

        this.parseStringAndCheck(
            "/123/SpreadsheetName456/cell/A1/valueType/save/" + urlEncode(valueType.value()),
            HistoryToken.cellValueTypeSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                Optional.of(valueType)
            )
        );
    }

    // column.............................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnMissingReference() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/column",
            SPREADSHEET_ID_SPREADSHEET_NAME_TOKEN
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnInvalidReference() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/column/!invalid",
            SPREADSHEET_ID_SPREADSHEET_NAME_TOKEN
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnInvalidReference2() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/column/!invalid/column/A",
            SPREADSHEET_ID_SPREADSHEET_NAME_TOKEN
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnColumnReference() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/column/AA",
            HistoryToken.columnSelect(
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
            HistoryToken.columnSelect(
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
            HistoryToken.columnSelect(
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
            HistoryToken.columnSelect(
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
            HistoryToken.columnSelect(
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
            HistoryToken.columnSelect(
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
            HistoryToken.columnSelect(
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
    public void testParseSpreadsheetIdSpreadsheetNameColumnFormula() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/column/AA",
            HistoryToken.columnSelect(
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
            HistoryToken.columnSelect(
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
            HistoryToken.columnSelect(
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
    public void testParseSpreadsheetIdSpreadsheetNameColumnNavigate() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/column/AA/navigate",
            HistoryToken.columnNavigate(
                ID,
                NAME,
                COLUMN.setDefaultAnchor(),
                Optional.empty()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnNavigateHomeNavigations() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/column/AA/navigate/Z99/right%20444px",
            HistoryToken.columnNavigate(
                ID,
                NAME,
                COLUMN.setDefaultAnchor(),
                Optional.of(
                SpreadsheetViewportHomeNavigationList.with(
                    SpreadsheetSelection.parseCell("Z99")
                ).setNavigations(
                    SpreadsheetViewportNavigationList.parse("right 444px")
                )
                )
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnRangeNavigateHomeNavigations() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/column/BB:CC/left/navigate/Z99/right%20444px",
            HistoryToken.columnNavigate(
                ID,
                NAME,
                COLUMN_RANGE.setAnchor(SpreadsheetViewportAnchor.LEFT),
                Optional.of(
                SpreadsheetViewportHomeNavigationList.with(
                    SpreadsheetSelection.parseCell("Z99")
                ).setNavigations(
                    SpreadsheetViewportNavigationList.parse("right 444px")
                )
                )
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameColumnPattern() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/column/AA/formatter/yymmdd",
            HistoryToken.columnSelect(
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
            HistoryToken.columnSelect(
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
            HistoryToken.columnSelect(
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

    // form.............................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameForm() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/form",
            HistoryToken.formList(
                ID,
                NAME,
                HistoryTokenOffsetAndCount.EMPTY
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameFormOffsetAndCount() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/form/*/offset/1/count/2",
            HistoryToken.formList(
                ID,
                NAME,
                HistoryTokenOffsetAndCount.EMPTY.setOffset(
                    OptionalInt.of(1)
                ).setCount(
                    OptionalInt.of(2)
                )
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameFormFormName() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/form/FormName123",
            HistoryToken.formSelect(
                ID,
                NAME,
                FormName.with("FormName123")
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameFormFormNameDelete() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/form/FormName123/delete",
            HistoryToken.formDelete(
                ID,
                NAME,
                FormName.with("FormName123")
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameFormFormNameSave() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/form/FormName123/save",
            HistoryToken.formSelect(
                ID,
                NAME,
                FormName.with("FormName123")
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameFormFormNameSaveForm() {
        final Form<SpreadsheetExpressionReference> form = SpreadsheetForms.form(
            FormName.with("FormName123")
        ).setFields(
            Lists.of(
                SpreadsheetForms.field(
                    SpreadsheetSelection.A1
                ).setLabel("LabelA1")
            )
        );

        this.parseStringAndCheck(
            "/123/SpreadsheetName456/form/FormName123/save/" + JSON_NODE_MARSHALL_CONTEXT.marshall(form),
            HistoryToken.formSave(
                ID,
                NAME,
                form
            )
        );
    }

    // label............................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameCreateLabel() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/create-label",
            HistoryToken.labelMappingCreate(
                ID,
                NAME
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelInvalid() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/label/!!/cell/A1",
            SPREADSHEET_ID_SPREADSHEET_NAME_TOKEN
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelLabelReference() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/label/Label123",
            LABEL_MAPPING_TOKEN
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelInvalidAction() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/label/Label123/!invalid",
            LABEL_MAPPING_TOKEN
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelClear() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/label/Label123/clear",
            LABEL_MAPPING_TOKEN
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
            LABEL_MAPPING_TOKEN
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelMenu() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/label/Label123/menu",
            LABEL_MAPPING_TOKEN
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelUnfreeze() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/label/Label123/unfreeze",
            LABEL_MAPPING_TOKEN
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
    public void testParseSpreadsheetIdSpreadsheetNameLabel() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/label",
            HistoryToken.labelMappingList(
                ID,
                NAME,
                HistoryTokenOffsetAndCount.EMPTY
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelStar() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/label/*",
            HistoryToken.labelMappingList(
                ID,
                NAME,
                HistoryTokenOffsetAndCount.EMPTY
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelStarOffsetCount() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/label/*/offset/123/count/456",
            HistoryToken.labelMappingList(
                ID,
                NAME,
                HistoryTokenOffsetAndCount.with(
                    OptionalInt.of(123),
                    OptionalInt.of(456)
                )
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelSaveMissingReference() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/label/Label123/save",
            HistoryToken.labelMappingSelect(
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
            HistoryToken.labelMappingSelect(
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
                LABEL.setLabelMappingReference(CELL)
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
                LABEL.setLabelMappingReference(CELL_RANGE)
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
                LABEL.setLabelMappingReference(
                    SpreadsheetSelection.labelName("Label456")
                )
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelFormula() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/label/Label123",
            LABEL_MAPPING_TOKEN
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelPattern() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/label/Label123/formatter/yymmdd",
            LABEL_MAPPING_TOKEN
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameLabelStyle() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/label/Label123/style",
            LABEL_MAPPING_TOKEN
        );
    }

    // metadata.........................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameSpreadsheet() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/spreadsheet",
            HistoryToken.metadataSelect(
                ID,
                NAME
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameMetadataPropertyNameInvalid() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/spreadsheet/!invalid",
            HistoryToken.metadataSelect(
                ID,
                NAME
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameMetadataPropertyName() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/spreadsheet/defaultYear",
            HistoryToken.metadataPropertySelect(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DEFAULT_YEAR
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameMetadataPropertyNameSaveInvalid() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/spreadsheet/defaultYear/save/XYZ",
            HistoryToken.metadataPropertySelect(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DEFAULT_YEAR
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameMetadataPropertyNameSaveWithoutValue() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/spreadsheet/defaultYear/save/",
            HistoryToken.metadataPropertySave(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DEFAULT_YEAR,
                Optional.empty()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameMetadataPropertyNameSave() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/spreadsheet/defaultYear/save/1950",
            HistoryToken.metadataPropertySave(
                ID,
                NAME,
                SpreadsheetMetadataPropertyName.DEFAULT_YEAR,
                Optional.of(1950)
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameMetadataStylePropertyNameInvalid() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/spreadsheet/style/!invalid",
            HistoryToken.metadataSelect(
                ID,
                NAME
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameMetadataStylePropertyName() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/spreadsheet/style/color",
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
            "/123/SpreadsheetName456/spreadsheet/style/color/save/",
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
            "/123/SpreadsheetName456/spreadsheet/style/color/save/#123456",
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

    // /navigate........................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameNavigateEmpty() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/navigate",
            HistoryToken.navigate(
                ID,
                NAME,
                Optional.empty()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameNavigate() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/navigate/Z99/right%20444px",
            HistoryToken.navigate(
                ID,
                NAME,
                Optional.of(
                    SpreadsheetViewportHomeNavigationList.with(
                        SpreadsheetSelection.parseCell("Z99")
                    ).setNavigations(
                        SpreadsheetViewportNavigationList.parse("right 444px")
                    )
                )
            )
        );
    }

    // row.............................................................................................................

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowMissingReference() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/row",
            SPREADSHEET_ID_SPREADSHEET_NAME_TOKEN
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowInvalidReference() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/row/A1",
            SPREADSHEET_ID_SPREADSHEET_NAME_TOKEN
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowInvalidReference2() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/row/123456789/row/1",
            SPREADSHEET_ID_SPREADSHEET_NAME_TOKEN
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowRowReference() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/row/11",
            HistoryToken.rowSelect(
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
            HistoryToken.rowSelect(
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
            HistoryToken.rowSelect(
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
            HistoryToken.rowSelect(
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
            HistoryToken.rowSelect(
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
            HistoryToken.rowSelect(
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
            HistoryToken.rowSelect(
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
    public void testParseSpreadsheetIdSpreadsheetNameRowFormula() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/row/11",
            HistoryToken.rowSelect(
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
            HistoryToken.rowSelect(
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
            HistoryToken.rowSelect(
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
    public void testParseSpreadsheetIdSpreadsheetNameRowNavigate() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/row/11/navigate",
            HistoryToken.rowNavigate(
                ID,
                NAME,
                ROW.setDefaultAnchor(),
                Optional.empty()
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowNavigateHomeNavigations() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/row/11/navigate/Z99/down%20444px",
            HistoryToken.rowNavigate(
                ID,
                NAME,
                ROW.setDefaultAnchor(),
                Optional.of(
                    SpreadsheetViewportHomeNavigationList.with(
                        SpreadsheetSelection.parseCell("Z99")
                    ).setNavigations(
                        SpreadsheetViewportNavigationList.parse("down 444px")
                    )
                )
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowRangeNavigateHomeNavigations() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/row/22:33/bottom/navigate/Z99/down%20444px",
            HistoryToken.rowNavigate(
                ID,
                NAME,
                ROW_RANGE.setAnchor(SpreadsheetViewportAnchor.BOTTOM),
                Optional.of(
                    SpreadsheetViewportHomeNavigationList.with(
                        SpreadsheetSelection.parseCell("Z99")
                    ).setNavigations(
                        SpreadsheetViewportNavigationList.parse("down 444px")
                    )
                )
            )
        );
    }

    @Test
    public void testParseSpreadsheetIdSpreadsheetNameRowPattern() {
        this.parseStringAndCheck(
            "/123/SpreadsheetName456/row/11/formatter/yymmdd",
            HistoryToken.rowSelect(
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
            HistoryToken.rowSelect(
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
            HistoryToken.rowSelect(
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
            HistoryToken.rowSelect(
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

    @Override
    public HistoryToken parseString(final String urlFragment) {
        return HistoryToken.parseString(urlFragment);
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> thrown) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException thrown) {
        throw new UnsupportedOperationException();
    }

    private String urlEncode(final Object object) {
        return URLEncoder.encode(object.toString());
    }

    // class............................................................................................................

    @Override
    public Class<HistoryToken> type() {
        return HistoryToken.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
