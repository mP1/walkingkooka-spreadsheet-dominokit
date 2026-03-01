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

import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;
import elemental2.dom.Headers;
import walkingkooka.InvalidCharacterException;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.ImmutableSortedSet;
import walkingkooka.collect.set.Sets;
import walkingkooka.convert.ConverterLike;
import walkingkooka.convert.provider.ConverterInfoSet;
import walkingkooka.convert.provider.ConverterProvider;
import walkingkooka.convert.provider.ConverterProviders;
import walkingkooka.convert.provider.ConverterSelector;
import walkingkooka.currency.CurrencyContext;
import walkingkooka.currency.CurrencyContextDelegator;
import walkingkooka.currency.CurrencyContexts;
import walkingkooka.currency.CurrencyLocaleContext;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.datetime.HasNow;
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.environment.EnvironmentContexts;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.j2cl.locale.LocaleAware;
import walkingkooka.locale.LocaleContext;
import walkingkooka.locale.LocaleContextDelegator;
import walkingkooka.locale.LocaleContexts;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.Url;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.plugin.PluginName;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContextDelegator;
import walkingkooka.plugin.store.Plugin;
import walkingkooka.plugin.store.PluginSet;
import walkingkooka.plugin.store.PluginStores;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorInfoSet;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorProvider;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorProviders;
import walkingkooka.spreadsheet.convert.provider.MissingConverter;
import walkingkooka.spreadsheet.convert.provider.SpreadsheetConvertersConverterProviders;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardContext;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardContextReadWatcher;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardContextWriteWatcher;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardContexts;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardTextItem;
import walkingkooka.spreadsheet.dominokit.currency.CurrencyComponent;
import walkingkooka.spreadsheet.dominokit.fetcher.ConverterFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.ConverterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.ConverterFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.CurrencyFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.CurrencyFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.CurrencyFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.DateTimeSymbolsFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.DateTimeSymbolsFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.DateTimeSymbolsFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.DecimalNumberSymbolsFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.DecimalNumberSymbolsFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.DecimalNumberSymbolsFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.ExpressionFunctionFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.ExpressionFunctionFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.ExpressionFunctionFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.FetcherRequestBody;
import walkingkooka.spreadsheet.dominokit.fetcher.FormHandlerFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.FormHandlerFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.FormHandlerFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasPluginFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasPluginFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.LocaleFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.LocaleFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.LocaleFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.PluginFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetComparatorFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetComparatorFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetComparatorFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetExporterFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetExporterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetExporterFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetFormatterFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetFormatterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetFormatterFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetImporterFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetImporterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetImporterFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetParserFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetParserFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetParserFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.ValidatorFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.ValidatorFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.ValidatorFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.focus.CanGiveFocus;
import walkingkooka.spreadsheet.dominokit.focus.CanGiveFocuses;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetListRenameHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetListSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.recent.RecentValueSavesContext;
import walkingkooka.spreadsheet.dominokit.history.recent.RecentValueSavesContextDelegator;
import walkingkooka.spreadsheet.dominokit.history.recent.RecentValueSavesContexts;
import walkingkooka.spreadsheet.dominokit.key.SpreadsheetKeyBindings;
import walkingkooka.spreadsheet.dominokit.key.SpreadsheetKeyBindingses;
import walkingkooka.spreadsheet.dominokit.locale.LocaleComponent;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;
import walkingkooka.spreadsheet.dominokit.log.LoggingContexts;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportComponent;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportComponentContexts;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.environment.SpreadsheetEnvironmentContext;
import walkingkooka.spreadsheet.environment.SpreadsheetEnvironmentContextDelegator;
import walkingkooka.spreadsheet.environment.SpreadsheetEnvironmentContexts;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterInfoSet;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterProvider;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterProviders;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContextDelegator;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContexts;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterInfoSet;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterProvider;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterProviders;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterInfoSet;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterProvider;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterProviders;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.parser.SpreadsheetParserContext;
import walkingkooka.spreadsheet.parser.SpreadsheetParserContexts;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserInfoSet;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserProvider;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserProviders;
import walkingkooka.spreadsheet.provider.SpreadsheetProvider;
import walkingkooka.spreadsheet.provider.SpreadsheetProviderContexts;
import walkingkooka.spreadsheet.provider.SpreadsheetProviderDelegator;
import walkingkooka.spreadsheet.provider.SpreadsheetProviders;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.server.SpreadsheetServerStartup;
import walkingkooka.spreadsheet.server.currency.CurrencyCode;
import walkingkooka.spreadsheet.server.currency.CurrencyHateosResource;
import walkingkooka.spreadsheet.server.currency.CurrencyHateosResourceSet;
import walkingkooka.spreadsheet.server.datetimesymbols.DateTimeSymbolsHateosResource;
import walkingkooka.spreadsheet.server.datetimesymbols.DateTimeSymbolsHateosResourceSet;
import walkingkooka.spreadsheet.server.decimalnumbersymbols.DecimalNumberSymbolsHateosResource;
import walkingkooka.spreadsheet.server.decimalnumbersymbols.DecimalNumberSymbolsHateosResourceSet;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterMenuList;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorEdit;
import walkingkooka.spreadsheet.server.locale.LocaleHateosResource;
import walkingkooka.spreadsheet.server.locale.LocaleHateosResourceSet;
import walkingkooka.spreadsheet.server.locale.LocaleLanguageTag;
import walkingkooka.spreadsheet.server.parser.SpreadsheetParserSelectorEdit;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoList;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewport;
import walkingkooka.storage.StoragePath;
import walkingkooka.storage.Storages;
import walkingkooka.text.CharSequences;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfoSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionProvider;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionProviders;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContextDelegator;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContextDelegator;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContexts;
import walkingkooka.validation.form.provider.FormHandlerInfo;
import walkingkooka.validation.form.provider.FormHandlerInfoSet;
import walkingkooka.validation.form.provider.FormHandlerProvider;
import walkingkooka.validation.form.provider.FormHandlerProviders;
import walkingkooka.validation.provider.ValidatorInfo;
import walkingkooka.validation.provider.ValidatorInfoSet;
import walkingkooka.validation.provider.ValidatorProvider;
import walkingkooka.validation.provider.ValidatorProviders;

import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Predicate;

