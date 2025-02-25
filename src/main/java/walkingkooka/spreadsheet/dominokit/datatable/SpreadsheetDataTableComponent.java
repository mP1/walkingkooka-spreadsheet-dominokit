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

package walkingkooka.spreadsheet.dominokit.datatable;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.TableConfig;
import org.dominokit.domino.ui.datatable.plugins.pagination.BodyScrollPlugin;
import org.dominokit.domino.ui.datatable.plugins.summary.EmptyStatePlugin;
import org.dominokit.domino.ui.datatable.store.LocalListDataStore;
import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link FormValueComponent} wrapper around a {@link DataTable}.
 */
public final class SpreadsheetDataTableComponent<T> extends SpreadsheetDataTableComponentLike<T> {

    public static <T> SpreadsheetDataTableComponent<T> with(final String id,
                                                            final List<ColumnConfig<T>> columnConfigs,
                                                            final SpreadsheetDataTableComponentCellRenderer<T> cellRenderer) {
        return new SpreadsheetDataTableComponent<>(
            id,
            columnConfigs,
            cellRenderer
        );
    }

    private SpreadsheetDataTableComponent(final String id,
                                          final List<ColumnConfig<T>> columnConfigs,
                                          final SpreadsheetDataTableComponentCellRenderer<T> cellRenderer) {
        CharSequences.failIfNullOrEmpty(id, "id");

        this.id = id;

        final LocalListDataStore<T> localListDataStore = new LocalListDataStore<>();

        this.table = new DataTable<>(
            tableConfig(
                Objects.requireNonNull(
                    Lists.immutable(columnConfigs),
                    "tableConfigs"
                ),
                Objects.requireNonNull(
                    cellRenderer,
                    "cellRenderer"
                )
            ),
            localListDataStore
        ).setId(
            CharSequences.subSequence(id, 0, -1) + SpreadsheetElementIds.TABLE
        );
        this.dataStore = localListDataStore;

        this.cellRenderer = cellRenderer;

        this.children = Lists.array();

        this.plugins = Lists.array();

        this.previous = null;
        this.next = null;
    }

    private TableConfig<T> tableConfig(final List<ColumnConfig<T>> columnConfigs,
                                       final SpreadsheetDataTableComponentCellRenderer<T> cellRenderer) {
        final TableConfig<T> tableConfig = new TableConfig<>();

        int i = 0;

        for (final ColumnConfig<T> columnConfig : columnConfigs) {
            tableConfig.addColumn(
                // overwrite the original cellrenderer
                columnConfig.setCellRenderer(
                    SpreadsheetDataTableComponentDataTableCellRenderer.with(
                        i,
                        cellRenderer
                    )
                )
            );

            i++;
        }

        return tableConfig;
    }

    // id...............................................................................................................

    @Override
    public String id() {
        return this.id;
    }

    private final String id;

    @Override
    public SpreadsheetDataTableComponent<T> setId(final String id) {
        throw new UnsupportedOperationException();
    }

    // disabled.........................................................................................................

    @Override
    public boolean isDisabled() {
        return this.table.isDisabled();
    }

    @Override
    public SpreadsheetDataTableComponent<T> setDisabled(final boolean disabled) {
        this.table.setDisabled(disabled);
        return this;
    }

    // value............................................................................................................

    @Override
    public Optional<List<T>> value() {
        return Optional.of(
            Lists.immutable(
                this.dataStore.getRecords()
            )
        );
    }

    @Override
    public SpreadsheetDataTableComponent<T> setValue(final Optional<List<T>> value) {
        Objects.requireNonNull(value, "value");

        this.dataStore.setData(
            value.map(Lists::immutable)
                .orElse(Lists.empty())
        );
        return this;
    }

    private final DataTable<T> table;

    private final LocalListDataStore<T> dataStore;

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetDataTableComponent<T> setCssText(final String css) {
        this.table.cssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public SpreadsheetDataTableComponent<T> setCssProperty(final String name,
                                                           final String value) {
        this.table.setCssProperty(
            name,
            value
        );
        return this;
    }

    // ComponentWithChildren............................................................................................

    @Override
    public SpreadsheetDataTableComponent<T> appendChild(final IsElement<?> child) {
        this.table.element()
            .appendChild(child.element());
        return this;
    }

    @Override
    public SpreadsheetDataTableComponent<T> removeChild(final int index) {
        final IsElement<?> child = this.children.remove(index);
        this.table.element()
            .removeChild(child.element());
        return this;
    }

    @Override
    public List<IsElement<?>> children() {
        return Lists.immutable(
            this.children
        );
    }

    private final List<IsElement<?>> children;

    // IsElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.table.element();
    }

    // prev / next links................................................................................................

    /**
     * Creates and adds previous and next links. It is assumed the {@link walkingkooka.spreadsheet.dominokit.history.HistoryToken}
     */
    @Override
    public SpreadsheetDataTableComponent<T> previousNextLinks(final String idPrefix) {
        Objects.requireNonNull(idPrefix, "idPrefix");

        this.previous = this.previous(idPrefix);
        this.previous.setCssText("float=left");

        this.next = this.next(idPrefix);
        this.next.setCssText("float=right");

        this.appendChild(
            SpreadsheetFlexLayout.row()
                .appendChild(this.previous)
                .appendChild(this.next)
        );

        return this;
    }

    // previous.........................................................................................................

    @Override
    public SpreadsheetDataTableComponent<T> setPrevious(final Optional<HistoryToken> historyToken) {
        this.previous.setHistoryToken(historyToken);
        return this;
    }

    private HistoryTokenAnchorComponent previous;

    // next.............................................................................................................

    @Override
    public SpreadsheetDataTableComponent<T> setNext(final Optional<HistoryToken> historyToken) {
        this.next.setHistoryToken(historyToken);
        return this;
    }

    private HistoryTokenAnchorComponent next;

    // header...........................................................................................................

    @Override
    public SpreadsheetDataTableComponent<T> hideHeaders() {
        this.table.headerElement()
            .hide();
        this.headersHidden = true;
        return this;
    }

    private boolean headersHidden;

    // plugins..........................................................................................................

    @Override
    public SpreadsheetDataTableComponent<T> bodyScrollPlugin() {
        this.plugins.add(
            SpreadsheetDataTableComponentLike.bodyScrollPluginText()
        );
        this.table.getTableConfig()
            .addPlugin(
                new BodyScrollPlugin<>()
            );
        return this;
    }

    @Override
    public SpreadsheetDataTableComponent<T> emptyStatePlugin(final Icon<?> icon,
                                                             final String title) {
        this.plugins.add(
            SpreadsheetDataTableComponentLike.emptyStatePluginText(
                icon,
                title
            )
        );
        this.table.getTableConfig()
            .addPlugin(
                EmptyStatePlugin.create(
                    icon,
                    title
                )
            );
        return this;
    }

    private final List<String> plugins;

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        this.printTreeTable(
            this.table.getTableConfig()
                .getColumns(),
            this.headersHidden,
            this.cellRenderer,
            this.plugins,
            printer
        );
    }

    private final SpreadsheetDataTableComponentCellRenderer<T> cellRenderer;
}
