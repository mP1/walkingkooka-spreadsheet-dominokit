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

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.color.Color;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetFormatterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportRectangle;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportWindows;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Arrays;
import java.util.Optional;

public final class SpreadsheetViewportComponentTest implements HtmlComponentTesting<SpreadsheetViewportComponent, HTMLDivElement>,
    ParseStringTesting<Optional<SpreadsheetSelection>>,
    SpreadsheetMetadataTesting {

    // id...............................................................................................................

    @Test
    public void testIdWithCell() {
        this.idAndCheck(
            SpreadsheetSelection.A1,
            "viewport-cell-A1"
        );
    }

    @Test
    public void testIdWithColumn() {
        this.idAndCheck(
            SpreadsheetSelection.parseColumn("B"),
            "viewport-column-B"
        );
    }

    @Test
    public void testIdWithRow() {
        this.idAndCheck(
            SpreadsheetSelection.parseRow("3"),
            "viewport-row-3"
        );
    }

    private void idAndCheck(final SpreadsheetSelection selection,
                            final String id) {
        this.checkEquals(
            id,
            SpreadsheetViewportComponent.id(selection),
            () -> selection + " id"
        );
    }

    // parseId.........................................................................................................

    @Override
    public void testParseStringNullFails() {
        throw new UnsupportedOperationException(); // shouldnt be invoked
    }

    @Override
    public void testParseStringEmptyFails() {
        throw new UnsupportedOperationException(); // shouldnt be invoked
    }

    @Test
    public void testParseElementIdWithNull() {
        this.parseStringAndCheck(null);
    }

    @Test
    public void testParseElementIdWithEmpty() {
        this.parseStringAndCheck(
            ""
        );
    }

    @Test
    public void testParseElementIdWithMissingViewportIdPrefix() {
        this.parseStringAndCheck(
            "xyz"
        );
    }

    @Test
    public void testParseElementIdWithInvalidSelectionType() {
        this.parseStringAndCheck(
            "viewport-invalid"
        );
    }

    @Test
    public void testParseElementIdWithInvalidSelectionType2() {
        this.parseStringAndCheck(
            "viewport-invalid-A1"
        );
    }

    @Test
    public void testParseElementIdWithCellRangeFails() {
        this.parseStringAndCheck(
            "viewport-cell-A1:A2"
        );
    }

    @Test
    public void testParseElementIdWithColumnRangeFails() {
        this.parseStringAndCheck(
            "viewport-column-B:C"
        );
    }

    @Test
    public void testParseElementIdWithLabelFails() {
        this.parseStringAndCheck(
            "viewport-cell-Label123"
        );
    }

    @Test
    public void testParseElementIdWithRowRangeFails() {
        this.parseStringAndCheck(
            "viewport-row-4:5"
        );
    }

    private void parseStringAndCheck(final String id) {
        this.parseStringAndCheck(
            id,
            Optional.empty()
        );
    }

    @Test
    public void testParseElementIdWithCell() {
        this.parseStringAndCheck(
            "viewport-cell-A1",
            SpreadsheetSelection.A1
        );
    }

    @Test
    public void testParseElementIdWithColumn() {
        this.parseStringAndCheck(
            "viewport-column-B",
            SpreadsheetSelection.parseColumn("B")
        );
    }

    @Test
    public void testParseElementIdWithRow() {
        this.parseStringAndCheck(
            "viewport-row-3",
            SpreadsheetSelection.parseRow("3")
        );
    }

    private void parseStringAndCheck(final String id,
                                     final SpreadsheetSelection selection) {
        this.parseStringAndCheck(
            id,
            Optional.of(selection)
        );

        this.idAndCheck(
            selection,
            id
        );
    }

    // ParseStringTesting...............................................................................................

    @Override
    public Optional<SpreadsheetSelection> parseString(final String id) {
        return SpreadsheetViewportComponent.parseElementId(id);
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> thrown) {
        return thrown;
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException thrown) {
        return thrown;
    }

    private final static int CELL_WIDTH = 50;
    private final static int CELL_HEIGHT = 50;

    @Test
    public void testTreePrintWithFormulaHeadersScrollbar() {
        final SpreadsheetId spreadsheetId = SpreadsheetId.with(1);
        final SpreadsheetName spreadsheetName = SpreadsheetName.with("SpreadsheetName111");

        final int viewportWidth = CELL_WIDTH * 2 - 1;
        final int viewportHeight = CELL_HEIGHT * 2 - 1;

        final SpreadsheetMetadata metadata = SpreadsheetMetadataTesting.METADATA_EN_AU.set(
            SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
            spreadsheetId
        ).set(
            SpreadsheetMetadataPropertyName.SPREADSHEET_NAME,
            spreadsheetName
        ).set(
            SpreadsheetMetadataPropertyName.HIDE_ZERO_VALUES,
            false
        ).set(
            SpreadsheetMetadataPropertyName.STYLE,
            TextStyle.EMPTY.set(
                TextStylePropertyName.WIDTH,
                Length.parse(CELL_WIDTH + "px")
            ).set(
                TextStylePropertyName.HEIGHT,
                Length.parse(CELL_HEIGHT + "px")
            ).set(
                TextStylePropertyName.BACKGROUND_COLOR,
                Color.WHITE
            )
        ).set(
            SpreadsheetMetadataPropertyName.VIEWPORT,
            SpreadsheetViewportRectangle.with(
                SpreadsheetSelection.A1,
                viewportWidth,
                viewportHeight
            ).viewport()
        );

        final AppContext appContext = new FakeAppContext() {

            @Override
            public HistoryToken historyToken() {
                return HistoryToken.cellSelect(
                    spreadsheetId,
                    spreadsheetName,
                    SpreadsheetSelection.A1.setDefaultAnchor()
                );
            }

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return metadata;
            }

            @Override
            public void debug(final Object... values) {
                System.out.println("DEBUG " + Arrays.toString(values));
            }
        };

        final SpreadsheetViewportCacheContext cacheContext = new FakeSpreadsheetViewportCacheContext() {
            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                return null;
            }
        };

        final SpreadsheetViewportComponentTableContext tableContext = new FakeSpreadsheetViewportComponentTableContext() {

            @Override
            public HistoryToken historyToken() {
                return appContext.historyToken();
            }

            @Override
            public boolean hideZeroValues() {
                return false;
            }

            @Override
            public boolean isShiftKeyDown() {
                return false;
            }

            @Override
            public boolean mustRefresh() {
                return false;
            }

            @Override
            public TextStyle defaultCellStyle() {
                return TextStyle.EMPTY.set(
                    TextStylePropertyName.BACKGROUND_COLOR,
                    Color.WHITE
                ).set(
                    TextStylePropertyName.COLOR,
                    Color.BLACK
                );
            }

            @Override
            public SpreadsheetViewportCache spreadsheetViewportCache() {
                return this.spreadsheetViewportCache;
            }

            private final SpreadsheetViewportCache spreadsheetViewportCache = SpreadsheetViewportCache.empty(cacheContext);

            @Override
            public void debug(final Object... values) {
                appContext.debug(values);
            }
        };

        final SpreadsheetViewportComponent component = SpreadsheetViewportComponent.empty(
            new FakeSpreadsheetViewportComponentContext() {

                @Override
                public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                    return null;
                }

                @Override
                public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
                    return null;
                }

                @Override
                public Runnable addSpreadsheetFormatterFetcherWatcher(final SpreadsheetFormatterFetcherWatcher watcher) {
                    return null;
                }

                @Override
                public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                    return null;
                }

                @Override
                public HistoryToken historyToken() {
                    return tableContext.historyToken();
                }

                @Override
                public SpreadsheetMetadata spreadsheetMetadata() {
                    return appContext.spreadsheetMetadata();
                }

                @Override
                public SpreadsheetViewportCache spreadsheetViewportCache() {
                    return tableContext.spreadsheetViewportCache();
                }

                @Override
                public void debug(final Object... values) {
                    appContext.debug(values);
                }
            }
        );

        component.metadata = metadata;

        final SpreadsheetCellReference selection = SpreadsheetSelection.A1;
        component.spreadsheetFormatterSelectorSelection = selection;

        tableContext.spreadsheetViewportCache()
            .onSpreadsheetMetadata(
                metadata,
                appContext
            );

        tableContext.spreadsheetViewportCache()
            .onSpreadsheetDelta(
                HttpMethod.GET,
                Url.parseRelative("/api/spreadsheet/1/cell/" + selection),
                SpreadsheetDelta.EMPTY.setCells(
                    Sets.of(
                        SpreadsheetSelection.A1.setFormula(
                            SpreadsheetFormula.EMPTY.setText("=1+2")
                        ).setFormattedValue(
                            Optional.of(
                                TextNode.text("*** 3.0")
                            )
                        )
                    )
                ).setWindow(SpreadsheetViewportWindows.parse("A1:B2")),
                appContext
            );

        component.tableWidth = viewportWidth;
        component.tableHeight = viewportHeight;

        component.onHistoryTokenChange(
            appContext.historyToken(),
            appContext
        );

        this.treePrintAndCheck(
            component,
            "SpreadsheetViewportComponent\n" +
                "  SpreadsheetViewportFormulaComponent\n" +
                "    SpreadsheetFormulaComponent\n" +
                "      ValueTextBoxComponent\n" +
                "        TextBoxComponent\n" +
                "          [] DISABLED\n" +
                "  TABLE\n" +
                "    id=\"viewport\" style=\"overflow: hidden;\"\n" +
                "      THEAD\n" +
                "        SpreadsheetViewportComponentTableRowColumnHeaders\n" +
                "          TR\n" +
                "            SpreadsheetViewportComponentTableCellHeaderSelectAll\n" +
                "              TH\n" +
                "                id=\"viewport-select-all-cells\" style=\"background-color: #dddddd; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 30px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 30px; min-width: 80px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: center; vertical-align: middle; width: 80px; word-break: normal;\"\n" +
                "                  \"All\" [#/1/SpreadsheetName111/cell/*/bottom-right] id=viewport-select-all-cells-Link\n" +
                "            SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "              TH\n" +
                "                id=\"viewport-column-A\" style=\"background-color: #bbbbbb; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 30px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 30px; min-width: 50px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: center; vertical-align: middle; width: 50px; word-break: normal;\"\n" +
                "                  \"A\" [#/1/SpreadsheetName111/column/A] id=viewport-column-A-Link\n" +
                "            SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "              TH\n" +
                "                id=\"viewport-column-B\" style=\"background-color: #dddddd; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 30px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 30px; min-width: 50px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: center; vertical-align: middle; width: 50px; word-break: normal;\"\n" +
                "                  \"B\" [#/1/SpreadsheetName111/column/B] id=viewport-column-B-Link\n" +
                "      TBODY\n" +
                "        SpreadsheetViewportComponentTableRowCells\n" +
                "          TR\n" +
                "            SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow\n" +
                "              TH\n" +
                "                id=\"viewport-row-1\" style=\"background-color: #bbbbbb; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 50px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 50px; min-width: 80px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: center; vertical-align: middle; width: 80px; word-break: normal;\"\n" +
                "                  \"1\" [#/1/SpreadsheetName111/row/1] id=viewport-row-1-Link\n" +
                "            SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "              TD\n" +
                "                id=\"viewport-cell-A1\" tabIndex=0 style=\"background-color: #eeeeee; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 50px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 50px; min-width: 50px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: left; vertical-align: top; width: 50px; word-break: normal;\"\n" +
                "                  Text \"*** 3.0\"\n" +
                "            SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "              TD\n" +
                "                id=\"viewport-cell-B1\" tabIndex=0 style=\"background-color: #ffffff; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 50px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 50px; min-width: 50px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: left; vertical-align: top; width: 50px; word-break: normal;\"\n" +
                "        SpreadsheetViewportComponentTableRowCells\n" +
                "          TR\n" +
                "            SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow\n" +
                "              TH\n" +
                "                id=\"viewport-row-2\" style=\"background-color: #dddddd; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 50px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 50px; min-width: 80px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: center; vertical-align: middle; width: 80px; word-break: normal;\"\n" +
                "                  \"2\" [#/1/SpreadsheetName111/row/2] id=viewport-row-2-Link\n" +
                "            SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "              TD\n" +
                "                id=\"viewport-cell-A2\" tabIndex=0 style=\"background-color: #ffffff; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 50px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 50px; min-width: 50px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: left; vertical-align: top; width: 50px; word-break: normal;\"\n" +
                "            SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "              TD\n" +
                "                id=\"viewport-cell-B2\" tabIndex=0 style=\"background-color: #ffffff; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 50px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 50px; min-width: 50px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: left; vertical-align: top; width: 50px; word-break: normal;\"\n" +
                "  SpreadsheetViewportScrollbarComponentRows\n" +
                "    FlexLayoutComponent\n" +
                "      COLUMN\n" +
                "        id=viewport-vertical-scrollbar-Layout\n" +
                "          mdi-arrow-up [#/1/SpreadsheetName111/cell/A1/navigate/A1/up%2099px] id=viewport-vertical-scrollbar-up-Link\n" +
                "          SliderComponent\n" +
                "            VERTICAL\n" +
                "              [0.0] min=1.0 max=1048575.0 id=viewport-vertical-scrollbar-value-Slider\n" +
                "          [#/1/SpreadsheetName111/cell/A1/navigate/A1/down%2099px] mdi-arrow-down id=viewport-vertical-scrollbar-down-Link\n" +
                "    SpreadsheetViewportScrollbarComponentColumns\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          id=viewport-horizontal-scrollbar-Layout\n" +
                "            mdi-arrow-left [#/1/SpreadsheetName111/cell/A1/navigate/A1/left%2099px] id=viewport-horizontal-scrollbar-left-Link\n" +
                "            SliderComponent\n" +
                "              HORIZONTAL\n" +
                "                [0.0] min=1.0 max=16383.0 id=viewport-horizontal-scrollbar-value-Slider\n" +
                "            [#/1/SpreadsheetName111/cell/A1/navigate/A1/right%2099px] mdi-arrow-right id=viewport-horizontal-scrollbar-right-Link\n"
        );
    }

    // ClassTesting....................................................................................................

    @Override
    public Class<SpreadsheetViewportComponent> type() {
        return SpreadsheetViewportComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
