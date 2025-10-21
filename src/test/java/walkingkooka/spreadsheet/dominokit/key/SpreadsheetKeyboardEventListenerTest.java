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
import walkingkooka.tree.text.FontWeight;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public final class SpreadsheetKeyboardEventListenerTest implements TreePrintableTesting {

    private final static SpreadsheetId SPREADSHEET_ID = SpreadsheetId.with(1);

    private final static SpreadsheetName SPREADSHEET_NAME = SpreadsheetName.with("SpreadsheetName1");

    private final static SpreadsheetCellReference CELL = SpreadsheetSelection.A1;

    private final static AnchoredSpreadsheetSelection CELL_RANGE = SpreadsheetSelection.parseCellRange("A1:B2")
        .setAnchor(SpreadsheetViewportAnchor.TOP_LEFT);

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

    private static KeyboardEvent controlKey(final String key) {
        final KeyboardEvent event = new KeyboardEvent(EventType.keydown.getName());
        event.ctrlKey = true;
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
        SpreadsheetKeyboardEventListener.with(context)
            .handleEvent(event);

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

            if(cell.isPresent()) {
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
