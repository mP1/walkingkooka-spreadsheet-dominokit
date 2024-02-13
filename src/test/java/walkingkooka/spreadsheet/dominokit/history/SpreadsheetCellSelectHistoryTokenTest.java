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
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class SpreadsheetCellSelectHistoryTokenTest extends SpreadsheetCellHistoryTokenTestCase<SpreadsheetCellSelectHistoryToken> {

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/B2:C3/top-left"
        );
    }

    @Test
    public void testUrlFragmentCellRangeStar() {
        this.urlFragmentAndCheck(
                SpreadsheetSelection.ALL_CELLS.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/*/top-left"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/123/SpreadsheetName456/cell/Label123"
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseCell() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1",
                SpreadsheetCellSelectHistoryToken.with(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseCellRange() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/B2:C3",
                SpreadsheetCellSelectHistoryToken.with(
                        ID,
                        NAME,
                        RANGE.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseCellRangeAndAnchor() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/B2:C3",
                SpreadsheetCellSelectHistoryToken.with(
                        ID,
                        NAME,
                        RANGE.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseCellRangeStar() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/*",
                SpreadsheetCellSelectHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.ALL_CELLS.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testParseCellRangeStarAndAnchor() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/*/bottom-right",
                SpreadsheetCellSelectHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.ALL_CELLS.setAnchor(SpreadsheetViewportAnchor.BOTTOM_RIGHT)
                )
        );
    }

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck();
    }

    // freezeOrEmpty....................................................................................................

    @Test
    public void testFreezeOrEmptyCellInvalid() {
        this.freezeOrEmptyAndCheck(
                SpreadsheetSelection.parseCell("B2")
                        .setDefaultAnchor()
        );
    }

    @Test
    public void testFreezeOrEmptyCellRangeInvalid() {
        this.freezeOrEmptyAndCheck(
                SpreadsheetSelection.parseCellRange("C3:D4")
                        .setAnchor(SpreadsheetViewportAnchor.BOTTOM_RIGHT)
        );
    }

    // setMenu1(Selection)..................................................................................................

    @Test
    public void testSetMenuWithCell() {
        this.setMenuWithCellAndCheck();
    }

    // patternKind......................................................................................................

    @Test
    public void testPatternKind() {
        this.patternKindAndCheck(
                this.createHistoryToken()
        );
    }

    // setPatternKind...................................................................................................

    @Test
    public void testSetPatternKindWithDateFormatPattern() {
        final SpreadsheetPatternKind kind = SpreadsheetPatternKind.DATE_FORMAT_PATTERN;

        this.setPatternKindAndCheck(
                this.createHistoryToken(),
                kind,
                HistoryToken.cellPattern(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        kind
                )
        );
    }

    @Test
    public void testSetPatternKindWithDateParsePattern() {
        final SpreadsheetPatternKind kind = SpreadsheetPatternKind.DATE_PARSE_PATTERN;

        this.setPatternKindAndCheck(
                this.createHistoryToken(),
                kind,
                HistoryToken.cellPattern(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        kind
                )
        );
    }

    @Test
    public void testSetPatternKindWithEmpty() {
        this.setPatternKindAndCheck();
    }

    // setSave.........................................................................................................

    @Test
    public void testSetSave() {
        final AnchoredSpreadsheetSelection selection = CELL.setDefaultAnchor();
        final String formulaText = "=1";
        final HistoryToken historyToken = HistoryToken.cell(ID, NAME, selection);

        assertSame(
                historyToken.setSave(formulaText),
                historyToken
        );
    }

    // unfreezeOrEmpty..................................................................................................

    @Test
    public void testUnfreezeOrEmptyCellInvalid() {
        this.unfreezeOrEmptyAndCheck(
                SpreadsheetSelection.parseCell("B2")
                        .setDefaultAnchor()
        );
    }

    @Test
    public void testUnfreezeOrEmptyCellRangeInvalid() {
        this.unfreezeOrEmptyAndCheck(
                SpreadsheetSelection.parseCellRange("C3:D4")
                        .setAnchor(SpreadsheetViewportAnchor.BOTTOM_RIGHT)
        );
    }

    @Override
    SpreadsheetCellSelectHistoryToken createHistoryToken(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetCellSelectHistoryToken.with(
                id,
                name,
                selection
        );
    }

    @Override
    public Class<SpreadsheetCellSelectHistoryToken> type() {
        return SpreadsheetCellSelectHistoryToken.class;
    }
}
