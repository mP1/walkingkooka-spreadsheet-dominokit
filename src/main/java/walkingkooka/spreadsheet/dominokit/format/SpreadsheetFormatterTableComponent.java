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
import org.dominokit.domino.ui.datatable.CellTextAlign;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import walkingkooka.Value;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.card.SpreadsheetCard;
import walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponent;
import walkingkooka.spreadsheet.dominokit.value.TableComponent;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSample;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextAlign;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A table that displays {@link walkingkooka.spreadsheet.format.SpreadsheetFormatterSample} with one per row.
 */
public final class SpreadsheetFormatterTableComponent implements TableComponent<SpreadsheetFormatterTableComponent>,
    Value<Optional<List<SpreadsheetFormatterSample>>> {

    /**
     * Creates an empty {@link SpreadsheetFormatterTableComponent}.
     */
    public static SpreadsheetFormatterTableComponent empty(final String id) {
        return new SpreadsheetFormatterTableComponent(
            CharSequences.failIfNullOrEmpty(id, "id")
        );
    }

    private SpreadsheetFormatterTableComponent(final String id) {
        this.cellRenderer = SpreadsheetFormatterTableComponentSpreadsheetDataTableComponentCellRenderer.with(id);
        this.dataTable = SpreadsheetDataTableComponent.with(
            id, // id
            columnConfigs(), // configs
            this.cellRenderer
        ).hideHeaders();

        this.card = SpreadsheetCard.empty()
            .appendChild(this.dataTable);
    }

    private static List<ColumnConfig<SpreadsheetFormatterSample>> columnConfigs() {
        final List<ColumnConfig<SpreadsheetFormatterSample>> columns = Lists.array();

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

    private static ColumnConfig<SpreadsheetFormatterSample> columnConfig(final String columnName,
                                                                         final TextAlign textAlign) {
        return ColumnConfig.<SpreadsheetFormatterSample>create(columnName)
            .setFixed(true)
            .minWidth("33%")
            .setTextAlign(
                CellTextAlign.valueOf(
                    textAlign.name()
                )
            );
    }

    @Override
    public Optional<List<SpreadsheetFormatterSample>> value() {
        return this.samples;
    }

    public void setValue(final Optional<List<SpreadsheetFormatterSample>> samples) {
        this.samples = Objects.requireNonNull(samples, "samples");
    }

    private Optional<List<SpreadsheetFormatterSample>> samples;

    public void refresh(final SpreadsheetFormatterTableComponentContext context) {
        this.cellRenderer.context = Objects.requireNonNull(context, "context");
        this.dataTable.setValue(this.samples);

        // manually show/hide depending on samples. Card will never be empty because SpreadsheetDataTableComponent is never empty
        if (this.samples.isEmpty()) {
            this.card.hide();
        } else {
            this.card.show();
        }
    }

    private final SpreadsheetDataTableComponent<SpreadsheetFormatterSample> dataTable;

    private final SpreadsheetFormatterTableComponentSpreadsheetDataTableComponentCellRenderer cellRenderer;

    // IsElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.card.element();
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
