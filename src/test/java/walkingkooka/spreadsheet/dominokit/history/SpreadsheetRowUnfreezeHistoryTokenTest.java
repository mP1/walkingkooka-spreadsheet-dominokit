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
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetRowUnfreezeHistoryTokenTest extends SpreadsheetRowHistoryTokenTestCase<SpreadsheetRowUnfreezeHistoryToken> {

    @Test
    public void testUrlFragmentRow() {
        this.urlFragmentAndCheck(
                ROW,
                "/123/SpreadsheetName456/row/1/unfreeze"
        );
    }

    @Test
    public void testUrlFragmentRowRange() {
        this.urlFragmentAndCheck(
                SpreadsheetSelection.parseRowRange("1:2").setAnchor(SpreadsheetViewportAnchor.BOTTOM),
                "/123/SpreadsheetName456/row/1:2/bottom/unfreeze"
        );
    }

    @Test
    public void testUrlFragmentRowRangeInvalidFails() {
        assertThrows(
                SpreadsheetMetadataPropertyValueException.class,
                () -> this.createHistoryToken(
                        SpreadsheetSelection.parseRowRange("2:3").setAnchor(SpreadsheetViewportAnchor.BOTTOM)
                )
        );
    }

    // setMenu1(Selection)..................................................................................................

    @Test
    public void testSetMenuWithRow() {
        this.setMenuWithRowAndCheck();
    }

    @Override
    SpreadsheetRowUnfreezeHistoryToken createHistoryToken(final SpreadsheetId id,
                                                          final SpreadsheetName name,
                                                          final SpreadsheetViewport viewport) {
        return SpreadsheetRowUnfreezeHistoryToken.with(
                id,
                name,
                viewport
        );
    }

    @Override
    public Class<SpreadsheetRowUnfreezeHistoryToken> type() {
        return SpreadsheetRowUnfreezeHistoryToken.class;
    }
}
