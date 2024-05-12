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
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.text.printer.TreePrintable;

/**
 * Interface that defines all the PUBLIC methods of {@link SpreadsheetDialogComponent}.
 * This is used to keep the two SpreadsheetDialogComponent.java (main/test) in sync.
 */
public interface SpreadsheetDialogComponentLike extends HtmlElementComponent<HTMLDivElement, SpreadsheetDialogComponent>,
        TreePrintable {

    /**
     * Getter that returns the ID.
     */
    String id();

    /**
     * Sets a new title on the dialog.
     */
    SpreadsheetDialogComponent setTitle(final String title);


    /**
     * Getter that returns the current title
     */
    String title();

    /**
     * Appends a new child to this {@link SpreadsheetDialogComponent}
     */
    SpreadsheetDialogComponent appendChild(final IsElement<?> child);

    /**
     * Tests if the dialog is open.
     */
    boolean isOpen();

    /**
     * Opens the dialog.
     */
    void open();

    /**
     * Closes or hides the dialog.
     */
    void close();
}
