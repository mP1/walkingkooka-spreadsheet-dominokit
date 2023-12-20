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

import walkingkooka.ToStringBuilder;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetColumn;
import walkingkooka.spreadsheet.SpreadsheetRow;
import walkingkooka.spreadsheet.SpreadsheetViewportWindows;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.net.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A cache of the cells and labels for a viewport. This is mostly used during the rendering phase to provide content
 * for the cells in the spreadsheet viewport TABLE.
 */
public final class SpreadsheetViewportCache implements NopFetcherWatcher,
        SpreadsheetDeltaFetcherWatcher,
        SpreadsheetMetadataFetcherWatcher {

    /**
     * Creates a new cache with no cells or labels present.
     */
    public static SpreadsheetViewportCache empty() {
        return new SpreadsheetViewportCache();
    }

    /**
     * Private ctor use static factory
     */
    private SpreadsheetViewportCache() {
        super();
    }

    /**
     * Clearing all caches.
     */
    public void clear() {
        this.cells.clear();

        this.labelMappings.clear();
        this.cellToLabels.clear();
        this.labelToNonLabel.clear();

        this.columns.clear();
        this.rows.clear();

        this.columnWidths.clear();
        this.rowHeights.clear();

        this.columnCount = OptionalInt.empty();
        this.rowCount = OptionalInt.empty();

        this.windows = SpreadsheetViewportWindows.EMPTY;
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
        final Map<SpreadsheetLabelName, SpreadsheetSelection> labelToNonLabel = this.labelToNonLabel;

        final Map<SpreadsheetColumnReference, SpreadsheetColumn> columns = this.columns;
        final Map<SpreadsheetRowReference, SpreadsheetRow> rows = this.rows;

        final Map<SpreadsheetColumnReference, Length<?>> columnWidths = this.columnWidths;
        final Map<SpreadsheetRowReference, Length<?>> rowHeights = this.rowHeights;

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

        this.labelMappings = delta.labels();

        SpreadsheetViewportCacheUpdatingSpreadsheetSelectionVisitor.accept(
                delta.labels(),
                cellToLabels,
                labelToNonLabel,
                this.windows
        );

        final OptionalInt columnCount = delta.columnCount();
        if (columnCount.isPresent()) {
            this.columnCount = columnCount;
        }

        final OptionalInt rowCount = delta.rowCount();
        if (rowCount.isPresent()) {
            this.rowCount = rowCount;
        }
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

    /**
     * Returns all {@link SpreadsheetLabelMapping} within the active window.
     */
    public Set<SpreadsheetLabelMapping> labelMappings() {
        return this.labelMappings;
    }

    private Set<SpreadsheetLabelMapping> labelMappings = Sets.empty();

    Set<SpreadsheetLabelName> labels(final SpreadsheetCellReference cell) {
        return this.cellToLabels.getOrDefault(
                cell,
                Sets.empty()
        );
    }

    /**
     * Returns all {@link SpreadsheetLabelMapping} for the given {@link SpreadsheetSelection}.
     * <br>
     * This will be useful for creating a context menu item holding all the labels for current selection.
     */
    public Set<SpreadsheetLabelMapping> labelMappings(final SpreadsheetSelection selection) {
        return this.nonLabelSelection(selection)
                .map(this::labelMappings0)
                .orElse(Sets.empty());
    }

    private Set<SpreadsheetLabelMapping> labelMappings0(final SpreadsheetSelection selection) {
        return this.labelMappings()
                .stream()
                .filter(
                        m -> this.nonLabelSelection(
                                        m.target()
                                ).map(s -> s.test(selection))
                                .orElse(false)
                )
                .collect(Collectors.toCollection(Sets::sorted));
    }

    /**
     * Attempts to resolve any labels to a non label {@link SpreadsheetSelection}.
     * This is useful when trying to show selected cells for a label.
     */
    public Optional<SpreadsheetSelection> nonLabelSelection(final SpreadsheetSelection selection) {
        Objects.requireNonNull(selection, "selection");

        SpreadsheetSelection nonLabel = selection;

        if (selection.isLabelName()) {
            nonLabel = this.labelToNonLabel.get((SpreadsheetLabelName) selection);
        }

        return Optional.ofNullable(nonLabel);
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
     * A cache of {@link SpreadsheetLabelMapping} expanded to {@link SpreadsheetLabelName} to its {@link walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference}.
     */
    final Map<SpreadsheetLabelName, SpreadsheetSelection> labelToNonLabel = Maps.sorted();

    /**
     * A cache of rows, this is used mostly to track hidden rows.
     */
    // VisibleForTesting
    final Map<SpreadsheetRowReference, SpreadsheetRow> rows = Maps.sorted();

    /**
     * A cache holding the max height for interesting rows. If the row is hidden it will have a height of zero.
     */
    // VisibleForTesting
    final Map<SpreadsheetRowReference, Length<?>> rowHeights = Maps.sorted();

    public OptionalInt columnCount() {
        return this.columnCount;
    }

    private OptionalInt columnCount = OptionalInt.empty();

    public OptionalInt rowCount() {
        return this.rowCount;
    }

    private OptionalInt rowCount = OptionalInt.empty();

    /**
     * Sets a new {@link SpreadsheetViewportWindows}.
     */
    public void setWindows(final SpreadsheetViewportWindows windows) {
        Objects.requireNonNull(windows, "windows");

        if (windows.isEmpty()) {
            if (false == this.windows.equals(windows)) {
                // no window clear caches
                this.cells.clear();

                this.labelMappings.clear();
                this.cellToLabels.clear();
                this.labelToNonLabel.clear();

                this.columns.clear();
                this.rows.clear();

                this.columnWidths.clear();
                this.rowHeights.clear();
            }
        }

        this.windows = windows;
    }

    /**
     * The viewport window.
     */
    public SpreadsheetViewportWindows windows() {
        return this.windows;
    }

    /**
     * The viewport. This is used to filter cells and labels in the cache.
     */
    private SpreadsheetViewportWindows windows = SpreadsheetViewportWindows.EMPTY;

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .value(this.cells)
                .value(this.columns)
                .value(this.columnWidths)
                .value(this.cellToLabels)
                .value(this.labelMappings)
                .value(this.rows)
                .value(this.rowHeights)
                .value(this.windows)
                .build();
    }
}
