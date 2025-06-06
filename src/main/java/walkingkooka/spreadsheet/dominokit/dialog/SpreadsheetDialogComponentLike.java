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
import org.dominokit.domino.ui.IsElement;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.text.printer.IndentingPrinter;

/**
 * Interface that defines all the PUBLIC methods of {@link SpreadsheetDialogComponent}.
 * This is used to keep the two SpreadsheetDialogComponent.java (main/test) in sync.
 */
abstract class SpreadsheetDialogComponentLike implements HtmlElementComponent<HTMLDivElement, SpreadsheetDialogComponent> {

    public final static boolean INCLUDE_CLOSE = true;

    public final static boolean WITHOUT_CLOSE = false;

    SpreadsheetDialogComponentLike() {
        super();
    }

    /**
     * Getter that returns the ID.
     */
    abstract public String id();

    /**
     * Sets a new title on the dialog.
     */
    abstract public SpreadsheetDialogComponent setTitle(final String title);

    /**
     * Getter that returns the current title
     */
    abstract public String title();

    /**
     * Tests if the title includes a CLOSE X.
     */
    abstract public boolean isTitleIncludeClose();

    /**
     * Appends a new child to this {@link SpreadsheetDialogComponent}
     */
    abstract public SpreadsheetDialogComponent appendChild(final IsElement<?> child);

    /**
     * Tests if the dialog is open.
     */
    abstract public boolean isOpen();

    /**
     * Opens the dialog.
     */
    abstract public void open();

    /**
     * Closes or hides the dialog.
     */
    abstract public void close();

    // TreePrintable....................................................................................................

    @Override
    public final void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            final String title = this.title();
            if (false == title.isEmpty()) {
                printer.println(title);
            }

            printer.print("id=" + this.id() + " includeClose=" + this.isTitleIncludeClose());
            printer.println(this.isOpen() ? "" : " CLOSED");

            printer.indent();
            {
                this.printTreeChildren(printer);
            }
            printer.outdent();
        }
        printer.outdent();
    }

    abstract void printTreeChildren(final IndentingPrinter printer);
}
