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
import walkingkooka.spreadsheet.SpreadsheetRow;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

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

    private final static SpreadsheetRowReference ROW_REF_1 = SpreadsheetSelection.parseRow("1");
    private final static SpreadsheetRowReference ROW_REF_2 = SpreadsheetSelection.parseRow("2");
    private final static SpreadsheetRowReference ROW_REF_3 = SpreadsheetSelection.parseRow("3");

    private final static SpreadsheetRow ROW_1 = ROW_REF_1.row();
    private final static SpreadsheetRow ROW_2 = ROW_REF_2.row();
    private final static SpreadsheetRow ROW_3 = ROW_REF_3.row().setHidden(true);
    
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

    @Override
    public void testAllMethodsVisibility() {
        // nop
    }

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

        this.checkRows(
                cache
        );
    }

    @Test
    public void testOnSpreadsheetMetadata() {
        final double width = 123;
        final double height = 456;

        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.onSpreadsheetMetadata(
                SpreadsheetMetadata.EMPTY
                        .set(
                                SpreadsheetMetadataPropertyName.STYLE,
                                TextStyle.EMPTY
                                        .set(TextStylePropertyName.WIDTH, Length.pixel(width))
                                        .set(TextStylePropertyName.HEIGHT, Length.pixel(height))
                        )
        );

        this.checkEquals(
                width,
                cache.defaultWidth,
                "defaultWidth"
        );
        this.checkEquals(
                height,
                cache.defaultHeight,
                "defaultHeight"
        );
    }

    @Test
    public void testOnSpreadsheetDeltaFirstWithoutWindow() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.onSpreadsheetDelta(
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
                        ).setRows(
                Sets.of(
                        ROW_1,
                        ROW_2
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

        this.checkRows(
                cache,
                ROW_1,
                ROW_2
        );

        this.checkWindow(
                cache,
                ""
        );
    }

    @Test
    public void testOnSpreadsheetDeltaFirstWithWindow() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.onSpreadsheetDelta(
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
                        ).setRows(
                                Sets.of(
                                        ROW_1,
                                        ROW_2
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


        this.checkRows(
                cache,
                ROW_1,
                ROW_2
        );

        this.checkWindow(
                cache,
                WINDOW
        );
    }

    @Test
    public void testOnSpreadsheetDeltaFirstLabelToRange() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.onSpreadsheetDelta(
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
    public void testOnSpreadsheetDeltaFirstLabelToRangeOutsideWindow() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        final Set<SpreadsheetCellRange> windows = SpreadsheetSelection.parseWindow("A1:A2");

        cache.onSpreadsheetDelta(
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
    public void testOnSpreadsheetDeltaTwiceCellReplaced() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY
                        .setCells(
                                Sets.of(
                                        A1.setFormula(SpreadsheetFormula.EMPTY.setText("Lost"))
                                )
                        )
        );

        cache.onSpreadsheetDelta(
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
    public void testOnSpreadsheetDeltaTwiceColumnsReplaced() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY
                        .setColumns(
                                Sets.of(
                                        COLUMN_A.setHidden(true),
                                        COLUMN_B.setHidden(true)
                                )
                        )
        );

        cache.onSpreadsheetDelta(
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
    public void testOnSpreadsheetDeltaTwiceColumnsReplaced2() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY
                        .setColumns(
                                Sets.of(
                                        COLUMN_A.setHidden(true),
                                        COLUMN_B.setHidden(true)
                                )
                        )
        );

        cache.onSpreadsheetDelta(
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
    public void testOnSpreadsheetDeltaTwiceColumnsDeleted() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY
                        .setColumns(
                                Sets.of(
                                        COLUMN_A,
                                        COLUMN_B
                                )
                        )
        );

        cache.onSpreadsheetDelta(
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
    public void testOnSpreadsheetDeltaTwiceLabelsReplaced() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY
                        .setLabels(
                                Sets.of(
                                        LABEL_MAPPINGA1A
                                )
                        )
        );

        cache.onSpreadsheetDelta(
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
    public void testOnSpreadsheetDeltaTwiceLabelsReplaced2() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY
                        .setLabels(
                                Sets.of(
                                        SpreadsheetSelection.labelName("LostLabel").mapping(A1)
                                )
                        )
        );

        cache.onSpreadsheetDelta(
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
    public void testOnSpreadsheetDeltaTwiceDeletedCell() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.onSpreadsheetDelta(
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

        cache.onSpreadsheetDelta(
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
    public void testOnSpreadsheetDeltaTwiceMergedDifferentNoWindow() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.onSpreadsheetDelta(
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

        cache.onSpreadsheetDelta(
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
    public void testOnSpreadsheetDeltaTwiceSecondEmpty() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.onSpreadsheetDelta(
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

        cache.onSpreadsheetDelta(
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
    public void testOnSpreadsheetDeltaTwiceMergedDifferentSameWindows() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.onSpreadsheetDelta(
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

        cache.onSpreadsheetDelta(
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


    @Test
    public void testOnSpreadsheetDeltaTwiceRowsReplaced() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY
                        .setRows(
                                Sets.of(
                                        ROW_1.setHidden(true),
                                        ROW_2.setHidden(true)
                                )
                        )
        );

        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY
                        .setDeletedCells(
                                Sets.of(
                                        A1
                                )
                        )
                        .setRows(
                                Sets.of(
                                        ROW_1.setHidden(false),
                                        ROW_2.setHidden(false)
                                )
                        )
        );

        this.checkRows(
                cache,
                ROW_1.setHidden(false),
                ROW_2.setHidden(false)
        );

        this.checkWindow(
                cache,
                ""
        );
    }

    @Test
    public void testOnSpreadsheetDeltaTwiceRowsReplaced2() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY
                        .setRows(
                                Sets.of(
                                        ROW_1.setHidden(true),
                                        ROW_2.setHidden(true)
                                )
                        )
        );

        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY
                        .setDeletedCells(
                                Sets.of(
                                        A1
                                )
                        )
                        .setRows(
                                Sets.of(
                                        ROW_1.setHidden(false)
                                )
                        )
        );

        this.checkRows(
                cache,
                ROW_1.setHidden(false),
                ROW_2.setHidden(true)
        );

        this.checkWindow(
                cache,
                ""
        );
    }

    @Test
    public void testOnSpreadsheetDeltaTwiceRowsDeleted() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY
                        .setRows(
                                Sets.of(
                                        ROW_1,
                                        ROW_2
                                )
                        )
        );

        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY
                        .setDeletedCells(
                                Sets.of(
                                        A1
                                )
                        )
                        .setDeletedRows(
                                Sets.of(
                                        ROW_REF_1
                                )
                        )
        );

        this.checkRows(
                cache,
                ROW_2
        );

        this.checkWindow(
                cache,
                ""
        );
    }

    // columnWidth......................................................................................................

    @Test
    public void testOnSpreadsheetDeltaColumnWidths() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY
                        .setColumnWidths(
                                Maps.of(
                                        A, 10.0,
                                        B, 20.0
                                )
                        )
        );

        this.checkColumnsWidths(
                cache,
                Maps.of(
                        A, 10.0,
                        B, 20.0
                )
        );
    }

    @Test
    public void testOnSpreadsheetDeltaColumnWidthsUpdates() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY
                        .setColumnWidths(
                                Maps.of(
                                        A, 10.0,
                                        B, 20.0,
                                        C, 30.0
                                )
                        )
        );

        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY
                        .setColumnWidths(
                                Maps.of(
                                        A, 100.0,
                                        B, 200.0
                                )
                        )
        );

        this.checkColumnsWidths(
                cache,
                Maps.of(
                        A, 100.0,
                        B, 200.0,
                        C, 30.0
                )
        );
    }

    @Test
    public void testColumnWidthNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetViewportCache.empty()
                        .columnWidth(null)
        );
    }
    
    @Test
    public void testColumnWidthMissingDefaulted() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY
                        .setColumnWidths(
                                Maps.of(
                                        A, 10.0,
                                        B, 20.0
                                )
                        )
        );

        final double width = 10;
        final double height = 20;

        cache.onSpreadsheetMetadata(
                SpreadsheetMetadata.EMPTY
                        .set(
                                SpreadsheetMetadataPropertyName.STYLE,
                                TextStyle.EMPTY
                                        .set(
                                                TextStylePropertyName.WIDTH,
                                                Length.pixel(width)
                                        )
                                        .set(
                                                TextStylePropertyName.HEIGHT,
                                                Length.pixel(height)
                                        )
                        )
        );

        this.columnsWidthAndCheck(
                cache,
                SpreadsheetSelection.parseColumn("Z"),
                width
        );
    }

    // rowHeight......................................................................................................

    @Test
    public void testOnSpreadsheetDeltaRowHeights() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY
                        .setRowHeights(
                                Maps.of(
                                        ROW_REF_1, 10.0,
                                        ROW_REF_2, 20.0
                                )
                        )
        );

        this.checkRowsHeights(
                cache,
                Maps.of(
                        ROW_REF_1, 10.0,
                        ROW_REF_2, 20.0
                )
        );
    }

    @Test
    public void testOnSpreadsheetDeltaRowHeightsUpdates() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY
                        .setRowHeights(
                                Maps.of(
                                        ROW_REF_1, 10.0,
                                        ROW_REF_2, 20.0,
                                        ROW_REF_3, 30.0
                                )
                        )
        );

        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY
                        .setRowHeights(
                                Maps.of(
                                        ROW_REF_1, 100.0,
                                        ROW_REF_2, 200.0
                                )
                        )
        );

        this.checkRowsHeights(
                cache,
                Maps.of(
                        ROW_REF_1, 100.0,
                        ROW_REF_2, 200.0,
                        ROW_REF_3, 30.0
                )
        );
    }

    @Test
    public void testRowHeightNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetViewportCache.empty()
                        .rowHeight(null)
        );
    }

    @Test
    public void testRowHeightMissingDefaulted() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY
                        .setRowHeights(
                                Maps.of(
                                        ROW_REF_1, 10.0,
                                        ROW_REF_2, 20.0
                                )
                        )
        );

        final double width = 10;
        final double height = 20;

        cache.onSpreadsheetMetadata(
                SpreadsheetMetadata.EMPTY
                        .set(
                                SpreadsheetMetadataPropertyName.STYLE,
                                TextStyle.EMPTY
                                        .set(
                                                TextStylePropertyName.WIDTH,
                                                Length.pixel(width)
                                        ).set(
                                                TextStylePropertyName.HEIGHT,
                                                Length.pixel(height)
                                        )
                        )
        );

        this.rowsHeightAndCheck(
                cache,
                SpreadsheetSelection.parseRow("99"),
                height
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

    private void checkColumnsWidths(final SpreadsheetViewportCache cache,
                                    final Map<SpreadsheetColumnReference, Double> expected) {
        this.checkEquals(
                expected,
                cache.columnWidths,
                "columnWidths"
        );

        for (final Entry<SpreadsheetColumnReference, Double> entry : expected.entrySet()) {
            this.columnsWidthAndCheck(
                    cache,
                    entry.getKey(),
                    entry.getValue()
            );
        }
    }

    private void columnsWidthAndCheck(final SpreadsheetViewportCache cache,
                                      final SpreadsheetColumnReference column,
                                      final double expected) {

        this.checkEquals(
                expected,
                cache.columnWidth(column),
                () -> "columnWidth of " + column + " from " + cache
        );
    }

    private void checkRows(final SpreadsheetViewportCache cache,
                              final SpreadsheetRow... expected) {
        final Map<SpreadsheetRowReference, SpreadsheetRow> expectedMaps = Maps.ordered();
        for (final SpreadsheetRow row : expected) {
            expectedMaps.put(
                    row.reference(),
                    row
            );
        }

        this.checkRows(
                cache,
                expectedMaps
        );
    }

    private void checkRows(final SpreadsheetViewportCache cache,
                              final Map<SpreadsheetRowReference, SpreadsheetRow> expected) {
        this.checkEquals(
                expected,
                cache.rows,
                "rows"
        );

        for (final Entry<SpreadsheetRowReference, SpreadsheetRow> entry : expected.entrySet()) {
            this.checkEquals(
                    Optional.of(
                            entry.getValue()
                    ),
                    cache.row(entry.getKey())
            );
        }
    }

    private void checkRowsHeights(final SpreadsheetViewportCache cache,
                                  final Map<SpreadsheetRowReference, Double> expected) {
        this.checkEquals(
                expected,
                cache.rowHeights,
                "rowHeights"
        );

        for (final Entry<SpreadsheetRowReference, Double> entry : expected.entrySet()) {
            this.rowsHeightAndCheck(
                    cache,
                    entry.getKey(),
                    entry.getValue()
            );
        }
    }

    private void rowsHeightAndCheck(final SpreadsheetViewportCache cache,
                                    final SpreadsheetRowReference row,
                                    final double expected) {

        this.checkEquals(
                expected,
                cache.rowHeight(row),
                () -> "rowHeight of " + row + " from " + cache
        );
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

    // isColumnHidden...................................................................................................

    @Test
    public void testIsColumnHiddenAbsent() {
        this.isColumnHiddenAndCheck(
                SpreadsheetViewportCache.empty(),
                A,
                false
        );
    }

    @Test
    public void testIsColumnHiddenPresent() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();
        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY.setColumns(
                        Sets.of(COLUMN_A)
                )
        );
        this.isColumnHiddenAndCheck(
                cache,
                A,
                false
        );
    }

    @Test
    public void testIsColumnHiddenPresentAndHidden() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();
        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY.setColumns(
                        Sets.of(COLUMN_A.setHidden(true))
                )
        );
        this.isColumnHiddenAndCheck(
                cache,
                A,
                true
        );
    }

    private void isColumnHiddenAndCheck(final SpreadsheetViewportCache cache,
                                        final SpreadsheetColumnReference column,
                                        final boolean expected) {
        this.checkEquals(
                expected,
                cache.isColumnHidden(column),
                () -> "isHidden " + column
        );
    }

    // isRowHidden......................................................................................................

    @Test
    public void testIsRowHiddenAbsent() {
        this.isRowHiddenAndCheck(
                SpreadsheetViewportCache.empty(),
                ROW_REF_1,
                false
        );
    }

    @Test
    public void testIsRowHiddenPresent() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();
        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY.setRows(
                        Sets.of(ROW_1)
                )
        );
        this.isRowHiddenAndCheck(
                cache,
                ROW_REF_1,
                false
        );
    }

    @Test
    public void testIsRowHiddenPresentAndHidden() {
        final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();
        cache.onSpreadsheetDelta(
                SpreadsheetDelta.EMPTY.setRows(
                        Sets.of(ROW_1.setHidden(true))
                )
        );
        this.isRowHiddenAndCheck(
                cache,
                ROW_REF_1,
                true
        );
    }

    private void isRowHiddenAndCheck(final SpreadsheetViewportCache cache,
                                     final SpreadsheetRowReference row,
                                     final boolean expected) {
        this.checkEquals(
                expected,
                cache.isRowHidden(row),
                () -> "isHidden " + row
        );
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
