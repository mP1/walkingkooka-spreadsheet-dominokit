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

import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContextDelegator;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorProvider;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorProviderDelegator;
import walkingkooka.spreadsheet.dominokit.AppContext;
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
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.dominokit.history.recent.RecentValueSavesContext;
import walkingkooka.spreadsheet.dominokit.history.recent.RecentValueSavesContextDelegator;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.SpreadsheetParserProvider;
import walkingkooka.spreadsheet.parser.SpreadsheetParserProviderDelegator;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelNameResolver;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

final class AppContextSpreadsheetViewportComponentContext implements SpreadsheetViewportComponentContext,
    HistoryContextDelegator,
    LoggingContextDelegator,
    SpreadsheetComparatorProviderDelegator,
    HasSpreadsheetDeltaFetcher,
    HasSpreadsheetDeltaFetcherWatchersDelegator,
    HasSpreadsheetFormatterFetcherWatchersDelegator,
    HasSpreadsheetMetadataFetcher,
    HasSpreadsheetMetadataFetcherWatchersDelegator,
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
    public HistoryContext historyContext() {
        return this.context;
    }

    @Override
    public LoggingContext loggingContext() {
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
    public SpreadsheetViewportComponentContext setLocale(final Locale locale) {
        this.context.setLocale(locale);
        return this;
    }

    @Override
    public <T> SpreadsheetViewportComponentContext setEnvironmentValue(final EnvironmentValueName<T> name,
                                                                       final T value) {
        this.context.setEnvironmentValue(
            name,
            value
        );
        return this;
    }

    @Override
    public SpreadsheetViewportComponentContext removeEnvironmentValue(final EnvironmentValueName<?> name) {
        this.context.removeEnvironmentValue(name);
        return this;
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
