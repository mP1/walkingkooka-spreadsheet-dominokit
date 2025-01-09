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

public final class SpreadsheetCellFormatterSelectHistoryTokenTest extends SpreadsheetCellFormatterHistoryTokenTestCase<SpreadsheetCellFormatterSelectHistoryToken> {

    // urlFragment......................................................................................................

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1/formatter");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
            RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
            "/123/SpreadsheetName456/cell/B2:C3/top-left/formatter"
        );
    }

    @Test
    public void testUrlFragmentCellRangeStar() {
        this.urlFragmentAndCheck(
            SpreadsheetSelection.ALL_CELLS.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
            "/123/SpreadsheetName456/cell/*/top-left/formatter"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
            LABEL,
            "/123/SpreadsheetName456/cell/Label123/formatter"
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
            HistoryToken.cellSelect(
                ID,
                NAME,
                SELECTION
            )
        );
    }

    // saveValue........................................................................................................

    @Test
    public void testSetSaveValue() {
        final SpreadsheetFormatPattern pattern = SpreadsheetPattern.parseTimeFormatPattern("hh:mm");
        final SpreadsheetFormatterSelector selector = pattern.spreadsheetFormatterSelector();

        this.setSaveValueAndCheck(
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
    public void testSetSaveValueWithEmpty() {
        this.setSaveValueAndCheck(
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
    SpreadsheetCellFormatterSelectHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                  final SpreadsheetName name,
                                                                  final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetCellFormatterSelectHistoryToken.with(
            id,
            name,
            selection
        );
    }

    @Override
    public Class<SpreadsheetCellFormatterSelectHistoryToken> type() {
        return SpreadsheetCellFormatterSelectHistoryToken.class;
    }
}
