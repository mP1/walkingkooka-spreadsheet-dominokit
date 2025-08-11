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

import walkingkooka.Context;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStyle;

import java.util.Objects;

/**
 * A {@link Context} that mostly contains styles for a viewport.
 */
public interface SpreadsheetViewportContext extends Context {

    /**
     * The {@link TextStyle} for the top/left select all box.
     */
    TextStyle allCellsStyle();

    /**
     * The {@link TextStyle} for the top/left select all box when selected.
     */
    TextStyle selectedAllCellsStyle();

    /**
     * The {@link TextStyle} for an unselected cell.
     */
    TextStyle cellStyle();

    /**
     * The {@link TextStyle} for a selected cell.
     */
    TextStyle selectedCellStyle(final TextStyle cellStyle);

    /**
     * The {@link TextStyle} for a unselected column.
     */
    TextStyle columnStyle();

    /**
     * The {@link TextStyle} for a selected column.
     */
    TextStyle selectedColumnStyle();

    /**
     * The {@link TextStyle} for a unselected row.
     */
    TextStyle rowStyle();

    /**
     * The {@link TextStyle} for a selected row.
     */
    TextStyle selectedRowStyle();

    /**
     * Picks the {@link TextStyle} given the {@link SpreadsheetSelection} type.
     */
    default TextStyle selectionStyle(final SpreadsheetSelection selection) {
        Objects.requireNonNull(selection, "selection");

        return selection.isExternalReference() ?
            this.cellStyle() :
            selection.isColumnOrColumnRange() ?
                this.columnStyle() :
                selection.isRowOrRowRange() ?
                    this.rowStyle() :
                    unknownSelection(selection);
    }

    /**
     * Picks the selected {@link TextStyle} given the {@link SpreadsheetSelection} type.
     */
    default TextStyle selectedSelectionStyle(final SpreadsheetSelection selection,
                                             final TextStyle cellStyle) {
        Objects.requireNonNull(selection, "selection");

        return selection.isExternalReference() ?
            this.selectedCellStyle(cellStyle) :
            selection.isColumnOrColumnRange() ?
                this.selectedColumnStyle() :
                selection.isRowOrRowRange() ?
                    this.selectedRowStyle() :
                    unknownSelection(selection);
    }

    private TextStyle unknownSelection(final SpreadsheetSelection selection) {
        throw new IllegalArgumentException("Invalid selection " + selection);
    }

    /**
     * Add some styling to indicate a hide zero.
     */
    TextStyle hideZeroStyle(final TextStyle style);
}
