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
import org.dominokit.domino.ui.cards.Card;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.icons.lib.Icons;
import org.dominokit.domino.ui.layout.AppLayout;
import org.dominokit.domino.ui.layout.RightDrawerSize;
import org.dominokit.domino.ui.notifications.Notification;
import org.dominokit.domino.ui.notifications.Notification.Position;
import org.gwtproject.core.client.Scheduler;
import org.gwtproject.core.client.Scheduler.ScheduledCommand;
import walkingkooka.color.Color;
import walkingkooka.j2cl.locale.LocaleAware;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.history.History;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.dominokit.history.Historys;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetIdHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.UnknownHistoryToken;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContexts;
import walkingkooka.spreadsheet.dominokit.net.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.ui.AppRightDrawerOpenableComponent;
import walkingkooka.spreadsheet.dominokit.ui.label.SpreadsheetLabelMappingEditorComponent;
import walkingkooka.spreadsheet.dominokit.ui.label.SpreadsheetLabelMappingEditorComponentContexts;
import walkingkooka.spreadsheet.dominokit.ui.meta.SpreadsheetMetadataPanelComponent;
import walkingkooka.spreadsheet.dominokit.ui.meta.SpreadsheetMetadataPanelComponentContexts;
import walkingkooka.spreadsheet.dominokit.ui.pattern.SpreadsheetPatternEditorComponent;
import walkingkooka.spreadsheet.dominokit.ui.pattern.SpreadsheetPatternEditorComponentContexts;
import walkingkooka.spreadsheet.dominokit.ui.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.dominokit.ui.viewport.SpreadsheetViewportComponent;
import walkingkooka.spreadsheet.dominokit.ui.viewport.SpreadsheetViewportToolbarComponent;
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
import walkingkooka.tree.text.BorderStyle;
import walkingkooka.tree.text.FontFamily;
import walkingkooka.tree.text.FontSize;
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.FontVariant;
import walkingkooka.tree.text.FontWeight;
import walkingkooka.tree.text.Hyphens;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.VerticalAlign;
import walkingkooka.tree.text.WordBreak;

import java.math.MathContext;
import java.util.Optional;

