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
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
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

public final class SpreadsheetViewportComponentTableCellHeaderSelectAllTest extends SpreadsheetViewportComponentTableCellTestCase<HTMLTableCellElement, SpreadsheetViewportComponentTableCellHeaderSelectAll>  {

    @Test
    public void testTreePrint() {
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
                    TextStylePropertyName.COLOR,
                    Color.BLACK
                );
            }

            @Override
            public SpreadsheetViewportCache spreadsheetViewportCache() {
                return this.spreadsheetViewportCache;
            }

            private final SpreadsheetViewportCache spreadsheetViewportCache = SpreadsheetViewportCache.empty(cacheContext);
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
                metadata,
                new FakeAppContext()
            );

        this.treePrintAndCheck(
            SpreadsheetViewportComponentTableCellHeaderSelectAll.empty(tableContext),
            "SpreadsheetViewportComponentTableCellHeaderSelectAll\n" +
                "  TH\n" +
                "    id=viewport-select-all-cells style=border-bottom-color: rgb(136, 136, 136); border-bottom-style: solid; border-bottom-width: 1px; border-left-color: rgb(136, 136, 136); border-left-style: solid; border-left-width: 1px; border-right-color: rgb(136, 136, 136); border-right-style: solid; border-right-width: 1px; border-top-color: rgb(136, 136, 136); border-top-style: solid; border-top-width: 1px; box-sizing: border-box; font-size: 11; font-style: normal; font-variant: normal; font-weight: normal; height: 30px; hyphens: none; margin-bottom: none; margin-left: none; margin-right: none; margin-top: none; min-height: 30px; min-width: 80px; padding-bottom: none; padding-left: none; padding-right: none; padding-top: none; text-align: center; vertical-align: middle; width: 80px; word-break: normal\n" +
                "      \"All\" [#/1/SpreadsheetName222/cell/*/bottom-right] id=viewport-select-all-cells-Link"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetViewportComponentTableCellHeaderSelectAll> type() {
        return SpreadsheetViewportComponentTableCellHeaderSelectAll.class;
    }
}
