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
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellPatternSaveHistoryTokenTest extends SpreadsheetCellPatternHistoryTokenTestCase<SpreadsheetCellPatternSaveHistoryToken> {

    private final static SpreadsheetPattern PATTERN = SpreadsheetPattern.parseDateFormatPattern("yyyy-mm-dd");

    @Test
    public void testWithNullPatternFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetCellPatternSaveHistoryToken.with(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        PATTERN.patternKind(),
                        null
                )
        );
    }

    @Test
    public void testWithIncompatiblePatternKindAndParsePatternFails() {
        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetCellPatternSaveHistoryToken.with(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetPatternKind.NUMBER_PARSE_PATTERN,
                        Optional.of(
                                SpreadsheetPattern.parseNumberFormatPattern("$0.00")
                        )
                )
        );

        this.checkEquals(
                "Pattern \"$0.00\" is not a NUMBER_FORMAT_PATTERN.",
                thrown.getMessage()
        );
    }

    @Test
    public void testWithIncompatiblePatternKindAndFormatPattern() {
        SpreadsheetCellPatternSaveHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                SpreadsheetPatternKind.TEXT_FORMAT_PATTERN,
                Optional.of(
                        PATTERN
                )
        );
    }

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1/formatter/date/save/date-format-pattern yyyy-mm-dd");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/B2:C3/top-left/formatter/date/save/date-format-pattern yyyy-mm-dd"
        );
    }

    @Test
    public void testUrlFragmentCellRangeStar() {
        this.urlFragmentAndCheck(
                SpreadsheetSelection.ALL_CELLS.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/*/top-left/formatter/date/save/date-format-pattern yyyy-mm-dd"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/123/SpreadsheetName456/cell/Label123/formatter/date/save/date-format-pattern yyyy-mm-dd"
        );
    }

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
                this.createHistoryToken(),
                HistoryToken.cellPattern(
                        ID,
                        NAME,
                        SELECTION,
                        PATTERN.patternKind()
                )
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
                        formatPattern.patternKind(),
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
                        parsePattern.patternKind(),
                        Optional.of(parsePattern)
                ),
                HistoryToken.cellParsePattern(
                        ID,
                        NAME,
                        SELECTION
                )
        );
    }

    // setDelete........................................................................................................

    @Test
    public void testSetDelete() {
        final HistoryToken token = this.createHistoryToken();

        this.setDeleteAndCheck(
                token,
                HistoryToken.cellPatternSave(
                        ID,
                        NAME,
                        SELECTION,
                        token.patternKind()
                                .get(),
                        Optional.empty()
                )
        );
    }

    // setSave..........................................................................................................

    @Test
    public void testSetSave() {
        final HistoryToken token = this.createHistoryToken();
        final SpreadsheetPatternKind kind = token.patternKind()
                .get();
        final String pattern = "dd/mm/yyyy";

        this.setSaveAndCheck(
                token,
                pattern,
                HistoryToken.cellPatternSave(
                        ID,
                        NAME,
                        SELECTION,
                        kind,
                        Optional.of(
                                kind.parse(pattern)
                        )
                )
        );
    }

    @Test
    public void testSetSaveEmpty() {
        final HistoryToken token = this.createHistoryToken();
        final SpreadsheetPatternKind kind = token.patternKind()
                .get();
        final String pattern = "";

        this.setSaveAndCheck(
                token,
                pattern,
                HistoryToken.cellPatternSave(
                        ID,
                        NAME,
                        SELECTION,
                        kind,
                        Optional.empty()
                )
        );
    }

    @Override
    SpreadsheetCellPatternSaveHistoryToken createHistoryToken(final SpreadsheetId id,
                                                              final SpreadsheetName name,
                                                              final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetCellPatternSaveHistoryToken.with(
                id,
                name,
                selection,
                PATTERN.patternKind(),
                Optional.of(PATTERN)
        );
    }

    @Override
    public Class<SpreadsheetCellPatternSaveHistoryToken> type() {
        return SpreadsheetCellPatternSaveHistoryToken.class;
    }
}
