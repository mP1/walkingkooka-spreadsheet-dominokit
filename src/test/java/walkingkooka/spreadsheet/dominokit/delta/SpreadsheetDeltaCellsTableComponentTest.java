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
import walkingkooka.spreadsheet.dominokit.history.FakeHistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.value.TableComponentTesting;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.format.SpreadsheetText;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.validation.ValidationValueTypeName;

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
                "  CardComponent\n" +
                "    Card\n" +
                "      SpreadsheetDataTableComponent\n" +
                "        id=ID123-cells-Table\n" +
                "        COLUMN(S)\n" +
                "          Cell\n" +
                "          Formula\n" +
                "          Value\n" +
                "          Formatted\n" +
                "          Links\n" +
                "        CHILDREN\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              mdi-arrow-left \"previous\" DISABLED id=ID123-previous-Link\n" +
                "              \"next\" DISABLED mdi-arrow-right id=ID123-next-Link\n" +
                "        PLUGINS\n" +
                "          BodyScrollPlugin\n"
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
                "  CardComponent\n" +
                "    Card\n" +
                "      SpreadsheetDataTableComponent\n" +
                "        id=ID123-cells-Table\n" +
                "        COLUMN(S)\n" +
                "          Cell\n" +
                "          Formula\n" +
                "          Value\n" +
                "          Formatted\n" +
                "          Links\n" +
                "        ROW(S)\n" +
                "          ROW 0\n" +
                "            \"A1\" [#/1/Spreadsheet222/cell/A1] id=ID123-cells-A1-Link\n" +
                "            \"A1=1+2+3000\" [#/1/Spreadsheet222/cell/A1/formula] id=ID123-cells-A1-formula-Link\n" +
                "            SpreadsheetTextComponent\n" +
                "              \"\"\n" +
                "            TextNodeComponent\n" +
                "              Hello\n" +
                "            SpreadsheetCellLinksComponent\n" +
                "              AnchorListComponent\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    \"Value\" [#/1/Spreadsheet222/cell/A1/value/text] id=ID123-cells-A1-value-Link\n" +
                "                    \"Create Label\" [#/1/Spreadsheet222/cell/A1/label] id=ID123-cells-A1-createLabel-Link\n" +
                "                    \"Labels\" [#/1/Spreadsheet222/cell/A1/labels] (2) id=ID123-cells-A1-label-Link\n" +
                "                    \"References\" [#/1/Spreadsheet222/cell/A1/references] (2) id=ID123-cells-A1-references-Link\n" +
                "                    \"Delete\" [#/1/Spreadsheet222/cell/A1/delete] id=ID123-cells-A1-delete-Link\n" +
                "        CHILDREN\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              mdi-arrow-left \"previous\" DISABLED id=ID123-previous-Link\n" +
                "              \"next\" DISABLED mdi-arrow-right id=ID123-next-Link\n" +
                "        PLUGINS\n" +
                "          BodyScrollPlugin\n"
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
                "  CardComponent\n" +
                "    Card\n" +
                "      SpreadsheetDataTableComponent\n" +
                "        id=ID123-cells-Table\n" +
                "        COLUMN(S)\n" +
                "          Cell\n" +
                "          Formula\n" +
                "          Value\n" +
                "          Formatted\n" +
                "          Links\n" +
                "        ROW(S)\n" +
                "          ROW 0\n" +
                "            \"A1\" [#/1/Spreadsheet222/cell/A1] id=ID123-cells-A1-Link\n" +
                "            \"A1=1+2+3000\" [#/1/Spreadsheet222/cell/A1/formula] id=ID123-cells-A1-formula-Link\n" +
                "            SpreadsheetTextComponent\n" +
                "              \"\"\n" +
                "            TextNodeComponent\n" +
                "              Hello\n" +
                "            SpreadsheetCellLinksComponent\n" +
                "              AnchorListComponent\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    \"Value\" [#/1/Spreadsheet222/cell/A1/value/text] id=ID123-cells-A1-value-Link\n" +
                "                    \"Create Label\" [#/1/Spreadsheet222/cell/A1/label] id=ID123-cells-A1-createLabel-Link\n" +
                "                    \"Labels\" [#/1/Spreadsheet222/cell/A1/labels] (2) id=ID123-cells-A1-label-Link\n" +
                "                    \"References\" [#/1/Spreadsheet222/cell/A1/references] (2) id=ID123-cells-A1-references-Link\n" +
                "                    \"Delete\" [#/1/Spreadsheet222/cell/A1/delete] id=ID123-cells-A1-delete-Link\n" +
                "          ROW 1\n" +
                "            \"A2\" [#/1/Spreadsheet222/cell/A2] id=ID123-cells-A2-Link\n" +
                "            \"A2\" [#/1/Spreadsheet222/cell/A2/formula] id=ID123-cells-A2-formula-Link\n" +
                "            SpreadsheetTextComponent\n" +
                "              \"\"\n" +
                "            TextNodeComponent\n" +
                "              2222\n" +
                "            SpreadsheetCellLinksComponent\n" +
                "              AnchorListComponent\n" +
                "                FlexLayoutComponent\n" +
                "                  ROW\n" +
                "                    \"Value\" DISABLED id=ID123-cells-A2-value-Link\n" + // cell missing valueType so should be disabled
                "                    \"Create Label\" [#/1/Spreadsheet222/cell/A2/label] id=ID123-cells-A2-createLabel-Link\n" +
                "                    \"Labels\" [#/1/Spreadsheet222/cell/A2/labels] (2) id=ID123-cells-A2-label-Link\n" +
                "                    \"References\" [#/1/Spreadsheet222/cell/A2/references] (2) id=ID123-cells-A2-references-Link\n" +
                "                    \"Delete\" [#/1/Spreadsheet222/cell/A2/delete] id=ID123-cells-A2-delete-Link\n" +
                "        CHILDREN\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              mdi-arrow-left \"previous\" DISABLED id=ID123-previous-Link\n" +
                "              \"next\" DISABLED mdi-arrow-right id=ID123-next-Link\n" +
                "        PLUGINS\n" +
                "          BodyScrollPlugin\n"
        );
    }

    private void renderAndCheck(final SpreadsheetDelta delta,
                                final String expected) {
        final SpreadsheetDeltaCellsTableComponent component = SpreadsheetDeltaCellsTableComponent.with(
            "ID123-",
            SpreadsheetDeltaCellsTableComponentContexts.basic(
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
                },
                (SpreadsheetExpressionReference r) -> Sets.of(
                    SpreadsheetSelection.labelName("LabelReference111"),
                    SpreadsheetSelection.labelName("LabelReference222")
                ), // cellLabels
                (SpreadsheetExpressionReference r) -> Sets.of(
                    SpreadsheetSelection.A1,
                    SpreadsheetSelection.parseCell("B2")
                ), // cellReferences
                (SpreadsheetExpressionReference r) -> {
                    switch (r.toString().toUpperCase()) {
                        case "A1":
                            return Optional.of("A1=1+2+3000");
                        default:
                            return Optional.empty();
                    }
                }, // formulaText
                (SpreadsheetSelection s) -> {
                    switch (s.toString().toUpperCase()) {
                        case "A1":
                            return Optional.of(
                                SpreadsheetSelection.A1.setFormula(
                                    SpreadsheetFormula.EMPTY.setText("A1=1+2+3000")
                                        .setValueType(
                                            Optional.of(ValidationValueTypeName.TEXT)
                                        )
                                )
                            );
                        default:
                            return Optional.empty();
                    }
                }, // selectionToCell
                new FakeHistoryContext() {
                    @Override
                    public HistoryToken historyToken() {
                        return HistoryToken.parseString("/1/Spreadsheet222/cell/A1/find");
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
