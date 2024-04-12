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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.SpreadsheetViewportWindows;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A TR holding the select-all and column headers.
 */
final class SpreadsheetViewportComponentTableRowColumnHeaders extends SpreadsheetViewportComponentTableRow implements IsElement<HTMLTableRowElement> {

    static SpreadsheetViewportComponentTableRowColumnHeaders empty(final HistoryTokenContext context) {
        return new SpreadsheetViewportComponentTableRowColumnHeaders(context);
    }

    private SpreadsheetViewportComponentTableRowColumnHeaders(final HistoryTokenContext context) {
        this.element = ElementsFactory.elements.tr();

        this.selectAll = SpreadsheetViewportComponentTableCellHeaderSelectAll.empty(context);
        this.columns = null;
        this.columnToHeaders = Maps.sorted();

        this.element.appendChild(this.selectAll);
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

            final TableRowElement element = this.element;
            element.clearElement();

            element.appendChild(this.selectAll);

            final Map<SpreadsheetColumnReference, SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn> oldColumnToHeaders = this.columnToHeaders;
            final Map<SpreadsheetColumnReference, SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn> newColumnToHeaders = Maps.sorted();

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

    // IsElement........................................................................................................

    @Override
    public HTMLTableRowElement element() {
        return this.element.element();
    }

    private final TableRowElement element;
}
