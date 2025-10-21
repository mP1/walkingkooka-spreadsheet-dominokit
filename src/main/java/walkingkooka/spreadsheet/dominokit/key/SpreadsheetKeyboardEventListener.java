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

import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.KeyboardEvent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHistoryToken;
import walkingkooka.spreadsheet.dominokit.log.Logging;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;


public class SpreadsheetKeyboardEventListener implements EventListener,
    Logging {

    public static SpreadsheetKeyboardEventListener with(final SpreadsheetKeyboardContext context) {
        return new SpreadsheetKeyboardEventListener(
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetKeyboardEventListener(final SpreadsheetKeyboardContext context) {
        this.context = context;
    }

    @Override
    public void handleEvent(final Event event) {
        this.handleKeyEvent(
            (KeyboardEvent) event
        );
    }

    private void handleKeyEvent(final KeyboardEvent event) {
        if (SPREADSHEET_KEYBOARD_EVENT_LISTENER) {
            this.context.debug(this.getClass().getSimpleName() + " handleKeyEvent " +
                (event.ctrlKey ? "control " : "") +
                (event.shiftKey ? "shift " : "") +
                (event.altKey ? "alt " : "") +
                "key: " + CharSequences.quote(event.key)
            );
        }

        if (event.ctrlKey) {
            this.handleControlKeyEvent(event);
        }
    }

    private void handleControlKeyEvent(final KeyboardEvent event) {
        switch (event.key) {
            case "a":
                this.selectAll(event);
                break;
            default:
                break;
        }
    }

    private void selectAll(final KeyboardEvent event) {
        final SpreadsheetKeyboardContext context = this.context;

        final HistoryToken historyToken = context.historyToken()
            .setSelection(
                Optional.of(SpreadsheetSelection.ALL_CELLS)
            ).clearAction();
        if (historyToken instanceof SpreadsheetCellHistoryToken) {
            context.pushHistoryToken(historyToken);
        }

        event.preventDefault();
    }

    private final SpreadsheetKeyboardContext context;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }
}
