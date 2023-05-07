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
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

public abstract class SpreadsheetLabelMappingHistoryTokenTestCase<T extends SpreadsheetLabelMappingHistoryToken> extends SpreadsheetSelectionHistoryTokenTestCase<T> {

    final static SpreadsheetCellReference CELL = SpreadsheetSelection.A1;

    final static SpreadsheetLabelName LABEL = SpreadsheetSelection.labelName("Label123");

    SpreadsheetLabelMappingHistoryTokenTestCase() {
        super();
    }

    // setMenu1(Selection)..................................................................................................

    @Test
    public final void testSetMenuWithCell() {
        this.setMenuWithCellAndCheck();
    }

    @Test
    public final void testSetMenuWithColumn() {
        this.setMenuWithColumnAndCheck();
    }

    @Test
    public final void testSetMenuWithRow() {
        this.setMenuWithRowAndCheck();
    }

    final void urlFragmentAndCheck(final SpreadsheetLabelName label,
                                   final String expected) {
        this.urlFragmentAndCheck(
                this.createHistoryToken(
                        label
                ),
                expected
        );
    }

    final T createHistoryToken(final SpreadsheetId id,
                               final SpreadsheetName name) {
        return this.createHistoryToken(
                id,
                name,
                LABEL
        );
    }

    final T createHistoryToken(final SpreadsheetLabelName label) {
        return this.createHistoryToken(
                ID,
                NAME,
                label
        );
    }

    abstract T createHistoryToken(final SpreadsheetId id,
                                  final SpreadsheetName name,
                                  final SpreadsheetLabelName label);
}
