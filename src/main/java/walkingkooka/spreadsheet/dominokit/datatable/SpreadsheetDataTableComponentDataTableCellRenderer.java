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

import elemental2.dom.Node;
import org.dominokit.domino.ui.datatable.CellRenderer;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;

/**
 * A {@link CellRenderer} that acts as a bridge to a function which returns a {@link HtmlComponent}.
 */
final class SpreadsheetDataTableComponentDataTableCellRenderer<T> implements CellRenderer<T> {

    static <T> SpreadsheetDataTableComponentDataTableCellRenderer<T> with(final int columnNumber,
                                                                          final SpreadsheetDataTableComponentCellRenderer<T> cellRenderer) {
        return new SpreadsheetDataTableComponentDataTableCellRenderer<>(
            columnNumber,
            cellRenderer
        );
    }

    private SpreadsheetDataTableComponentDataTableCellRenderer(final int columnNumber,
                                                               final SpreadsheetDataTableComponentCellRenderer<T> cellRenderer) {
        this.columnNumber = columnNumber;
        this.cellRenderer = cellRenderer;
    }

    @Override
    public Node asElement(final CellInfo<T> cellInfo) {
        return this.cellRenderer.render(
            this.columnNumber,
            cellInfo.getRecord()
        ).node();
    }

    private final Integer columnNumber;

    /**
     * Provides the source {@link HtmlComponent} which will appear in the requested column.
     */
    private final SpreadsheetDataTableComponentCellRenderer<T> cellRenderer;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return "column: " + this.columnNumber;
    }
}
