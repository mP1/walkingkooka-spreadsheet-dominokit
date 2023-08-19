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
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.layout.AppLayout;
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
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetSelectionHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.UnknownHistoryToken;
import walkingkooka.spreadsheet.dominokit.label.SpreadsheetLabelMappingEditorComponent;
import walkingkooka.spreadsheet.dominokit.label.SpreadsheetLabelMappingEditorComponentContexts;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContexts;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaWatchers;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingWatchers;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataWatchers;
import walkingkooka.spreadsheet.dominokit.pattern.SpreadsheetPatternEditorComponent;
import walkingkooka.spreadsheet.dominokit.pattern.SpreadsheetPatternEditorComponentContexts;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportComponent;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportToolbarComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
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
public class App implements EntryPoint, AppContext, HistoryTokenWatcher,
        SpreadsheetMetadataWatcher,
        SpreadsheetDeltaWatcher,
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
        this.metadataWatchers = SpreadsheetMetadataWatchers.empty();
        this.spreadsheetMetadataFetcher = SpreadsheetMetadataFetcher.with(
                this.metadataWatchers,
                this
        );
        this.addSpreadsheetMetadataWatcher(this);

        // delta
        this.spreadsheetDeltaWatchers = SpreadsheetDeltaWatchers.empty();
        this.spreadsheetDeltaFetcher = SpreadsheetDeltaFetcher.with(
                this.spreadsheetDeltaWatchers,
                this
        );
        this.addSpreadsheetDeltaWatcher(this);

        // labelMapping
        this.spreadsheetLabelMappingWatchers = SpreadsheetLabelMappingWatchers.empty();
        this.spreadsheetLabelMappingFetcher = SpreadsheetLabelMappingFetcher.with(
                this.spreadsheetLabelMappingWatchers,
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
        this.spreadsheetPatternEditorComponent();
    }

    private void spreadsheetPatternEditorComponent() {
        final SpreadsheetPatternEditorComponent component = SpreadsheetPatternEditorComponent.with(
                SpreadsheetPatternEditorComponentContexts.cell(this)
        );
        this.addSpreadsheetDeltaWatcher(
                (delta, context) -> component.refreshIfOpen(this)
        );
        this.addSpreadsheetMetadataWatcher(
                (metadata, context) -> component.refreshIfOpen(this)
        );
    }

    // header = metadata toggle | clickable(editable) spreadsheet name
    // right = editable metadata properties
    // content = toolbar
    //   formula,
    //   table holding spreadsheet cells
    private final AppLayout layout = AppLayout.create();

    @Override
    public void onModuleLoad() {
        this.prepareLayout();

        this.setSpreadsheetName("Untitled 123");
        this.showMetadataPanel(false);
        this.fireInitialHistoryToken();
        this.fireInitialWindowSize();
    }

    // layout...........................................................................................................

    private void prepareLayout() {
        final AppLayout layout = this.layout;

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

        DomGlobal.document.body.append(
                layout.element()
        );
    }

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
    public Runnable addSpreadsheetDeltaWatcher(final SpreadsheetDeltaWatcher watcher) {
        return this.spreadsheetDeltaWatchers.add(watcher);
    }

    @Override
    public Runnable addSpreadsheetDeltaWatcherOnce(final SpreadsheetDeltaWatcher watcher) {
        return this.spreadsheetDeltaWatchers.addOnce(watcher);
    }

    /**
     * A collection of listeners for {@link SpreadsheetDeltaWatcher}
     */
    private final SpreadsheetDeltaWatchers spreadsheetDeltaWatchers;

    /**
     * If a viewport selection is present then copy the received selection even if its now gone.
     */
    @Override
    public void onSpreadsheetDelta(final SpreadsheetDelta delta,
                                   final AppContext context) {
        final HistoryToken historyToken = context.historyToken();

        // if a selection is already present copy from the metadata
        if (historyToken instanceof SpreadsheetSelectionHistoryToken) {
            final HistoryToken withViewportSelection = historyToken.setViewportSelection(
                    delta.viewportSelection()
            );

            if (false == historyToken.equals(withViewportSelection)) {
                context.debug("App.onSpreadsheetDelta selection active, updating " + withViewportSelection, delta);
                context.pushHistoryToken(
                        withViewportSelection
                );
            }
        }
    }

    // SpreadsheetLabelMapping..........................................................................................

    @Override
    public Runnable addSpreadsheetLabelMappingWatcher(final SpreadsheetLabelMappingWatcher watcher) {
        return this.spreadsheetLabelMappingWatchers.add(watcher);
    }

    @Override
    public Runnable addSpreadsheetLabelMappingWatcherOnce(final SpreadsheetLabelMappingWatcher watcher) {
        return this.spreadsheetLabelMappingWatchers.addOnce(watcher);
    }

    /**
     * A collection of listeners for {@link SpreadsheetLabelMappingWatcher}
     */
    private final SpreadsheetLabelMappingWatchers spreadsheetLabelMappingWatchers;

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
    public Runnable addSpreadsheetMetadataWatcher(final SpreadsheetMetadataWatcher watcher) {
        return this.metadataWatchers.add(watcher);
    }

    @Override
    public Runnable addSpreadsheetMetadataWatcherOnce(final SpreadsheetMetadataWatcher watcher) {
        return this.metadataWatchers.add(watcher);
    }

    /**
     * A collection of listeners for {@link SpreadsheetMetadataWatcher}
     */
    private final SpreadsheetMetadataWatchers metadataWatchers;

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
            final HistoryToken idNameViewportSelectionHistoryToken = historyToken
                    .setIdAndName(
                            id,
                            name
                    ).setViewportSelection(
                            metadata.get(SpreadsheetMetadataPropertyName.SELECTION)
                    );

            if (false == historyToken.equals(idNameViewportSelectionHistoryToken)) {
                context.debug("App.onSpreadsheetMetadata from " + historyToken + " to different id/name/viewportSelection " + idNameViewportSelectionHistoryToken, metadata);
                context.pushHistoryToken(idNameViewportSelectionHistoryToken);
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

    private void showMetadataPanel(final boolean show) {
        this.layout.getRightDrawer()
                .toggleDisplay(show);
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
        this.debug("App.onHistoryTokenChange token change " + token);

        final HistoryToken previousToken = this.previousToken;
        this.previousToken = token;

        if (false == token.equals(previousToken)) {
            if (token instanceof UnknownHistoryToken) {
                this.debug("App.onHistoryTokenChange updated with invalid token " + token + ", will restore previous " + previousToken);
                this.pushHistoryToken(previousToken);

            } else {
                this.debug("App.onHistoryTokenChange from " + previousToken + " to " + token);

                this.historyWatchers.onHistoryTokenChange(
                        previousToken,
                        this
                );
            }
        }
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

    private final HistoryTokenWatchers historyWatchers;

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {

        // if the viewport selection changed update metadata
        final HistoryToken historyToken = context.historyToken();
        if (historyToken instanceof SpreadsheetIdHistoryToken) {
            final Optional<SpreadsheetViewportSelection> viewportSelection = historyToken.viewportSelectionOrEmpty();
            final Optional<SpreadsheetViewportSelection> previousViewportSelection = previous.viewportSelectionOrEmpty();
            if (false == viewportSelection.equals(previousViewportSelection)) {

                context.debug("App.onHistoryTokenChange viewportSelection changed from " + previousViewportSelection.orElse(null) + " TO " + viewportSelection.orElse(null) + " will update Metadata");

                final SpreadsheetIdHistoryToken spreadsheetIdHistoryToken = (SpreadsheetIdHistoryToken) historyToken;
                context.spreadsheetMetadataFetcher()
                        .patchMetadata(
                                spreadsheetIdHistoryToken.id(),
                                SpreadsheetMetadataPropertyName.SELECTION.patch(
                                        viewportSelection.orElse(null)
                                )
                        );
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
        return CELL_STYLE.set(
                TextStylePropertyName.BACKGROUND_COLOR,
                selected ? CELL_SELECTED : CELL_UNSELECTED
        );
    }

    private final static Color BORDER_COLOR = Color.BLACK;
    private final static BorderStyle BORDER_STYLE = BorderStyle.SOLID;
    private final static Length<?> BORDER_LENGTH = Length.pixel(1.0);

    /**
     * THe default style for the TD holding the formatted cell.
     */
    private final static TextStyle CELL_STYLE = TextStyle.EMPTY
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
            ); // overflow-wrap ?

    // TODO get these from theme
    private final static Color CELL_SELECTED = Color.parse("#ccc");
    private final static Color CELL_UNSELECTED = Color.parse("#fff");

    @Override
    public TextStyle viewportColumnHeaderStyle(final boolean selected) {
        return this.viewportColumnRowHeaderStyle(selected);
    }

    @Override
    public TextStyle viewportRowHeaderStyle(final boolean selected) {
        return this.viewportColumnRowHeaderStyle(selected);
    }

    private TextStyle viewportColumnRowHeaderStyle(final boolean selected) {
        return COLUMN_ROW_STYLE.set(
                TextStylePropertyName.BACKGROUND_COLOR,
                selected ? COLUMN_ROW_SELECTED : COLUMN_ROW_UNSELECTED
        );
    }

    private final static TextStyle COLUMN_ROW_STYLE = TextStyle.EMPTY
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

    // TODO get these from theme
    private final static Color COLUMN_ROW_SELECTED = Color.parse("#555");
    private final static Color COLUMN_ROW_UNSELECTED = Color.parse("#aaa");

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
