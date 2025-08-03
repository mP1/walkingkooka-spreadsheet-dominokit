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

package walkingkooka.spreadsheet.dominokit.dom;

import elemental2.dom.HTMLDivElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;

abstract class SpreadsheetDivComponentLike implements HtmlElementComponent<HTMLDivElement, SpreadsheetDivComponent> {

    SpreadsheetDivComponentLike() {
        super();
        this.children = Lists.array();
    }

    abstract SpreadsheetDivComponent appendChild(final HtmlElementComponent<?, ?> child);

    final List<HtmlElementComponent<?, ?>> children;

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return false;
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            for (final HtmlElementComponent<?, ?> child : this.children) {
                child.printTree(printer);
            }
        }
        printer.outdent();
    }
}
