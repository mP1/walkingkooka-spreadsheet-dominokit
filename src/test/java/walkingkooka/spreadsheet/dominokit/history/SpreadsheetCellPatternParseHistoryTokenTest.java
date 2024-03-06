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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class SpreadsheetCellPatternParseHistoryTokenTest extends SpreadsheetCellPatternHistoryTokenTestCase<SpreadsheetCellPatternParseHistoryToken> {

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck();
    }

    // setPatternKind......................................................................................................

    @Test
    public void testSetPatternKindSame() {
        final SpreadsheetCellPatternParseHistoryToken historyToken = this.createHistoryToken();
        assertSame(
                historyToken,
                historyToken.setPatternKind(Optional.empty())
        );
    }

    @Test
    public void testSetPatternKindDateFormat() {
        final SpreadsheetPatternKind different = SpreadsheetPatternKind.DATE_FORMAT_PATTERN;

        this.setPatternKindAndCheck(
                different
        );
    }

    @Test
    public void testSetPatternKindTextFormat() {
        this.setPatternKindAndCheck(
                SpreadsheetPatternKind.TEXT_FORMAT_PATTERN
        );
    }

    @Test
    public void testSetPatternKindDifferent() {
        final SpreadsheetPatternKind different = SpreadsheetPatternKind.DATE_PARSE_PATTERN;

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
    public void testSetPatternKindEmpty() {
        this.setPatternKindAndCheck(
                this.createHistoryToken(),
                Optional.empty(),
                SpreadsheetCellPatternParseHistoryToken.with(
                        ID,
                        NAME,
                        SELECTION
                )
        );
    }

    private void setPatternKindAndCheck(
            final SpreadsheetCellPatternParseHistoryToken historyToken,
            final Optional<SpreadsheetPatternKind> kind,
            final HistoryToken expected) {
        this.checkEquals(
                expected,
                historyToken.setPatternKind(kind),
                () -> historyToken + " setPatternKind " + kind
        );
    }

    // urlFragment......................................................................................................

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1/parse-pattern");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/B2:C3/top-left/parse-pattern"
        );
    }

    @Test
    public void testUrlFragmentCellRangeStar() {
        this.urlFragmentAndCheck(
                SpreadsheetSelection.ALL_CELLS.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/*/top-left/parse-pattern"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/123/SpreadsheetName456/cell/Label123/parse-pattern"
        );
    }

    @Override
    SpreadsheetCellPatternParseHistoryToken createHistoryToken(final SpreadsheetId id,
                                                               final SpreadsheetName name,
                                                               final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetCellPatternParseHistoryToken.with(
                id,
                name,
                selection
        );
    }

    @Override
    public Class<SpreadsheetCellPatternParseHistoryToken> type() {
        return SpreadsheetCellPatternParseHistoryToken.class;
    }
}
