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
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

public abstract class SpreadsheetCellFormulaHistoryTokenTestCase<T extends SpreadsheetCellFormulaHistoryToken> extends SpreadsheetCellHistoryTokenTestCase<T> {

    SpreadsheetCellFormulaHistoryTokenTestCase() {
        super();
    }

    // menu with selection..............................................................................................

    @Test
    public final void testCellMenuWithSameCell() {
        final SpreadsheetCellReference cell = SpreadsheetSelection.parseCell("A1");

        this.menuAndCheck(
                this.createHistoryToken(
                        cell.setDefaultAnchor()
                ),
                cell,
                HistoryToken.cellMenu(
                        ID,
                        NAME,
                        cell.setDefaultAnchor()
                )
        );
    }

    @Test
    public final void testCellMenuWithDifferentCell() {
        final SpreadsheetCellReference cell = SpreadsheetSelection.parseCell("B2");

        this.menuAndCheck(
                this.createHistoryToken(
                        SpreadsheetSelection.parseCell("A1").setDefaultAnchor()
                ),
                cell,
                HistoryToken.cellMenu(
                        ID,
                        NAME,
                        cell.setDefaultAnchor()
                )
        );
    }

    @Test
    public void testMenuWithCell() {
        this.menuWithCellAndCheck();
    }
}
