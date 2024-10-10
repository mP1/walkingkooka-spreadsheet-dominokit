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
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import elemental2.dom.DomGlobal;
import elemental2.dom.Element;
import elemental2.dom.Event;
import elemental2.dom.Headers;
import org.dominokit.domino.ui.cards.Card;
import org.dominokit.domino.ui.elements.SectionElement;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.icons.lib.Icons;
import org.dominokit.domino.ui.layout.AppLayout;
import org.dominokit.domino.ui.layout.NavBar;
import org.dominokit.domino.ui.layout.RightDrawerSize;
import org.dominokit.domino.ui.notifications.Notification;
import org.dominokit.domino.ui.notifications.Notification.Position;
import org.gwtproject.core.client.Scheduler;
import walkingkooka.convert.provider.ConverterInfoSet;
import walkingkooka.convert.provider.ConverterProvider;
import walkingkooka.convert.provider.ConverterProviders;
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.j2cl.locale.LocaleAware;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlFragment;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContextDelegator;
import walkingkooka.plugin.ProviderContexts;
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
import walkingkooka.spreadsheet.dominokit.comparator.SpreadsheetComparatorNameListDialogComponent;
import walkingkooka.spreadsheet.dominokit.comparator.SpreadsheetComparatorNameListDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.find.SpreadsheetCellFind;
import walkingkooka.spreadsheet.dominokit.find.SpreadsheetFindDialogComponent;
import walkingkooka.spreadsheet.dominokit.find.SpreadsheetFindDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.format.SpreadsheetFormatterSelectorDialogComponent;
import walkingkooka.spreadsheet.dominokit.format.SpreadsheetFormatterSelectorDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.history.History;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.dominokit.history.Historys;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetIdHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetListRenameHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetListSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetNameHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.UnknownHistoryToken;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContexts;
import walkingkooka.spreadsheet.dominokit.meta.SpreadsheetMetadataPanelComponent;
import walkingkooka.spreadsheet.dominokit.meta.SpreadsheetMetadataPanelComponentContexts;
import walkingkooka.spreadsheet.dominokit.net.ConverterFetcher;
import walkingkooka.spreadsheet.dominokit.net.ConverterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.ConverterFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.net.ExpressionFunctionFetcher;
import walkingkooka.spreadsheet.dominokit.net.ExpressionFunctionFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.ExpressionFunctionFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.net.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetComparatorFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetComparatorFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetComparatorFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetExporterFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetExporterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetExporterFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetFormatterFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetFormatterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetFormatterFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetImporterFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetImporterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetImporterFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetParserFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetParserFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetParserFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.parser.SpreadsheetParserSelectorDialogComponent;
import walkingkooka.spreadsheet.dominokit.parser.SpreadsheetParserSelectorDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.pluginfoset.PluginInfoSetDialogComponent;
import walkingkooka.spreadsheet.dominokit.pluginfoset.PluginInfoSetDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.reference.SpreadsheetColumnRowInsertCountDialogComponent;
import walkingkooka.spreadsheet.dominokit.reference.SpreadsheetColumnRowInsertCountDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.reference.SpreadsheetLabelMappingDialogComponent;
import walkingkooka.spreadsheet.dominokit.reference.SpreadsheetLabelMappingDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.sort.SpreadsheetSortDialogComponent;
import walkingkooka.spreadsheet.dominokit.sort.SpreadsheetSortDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.spreadsheet.SpreadsheetListComponentContexts;
import walkingkooka.spreadsheet.dominokit.spreadsheet.SpreadsheetListDialogComponent;
import walkingkooka.spreadsheet.dominokit.spreadsheet.SpreadsheetNameDialogComponent;
import walkingkooka.spreadsheet.dominokit.spreadsheet.SpreadsheetNameDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.toolbar.SpreadsheetToolbarComponent;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.export.SpreadsheetExporterInfoSet;
import walkingkooka.spreadsheet.export.SpreadsheetExporterProvider;
import walkingkooka.spreadsheet.export.SpreadsheetExporterProviders;
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
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfoSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionProvider;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionProviders;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.tree.json.marshall.JsonNodeMarshallUnmarshallContextDelegator;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContexts;

