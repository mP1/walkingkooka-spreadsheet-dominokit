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
import walkingkooka.convert.CanConvert;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.provider.ConverterInfoSet;
import walkingkooka.convert.provider.ConverterProvider;
import walkingkooka.convert.provider.ConverterProviders;
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.environment.EnvironmentContexts;
import walkingkooka.j2cl.locale.LocaleAware;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.plugin.PluginName;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContextDelegator;
import walkingkooka.plugin.ProviderContexts;
import walkingkooka.plugin.store.Plugin;
import walkingkooka.plugin.store.PluginSet;
import walkingkooka.plugin.store.PluginStores;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfoSet;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorProvider;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorProviders;
import walkingkooka.spreadsheet.convert.SpreadsheetConvertersConverterProviders;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardContext;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardContextReadWatcher;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardContextWriteWatcher;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardContexts;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardTextItem;
import walkingkooka.spreadsheet.dominokit.fetcher.ConverterFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.ConverterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.ConverterFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.ExpressionFunctionFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.ExpressionFunctionFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.ExpressionFunctionFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.FetcherRequestBody;
import walkingkooka.spreadsheet.dominokit.fetcher.HasPluginFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasPluginFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchersDelegator;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcherWatchersDelegator;
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
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;
import walkingkooka.spreadsheet.dominokit.log.LoggingContexts;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportComponent;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportComponentContexts;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.export.SpreadsheetExporterInfoSet;
import walkingkooka.spreadsheet.export.SpreadsheetExporterProvider;
import walkingkooka.spreadsheet.export.SpreadsheetExporterProviders;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContextDelegator;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterContexts;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterInfoSet;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterProvider;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterProviders;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterInfoSet;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterProvider;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterProviders;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.parser.SpreadsheetParserContext;
import walkingkooka.spreadsheet.parser.SpreadsheetParserContexts;
import walkingkooka.spreadsheet.parser.SpreadsheetParserInfoSet;
import walkingkooka.spreadsheet.parser.SpreadsheetParserProvider;
import walkingkooka.spreadsheet.parser.SpreadsheetParserProviders;
import walkingkooka.spreadsheet.provider.SpreadsheetProvider;
import walkingkooka.spreadsheet.provider.SpreadsheetProviderDelegator;
import walkingkooka.spreadsheet.provider.SpreadsheetProviders;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorEdit;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorMenuList;
import walkingkooka.spreadsheet.server.parser.SpreadsheetParserSelectorEdit;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoList;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;
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
import walkingkooka.validation.form.provider.FormHandlerInfoSet;
import walkingkooka.validation.form.provider.FormHandlerProvider;
import walkingkooka.validation.form.provider.FormHandlerProviders;
import walkingkooka.validation.provider.ValidatorInfoSet;
import walkingkooka.validation.provider.ValidatorProvider;
import walkingkooka.validation.provider.ValidatorProviders;

import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Predicate;

