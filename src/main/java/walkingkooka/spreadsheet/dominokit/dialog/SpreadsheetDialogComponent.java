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

import elemental2.dom.HTMLDivElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.dialogs.Dialog;
import org.dominokit.domino.ui.dialogs.DialogSize;
import org.dominokit.domino.ui.layout.NavBar;
import org.dominokit.domino.ui.utils.PostfixAddOn;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Objects;
import java.util.Optional;

/**
 * A standard dialog model component with support for title and close icon.
 */
public class SpreadsheetDialogComponent implements SpreadsheetDialogComponentLike {

    /**
     * Factory that creates a new empty {@link SpreadsheetDialogComponent}.
     */
    public static SpreadsheetDialogComponent with(final String id,
                                                  final String title,
                                                  final boolean includeClose,
                                                  final HistoryTokenContext context) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(title, "title");
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
        this.includeClose = includeClose;
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
                .setIconAfter(
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
        // cant use Dialog Close listener to fire current HistoryToken.close() because that causes problems.
        //
        // 1. show spreadsheet list
        // 2. user clicks load spreadsheet with id
        // 3. listener sees load spreadsheet with id
        // 4. dialog closes and fires listener, which fires current history token.close which "changes" history token.
        //
        // 3 & 4 break the history tokens.
        return Dialog.create()
                //.setType(DialogType.DEFAULT) // large
                .setModal(true)
                .setStretchWidth(DialogSize.LARGE)
                .setStretchHeight(DialogSize.LARGE)
                .withHeader(
                        (d, header) ->
                                header.appendChild(navBar)
                );
    }

    private final HistoryTokenContext context;

    @Override
    public String id() {
        return this.dialog.getId();
    }

    /**
     * Replaces the existing title with the new title.
     */
    @Override
    public SpreadsheetDialogComponent setTitle(final String title) {
        this.navBar.setTitle(title);
        return this;
    }

    @Override
    public String title() {
        return this.navBar.getTitle();
    }

    // isTitleIncludesClose.............................................................................................

    @Override
    public boolean isTitleIncludeClose() {
        return this.includeClose;
    }

    private final boolean includeClose;

    // appendChild......................................................................................................

    /**
     * Appends a child to the dialog.
     */
    @Override
    public SpreadsheetDialogComponent appendChild(final IsElement<?> child) {
        this.dialog.appendChild(child);
        return this;
    }

    /**
     * Tests if the dialog is open.
     */
    @Override
    public boolean isOpen() {
        return this.open;
    }

    /**
     * Opens the modal dialog.
     */
    @Override
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
    }

    /**
     * Closes the modal dialog.
     */
    @Override
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

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetDialogComponent setCssText(final String css) {
        this.dialog.cssText(css);
        return this;
    }

    // HtmlElementComponent.............................................................................................

    @Override
    public HTMLDivElement element() {
        return this.dialog.element();
    }

    // node.............................................................................................................

    @Override
    public Node node() {
        return this.element();
    }

    // Object..........................................................................................................

    @Override
    public String toString() {
        return this.dialog.toString();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTreeChildren(final IndentingPrinter printer) {
        printer.println(
                this.element()
                        .toString()
        );
    }
}
