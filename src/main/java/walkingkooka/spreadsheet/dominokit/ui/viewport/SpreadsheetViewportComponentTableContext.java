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

import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.tree.text.TextStyle;

/**
 * A {@link walkingkooka.Context} required by table components to refresh themselves.
 */
interface SpreadsheetViewportComponentTableContext extends HistoryTokenContext,
        LoggingContext,
        HasSpreadsheetViewportCache {

    /**
     * When true indicates that hide zero values is active, and cells should add extra styling to show this.
     */
    boolean hideZeroValues();

    /**
     * The default {@link TextStyle} for a cell.
     */
    TextStyle defaultCellStyle();

    /**
     * When true a {@link SpreadsheetViewportComponentTableCell} must refresh.
     */
    boolean mustRefresh();


    /**
     * When true indicates that the SHIFT key is currently DOWN.
     * This will be useful to update the links within the COLUMN and ROW headers.
     */
    boolean isShiftKeyDown();

    /**
     * Returns a {@link AnchoredSpreadsheetSelection} that extends the current selection to include this column.
     */
    AnchoredSpreadsheetSelection extendColumn(final SpreadsheetColumnReference column);

    /**
     * Returns a {@link HistoryToken} that extends the current selection to include this row.
     */
    AnchoredSpreadsheetSelection extendRow(final SpreadsheetRowReference row);
}
