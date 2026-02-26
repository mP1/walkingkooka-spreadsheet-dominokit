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

package walkingkooka.spreadsheet.dominokit.reference;

import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.dominokit.color.FakeColorComponentContext;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterMenu;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.tree.text.TextStyleProperty;
import walkingkooka.validation.provider.ValidatorSelector;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class FakeSpreadsheetSelectionMenuContext extends FakeColorComponentContext implements SpreadsheetSelectionMenuContext {

    @Override
    public List<SpreadsheetComparatorName> sortComparatorNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Currency> recentCurrencies() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SpreadsheetFormatterSelector> recentSpreadsheetFormatterSelectors() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SpreadsheetFormatterMenu> spreadsheetFormatterMenus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<SpreadsheetLabelMapping> labelMappings(final SpreadsheetSelection selection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Locale> recentLocales() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SpreadsheetParserSelector> recentSpreadsheetParserSelectors() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<SpreadsheetExpressionReference> references(final SpreadsheetSelection selection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TextStyleProperty<?>> recentTextStyleProperties() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ValidatorSelector> validatorSelectors() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ValidatorSelector> recentValidatorSelectors() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String idPrefix() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<SpreadsheetCell> selectionSummary() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        throw new UnsupportedOperationException();
    }

    // SpreadsheetLabelNameResolver.....................................................................................

    @Override
    public Optional<SpreadsheetSelection> resolveLabel(final SpreadsheetLabelName spreadsheetLabelName) {
        throw new UnsupportedOperationException();
    }

    // CurrencyContext..................................................................................................

    @Override
    public Currency currency() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCurrency(final Currency currency) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Currency> availableCurrencies() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Currency> currencyForCurrencyCode(final String currencyCode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<String> currencyText(final Currency currency) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Currency> currencyForLocale(final Locale locale) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Currency> findByCurrencyText(final String text,
                                            final int offset,
                                            final int count) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Number exchangeRate(final Currency from,
                               final Currency to,
                               final Optional<LocalDateTime> dateTime) {
        throw new UnsupportedOperationException();
    }

    // LocaleContext....................................................................................................

    @Override
    public Set<Locale> availableLocales() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<DateTimeSymbols> dateTimeSymbolsForLocale(final Locale locale) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<DecimalNumberSymbols> decimalNumberSymbolsForLocale(final Locale locale) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Locale> findByLocaleText(final String text,
                                        final int offset,
                                        final int count) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Locale> localeForLanguageTag(final String languageTag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<String> localeText(final Locale locale) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Locale locale() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLocale(final Locale locale) {
        throw new UnsupportedOperationException();
    }
}
