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

package walkingkooka.spreadsheet.dominokit.spreadsheet;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.datatable.CellTextAlign;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.card.SpreadsheetCard;
import walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponent;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetListHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextAlign;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * A datatable where each row contains a single spreadsheet, showing various metadata items such as creator, timestamps and links for actions.
 */
final class SpreadsheetListTableComponent implements HtmlElementComponent<HTMLDivElement, SpreadsheetListTableComponent> {

    /**
     * Creates an empty {@link SpreadsheetListTableComponent}.
     */
    static SpreadsheetListTableComponent empty(final SpreadsheetListComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetListTableComponent(context);
    }

    private final static String ID = "spreadsheetList";

    private final static String ID_PREFIX = ID + '-';

    final static int DEFAULT_COUNT = 10;

    private SpreadsheetListTableComponent(final SpreadsheetListComponentContext context) {
        this.card = SpreadsheetCard.empty();

        this.table = SpreadsheetDataTableComponent.with(
                ID_PREFIX,
                columnConfigs(),
                SpreadsheetListTableComponentSpreadsheetDataTableComponentCellRenderer.with(context)
        ).emptyStatePlugin(
                SpreadsheetIcons.spreadsheetListTableEmpty(),
                "No spreadsheets"
        );

        this.previous = previous(context);
        this.previous.setCssText("float=left");

        this.next = next(context);
        this.next.setCssText("float=right");

        this.table.appendChild(
                SpreadsheetFlexLayout.row()
                        .appendChild(this.previous)
                        .appendChild(this.next)
        );

        this.card.appendChild(table);

        this.tableRowCount = 0;
    }

    private List<ColumnConfig<SpreadsheetMetadata>> columnConfigs() {
        return Lists.of(
                columnConfig(
                        "Name",
                        "name",
                        TextAlign.LEFT
                ),
                columnConfig(
                        "Created by",
                        "created-by",
                        TextAlign.LEFT
                ),
                columnConfig(
                        "Created",
                        "create-date-time",
                        TextAlign.LEFT
                ),
                columnConfig(
                        "Last modified by",
                        "last-modified-by",
                        TextAlign.LEFT
                ),
                columnConfig(
                        "Last modified",
                        "last-modified-date-time",
                        TextAlign.LEFT
                ),
                columnConfig(
                        "Links",
                        "links",
                        TextAlign.LEFT
                )
        );
    }

    private static ColumnConfig<SpreadsheetMetadata> columnConfig(final String title,
                                                                  final String columnName,
                                                                  final TextAlign textAlign) {
        return ColumnConfig.<SpreadsheetMetadata>create(columnName)
                .setTitle(title)
                .setFixed(true)
                .setTextAlign(
                        CellTextAlign.valueOf(
                                textAlign.name()
                        )
                );
    }

    private final SpreadsheetDataTableComponent<SpreadsheetMetadata> table;

    SpreadsheetListTableComponent setMetadata(final List<SpreadsheetMetadata> metadatas) {
        this.table.setValue(
                Optional.of(
                        metadatas
                )
        );
        this.tableRowCount = metadatas.size();

        return this;
    }

    private int tableRowCount;

    void refresh(final SpreadsheetListHistoryToken historyToken) {
        final int offset = historyToken.offset()
                .orElse(0);
        final int count = historyToken.count()
                .orElse(DEFAULT_COUNT);

        // previous
        final HistoryTokenAnchorComponent previous = this.previous;
        final boolean previousDisabled = 0 == offset;
        previous.setDisabled(previousDisabled);
        if (false == previousDisabled) {
            previous.setHistoryToken(
                    Optional.of(
                            historyToken.setOffset(
                                    OptionalInt.of(
                                            Math.max(
                                                    0,
                                                    offset - count + 1
                                            )
                                    )
                            )
                    )
            );
        }

        // next
        final HistoryTokenAnchorComponent next = this.next;
        final boolean nextDisabled = this.tableRowCount < count;
        next.setDisabled(nextDisabled);
        if (false == nextDisabled) {
            next.setHistoryToken(
                    Optional.of(
                            historyToken.setOffset(
                                    OptionalInt.of(
                                            offset + count - 1
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

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetListTableComponent setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.card.setCssText(css);
        return this;
    }

    // HtmlElementComponent.............................................................................................

    @Override
    public HTMLDivElement element() {
        return this.card.element();
    }

    // node.............................................................................................................

    @Override
    public Node node() {
        return this.element();
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
