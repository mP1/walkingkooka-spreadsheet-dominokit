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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetColumn;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

public final class SpreadsheetViewportCacheTest implements ClassTesting<SpreadsheetViewportCache> {

    private final static SpreadsheetCellReference A1 = SpreadsheetCellReference.A1;
    private final static SpreadsheetCellReference A2 = SpreadsheetSelection.parseCell("A2");
    private final static SpreadsheetCellReference A3 = SpreadsheetSelection.parseCell("A3");
    private final static SpreadsheetCellReference B3 = SpreadsheetSelection.parseCell("B3");

    private final static SpreadsheetCell A1_CELL = A1.setFormula(SpreadsheetFormula.EMPTY.setText("=1"));
    private final static SpreadsheetCell A2_CELL = A2.setFormula(SpreadsheetFormula.EMPTY.setText("=22"));
    private final static SpreadsheetCell A3_CELL = A3.setFormula(SpreadsheetFormula.EMPTY.setText("=333"));

    private final static SpreadsheetColumnReference A = SpreadsheetSelection.parseColumn("A");
    private final static SpreadsheetColumnReference B = SpreadsheetSelection.parseColumn("B");
    private final static SpreadsheetColumnReference C = SpreadsheetSelection.parseColumn("C");

    private final static SpreadsheetColumn COLUMN_A = A.column();
    private final static SpreadsheetColumn COLUMN_B = B.column();
    private final static SpreadsheetColumn COLUMN_C = C.column().setHidden(true);

    private final static SpreadsheetLabelName LABEL1 = SpreadsheetSelection.labelName("Label123");
    private final static SpreadsheetLabelName LABEL2 = SpreadsheetSelection.labelName("Label234");
    private final static SpreadsheetLabelName LABEL3 = SpreadsheetSelection.labelName("Label345");

    private final static SpreadsheetLabelMapping LABEL_MAPPINGA1A = LABEL1.mapping(A1);
    private final static SpreadsheetLabelMapping LABEL_MAPPINGA1B = LABEL2.mapping(A1);
    private final static SpreadsheetLabelMapping LABEL_MAPPINGB3 = LABEL3.mapping(B3);

    private final static SpreadsheetCellRange WINDOW1 = SpreadsheetSelection.parseCellRange("A1:B3");
    private final static Set<SpreadsheetCellRange> WINDOW = Sets.of(
            WINDOW1
    );

    // tests............................................................................................................

    @Test
    public void testEmpty() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        this.checkCells(
                cache
        );

        this.checkColumns(
                cache
        );

