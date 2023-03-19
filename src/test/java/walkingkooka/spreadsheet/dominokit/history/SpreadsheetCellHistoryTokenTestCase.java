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

import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public abstract class SpreadsheetCellHistoryTokenTestCase<T extends SpreadsheetCellHistoryToken> extends SpreadsheetViewportSelectionHistoryTokenTestCase<T> {

    final static SpreadsheetCellReference CELL = SpreadsheetSelection.parseCell("A1");

    final static SpreadsheetCellRange RANGE = SpreadsheetSelection.parseCellRange("B2:C3");

    final static SpreadsheetLabelName LABEL = SpreadsheetSelection.labelName("Label123");

    SpreadsheetCellHistoryTokenTestCase() {
        super();
    }

    final void urlFragmentAndCheck(final SpreadsheetExpressionReference reference,
                                   final String expected) {
        this.urlFragmentAndCheck(
                this.createHistoryToken(
                        reference.setAnchor(SpreadsheetViewportSelectionAnchor.NONE)
                ),
                expected
        );
    }

    final T createHistoryToken() {
        return this.createHistoryToken(
                CELL.setAnchor(SpreadsheetViewportSelectionAnchor.NONE)
        );
    }
}
