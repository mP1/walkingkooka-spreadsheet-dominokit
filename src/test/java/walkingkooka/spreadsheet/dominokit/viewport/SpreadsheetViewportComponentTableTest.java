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

import elemental2.dom.HTMLTableElement;
import elemental2.dom.KeyboardEvent;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.color.Color;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.predicate.Predicates;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.dom.Key;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.key.SpreadsheetKeyBindingses;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportNavigation;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportWindows;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public final class SpreadsheetViewportComponentTableTest implements HtmlComponentTesting<SpreadsheetViewportComponentTable, HTMLTableElement> {

    final static SpreadsheetId SPREADSHEET_ID = SpreadsheetId.with(1);

    final static SpreadsheetName SPREADSHEET_NAME = SpreadsheetName.with("SpreadsheetName111");

    // onKeyDownEvent...................................................................................................

    @Test
    public void testOnKeyDownEventWithUnknownKey() {
        this.onKeyDownEventAndCheck(
            key("A"),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            null, // HistoryToken
            null // SpreadsheetViewportNavigation
        );
    }

    @Test
    public void testOnKeyDownEventWithEnter() {
        this.onKeyDownEventAndCheck(
            key(Key.Enter),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            HistoryToken.cellFormula(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ), // HistoryToken
            null // SpreadsheetViewportNavigation
        );
    }

    @Test
    public void testOnKeyDownEventWithEscape() {
        this.onKeyDownEventAndCheck(
            key(Key.Escape),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ), // HistoryToken
            null // SpreadsheetViewportNavigation
        );
    }

    @Test
    public void testOnKeyDownEventWithLeftArrow() {
        this.onKeyDownEventAndCheck(
            key(Key.ArrowLeft),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            null, // HistoryToken
            SpreadsheetViewportNavigation.leftColumn()
        );
    }

    @Test
    public void testOnKeyDownEventWithRightArrow() {
        this.onKeyDownEventAndCheck(
            key(Key.ArrowRight),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            null, // HistoryToken
            SpreadsheetViewportNavigation.rightColumn()
        );
    }

    @Test
    public void testOnKeyDownEventWithUpArrow() {
        this.onKeyDownEventAndCheck(
            key(Key.ArrowUp),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            null, // HistoryToken
            SpreadsheetViewportNavigation.upRow()
        );
    }

    @Test
    public void testOnKeyDownEventWithDownArrow() {
        this.onKeyDownEventAndCheck(
            key(Key.ArrowDown),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            null, // HistoryToken
            SpreadsheetViewportNavigation.downRow()
        );
    }

    @Test
    public void testOnKeyDownEventWithShiftedLeftArrow() {
        this.onKeyDownEventAndCheck(
            shiftedKey(Key.ArrowLeft),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            null, // HistoryToken
            SpreadsheetViewportNavigation.extendLeftColumn()
        );
    }

    @Test
    public void testOnKeyDownEventWithShiftedRightArrow() {
        this.onKeyDownEventAndCheck(
            shiftedKey(Key.ArrowRight),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            null, // HistoryToken
            SpreadsheetViewportNavigation.extendRightColumn()
        );
    }

    @Test
    public void testOnKeyDownEventWithShiftedUpArrow() {
        this.onKeyDownEventAndCheck(
            shiftedKey(Key.ArrowUp),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            null, // HistoryToken
            SpreadsheetViewportNavigation.extendUpRow()
        );
    }

    @Test
    public void testOnKeyDownEventWithShiftedDownArrow() {
        this.onKeyDownEventAndCheck(
            shiftedKey(Key.ArrowDown),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            null, // HistoryToken
            SpreadsheetViewportNavigation.extendDownRow()
        );
    }

    @Test
    public void testOnKeyDownEventWithScreenLeft() {
        this.onKeyDownEventAndCheck(
            key(Key.Home),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            null, // HistoryToken
            SpreadsheetViewportNavigation.leftPixel(300)
        );
    }

    @Test
    public void testOnKeyDownEventWithScreenRight() {
        this.onKeyDownEventAndCheck(
            key(Key.End),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            null, // HistoryToken
            SpreadsheetViewportNavigation.rightPixel(300)
        );
    }

    @Test
    public void testOnKeyDownEventWithScreenUp() {
        this.onKeyDownEventAndCheck(
            key(Key.PageUp),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            null, // HistoryToken
            SpreadsheetViewportNavigation.upPixel(150)
        );
    }

    @Test
    public void testOnKeyDownEventWithScreenDown() {
        this.onKeyDownEventAndCheck(
            key(Key.PageDown),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            null, // HistoryToken
            SpreadsheetViewportNavigation.downPixel(150)
        );
    }

    @Test
    public void testOnKeyDownEventWithExtendScreenLeft() {
        this.onKeyDownEventAndCheck(
            shiftedKey(Key.Home),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            null, // HistoryToken
            SpreadsheetViewportNavigation.extendLeftPixel(300)
        );
    }

    private KeyboardEvent key(final Key key) {
        return this.key(
            key.name()
        );
    }

    private KeyboardEvent shiftedKey(final Key key) {
        return this.key(
            key.name(),
            true // shifted
        );
    }

    private KeyboardEvent key(final String key) {
        return this.key(
            key,
            false // shift
        );
    }

    private KeyboardEvent key(final String key,
                              final boolean shift) {
        final KeyboardEvent event = new KeyboardEvent("keydown");
        event.key = key;
        event.shiftKey = shift;
        return event;
    }

    private void onKeyDownEventAndCheck(final KeyboardEvent event,
                                        final HistoryToken initialHistoryToken,
                                        final HistoryToken expectedHistoryToken,
                                        final SpreadsheetViewportNavigation expectedNavigation) {
        final AppContext appContext = new FakeAppContext() {
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
            public boolean shouldHideZeroValues() {
                return false;
            }

            @Override
            public boolean shouldShowFormulas() {
                return false;
            }

            @Override
            public boolean shouldShowHeaders() {
                return true;
            }

            @Override
            public boolean mustRefresh() {
                return false;
            }

            @Override
            public boolean isShiftKeyDown() {
                return false;
            }

            @Override
            public TextStyle allCellsStyle() {
                return TextStyle.EMPTY;
            }

            @Override
            public TextStyle cellStyle() {
                return TextStyle.EMPTY;
            }

            @Override
            public TextStyle selectedCellStyle(final TextStyle cellStyle) {
                return TextStyle.EMPTY;
            }

            @Override
            public TextStyle columnStyle() {
                return TextStyle.EMPTY;
            }

            @Override
            public TextStyle selectedColumnStyle() {
                return TextStyle.EMPTY;
            }

            @Override
            public TextStyle rowStyle() {
                return TextStyle.EMPTY;
            }

            @Override
            public TextStyle selectedRowStyle() {
                return TextStyle.EMPTY;
            }

            @Override
            public int viewportGridWidth() {
                return 4 * SpreadsheetViewportContext.ROW_HEADER_WIDTH_PIXELS;
            }

            @Override
            public int viewportGridHeight() {
                return 3 * SpreadsheetViewportContext.COLUMN_HEADER_HEIGHT_PIXELS;
            }

            @Override
            public HistoryToken historyToken() {
                return initialHistoryToken;
            }

            @Override
            public void pushHistoryToken(final HistoryToken historyToken) {
                SpreadsheetViewportComponentTableTest.this.historyToken = historyToken;
            }

            @Override
            public void pushNavigation(final SpreadsheetViewportNavigation navigation) {
                SpreadsheetViewportComponentTableTest.this.navigation = navigation;
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

        final SpreadsheetMetadata metadata = SpreadsheetMetadata.EMPTY.set(
            SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
            SPREADSHEET_ID
        ).set(
            SpreadsheetMetadataPropertyName.STYLE,
            TextStyle.EMPTY.set(
                TextStylePropertyName.WIDTH,
                Length.parse("100px")
            ).set(
                TextStylePropertyName.HEIGHT,
                Length.parse("50px")
            )
        );

        tableContext.spreadsheetViewportCache()
            .onSpreadsheetMetadata(
                metadata,
                appContext
            );

        tableContext.spreadsheetViewportCache()
            .onSpreadsheetDelta(
                HttpMethod.GET,
                Url.parseRelative("/api/spreadsheet/1/cell/A1"),
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
                ).setWindow(
                    SpreadsheetViewportWindows.parse("A1:C3")
                ),
                appContext
            );

        this.historyToken = null;
        this.navigation = null;

        final SpreadsheetViewportComponentTable component = SpreadsheetViewportComponentTable.empty(
            SpreadsheetKeyBindingses.basic(),
            tableContext
        );

        component.onKeyDownEvent(event);

        this.checkEquals(
            expectedHistoryToken,
            this.historyToken,
            () -> "historyToken event=" + event
        );

        this.checkEquals(
            expectedNavigation,
            this.navigation,
            () -> "navigation event=" + event
        );
    }

    private HistoryToken historyToken;

    private SpreadsheetViewportNavigation navigation;

    // refresh..........................................................................................................

    @Test
    public void testTreePrintCellSelectedAndShouldShowHeadersTrue() {
        final String printTree = this.printTreeAndCheck(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            true, // shouldShowHeaders
            "TABLE\n" +
                "  id=\"viewport\" style=\"overflow-x: hidden; overflow-y: hidden;\"\n" +
                "    THEAD\n" +
                "      SpreadsheetViewportComponentTableRowColumnHeaders\n" +
                "        TR\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSelectAll\n" +
                "            TH\n" +
                "              id=\"viewport-select-all-cells\" style=\"box-sizing: border-box; color: #111111; height: 30px; min-height: 30px; min-width: 80px; width: 80px;\"\n" +
                "                \"All\" [#/1/SpreadsheetName111/cell/*/bottom-right] id=viewport-select-all-cells-Link\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "            TH\n" +
                "              id=\"viewport-column-A\" style=\"background-color: #222222; box-sizing: border-box; height: 30px; min-height: 30px; min-width: 100px; width: 100px;\"\n" +
                "                \"A\" [#/1/SpreadsheetName111/column/A] id=viewport-column-A-Link\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "            TH\n" +
                "              id=\"viewport-column-B\" style=\"box-sizing: border-box; color: #222222; height: 30px; min-height: 30px; min-width: 100px; width: 100px;\"\n" +
                "                \"B\" [#/1/SpreadsheetName111/column/B] id=viewport-column-B-Link\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "            TH\n" +
                "              id=\"viewport-column-C\" style=\"box-sizing: border-box; color: #222222; height: 30px; min-height: 30px; min-width: 100px; width: 100px;\"\n" +
                "                \"C\" [#/1/SpreadsheetName111/column/C] id=viewport-column-C-Link\n" +
                "    TBODY\n" +
                "      SpreadsheetViewportComponentTableRowCells\n" +
                "        TR\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow\n" +
                "            TH\n" +
                "              id=\"viewport-row-1\" style=\"background-color: #333333; box-sizing: border-box; height: 50px; min-height: 50px; min-width: 80px; width: 80px;\"\n" +
                "                \"1\" [#/1/SpreadsheetName111/row/1] id=viewport-row-1-Link\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-A1\" tabIndex=0 style=\"background-color: black; box-sizing: border-box; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "                Text \"*** 3.0\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-B1\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-C1\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "      SpreadsheetViewportComponentTableRowCells\n" +
                "        TR\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow\n" +
                "            TH\n" +
                "              id=\"viewport-row-2\" style=\"box-sizing: border-box; color: #333333; height: 50px; min-height: 50px; min-width: 80px; width: 80px;\"\n" +
                "                \"2\" [#/1/SpreadsheetName111/row/2] id=viewport-row-2-Link\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-A2\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-B2\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-C2\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "      SpreadsheetViewportComponentTableRowCells\n" +
                "        TR\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow\n" +
                "            TH\n" +
                "              id=\"viewport-row-3\" style=\"box-sizing: border-box; color: #333333; height: 50px; min-height: 50px; min-width: 80px; width: 80px;\"\n" +
                "                \"3\" [#/1/SpreadsheetName111/row/3] id=viewport-row-3-Link\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-A3\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-B3\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-C3\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n"
        );

        this.checkEquals(
            true,
            printTree.contains("select-all"),
            "TR should contain select-all\n" + printTree
        );

        this.checkEquals(
            true,
            printTree.contains("column"),
            "TR should be contain column\n" + printTree
        );

        this.checkEquals(
            true,
            printTree.contains("row"),
            "TR should be contain ROW header\n" + printTree
        );

        this.checkEquals(
            true,
            printTree.contains("background-color"),
            "Missing background-color missing selections\n" + printTree
        );
    }

    @Test
    public void testTreePrintNothingSelectedAndShouldShowHeadersTrue() {
        final String printTree = this.printTreeAndCheck(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            true, // shouldShowHeaders
            "TABLE\n" +
                "  id=\"viewport\" style=\"overflow-x: hidden; overflow-y: hidden;\"\n" +
                "    THEAD\n" +
                "      SpreadsheetViewportComponentTableRowColumnHeaders\n" +
                "        TR\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSelectAll\n" +
                "            TH\n" +
                "              id=\"viewport-select-all-cells\" style=\"box-sizing: border-box; color: #111111; height: 30px; min-height: 30px; min-width: 80px; width: 80px;\"\n" +
                "                \"All\" [#/1/SpreadsheetName111/cell/*/bottom-right] id=viewport-select-all-cells-Link\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "            TH\n" +
                "              id=\"viewport-column-A\" style=\"box-sizing: border-box; color: #222222; height: 30px; min-height: 30px; min-width: 100px; width: 100px;\"\n" +
                "                \"A\" [#/1/SpreadsheetName111/column/A] id=viewport-column-A-Link\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "            TH\n" +
                "              id=\"viewport-column-B\" style=\"box-sizing: border-box; color: #222222; height: 30px; min-height: 30px; min-width: 100px; width: 100px;\"\n" +
                "                \"B\" [#/1/SpreadsheetName111/column/B] id=viewport-column-B-Link\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "            TH\n" +
                "              id=\"viewport-column-C\" style=\"box-sizing: border-box; color: #222222; height: 30px; min-height: 30px; min-width: 100px; width: 100px;\"\n" +
                "                \"C\" [#/1/SpreadsheetName111/column/C] id=viewport-column-C-Link\n" +
                "    TBODY\n" +
                "      SpreadsheetViewportComponentTableRowCells\n" +
                "        TR\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow\n" +
                "            TH\n" +
                "              id=\"viewport-row-1\" style=\"box-sizing: border-box; color: #333333; height: 50px; min-height: 50px; min-width: 80px; width: 80px;\"\n" +
                "                \"1\" [#/1/SpreadsheetName111/row/1] id=viewport-row-1-Link\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-A1\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "                Text \"*** 3.0\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-B1\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-C1\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "      SpreadsheetViewportComponentTableRowCells\n" +
                "        TR\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow\n" +
                "            TH\n" +
                "              id=\"viewport-row-2\" style=\"box-sizing: border-box; color: #333333; height: 50px; min-height: 50px; min-width: 80px; width: 80px;\"\n" +
                "                \"2\" [#/1/SpreadsheetName111/row/2] id=viewport-row-2-Link\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-A2\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-B2\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-C2\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "      SpreadsheetViewportComponentTableRowCells\n" +
                "        TR\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow\n" +
                "            TH\n" +
                "              id=\"viewport-row-3\" style=\"box-sizing: border-box; color: #333333; height: 50px; min-height: 50px; min-width: 80px; width: 80px;\"\n" +
                "                \"3\" [#/1/SpreadsheetName111/row/3] id=viewport-row-3-Link\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-A3\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-B3\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-C3\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n"
        );

        this.checkEquals(
            true,
            printTree.contains("select-all"),
            "TR should contain select-all\n" + printTree
        );

        this.checkEquals(
            true,
            printTree.contains("column"),
            "TR should be contain column\n" + printTree
        );

        this.checkEquals(
            true,
            printTree.contains("row"),
            "TR should be contain ROW header\n" + printTree
        );

        this.checkEquals(
            false,
            printTree.contains("background-color"),
            "background-color found something was selected\n" + printTree
        );
    }

    @Test
    public void testTreePrintNothingSelectedAndShouldShowHeadersFalse() {
        final String printTree = this.printTreeAndCheck(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            false, // shouldShowHeaders
            "TABLE\n" +
                "  id=\"viewport\" style=\"overflow-x: hidden; overflow-y: hidden;\"\n" +
                "    THEAD\n" +
                "    TBODY\n" +
                "      SpreadsheetViewportComponentTableRowCells\n" +
                "        TR\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-A1\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "                Text \"*** 3.0\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-B1\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-C1\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "      SpreadsheetViewportComponentTableRowCells\n" +
                "        TR\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-A2\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-B2\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-C2\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "      SpreadsheetViewportComponentTableRowCells\n" +
                "        TR\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-A3\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-B3\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-C3\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n"
        );

        this.checkEquals(
            false,
            printTree.contains("select-all"),
            "TR should be missing select-all\n" + printTree
        );

        this.checkEquals(
            false,
            printTree.contains("column"),
            "TR should be missing column\n" + printTree
        );

        this.checkEquals(
            false,
            printTree.contains("row"),
            "TR should be missing ROW header\n" + printTree
        );

        this.checkEquals(
            false,
            printTree.contains("background-color"),
            "background-color found something was selected\n" + printTree
        );
    }

    private String printTreeAndCheck(final HistoryToken historyToken,
                                     final boolean shouldShowHeaders,
                                     final String expected) {
        final AppContext appContext = new FakeAppContext() {
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
            public int viewportGridWidth() {
                return 4 * SpreadsheetViewportContext.ROW_HEADER_WIDTH_PIXELS;
            }

            @Override
            public int viewportGridHeight() {
                return 4 * SpreadsheetViewportContext.COLUMN_HEADER_HEIGHT_PIXELS;
            }

            @Override
            public HistoryToken historyToken() {
                return historyToken;
            }

            @Override
            public boolean shouldHideZeroValues() {
                return false;
            }

            @Override
            public boolean shouldShowFormulas() {
                return false;
            }

            @Override
            public boolean shouldShowHeaders() {
                return shouldShowHeaders;
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
            public TextStyle allCellsStyle() {
                return TextStyle.parse("color: #111;");
            }

            @Override
            public TextStyle cellStyle() {
                return TextStyle.parse("color: white;");
            }

            @Override
            public TextStyle selectedCellStyle(final TextStyle cellStyle) {
                return cellStyle.set(
                    TextStylePropertyName.BACKGROUND_COLOR,
                    Color.BLACK
                );
            }

            @Override
            public TextStyle columnStyle() {
                return TextStyle.parse("color: #222;");
            }

            @Override
            public TextStyle selectedColumnStyle() {
                return TextStyle.parse("background-color: #222;");
            }

            @Override
            public TextStyle rowStyle() {
                return TextStyle.parse("color: #333;");
            }

            @Override
            public TextStyle selectedRowStyle() {
                return TextStyle.parse("background-color: #333;");
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

        final SpreadsheetMetadata metadata = SpreadsheetMetadata.EMPTY.set(
            SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
            SPREADSHEET_ID
        ).set(
            SpreadsheetMetadataPropertyName.STYLE,
            TextStyle.EMPTY.set(
                TextStylePropertyName.WIDTH,
                Length.parse("100px")
            ).set(
                TextStylePropertyName.HEIGHT,
                Length.parse("50px")
            )
        );

        tableContext.spreadsheetViewportCache()
            .onSpreadsheetMetadata(
                metadata,
                appContext
            );

        tableContext.spreadsheetViewportCache()
            .onSpreadsheetDelta(
                HttpMethod.GET,
                Url.parseRelative("/api/spreadsheet/1/cell/A1"),
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
                ),
                appContext
            );

        final SpreadsheetViewportComponentTable component = SpreadsheetViewportComponentTable.empty(
            SpreadsheetKeyBindingses.basic(),
            tableContext
        );

        component.refresh(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            SpreadsheetViewportWindows.parse("A1:C3"),
            historyToken.anchoredSelectionOrEmpty()
                .map(a -> (Predicate<SpreadsheetSelection>) a.selection())
                .orElse(Predicates.never())
        );

        this.treePrintAndCheck(
            component,
            expected
        );

        return component.treeToString(
            INDENTATION,
            EOL
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetViewportComponentTable> type() {
        return SpreadsheetViewportComponentTable.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
