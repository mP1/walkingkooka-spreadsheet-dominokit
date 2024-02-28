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

import org.junit.Test;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class SpreadsheetCellPatternSelectHistoryTokenTest extends SpreadsheetCellPatternHistoryTokenTestCase<SpreadsheetCellPatternSelectHistoryToken> {

    private final static SpreadsheetPatternKind KIND = SpreadsheetPatternKind.DATE_FORMAT_PATTERN;

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck();
    }

    // setDelete........................................................................................................

    @Test
    public void testSetDelete() {
        this.setDeleteAndCheck(
                this.createHistoryToken(),
                HistoryToken.cellPatternSave(
                        ID,
                        NAME,
                        SELECTION,
                        KIND,
                        Optional.empty()
                )
        );
    }

    // setPatternKind......................................................................................................

    @Test
    public void testSetPatternKindSame() {
        final SpreadsheetCellPatternSelectHistoryToken historyToken = this.createHistoryToken();
        assertSame(
                historyToken,
                historyToken.setPatternKind(
                        Optional.of(KIND)
                )
        );
    }

    @Test
    public void testSetPatternKindDifferent() {
        final SpreadsheetPatternKind different = SpreadsheetPatternKind.TEXT_FORMAT_PATTERN;

        this.setPatternKindAndCheck(
                this.createHistoryToken(),
                different,
                SpreadsheetCellPatternSelectHistoryToken.with(
                        ID,
                        NAME,
                        SELECTION,
                        different
                )
        );
    }

    @Test
    public void testSetPatternKindDifferent2() {
        final SpreadsheetPatternKind different = SpreadsheetPatternKind.TIME_FORMAT_PATTERN;

        this.setPatternKindAndCheck(
                this.createHistoryToken(),
                different,
                SpreadsheetCellPatternSelectHistoryToken.with(
                        ID,
                        NAME,
                        SELECTION,
                        different
                )
        );
    }

    @Test
    public void testSetPatternKindEmptyWasDateFormat() {
        this.setPatternKindAndCheck(
                SpreadsheetCellPatternSelectHistoryToken.with(
                        ID,
                        NAME,
                        SELECTION,
                        SpreadsheetPatternKind.DATE_FORMAT_PATTERN
                ),
                HistoryToken.cellFormatPattern(
                        ID,
                        NAME,
                        SELECTION
                )
        );
    }

    @Test
    public void testSetPatternKindEmptyWasDateParse() {
        this.setPatternKindAndCheck(
                SpreadsheetCellPatternSelectHistoryToken.with(
                        ID,
                        NAME,
                        SELECTION,
                        SpreadsheetPatternKind.DATE_PARSE_PATTERN
                ),
                HistoryToken.cellParsePattern(
                        ID,
                        NAME,
                        SELECTION
                )
        );
    }

    // urlFragment......................................................................................................

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1/format-pattern/date");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/B2:C3/top-left/format-pattern/date"
        );
    }

    @Test
    public void testUrlFragmentCellRangeStar() {
        this.urlFragmentAndCheck(
                SpreadsheetSelection.ALL_CELLS.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/*/top-left/format-pattern/date"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/123/SpreadsheetName456/cell/Label123/format-pattern/date"
        );
    }

    // close............................................................................................................

    @Test
    public void testCloseFormatPattern() {
        final SpreadsheetPattern formatPattern = SpreadsheetPattern.parseTextFormatPattern("@");

        this.closeAndCheck(
                SpreadsheetCellPatternSaveHistoryToken.with(
                        ID,
                        NAME,
                        SELECTION,
                        formatPattern.kind(),
                        Optional.of(formatPattern)
                ),
                HistoryToken.cellFormatPattern(
                        ID,
                        NAME,
                        SELECTION
                )
        );
    }

    @Test
    public void testCloseParePattern() {
        final SpreadsheetPattern parsePattern = SpreadsheetPattern.parseDateParsePattern("dd/mm/yyyy");

        this.closeAndCheck(
                SpreadsheetCellPatternSaveHistoryToken.with(
                        ID,
                        NAME,
                        SELECTION,
                        parsePattern.kind(),
                        Optional.of(parsePattern)
                ),
                HistoryToken.cellParsePattern(
                        ID,
                        NAME,
                        SELECTION
                )
        );
    }

    // helpers.........................................................................................................

    @Override
    SpreadsheetCellPatternSelectHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                final SpreadsheetName name,
                                                                final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetCellPatternSelectHistoryToken.with(
                id,
                name,
                selection,
                KIND
        );
    }

    @Override
    public Class<SpreadsheetCellPatternSelectHistoryToken> type() {
        return SpreadsheetCellPatternSelectHistoryToken.class;
    }
}
