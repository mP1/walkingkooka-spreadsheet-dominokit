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

import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;

/**
 * A TH holding a single COLUMN header. Note new instances are created when a new spreadsheet is created/loaded.
 */
final class SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn extends SpreadsheetViewportComponentTableCellHeader<SpreadsheetColumnReference> {

    static SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn empty(final SpreadsheetColumnReference column,
                                                                              final SpreadsheetViewportComponentTableContext context) {
        return new SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn(
                column,
                context
        );
    }

    private SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn(final SpreadsheetColumnReference column,
                                                                         final SpreadsheetViewportComponentTableContext context) {
        super(
                SpreadsheetViewportComponent.id(column), // id
                css(
                        SpreadsheetViewportComponentTableCell.HEADER_STYLE,
                        context.spreadsheetViewportCache()
                                .columnWidth(column),
                        SpreadsheetViewportComponent.COLUMN_HEIGHT
                ), // css
                column, // selection
                column.toString()
                        .toUpperCase(), // link text
                context
        );
    }

    @Override
    void refreshNonExtendLink(final SpreadsheetViewportComponentTableContext context) {
        this.setAnchoredSpreadsheetSelection(
                this.selection.setDefaultAnchor()
        );
    }

    @Override
    void refreshExtendLink(final SpreadsheetViewportComponentTableContext context) {
        this.setAnchoredSpreadsheetSelection(
                context.extendColumn(this.selection)
        );
    }
}
