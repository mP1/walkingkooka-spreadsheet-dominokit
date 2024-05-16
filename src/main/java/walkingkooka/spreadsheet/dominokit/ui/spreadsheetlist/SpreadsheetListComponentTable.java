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
import org.dominokit.domino.ui.datatable.CellTextAlign;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.TableConfig;
import org.dominokit.domino.ui.datatable.plugins.summary.EmptyStatePlugin;
import org.dominokit.domino.ui.datatable.store.LocalListDataStore;
import walkingkooka.spreadsheet.dominokit.dom.Doms;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetListHistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.ui.card.SpreadsheetCard;
import walkingkooka.spreadsheet.dominokit.ui.flexlayout.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.tree.text.TextAlign;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A table where each row contains a single spreadsheet, showing various metadata items such as creator, timestamps and links for actions.
 */
final class SpreadsheetListComponentTable implements HtmlElementComponent<HTMLDivElement, SpreadsheetListComponentTable> {

    /**
     * Creates an empty {@link walkingkooka.spreadsheet.dominokit.ui.spreadsheetlist.SpreadsheetListComponentTable}.
     */
    static SpreadsheetListComponentTable empty(final SpreadsheetListComponentContext context) {
        return new SpreadsheetListComponentTable(context);
    }

    private final static String ID = SpreadsheetListDialogComponent.ID_PREFIX + "table";

    private final static String ID_PREFIX = ID + '-';

    final static int DEFAULT_COUNT = 10;

    private SpreadsheetListComponentTable(final SpreadsheetListComponentContext context) {
        this.card = SpreadsheetCard.empty();

        final LocalListDataStore<SpreadsheetListComponentTableRow> localListDataStore = new LocalListDataStore<>();

        final DataTable<SpreadsheetListComponentTableRow> table = new DataTable<>(
                tableConfig(),
                localListDataStore
        );
        table.id(ID);

        this.table = table;
        this.dataStore = localListDataStore;

        this.previous = previous(context);
        this.previous.element().style.setProperty("float", "left");

        this.next = next(context);
        this.next.element().style.setProperty("float", "right");

        this.table.element()
                .appendChild(
                        SpreadsheetFlexLayout.row()
                                .appendChild(this.previous)
                                .appendChild(this.next)
                                .element()
                );

        this.card.appendChild(table);

        this.context = context;

        this.tableCount = 0;
    }

    private static TableConfig<SpreadsheetListComponentTableRow> tableConfig() {
        return new TableConfig<SpreadsheetListComponentTableRow>()
                .addColumn(
                        columnConfig(
                                "Name",
                                "name",
                                TextAlign.LEFT,
                                row -> row.name().element()
                        )
                ).addColumn(
                        columnConfig(
                                "Created by",
                                "created-by",
                                TextAlign.CENTER,
                                row -> Doms.textNode(
                                        row.createdBy()
                                )
                        )
                ).addColumn(
                        columnConfig(
                                "Created",
                                "create-date-time",
                                TextAlign.CENTER,
                                row -> Doms.textNode(
                                        row.createDateTime()
                                )
                        )
                ).addColumn(
                        columnConfig(
                                "Last modified by",
                                "last-modified-by",
                                TextAlign.CENTER,
                                row -> Doms.textNode(
                                        row.lastModifiedBy()
                                )
                        )
                ).addColumn(
                        columnConfig(
                                "Last modified",
                                "last-modified-date-time",
                                TextAlign.CENTER,
                                row -> Doms.textNode(
                                        row.lastModifiedDateTime()
                                )
                        )
                ).addColumn(
                        columnConfig(
                                "Links",
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

    private static ColumnConfig<SpreadsheetListComponentTableRow> columnConfig(final String title,
                                                                               final String columnName,
                                                                               final TextAlign textAlign,
                                                                               final Function<SpreadsheetListComponentTableRow, Node> cellMapper) {
        return ColumnConfig.<SpreadsheetListComponentTableRow>create(columnName)
                .setTitle(title)
                .setFixed(true)
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

    private final DataTable<SpreadsheetListComponentTableRow> table;

    private final LocalListDataStore<SpreadsheetListComponentTableRow> dataStore;

    void setMetadata(final List<SpreadsheetMetadata> metadatas) {
        this.dataStore.setData(
                metadatas.stream()
                        .map(m -> SpreadsheetListComponentTableRow.with(m, this.context))
                        .collect(Collectors.toList()));
        this.tableCount = metadatas.size();
    }

    private int tableCount;

    void refresh(final SpreadsheetListHistoryToken historyToken) {
        final int from = historyToken.from()
                .orElse(0);
        final int count = historyToken.count()
                .orElse(DEFAULT_COUNT);

        // previous
        final HistoryTokenAnchorComponent previous = this.previous;
        final boolean previousDisabled = 0 == from;
        previous.setDisabled(previousDisabled);
        if (false == previousDisabled) {
            previous.setHistoryToken(
                    Optional.of(
                            historyToken.setFrom(
                                    OptionalInt.of(
                                            Math.max(
                                                    0,
                                                    from - count + 1
                                            )
                                    )
                            )
                    )
            );
        }

        // next
        final HistoryTokenAnchorComponent next = this.next;
        final boolean nextDisabled = this.tableCount < count;
        next.setDisabled(nextDisabled);
        if (false == nextDisabled) {
            next.setHistoryToken(
                    Optional.of(
                            historyToken.setFrom(
                                    OptionalInt.of(
                                            from + count - 1
                                    )
                            )
                    )
            );
        }
    }

    // previous.........................................................................................................

    private static HistoryTokenAnchorComponent previous(final SpreadsheetListComponentContext context) {
        return context.historyToken()
                .link(ID_PREFIX + "previous")
                .setTextContent("previous")
                .setIconBefore(
                        Optional.of(
                                SpreadsheetIcons.spreadsheetListTablePrevious()
                        )
                );
    }

    private final HistoryTokenAnchorComponent previous;

    // next.............................................................................................................

    private static HistoryTokenAnchorComponent next(final SpreadsheetListComponentContext context) {
        return context.historyToken()
                .link(ID_PREFIX + "next")
                .setTextContent("next")
                .setIconAfter(
                        Optional.of(
                                SpreadsheetIcons.spreadsheetListTableNext()
                        )
                );
    }

    private final HistoryTokenAnchorComponent next;

    private final SpreadsheetListComponentContext context;

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetListComponentTable setCssText(final String css) {
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
}
