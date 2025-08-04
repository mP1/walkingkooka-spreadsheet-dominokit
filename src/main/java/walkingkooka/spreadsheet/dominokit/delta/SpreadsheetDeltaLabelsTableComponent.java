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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.card.CardComponent;
import walkingkooka.spreadsheet.dominokit.datatable.SpreadsheetDataTableComponent;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.value.TableComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A table that displays {@link SpreadsheetDelta#labels()} in a table form.
 */
public final class SpreadsheetDeltaLabelsTableComponent implements TableComponent<HTMLDivElement, SpreadsheetDelta, SpreadsheetDeltaLabelsTableComponent>,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher,
    SpreadsheetDeltaFetcherWatcher {

    final static int DEFAULT_COUNT = 10;

    public static SpreadsheetDeltaLabelsTableComponent with(final String id,
                                                            final SpreadsheetDeltaLabelsTableComponentContext context) {
        return new SpreadsheetDeltaLabelsTableComponent(
            CharSequences.failIfNullOrEmpty(id, "id"),
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetDeltaLabelsTableComponent(final String id,
                                                 final SpreadsheetDeltaLabelsTableComponentContext context) {
        final String idPrefix = id + "labels-";

        this.card = CardComponent.empty();

        this.dataTable = SpreadsheetDataTableComponent.with(
            idPrefix, // id-prefix
            columnConfigs(), // column configs
            SpreadsheetDeltaLabelsTableComponentSpreadsheetDataTableComponentCellRenderer.with(
                idPrefix,
                context
            )
        ).bodyScrollPlugin();

        this.card.appendChild(
            this.dataTable.previousNextLinks(id)
        );

        context.addSpreadsheetDeltaFetcherWatcher(this);

        this.context = context;
    }

    /**
     * The table showing labels will have four columns.
     * <pre>
     * cell | formula | formatted | link
     * </pre>
     */
    private static List<ColumnConfig<SpreadsheetDeltaLabelsTableComponentRow>> columnConfigs() {
        return Lists.of(
            columnConfig(
                "Label",
                CellTextAlign.LEFT
            ),

            columnConfig(
                "Cell",
                CellTextAlign.LEFT
            ),
            columnConfig(
                "Formatted",
                CellTextAlign.LEFT
            ),
            columnConfig(
                "Links",
                CellTextAlign.LEFT
            )
        );
    }

    private static ColumnConfig<SpreadsheetDeltaLabelsTableComponentRow> columnConfig(final String title,
                                                                                      final CellTextAlign cellTextAlign) {
        return ColumnConfig.<SpreadsheetDeltaLabelsTableComponentRow>create(
            title.toLowerCase(),
            title
        ).setTextAlign(cellTextAlign);
    }

    @Override
    public SpreadsheetDeltaLabelsTableComponent focus() {
        this.dataTable.focus();
        return this;
    }

    // value............................................................................................................

    @Override
    public Optional<SpreadsheetDelta> value() {
        return this.value;
    }

    @Override
    public SpreadsheetDeltaLabelsTableComponent setValue(final Optional<SpreadsheetDelta> value) {
        Objects.requireNonNull(value, "value");

        this.dataTable.setValue(
            value.map((final SpreadsheetDelta d) -> d.labels()
                .stream()
                .map(m ->
                    SpreadsheetDeltaLabelsTableComponentRow.with(
                        m,
                        this.context.labelCell(
                            m.label()
                        )
                    )
                ).collect(Collectors.toList())
            )
        );
        this.value = value;

        return this;
    }

    private Optional<SpreadsheetDelta> value;

    private final SpreadsheetDeltaLabelsTableComponentContext context;

    // refresh..........................................................................................................

    public SpreadsheetDeltaLabelsTableComponent refresh(final HistoryToken token) {
        this.dataTable.refreshPreviousNextLinks(
            token,
            DEFAULT_COUNT
        );
        return this;
    }

    // SpreadsheetDeltaFetcherWatcher...................................................................................

    /**
     * Replaces the cells in the {@link SpreadsheetDataTableComponent#setValue(Optional)}.
     */
    @Override
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta,
                                   final AppContext context) {
        this.setValue(
            Optional.of(delta)
        );
    }

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetDeltaLabelsTableComponent setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.card.setCssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public SpreadsheetDeltaLabelsTableComponent setCssProperty(final String name,
                                                               final String value) {
        this.card.setCssProperty(
            name,
            value
        );
        return this;
    }

    // HtmlComponent.............................................................................................

    @Override
    public HTMLDivElement element() {
        return this.card.element();
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

    private final SpreadsheetDataTableComponent<SpreadsheetDeltaLabelsTableComponentRow> dataTable;
}
