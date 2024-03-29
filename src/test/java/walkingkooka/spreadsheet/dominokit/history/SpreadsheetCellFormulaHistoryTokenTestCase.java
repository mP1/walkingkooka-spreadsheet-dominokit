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
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

public abstract class SpreadsheetCellFormulaHistoryTokenTestCase<T extends SpreadsheetCellFormulaHistoryToken> extends SpreadsheetCellHistoryTokenTestCase<T> {

    SpreadsheetCellFormulaHistoryTokenTestCase() {
        super();
    }

    @Test
    public final void testSetAnchoredSelectionDifferentColumn() {
        final AnchoredSpreadsheetSelection different = SpreadsheetSelection.parseColumn("B")
                .setDefaultAnchor();

        this.setAnchoredSelectionAndCheck(
                this.createHistoryToken(),
                different,
                HistoryToken.column(
                        ID,
                        NAME,
                        different
                )
        );
    }

    @Test
    public final void testSetAnchoredSelectionDifferentRow() {
        final AnchoredSpreadsheetSelection different = SpreadsheetSelection.parseRow("2")
                .setDefaultAnchor();

        this.setAnchoredSelectionAndCheck(
                this.createHistoryToken(),
                different,
                HistoryToken.row(
                        ID,
                        NAME,
                        different
                )
        );
    }

    @Test
    public final void testSetAnchoredSelectionDifferentCell() {
        final AnchoredSpreadsheetSelection different = SpreadsheetSelection.parseCell("B2")
                .setDefaultAnchor();

        this.setAnchoredSelectionAndCheck(
                this.createHistoryToken(),
                different,
                this.createHistoryToken(
                        ID,
                        NAME,
                        different
                )
        );
    }

    // setMenu..........................................................................................................

    @Test
    public final void testSetMenuWithCell() {
        this.setMenuWithCellAndCheck();
    }

    // patternKind......................................................................................................

    @Test
    public final void testPatternKind() {
        this.patternKindAndCheck(
                this.createHistoryToken()
        );
    }
}
