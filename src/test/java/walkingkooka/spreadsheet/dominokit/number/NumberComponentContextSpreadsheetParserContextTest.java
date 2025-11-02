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

import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContextDelegator;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.spreadsheet.parser.SpreadsheetParserContextTesting;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.math.MathContext;

public final class NumberComponentContextSpreadsheetParserContextTest implements SpreadsheetParserContextTesting<NumberComponentContextSpreadsheetParserContext>,
    DecimalNumberContextDelegator {

    @Override
    public NumberComponentContextSpreadsheetParserContext createContext() {
        return NumberComponentContextSpreadsheetParserContext.with(
            new TestNumberComponentContext()
        );
    }

    final static class TestNumberComponentContext implements NumberComponentContext,
        DecimalNumberContextDelegator {

        // DecimalNumberContextDelegator................................................................................

        @Override
        public DecimalNumberContext decimalNumberContext() {
            return DECIMAL_NUMBER_CONTEXT;
        }

        @Override
        public ExpressionNumberKind expressionNumberKind() {
            return ExpressionNumberKind.BIG_DECIMAL;
        }

        @Override
        public int generalFormatNumberDigitCount() {
            throw new UnsupportedOperationException();
        }
    }

    // DecimalNumberContextDelegator....................................................................................

    @Override
    public DecimalNumberContext decimalNumberContext() {
        return DECIMAL_NUMBER_CONTEXT;
    }

    private final static DecimalNumberContext DECIMAL_NUMBER_CONTEXT = DecimalNumberContexts.american(MathContext.DECIMAL32);

    @Override
    public MathContext mathContext() {
        return DECIMAL_NUMBER_CONTEXT.mathContext();
    }

    // class............................................................................................................

    @Override
    public Class<NumberComponentContextSpreadsheetParserContext> type() {
        return NumberComponentContextSpreadsheetParserContext.class;
    }
}
