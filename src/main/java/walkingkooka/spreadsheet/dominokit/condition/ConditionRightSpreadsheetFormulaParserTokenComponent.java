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

import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentDelegator;
import walkingkooka.spreadsheet.formula.parser.ConditionRightSpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.parser.SpreadsheetParserContext;
import walkingkooka.text.HasText;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * A text box that accepts {@link ConditionRightSpreadsheetFormulaParserToken} expressions.
 */
public final class ConditionRightSpreadsheetFormulaParserTokenComponent implements ValueTextBoxComponentDelegator<ConditionRightSpreadsheetFormulaParserTokenComponent, ConditionRightSpreadsheetFormulaParserToken> {

    public static ConditionRightSpreadsheetFormulaParserTokenComponent empty(final Supplier<SpreadsheetParserContext> context) {
        return new ConditionRightSpreadsheetFormulaParserTokenComponent(
            Objects.requireNonNull(context, "context")
        );
    }

    private ConditionRightSpreadsheetFormulaParserTokenComponent(final Supplier<SpreadsheetParserContext> context) {
        final ConditionRightSpreadsheetFormulaParserTokenComponentConditionFunction function = ConditionRightSpreadsheetFormulaParserTokenComponentConditionFunction.with(context);

        this.textBox = ValueTextBoxComponent.with(
            function,
            HasText::text
        ).setValidator(
            ConditionRightSpreadsheetFormulaParserTokenComponentValidator.with(function)
        );
    }

    // ValueTextBoxComponentDelegator..................................................................................

    @Override
    public ValueTextBoxComponent<ConditionRightSpreadsheetFormulaParserToken> valueTextBoxComponent() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<ConditionRightSpreadsheetFormulaParserToken> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}
