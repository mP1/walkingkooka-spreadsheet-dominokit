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

package walkingkooka.spreadsheet.dominokit.viewport;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLTableElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.SpreadsheetViewportWindows;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.dom.SpreadsheetElementComponent;
import walkingkooka.spreadsheet.dominokit.dom.SpreadsheetTBodyComponent;
import walkingkooka.spreadsheet.dominokit.dom.SpreadsheetTHeadComponent;
import walkingkooka.spreadsheet.dominokit.dom.SpreadsheetTableComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A TABLE which holds all the ROWs that are displayed. It also contains and caches rows.
 */
final class SpreadsheetViewportComponentTable implements HtmlComponent<HTMLTableElement, SpreadsheetViewportComponentTable> {

    static SpreadsheetViewportComponentTable empty(final SpreadsheetViewportComponentTableContext context) {
        return new SpreadsheetViewportComponentTable(context);
    }

    private SpreadsheetViewportComponentTable(final SpreadsheetViewportComponentTableContext context) {
        this.context = context;

        final SpreadsheetTableComponent table = SpreadsheetElementComponent.table()
            .setId(SpreadsheetViewportComponent.ID)
            .setOverflow("hidden");
        this.columnHeaders = SpreadsheetViewportComponentTableRowColumnHeaders.empty(context);

        final SpreadsheetTHeadComponent thead = SpreadsheetElementComponent.thead();
        thead.appendChild(this.columnHeaders);
        table.appendChild(thead);

        final SpreadsheetTBodyComponent tbody = SpreadsheetElementComponent.tbody();
        table.appendChild(tbody);

        this.table = table;
        this.tbody = tbody;

        this.id = null;
        this.name = null;

        this.rows = SortedSets.tree();
        this.rowsToTableRowCells = Maps.sorted();
    }

    void setWidth(final int width) {
        this.table.setWidth(width + "px");
    }

    void setHeight(final int height) {
        this.table.setHeight(height + "px");
    }

    void refresh(final SpreadsheetId id,
                 final SpreadsheetName name,
                 final SpreadsheetViewportWindows windows,
                 final Predicate<SpreadsheetSelection> selected) {
        Objects.requireNonNull(windows, "windows");

        final SpreadsheetViewportComponentTableContext context = this.context;

        if (false == id.equals(this.id) || false == name.equals(this.name)) {
            this.id = id;
            this.name = name;

            this.columnHeaders.setIdAndName(
                id,
                name
            );

            this.rowsToTableRowCells.values()
                .forEach(r -> r.setIdAndName(id, name));
        }

        final Set<SpreadsheetRowReference> rows = windows.rows();
        if (false == this.rows.equals(rows)) {
            this.rows = rows;

            // rows changed, create new rows
            final SpreadsheetTBodyComponent tbody = this.tbody;
            tbody.clear();

            this.columnHeaders.refresh(
                windows,
                selected,
                context
            );

            final Map<SpreadsheetRowReference, SpreadsheetViewportComponentTableRowCells> oldRowsToTableRowCells = this.rowsToTableRowCells;
            final Map<SpreadsheetRowReference, SpreadsheetViewportComponentTableRowCells> newRowsToTableRowCells = Maps.sorted();

            // create new rows as necessary
            for (final SpreadsheetRowReference row : rows) {
                SpreadsheetViewportComponentTableRowCells tableRowCells = oldRowsToTableRowCells.get(row);
                if (null == tableRowCells) {
                    tableRowCells = SpreadsheetViewportComponentTableRowCells.empty(
                        row,
                        context
                    );
                }
                newRowsToTableRowCells.put(
                    row,
                    tableRowCells
                );
                tbody.appendChild(tableRowCells);
            }

            this.rowsToTableRowCells = newRowsToTableRowCells;
        }

        final long started = System.currentTimeMillis();
        long ended = 0;

        final List<String> timings = Lists.array();
        {
            this.columnHeaders.refresh(
                windows,
                selected,
                context
            );
            timings.add(String.valueOf(System.currentTimeMillis() - started));
        }

        for (final SpreadsheetViewportComponentTableRowCells tableRowCells : this.rowsToTableRowCells.values()) {
            final long tableRowCellsStart = System.currentTimeMillis();

            tableRowCells.refresh(
                windows,
                selected,
                context
            );

            ended = System.currentTimeMillis();
            timings.add(String.valueOf(ended - tableRowCellsStart));
        }

        context.debug(this.getClass().getSimpleName() + ".refresh " + (ended - started) + "ms, row rendering timings: " + String.join(", ", timings));
    }

    private SpreadsheetId id;

    private SpreadsheetName name;

    private final SpreadsheetViewportComponentTableRowColumnHeaders columnHeaders;

    private Set<SpreadsheetRowReference> rows;

    private Map<SpreadsheetRowReference, SpreadsheetViewportComponentTableRowCells> rowsToTableRowCells;

    @Override
    public boolean isEditing() {
        return false;
    }

    @Override
    public SpreadsheetViewportComponentTable setCssText(final String css) {
        this.table.setCssText(css);
        return this;
    }

    @Override
    public SpreadsheetViewportComponentTable setCssProperty(final String name,
                                                            final String value) {
        this.table.setCssProperty(
            name,
            value
        );
        return this;
    }

    SpreadsheetViewportComponentTable addClickListener(final EventListener listener) {
        this.table.addClickListener(listener);
        return this;
    }

    SpreadsheetViewportComponentTable addContextMenuListener(final EventListener listener) {
        this.table.addContextMenuListener(listener);
        return this;
    }

    SpreadsheetViewportComponentTable addFocusInListener(final EventListener listener) {
        this.table.addFocusInListener(listener);
        return this;
    }

    SpreadsheetViewportComponentTable addFocusOutListener(final EventListener listener) {
        this.table.addFocusOutListener(listener);
        return this;
    }

    SpreadsheetViewportComponentTable addKeyDownListener(final EventListener listener) {
        this.table.addKeyDownListener(listener);
        return this;
    }

    SpreadsheetViewportComponentTable addKeyPressListener(final EventListener listener) {
        this.table.addKeyPressListener(listener);
        return this;
    }

    SpreadsheetViewportComponentTable addKeyUpListener(final EventListener listener) {
        this.table.addKeyUpListener(listener);
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLTableElement element() {
        return this.table.element();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        this.table.printTree(printer);
    }

    private final SpreadsheetTableComponent table;

    private final SpreadsheetTBodyComponent tbody;

    private final SpreadsheetViewportComponentTableContext context;
}
