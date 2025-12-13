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

package walkingkooka.spreadsheet.dominokit.formula;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.formula.SpreadsheetFormulaParsers;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.value.SpreadsheetErrorKind;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.util.FunctionTesting;

import java.util.Optional;

public final class SpreadsheetFormulaComponentExpressionParserFunctionTest implements FunctionTesting<SpreadsheetFormulaComponentExpressionParserFunction,
    String,
    SpreadsheetFormula>,
    SpreadsheetMetadataTesting,
    TreePrintableTesting {

    @Test
    public void testApplyExpressionWithExpressionParser() {
        final String text = "1+2";

        this.applyAndCheck(
            text,
            SpreadsheetFormula.parse(
                TextCursors.charSequence(text),
                SpreadsheetFormulaParsers.expression(),
                SPREADSHEET_PARSER_CONTEXT
            )
        );
    }

    @Test
    public void testApplyIncompleteExpressionGivesError() {
        final String text = "400+";

        this.applyAndCheck(
            text,
            SpreadsheetFormula.EMPTY.setText(text)
                .setValue(
                    Optional.of(
                        SpreadsheetErrorKind.ERROR.setMessage(
                            "End of text, expected LAMBDA_FUNCTION | NAMED_FUNCTION | \"TRUE\" | \"FALSE\" | LABEL | CELL_RANGE | CELL | GROUP | NEGATIVE | \"#.#E+#;#.#%;#.#;#%;#\" | TEXT | \"#NULL!\" | \"#DIV/0!\" | \"#VALUE!\" | \"#REF!\" | \"#NAME?\" | \"#NAME?\" | \"#NUM!\" | \"#N/A\" | \"#ERROR\" | \"#SPILL!\" | \"#CALC!\""
                        )
                    )
                )
        );
    }

    @Test
    public void testApplyConditionGivesError() {
        final String text = "<999";

        this.applyAndCheck(
            text,
            SpreadsheetFormula.EMPTY.setText(text)
                .setValue(
                    Optional.of(
                        SpreadsheetErrorKind.ERROR.setMessage(
                            "Invalid character \'<\' at (1,1) expected BINARY_EXPRESSION | LAMBDA_FUNCTION | NAMED_FUNCTION | \"TRUE\" | \"FALSE\" | LABEL | CELL_RANGE | CELL | GROUP | NEGATIVE | \"#.#E+#;#.#%;#.#;#%;#\" | TEXT | \"#NULL!\" | \"#DIV/0!\" | \"#VALUE!\" | \"#REF!\" | \"#NAME?\" | \"#NAME?\" | \"#NUM!\" | \"#N/A\" | \"#ERROR\" | \"#SPILL!\" | \"#CALC!\""
                        )
                    )
                )
        );
    }

    @Override
    public SpreadsheetFormulaComponentExpressionParserFunction createFunction() {
        return SpreadsheetFormulaComponentExpressionParserFunction.with(
            () -> SPREADSHEET_PARSER_CONTEXT
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetFormulaComponentExpressionParserFunction> type() {
        return SpreadsheetFormulaComponentExpressionParserFunction.class;
    }
}
