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
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;

public class RowComponent implements TestHtmlElementComponent<HTMLDivElement, RowComponent> {

    public static RowComponent columnSpan3() {
        return new RowComponent();
    }

    public static RowComponent columnSpan4() {
        return new RowComponent();
    }

    private RowComponent() {
        this.children = Lists.array();
    }

    public RowComponent appendChild(final HtmlComponent<?, ?> child) {
        Objects.requireNonNull(child, "child");

        this.children.add(child);
        return this;
    }

    private final List<HtmlComponent<?, ?>> children;

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            for (final HtmlComponent<?, ?> child : this.children) {
                child.printTree(printer);
            }
        }
        printer.outdent();
    }
}
