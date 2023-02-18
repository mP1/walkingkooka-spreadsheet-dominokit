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
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public final class SpreadsheetCellPatternSaveHistoryHashTokenTest extends SpreadsheetCellPatternHistoryHashTokenTestCase<SpreadsheetCellPatternSaveHistoryHashToken> {

    private final static SpreadsheetPattern PATTERN = SpreadsheetPattern.parseDateFormatPattern("yyyy-mm-dd");

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/cell/A1/pattern/date-format/save/yyyy-mm-dd");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.TOP_LEFT),
                "/cell/B2:C3/top-left/pattern/date-format/save/yyyy-mm-dd"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/cell/Label123/pattern/date-format/save/yyyy-mm-dd"
        );
    }

    @Override
    SpreadsheetCellPatternSaveHistoryHashToken createSpreadsheetHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellPatternSaveHistoryHashToken.with(
                viewportSelection,
                PATTERN
        );
    }

    @Override
    public Class<SpreadsheetCellPatternSaveHistoryHashToken> type() {
        return SpreadsheetCellPatternSaveHistoryHashToken.class;
    }
}
