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
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public final class SpreadsheetColumnDeleteHistoryTokenTest extends SpreadsheetColumnHistoryTokenTestCase<SpreadsheetColumnDeleteHistoryToken> {

    @Test
    public void testUrlFragmentColumn() {
        this.urlFragmentAndCheck(
                COLUMN,
                "/123/SpreadsheetName456/column/A/delete");
    }

    @Test
    public void testUrlFragmentColumnRange() {
        this.urlFragmentAndCheck(
                COLUMN_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT),
                "/123/SpreadsheetName456/column/B:C/right/delete"
        );
    }

    // setMenu1(Selection)..................................................................................................

    @Test
    public void testSetMenuWithColumn() {
        this.setMenuWithColumnAndCheck();
    }

    @Override
    SpreadsheetColumnDeleteHistoryToken createHistoryToken(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final SpreadsheetViewport viewport) {
        return SpreadsheetColumnDeleteHistoryToken.with(
                id,
                name,
                viewport
        );
    }

    @Override
    public Class<SpreadsheetColumnDeleteHistoryToken> type() {
        return SpreadsheetColumnDeleteHistoryToken.class;
    }
}
