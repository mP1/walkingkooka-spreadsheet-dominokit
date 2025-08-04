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

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

import java.util.Optional;

public final class SpreadsheetFormulaComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetFormula, SpreadsheetFormulaComponent>,
    SpreadsheetMetadataTesting {

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        "1+2"
                    )
                ),
            "SpreadsheetFormulaComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [1+2]\n"
        );
    }

    @Test
    public void testSetStringValueGetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        "1+2"
                    )
                ).value(),
            "Formula\n" +
                "  token:\n" +
                "    AdditionSpreadsheetFormula \"1+2\"\n" +
                "      NumberSpreadsheetFormula \"1\"\n" +
                "        DigitsSpreadsheetFormula \"1\" \"1\"\n" +
                "      PlusSymbolSpreadsheetFormula \"+\" \"+\"\n" +
                "      NumberSpreadsheetFormula \"2\"\n" +
                "        DigitsSpreadsheetFormula \"2\" \"2\"\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        "1+!!!"
                    )
                ),
            "SpreadsheetFormulaComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [1+!!!]\n" +
                "      Errors\n" +
                "        Invalid character '!' at (3,1) expected LAMBDA_FUNCTION | NAMED_FUNCTION | \"true\" | \"false\" | LABEL | CELL_RANGE | CELL | GROUP | NEGATIVE | \"#.#E+#;#.#%;#.#;#%;#\" | TEXT | \"#NULL!\" | \"#DIV/0!\" | \"#VALUE!\" | \"#REF!\" | \"#NAME?\" | \"#NAME?\" | \"#NUM!\" | \"#N/A\" | \"#ERROR\" | \"#SPILL!\" | \"#CALC!\"\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetFormulaComponent createComponent() {
        return SpreadsheetFormulaComponent.empty(
            SpreadsheetFormulaComponentFunctions.expressionParser(
                () -> SPREADSHEET_PARSER_CONTEXT
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetFormulaComponent> type() {
        return SpreadsheetFormulaComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
