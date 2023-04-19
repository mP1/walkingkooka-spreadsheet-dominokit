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
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

import java.util.Optional;

public abstract class SpreadsheetSelectionHistoryTokenTestCase<T extends SpreadsheetSelectionHistoryToken> extends SpreadsheetNameHistoryTokenTestCase<T> {

    SpreadsheetSelectionHistoryTokenTestCase() {
        super();
    }

    // setViewportSelection.............................................................................................

    @Test
    public final void testSetViewportSelectionWithDifferentCell() {
        this.setViewportSelectionCellAndCheck(
                SpreadsheetSelection.parseCell("Z99")
        );
    }

    @Test
    public final void testSetViewportSelectionWithDifferentCellRange() {
        this.setViewportSelectionCellAndCheck(
                SpreadsheetSelection.parseCellRange("B2:C3")
        );
    }

    @Test
    public final void testSetViewportSelectionWithDifferentLabel() {
        this.setViewportSelectionCellAndCheck(
                SpreadsheetSelection.labelName("Label123")
        );
    }

    private void setViewportSelectionCellAndCheck(final SpreadsheetExpressionReference selection) {
        this.setViewportSelectionAndCheck(
                selection,
                HistoryToken.cell(
                        ID,
                        NAME,
                        selection.setDefaultAnchor()
                )
        );
    }

    @Test
    public final void testSetViewportSelectionWithDifferentColumn() {
        this.setViewportSelectionColumnAndCheck(
                SpreadsheetSelection.parseColumn("Z")
        );
    }

    @Test
    public final void testSetViewportSelectionWithDifferentColumnRange() {
        this.setViewportSelectionColumnAndCheck(
                SpreadsheetSelection.parseColumnRange("B:C")
        );
    }

    private void setViewportSelectionColumnAndCheck(final SpreadsheetSelection selection) {
        this.setViewportSelectionAndCheck(
                selection,
                HistoryToken.column(
                        ID,
                        NAME,
                        selection.setDefaultAnchor()
                )
        );
    }

    @Test
    public final void testSetViewportSelectionWithDifferentRow() {
        this.setViewportSelectionRowAndCheck(
                SpreadsheetSelection.parseRow("99")
        );
    }

    @Test
    public final void testSetViewportSelectionWithDifferentRowRange() {
        this.setViewportSelectionRowAndCheck(
                SpreadsheetSelection.parseRowRange("2:3")
        );
    }

    private void setViewportSelectionRowAndCheck(final SpreadsheetSelection selection) {
        this.setViewportSelectionAndCheck(
                selection,
                HistoryToken.row(
                        ID,
                        NAME,
                        selection.setDefaultAnchor()
                )
        );
    }

    private void setViewportSelectionAndCheck(final SpreadsheetSelection selection,
                                              final HistoryToken expected) {
        this.setViewportSelectionAndCheck(
                this.createHistoryToken(),
                selection.setDefaultAnchor(),
                expected
        );
    }

    final void setViewportSelectionAndCheck(final T token,
                                            final SpreadsheetViewportSelection viewportSelection,
                                            final HistoryToken expected) {
        this.checkEquals(
                expected,
                token.setViewportSelection(
                        Optional.of(
                                viewportSelection
                        )
                ),
                () -> token + " setViewportSelection " + viewportSelection
        );
    }

    // menu(Selection)..................................................................................................

    @Test
    public void testMenuWithCell() {
        this.menuWithCellAndCheck();
    }

    @Test
    public void testMenuWithColumn() {
        this.menuWithColumnAndCheck();
    }

    @Test
    public void testMenuWithRow() {
        this.menuWithRowAndCheck();
    }
}
