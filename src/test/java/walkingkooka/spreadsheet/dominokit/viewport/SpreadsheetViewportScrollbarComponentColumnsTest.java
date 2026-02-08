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
import walkingkooka.Cast;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportWindows;

public final class SpreadsheetViewportScrollbarComponentColumnsTest extends SpreadsheetViewportScrollbarComponentTestCase<SpreadsheetColumnReference> {

    @Test
    public void testTreePrintAfterOnSpreadsheetListHistoryToken() {
        final TestAppContext appContext = new TestAppContext(
            SpreadsheetSelection.parseCell("C3"), // home
            CELL_SELECT
        );

        final SpreadsheetViewportScrollbarComponent<SpreadsheetColumnReference> component = SpreadsheetViewportScrollbarComponent.columns(
            new TestSpreadsheetViewportScrollbarComponentContext(appContext)
        );
        appContext.pushHistoryToken(SPREADSHEET_LIST);

        this.treePrintAndCheck(
            component,
            "SpreadsheetViewportScrollbarComponentColumns\n" +
                "  FlexLayoutComponent\n" +
                "    ROW\n" +
                "      id=viewport-horizontal-scrollbar-Layout\n" +
                "        mdi-arrow-left DISABLED id=viewport-horizontal-scrollbar-left-Link\n" +
                "        SliderComponent\n" +
                "          HORIZONTAL\n" +
                "            [] min=1.0 max=16384.0 id=viewport-horizontal-scrollbar-value-Slider\n" +
                "        DISABLED mdi-arrow-right id=viewport-horizontal-scrollbar-right-Link\n"
        );
    }

    @Test
    public void testTreePrintAfterOnSpreadsheetSelectHistoryToken() {
        final TestAppContext appContext = new TestAppContext(
            SpreadsheetSelection.parseCell("C3"), // home
            CELL_SELECT
        );

        final SpreadsheetViewportScrollbarComponent<SpreadsheetColumnReference> component = SpreadsheetViewportScrollbarComponent.columns(
            new TestSpreadsheetViewportScrollbarComponentContext(appContext)
        );
        appContext.pushHistoryToken(SPREADSHEET_SELECT);

        this.treePrintAndCheck(
            component,
            "SpreadsheetViewportScrollbarComponentColumns\n" +
                "  FlexLayoutComponent\n" +
                "    ROW\n" +
                "      id=viewport-horizontal-scrollbar-Layout\n" +
                "        mdi-arrow-left [#/1/SpreadsheetName222/navigate/C3/scroll%20left%201000px] id=viewport-horizontal-scrollbar-left-Link\n" +
                "        SliderComponent\n" +
                "          HORIZONTAL\n" +
                "            [3.0] min=1.0 max=16384.0 id=viewport-horizontal-scrollbar-value-Slider\n" +
                "        [#/1/SpreadsheetName222/navigate/C3/scroll%20right%201000px] mdi-arrow-right id=viewport-horizontal-scrollbar-right-Link\n"
        );
    }

    @Test
    public void testTreePrintAfterOnSpreadsheetCellSelectHistoryToken() {
        final TestAppContext appContext = new TestAppContext(
            SpreadsheetSelection.parseCell("C3"), // home
            CELL_SELECT
        );

        final SpreadsheetViewportScrollbarComponent<SpreadsheetColumnReference> component = SpreadsheetViewportScrollbarComponent.columns(
            new TestSpreadsheetViewportScrollbarComponentContext(appContext)
        );
        appContext.spreadsheetViewportCache()
            .setWindows(
                SpreadsheetViewportWindows.parse("A1:C3")
            );
        appContext.pushHistoryToken(CELL_SELECT);

        this.treePrintAndCheck(
            component,
            "SpreadsheetViewportScrollbarComponentColumns\n" +
                "  FlexLayoutComponent\n" +
                "    ROW\n" +
                "      id=viewport-horizontal-scrollbar-Layout\n" +
                "        mdi-arrow-left [#/1/SpreadsheetName222/cell/A1/navigate/C3/scroll%20left%201000px] id=viewport-horizontal-scrollbar-left-Link\n" +
                "        SliderComponent\n" +
                "          HORIZONTAL\n" +
                "            [3.0] min=1.0 max=16384.0 id=viewport-horizontal-scrollbar-value-Slider\n" +
                "        [#/1/SpreadsheetName222/cell/A1/navigate/C3/scroll%20right%201000px] mdi-arrow-right id=viewport-horizontal-scrollbar-right-Link\n"
        );
    }

    @Override
    public SpreadsheetViewportScrollbarComponent<SpreadsheetColumnReference> createComponent() {
        return SpreadsheetViewportScrollbarComponent.columns(
            new TestSpreadsheetViewportScrollbarComponentContext(
                new TestAppContext(
                    HOME,
                    CELL_SELECT
                )
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetViewportScrollbarComponent<SpreadsheetColumnReference>> type() {
        return Cast.to(SpreadsheetViewportScrollbarComponentColumns.class);
    }
}
