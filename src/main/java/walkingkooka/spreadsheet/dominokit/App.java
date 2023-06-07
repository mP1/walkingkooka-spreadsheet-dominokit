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
import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.dominokit.domino.ui.layout.Layout;
import org.gwtproject.core.client.Scheduler;
import org.gwtproject.core.client.Scheduler.ScheduledCommand;
import org.jboss.elemento.EventType;
import walkingkooka.color.Color;
import walkingkooka.j2cl.locale.LocaleAware;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.SpreadsheetViewportWindows;
import walkingkooka.spreadsheet.dominokit.dom.Doms;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetIdHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetSelectionHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaWatchers;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataWatchers;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportToolbar;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportWidget;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.text.CharSequences;
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
import java.util.Objects;
import java.util.Optional;

@LocaleAware
public class App implements EntryPoint, AppContext, HistoryTokenWatcher, SpreadsheetMetadataWatcher, SpreadsheetDeltaWatcher, UncaughtExceptionHandler {


    public App() {
        SpreadsheetDelta.EMPTY.toString(); // force json register.
    }

    // header = metadata toggle | clickable(editable) spreadsheet name
    // right = editable metadata properties
    // content = toolbar
    //   formula,
    //   table holding spreadsheet cells
    private final Layout layout = Layout.create();

    public void onModuleLoad() {
        this.addSpreadsheetDeltaWatcher(this);
        this.addSpreadsheetMetadataWatcher(this);
        GWT.setUncaughtExceptionHandler(this);

        this.setupHistoryListener();

        this.prepareLayout();

        this.setSpreadsheetName("Untitled 123");
        this.showMetadataPanel(false);

        this.registerWindowResizeListener();
        this.fireInitialHistoryToken();
        this.fireInitialWindowSize();
    }

    // layout...........................................................................................................

    private void prepareLayout() {
        final SpreadsheetViewportToolbar toolbar = SpreadsheetViewportToolbar.create(this);

        final Layout layout = this.layout;
        layout.style()
                .setOverFlowX("hidden")
                .setOverFlowY("hidden");
        layout.fitHeight();
        layout.fitWidth();

        layout.getTopBar()
                .element()
                .append(toolbar.element());

        // remove flex-grow results in the toolbar now being left aligned rather than right.
        Js.<HTMLElement>cast(
                layout.getAppTitle()
                        .parentElement
                        .parentElement
        ).style.set("flex-grow", "");

        layout.setContent(this.viewportWidget.element());

        layout.show();
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
        final Layout layout = this.layout;
        final int navigationBarHeight = layout.getNavigationBar().element().offsetHeight;

        final int newHeight = height - navigationBarHeight;
        this.debug("App.onResize: " + width + " x " + height + " navigationBarHeight: " + navigationBarHeight + " newHeight: " + newHeight);

        this.viewportWidget.setWidthAndHeight(
                width,
                newHeight
        );
    }

    // delta & metadata change watches..................................................................................

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

    /**
     * Update the spreadsheet-id, spreadsheet-name and viewport selection from the given {@link SpreadsheetMetadata}.
     */
    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        // update the global JsonNodeUnmarshallContext.
        this.unmarshallContext = JsonNodeUnmarshallContexts.basic(
                metadata.expressionNumberKind(),
                metadata.mathContext()
        );

        final Optional<SpreadsheetId> id = metadata.id();
        final Optional<SpreadsheetName> name = metadata.name();

