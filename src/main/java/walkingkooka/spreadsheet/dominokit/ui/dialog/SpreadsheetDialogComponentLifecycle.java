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

import elemental2.dom.Event;
import elemental2.dom.EventListener;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.dialogs.Dialog;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.style.Elevation;
import org.dominokit.domino.ui.style.StyleType;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.CloseableHistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.ComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;

import java.util.Optional;

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

    /**
     * When clicked the CLOSE button invokes {@link #close}.
     */
    default Button closeButton() {
        return this.button(
                "Close",
                StyleType.DANGER,
                this::onCloseButtonClick
        );
    }

    private void onCloseButtonClick(final Event event) {
        this.closeableHistoryTokenContext()
                .close();
    }

    /**
     * Creates one of the modal action buttons that appear at the bottom of the modal dialog.
     */
    default Button button(final String text,
                          final StyleType type,
                          final EventListener listener) {
        final Button button = new Button(text);

        button.id(this.idPrefix() + text.toLowerCase() + SpreadsheetIds.BUTTON);
        button.addCss("dui-" + type.getStyle());
        button.elevate(Elevation.LEVEL_1);

        button.addEventListener(
                EventType.click,
                listener
        );

        return button;
    }

    /**
     * Creates a {@link HistoryTokenAnchorComponent} with the given text and also generates an ID.
     */
    default HistoryTokenAnchorComponent anchor(final String text) {
        return HistoryTokenAnchorComponent.empty()
                .setId(this.idPrefix() + text.toLowerCase() + SpreadsheetIds.LINK)
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
}
