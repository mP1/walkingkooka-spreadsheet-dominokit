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
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public final class SpreadsheetColumnSelectHistoryTokenTest extends SpreadsheetColumnHistoryTokenTestCase<SpreadsheetColumnSelectHistoryToken> {

    @Test
    public void testUrlFragmentColumn() {
        this.urlFragmentAndCheck(
                COLUMN,
                "/123/SpreadsheetName456/column/A");
    }

    @Test
    public void testUrlFragmentColumnRange() {
        this.urlFragmentAndCheck(
                COLUMN_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT),
                "/123/SpreadsheetName456/column/B:C/right"
        );
    }

    // freezeOrEmpty....................................................................................................

    @Test
    public void testFreezeOrEmptyColumnInvalid() {
        this.freezeOrEmptyAndCheck(
                SpreadsheetSelection.parseColumn("B")
                        .setDefaultAnchor()
        );
    }

    @Test
    public void testFreezeOrEmptyColumnRangeInvalid() {
        this.freezeOrEmptyAndCheck(
                SpreadsheetSelection.parseColumnRange("C:D")
                        .setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT)
        );
    }

    // setMenu1(Selection)..................................................................................................

    @Test
    public void testSetMenuWithColumn() {
        this.setMenuWithColumnAndCheck();
    }

    // unfreezeOrEmpty....................................................................................................

    @Test
    public void testUnfreezeOrEmptyColumnInvalid() {
        this.unfreezeOrEmptyAndCheck(
                SpreadsheetSelection.parseColumn("B")
                        .setDefaultAnchor()
        );
    }

    @Test
    public void testUnfreezeOrEmptyColumnRangeInvalid() {
        this.unfreezeOrEmptyAndCheck(
                SpreadsheetSelection.parseColumnRange("C:D")
                        .setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT)
        );
    }

    @Override
    SpreadsheetColumnSelectHistoryToken createHistoryToken(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnSelectHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    @Override
    public Class<SpreadsheetColumnSelectHistoryToken> type() {
        return SpreadsheetColumnSelectHistoryToken.class;
    }
}
