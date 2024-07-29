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
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellParserSelectHistoryTokenTest extends SpreadsheetCellParserHistoryTokenTestCase<SpreadsheetCellParserSelectHistoryToken> {

    private final static SpreadsheetParsePattern PATTERN = SpreadsheetPattern.parseDateParsePattern("yyyy-mm-dd");

    @Test
    public void testWithNullSpreadsheetParseKindFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetCellParserSelectHistoryToken.with(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        null
                )
        );
    }

    @Test
    public void testWithInvalidSpreadsheetParseKindFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetCellParserSelectHistoryToken.with(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetPatternKind.DATE_FORMAT_PATTERN
                )
        );
    }

    // urlFragment......................................................................................................

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1/parser/date");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/B2:C3/top-left/parser/date"
        );
    }

    @Test
    public void testUrlFragmentCellRangeStar() {
        this.urlFragmentAndCheck(
                SpreadsheetSelection.ALL_CELLS.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/*/top-left/parser/date"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/123/SpreadsheetName456/cell/Label123/parser/date"
        );
    }

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck();
    }

    // close............................................................................................................

    @Test
    public void testClose() {
        this.closeAndCheck(
                HistoryToken.cell(
                        ID,
                        NAME,
                        SELECTION
                )
        );
    }

    // setSave..........................................................................................................

    @Test
    public void testSetSave() {
        final SpreadsheetParsePattern pattern = SpreadsheetPattern.parseTimeParsePattern("hh:mm");
        final SpreadsheetParserSelector selector = pattern.spreadsheetParserSelector();

        this.setSaveAndCheck(
                this.createHistoryToken(),
                selector.toString(),
                HistoryToken.cellParserSave(
                        ID,
                        NAME,
                        SELECTION,
                        PATTERN.patternKind(),
                        Optional.of(selector)
                )
        );
    }

    @Test
    public void testSetSaveEmpty() {
        this.setSaveAndCheck(
                this.createHistoryToken(),
                "",
                HistoryToken.cellParserSave(
                        ID,
                        NAME,
                        SELECTION,
                        PATTERN.patternKind(),
                        Optional.empty()
                )
        );
    }

    @Override
    SpreadsheetCellParserSelectHistoryToken createHistoryToken(final SpreadsheetId id,
                                                               final SpreadsheetName name,
                                                               final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetCellParserSelectHistoryToken.with(
                id,
                name,
                selection,
                PATTERN.patternKind()
        );
    }

    @Override
    public Class<SpreadsheetCellParserSelectHistoryToken> type() {
        return SpreadsheetCellParserSelectHistoryToken.class;
    }
}
