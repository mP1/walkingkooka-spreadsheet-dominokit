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

import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStyle;

/**
 * A Header that contains the SELECT ALL link. Note new instances are created when a new spreadsheet is created/loaded.
 */
final class SpreadsheetViewportComponentTableCellHeaderSelectAll extends SpreadsheetViewportComponentTableCellHeader<SpreadsheetCellRangeReference, SpreadsheetViewportComponentTableCellHeaderSelectAll> {

    static SpreadsheetViewportComponentTableCellHeaderSelectAll empty(final SpreadsheetViewportComponentTableContext context) {
        return new SpreadsheetViewportComponentTableCellHeaderSelectAll(context);
    }

    private SpreadsheetViewportComponentTableCellHeaderSelectAll(final SpreadsheetViewportComponentTableContext context) {
        super(
            SpreadsheetViewportComponent.ID_PREFIX + "select-all-cells", // id// css
            SpreadsheetSelection.ALL_CELLS, // selection
            "All", // link text
            context
        );
    }

    @Override //
    Length<?> width(final SpreadsheetViewportComponentTableContext context) {
        return SpreadsheetViewportContext.ROW_HEADER_WIDTH;
    }

    @Override //
    Length<?> height(final SpreadsheetViewportComponentTableContext context) {
        return SpreadsheetViewportContext.COLUMN_HEADER_HEIGHT;
    }

    @Override//
    TextStyle selectedTextStyle(final SpreadsheetViewportComponentTableContext context) {
        return context.selectedAllCellsStyle();
    }

    @Override //
    TextStyle unselectedTextStyle(final SpreadsheetViewportComponentTableContext context) {
        return context.allCellsStyle();
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
