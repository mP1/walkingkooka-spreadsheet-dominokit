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

public final class SpreadsheetViewportComponentTableTest implements HtmlComponentTesting<SpreadsheetViewportComponentTable, HTMLTableElement> {

    @Test
    public void testTreePrint() {
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
            public HistoryToken historyToken() {
                return HistoryToken.cellSelect(
                    SpreadsheetId.with(1),
                    SpreadsheetName.with("SpreadsheetName222"),
                    SpreadsheetSelection.A1.setDefaultAnchor()
                );
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

        final SpreadsheetMetadata metadata = SpreadsheetMetadata.EMPTY.set(
            SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
            SpreadsheetId.with(1)
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
            SpreadsheetSelection.A1::equalsIgnoreReferenceKind
        );

        this.treePrintAndCheck(
            component,
            "TABLE\n" +
                "  id=\"viewport\" style=\"overflow: hidden;\"\n" +
                "    THEAD\n" +
                "      SpreadsheetViewportComponentTableRowColumnHeaders\n" +
                "        TR\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSelectAll\n" +
                "            TH\n" +
                "              id=\"viewport-select-all-cells\" style=\"background-color: #dddddd; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 30px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 30px; min-width: 80px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: center; vertical-align: middle; width: 80px; word-break: normal;\"\n" +
                "                \"All\" [#/1/sheet1/cell/*/bottom-right] id=viewport-select-all-cells-Link\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "            TH\n" +
                "              id=\"viewport-column-A\" style=\"background-color: #dddddd; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 30px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 30px; min-width: 100px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: center; vertical-align: middle; width: 100px; word-break: normal;\"\n" +
                "                \"A\" [#/1/SpreadsheetName222/column/A] id=viewport-column-A-Link\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "            TH\n" +
                "              id=\"viewport-column-B\" style=\"background-color: #dddddd; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 30px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 30px; min-width: 100px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: center; vertical-align: middle; width: 100px; word-break: normal;\"\n" +
                "                \"B\" [#/1/SpreadsheetName222/column/B] id=viewport-column-B-Link\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "            TH\n" +
                "              id=\"viewport-column-C\" style=\"background-color: #dddddd; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 30px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 30px; min-width: 100px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: center; vertical-align: middle; width: 100px; word-break: normal;\"\n" +
                "                \"C\" [#/1/SpreadsheetName222/column/C] id=viewport-column-C-Link\n" +
                "    TBODY\n" +
                "      SpreadsheetViewportComponentTableRowCells\n" +
                "        TR\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow\n" +
                "            TH\n" +
                "              id=\"viewport-row-1\" style=\"background-color: #dddddd; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 50px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 50px; min-width: 80px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: center; vertical-align: middle; width: 80px; word-break: normal;\"\n" +
                "                \"1\" [#/1/SpreadsheetName222/row/1] id=viewport-row-1-Link\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-A1\" tabIndex=0 style=\"background-color: #eeeeee; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; color: #000000; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 50px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 50px; min-width: 100px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: left; vertical-align: top; width: 100px; word-break: normal;\"\n" +
                "                Text \"*** 3.0\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-B1\" tabIndex=0 style=\"background-color: #ffffff; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; color: #000000; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 50px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 50px; min-width: 100px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: left; vertical-align: top; width: 100px; word-break: normal;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-C1\" tabIndex=0 style=\"background-color: #ffffff; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; color: #000000; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 50px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 50px; min-width: 100px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: left; vertical-align: top; width: 100px; word-break: normal;\"\n" +
                "      SpreadsheetViewportComponentTableRowCells\n" +
                "        TR\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow\n" +
                "            TH\n" +
                "              id=\"viewport-row-2\" style=\"background-color: #dddddd; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 50px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 50px; min-width: 80px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: center; vertical-align: middle; width: 80px; word-break: normal;\"\n" +
                "                \"2\" [#/1/SpreadsheetName222/row/2] id=viewport-row-2-Link\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-A2\" tabIndex=0 style=\"background-color: #ffffff; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; color: #000000; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 50px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 50px; min-width: 100px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: left; vertical-align: top; width: 100px; word-break: normal;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-B2\" tabIndex=0 style=\"background-color: #ffffff; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; color: #000000; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 50px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 50px; min-width: 100px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: left; vertical-align: top; width: 100px; word-break: normal;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-C2\" tabIndex=0 style=\"background-color: #ffffff; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; color: #000000; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 50px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 50px; min-width: 100px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: left; vertical-align: top; width: 100px; word-break: normal;\"\n" +
                "      SpreadsheetViewportComponentTableRowCells\n" +
                "        TR\n" +
                "          SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow\n" +
                "            TH\n" +
                "              id=\"viewport-row-3\" style=\"background-color: #dddddd; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 50px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 50px; min-width: 80px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: center; vertical-align: middle; width: 80px; word-break: normal;\"\n" +
                "                \"3\" [#/1/SpreadsheetName222/row/3] id=viewport-row-3-Link\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-A3\" tabIndex=0 style=\"background-color: #ffffff; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; color: #000000; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 50px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 50px; min-width: 100px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: left; vertical-align: top; width: 100px; word-break: normal;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-B3\" tabIndex=0 style=\"background-color: #ffffff; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; color: #000000; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 50px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 50px; min-width: 100px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: left; vertical-align: top; width: 100px; word-break: normal;\"\n" +
                "          SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "            TD\n" +
                "              id=\"viewport-cell-C3\" tabIndex=0 style=\"background-color: #ffffff; border-bottom-color: #888888; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #888888; border-left-style: solid; border-left-width: 1px; border-right-color: #888888; border-right-style: solid; border-right-width: 1px; border-top-color: #888888; border-top-style: solid; border-top-width: 1px; box-sizing: border-box; color: #000000; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 50px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 50px; min-width: 100px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: left; vertical-align: top; width: 100px; word-break: normal;\"\n"
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
