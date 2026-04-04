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

import walkingkooka.Either;
import walkingkooka.color.Color;
import walkingkooka.convert.Converter;
import walkingkooka.currency.CurrencyCode;
import walkingkooka.currency.CurrencyContext;
import walkingkooka.currency.CurrencyContextDelegator;
import walkingkooka.currency.CurrencyContexts;
import walkingkooka.datetime.DateTimeContext;
import walkingkooka.datetime.DateTimeContextDelegator;
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
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.storage.StoragePath;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContextObjectPostProcessor;
import walkingkooka.tree.json.marshall.JsonNodeMarshallUnmarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallUnmarshallContextDelegator;
import walkingkooka.tree.json.marshall.JsonNodeMarshallUnmarshallContexts;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContextPreProcessor;
import walkingkooka.tree.text.TextNode;

import java.math.MathContext;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link SpreadsheetFormatterContext} that only implements {@link walkingkooka.datetime.DateTimeContext} and
 * {@link DecimalNumberContext} members.
 */
final class AppSpreadsheetFormatterContext implements SpreadsheetFormatterContext,
    CurrencyContextDelegator,
    DateTimeContextDelegator,
    DecimalNumberContextDelegator,
    JsonNodeMarshallUnmarshallContextDelegator,
    LocaleContextDelegator {

    static AppSpreadsheetFormatterContext with(final DateTimeContext dateTimeContext,
                                               final DecimalNumberContext decimalNumberContext) {
        return new AppSpreadsheetFormatterContext(
            Objects.requireNonNull(dateTimeContext, "dateTimeContext"),
            Objects.requireNonNull(decimalNumberContext, "decimalNumberContext")
        );
    }

    private AppSpreadsheetFormatterContext(final DateTimeContext dateTimeContext,
                                           final DecimalNumberContext decimalNumberContext) {
        super();

        this.dateTimeContext = dateTimeContext;
        this.decimalNumberContext = decimalNumberContext;
    }

    @Override
    public boolean canNumbersHaveGroupSeparator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long dateOffset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public char valueSeparator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean canConvert(final Object value,
                              final Class<?> tyoe) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Either<T, String> convert(final Object value,
                                         final Class<T> type) {
        throw new UnsupportedOperationException();
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
    public Optional<Color> colorNumber(final int number) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Color> colorName(final SpreadsheetColorName name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<TextNode> formatValue(final Optional<Object> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetExpressionEvaluationContext spreadsheetExpressionEvaluationContext(final Optional<Object> value) {
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
    public Optional<SpreadsheetSelection> resolveLabel(final SpreadsheetLabelName labelName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<SpreadsheetCell> cell() {
        throw new UnsupportedOperationException();
    }

    @Override
    public StoragePath parseStoragePath(final String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<StoragePath> currentWorkingDirectory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<StoragePath> homeDirectory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Indentation indentation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public LineEnding lineEnding() {
        throw new UnsupportedOperationException();
    }

    // CurrencyContextDelegator.........................................................................................

    @Override
    public Optional<Currency> currencyForCurrencyCode(final CurrencyCode currencyCode) {
        Objects.requireNonNull(currencyCode, "currencyCode");

        return this.currencyContext()
            .currencyForCurrencyCode(currencyCode);
    }

    @Override
    public CurrencyContext currencyContext() {
        return CurrencyContexts.fake();
    }
    
    // DateTimeContextDelegator.........................................................................................

    @Override
    public Locale locale() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DateTimeContext dateTimeContext() {
        return this.dateTimeContext;
    }
    
    private final DateTimeContext dateTimeContext;

    // DecimalNumberContextDelegator....................................................................................

    @Override
    public MathContext mathContext() {
        return this.decimalNumberContext()
            .mathContext();
    }

    @Override
    public DecimalNumberContext decimalNumberContext() {
        return this.decimalNumberContext;
    }

    private final DecimalNumberContext decimalNumberContext;

    // JsonNodeMarshallUnmarshallContextDelegator.......................................................................

    @Override
    public SpreadsheetFormatterContext setPreProcessor(final JsonNodeUnmarshallContextPreProcessor processor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetFormatterContext setObjectPostProcessor(final JsonNodeMarshallContextObjectPostProcessor processor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNodeMarshallUnmarshallContext jsonNodeMarshallUnmarshallContext() {
        return JsonNodeMarshallUnmarshallContexts.fake();
    }

    // LocaleContextDelegator...........................................................................................

    @Override
    public Optional<Locale> localeForLanguageTag(final String languageTag) {
        Objects.requireNonNull(languageTag, "languageTag");

        return this.localeContext()
            .localeForLanguageTag(languageTag);
    }

    @Override
    public LocaleContext localeContext() {
        return LocaleContexts.fake();
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return "dateTimeContext: " + this.dateTimeContext + ", decimalNumberContext: " + this.decimalNumberContext;
    }
}
