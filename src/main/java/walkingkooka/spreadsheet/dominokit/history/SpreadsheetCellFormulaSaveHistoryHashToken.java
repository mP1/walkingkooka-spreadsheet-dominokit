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

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

import java.util.Objects;

public final class SpreadsheetCellFormulaSaveHistoryHashToken extends SpreadsheetCellFormulaHistoryHashToken {

    static SpreadsheetCellFormulaSaveHistoryHashToken with(final SpreadsheetViewportSelection viewportSelection,
                                                           final SpreadsheetFormula formula) {
        return new SpreadsheetCellFormulaSaveHistoryHashToken(
                viewportSelection,
                formula
        );
    }

    private SpreadsheetCellFormulaSaveHistoryHashToken(final SpreadsheetViewportSelection viewportSelection,
                                                       final SpreadsheetFormula formula) {
        super(viewportSelection);

        this.formula = Objects.requireNonNull(formula, "formula");
    }

    public SpreadsheetFormula formula() {
        return this.formula;
    }

    private final SpreadsheetFormula formula;

    @Override
    UrlFragment formulaUrlFragment() {
        return SAVE.append(this.formula.urlFragment());
    }
}
