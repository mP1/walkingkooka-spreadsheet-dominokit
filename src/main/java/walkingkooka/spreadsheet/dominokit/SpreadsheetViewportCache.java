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

package walkingkooka.spreadsheet.dominokit;

import walkingkooka.ToStringBuilder;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetColumn;
import walkingkooka.spreadsheet.SpreadsheetRow;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A cache of the cells and labels for a viewport. This is mostly used during the rendering phase to provide content
 * for the cells in the spreadsheet viewport TABLE.
 */
final class SpreadsheetViewportCache implements SpreadsheetDeltaWatcher, SpreadsheetMetadataWatcher {

    /**
     * Creates a new cache with no cells or labels present.
     */
    static SpreadsheetViewportCache empty() {
        return new SpreadsheetViewportCache();
    }

    /**
     * Private ctor use static factory
     */
    private SpreadsheetViewportCache() {
        super();
    }

    /**
     * Captures the default width and height which will be used when rendering
     */
    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        this.defaultWidth = metadata.getEffectiveStylePropertyOrFail(TextStylePropertyName.WIDTH);
        this.defaultHeight = metadata.getEffectiveStylePropertyOrFail(TextStylePropertyName.HEIGHT);
    }

    // @VisibleForTesting
    Length<?> defaultWidth;

    // @VisibleForTesting
    Length<?> defaultHeight;

    /**
     * Removes any deleted cells and then adds updated cells to the {@link #cells}.
     */
    @Override
    public void onSpreadsheetDelta(final SpreadsheetDelta delta,
                                   final AppContext context) {
        final Map<SpreadsheetCellReference, SpreadsheetCell> cells = this.cells;

        final Map<SpreadsheetCellReference, Set<SpreadsheetLabelName>> cellToLabels = this.cellToLabels;

        final Map<SpreadsheetColumnReference, SpreadsheetColumn> columns = this.columns;
        final Map<SpreadsheetRowReference, SpreadsheetRow> rows = this.rows;

        final Map<SpreadsheetColumnReference, Length<?>> columnsWidths = this.columnWidths;
        final Map<SpreadsheetRowReference, Length<?>> rowHeights = this.rowHeights;

        final Set<SpreadsheetCellRange> windows = delta.window();
        if (false == this.windows.equals(windows)) {
            // no window clear caches
            cells.clear();

            cellToLabels.clear();

            columns.clear();
            rows.clear();

            columnsWidths.clear();
            rowHeights.clear();
        }

        for (final SpreadsheetCellReference cell : delta.deletedCells()) {
            cells.remove(cell);
            cellToLabels.remove(cell);
        }

        for (final SpreadsheetCell cell : delta.cells()) {
            final SpreadsheetCellReference cellReference = cell.reference();
            cells.put(
                    cellReference,
                    cell
            );
            cellToLabels.remove(cellReference);
        }

        for (final SpreadsheetColumnReference column : delta.deletedColumns()) {
            columns.remove(column);
            columnWidths.remove(column);
        }

        for (final SpreadsheetColumn column : delta.columns()) {
            columns.put(
                    column.reference(),
                    column
            );
        }

        for (final Entry<SpreadsheetColumnReference, Double> width : delta.columnWidths().entrySet()) {
            columnWidths.put(
                    width.getKey(),
                    Length.pixel(width.getValue())
            );
        }
        for (final SpreadsheetRowReference row : delta.deletedRows()) {
            rows.remove(row);
            rowHeights.remove(row);
        }

        for (final SpreadsheetRow row : delta.rows()) {
            rows.put(
                    row.reference(),
                    row
            );
        }

        for (final Entry<SpreadsheetRowReference, Double> height : delta.rowHeights().entrySet()) {
            rowHeights.put(
                    height.getKey(),
                    Length.pixel(height.getValue())
            );
        }

        final Set<SpreadsheetLabelMapping> labelMappings = delta.labels();

        final SpreadsheetViewportCacheUpdatingSpreadsheetSelectionVisitor labelUpdater = SpreadsheetViewportCacheUpdatingSpreadsheetSelectionVisitor.with(
                cellToLabels,
                windows
        );
        for (final SpreadsheetLabelMapping mapping : labelMappings) {
            labelUpdater.accept(mapping);
        }

        this.windows = windows;
    }

    Optional<SpreadsheetCell> cell(final SpreadsheetCellReference cell) {
        return Optional.ofNullable(this.cells.get(cell));
    }

    Optional<SpreadsheetColumn> column(final SpreadsheetColumnReference column) {
        return Optional.ofNullable(this.columns.get(column));
    }

    /**
     * Retrieves the width for the given {@link SpreadsheetColumnReference} using the default if none is available.
     */
    Length<?> columnWidth(final SpreadsheetColumnReference column) {
        Objects.requireNonNull(column, "column");

        Length<?> width = this.columnWidths.get(column);
        if (null == width) {
            width = this.defaultWidth;
        }
        return width;
    }

    Set<SpreadsheetLabelName> labels(final SpreadsheetCellReference cell) {
        return this.cellToLabels.getOrDefault(
                cell,
                Sets.empty()
        );
    }

    Optional<SpreadsheetRow> row(final SpreadsheetRowReference row) {
        return Optional.ofNullable(this.rows.get(row));
    }

    /**
     * Retrieves the height for the given {@link SpreadsheetRowReference} using the default if none is available.
     */
    Length<?> rowHeight(final SpreadsheetRowReference row) {
        Objects.requireNonNull(row, "row");

        Length<?> height = this.rowHeights.get(row);
        if (null == height) {
            height = this.defaultHeight;
        }
        return height;
    }

    /**
     * Returns true only if the column is present and hidden.
     */
    boolean isColumnHidden(final SpreadsheetColumnReference column) {
        Objects.requireNonNull(column, "column");

        final SpreadsheetColumn spreadsheetColumn = this.columns.get(column);
        return null != spreadsheetColumn && spreadsheetColumn.hidden();
    }

    /**
     * Returns true only if the row is present and hidden.
     */
    boolean isRowHidden(final SpreadsheetRowReference row) {
        Objects.requireNonNull(row, "row");

        final SpreadsheetRow spreadsheetRow = this.rows.get(row);
        return null != spreadsheetRow && spreadsheetRow.hidden();
    }

    /**
     * A cache of cells, this allows partial updates such as a single cell and still be able to render a complete viewport.
     */
    // VisibleForTesting
    final Map<SpreadsheetCellReference, SpreadsheetCell> cells = Maps.sorted();

    /**
     * A cache of columns, this is used mostly to track hidden columns.
     */
    // VisibleForTesting
    final Map<SpreadsheetColumnReference, SpreadsheetColumn> columns = Maps.sorted();

    /**
     * A cache holding the max width for interesting columns. If the column is hidden it will have a width of zero.
     */
    // VisibleForTesting
    final Map<SpreadsheetColumnReference, Length<?>> columnWidths = Maps.sorted();

    /**
     * A cache of cell references and their one or more labels.
     */
    // VisibleForTesting
    final Map<SpreadsheetCellReference, Set<SpreadsheetLabelName>> cellToLabels = Maps.sorted();

    /**
     * A cache of rows, this is used mostly to track hidden rowss.
     */
    // VisibleForTesting
    final Map<SpreadsheetRowReference, SpreadsheetRow> rows = Maps.sorted();

    /**
     * A cache holding the max height for interesting rows. If the row is hidden it will have a height of zero.
     */
    // VisibleForTesting
    final Map<SpreadsheetRowReference, Length<?>> rowHeights = Maps.sorted();

    /**
     * The viewport. This is used to filter cells and labels in the cache.
     */
    // VisibleForTesting
    Set<SpreadsheetCellRange> windows = Sets.empty();

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .value(this.cells)
                .value(this.columns)
                .value(this.columnWidths)
                .value(this.cellToLabels)
                .value(this.rows)
                .value(this.rowHeights)
                .value(this.windows)
                .build();
    }
}