@LocaleAware
public class App implements EntryPoint,
    AppContext,
    WindowResizeWatcher,
    HistoryContextDelegator,
    JsonNodeMarshallContextDelegator,
    JsonNodeUnmarshallContextDelegator,
    LoggingContextDelegator,
    NopEmptyResponseFetcherWatcher,
    ProviderContextDelegator,
    SpreadsheetComparatorFetcherWatcher,
    ConverterFetcherWatcher,
    SpreadsheetDeltaFetcherWatcher,
    HasSpreadsheetMetadataFetcherWatchersDelegator,
    HasSpreadsheetDeltaFetcherWatchersDelegator,
    SpreadsheetExporterFetcherWatcher,
    ExpressionFunctionFetcherWatcher,
    SpreadsheetFormatterFetcherWatcher,
    SpreadsheetImporterFetcherWatcher,
    SpreadsheetMetadataFetcherWatcher,
    SpreadsheetParserFetcherWatcher,
    ValidatorFetcherWatcher,
    PluginFetcherWatcher,
    HasPluginFetcherWatchersDelegator,
    SpreadsheetProviderDelegator,
    SpreadsheetFormatterContextDelegator {

    /**
     * When a {@link HistoryTokenWatcher#onHistoryTokenChange(HistoryToken, AppContext)} exceeds this value, it should be considered too slow and a WARN message logged.
     */
    public final static long SLOW_HISTORY_TOKEN_CHANGE = 150;

    public App() {
        SpreadsheetDelta.EMPTY.toString(); // force json register.

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
        this.expressionFunctionInfoSet = ExpressionFunctionInfoSet.EMPTY;
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
        this.formHandlerInfoSet = FormHandlerInfoSet.EMPTY;

        // importer
        this.spreadsheetImporterFetcherWatchers = SpreadsheetImporterFetcherWatchers.empty();
        this.spreadsheetImporterFetcher = SpreadsheetImporterFetcher.with(
            this.spreadsheetImporterFetcherWatchers,
            this
        );
        this.spreadsheetImporterInfoSet = SpreadsheetImporterInfoSet.EMPTY;
        this.addSpreadsheetImporterFetcherWatcher(this);

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

        this.providerContext = ProviderContexts.fake();

        // history
        this.apphistoryContextHistoryTokenWatcher = AppHistoryContextHistoryWatcher.with(this);

        this.viewportCache = SpreadsheetViewportCache.empty(this);

        this.viewportComponent = SpreadsheetViewportComponent.empty(
            SpreadsheetViewportComponentContexts.appContext(this)
        );

        AppSpreadsheetDialogComponents.register(this);

        this.layout = SpreadsheetAppLayout.prepare(
            this.viewportComponent,
            this
        );

        DomGlobal.document.body.append(
            this.layout.element()
        );
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
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        // SKIP spreadsheet id change if SpreadsheetListRenameHistoryToken
        if (false == context.historyToken() instanceof SpreadsheetListRenameHistoryToken) {
            final SpreadsheetMetadata previousMetadata = this.spreadsheetMetadata;
            this.spreadsheetMetadata = metadata;

            this.refreshSpreadsheetProvider();

            // update the global JsonNodeUnmarshallContext.
            this.unmarshallContext = JsonNodeUnmarshallContexts.basic(
                metadata.expressionNumberKind(),
                metadata.mathContext()
            );

            final EnvironmentContext environmentContext = metadata.environmentContext(
                EnvironmentContexts.empty(
                    this,
                    Optional.of(
                        EmailAddress.parse("user123@example.com")
                    )
                )
            );

            // TODO https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/4833
            // App#onSpreadsheetMetadata ProviderContext given ConverterContexts.fake() for CanConvert
            this.providerContext = ProviderContexts.basic(
                ConverterContexts.fake(),
                environmentContext,
                PluginStores.fake()
            );

            final Optional<SpreadsheetId> maybeId = metadata.id();
            final Optional<SpreadsheetName> maybeName = metadata.name();

            if (maybeId.isPresent() && maybeName.isPresent()) {
                final SpreadsheetId id = maybeId.get();
                final SpreadsheetName name = maybeName.get();

                final HistoryToken historyToken = context.historyToken();
                final Optional<SpreadsheetViewport> viewport = metadata.get(SpreadsheetMetadataPropertyName.VIEWPORT);

                final HistoryToken idNameSelectionHistoryToken = historyToken
                    .setIdAndName(
                        id,
                        name
                    ).setAnchoredSelection(
                        viewport.flatMap(SpreadsheetViewport::anchoredSelection)
                    );

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
                        context.converterFetcher()
                            .infoSet(id);
                        context.spreadsheetComparatorFetcher()
                            .infoSet(id);
                        context.spreadsheetExporterFetcher()
                            .infoSet(id);
                        context.expressionFunctionFetcher()
                            .infoSet(id);
                        context.spreadsheetFormatterFetcher()
                            .infoSet(id);
                        context.spreadsheetImporterFetcher()
                            .infoSet(id);
                        context.spreadsheetParserFetcher()
                            .infoSet(id);
                    }
                }
            }
        }
    }

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas,
                                         final AppContext context) {
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
                                   final SpreadsheetDelta delta,
                                   final AppContext context) {
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
                    this.spreadsheetMetadata = metadata.setOrRemove(
                        SpreadsheetMetadataPropertyName.VIEWPORT,
                        metadata.get(SpreadsheetMetadataPropertyName.VIEWPORT)
                            .map(
                                oldV -> oldV.setAnchoredSelection(
                                    newV.anchoredSelection()
                                )
                            ).orElse(null)
                    );
                }
            );
    }

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

    // Fetcher..........................................................................................................

    @Override
    public void onBegin(final HttpMethod method,
                        final Url url,
                        final Optional<FetcherRequestBody<?>> body,
                        final AppContext context) {
        context.debug(method + " " + url, body.orElse(null));
    }

    @Override
    public void onFailure(final HttpMethod method,
                          final AbsoluteOrRelativeUrl url,
                          final HttpStatus status,
                          final Headers headers,
                          final String body,
                          final AppContext context) {
        context.error(method + " " + url + " " + status, body);
    }

    @Override
    public void onError(final Object cause,
                        final AppContext context) {
        context.error(cause);
    }

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
    public void onSpreadsheetComparatorInfoSet(final SpreadsheetId id,
                                               final SpreadsheetComparatorInfoSet infos,
                                               final AppContext context) {
        this.maybeRefreshSpreadsheetProvider(
            id,
            () -> this.spreadsheetComparatorInfoSet = infos
        );
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
    public void onConverterInfoSet(final SpreadsheetId id,
                                   final ConverterInfoSet infos,
                                   final AppContext context) {
        this.maybeRefreshSpreadsheetProvider(
            id,
            () -> this.converterInfoSet = infos
        );
    }

    private ConverterInfoSet converterInfoSet;

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
    public void onSpreadsheetExporterInfoSet(final SpreadsheetId id,
                                             final SpreadsheetExporterInfoSet infos,
                                             final AppContext context) {
        this.maybeRefreshSpreadsheetProvider(
            id,
            () -> this.spreadsheetExporterInfoSet = infos
        );
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
    public void onExpressionFunctionInfoSet(final SpreadsheetId id,
                                            final ExpressionFunctionInfoSet infos,
                                            final AppContext context) {
        this.maybeRefreshSpreadsheetProvider(
            id,
            () -> this.expressionFunctionInfoSet = infos
        );
    }

    private ExpressionFunctionInfoSet expressionFunctionInfoSet;

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
    public void onSpreadsheetFormatterInfoSet(final SpreadsheetId id,
                                              final SpreadsheetFormatterInfoSet infos,
                                              final AppContext context) {
        this.maybeRefreshSpreadsheetProvider(
            id,
            () -> this.spreadsheetFormatterInfoSet = infos
        );
    }

    private SpreadsheetFormatterInfoSet spreadsheetFormatterInfoSet;

    @Override
    public void onSpreadsheetFormatterSelectorEdit(final SpreadsheetId id,
                                                   final SpreadsheetFormatterSelectorEdit edit,
                                                   final AppContext context) {
        // nop
    }

    @Override
    public void onSpreadsheetFormatterSelectorMenuList(final SpreadsheetId id,
                                                       final SpreadsheetFormatterSelectorMenuList menu,
                                                       final AppContext context) {
        // nop
    }

    // FormHandlerFetcher...............................................................................................

    private FormHandlerInfoSet formHandlerInfoSet;

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
    public void onSpreadsheetImporterInfoSet(final SpreadsheetId id,
                                             final SpreadsheetImporterInfoSet infos,
                                             final AppContext context) {
        this.maybeRefreshSpreadsheetProvider(
            id,
            () -> this.spreadsheetImporterInfoSet = infos
        );
    }

    private SpreadsheetImporterInfoSet spreadsheetImporterInfoSet;

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
    public void onSpreadsheetParserInfoSet(final SpreadsheetId id,
                                           final SpreadsheetParserInfoSet infos,
                                           final AppContext context) {
        this.maybeRefreshSpreadsheetProvider(
            id,
            () -> this.spreadsheetParserInfoSet = infos
        );
    }

    private SpreadsheetParserInfoSet spreadsheetParserInfoSet;

    @Override
    public void onSpreadsheetParserSelectorEdit(final SpreadsheetId id,
                                                final SpreadsheetParserSelectorEdit edit,
                                                final AppContext context) {
        // nop
    }

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
    public void onValidatorInfoSet(final SpreadsheetId id,
                                   final ValidatorInfoSet infos,
                                   final AppContext context) {
        this.maybeRefreshSpreadsheetProvider(
            id,
            () -> this.validatorInfoSet = infos
        );
    }

    private ValidatorInfoSet validatorInfoSet;

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
                                   final Optional<JarEntryInfoList> list,
                                   final AppContext context) {
        // NOP
    }

    @Override
    public void onJarEntryInfoName(final PluginName pluginName,
                                   final Optional<JarEntryInfoName> filename,
                                   final Optional<String> body,
                                   final AppContext context) {
        // NOP
    }

    @Override
    public void onPlugin(final PluginName name,
                         final Optional<Plugin> plugin,
                         final AppContext context) {
        // NOP
    }

    @Override
    public void onPluginSet(final PluginSet plugins,
                            final AppContext context) {
        // TODO
    }

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

    // SpreadsheetFormatterContext......................................................................................

    @Override
    public SpreadsheetFormatterContext spreadsheetFormatterContext() {
        return this.formatterContext;
    }

    /**
     * A new {@link SpreadsheetFormatterContext} is created each time a new {@link SpreadsheetMetadata} arrives.
     */
    private SpreadsheetFormatterContext formatterContext;

    // SpreadsheetParserContext.........................................................................................

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

    // ProviderContext..................................................................................................

    @Override
    public CanConvert canConvert() {
        return this.spreadsheetFormatterContext(); // prioritize SpreadsheetFormatterContext over ProviderContext
    }

    @Override
    public ProviderContext providerContext() {
        return this.providerContext;
    }

    private ProviderContext providerContext;

    // SpreadsheetProvider..............................................................................................

    @Override
    public SpreadsheetProvider spreadsheetProvider() {
        return this.spreadsheetProvider;
    }

    private void maybeRefreshSpreadsheetProvider(final SpreadsheetId id,
                                                 final Runnable newInfoSet) {
        if (id.equals(this.spreadsheetMetadata.id().orElse(null))) {
            newInfoSet.run();
            this.refreshSpreadsheetProvider();
        }
    }

    private void refreshSpreadsheetProvider() {
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
                ExpressionFunctionInfoSet.EMPTY
            ),
            ExpressionFunctionProviders.empty(SpreadsheetExpressionFunctions.NAME_CASE_SENSITIVITY) // TODO should have a non empty EFP
        );

        final SpreadsheetFormatterProvider spreadsheetFormatterProvider = SpreadsheetFormatterProviders.mergedMapped(
            this.spreadsheetFormatterInfoSet.renameIfPresent(
                SpreadsheetFormatterInfoSet.EMPTY
            ),
            SpreadsheetFormatterProviders.spreadsheetFormatPattern()
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
                metadata,
                spreadsheetFormatterProvider,
                spreadsheetParserProvider
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
                this.viewportCache, // SpreadsheetLabelNameResolver
                converterProvider,// ConverterProvider
                spreadsheetFormatterProvider, // SpreadsheetFormatterProvider
                this.providerContext // ProviderContext
            );
        } catch (final RuntimeException cause) {
            this.warn("App.refreshSpreadsheetProvider Failed to create SpreadsheetFormatterContext=" + cause.getMessage(), cause);
            this.formatterContext = SpreadsheetFormatterContexts.fake();
        }

        try {
            this.parserContext = metadata.spreadsheetParserContext(
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

    // system SpreadsheetProvider.......................................................................................

    @Override
    public SpreadsheetProvider systemSpreadsheetProvider() {
        return this.systemSpreadsheetProvider;
    }

    private SpreadsheetProvider systemSpreadsheetProvider;

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

    // CanGiveFocus.....................................................................................................

    @Override
    public void giveFocus(final Runnable giveFocus) {
        this.canGiveFocus.giveFocus(giveFocus);
    }

    private final CanGiveFocus canGiveFocus;

    // HasLocale........................................................................................................

    @Override
    public Locale locale() {
        return LOCALE;
    }

    private final static Locale LOCALE = Locale.forLanguageTag("EN-AU"); // TODO use browser locale

    // HasNow...........................................................................................................

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    // HistoryContextDelegator.....................................................................................

    @Override public HistoryContext historyContext() {
        return this.apphistoryContextHistoryTokenWatcher;
    }

    private final AppHistoryContextHistoryWatcher apphistoryContextHistoryTokenWatcher;

    // LoggingContext...................................................................................................

    @Override
    public LoggingContext loggingContext() {
        return this.loggingContext;
    }

    private final LoggingContext loggingContext;
}
