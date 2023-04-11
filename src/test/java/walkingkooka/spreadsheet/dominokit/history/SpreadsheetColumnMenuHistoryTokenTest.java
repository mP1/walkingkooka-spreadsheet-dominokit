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
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public final class SpreadsheetColumnMenuHistoryTokenTest extends SpreadsheetColumnHistoryTokenTestCase<SpreadsheetColumnMenuHistoryToken> {

    @Test
    public void testUrlFragmentColumn() {
        this.urlFragmentAndCheck(
                COLUMN,
                "/123/SpreadsheetName456/column/A/menu");
    }

    @Test
    public void testUrlFragmentColumnRange() {
        this.urlFragmentAndCheck(
                COLUMN_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT),
                "/123/SpreadsheetName456/column/B:C/right/menu"
        );
    }

    // menu(Selection)..................................................................................................

    @Test
    public void testColumnMenuWithSameColumn() {
        final SpreadsheetColumnReference column = SpreadsheetSelection.parseColumn("B");

        this.menuAndCheck(
                this.createHistoryToken(
                        column.setDefaultAnchor()
                ),
                column,
                HistoryToken.columnMenu(
                        ID,
                        NAME,
                        column.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testColumnMenuWithDifferentColumn() {
        final SpreadsheetColumnReference column = SpreadsheetSelection.parseColumn("B");

        this.menuAndCheck(
                this.createHistoryToken(
                        SpreadsheetSelection.parseColumn("A").setDefaultAnchor()
                ),
                column,
                HistoryToken.columnMenu(
                        ID,
                        NAME,
                        column.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testColumnRangeMenuWithColumnInside() {
        this.menuAndCheck(
                this.createHistoryToken(
                        SpreadsheetSelection.parseColumnRange("A:C").setDefaultAnchor()
                ),
                SpreadsheetSelection.parseColumn("B")
        );
    }

    @Test
    public void testColumnRangeMenuWithColumnOutside() {
        final SpreadsheetColumnReference column = SpreadsheetSelection.parseColumn("Z");

        this.menuAndCheck(
                this.createHistoryToken(
                        SpreadsheetSelection.parseColumnRange("A:C").setDefaultAnchor()
                ),
                column,
                HistoryToken.columnMenu(
                        ID,
                        NAME,
                        column.setDefaultAnchor()
                )
        );
    }

    @Override
    SpreadsheetColumnMenuHistoryToken createHistoryToken(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnMenuHistoryToken.with(
                id,
                name,
                viewportSelection
        );
    }

    @Override
    public Class<SpreadsheetColumnMenuHistoryToken> type() {
        return SpreadsheetColumnMenuHistoryToken.class;
    }
}
