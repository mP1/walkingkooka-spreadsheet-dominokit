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

import walkingkooka.Either;
import walkingkooka.color.Color;
import walkingkooka.convert.Converter;
import walkingkooka.datetime.DateTimeContext;
import walkingkooka.datetime.DateTimeContextDelegator;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.locale.LocaleContext;
import walkingkooka.locale.LocaleContextDelegator;
import walkingkooka.locale.LocaleContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContextDelegator;
import walkingkooka.spreadsheet.convert.SpreadsheetConverterContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.format.SpreadsheetColorName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatter;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContextObjectPostProcessor;
import walkingkooka.tree.json.marshall.JsonNodeMarshallUnmarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallUnmarshallContextDelegator;
import walkingkooka.tree.json.marshall.JsonNodeMarshallUnmarshallContexts;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContextPreProcessor;
import walkingkooka.tree.text.TextNode;

import java.math.MathContext;
import java.util.Locale;
import java.util.Optional;

/**
 * A {@link SpreadsheetFormatterContext} used to format a {@link walkingkooka.tree.expression.ExpressionNumber} into a {@link String}.
 * Most methods throw {@link UnsupportedOperationException}, because those {@link SpreadsheetFormatterContext} methods
 * are not required, eg colors, resolving labels etc.
 */
final class NumberComponentContextSpreadsheetFormatterContext implements SpreadsheetFormatterContext,
    DateTimeContextDelegator,
    DecimalNumberContextDelegator,
    JsonNodeMarshallUnmarshallContextDelegator,
    LocaleContextDelegator {

    static NumberComponentContextSpreadsheetFormatterContext with(final NumberComponentContext context) {
        return new NumberComponentContextSpreadsheetFormatterContext(context);
    }

    private NumberComponentContextSpreadsheetFormatterContext(final NumberComponentContext context) {
        this.context = context;
    }

    @Override
    public boolean canConvert(final Object value,
                              final Class<?> type) {
        return value instanceof ExpressionNumber &&
            ExpressionNumber.isClass(type);
    }

    @Override
    public <T> Either<T, String> convert(final Object value,
                                         final Class<T> type) {
        return this.canConvert(
            value,
            type
        ) ?
            this.successfulConversion(
                value,
                type
            ) :
            this.failConversion(
                value,
                type
            );
    }

    @Override
    public Converter<SpreadsheetConverterContext> converter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int cellCharacterWidth() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Color> colorNumber(int i) {
        throw new UnsupportedOperationException();
    }

    @Override public Optional<Color> colorName(SpreadsheetColorName spreadsheetColorName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetExpressionEvaluationContext spreadsheetExpressionEvaluationContext(final Optional<Object> cell) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<SpreadsheetCell> cell() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<SpreadsheetSelection> resolveLabel(final SpreadsheetLabelName spreadsheetLabelName) {
        throw new UnsupportedOperationException();
    }

    // SpreadsheetFormatterContext.......................................................................................

    @Override
    public boolean canNumbersHaveGroupSeparator() {
        return false;
    }

    @Override
    public long dateOffset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<TextNode> formatValue(final Optional<Object> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetFormatter spreadsheetFormatter(final SpreadsheetFormatterSelector selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetFormatterContext setLocale(final Locale locale) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetExpressionReference validationReference() {
        throw new UnsupportedOperationException();
    }

    @Override
    public char valueSeparator() {
        throw new UnsupportedOperationException();
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

    @Override
    public MathContext mathContext() {
        return this.context.mathContext();
    }

    private final NumberComponentContext context;

    // JsonNodeMarshallUnmarshallContextDelegator................................................................................

    @Override
    public JsonNodeMarshallUnmarshallContext jsonNodeMarshallUnmarshallContext() {
        return JsonNodeMarshallUnmarshallContexts.fake();
    }

    @Override
    public SpreadsheetFormatterContext setObjectPostProcessor(final JsonNodeMarshallContextObjectPostProcessor processor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetFormatterContext setPreProcessor(final JsonNodeUnmarshallContextPreProcessor processor) {
        throw new UnsupportedOperationException();
    }

    // LocaleContextDelegator...........................................................................................

    @Override
    public LocaleContext localeContext() {
        return LOCALE_CONTEXT;
    }

    private final static LocaleContext LOCALE_CONTEXT = LocaleContexts.fake();

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }
}
