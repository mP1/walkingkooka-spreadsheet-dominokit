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

package walkingkooka.spreadsheet.dominokit.key;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.datatable.CellTextAlign;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.card.CardComponent;
import walkingkooka.spreadsheet.dominokit.datatable.DataTableComponent;
import walkingkooka.spreadsheet.dominokit.value.TableComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextAlign;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A table that displays {@link KeyBinding} with one per row.
 */
public final class KeyBindingTableComponent implements TableComponent<HTMLDivElement, List<KeyBinding>, KeyBindingTableComponent>,
    HtmlComponentDelegator<HTMLDivElement, KeyBindingTableComponent> {

    /**
     * Creates an empty {@link KeyBindingTableComponent}.
     */
    public static KeyBindingTableComponent empty(final String id) {
        return new KeyBindingTableComponent(
            CharSequences.failIfNullOrEmpty(id, "id")
        );
    }

    private KeyBindingTableComponent(final String id) {
        this.cellRenderer = KeyBindingTableComponentDataTableComponentCellRenderer.INSTANCE;
        this.dataTable = DataTableComponent.with(
            id, // id
            columnConfigs(), // configs
            this.cellRenderer
        ).hideHeaders();

        this.card = CardComponent.empty()
            .appendChild(this.dataTable);
    }

    private static List<ColumnConfig<KeyBinding>> columnConfigs() {
        final List<ColumnConfig<KeyBinding>> columns = Lists.array();

        columns.add(
            columnConfig(
                "Command/Action",
                TextAlign.LEFT
            )
        );

        columns.add(
            columnConfig(
                "Key",
                TextAlign.CENTER
            )
        );

        return columns;
    }

    private static ColumnConfig<KeyBinding> columnConfig(final String columnName,
                                                         final TextAlign textAlign) {
        return ColumnConfig.<KeyBinding>create(columnName)
            .setFixed(true)
            .minWidth("50%")
            .setTextAlign(
                CellTextAlign.valueOf(
                    textAlign.name()
                )
            );
    }

    @Override
    public KeyBindingTableComponent focus() {
        this.dataTable.focus();
        return this;
    }

    @Override
    public KeyBindingTableComponent blur() {
        this.dataTable.blur();
        return this;
    }

    @Override
    public Optional<List<KeyBinding>> value() {
        return this.dataTable.value();
    }

    @Override
    public KeyBindingTableComponent setValue(final Optional<List<KeyBinding>> bindings) {
        this.dataTable.setValue(
            Objects.requireNonNull(bindings, "bindings")
        );
        return this;
    }

    public void refresh(final KeyBindingTableComponentContext context) {
        this.setValue(
            Optional.of(
                context.keyBindings()
            )
        );
    }

    private final DataTableComponent<KeyBinding> dataTable;

    private final KeyBindingTableComponentDataTableComponentCellRenderer cellRenderer;

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLDivElement, ?> htmlComponent() {
        return this.card;
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
}
