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

package walkingkooka.spreadsheet.dominokit.find;


import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBox;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBoxWrapper;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.parser.SpreadsheetParser;
import walkingkooka.spreadsheet.parser.SpreadsheetParserContext;
import walkingkooka.spreadsheet.parser.SpreadsheetParserToken;
import walkingkooka.tree.expression.Expression;

import java.util.Objects;

/**
 * A text box that allows entries of a value expression.
 */
public final class ValueExpressionParserComponent implements ValueSpreadsheetTextBoxWrapper<ValueExpressionParserComponent, Expression> {

    public static ValueExpressionParserComponent empty(final SpreadsheetParser parser,
                                                       final SpreadsheetParserContext parseContext,
                                                       final SpreadsheetExpressionEvaluationContext evaluationContext) {
        return new ValueExpressionParserComponent(
                Objects.requireNonNull(parser, "parser"),
                Objects.requireNonNull(parseContext, "parseContext"),
                Objects.requireNonNull(evaluationContext, "evaluationContext")
        );
    }

    private ValueExpressionParserComponent(final SpreadsheetParser parser,
                                           final SpreadsheetParserContext parseContext,
                                           final SpreadsheetExpressionEvaluationContext evaluationContext) {
        this.textBox = ValueSpreadsheetTextBox.with(
                (text) -> parser.parseText(
                                text,
                                parseContext
                        ).cast(SpreadsheetParserToken.class)
                        .toExpression(evaluationContext)
                        .orElseThrow(() -> new IllegalArgumentException("Unable to parse expression")),
                Object::toString
        );
    }

    // ValueSpreadsheetTextBoxWrapper..................................................................................

    @Override
    public ValueSpreadsheetTextBox<Expression> parserSpreadsheetTextBox() {
        return this.textBox;
    }

    private final ValueSpreadsheetTextBox<Expression> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}
