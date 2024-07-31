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
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.provider.ConverterInfo;
import walkingkooka.convert.provider.ConverterName;
import walkingkooka.convert.provider.ConverterSelector;
import walkingkooka.net.header.MediaType;
import walkingkooka.spreadsheet.compare.SpreadsheetComparator;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.convert.SpreadsheetConverterContext;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardContextReadWatcher;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardContextWriteWatcher;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardTextItem;
import walkingkooka.spreadsheet.dominokit.find.SpreadsheetCellFind;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.format.SpreadsheetColorName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatter;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterInfo;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSample;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelectorTextComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.SpreadsheetParser;
import walkingkooka.spreadsheet.parser.SpreadsheetParserInfo;
import walkingkooka.spreadsheet.parser.SpreadsheetParserName;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelectorTextComponent;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.text.TextNode;

import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Predicate;

public class FakeAppContext implements AppContext {

    @Override
    public void readClipboardItem(final Predicate<MediaType> filter,
                                  final ClipboardContextReadWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeClipboardItem(final ClipboardTextItem item,
                                   final ClipboardContextWriteWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    // SpreadsheetDeltaWatcher.........................................................................................

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

    @Override
    public Runnable addSpreadsheetLabelMappingFetcherWatcher(final SpreadsheetLabelMappingFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addSpreadsheetLabelMappingFetcherWatcherOnce(final SpreadsheetLabelMappingFetcherWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetLabelMappingFetcher spreadsheetLabelMappingFetcher() {
        throw new UnsupportedOperationException();
    }

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

    // json.............................................................................................................

    @Override
    public JsonNodeMarshallContext marshallContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonNodeUnmarshallContext unmarshallContext() {
        throw new UnsupportedOperationException();
    }

    // SpreadsheetComparatorProvider....................................................................................

    @Override
    public SpreadsheetComparator<?> spreadsheetComparator(final SpreadsheetComparatorName spreadsheetComparatorName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<SpreadsheetComparatorInfo> spreadsheetComparatorInfos() {
        throw new UnsupportedOperationException();
    }

    // ConverterProvider................................................................................................

    @Override
    public <C extends ConverterContext> Converter<C> converter(final ConverterSelector selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <C extends ConverterContext> Converter<C> converter(final ConverterName converterName,
                                                               final List<?> values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<ConverterInfo> converterInfos() {
        throw new UnsupportedOperationException();
    }

    // SpreadsheetFormatterProvider.....................................................................................

    @Override
    public SpreadsheetFormatter spreadsheetFormatter(final SpreadsheetFormatterName name,
                                                     final List<?> values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetFormatter spreadsheetFormatter(final SpreadsheetFormatterSelector spreadsheetFormatterSelector) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<SpreadsheetFormatterSelectorTextComponent> spreadsheetFormatterNextTextComponent(final SpreadsheetFormatterSelector selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SpreadsheetFormatterSample> spreadsheetFormatterSamples(final SpreadsheetFormatterName name,
                                                                        final SpreadsheetFormatterContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<SpreadsheetFormatterInfo> spreadsheetFormatterInfos() {
        throw new UnsupportedOperationException();
    }

    // SpreadsheetParserProvider........................................................................................

    @Override
    public SpreadsheetParser spreadsheetParser(final SpreadsheetParserSelector spreadsheetparserSelector) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetParser spreadsheetParser(final SpreadsheetParserName name,
                                               final List<?> values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<SpreadsheetParserSelectorTextComponent> spreadsheetParserNextTextComponent(final SpreadsheetParserSelector selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<SpreadsheetFormatterSelector> spreadsheetFormatterSelector(final SpreadsheetParserSelector selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<SpreadsheetParserInfo> spreadsheetParserInfos() {
        throw new UnsupportedOperationException();
    }

    // HistoryToken.....................................................................................................

    @Override
    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addHistoryTokenWatcherOnce(final HistoryTokenWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HistoryToken historyToken() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void pushHistoryToken(final HistoryToken token) {
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

    @Override
    public boolean isViewportHighlightEnabled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setViewportHighlightEnabled(final boolean viewportHighlightEnabled) {
        throw new UnsupportedOperationException();
    }

    // cellFind.........................................................................................................

    @Override
    public SpreadsheetCellFind lastCellFind() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLastCellFind(final SpreadsheetCellFind lastCellFind) {
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
    public Optional<TextNode> format(final Object value) {
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
    public char percentageSymbol() {
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
    public SpreadsheetSelection resolveLabel(final SpreadsheetLabelName name) {
        throw new UnsupportedOperationException();
    }

    // SpreadsheetListDialogComponent...................................................................................

    @Override
    public OptionalInt spreadsheetListDialogComponentDefaultCount() {
        throw new UnsupportedOperationException();
    }

    // LoggingContext...................................................................................................

    @Override
    public void debug(final Object... values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void error(final Object... values) {
        throw new UnsupportedOperationException();
    }
}
