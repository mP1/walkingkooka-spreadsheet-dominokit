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

import elemental2.dom.HTMLTableRowElement;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.elements.TableRowElement;
import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.collect.map.Maps;
import walkingkooka.spreadsheet.SpreadsheetViewportWindows;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

final class SpreadsheetViewportComponentTableRowCells extends SpreadsheetViewportComponentTableRow implements IsElement<HTMLTableRowElement> {

    static SpreadsheetViewportComponentTableRowCells empty(final SpreadsheetRowReference row,
                                                           final SpreadsheetViewportComponentTableContext context) {
        return new SpreadsheetViewportComponentTableRowCells(
                row,
                context
        );
    }

    private SpreadsheetViewportComponentTableRowCells(final SpreadsheetRowReference row,
                                                      final SpreadsheetViewportComponentTableContext context) {
        this.element = ElementsFactory.elements.tr();
        this.rowHeader = SpreadsheetViewportComponentTableCellSpreadsheetRowHeader.empty(
                row,
                context
        );
        this.columnToCells = Maps.sorted();
    }

    @Override
    void refresh(final SpreadsheetViewportWindows windows,
                 final Predicate<SpreadsheetSelection> selected,
                 final SpreadsheetViewportComponentTableContext context) {
        final Set<SpreadsheetColumnReference> columns = windows.columns();

        if (false == columns.equals(this.columns)) {
            this.columns = columns;

            final Map<SpreadsheetColumnReference, SpreadsheetViewportComponentTableCellSpreadsheetCell> oldColumnToCells = this.columnToCells;
            final Map<SpreadsheetColumnReference, SpreadsheetViewportComponentTableCellSpreadsheetCell> newColumnToCells = Maps.sorted();

            final TableRowElement element = this.element;
            element.clearElement();

            element.appendChild(this.rowHeader);

            final SpreadsheetRowReference row = this.rowHeader.row;

            // create the cells as necessary for this row...
            for (final SpreadsheetColumnReference column : columns) {
                SpreadsheetViewportComponentTableCellSpreadsheetCell columnTableCell = oldColumnToCells.get(column);
                if (null == columnTableCell) {
                    columnTableCell = SpreadsheetViewportComponentTableCellSpreadsheetCell.empty(
                            column.setRow(row),
                            context
                    );
                }
                ;
                newColumnToCells.put(column, columnTableCell);
                element.appendChild(columnTableCell);
            }

            this.columnToCells = newColumnToCells;
        }

        this.rowHeader.refresh(
                selected,
                context
        );

        // refresh cells & selection here
        this.columnToCells.values()
                .forEach(c -> c.refresh(
                                selected,
                                context
                        )
                );
    }

    private Set<SpreadsheetColumnReference> columns;

    private final SpreadsheetViewportComponentTableCellSpreadsheetRowHeader rowHeader;

    private Map<SpreadsheetColumnReference, SpreadsheetViewportComponentTableCellSpreadsheetCell> columnToCells;

    // IsElement........................................................................................................

    @Override
    public HTMLTableRowElement element() {
        return this.element.element();
    }

    private final TableRowElement element;
}
