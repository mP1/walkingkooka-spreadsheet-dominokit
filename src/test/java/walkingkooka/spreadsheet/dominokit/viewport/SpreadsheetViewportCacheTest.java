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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.iterator.IteratorTesting;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.color.Color;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlFragment;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetCellRange;
import walkingkooka.spreadsheet.SpreadsheetColumn;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.SpreadsheetRow;
import walkingkooka.spreadsheet.SpreadsheetViewportWindows;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelNameResolverTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetViewportCacheTest implements IteratorTesting,
    SpreadsheetLabelNameResolverTesting<SpreadsheetViewportCache>,
    ClassTesting<SpreadsheetViewportCache> {

    private final static SpreadsheetCellReference A1 = SpreadsheetCellReference.A1;
    private final static SpreadsheetCellReference A2 = SpreadsheetSelection.parseCell("A2");
    private final static SpreadsheetCellReference A3 = SpreadsheetSelection.parseCell("A3");
    private final static SpreadsheetCellReference B3 = SpreadsheetSelection.parseCell("B3");
    private final static SpreadsheetCellReference B4 = SpreadsheetSelection.parseCell("B4");

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
    private final static SpreadsheetLabelName LABEL999 = SpreadsheetSelection.labelName("Label999");

    private final static SpreadsheetLabelMapping LABEL_MAPPINGA1A = LABEL1.setLabelMappingTarget(A1);
    private final static SpreadsheetLabelMapping LABEL_MAPPINGA1B = LABEL2.setLabelMappingTarget(A1);
    private final static SpreadsheetLabelMapping LABEL_MAPPINGB3 = LABEL3.setLabelMappingTarget(B3);
    private final static SpreadsheetLabelMapping LABEL999_LABEL_MAPPINGB3 = LABEL999.setLabelMappingTarget(LABEL3);

    private final static SpreadsheetCellRangeReference WINDOW1 = SpreadsheetSelection.parseCellRange("A1:B3");
    private final static SpreadsheetViewportWindows WINDOW = SpreadsheetViewportWindows.with(
        Sets.of(
            WINDOW1
        )
    );

    private final static HttpMethod METHOD = HttpMethod.GET;

    private final static AbsoluteOrRelativeUrl URL_ID1 = Url.parseAbsoluteOrRelative("https://example.com/api/spreadsheet/1/cell");

    private final static AbsoluteOrRelativeUrl URL_ID2 = Url.parseAbsoluteOrRelative("https://example.com/api/spreadsheet/22/cell");

    private final static AbsoluteOrRelativeUrl URL_ID3 = Url.parseAbsoluteOrRelative("https://example.com/api/spreadsheet/333/cell");

    private final static SpreadsheetId ID1 = SpreadsheetId.parse("1");

    private final static SpreadsheetId ID2 = SpreadsheetId.parse("22");

    private final static SpreadsheetId ID3 = SpreadsheetId.parse("333");

    private final static AppContext CONTEXT = new FakeAppContext() {
        @Override
        public void debug(final Object... values) {
            // nop
        }
    };

    @Override
    public void testAllMethodsVisibility() {
        // nop
    }

    // tests............................................................................................................

    @Test
    public void testEmpty() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        this.checkCells(
            cache
        );

        this.checkMatchedCells(
            cache
        );

        this.checkColumns(
            cache
        );

        this.checkCellToLabels(
            cache,
            Maps.empty()
        );

        this.checkRows(
            cache
        );
    }

    // clear............................................................................................................

    @Test
    public void testClear() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();
        cache.clear();
    }

    @Test
    public void testClearOnSpreadsheetMetadata() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();
        cache.clear();

        final double width = 100;
        final double height = 200;

        cache.onSpreadsheetMetadata(
            SpreadsheetMetadata.EMPTY
                .set(SpreadsheetMetadataPropertyName.SPREADSHEET_ID, ID1)
                .set(
                    SpreadsheetMetadataPropertyName.STYLE,
                    TextStyle.EMPTY
                        .set(TextStylePropertyName.WIDTH, Length.pixel(width))
                        .set(TextStylePropertyName.HEIGHT, Length.pixel(height))
                ),
            CONTEXT
        );

        this.checkEquals(
            Length.pixel(width),
            cache.defaultWidth,
            "defaultWidth"
        );
        this.checkEquals(
            Length.pixel(height),
            cache.defaultHeight,
            "defaultHeight"
        );
    }

    @Test
    public void testClearOnSpreadsheetDelta() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.clear();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setCells(
                    Sets.of(
                        A1_CELL,
                        A2_CELL,
                        A3_CELL
                    )
                ).setMatchedCells(
                    Sets.of(
                        A1,
                        A2
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
                ),
            CONTEXT
        );

        this.checkCells(
            cache,
            A1_CELL,
            A2_CELL,
            A3_CELL
        );

        this.checkMatchedCells(
            cache,
            A1,
            A2
        );

        this.checkColumns(
            cache,
            COLUMN_A,
            COLUMN_B
        );

        this.checkCellToLabels(
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

        cache.clear();

        this.checkCells(cache);
        this.checkColumns(cache);
        this.checkCellToLabels(cache);
        this.checkRows(cache);
    }

    // onSpreadsheetMetadata...........................................................................................

    @Test
    public void testOnSpreadsheetMetadata() {
        final double width = 123;
        final double height = 456;

        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetMetadata(
            SpreadsheetMetadata.EMPTY
                .set(SpreadsheetMetadataPropertyName.SPREADSHEET_ID, ID1)
                .set(
                    SpreadsheetMetadataPropertyName.STYLE,
                    TextStyle.EMPTY
                        .set(TextStylePropertyName.WIDTH, Length.pixel(width))
                        .set(TextStylePropertyName.HEIGHT, Length.pixel(height))
                ),
            CONTEXT
        );

        this.checkEquals(
            Length.pixel(width),
            cache.defaultWidth,
            "defaultWidth"
        );
        this.checkEquals(
            Length.pixel(height),
            cache.defaultHeight,
            "defaultHeight"
        );
    }

    @Test
    public void testOnSpreadsheetMetadataSpreadsheetIdChange() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setText("will be lost")
                    )
                )
            ),
            CONTEXT
        );

        cache.onSpreadsheetMetadata(
            SpreadsheetMetadata.EMPTY.set(
                SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                ID2
            ).set(
                SpreadsheetMetadataPropertyName.STYLE,
                TextStyle.EMPTY
                    .set(TextStylePropertyName.WIDTH, Length.pixel(100.0))
                    .set(TextStylePropertyName.HEIGHT, Length.pixel(200.0))
            ),
            CONTEXT
        );

        final SpreadsheetCell b2 = SpreadsheetSelection.parseCell("B2")
            .setFormula(
                SpreadsheetFormula.EMPTY.setText("kept")
            );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID2,
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    b2
                )
            ),
            CONTEXT
        );

        this.checkCells(
            cache,
            b2
        );
    }

    @Test
    public void testOnSpreadsheetMetadataSpreadsheetIdChangeTwice() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setText("will be lost")
                    )
                )
            ),
            CONTEXT
        );

        cache.onSpreadsheetMetadata(
            SpreadsheetMetadata.EMPTY.set(
                SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                ID2
            ).set(
                SpreadsheetMetadataPropertyName.STYLE,
                TextStyle.EMPTY
                    .set(TextStylePropertyName.WIDTH, Length.pixel(100.0))
                    .set(TextStylePropertyName.HEIGHT, Length.pixel(200.0))
            ),
            CONTEXT
        );

        final SpreadsheetCell b2 = SpreadsheetSelection.parseCell("B2")
            .setFormula(
                SpreadsheetFormula.EMPTY.setText("will be lost")
            );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID2,
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.parseCell("B2")
                        .setFormula(
                            SpreadsheetFormula.EMPTY.setText("will be lost")
                        )
                )
            ),
            CONTEXT
        );

        this.checkCells(
            cache,
            b2
        );

        cache.onSpreadsheetMetadata(
            SpreadsheetMetadata.EMPTY.set(
                SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                ID3
            ).set(
                SpreadsheetMetadataPropertyName.STYLE,
                TextStyle.EMPTY
                    .set(TextStylePropertyName.WIDTH, Length.pixel(100.0))
                    .set(TextStylePropertyName.HEIGHT, Length.pixel(200.0))
            ),
            CONTEXT
        );

        final SpreadsheetCell c3 = SpreadsheetSelection.parseCell("c3")
            .setFormula(
                SpreadsheetFormula.EMPTY.setText("kept!")
            );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID3,
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(c3)
            ),
            CONTEXT
        );

        this.checkCells(
            cache,
            c3
        );
    }

    @Test
    public void testOnSpreadsheetDeltaFirstWithoutWindow() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
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
                ),
            CONTEXT
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

        this.checkCellToLabels(
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
    }

    @Test
    public void testOnSpreadsheetDeltaFirstWithWindow() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
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
                ).setWindow(WINDOW),
            CONTEXT
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

        this.checkCellToLabels(
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
    }

    @Test
    public void testOnSpreadsheetDeltaFirstLabelToRange() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setCells(
                    Sets.of(
                        A1_CELL
                    )
                ).setLabels(
                    Sets.of(
                        LABEL1.setLabelMappingTarget(
                            SpreadsheetSelection.parseCellRange("A1:A2")
                        )
                    )
                ),
            CONTEXT
        );

        this.checkCells(
            cache,
            A1_CELL
        );

        this.checkCellToLabels(
            cache,
            LABEL1.setLabelMappingTarget(A1),
            LABEL1.setLabelMappingTarget(A2)
        );
    }

    @Test
    public void testOnSpreadsheetDeltaFirstLabelToRangeOutsideWindow() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        final SpreadsheetViewportWindows windows = SpreadsheetViewportWindows.parse("A1:A2");

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setCells(
                    Sets.of(
                        A1_CELL
                    )
                ).setLabels(
                    Sets.of(
                        LABEL1.setLabelMappingTarget(
                            SpreadsheetSelection.parseCellRange("A1:A3")
                        )
                    )
                ).setWindow(windows),
            CONTEXT
        );

        this.checkCells(
            cache,
            A1_CELL
        );

        this.checkCellToLabels(
            cache,
            Maps.of(
                A1,
                Sets.of(LABEL1),
                A2,
                Sets.of(LABEL1),
                A3,
                Sets.of(LABEL1)
            )
        );
    }

    @Test
    public void testOnSpreadsheetDeltaTwiceCellReplaced() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setCells(
                    Sets.of(
                        A1.setFormula(SpreadsheetFormula.EMPTY.setText("Lost"))
                    )
                ).setMatchedCells(
                    Sets.of(
                        A1
                    )
                ),
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setCells(
                    Sets.of(
                        A1_CELL
                    )
                ),
            CONTEXT
        );

        this.checkCells(
            cache,
            A1_CELL
        );

        this.checkMatchedCells(
            cache,
            A1
        );

        this.isMatchedCellAndCheck(
            cache,
            A2,
            false
        );

        this.resolveIfLabelAndCheck(
            cache,
            A1,
            A1
        );
    }

    @Test
    public void testOnSpreadsheetDeltaTwiceColumnsReplaced() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setColumns(
                    Sets.of(
                        COLUMN_A.setHidden(true),
                        COLUMN_B.setHidden(true)
                    )
                ),
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
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
                ),
            CONTEXT
        );

        this.checkColumns(
            cache,
            COLUMN_A.setHidden(false),
            COLUMN_B.setHidden(false)
        );
    }

    @Test
    public void testOnSpreadsheetDeltaTwiceColumnsReplaced2() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setColumns(
                    Sets.of(
                        COLUMN_A.setHidden(true),
                        COLUMN_B.setHidden(true)
                    )
                ),
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
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
                ),
            CONTEXT
        );

        this.checkColumns(
            cache,
            COLUMN_A.setHidden(false),
            COLUMN_B.setHidden(true)
        );
    }

    @Test
    public void testOnSpreadsheetDeltaTwiceColumnsDeleted() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setColumns(
                    Sets.of(
                        COLUMN_A,
                        COLUMN_B
                    )
                ),
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
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
                ),
            CONTEXT
        );

        this.checkColumns(
            cache,
            COLUMN_B
        );
    }

    @Test
    public void testOnSpreadsheetDeltaTwiceLabelsReplaced() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setLabels(
                    Sets.of(
                        LABEL_MAPPINGA1A
                    )
                ),
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
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
                ),
            CONTEXT
        );

        this.checkCellToLabels(
            cache,
            LABEL_MAPPINGA1B
        );

        this.resolveIfLabelAndCheck(
            cache,
            LABEL_MAPPINGA1B.label(),
            LABEL_MAPPINGA1B.target()
        );
    }

    @Test
    public void testOnSpreadsheetDeltaTwiceLabelsReplaced2() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setLabels(
                    Sets.of(
                        SpreadsheetSelection.labelName("LostLabel")
                            .setLabelMappingTarget(A1)
                    )
                ),
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
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
                ),
            CONTEXT
        );

        this.checkCellToLabels(
            cache,
            LABEL_MAPPINGA1A,
            LABEL_MAPPINGA1B
        );
    }

    @Test
    public void testOnSpreadsheetDeltaTwiceDeletedCell() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setCells(
                    Sets.of(
                        A1.setFormula(SpreadsheetFormula.EMPTY.setText("Lost")),
                        A2_CELL
                    )
                ).setLabels(
                    Sets.of(
                        SpreadsheetSelection.labelName("LostLabel")
                            .setLabelMappingTarget(A1)
                    )
                ),
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setDeletedCells(
                    Sets.of(
                        A1
                    )
                ).setLabels(
                    Sets.of(
                        LABEL_MAPPINGA1A
                    )
                ),
            CONTEXT
        );

        this.checkCells(
            cache,
            A2_CELL
        );

        this.checkCellToLabels(
            cache,
            LABEL_MAPPINGA1A
        );
    }

    @Test
    public void testOnSpreadsheetDeltaTwiceMergedDifferentNoWindow() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setCells(
                    Sets.of(
                        A1_CELL
                    )
                ).setMatchedCells(
                    Sets.of(A1)
                ).setLabels(
                    Sets.of(
                        LABEL_MAPPINGA1A
                    )
                ).setWindow(WINDOW),
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setCells(
                    Sets.of(
                        A2_CELL
                    )
                ).setMatchedCells(
                    Sets.of(
                        A2
                    )
                ).setLabels(
                    Sets.of(
                        LABEL_MAPPINGB3
                    )
                ),
            CONTEXT
        );

        // A1_CELL and LABEL_MAPPINGA1A not lost

        this.checkCells(
            cache,
            A1_CELL,
            A2_CELL
        );

        this.checkMatchedCells(
            cache,
            A1,
            A2
        );

        this.checkCellToLabels(
            cache,
            LABEL_MAPPINGA1A,
            LABEL_MAPPINGB3
        );
    }

    @Test
    public void testOnSpreadsheetDeltaTwiceSecondEmpty() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setCells(
                    Sets.of(
                        A1_CELL
                    )
                ).setMatchedCells(
                    Sets.of(
                        A1
                    )
                ).setLabels(
                    Sets.of(
                        LABEL_MAPPINGA1A
                    )
                ).setWindow(WINDOW),
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setWindow(WINDOW),
            CONTEXT
        );

        this.checkCells(
            cache,
            A1_CELL
        );

        this.checkMatchedCells(
            cache,
            A1
        );

        this.checkCellToLabels(
            cache,
            LABEL_MAPPINGA1A
        );
    }

    @Test
    public void testOnSpreadsheetDeltaTwiceSecondDeletedCells() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setCells(
                    Sets.of(
                        A1_CELL,
                        A2_CELL
                    )
                ).setMatchedCells(
                    Sets.of(
                        A1,
                        A2
                    )
                ).setLabels(
                    Sets.of(
                        LABEL_MAPPINGA1A,
                        LABEL_MAPPINGB3
                    )
                ).setWindow(WINDOW),
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setDeletedCells(
                    Sets.of(
                        A1
                    )
                )
                .setWindow(WINDOW),
            CONTEXT
        );

        this.checkCells(
            cache,
            A2_CELL
        );

        this.checkMatchedCells(
            cache,
            A2
        );

        this.checkCellToLabels(
            cache,
            LABEL_MAPPINGB3
        );
    }

    @Test
    public void testOnSpreadsheetDeltaTwiceMergedDifferentSameWindows() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setCells(
                    Sets.of(
                        A1_CELL
                    )
                ).setMatchedCells(
                    Sets.of(
                        A1
                    )
                ).setLabels(
                    Sets.of(
                        LABEL_MAPPINGA1A
                    )
                ).setWindow(WINDOW),
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setCells(
                    Sets.of(
                        A2_CELL
                    )
                ).setMatchedCells(
                    Sets.of(
                        A2
                    )
                ).setLabels(
                    Sets.of(
                        LABEL_MAPPINGB3
                    )
                ).setWindow(WINDOW),
            CONTEXT
        );

        this.checkCells(
            cache,
            A1_CELL,
            A2_CELL
        );

        this.checkMatchedCells(
            cache,
            A1,
            A2
        );

        this.checkCellToLabels(
            cache,
            LABEL_MAPPINGA1A,
            LABEL_MAPPINGB3
        );
    }


    @Test
    public void testOnSpreadsheetDeltaTwiceRowsReplaced() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setRows(
                    Sets.of(
                        ROW_1.setHidden(true),
                        ROW_2.setHidden(true)
                    )
                ),
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
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
                ),
            CONTEXT
        );

        this.checkRows(
            cache,
            ROW_1.setHidden(false),
            ROW_2.setHidden(false)
        );
    }

    @Test
    public void testOnSpreadsheetDeltaTwiceRowsReplaced2() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setRows(
                    Sets.of(
                        ROW_1.setHidden(true),
                        ROW_2.setHidden(true)
                    )
                ),
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
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
                ),
            CONTEXT
        );

        this.checkRows(
            cache,
            ROW_1.setHidden(false),
            ROW_2.setHidden(true)
        );
    }

    @Test
    public void testOnSpreadsheetDeltaTwiceRowsDeleted() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setRows(
                    Sets.of(
                        ROW_1,
                        ROW_2
                    )
                ),
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
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
                ),
            CONTEXT
        );

        this.checkRows(
            cache,
            ROW_2
        );
    }

    // columnWidth......................................................................................................

    @Test
    public void testOnSpreadsheetDeltaColumnWidths() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setColumnWidths(
                    Maps.of(
                        A, 10.0,
                        B, 20.0
                    )
                ),
            CONTEXT
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
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setColumnWidths(
                    Maps.of(
                        A, 10.0,
                        B, 20.0,
                        C, 30.0
                    )
                ),
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setColumnWidths(
                    Maps.of(
                        A, 100.0,
                        B, 200.0
                    )
                ),
            CONTEXT
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

    // cells............................................................................................................

    @Test
    public void testCellsWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.viewportCacheAndOpen()
                .cells(null)
        );
    }

    @Test
    public void testCells() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
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
                ),
            CONTEXT
        );

        this.checkCells(
            cache,
            A1_CELL,
            A2_CELL,
            A3_CELL
        );

        this.iterateAndCheck(
            cache.cells(
                SpreadsheetSelection.parseCellRange("A1:A2")
            ),
            A1_CELL,
            A2_CELL
        );

        this.iterateAndCheck(
            cache.cells(
                SpreadsheetSelection.parseCellRange("A2:A3")
            ),
            A2_CELL,
            A3_CELL
        );
    }

    // cells............................................................................................................

    @Test
    public void testCellRangeWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.viewportCacheAndOpen()
                .cellRange(null)
        );
    }

    @Test
    public void testCellRange() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
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
                ),
            CONTEXT
        );

        this.checkCells(
            cache,
            A1_CELL,
            A2_CELL,
            A3_CELL
        );

        final SpreadsheetCellRangeReference range = SpreadsheetSelection.parseCellRange("A1:A2");

        this.cellRangeAndCheck(
            cache,
            range,
            range.setValue(
                Sets.of(
                    A1_CELL,
                    A2_CELL
                )
            )
        );
    }

    @Test
    public void testCellRange2() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
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
                ),
            CONTEXT
        );

        this.checkCells(
            cache,
            A1_CELL,
            A2_CELL,
            A3_CELL
        );

        final SpreadsheetCellRangeReference range = SpreadsheetSelection.parseCellRange("A1:A3");

        this.cellRangeAndCheck(
            cache,
            range,
            range.setValue(
                Sets.of(
                    A1_CELL,
                    A2_CELL,
                    A3_CELL
                )
            )
        );
    }

    @Test
    public void testCellRange3() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
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
                ),
            CONTEXT
        );

        this.checkCells(
            cache,
            A1_CELL,
            A2_CELL,
            A3_CELL
        );

        final SpreadsheetCellRangeReference range = SpreadsheetSelection.parseCellRange("A2");

        this.cellRangeAndCheck(
            cache,
            range,
            range.setValue(
                Sets.of(
                    A2_CELL
                )
            )
        );
    }

    @Test
    public void testCellRange4() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
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
                ),
            CONTEXT
        );

        this.checkCells(
            cache,
            A1_CELL,
            A2_CELL,
            A3_CELL
        );

        final SpreadsheetCellRangeReference range = SpreadsheetSelection.parseCellRange("B1:Z99");

        this.cellRangeAndCheck(
            cache,
            range,
            range.setValue(
                Sets.empty()
            )
        );
    }

    private void cellRangeAndCheck(final SpreadsheetViewportCache cache,
                                   final SpreadsheetCellRangeReference range,
                                   final SpreadsheetCellRange expected) {
        this.checkEquals(
            expected,
            cache.cellRange(range),
            () -> cache + " " + range
        );
    }

    // columnWidth......................................................................................................

    @Test
    public void testColumnWidthNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.viewportCacheAndOpen()
                .columnWidth(null)
        );
    }

    @Test
    public void testColumnWidthMissingDefaulted() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setColumnWidths(
                    Maps.of(
                        A, 10.0,
                        B, 20.0
                    )
                ),
            CONTEXT
        );

        final double width = 10;
        final double height = 20;

        cache.onSpreadsheetMetadata(
            SpreadsheetMetadata.EMPTY
                .set(SpreadsheetMetadataPropertyName.SPREADSHEET_ID, ID1)
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
                ),
            CONTEXT
        );

        this.columnsWidthAndCheck(
            cache,
            SpreadsheetSelection.parseColumn("Z"),
            width
        );
    }

    // labelMappings....................................................................................................

    @Test
    public void testOnSpreadsheetDeltaLabelMappings() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setLabels(
                    Sets.of(
                        LABEL_MAPPINGA1A,
                        LABEL_MAPPINGA1B,
                        LABEL_MAPPINGB3
                    )
                ),
            CONTEXT
        );

        this.checkLabelMappings(
            cache,
            LABEL_MAPPINGA1A,
            LABEL_MAPPINGA1B,
            LABEL_MAPPINGB3
        );
    }

    @Test
    public void testOnSpreadsheetDeltaLabelMappingsReplaces() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setLabels(
                    Sets.of(
                        LABEL_MAPPINGA1A
                    )
                ),
            CONTEXT
        );

        this.checkLabelMappings(
            cache,
            LABEL_MAPPINGA1A
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setLabels(
                    Sets.of(
                        LABEL_MAPPINGA1B,
                        LABEL_MAPPINGB3
                    )
                ),
            CONTEXT
        );

        this.checkLabelMappings(
            cache,
            LABEL_MAPPINGA1B,
            LABEL_MAPPINGB3
        );
    }

    // rowHeight......................................................................................................

    @Test
    public void testOnSpreadsheetDeltaRowHeights() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setRowHeights(
                    Maps.of(
                        ROW_REF_1, 10.0,
                        ROW_REF_2, 20.0
                    )
                ),
            CONTEXT
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
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setRowHeights(
                    Maps.of(
                        ROW_REF_1, 10.0,
                        ROW_REF_2, 20.0,
                        ROW_REF_3, 30.0
                    )
                ),
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setRowHeights(
                    Maps.of(
                        ROW_REF_1, 100.0,
                        ROW_REF_2, 200.0
                    )
                ),
            CONTEXT
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
            () -> this.viewportCacheAndOpen()
                .rowHeight(null)
        );
    }

    @Test
    public void testRowHeightMissingDefaulted() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setRowHeights(
                    Maps.of(
                        ROW_REF_1, 10.0,
                        ROW_REF_2, 20.0
                    )
                ),
            CONTEXT
        );

        final double width = 10;
        final double height = 20;

        cache.onSpreadsheetMetadata(
            SpreadsheetMetadata.EMPTY
                .set(SpreadsheetMetadataPropertyName.SPREADSHEET_ID, ID1)
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
                ),
            CONTEXT
        );

        this.rowsHeightAndCheck(
            cache,
            SpreadsheetSelection.parseRow("99"),
            height
        );
    }

    @Test
    public void testNonLabelSelectionLost() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setLabels(
                Sets.of(
                    SpreadsheetSelection.labelName("LostLabel")
                        .setLabelMappingTarget(A1)
                )
            ),
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setDeletedCells(
                Sets.of(
                    A1
                )
            ),
            CONTEXT
        );

        this.resolveIfLabelFails(
            cache,
            LABEL999
        );
    }

    @Test
    public void testNonLabelSelectionCell() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setLabels(
                Sets.of(
                    SpreadsheetSelection.labelName("LostLabel")
                        .setLabelMappingTarget(A1)
                )
            ),
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setDeletedCells(
                Sets.of(
                    A1
                )
            ).setLabels(
                Sets.of(
                    LABEL_MAPPINGB3
                )
            ),
            CONTEXT
        );

        this.resolveIfLabelAndCheck(
            cache,
            A1,
            A1
        );

        this.resolveIfLabelAndCheck(
            cache,
            A2,
            A2
        );
    }

    @Test
    public void testNonLabelSelectionCellRange() {
        this.testNonLabelSelectionCellRange(
            SpreadsheetSelection.parseCellRange("A1:B2")
        );
    }

    @Test
    public void testNonLabelSelectionColumn() {
        this.testNonLabelSelectionCellRange(
            SpreadsheetSelection.parseColumn("A")
        );
    }

    @Test
    public void testNonLabelSelectionColumnRange() {
        this.testNonLabelSelectionCellRange(
            SpreadsheetSelection.parseColumnRange("A:B")
        );
    }

    @Test
    public void testNonLabelSelectionRow() {
        this.testNonLabelSelectionCellRange(
            SpreadsheetSelection.parseRow("1")
        );
    }

    @Test
    public void testNonLabelSelectionRowRange() {
        this.testNonLabelSelectionCellRange(
            SpreadsheetSelection.parseRowRange("2:3")
        );
    }

    private void testNonLabelSelectionCellRange(final SpreadsheetSelection nonLabel) {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setLabels(
                    Sets.of(
                        SpreadsheetSelection.labelName("LostLabel")
                            .setLabelMappingTarget(A1)
                    )
                ),
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setDeletedCells(
                    Sets.of(
                        A1
                    )
                )
                .setLabels(
                    Sets.of(
                        LABEL_MAPPINGB3
                    )
                ),
            CONTEXT
        );

        this.resolveIfLabelAndCheck(
            cache,
            nonLabel,
            nonLabel
        );
    }

    @Test
    public void testNonLabelSelectionLabelToCell() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setLabels(
                    Sets.of(
                        SpreadsheetSelection.labelName("LostLabel")
                            .setLabelMappingTarget(A1)
                    )
                ),
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setDeletedCells(
                    Sets.of(
                        A1
                    )
                )
                .setLabels(
                    Sets.of(
                        LABEL_MAPPINGB3
                    )
                ),
            CONTEXT
        );

        this.resolveIfLabelAndCheck(
            cache,
            LABEL3,
            B3
        );
    }

    @Test
    public void testNonLabelSelectionLabelToCell2() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setLabels(
                    Sets.of(
                        SpreadsheetSelection.labelName("LostLabel")
                            .setLabelMappingTarget(A1)
                    )
                ),
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
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
                ),
            CONTEXT
        );

        this.checkCellToLabels(
            cache,
            LABEL_MAPPINGA1A,
            LABEL_MAPPINGA1B
        );

        this.resolveIfLabelAndCheck(
            cache,
            LABEL1,
            A1
        );

        this.resolveIfLabelAndCheck(
            cache,
            LABEL2,
            A1
        );
    }


    @Test
    public void testNonLabelSelectionLabelToCellRange() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setLabels(
                    Sets.of(
                        SpreadsheetSelection.labelName("LostLabel")
                            .setLabelMappingTarget(A1)
                    )
                ),
            CONTEXT
        );

        final SpreadsheetCellRangeReference b3b4 = SpreadsheetSelection.parseCellRange("B3:B4");

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setDeletedCells(
                    Sets.of(
                        A1
                    )
                )
                .setLabels(
                    Sets.of(
                        LABEL999.setLabelMappingTarget(b3b4)
                    )
                ),
            CONTEXT
        );

        this.checkCellToLabels(
            cache,
            Maps.of(
                B3,
                Sets.of(
                    LABEL999
                ),
                B4,
                Sets.of(
                    LABEL999
                )
            )
        );

        this.resolveIfLabelAndCheck(
            cache,
            LABEL999,
            b3b4
        );

        this.resolveIfLabelAndCheck(
            cache,
            B3,
            B3
        );
    }

    @Test
    public void testNonLabelSelectionLabelToLabelToCell() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY,
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setDeletedCells(
                    Sets.of(
                        A1
                    )
                )
                .setLabels(
                    Sets.of(
                        LABEL_MAPPINGB3,
                        LABEL999_LABEL_MAPPINGB3
                    )
                ),
            CONTEXT
        );

        this.checkCellToLabels(
            cache,
            Maps.of(
                B3,
                Sets.of(
                    LABEL3,
                    LABEL999
                )
            )
        );

        this.resolveIfLabelAndCheck(
            cache,
            LABEL999,
            B3
        );

        this.resolveIfLabelAndCheck(
            cache,
            LABEL3,
            B3
        );
    }

    @Test
    public void testNonLabelSelectionLabelToLabelToCellRange() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY,
            CONTEXT
        );

        final SpreadsheetCellRangeReference b3b4 = SpreadsheetSelection.parseCellRange("B3:B4");

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setDeletedCells(
                    Sets.of(
                        A1
                    )
                )
                .setLabels(
                    Sets.of(
                        LABEL3.setLabelMappingTarget(b3b4),
                        LABEL999_LABEL_MAPPINGB3
                    )
                ),
            CONTEXT
        );

        this.checkCellToLabels(
            cache,
            Maps.of(
                B3,
                Sets.of(
                    LABEL3,
                    LABEL999
                ),
                B4,
                Sets.of(
                    LABEL3,
                    LABEL999
                )
            )
        );

        this.resolveIfLabelAndCheck(
            cache,
            LABEL999,
            b3b4
        );

        this.resolveIfLabelAndCheck(
            cache,
            LABEL3,
            b3b4
        );
    }

    @Test
    public void testNonLabelSelectionLabelToLabelToCell2() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY,
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setDeletedCells(
                    Sets.of(
                        A1
                    )
                )
                .setLabels(
                    Sets.of(
                        LABEL999_LABEL_MAPPINGB3,
                        LABEL_MAPPINGB3
                    )
                ),
            CONTEXT
        );

        this.checkCellToLabels(
            cache,
            Maps.of(
                B3,
                Sets.of(
                    LABEL3,
                    LABEL999
                )
            )
        );

        this.resolveIfLabelAndCheck(
            cache,
            LABEL3,
            B3
        );

        this.resolveIfLabelAndCheck(
            cache,
            LABEL999,
            B3
        );
    }

    @Test
    public void testNonLabelSelectionLabelToLabelToCell3() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setLabels(
                    Sets.of(
                        SpreadsheetSelection.labelName("LostLabel")
                            .setLabelMappingTarget(A1)
                    )
                ),
            CONTEXT
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setDeletedCells(
                    Sets.of(
                        A1
                    )
                )
                .setLabels(
                    Sets.of(
                        LABEL_MAPPINGA1A,
                        LABEL_MAPPINGA1B,
                        LABEL_MAPPINGB3,
                        LABEL999_LABEL_MAPPINGB3
                    )
                ),
            CONTEXT
        );

        this.checkCellToLabels(
            cache,
            Maps.of(
                A1,
                Sets.of(
                    LABEL1,
                    LABEL2
                ),
                B3,
                Sets.of(
                    LABEL3,
                    LABEL999
                )
            )
        );

        this.resolveIfLabelAndCheck(
            cache,
            LABEL1,
            A1
        );

        this.resolveIfLabelAndCheck(
            cache,
            LABEL2,
            A1
        );

        this.resolveIfLabelAndCheck(
            cache,
            LABEL3,
            B3
        );

        this.resolveIfLabelAndCheck(
            cache,
            LABEL999,
            B3
        );
    }

    @Test
    public void testNonLabelSelectionLabelToLabelToLabelToCell() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setLabels(
                    Sets.of(
                        SpreadsheetSelection.labelName("LostLabel")
                            .setLabelMappingTarget(A1)
                    )
                ),
            CONTEXT
        );

        final SpreadsheetLabelName third = SpreadsheetSelection.labelName("LabelToLabel999");

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setDeletedCells(
                    Sets.of(
                        A1
                    )
                )
                .setLabels(
                    Sets.of(
                        LABEL_MAPPINGA1A,
                        LABEL_MAPPINGA1B,
                        LABEL_MAPPINGB3,
                        LABEL999_LABEL_MAPPINGB3,
                        third.setLabelMappingTarget(LABEL999)
                    )
                ),
            CONTEXT
        );

        this.checkCellToLabels(
            cache,
            Maps.of(
                A1,
                Sets.of(
                    LABEL1,
                    LABEL2
                ),
                B3,
                Sets.of(
                    LABEL3,
                    LABEL999,
                    third
                )
            )
        );

        this.resolveIfLabelAndCheck(
            cache,
            LABEL1,
            A1
        );

        this.resolveIfLabelAndCheck(
            cache,
            LABEL2,
            A1
        );

        this.resolveIfLabelAndCheck(
            cache,
            LABEL3,
            B3
        );

        this.resolveIfLabelAndCheck(
            cache,
            LABEL999,
            B3
        );

        this.resolveIfLabelAndCheck(
            cache,
            third,
            B3
        );
    }

    // labelMappings...................................................................................................

    @Test
    public void testLabelMappingsEmpty() {
        this.labelMappingsAndCheck(
            this.viewportCacheAndOpen(),
            SpreadsheetSelection.A1
        );
    }

    @Test
    public void testLabelMappingsOutside() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setLabels(
                Sets.of(
                    LABEL_MAPPINGA1A
                )
            ),
            CONTEXT
        );

        this.labelMappingsAndCheck(
            cache,
            SpreadsheetSelection.parseCell("Z99")
        );
    }

    @Test
    public void testLabelMappingsCell() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setLabels(
                Sets.of(
                    LABEL_MAPPINGA1A,
                    LABEL_MAPPINGA1B
                )
            ),
            CONTEXT
        );

        this.labelMappingsAndCheck(
            cache,
            SpreadsheetSelection.A1,
            LABEL_MAPPINGA1A,
            LABEL_MAPPINGA1B
        );
    }

    @Test
    public void testLabelMappingsCellRange() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setLabels(
                Sets.of(
                    LABEL_MAPPINGA1A,
                    LABEL_MAPPINGA1B,
                    LABEL_MAPPINGB3
                )
            ),
            CONTEXT
        );

        this.labelMappingsAndCheck(
            cache,
            SpreadsheetSelection.parseCellRange("A1:B2"),
            LABEL_MAPPINGA1A,
            LABEL_MAPPINGA1B
        );
    }

    @Test
    public void testLabelMappingsCellRange2() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setLabels(
                Sets.of(
                    LABEL_MAPPINGB3
                )
            ),
            CONTEXT
        );

        this.labelMappingsAndCheck(
            cache,
            SpreadsheetSelection.parseCellRange("B2:C4"),
            LABEL_MAPPINGB3
        );
    }

    @Test
    public void testLabelMappingsLabel() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        final SpreadsheetLabelMapping labelToLabel = LABEL999.setLabelMappingTarget(
            LABEL_MAPPINGA1A.label()
        );

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setLabels(
                Sets.of(
                    LABEL_MAPPINGA1A,
                    LABEL_MAPPINGA1B,
                    labelToLabel
                )
            ),
            CONTEXT
        );

        this.labelMappingsAndCheck(
            cache,
            LABEL999,
            LABEL_MAPPINGA1A,
            LABEL_MAPPINGA1B,
            labelToLabel
        );
    }

    private void labelMappingsAndCheck(final SpreadsheetViewportCache cache,
                                       final SpreadsheetSelection selection,
                                       final SpreadsheetLabelMapping... mappings) {
        this.labelMappingsAndCheck(
            cache,
            selection,
            Sets.of(mappings)
        );
    }

    private void labelMappingsAndCheck(final SpreadsheetViewportCache cache,
                                       final SpreadsheetSelection selection,
                                       final Set<SpreadsheetLabelMapping> mappings) {
        this.checkEquals(
            mappings,
            cache.labelMappings(selection),
            () -> cache + " labelMappings " + selection
        );
    }

    // selectionSummary.................................................................................................

    @Test
    public void testSelectionSummaryNoSelection() {
        final AppContext context = this.context();
        final SpreadsheetViewportCache cache = context.spreadsheetViewportCache();

        this.selectionSummaryAndCheck(
            cache,
            SpreadsheetSelectionSummary.EMPTY
        );
    }

    private final static SpreadsheetName NAME = SpreadsheetName.with("Spreadsheet123");

    @Test
    public void testSelectionSummaryChangeClearsSelection() {
        final TestAppContext context = this.context();
        final SpreadsheetViewportCache cache = context.spreadsheetViewportCache();

        context.pushHistoryToken(
            HistoryToken.unknown(
                UrlFragment.with("/hello")
            )
        );

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                ID1,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );

        context.pushHistoryToken(
            HistoryToken.unknown(
                UrlFragment.with("/hello")
            )
        );

        this.selectionSummaryAndCheck(
            cache,
            SpreadsheetSelectionSummary.EMPTY
        );
    }

    @Test
    public void testSelectionSummarySpreadsheetFormatterSelector() {
        final TestAppContext context = this.context();
        final SpreadsheetViewportCache cache = context.spreadsheetViewportCache();

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                ID1,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:B2")
                    .setDefaultAnchor()
            )
        );

        final SpreadsheetFormatterSelector date = SpreadsheetPattern.parseDateFormatPattern("yyyy/mm/dd")
            .spreadsheetFormatterSelector();
        final SpreadsheetFormatterSelector text = SpreadsheetPattern.parseTextFormatPattern("@@@")
            .spreadsheetFormatterSelector();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY
                    ).setFormatter(
                        Optional.of(date)
                    ),
                    SpreadsheetSelection.parseCell("A2")
                        .setFormula(
                            SpreadsheetFormula.EMPTY
                        ).setFormatter(
                            Optional.of(date)
                        ),
                    SpreadsheetSelection.parseCell("B1")
                        .setFormula(
                            SpreadsheetFormula.EMPTY
                        ),
                    SpreadsheetSelection.parseCell("C3")
                        .setFormula(
                            SpreadsheetFormula.EMPTY
                        ).setFormatter(
                            Optional.of(text)
                        )
                )
            ),
            context
        );

        this.selectionSummaryAndCheck(
            cache,
            SpreadsheetSelectionSummary.with(
                Optional.of(date), // format
                Optional.empty(), // parse
                TextStyle.EMPTY // style
            )
        );
    }

    @Test
    public void testSelectionSummarySpreadsheetFormatterSelectorClash() {
        final TestAppContext context = this.context();
        final SpreadsheetViewportCache cache = context.spreadsheetViewportCache();

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                ID1,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:B2")
                    .setDefaultAnchor()
            )
        );

        final SpreadsheetFormatterSelector date = SpreadsheetPattern.parseDateFormatPattern("yyyy/mm/dd")
            .spreadsheetFormatterSelector();
        final SpreadsheetFormatterSelector text = SpreadsheetPattern.parseTextFormatPattern("@@@")
            .spreadsheetFormatterSelector();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY
                    ).setFormatter(
                        Optional.of(date)
                    ),
                    SpreadsheetSelection.parseCell("A2")
                        .setFormula(
                            SpreadsheetFormula.EMPTY
                        ).setFormatter(
                            Optional.of(text)
                        )
                )
            ),
            context
        );

        this.selectionSummaryAndCheck(
            cache,
            SpreadsheetSelectionSummary.with(
                Optional.empty(), // format
                Optional.empty(), // parse
                TextStyle.EMPTY // style
            )
        );
    }

    @Test
    public void testSelectionSummarySpreadsheetParserSelector() {
        final TestAppContext context = this.context();
        final SpreadsheetViewportCache cache = context.spreadsheetViewportCache();

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                ID1,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:B2")
                    .setDefaultAnchor()
            )
        );

        final SpreadsheetParserSelector date = SpreadsheetPattern.parseDateParsePattern("yyyy/mm/dd")
            .spreadsheetParserSelector();
        final SpreadsheetParserSelector time = SpreadsheetPattern.parseTimeParsePattern("hh/mm/ss")
            .spreadsheetParserSelector();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY
                    ).setParser(
                        Optional.of(date)
                    ),
                    SpreadsheetSelection.parseCell("A2")
                        .setFormula(
                            SpreadsheetFormula.EMPTY
                        ).setParser(
                            Optional.of(date)
                        ),
                    SpreadsheetSelection.parseCell("B1")
                        .setFormula(
                            SpreadsheetFormula.EMPTY
                        ),
                    SpreadsheetSelection.parseCell("C3")
                        .setFormula(
                            SpreadsheetFormula.EMPTY
                        ).setParser(
                            Optional.of(time)
                        )
                )
            ),
            context
        );

        this.selectionSummaryAndCheck(
            cache,
            SpreadsheetSelectionSummary.with(
                Optional.empty(), // format
                Optional.of(date), // parse
                TextStyle.EMPTY // style
            )
        );
    }

    @Test
    public void testSelectionSummarySpreadsheetParserSelectorClash() {
        final TestAppContext context = this.context();
        final SpreadsheetViewportCache cache = context.spreadsheetViewportCache();

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                ID1,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:B2")
                    .setDefaultAnchor()
            )
        );

        final SpreadsheetParserSelector date = SpreadsheetPattern.parseDateParsePattern("yyyy/mm/dd")
            .spreadsheetParserSelector();
        final SpreadsheetParserSelector time = SpreadsheetPattern.parseTimeParsePattern("hh/mm/ss")
            .spreadsheetParserSelector();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY
                    ).setParser(
                        Optional.of(date)
                    ),
                    SpreadsheetSelection.parseCell("A2")
                        .setFormula(
                            SpreadsheetFormula.EMPTY
                        ).setParser(
                            Optional.of(time)
                        )
                )
            ),
            context
        );

        this.selectionSummaryAndCheck(
            cache,
            SpreadsheetSelectionSummary.with(
                Optional.empty(), // format
                Optional.empty(), // parse
                TextStyle.EMPTY // style
            )
        );
    }

    @Test
    public void testSelectionSummaryStyle() {
        final TestAppContext context = this.context();
        final SpreadsheetViewportCache cache = context.spreadsheetViewportCache();

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                ID1,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:B2")
                    .setDefaultAnchor()
            )
        );

        final TextStylePropertyName<Color> colorName = TextStylePropertyName.COLOR;
        final Color color1 = Color.parse("#111");
        final Color color2 = Color.parse("#222");

        final TextStylePropertyName<TextAlign> textAlign = TextStylePropertyName.TEXT_ALIGN;
        final TextAlign align1 = TextAlign.LEFT;
        final TextAlign align2 = TextAlign.RIGHT;

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY
                    ).setStyle(
                        TextStyle.EMPTY.set(colorName, color1)
                            .set(textAlign, align1)
                    ),
                    SpreadsheetSelection.parseCell("A2")
                        .setFormula(
                            SpreadsheetFormula.EMPTY
                        ).setStyle(
                            TextStyle.EMPTY.set(colorName, color1)
                                .set(textAlign, align1)
                        ),
                    SpreadsheetSelection.parseCell("B1")
                        .setFormula(
                            SpreadsheetFormula.EMPTY
                        ).setStyle(
                            TextStyle.EMPTY
                        ),
                    SpreadsheetSelection.parseCell("C3")
                        .setFormula(
                            SpreadsheetFormula.EMPTY
                        ).setStyle(
                            TextStyle.EMPTY.set(colorName, color2)
                                .set(textAlign, align2)
                        )
                )
            ),
            context
        );

        this.selectionSummaryAndCheck(
            cache,
            SpreadsheetSelectionSummary.with(
                Optional.empty(), // format
                Optional.empty(), // parse
                TextStyle.EMPTY.set(
                    colorName,
                    color1
                ).set(
                    textAlign,
                    align1
                ) // style
            )
        );
    }

    @Test
    public void testSelectionSummaryStyleAllClash() {
        final TestAppContext context = this.context();
        final SpreadsheetViewportCache cache = context.spreadsheetViewportCache();

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                ID1,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:B2")
                    .setDefaultAnchor()
            )
        );

        final TextStylePropertyName<Color> colorName = TextStylePropertyName.COLOR;
        final Color color1 = Color.parse("#111");
        final Color color2 = Color.parse("#222");

        final TextStylePropertyName<TextAlign> textAlign = TextStylePropertyName.TEXT_ALIGN;
        final TextAlign align1 = TextAlign.LEFT;
        final TextAlign align2 = TextAlign.RIGHT;

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY
                    ).setStyle(
                        TextStyle.EMPTY.set(colorName, color1)
                            .set(textAlign, align1)
                    ),
                    SpreadsheetSelection.parseCell("A2")
                        .setFormula(
                            SpreadsheetFormula.EMPTY
                        ).setStyle(
                            TextStyle.EMPTY.set(colorName, color2)
                                .set(textAlign, align2)
                        )
                )
            ),
            context
        );

        this.selectionSummaryAndCheck(
            cache,
            SpreadsheetSelectionSummary.with(
                Optional.empty(), // format
                Optional.empty(), // parse
                TextStyle.EMPTY // style
            )
        );
    }

    @Test
    public void testSelectionSummaryStyleSomeClashes() {
        final TestAppContext context = this.context();
        final SpreadsheetViewportCache cache = context.spreadsheetViewportCache();

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                ID1,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:B2")
                    .setDefaultAnchor()
            )
        );

        final TextStylePropertyName<Color> colorName = TextStylePropertyName.COLOR;
        final Color color1 = Color.parse("#111");

        final TextStylePropertyName<TextAlign> textAlign = TextStylePropertyName.TEXT_ALIGN;
        final TextAlign align1 = TextAlign.LEFT;
        final TextAlign align2 = TextAlign.RIGHT;

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY
                    ).setStyle(
                        TextStyle.EMPTY.set(colorName, color1)
                            .set(textAlign, align1)
                    ),
                    SpreadsheetSelection.parseCell("A2")
                        .setFormula(
                            SpreadsheetFormula.EMPTY
                        ).setStyle(
                            TextStyle.EMPTY.set(colorName, color1)
                                .set(textAlign, align2)
                        )
                )
            ),
            context
        );

        this.selectionSummaryAndCheck(
            cache,
            SpreadsheetSelectionSummary.with(
                Optional.empty(), // format
                Optional.empty(), // parse
                TextStyle.EMPTY.set(
                    colorName,
                    color1
                ) // style
            )
        );
    }

    @Test
    public void testSelectionSummaryHistoryTokenDeltaHistoryTokenDifferentSelection() {
        final TestAppContext context = this.context();
        final SpreadsheetViewportCache cache = context.spreadsheetViewportCache();

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                ID1,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:B2")
                    .setDefaultAnchor()
            )
        );

        final SpreadsheetFormatterSelector formatter1 = SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy")
            .spreadsheetFormatterSelector();
        final SpreadsheetFormatterSelector formatter2 = SpreadsheetPattern.parseNumberFormatPattern("$0.00")
            .spreadsheetFormatterSelector();

        final TextStylePropertyName<Color> colorName = TextStylePropertyName.COLOR;
        final Color color1 = Color.parse("#111");

        final TextStylePropertyName<TextAlign> textAlign = TextStylePropertyName.TEXT_ALIGN;
        final TextAlign align1 = TextAlign.LEFT;
        final TextAlign align2 = TextAlign.RIGHT;

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY
                    ).setFormatter(
                        Optional.of(formatter1)
                    ).setStyle(
                        TextStyle.EMPTY.set(colorName, color1)
                            .set(textAlign, align1)
                    ),
                    SpreadsheetSelection.parseCell("A2")
                        .setFormula(
                            SpreadsheetFormula.EMPTY
                        ).setFormatter(
                            Optional.of(formatter2)
                        ).setStyle(
                            TextStyle.EMPTY.set(colorName, color1)
                                .set(textAlign, align2)
                        )
                )
            ),
            context
        );

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                ID1,
                NAME,
                SpreadsheetSelection.parseCellRange("A2:B2")
                    .setDefaultAnchor()
            )
        );

        this.selectionSummaryAndCheck(
            cache,
            SpreadsheetSelectionSummary.with(
                Optional.of(formatter2), // format
                Optional.empty(), // parse
                TextStyle.EMPTY.set(
                    colorName,
                    color1
                ).set(
                    textAlign,
                    align2
                ) // style
            )
        );
    }

    @Test
    public void testSelectionSummaryHistoryTokenDeltaHistoryTokenClearSelection() {
        final TestAppContext context = this.context();
        final SpreadsheetViewportCache cache = context.spreadsheetViewportCache();

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                ID1,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:B2")
                    .setDefaultAnchor()
            )
        );

        final SpreadsheetFormatterSelector formatter1 = SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy")
            .spreadsheetFormatterSelector();
        final SpreadsheetFormatterSelector formatter2 = SpreadsheetPattern.parseNumberFormatPattern("$0.00")
            .spreadsheetFormatterSelector();

        final TextStylePropertyName<Color> colorName = TextStylePropertyName.COLOR;
        final Color color1 = Color.parse("#111");

        final TextStylePropertyName<TextAlign> textAlign = TextStylePropertyName.TEXT_ALIGN;
        final TextAlign align1 = TextAlign.LEFT;
        final TextAlign align2 = TextAlign.RIGHT;

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY
                    ).setFormatter(
                        Optional.of(formatter1)
                    ).setStyle(
                        TextStyle.EMPTY.set(colorName, color1)
                            .set(textAlign, align1)
                    ),
                    SpreadsheetSelection.parseCell("A2")
                        .setFormula(
                            SpreadsheetFormula.EMPTY
                        ).setFormatter(
                            Optional.of(formatter2)
                        ).setStyle(
                            TextStyle.EMPTY.set(colorName, color1)
                                .set(textAlign, align2)
                        )
                )
            ),
            context
        );

        context.pushHistoryToken(
            HistoryToken.labelMappingSelect(
                ID1,
                NAME,
                Optional.of(
                    SpreadsheetSelection.labelName("Label123")
                )
            )
        );

        this.selectionSummaryAndCheck(
            cache,
            SpreadsheetSelectionSummary.EMPTY
        );
    }

    @Test
    public void testSelectionSummaryHistoryTokenDeltaDelta() {
        final TestAppContext context = this.context();
        final SpreadsheetViewportCache cache = context.spreadsheetViewportCache();

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                ID1,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:B2")
                    .setDefaultAnchor()
            )
        );

        final SpreadsheetFormatterSelector formatter1 = SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy")
            .spreadsheetFormatterSelector();
        final SpreadsheetFormatterSelector formatter2 = SpreadsheetPattern.parseNumberFormatPattern("$0.00")
            .spreadsheetFormatterSelector();

        final TextStylePropertyName<Color> colorName = TextStylePropertyName.COLOR;
        final Color color1 = Color.parse("#111");

        final TextStylePropertyName<TextAlign> textAlign = TextStylePropertyName.TEXT_ALIGN;
        final TextAlign align1 = TextAlign.LEFT;

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY
                    ).setFormatter(
                        Optional.of(formatter1)
                    ).setStyle(
                        TextStyle.EMPTY.set(colorName, color1)
                            .set(textAlign, align1)
                    ),
                    SpreadsheetSelection.parseCell("A2")
                        .setFormula(
                            SpreadsheetFormula.EMPTY
                        ).setFormatter(
                            Optional.of(formatter2)
                        ).setStyle(
                            TextStyle.EMPTY.set(colorName, color1)
                                .set(textAlign, align1)
                        )
                )
            ),
            context
        );

        final TextAlign align2 = TextAlign.RIGHT;

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY
                    ).setFormatter(
                        Optional.of(formatter1)
                    ).setStyle(
                        TextStyle.EMPTY.set(colorName, color1)
                            .set(textAlign, align2)
                    ),
                    SpreadsheetSelection.parseCell("A2")
                        .setFormula(
                            SpreadsheetFormula.EMPTY
                        ).setFormatter(
                            Optional.of(formatter2)
                        ).setStyle(
                            TextStyle.EMPTY.set(colorName, color1)
                                .set(textAlign, align2)
                        )
                )
            ),
            context
        );

        this.selectionSummaryAndCheck(
            cache,
            SpreadsheetSelectionSummary.with(
                Optional.empty(), // format
                Optional.empty(), // parse
                TextStyle.EMPTY.set(colorName, color1)
                    .set(textAlign, align2)
            )
        );
    }

    @Test
    public void testSelectionSummaryHistoryTokenDeltaDelta2() {
        final TestAppContext context = this.context();
        final SpreadsheetViewportCache cache = context.spreadsheetViewportCache();
        cache.spreadsheetId = ID1;

        context.pushHistoryToken(
            HistoryToken.cellSelect(
                ID1,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:B2")
                    .setDefaultAnchor()
            )
        );

        final SpreadsheetFormatterSelector formatter1 = SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy")
            .spreadsheetFormatterSelector();
        final SpreadsheetFormatterSelector formatter2 = SpreadsheetPattern.parseNumberFormatPattern("$0.00")
            .spreadsheetFormatterSelector();

        final TextStylePropertyName<Color> colorName = TextStylePropertyName.COLOR;
        final Color color1 = Color.parse("#111");

        final TextStylePropertyName<TextAlign> textAlign = TextStylePropertyName.TEXT_ALIGN;
        final TextAlign align1 = TextAlign.LEFT;

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY
                    ).setFormatter(
                        Optional.of(formatter1)
                    ).setStyle(
                        TextStyle.EMPTY.set(colorName, color1)
                    ),
                    SpreadsheetSelection.parseCell("A2")
                        .setFormula(
                            SpreadsheetFormula.EMPTY
                        ).setFormatter(
                            Optional.of(formatter2)
                        ).setStyle(
                            TextStyle.EMPTY.set(colorName, color1)
                                .set(textAlign, align1)
                        )
                )
            ),
            context
        );

        final TextAlign align2 = TextAlign.RIGHT;

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.parseCell("A2")
                        .setFormula(
                            SpreadsheetFormula.EMPTY
                        ).setFormatter(
                            Optional.of(formatter2)
                        ).setStyle(
                            TextStyle.EMPTY.set(colorName, color1)
                                .set(textAlign, align2)
                        )
                )
            ),
            context
        );

        this.selectionSummaryAndCheck(
            cache,
            SpreadsheetSelectionSummary.with(
                Optional.empty(), // format
                Optional.empty(), // parse
                TextStyle.EMPTY.set(colorName, color1)
                    .set(textAlign, align2)
            )
        );
    }

    private void selectionSummaryAndCheck(final SpreadsheetViewportCache cache,
                                          final SpreadsheetSelectionSummary expected) {
        this.checkEquals(
            expected,
            cache.selectionSummary(),
            cache::toString
        );
    }

    // helpers..........................................................................................................

    private SpreadsheetViewportCache viewportCache() {
        return this.context()
            .spreadsheetViewportCache();
    }

    private SpreadsheetViewportCache viewportCacheAndOpen() {
        final SpreadsheetViewportCache cache = this.viewportCache();
        cache.spreadsheetId = ID1;
        return cache;
    }

    private TestAppContext context() {
        return new TestAppContext();
    }

    static final class TestAppContext extends FakeAppContext {

        @Override
        public void pushHistoryToken(final HistoryToken token) {
            final HistoryToken previous = this.historyToken;
            this.historyToken = token;

            this.historyTokenWatchers.onHistoryTokenChange(
                previous,
                this
            );
        }

        @Override
        public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
            return this.historyTokenWatchers.add(watcher);
        }

        private final HistoryTokenWatchers historyTokenWatchers = HistoryTokenWatchers.empty();

        @Override
        public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
            return null;
        }

        @Override
        public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
            return null;
        }

        @Override
        public HistoryToken historyToken() {
            return this.historyToken;
        }

        private HistoryToken historyToken;

        @Override
        public SpreadsheetViewportCache spreadsheetViewportCache() {
            return this.viewportCache;
        }

        private final SpreadsheetViewportCache viewportCache = SpreadsheetViewportCache.empty(this);

        @Override
        public void debug(final Object... values) {
            // NOP
        }
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

    private void checkMatchedCells(final SpreadsheetViewportCache cache,
                                   final SpreadsheetCellReference... cells) {
        this.checkMatchedCells(
            cache,
            Sets.of(cells)
        );

        for (final SpreadsheetCellReference cell : cells) {
            this.isMatchedCellAndCheck(
                cache,
                cell,
                true
            );
        }
    }

    private void checkMatchedCells(final SpreadsheetViewportCache cache,
                                   final Set<SpreadsheetCellReference> matchedCells) {
        this.checkEquals(
            matchedCells,
            cache.matchedCells,
            "matchedCells"
        );


        final Set<SpreadsheetCellReference> cells = SortedSets.tree();
        cells.addAll(cache.matchedCells);
        cells.removeAll(cache.cells.keySet());

        this.checkEquals(
            Sets.empty(),
            cells,
            "matchedCells should only include cells"
        );
    }

    private void isMatchedCellAndCheck(final SpreadsheetViewportCache cache,
                                       final SpreadsheetCellReference cell,
                                       final boolean expected) {
        this.checkEquals(
            expected,
            cache.isMatchedCell(cell),
            () -> cache + " isMatchedCell " + cell
        );
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
        final Map<SpreadsheetColumnReference, Length<?>> expectedLengths = Maps.ordered();
        for (final Entry<SpreadsheetColumnReference, Double> widths : expected.entrySet()) {
            expectedLengths.put(
                widths.getKey(),
                Length.pixel(widths.getValue())
            );
        }

        this.checkEquals(
            expectedLengths,
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

        this.columnsWidthAndCheck(
            cache,
            column,
            Length.pixel(expected)
        );
    }

    private void columnsWidthAndCheck(final SpreadsheetViewportCache cache,
                                      final SpreadsheetColumnReference column,
                                      final Length<?> expected) {

        this.checkEquals(
            expected,
            cache.columnWidth(column),
            () -> "columnWidth of " + column + " from " + cache
        );
    }

    private void checkLabelMappings(final SpreadsheetViewportCache cache,
                                    final SpreadsheetLabelMapping... mappings) {
        this.checkLabelMappings(
            cache,
            Sets.of(mappings)
        );
    }

    private void checkLabelMappings(final SpreadsheetViewportCache cache,
                                    final Set<SpreadsheetLabelMapping> mappings) {
        this.checkEquals(
            mappings,
            cache.labelMappings(),
            () -> "cache: " + cache
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
        final Map<SpreadsheetRowReference, Length<?>> expectedLengths = Maps.ordered();
        for (final Entry<SpreadsheetRowReference, Double> widths : expected.entrySet()) {
            expectedLengths.put(
                widths.getKey(),
                Length.pixel(widths.getValue())
            );
        }

        this.checkEquals(
            expectedLengths,
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

        this.rowsHeightAndCheck(
            cache,
            row,
            Length.pixel(expected)
        );
    }

    private void rowsHeightAndCheck(final SpreadsheetViewportCache cache,
                                    final SpreadsheetRowReference row,
                                    final Length<?> expected) {

        this.checkEquals(
            expected,
            cache.rowHeight(row),
            () -> "rowHeight of " + row + " from " + cache
        );
    }

    private void checkCellToLabels(final SpreadsheetViewportCache cache,
                                   final SpreadsheetLabelMapping... expected) {
        final Map<SpreadsheetCellReference, Set<SpreadsheetLabelName>> expectedMaps = Maps.ordered();
        for (final SpreadsheetLabelMapping mapping : expected) {
            final SpreadsheetCellReference cell = (SpreadsheetCellReference) mapping.target();
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

        this.checkCellToLabels(
            cache,
            expectedMaps
        );
    }

    private void checkCellToLabels(final SpreadsheetViewportCache cache,
                                   final Map<SpreadsheetCellReference, Set<SpreadsheetLabelName>> expected) {
        this.checkEquals(
            expected,
            cache.cellToLabels,
            "cellToLabels"
        );

        for (final Entry<SpreadsheetCellReference, Set<SpreadsheetLabelName>> entry : expected.entrySet()) {
            final SpreadsheetCellReference cell = entry.getKey();

            this.checkEquals(
                entry.getValue(),
                cache.labels(cell)
            );
        }
    }

    // isColumnHidden...................................................................................................

    @Test
    public void testIsColumnHiddenAbsent() {
        this.isColumnHiddenAndCheck(
            this.viewportCacheAndOpen(),
            A,
            false
        );
    }

    @Test
    public void testIsColumnHiddenPresent() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();
        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setColumns(
                Sets.of(COLUMN_A)
            ),
            CONTEXT
        );
        this.isColumnHiddenAndCheck(
            cache,
            A,
            false
        );
    }

    @Test
    public void testIsColumnHiddenPresentAndHidden() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();
        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setColumns(
                Sets.of(COLUMN_A.setHidden(true))
            ),
            CONTEXT
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
            this.viewportCacheAndOpen(),
            ROW_REF_1,
            false
        );
    }

    @Test
    public void testIsRowHiddenPresent() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();
        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setRows(
                Sets.of(ROW_1)
            ),
            CONTEXT
        );
        this.isRowHiddenAndCheck(
            cache,
            ROW_REF_1,
            false
        );
    }

    @Test
    public void testIsRowHiddenPresentAndHidden() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();
        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY.setRows(
                Sets.of(ROW_1.setHidden(true))
            ),
            CONTEXT
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

    // clear............................................................................................................

    @Test
    public void testOnSpreadsheetDeltaGetAllCells() {
        final SpreadsheetViewportCache cache = this.viewportCacheAndOpen();

        cache.onSpreadsheetDelta(
            METHOD,
            URL_ID1,
            SpreadsheetDelta.EMPTY
                .setCells(
                    Sets.of(
                        A1.setFormula(SpreadsheetFormula.EMPTY.setText("Lost"))
                    )
                ).setMatchedCells(
                    Sets.of(
                        A1
                    )
                ),
            CONTEXT
        );

        // cache should be cleared and A1 above lost.
        cache.onSpreadsheetDelta(
            METHOD,
            Url.parseAbsoluteOrRelative("https://server/api/spreadsheet/" + ID1 + "/cells/*"),
            SpreadsheetDelta.EMPTY
                .setCells(
                    Sets.of(
                        A2_CELL
                    )
                ),
            CONTEXT
        );

        this.checkCells(
            cache,
            A2_CELL
        );

        this.checkMatchedCells(
            cache
        );

        this.isMatchedCellAndCheck(
            cache,
            A2,
            false
        );

        this.resolveIfLabelAndCheck(
            cache,
            A2,
            A2
        );
    }

    // SpreadsheetLabelNameResolver.....................................................................................

    @Override
    public SpreadsheetViewportCache createSpreadsheetLabelNameResolver() {
        return this.viewportCache();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetViewportCache> type() {
        return SpreadsheetViewportCache.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
