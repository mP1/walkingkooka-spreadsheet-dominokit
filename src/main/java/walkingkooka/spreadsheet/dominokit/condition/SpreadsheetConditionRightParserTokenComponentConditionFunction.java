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

import walkingkooka.spreadsheet.formula.ConditionRightSpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.formula.SpreadsheetParsers;
import walkingkooka.spreadsheet.parser.SpreadsheetParserContext;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A {@link Function} that parsers any given text into a {@link ConditionRightSpreadsheetFormulaParserToken}.
 */
final class ConditionRightSpreadsheetFormulaParserTokenComponentConditionFunction implements Function<String, ConditionRightSpreadsheetFormulaParserToken> {

    static ConditionRightSpreadsheetFormulaParserTokenComponentConditionFunction with(final Supplier<SpreadsheetParserContext> context) {
        Objects.requireNonNull(context, "context");

        return new ConditionRightSpreadsheetFormulaParserTokenComponentConditionFunction(context);
    }

    private ConditionRightSpreadsheetFormulaParserTokenComponentConditionFunction(final Supplier<SpreadsheetParserContext> context) {
        this.context = context;
    }

    @Override
    public ConditionRightSpreadsheetFormulaParserToken apply(final String text) {
        return SpreadsheetParsers.conditionRight(
            SpreadsheetParsers.expression()
        ).parseText(
            text,
            this.context.get()
        ).cast(ConditionRightSpreadsheetFormulaParserToken.class);
    }

    private final Supplier<SpreadsheetParserContext> context;

    @Override
    public String toString() {
        return this.context.toString();
    }
}
