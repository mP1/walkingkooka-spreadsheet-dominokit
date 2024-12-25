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

package walkingkooka.spreadsheet.dominokit.plugin;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.datatable.CellTextAlign;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import walkingkooka.collect.list.Lists;
import walkingkooka.plugin.store.Plugin;
import walkingkooka.plugin.store.PluginSet;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.card.SpreadsheetCard;
import walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponent;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextAlign;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A table that lists all the entries from a {@link PluginSet}.
 */
final class PluginSetTableComponent implements HtmlElementComponent<HTMLDivElement, PluginSetTableComponent> {

    /**
     * Creates an empty {@link PluginSetTableComponent}.
     */
    static PluginSetTableComponent empty(final String id,
                                         final PluginSetTableComponentContext context) {
        return new PluginSetTableComponent(
                Objects.requireNonNull(id, "id"),
                Objects.requireNonNull(context, "context")
        );
    }

    private PluginSetTableComponent(final String id,
                                    final PluginSetTableComponentContext context) {
        this.card = SpreadsheetCard.empty();

        this.table = SpreadsheetDataTableComponent.with(
                id,
                columnConfigs(),
                PluginSetTableComponentSpreadsheetDataTableComponentCellRenderer.with(
                        id,
                        context
                )
        ).emptyStatePlugin(
                SpreadsheetIcons.spreadsheetListTableEmpty(),
                "No plugins available"
        );

        this.card.appendChild(
                this.table.previousNextLinks(id)
        );
    }

    private List<ColumnConfig<Plugin>> columnConfigs() {
        return Lists.of(
                columnConfig(
                        "Name",
                        "name",
                        TextAlign.LEFT
                ),
                columnConfig(
                        "Filename",
                        "filename",
                        TextAlign.LEFT
                ),
                columnConfig(
                        "User",
                        "user",
                        TextAlign.LEFT
                ),
                columnConfig(
                        "Timestamp",
                        "timestamp",
                        TextAlign.LEFT
                ),
                columnConfig(
                        "Links",
                        "links",
                        TextAlign.LEFT
                )
        );
    }

    private static ColumnConfig<Plugin> columnConfig(final String title,
                                                     final String columnName,
                                                     final TextAlign textAlign) {
        return ColumnConfig.<Plugin>create(columnName)
                .setTitle(title)
                .setFixed(true)
                .setTextAlign(
                        CellTextAlign.valueOf(
                                textAlign.name()
                        )
                );
    }

    private final SpreadsheetDataTableComponent<Plugin> table;

    PluginSetTableComponent setSet(final PluginSet set) {
        this.table.setValue(
                Optional.of(
                        new ArrayList<>(set)
                )
        );
        return this;
    }

    // setCssText.......................................................................................................

    @Override
    public PluginSetTableComponent setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.card.setCssText(css);
        return this;
    }

    // HtmlElementComponent.............................................................................................

    @Override
    public HTMLDivElement element() {
        return this.card.element();
    }

    private final SpreadsheetCard card;

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.card.printTree(printer);
        }
        printer.outdent();
    }
}
