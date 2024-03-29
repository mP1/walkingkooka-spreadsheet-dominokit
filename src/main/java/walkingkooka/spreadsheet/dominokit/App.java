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
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.icons.lib.Icons;
import org.dominokit.domino.ui.layout.AppLayout;
import org.dominokit.domino.ui.layout.NavBar;
import org.dominokit.domino.ui.layout.RightDrawerSize;
import org.dominokit.domino.ui.notifications.Notification;
import org.dominokit.domino.ui.notifications.Notification.Position;
import org.gwtproject.core.client.Scheduler;
import org.gwtproject.core.client.Scheduler.ScheduledCommand;
import walkingkooka.j2cl.locale.LocaleAware;
import walkingkooka.net.Url;
import walkingkooka.net.UrlFragment;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardContext;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardContextReadWatcher;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardContextWriteWatcher;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardContexts;
import walkingkooka.spreadsheet.dominokit.clipboard.ClipboardTextItem;
import walkingkooka.spreadsheet.dominokit.history.History;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.dominokit.history.Historys;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetIdHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.UnknownHistoryToken;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContexts;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetCellFind;
import walkingkooka.spreadsheet.dominokit.ui.applayoutrightdrawer.AppLayoutRightDrawerComponent;
import walkingkooka.spreadsheet.dominokit.ui.columnrowinsert.SpreadsheetColumnRowInsertCountComponent;
import walkingkooka.spreadsheet.dominokit.ui.columnrowinsert.SpreadsheetColumnRowInsertCountComponentContexts;
import walkingkooka.spreadsheet.dominokit.ui.find.SpreadsheetFindComponent;
import walkingkooka.spreadsheet.dominokit.ui.find.SpreadsheetFindComponentContexts;
import walkingkooka.spreadsheet.dominokit.ui.labelmapping.SpreadsheetLabelMappingComponent;
import walkingkooka.spreadsheet.dominokit.ui.labelmapping.SpreadsheetLabelMappingComponentContexts;
import walkingkooka.spreadsheet.dominokit.ui.meta.SpreadsheetMetadataPanelComponent;
import walkingkooka.spreadsheet.dominokit.ui.meta.SpreadsheetMetadataPanelComponentContexts;
import walkingkooka.spreadsheet.dominokit.ui.pattern.SpreadsheetPatternComponent;
import walkingkooka.spreadsheet.dominokit.ui.pattern.SpreadsheetPatternComponentContexts;
import walkingkooka.spreadsheet.dominokit.ui.spreadsheetnameanchor.SpreadsheetNameAnchorComponent;
import walkingkooka.spreadsheet.dominokit.ui.toolbar.SpreadsheetToolbarComponent;
import walkingkooka.spreadsheet.dominokit.ui.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.dominokit.ui.viewport.SpreadsheetViewportComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContexts;

