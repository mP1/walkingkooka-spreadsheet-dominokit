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

import walkingkooka.InvalidCharacterException;
import walkingkooka.datetime.DateTimeContext;
import walkingkooka.datetime.DateTimeContextDelegator;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContextDelegator;
import walkingkooka.spreadsheet.parser.SpreadsheetParserContext;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.parser.InvalidCharacterExceptionFactory;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.util.Locale;

final class NumberComponentContextSpreadsheetParserContext implements SpreadsheetParserContext,
    DateTimeContextDelegator,
    DecimalNumberContextDelegator {

    static NumberComponentContextSpreadsheetParserContext with(final NumberComponentContext context) {
        return new NumberComponentContextSpreadsheetParserContext(context);
    }

    private NumberComponentContextSpreadsheetParserContext(final NumberComponentContext context) {
        this.context = context;
    }
    
    // DateTimeContextDelegator.........................................................................................

    @Override
    public DateTimeContext dateTimeContext() {
        return DATE_TIME_CONTEXT;
    }

    private final static DateTimeContext DATE_TIME_CONTEXT = DateTimeContexts.fake();

    // DecimalNumberContextDelegator....................................................................................
    
    @Override
    public DecimalNumberContext decimalNumberContext() {
        return this.context;
    }

    @Override
    public Locale locale() {
        return this.context.locale();
    }

    // SpreadsheetParserContext.........................................................................................

    @Override
    public boolean canNumbersHaveGroupSeparator() {
        return false;
    }

    @Override
    public InvalidCharacterException invalidCharacterException(final Parser<?> parser,
                                                               final TextCursor text) {
        return InvalidCharacterExceptionFactory.POSITION.apply(
            parser,
            text
        );
    }

    @Override
    public char valueSeparator() {
        //return this.context.valueSeparator();
        throw new UnsupportedOperationException();
    }

    @Override
    public ExpressionNumberKind expressionNumberKind() {
        return this.context.expressionNumberKind();
    }

    private final NumberComponentContext context;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }
}
