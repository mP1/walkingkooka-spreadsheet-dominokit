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

import elemental2.dom.HTMLDivElement;
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
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetFormatterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportRectangle;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportWindows;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Arrays;
import java.util.Optional;

public final class SpreadsheetViewportComponentTest implements HtmlComponentTesting<SpreadsheetViewportComponent, HTMLDivElement>,
    ParseStringTesting<Optional<SpreadsheetSelection>>,
    SpreadsheetMetadataTesting {

    // id...............................................................................................................

    @Test
    public void testIdWithCell() {
        this.idAndCheck(
            SpreadsheetSelection.A1,
            "viewport-cell-A1"
        );
    }

    @Test
    public void testIdWithColumn() {
        this.idAndCheck(
            SpreadsheetSelection.parseColumn("B"),
            "viewport-column-B"
        );
    }

    @Test
    public void testIdWithRow() {
        this.idAndCheck(
            SpreadsheetSelection.parseRow("3"),
            "viewport-row-3"
        );
    }

    private void idAndCheck(final SpreadsheetSelection selection,
                            final String id) {
        this.checkEquals(
            id,
            SpreadsheetViewportComponent.id(selection),
            () -> selection + " id"
        );
    }

    // parseId.........................................................................................................

    @Override
    public void testParseStringNullFails() {
        throw new UnsupportedOperationException(); // shouldnt be invoked
    }

    @Override
    public void testParseStringEmptyFails() {
        throw new UnsupportedOperationException(); // shouldnt be invoked
    }

    @Test
    public void testParseElementIdWithNull() {
        this.parseStringAndCheck(null);
    }

    @Test
    public void testParseElementIdWithEmpty() {
        this.parseStringAndCheck(
            ""
        );
    }

    @Test
    public void testParseElementIdWithMissingViewportIdPrefix() {
        this.parseStringAndCheck(
            "xyz"
        );
    }

    @Test
    public void testParseElementIdWithInvalidSelectionType() {
        this.parseStringAndCheck(
            "viewport-invalid"
        );
    }

    @Test
    public void testParseElementIdWithInvalidSelectionType2() {
        this.parseStringAndCheck(
            "viewport-invalid-A1"
        );
    }

    @Test
    public void testParseElementIdWithCellRangeFails() {
        this.parseStringAndCheck(
            "viewport-cell-A1:A2"
        );
    }

    @Test
    public void testParseElementIdWithColumnRangeFails() {
        this.parseStringAndCheck(
            "viewport-column-B:C"
        );
    }

    @Test
    public void testParseElementIdWithLabelFails() {
        this.parseStringAndCheck(
            "viewport-cell-Label123"
        );
    }

    @Test
    public void testParseElementIdWithRowRangeFails() {
        this.parseStringAndCheck(
            "viewport-row-4:5"
        );
    }

    private void parseStringAndCheck(final String id) {
        this.parseStringAndCheck(
            id,
            Optional.empty()
        );
    }

    @Test
    public void testParseElementIdWithCell() {
        this.parseStringAndCheck(
            "viewport-cell-A1",
            SpreadsheetSelection.A1
        );
    }

    @Test
    public void testParseElementIdWithColumn() {
        this.parseStringAndCheck(
            "viewport-column-B",
            SpreadsheetSelection.parseColumn("B")
        );
    }

    @Test
    public void testParseElementIdWithRow() {
        this.parseStringAndCheck(
            "viewport-row-3",
            SpreadsheetSelection.parseRow("3")
        );
    }

    private void parseStringAndCheck(final String id,
                                     final SpreadsheetSelection selection) {
        this.parseStringAndCheck(
            id,
            Optional.of(selection)
        );

        this.idAndCheck(
            selection,
            id
        );
    }

    // ParseStringTesting...............................................................................................

    @Override
    public Optional<SpreadsheetSelection> parseString(final String id) {
        return SpreadsheetViewportComponent.parseElementId(id);
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> thrown) {
        return thrown;
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException thrown) {
        return thrown;
    }

    private final static SpreadsheetId SPREADSHEET_ID = SpreadsheetId.with(1);
    private final static SpreadsheetName SPREADSHEET_NAME = SpreadsheetName.with("SpreadsheetName111");

    private final static SpreadsheetCellReference SELECTION = SpreadsheetSelection.A1;

    private final static int CELL_WIDTH = 50;
    private final static int CELL_HEIGHT = 50;

    private final static int VIEWPORT_GRID_WIDTH = CELL_WIDTH * 2 - 1;
    private final static int VIEWPORT_GRID_HEIGHT = CELL_HEIGHT * 2 - 1;

    private final static SpreadsheetMetadata SPREADSHEET_METADATA = SpreadsheetMetadataTesting.METADATA_EN_AU.set(
        SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
        SPREADSHEET_ID
    ).set(
        SpreadsheetMetadataPropertyName.SPREADSHEET_NAME,
        SPREADSHEET_NAME
    ).set(
        SpreadsheetMetadataPropertyName.HIDE_ZERO_VALUES,
        false
    ).set(
        SpreadsheetMetadataPropertyName.STYLE,
        TextStyle.EMPTY.set(
            TextStylePropertyName.WIDTH,
            Length.parse(CELL_WIDTH + "px")
        ).set(
            TextStylePropertyName.HEIGHT,
            Length.parse(CELL_HEIGHT + "px")
        ).set(
            TextStylePropertyName.BACKGROUND_COLOR,
            Color.WHITE
        )
    ).set(
        SpreadsheetMetadataPropertyName.VIEWPORT,
        SpreadsheetViewportRectangle.with(
            SpreadsheetSelection.A1,
            VIEWPORT_GRID_WIDTH,
            VIEWPORT_GRID_HEIGHT
        ).viewport()
    );

    @Test
    public void testOnHistoryTokenSpreadsheetCellSelectWithFormulaHeadersScrollbar() {
        this.treePrintAndCheck(
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SELECTION.setDefaultAnchor()
            ),
            SPREADSHEET_METADATA,
            "SpreadsheetViewportComponent\n" +
                "  SpreadsheetViewportFormulaComponent\n" +
                "    SpreadsheetFormulaComponent\n" +
                "      ValueTextBoxComponent\n" +
                "        TextBoxComponent\n" +
                "          [=1+2]\n" +
                "  TABLE\n" +
                "    id=\"viewport\" style=\"overflow: hidden;\"\n" +
                "      THEAD\n" +
                "        SpreadsheetViewportComponentTableRowColumnHeaders\n" +
                "          TR\n" +
                "            SpreadsheetViewportComponentTableCellHeaderSelectAll\n" +
                "              TH\n" +
                "                id=\"viewport-select-all-cells\" style=\"background-color: #111111; box-sizing: border-box; height: 30px; min-height: 30px; min-width: 80px; width: 80px;\"\n" +
                "                  \"All\" [#/1/SpreadsheetName111/cell/*/bottom-right] id=viewport-select-all-cells-Link\n" +
                "            SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "              TH\n" +
                "                id=\"viewport-column-A\" style=\"background-color: #333333; box-sizing: border-box; height: 30px; min-height: 30px; min-width: 50px; width: 50px;\"\n" +
                "                  \"A\" [#/1/SpreadsheetName111/column/A] id=viewport-column-A-Link\n" +
                "            SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "              TH\n" +
                "                id=\"viewport-column-B\" style=\"background-color: #222222; box-sizing: border-box; height: 30px; min-height: 30px; min-width: 50px; width: 50px;\"\n" +
                "                  \"B\" [#/1/SpreadsheetName111/column/B] id=viewport-column-B-Link\n" +
                "      TBODY\n" +
                "        SpreadsheetViewportComponentTableRowCells\n" +
                "          TR\n" +
                "            SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow\n" +
                "              TH\n" +
                "                id=\"viewport-row-1\" style=\"background-color: #555555; box-sizing: border-box; height: 50px; min-height: 50px; min-width: 80px; width: 80px;\"\n" +
                "                  \"1\" [#/1/SpreadsheetName111/row/1] id=viewport-row-1-Link\n" +
                "            SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "              TD\n" +
                "                id=\"viewport-cell-A1\" tabIndex=0 style=\"box-sizing: border-box; color: black; height: 50px; min-height: 50px; min-width: 50px; width: 50px;\"\n" +
                "                  Text \"*** 3.0\"\n" +
                "            SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "              TD\n" +
                "                id=\"viewport-cell-B1\" tabIndex=0 style=\"background-color: white; box-sizing: border-box; height: 50px; min-height: 50px; min-width: 50px; width: 50px;\"\n" +
                "        SpreadsheetViewportComponentTableRowCells\n" +
                "          TR\n" +
                "            SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow\n" +
                "              TH\n" +
                "                id=\"viewport-row-2\" style=\"background-color: #444444; box-sizing: border-box; height: 50px; min-height: 50px; min-width: 80px; width: 80px;\"\n" +
                "                  \"2\" [#/1/SpreadsheetName111/row/2] id=viewport-row-2-Link\n" +
                "            SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "              TD\n" +
                "                id=\"viewport-cell-A2\" tabIndex=0 style=\"background-color: white; box-sizing: border-box; height: 50px; min-height: 50px; min-width: 50px; width: 50px;\"\n" +
                "            SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "              TD\n" +
                "                id=\"viewport-cell-B2\" tabIndex=0 style=\"background-color: white; box-sizing: border-box; height: 50px; min-height: 50px; min-width: 50px; width: 50px;\"\n" +
                "  SpreadsheetViewportScrollbarComponentRows\n" +
                "    FlexLayoutComponent\n" +
                "      COLUMN\n" +
                "        id=viewport-vertical-scrollbar-Layout\n" +
                "          mdi-arrow-up [#/1/SpreadsheetName111/cell/A1/navigate/A1/up%2099px] id=viewport-vertical-scrollbar-up-Link\n" +
                "          SliderComponent\n" +
                "            VERTICAL\n" +
                "              [0.0] min=1.0 max=1048575.0 id=viewport-vertical-scrollbar-value-Slider\n" +
                "          [#/1/SpreadsheetName111/cell/A1/navigate/A1/down%2099px] mdi-arrow-down id=viewport-vertical-scrollbar-down-Link\n" +
                "    SpreadsheetViewportScrollbarComponentColumns\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          id=viewport-horizontal-scrollbar-Layout\n" +
                "            mdi-arrow-left [#/1/SpreadsheetName111/cell/A1/navigate/A1/left%2099px] id=viewport-horizontal-scrollbar-left-Link\n" +
                "            SliderComponent\n" +
                "              HORIZONTAL\n" +
                "                [0.0] min=1.0 max=16383.0 id=viewport-horizontal-scrollbar-value-Slider\n" +
                "            [#/1/SpreadsheetName111/cell/A1/navigate/A1/right%2099px] mdi-arrow-right id=viewport-horizontal-scrollbar-right-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenSpreadsheetColumnSelectWithFormulaHeadersScrollbar() {
        this.treePrintAndCheck(
            HistoryToken.columnSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SELECTION.column()
                    .setDefaultAnchor() // A
            ),
            SPREADSHEET_METADATA,
            "SpreadsheetViewportComponent\n" +
                "  SpreadsheetViewportFormulaComponent\n" +
                "    SpreadsheetFormulaComponent\n" +
                "      ValueTextBoxComponent\n" +
                "        TextBoxComponent\n" +
                "          [] DISABLED\n" +
                "  TABLE\n" +
                "    id=\"viewport\" style=\"overflow: hidden;\"\n" +
                "      THEAD\n" +
                "        SpreadsheetViewportComponentTableRowColumnHeaders\n" +
                "          TR\n" +
                "            SpreadsheetViewportComponentTableCellHeaderSelectAll\n" +
                "              TH\n" +
                "                id=\"viewport-select-all-cells\" style=\"background-color: #111111; box-sizing: border-box; height: 30px; min-height: 30px; min-width: 80px; width: 80px;\"\n" +
                "                  \"All\" [#/1/SpreadsheetName111/cell/*/bottom-right] id=viewport-select-all-cells-Link\n" +
                "            SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "              TH\n" +
                "                id=\"viewport-column-A\" style=\"background-color: #333333; box-sizing: border-box; height: 30px; min-height: 30px; min-width: 50px; width: 50px;\"\n" +
                "                  \"A\" [#/1/SpreadsheetName111/column/A] id=viewport-column-A-Link\n" +
                "            SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "              TH\n" +
                "                id=\"viewport-column-B\" style=\"background-color: #222222; box-sizing: border-box; height: 30px; min-height: 30px; min-width: 50px; width: 50px;\"\n" +
                "                  \"B\" [#/1/SpreadsheetName111/column/B] id=viewport-column-B-Link\n" +
                "      TBODY\n" +
                "        SpreadsheetViewportComponentTableRowCells\n" +
                "          TR\n" +
                "            SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow\n" +
                "              TH\n" +
                "                id=\"viewport-row-1\" style=\"background-color: #444444; box-sizing: border-box; height: 50px; min-height: 50px; min-width: 80px; width: 80px;\"\n" +
                "                  \"1\" [#/1/SpreadsheetName111/row/1] id=viewport-row-1-Link\n" +
                "            SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "              TD\n" +
                "                id=\"viewport-cell-A1\" tabIndex=0 style=\"box-sizing: border-box; color: black; height: 50px; min-height: 50px; min-width: 50px; width: 50px;\"\n" +
                "                  Text \"*** 3.0\"\n" +
                "            SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "              TD\n" +
                "                id=\"viewport-cell-B1\" tabIndex=0 style=\"background-color: white; box-sizing: border-box; height: 50px; min-height: 50px; min-width: 50px; width: 50px;\"\n" +
                "        SpreadsheetViewportComponentTableRowCells\n" +
                "          TR\n" +
                "            SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow\n" +
                "              TH\n" +
                "                id=\"viewport-row-2\" style=\"background-color: #444444; box-sizing: border-box; height: 50px; min-height: 50px; min-width: 80px; width: 80px;\"\n" +
                "                  \"2\" [#/1/SpreadsheetName111/row/2] id=viewport-row-2-Link\n" +
                "            SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "              TD\n" +
                "                id=\"viewport-cell-A2\" tabIndex=0 style=\"box-sizing: border-box; color: black; height: 50px; min-height: 50px; min-width: 50px; width: 50px;\"\n" +
                "            SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "              TD\n" +
                "                id=\"viewport-cell-B2\" tabIndex=0 style=\"background-color: white; box-sizing: border-box; height: 50px; min-height: 50px; min-width: 50px; width: 50px;\"\n" +
                "  SpreadsheetViewportScrollbarComponentRows\n" +
                "    FlexLayoutComponent\n" +
                "      COLUMN\n" +
                "        id=viewport-vertical-scrollbar-Layout\n" +
                "          mdi-arrow-up [#/1/SpreadsheetName111/column/A/navigate/A1/up%2099px] id=viewport-vertical-scrollbar-up-Link\n" +
                "          SliderComponent\n" +
                "            VERTICAL\n" +
                "              [0.0] min=1.0 max=1048575.0 id=viewport-vertical-scrollbar-value-Slider\n" +
                "          [#/1/SpreadsheetName111/column/A/navigate/A1/down%2099px] mdi-arrow-down id=viewport-vertical-scrollbar-down-Link\n" +
                "    SpreadsheetViewportScrollbarComponentColumns\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          id=viewport-horizontal-scrollbar-Layout\n" +
                "            mdi-arrow-left [#/1/SpreadsheetName111/column/A/navigate/A1/left%2099px] id=viewport-horizontal-scrollbar-left-Link\n" +
                "            SliderComponent\n" +
                "              HORIZONTAL\n" +
                "                [0.0] min=1.0 max=16383.0 id=viewport-horizontal-scrollbar-value-Slider\n" +
                "            [#/1/SpreadsheetName111/column/A/navigate/A1/right%2099px] mdi-arrow-right id=viewport-horizontal-scrollbar-right-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenSpreadsheetRowSelectWithFormulaHeadersScrollbar() {
        this.treePrintAndCheck(
            HistoryToken.rowSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SELECTION.row()
                    .setDefaultAnchor() // 1
            ),
            SPREADSHEET_METADATA,
            "SpreadsheetViewportComponent\n" +
                "  SpreadsheetViewportFormulaComponent\n" +
                "    SpreadsheetFormulaComponent\n" +
                "      ValueTextBoxComponent\n" +
                "        TextBoxComponent\n" +
                "          [] DISABLED\n" +
                "  TABLE\n" +
                "    id=\"viewport\" style=\"overflow: hidden;\"\n" +
                "      THEAD\n" +
                "        SpreadsheetViewportComponentTableRowColumnHeaders\n" +
                "          TR\n" +
                "            SpreadsheetViewportComponentTableCellHeaderSelectAll\n" +
                "              TH\n" +
                "                id=\"viewport-select-all-cells\" style=\"background-color: #111111; box-sizing: border-box; height: 30px; min-height: 30px; min-width: 80px; width: 80px;\"\n" +
                "                  \"All\" [#/1/SpreadsheetName111/cell/*/bottom-right] id=viewport-select-all-cells-Link\n" +
                "            SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "              TH\n" +
                "                id=\"viewport-column-A\" style=\"background-color: #222222; box-sizing: border-box; height: 30px; min-height: 30px; min-width: 50px; width: 50px;\"\n" +
                "                  \"A\" [#/1/SpreadsheetName111/column/A] id=viewport-column-A-Link\n" +
                "            SpreadsheetViewportComponentTableCellHeaderSpreadsheetColumn\n" +
                "              TH\n" +
                "                id=\"viewport-column-B\" style=\"background-color: #222222; box-sizing: border-box; height: 30px; min-height: 30px; min-width: 50px; width: 50px;\"\n" +
                "                  \"B\" [#/1/SpreadsheetName111/column/B] id=viewport-column-B-Link\n" +
                "      TBODY\n" +
                "        SpreadsheetViewportComponentTableRowCells\n" +
                "          TR\n" +
                "            SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow\n" +
                "              TH\n" +
                "                id=\"viewport-row-1\" style=\"background-color: #555555; box-sizing: border-box; height: 50px; min-height: 50px; min-width: 80px; width: 80px;\"\n" +
                "                  \"1\" [#/1/SpreadsheetName111/row/1] id=viewport-row-1-Link\n" +
                "            SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "              TD\n" +
                "                id=\"viewport-cell-A1\" tabIndex=0 style=\"box-sizing: border-box; color: black; height: 50px; min-height: 50px; min-width: 50px; width: 50px;\"\n" +
                "                  Text \"*** 3.0\"\n" +
                "            SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "              TD\n" +
                "                id=\"viewport-cell-B1\" tabIndex=0 style=\"box-sizing: border-box; color: black; height: 50px; min-height: 50px; min-width: 50px; width: 50px;\"\n" +
                "        SpreadsheetViewportComponentTableRowCells\n" +
                "          TR\n" +
                "            SpreadsheetViewportComponentTableCellHeaderSpreadsheetRow\n" +
                "              TH\n" +
                "                id=\"viewport-row-2\" style=\"background-color: #444444; box-sizing: border-box; height: 50px; min-height: 50px; min-width: 80px; width: 80px;\"\n" +
                "                  \"2\" [#/1/SpreadsheetName111/row/2] id=viewport-row-2-Link\n" +
                "            SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "              TD\n" +
                "                id=\"viewport-cell-A2\" tabIndex=0 style=\"background-color: white; box-sizing: border-box; height: 50px; min-height: 50px; min-width: 50px; width: 50px;\"\n" +
                "            SpreadsheetViewportComponentTableCellSpreadsheetCell\n" +
                "              TD\n" +
                "                id=\"viewport-cell-B2\" tabIndex=0 style=\"background-color: white; box-sizing: border-box; height: 50px; min-height: 50px; min-width: 50px; width: 50px;\"\n" +
                "  SpreadsheetViewportScrollbarComponentRows\n" +
                "    FlexLayoutComponent\n" +
                "      COLUMN\n" +
                "        id=viewport-vertical-scrollbar-Layout\n" +
                "          mdi-arrow-up [#/1/SpreadsheetName111/row/1/navigate/A1/up%2099px] id=viewport-vertical-scrollbar-up-Link\n" +
                "          SliderComponent\n" +
                "            VERTICAL\n" +
                "              [0.0] min=1.0 max=1048575.0 id=viewport-vertical-scrollbar-value-Slider\n" +
                "          [#/1/SpreadsheetName111/row/1/navigate/A1/down%2099px] mdi-arrow-down id=viewport-vertical-scrollbar-down-Link\n" +
                "    SpreadsheetViewportScrollbarComponentColumns\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          id=viewport-horizontal-scrollbar-Layout\n" +
                "            mdi-arrow-left [#/1/SpreadsheetName111/row/1/navigate/A1/left%2099px] id=viewport-horizontal-scrollbar-left-Link\n" +
                "            SliderComponent\n" +
                "              HORIZONTAL\n" +
                "                [0.0] min=1.0 max=16383.0 id=viewport-horizontal-scrollbar-value-Slider\n" +
                "            [#/1/SpreadsheetName111/row/1/navigate/A1/right%2099px] mdi-arrow-right id=viewport-horizontal-scrollbar-right-Link\n"
        );
    }

