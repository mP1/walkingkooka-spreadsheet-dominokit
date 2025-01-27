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

import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBox;
import walkingkooka.spreadsheet.dominokit.value.ValueSpreadsheetTextBoxWrapper;
import walkingkooka.spreadsheet.formula.ConditionRightSpreadsheetParserToken;
import walkingkooka.spreadsheet.parser.SpreadsheetParserContext;
import walkingkooka.text.HasText;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * A text box that accepts {@link ConditionRightSpreadsheetParserToken} expressions.
 */
public final class ConditionRightSpreadsheetParserTokenComponent implements ValueSpreadsheetTextBoxWrapper<ConditionRightSpreadsheetParserTokenComponent, ConditionRightSpreadsheetParserToken> {

    public static ConditionRightSpreadsheetParserTokenComponent empty(final Supplier<SpreadsheetParserContext> context) {
        return new ConditionRightSpreadsheetParserTokenComponent(
            Objects.requireNonNull(context, "context")
        );
    }

    private ConditionRightSpreadsheetParserTokenComponent(final Supplier<SpreadsheetParserContext> context) {
        final ConditionRightSpreadsheetParserTokenComponentConditionFunction function = ConditionRightSpreadsheetParserTokenComponentConditionFunction.with(context);

        this.textBox = ValueSpreadsheetTextBox.with(
            function,
            HasText::text
        ).setValidator(
            ConditionRightSpreadsheetParserTokenComponentValidator.with(function)
        );
    }

    // ValueSpreadsheetTextBoxWrapper..................................................................................

    @Override
    public ValueSpreadsheetTextBox<ConditionRightSpreadsheetParserToken> parserSpreadsheetTextBox() {
        return this.textBox;
    }

    private final ValueSpreadsheetTextBox<ConditionRightSpreadsheetParserToken> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}
