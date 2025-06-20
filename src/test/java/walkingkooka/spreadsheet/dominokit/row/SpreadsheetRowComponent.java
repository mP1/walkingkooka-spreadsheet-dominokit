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

package walkingkooka.spreadsheet.dominokit.row;

import elemental2.dom.HTMLDivElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;

public class SpreadsheetRowComponent implements TestHtmlElementComponent<HTMLDivElement, SpreadsheetRowComponent> {

    public static SpreadsheetRowComponent columnSpan3() {
        return new SpreadsheetRowComponent();
    }

    public static SpreadsheetRowComponent columnSpan4() {
        return new SpreadsheetRowComponent();
    }

    private SpreadsheetRowComponent() {
        this.children = Lists.array();
    }

    public SpreadsheetRowComponent appendChild(final HtmlElementComponent<?, ?> child) {
        Objects.requireNonNull(child, "child");

        this.children.add(child);
        return this;
    }

    private final List<HtmlElementComponent<?, ?>> children;

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
