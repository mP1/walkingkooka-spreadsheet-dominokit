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

import java.util.Optional;

public abstract class SpreadsheetCellFormulaHistoryTokenTestCase<T extends SpreadsheetCellFormulaHistoryToken> extends SpreadsheetCellHistoryTokenTestCase<T> {

    SpreadsheetCellFormulaHistoryTokenTestCase() {
        super();
    }

    // patternKind......................................................................................................

    @Test
    public final void testPatternKind() {
        this.patternKindAndCheck(
            this.createHistoryToken()
        );
    }

    // setSaveValue.....................................................................................................

    @Test
    public final void testSetSaveValueWithInvalidOptionalValueFails() {
        this.setSaveValueFails(
            Optional.of(this)
        );
    }

    @Test
    public final void testSetSaveValueWithEmptyFormula() {
        final String value = "";

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.empty(),
            HistoryToken.cellFormulaSave(
                ID,
                NAME,
                SELECTION,
                value
            )
        );
    }

    @Test
    public final void testSetSaveValueWithDifferentFormula() {
        final String value = "different";

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(value),
            HistoryToken.cellFormulaSave(
                ID,
                NAME,
                SELECTION,
                value
            )
        );
    }
}
