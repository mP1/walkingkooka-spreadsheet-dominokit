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
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

/**
 * A specialized {@link HistoryTokenAwareComponentLifecycle} that adds some basic support for {@link DialogComponent}.
 */
public interface DialogComponentLifecycle extends HistoryTokenAwareComponentLifecycle,
    DialogComponentAnchors,
    TreePrintable {

    /**
     * Getter that returns the {@link DialogComponent}. This is required by the other default methods.
     */
    DialogComponent dialog();

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

    /**
     * Callback that is called as the first step when a dialog is opened and closed.
     * This should be used to reset state.
     * <br>
     * After this is returns eventually the {@link #refresh(RefreshContext)} and {@link #openGiveFocus(RefreshContext)}
     * in that order are called.
     */
    void dialogReset();

    /**
     * Creates a {@link DialogAnchorListComponent} that will only receive {@link walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher}
     * events if this dialog {@link #isOpen()}.
     */
    default <T> DialogAnchorListComponent<T> dialogAnchorListComponent(final DialogAnchorListComponentContext<T> context) {
        return DialogAnchorListComponent.empty(
            this.idPrefix(),
            DialogAnchorListComponentContexts.isDialogOpen(
                this::isOpen,
                context
            )
        );
    }

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
