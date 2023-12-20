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

package walkingkooka.spreadsheet.dominokit.ui;

import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.dialogs.Dialog;
import org.dominokit.domino.ui.dialogs.DialogSize;
import org.dominokit.domino.ui.layout.NavBar;
import org.dominokit.domino.ui.utils.PostfixAddOn;

import java.util.Objects;

/**
 * A standard dialog model aialog component with support for title and close icon.
 */
public class SpreadsheetDialogComponent {

    /**
     * Factory that creates a new empty {@link SpreadsheetDialogComponent}.
     */
    public static SpreadsheetDialogComponent create(final Runnable close) {
        Objects.requireNonNull(close, "close");

        return new SpreadsheetDialogComponent(close);
    }

    private SpreadsheetDialogComponent(final Runnable close) {
        final NavBar navBar = this.dialogNavBar(close);
        this.navBar = navBar;

        this.dialog = dialog(navBar);
    }

    private NavBar dialogNavBar(final Runnable close) {
        return NavBar.create() //
                .appendChild(
                        PostfixAddOn.of(
                                SpreadsheetIcons.close()
                                        .clickable()
                                        .addClickListener((event) -> close.run())
                        )
                );
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
                );
    }

    public SpreadsheetDialogComponent id(final String id) {
        this.dialog.id(id);
        return this;
    }

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
        return this.dialog.isOpen();
    }

    /**
     * Opens the modal dialog.
     */
    public void open() {
        this.dialog.open();
    }

    /**
     * Closes the modal dialog.
     */
    public void close() {
        this.dialog.close();
    }

    private final Dialog dialog;

    /**
     * Includes the dialog title.
     */
    private final NavBar navBar;

    @Override
    public String toString() {
        return this.dialog.toString();
    }
}
