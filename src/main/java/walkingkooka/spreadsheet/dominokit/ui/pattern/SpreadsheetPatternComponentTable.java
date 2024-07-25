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

package walkingkooka.spreadsheet.dominokit.ui.pattern;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.datatable.CellTextAlign;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.ui.card.SpreadsheetCard;
import walkingkooka.spreadsheet.dominokit.ui.datatable.SpreadsheetDataTableComponent;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.tree.text.TextAlign;

import java.util.List;
import java.util.Optional;

/**
 * A table that holds available patterns used to format values.
 */
final class SpreadsheetPatternComponentTable implements HtmlElementComponent<HTMLDivElement, SpreadsheetPatternComponentTable>,
        TreePrintable {

    /**
     * Creates an empty {@link SpreadsheetPatternComponentTable}.
     */
    static SpreadsheetPatternComponentTable empty() {
        return new SpreadsheetPatternComponentTable();
    }

    private SpreadsheetPatternComponentTable() {
        this.card = SpreadsheetCard.empty();
    }

    void refresh(final String patternText,
                 final SpreadsheetPatternDialogComponentContext context) {
        final SpreadsheetPatternKind patternKind = context.patternKind();

        // recreate table if pattern kind different
        if (false == patternKind.equals(this.patternKind)) {
            this.card.removeAllChildren();

            this.patternKind = patternKind;
            this.dataTable = SpreadsheetDataTableComponent.with(
                    SpreadsheetPatternDialogComponent.ID_PREFIX + SpreadsheetElementIds.TABLE, // id
                    columnConfigs(patternKind), // configs
                    SpreadsheetPatternComponentTableSpreadsheetDataTableComponentCellRenderer.with(context)
            ).hideHeaders();
            this.card.appendChild(this.dataTable);
        }

        // load dataStore and table with new rows...
        this.dataTable.setValue(
                Optional.of(
                        SpreadsheetPatternComponentTableRowProvider.spreadsheetPatternKind(patternKind)
                                .apply(
                                        patternText,
                                        SpreadsheetPatternComponentTableRowProviderContexts.basic(
                                                patternKind,
                                                context.spreadsheetFormatterContext(),
                                                context
                                        )
                                )
                )
        );
    }

    private SpreadsheetPatternKind patternKind;

    private SpreadsheetDataTableComponent<SpreadsheetPatternComponentTableRow> dataTable;

    /**
     * Prepares the {@link ColumnConfig} for each of the columns that will appear in the table.
     */
    private static List<ColumnConfig<SpreadsheetPatternComponentTableRow>> columnConfigs(final SpreadsheetPatternKind kind) {
        final List<ColumnConfig<SpreadsheetPatternComponentTableRow>> columns = Lists.array();

        columns.add(
                columnConfig(
                        "label",
                        TextAlign.LEFT
                )
        );

        columns.add(
                columnConfig(
                        "pattern-text",
                        TextAlign.CENTER
                )
        );

        switch (kind) {
            case NUMBER_FORMAT_PATTERN:
            case NUMBER_PARSE_PATTERN:
                for (int i = 0; i < 3; i++) {
                    final int j = i;
                    columns.add(
                            columnConfig(
                                    "formatted-" + j,
                                    TextAlign.CENTER
                            )
                    );
                }

                break;
            default:
                columns.add(
                        columnConfig(
                                "formatted",
                                TextAlign.CENTER
                        )
                );
        }

        return columns;
    }

    private static ColumnConfig<SpreadsheetPatternComponentTableRow> columnConfig(final String columnName,
                                                                                  final TextAlign textAlign) {
        return ColumnConfig.<SpreadsheetPatternComponentTableRow>create(columnName)
                .setFixed(true)
                .minWidth("25%")
                .setTextAlign(
                        CellTextAlign.valueOf(
                                textAlign.name()
                        )
                );
    }

    // IsElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.card.element();
    }

    // node.............................................................................................................

    @Override
    public Node node() {
        return this.element();
    }

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetPatternComponentTable setCssText(final String css) {
        this.card.setCssText(css);
        return this;
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
