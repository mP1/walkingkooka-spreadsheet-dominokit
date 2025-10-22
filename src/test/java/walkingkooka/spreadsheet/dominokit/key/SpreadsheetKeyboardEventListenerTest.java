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

package walkingkooka.spreadsheet.dominokit.key;

import elemental2.dom.KeyboardEvent;
import org.dominokit.domino.ui.events.EventType;
import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportAnchor;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.FontWeight;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextDecorationLine;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.VerticalAlign;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetKeyboardEventListenerTest implements TreePrintableTesting {

    private final static SpreadsheetId SPREADSHEET_ID = SpreadsheetId.with(1);

    private final static SpreadsheetName SPREADSHEET_NAME = SpreadsheetName.with("SpreadsheetName1");

    private final static SpreadsheetCellReference CELL = SpreadsheetSelection.A1;

    private final static AnchoredSpreadsheetSelection CELL_RANGE = SpreadsheetSelection.parseCellRange("A1:B2")
        .setAnchor(SpreadsheetViewportAnchor.TOP_LEFT);

    // with.............................................................................................................

    @Test
    public void testWithNullSpreadsheetKeyBindingFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetKeyboardEventListener.with(
                null,
                SpreadsheetKeyboardContexts.fake()
            )
        );
    }

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetKeyboardEventListener.with(
                SpreadsheetKeyBindings.fake(),
                null
            )
        );
    }

    // bold.............................................................................................................

    @Test
    public void testHandleEventWithBoldWithoutCellSelection() {
        final KeyboardEvent event = controlKey("b");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.spreadsheetSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME
                )
            )
        );
    }

    @Test
    public void testHandleEventWithBoldWithCellSelectionWithNonBoldFontWeight() {
        final KeyboardEvent event = controlKey("b");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.FONT_WEIGHT,
                            FontWeight.with(1)
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.FONT_WEIGHT,
                Optional.of(FontWeight.BOLD)
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    @Test
    public void testHandleEventWithBoldWithCellSelectionWithoutBold() {
        final KeyboardEvent event = controlKey("b");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.FONT_WEIGHT,
                Optional.of(FontWeight.BOLD)
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    @Test
    public void testHandleEventWithBoldWithCellSelectionWithBold() {
        final KeyboardEvent event = controlKey("b");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.FONT_WEIGHT,
                            FontWeight.BOLD
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.FONT_WEIGHT,
                Optional.empty()
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    @Test
    public void testHandleEventWithBoldWithCellRangeSelectionWithoutBold() {
        final KeyboardEvent event = controlKey("b");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL_RANGE
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL_RANGE,
                TextStylePropertyName.FONT_WEIGHT,
                Optional.of(FontWeight.BOLD)
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    @Test
    public void testHandleEventWithBoldKey2WithCellRangeSelectionWithoutBold() {
        final KeyboardEvent event = controlKey("2");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL_RANGE
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL_RANGE,
                TextStylePropertyName.FONT_WEIGHT,
                Optional.of(FontWeight.BOLD)
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    // bottomTextAlignment..............................................................................................

    @Test
    public void testHandleEventWithBottomTextAlignWithoutCellSelection() {
        final KeyboardEvent event = shiftControlKey("B");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.spreadsheetSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME
                )
            )
        );
    }

    @Test
    public void testHandleEventWithBottomTextAlignWithCellSelectionWithTop() {
        final KeyboardEvent event = shiftControlKey("B");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.VERTICAL_ALIGN,
                            VerticalAlign.TOP
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.VERTICAL_ALIGN,
                Optional.of(VerticalAlign.BOTTOM)
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    @Test
    public void testHandleEventWithBottomTextAlignWithCellSelectionWithBottom() {
        final KeyboardEvent event = shiftControlKey("B");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.VERTICAL_ALIGN,
                            VerticalAlign.BOTTOM
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.VERTICAL_ALIGN,
                Optional.of(VerticalAlign.BOTTOM)
            )
        );

        this.defaultPreventedAndCheck(event);
    }
    
    // centerTextAlignment..............................................................................................

    @Test
    public void testHandleEventWithCenterTextAlignWithoutCellSelection() {
        final KeyboardEvent event = controlKey("c");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.spreadsheetSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME
                )
            )
        );
    }

    @Test
    public void testHandleEventWithCenterTextAlignWithCellSelectionWithLeftAlign() {
        final KeyboardEvent event = controlKey("c");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.TEXT_ALIGN,
                            TextAlign.LEFT
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.TEXT_ALIGN,
                Optional.of(TextAlign.CENTER)
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    @Test
    public void testHandleEventWithCenterTextAlignWithCellSelectionWithCenterAlign() {
        final KeyboardEvent event = controlKey("c");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.TEXT_ALIGN,
                            TextAlign.CENTER
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.TEXT_ALIGN,
                Optional.of(TextAlign.CENTER)
            )
        );

        this.defaultPreventedAndCheck(event);
    }
    
    // italics..........................................................................................................

    @Test
    public void testHandleEventWithItalicsWithoutCellSelection() {
        final KeyboardEvent event = controlKey("i");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.spreadsheetSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME
                )
            )
        );
    }

    @Test
    public void testHandleEventWithItalicsWithCellSelectionWithoutFontStyle() {
        final KeyboardEvent event = controlKey("i");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.FONT_STYLE,
                Optional.of(FontStyle.ITALIC)
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    @Test
    public void testHandleEventWithItalicsWithCellSelectionWithNormal() {
        final KeyboardEvent event = controlKey("i");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.FONT_STYLE,
                            FontStyle.NORMAL
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.FONT_STYLE,
                Optional.of(FontStyle.ITALIC)
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    @Test
    public void testHandleEventWithItalicsWithCellSelectionWithItalics() {
        final KeyboardEvent event = controlKey("i");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.FONT_STYLE,
                            FontStyle.ITALIC
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.FONT_STYLE,
                Optional.empty()
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    @Test
    public void testHandleEventWithItalicsThreeDigitWithCellSelectionWithItalics() {
        final KeyboardEvent event = controlKey("3");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.FONT_STYLE,
                            FontStyle.ITALIC
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.FONT_STYLE,
                Optional.empty()
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    // justifyTextAlignment..............................................................................................

    @Test
    public void testHandleEventWithJustifyTextAlignWithoutCellSelection() {
        final KeyboardEvent event = controlKey("j");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.spreadsheetSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME
                )
            )
        );
    }

    @Test
    public void testHandleEventWithJustifyTextAlignWithCellSelectionWithLeftAlign() {
        final KeyboardEvent event = controlKey("j");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.TEXT_ALIGN,
                            TextAlign.LEFT
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.TEXT_ALIGN,
                Optional.of(TextAlign.JUSTIFY)
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    @Test
    public void testHandleEventWithJustifyTextAlignWithCellSelectionWithJustifyAlign() {
        final KeyboardEvent event = controlKey("j");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.TEXT_ALIGN,
                            TextAlign.JUSTIFY
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.TEXT_ALIGN,
                Optional.of(TextAlign.JUSTIFY)
            )
        );

        this.defaultPreventedAndCheck(event);
    }
    
    // leftTextAlignment.............................................................................................................

    @Test
    public void testHandleEventWithLeftTextAlignWithoutCellSelection() {
        final KeyboardEvent event = controlKey("l");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.spreadsheetSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME
                )
            )
        );
    }

    @Test
    public void testHandleEventWithLeftTextAlignWithCellSelectionWithRightAlign() {
        final KeyboardEvent event = controlKey("l");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.TEXT_ALIGN,
                            TextAlign.RIGHT
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.TEXT_ALIGN,
                Optional.of(TextAlign.LEFT)
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    @Test
    public void testHandleEventWithLeftTextAlignWithCellSelectionWithLeftAlign() {
        final KeyboardEvent event = controlKey("l");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.TEXT_ALIGN,
                            TextAlign.LEFT
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.TEXT_ALIGN,
                Optional.of(TextAlign.LEFT)
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    // middleVerticalAlignment..........................................................................................

    @Test
    public void testHandleEventWithMiddleVerticalAlignWithoutCellSelection() {
        final KeyboardEvent event = shiftControlKey("M");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.spreadsheetSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME
                )
            )
        );
    }

    @Test
    public void testHandleEventWithMiddleVerticalAlignWithCellSelectionWithTop() {
        final KeyboardEvent event = shiftControlKey("M");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.VERTICAL_ALIGN,
                            VerticalAlign.TOP
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.VERTICAL_ALIGN,
                Optional.of(VerticalAlign.MIDDLE)
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    @Test
    public void testHandleEventWithMiddleVerticalAlignWithCellSelectionWithMiddle() {
        final KeyboardEvent event = shiftControlKey("M");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.VERTICAL_ALIGN,
                            VerticalAlign.MIDDLE
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.VERTICAL_ALIGN,
                Optional.of(VerticalAlign.MIDDLE)
            )
        );

        this.defaultPreventedAndCheck(event);
    }
    
    // rightTextAlignment...............................................................................................

    @Test
    public void testHandleEventWithRightTextAlignWithoutCellSelection() {
        final KeyboardEvent event = controlKey("r");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.spreadsheetSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME
                )
            )
        );
    }

    @Test
    public void testHandleEventWithRightTextAlignWithCellSelectionWithLeftAlign() {
        final KeyboardEvent event = controlKey("r");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.TEXT_ALIGN,
                            TextAlign.LEFT
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.TEXT_ALIGN,
                Optional.of(TextAlign.RIGHT)
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    @Test
    public void testHandleEventWithRightTextAlignWithCellSelectionWithRightAlign() {
        final KeyboardEvent event = controlKey("r");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.TEXT_ALIGN,
                            TextAlign.RIGHT
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.TEXT_ALIGN,
                Optional.of(TextAlign.RIGHT)
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    // selectAll........................................................................................................

    @Test
    public void testHandleEventWithSelectAllWithoutSelection() {
        final KeyboardEvent event = controlKey("a");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.spreadsheetSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME
                )
            ),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.ALL_CELLS.setDefaultAnchor()
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    @Test
    public void testHandleEventWithSelectAllWithCellSelection() {
        final KeyboardEvent event = controlKey("a");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                )
            ),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.ALL_CELLS.setDefaultAnchor()
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    // strikethru.......................................................................................................

    @Test
    public void testHandleEventWithStrikethruWithoutCellSelection() {
        final KeyboardEvent event = controlKey("5");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.spreadsheetSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME
                )
            )
        );
    }

    @Test
    public void testHandleEventWithStrikethruWithCellSelectionWithoutTextDecorationLine() {
        final KeyboardEvent event = controlKey("5");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.TEXT_DECORATION_LINE,
                Optional.of(TextDecorationLine.LINE_THROUGH)
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    @Test
    public void testHandleEventWithStrikethruWithCellSelectionWithOverline() {
        final KeyboardEvent event = controlKey("5");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.TEXT_DECORATION_LINE,
                            TextDecorationLine.OVERLINE
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.TEXT_DECORATION_LINE,
                Optional.of(TextDecorationLine.LINE_THROUGH)
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    @Test
    public void testHandleEventWithStrikethruWithCellSelectionWithLineThrough() {
        final KeyboardEvent event = controlKey("5");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.TEXT_DECORATION_LINE,
                            TextDecorationLine.LINE_THROUGH
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.TEXT_DECORATION_LINE,
                Optional.empty()
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    // topTextAlignment.................................................................................................

    @Test
    public void testHandleEventWithTopTextAlignWithoutCellSelection() {
        final KeyboardEvent event = shiftControlKey("T");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.spreadsheetSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME
                )
            )
        );
    }

    @Test
    public void testHandleEventWithTopTextAlignWithCellSelectionWithBottom() {
        final KeyboardEvent event = shiftControlKey("T");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.VERTICAL_ALIGN,
                            VerticalAlign.BOTTOM
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.VERTICAL_ALIGN,
                Optional.of(VerticalAlign.TOP)
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    @Test
    public void testHandleEventWithTopTextAlignWithCellSelectionWithTop() {
        final KeyboardEvent event = shiftControlKey("T");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.VERTICAL_ALIGN,
                            VerticalAlign.TOP
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.VERTICAL_ALIGN,
                Optional.of(VerticalAlign.TOP)
            )
        );

        this.defaultPreventedAndCheck(event);
    }
    
    // underline........................................................................................................

    @Test
    public void testHandleEventWithUnderlineWithoutCellSelection() {
        final KeyboardEvent event = controlKey("u");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.spreadsheetSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME
                )
            )
        );
    }

    @Test
    public void testHandleEventWithUnderlineWithCellSelectionWithoutFontStyle() {
        final KeyboardEvent event = controlKey("u");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.TEXT_DECORATION_LINE,
                Optional.of(
                    TextDecorationLine.UNDERLINE
                )
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    @Test
    public void testHandleEventWithUnderlineWithCellSelectionWithLineThrough() {
        final KeyboardEvent event = controlKey("u");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.TEXT_DECORATION_LINE,
                            TextDecorationLine.LINE_THROUGH
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.TEXT_DECORATION_LINE,
                Optional.of(
                    TextDecorationLine.UNDERLINE
                )
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    @Test
    public void testHandleEventWithUnderlineWithCellSelectionWithUnderline() {
        final KeyboardEvent event = controlKey("u");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.TEXT_DECORATION_LINE,
                            TextDecorationLine.UNDERLINE
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.TEXT_DECORATION_LINE,
                Optional.empty()
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    @Test
    public void testHandleEventWithUnderlineFourDigitWithCellSelectionWithUnderline() {
        final KeyboardEvent event = controlKey("4");

        this.handleEventAndCheck(
            event,
            new TestSpreadsheetKeyboardContext(
                HistoryToken.cellSelect(
                    SPREADSHEET_ID,
                    SPREADSHEET_NAME,
                    CELL.setDefaultAnchor()
                ),
                CELL.setFormula(SpreadsheetFormula.EMPTY)
                    .setStyle(
                        TextStyle.EMPTY.set(
                            TextStylePropertyName.TEXT_DECORATION_LINE,
                            TextDecorationLine.UNDERLINE
                        )
                    )
            ),
            HistoryToken.cellStyleSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL.setDefaultAnchor(),
                TextStylePropertyName.TEXT_DECORATION_LINE,
                Optional.empty()
            )
        );

        this.defaultPreventedAndCheck(event);
    }

    private static KeyboardEvent controlKey(final String key) {
        final KeyboardEvent event = new KeyboardEvent(EventType.keydown.getName());
        event.ctrlKey = true;
        event.key = key;
        return event;
    }

    private static KeyboardEvent shiftControlKey(final String key) {
        final KeyboardEvent event = new KeyboardEvent(EventType.keydown.getName());
        event.ctrlKey = true;
        event.shiftKey = true;
        event.key = key;
        return event;
    }

    // handleEventAndCheck..............................................................................................

    private void handleEventAndCheck(final KeyboardEvent event,
                                     final TestSpreadsheetKeyboardContext context) {
        this.handleEventAndCheck(
            event,
            context,
            context.historyToken()
        );
    }

    private void handleEventAndCheck(final KeyboardEvent event,
                                     final TestSpreadsheetKeyboardContext context,
                                     final HistoryToken expected) {
        SpreadsheetKeyboardEventListener.with(
            SpreadsheetKeyBindings.basic(),
            context
        ).handleEvent(event);

        this.checkEquals(
            expected,
            context.historyToken,
            event::toString
        );
    }

    private void defaultPreventedAndCheck(final KeyboardEvent event) {
        this.checkEquals(
            true,
            event.defaultPrevented,
            event::toString
        );
    }

    private class TestSpreadsheetKeyboardContext extends FakeSpreadsheetKeyboardContext {

        TestSpreadsheetKeyboardContext(final HistoryToken historyToken) {
            this(
                historyToken,
                Optional.empty() // no cell
            );
        }

        TestSpreadsheetKeyboardContext(final HistoryToken historyToken,
                                       final SpreadsheetCell cell) {
            this(
                historyToken,
                Optional.of(cell)
            );
        }

        TestSpreadsheetKeyboardContext(final HistoryToken historyToken,
                                       final Optional<SpreadsheetCell> cell) {
            this.historyToken = historyToken;
            this.cell = cell;

            if (cell.isPresent()) {
                final SpreadsheetSelection historyTokenCell = historyToken.selection()
                    .get();

                checkEquals(
                    historyTokenCell.toCell(),
                    cell.get()
                        .reference(),
                    () -> "historyToken has incompatible selection " + historyToken
                );
            }
        }

        @Override
        public HistoryToken historyToken() {
            return this.historyToken;
        }

        @Override
        public void pushHistoryToken(final HistoryToken historyToken) {
            Objects.requireNonNull(historyToken, "historyToken");
            this.historyToken = historyToken;
        }

        private HistoryToken historyToken;

        @Override
        public Optional<SpreadsheetCell> historyTokenCell() {
            return this.cell;
        }

        private final Optional<SpreadsheetCell> cell;

        // LoggingContext...............................................................................................

        @Override
        public void debug(final Object... values) {
            System.out.println("DEBUG " + Arrays.toString(values));
        }

    }
}
