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
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellFormatterSaveHistoryTokenTest extends SpreadsheetCellFormatterHistoryTokenTestCase<SpreadsheetCellFormatterSaveHistoryToken> {

    private final static SpreadsheetFormatPattern PATTERN = SpreadsheetPattern.parseDateFormatPattern("yyyy-mm-dd");

    @Test
    public void testWithNullSpreadsheetParseKindFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetCellFormatterSaveHistoryToken.with(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        null,
                        Optional.of(
                                PATTERN.spreadsheetFormatterSelector()
                        )
                )
        );
    }

    @Test
    public void testWithInvalidSpreadsheetParseKindFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetCellFormatterSaveHistoryToken.with(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetPatternKind.DATE_PARSE_PATTERN,
                        Optional.of(
                                PATTERN.spreadsheetFormatterSelector()
                        )
                )
        );
    }

    @Test
    public void testWithNullSpreadsheetFormatterSelectorFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetCellFormatterSaveHistoryToken.with(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        PATTERN.patternKind(),
                        null
                )
        );
    }

    // urlFragment......................................................................................................

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1/formatter/date/save/date-format-pattern yyyy-mm-dd");
    }

    @Test
    public void testUrlFragmentCellEmptySave() {
        this.urlFragmentAndCheck(
                SpreadsheetCellFormatterSaveHistoryToken.with(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        PATTERN.patternKind(),
                        Optional.empty()
                ),
                "/123/SpreadsheetName456/cell/A1/formatter/date/save/"
        );
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
                HistoryToken.cellFormatterSelect(
                        ID,
                        NAME,
                        SELECTION,
                        PATTERN.patternKind()
                )
        );
    }

    // close............................................................................................................

    @Test
    public void testClose() {
        final SpreadsheetFormatPattern formatPattern = SpreadsheetPattern.parseTextFormatPattern("@");
        final SpreadsheetPatternKind spreadsheetPatternKind = formatPattern.patternKind();

        this.closeAndCheck(
                HistoryToken.cellFormatterSave(
                        ID,
                        NAME,
                        SELECTION,
                        spreadsheetPatternKind,
                        Optional.of(formatPattern.spreadsheetFormatterSelector())
                ),
                HistoryToken.cellFormatterSelect(
                        ID,
                        NAME,
                        SELECTION,
                        spreadsheetPatternKind
                )
        );
    }

    // setSave..........................................................................................................

    @Test
    public void testSetSave() {
        final SpreadsheetFormatPattern pattern = SpreadsheetPattern.parseTimeFormatPattern("hh:mm");
        final SpreadsheetFormatterSelector selector = pattern.spreadsheetFormatterSelector();

        this.setSaveAndCheck(
                this.createHistoryToken(),
                selector.toString(),
                HistoryToken.cellFormatterSave(
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
                HistoryToken.cellFormatterSave(
                        ID,
                        NAME,
                        SELECTION,
                        PATTERN.patternKind(),
                        Optional.empty()
                )
        );
    }

    @Override
    SpreadsheetCellFormatterSaveHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                final SpreadsheetName name,
                                                                final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetCellFormatterSaveHistoryToken.with(
                id,
                name,
                selection,
                PATTERN.patternKind(),
                Optional.of(
                        PATTERN.toFormat()
                                .spreadsheetFormatterSelector()
                )
        );
    }

    @Override
    public Class<SpreadsheetCellFormatterSaveHistoryToken> type() {
        return SpreadsheetCellFormatterSaveHistoryToken.class;
    }
}
