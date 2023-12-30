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
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public abstract class SpreadsheetSelectionHistoryTokenTestCase<T extends SpreadsheetSelectionHistoryToken> extends SpreadsheetNameHistoryTokenTestCase<T> {

    SpreadsheetSelectionHistoryTokenTestCase() {
        super();
    }

    // setMenu1(Selection)..............................................................................................

    @Test
    public void testSetMenuWithCell() {
        this.setMenuWithCellAndCheck();
    }

    @Test
    public void testSetMenuWithColumn() {
        this.setMenuWithColumnAndCheck();
    }

    @Test
    public void testSetMenuWithRow() {
        this.setMenuWithRowAndCheck();
    }

    // setSelection.............................................................................................

    @Test
    public final void testSetSelectionWithDifferentCell() {
        this.setSelectionCellAndCheck(
                SpreadsheetSelection.parseCell("Z99")
        );
    }

    @Test
    public final void testSetSelectionWithDifferentCellRange() {
        this.setSelectionCellAndCheck(
                SpreadsheetSelection.parseCellRange("B2:C3")
        );
    }

    @Test
    public final void testSetSelectionWithDifferentLabel() {
        this.setSelectionCellAndCheck(
                SpreadsheetSelection.labelName("Label123")
        );
    }

    private void setSelectionCellAndCheck(final SpreadsheetExpressionReference selection) {
        this.setSelectionAndCheck(
                selection,
                HistoryToken.cell(
                        ID,
                        NAME,
                        selection.setDefaultAnchor()
                )
        );
    }

    @Test
    public final void testSetSelectionWithDifferentColumn() {
        this.setSelectionColumnAndCheck(
                SpreadsheetSelection.parseColumn("Z")
        );
    }

    @Test
    public final void testSetSelectionWithDifferentColumnRange() {
        this.setSelectionColumnAndCheck(
                SpreadsheetSelection.parseColumnRange("B:C")
        );
    }

    private void setSelectionColumnAndCheck(final SpreadsheetSelection selection) {
        this.setSelectionAndCheck(
                selection,
                HistoryToken.column(
                        ID,
                        NAME,
                        selection.setDefaultAnchor()
                )
        );
    }

    @Test
    public final void testSetSelectionWithDifferentRow() {
        this.setSelectionRowAndCheck(
                SpreadsheetSelection.parseRow("99")
        );
    }

    @Test
    public final void testSetSelectionWithDifferentRowRange() {
        this.setSelectionRowAndCheck(
                SpreadsheetSelection.parseRowRange("2:3")
        );
    }

    private void setSelectionRowAndCheck(final SpreadsheetSelection selection) {
        this.setSelectionAndCheck(
                selection,
                HistoryToken.row(
                        ID,
                        NAME,
                        selection.setDefaultAnchor()
                )
        );
    }

    private void setSelectionAndCheck(final SpreadsheetSelection selection,
                                      final HistoryToken expected) {
        this.setSelectionAndCheck(
                this.createHistoryToken(),
                selection.setDefaultAnchor(),
                expected
        );
    }

    final void setSelectionAndCheck(final T token,
                                    final AnchoredSpreadsheetSelection selection,
                                    final HistoryToken expected) {
        this.checkEquals(
                expected,
                token.setSelection(
                        Optional.of(
                                selection
                        )
                ),
                () -> token + " setSelection " + selection
        );
    }
}
