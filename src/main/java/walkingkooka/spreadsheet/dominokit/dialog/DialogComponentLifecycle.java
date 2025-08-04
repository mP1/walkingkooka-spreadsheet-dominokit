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

package walkingkooka.spreadsheet.dominokit.dialog;

import org.dominokit.domino.ui.dialogs.Dialog;
import walkingkooka.spreadsheet.dominokit.HistoryTokenAwareComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.anchor.HistoryTokenSaveValueAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.text.CaseKind;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

/**
 * A specialized {@link HistoryTokenAwareComponentLifecycle} that adds some basic support for {@link DialogComponent}.
 */
public interface DialogComponentLifecycle extends HistoryTokenAwareComponentLifecycle,
    TreePrintable {

    /**
     * Getter that returns the {@link DialogComponent}. This is required by the other default methods.
     */
    DialogComponent dialog();

    /**
     * Base id that should be used for all components within this dialog.
     */
    String idPrefix();

    /**
     * Creates a {@link HistoryTokenAnchorComponent} with the given text and also generates an ID.
     */
    default HistoryTokenAnchorComponent anchor(final String text) {
        return HistoryTokenAnchorComponent.empty()
            .setId(
                this.idPrefix() +
                    CaseKind.TITLE.change(
                        text,
                        CaseKind.KEBAB
                    ) + SpreadsheetElementIds.LINK)
            .setTextContent(text);
    }


    /**
     * Creates a {@link HistoryTokenSaveValueAnchorComponent} with a label of "Clear".
     */
    default <TT> HistoryTokenSaveValueAnchorComponent<TT> clearValueAnchor(final HistoryContext historyContext) {
        return HistoryTokenSaveValueAnchorComponent.<TT>with(
            this.idPrefix() +
                "clear" +
                SpreadsheetElementIds.LINK,
            historyContext
        ).setTextContent("Clear");
    }

    /**
     * Factory that creates an Anchor which will close, it will need to be updated with a closed {@link HistoryToken}.
     */
    default HistoryTokenAnchorComponent closeAnchor() {
        return this.anchor("Close");
    }

    /**
     * Creates a {@link HistoryTokenSaveValueAnchorComponent}.
     */
    default <TT> HistoryTokenSaveValueAnchorComponent<TT> saveValueAnchor(final HistoryContext historyContext) {
        return HistoryTokenSaveValueAnchorComponent.<TT>with(
            this.idPrefix() +
                "save" +
                SpreadsheetElementIds.LINK,
            historyContext
        ).setTextContent("Save");
    }

    /**
     * Creates a {@link HistoryTokenSaveValueAnchorComponent}.
     */
    default <TT> HistoryTokenSaveValueAnchorComponent<TT> undoAnchor(final HistoryContext historyContext) {
        return HistoryTokenSaveValueAnchorComponent.<TT>with(
            this.idPrefix() +
                "undo" +
                SpreadsheetElementIds.LINK,
            historyContext
        ).setTextContent("Undo");
    }

    // HistoryTokenAwareComponentLifecycle..............................................................................................

    /**
     * Returns true if the dialog is open.
     */
    @Override
    default boolean isOpen() {
        return this.dialog().isOpen();
    }

    @Override
    default void open(final RefreshContext context) {
        this.dialogReset();
        this.dialog()
            .open();
    }

    /**
     * Closes or hides the {@link Dialog}.
     */
    @Override
    default void close(final RefreshContext context) {
        this.dialog()
            .close();
        this.dialogReset();
    }

    @Override
    default boolean shouldLogLifecycleChanges() {
        return false; // no need to log, dialog will disappear/appear is enough
    }

    /**
     * Callback that is called as the first step when a dialog is opened and closed.
     * This should be used to reset state.
     * <br>
     * After this is returns eventually the {@link #refresh(RefreshContext)} and {@link #openGiveFocus(RefreshContext)}
     * in that order are called.
     */
    void dialogReset();

    // TreePrintable....................................................................................................

    @Override
    default void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.dialog()
                .printTree(printer);
        }
        printer.outdent();
    }
}
