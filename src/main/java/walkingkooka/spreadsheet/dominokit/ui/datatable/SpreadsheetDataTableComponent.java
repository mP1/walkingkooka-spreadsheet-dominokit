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

package walkingkooka.spreadsheet.dominokit.ui.datatable;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.TableConfig;
import org.dominokit.domino.ui.datatable.store.LocalListDataStore;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.ValueComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * A {@link ValueComponent} wrapper around a {@link DataTable}.
 */
public final class SpreadsheetDataTableComponent<T> implements SpreadsheetDataTableComponentLike<T> {

    public static <T> SpreadsheetDataTableComponent<T> with(final String id,
                                                            final List<ColumnConfig<T>> columnConfigs,
                                                            final BiFunction<Integer, T, HtmlElementComponent<?, ?>> cellRenderer) {
        return new SpreadsheetDataTableComponent<>(
                id,
                columnConfigs,
                cellRenderer
        );
    }

    private SpreadsheetDataTableComponent(final String id,
                                          final List<ColumnConfig<T>> columnConfigs,
                                          final BiFunction<Integer, T, HtmlElementComponent<?, ?>> cellRenderer) {
        CharSequences.failIfNullOrEmpty(id, "id");

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
        ).id(id);
        this.dataStore = localListDataStore;

        this.cellRenderer = cellRenderer;

        this.setId(id);
    }

    private TableConfig<T> tableConfig(final List<ColumnConfig<T>> columnConfigs,
                                       final BiFunction<Integer, T, HtmlElementComponent<?, ?>> cellRenderer) {
        final TableConfig<T> tableConfig = new TableConfig<>();

        int i = 0;

        for (final ColumnConfig<T> columnConfig : columnConfigs) {
            tableConfig.addColumn(
                    // overwrite the original cellrenderer
                    columnConfig.setCellRenderer(
                            SpreadsheetDataTableComponentCellRenderer.with(
                                    i,
                                    cellRenderer
                            )
                    )
            );

            i++;
        }

        return tableConfig;
    }

    @Override
    public SpreadsheetDataTableComponent<T> setId(final String id) {
        this.table.setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.table.getId();
    }

    @Override
    public SpreadsheetDataTableComponent<T> setLabel(String label) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String label() {
        return "";
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

    // helperText.......................................................................................................

    @Override
    public SpreadsheetDataTableComponent<T> alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<String> helperText() {
        return Optional.empty();
    }

    @Override
    public SpreadsheetDataTableComponent<T> setHelperText(final Optional<String> text) {
        throw new UnsupportedOperationException();
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

    // validate.........................................................................................................

    @Override
    public SpreadsheetDataTableComponent<T> validate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDataTableComponent<T> optional() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDataTableComponent<T> required() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRequired() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> errors() {
        return Lists.empty();
    }

    @Override
    public SpreadsheetDataTableComponent<T> setErrors(final List<String> errors) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDataTableComponent<T> setCssText(String css) {
        throw new UnsupportedOperationException();
    }

    // events...........................................................................................................

    @Override
    public SpreadsheetDataTableComponent<T> addChangeListener(final ChangeListener<Optional<List<T>>> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDataTableComponent<T> addFocusListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDataTableComponent<T> addKeydownListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDataTableComponent<T> addKeyupListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDataTableComponent<T> hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDataTableComponent<T> removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDataTableComponent<T> focus() {
        throw new UnsupportedOperationException();
    }

    // IsElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.table.element();
    }

    // TreePrintable....................................................................................................

    // TODO print row by row etc
    @Override
    public void printTree(final IndentingPrinter printer) {
        this.printTreeTable(
                this.table.getTableConfig()
                        .getColumns(),
                this.cellRenderer,
                printer
        );
    }

    private final BiFunction<Integer, T, HtmlElementComponent<?, ?>> cellRenderer;
}
