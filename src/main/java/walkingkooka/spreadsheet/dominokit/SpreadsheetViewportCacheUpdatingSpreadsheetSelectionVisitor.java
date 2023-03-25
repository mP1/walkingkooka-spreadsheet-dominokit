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
import walkingkooka.spreadsheet.reference.SpreadsheetSelectionVisitor;

import java.util.Map;
import java.util.Set;

/**
 * A {@link SpreadsheetSelectionVisitor} that is used primarily to expand {@link SpreadsheetCellRange} into the individual {@link SpreadsheetCellReference} within any range.
 */
final class SpreadsheetViewportCacheUpdatingSpreadsheetSelectionVisitor extends SpreadsheetSelectionVisitor {

    static SpreadsheetViewportCacheUpdatingSpreadsheetSelectionVisitor with(final Map<SpreadsheetCellReference, Set<SpreadsheetLabelName>> labels,
                                                                            final Set<SpreadsheetCellRange> window) {
        return new SpreadsheetViewportCacheUpdatingSpreadsheetSelectionVisitor(
                labels,
                window
        );
    }

    private SpreadsheetViewportCacheUpdatingSpreadsheetSelectionVisitor(final Map<SpreadsheetCellReference, Set<SpreadsheetLabelName>> labels,
                                                                        final Set<SpreadsheetCellRange> window) {
        this.labels = labels;
        this.window = window;
    }

    void accept(final SpreadsheetLabelMapping mapping) {
        this.label = mapping.label();
        this.accept(mapping.reference());
    }

    @Override
    protected void visit(final SpreadsheetCellRange range) {
        range.cellStream()
                .forEach(this::visit);
    }

    @Override
    protected void visit(final SpreadsheetCellReference reference) {
        final Set<SpreadsheetCellRange> window = this.window;
        if (window.isEmpty()) {
            this.update(reference);
        } else {
            for (final SpreadsheetCellRange oneWindow : this.window) {
                if (oneWindow.testCell(reference)) {
                    this.update(reference);
                    break;
                }
            }
        }
    }

    private void update(final SpreadsheetCellReference cell) {
        Set<SpreadsheetLabelName> labels = this.labels.get(cell);
        if (null == labels) {
            labels = Sets.sorted();
            this.labels.put(cell, labels);
        }

        labels.add(this.label);
    }

    @Override
    protected void visit(final SpreadsheetLabelName label) {
        // doesnt support label to label mappings.
        throw new UnsupportedOperationException();
    }

    /**
     * The label.
     */
    private SpreadsheetLabelName label;

    /**
     * Cells can have one or more labels mapped to them.
     */
    private final Map<SpreadsheetCellReference, Set<SpreadsheetLabelName>> labels;

    /**
     * The window used to filter any cells and labels.
     */
    private final Set<SpreadsheetCellRange> window;

    @Override
    public String toString() {
        return this.labels.toString();
    }
}