@LocaleAware
public class App implements EntryPoint,
    AppContext,
    ConverterFetcherWatcher,
    CurrencyFetcherWatcher,
    CurrencyContextDelegator,
    CurrencyLocaleContext,
    DateTimeSymbolsFetcherWatcher,
    DecimalNumberSymbolsFetcherWatcher,
    ExpressionFunctionFetcherWatcher,
    FormHandlerFetcherWatcher,
    HasPluginFetcherWatchersDelegator,
    HasSpreadsheetDeltaFetcherWatchersDelegator,
    HasSpreadsheetMetadataFetcherWatchersDelegator,
    HistoryContextDelegator,
    JsonNodeMarshallContextDelegator,
    JsonNodeUnmarshallContextDelegator,
    LocaleContextDelegator,
    LocaleFetcherWatcher,
    LoggingContextDelegator,
    NopEmptyResponseFetcherWatcher,
    PluginFetcherWatcher,
    ProviderContextDelegator,
    RecentValueSavesContextDelegator,
    SpreadsheetComparatorFetcherWatcher,
    SpreadsheetDeltaFetcherWatcher,
    SpreadsheetEnvironmentContextDelegator,
    SpreadsheetExporterFetcherWatcher,
    SpreadsheetFormatterContextDelegator,
    SpreadsheetFormatterFetcherWatcher,
    SpreadsheetImporterFetcherWatcher,
    SpreadsheetMetadataFetcherWatcher,
    SpreadsheetParserFetcherWatcher,
    SpreadsheetProviderDelegator,
    ValidatorFetcherWatcher,
    WindowResizeWatcher {

    /**
     * When a {@link HistoryTokenWatcher#onHistoryTokenChange(HistoryToken, AppContext)} exceeds this value, it should be considered too slow and a WARN message logged.
     */
    public final static long SLOW_HISTORY_TOKEN_CHANGE = 150;

    private final static Locale LOCALE = Locale.forLanguageTag("en-AU");

    private final static Currency CURRENCY = Currency.getInstance(LOCALE);

    private final static LocaleContext LOCALE_CONTEXT = LocaleContexts.readOnly(
        LocaleContexts.jre(LOCALE)
    );

    private final static CurrencyContext CURRENCY_CONTEXT = CurrencyContexts.jre(
        CURRENCY,
        (Currency from, Currency to) -> {
            throw new UnsupportedOperationException();
        }, // exchange rate computer
        LOCALE_CONTEXT
    );

    private final static LineEnding LINE_ENDING = LineEnding.CRNL;

    private final static HasNow NOW = LocalDateTime::now;

    public App() {
        SpreadsheetServerStartup.init();

        this.addWindowResizeListener(this::onWindowResize);

        // logging
        this.loggingContext = AppLoggingContext.with(
            LoggingContexts.elemental()
        );

        this.canGiveFocus = CanGiveFocuses.scheduler(this.loggingContext);
        AppUncaughtExceptionHandler.with(this.loggingContext);

        this.spreadsheetProvider = SpreadsheetProviders.basic(
            ConverterProviders.empty(),
            ExpressionFunctionProviders.empty(SpreadsheetExpressionFunctions.NAME_CASE_SENSITIVITY),
            SpreadsheetComparatorProviders.empty(),
            SpreadsheetExporterProviders.empty(),
            SpreadsheetFormatterProviders.empty(),
            FormHandlerProviders.empty(),
            SpreadsheetImporterProviders.empty(),
            SpreadsheetParserProviders.empty(),
            ValidatorProviders.empty()
        );

        this.unmarshallContext = JsonNodeUnmarshallContexts.basic(
            CURRENCY_CONTEXT,
            LOCALE_CONTEXT,
            ExpressionNumberKind.DEFAULT,
            MathContext.DECIMAL32
        );

        // metadata
        this.spreadsheetMetadata = SpreadsheetMetadata.EMPTY;
        this.metadataFetcherWatchers = SpreadsheetMetadataFetcherWatchers.empty();
        this.spreadsheetMetadataFetcher = SpreadsheetMetadataFetcher.with(
            this.metadataFetcherWatchers,
            this
        );
        this.addSpreadsheetMetadataFetcherWatcher(this);

        this.formatterContext = SpreadsheetFormatterContexts.fake();
        this.parserContext = SpreadsheetParserContexts.fake();

        // comparator
        this.spreadsheetComparatorFetcherWatchers = SpreadsheetComparatorFetcherWatchers.empty();
        this.spreadsheetComparatorFetcher = SpreadsheetComparatorFetcher.with(
            this.spreadsheetComparatorFetcherWatchers,
            this
        );
        this.spreadsheetComparatorInfoSet = SpreadsheetComparatorInfoSet.EMPTY;
        this.addSpreadsheetComparatorFetcherWatcher(this);

        // converter
        this.converterFetcherWatchers = ConverterFetcherWatchers.empty();
        this.converterFetcher = ConverterFetcher.with(
            this.converterFetcherWatchers,
            this
        );
        this.converterInfoSet = ConverterInfoSet.EMPTY;
        this.addConverterFetcherWatcher(this);

        // currency
        this.currencyFetcherWatchers = CurrencyFetcherWatchers.empty();
        this.currencyFetcher = CurrencyFetcher.with(
            this.currencyFetcherWatchers,
            this
        );
        this.addCurrencyFetcherWatcher(this);

        // dateTimeSymbols
        this.dateTimeSymbolsFetcherWatchers = DateTimeSymbolsFetcherWatchers.empty();
        this.dateTimeSymbolsFetcher = DateTimeSymbolsFetcher.with(
            this.dateTimeSymbolsFetcherWatchers,
            this
        );
        this.addDateTimeSymbolsFetcherWatcher(this);

        // decimalNumberSymbols
        this.decimalNumberSymbolsFetcherWatchers = DecimalNumberSymbolsFetcherWatchers.empty();
        this.decimalNumberSymbolsFetcher = DecimalNumberSymbolsFetcher.with(
            this.decimalNumberSymbolsFetcherWatchers,
            this
        );
        this.addDecimalNumberSymbolsFetcherWatcher(this);

        // delta
        this.spreadsheetDeltaFetcherWatchers = SpreadsheetDeltaFetcherWatchers.empty();
        this.spreadsheetDeltaFetcher = SpreadsheetDeltaFetcher.with(
            this.spreadsheetDeltaFetcherWatchers,
            this
        );
        this.addSpreadsheetDeltaFetcherWatcher(this);

        // exporter
        this.spreadsheetExporterFetcherWatchers = SpreadsheetExporterFetcherWatchers.empty();
        this.spreadsheetExporterFetcher = SpreadsheetExporterFetcher.with(
            this.spreadsheetExporterFetcherWatchers,
            this
        );
        this.spreadsheetExporterInfoSet = SpreadsheetExporterInfoSet.EMPTY;
        this.addSpreadsheetExporterFetcherWatcher(this);

        // expressionFunction
        this.expressionFunctionFetcherWatchers = ExpressionFunctionFetcherWatchers.empty();
        this.expressionFunctionFetcher = ExpressionFunctionFetcher.with(
            this.expressionFunctionFetcherWatchers,
            this
        );
        this.expressionFunctionInfoSet = SpreadsheetExpressionFunctions.EMPTY_INFO_SET;
        this.addExpressionFunctionFetcherWatcher(this);

        // formatter
        this.spreadsheetFormatterFetcherWatchers = SpreadsheetFormatterFetcherWatchers.empty();
        this.spreadsheetFormatterFetcher = SpreadsheetFormatterFetcher.with(
            this.spreadsheetFormatterFetcherWatchers,
            this
        );
        this.spreadsheetFormatterInfoSet = SpreadsheetFormatterInfoSet.EMPTY;
        this.addSpreadsheetFormatterFetcherWatcher(this);

        // formHandler
        this.formHandlerFetcherWatchers = FormHandlerFetcherWatchers.empty();
        this.formHandlerFetcher = FormHandlerFetcher.with(
            this.formHandlerFetcherWatchers,
            this
        );
        this.formHandlerInfoSet = FormHandlerInfoSet.EMPTY;
        this.addFormHandlerFetcherWatcher(this);

        // importer
        this.spreadsheetImporterFetcherWatchers = SpreadsheetImporterFetcherWatchers.empty();
        this.spreadsheetImporterFetcher = SpreadsheetImporterFetcher.with(
            this.spreadsheetImporterFetcherWatchers,
            this
        );
        this.spreadsheetImporterInfoSet = SpreadsheetImporterInfoSet.EMPTY;
        this.addSpreadsheetImporterFetcherWatcher(this);

        // locale
        this.localeFetcherWatchers = LocaleFetcherWatchers.empty();
        this.localeFetcher = LocaleFetcher.with(
            this.localeFetcherWatchers,
            this
        );
        this.addLocaleFetcherWatcher(this);

        // parser
        this.spreadsheetParserFetcherWatchers = SpreadsheetParserFetcherWatchers.empty();
        this.spreadsheetParserFetcher = SpreadsheetParserFetcher.with(
            this.spreadsheetParserFetcherWatchers,
            this
        );
        this.spreadsheetParserInfoSet = SpreadsheetParserInfoSet.EMPTY;
        this.addSpreadsheetParserFetcherWatcher(this);

        // importer
        this.validatorFetcherWatchers = ValidatorFetcherWatchers.empty();
        this.validatorFetcher = ValidatorFetcher.with(
            this.validatorFetcherWatchers,
            this
        );
        this.validatorInfoSet = ValidatorInfoSet.EMPTY;
        this.addValidatorFetcherWatcher(this);

        // plugins
        this.pluginFetcherWatchers = PluginFetcherWatchers.empty();
        this.pluginFetcher = PluginFetcher.with(
            this.pluginFetcherWatchers,
            this
        );
        this.addPluginFetcherWatcher(this);

        this.spreadsheetEnvironmentContext = SpreadsheetEnvironmentContexts.basic(
            Storages.fake(),
            EnvironmentContexts.empty(
                CURRENCY,
                Indentation.SPACES2, // "default" cant use this.indentation() to avoid race
                LINE_ENDING,
                LOCALE,
                NOW,
                EnvironmentContext.ANONYMOUS // will be replaced when the metadata loads
            )
        );

        this.providerContext = SpreadsheetProviderContexts.spreadsheet(
            PluginStores.fake(),
            this,
            this.spreadsheetEnvironmentContext(),
            this.jsonNodeMarshallUnmarshallContext()
        );

        // history
        this.apphistoryContextHistoryTokenWatcher = AppHistoryContextHistoryWatcher.with(this);

        this.viewportCache = SpreadsheetViewportCache.empty(this);

        final SpreadsheetKeyBindings keyBindings = SpreadsheetKeyBindingses.basic();

        this.viewportComponent = SpreadsheetViewportComponent.empty(
            keyBindings,
            SpreadsheetViewportComponentContexts.appContext(this)
        );

        AppSpreadsheetDialogComponents.register(this);

        this.layout = SpreadsheetAppLayout.prepare(
            keyBindings,
            this.viewportComponent,
            this
        );

        DomGlobal.document.body.append(
            this.layout.element()
        );

        // load all Currencys
        this.currencyFetcher.getCurrencies(
            0,
            Integer.MAX_VALUE
        );
        
        // load all Locales
        this.localeFetcher.getLocales(
            0,
            Integer.MAX_VALUE
        );

        this.recentValueSavesContext = RecentValueSavesContexts.historyContext(this);
    }

    // WindowResize.....................................................................................................

    private void onWindowResize(final Integer width,
                                final Integer height) {
        final SpreadsheetAppLayout layout = this.layout;
        final int navigationBarHeight = layout.getNavBar()
            .element()
            .offsetHeight;

        final int newHeight = height - navigationBarHeight;
        this.debug("App.onWindowResize: " + width + " x " + height + " navigationBarHeight: " + navigationBarHeight + " newHeight: " + newHeight);

        this.viewportComponent.setWidthAndHeight(
            width,
            newHeight
        );

        this.computeAndSaveSpreadsheetListDialogComponentDefaultCount(newHeight);
    }

    private final SpreadsheetAppLayout layout;

    // defaultCount.....................................................................................................

    @Override
    public OptionalInt spreadsheetListDialogComponentDefaultCount() {
        return this.defaultCount;
    }

    private OptionalInt defaultCount = OptionalInt.of(10);

    private void computeAndSaveSpreadsheetListDialogComponentDefaultCount(final int windowHeight) {
        // height - 350 reserved for dialog title, links along bottom etc divided by 32 for each row
        final OptionalInt defaultCount = OptionalInt.of(
            (windowHeight - 300) / 32
        );
        this.debug("App.computeAndSaveSpreadsheetListDialogComponentDefaultCount: (windowHeight: " + windowHeight + " - 300) / 32 = defaultCount): " + defaultCount);
        this.defaultCount = defaultCount;

        this.lastResize = System.currentTimeMillis();

        // only reload SpreadsheetListDialogComponent after resizing stops.
        DomGlobal.setTimeout(
            (values) -> {
                if (System.currentTimeMillis() - this.lastResize > SPREADSHEET_LIST_RESIZE_DELAY) {
                    final HistoryToken historyToken = this.historyToken();
                    if (historyToken instanceof SpreadsheetListSelectHistoryToken) {
                        final OptionalInt count = historyToken.count();
                        if (false == count.isPresent()) {
                            this.pushHistoryToken(
                                historyToken.reload()
                            );
                        }
                    }
                }
            },
            SPREADSHEET_LIST_RESIZE_DELAY
        );
    }

    private final static int SPREADSHEET_LIST_RESIZE_DELAY = 1000;

    /**
     * Used to track when resizing stops, after resizing stops a reload will happen if SpreadsheetListDialogComponent is displayed.
     */
    private long lastResize;

    // EntryPoint.......................................................................................................

    @Override
    public void onModuleLoad() {
        this.apphistoryContextHistoryTokenWatcher.onHashChange(
            this.historyToken()
        );

        this.fireWindowSizeLater(this::onWindowResize);
    }

    @Override
    public void clearSpreadsheetMetadata() {
        this.spreadsheetMetadata = SpreadsheetMetadata.EMPTY;
    }

    // CanGiveFocus.....................................................................................................

    @Override
    public void giveFocus(final Runnable giveFocus) {
        this.canGiveFocus.giveFocus(giveFocus);
    }

    private final CanGiveFocus canGiveFocus;

    // ClipboardContext.................................................................................................

    @Override
    public void readClipboardItem(final Predicate<MediaType> filter,
                                  final ClipboardContextReadWatcher watcher) {
        this.clipboardContext.readClipboardItem(
            filter,
            watcher
        );
    }

    @Override
    public void writeClipboardItem(final ClipboardTextItem item,
                                   final ClipboardContextWriteWatcher watcher) {
        this.clipboardContext.writeClipboardItem(
            item,
            watcher
        );
    }

    private final ClipboardContext clipboardContext = ClipboardContexts.elemental();

    // SpreadsheetComparatorFetcher.....................................................................................

    @Override
    public SpreadsheetComparatorFetcher spreadsheetComparatorFetcher() {
        return this.spreadsheetComparatorFetcher;
    }

    private final SpreadsheetComparatorFetcher spreadsheetComparatorFetcher;

    @Override
    public Runnable addSpreadsheetComparatorFetcherWatcher(final SpreadsheetComparatorFetcherWatcher watcher) {
        return this.spreadsheetComparatorFetcherWatchers.add(watcher);
    }

    @Override
    public Runnable addSpreadsheetComparatorFetcherWatcherOnce(final SpreadsheetComparatorFetcherWatcher watcher) {
        return this.spreadsheetComparatorFetcherWatchers.addOnce(watcher);
    }

    private final SpreadsheetComparatorFetcherWatchers spreadsheetComparatorFetcherWatchers;

    @Override
    public void onSpreadsheetComparatorInfoSet(final SpreadsheetComparatorInfoSet infos) {
        this.spreadsheetComparatorInfoSet = infos;
        this.refreshSpreadsheetProvider();
    }

    private SpreadsheetComparatorInfoSet spreadsheetComparatorInfoSet;

    // ConverterFetcher.................................................................................................

    @Override
    public ConverterFetcher converterFetcher() {
        return this.converterFetcher;
    }

    private final ConverterFetcher converterFetcher;

    @Override
    public Runnable addConverterFetcherWatcher(final ConverterFetcherWatcher watcher) {
        return this.converterFetcherWatchers.add(watcher);
    }

    @Override
    public Runnable addConverterFetcherWatcherOnce(final ConverterFetcherWatcher watcher) {
        return this.converterFetcherWatchers.addOnce(watcher);
    }

    private final ConverterFetcherWatchers converterFetcherWatchers;

    @Override
    public void onConverterInfoSet(final ConverterInfoSet infos) {
        this.converterInfoSet = infos;
        this.refreshSpreadsheetProvider();
    }

    private ConverterInfoSet converterInfoSet;

    @Override
    public void onVerify(final SpreadsheetId id,
                         final SpreadsheetMetadataPropertyName<ConverterSelector> metadataPropertyName,
                         final Set<MissingConverter> missingConverters) {
        // NOP
    }

    // CurrencyWatcher..................................................................................................

    @Override
    public CurrencyFetcher currencyFetcher() {
        return this.currencyFetcher;
    }

    private final CurrencyFetcher currencyFetcher;

    @Override
    public Runnable addCurrencyFetcherWatcher(final CurrencyFetcherWatcher watcher) {
        return this.currencyFetcherWatchers.add(watcher);
    }

    @Override
    public Runnable addCurrencyFetcherWatcherOnce(final CurrencyFetcherWatcher watcher) {
        return this.currencyFetcherWatchers.addOnce(watcher);
    }

    private final CurrencyFetcherWatchers currencyFetcherWatchers;

    @Override
    public void onCurrencyHateosResource(final CurrencyCode id,
                                         final CurrencyHateosResource currency) {
        // NOP
    }

    /**
     * Save the loaded currencies. These will appear in the {@link CurrencyComponent}.
     */
    @Override
    public void onCurrencyHateosResourceSet(final CurrencyHateosResourceSet currencies) {
        // NOP
    }

    // CurrencyContextDelegator.........................................................................................

    @Override
    public CurrencyContext currencyContext() {
        return CURRENCY_CONTEXT;
    }

    @Override
    public Optional<Currency> currencyForCurrencyCode(final String currencyCode) {
        return this.currencyContext()
            .currencyForCurrencyCode(currencyCode);
    }

    @Override
    public Optional<Currency> currencyForLocale(final Locale locale) {
        return this.currencyContext()
            .currencyForLocale(locale);
    }

    // SpreadsheetDeltaFetcher..........................................................................................

    @Override
    public SpreadsheetDeltaFetcher spreadsheetDeltaFetcher() {
        return this.spreadsheetDeltaFetcher;
    }

    private final SpreadsheetDeltaFetcher spreadsheetDeltaFetcher;

    @Override
    public HasSpreadsheetDeltaFetcherWatchers hasSpreadsheetDeltaFetcherWatchers() {
        return this.spreadsheetDeltaFetcherWatchers;
    }

    private final SpreadsheetDeltaFetcherWatchers spreadsheetDeltaFetcherWatchers;

    @Override
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta) {
        // Updates the anchoredSpreadsheetSelection of the local Metadata.
        // This will prevent a PATCH of the server metadata when the history token anchoredSpreadsheetSelection changes, which
        // is fine because it was already updated when the delta above was returned.
        //
        // this will prevent looping where multiple metadata/deltas happen and each overwrites the previous.
        //
        // we only update the anchoredSpreadsheetMetadata because some metadata GETS such as load metadata will not
        // have the window property (unnecessary to calculate and return).
        delta.viewport()
            .ifPresent(
                newV -> {
                    final SpreadsheetMetadata metadata = this.spreadsheetMetadata;
                    this.spreadsheetMetadata = metadata.set(
                        SpreadsheetMetadataPropertyName.VIEWPORT_HOME,
                        newV.rectangle().home()
                    ).setOrRemove(
                        SpreadsheetMetadataPropertyName.VIEWPORT_SELECTION,
                        newV.anchoredSelection()
                            .orElse(null)
                    );
                }
            );
    }

    // DateTimeSymbolsFetcher...........................................................................................

    @Override
    public DateTimeSymbolsFetcher dateTimeSymbolsFetcher() {
        return this.dateTimeSymbolsFetcher;
    }

    private final DateTimeSymbolsFetcher dateTimeSymbolsFetcher;

    @Override
    public Runnable addDateTimeSymbolsFetcherWatcher(final DateTimeSymbolsFetcherWatcher watcher) {
        return this.dateTimeSymbolsFetcherWatchers.add(watcher);
    }

    private final DateTimeSymbolsFetcherWatchers dateTimeSymbolsFetcherWatchers;

    @Override
    public Runnable addDateTimeSymbolsFetcherWatcherOnce(final DateTimeSymbolsFetcherWatcher watcher) {
        return this.dateTimeSymbolsFetcherWatchers.addOnce(watcher);
    }

    @Override
    public void onDateTimeSymbolsHateosResource(final LocaleLanguageTag id,
                                                final DateTimeSymbolsHateosResource dateTimeSymbols) {
        // NOP
    }

    @Override
    public void onDateTimeSymbolsHateosResourceSet(final String localeStartsWith,
                                                   final DateTimeSymbolsHateosResourceSet symbols) {
        // NOP
    }

    // DecimalNumberSymbolsFetcher...........................................................................................

    @Override
    public DecimalNumberSymbolsFetcher decimalNumberSymbolsFetcher() {
        return this.decimalNumberSymbolsFetcher;
    }

    private final DecimalNumberSymbolsFetcher decimalNumberSymbolsFetcher;

    @Override
    public Runnable addDecimalNumberSymbolsFetcherWatcher(final DecimalNumberSymbolsFetcherWatcher watcher) {
        return this.decimalNumberSymbolsFetcherWatchers.add(watcher);
    }

    private final DecimalNumberSymbolsFetcherWatchers decimalNumberSymbolsFetcherWatchers;

    @Override
    public Runnable addDecimalNumberSymbolsFetcherWatcherOnce(final DecimalNumberSymbolsFetcherWatcher watcher) {
        return this.decimalNumberSymbolsFetcherWatchers.addOnce(watcher);
    }

    @Override
    public void onDecimalNumberSymbolsHateosResource(final LocaleLanguageTag id,
                                                     final DecimalNumberSymbolsHateosResource decimalNumberSymbols) {
        // NOP
    }

    @Override
    public void onDecimalNumberSymbolsHateosResourceSet(final String localeStartsWith,
                                                        final DecimalNumberSymbolsHateosResourceSet decimalNumberSymbolsSet) {
        // NOP
    }

    // EnvironmentContext...............................................................................................

    @Override
    public AppContext cloneEnvironment() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AppContext setEnvironmentContext(final EnvironmentContext environmentContext) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> void setEnvironmentValue(final EnvironmentValueName<T> name,
                                        final T value) {
        this.spreadsheetEnvironmentContext()
            .setEnvironmentValue(
                name,
                value
            );
    }

    @Override
    public void removeEnvironmentValue(final EnvironmentValueName<?> name) {
        this.spreadsheetEnvironmentContext()
            .removeEnvironmentValue(name);
    }

    @Override
    public Currency currency() {
        return CURRENCY;
    }

    @Override
    public void setCurrency(final Currency currency) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<StoragePath> currentWorkingDirectory() {
        return this.spreadsheetEnvironmentContext().currentWorkingDirectory();
    }

    @Override
    public void setCurrentWorkingDirectory(final Optional<StoragePath> currentWorkingDirectory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<StoragePath> homeDirectory() {
        return this.spreadsheetEnvironmentContext().homeDirectory();
    }

    @Override
    public void setHomeDirectory(final Optional<StoragePath> homeDirectory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Indentation indentation() {
        return this.spreadsheetEnvironmentContext()
            .indentation();
    }

    @Override
    public void setIndentation(final Indentation indentation) {
        this.spreadsheetEnvironmentContext()
            .setIndentation(indentation);
    }

    @Override
    public LineEnding lineEnding() {
        return this.spreadsheetEnvironmentContext()
            .lineEnding();
    }

    @Override
    public Locale locale() {
        return this.spreadsheetEnvironmentContext()
            .locale();
    }

    @Override
    public void setLocale(final Locale locale) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LocalDateTime now() {
        return NOW.now();
    }

    @Override
    public AbsoluteUrl serverUrl() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSpreadsheetId(final Optional<SpreadsheetId> spreadsheetId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setUser(final Optional<EmailAddress> user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetEnvironmentContext environmentContext() {
        return this.spreadsheetEnvironmentContext;
    }

    @Override
    public SpreadsheetEnvironmentContext spreadsheetEnvironmentContext() {
        return this.spreadsheetEnvironmentContext;
    }

    private SpreadsheetEnvironmentContext spreadsheetEnvironmentContext;

    // SpreadsheetExporterFetcher.......................................................................................

    @Override
    public SpreadsheetExporterFetcher spreadsheetExporterFetcher() {
        return this.spreadsheetExporterFetcher;
    }

    private final SpreadsheetExporterFetcher spreadsheetExporterFetcher;

    @Override
    public Runnable addSpreadsheetExporterFetcherWatcher(final SpreadsheetExporterFetcherWatcher watcher) {
        return this.spreadsheetExporterFetcherWatchers.add(watcher);
    }

    @Override
    public Runnable addSpreadsheetExporterFetcherWatcherOnce(final SpreadsheetExporterFetcherWatcher watcher) {
        return this.spreadsheetExporterFetcherWatchers.addOnce(watcher);
    }

    private final SpreadsheetExporterFetcherWatchers spreadsheetExporterFetcherWatchers;

    @Override
    public void onSpreadsheetExporterInfoSet(final SpreadsheetExporterInfoSet infos) {
        this.spreadsheetExporterInfoSet = infos;
        this.refreshSpreadsheetProvider();
    }

    private SpreadsheetExporterInfoSet spreadsheetExporterInfoSet;

    // ExpressionFunctionFetcher........................................................................................

    @Override
    public ExpressionFunctionFetcher expressionFunctionFetcher() {
        return this.expressionFunctionFetcher;
    }

    private final ExpressionFunctionFetcher expressionFunctionFetcher;

    @Override
    public Runnable addExpressionFunctionFetcherWatcher(final ExpressionFunctionFetcherWatcher watcher) {
        return this.expressionFunctionFetcherWatchers.add(watcher);
    }

    @Override
    public Runnable addExpressionFunctionFetcherWatcherOnce(final ExpressionFunctionFetcherWatcher watcher) {
        return this.expressionFunctionFetcherWatchers.addOnce(watcher);
    }

    private final ExpressionFunctionFetcherWatchers expressionFunctionFetcherWatchers;

    @Override
    public void onExpressionFunctionInfoSet(final ExpressionFunctionInfoSet infos) {
        this.expressionFunctionInfoSet = infos;
        this.refreshSpreadsheetProvider();
    }

    private ExpressionFunctionInfoSet expressionFunctionInfoSet;

    // Fetcher..........................................................................................................

    @Override
    public void onBegin(final HttpMethod method,
                        final Url url,
                        final Optional<FetcherRequestBody<?>> body) {
        // NOP
    }

    @Override
    public void onFailure(final HttpMethod method,
                          final AbsoluteOrRelativeUrl url,
                          final HttpStatus status,
                          final Headers headers,
                          final String body) {
        if (CharSequences.isNullOrEmpty(body)) {
            this.error(method + " " + url + " " + status);
        } else {
            this.error(method + " " + url + " " + status, body);
        }
    }

    @Override
    public void onError(final Object cause) {
        this.error(cause);
    }

    // SpreadsheetFormatterFetcher......................................................................................

    @Override
    public SpreadsheetFormatterFetcher spreadsheetFormatterFetcher() {
        return this.spreadsheetFormatterFetcher;
    }

    private final SpreadsheetFormatterFetcher spreadsheetFormatterFetcher;

    @Override
    public Runnable addSpreadsheetFormatterFetcherWatcher(final SpreadsheetFormatterFetcherWatcher watcher) {
        return this.spreadsheetFormatterFetcherWatchers.add(watcher);
    }

    @Override
    public Runnable addSpreadsheetFormatterFetcherWatcherOnce(final SpreadsheetFormatterFetcherWatcher watcher) {
        return this.spreadsheetFormatterFetcherWatchers.addOnce(watcher);
    }

    private final SpreadsheetFormatterFetcherWatchers spreadsheetFormatterFetcherWatchers;

    @Override
    public void onSpreadsheetFormatterInfoSet(final SpreadsheetFormatterInfoSet infos) {
        this.spreadsheetFormatterInfoSet = infos;
        this.refreshSpreadsheetProvider();
    }

    private SpreadsheetFormatterInfoSet spreadsheetFormatterInfoSet;

    @Override
    public void onSpreadsheetFormatterSelectorEdit(final SpreadsheetId id,
                                                   final Optional<SpreadsheetExpressionReference> cellOrLabel,
                                                   final SpreadsheetFormatterSelectorEdit edit) {
        // nop
    }

    @Override
    public void onSpreadsheetFormatterMenuList(final SpreadsheetId id,
                                               final SpreadsheetExpressionReference cellOrLabel,
                                               final SpreadsheetFormatterMenuList menu) {
        // nop
    }

    // SpreadsheetFormatterContext......................................................................................

    @Override
    public ConverterLike converterLike() {
        return this.spreadsheetFormatterContext(); // prioritize SpreadsheetFormatterContext over ProviderContext
    }

    @Override
    public int generalFormatNumberDigitCount() {
        return this.spreadsheetMetadata.getOrFail(SpreadsheetMetadataPropertyName.DECIMAL_NUMBER_DIGIT_COUNT);
    }

    @Override
    public SpreadsheetFormatterContext spreadsheetFormatterContext() {
        return this.formatterContext;
    }

    /**
     * A new {@link SpreadsheetFormatterContext} is created each time a new {@link SpreadsheetMetadata} arrives.
     */
    private SpreadsheetFormatterContext formatterContext;

    // FormHandlerFetcher...............................................................................................

    @Override
    public FormHandlerFetcher formHandlerFetcher() {
        return this.formHandlerFetcher;
    }

    private final FormHandlerFetcher formHandlerFetcher;

    @Override
    public Runnable addFormHandlerFetcherWatcher(final FormHandlerFetcherWatcher watcher) {
        return this.formHandlerFetcherWatchers.add(watcher);
    }

    @Override
    public Runnable addFormHandlerFetcherWatcherOnce(final FormHandlerFetcherWatcher watcher) {
        return this.formHandlerFetcherWatchers.addOnce(watcher);
    }

    private final FormHandlerFetcherWatchers formHandlerFetcherWatchers;

    @Override
    public void onFormHandlerInfo(final FormHandlerInfo info) {
        // NOP
    }

    @Override
    public void onFormHandlerInfoSet(final FormHandlerInfoSet infos) {
        this.formHandlerInfoSet = infos;
        this.refreshSpreadsheetProvider();
    }

    private FormHandlerInfoSet formHandlerInfoSet;

    // HistoryContextDelegator..........................................................................................

    @Override
    public HistoryContext historyContext() {
        return this.apphistoryContextHistoryTokenWatcher;
    }

    private final AppHistoryContextHistoryWatcher apphistoryContextHistoryTokenWatcher;

    // SpreadsheetImporterFetcher.......................................................................................

    @Override
    public SpreadsheetImporterFetcher spreadsheetImporterFetcher() {
        return this.spreadsheetImporterFetcher;
    }

    private final SpreadsheetImporterFetcher spreadsheetImporterFetcher;

    @Override
    public Runnable addSpreadsheetImporterFetcherWatcher(final SpreadsheetImporterFetcherWatcher watcher) {
        return this.spreadsheetImporterFetcherWatchers.add(watcher);
    }

    private final SpreadsheetImporterFetcherWatchers spreadsheetImporterFetcherWatchers;

    @Override
    public Runnable addSpreadsheetImporterFetcherWatcherOnce(final SpreadsheetImporterFetcherWatcher watcher) {
        return this.spreadsheetImporterFetcherWatchers.addOnce(watcher);
    }

    @Override
    public void onSpreadsheetImporterInfoSet(final SpreadsheetImporterInfoSet infos) {
        this.spreadsheetImporterInfoSet = infos;
        this.refreshSpreadsheetProvider();
    }

    private SpreadsheetImporterInfoSet spreadsheetImporterInfoSet;

    // JsonNodeMarshallContext..........................................................................................

    @Override
    public JsonNodeMarshallContext jsonNodeMarshallContext() {
        return MARSHALL_CONTEXT;
    }

    private final static JsonNodeMarshallContext MARSHALL_CONTEXT = JsonNodeMarshallContexts.basic();

    /**
     * The {@link JsonNodeUnmarshallContext} will be updated each time a new {@link SpreadsheetMetadata} is received.
     */
    @Override
    public JsonNodeUnmarshallContext jsonNodeUnmarshallContext() {
        return this.unmarshallContext;
    }

    private JsonNodeUnmarshallContext unmarshallContext;

    @Override
    public JsonNodeContext jsonNodeContext() {
        return MARSHALL_CONTEXT;
    }

    // KeyboardContext..................................................................................................

    @Override
    public Optional<SpreadsheetCell> historyTokenCell() {
        return this.spreadsheetViewportCache()
            .historyTokenCell();
    }

    // LocaleContextDelegator...........................................................................................

    @Override
    public Set<Locale> availableLocales() {
        return this.availableLocales;
    }

    /**
     * Will be replaced when the server replies with all available locales.
     */
    private Set<Locale> availableLocales = Sets.of(LOCALE);

    @Override
    public Set<Locale> findByLocaleText(final String text,
                                        final int offset,
                                        final int count) {
        return this.localeToText.entrySet()
            .stream()
            .filter(localeAndText -> {
                final String localeText = localeAndText.getValue();
                return false == localeText.isEmpty() &&
                    (LocaleContexts.CASE_SENSITIVITY.equals(text, localeText) || LocaleContexts.CASE_SENSITIVITY.startsWith(localeText, text));
            }).skip(offset)
            .limit(count)
            .map(Entry::getKey)
            .collect(
                ImmutableSortedSet.collector(LocaleContexts.LANGUAGE_TAG_COMPARATOR)
            );
    }

    @Override
    public Optional<String> localeText(final Locale locale) {
        Objects.requireNonNull(locale, "locale");

        return Optional.ofNullable(
            this.localeToText.get(locale)
        );
    }

    private Map<Locale, String> localeToText = Maps.empty();

    @Override
    public Optional<DateTimeSymbols> dateTimeSymbolsForLocale(final Locale locale) {
        return this.localeContext()
            .dateTimeSymbolsForLocale(locale);
    }

    @Override
    public Optional<DecimalNumberSymbols> decimalNumberSymbolsForLocale(final Locale locale) {
        return this.localeContext()
            .decimalNumberSymbolsForLocale(locale);
    }

    @Override
    public Optional<Locale> localeForLanguageTag(final String languageTag) {
        return this.localeContext()
            .localeForLanguageTag(languageTag);
    }

    @Override
    public LocaleContext localeContext() {
        return LOCALE_CONTEXT;
    }

    // LocaleFetcher....................................................................................................

    @Override
    public LocaleFetcher localeFetcher() {
        return this.localeFetcher;
    }

    private final LocaleFetcher localeFetcher;

    @Override
    public Runnable addLocaleFetcherWatcher(final LocaleFetcherWatcher watcher) {
        return this.localeFetcherWatchers.add(watcher);
    }

    private final LocaleFetcherWatchers localeFetcherWatchers;

    @Override
    public Runnable addLocaleFetcherWatcherOnce(final LocaleFetcherWatcher watcher) {
        return this.localeFetcherWatchers.addOnce(watcher);
    }

    @Override
    public void onLocaleHateosResource(final LocaleLanguageTag id,
                                       final LocaleHateosResource locale) {
        // NOP
    }

    /**
     * Save the loaded locales. These will appear in the {@link LocaleComponent}.
     */
    @Override
    public void onLocaleHateosResourceSet(final LocaleHateosResourceSet locales) {
        final Set<Locale> availableLocales = Sets.hash();
        final Map<Locale, String> localeToText = Maps.hash();

        for (final LocaleHateosResource localeHateosResource : locales) {
            final Locale locale = Locale.forLanguageTag(
                localeHateosResource.value()
                    .value()
            );

            availableLocales.add(locale);

            localeToText.put(
                locale,
                localeHateosResource.text()
            );
        }

        this.availableLocales = Sets.readOnly(availableLocales);
        this.localeToText = localeToText;
    }

    // LoggingContext...................................................................................................

    @Override
    public LoggingContext loggingContext() {
        return this.loggingContext;
    }

    private final LoggingContext loggingContext;

    // SpreadsheetMetadataFetcher.......................................................................................

    @Override
    public SpreadsheetMetadataFetcher spreadsheetMetadataFetcher() {
        return this.spreadsheetMetadataFetcher;
    }

    private final SpreadsheetMetadataFetcher spreadsheetMetadataFetcher;

    @Override
    public HasSpreadsheetMetadataFetcherWatchers hasSpreadsheetMetadataFetcherWatchers() {
        return this.metadataFetcherWatchers;
    }

    private final SpreadsheetMetadataFetcherWatchers metadataFetcherWatchers;

    /**
     * Update the spreadsheet-id, spreadsheet-name and viewport selection from the given {@link SpreadsheetMetadata}.
     */
    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata) {
        final AppContext context = this;

        // SKIP spreadsheet id change if SpreadsheetListRenameHistoryToken
        if (false == context.historyToken() instanceof SpreadsheetListRenameHistoryToken) {
            final SpreadsheetMetadata previousMetadata = this.spreadsheetMetadata;
            this.spreadsheetMetadata = metadata;

            this.refreshSpreadsheetProvider();

            // update the global JsonNodeUnmarshallContext.
            this.unmarshallContext = JsonNodeUnmarshallContexts.basic(
                CURRENCY_CONTEXT, // CanCurrencyForCurrencyCode
                LOCALE_CONTEXT,
                metadata.expressionNumberKind(),
                metadata.mathContext()
            );

            // SpreasdsheetMetadata#spreasheetEnvironmentContext will unwrap previous
            final SpreadsheetEnvironmentContext spreadsheetEnvironmentContext = metadata.spreadsheetEnvironmentContext(
                this.spreadsheetEnvironmentContext
            );

            this.providerContext = SpreadsheetProviderContexts.spreadsheet(
                PluginStores.fake(),
                this, // CurrencyLocaleContext
                spreadsheetEnvironmentContext,
                this.jsonNodeMarshallUnmarshallContext()
            );

            final Optional<SpreadsheetId> maybeId = metadata.id();
            final Optional<SpreadsheetName> maybeName = metadata.name();

            if (maybeId.isPresent() && maybeName.isPresent()) {
                final SpreadsheetId id = maybeId.get();
                final SpreadsheetName name = maybeName.get();

                final HistoryToken historyToken = context.historyToken();
                final Optional<AnchoredSpreadsheetSelection> anchoredSpreadsheetSelection = metadata.get(SpreadsheetMetadataPropertyName.VIEWPORT_SELECTION);

                final HistoryToken idNameSelectionHistoryToken = historyToken
                    .setIdAndName(
                        id,
                        name
                    ).setAnchoredSelection(anchoredSpreadsheetSelection);

                if (false == historyToken.equals(idNameSelectionHistoryToken)) {
                    context.debug("App.onSpreadsheetMetadata from " + historyToken + " to different id/name/anchoredSelection " + idNameSelectionHistoryToken, metadata);
                    context.pushHistoryToken(idNameSelectionHistoryToken);
                } else {
                    // must have loaded a new spreadsheet, need to fire history token
                    //
                    // eg so a focused cell is given focus etc.
                    if (false == id.equals(
                        previousMetadata.id()
                            .orElse(null)
                    )) {
                        context.debug("App.onSpreadsheetMetadata new spreadsheet " + id + " loaded, firing history token again");

                        context.fireCurrentHistoryToken();

                        // need to also load all PluginInfoSetLikes...as they are also used to build menus etc.
                        context.spreadsheetComparatorFetcher()
                            .getInfoSet();
                        context.converterFetcher()
                            .getInfoSet();
                        context.spreadsheetExporterFetcher()
                            .getInfoSet();
                        context.expressionFunctionFetcher()
                            .getInfoSet();
                        context.spreadsheetFormatterFetcher()
                            .getInfoSet();
                        context.spreadsheetImporterFetcher()
                            .getInfoSet();
                        context.spreadsheetParserFetcher()
                            .getInfoSet();
                    }
                }
            }
        }
    }

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas) {
        // IGNORE
    }

    /**
     * Returns the current or last loaded {@link SpreadsheetMetadata}
     */
    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        return this.spreadsheetMetadata;
    }

    private SpreadsheetMetadata spreadsheetMetadata;

    @Override
    public ExpressionNumberKind expressionNumberKind() {
        return this.spreadsheetMetadata.expressionNumberKind();
    }

    @Override
    public MathContext mathContext() {
        return this.spreadsheetMetadata.mathContext();
    }

    // SpreadsheetParserContext.........................................................................................

    @Override
    public boolean canNumbersHaveGroupSeparator() {
        return this.parserContext.canNumbersHaveGroupSeparator();
    }

    @Override
    public InvalidCharacterException invalidCharacterException(final Parser<?> parser,
                                                               final TextCursor cursor) {
        return this.parserContext.invalidCharacterException(
            parser,
            cursor
        );
    }

    @Override
    public char valueSeparator() {
        return this.parserContext.valueSeparator();
    }

    private SpreadsheetParserContext parserContext;

    // SpreadsheetParserFetcher..........................................................................................

    @Override
    public SpreadsheetParserFetcher spreadsheetParserFetcher() {
        return this.spreadsheetParserFetcher;
    }

    private final SpreadsheetParserFetcher spreadsheetParserFetcher;

    @Override
    public Runnable addSpreadsheetParserFetcherWatcher(final SpreadsheetParserFetcherWatcher watcher) {
        return this.spreadsheetParserFetcherWatchers.add(watcher);
    }

    @Override
    public Runnable addSpreadsheetParserFetcherWatcherOnce(final SpreadsheetParserFetcherWatcher watcher) {
        return this.spreadsheetParserFetcherWatchers.addOnce(watcher);
    }

    private final SpreadsheetParserFetcherWatchers spreadsheetParserFetcherWatchers;

    @Override
    public void onSpreadsheetParserInfoSet(final SpreadsheetParserInfoSet infos) {
        this.spreadsheetParserInfoSet = infos;
        this.refreshSpreadsheetProvider();
    }

    private SpreadsheetParserInfoSet spreadsheetParserInfoSet;

    @Override
    public void onSpreadsheetParserSelectorEdit(final SpreadsheetId id,
                                                final SpreadsheetParserSelectorEdit edit) {
        // nop
    }

    // PluginFetcher....................................................................................................

    @Override
    public PluginFetcher pluginFetcher() {
        return this.pluginFetcher;
    }

    private final PluginFetcher pluginFetcher;

    @Override
    public HasPluginFetcherWatchers hasPluginFetcherWatchers() {
        return this.pluginFetcherWatchers;
    }

    private final PluginFetcherWatchers pluginFetcherWatchers;

    @Override
    public void onJarEntryInfoList(final PluginName name,
                                   final Optional<JarEntryInfoList> list) {
        // NOP
    }

    @Override
    public void onJarEntryInfoName(final PluginName pluginName,
                                   final Optional<JarEntryInfoName> filename,
                                   final Optional<String> body) {
        // NOP
    }

    @Override
    public void onPlugin(final PluginName name,
                         final Optional<Plugin> plugin) {
        // NOP
    }

    @Override
    public void onPluginSet(final PluginSet plugins) {
        // TODO
    }

    // ProviderContext..................................................................................................

    @Override
    public ProviderContext providerContext() {
        return this.providerContext;
    }

    private ProviderContext providerContext;

    // RecentValueSavesContextDelegator.................................................................................

    @Override
    public RecentValueSavesContext recentValueSavesContext() {
        return this.recentValueSavesContext;
    }

    private final RecentValueSavesContext recentValueSavesContext;

    // SpreadsheetProvider..............................................................................................

    @Override
    public SpreadsheetProvider spreadsheetProvider() {
        return this.spreadsheetProvider;
    }

    @Override
    public SpreadsheetProvider systemSpreadsheetProvider() {
        return this.systemSpreadsheetProvider;
    }

    private SpreadsheetProvider systemSpreadsheetProvider;

    private void refreshSpreadsheetProvider() {
        this.providerContext = SpreadsheetProviderContexts.spreadsheet(
            PluginStores.fake(),
            this, // CurrencyLocaleContext
            this.environmentContext(),
            this.jsonNodeMarshallUnmarshallContext()
        );

        final SpreadsheetMetadata metadata = this.spreadsheetMetadata();

        final SpreadsheetComparatorProvider spreadsheetComparatorProvider = SpreadsheetComparatorProviders.mergedMapped(
            this.spreadsheetComparatorInfoSet.renameIfPresent(
                SpreadsheetComparatorInfoSet.EMPTY
            ),
            SpreadsheetComparatorProviders.spreadsheetComparators()
        );

        final SpreadsheetExporterProvider spreadsheetExporterProvider = SpreadsheetExporterProviders.mergedMapped(
            this.spreadsheetExporterInfoSet.renameIfPresent(
                SpreadsheetExporterInfoSet.EMPTY
            ),
            SpreadsheetExporterProviders.empty()
        );

        final ExpressionFunctionProvider<SpreadsheetExpressionEvaluationContext> expressionFunctionProvider = ExpressionFunctionProviders.mergedMapped(
            this.expressionFunctionInfoSet.renameIfPresent(
                SpreadsheetExpressionFunctions.EMPTY_INFO_SET
            ),
            ExpressionFunctionProviders.empty(SpreadsheetExpressionFunctions.NAME_CASE_SENSITIVITY) // TODO should have a non empty EFP
        );

        final SpreadsheetFormatterProvider spreadsheetFormatterProvider = SpreadsheetFormatterProviders.mergedMapped(
            this.spreadsheetFormatterInfoSet.renameIfPresent(
                SpreadsheetFormatterInfoSet.EMPTY
            ),
            SpreadsheetFormatterProviders.spreadsheetFormatters()
        );

        final FormHandlerProvider formHandlerProvider = FormHandlerProviders.mergedMapped(
            this.formHandlerInfoSet.renameIfPresent(
                FormHandlerInfoSet.EMPTY
            ),
            FormHandlerProviders.validation()
        );

        final SpreadsheetImporterProvider spreadsheetImporterProvider = SpreadsheetImporterProviders.mergedMapped(
            this.spreadsheetImporterInfoSet.renameIfPresent(
                SpreadsheetImporterInfoSet.EMPTY
            ),
            SpreadsheetImporterProviders.empty()
        );

        final SpreadsheetParserProvider spreadsheetParserProvider = SpreadsheetParserProviders.mergedMapped(
            this.spreadsheetParserInfoSet.renameIfPresent(
                SpreadsheetParserInfoSet.EMPTY
            ),
            SpreadsheetParserProviders.spreadsheetParsePattern(spreadsheetFormatterProvider)
        );

        final ConverterProvider converterProvider = ConverterProviders.mergedMapped(
            this.converterInfoSet.renameIfPresent(
                ConverterInfoSet.EMPTY
            ),
            SpreadsheetConvertersConverterProviders.spreadsheetConverters(
                (ProviderContext p) -> metadata.dateTimeConverter(
                    spreadsheetFormatterProvider,
                    spreadsheetParserProvider,
                    p
                )
            )
        );

        final ValidatorProvider validatorProvider = ValidatorProviders.mergedMapped(
            this.validatorInfoSet.renameIfPresent(
                ValidatorInfoSet.EMPTY
            ),
            ValidatorProviders.validators()
        );

        this.spreadsheetProvider = metadata.spreadsheetProvider(
            SpreadsheetProviders.basic(
                converterProvider,
                expressionFunctionProvider,
                spreadsheetComparatorProvider,
                spreadsheetExporterProvider,
                spreadsheetFormatterProvider,
                formHandlerProvider,
                spreadsheetImporterProvider,
                spreadsheetParserProvider,
                validatorProvider
            )
        );
        this.systemSpreadsheetProvider = SpreadsheetProviders.basic(
            converterProvider,
            expressionFunctionProvider,
            spreadsheetComparatorProvider,
            spreadsheetExporterProvider,
            spreadsheetFormatterProvider,
            formHandlerProvider,
            spreadsheetImporterProvider,
            spreadsheetParserProvider,
            validatorProvider
        );

        try {
            this.formatterContext = metadata.spreadsheetFormatterContext(
                SpreadsheetMetadata.NO_CELL,
                (final Optional<Object> value) -> {
                    throw new UnsupportedOperationException();
                },
                this, // HasUserDirectories
                this.indentation(),
                this.viewportCache, // SpreadsheetLabelNameResolver
                this.lineEnding(),
                this, // CurrencyLocaleContext
                this.systemSpreadsheetProvider,
                this.providerContext // ProviderContext
            );
        } catch (final RuntimeException cause) {
            this.warn("App.refreshSpreadsheetProvider Failed to create SpreadsheetFormatterContext=" + cause.getMessage(), cause);
            this.formatterContext = SpreadsheetFormatterContexts.fake();
        }

        try {
            this.parserContext = metadata.spreadsheetParserContext(
                SpreadsheetMetadata.NO_CELL,
                this, // LocaleContext
                this // HasNow
            );
        } catch (final RuntimeException cause) {
            this.warn("App.refreshSpreadsheetProvider Failed to create SpreadsheetParserContext=" + cause.getMessage(), cause);
            this.parserContext = SpreadsheetParserContexts.fake();
        }
    }

    /**
     * This will be updated every time anytime a new {@link SpreadsheetMetadata} or any of its component are received by
     * a onXXX watcher method.
     */
    private SpreadsheetProvider spreadsheetProvider;

    // HasValidatorFetcher..............................................................................................

    @Override
    public ValidatorFetcher validatorFetcher() {
        return this.validatorFetcher;
    }

    private final ValidatorFetcher validatorFetcher;

    @Override
    public Runnable addValidatorFetcherWatcher(final ValidatorFetcherWatcher watcher) {
        return this.validatorFetcherWatchers.add(watcher);
    }

    @Override
    public Runnable addValidatorFetcherWatcherOnce(final ValidatorFetcherWatcher watcher) {
        return this.validatorFetcherWatchers.addOnce(watcher);
    }

    private final ValidatorFetcherWatchers validatorFetcherWatchers;

    @Override
    public void onValidatorInfo(final ValidatorInfo info) {
        // NOP
    }

    @Override
    public void onValidatorInfoSet(final ValidatorInfoSet infos) {
        this.validatorInfoSet = infos;
        this.refreshSpreadsheetProvider();
    }

    private ValidatorInfoSet validatorInfoSet;

    // SpreadsheetViewport..............................................................................................

    @Override
    public void reload() {
        this.viewportComponent.loadViewportCells();
    }

    @Override
    public SpreadsheetViewport viewport(final Optional<AnchoredSpreadsheetSelection> selection) {
        return this.viewportComponent.viewport(selection);
    }

    /**
     * Init here to avoid race conditions with other fields like {@link #metadataFetcherWatchers}.
     */
    private final SpreadsheetViewportComponent viewportComponent;

    // SpreadsheetViewportCache.........................................................................................

    /**
     * Cache for the contents of the viewport.
     */
    @Override
    public SpreadsheetViewportCache spreadsheetViewportCache() {
        return this.viewportCache;
    }

    private final SpreadsheetViewportCache viewportCache;
}
