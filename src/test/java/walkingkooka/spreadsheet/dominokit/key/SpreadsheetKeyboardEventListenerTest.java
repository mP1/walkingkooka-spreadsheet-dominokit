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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Arrays;
import java.util.Objects;

public final class SpreadsheetKeyboardEventListenerTest implements TreePrintableTesting {

    private final static SpreadsheetId SPREADSHEET_ID = SpreadsheetId.with(1);

    private final static SpreadsheetName SPREADSHEET_NAME = SpreadsheetName.with("SpreadsheetName1");

    private final static SpreadsheetCellReference CELL = SpreadsheetSelection.A1;

    // selectAll........................................................................................................

    @Test
    public void testHandleEventWithSelectAllWithoutSelection() {
        final KeyboardEvent event = new KeyboardEvent(EventType.keydown.getName());
        event.ctrlKey = true;
        event.key = "a";

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
        final KeyboardEvent event = new KeyboardEvent(EventType.keydown.getName());
        event.ctrlKey = true;
        event.key = "a";

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

    private static class TestSpreadsheetKeyboardContext extends FakeSpreadsheetKeyboardContext {

        TestSpreadsheetKeyboardContext(final HistoryToken historyToken) {
            this.historyToken = historyToken;
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
        public void debug(final Object... values) {
            System.out.println("DEBUG " + Arrays.toString(values));
        }

    }
}
