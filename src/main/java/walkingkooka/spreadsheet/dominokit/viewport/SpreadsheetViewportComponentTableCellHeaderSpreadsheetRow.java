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

import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStyle;

/**
 * A TH that contains a ROW header. Note new instances are created when a new spreadsheet is created/loaded.
 */
final class SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow extends SpreadsheetViewportComponentTableCellHeader<SpreadsheetRowReference, SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow> {

    static SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow empty(final SpreadsheetRowReference row,
                                                                           final SpreadsheetViewportComponentTableContext context) {
        return new SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow(
            row,
            context
        );
    }

    private SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow(final SpreadsheetRowReference row,
                                                                      final SpreadsheetViewportComponentTableContext context) {
        super(
            SpreadsheetViewportComponent.id(row), // id
            row, // selection
            row.toString()
                .toUpperCase(), // link text
            context
        );
    }

    @Override //
    Length<?> width(final SpreadsheetViewportComponentTableContext context) {
        return SpreadsheetViewportContext.ROW_HEADER_WIDTH;
    }

    @Override //
    Length<?> height(final SpreadsheetViewportComponentTableContext context) {
        return context.spreadsheetViewportCache()
            .rowHeight(this.selection);
    }

    @Override//
    TextStyle selectedTextStyle(final SpreadsheetViewportComponentTableContext context) {
        return context.selectedRowStyle();
    }

    @Override //
    TextStyle unselectedTextStyle(final SpreadsheetViewportComponentTableContext context) {
        return context.rowStyle();
    }

    @Override //
    void refreshNonExtendLink(final SpreadsheetViewportComponentTableContext context) {
        this.setAnchoredSpreadsheetSelection(
            this.selection.setDefaultAnchor()
        );
    }

    @Override
    void refreshExtendLink(final SpreadsheetViewportComponentTableContext context) {
        this.setAnchoredSpreadsheetSelection(
            context.extendRow(this.selection)
        );
    }
}
