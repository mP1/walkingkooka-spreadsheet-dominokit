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
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public final class SpreadsheetViewportScrollbarComponentRowsTest extends SpreadsheetViewportScrollbarComponentTestCase<SpreadsheetRowReference> {

    @Test
    public void testTreePrintLinksMissingHistoryToken() {
        this.treePrintAndCheck(
            SpreadsheetViewportScrollbarComponent.rows(
                new TestSpreadsheetViewportScrollbarComponentContext(CELL_SELECT)
            ).setValue(
                Optional.of(
                    SpreadsheetSelection.parseRow("99")
                )
            ),
            "SpreadsheetViewportScrollbarComponentRows\n" +
                "  FlexLayoutComponent\n" +
                "    COLUMN\n" +
                "      id=viewport-vertical-scrollbar-Layout\n" +
                "        mdi-arrow-up \"Up\" DISABLED id=viewport-vertical-scrollbar-up-Link\n" +
                "        SliderComponent\n" +
                "          Vertical\n" +
                "            [98.0] min=1.0 max=1048575.0 id=viewport-vertical-scrollbar-value-Slider\n" +
                "        \"Down\" DISABLED mdi-arrow-down id=viewport-vertical-scrollbar-down-Link\n"
        );
    }

    @Test
    public void testTreePrintAfterOnHistoryToken() {
        final SpreadsheetViewportScrollbarComponent<SpreadsheetRowReference> component = SpreadsheetViewportScrollbarComponent.rows(
            new TestSpreadsheetViewportScrollbarComponentContext(CELL_SELECT)
        );
        component.onHistoryTokenChange(
            CELL_SELECT,
            new TestAppContext(
                SpreadsheetSelection.parseCell("C3"), // home
                CELL_SELECT
            )
        );

        this.treePrintAndCheck(
            component,
            "SpreadsheetViewportScrollbarComponentRows\n" +
                "  FlexLayoutComponent\n" +
                "    COLUMN\n" +
                "      id=viewport-vertical-scrollbar-Layout\n" +
                "        mdi-arrow-up \"Up\" [#/1/SpreadsheetName222/cell/A1/navigate/C3/up%20500px] id=viewport-vertical-scrollbar-up-Link\n" +
                "        SliderComponent\n" +
                "          Vertical\n" +
                "            [] min=1.0 max=1048575.0 id=viewport-vertical-scrollbar-value-Slider\n" +
                "        \"Down\" [#/1/SpreadsheetName222/cell/A1/navigate/C3/down%20500px] mdi-arrow-down id=viewport-vertical-scrollbar-down-Link\n"
        );
    }

    @Override
    public SpreadsheetViewportScrollbarComponent<SpreadsheetRowReference> createComponent() {
        return SpreadsheetViewportScrollbarComponent.rows(
            new TestSpreadsheetViewportScrollbarComponentContext(
                CELL_SELECT
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetViewportScrollbarComponent<SpreadsheetRowReference>> type() {
        return Cast.to(SpreadsheetViewportScrollbarComponentRows.class);
    }
}