        this.checkLabels(
                cache,
                Maps.empty()
        );
    }

    @Test
    public void testAcceptFirstWithoutWindow() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setCells(
                                Sets.of(
                                        A1_CELL,
                                        A2_CELL,
                                        A3_CELL
                                )
                        ).setColumns(
                                Sets.of(
                                        COLUMN_A,
                                        COLUMN_B
                                )
                        ).setLabels(
                                Sets.of(
                                        LABEL_MAPPINGA1A,
                                        LABEL_MAPPINGA1B,
                                        LABEL_MAPPINGB3
                                )
                        )
        );

        this.checkCells(
                cache,
                A1_CELL,
                A2_CELL,
                A3_CELL
        );

        this.checkColumns(
                cache,
                COLUMN_A,
                COLUMN_B
        );

        this.checkLabels(
                cache,
                LABEL_MAPPINGA1A,
                LABEL_MAPPINGA1B,
                LABEL_MAPPINGB3
        );

        this.checkWindow(
                cache,
                ""
        );
    }

    @Test
    public void testAcceptFirstWithWindow() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setCells(
                                Sets.of(
                                        A1_CELL,
                                        A2_CELL,
                                        A3_CELL
                                )
                        ).setColumns(
                                Sets.of(
                                        COLUMN_A,
                                        COLUMN_B
                                )
                        ).setLabels(
                                Sets.of(
                                        LABEL_MAPPINGA1A,
                                        LABEL_MAPPINGA1B,
                                        LABEL_MAPPINGB3
                                )
                        ).setWindow(WINDOW)
        );

        this.checkCells(
                cache,
                A1_CELL,
                A2_CELL,
                A3_CELL
        );

        this.checkColumns(
                cache,
                COLUMN_A,
                COLUMN_B
        );

        this.checkLabels(
                cache,
                LABEL_MAPPINGA1A,
                LABEL_MAPPINGA1B,
                LABEL_MAPPINGB3
        );

        this.checkWindow(
                cache,
                WINDOW
        );
    }

    @Test
    public void testAcceptFirstLabelToRange() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setCells(
                                Sets.of(
                                        A1_CELL
                                )
                        ).setLabels(
                                Sets.of(
                                        LABEL1.mapping(SpreadsheetSelection.parseCellRange("A1:A2"))
                                )
                        )
        );

        this.checkCells(
                cache,
                A1_CELL
        );

        this.checkLabels(
                cache,
                LABEL1.mapping(A1),
                LABEL1.mapping(A2)
        );

        this.checkWindow(
                cache,
                ""
        );
    }

    @Test
    public void testAcceptFirstLabelToRangeOutsideWindow() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        final Set<SpreadsheetCellRange> windows = SpreadsheetSelection.parseWindow("A1:A2");

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setCells(
                                Sets.of(
                                        A1_CELL
                                )
                        ).setLabels(
                                Sets.of(
                                        LABEL1.mapping(SpreadsheetSelection.parseCellRange("A1:A3"))
                                )
                        ).setWindow(windows)
        );

        this.checkCells(
                cache,
                A1_CELL
        );

        this.checkLabels(
                cache,
                LABEL1.mapping(A1),
                LABEL1.mapping(A2)
        );

        this.checkWindow(
                cache,
                windows
        );
    }

    @Test
    public void testAcceptTwiceCellReplaced() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setCells(
                                Sets.of(
                                        A1.setFormula(SpreadsheetFormula.EMPTY.setText("Lost"))
                                )
                        )
        );

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setCells(
                                Sets.of(
                                        A1_CELL
                                )
                        )
        );

        this.checkCells(
                cache,
                A1_CELL
        );

        this.checkWindow(
                cache,
                ""
        );
    }

    @Test
    public void testAcceptTwiceColumnsReplaced() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setColumns(
                                Sets.of(
                                        COLUMN_A.setHidden(true),
                                        COLUMN_B.setHidden(true)
                                )
                        )
        );

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setDeletedCells(
                                Sets.of(
                                        A1
                                )
                        )
                        .setColumns(
                                Sets.of(
                                        COLUMN_A.setHidden(false),
                                        COLUMN_B.setHidden(false)
                                )
                        )
        );

        this.checkColumns(
                cache,
                COLUMN_A.setHidden(false),
                COLUMN_B.setHidden(false)
        );

        this.checkWindow(
                cache,
                ""
        );
    }

    @Test
    public void testAcceptTwiceColumnsReplaced2() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setColumns(
                                Sets.of(
                                        COLUMN_A.setHidden(true),
                                        COLUMN_B.setHidden(true)
                                )
                        )
        );

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setDeletedCells(
                                Sets.of(
                                        A1
                                )
                        )
                        .setColumns(
                                Sets.of(
                                        COLUMN_A.setHidden(false)
                                )
                        )
        );

        this.checkColumns(
                cache,
                COLUMN_A.setHidden(false),
                COLUMN_B.setHidden(true)
        );

        this.checkWindow(
                cache,
                ""
        );
    }

    @Test
    public void testAcceptTwiceColumnsDeleted() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setColumns(
                                Sets.of(
                                        COLUMN_A,
                                        COLUMN_B
                                )
                        )
        );

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setDeletedCells(
                                Sets.of(
                                        A1
                                )
                        )
                        .setDeletedColumns(
                                Sets.of(
                                        A
                                )
                        )
        );

        this.checkColumns(
                cache,
                COLUMN_B
        );

        this.checkWindow(
                cache,
                ""
        );
    }
    
    @Test
    public void testAcceptTwiceLabelsReplaced() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setLabels(
                                Sets.of(
                                        LABEL_MAPPINGA1A
                                )
                        )
        );

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setDeletedCells(
                                Sets.of(
                                        A1
                                )
                        )
                        .setLabels(
                                Sets.of(
                                        LABEL_MAPPINGA1B
                                )
                        )
        );

        this.checkLabels(
                cache,
                LABEL_MAPPINGA1B
        );

        this.checkWindow(
                cache,
                ""
        );
    }

    @Test
    public void testAcceptTwiceLabelsReplaced2() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setLabels(
                                Sets.of(
                                        SpreadsheetSelection.labelName("LostLabel").mapping(A1)
                                )
                        )
        );

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setDeletedCells(
                                Sets.of(
                                        A1
                                )
                        )
                        .setLabels(
                                Sets.of(
                                        LABEL_MAPPINGA1A,
                                        LABEL_MAPPINGA1B
                                )
                        )
        );

        this.checkLabels(
                cache,
                LABEL_MAPPINGA1A,
                LABEL_MAPPINGA1B
        );

        this.checkWindow(
                cache,
                ""
        );
    }

    @Test
    public void testAcceptTwiceDeletedCell() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setCells(
                                Sets.of(
                                        A1.setFormula(SpreadsheetFormula.EMPTY.setText("Lost")),
                                        A2_CELL
                                )
                        ).setLabels(
                                Sets.of(
                                        SpreadsheetSelection.labelName("LostLabel").mapping(A1)
                                )
                        )
        );

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setDeletedCells(
                                Sets.of(
                                        A1
                                )
                        ).setLabels(
                                Sets.of(
                                        LABEL_MAPPINGA1A
                                )
                        )
        );

        this.checkCells(
                cache,
                A2_CELL
        );

        this.checkLabels(
                cache,
                LABEL_MAPPINGA1A
        );

        this.checkWindow(
                cache,
                ""
        );
    }

    @Test
    public void testAcceptTwiceMergedDifferentNoWindow() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setCells(
                                Sets.of(
                                        A1_CELL
                                )
                        ).setLabels(
                                Sets.of(
                                        LABEL_MAPPINGA1A
                                )
                        ).setWindow(WINDOW)
        );

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setCells(
                                Sets.of(
                                        A2_CELL
                                )
                        ).setLabels(
                                Sets.of(
                                        LABEL_MAPPINGB3
                                )
                        )
        );

        this.checkCells(
                cache,
                A2_CELL
        );

        this.checkLabels(
                cache,
                LABEL_MAPPINGB3
        );

        this.checkWindow(
                cache,
                ""
        );
    }

    @Test
    public void testAcceptTwiceSecondEmpty() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setCells(
                                Sets.of(
                                        A1_CELL
                                )
                        ).setLabels(
                                Sets.of(
                                        LABEL_MAPPINGA1A
                                )
                        ).setWindow(WINDOW)
        );

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setWindow(WINDOW)
        );

        this.checkCells(
                cache,
                A1_CELL
        );

        this.checkLabels(
                cache,
                LABEL_MAPPINGA1A
        );

        this.checkWindow(
                cache,
                WINDOW
        );
    }

    @Test
    public void testAcceptTwiceMergedDifferentSameWindows() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setCells(
                                Sets.of(
                                        A1_CELL
                                )
                        ).setLabels(
                                Sets.of(
                                        LABEL_MAPPINGA1A
                                )
                        ).setWindow(WINDOW)
        );

        cache.accept(
                SpreadsheetDelta.EMPTY
                        .setCells(
                                Sets.of(
                                        A2_CELL
                                )
                        ).setLabels(
                                Sets.of(
                                        LABEL_MAPPINGB3
                                )
                        ).setWindow(WINDOW)
        );

        this.checkCells(
                cache,
                A1_CELL,
                A2_CELL
        );

        this.checkLabels(
                cache,
                LABEL_MAPPINGA1A,
                LABEL_MAPPINGB3
        );

        this.checkWindow(
                cache,
                WINDOW
        );
    }

    private void checkCells(final SpreadsheetViewportCache cache,
                            final SpreadsheetCell... expected) {
        final Map<SpreadsheetCellReference, SpreadsheetCell> expectedMaps = Maps.ordered();
        for (final SpreadsheetCell cell : expected) {
            expectedMaps.put(
                    cell.reference(),
                    cell
            );
        }

        this.checkCells(
                cache,
                expectedMaps
        );
    }

    private void checkCells(final SpreadsheetViewportCache cache,
                            final Map<SpreadsheetCellReference, SpreadsheetCell> expected) {
        this.checkEquals(
                expected,
                cache.cells,
                "cells"
        );

        for (final Entry<SpreadsheetCellReference, SpreadsheetCell> entry : expected.entrySet()) {
            this.checkEquals(
                    Optional.of(
                            entry.getValue()
                    ),
                    cache.cell(entry.getKey())
            );
        }
    }

    private void checkColumns(final SpreadsheetViewportCache cache,
                              final SpreadsheetColumn... expected) {
        final Map<SpreadsheetColumnReference, SpreadsheetColumn> expectedMaps = Maps.ordered();
        for (final SpreadsheetColumn column : expected) {
            expectedMaps.put(
                    column.reference(),
                    column
            );
        }

        this.checkColumns(
                cache,
                expectedMaps
        );
    }

    private void checkColumns(final SpreadsheetViewportCache cache,
                              final Map<SpreadsheetColumnReference, SpreadsheetColumn> expected) {
        this.checkEquals(
                expected,
                cache.columns,
                "columns"
        );

        for (final Entry<SpreadsheetColumnReference, SpreadsheetColumn> entry : expected.entrySet()) {
            this.checkEquals(
                    Optional.of(
                            entry.getValue()
                    ),
                    cache.column(entry.getKey())
            );
        }
    }

    private void checkWindow(final SpreadsheetViewportCache cache,
                             final String expected) {
        this.checkWindow(
                cache,
                SpreadsheetSelection.parseWindow(expected)
        );
    }

    private void checkWindow(final SpreadsheetViewportCache cache,
                             final Set<SpreadsheetCellRange> expected) {
        this.checkEquals(
                expected,
                cache.windows,
                "windows"
        );
    }

    private void checkLabels(final SpreadsheetViewportCache cache,
                             final SpreadsheetLabelMapping... expected) {
        final Map<SpreadsheetCellReference, Set<SpreadsheetLabelName>> expectedMaps = Maps.ordered();
        for (final SpreadsheetLabelMapping mapping : expected) {
            final SpreadsheetCellReference cell = (SpreadsheetCellReference) mapping.reference();
            Set<SpreadsheetLabelName> labels = expectedMaps.get(cell);
            if (null == labels) {
                labels = Sets.ordered();
                expectedMaps.put(
                        cell,
                        labels
                );
            }
            labels.add(mapping.label());
        }

        this.checkLabels(
                cache,
                expectedMaps
        );
    }

    private void checkLabels(final SpreadsheetViewportCache cache,
                             final Map<SpreadsheetCellReference, Set<SpreadsheetLabelName>> expected) {
        this.checkEquals(
                expected,
                cache.labels,
                "labels"
        );

        for (final Entry<SpreadsheetCellReference, Set<SpreadsheetLabelName>> entry : expected.entrySet()) {
            this.checkEquals(
                    entry.getValue(),
                    cache.labels(entry.getKey())
            );
        }
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetViewportCache> type() {
        return SpreadsheetViewportCache.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