import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@LocaleAware
public class App implements EntryPoint,
        AppContext,
        HistoryTokenWatcher,
        SpreadsheetDeltaFetcherWatcher,
        SpreadsheetMetadataFetcherWatcher,
        UncaughtExceptionHandler {

    public App() {
        GWT.setUncaughtExceptionHandler(this);
        SpreadsheetDelta.EMPTY.toString(); // force json register.

        // logging
        final LoggingContext loggingContext = LoggingContexts.elemental();
        this.loggingContext = loggingContext;

        this.unmarshallContext = JsonNodeUnmarshallContexts.basic(
                ExpressionNumberKind.DEFAULT,
                MathContext.DECIMAL32
        );

        // metadata
        this.spreadsheetMetadata = SpreadsheetMetadata.EMPTY;
        this.metadataWatchers = SpreadsheetMetadataFetcherWatchers.empty();
        this.spreadsheetMetadataFetcher = SpreadsheetMetadataFetcher.with(
                this.metadataWatchers,
                this
        );
        this.addSpreadsheetMetadataWatcher(this);

        // delta
        this.spreadsheetDeltaWatchers = SpreadsheetDeltaFetcherWatchers.empty();
        this.spreadsheetDeltaFetcher = SpreadsheetDeltaFetcher.with(
                this.spreadsheetDeltaWatchers,
                this
        );
        this.addSpreadsheetDeltaWatcher(this);

        // labelMapping
        this.spreadsheetLabelMappingFetcherWatchers = SpreadsheetLabelMappingFetcherWatchers.empty();
        this.spreadsheetLabelMappingFetcher = SpreadsheetLabelMappingFetcher.with(
                this.spreadsheetLabelMappingFetcherWatchers,
                this
        );

        // history
        this.history = Historys.elemental(loggingContext);
        this.previousToken = HistoryToken.unknown(UrlFragment.EMPTY);
        this.historyWatchers = HistoryTokenWatchers.empty();
        this.setupHistoryListener();

        this.registerWindowResizeListener();

        this.viewportCache = SpreadsheetViewportCache.empty(this);

        this.viewportComponent = SpreadsheetViewportComponent.empty(this);

        SpreadsheetColumnRowInsertCountComponent.with(
                SpreadsheetColumnRowInsertCountComponentContexts.appContext(this)
        );

        SpreadsheetFindComponent.with(
                SpreadsheetFindComponentContexts.appContext(this)
        );

        SpreadsheetLabelMappingComponent.with(
                SpreadsheetLabelMappingComponentContexts.appContext(this)
        );

        SpreadsheetPatternComponent.format(
                SpreadsheetPatternComponentContexts.cellFormat(this)
        );
        SpreadsheetPatternComponent.parse(
                SpreadsheetPatternComponentContexts.cellParse(this)
        );

        SpreadsheetPatternComponent.format(
                SpreadsheetPatternComponentContexts.metadataFormat(this)
        );
        SpreadsheetPatternComponent.parse(
                SpreadsheetPatternComponentContexts.metadataParse(this)
        );

        this.layout = this.prepareLayout();
    }

    private final AppLayout layout;

    // header = metadata toggle | clickable(editable) spreadsheet name
    // right = editable metadata properties
    // content = toolbar
    //   formula,
    //   table holding spreadsheet cells
    private AppLayout prepareLayout() {
        final AppLayout layout = AppLayout.create();

        layout.setOverFlowX("hidden")
                .setOverFlowY("hidden");

        layout.getContent()
                .setPadding("0px") // kills the dui-layout-content padding: 25px
                .setOverFlowY("hidden") // stop scrollbars on the cell viewport
                .appendChild(this.viewportComponent);

        final SpreadsheetNameAnchorComponent spreadsheetNameAnchor = SpreadsheetNameAnchorComponent.empty();
        this.addHistoryTokenWatcher(spreadsheetNameAnchor);

        final NavBar navBar = layout.getNavBar();
        navBar.withTitle(
                (n, header) -> {
                    header.appendChild(spreadsheetNameAnchor.element());
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
                                                AppLayoutRightDrawerComponent.with(layout),
                                                SpreadsheetMetadataPanelComponentContexts.appContext(this)
                                        ))
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

    @Override
    public void onModuleLoad() {
        this.fireInitialHistoryToken();
        this.fireInitialWindowSize();
    }

    // window...........................................................................................................

    private void registerWindowResizeListener() {
        DomGlobal.window.addEventListener(
                EventType.resize.getName(),
                (e) -> App.this.onResize(
                        DomGlobal.window.innerWidth,
                        DomGlobal.window.innerHeight
                )
        );
    }

    /**
     * Fire the window size. This is eventually used to compute the spreadsheet viewport size.
     */
    private void fireInitialWindowSize() {
        this.debug("App.fireInitialWindowSize");

        Scheduler.get()
                .scheduleDeferred(
                        new ScheduledCommand() {
                            @Override
                            public void execute() {
                                App.this.onResize(
                                        DomGlobal.window.innerWidth,
                                        DomGlobal.window.innerHeight
                                );
                            }
                        }
                );
    }

    private void onResize(final int width,
                          final int height) {
        final AppLayout layout = this.layout;
        final int navigationBarHeight = layout.getNavBar()
                .element()
                .offsetHeight;

        final int newHeight = height - navigationBarHeight;
        this.debug("App.onResize: " + width + " x " + height + " navigationBarHeight: " + navigationBarHeight + " newHeight: " + newHeight);

        this.viewportComponent.setWidthAndHeight(
                width,
                newHeight
        );
    }

    // ClipboardContext.................................................................................................

    @Override
    public void readClipboardItem(final Predicate<MediaType> filter,
                                  final ClipboardContextReadWatcher watcher) {
        this.readClipboardItem(
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
    public void onFailure(final HttpStatus status,
                          final Headers headers,
                          final String body,
                          final AppContext context) {
        context.error(status, body);
    }

    @Override
    public void onError(final Object cause,
                        final AppContext context) {
        context.error(cause);
    }


    // SpreadsheetDelta.................................................................................................

    @Override
    public SpreadsheetDeltaFetcher spreadsheetDeltaFetcher() {
        return this.spreadsheetDeltaFetcher;
    }

    private final SpreadsheetDeltaFetcher spreadsheetDeltaFetcher;

    @Override
    public Runnable addSpreadsheetDeltaWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.spreadsheetDeltaWatchers.add(watcher);
    }

    @Override
    public Runnable addSpreadsheetDeltaWatcherOnce(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.spreadsheetDeltaWatchers.addOnce(watcher);
    }

    /**
     * A collection of listeners for {@link SpreadsheetDeltaFetcherWatcher}
     */
    private final SpreadsheetDeltaFetcherWatchers spreadsheetDeltaWatchers;

    @Override
    public void onSpreadsheetDelta(final SpreadsheetDelta delta,
                                   final AppContext context) {
        // nop
    }

    // SpreadsheetLabelMapping..........................................................................................

    @Override
    public Runnable addSpreadsheetLabelMappingWatcher(final SpreadsheetLabelMappingFetcherWatcher watcher) {
        return this.spreadsheetLabelMappingFetcherWatchers.add(watcher);
    }

    @Override
    public Runnable addSpreadsheetLabelMappingWatcherOnce(final SpreadsheetLabelMappingFetcherWatcher watcher) {
        return this.spreadsheetLabelMappingFetcherWatchers.addOnce(watcher);
    }

    /**
     * A collection of listeners for {@link SpreadsheetLabelMappingFetcherWatcher}
     */
    private final SpreadsheetLabelMappingFetcherWatchers spreadsheetLabelMappingFetcherWatchers;

    @Override
    public SpreadsheetLabelMappingFetcher spreadsheetLabelMappingFetcher() {
        return this.spreadsheetLabelMappingFetcher;
    }

    private final SpreadsheetLabelMappingFetcher spreadsheetLabelMappingFetcher;

    // SpreadsheetMetadata..............................................................................................

    @Override
    public SpreadsheetMetadataFetcher spreadsheetMetadataFetcher() {
        return this.spreadsheetMetadataFetcher;
    }

    private final SpreadsheetMetadataFetcher spreadsheetMetadataFetcher;

    @Override
    public Runnable addSpreadsheetMetadataWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.metadataWatchers.add(watcher);
    }

    @Override
    public Runnable addSpreadsheetMetadataWatcherOnce(final SpreadsheetMetadataFetcherWatcher watcher) {
        return this.metadataWatchers.add(watcher);
    }

    /**
     * A collection of listeners for {@link SpreadsheetMetadataFetcherWatcher}
     */
    private final SpreadsheetMetadataFetcherWatchers metadataWatchers;

    /**
     * Update the spreadsheet-id, spreadsheet-name and viewport selection from the given {@link SpreadsheetMetadata}.
     */
    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        final SpreadsheetMetadata previousMetadata = this.spreadsheetMetadata;
        this.spreadsheetMetadata = metadata;

        // update the global JsonNodeUnmarshallContext.
        this.unmarshallContext = JsonNodeUnmarshallContexts.basic(
                metadata.expressionNumberKind(),
                metadata.mathContext()
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
                }
            }
        }
    }

    /**
     * Returns the current or last loaded {@link SpreadsheetMetadata}
     */
    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        return this.spreadsheetMetadata;
    }

    private SpreadsheetMetadata spreadsheetMetadata;

    // json.............................................................................................................

    @Override
    public JsonNodeMarshallContext marshallContext() {
        return MARSHALL_CONTEXT;
    }

    /**
     * A constant
     */
    private final static JsonNodeMarshallContext MARSHALL_CONTEXT = JsonNodeMarshallContexts.basic();

    /**
     * The {@link JsonNodeUnmarshallContext} will be updated each time a new {@link SpreadsheetMetadata} is received.
     */
    @Override
    public JsonNodeUnmarshallContext unmarshallContext() {
        return this.unmarshallContext;
    }

    private JsonNodeUnmarshallContext unmarshallContext;

    // history eventListener............................................................................................

    private void fireInitialHistoryToken() {
        final HistoryToken token = this.historyToken();
        this.debug("App.fireInitialHistoryToken " + token);
        this.onHistoryTokenChange(token);
    }

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

    // AppContext history...............................................................................................

    /**
     * Pushes the given {@link HistoryToken} to the browser location#hash.
     */
    @Override
    public void pushHistoryToken(final HistoryToken token) {
        Objects.requireNonNull(token, "token");

        HistoryToken push = token;

        // this check is necessary so when historyToken = something save and that gets pushed again we dont want to reply that again.
        final HistoryToken previous = this.firePrevious;
        if (null != previous && push.shouldIgnore() && push.equals(previous)) {
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

    // HistoryTokenWatcher...............................................................................................

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
                    previous,
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
     * Used to track if the history token actually changed. Changes will fire the HistoryToken#onChange method.
     */
    private HistoryToken previousToken;

    private static void patchMetadataIfSelectionChanged(final HistoryToken historyToken,
                                                        final HistoryToken previous,
                                                        final AppContext context) {
        if (historyToken instanceof SpreadsheetIdHistoryToken) {
            final Optional<AnchoredSpreadsheetSelection> selection = historyToken.anchoredSelectionOrEmpty();
            final Optional<AnchoredSpreadsheetSelection> previousSelection = previous.anchoredSelectionOrEmpty();
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

    // reload...........................................................................................................

    @Override
    public void reload() {
        this.viewportComponent.loadViewportCells(this);
    }

    // Viewport.........................................................................................................

    @Override
    public SpreadsheetViewport viewport(final Optional<AnchoredSpreadsheetSelection> selection) {
        return this.viewportComponent.viewport(
                selection,
                this
        );
    }

    /**
     * Cache for the contents of the viewport.
     */
    public SpreadsheetViewportCache viewportCache() {
        return this.viewportCache;
    }

    private final SpreadsheetViewportCache viewportCache;

    /**
     * Init here to avoid race conditions with other fields like {@link #metadataWatchers}.
     */
    private final SpreadsheetViewportComponent viewportComponent;

    @Override
    public Optional<SpreadsheetCell> viewportCell(final SpreadsheetSelection selection) {
        return this.viewportComponent.viewportCell(
                selection,
                this
        );
    }

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

    // focus............................................................................................................

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

    // cellFind.........................................................................................................

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

    // HasNow...........................................................................................................

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    // logging..........................................................................................................

    @Override
    public void onUncaughtException(final Throwable caught) {
        this.error(caught);
    }

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
