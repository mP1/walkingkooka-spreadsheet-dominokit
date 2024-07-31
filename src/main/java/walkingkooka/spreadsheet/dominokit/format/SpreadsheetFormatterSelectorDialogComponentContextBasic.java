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

package walkingkooka.spreadsheet.dominokit.format;

import walkingkooka.Either;
import walkingkooka.color.Color;
import walkingkooka.convert.Converter;
import walkingkooka.spreadsheet.convert.SpreadsheetConverterContext;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContextDelegator;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.format.SpreadsheetColorName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatter;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterInfo;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSample;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelectorTextComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.text.TextNode;

import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

/**
 * An base class capturing most of the requirements for {@link SpreadsheetFormatterSelectorDialogComponentContext}
 */
abstract class SpreadsheetFormatterSelectorDialogComponentContextBasic implements SpreadsheetFormatterSelectorDialogComponentContext,
        HistoryTokenContextDelegator,
        LoggingContextDelegator {

    SpreadsheetFormatterSelectorDialogComponentContextBasic(final AppContext context) {
        super();

        this.context = context;
    }

    @Override
    public final String formatterTableHistoryTokenSave(final SpreadsheetFormatterSelector selector) {
        return selector.toString();
    }

    // SpreadsheetFormatterContext......................................................................................

    @Override
    public final int cellCharacterWidth() {
        return this.context.cellCharacterWidth();
    }

    @Override
    public final Optional<Color> colorNumber(final int number) {
        return this.context.colorNumber(number);
    }

    @Override
    public final Optional<Color> colorName(final SpreadsheetColorName name) {
        return this.context.colorName(name);
    }

    @Override
    public final Optional<TextNode> format(final Object value) {
        return this.context.format(value);
    }

    @Override
    public final TextNode formatOrEmptyText(final Object value) {
        return this.context.formatOrEmptyText(value);
    }

    @Override
    public final int generalFormatNumberDigitCount() {
        return this.context.generalFormatNumberDigitCount();
    }

    @Override
    public final long dateOffset() {
        return this.context.dateOffset();
    }

    @Override
    public final boolean canConvert(final Object value,
                                    final Class<?> type) {
        return this.context.canConvert(
                value,
                type
        );
    }

    @Override
    public final <T> Either<T, String> convert(final Object value,
                                               final Class<T> type) {
        return this.context.convert(
                value,
                type
        );
    }

    @Override
    public final List<String> ampms() {
        return this.context.ampms();
    }

    @Override
    public final String ampm(final int hourOfDay) {
        return this.context.ampm(hourOfDay);
    }

    @Override public final List<String> monthNames() {
        return this.context.monthNames();
    }

    @Override
    public final String monthName(final int month) {
        return this.context.monthName(month);
    }

    @Override
    public final List<String> monthNameAbbreviations() {
        return this.context.monthNameAbbreviations();
    }

    @Override
    public final String monthNameAbbreviation(final int month) {
        return this.context.monthNameAbbreviation(month);
    }

    @Override
    public final List<String> weekDayNames() {
        return this.context.weekDayNames();
    }

    @Override
    public final String weekDayName(final int day) {
        return this.context.weekDayName(day);
    }

    @Override
    public final List<String> weekDayNameAbbreviations() {
        return this.context.weekDayNameAbbreviations();
    }

    @Override
    public final String weekDayNameAbbreviation(final int day) {
        return this.context.weekDayNameAbbreviation(day);
    }

    @Override
    public final int defaultYear() {
        return this.context.defaultYear();
    }

    @Override
    public final int twoDigitYear() {
        return this.context.twoDigitYear();
    }

    @Override
    public final int twoToFourDigitYear(final int year) {
        return this.context.twoToFourDigitYear(year);
    }

    @Override
    public final Locale locale() {
        return this.context.locale();
    }

    @Override
    public final LocalDateTime now() {
        return this.context.now();
    }

    @Override
    public final String currencySymbol() {
        return this.context.currencySymbol();
    }

    @Override
    public final char decimalSeparator() {
        return this.context.decimalSeparator();
    }

    @Override
    public final String exponentSymbol() {
        return this.context.exponentSymbol();
    }

    @Override
    public final char groupSeparator() {
        return this.context.groupSeparator();
    }

    @Override
    public final char percentageSymbol() {
        return this.context.percentageSymbol();
    }

    @Override
    public final char negativeSign() {
        return this.context.negativeSign();
    }

    @Override
    public final char positiveSign() {
        return this.context.positiveSign();
    }

    @Override
    public final MathContext mathContext() {
        return this.context.mathContext();
    }

    @Override
    public final ExpressionNumberKind expressionNumberKind() {
        return this.context.expressionNumberKind();
    }

    @Override
    public final Converter<SpreadsheetConverterContext> converter() {
        return this.context.converter();
    }

    @Override
    public final SpreadsheetSelection resolveLabel(final SpreadsheetLabelName name) {
        return this.context.resolveLabel(name);
    }

    // SpreadsheetFormatterProvider.....................................................................................

    @Override
    public final SpreadsheetFormatter spreadsheetFormatter(final SpreadsheetFormatterSelector selector) {
        return this.context.spreadsheetFormatter(selector);
    }

    @Override
    public final SpreadsheetFormatter spreadsheetFormatter(final SpreadsheetFormatterName name,
                                                           final List<?> values) {
        return this.context.spreadsheetFormatter(
                name,
                values
        );
    }

    @Override
    public final Optional<SpreadsheetFormatterSelectorTextComponent> spreadsheetFormatterNextTextComponent(final SpreadsheetFormatterSelector selector) {
        return this.context.spreadsheetFormatterNextTextComponent(selector);
    }

    @Override
    public final List<SpreadsheetFormatterSample> spreadsheetFormatterSamples(final SpreadsheetFormatterName name,
                                                                              final SpreadsheetFormatterContext context) {
        return this.context.spreadsheetFormatterSamples(
                name,
                context
        );
    }

    @Override
    public final Set<SpreadsheetFormatterInfo> spreadsheetFormatterInfos() {
        return this.context.spreadsheetFormatterInfos();
    }

    // HistoryTokenContext..............................................................................................

    @Override
    public final HistoryTokenContext historyTokenContext() {
        return this.context;
    }

    // misc.............................................................................................................

    @Override
    public final void giveFocus(final Runnable focus) {
        this.context.giveFocus(focus);
    }

    @Override
    public final Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.context.addSpreadsheetDeltaFetcherWatcher(watcher);
    }

    @Override
    public final Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.context.addSpreadsheetMetadataFetcherWatcher(watcher);
    }

    // log..............................................................................................................

    @Override
    public LoggingContext loggingContext() {
        return this.context;
    }

    // Object..........................................................................................................

    @Override
    public final String toString() {
        return this.context.toString();
    }

    final AppContext context;
}