@LocaleAware
public class App implements EntryPoint,
        AppContext,
        HistoryTokenWatcher,
        NopFetcherWatcher,
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

        this.viewportCache = SpreadsheetViewportCache.empty();
        this.addSpreadsheetMetadataWatcher(this.viewportCache);
        this.addSpreadsheetDeltaWatcher(this.viewportCache);

        this.viewportComponent = SpreadsheetViewportComponent.empty(this);

        SpreadsheetLabelMappingEditorComponent.with(
                SpreadsheetLabelMappingEditorComponentContexts.basic(this)
        );

        SpreadsheetPatternEditorComponent.format(
                SpreadsheetPatternEditorComponentContexts.cellFormat(this)
        );
        SpreadsheetPatternEditorComponent.parse(
                SpreadsheetPatternEditorComponentContexts.cellParse(this)
        );

        SpreadsheetPatternEditorComponent.format(
                SpreadsheetPatternEditorComponentContexts.metadataFormat(this)
        );
        SpreadsheetPatternEditorComponent.parse(
                SpreadsheetPatternEditorComponentContexts.metadataParse(this)
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

        layout.getNavBar()
                .getBody()
                .appendChild(
                        SpreadsheetViewportToolbarComponent.with(this)
                );

        layout.setRightDrawerSize(RightDrawerSize.XLARGE)
                .getRightDrawerContent()
                .appendChild(
                        Card.create()
                                .appendChild(
                                        SpreadsheetMetadataPanelComponent.with(
                                                AppRightDrawerOpenableComponent.with(layout),
                                                SpreadsheetMetadataPanelComponentContexts.basic(this)
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
        this.setSpreadsheetName("Untitled 123");
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

        final TextStyle cellStyle = metadata.effectiveStyle();
        this.cellSelectedStyle = cellStyle.merge(CELL_SELECTED_STYLE);
        this.cellUnselectedStyle = cellStyle.merge(CELL_UNSELECTED_STYLE);

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
                    ).setSelection(
                            viewport.flatMap(v -> v.selection())
                    );

            if (false == historyToken.equals(idNameSelectionHistoryToken)) {
                context.debug("App.onSpreadsheetMetadata from " + historyToken + " to different id/name/selection " + idNameSelectionHistoryToken, metadata);
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

    // misc..........................................................................................................

    public void setSpreadsheetName(final String name) {
        this.layout.getNavBar()
                .setTitle(name);
    }

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
        this.history.pushHistoryToken(token);
    }

    @Override
    public HistoryToken historyToken() {
        return this.history.historyToken();
    }

    private final History history;

    @Override
    public void fireCurrentHistoryToken() {
        this.historyWatchers.onHistoryTokenChange(
                this.historyToken(),
                this
        );
    }

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
        if (historyToken instanceof SpreadsheetIdHistoryToken) {
            final Optional<AnchoredSpreadsheetSelection> selection = historyToken.selectionOrEmpty();
            final Optional<AnchoredSpreadsheetSelection> previousSelection = previous.selectionOrEmpty();
            if (false == selection.equals(previousSelection)) {

                context.debug("App.onHistoryTokenChange selection changed from " + previousSelection.orElse(null) + " TO " + selection.orElse(null) + " will update Metadata");

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

        historyToken.onHistoryTokenChange(
                previous,
                context
        );
    }

    /**
     * Used to track if the history token actually changed. Changes will fire the HistoryToken#onChange method.
     */
    private HistoryToken previousToken;

    // UI...............................................................................................................

    @Override
    public TextStyle selectedIconStyle() {
        return SELECTED_ICON;
    }

    private final static TextStyle SELECTED_ICON = TextStyle.EMPTY
            .set(
                    TextStylePropertyName.BACKGROUND_COLOR,
                    Color.parse("#ffff00")
            ).set(
                    TextStylePropertyName.COLOR,
                    Color.parse("#0000ff")
            );

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

    @Override
    public TextStyle viewportAllStyle(final boolean selected) {
        return this.viewportColumnRowHeaderStyle(selected);
    }

    @Override
    public TextStyle viewportCellStyle(final boolean selected) {
        return selected ?
                this.cellSelectedStyle :
                this.cellUnselectedStyle;
    }

    private TextStyle cellSelectedStyle;
    private TextStyle cellUnselectedStyle;

    private final static Color BORDER_COLOR = Color.BLACK;
    private final static BorderStyle BORDER_STYLE = BorderStyle.SOLID;
    private final static Length<?> BORDER_LENGTH = Length.pixel(1.0);

    private final static TextStyle CELL_SELECTED_STYLE;
    private final static TextStyle CELL_UNSELECTED_STYLE;

    static {
        final TextStyle style = TextStyle.EMPTY
                .setMargin(
                        Length.none()
                ).setBorder(
                        BORDER_COLOR,
                        BORDER_STYLE,
                        BORDER_LENGTH

                ).setPadding(
                        Length.none()
                ).set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.LEFT
                ).set(
                        TextStylePropertyName.VERTICAL_ALIGN,
                        VerticalAlign.TOP
                ).set(
                        TextStylePropertyName.FONT_FAMILY,
                        FontFamily.with("MS Sans Serif")
                ).set(
                        TextStylePropertyName.FONT_SIZE,
                        FontSize.with(11)
                ).set(
                        TextStylePropertyName.FONT_STYLE,
                        FontStyle.NORMAL
                ).set(
                        TextStylePropertyName.FONT_WEIGHT,
                        FontWeight.NORMAL
                ).set(
                        TextStylePropertyName.FONT_VARIANT,
                        FontVariant.NORMAL
                ).set(
                        TextStylePropertyName.HYPHENS,
                        Hyphens.NONE
                ).set(
                        TextStylePropertyName.WORD_BREAK,
                        WordBreak.NORMAL
                );
        CELL_SELECTED_STYLE = style.set(
                TextStylePropertyName.BACKGROUND_COLOR,
                Color.parse("#ccc")
        );
        CELL_UNSELECTED_STYLE = style.set(
                TextStylePropertyName.BACKGROUND_COLOR,
                Color.parse("#fff")
        );
    }

    @Override
    public TextStyle viewportColumnHeaderStyle(final boolean selected) {
        return this.viewportColumnRowHeaderStyle(selected);
    }

    @Override
    public TextStyle viewportRowHeaderStyle(final boolean selected) {
        return this.viewportColumnRowHeaderStyle(selected);
    }

    private TextStyle viewportColumnRowHeaderStyle(final boolean selected) {
        return selected ?
                COLUMN_ROW_HEADER_SELECTED_STYLE :
                COLUMN_ROW_HEADER_UNSELECTED_STYLE;
    }

    private final static TextStyle COLUMN_ROW_HEADER_SELECTED_STYLE;
    private final static TextStyle COLUMN_ROW_HEADER_UNSELECTED_STYLE;

    static {
        final TextStyle style = TextStyle.EMPTY
                .setMargin(
                        Length.none()
                ).setBorder(
                        BORDER_COLOR,
                        BORDER_STYLE,
                        BORDER_LENGTH

                ).setPadding(
                        Length.none()
                ).set(
                        TextStylePropertyName.TEXT_ALIGN,
                        TextAlign.CENTER
                ).set(
                        TextStylePropertyName.VERTICAL_ALIGN,
                        VerticalAlign.MIDDLE
                ).set(
                        TextStylePropertyName.FONT_WEIGHT,
                        FontWeight.NORMAL
                );
        COLUMN_ROW_HEADER_SELECTED_STYLE = style.set(
                TextStylePropertyName.BACKGROUND_COLOR,
                Color.parse("#555")
        );
        COLUMN_ROW_HEADER_UNSELECTED_STYLE = style.set(
                TextStylePropertyName.BACKGROUND_COLOR,
                Color.parse("#aaa")
        );
    }

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
