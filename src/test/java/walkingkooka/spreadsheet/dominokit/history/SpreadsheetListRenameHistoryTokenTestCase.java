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
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.OptionalInt;

public abstract class SpreadsheetListRenameHistoryTokenTestCase<T extends SpreadsheetListRenameHistoryToken> extends SpreadsheetIdHistoryTokenTestCase<T> {

    SpreadsheetListRenameHistoryTokenTestCase() {
        super();
    }

    @Test
    public final void testClose() {
        this.closeAndCheck(
            this.createHistoryToken(),
            HistoryToken.spreadsheetListSelect(HistoryTokenOffsetAndCount.EMPTY)
        );
    }

    @Test
    public final void testSetIdAndName() {
        this.setIdAndNameAndCheck(
            this.createHistoryToken(),
            ID,
            NAME,
            HistoryToken.spreadsheetSelect(
                ID,
                NAME
            )
        );
    }

    // labels...........................................................................................................

    @Test
    public final void testSetLabels() {
        this.setLabelsAndCheck(
            this.createHistoryToken(),
            HistoryTokenOffsetAndCount.EMPTY.setCount(
                OptionalInt.of(123)
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

    // setSelection.....................................................................................................

    @Test
    public final void testSetSelectionWithCell() {
        this.setSelectionAndCheck(
            this.createHistoryToken(),
            SpreadsheetSelection.A1
        );
    }
}
