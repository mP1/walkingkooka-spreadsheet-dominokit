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

package walkingkooka.spreadsheet.dominokit.viewport;

import walkingkooka.currency.CurrencyLocaleContext;
import walkingkooka.currency.CurrencyLocaleContextDelegator;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContextDelegator;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorProvider;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorProviderDelegator;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.RefreshContextDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetFormatterFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetFormatterFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetFormatterFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.history.recent.RecentValueSavesContext;
import walkingkooka.spreadsheet.dominokit.history.recent.RecentValueSavesContextDelegator;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserProvider;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserProviderDelegator;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelNameResolver;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.text.LineEnding;

import java.util.Currency;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

final class AppContextSpreadsheetViewportComponentContext implements SpreadsheetViewportComponentContext,
    RefreshContextDelegator,
    SpreadsheetComparatorProviderDelegator,
    HasSpreadsheetDeltaFetcher,
    HasSpreadsheetDeltaFetcherWatchersDelegator,
    HasSpreadsheetFormatterFetcherWatchersDelegator,
    HasSpreadsheetMetadataFetcher,
    HasSpreadsheetMetadataFetcherWatchersDelegator,
    CurrencyLocaleContextDelegator,
    ProviderContextDelegator,
    SpreadsheetLabelNameResolver,
    SpreadsheetParserProviderDelegator,
    RecentValueSavesContextDelegator,
    SpreadsheetViewportContextDelegator{

    static AppContextSpreadsheetViewportComponentContext with(final AppContext appContext) {
        return new AppContextSpreadsheetViewportComponentContext(
            Objects.requireNonNull(appContext, "appContext")
        );
    }

    private AppContextSpreadsheetViewportComponentContext(final AppContext context) {
        this.context = context;
        this.spreadsheetViewportContext = SpreadsheetViewportContexts.spreadsheetMetadata(context);
    }

    @Override
    public HasSpreadsheetDeltaFetcherWatchers hasSpreadsheetDeltaFetcherWatchers() {
        return this.context;
    }

    @Override
    public HasSpreadsheetFormatterFetcherWatchers hasSpreadsheetFormatterFetcherWatchers() {
        return this.context;
    }

    @Override
    public HasSpreadsheetMetadataFetcherWatchers hasSpreadsheetMetadataFetcherWatchers() {
        return this.context;
    }

    @Override
    public ProviderContext providerContext() {
        return this.context;
    }

    @Override
    public SpreadsheetComparatorProvider spreadsheetComparatorProvider() {
        return this.context;
    }

    @Override
    public SpreadsheetDeltaFetcher spreadsheetDeltaFetcher() {
        return this.context.spreadsheetDeltaFetcher();
    }

    @Override
    public SpreadsheetFormatterFetcher spreadsheetFormatterFetcher() {
        return this.context.spreadsheetFormatterFetcher();
    }

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        return this.context.spreadsheetMetadata();
    }

    @Override
    public SpreadsheetMetadataFetcher spreadsheetMetadataFetcher() {
        return this.context.spreadsheetMetadataFetcher();
    }

    @Override
    public SpreadsheetParserProvider spreadsheetParserProvider() {
        return this.context;
    }

    @Override
    public SpreadsheetViewportCache spreadsheetViewportCache() {
        return this.context.spreadsheetViewportCache();
    }

    @Override
    public Optional<SpreadsheetSelection> resolveLabel(final SpreadsheetLabelName label) {
        return this.context.resolveLabel(label);
    }

    @Override
    public void giveFocus(final Runnable focus) {
        this.context.giveFocus(focus);
    }

    @Override
    public Optional<SpreadsheetCell> cell(final SpreadsheetSelection selection) {
        return this.spreadsheetViewportCache()
            .cell(selection);
    }

    @Override
    public Set<SpreadsheetLabelName> cellLabels(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
        return this.spreadsheetViewportCache()
            .cellLabels(spreadsheetExpressionReference);
    }

    @Override
    public Set<SpreadsheetExpressionReference> cellReferences(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
        return this.spreadsheetViewportCache()
            .cellReferences(spreadsheetExpressionReference);
    }

    @Override
    public RecentValueSavesContext recentValueSavesContext() {
        return this.context;
    }

    private final AppContext context;

    // EnvironmentContext...............................................................................................

    @Override
    public <T> void setEnvironmentValue(final EnvironmentValueName<T> name,
                                        final T value) {
        this.context.setEnvironmentValue(
            name,
            value
        );
    }

    @Override
    public void removeEnvironmentValue(final EnvironmentValueName<?> name) {
        this.context.removeEnvironmentValue(name);
    }

    @Override
    public Currency currency() {
        return this.context.currency();
    }

    @Override
    public void setCurrency(final Currency currency) {
        this.context.setCurrency(currency);
    }
    
    @Override
    public LineEnding lineEnding() {
        return this.context.lineEnding();
    }

    @Override
    public void setLineEnding(final LineEnding lineEnding) {
        this.context.setLineEnding(lineEnding);
    }

    @Override
    public Locale locale() {
        return this.context.locale();
    }

    @Override
    public void setLocale(final Locale locale) {
        this.context.setLocale(locale);
    }

    @Override
    public void setUser(final Optional<EmailAddress> user) {
        this.context.setUser(user);
    }

    // CurrencyLocaleContextDelegator...................................................................................

    @Override
    public CurrencyLocaleContext currencyLocaleContext() {
        return this.context;
    }

    // RefreshContextDelegator..........................................................................................

    @Override
    public RefreshContext refreshContext() {
        return this.context;
    }

    // SpreadsheetViewportContextDelegator..............................................................................

    @Override
    public SpreadsheetViewportContext spreadsheetViewportContext() {
        return this.spreadsheetViewportContext;
    }

    private final SpreadsheetViewportContext spreadsheetViewportContext;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }
}
