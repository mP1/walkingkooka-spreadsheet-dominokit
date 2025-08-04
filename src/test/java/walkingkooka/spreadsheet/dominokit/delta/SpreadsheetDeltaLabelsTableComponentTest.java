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
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetDeltaLabelsTableComponentTest implements TableComponentTesting<HTMLDivElement, SpreadsheetDelta, SpreadsheetDeltaLabelsTableComponent> {

    private final static SpreadsheetLabelName LABEL = SpreadsheetSelection.labelName("LabelXYZ");

    // with.............................................................................................................

    @Test
    public void testWithNullIdFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetDeltaLabelsTableComponent.with(
                null,
                SpreadsheetDeltaLabelsTableComponentContexts.fake()
            )
        );
    }

    @Test
    public void testWithEmptyIdFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetDeltaLabelsTableComponent.with(
                "",
                SpreadsheetDeltaLabelsTableComponentContexts.fake()
            )
        );
    }

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetDeltaLabelsTableComponent.with(
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
            "SpreadsheetDeltaLabelsTableComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      SpreadsheetDataTableComponent\n" +
                "        id=ID123-labels-Table\n" +
                "        COLUMN(S)\n" +
                "          Label\n" +
                "          Cell\n" +
                "          Formatted\n" +
                "          Links\n" +
                "        CHILDREN\n" +
                "          SpreadsheetFlexLayout\n" +
                "            ROW\n" +
                "              mdi-arrow-left \"previous\" DISABLED id=ID123-previous-Link\n" +
                "              \"next\" DISABLED mdi-arrow-right id=ID123-next-Link\n" +
                "        PLUGINS\n" +
                "          BodyScrollPlugin\n"
        );
    }

    @Test
    public void testRenderSpreadsheetDeltaOneLabelWithoutCell() {
        this.renderAndCheck(
            SpreadsheetDelta.EMPTY.setLabels(
                Sets.of(
                    LABEL.setLabelMappingReference(SpreadsheetSelection.A1)
                )
            ),
            "SpreadsheetDeltaLabelsTableComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      SpreadsheetDataTableComponent\n" +
                "        id=ID123-labels-Table\n" +
                "        COLUMN(S)\n" +
                "          Label\n" +
                "          Cell\n" +
                "          Formatted\n" +
                "          Links\n" +
                "        ROW(S)\n" +
                "          ROW 0\n" +
                "            \"LabelXYZ\" [#/1/Spreadsheet222/label/LabelXYZ] id=ID123-labels-LabelXYZ-Link\n" +
                "            \"A1\" [#/1/Spreadsheet222/cell/LabelXYZ/formula] id=ID123-labels-LabelXYZ-formula-Link\n" +
                "            TextNodeComponent\n" +
                "            SpreadsheetLabelLinksComponent\n" +
                "              SpreadsheetLinkListComponent\n" +
                "                SpreadsheetFlexLayout\n" +
                "                  ROW\n" +
                "                    \"References\" [#/1/Spreadsheet222/cell/LabelXYZ/references] (2) id=ID123-labels-LabelXYZ-references-Link\n" +
                "                    \"Delete\" [#/1/Spreadsheet222/label/LabelXYZ/delete] id=ID123-labels-LabelXYZ-delete-Link\n" +
                "        CHILDREN\n" +
                "          SpreadsheetFlexLayout\n" +
                "            ROW\n" +
                "              mdi-arrow-left \"previous\" DISABLED id=ID123-previous-Link\n" +
                "              \"next\" DISABLED mdi-arrow-right id=ID123-next-Link\n" +
                "        PLUGINS\n" +
                "          BodyScrollPlugin\n"
        );
    }

    @Test
    public void testRenderSpreadsheetDeltaOneLabelWithCell() {
        this.renderAndCheck(
            SpreadsheetDelta.EMPTY.setLabels(
                Sets.of(
                    LABEL.setLabelMappingReference(SpreadsheetSelection.A1)
                )
            ).setCells(
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
            ),
            "SpreadsheetDeltaLabelsTableComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      SpreadsheetDataTableComponent\n" +
                "        id=ID123-labels-Table\n" +
                "        COLUMN(S)\n" +
                "          Label\n" +
                "          Cell\n" +
                "          Formatted\n" +
                "          Links\n" +
                "        ROW(S)\n" +
                "          ROW 0\n" +
                "            \"LabelXYZ\" [#/1/Spreadsheet222/label/LabelXYZ] id=ID123-labels-LabelXYZ-Link\n" +
                "            \"A1\" [#/1/Spreadsheet222/cell/LabelXYZ/formula] id=ID123-labels-LabelXYZ-formula-Link\n" +
                "            TextNodeComponent\n" +
                "            SpreadsheetLabelLinksComponent\n" +
                "              SpreadsheetLinkListComponent\n" +
                "                SpreadsheetFlexLayout\n" +
                "                  ROW\n" +
                "                    \"References\" [#/1/Spreadsheet222/cell/LabelXYZ/references] (2) id=ID123-labels-LabelXYZ-references-Link\n" +
                "                    \"Delete\" [#/1/Spreadsheet222/label/LabelXYZ/delete] id=ID123-labels-LabelXYZ-delete-Link\n" +
                "        CHILDREN\n" +
                "          SpreadsheetFlexLayout\n" +
                "            ROW\n" +
                "              mdi-arrow-left \"previous\" DISABLED id=ID123-previous-Link\n" +
                "              \"next\" DISABLED mdi-arrow-right id=ID123-next-Link\n" +
                "        PLUGINS\n" +
                "          BodyScrollPlugin\n"
        );
    }

    @Test
    public void testRenderSpreadsheetDeltaSeveralLabels() {
        this.renderAndCheck(
            SpreadsheetDelta.EMPTY.setLabels(
                Sets.of(
                    LABEL.setLabelMappingReference(SpreadsheetSelection.A1),
                    SpreadsheetSelection.labelName("LABEL2")
                        .setLabelMappingReference(
                            SpreadsheetSelection.parseCell("B2")
                        )
                )
            ).setCells(
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
            ),
            "SpreadsheetDeltaLabelsTableComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      SpreadsheetDataTableComponent\n" +
                "        id=ID123-labels-Table\n" +
                "        COLUMN(S)\n" +
                "          Label\n" +
                "          Cell\n" +
                "          Formatted\n" +
                "          Links\n" +
                "        ROW(S)\n" +
                "          ROW 0\n" +
                "            \"LABEL2\" [#/1/Spreadsheet222/label/LABEL2] id=ID123-labels-LABEL2-Link\n" +
                "            \"LABEL2\" [#/1/Spreadsheet222/cell/LABEL2/formula] id=ID123-labels-LABEL2-formula-Link\n" +
                "            TextNodeComponent\n" +
                "            SpreadsheetLabelLinksComponent\n" +
                "              SpreadsheetLinkListComponent\n" +
                "                SpreadsheetFlexLayout\n" +
                "                  ROW\n" +
                "                    \"References\" [#/1/Spreadsheet222/cell/LABEL2/references] (2) id=ID123-labels-LABEL2-references-Link\n" +
                "                    \"Delete\" [#/1/Spreadsheet222/label/LABEL2/delete] id=ID123-labels-LABEL2-delete-Link\n" +
                "          ROW 1\n" +
                "            \"LabelXYZ\" [#/1/Spreadsheet222/label/LabelXYZ] id=ID123-labels-LabelXYZ-Link\n" +
                "            \"A1\" [#/1/Spreadsheet222/cell/LabelXYZ/formula] id=ID123-labels-LabelXYZ-formula-Link\n" +
                "            TextNodeComponent\n" +
                "            SpreadsheetLabelLinksComponent\n" +
                "              SpreadsheetLinkListComponent\n" +
                "                SpreadsheetFlexLayout\n" +
                "                  ROW\n" +
                "                    \"References\" [#/1/Spreadsheet222/cell/LabelXYZ/references] (2) id=ID123-labels-LabelXYZ-references-Link\n" +
                "                    \"Delete\" [#/1/Spreadsheet222/label/LabelXYZ/delete] id=ID123-labels-LabelXYZ-delete-Link\n" +
                "        CHILDREN\n" +
                "          SpreadsheetFlexLayout\n" +
                "            ROW\n" +
                "              mdi-arrow-left \"previous\" DISABLED id=ID123-previous-Link\n" +
                "              \"next\" DISABLED mdi-arrow-right id=ID123-next-Link\n" +
                "        PLUGINS\n" +
                "          BodyScrollPlugin\n"
        );
    }

    private void renderAndCheck(final SpreadsheetDelta delta,
                                final String expected) {
        final SpreadsheetDeltaLabelsTableComponent component = SpreadsheetDeltaLabelsTableComponent.with(
            "ID123-",
            SpreadsheetDeltaLabelsTableComponentContexts.basic(
                (SpreadsheetExpressionReference r) -> Optional.ofNullable(
                    r.equals(SpreadsheetSelection.A1) ?
                        "=1+2" :
                        null
                ),
                (SpreadsheetExpressionReference r) -> Sets.of(
                    SpreadsheetSelection.A1,
                    SpreadsheetSelection.parseCell("B2")
                ), // cellReferences
                (SpreadsheetLabelName l) -> Optional.ofNullable(
                    LABEL.equals(l) ?
                        SpreadsheetSelection.A1.setFormula(
                            SpreadsheetFormula.EMPTY.setText("=1+2")
                        ) :
                        null
                ),// cellToLabel
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
                (l) -> Optional.ofNullable(
                    LABEL.equals(l) ?
                        SpreadsheetSelection.A1 :
                        null
                ),
                new FakeHistoryContext() {
                    @Override
                    public HistoryToken historyToken() {
                        return HistoryToken.parseString("/1/Spreadsheet222/label/LABELXYZ");
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
    public SpreadsheetDeltaLabelsTableComponent createComponent() {
        return SpreadsheetDeltaLabelsTableComponent.with(
            "id-",
            SpreadsheetDeltaLabelsTableComponentContexts.fake()
        );
    }

// class............................................................................................................

    @Override
    public Class<SpreadsheetDeltaLabelsTableComponent> type() {
        return SpreadsheetDeltaLabelsTableComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
