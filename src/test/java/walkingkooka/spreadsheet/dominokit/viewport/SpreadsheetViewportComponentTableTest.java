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
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
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

    final static SpreadsheetName SPREADSHEET_NAME = SpreadsheetName.with("SpreadsheetName222");

    @Test
    public void testTreePrintCellSelected() {
        final String printTree = this.printTreeAndCheck(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            "TABLE\n" +
                "  id=\"viewport\" style=\"overflow-x: hidden; overflow-y: hidden;\"\n" +
                "    THEAD\n" +
                "      SpreadsheetViewportComponentTableRowColumnHeaders\n" +
                "        TR\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSelectAll\n" +
                "            TH\n" +
                "              id=\"viewport-select-all-cells\" style=\"box-sizing: border-box; color: #111111; height: 30px; min-height: 30px; min-width: 80px; width: 80px;\"\n" +
                "                \"All\" [#/1/sheet1/cell/*/bottom-right] id=viewport-select-all-cells-Link\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "            TH\n" +
                "              id=\"viewport-column-A\" style=\"background-color: #222222; box-sizing: border-box; height: 30px; min-height: 30px; min-width: 100px; width: 100px;\"\n" +
                "                \"A\" [#/1/SpreadsheetName222/column/A] id=viewport-column-A-Link\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "            TH\n" +
                "              id=\"viewport-column-B\" style=\"box-sizing: border-box; color: #222222; height: 30px; min-height: 30px; min-width: 100px; width: 100px;\"\n" +
                "                \"B\" [#/1/SpreadsheetName222/column/B] id=viewport-column-B-Link\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "            TH\n" +
                "              id=\"viewport-column-C\" style=\"box-sizing: border-box; color: #222222; height: 30px; min-height: 30px; min-width: 100px; width: 100px;\"\n" +
                "                \"C\" [#/1/SpreadsheetName222/column/C] id=viewport-column-C-Link\n" +
                "    TBODY\n" +
                "      SpreadsheetViewportComponentTableRowCells\n" +
                "        TR\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow\n" +
                "            TH\n" +
                "              id=\"viewport-row-1\" style=\"background-color: #333333; box-sizing: border-box; height: 50px; min-height: 50px; min-width: 80px; width: 80px;\"\n" +
                "                \"1\" [#/1/SpreadsheetName222/row/1] id=viewport-row-1-Link\n" +
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
                "                \"2\" [#/1/SpreadsheetName222/row/2] id=viewport-row-2-Link\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-A2\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-B2\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-C2\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n"
        );

        this.checkEquals(
            true,
            printTree.contains("background-color"),
            "Missing background-color missing selections\n" + printTree
        );
    }

    @Test
    public void testTreePrintNothingSelected() {
        final String printTree = this.printTreeAndCheck(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            "TABLE\n" +
                "  id=\"viewport\" style=\"overflow-x: hidden; overflow-y: hidden;\"\n" +
                "    THEAD\n" +
                "      SpreadsheetViewportComponentTableRowColumnHeaders\n" +
                "        TR\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSelectAll\n" +
                "            TH\n" +
                "              id=\"viewport-select-all-cells\" style=\"box-sizing: border-box; color: #111111; height: 30px; min-height: 30px; min-width: 80px; width: 80px;\"\n" +
                "                \"All\" [#/1/sheet1/cell/*/bottom-right] id=viewport-select-all-cells-Link\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "            TH\n" +
                "              id=\"viewport-column-A\" style=\"box-sizing: border-box; color: #222222; height: 30px; min-height: 30px; min-width: 100px; width: 100px;\"\n" +
                "                \"A\" [#/1/SpreadsheetName222/column/A] id=viewport-column-A-Link\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "            TH\n" +
                "              id=\"viewport-column-B\" style=\"box-sizing: border-box; color: #222222; height: 30px; min-height: 30px; min-width: 100px; width: 100px;\"\n" +
                "                \"B\" [#/1/SpreadsheetName222/column/B] id=viewport-column-B-Link\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "            TH\n" +
                "              id=\"viewport-column-C\" style=\"box-sizing: border-box; color: #222222; height: 30px; min-height: 30px; min-width: 100px; width: 100px;\"\n" +
                "                \"C\" [#/1/SpreadsheetName222/column/C] id=viewport-column-C-Link\n" +
                "    TBODY\n" +
                "      SpreadsheetViewportComponentTableRowCells\n" +
                "        TR\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow\n" +
                "            TH\n" +
                "              id=\"viewport-row-1\" style=\"box-sizing: border-box; color: #333333; height: 50px; min-height: 50px; min-width: 80px; width: 80px;\"\n" +
                "                \"1\" [#/1/SpreadsheetName222/row/1] id=viewport-row-1-Link\n" +
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
                "                \"2\" [#/1/SpreadsheetName222/row/2] id=viewport-row-2-Link\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-A2\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-B2\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-C2\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n"
        );

        this.checkEquals(
            false,
            printTree.contains("background-color"),
            "background-color found something was selected\n" + printTree
        );
    }

    private String printTreeAndCheck(final HistoryToken historyToken,
                                     final String expected) {
        final SpreadsheetId spreadsheetId = SpreadsheetId.with(1);
        final SpreadsheetName spreadsheetName = SpreadsheetName.with("sheet1");

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
            tableContext
        );

        component.refresh(
            spreadsheetId,
            spreadsheetName,
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
