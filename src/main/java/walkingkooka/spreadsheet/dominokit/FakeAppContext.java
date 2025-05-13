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
import walkingkooka.InvalidCharacterException;
import walkingkooka.color.Color;
import walkingkooka.convert.Converter;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.net.header.MediaType;
import walkingkooka.plugin.store.PluginStore;
import walkingkooka.spreadsheet.convert.SpreadsheetConverterContext;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardContextReadWatcher;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardContextWriteWatcher;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardTextItem;
import walkingkooka.spreadsheet.dominokit.fetcher.ConverterFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.ConverterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.ExpressionFunctionFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.ExpressionFunctionFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetComparatorFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetComparatorFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetExporterFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetExporterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetFormatterFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetFormatterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetImporterFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetImporterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetParserFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetParserFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.ValidatorFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.ValidatorFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.format.SpreadsheetColorName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.provider.FakeSpreadsheetProvider;
import walkingkooka.spreadsheet.provider.SpreadsheetProvider;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallUnmarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallUnmarshallContextDelegator;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.text.TextNode;

import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Predicate;

public class FakeAppContext extends FakeSpreadsheetProvider
    implements AppContext,
    JsonNodeMarshallUnmarshallContextDelegator {

    @Override
    public void clearSpreadsheetMetadata() {
        throw new UnsupportedOperationException();
    }

    // ClipboardReaderWatcher...........................................................................................

    @Override
    public void readClipboardItem(final Predicate<MediaType> filter,
                                  final ClipboardContextReadWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    // ClipboardWriterWatcher...........................................................................................

    @Override
    public void writeClipboardItem(final ClipboardTextItem item,
                                   final ClipboardContextWriteWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    // SpreadsheetComparatorWatcher.........................................................................................

    @Override
    public Runnable addSpreadsheetComparatorFetcherWatcher(final SpreadsheetComparatorFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addSpreadsheetComparatorFetcherWatcherOnce(final SpreadsheetComparatorFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetComparatorFetcher spreadsheetComparatorFetcher() {
        throw new UnsupportedOperationException();
    }

    // ConverterWatcher.................................................................................................

    @Override
    public Runnable addConverterFetcherWatcher(final ConverterFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addConverterFetcherWatcherOnce(final ConverterFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConverterFetcher converterFetcher() {
        throw new UnsupportedOperationException();
    }

    // SpreadsheetDeltaFetcherWatcher...................................................................................

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcherOnce(final SpreadsheetDeltaFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDeltaFetcher spreadsheetDeltaFetcher() {
        throw new UnsupportedOperationException();
    }

    // SpreadsheetExporterWatcher.......................................................................................

    @Override
    public Runnable addSpreadsheetExporterFetcherWatcher(final SpreadsheetExporterFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addSpreadsheetExporterFetcherWatcherOnce(final SpreadsheetExporterFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetExporterFetcher spreadsheetExporterFetcher() {
        throw new UnsupportedOperationException();
    }

    // ExpressionFunctionWatcher........................................................................................

    @Override
    public Runnable addExpressionFunctionFetcherWatcher(final ExpressionFunctionFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addExpressionFunctionFetcherWatcherOnce(final ExpressionFunctionFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExpressionFunctionFetcher expressionFunctionFetcher() {
        throw new UnsupportedOperationException();
    }

    // SpreadsheetFormatterWatcher......................................................................................

    @Override
    public Runnable addSpreadsheetFormatterFetcherWatcher(final SpreadsheetFormatterFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addSpreadsheetFormatterFetcherWatcherOnce(final SpreadsheetFormatterFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetFormatterFetcher spreadsheetFormatterFetcher() {
        throw new UnsupportedOperationException();
    }

    // SpreadsheetImporterWatcher.......................................................................................

    @Override
    public Runnable addSpreadsheetImporterFetcherWatcher(final SpreadsheetImporterFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addSpreadsheetImporterFetcherWatcherOnce(final SpreadsheetImporterFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetImporterFetcher spreadsheetImporterFetcher() {
        throw new UnsupportedOperationException();
    }

    // SpreadsheetMetadataWatcher.......................................................................................

    @Override
    public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addSpreadsheetMetadataFetcherWatcherOnce(final SpreadsheetMetadataFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetMetadataFetcher spreadsheetMetadataFetcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        throw new UnsupportedOperationException();
    }

    // SpreadsheetParserWatcher.........................................................................................

    @Override
    public Runnable addSpreadsheetParserFetcherWatcher(final SpreadsheetParserFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addSpreadsheetParserFetcherWatcherOnce(final SpreadsheetParserFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetParserFetcher spreadsheetParserFetcher() {
        throw new UnsupportedOperationException();
    }

    // PluginWatcher....................................................................................................

    @Override
    public Runnable addPluginFetcherWatcher(final PluginFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addPluginFetcherWatcherOnce(final PluginFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PluginFetcher pluginFetcher() {
        throw new UnsupportedOperationException();
    }

    // HasValidatorFetcher..............................................................................................

    @Override
    public ValidatorFetcher validatorFetcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addValidatorFetcherWatcher(final ValidatorFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addValidatorFetcherWatcherOnce(final ValidatorFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    // ProviderContext..................................................................................................

    @Override
    public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<EnvironmentValueName<?>> environmentValueNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PluginStore pluginStore() {
        throw new UnsupportedOperationException();
    }

    // json.............................................................................................................

    @Override
    public JsonNodeMarshallContext jsonNodeMarshallContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNodeUnmarshallContext jsonNodeUnmarshallContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNodeMarshallUnmarshallContext jsonNodeMarshallUnmarshallContext() {
        throw new UnsupportedOperationException();
    }

    // HistoryToken.....................................................................................................

    @Override
    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
        Objects.requireNonNull(watcher, "watcher");

        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addHistoryTokenWatcherOnce(final HistoryTokenWatcher watcher) {
        Objects.requireNonNull(watcher, "watcher");

        throw new UnsupportedOperationException();
    }

    @Override
    public HistoryToken historyToken() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void pushHistoryToken(final HistoryToken token) {
        Objects.requireNonNull(token, "token");

        throw new UnsupportedOperationException();
    }

    @Override
    public void fireCurrentHistoryToken() {
        throw new UnsupportedOperationException();
    }

    // reload...........................................................................................................

    @Override
    public void reload() {
        throw new UnsupportedOperationException();
    }

    // viewport.........................................................................................................

    @Override
    public SpreadsheetViewport viewport(final Optional<AnchoredSpreadsheetSelection> selection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetViewportCache spreadsheetViewportCache() {
        throw new UnsupportedOperationException();
    }

    // CanGiveFocus.....................................................................................................

    @Override
    public void giveFocus(final Runnable focus) {
        throw new UnsupportedOperationException();
    }

    // HasLocale........................................................................................................

    @Override
    public Locale locale() {
        throw new UnsupportedOperationException();
    }

    // HasNow...........................................................................................................

    @Override
    public LocalDateTime now() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<EmailAddress> user() {
        throw new UnsupportedOperationException();
    }

    // SpreadsheetFormatterContext......................................................................................

    @Override
    public boolean canConvert(final Object value,
                              final Class<?> type) {
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
    public Optional<TextNode> format(final Optional<Object> value) {
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
    public long dateOffset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> ampms() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String ampm(final int hourOfDay) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> monthNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String monthName(final int month) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> monthNameAbbreviations() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String monthNameAbbreviation(final int month) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> weekDayNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String weekDayName(final int day) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> weekDayNameAbbreviations() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String weekDayNameAbbreviation(final int day) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int defaultYear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int twoDigitYear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int twoToFourDigitYear(final int year) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int generalFormatNumberDigitCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MathContext mathContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExpressionNumberKind expressionNumberKind() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String currencySymbol() {
        throw new UnsupportedOperationException();
    }

    @Override
    public char decimalSeparator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String exponentSymbol() {
        throw new UnsupportedOperationException();
    }

    @Override
    public char groupSeparator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public char percentSymbol() {
        throw new UnsupportedOperationException();
    }

    @Override
    public char negativeSign() {
        throw new UnsupportedOperationException();
    }

    @Override
    public char positiveSign() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<SpreadsheetSelection> resolveLabel(final SpreadsheetLabelName name) {
        throw new UnsupportedOperationException();
    }

    // SpreadsheetParserContext.........................................................................................

    @Override
    public InvalidCharacterException invalidCharacterException(final Parser<?> parser,
                                                               final TextCursor cursor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public char valueSeparator() {
        throw new UnsupportedOperationException();
    }

    // SpreadsheetListDialogComponent...................................................................................

    @Override
    public OptionalInt spreadsheetListDialogComponentDefaultCount() {
        throw new UnsupportedOperationException();
    }

    // system SpreadsheetProvider.......................................................................................

    @Override
    public SpreadsheetProvider systemSpreadsheetProvider() {
        throw new UnsupportedOperationException();
    }

    // LoggingContext...................................................................................................

    @Override
    public void debug(final Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void info(final Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void warn(final Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void error(final Object... values) {
        throw new UnsupportedOperationException();
    }
}
