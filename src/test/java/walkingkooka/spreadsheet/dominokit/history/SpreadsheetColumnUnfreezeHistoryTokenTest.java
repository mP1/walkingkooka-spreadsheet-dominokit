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

public final class SpreadsheetColumnUnfreezeHistoryTokenTest extends SpreadsheetColumnHistoryTokenTestCase<SpreadsheetColumnUnfreezeHistoryToken> {

    @Test
    public void testUrlFragmentColumn() {
        this.urlFragmentAndCheck(
                COLUMN,
                "/123/SpreadsheetName456/column/A/unfreeze");
    }

    @Test
    public void testUrlFragmentColumnRange() {
        this.urlFragmentAndCheck(
                SpreadsheetSelection.parseColumnRange("A:B").setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT),
                "/123/SpreadsheetName456/column/A:B/right/unfreeze"
        );
    }

    @Test
    public void testUrlFragmentColumnRangeInvalidFails() {
        assertThrows(
                SpreadsheetMetadataPropertyValueException.class,
                () -> this.createHistoryToken(
                        SpreadsheetSelection.parseColumnRange("B:C").setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT)
                )
        );
    }

    // setMenu1(Selection)..................................................................................................

    @Test
    public void testSetMenuWithColumn() {
        this.setMenuWithColumnAndCheck();
    }

    @Override
    SpreadsheetColumnUnfreezeHistoryToken createHistoryToken(final SpreadsheetId id,
                                                             final SpreadsheetName name,
                                                             final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnUnfreezeHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    @Override
    public Class<SpreadsheetColumnUnfreezeHistoryToken> type() {
        return SpreadsheetColumnUnfreezeHistoryToken.class;
    }
}
