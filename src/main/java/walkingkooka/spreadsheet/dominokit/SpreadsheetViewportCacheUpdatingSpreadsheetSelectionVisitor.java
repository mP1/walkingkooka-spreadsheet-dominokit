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

import walkingkooka.collect.set.Sets;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelectionVisitor;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A {@link SpreadsheetSelectionVisitor} that is used primarily to expand {@link SpreadsheetCellRange} into the individual {@link SpreadsheetCellReference} within any range.
 */
final class SpreadsheetViewportCacheUpdatingSpreadsheetSelectionVisitor extends SpreadsheetSelectionVisitor {

    static SpreadsheetViewportCacheUpdatingSpreadsheetSelectionVisitor accept(final Collection<SpreadsheetLabelMapping> mappings,
                                                                              final Map<SpreadsheetCellReference, Set<SpreadsheetLabelName>> cellToLabels,
                                                                              final Map<SpreadsheetLabelName, SpreadsheetSelection> labelToNonLabel,
                                                                              final Set<SpreadsheetCellRange> window) {
        return new SpreadsheetViewportCacheUpdatingSpreadsheetSelectionVisitor(
                mappings,
                cellToLabels,
                labelToNonLabel,
                window
        );
    }

    private SpreadsheetViewportCacheUpdatingSpreadsheetSelectionVisitor(final Collection<SpreadsheetLabelMapping> mappings,
                                                                        final Map<SpreadsheetCellReference, Set<SpreadsheetLabelName>> cellToLabels,
                                                                        final Map<SpreadsheetLabelName, SpreadsheetSelection> labelToNonLabel,
                                                                        final Set<SpreadsheetCellRange> window) {
        this.cellToLabels = cellToLabels;
        this.labelToNonLabel = labelToNonLabel;
        this.window = window;

        final Set<SpreadsheetLabelName> done = Sets.sorted();

        for (final SpreadsheetLabelMapping mapping : mappings) {
            final SpreadsheetLabelName label = mapping.label();
            this.label = label;

            final SpreadsheetSelection reference = mapping.reference();
            if (false == reference.isLabelName()) {
                this.accept(reference);
            }
        }

        for (; ; ) {
            boolean change = false;

            for (final SpreadsheetLabelMapping mapping : mappings) {
                final SpreadsheetLabelName unknownLabel = mapping.label();
                final SpreadsheetSelection possibleLabel = mapping.reference();
                if (false == possibleLabel.isLabelName() || done.contains(unknownLabel)) {
                    continue;
                }

                final SpreadsheetLabelName maybeKnownLabel = (SpreadsheetLabelName) possibleLabel;

                final SpreadsheetSelection nonLabel = labelToNonLabel.get(maybeKnownLabel);
                if (null != nonLabel) {
                    // assumes nonLabel must be either a cell or cell-range
                    if (nonLabel.isCellReference()) {
                        final SpreadsheetCellReference cell = (SpreadsheetCellReference) nonLabel;
                        cellToLabels.get(cell).add(unknownLabel);
                    } else {
                        final SpreadsheetCellRange range = (SpreadsheetCellRange) nonLabel;
                        for (final SpreadsheetCellReference cell : range) {
                            cellToLabels.get(cell).add(unknownLabel);
                        }
                    }
                    labelToNonLabel.put(unknownLabel, nonLabel);
                    change = true;
                    done.add(unknownLabel);
                }
            }

            if (false == change) {
                break;
            }
        }
    }

    @Override
    protected void visit(final SpreadsheetCellRange range) {
        final Set<SpreadsheetCellRange> window = this.window;
        if (window.isEmpty()) {
            this.updateCellRange(range);
        } else {
            for (final SpreadsheetCellRange oneWindow : this.window) {
                if (oneWindow.testCellRange(range)) {
                    this.updateCellRange(range);
                    break;
                }
            }
        }
    }

    private void updateCellRange(final SpreadsheetCellRange range) {
        final SpreadsheetLabelName label = this.label;

        final Map<SpreadsheetCellReference, Set<SpreadsheetLabelName>> cellToLabels = this.cellToLabels;
        for (final SpreadsheetCellReference cell : range) {
            Set<SpreadsheetLabelName> labels = cellToLabels.get(cell);
            if (null == labels) {
                labels = Sets.sorted();
                cellToLabels.put(cell, labels);
            }
            labels.add(label);
        }

        final Map<SpreadsheetLabelName, SpreadsheetSelection> labelToNonLabel = this.labelToNonLabel;
        if (false == labelToNonLabel.containsKey(label)) {
            labelToNonLabel.put(label, range);
        }
    }

    @Override
    protected void visit(final SpreadsheetCellReference reference) {
        final Set<SpreadsheetCellRange> window = this.window;
        if (window.isEmpty()) {
            this.updateCell(reference);
        } else {
            for (final SpreadsheetCellRange oneWindow : window) {
                if (oneWindow.testCell(reference)) {
                    this.updateCell(reference);
                    break;
                }
            }
        }
    }

    private void updateCell(final SpreadsheetCellReference cell) {
        final Map<SpreadsheetCellReference, Set<SpreadsheetLabelName>> cellToLabels = this.cellToLabels;
        Set<SpreadsheetLabelName> labels = cellToLabels.get(cell);
        if (null == labels) {
            labels = Sets.sorted();
            cellToLabels.put(cell, labels);
        }

        final SpreadsheetLabelName label = this.label;
        final Map<SpreadsheetLabelName, SpreadsheetSelection> labelToNonLabel = this.labelToNonLabel;
        if (false == labelToNonLabel.containsKey(label)) {
            labelToNonLabel.put(label, cell);
        }

        labels.add(label);
    }

    @Override
    protected void visit(final SpreadsheetLabelName label) {
        throw new UnsupportedOperationException();
    }

    /**
     * The label.
     */
    private SpreadsheetLabelName label;

    /**
     * Cells can have one or more labels mapped to them.
     */
    private final Map<SpreadsheetCellReference, Set<SpreadsheetLabelName>> cellToLabels;

    /**
     * Label to non label mappings, for the moment this is cell or cell-range.
     */
    private final Map<SpreadsheetLabelName, SpreadsheetSelection> labelToNonLabel;

    /**
     * The window used to filter any cells and labels.
     */
    private final Set<SpreadsheetCellRange> window;

    @Override
    public String toString() {
        return this.cellToLabels.toString();
    }
}
