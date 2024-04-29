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

package walkingkooka.spreadsheet.dominokit.ui.viewport;

import elemental2.dom.Event;
import elemental2.dom.HTMLFieldSetElement;
import elemental2.dom.KeyboardEvent;
import jsinterop.base.Js;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetError;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dom.Key;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellFormulaHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellFormulaSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.NopNoResponseWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.ui.ComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.formula.SpreadsheetFormulaComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;

/**
 * Provides a text box which supports editing of a formula belonging to a cell.
 */
public final class SpreadsheetViewportFormulaComponent implements HtmlElementComponent<HTMLFieldSetElement, SpreadsheetViewportFormulaComponent>,
        ComponentLifecycle,
        NopFetcherWatcher,
        NopNoResponseWatcher,
        SpreadsheetDeltaFetcherWatcher,
        LoadedSpreadsheetMetadataRequired {

    public static SpreadsheetViewportFormulaComponent with(final AppContext context) {
        return new SpreadsheetViewportFormulaComponent(
                Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetViewportFormulaComponent(final AppContext context) {
        this.formula = SpreadsheetFormulaComponent.empty(
                        SpreadsheetViewportFormulaComponentSpreadsheetFormulaComponentParserFunction.with(context)
                ).alwaysShowHelperText()
                .hideMarginBottom()
                .removeBorders()
                .addFocusListener(this::onFocus)
                .addKeydownListener(
                        (event) -> onKeyDownEvent(
                                Js.cast(event)
                        )
                );
        this.context = context;

        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetDeltaWatcher(this);
    }

    private void onFocus(final Event event) {
        final AppContext context = this.context;
        final HistoryToken historyToken = context.historyToken();

        context.debug("SpreadsheetViewportFormulaComponent.onFocus " + historyToken.anchoredSelectionOrEmpty());

        context.pushHistoryToken(
                historyToken.setFormula()
        );
    }

    private void onKeyDownEvent(final KeyboardEvent event) {
        final AppContext context = this.context;

        switch (Key.fromEvent(event)) {
            case Enter:
                context.debug("SpreadsheetViewportFormulaComponent.onKeyDownEvent ENTER");

                // if cell then edit formula
                context.pushHistoryToken(
                        context.historyToken()
                                .setFormula()
                                .setSave(
                                        this.formula.stringValue()
                                                .orElse("")
                                )
                );
                break;
            case Escape:
                context.debug("SpreadsheetViewportFormulaComponent.onKeyDownEvent ESCAPE restoring text");
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
        this.formula.setStringValue(this.undoText);
    }

    private Optional<String> undoText;

    // IsElement.......................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.formula.element();
    }

    // ComponentLifecycle..............................................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetCellFormulaSaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellHistoryToken;
    }

    @Override
    public boolean isOpen() {
        return false == this.formula.isDisabled();
    }

    @Override
    public void open(final AppContext context) {
        this.formula.setDisabled(false);

        this.selection = context.historyToken()
                .anchoredSelectionOrEmpty()
                .get()
                .selection();
    }

    @Override
    public void refresh(final AppContext context) {
        final HistoryToken token = context.historyToken();

        final SpreadsheetFormulaComponent formula = this.formula;
        if (token instanceof SpreadsheetCellHistoryToken) {
            final SpreadsheetSelection selection = token.cast(SpreadsheetCellHistoryToken.class)
                    .anchoredSelection()
                    .selection();

            // if selection change reload formula text
            if (false == selection.equalsIgnoreReferenceKind(this.selection)) {
                this.reload(
                        selection,
                        context
                );
            } else {
                final SpreadsheetViewportCache cache = context.spreadsheetViewportCache();

                // refresh could have happened before label from selection has returned from server.
                // remove try/catch and if != null when https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/2575 implemented.
                Optional<SpreadsheetCell> cell;
                try {
                    cell = cache.cell(selection);
                } catch (final IllegalArgumentException labelNotReady) {
                    cell = null;
                }

                if (null != cell) {
                    formula.setStringValue(
                            cell.map(c -> c.formula().text())
                    ).setHelperText(
                            cell.flatMap(
                                    c -> c.formula()
                                            .error()
                                            .map(SpreadsheetError::message)
                            )
                    );

                    if (token instanceof SpreadsheetCellFormulaHistoryToken) {
                        context.debug("SpreadsheetViewportFormulaComponent.refresh giving focus");

                        context.giveFocus(formula::focus);
                    }
                }
            }
        } else {
            context.debug("SpreadsheetViewportFormulaComponent.refresh not cell historyToken clearing text");
            formula.clearValue();
            formula.clearHelperText();
        }
    }

    private void reload(final SpreadsheetSelection selection,
                        final AppContext context) {
        final SpreadsheetViewportCache cache = context.spreadsheetViewportCache();

        final SpreadsheetSelection nonLabel = cache.resolveIfLabel(selection);
        final Optional<SpreadsheetCell> cell = cache.cell(nonLabel.toCell());
        final Optional<String> text = cell.map((c) -> c.formula().text());

        context.debug("SpreadsheetViewportFormulaComponent.reload text=" + text);

        this.formula.setStringValue(text);
        this.formula.validate();
        this.undoText = text;
    }

    @Override
    public void openGiveFocus(final AppContext context) {
        // nop MAYBE should give focus here
    }

    @Override
    public void close(final AppContext context) {
        this.formula.setDisabled(true)
                .clearValue()
                .clearHelperText();

        this.selection = null;
    }

    private SpreadsheetSelection selection;

    @Override
    public boolean shouldLogLifecycleChanges() {
        return false;
    }

    // SpreadsheetDeltaWatcher..........................................................................................

    @Override
    public void onSpreadsheetDelta(final SpreadsheetId id,
                                   final SpreadsheetDelta delta,
                                   final AppContext context) {
        this.refresh(context);
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.formula.toString();
    }

    private final SpreadsheetFormulaComponent formula;

    private final AppContext context;
}
