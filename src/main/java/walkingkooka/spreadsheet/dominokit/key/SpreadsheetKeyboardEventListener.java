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
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHistoryToken;
import walkingkooka.spreadsheet.dominokit.log.Logging;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.FontWeight;
import walkingkooka.tree.text.TextDecorationLine;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;


/**
 * Maps well known Excel keyboard shortcuts
 * <pre>https://support.microsoft.com/en-au/office/keyboard-shortcuts-in-excel-1798d9d5-842a-42b8-9c99-9b7213f0040f</pre>
 */
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
            case "b":
            case "2":
                this.bold(event);
                break;
            case "i":
            case "3":
                this.italics(event);
                break;
            case "u":
            case "4":
                this.underline(event);
                break;
            default:
                break;
        }
    }

    private void bold(final KeyboardEvent event) {
        this.flipCellStyle(
            TextStylePropertyName.FONT_WEIGHT,
            FontWeight.BOLD
        );
        event.preventDefault();
    }

    private void italics(final KeyboardEvent event) {
        this.flipCellStyle(
            TextStylePropertyName.FONT_STYLE,
            FontStyle.ITALIC
        );
        event.preventDefault();
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

    private void underline(final KeyboardEvent event) {
        this.flipCellStyle(
            TextStylePropertyName.TEXT_DECORATION_LINE,
            TextDecorationLine.UNDERLINE
        );
        event.preventDefault();
    }

    /**
     * Helper that may be used to FLIP a {@link SpreadsheetCell} {@link TextStyle}, such as BOLD.
     */
    private <T> void flipCellStyle(final TextStylePropertyName<T> name,
                                   final T value) {
        final SpreadsheetKeyboardContext context = this.context;

        final SpreadsheetCell cell = context.historyTokenCell()
            .orElse(null);

        if (null != cell) {
            final TextStyle style = cell.textStyle();
            final T previous = style.get(name)
                .orElse(null);

            context.pushHistoryToken(
                context.historyToken()
                    .setStylePropertyName(
                        name
                    ).setSaveValue(
                        Optional.ofNullable(
                            Objects.equals(
                                previous,
                                value
                            ) ?
                                null :
                                value
                        )
                    )
            );
        }
    }

    private final SpreadsheetKeyboardContext context;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }
}
