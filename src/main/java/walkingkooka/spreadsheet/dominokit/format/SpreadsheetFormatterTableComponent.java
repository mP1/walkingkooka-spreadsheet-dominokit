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

package walkingkooka.spreadsheet.dominokit.format;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.datatable.CellTextAlign;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponent;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetCard;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetElementIds;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSample;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextNode;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A table that displays {@link walkingkooka.spreadsheet.format.SpreadsheetFormatterSample} with one per row.
 */
public final class SpreadsheetFormatterTableComponent implements HtmlElementComponent<HTMLDivElement, SpreadsheetFormatterTableComponent>,
        TreePrintable {

    /**
     * Creates an empty {@link SpreadsheetFormatterTableComponent}.
     */
    static SpreadsheetFormatterTableComponent empty(final String id,
                                                    final SpreadsheetFormatterTableComponentContext context) {
        return new SpreadsheetFormatterTableComponent(
                CharSequences.failIfNullOrEmpty(id, "id"),
                Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetFormatterTableComponent(final String id,
                                               final SpreadsheetFormatterTableComponentContext context) {
        this.dataTable = SpreadsheetDataTableComponent.with(
                id + SpreadsheetElementIds.TABLE, // id
                columnConfigs(), // configs
                SpreadsheetFormatterTableComponentSpreadsheetDataTableComponentCellRenderer.with(
                        id,
                        context
                )
        ).hideHeaders();

        this.card = SpreadsheetCard.empty()
                .appendChild(this.dataTable);
    }

    private static List<ColumnConfig<SpreadsheetFormatterSample<TextNode>>> columnConfigs() {
        final List<ColumnConfig<SpreadsheetFormatterSample<TextNode>>> columns = Lists.array();

        columns.add(
                columnConfig(
                        "label",
                        TextAlign.LEFT
                )
        );

        columns.add(
                columnConfig(
                        "selector",
                        TextAlign.CENTER
                )
        );

        columns.add(
                columnConfig(
                        "formatted",
                        TextAlign.CENTER
                )
        );

        return columns;
    }

    private static ColumnConfig<SpreadsheetFormatterSample<TextNode>> columnConfig(final String columnName,
                                                                                   final TextAlign textAlign) {
        return ColumnConfig.<SpreadsheetFormatterSample<TextNode>>create(columnName)
                .setFixed(true)
                .minWidth("33%")
                .setTextAlign(
                        CellTextAlign.valueOf(
                                textAlign.name()
                        )
                );
    }

    public void refresh(final List<SpreadsheetFormatterSample<TextNode>> samples) {
        this.dataTable.setValue(
                Optional.of(samples)
        );
    }

    private final SpreadsheetDataTableComponent<SpreadsheetFormatterSample<TextNode>> dataTable;

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
    public SpreadsheetFormatterTableComponent setCssText(final String css) {
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
