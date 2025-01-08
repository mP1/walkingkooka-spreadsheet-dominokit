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

import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.parser.SpreadsheetParserContext;
import walkingkooka.spreadsheet.parser.SpreadsheetParsers;
import walkingkooka.text.cursor.TextCursors;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A {@link Function} that parsers any given text into a {@link SpreadsheetFormula} and the provided {@link SpreadsheetParserContext}
 */
final class SpreadsheetFormulaComponentExpressionParserFunction implements Function<String, SpreadsheetFormula> {

    static SpreadsheetFormulaComponentExpressionParserFunction with(final Supplier<SpreadsheetParserContext> context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetFormulaComponentExpressionParserFunction(context);
    }

    private SpreadsheetFormulaComponentExpressionParserFunction(final Supplier<SpreadsheetParserContext> context) {
        this.context = context;
    }

    @Override
    public SpreadsheetFormula apply(final String text) {
        return SpreadsheetFormula.parse(
            TextCursors.charSequence(text),
            SpreadsheetParsers.expression(),
            this.context.get()
        );
    }

    private final Supplier<SpreadsheetParserContext> context;

    @Override
    public String toString() {
        return this.context.toString();
    }
}
