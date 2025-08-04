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
public final class SpreadsheetRowComponent implements HtmlComponent<HTMLDivElement, SpreadsheetRowComponent> {

    public static SpreadsheetRowComponent columnSpan3() {
        return with(Column::span3);
    }

    public static SpreadsheetRowComponent columnSpan4() {
        return with(Column::span4);
    }

    private static SpreadsheetRowComponent with(final Supplier<Column> column) {
        return new SpreadsheetRowComponent(column);
    }

    private SpreadsheetRowComponent(final Supplier<Column> column) {
        this.row = Row.create();
        this.children = Lists.array();

        this.column = column;
    }

    public SpreadsheetRowComponent appendChild(final HtmlComponent<?, ?> child) {
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

    @Override
    public SpreadsheetRowComponent setCssText(final String css) {
        this.row.cssText(css);
        return this;
    }

    @Override
    public SpreadsheetRowComponent setCssProperty(final String name,
                                                  final String value) {
        this.row.setCssProperty(
            name,
            value
        );
        return this;
    }

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return this.row.isExpanded(); // should really ask components within the row
    }

    @Override
    public HTMLDivElement element() {
        return this.row.element();
    }

    private final Row row;

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
