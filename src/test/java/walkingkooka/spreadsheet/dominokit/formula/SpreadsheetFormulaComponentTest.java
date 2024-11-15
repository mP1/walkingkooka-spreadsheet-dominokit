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
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

import java.util.Optional;

public final class SpreadsheetFormulaComponentTest implements ValueComponentTesting<HTMLFieldSetElement, SpreadsheetFormula, SpreadsheetFormulaComponent>,
        SpreadsheetMetadataTesting {

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
                SpreadsheetFormulaComponent.empty(
                        SpreadsheetFormulaComponentFunctions.expressionParser(
                                () -> SPREADSHEET_PARSER_CONTEXT
                        )
                ).setStringValue(
                        Optional.of(
                                "1+2"
                        )
                ),
                "SpreadsheetFormulaComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [1+2]\n"
        );
    }

    @Test
    public void testSetStringValueGetValue() {
        this.treePrintAndCheck(
                SpreadsheetFormulaComponent.empty(
                        SpreadsheetFormulaComponentFunctions.expressionParser(
                                () -> SPREADSHEET_PARSER_CONTEXT
                        )
                ).setStringValue(
                        Optional.of(
                                "1+2"
                        )
                ).value(),
                "Formula\n" +
                        "  token:\n" +
                        "    SpreadsheetAddition \"1+2\"\n" +
                        "      SpreadsheetNumber \"1\"\n" +
                        "        SpreadsheetDigits \"1\" \"1\"\n" +
                        "      SpreadsheetPlusSymbol \"+\" \"+\"\n" +
                        "      SpreadsheetNumber \"2\"\n" +
                        "        SpreadsheetDigits \"2\" \"2\"\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
                SpreadsheetFormulaComponent.empty(
                                SpreadsheetFormulaComponentFunctions.expressionParser(
                                        () -> SPREADSHEET_PARSER_CONTEXT
                                )
                        )
                        .setStringValue(
                                Optional.of(
                                        "1+!!!"
                                )
                        ),
                "SpreadsheetFormulaComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [1+!!!]\n" +
                        "      Errors\n" +
                        "        Invalid character '!' at (3,1) \"1+!!!\" expected BINARY_SUB_EXPRESSION\n"
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
