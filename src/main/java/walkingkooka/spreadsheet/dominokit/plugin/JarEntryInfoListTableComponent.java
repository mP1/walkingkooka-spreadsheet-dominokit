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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.card.CardComponent;
import walkingkooka.spreadsheet.dominokit.datatable.DataTableComponent;
import walkingkooka.spreadsheet.dominokit.value.TableComponent;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfo;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoList;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextAlign;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A table that lists all the entries from a {@link JarEntryInfoList}.
 * No previous or next paging links are provided, it is assumed the table will scroll.
 */
final class JarEntryInfoListTableComponent implements TableComponent<HTMLDivElement, JarEntryInfoList, JarEntryInfoListTableComponent>,
    HtmlComponentDelegator<HTMLDivElement, JarEntryInfoListTableComponent> {

    /**
     * Creates an empty {@link JarEntryInfoListTableComponent}.
     */
    static JarEntryInfoListTableComponent empty(final String id,
                                                final JarEntryInfoListTableComponentContext context) {
        return new JarEntryInfoListTableComponent(
            Objects.requireNonNull(id, "id"),
            Objects.requireNonNull(context, "context")
        );
    }


    private JarEntryInfoListTableComponent(final String id,
                                           final JarEntryInfoListTableComponentContext context) {
        this.card = CardComponent.empty();

        this.table = DataTableComponent.with(
            id,
            columnConfigs(),
            JarEntryInfoListTableComponentDataTableComponentCellRenderer.with(
                id,
                context
            )
        ).emptyStatePlugin(
            SpreadsheetIcons.spreadsheetListTableEmpty(),
            "empty JAR file"
        );

        this.card.appendChild(
            this.table
        );
    }

    private List<ColumnConfig<JarEntryInfo>> columnConfigs() {
        return Lists.of(
            columnConfig(
                "Name",
                "name",
                TextAlign.LEFT
            ),
            columnConfig(
                "Size(Compressed)",
                "size",
                TextAlign.RIGHT
            ),
            columnConfig(
                "Method",
                "method",
                TextAlign.RIGHT
            ),
            columnConfig(
                "CRC",
                "crc",
                TextAlign.RIGHT
            ),
            columnConfig(
                "Created",
                "create-date-time",
                TextAlign.LEFT
            ),
            columnConfig(
                "Last modified timestamp",
                "last-modified-by",
                TextAlign.LEFT
            ),
            columnConfig(
                "Links",
                "links",
                TextAlign.LEFT
            )
        );
    }

    private static ColumnConfig<JarEntryInfo> columnConfig(final String title,
                                                           final String columnName,
                                                           final TextAlign textAlign) {
        return ColumnConfig.<JarEntryInfo>create(columnName)
            .setTitle(title)
            .setFixed(true)
            .setTextAlign(
                CellTextAlign.valueOf(
                    textAlign.name()
                )
            );
    }

    @Override
    public Optional<JarEntryInfoList> value() {
        return this.table.value()
            .map(JarEntryInfoList.EMPTY::setElements);
    }

    @Override
    public JarEntryInfoListTableComponent setValue(final Optional<JarEntryInfoList> list) {
        this.table.setValue(
            Cast.to(list)
        );
        return this;
    }

    @Override
    public JarEntryInfoListTableComponent focus() {
        this.table.focus();
        return this;
    }

    @Override
    public JarEntryInfoListTableComponent blur() {
        this.table.blur();
        return this;
    }

    private final DataTableComponent<JarEntryInfo> table;

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLDivElement, ?> htmlComponent() {
        return this.card;
    }

    private final CardComponent card;

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