import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Predicate;

@LocaleAware
public class App implements EntryPoint,
        AppContext,
        WindowResizeWatcher,
        HistoryTokenWatcher,
        JsonNodeMarshallUnmarshallContextDelegator,
        NopEmptyResponseFetcherWatcher,
        ProviderContextDelegator,
        SpreadsheetComparatorFetcherWatcher,
        ConverterFetcherWatcher,
        SpreadsheetDeltaFetcherWatcher,
        SpreadsheetExporterFetcherWatcher,
        ExpressionFunctionFetcherWatcher,
        SpreadsheetFormatterFetcherWatcher,
        SpreadsheetImporterFetcherWatcher,
        SpreadsheetMetadataFetcherWatcher,
        UncaughtExceptionHandler,
        SpreadsheetParserFetcherWatcher,
        SpreadsheetProviderDelegator,
        SpreadsheetFormatterContextDelegator {

    public App() {
        GWT.setUncaughtExceptionHandler(this);
        SpreadsheetDelta.EMPTY.toString(); // force json register.

        this.addWindowResizeListener(this::onWindowResize);

        // logging
        final LoggingContext loggingContext = LoggingContexts.elemental();
        this.loggingContext = loggingContext;

        this.spreadsheetProvider = SpreadsheetProviders.basic(
                ConverterProviders.empty(),
                ExpressionFunctionProviders.empty(),
                SpreadsheetComparatorProviders.empty(),
                SpreadsheetExporterProviders.empty(),
                SpreadsheetFormatterProviders.empty(),
                SpreadsheetImporterProviders.empty(),
                SpreadsheetParserProviders.empty()
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

        this.providerContext = ProviderContexts.fake();

        // history
        this.history = Historys.elemental(loggingContext);
        this.previousToken = HistoryToken.unknown(UrlFragment.EMPTY);
        this.historyWatchers = HistoryTokenWatchers.empty();
        this.setupHistoryListener();

        this.viewportCache = SpreadsheetViewportCache.empty(this);

        this.viewportComponent = SpreadsheetViewportComponent.empty(this);

        SpreadsheetListDialogComponent.with(
                SpreadsheetListComponentContexts.basic(
                        this,
                        this.spreadsheetMetadataFetcher,
                        this.metadataFetcherWatchers,
                        this
                )
        );

        PluginInfoSetDialogComponent.with(
                PluginInfoSetDialogComponentContexts.converters(this)
        );
        PluginInfoSetDialogComponent.with(
                PluginInfoSetDialogComponentContexts.comparators(this)
        );
        PluginInfoSetDialogComponent.with(
                PluginInfoSetDialogComponentContexts.exporters(this)
        );
        PluginInfoSetDialogComponent.with(
                PluginInfoSetDialogComponentContexts.expressionFunctions(this)
        );
        PluginInfoSetDialogComponent.with(
                PluginInfoSetDialogComponentContexts.formatters(this)
        );
        PluginInfoSetDialogComponent.with(
                PluginInfoSetDialogComponentContexts.importers(this)
        );
        PluginInfoSetDialogComponent.with(
                PluginInfoSetDialogComponentContexts.parsers(this)
        );

        SpreadsheetComparatorNameListDialogComponent.with(
                SpreadsheetComparatorNameListDialogComponentContexts.sortComparators(this)
        );

        SpreadsheetNameDialogComponent.with(
                SpreadsheetNameDialogComponentContexts.spreadsheetListRename(this)
        );

        SpreadsheetNameDialogComponent.with(
                SpreadsheetNameDialogComponentContexts.spreadsheetRename(this)
        );

        SpreadsheetColumnRowInsertCountDialogComponent.with(
                SpreadsheetColumnRowInsertCountDialogComponentContexts.appContext(this)
        );

        SpreadsheetFindDialogComponent.with(
                SpreadsheetFindDialogComponentContexts.appContext(this)
        );

        SpreadsheetLabelMappingDialogComponent.with(
                SpreadsheetLabelMappingDialogComponentContexts.appContext(this)
        );

        SpreadsheetFormatterSelectorDialogComponent.with(
                SpreadsheetFormatterSelectorDialogComponentContexts.cell(this)
        );

        SpreadsheetParserSelectorDialogComponent.with(
                SpreadsheetParserSelectorDialogComponentContexts.cell(this)
        );

        SpreadsheetFormatterSelectorDialogComponent.with(
                SpreadsheetFormatterSelectorDialogComponentContexts.metadata(this)
        );
        SpreadsheetParserSelectorDialogComponent.with(
                SpreadsheetParserSelectorDialogComponentContexts.metadata(this)
        );

        SpreadsheetSortDialogComponent.with(
                SpreadsheetSortDialogComponentContexts.basic(
                        this, // SpreadsheetComparatorProvider
                        this // HistoryTokenContext
                )
        );

        this.files = this.files();
        this.layout = this.prepareLayout();
    }

    // EntryPoint.......................................................................................................

    @Override
    public void onModuleLoad() {
        this.fireInitialHistoryToken();
        this.fireWindowSizeLater(this::onWindowResize);
    }

    /**
     * A link that shows the File browser.
     */
    private HistoryTokenAnchorComponent files() {
        // TODO need to *READ* from and count
        return HistoryToken.spreadsheetListSelect(
                        OptionalInt.empty(), // from
                        OptionalInt.empty() // count
                )
                .link("files")
                .setTextContent("Files");
    }

    // header = metadata toggle | clickable(editable) spreadsheet name
    // right = editable metadata properties
    // content = toolbar
    //   formula,
    //   table holding spreadsheet cells
    private SpreadsheetAppLayout prepareLayout() {
        final SpreadsheetAppLayout layout = SpreadsheetAppLayout.empty(this);

        layout.setOverFlowX("hidden")
                .setOverFlowY("hidden");

        layout.getContent()
                .setPadding("0px") // kills the dui-layout-content padding: 25px
                .setOverFlowY("hidden") // stop scrollbars on the cell viewport
                .appendChild(this.viewportComponent);

        final NavBar navBar = layout.getNavBar();
        navBar.withTitle(
                (n, header) -> {
                    header.appendChild(
                            this.files.element()
                    );
                    header.appendChild(
                            this.spreadsheetNameAnchorComponent().element()
                    );
                }
        );

        navBar.getBody()
                .appendChild(
                        SpreadsheetToolbarComponent.with(this)
                );

        layout.setRightDrawerSize(RightDrawerSize.XLARGE)
                .getRightDrawerContent()
                .appendChild(
                        Card.create()
                                .appendChild(
                                        SpreadsheetMetadataPanelComponent.with(
                                                SpreadsheetAppLayoutRightDrawerComponent.with(layout),
                                                SpreadsheetMetadataPanelComponentContexts.appContext(this)
                                        ))
                );
        layout.onRightDrawerClosed(
                (AppLayout a, final SectionElement s) -> this.appLayoutRightPanelClosed()
        );

        final Icon<?> rightToggleIcon = Icons.menu_open();
        rightToggleIcon.addClickListener(
                this::appLayoutRightToggleIconOnClick
        );
        layout.setRightDrawerToggleIcon(rightToggleIcon);

        DomGlobal.document.body.append(
                layout.element()
        );

        return layout;
    }

    private final HistoryTokenAnchorComponent files;

    /**
     * A {@link HistoryTokenAnchorComponent} which is updated to something like #/1/SpreadsheetName/rename, which
     * when clicked will open the {@link SpreadsheetNameDialogComponent}.
     */
    private HistoryTokenAnchorComponent spreadsheetNameAnchorComponent() {
        final HistoryTokenAnchorComponent anchor = this.historyToken()
                .link("spreadsheetNameRename");

        this.addHistoryTokenWatcher(
                (previous, context) -> {
                    final HistoryToken historyToken = context.historyToken();

                    SpreadsheetNameHistoryToken nameHistoryToken = null;
                    String nameText = "";

                    if (historyToken instanceof SpreadsheetNameHistoryToken) {
                        nameHistoryToken = historyToken.cast(SpreadsheetNameHistoryToken.class);
                        final SpreadsheetName name = nameHistoryToken.name();

                        nameHistoryToken = HistoryToken.spreadsheetRenameSelect(
                                nameHistoryToken.id(),
                                name
                        );
                        nameText = name.text();
                    }
                    anchor.setHistoryToken(
                            Optional.ofNullable(nameHistoryToken)
                    );
                    anchor.setTextContent(nameText);
                }
        );
        return anchor;
    }

    /**
     * Handler that reacts to the right panel toggle icon being clicked, updating the history. The {@link SpreadsheetMetadataPanelComponent}
     * will see the history token change and then open or hide itself.
     */
    private void appLayoutRightToggleIconOnClick(final Event event) {
        HistoryToken token = this.historyToken();

        this.pushHistoryToken(
                this.layout.isRightDrawerOpen() ?
                        token.metadataHide() :
                        token.metadataShow()
        );
    }

    private final SpreadsheetAppLayout layout;

    /**
     * This event is fired when the right panel closes, such as when the user clicks away from it and the history token needs to be updated.
     */
    private void appLayoutRightPanelClosed() {
        final HistoryToken token = this.historyToken();

        // HACK only hide metadata panel if NOT displaying a metadata editor dialog
        if (false == token.isMetadataFormatter() &&
                false == token.isMetadataParser() &&
                false == token.isMetadataPlugin()) {
            this.pushHistoryToken(
                    token.metadataHide()
            );
        }
    }

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
                        final Optional<String> body,
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
        if (id.equals(this.spreadsheetMetadata.id().orElse(null))) {
            this.spreadsheetComparatorInfoSet = infos;
            this.refreshSpreadsheetProvider();
        }
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
        if (id.equals(this.spreadsheetMetadata.id().orElse(null))) {
            this.converterInfoSet = infos;
            this.refreshSpreadsheetProvider();
        }
    }

    private ConverterInfoSet converterInfoSet;

    // SpreadsheetDeltaFetcher..........................................................................................

    @Override
    public SpreadsheetDeltaFetcher spreadsheetDeltaFetcher() {
        return this.spreadsheetDeltaFetcher;
    }

    private final SpreadsheetDeltaFetcher spreadsheetDeltaFetcher;

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.spreadsheetDeltaFetcherWatchers.add(watcher);
    }

    @Override
    public Runnable addSpreadsheetDeltaFetcherWatcherOnce(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.spreadsheetDeltaFetcherWatchers.addOnce(watcher);
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
        if (id.equals(this.spreadsheetMetadata.id().orElse(null))) {
            this.spreadsheetExporterInfoSet = infos;
            this.refreshSpreadsheetProvider();
        }
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
        if (id.equals(this.spreadsheetMetadata.id().orElse(null))) {
            this.expressionFunctionInfoSet = infos;
            this.refreshSpreadsheetProvider();
        }
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
        if (id.equals(this.spreadsheetMetadata.id().orElse(null))) {
            this.spreadsheetFormatterInfoSet = infos;
            this.refreshSpreadsheetProvider();
        }
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
        if (id.equals(this.spreadsheetMetadata.id().orElse(null))) {
            this.spreadsheetImporterInfoSet = infos;
            this.refreshSpreadsheetProvider();
        }
    }

    private SpreadsheetImporterInfoSet spreadsheetImporterInfoSet;

    // SpreadsheetMetadataFetcher.......................................................................................

    @Override
    public SpreadsheetMetadataFetcher spreadsheetMetadataFetcher() {
        return this.spreadsheetMetadataFetcher;
    }

    private final SpreadsheetMetadataFetcher spreadsheetMetadataFetcher;

    @Override
    public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.metadataFetcherWatchers.add(watcher);
    }

    @Override
    public Runnable addSpreadsheetMetadataFetcherWatcherOnce(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.metadataFetcherWatchers.addOnce(watcher);
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

            final EnvironmentContext environmentContext = metadata.environmentContext();
            this.providerContext = ProviderContexts.basic(environmentContext);

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
        if (id.equals(this.spreadsheetMetadata.id().orElse(null))) {
            this.spreadsheetParserInfoSet = infos;
            this.refreshSpreadsheetProvider();
        }
    }

    private SpreadsheetParserInfoSet spreadsheetParserInfoSet;

    @Override
    public void onSpreadsheetParserSelectorEdit(final SpreadsheetId id,
                                                final SpreadsheetParserSelectorEdit edit,
                                                final AppContext context) {
        // nop
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
    public char valueSeparator() {
        return this.parserContext.valueSeparator();
    }

    private SpreadsheetParserContext parserContext;

    // ProviderContext..................................................................................................

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

    private void refreshSpreadsheetProvider() {
        final SpreadsheetMetadata metadata = this.spreadsheetMetadata();

        final SpreadsheetComparatorProvider spreadsheetComparatorProvider = SpreadsheetComparatorProviders.mergedMapped(
                this.spreadsheetComparatorInfoSet.renameIfPresent(
                        metadata.get(SpreadsheetMetadataPropertyName.COMPARATORS)
                                .orElse(SpreadsheetComparatorInfoSet.EMPTY)
                ),
                SpreadsheetComparatorProviders.spreadsheetComparators()
        );

        final SpreadsheetExporterProvider spreadsheetExporterProvider = SpreadsheetExporterProviders.mergedMapped(
                this.spreadsheetExporterInfoSet.renameIfPresent(
                        metadata.get(SpreadsheetMetadataPropertyName.EXPORTERS)
                                .orElse(SpreadsheetExporterInfoSet.EMPTY)
                ),
                SpreadsheetExporterProviders.spreadsheetExport()
        );

        final ExpressionFunctionProvider expressionFunctionProvider = ExpressionFunctionProviders.mergedMapped(
                this.expressionFunctionInfoSet.renameIfPresent(
                        metadata.get(SpreadsheetMetadataPropertyName.FUNCTIONS)
                                .orElse(ExpressionFunctionInfoSet.EMPTY)
                ),
                ExpressionFunctionProviders.empty() // TODO should have a non empty EFP
        );

        final SpreadsheetFormatterProvider spreadsheetFormatterProvider = SpreadsheetFormatterProviders.mergedMapped(
                this.spreadsheetFormatterInfoSet.renameIfPresent(
                        metadata.get(SpreadsheetMetadataPropertyName.FORMATTERS)
                                .orElse(SpreadsheetFormatterInfoSet.EMPTY)
                ),
                SpreadsheetFormatterProviders.spreadsheetFormatPattern()
        );

        final SpreadsheetImporterProvider spreadsheetImporterProvider = SpreadsheetImporterProviders.mergedMapped(
                this.spreadsheetImporterInfoSet.renameIfPresent(
                        metadata.get(SpreadsheetMetadataPropertyName.IMPORTERS)
                                .orElse(SpreadsheetImporterInfoSet.EMPTY)
                ),
                SpreadsheetImporterProviders.spreadsheetImport()
        );

        final SpreadsheetParserProvider spreadsheetParserProvider = SpreadsheetParserProviders.mergedMapped(
                this.spreadsheetParserInfoSet.renameIfPresent(
                        metadata.get(SpreadsheetMetadataPropertyName.PARSERS)
                                .orElse(SpreadsheetParserInfoSet.EMPTY)
                ),
                SpreadsheetParserProviders.spreadsheetParsePattern(spreadsheetFormatterProvider)
        );

        final ConverterProvider converterProvider = ConverterProviders.mergedMapped(
                this.converterInfoSet.renameIfPresent(
                        metadata.get(SpreadsheetMetadataPropertyName.CONVERTERS)
                                .orElse(ConverterInfoSet.EMPTY)
                ),
                SpreadsheetConvertersConverterProviders.spreadsheetConverters(
                        metadata,
                        spreadsheetFormatterProvider,
                        spreadsheetParserProvider
                )
        );

        this.spreadsheetProvider = metadata.spreadsheetProvider(
                SpreadsheetProviders.basic(
                        converterProvider,
                        expressionFunctionProvider,
                        spreadsheetComparatorProvider,
                        spreadsheetExporterProvider,
                        spreadsheetFormatterProvider,
                        spreadsheetImporterProvider,
                        spreadsheetParserProvider
                )
        );
        this.systemSpreadsheetProvider = SpreadsheetProviders.basic(
                converterProvider,
                expressionFunctionProvider,
                spreadsheetComparatorProvider,
                spreadsheetExporterProvider,
                spreadsheetFormatterProvider,
                spreadsheetImporterProvider,
                spreadsheetParserProvider
        );

        try {
            this.formatterContext = metadata.spreadsheetFormatterContext(
                    () -> this.now(), // not sure why but method ref fails.
                    this.viewportCache, // SpreadsheetLabelNameResolver
                    converterProvider,// ConverterProvider
                    spreadsheetFormatterProvider, // SpreadsheetFormatterProvider
                    this.providerContext // ProviderContext
            );
        } catch (final RuntimeException cause) {
            this.debug(".refreshSpreadsheetProvider Failed to create SpreadsheetFormatterContext=" + cause.getMessage(), cause.getCause());
            this.formatterContext = SpreadsheetFormatterContexts.fake();
        }

        try {
            this.parserContext = metadata.parserContext(
                    () -> this.now() // not sure why but method ref fails.
            );
        } catch (final RuntimeException cause) {
            this.debug(".refreshSpreadsheetProvider Failed to create SpreadsheetParserContext=" + cause.getMessage(), cause.getCause());
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

    // reload...........................................................................................................

    private void fireInitialHistoryToken() {
        final HistoryToken token = this.historyToken();
        this.debug("App.fireInitialHistoryToken " + token);
        this.onHistoryTokenChange(token);
    }

    // Viewport.........................................................................................................

    private void setupHistoryListener() {
        this.addHistoryTokenWatcher(this);

        DomGlobal.self.addEventListener(
                EventType.hashchange.getName(),
                event -> this.onHistoryTokenChange(
                        this.historyToken()
                )
        );
    }

    private void onHistoryTokenChange(final HistoryToken token) {
        final HistoryToken previousToken = this.previousToken;
        this.debug("App.onHistoryTokenChange BEGIN from " + previousToken + " to " + token);

        this.previousToken = token;

        if (false == token.equals(previousToken)) {
            if (token instanceof UnknownHistoryToken) {
                this.debug("App.onHistoryTokenChange updated with invalid token " + token + ", will restore previous " + previousToken);
                this.pushHistoryToken(previousToken);

            } else {
                this.historyWatchers.onHistoryTokenChange(
                        previousToken,
                        this
                );
            }
        }

        this.debug("App.onHistoryTokenChange END from " + previousToken + " to " + token);
    }

    /**
     * Used to track if the history token actually changed. Changes will fire the HistoryToken#onChange method.
     */
    private HistoryToken previousToken;

    /**
     * Pushes the given {@link HistoryToken} to the browser location#hash.
     */
    @Override
    public void pushHistoryToken(final HistoryToken token) {
        Objects.requireNonNull(token, "token");

        HistoryToken push = token;

        // this check is necessary so when historyToken = something save and that gets pushed again we dont want to reply that again.
        final HistoryToken previous = this.firePrevious;
        if (push.shouldIgnore() && push.equals(previous)) {
            push = push.clearAction();
        }
        this.history.pushHistoryToken(push);
    }

    @Override
    public HistoryToken historyToken() {
        return this.history.historyToken();
    }

    private final History history;

    @Override
    public void fireCurrentHistoryToken() {
        final HistoryToken previous = this.historyToken();
        this.firePrevious = previous;

        try {
            this.historyWatchers.onHistoryTokenChange(
                    previous,
                    this
            );
        } finally {
            this.firePrevious = null;
        }
    }

    private HistoryToken firePrevious;

    @Override
    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
        return this.historyWatchers.add(watcher);
    }

    @Override
    public Runnable addHistoryTokenWatcherOnce(final HistoryTokenWatcher watcher) {
        return this.historyWatchers.addOnce(watcher);
    }

    private final HistoryTokenWatchers historyWatchers;

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        // if the selection changed update metadata
        final HistoryToken historyToken = context.historyToken();
        if (false == historyToken.shouldIgnore()) {
            patchMetadataIfSelectionChanged(
                    historyToken,
                    context
            );
        }

        this.firePrevious = previous;
        try {
            historyToken.onHistoryTokenChange(
                    previous,
                    context
            );
        } finally {
            this.firePrevious = null;
        }
    }

    /**
     * Only PATCH the spreadsheet metadata on the server if the local {@link SpreadsheetMetadata} has a different
     * {@link AnchoredSpreadsheetSelection}. Note we ignore the previous {@link HistoryToken} because that might cause
     * synching issues where
     * <pre>
     * click A1
     *   load viewport selection=A1
     * click B2
     *   load viewport selection=A2
     * load viewport response
     *   update local metadata selection=A1
     *   push history selection=A1
     *   DONT want to PATCH metadata selection=A1 as this will cause load viewport selection=A2 to be ovewritten.
     * </pre>
     */
    private static void patchMetadataIfSelectionChanged(final HistoryToken historyToken,
                                                        final AppContext context) {
        if (historyToken instanceof SpreadsheetIdHistoryToken) {
            // check against local metadata NOT previous history selection, otherwise PATCH will be made
            // when a loadViewport has not yet updated history token with response selection.
            final Optional<AnchoredSpreadsheetSelection> selection = historyToken.anchoredSelectionOrEmpty();

            final Optional<AnchoredSpreadsheetSelection> previousSelection = context.spreadsheetMetadata()
                    .get(SpreadsheetMetadataPropertyName.VIEWPORT)
                    .flatMap(SpreadsheetViewport::anchoredSelection);
            if (false == selection.equals(previousSelection)) {

                context.debug("App.patchMetadataIfSelectionChanged selection changed from " + previousSelection.orElse(null) + " TO " + selection.orElse(null) + " will update Metadata");

                // initially metadata will be empty because it has not yet loaded, context.viewport below will fail.
                if (context.spreadsheetMetadata()
                        .get(SpreadsheetMetadataPropertyName.VIEWPORT)
                        .isPresent()) {
                    final SpreadsheetIdHistoryToken spreadsheetIdHistoryToken = (SpreadsheetIdHistoryToken) historyToken;
                    context.spreadsheetMetadataFetcher()
                            .patchMetadata(
                                    spreadsheetIdHistoryToken.id(),
                                    SpreadsheetMetadataPropertyName.VIEWPORT.patch(
                                            selection.map(
                                                    s -> context.viewport(
                                                            Optional.of(s)
                                                    )
                                            ).orElse(null)
                                    )
                            );
                }
            }
        }
    }

    // SpreadsheetViewport..............................................................................................

    @Override
    public void reload() {
        this.viewportComponent.loadViewportCells(this);
    }

    @Override
    public SpreadsheetViewport viewport(final Optional<AnchoredSpreadsheetSelection> selection) {
        return this.viewportComponent.viewport(
                selection,
                this
        );
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

    // viewportHighlightEnabled.........................................................................................

    /**
     * Returns true if viewport {@link SpreadsheetCellFind} highlighting is enabled.
     */
    @Override
    public boolean isViewportHighlightEnabled() {
        return this.viewportHighlightEnabled;
    }

    @Override
    public void setViewportHighlightEnabled(final boolean viewportHighlightEnabled) {
        this.viewportHighlightEnabled = viewportHighlightEnabled;
    }

    private boolean viewportHighlightEnabled = false;

    // HasLocale........................................................................................................

    /**
     * Schedules giving focus to the {@link Element} if it exists. If multiple attempts are made to give focus in a short
     * period of time an {@link IllegalStateException} will be thrown.
     */
    public void giveFocus(final Runnable giveFocus) {
        this.debug("App.giveFocus " + giveFocus);

        final Runnable existingGiveFocus = this.giveFocus;
        if (null != existingGiveFocus && false == giveFocus.equals(existingGiveFocus)) {
            this.giveFocus = null;
            throw new IllegalStateException("Second attempt to give focus " + existingGiveFocus + " AND " + giveFocus);
        }
        this.giveFocus = giveFocus;

        Scheduler.get()
                .scheduleDeferred(this::giveFocus0);
    }

    /**
     * If {@link Runnable #giveFocus} is available run it.
     */
    private void giveFocus0() {
        final Runnable giveFocus = this.giveFocus;
        this.giveFocus = null;

        if (null != giveFocus) {
            giveFocus.run();
        }
    }

    /**
     * A {@link Runnable} which will give focus to some element. This is used to track and prevent multiple give focus attempts
     */
    private Runnable giveFocus;

    // lastCellFind......................................................................................................

    @Override
    public SpreadsheetCellFind lastCellFind() {
        return this.lastCellFind;
    }

    @Override
    public void setLastCellFind(final SpreadsheetCellFind lastCellFind) {
        Objects.requireNonNull(lastCellFind, "lastCellFind");
        this.lastCellFind = lastCellFind;
    }

    private SpreadsheetCellFind lastCellFind = SpreadsheetCellFind.empty();

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

    // defaultCount.....................................................................................................

    @Override
    public OptionalInt spreadsheetListDialogComponentDefaultCount() {
        return this.defaultCount;
    }

    private OptionalInt defaultCount = OptionalInt.of(10);

    private void computeAndSaveSpreadsheetListDialogComponentDefaultCount(final int windowHeight) {
        // height - 350 reserved for dialog title, links along bottom etc divided by 32 for each row
        final OptionalInt defaultCount = OptionalInt.of(
                (windowHeight - 350) / 32
        );
        this.defaultCount = defaultCount;

        this.lastResize = System.currentTimeMillis();

        // only reload SpreadsheetListDialogComponent after resizing stops.
        DomGlobal.setTimeout(
                (values) -> {
                    if (System.currentTimeMillis() - this.lastResize > 1000) {
                        final HistoryToken historyToken = this.historyToken();
                        if (historyToken instanceof SpreadsheetListSelectHistoryToken) {
                            final OptionalInt count = historyToken.count();
                            if (false == count.isPresent()) {
                                this.pushHistoryToken(
                                        historyToken.setReload()
                                );
                            }
                        }
                    }
                },
                1000
        );
    }

    /**
     * Used to track when resizing stops, after resizing stops a reload will happen if SpreadsheetListDialogComponent is displayed.
     */
    private long lastResize;

    // UncaughtExceptionHandler.........................................................................................

    @Override
    public void onUncaughtException(final Throwable caught) {
        this.error(caught);
    }

    // LoggingContext...................................................................................................

    @Override
    public void debug(final Object... values) {
        this.loggingContext.debug(values);
    }

    /**
     * Logs an error to the console and shows a DANGER notification.
     */
    @Override
    public void error(final Object... values) {
        this.loggingContext.error(values);

        Notification.create(
                        String.valueOf(values[0])
                ).setPosition(Position.TOP_MIDDLE)
                .show();
    }

    private final LoggingContext loggingContext;
}
