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

package walkingkooka.spreadsheet.dominokit.viewport;

import elemental2.dom.HTMLTableRowElement;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.dom.TrComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportWindows;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.function.Predicate;

/**
 * Represents a single TR row within the TABLE which contains all rendered column & row headers and cells.
 * Note it does not add any styling such as colors or borders to show a selected or unselected row.
 */
abstract class SpreadsheetViewportComponentTableRow<T extends SpreadsheetViewportComponentTableRow<T>>
    implements HtmlComponentDelegator<HTMLTableRowElement, T> {

    SpreadsheetViewportComponentTableRow() {
        this.tr = HtmlElementComponent.tr();
    }

    abstract void setIdAndName(final SpreadsheetId id,
                               final SpreadsheetName name);

    abstract void refresh(final SpreadsheetViewportWindows windows,
                          final Predicate<SpreadsheetSelection> selected,
                          final SpreadsheetViewportComponentTableContext context);

    @Override
    public final boolean isEditing() {
        return false;
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public final HtmlElementComponent<HTMLTableRowElement, ?> htmlComponent() {
        return this.tr;
    }

    // IsElement........................................................................................................

    @Override
    public final HTMLTableRowElement element() {
        return this.tr.element();
    }

    final TrComponent tr;

    // TreePrintable....................................................................................................

    @Override
    public final void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.tr.printTree(printer);
        }
        printer.outdent();
    }
}
