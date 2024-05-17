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
import walkingkooka.Value;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetListHistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.ui.card.SpreadsheetCard;
import walkingkooka.spreadsheet.dominokit.ui.datatable.SpreadsheetDataTableComponent;
import walkingkooka.spreadsheet.dominokit.ui.flexlayout.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.ui.text.SpreadsheetTextComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.tree.text.TextAlign;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.BiFunction;

/**
 * A datatable where each row contains a single spreadsheet, showing various metadata items such as creator, timestamps and links for actions.
 */
final class SpreadsheetListComponentTable implements HtmlElementComponent<HTMLDivElement, SpreadsheetListComponentTable>,
        TreePrintable {

    /**
     * Creates an empty {@link SpreadsheetListComponentTable}.
     */
    static SpreadsheetListComponentTable empty(final SpreadsheetListComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetListComponentTable(context);
    }

    private final static String ID = SpreadsheetListDialogComponent.ID_PREFIX + "datatable";

    private final static String ID_PREFIX = ID + '-';

    final static int DEFAULT_COUNT = 10;

    private SpreadsheetListComponentTable(final SpreadsheetListComponentContext context) {
        this.card = SpreadsheetCard.empty();

        this.table = SpreadsheetDataTableComponent.with(
                ID,
                columnConfigs(),
                cellRenderer()
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

        this.context = context;

        this.tableCount = 0;
    }

    private BiFunction<Integer, SpreadsheetMetadata, HtmlElementComponent<?, ?>> cellRenderer() {
        return (column, metadata) -> {
            final HtmlElementComponent<?, ?> component;

            switch (column) {
                case 0: // name
                    component = spreadsheetName(metadata);
                    break;
                case 1: // created by
                    component = hasText(
                            SpreadsheetMetadataPropertyName.CREATOR,
                            metadata
                    );
                    break;
                case 2: // create-date-time
                    component = dateTime(
                            SpreadsheetMetadataPropertyName.CREATE_DATE_TIME,
                            metadata
                    );
                    break;
                case 3: // lastmod by
                    component = hasText(
                            SpreadsheetMetadataPropertyName.MODIFIED_BY,
                            metadata
                    );
                    break;
                case 4: // create-date-time
                    component = dateTime(
                            SpreadsheetMetadataPropertyName.MODIFIED_DATE_TIME,
                            metadata
                    );
                    break;
                case 5: // links
                    component = links(metadata);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown column " + column);
            }


            return component;
        };
    }

    private HtmlElementComponent<?, ?> spreadsheetName(final SpreadsheetMetadata metadata) {
        final SpreadsheetId id = metadata.id().orElse(null);

        return HistoryToken.spreadsheetLoad(id)
                .link(
                        SpreadsheetListDialogComponent.ID_PREFIX + id.toString()
                ).setTextContent(
                        metadata.name()
                                .orElse(null)
                                .toString()
                );
    }

    private <TT extends Value<String>> SpreadsheetTextComponent hasText(final SpreadsheetMetadataPropertyName<TT> propertyName,
                                                                        final SpreadsheetMetadata metadata) {
        return text(
                metadata.get(propertyName)
                        .map(Value::value)
        );
    }

    private SpreadsheetTextComponent dateTime(final SpreadsheetMetadataPropertyName<LocalDateTime> propertyName,
                                              final SpreadsheetMetadata metadata) {
        return text(
                metadata.get(propertyName)
                        .map(this.context::formatDateTime)
        );
    }

    private SpreadsheetTextComponent text(final Optional<String> text) {
        return SpreadsheetTextComponent.with(text);
    }

    private SpreadsheetFlexLayout links(final SpreadsheetMetadata metadata) {
        final SpreadsheetId id = metadata.id()
                .orElse(null);

        final HistoryTokenAnchorComponent rename = HistoryToken.spreadsheetListRenameSelect(
                        id
                ).link(SpreadsheetListDialogComponent.ID_PREFIX + id + "-rename")
                .setTextContent("Rename");

        final HistoryTokenAnchorComponent delete = HistoryToken.spreadsheetListDelete(
                        id
                ).link(SpreadsheetListDialogComponent.ID_PREFIX + id + "-delete")
                .setTextContent("Delete");

        return SpreadsheetFlexLayout.row()
                .appendChild(rename)
                .appendChild(delete);
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
                        TextAlign.CENTER
                ),
                columnConfig(
                        "Created",
                        "create-date-time",
                        TextAlign.CENTER
                ),
                columnConfig(
                        "Last modified by",
                        "last-modified-by",
                        TextAlign.CENTER
                ),
                columnConfig(
                        "Last modified",
                        "last-modified-date-time",
                        TextAlign.CENTER
                ),
                columnConfig(
                        "Links",
                        "links",
                        TextAlign.CENTER
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

    SpreadsheetListComponentTable setMetadata(final List<SpreadsheetMetadata> metadatas) {
        this.table.setValue(
                Optional.of(
                        metadatas
                )
        );
        this.tableCount = metadatas.size();

        return this;
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
