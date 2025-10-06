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
import walkingkooka.collect.set.Sets;
import walkingkooka.color.Color;
import walkingkooka.color.WebColorName;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.SpreadsheetErrorKind;
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
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.WordBreak;

import java.util.Arrays;
import java.util.Optional;

public final class SpreadsheetViewportComponentTableCellSpreadsheetCellTest extends SpreadsheetViewportComponentTableCellTestCase<HTMLTableCellElement, SpreadsheetViewportComponentTableCellSpreadsheetCell> {

    @Test
    public void testTreePrintNothingSelected() {
        this.treePrintAndCheck2(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            false, // shouldHideZeroValues
            false, // shouldShowFormulas
            123,
            "SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "  TD\n" +
                "    id=\"viewport-cell-A1\" tabIndex=0 style=\"background-color: black; box-sizing: border-box; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "      Text \"123\"\n"
        );
    }

    @Test
    public void testTreePrintSpreadsheetCellWithErrorWithMessage() {
        this.treePrintAndCheck2(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            false, // shouldHideZeroValues
            false, // shouldShowFormulas
            SpreadsheetErrorKind.DIV0.setMessage("Divide by zero is bad!"), // value
            "SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "  TD\n" +
                "    id=\"viewport-cell-A1\" tabIndex=0 style=\"background-color: black; box-sizing: border-box; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "      (Divide by zero is bad!)\n" +
                "      Text \"#DIV/0! \\\"Divide by zero is bad!\\\"\"\n"
        );
    }

    @Test
    public void testTreePrintSpreadsheetCellWithBadge() {
        this.treePrintAndCheck2(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            false, // shouldHideZeroValues
            false, // shouldShowFormulas
            123, // value
            Optional.of(
                TextNode.badge(
                    "BadgeText111"
                ).appendChild(
                    TextNode.text(
                        String.valueOf(123)
                    )
                )
            ),
            "SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "  TD\n" +
                "    id=\"viewport-cell-A1\" tabIndex=0 style=\"background-color: black; box-sizing: border-box; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "      (BadgeText111)\n" +
                "      Text \"123\"\n"
        );
    }

    @Test
    public void testTreePrintSpreadsheetCellWithBadgeIgnoresError() {
        this.treePrintAndCheck2(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            false, // shouldHideZeroValues
            false, // shouldShowFormulas
            SpreadsheetErrorKind.DIV0.setMessage("Divide by zero is really bad!!!"), // value
            Optional.of(
                TextNode.badge(
                    "BadgeText111"
                ).appendChild(
                    TextNode.text(
                        String.valueOf(123)
                    )
                )
            ),
            "SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "  TD\n" +
                "    id=\"viewport-cell-A1\" tabIndex=0 style=\"background-color: black; box-sizing: border-box; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "      (BadgeText111)\n" +
                "      Text \"123\"\n"
        );
    }

    @Test
    public void testTreePrintNothingSelectedHideZeroValues() {
        this.treePrintAndCheck2(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            true, // shouldHideZeroValues
            false, // shouldShowFormulas
            0,
            "SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "  TD\n" +
                "    id=\"viewport-cell-A1\" tabIndex=0 style=\"background-color: black; box-sizing: border-box; height: 50px; min-height: 50px; min-width: 100px; width: 100px; word-break: keep-all;\"\n"
        );
    }

