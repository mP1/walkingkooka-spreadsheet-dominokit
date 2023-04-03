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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyValueException;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellFreezeHistoryTokenTest extends SpreadsheetCellHistoryTokenTestCase<SpreadsheetCellFreezeHistoryToken> {

    @Test
    public void testUrlFragmentColumnRangeInvalidFails() {
        assertThrows(
                SpreadsheetMetadataPropertyValueException.class,
                () -> this.createHistoryToken(
                        SpreadsheetSelection.parseCellRange("B1:C3")
                                .setDefaultAnchor()
                )
        );
    }

    @Test
    public void testUrlFragmentRowRangeInvalidFails() {
        assertThrows(
                SpreadsheetMetadataPropertyValueException.class,
                () -> this.createHistoryToken(
                        SpreadsheetSelection.parseCellRange("A2:C3")
                                .setDefaultAnchor()
                )
        );
    }

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck(
                SpreadsheetSelection.parseCell("A1").setDefaultAnchor(),
                "/123/SpreadsheetName456/cell/A1/freeze"
        );
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                SpreadsheetSelection.parseCellRange("A1:B2").setAnchor(SpreadsheetViewportSelectionAnchor.TOP_LEFT),
                "/123/SpreadsheetName456/cell/A1:B2/top-left/freeze"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/123/SpreadsheetName456/cell/Label123/freeze"
        );
    }

    // menu(Selection)..................................................................................................

    @Test
    public void testMenuWithCell() {
        this.menuWithCellAndCheck();
    }

    @Override
    SpreadsheetCellFreezeHistoryToken createHistoryToken(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellFreezeHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    @Override
    public Class<SpreadsheetCellFreezeHistoryToken> type() {
        return SpreadsheetCellFreezeHistoryToken.class;
    }
}
