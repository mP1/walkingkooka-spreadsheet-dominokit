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

package walkingkooka.spreadsheet.dominokit.delta;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.datatable.CellTextAlign;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponent;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A table that displays matched cells in {@link SpreadsheetDelta} as a table
 */
public final class SpreadsheetDeltaMatchedCellsTableComponent implements HtmlElementComponent<HTMLDivElement, SpreadsheetDeltaMatchedCellsTableComponent>,
        NopFetcherWatcher,
        NopEmptyResponseFetcherWatcher,
        SpreadsheetDeltaFetcherWatcher {

    public static SpreadsheetDeltaMatchedCellsTableComponent with(final String id,
                                                                  final SpreadsheetDeltaMatchedCellsTableComponentContext context) {
        return new SpreadsheetDeltaMatchedCellsTableComponent(
                CharSequences.failIfNullOrEmpty(id, "id"),
                Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetDeltaMatchedCellsTableComponent(final String id,
                                                       final SpreadsheetDeltaMatchedCellsTableComponentContext context) {
        this.dataTable = SpreadsheetDataTableComponent.with(
                id + "cells-", // id-prefix
                columnConfigs(), // column configs
                SpreadsheetDeltaMatchedCellsTableComponentSpreadsheetDataTableComponentCellRenderer.with(context)
        ).bodyScrollPlugin();

        context.addSpreadsheetDeltaFetcherWatcher(this);
    }

    /**
     * The table showing matching cells will have four columns.
     * <pre>
     * cell | formula | value | formatted
     * </pre>
     */
    private static List<ColumnConfig<SpreadsheetCell>> columnConfigs() {
        return Lists.of(
                columnConfig(
                        "Cell",
                        CellTextAlign.LEFT
                ),

                columnConfig(
                        "Formula",
                        CellTextAlign.LEFT
                ),
                columnConfig(
                        "Formatted",
                        CellTextAlign.LEFT
                ),
                columnConfig(
                        "Value",
                        CellTextAlign.LEFT
                )
        );
    }

    private static ColumnConfig<SpreadsheetCell> columnConfig(final String title,
                                                              final CellTextAlign cellTextAlign) {
        return ColumnConfig.<SpreadsheetCell>create(
                title.toLowerCase(),
                title
        ).setTextAlign(cellTextAlign);
    }

    // SpreadsheetDeltaWatcher.........................................................................................

    /**
     * Replaces the cells in the {@link SpreadsheetDataTableComponent#setValue(Optional)}.
     */
    @Override
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta,
                                   final AppContext context) {
        final List<SpreadsheetCell> cells = Lists.array();
        cells.addAll(delta.cells());

        this.dataTable.setValue(
                Optional.of(cells)
        );
    }

    // HtmlElementComponent.............................................................................................

    @Override
    public HTMLDivElement element() {
        return this.dataTable.element();
    }

    @Override
    public SpreadsheetDeltaMatchedCellsTableComponent setCssText(final String css) {
        this.dataTable.setCssText(css);
        return this;
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        this.dataTable.printTree(printer);
        printer.outdent();
    }

    private final SpreadsheetDataTableComponent<SpreadsheetCell> dataTable;
}
