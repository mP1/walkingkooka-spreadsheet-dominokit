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

import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

/**
 * A Header that contains the SELECT ALL link. Note new instances are created when a new spreadsheet is created/loaded.
 */
final class SpreadsheetViewportComponentTableCellHeaderSelectAll extends SpreadsheetViewportComponentTableCellHeader<SpreadsheetCellRangeReference> {

    static SpreadsheetViewportComponentTableCellHeaderSelectAll empty(final HistoryTokenContext context) {
        return new SpreadsheetViewportComponentTableCellHeaderSelectAll(context);
    }

    private SpreadsheetViewportComponentTableCellHeaderSelectAll(final HistoryTokenContext context) {
        super(
                SpreadsheetViewportComponent.ID_PREFIX + "select-all-cells", // id
                css(
                        SpreadsheetViewportComponentTableCell.HEADER_STYLE,
                        SpreadsheetViewportComponent.ROW_WIDTH,
                        SpreadsheetViewportComponent.COLUMN_HEIGHT
                ), // css
                SpreadsheetSelection.ALL_CELLS, // selection
                "All", // link text
                context
        );
    }

    @Override
    void refreshNonExtendLink(final SpreadsheetViewportComponentTableContext context) {
        // nop
    }

    @Override
    void refreshExtendLink(final SpreadsheetViewportComponentTableContext context) {
        // nop
    }
}
