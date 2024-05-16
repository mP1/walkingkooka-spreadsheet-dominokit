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

package walkingkooka.spreadsheet.dominokit.ui.flexlayout;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.IsElement;
import walkingkooka.spreadsheet.dominokit.ui.ComponentWithChildren;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

/**
 * Defines the public API for {@link SpreadsheetFlexLayout}. Used to keep the main/text versions in sync.
 */
public interface SpreadsheetFlexLayoutLike extends HtmlElementComponent<HTMLDivElement, SpreadsheetFlexLayout>,
        ComponentWithChildren<SpreadsheetFlexLayout, HTMLDivElement>,
        TreePrintable {

    SpreadsheetFlexLayout setId(final String id);

    String id();

    /**
     * Returns true if this flex is COLUMN otherwise returns false for ROW.
     */
    boolean isColumn();

    // TreePrintable....................................................................................................

    @Override
    default void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            final String id = this.id();
            if (false == CharSequences.isNullOrEmpty(id)) {
                printer.println("id=" + id);
                printer.indent();
            }


            for (final IsElement<?> child : this.children()) {
                TreePrintable.printTreeOrToString(
                        child,
                        printer
                );
                printer.lineStart();
            }

            if (false == CharSequences.isNullOrEmpty(id)) {
                printer.outdent();
            }
        }
        printer.outdent();
    }
}
