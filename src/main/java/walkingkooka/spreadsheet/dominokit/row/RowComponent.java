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
import org.dominokit.domino.ui.grid.Column;
import org.dominokit.domino.ui.grid.Row;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A {@link walkingkooka.spreadsheet.dominokit.Component} that uses Columns to hold {@link HtmlComponent} as they
 * are added.
 */
public final class RowComponent implements HtmlComponent<HTMLDivElement, RowComponent> {

    public static RowComponent columnSpan3() {
        return with(Column::span3);
    }

    public static RowComponent columnSpan4() {
        return with(Column::span4);
    }

    private static RowComponent with(final Supplier<Column> column) {
        return new RowComponent(column);
    }

    private RowComponent(final Supplier<Column> column) {
        this.row = Row.create();
        this.children = Lists.array();

        this.column = column;
    }

    // appendChild......................................................................................................

    public RowComponent appendChild(final HtmlComponent<?, ?> child) {
        Objects.requireNonNull(child, "child");

        this.row.appendChild(
            this.column.get()
                .appendChild(child)
        );
        this.children.add(child);
        return this;
    }

    private final Supplier<Column> column;

    private final List<HtmlComponent<?, ?>> children;

    // width............................................................................................................

    @Override
    public int width() {
        return this.element()
            .offsetWidth;
    }

    // height...........................................................................................................

    @Override
    public int height() {
        return this.element()
            .offsetHeight;
    }

    // css..............................................................................................................

    @Override
    public RowComponent setCssText(final String css) {
        this.row.cssText(css);
        return this;
    }

    @Override
    public RowComponent setCssProperty(final String name,
                                       final String value) {
        this.row.setCssProperty(
            name,
            value
        );
        return this;
    }

    @Override
    public RowComponent removeCssProperty(final String name) {
        this.row.removeCssProperty(name);
        return this;
    }

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return this.row.isExpanded(); // should really ask components within the row
    }

    // IsElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.row.element();
    }

    private final Row row;

    // TreePrintable....................................................................................................

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