// renderingContextMenu fails because of some native Element calls.
//    @Test
//    public void testOnHistoryTokenSpreadsheetCellMenuWithFormulaHeadersScrollbar() {
//        this.treePrintAndCheck(
//            HistoryToken.cellMenu(
//                SPREADSHEET_ID,
//                SPREADSHEET_NAME,
//                SELECTION.setDefaultAnchor()
//            ),
//            SPREADSHEET_METADATA,
//            "@"
//        );
//    }

    private void treePrintAndCheck(final HistoryToken historyToken,
                                   final SpreadsheetMetadata metadata,
                                   final String expected) {
        final AppContext appContext = new FakeAppContext() {

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return this.historyTokenWatcher.add(watcher);
            }

            @Override
            public void fireCurrentHistoryToken() {
                this.historyTokenWatcher.onHistoryTokenChange(
                    this.historyToken(),
                    this
                );
            }

            private final HistoryTokenWatchers historyTokenWatcher = HistoryTokenWatchers.empty();

            @Override
            public HistoryToken historyToken() {
                return historyToken;
            }

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return metadata;
            }

            @Override
            public void debug(final Object... values) {
                System.out.println("DEBUG " + Arrays.toString(values));
            }
        };

        final SpreadsheetViewportCacheContext cacheContext = new FakeSpreadsheetViewportCacheContext() {
            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return appContext.addHistoryTokenWatcher(watcher);
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
                return appContext.historyToken();
            }

            @Override
            public boolean shouldHideZeroValues() {
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
            public SpreadsheetViewportCache spreadsheetViewportCache() {
                return this.spreadsheetViewportCache;
            }

            private final SpreadsheetViewportCache spreadsheetViewportCache = SpreadsheetViewportCache.empty(cacheContext);

            @Override
            public void debug(final Object... values) {
                appContext.debug(values);
            }
        };

        final SpreadsheetViewportComponent component = SpreadsheetViewportComponent.empty(
            new FakeSpreadsheetViewportComponentContext() {

                @Override
                public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                    return appContext.addHistoryTokenWatcher(watcher);
                }

                @Override
                public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
                    return null;
                }

                @Override
                public Runnable addSpreadsheetFormatterFetcherWatcher(final SpreadsheetFormatterFetcherWatcher watcher) {
                    return null;
                }

                @Override
                public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                    return null;
                }

                @Override
                public HistoryToken historyToken() {
                    return tableContext.historyToken();
                }

                @Override
                public SpreadsheetMetadata spreadsheetMetadata() {
                    return appContext.spreadsheetMetadata();
                }

                @Override
                public SpreadsheetViewportCache spreadsheetViewportCache() {
                    return tableContext.spreadsheetViewportCache();
                }

                @Override
                public TextStyle allCellsStyle() {
                    return TextStyle.parse("background-color: #111");
                }

                @Override
                public TextStyle cellStyle() {
                    return TextStyle.parse("background-color: white;");
                }

                @Override
                public TextStyle selectedCellStyle(final TextStyle cellStyle) {
                    return cellStyle.merge(
                        TextStyle.parse("color: black;")
                    );
                }

                @Override
                public TextStyle columnStyle() {
                    return TextStyle.parse("background-color: #222;");
                }

                @Override
                public TextStyle selectedColumnStyle() {
                    return TextStyle.parse("background-color: #333;");
                }

                @Override
                public TextStyle rowStyle() {
                    return TextStyle.parse("background-color: #444;");
                }

                @Override
                public TextStyle selectedRowStyle() {
                    return TextStyle.parse("background-color: #555;");
                }

                @Override
                public void debug(final Object... values) {
                    appContext.debug(values);
                }
            }
        );

        component.metadata = metadata;

        component.spreadsheetFormatterSelectorSelection = SELECTION;

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
                ).setWindow(SpreadsheetViewportWindows.parse("A1:B2")),
                appContext
            );

        component.viewportGridWidth = VIEWPORT_GRID_WIDTH;
        component.viewportGridHeight = VIEWPORT_GRID_HEIGHT;

        appContext.fireCurrentHistoryToken();

        this.treePrintAndCheck(
            component,
            expected
        );
    }

    // ClassTesting....................................................................................................

    @Override
    public Class<SpreadsheetViewportComponent> type() {
        return SpreadsheetViewportComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
