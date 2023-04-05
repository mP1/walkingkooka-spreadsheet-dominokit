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
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

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
                        PATTERN.kind(),
                        null
                )
        );
    }

    @Test
    public void testWithIncompatiblePatternKindAndPatternFails() {
        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetCellPatternSaveHistoryToken.with(
                        ID,
                        NAME,
                        CELL.setDefaultAnchor(),
                        SpreadsheetPatternKind.TEXT_FORMAT_PATTERN,
                        Optional.of(
                                PATTERN
                        )
                )
        );

        this.checkEquals(
                "Pattern \"yyyy-mm-dd\" is not a DATE_FORMAT_PATTERN.",
                thrown.getMessage()
        );
    }

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1/pattern/date-format/save/yyyy-mm-dd");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/B2:C3/top-left/pattern/date-format/save/yyyy-mm-dd"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/123/SpreadsheetName456/cell/Label123/pattern/date-format/save/yyyy-mm-dd"
        );
    }

    @Override
    SpreadsheetCellPatternSaveHistoryToken createHistoryToken(final SpreadsheetId id,
                                                              final SpreadsheetName name,
                                                              final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellPatternSaveHistoryToken.with(
                id,
                name,
                viewportSelection,
                PATTERN.kind(),
                Optional.of(PATTERN)
        );
    }

    @Override
    public Class<SpreadsheetCellPatternSaveHistoryToken> type() {
        return SpreadsheetCellPatternSaveHistoryToken.class;
    }
}
