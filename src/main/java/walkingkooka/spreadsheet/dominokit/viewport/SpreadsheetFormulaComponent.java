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

package walkingkooka.spreadsheet.dominokit.viewport;

import elemental2.dom.Event;
import elemental2.dom.HTMLFieldSetElement;
import elemental2.dom.KeyboardEvent;
import jsinterop.base.Js;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.icons.lib.Icons;
import org.dominokit.domino.ui.utils.PostfixAddOn;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.dom.Key;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellFormulaSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * Provides a text box which supports editing of a formula belonging to a cell.
 */
public final class SpreadsheetFormulaComponent implements IsElement<HTMLFieldSetElement>,
        ComponentLifecycle {

    public static SpreadsheetFormulaComponent with(final AppContext context) {
        return new SpreadsheetFormulaComponent(
                Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetFormulaComponent(final AppContext context) {
        final TextBox textBox = TextBox.create()
                .addEventListener(
                        EventType.keydown.getName(),
                        (event) -> onKeyDownEvent(
                                Js.cast(event)
                        )
                );

        textBox.element()
                .style.set("margin-bottom", "0"); //

        textBox.getInputElement()
                .addEventListener(
                        EventType.focus.getName(),
                        this::onFocus
                );
        textBox.apply(
                self ->
                        self.appendChild(
                                PostfixAddOn.of(
                                        Icons.close_circle()
                                                .clickable()
                                                .addClickListener(this::onClear)
                                )
                        )
        );

        this.textBox = textBox;
        this.context = context;

        context.addHistoryTokenWatcher(this);
    }

    private void onKeyDownEvent(final KeyboardEvent event) {
        final AppContext context = this.context;

        switch (Key.fromEvent(event)) {
            case Enter:
                context.debug("SpreadsheetFormulaComponent.onKeyDownEvent ENTER");

                // if cell then edit formula
                context.pushHistoryToken(
                        context.historyToken()
                                .setFormula()
                                .setSave(this.textBox.getValue())
                );
                break;
            case Escape:
                context.debug("SpreadsheetFormulaComponent.onKeyDownEvent ESCAPE restoring text");
                this.onUndo();
                break;
            default:
                // ignore other keys
                break;
        }
    }

    /**
     * Reloads the textbox with the last saved (loaded) value.
     */
    private void onUndo() {
        this.textBox.setValue(this.undoText);
    }

    private String undoText;

    private void onFocus(final Event event) {
        final AppContext context = this.context;
        final HistoryToken historyToken = context.historyToken();

        context.debug("SpreadsheetFormulaComponent.onFocus " + historyToken.viewportSelectionOrEmpty());

        context.pushHistoryToken(
                historyToken.setFormula()
        );
    }

    /**
     * Clears the textbox when the big CROSS to the right of the formula is clicked.
     */
    private void onClear(final Event event) {
        this.context.debug("SpreadsheetFormulaComponent.onClear");
        this.textBox.clear();
    }

    private final TextBox textBox;

    private final AppContext context;

    // IsElement.......................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.textBox.element();
    }

    // ComponentLifecycle..............................................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return false;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellHistoryToken;
    }

    @Override
    public boolean isOpen() {
        return false == this.textBox.isDisabled();
    }

    @Override
    public void open(final AppContext context) {
        this.textBox.setDisabled(false);

        final SpreadsheetSelection selection = context.historyToken()
                .viewportSelectionOrEmpty()
                .get()
                .selection();

        this.reload(
                selection,
                context
        );
    }

    @Override
    public void refresh(final AppContext context) {
        final HistoryToken token = context.historyToken();

        if(token instanceof SpreadsheetCellHistoryToken) {
            final SpreadsheetCellHistoryToken cellHistoryToken = (SpreadsheetCellHistoryToken) token;
            final SpreadsheetSelection selection = cellHistoryToken.viewportSelection().selection();
            if(false == selection.equals(this.selection)) {
                this.reload(
                        selection,
                        context
                );
                this.selection = selection;
            }

            if(token instanceof SpreadsheetCellFormulaSelectHistoryToken) {
                context.debug("SpreadsheetFormulaComponent.refresh giving focus");
                this.textBox.focus();
            }
        }
    }

    private void reload(final SpreadsheetSelection selection,
                        final AppContext context) {
        String text = "";

        final SpreadsheetViewportCache cache = context.viewportCache();
        final Optional<SpreadsheetSelection> maybeNonLabel = cache.nonLabelSelection(selection);
        if (maybeNonLabel.isPresent()) {
            final SpreadsheetSelection nonLabel = maybeNonLabel.get();
            final Optional<SpreadsheetCell> maybeCell = cache.cell(nonLabel.toCell());

            if (maybeCell.isPresent()) {
                text = maybeCell.get()
                        .formula()
                        .text();
            }

            // TODO show error somewhere in formula ?
        }

        context.debug("SpreadsheetFormulaComponent.reload " + selection + " with " + CharSequences.quoteAndEscape(text));
        this.textBox.setValue(text);
        this.undoText = text;
    }

    @Override
    public void close(final AppContext context) {
        this.textBox.setDisabled(true);
        this.textBox.clear(); // lost focus etc clear the textbox
        this.selection = null;
    }

    private SpreadsheetSelection selection;
}
