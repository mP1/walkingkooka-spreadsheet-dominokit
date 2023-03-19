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
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

public abstract class SpreadsheetSelectionHistoryTokenTestCase<T extends SpreadsheetSelectionHistoryToken> extends SpreadsheetNameHistoryTokenTestCase<T> {

    SpreadsheetSelectionHistoryTokenTestCase() {
        super();
    }

    @Test
    public final void testSelectionWithCell() {
        this.selectionCellAndCheck(
                SpreadsheetSelection.parseCell("A1")
        );
    }

    @Test
    public final void testSelectionWithCellRange() {
        this.selectionCellAndCheck(
                SpreadsheetSelection.parseCellRange("B2:C3")
        );
    }

    @Test
    public final void testSelectionWithLabel() {
        this.selectionCellAndCheck(
                SpreadsheetSelection.labelName("Label123")
        );
    }

    private void selectionCellAndCheck(final SpreadsheetExpressionReference selection) {
        this.checkEquals(
                SpreadsheetHistoryToken.cell(
                        ID,
                        NAME,
                        selection.setDefaultAnchor()
                ),
                this.createHistoryToken()
                        .selection(selection)
        );
    }

    @Test
    public final void testSelectionWithColumn() {
        this.selectionColumnAndCheck(
                SpreadsheetSelection.parseColumn("A")
        );
    }

    @Test
    public final void testSelectionWithColumnRange() {
        this.selectionColumnAndCheck(
                SpreadsheetSelection.parseColumnRange("B:C")
        );
    }

    private void selectionColumnAndCheck(final SpreadsheetSelection selection) {
        this.checkEquals(
                SpreadsheetHistoryToken.column(
                        ID,
                        NAME,
                        selection.setDefaultAnchor()
                ),
                this.createHistoryToken()
                        .selection(selection)
        );
    }

    @Test
    public final void testSelectionWithRow() {
        this.selectionRowAndCheck(
                SpreadsheetSelection.parseRow("1")
        );
    }

    @Test
    public final void testSelectionWithRowRange() {
        this.selectionRowAndCheck(
                SpreadsheetSelection.parseRowRange("2:3")
        );
    }

    private void selectionRowAndCheck(final SpreadsheetSelection selection) {
        this.checkEquals(
                SpreadsheetHistoryToken.row(
                        ID,
                        NAME,
                        selection.setDefaultAnchor()
                ),
                this.createHistoryToken()
                        .selection(selection)
        );
    }
}