        if (id.isPresent() && name.isPresent()) {
            final HistoryToken historyToken = context.historyToken();
            final HistoryToken idNameViewportSelectionHistoryToken = historyToken
                    .setIdAndName(
                            id.get(),
                            name.get()
                    ).setViewportSelection(
                            metadata.get(SpreadsheetMetadataPropertyName.SELECTION)
                    );

            if (false == historyToken.equals(idNameViewportSelectionHistoryToken)) {
                context.debug("App.onSpreadsheetMetadata from " + historyToken + " to different id/name/viewportSelection " + idNameViewportSelectionHistoryToken, metadata);
                context.pushHistoryToken(idNameViewportSelectionHistoryToken);
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

    private SpreadsheetMetadata spreadsheetMetadata = SpreadsheetMetadata.EMPTY;

    @Override
    public void addSpreadsheetDeltaWatcher(final SpreadsheetDeltaWatcher watcher) {
        this.spreadsheetDeltaWatchers.add(watcher);
    }

    /**
     * A collection of listeners for {@link SpreadsheetDeltaWatcher}
     */
    final SpreadsheetDeltaWatchers spreadsheetDeltaWatchers = SpreadsheetDeltaWatchers.empty();

    @Override
    public void addSpreadsheetMetadataWatcher(final SpreadsheetMetadataWatcher watcher) {
        Objects.requireNonNull(watcher, "watcher");
        this.metadataWatchers.add(watcher);
    }

    /**
     * A collection of listeners for {@link SpreadsheetMetadataWatcher}
     */
    final SpreadsheetMetadataWatchers metadataWatchers = SpreadsheetMetadataWatchers.empty();


    @Override
    public SpreadsheetDeltaFetcher spreadsheetDeltaFetcher() {
        return this.spreadsheetDeltaFetcher;
    }

    private final SpreadsheetDeltaFetcher spreadsheetDeltaFetcher = SpreadsheetDeltaFetcher.with(
            this.spreadsheetDeltaWatchers,
            this
    );

    @Override
    public SpreadsheetMetadataFetcher spreadsheetMetadataFetcher() {
        return this.spreadsheetMetadataFetcher;
    }

    private final SpreadsheetMetadataFetcher spreadsheetMetadataFetcher = SpreadsheetMetadataFetcher.with(
            this.metadataWatchers,
            this
    );

    // misc..........................................................................................................

    public void setSpreadsheetName(final String name) {
        this.layout.setTitle(name);
    }

    private void showMetadataPanel(final boolean show) {
        this.layout.getRightPanel()
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

    private JsonNodeUnmarshallContext unmarshallContext = JsonNodeUnmarshallContexts.basic(
            ExpressionNumberKind.DEFAULT,
            MathContext.DECIMAL32
    );

    // history eventListener............................................................................................

    private void fireInitialHistoryToken() {
        final HistoryToken token = this.historyToken();
        this.debug("App.fireInitialHistoryToken " + token);
        this.onHistoryTokenChange(token);
    }

    private void setupHistoryListener() {
        this.addHistoryWatcher(this);

        DomGlobal.self.addEventListener(
                EventType.hashchange.getName(),
                event -> this.onHistoryTokenChange(
                        this.historyToken()
                )
        );
    }

    private void onHistoryTokenChange(final HistoryToken token) {
            final HistoryToken previousToken = this.previousToken;
            this.debug("App.onHistoryTokenChange from " + previousToken + " to " + token);

            if (false == token.equals(previousToken)) {
                this.historyWatchers.onHistoryTokenChange(
                        previousToken,
                        this
                );

                this.previousToken = this.historyToken();
            }
    }

    // AppContext history...............................................................................................

    /**
     * Pushes the given {@link HistoryToken} to the browser location#hash.
     */
    @Override
    public void pushHistoryToken(final HistoryToken token) {
        Scheduler.get()
                .scheduleDeferred(
                        () -> {
                            final String newHash = "#" + token.urlFragment();
                            final String current = DomGlobal.location.hash;
                            if (false == current.equals(newHash)) {
                                this.debug("App.pushHistoryToken from " + CharSequences.quoteAndEscape(current) + " to " + CharSequences.quoteAndEscape(newHash));

                                DomGlobal.location.hash = newHash;
                            }
                        }
                );
    }

    @Override
    public HistoryToken historyToken() {
        // remove the leading hash if necessary.
        String hash = DomGlobal.location.hash;
        if (false == hash.equals(this.locationHash)) {
            if (hash.startsWith("#")) {
                hash = hash.substring(1);
            }

            this.locationHash = hash;
            this.historyToken = HistoryToken.parse(UrlFragment.parse(hash));
        }

        return this.historyToken;
    }

    /**
     * The original window.location.hash used to produce the {@link #historyToken}.
     */
    private String locationHash;

    /**
     * A cached {@link HistoryToken} for the current {@link DomGlobal#location#locationHash}.
     */
    private HistoryToken historyToken;

    // HistoryWatcher...................................................................................................

    @Override
    public Runnable addHistoryWatcher(final HistoryTokenWatcher watcher) {
        return this.historyWatchers.add(watcher);
    }

    private final HistoryTokenWatchers historyWatchers = HistoryTokenWatchers.empty();

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
    private HistoryToken previousToken = HistoryToken.unknown(UrlFragment.EMPTY);


    // UI...............................................................................................................

    @Override
    public TextStyle selectedIconStyle() {
        return SELECTED_ICON;
    }

    private final static TextStyle SELECTED_ICON = TextStyle.EMPTY.set(
            TextStylePropertyName.BACKGROUND_COLOR,
            Color.parse("#ffff00")
    );

    // Viewport.........................................................................................................

    /**
     * Init here to avoid race conditions with other fields like {@link #metadataWatchers}.
     */
    private final SpreadsheetViewportWidget viewportWidget = SpreadsheetViewportWidget.empty(this);

    @Override
    public void giveFormulaTextBoxFocus() {
        this.debug("App.giveFormulaTextBoxFocus");

        this.giveFocus(
                this.viewportWidget::giveFcrmulaTextBoxFocus
        );
    }

    @Override
    public void setFormula(final SpreadsheetSelection selection) {
        this.viewportWidget.setFormula(selection);
    }

    @Override
    public Optional<SpreadsheetCell> viewportCell(final SpreadsheetSelection selection) {
        return this.viewportWidget.viewportCell(selection);
    }

    @Override
    public SpreadsheetViewportWindows viewportWindow() {
        return this.viewportWidget.window();
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

    @Override
    public Optional<SpreadsheetSelection> nonLabelSelection(final SpreadsheetSelection selection) {
        return this.viewportWidget.nonLabelSelection(selection);
    }

    @Override
    public void giveViewportFocus(final SpreadsheetSelection selection) {
        this.debug("App.giveViewportFocus " + selection);

        this.giveFocus(() -> this.giveViewportFocus0(selection));
    }

    private void giveViewportFocus0(final SpreadsheetSelection selection) {
        final Optional<Element> maybeElement = this.findViewportElement(selection);
        if (maybeElement.isPresent()) {
            Element element = maybeElement.get();

            boolean give = true;

            final Element active = DomGlobal.document.activeElement;
            if (null != active) {
                // verify active element belongs to the same selection. if it does it must have focus so no need to focus again
                give = false == Doms.isOrHasChild(
                        element,
                        active
                );
            }

            if (give) {
                // for column/row the anchor and not the TH/TD should receive focus.
                if (selection.isColumnReference() || selection.isRowReference()) {
                    element = element.firstElementChild;
                }

                this.debug("App.giveViewportFocus0 " + selection + " focus element " + element);
                element.focus();
            }
        } else {
            this.debug("App.giveViewportFocus0 " + selection + " element not found!");
        }
    }

    @Override
    public Optional<Element> findViewportElement(final SpreadsheetSelection selection) {
        Element element = null;
        final Optional<SpreadsheetSelection> maybeNotLabel = this.nonLabelSelection(selection);
        if (maybeNotLabel.isPresent()) {
            element = DomGlobal.document
                    .getElementById(
                            SpreadsheetViewportWidget.id(
                                    selection
                            )
                    );
        }

        return Optional.ofNullable(element);
    }

    // focus............................................................................................................

    /**
     * Schedules giving focus to the {@link Element} if it exists. If multiple attempts are made to give focus in a short
     * period of time an {@link IllegalStateException} will be thrown.
     */
    private void giveFocus(final Runnable giveFocus) {
        this.debug("App.giveFocus " + giveFocus);

        final Runnable existingGiveFocus = this.giveFocus;
        if (null != existingGiveFocus) {
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
        // invoking DomGlobal.console.debug caused GWT compile failures
        //
        //[INFO]                [ERROR] at App.java(719): <source info not available>
        //[INFO]                   com.google.gwt.dev.js.ast.JsExprStmt
        //[INFO]                [ERROR] at com.google.gwt.dev.js.ast.JsProgram(0): var _;
        //[INFO] $wnd.goog = $wnd.goog || {};
        //[INFO] $wnd.goog.global = $wnd.goog.global || $wnd;
        //[INFO] bootstrap();
        //[INFO] [...]
        //[INFO]
        //[INFO]                   com.google.gwt.dev.js.ast.JsGlobalBlock
        final elemental2.dom.Console console = DomGlobal.console;
        console.debug(values);
    }

    @Override
    public void error(final Object... values) {
        // see App.debug
        final elemental2.dom.Console console = DomGlobal.console;
        console.error(values);
    }
}