    @Test
    public void testTreePrintNothingSelectedShowFormulas() {
        this.treePrintAndCheck2(
            HistoryToken.spreadsheetSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            ),
            false, // shouldHideZeroValues
            true, // shouldShowFormulas
            0,
            "SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "  TD\n" +
                "    id=\"viewport-cell-A1\" tabIndex=0 style=\"background-color: black; box-sizing: border-box; font-style: italic; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "      Text \"=1+2\"\n"
        );
    }

    @Test
    public void testTreePrintColumnSelected() {
        this.treePrintAndCheck2(
            HistoryToken.columnSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.column()
                    .setDefaultAnchor()
            ),
            false, // shouldHideZeroValues
            false, // shouldShowFormulas
            123,
            "SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "  TD\n" +
                "    id=\"viewport-cell-A1\" tabIndex=0 style=\"box-sizing: border-box; color: green; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "      Text \"123\"\n"
        );
    }

    @Test
    public void testTreePrintRowSelected() {
        this.treePrintAndCheck2(
            HistoryToken.rowSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.row()
                    .setDefaultAnchor()
            ),
            false, // shouldHideZeroValues
            false, // shouldShowFormulas
            123,
            "SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "  TD\n" +
                "    id=\"viewport-cell-A1\" tabIndex=0 style=\"box-sizing: border-box; color: green; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "      Text \"123\"\n"
        );
    }

    @Test
    public void testTreePrintCellSelected() {
        this.treePrintAndCheck2(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            false, // shouldHideZeroValues
            false, // shouldShowFormulas
            123,
            "SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "  TD\n" +
                "    id=\"viewport-cell-A1\" tabIndex=0 style=\"box-sizing: border-box; color: green; height: 50px; min-height: 50px; min-width: 100px; width: 100px;\"\n" +
                "      Text \"123\"\n"
        );
    }

    private void treePrintAndCheck2(final HistoryToken historyToken,
                                    final boolean shouldHideZeroValues,
                                    final boolean shouldShowFormulas,
                                    final Object value,
                                    final String expected) {
        this.treePrintAndCheck2(
            historyToken,
            shouldHideZeroValues,
            shouldShowFormulas,
            value,
            Optional.ofNullable(
                null != value ?
                    TextNode.text(
                        value.toString()
                    ) :
                    null
            ),
            expected
        );
    }

    private void treePrintAndCheck2(final HistoryToken historyToken,
                                    final boolean shouldHideZeroValues,
                                    final boolean shouldShowFormulas,
                                    final Object value,
                                    final Optional<TextNode> formatted,
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
            public boolean shouldHideZeroValues() {
                return shouldHideZeroValues;
            }

            @Override
            public boolean shouldShowFormulas() {
                return shouldShowFormulas;
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
                return TextStyle.EMPTY.set(
                    TextStylePropertyName.BACKGROUND_COLOR,
                    Color.BLACK
                );
            }

            @Override
            public TextStyle selectedCellStyle(final TextStyle cellStyle) {
                return cellStyle.set(
                    TextStylePropertyName.COLOR,
                    WebColorName.GREEN.color()
                );
            }

            @Override
            public TextStyle hideZeroStyle(final TextStyle style) {
                return style.merge(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.WORD_BREAK,
                        WordBreak.KEEP_ALL
                    )
                );
            }

            @Override
            public TextStyle showFormulasStyle(final TextStyle style) {
                return style.merge(
                    TextStyle.EMPTY.set(
                        TextStylePropertyName.FONT_STYLE,
                        FontStyle.ITALIC
                    )
                );
            }

            @Override
            public SpreadsheetViewportCache spreadsheetViewportCache() {
                return this.spreadsheetViewportCache;
            }

            private final SpreadsheetViewportCache spreadsheetViewportCache = SpreadsheetViewportCache.empty(cacheContext);
        };

        final SpreadsheetMetadata metadata = SpreadsheetMetadata.EMPTY.set(
            SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
            SPREADSHEET_ID
        ).set(
            SpreadsheetMetadataPropertyName.SPREADSHEET_NAME,
            SPREADSHEET_NAME
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

        final AppContext appContext = new FakeAppContext() {

            @Override
            public void debug(final Object... values) {
                System.out.println("DEBUG " + Arrays.toString(values));
            }
        };

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
                                .setValue(
                                    Optional.ofNullable(value)
                                )
                        ).setFormattedValue(formatted)
                    )
                ),
                appContext
            );

        this.treePrintAndCheck(
            SpreadsheetViewportComponentTableCellSpreadsheetCell.empty(
                SpreadsheetSelection.A1,
                tableContext
            ),
            expected
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetViewportComponentTableCellSpreadsheetCell> type() {
        return SpreadsheetViewportComponentTableCellSpreadsheetCell.class;
    }
}
