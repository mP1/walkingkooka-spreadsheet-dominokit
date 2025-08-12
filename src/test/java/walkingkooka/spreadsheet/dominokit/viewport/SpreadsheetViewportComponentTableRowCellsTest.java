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
import walkingkooka.collect.set.Sets;
import walkingkooka.color.Color;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
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

public final class SpreadsheetViewportComponentTableRowCellsTest extends SpreadsheetViewportComponentTableRowTestCase<SpreadsheetViewportComponentTableRowCells> {

    @Test
    public void testRefresh() {
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
            public TextStyle cellStyle() {
                return TextStyle.parse("color: white;");
            }

            @Override
            public TextStyle selectedCellStyle(final TextStyle cellStyle) {
                return cellStyle.set(
                    TextStylePropertyName.COLOR,
                    Color.WHITE
                );
            }

            @Override
            public TextStyle rowStyle() {
                return TextStyle.parse("background-color: #111;");
            }

            @Override
            public TextStyle selectedRowStyle() {
                return TextStyle.parse("color: #222;");
            }

            @Override
            public SpreadsheetViewportCache spreadsheetViewportCache() {
                return this.spreadsheetViewportCache;
            }

            private final SpreadsheetViewportCache spreadsheetViewportCache = SpreadsheetViewportCache.empty(cacheContext);
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

        final SpreadsheetViewportComponentTableRowCells component = SpreadsheetViewportComponentTableRowCells.empty(
            SpreadsheetSelection.A1.row(),
            tableContext
        );
        component.refresh(
            SpreadsheetViewportWindows.parse("A1:A3"),
            SpreadsheetSelection.A1::equals,
            tableContext
        );

        this.treePrintAndCheck(
            component,
            "SpreadsheetViewportComponentTableRowCells\n" +
                "  TR\n" +
                "    SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow\n" +
                "      TH\n" +
                "        id=\"viewport-row-1\" style=\"background-color: #111111; box-sizing: border-box; color: #222222; height: 50px; min-height: 50px; min-width: 80px; width: 80px;\"\n" +
                "          \"1\" [#/1/SpreadsheetName222/row/1] id=viewport-row-1-Link\n" +
                "    SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "      TD\n" +
                "        id=\"viewport-cell-A1\" tabIndex=0 style=\"box-sizing: border-box; color: white; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "          Text \"*** 3.0\"\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetViewportComponentTableRowCells> type() {
        return SpreadsheetViewportComponentTableRowCells.class;
    }
}
