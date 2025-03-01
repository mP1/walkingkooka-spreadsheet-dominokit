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

package walkingkooka.spreadsheet.dominokit.delta;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.color.Color;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.FakeHistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.value.TableComponentTesting;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.format.SpreadsheetText;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetDeltaCellsTableComponentTest implements TableComponentTesting<HTMLDivElement, SpreadsheetDelta, SpreadsheetDeltaCellsTableComponent> {

    // with.............................................................................................................

    @Test
    public void testWithNullIdFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetDeltaCellsTableComponent.with(
                null,
                SpreadsheetDeltaCellsTableComponentContexts.fake()
            )
        );
    }

    @Test
    public void testWithEmptyIdFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetDeltaCellsTableComponent.with(
                "",
                SpreadsheetDeltaCellsTableComponentContexts.fake()
            )
        );
    }

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetDeltaCellsTableComponent.with(
                "ID",
                null
            )
        );
    }

    // printTree........................................................................................................

    @Test
    public void testRenderEmptySpreadsheetDelta() {
        this.renderAndCheck(
            SpreadsheetDelta.EMPTY,
            "SpreadsheetDeltaCellsTableComponent\n" +
                "  SpreadsheetDataTableComponent\n" +
                "    id=ID123-cells-Table\n" +
                "    COLUMN(S)\n" +
                "      Cell\n" +
                "      Formula\n" +
                "      Value\n" +
                "      Formatted\n" +
                "    PLUGINS\n" +
                "      BodyScrollPlugin\n"
        );
    }

    @Test
    public void testRenderSpreadsheetDeltaOneCell() {
        this.renderAndCheck(
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setText("=1+2")
                    ).setFormattedValue(
                        Optional.of(
                            SpreadsheetText.with("Hello").setColor(
                                Optional.of(Color.parse("blue"))
                            ).toTextNode()
                        )
                    )
                )
            ).setMatchedCells(
                Sets.of(
                    SpreadsheetSelection.A1
                )
            ),
            "SpreadsheetDeltaCellsTableComponent\n" +
                "  SpreadsheetDataTableComponent\n" +
                "    id=ID123-cells-Table\n" +
                "    COLUMN(S)\n" +
                "      Cell\n" +
                "      Formula\n" +
                "      Value\n" +
                "      Formatted\n" +
                "    ROW(S)\n" +
                "      ROW 0\n" +
                "        \"A1\" [#/1/Spreadsheet222/cell/A1] id=ID123-cells-A1-Link\n" +
                "        \"A1\" [#/1/Spreadsheet222/cell/A1/formula] id=ID123-cells-A1-formula-Link\n" +
                "        SpreadsheetTextComponent\n" +
                "          \"\"\n" +
                "        SpreadsheetTextNodeComponent\n" +
                "          Hello\n" +
                "    PLUGINS\n" +
                "      BodyScrollPlugin\n"
        );
    }

    @Test
    public void testRenderSpreadsheetDeltaSeveralCells() {
        this.renderAndCheck(
            SpreadsheetDelta.EMPTY.setCells(
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(
                        SpreadsheetFormula.EMPTY.setText("=111")
                    ).setFormattedValue(
                        Optional.of(
                            SpreadsheetText.with("Hello").setColor(
                                Optional.of(Color.parse("blue"))
                            ).toTextNode()
                        )
                    ),
                    SpreadsheetSelection.parseCell("A2")
                        .setFormula(
                            SpreadsheetFormula.EMPTY.setText("=222")
                        ).setFormattedValue(
                            Optional.of(
                                SpreadsheetText.with("2222").toTextNode()
                            )
                        )
                )
            ).setMatchedCells(
                Sets.of(
                    SpreadsheetSelection.A1
                )
            ),
            "SpreadsheetDeltaCellsTableComponent\n" +
                "  SpreadsheetDataTableComponent\n" +
                "    id=ID123-cells-Table\n" +
                "    COLUMN(S)\n" +
                "      Cell\n" +
                "      Formula\n" +
                "      Value\n" +
                "      Formatted\n" +
                "    ROW(S)\n" +
                "      ROW 0\n" +
                "        \"A1\" [#/1/Spreadsheet222/cell/A1] id=ID123-cells-A1-Link\n" +
                "        \"A1\" [#/1/Spreadsheet222/cell/A1/formula] id=ID123-cells-A1-formula-Link\n" +
                "        SpreadsheetTextComponent\n" +
                "          \"\"\n" +
                "        SpreadsheetTextNodeComponent\n" +
                "          Hello\n" +
                "      ROW 1\n" +
                "        \"A2\" [#/1/Spreadsheet222/cell/A2] id=ID123-cells-A2-Link\n" +
                "        \"A2\" [#/1/Spreadsheet222/cell/A2/formula] id=ID123-cells-A2-formula-Link\n" +
                "        SpreadsheetTextComponent\n" +
                "          \"\"\n" +
                "        SpreadsheetTextNodeComponent\n" +
                "          2222\n" +
                "    PLUGINS\n" +
                "      BodyScrollPlugin\n"
        );
    }

    private void renderAndCheck(final SpreadsheetDelta delta,
                                final String expected) {
        final SpreadsheetDeltaCellsTableComponent component = SpreadsheetDeltaCellsTableComponent.with(
            "ID123-",
            SpreadsheetDeltaCellsTableComponentContexts.basic(
                new FakeHistoryTokenContext() {
                    @Override @Test
                    public HistoryToken historyToken() {
                        return HistoryToken.parseString("/1/Spreadsheet222/cell/A1/find");
                    }
                },
                new HasSpreadsheetDeltaFetcher() {
                    @Override
                    public SpreadsheetDeltaFetcher spreadsheetDeltaFetcher() {
                        return null;
                    }

                    @Override
                    public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
                        return null;
                    }

                    @Override
                    public Runnable addSpreadsheetDeltaFetcherWatcherOnce(final SpreadsheetDeltaFetcherWatcher watcher) {
                        return null;
                    }
                }
            )
        );
        component.onSpreadsheetDelta(
            HttpMethod.POST,
            Url.parseAbsolute("https://example.com/1/cell/"),
            delta,
            AppContexts.fake()
        );

        this.treePrintAndCheck(
            component,
            expected
        );
    }

    @Override
    public SpreadsheetDeltaCellsTableComponent createComponent() {
        return SpreadsheetDeltaCellsTableComponent.with(
            "id-",
            SpreadsheetDeltaCellsTableComponentContexts.fake()
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetDeltaCellsTableComponent> type() {
        return SpreadsheetDeltaCellsTableComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
