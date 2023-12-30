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
