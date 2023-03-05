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
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * A cache of the cells and labels for a viewport. This is mostly used during the rendering phase to provide content
 * for the cells in the spreadsheet viewport TABLE.
 */
final class SpreadsheetViewportCache implements Consumer<SpreadsheetDelta> {

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
     * Removes any deleted cells and then adds updated cells to the {@link #cells}.
     */
    @Override
    public void accept(final SpreadsheetDelta delta) {
        final Map<SpreadsheetCellReference, SpreadsheetCell> cells = this.cells;
        final Map<SpreadsheetCellReference, Set<SpreadsheetLabelName>> labels = this.labels;

        final Set<SpreadsheetCellRange> windows = delta.window();
        if (windows.isEmpty() || false == this.windows.equals(windows)) {
            // no window clear caches
            cells.clear();
            labels.clear();
        }

        for (final SpreadsheetCellReference cell : delta.deletedCells()) {
            cells.remove(cell);
        }

        for (final SpreadsheetCell cell : delta.cells()) {
            final SpreadsheetCellReference cellReference = cell.reference();
            cells.put(
                    cellReference,
                    cell
            );
        }

        final SpreadsheetViewportCacheUpdatingSpreadsheetSelectionVisitor labelUpdater = SpreadsheetViewportCacheUpdatingSpreadsheetSelectionVisitor.with(
                labels,
                windows
        );
        for (final SpreadsheetLabelMapping mapping : delta.labels()) {
            labelUpdater.accept(mapping);
        }

        this.windows = windows;
    }

    Optional<SpreadsheetCell> cell(final SpreadsheetCellReference cell) {
        return Optional.ofNullable(this.cells.get(cell));
    }

    Set<SpreadsheetLabelName> labels(final SpreadsheetCellReference cell) {
        return this.labels.getOrDefault(
                cell,
                Sets.empty()
        );
    }

    /**
     * A cache of cells, this allows partial updates such as a single cell and still be able to render a complete viewport.
     */
    // VisibleForTesting
    final Map<SpreadsheetCellReference, SpreadsheetCell> cells = Maps.sorted();

    /**
     * A cache of cell references and their one or more labels.
     */
    // VisibleForTesting
    final Map<SpreadsheetCellReference, Set<SpreadsheetLabelName>> labels = Maps.sorted();

    /**
     * The viewport. This is used to filter cells and labels in the cache.
     */
    // VisibleForTesting
    Set<SpreadsheetCellRange> windows = Sets.empty();

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .value(this.cells)
                .value(this.labels)
                .value(this.windows)
                .build();
    }
}
