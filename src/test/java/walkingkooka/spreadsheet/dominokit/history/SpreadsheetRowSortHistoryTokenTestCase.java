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
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public abstract class SpreadsheetRowSortHistoryTokenTestCase<T extends SpreadsheetRowSortHistoryToken> extends SpreadsheetRowHistoryTokenTestCase<T> {

    final static String COMPARATOR_NAMES_LIST_STRING = "1=day-of-month UP,month-of-year UP,year DOWN";

    final static SpreadsheetColumnOrRowSpreadsheetComparatorNamesList COMPARATOR_NAMES_LIST = SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse(COMPARATOR_NAMES_LIST_STRING);

    final static AnchoredSpreadsheetSelection ANCHORED_ROW = SpreadsheetSelection.parseRowRange("1:3")
        .setDefaultAnchor();

    final static String COMPARATOR_NAMES_LIST_STRING2 = "1=day-of-month UP;2=month-of-year UP;3=year DOWN";

    final static SpreadsheetColumnOrRowSpreadsheetComparatorNamesList COMPARATOR_NAMES_LIST2 = SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse(COMPARATOR_NAMES_LIST_STRING2);

    SpreadsheetRowSortHistoryTokenTestCase() {
        super();
    }

    @Test
    public final void testClose() {
        this.closeAndCheck(
            this.createHistoryToken(),
            HistoryToken.rowSelect(
                ID,
                NAME,
                ROW.setDefaultAnchor()
            )
        );
    }

    // setSaveValue.....................................................................................................

    @Test
    public final void testSetSaveValueWithInvalidOptionalValueFails() {
        this.setSaveValueFails(
            Optional.of(this)
        );
    }

    @Test
    public final void testSetSaveValueWithEmpty() {
        final Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNamesList> value = Optional.empty();

        this.setSaveValueAndCheck(
            value
        );
    }

    @Test
    public final void testSetSaveValueWithNonEmpty() {
        final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList value = SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse("1=different");

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(value),
            HistoryToken.rowSortSave(
                ID,
                NAME,
                ROW.setDefaultAnchor(),
                value
            )
        );
    }
}
