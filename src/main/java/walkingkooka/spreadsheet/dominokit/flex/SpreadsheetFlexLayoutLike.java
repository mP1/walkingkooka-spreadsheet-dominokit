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

package walkingkooka.spreadsheet.dominokit.flex;

import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.dominokit.ComponentWithChildren;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

/**
 * Defines the public API for {@link SpreadsheetFlexLayout}. Used to keep the main/text versions in sync.
 */
public interface SpreadsheetFlexLayoutLike extends HtmlElementComponent<HTMLDivElement, SpreadsheetFlexLayout>,
    ComponentWithChildren<SpreadsheetFlexLayout, HTMLDivElement> {

    SpreadsheetFlexLayout setId(final String id);

    String id();

    /**
     * Returns true if this flex is COLUMN otherwise returns false for ROW.
     */
    boolean isColumn();

    /**
     * Sets the style property display to block. Normally a {@link SpreadsheetFlexLayout} display=flex with a few additional
     * flex properties.
     */
    SpreadsheetFlexLayout displayBlock();

    // CanBeEmpty.......................................................................................................

    @Override
    default boolean isEmpty() {
        return this.isEmptyIfChildrenAreEmpty();
    }

    // TreePrintable....................................................................................................

    // SpreadsheetFlexLayout
    //  ROW
    //    id=Id123
    //      SpreadsheetTextBox
    //        [Value111]
    //      SpreadsheetTextBox
    //        [Value222]
    @Override
    default void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            printer.println(this.isColumn() ? "COLUMN" : "ROW");
            {
                final String id = this.id();
                if (false == CharSequences.isNullOrEmpty(id)) {
                    printer.indent();
                    printer.println("id=" + id);
                }

                this.printTreeChildren(printer);

                if (false == CharSequences.isNullOrEmpty(id)) {
                    printer.outdent();
                }
            }
        }
        printer.outdent();
    }
}
