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
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellFormatterSaveHistoryTokenTest extends SpreadsheetCellFormatterHistoryTokenTestCase<SpreadsheetCellFormatterSaveHistoryToken> {

    private final static SpreadsheetFormatPattern PATTERN = SpreadsheetPattern.parseDateFormatPattern("yyyy-mm-dd");

    @Test
    public void testWithNullSpreadsheetFormatterSelectorFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetCellFormatterSaveHistoryToken.with(
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
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1/formatter/save/date-format-pattern yyyy-mm-dd");
    }

    @Test
    public void testUrlFragmentCellEmptySave() {
        this.urlFragmentAndCheck(
                SpreadsheetCellFormatterSaveHistoryToken.with(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        Optional.empty()
                ),
                "/123/SpreadsheetName456/cell/A1/formatter/save/"
        );
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/B2:C3/top-left/formatter/save/date-format-pattern yyyy-mm-dd"
        );
    }

    @Test
    public void testUrlFragmentCellRangeStar() {
        this.urlFragmentAndCheck(
                SpreadsheetSelection.ALL_CELLS.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/*/top-left/formatter/save/date-format-pattern yyyy-mm-dd"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/123/SpreadsheetName456/cell/Label123/formatter/save/date-format-pattern yyyy-mm-dd"
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
                        SELECTION
                )
        );
    }

    // close............................................................................................................

    @Test
    public void testClose() {
        final SpreadsheetFormatPattern formatPattern = SpreadsheetPattern.parseTextFormatPattern("@");

        this.closeAndCheck(
                HistoryToken.cellFormatterSave(
                        ID,
                        NAME,
                        SELECTION,
                        Optional.of(formatPattern.spreadsheetFormatterSelector())
                ),
                HistoryToken.cellFormatterSelect(
                        ID,
                        NAME,
                        SELECTION
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
                selector.text(),
                HistoryToken.cellFormatterSave(
                        ID,
                        NAME,
                        SELECTION,
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
