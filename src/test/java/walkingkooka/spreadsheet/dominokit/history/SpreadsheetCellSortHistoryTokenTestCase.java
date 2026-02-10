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
import walkingkooka.spreadsheet.compare.provider.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Optional;

public abstract class SpreadsheetCellSortHistoryTokenTestCase<T extends SpreadsheetCellSortHistoryToken> extends SpreadsheetCellHistoryTokenTestCase<T> {

    final static String COMPARATOR_NAMES_LIST_STRING = "A=day-of-month,month-of-year,year-reversed";

    final static SpreadsheetColumnOrRowSpreadsheetComparatorNamesList COMPARATOR_NAMES_LIST = SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse(COMPARATOR_NAMES_LIST_STRING);

    final static AnchoredSpreadsheetSelection ANCHORED_CELL = SpreadsheetSelection.parseCellRange("A1:C3")
        .setDefaultAnchor();

    final static String COMPARATOR_NAMES_LIST_STRING2 = "A=day-of-month;B=month-of-year;C=year-reversed";

    final static SpreadsheetColumnOrRowSpreadsheetComparatorNamesList COMPARATOR_NAMES_LIST2 = SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse(COMPARATOR_NAMES_LIST_STRING2);

    SpreadsheetCellSortHistoryTokenTestCase() {
        super();
    }

    @Test
    public final void testClose() {
        this.closeAndCheck(
            this.createHistoryToken(),
            HistoryToken.cellSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    // navigation.......................................................................................................

    @Test
    public final void testNavigation() {
        this.navigationAndCheck(
            this.createHistoryToken()
        );
    }

    // setSaveStringValue.....................................................................................................

    @Test
    public final void testSetSaveValueWithInvalidOptionalValueFails() {
        this.setSaveValueFails(
            Optional.of(this)
        );
    }

    @Test
    public final void testSetSaveValueWithEmpty() {
        this.setSaveValueFails(
            Optional.empty()
        );
    }

    @Test
    public final void testSetSaveValueWithNonEmpty() {
        final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList value = SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse("A=different");

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(value),
            HistoryToken.cellSortSave(
                ID,
                NAME,
                SELECTION,
                value
            )
        );
    }
}
