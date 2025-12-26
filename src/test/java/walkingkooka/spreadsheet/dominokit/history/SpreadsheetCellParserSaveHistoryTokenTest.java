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

package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportAnchor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellParserSaveHistoryTokenTest extends SpreadsheetCellParserHistoryTokenTestCase<SpreadsheetCellParserSaveHistoryToken> {

    private final static SpreadsheetParsePattern PATTERN = SpreadsheetPattern.parseDateParsePattern("yyyy-mm-dd");

    @Test
    public void testWithNullSpreadsheetParserSelectorFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetCellParserSaveHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                null
            )
        );
    }

    // urlFragment......................................................................................................

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1/parser/save/date yyyy-mm-dd");
    }

    @Test
    public void testUrlFragmentCellEmptySave() {
        this.urlFragmentAndCheck(
            SpreadsheetCellParserSaveHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                Optional.empty()
            ),
            "/123/SpreadsheetName456/cell/A1/parser/save/"
        );
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
            CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
            "/123/SpreadsheetName456/cell/B2:C3/top-left/parser/save/date yyyy-mm-dd"
        );
    }

    @Test
    public void testUrlFragmentCellRangeStar() {
        this.urlFragmentAndCheck(
            SpreadsheetSelection.ALL_CELLS.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
            "/123/SpreadsheetName456/cell/*/top-left/parser/save/date yyyy-mm-dd"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
            LABEL,
            "/123/SpreadsheetName456/cell/Label123/parser/save/date yyyy-mm-dd"
        );
    }

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.cellParserSelect(
                ID,
                NAME,
                SELECTION
            )
        );
    }

    @Override
    SpreadsheetCellParserSaveHistoryToken createHistoryToken(final SpreadsheetId id,
                                                             final SpreadsheetName name,
                                                             final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetCellParserSaveHistoryToken.with(
            id,
            name,
            selection,
            Optional.of(
                PATTERN.spreadsheetParserSelector()
            )
        );
    }

    @Override
    public Class<SpreadsheetCellParserSaveHistoryToken> type() {
        return SpreadsheetCellParserSaveHistoryToken.class;
    }
}
