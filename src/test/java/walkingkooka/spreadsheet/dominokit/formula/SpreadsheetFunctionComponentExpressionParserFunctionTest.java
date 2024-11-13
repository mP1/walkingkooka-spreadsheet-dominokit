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
import walkingkooka.spreadsheet.SpreadsheetErrorKind;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.dominokit.find.FakeSpreadsheetFindDialogComponentContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.parser.SpreadsheetParsers;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.util.FunctionTesting;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetFunctionComponentExpressionParserFunctionTest implements FunctionTesting<SpreadsheetFunctionComponentExpressionParserFunction,
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
                        SpreadsheetParsers.expression(),
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
                                                "End of text at (5,1) \"400+\" expected BINARY_SUB_EXPRESSION"
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
                                                "Invalid character \'<\' at (1,1) \"<999\" expected EXPRESSION_OPT"
                                        )
                                )
                        )
        );
    }

    @Override
    public SpreadsheetFunctionComponentExpressionParserFunction createFunction() {
        return SpreadsheetFunctionComponentExpressionParserFunction.with(
                () -> SPREADSHEET_PARSER_CONTEXT
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetFunctionComponentExpressionParserFunction> type() {
        return SpreadsheetFunctionComponentExpressionParserFunction.class;
    }
}
