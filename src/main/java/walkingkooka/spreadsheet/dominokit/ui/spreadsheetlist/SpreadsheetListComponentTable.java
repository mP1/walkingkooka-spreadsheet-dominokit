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

package walkingkooka.spreadsheet.dominokit.ui.spreadsheetlist;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.cards.Card;
import org.dominokit.domino.ui.datatable.CellTextAlign;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.TableConfig;
import org.dominokit.domino.ui.datatable.plugins.summary.EmptyStatePlugin;
import org.dominokit.domino.ui.datatable.store.LocalListDataStore;
import walkingkooka.spreadsheet.dominokit.dom.Doms;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.tree.text.TextAlign;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SpreadsheetListComponentTable implements HtmlElementComponent<HTMLDivElement, SpreadsheetListComponentTable> {

    /**
     * Creates an empty {@link walkingkooka.spreadsheet.dominokit.ui.spreadsheetlist.SpreadsheetListComponentTable}.
     */
    static SpreadsheetListComponentTable empty() {
        return new SpreadsheetListComponentTable();
    }

    private SpreadsheetListComponentTable() {
        this.card = Card.create();

        final LocalListDataStore<SpreadsheetListComponentTableRow> localListDataStore = new LocalListDataStore<>();

        final DataTable<SpreadsheetListComponentTableRow> table = new DataTable<>(
                tableConfig(),
                localListDataStore
        );
        table.setCondensed(true);

        this.table = table;
        this.dataStore = localListDataStore;
        this.table.headerElement().hide();

        this.card.appendChild(table);
    }

    private static TableConfig<SpreadsheetListComponentTableRow> tableConfig() {
        return new TableConfig<SpreadsheetListComponentTableRow>()
                .addColumn(
                        columnConfig(
                                "name",
                                TextAlign.LEFT,
                                row -> row.name().element()
                        )
                ).addColumn(
                        columnConfig(
                                "created",
                                TextAlign.CENTER,
                                row -> Doms.textNode(
                                        row.created()
                                )
                        )
                ).addColumn(
                        columnConfig(
                                "last-modified",
                                TextAlign.CENTER,
                                row -> Doms.textNode(
                                        row.lastModified()
                                )
                        )
                ).addColumn(
                        columnConfig(
                                "links",
                                TextAlign.CENTER,
                                row -> Doms.div(
                                        row.links()
                                )
                        )
                ).addPlugin(
                        EmptyStatePlugin.create(
                                SpreadsheetIcons.spreadsheetListTableEmpty(),
                                "No spreadsheet(s) found."
                        )
                );
    }

    private static ColumnConfig<SpreadsheetListComponentTableRow> columnConfig(final String columnName,
                                                                               final TextAlign textAlign,
                                                                               final Function<SpreadsheetListComponentTableRow, Node> cellMapper) {
        return ColumnConfig.<SpreadsheetListComponentTableRow>create(columnName)
                .setFixed(true)
                .minWidth("25%")
                .setTextAlign(
                        CellTextAlign.valueOf(
                                textAlign.name()
                        )
                )
                .setCellRenderer(cell -> cellMapper.apply(
                                cell.getTableRow()
                                        .getRecord()
                        )
                );
    }

    private DataTable<SpreadsheetListComponentTableRow> table;

    private LocalListDataStore<SpreadsheetListComponentTableRow> dataStore;

    void setMetadata(final List<SpreadsheetMetadata> metadatas) {
        this.dataStore.setData(
                metadatas.stream()
                        .map(SpreadsheetListComponentTableRow::with)
                        .collect(Collectors.toList()));
        this.table.load();
    }

    // HtmlElementComponent.............................................................................................

    @Override
    public HTMLDivElement element() {
        return this.card.element();
    }

    private final Card card;
}
