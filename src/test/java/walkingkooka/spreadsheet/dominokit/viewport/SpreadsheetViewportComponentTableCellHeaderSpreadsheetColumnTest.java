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

import elemental2.dom.HTMLTableCellElement;
import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

public final class SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumnTest extends SpreadsheetViewportComponentTableCellTestCase<HTMLTableCellElement, SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn>  {

    @Test
    public void testTreePrintWithCellSelected() {
        this.treePrintAndCheck2(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            "SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "  TH\n" +
                "    id=\"viewport-column-A\" style=\"box-sizing: border-box; color: #222222; height: 30px; min-height: 30px; min-width: 100px; width: 100px;\"\n" +
                "      \"A\" [#/1/SpreadsheetName222/column/A] id=viewport-column-A-Link\n"
        );
    }

    @Test
    public void testTreePrintWithCellUnselected() {
        this.treePrintAndCheck2(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            "SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "  TH\n" +
                "    id=\"viewport-column-A\" style=\"background-color: #111111; box-sizing: border-box; height: 30px; min-height: 30px; min-width: 100px; width: 100px;\"\n" +
                "      \"A\" [#/1/SpreadsheetName222/column/A] id=viewport-column-A-Link\n"
        );
    }

    @Test
    public void testTreePrintWithColumnSelected() {
        this.treePrintAndCheck2(
            HistoryToken.columnSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.column()
                    .setDefaultAnchor()
            ),
            "SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "  TH\n" +
                "    id=\"viewport-column-A\" style=\"box-sizing: border-box; color: #222222; height: 30px; min-height: 30px; min-width: 100px; width: 100px;\"\n" +
                "      \"A\" [#/1/SpreadsheetName222/column/A] id=viewport-column-A-Link\n"
        );
    }

    @Test
    public void testTreePrintWithRowSelected() {
        this.treePrintAndCheck2(
            HistoryToken.rowSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.row()
                    .setDefaultAnchor()
            ),
            "SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "  TH\n" +
                "    id=\"viewport-column-A\" style=\"background-color: #111111; box-sizing: border-box; height: 30px; min-height: 30px; min-width: 100px; width: 100px;\"\n" +
                "      \"A\" [#/1/SpreadsheetName222/column/A] id=viewport-column-A-Link\n"
        );
    }

    private void treePrintAndCheck2(final HistoryToken historyToken,
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
        };

        final SpreadsheetViewportComponentTableContext tableContext = new FakeSpreadsheetViewportComponentTableContext() {

            @Override
            public HistoryToken historyToken() {
                return historyToken;
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
            public boolean shouldShowHeaders() {
                return true;
            }

            @Override
            public SpreadsheetViewportCache spreadsheetViewportCache() {
                return this.spreadsheetViewportCache;
            }

            private final SpreadsheetViewportCache spreadsheetViewportCache = SpreadsheetViewportCache.empty(cacheContext);

            @Override
            public TextStyle columnStyle() {
                return TextStyle.parse("background-color: #111;");
            }

            @Override
            public TextStyle selectedColumnStyle() {
                return TextStyle.parse("color: #222;");
            }
        };

        final SpreadsheetMetadata metadata = SpreadsheetMetadata.EMPTY.set(
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
                metadata
            );

        this.treePrintAndCheck(
            SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn.empty(
                SpreadsheetSelection.A1.column(),
                tableContext
            ),
            expected
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn> type() {
        return SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn.class;
    }
}
