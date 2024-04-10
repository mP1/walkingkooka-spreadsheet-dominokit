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

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.dialogs.Dialog;
import org.dominokit.domino.ui.dialogs.DialogSize;
import org.dominokit.domino.ui.layout.NavBar;
import org.dominokit.domino.ui.utils.PostfixAddOn;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * A standard dialog model aialog component with support for title and close icon.
 */
public class SpreadsheetDialogComponent implements HtmlElementComponent<HTMLDivElement, SpreadsheetDialogComponent> {

    /**
     * Factory that creates a new empty {@link SpreadsheetDialogComponent}.
     */
    public static SpreadsheetDialogComponent with(final String id,
                                                  final String title,
                                                  final boolean includeClose,
                                                  final HistoryTokenContext context) {
        Objects.requireNonNull(id, "id");
        CharSequences.failIfNullOrEmpty(title, "title");
        Objects.requireNonNull(context, "context");

        return new SpreadsheetDialogComponent(
                id,
                title,
                includeClose,
                context
        );
    }

    private SpreadsheetDialogComponent(final String id,
                                       final String title,
                                       final boolean includeClose,
                                       final HistoryTokenContext context) {
        this.context = context;

        final NavBar navBar = dialogNavBar();
        final HistoryTokenAnchorComponent close;

        if (includeClose) {
            close = this.closeLink(
                    id,
                    context
            );
            navBar.appendChild(
                    PostfixAddOn.of(
                            close
                    )
            );
        } else {
            close = null;
        }
        this.close = close;

        this.navBar = navBar;

        this.dialog = dialog(navBar)
                .id(id);

        this.setTitle(title);
    }

    private HistoryTokenAnchorComponent closeLink(final String id,
                                                  final HistoryTokenContext context) {
        return context.historyToken()
                .link(id + "-close-X")
                .setIcon(
                        Optional.of(
                                SpreadsheetIcons.close()
                        )
                );
    }

    private final HistoryTokenAnchorComponent close;

    private static NavBar dialogNavBar() {
        return NavBar.create();
    }

    /**
     * Prepares a new {@link Dialog} with a navbar which will hold the title and a close icon.
     */
    private Dialog dialog(final NavBar navBar) {
        return Dialog.create()
                //.setType(DialogType.DEFAULT) // large
                .setAutoClose(true)
                .setModal(true)
                .setStretchWidth(DialogSize.LARGE)
                .setStretchHeight(DialogSize.LARGE)
                .withHeader(
                        (d, header) ->
                                header.appendChild(navBar)
                ).addCloseListener(this::onClose);
    }

    private void onClose(final Dialog dialog) {
        this.fireClose();
    }

    private void fireClose() {
        this.context.historyToken()
                .close();
    }

    private final HistoryTokenContext context;

    /**
     * Replaces the existing title with the new title.
     */
    public void setTitle(final String title) {
        this.navBar.setTitle(title);
    }

    /**
     * Appends a child to the dialog.
     */
    public void appendChild(final IsElement<?> is) {
        this.dialog.appendChild(is);
    }

    /**
     * Tests if the dialog is open.
     */
    public boolean isOpen() {
        return this.open;
    }

    /**
     * Opens the modal dialog.
     */
    public void open() {
        this.open = true;
        this.dialog.open();

        final HistoryTokenAnchorComponent close = this.close;
        if (null != close) {
            close.setHistoryToken(
                    Optional.of(
                            this.context.historyToken()
                                    .close()
                    )
            );
        }
        ;
    }

    /**
     * Closes the modal dialog.
     */
    public void close() {
        this.open = false;
        this.dialog.close();
    }

    /**
     * A flag is used to test if a {@link Dialog}, because {@link Dialog#close} reports true until closing animations finish.
     */
    private boolean open;

    private final Dialog dialog;

    /**
     * Includes the dialog title.
     */
    private final NavBar navBar;

    // HtmlElementComponent.............................................................................................

    @Override
    public HTMLDivElement element() {
        return this.dialog.element();
    }

    // Object..........................................................................................................

    @Override
    public String toString() {
        return this.dialog.toString();
    }
}
