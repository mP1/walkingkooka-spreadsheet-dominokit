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
import walkingkooka.predicate.Predicates;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportWindows;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Arrays;
import java.util.function.Predicate;

public final class SpreadsheetViewportComponentTableRowColumnHeadersTest extends SpreadsheetViewportComponentTableRowTestCase<SpreadsheetViewportComponentTableRowColumnHeaders> {

    private final static Length<?> WIDTH = Length.parse("100px");
    private final static Length<?> HEIGHT = Length.parse("50px");

    @Test
    public void testRefreshNothingSelectedShouldShowHeadersFalse() {
        this.treePrintAndCheck2(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            false, // shouldShowHeaders
            "SpreadsheetViewportComponentTableRowColumnHeaders\n" +
                "  TR\n"
        );
    }

    @Test
    public void testRefreshNothingSelectedAndShouldShowHeadersTrue() {
        this.treePrintAndCheck2(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            true, // shouldShowHeaders
            "SpreadsheetViewportComponentTableRowColumnHeaders\n" +
                "  TR\n" +
                "    SpreadsheetViewportComponentTableCellHeaderSelectAll\n" +
                "      TH\n" +
                "        id=\"viewport-select-all-cells\" style=\"background-color: #111111; box-sizing: border-box; height: 30px; min-height: 30px; min-width: 80px; width: 80px;\"\n" +
                "          \"All\" [#/1/SpreadsheetName222/cell/*/bottom-right] id=viewport-select-all-cells-Link\n" +
                "    SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "      TH\n" +
                "        id=\"viewport-column-A\" style=\"background-color: #222222; box-sizing: border-box; height: 30px; min-height: 30px; min-width: 100px; width: 100px;\"\n" +
                "          \"A\" [#/1/SpreadsheetName222/column/A] id=viewport-column-A-Link\n"
        );
    }

