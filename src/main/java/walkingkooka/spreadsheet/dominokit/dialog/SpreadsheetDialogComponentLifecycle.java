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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.Optional;

/**
 * A specialized {@link ComponentLifecycle} that adds some basic support for {@link SpreadsheetDialogComponent}.
 */
public interface SpreadsheetDialogComponentLifecycle extends ComponentLifecycle,
        TreePrintable {

    /**
     * Getter that returns the {@link SpreadsheetDialogComponent}. This is required by the other default methods.
     */
    SpreadsheetDialogComponent dialog();

    /**
     * Base id that should be used for all components within this dialog.
     */
    String idPrefix();

    /**
     * Creates a {@link HistoryTokenAnchorComponent} with the given text and also generates an ID.
     */
    default HistoryTokenAnchorComponent anchor(final String text) {
        return HistoryTokenAnchorComponent.empty()
                .setId(this.idPrefix() + text.toLowerCase() + SpreadsheetElementIds.LINK)
                .setTextContent(text);
    }

    /**
     * Factory that creates a Anchor which will close this dialog by pushing a {@link HistoryToken#close()}.
     */
    default HistoryTokenAnchorComponent closeAnchor(final HistoryToken historyToken) {
        return this.anchor("Close")
                .setHistoryToken(
                        Optional.of(
                                historyToken.close()
                        )
                );
    }

    // ComponentLifecycle..............................................................................................

    /**
     * Returns true if the dialog is open.
     */
    @Override
    default boolean isOpen() {
        return this.dialog().isOpen();
    }

    @Override
    default void open(final AppContext context) {
        this.dialog()
                .open();
    }

    /**
     * Closes or hides the {@link Dialog}.
     */
    @Override
    default void close(final AppContext context) {
        this.dialog().close();
    }

    @Override
    default boolean shouldLogLifecycleChanges() {
        return false; // no need to log, dialog will disappear/appear is enough
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
