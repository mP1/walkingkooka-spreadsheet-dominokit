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

package walkingkooka.spreadsheet.dominokit.ui.viewport;

import elemental2.dom.HTMLTableElement;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.elements.TBodyElement;
import org.dominokit.domino.ui.elements.THeadElement;
import org.dominokit.domino.ui.elements.TableElement;
import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.spreadsheet.SpreadsheetViewportWindows;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

final class SpreadsheetViewportComponentTable implements IsElement<HTMLTableElement> {

    static SpreadsheetViewportComponentTable empty() {
        return new SpreadsheetViewportComponentTable();
    }

    private SpreadsheetViewportComponentTable() {
        final TableElement table = ElementsFactory.elements.table()
                .id(SpreadsheetViewportComponent.ID)
                .setOverFlow("hidden");
//        tableElement.style("width: 100%; height: 100%; overflow-x: hidden; overflow-y: hidden;");

        // overflow: hidden
//        table.setOverFlow("hidden");

        this.columnHeaders = SpreadsheetViewportComponentTableRowColumnHeaders.empty();

        final THeadElement thead = ElementsFactory.elements.thead();
        thead.appendChild(this.columnHeaders);
        table.appendChild(thead);

        final TBodyElement tbody = ElementsFactory.elements.tbody();
        table.appendChild(tbody);

        this.table = table;
        this.thead = thead;
        this.tbody = tbody;

        this.rows = Sets.sorted();
        this.rowsToTableRowCells = Maps.sorted();
    }

    void setWidth(final int width) {
        this.table.setWidth(width + "px");
    }

    void setHeight(final int height) {
        this.table.setHeight(height + "px");
    }

    void refresh(final SpreadsheetViewportWindows windows,
                 final Predicate<SpreadsheetSelection> selected,
                 final SpreadsheetViewportComponentTableContext context) {
        Objects.requireNonNull(windows, "windows");

        final Set<SpreadsheetRowReference> rows = windows.rows();
        if (false == this.rows.equals(rows)) {
            this.rows = rows;

            // rows changed, create new rows
            final TBodyElement tbody = this.tbody;
            tbody.clearElement();

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

        this.columnHeaders.refresh(
                windows,
                selected,
                context
        );

        for (final SpreadsheetViewportComponentTableRowCells tableRowCells : this.rowsToTableRowCells.values()) {
            tableRowCells.refresh(
                    windows,
                    selected,
                    context
            );
        }
    }

    private final SpreadsheetViewportComponentTableRowColumnHeaders columnHeaders;

    private Set<SpreadsheetRowReference> rows;

    private Map<SpreadsheetRowReference, SpreadsheetViewportComponentTableRowCells> rowsToTableRowCells;

    // IsElement........................................................................................................

    @Override
    public HTMLTableElement element() {
        return this.table.element();
    }

    private final TableElement table;

    private final THeadElement thead;

    private final TBodyElement tbody;
}