    @Test
    public void testRefreshDifferentCellSelectedAndShouldShowHeadersTrue() {
        this.treePrintAndCheck2(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.parseCell("B2")
                    .setDefaultAnchor()
            ),
            true, // shouldShowHeaders
            "SpreadsheetViewportComponentTableRowColumnHeaders\n" +
                "  TR\n" +
                "    SpreadsheetViewportComponentTableCellHeaderSelectAll\n" +
                "      TH\n" +
                "        id=\"viewport-select-all-cells\" style=\"background-color: #111111; box-sizing: border-box; height: 30px; min-height: 30px; min-width: 80px; width: 80px;\"\n" +
                "          \"All\" [#/1/SpreadsheetName222/cell/*/bottom-right] id=viewport-select-all-cells-Link\n" +
                "    SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "      TH\n" +
                "        id=\"viewport-column-A\" style=\"background-color: #222222; box-sizing: border-box; height: 30px; min-height: 30px; min-width: 100px; width: 100px;\"\n" +
                "          \"A\" [#/1/SpreadsheetName222/column/A] id=viewport-column-A-Link\n"
        );
    }

    @Test
    public void testRefreshColumnSelectedAndShouldShowHeadersTrue() {
        this.treePrintAndCheck2(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            true, // shouldShowHeaders
            "SpreadsheetViewportComponentTableRowColumnHeaders\n" +
                "  TR\n" +
                "    SpreadsheetViewportComponentTableCellHeaderSelectAll\n" +
                "      TH\n" +
                "        id=\"viewport-select-all-cells\" style=\"background-color: #111111; box-sizing: border-box; height: 30px; min-height: 30px; min-width: 80px; width: 80px;\"\n" +
                "          \"All\" [#/1/SpreadsheetName222/cell/*/bottom-right] id=viewport-select-all-cells-Link\n" +
                "    SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "      TH\n" +
                "        id=\"viewport-column-A\" style=\"box-sizing: border-box; color: #333333; height: 30px; min-height: 30px; min-width: 100px; width: 100px;\"\n" +
                "          \"A\" [#/1/SpreadsheetName222/column/A] id=viewport-column-A-Link\n"
        );
    }

    @Test
    public void testRefreshRowSelectedAndShouldShowHeadersTrue() {
        this.treePrintAndCheck2(
            HistoryToken.rowSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.parseRow("2")
                    .setDefaultAnchor()
            ),
            true, // shouldShowHeaders
            "SpreadsheetViewportComponentTableRowColumnHeaders\n" +
                "  TR\n" +
                "    SpreadsheetViewportComponentTableCellHeaderSelectAll\n" +
                "      TH\n" +
                "        id=\"viewport-select-all-cells\" style=\"background-color: #111111; box-sizing: border-box; height: 30px; min-height: 30px; min-width: 80px; width: 80px;\"\n" +
                "          \"All\" [#/1/SpreadsheetName222/cell/*/bottom-right] id=viewport-select-all-cells-Link\n" +
                "    SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "      TH\n" +
                "        id=\"viewport-column-A\" style=\"background-color: #222222; box-sizing: border-box; height: 30px; min-height: 30px; min-width: 100px; width: 100px;\"\n" +
                "          \"A\" [#/1/SpreadsheetName222/column/A] id=viewport-column-A-Link\n"
        );
    }


    private void treePrintAndCheck2(final HistoryToken historyToken,
                                    final boolean shouldShowHeaders,
                                    final String expected) {
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

            @Override
            public void debug(final Object... values) {
                System.out.println("DEBUG " + Arrays.toString(values));
            }
        };

        final SpreadsheetViewportComponentTableContext tableContext = new FakeSpreadsheetViewportComponentTableContext() {

            @Override
            public int viewportGridWidth() {
                return (int)WIDTH.pixelValue();
            }

            @Override
            public int viewportGridHeight() {
                return (int)HEIGHT.pixelValue();
            }

            @Override
            public HistoryToken historyToken() {
                return historyToken;
            }

            @Override
            public boolean isShiftKeyDown() {
                return false;
            }

            @Override
            public boolean shouldShowHeaders() {
                return shouldShowHeaders;
            }

            @Override
            public SpreadsheetViewportCache spreadsheetViewportCache() {
                return this.spreadsheetViewportCache;
            }

            private final SpreadsheetViewportCache spreadsheetViewportCache = SpreadsheetViewportCache.empty(cacheContext);

            @Override
            public TextStyle allCellsStyle() {
                return TextStyle.parse("background-color: #111;");
            }

            @Override
            public TextStyle cellStyle() {
                return TextStyle.parse("background-color: white;");
            }

            @Override
            public TextStyle selectedCellStyle(final TextStyle cellStyle) {
                return cellStyle.merge(
                    TextStyle.parse("color: white;")
                );
            }

            @Override
            public TextStyle columnStyle() {
                return TextStyle.parse("background-color: #222;");
            }

            @Override
            public TextStyle selectedColumnStyle() {
                return TextStyle.parse("color: #333;");
            }
        };

        final SpreadsheetMetadata metadata = SpreadsheetMetadata.EMPTY.set(
            SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
            SpreadsheetId.with(1)
        ).set(
            SpreadsheetMetadataPropertyName.STYLE,
            TextStyle.EMPTY.set(
                TextStylePropertyName.WIDTH,
                WIDTH
            ).set(
                TextStylePropertyName.HEIGHT,
                HEIGHT
            )
        );

        tableContext.spreadsheetViewportCache()
            .onSpreadsheetMetadata(
                metadata
            );

        final SpreadsheetViewportComponentTableRowColumnHeaders component = SpreadsheetViewportComponentTableRowColumnHeaders.empty(
            tableContext
        );

        component.refresh(
            SpreadsheetViewportWindows.parse("A1:A3"),
            historyToken.anchoredSelectionOrEmpty()
                .map(a -> (Predicate<SpreadsheetSelection>)a.selection())
                .orElse(Predicates.never()),
            tableContext
        );

        this.treePrintAndCheck(
            component,
            expected
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetViewportComponentTableRowColumnHeaders> type() {
        return SpreadsheetViewportComponentTableRowColumnHeaders.class;
    }
}
