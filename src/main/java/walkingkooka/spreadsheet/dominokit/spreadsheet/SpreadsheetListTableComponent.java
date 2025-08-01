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
import org.dominokit.domino.ui.datatable.CellTextAlign;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.card.SpreadsheetCard;
import walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponent;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetListHistoryToken;
import walkingkooka.spreadsheet.dominokit.value.TableComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextAlign;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A datatable where each row contains a single spreadsheet, showing various metadata items such as creator, timestamps and links for actions.
 */
final class SpreadsheetListTableComponent implements TableComponent<HTMLDivElement, List<SpreadsheetMetadata>, SpreadsheetListTableComponent> {

    /**
     * Creates an empty {@link SpreadsheetListTableComponent}.
     */
    static SpreadsheetListTableComponent empty(final String id,
                                               final SpreadsheetListDialogComponentContext context) {
        return new SpreadsheetListTableComponent(
            Objects.requireNonNull(id, "id"),
            Objects.requireNonNull(context, "context")
        );
    }

    final static int DEFAULT_COUNT = 10;

    private SpreadsheetListTableComponent(final String id,
                                          final SpreadsheetListDialogComponentContext context) {
        this.card = SpreadsheetCard.empty();

        this.dataTable = SpreadsheetDataTableComponent.with(
            id,
            columnConfigs(),
            SpreadsheetListTableComponentSpreadsheetDataTableComponentCellRenderer.with(
                id,
                context
            )
        ).emptyStatePlugin(
            SpreadsheetIcons.spreadsheetListTableEmpty(),
            "No spreadsheets"
        );

        this.card.appendChild(
            this.dataTable.previousNextLinks(id)
        );
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
                "Created timestamp",
                "create-date-time",
                TextAlign.LEFT
            ),
            columnConfig(
                "Last modified timestamp",
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

    private final SpreadsheetDataTableComponent<SpreadsheetMetadata> dataTable;

    @Override
    public SpreadsheetListTableComponent focus() {
        this.dataTable.focus();
        return this;
    }

    @Override
    public Optional<List<SpreadsheetMetadata>> value() {
        return this.dataTable.value();
    }

    @Override
    public SpreadsheetListTableComponent setValue(final Optional<List<SpreadsheetMetadata>> metadatas) {
        this.dataTable.setValue(metadatas);

        return this;
    }

    void refresh(final SpreadsheetListHistoryToken historyToken) {
        this.dataTable.refreshPreviousNextLinks(
            historyToken,
            DEFAULT_COUNT
        );
    }

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetListTableComponent setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.card.setCssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public SpreadsheetListTableComponent setCssProperty(final String name,
                                                        final String value) {
        this.card.setCssProperty(
            name,
            value
        );
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
