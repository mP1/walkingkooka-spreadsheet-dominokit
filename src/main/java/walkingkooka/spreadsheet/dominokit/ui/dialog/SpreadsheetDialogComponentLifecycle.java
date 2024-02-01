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

package walkingkooka.spreadsheet.dominokit.ui.dialog;

import org.dominokit.domino.ui.dialogs.Dialog;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.CloseableHistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.ui.ComponentLifecycle;

/**
 * A specialized {@link ComponentLifecycle} that adds some basic support for {@link SpreadsheetDialogComponent}.
 */
public interface SpreadsheetDialogComponentLifecycle extends ComponentLifecycle {

    /**
     * Getter that returns the {@link SpreadsheetDialogComponent}. This is required by the other default methods.
     */
    SpreadsheetDialogComponent dialog();

    /**
     * Returns a {@link CloseableHistoryTokenContext}, this will be used by any close button.
     */
    CloseableHistoryTokenContext closeableHistoryTokenContext();


    /**
     * Base id that should be used for all components within this dialog.
     */
    String idPrefix();

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
        this.refresh(context);
        this.openGiveFocus(context);
    }

    void openGiveFocus(final AppContext context);

    /**
     * Closes or hides the {@link Dialog}.
     */
    @Override
    default void close(final AppContext context) {
        this.dialog().close();
    }
}
