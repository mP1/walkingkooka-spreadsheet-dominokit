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

    // setViewport.............................................................................................

    @Test
    public final void testSetViewportWithDifferentCell() {
        this.setViewportCellAndCheck(
                SpreadsheetSelection.parseCell("Z99")
        );
    }

    @Test
    public final void testSetViewportWithDifferentCellRange() {
        this.setViewportCellAndCheck(
                SpreadsheetSelection.parseCellRange("B2:C3")
        );
    }

    @Test
    public final void testSetViewportWithDifferentLabel() {
        this.setViewportCellAndCheck(
                SpreadsheetSelection.labelName("Label123")
        );
    }

    private void setViewportCellAndCheck(final SpreadsheetExpressionReference selection) {
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
    public final void testSetViewportWithDifferentColumn() {
        this.setViewportColumnAndCheck(
                SpreadsheetSelection.parseColumn("Z")
        );
    }

    @Test
    public final void testSetViewportWithDifferentColumnRange() {
        this.setViewportColumnAndCheck(
                SpreadsheetSelection.parseColumnRange("B:C")
        );
    }

    private void setViewportColumnAndCheck(final SpreadsheetSelection selection) {
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
    public final void testSetViewportWithDifferentRow() {
        this.setViewportRowAndCheck(
                SpreadsheetSelection.parseRow("99")
        );
    }

    @Test
    public final void testSetViewportWithDifferentRowRange() {
        this.setViewportRowAndCheck(
                SpreadsheetSelection.parseRowRange("2:3")
        );
    }

    private void setViewportRowAndCheck(final SpreadsheetSelection selection) {
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
}
