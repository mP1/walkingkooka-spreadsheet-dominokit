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

package walkingkooka.spreadsheet.dominokit.condition;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.formula.parser.ConditionRightSpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

import java.util.Optional;

public final class ConditionRightSpreadsheetFormulaParserTokenComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, ConditionRightSpreadsheetFormulaParserToken, ConditionRightSpreadsheetFormulaParserTokenComponent>,
    SpreadsheetMetadataTesting {

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        "< 123"
                    )
                ),
            "ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [< 123]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        "1+2"
                    )
                ),
            "ConditionRightSpreadsheetFormulaParserTokenComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [1+2]\n" +
                "      Errors\n" +
                "        Invalid character '1' at 0 expected (\"=\" | \"<>\" | \">=\" | \">\" | \"<=\" | \"<\"), [WHITESPACE], CONDITION_RIGHT_VALUE_OR_EXPRESSION_REQUIRED, [WHITESPACE]\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public ConditionRightSpreadsheetFormulaParserTokenComponent createComponent() {
        return ConditionRightSpreadsheetFormulaParserTokenComponent.empty(
            () -> SPREADSHEET_PARSER_CONTEXT
        );
    }

    // class............................................................................................................

    @Override
    public Class<ConditionRightSpreadsheetFormulaParserTokenComponent> type() {
        return ConditionRightSpreadsheetFormulaParserTokenComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
