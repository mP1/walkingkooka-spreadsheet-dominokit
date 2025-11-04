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

package walkingkooka.spreadsheet.dominokit.number;


import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentDelegator;
import walkingkooka.spreadsheet.format.SpreadsheetFormatters;
import walkingkooka.spreadsheet.formula.parser.NumberSpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.parser.SpreadsheetParsers;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.text.TextNode;

import java.util.Objects;
import java.util.Optional;

/**
 * A text box that supports and validates {@link ExpressionNumber}.
 */
public final class NumberComponent implements ValueTextBoxComponentDelegator<NumberComponent, ExpressionNumber> {

    public static NumberComponent empty(final String id,
                                        final NumberComponentContext context) {
        CharSequences.failIfNullOrEmpty(id, id);
        Objects.requireNonNull(context, "context");
        return new NumberComponent(
            ValueTextBoxComponent.with(
                t -> {
                    final NumberComponentContextSpreadsheetParserContext numberComponentContextSpreadsheetParserContext = NumberComponentContextSpreadsheetParserContext.with(context);
                    return SpreadsheetParsers.general()
                        .parseText(
                            t,
                            numberComponentContextSpreadsheetParserContext
                        ).cast(NumberSpreadsheetFormulaParserToken.class)
                        .toNumber(numberComponentContextSpreadsheetParserContext);
                }, // parser String -> ExpressionNumber
                v -> SpreadsheetFormatters.general()
                    .format(
                        Optional.of(v),
                        NumberComponentContextSpreadsheetFormatterContext.with(context)
                    ).orElse(TextNode.EMPTY_TEXT)
                    .text() // formatter ExpressionNumber to String
            ).setId(id)
        );
    }

    private NumberComponent(final ValueTextBoxComponent<ExpressionNumber> textBox) {
        this.textBox = textBox;
    }

    // ValueTextBoxComponentDelegator...................................................................................

    @Override
    public ValueTextBoxComponent<ExpressionNumber> valueTextBoxComponent() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<ExpressionNumber> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}