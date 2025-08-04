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
import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import elemental2.dom.KeyboardEvent;
import jsinterop.base.Js;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetError;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.HistoryTokenAwareComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.dom.Key;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.formula.SpreadsheetFormulaComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellFormulaHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellFormulaSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHistoryToken;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Provides a text box which supports editing of a formula belonging to a cell.
 */
public final class SpreadsheetViewportFormulaComponent implements HtmlComponent<HTMLFieldSetElement, SpreadsheetViewportFormulaComponent>,
    HistoryTokenAwareComponentLifecycle,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher,
    SpreadsheetDeltaFetcherWatcher,
    LoadedSpreadsheetMetadataRequired {

    public static SpreadsheetViewportFormulaComponent with(final SpreadsheetViewportFormulaComponentContext context) {
        return new SpreadsheetViewportFormulaComponent(
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetViewportFormulaComponent(final SpreadsheetViewportFormulaComponentContext context) {
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
            ).setDisabled(true);
        this.context = context;

        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetDeltaFetcherWatcher(this);
    }

    private void onFocus(final Event event) {
        final SpreadsheetViewportFormulaComponentContext context = this.context;
        final HistoryToken historyToken = context.historyToken();

        context.debug("SpreadsheetViewportFormulaComponent.onFocus " + historyToken.anchoredSelectionOrEmpty());

        context.pushHistoryToken(
            historyToken.formula()
        );
    }

    private void onKeyDownEvent(final KeyboardEvent event) {
        final SpreadsheetViewportFormulaComponentContext context = this.context;

        switch (Key.fromEvent(event)) {
            case Enter:
                context.debug("SpreadsheetViewportFormulaComponent.onKeyDownEvent ENTER");

                // if cell then edit formula
                context.pushHistoryToken(
                    context.historyToken()
                        .formula()
                        .setSaveStringValue(
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

    public SpreadsheetViewportFormulaComponent addContextMenu(final EventListener listener) {
        this.formula.addContextMenuListener(listener);
        return this;
    }

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetViewportFormulaComponent setCssText(final String css) {
        this.formula.setCssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public SpreadsheetViewportFormulaComponent setCssProperty(final String name,
                                                              final String value) {
        this.formula.setCssProperty(
            name,
            value
        );
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.formula.element();
    }

    // HistoryTokenAwareComponentLifecycle...............................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetCellFormulaSaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellHistoryToken &&
            false == token instanceof SpreadsheetCellFormulaSaveHistoryToken;
    }

    @Override
    public boolean isOpen() {
        return this.open;
    }

    /**
     * Will be true when the current history token is a {@link SpreadsheetCellHistoryToken}.
     */
    private boolean open;

    @Override
    public void open(final RefreshContext context) {
        this.open = true;
        this.previousHistoryToken = null;
    }

    /**
     * Mostly contains logic about whether to refresh the formula text, error messages, enable/disable and whether to give focus.
     */
    @Override
    public void refresh(final RefreshContext context) {
        final SpreadsheetCellHistoryToken token = context.historyToken()
            .cast(SpreadsheetCellHistoryToken.class);
        final SpreadsheetFormulaComponent formula = this.formula;
        final SpreadsheetViewportCache cache = this.context.spreadsheetViewportCache();
        final SpreadsheetSelection notLabelSelection = cache.resolveIfLabelOrFail(
            token.anchoredSelection()
                .selection()
        );
        final boolean isCell = notLabelSelection.isCell();
        formula.setEnabled(isCell);

        if (isCell) {
            final SpreadsheetCellReference selectedCell = notLabelSelection.toCell();

            // if cell selection changed reload formula text
            if (false == notLabelSelection.equalsIgnoreReferenceKind(this.selectedCell)) {
                this.refreshFormula(
                    selectedCell,
                    context
                );
            } else {
                // refresh could have happened before label from selection has returned from server.
                // remove try/catch and if != null when https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/2575 implemented.
                Optional<SpreadsheetCell> cell;
                try {
                    cell = cache.cell(selectedCell);
                } catch (final IllegalArgumentException labelNotReady) {
                    cell = null;
                }

                context.debug("SpreadsheetViewportFormulaComponent.refresh formula cell: " + cell);
                if (null != cell) {
                    this.refreshFormula(
                        selectedCell,
                        context
                    );
                }
            }

            if (token instanceof SpreadsheetCellFormulaHistoryToken & false == this.previousHistoryToken instanceof SpreadsheetCellFormulaHistoryToken) {
                context.debug("SpreadsheetViewportFormulaComponent.refresh giving focus");
                context.giveFocus(formula::focus);
            }

            this.selectedCell = selectedCell;
        } else {
            // not a cell selection clear the formula & helper & error messages.
            formula.clear();

            this.selectedCell = null;
        }

        this.previousHistoryToken = token;
    }

    /**
     * Refreshes the {@link #formula} text and helper / error message.
     */
    private void refreshFormula(final SpreadsheetCellReference cellReference,
                                final RefreshContext context) {
        final SpreadsheetViewportCache cache = this.context.spreadsheetViewportCache();

        final Optional<SpreadsheetCell> cell = cache.cell(cellReference);
        final Optional<String> text = cell.map((c) -> c.formula().text());

        context.debug("SpreadsheetViewportFormulaComponent.refreshFormula " + cellReference + " text=" + text);

        final SpreadsheetFormulaComponent formula = this.formula;
        formula.setStringValue(text);
        formula.setErrors(
            cell.flatMap(
                    c -> c.formula()
                        .error()
                        .map(SpreadsheetError::message)
                ).stream()
                .collect(Collectors.toList())
        );

        this.undoText = text;
    }

    @Override
    public void openGiveFocus(final RefreshContext context) {
        // nop
    }

    @Override
    public void close(final RefreshContext context) {
        this.open = false;
        this.formula.disabled()
            .clearValue()
            .clearHelperText();

        this.selectedCell = null;
        this.previousHistoryToken = null;
    }

    /**
     * Holds the last cell selection. This is used to track changes, supporting reloading of the {@link #formula} content
     */
    private SpreadsheetCellReference selectedCell;

    /**
     * Used to test if focus should be given to the {@link #formula} because a new {@link SpreadsheetCellFormulaHistoryToken} happened.
     */
    private HistoryToken previousHistoryToken;

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return this.formula.isEditing();
    }


    @Override
    public boolean shouldLogLifecycleChanges() {
        return false;
    }

    // SpreadsheetDeltaFetcherWatcher...................................................................................

    @Override
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta,
                                   final AppContext context) {
        this.refreshIfOpen(context);
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.formula.toString();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.formula.printTree(printer);
        }
        printer.outdent();
    }

    private final SpreadsheetFormulaComponent formula;

    private final SpreadsheetViewportFormulaComponentContext context;
}
