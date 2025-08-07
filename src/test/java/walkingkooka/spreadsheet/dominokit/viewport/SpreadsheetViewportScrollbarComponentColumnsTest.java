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

import java.util.Optional;

public final class SpreadsheetViewportScrollbarComponentColumnsTest extends SpreadsheetViewportScrollbarComponentTestCase<SpreadsheetColumnReference> {

    @Test
    public void testTreePrintLinksMissingHistoryToken() {
        this.treePrintAndCheck(
            SpreadsheetViewportScrollbarComponent.columns(
                new TestSpreadsheetViewportScrollbarComponentContext(CELL_SELECT)
            ).setValue(
                Optional.of(
                    SpreadsheetSelection.parseColumn("M")
                )
            ),
            "SpreadsheetViewportScrollbarComponentColumns\n" +
                "  FlexLayoutComponent\n" +
                "    ROW\n" +
                "      id=viewport-horizontal-scrollbar-Layout\n" +
                "        mdi-arrow-left \"Left\" DISABLED id=viewport-horizontal-scrollbar-left-Link\n" +
                "        SliderComponent\n" +
                "          Horizontal\n" +
                "            [12.0] min=1.0 max=16383.0 id=viewport-horizontal-scrollbar-value-Slider\n" +
                "        \"Right\" DISABLED mdi-arrow-right id=viewport-horizontal-scrollbar-right-Link\n"
        );
    }

    @Test
    public void testTreePrintAfterOnHistoryToken() {
        final SpreadsheetViewportScrollbarComponent<SpreadsheetColumnReference> component = SpreadsheetViewportScrollbarComponent.columns(
            new TestSpreadsheetViewportScrollbarComponentContext(CELL_SELECT)
        );
        component.onHistoryTokenChange(
            CELL_SELECT,
            new TestAppContext(
                SpreadsheetSelection.parseCell("D4"),
                CELL_SELECT
            )
        );

        this.treePrintAndCheck(
            component,
            "SpreadsheetViewportScrollbarComponentColumns\n" +
                "  FlexLayoutComponent\n" +
                "    ROW\n" +
                "      id=viewport-horizontal-scrollbar-Layout\n" +
                "        mdi-arrow-left \"Left\" [#/1/SpreadsheetName222/cell/A1/navigate/D4/left%201000px] id=viewport-horizontal-scrollbar-left-Link\n" +
                "        SliderComponent\n" +
                "          Horizontal\n" +
                "            [] min=1.0 max=16383.0 id=viewport-horizontal-scrollbar-value-Slider\n" +
                "        \"Right\" [#/1/SpreadsheetName222/cell/A1/navigate/D4/right%201000px] mdi-arrow-right id=viewport-horizontal-scrollbar-right-Link\n"
        );
    }

    @Override
    public SpreadsheetViewportScrollbarComponent<SpreadsheetColumnReference> createComponent() {
        return SpreadsheetViewportScrollbarComponent.columns(
            new TestSpreadsheetViewportScrollbarComponentContext(
                CELL_SELECT
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetViewportScrollbarComponent<SpreadsheetColumnReference>> type() {
        return Cast.to(SpreadsheetViewportScrollbarComponentColumns.class);
    }
}
