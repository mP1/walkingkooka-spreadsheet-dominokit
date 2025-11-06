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

import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.dom.TrComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelectionMaps;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportWindows;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A single TR with the first column holding the ROW and the remaining TD's the individual cells.
 */
final class SpreadsheetViewportComponentTableRowCells extends SpreadsheetViewportComponentTableRow<SpreadsheetViewportComponentTableRowCells> {

    static SpreadsheetViewportComponentTableRowCells empty(final SpreadsheetRowReference row,
                                                           final SpreadsheetViewportComponentTableContext context) {
        return new SpreadsheetViewportComponentTableRowCells(
            row,
            context
        );
    }

    private SpreadsheetViewportComponentTableRowCells(final SpreadsheetRowReference row,
                                                      final SpreadsheetViewportComponentTableContext context) {
        this.rowHeader = SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow.empty(
            row,
            context
        );
        this.columnToCells = SpreadsheetSelectionMaps.column();
    }

    @Override
    void setIdAndName(final SpreadsheetId id,
                      final SpreadsheetName name) {
        this.rowHeader.setIdAndName(id, name);
        this.columnToCells.values()
            .forEach(c -> c.setIdAndName(id, name));
    }

    @Override
    void refresh(final SpreadsheetViewportWindows windows,
                 final Predicate<SpreadsheetSelection> selected,
                 final SpreadsheetViewportComponentTableContext context) {
        final Set<SpreadsheetColumnReference> columns = windows.columns();
        final boolean shouldShowHeaders = context.shouldShowHeaders();
        final TrComponent tr = this.tr;
        final SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow rowHeader = this.rowHeader;

        if (false == columns.equals(this.columns) || tr.contains(rowHeader) != shouldShowHeaders) {
            this.columns = columns;

            final Map<SpreadsheetColumnReference, SpreadsheetViewportComponentTableCellSpreadsheetCell> oldColumnToCells = this.columnToCells;
            final Map<SpreadsheetColumnReference, SpreadsheetViewportComponentTableCellSpreadsheetCell> newColumnToCells = SpreadsheetSelectionMaps.column();

            tr.clear();

            if(shouldShowHeaders) {
                tr.appendChild(this.rowHeader);
            }

            final SpreadsheetRowReference row = rowHeader.selection;

            double rowWidth = context.viewportGridWidth();

            // create the cells as necessary for this row...
            for (final SpreadsheetColumnReference column : columns) {
                SpreadsheetViewportComponentTableCellSpreadsheetCell td = oldColumnToCells.get(column);
                if (null == td) {
                    td = SpreadsheetViewportComponentTableCellSpreadsheetCell.empty(
                        column.setRow(row),
                        context
                    );
                }
                newColumnToCells.put(column, td);
                tr.appendChild(td);

                rowWidth = rowWidth - td.width(context)
                    .pixelValue();
                if(rowWidth <= 0) {
                    break;
                }
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

    private final SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow rowHeader;

    private Map<SpreadsheetColumnReference, SpreadsheetViewportComponentTableCellSpreadsheetCell> columnToCells;
}
