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

package walkingkooka.spreadsheet.dominokit;

import walkingkooka.datetime.DateTimeContext;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContextTesting2;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

import java.math.MathContext;

public final class AppSpreadsheetFormatterContextTest implements SpreadsheetFormatterContextTesting2<AppSpreadsheetFormatterContext>,
    SpreadsheetMetadataTesting {

    private final static DateTimeContext DATE_TIME_CONTEXT = DateTimeContexts.basic(
        DATE_TIME_SYMBOLS,
        LOCALE,
        1920, // defaultYear
        20, // twoDigitYear
        HAS_NOW
    );

    private final static DecimalNumberContext DECIMAL_NUMBER_CONTEXT = DecimalNumberContexts.basic(
        DecimalNumberContext.DEFAULT_NUMBER_DIGIT_COUNT,
        DECIMAL_NUMBER_SYMBOLS,
        LOCALE,
        MathContext.DECIMAL32
    );

    @Override
    public void testCurrencyForLocaleWithNullLocaleFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testCurrencyExchangeRateWithNullCurrencyExchangeFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testCurrencyExchangeRateWithNullDateTimeFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testDateTimeSymbolsForLocaleWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testDecimalNumberSymbolsForLocaleWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMultiplyWithNullLeftFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMultiplyWithNullRightFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMultiplyWithNullTypeFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSpreadsheetExpressionEvaluationContextWithNullValueFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMarshallCollectionNull() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMarshallCollectionWithTypeNull() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMarshallMapNull() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMarshallMapWithTypeNull() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMarshallNull() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMarshallOptionalWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMarshallOptionalWithTypeWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMarshallWithTypeNull() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testParseStoragePathWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRegisteredTypeBigDecimal() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRegisteredTypeBigInteger() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRegisteredTypeBoolean() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRegisteredTypeByte() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRegisteredTypeCharacter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRegisteredTypeDouble() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRegisteredTypeFloat() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRegisteredTypeInteger() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRegisteredTypeList() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRegisteredTypeLocalDate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRegisteredTypeLocalDateTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRegisteredTypeLocale() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRegisteredTypeLocalTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRegisteredTypeLong() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRegisteredTypeMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRegisteredTypeMathContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRegisteredTypeOptional() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRegisteredTypeSet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRegisteredTypeShort() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRegisteredTypeString() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testResolveLabelWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetObjectPostProcessor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetObjectPostProcessorNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetObjectPostProcessorSame() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetPreProcessor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetPreProcessorNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetPreProcessorSame() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNameBigDecimal() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNameBigInteger() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNameBoolean() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNameByte() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNameCharacter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNameDouble() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNameFloat() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNameInteger() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNameList() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNameLocalDate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNameLocalDateTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNameLocale() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNameLocalTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNameLong() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNameMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNameMathContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNameOptional() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNameSet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNameShort() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNameString() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testUnmarshallListWithNullTypeFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testUnmarshallMapNullKeyTypeFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testUnmarshallMapNullValueTypeFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testUnmarshallOptionalWithNullJsonNodeFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testUnmarshallOptionalWithNullTypeFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testUnmarshallOptionalWithTypeWithNullJsonNodeFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testUnmarshallSetWithNullTypeFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testUnmarshallWithNullTypeFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AppSpreadsheetFormatterContext createContext() {
        return AppSpreadsheetFormatterContext.with(
            DATE_TIME_CONTEXT,
            DECIMAL_NUMBER_CONTEXT
        );
    }

    @Override
    public int decimalNumberDigitCount() {
        return DECIMAL_NUMBER_CONTEXT.decimalNumberDigitCount();
    }

    @Override
    public MathContext mathContext() {
        return DECIMAL_NUMBER_CONTEXT.mathContext();
    }

    @Override
    public String currencySymbol() {
        return DECIMAL_NUMBER_CONTEXT.currencySymbol();
    }

    @Override
    public char decimalSeparator() {
        return DECIMAL_NUMBER_CONTEXT.decimalSeparator();
    }

    @Override
    public String exponentSymbol() {
        return DECIMAL_NUMBER_CONTEXT.exponentSymbol();
    }

    @Override
    public char groupSeparator() {
        return DECIMAL_NUMBER_CONTEXT.groupSeparator();
    }

    @Override
    public String infinitySymbol() {
        return DECIMAL_NUMBER_CONTEXT.infinitySymbol();
    }

    @Override
    public char monetaryDecimalSeparator() {
        return DECIMAL_NUMBER_CONTEXT.monetaryDecimalSeparator();
    }

    @Override
    public String nanSymbol() {
        return DECIMAL_NUMBER_CONTEXT.nanSymbol();
    }

    @Override
    public char percentSymbol() {
        return DECIMAL_NUMBER_CONTEXT.percentSymbol();
    }

    @Override
    public char permillSymbol() {
        return DECIMAL_NUMBER_CONTEXT.permillSymbol();
    }

    @Override
    public char negativeSign() {
        return DECIMAL_NUMBER_CONTEXT.negativeSign();
    }

    @Override
    public char positiveSign() {
        return DECIMAL_NUMBER_CONTEXT.positiveSign();
    }

    @Override
    public char zeroDigit() {
        return DECIMAL_NUMBER_CONTEXT.zeroDigit();
    }

    // class............................................................................................................

    @Override
    public Class<AppSpreadsheetFormatterContext> type() {
        return AppSpreadsheetFormatterContext.class;
    }
}
