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

import walkingkooka.collect.map.Maps;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.dom.TrComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportWindows;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A TR holding the select-all and column headers.
 */
final class SpreadsheetViewportComponentTableRowColumnHeaders extends SpreadsheetViewportComponentTableRow<SpreadsheetViewportComponentTableRowColumnHeaders> {

    static SpreadsheetViewportComponentTableRowColumnHeaders empty(final SpreadsheetViewportComponentTableContext context) {
        return new SpreadsheetViewportComponentTableRowColumnHeaders(context);
    }

    private SpreadsheetViewportComponentTableRowColumnHeaders(final SpreadsheetViewportComponentTableContext context) {
        this.selectAll = SpreadsheetViewportComponentTableCellHeaderSelectAll.empty(context);
        this.columns = null;
        this.columnToHeaders = Maps.sorted();

        this.tr.appendChild(this.selectAll);
    }

    @Override
    void setIdAndName(final SpreadsheetId id,
                      final SpreadsheetName name) {
        this.selectAll.setIdAndName(id, name);
        this.columnToHeaders.values()
            .forEach(c -> c.setIdAndName(id, name));
    }

    @Override
    void refresh(final SpreadsheetViewportWindows windows,
                 final Predicate<SpreadsheetSelection> selected,
                 final SpreadsheetViewportComponentTableContext context) {
        final Set<SpreadsheetColumnReference> columns = windows.columns();

        if (false == columns.equals(this.columns)) {
            this.columns = columns;

            final TrComponent element = this.tr;
            element.clear();

            element.appendChild(this.selectAll);

            final Map<SpreadsheetColumnReference, SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn> oldColumnToHeaders = this.columnToHeaders;
            final Map<SpreadsheetColumnReference, SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn> newColumnToHeaders = Maps.sorted();

            double rowWidth = context.viewportGridWidth();
            if(context.shouldShowHeaders()) {
                rowWidth = rowWidth - SpreadsheetViewportContext.ROW_HEADER_WIDTH_PIXELS;
            }

            // create new column headers as necessary
            for (final SpreadsheetColumnReference column : columns) {
                SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn columnTableCell = oldColumnToHeaders.get(column);
                if (null == columnTableCell) {
                    columnTableCell = SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn.empty(
                        column,
                        context
                    );
                }
                newColumnToHeaders.put(column, columnTableCell);
                element.appendChild(columnTableCell);

                rowWidth = rowWidth - columnTableCell.width(context)
                    .pixelValue();

                if(rowWidth <= 0) {
                    break; // stop rendering invisible columns
                }
            }

            this.columnToHeaders = newColumnToHeaders;
        }

        this.selectAll.refresh(
            selected,
            context
        );

        // column headers will clear/selection
        this.columnToHeaders.values()
            .forEach(
                c -> c.refresh(
                    selected,
                    context
                )
            );
    }

    private final SpreadsheetViewportComponentTableCellHeaderSelectAll selectAll;

    private Set<SpreadsheetColumnReference> columns;

    private Map<SpreadsheetColumnReference, SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn> columnToHeaders;
}
