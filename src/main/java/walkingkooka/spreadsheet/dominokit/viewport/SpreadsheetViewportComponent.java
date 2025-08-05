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

import elemental2.dom.DomGlobal;
import elemental2.dom.Element;
import elemental2.dom.Event;
import elemental2.dom.EventTarget;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import elemental2.dom.Headers;
import elemental2.dom.KeyboardEvent;
import elemental2.dom.MouseEvent;
import jsinterop.base.Js;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.icons.MdiIcon;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.predicate.Predicates;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.HistoryTokenAwareComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.cell.SpreadsheetCellLinksComponent;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuTargets;
import walkingkooka.spreadsheet.dominokit.dom.DivComponent;
import walkingkooka.spreadsheet.dominokit.dom.Doms;
import walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.dom.Key;
import walkingkooka.spreadsheet.dominokit.fetcher.FetcherRequestBody;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopSpreadsheetFormatterInfoSetFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetFormatterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellFormatterSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellFormulaHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellFormulaMenuHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellMenuHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellParserSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellStyleSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellValidatorSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetColumnMenuHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetColumnSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowMenuHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.util.HistoryTokenRecorder;
import walkingkooka.spreadsheet.dominokit.reference.SpreadsheetSelectionMenu;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterAliasSet;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterMenu;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterMenuList;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorEdit;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewport;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportNavigation;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportNavigationList;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportRectangle;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportWindows;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStyleProperty;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A ui that displays a table holding the cells and headers for the columns and rows.
 */
public final class SpreadsheetViewportComponent implements HtmlComponent<HTMLDivElement, SpreadsheetViewportComponent>,
    SpreadsheetDeltaFetcherWatcher,
    SpreadsheetFormatterFetcherWatcher,
    NopSpreadsheetFormatterInfoSetFetcherWatcher,
    SpreadsheetMetadataFetcherWatcher,
    HistoryTokenAwareComponentLifecycle,
    SpreadsheetViewportComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    NopEmptyResponseFetcherWatcher {

    /**
     * The maximum number of recent format / parse pattern saves.
     */
    final static int MAX_RECENT_COUNT = 3;

    public static SpreadsheetViewportComponent empty(final SpreadsheetViewportComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetViewportComponent(context);
    }

    private SpreadsheetViewportComponent(final SpreadsheetViewportComponentContext context) {
        this.context = context;

        this.formula = this.createFormula();
        this.formulaCellLinks = this.formulaCellLinks();

        this.table = this.table();

        this.horizontalScrollbarThumb = this.horizontalScrollbarThumb();
        this.horizontalScrollbar = this.horizontalScrollbar();

        this.verticalScrollbarThumb = this.verticalScrollbarThumb();
        this.verticalScrollbar = this.verticalScrollbar();

        this.tableContainer = this.tableContainer();
        this.refreshMetadata = SpreadsheetMetadata.EMPTY;

        this.root = this.root();

        this.recentFormatterSelectors = this.recentCellSaves(
            historyToken -> Optional.ofNullable(
                historyToken instanceof SpreadsheetCellFormatterSaveHistoryToken ?
                    historyToken.cast(SpreadsheetCellFormatterSaveHistoryToken.class)
                        .spreadsheetFormatterSelector()
                        .orElse(null) :
                    null
            ),
            context
        );

        this.spreadsheetFormatterSelectorSelection = null;
        this.spreadsheetFormatterSelectorMenus = null;

        this.recentParserSelectors = this.recentCellSaves(
            historyToken -> Optional.ofNullable(
                historyToken instanceof SpreadsheetCellParserSaveHistoryToken ?
                    historyToken.cast(SpreadsheetCellParserSaveHistoryToken.class)
                        .spreadsheetParserSelector()
                        .orElse(null) :
                    null
            ),
            context
        );

        this.recentValidatorSelectors = this.recentCellSaves(
            historyToken -> Optional.ofNullable(
                historyToken instanceof SpreadsheetCellValidatorSaveHistoryToken ?
                    historyToken.cast(SpreadsheetCellValidatorSaveHistoryToken.class)
                        .value()
                        .orElse(null) :
                    null
            ),
            context
        );

        this.recentTextStyleProperties = HistoryTokenRecorder.with(
            (historyToken) -> Optional.ofNullable(
                historyToken instanceof SpreadsheetCellStyleSaveHistoryToken ?
                    historyToken.cast(SpreadsheetCellStyleSaveHistoryToken.class)
                        .textStyleProperty() :
                    null
            ),
            MAX_RECENT_COUNT
        );
        context.addHistoryTokenWatcher(this.recentTextStyleProperties);

        this.setVisibility(false);

        // SpreadsheetViewportComponent#addHistoryTokenWatcher must happen after recent HistoryTokenWatchers
        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetFormatterFetcherWatcher(this);
        context.addSpreadsheetMetadataFetcherWatcher(this);
        context.addSpreadsheetDeltaFetcherWatcher(this);
    }

    /**
     * Creates a {@link HistoryTokenRecorder} which will keep the most recent saves of {@link SpreadsheetFormatterSelector},
     * {@link SpreadsheetParserSelector} or {@link ValidatorSelector}.
     */
    private <T> HistoryTokenRecorder<T> recentCellSaves(final Function<HistoryToken, Optional<T>> mapper,
                                                        final HistoryContext context) {
        final HistoryTokenRecorder<T> recorder = HistoryTokenRecorder.with(
            mapper,
            MAX_RECENT_COUNT
        );
        context.addHistoryTokenWatcher(recorder);
        return recorder;
    }

    // root.............................................................................................................

    private DivComponent root() {
        final DivComponent root = DivComponent.div();

        // overflow:hidden required to prevent scrollbars...
        root.setCssText("width:100%; border: none; margin: 0px; padding: none; overflow: hidden");

        // The display:flex on $formulaCellLinks is necessary otherwise the badges will all be grouped to the right of all the links.
        // giving
        // link1  link2  link3  badge1  badge2
        // rather than
        // link1 badge1  link2 badge2 link3
        root.appendChild(
            HtmlElementComponent.div()
                .setCssText("position: relative;")
                .appendChild(this.formula)
                .appendChild(this.formulaCellLinks.setCssText("position:absolute; bottom: 0px; right: 15px; height: fit-content; display:flex;"))
        );
        root.appendChild(this.tableContainer);

        root.addMouseOverListener(
            e -> this.onMouseEvent(
                Js.cast(e)
            )
        );

        root.addKeyDownListener(
            e -> this.onKeyEvent(
                Js.cast(e)
            )
        );

        root.addKeyUpListener(
            e -> this.onKeyEvent(
                Js.cast(e)
            )
        );

        return root;
    }

    /**
     * The root or container that holds the {@link SpreadsheetViewportFormulaComponent} and {@link #table}.
     */
    private final DivComponent root;

    private void onMouseEvent(final MouseEvent event) {
        this.setShiftKeyDown(event.shiftKey);
    }

    private void onKeyEvent(final KeyboardEvent event) {
        this.setShiftKeyDown(event.shiftKey);
    }

    private void setShiftKeyDown(final boolean shiftKeyDown) {
        if (this.shiftKeyDown != shiftKeyDown) {
            this.shiftKeyDown = shiftKeyDown;
            // refresh the column and row links if the SHIFT key up/down changed.
            this.refreshIfOpen(this.context);
        }
    }

    /**
     * True when the SHIFT key is down. Column and Row headers will create {@link SpreadsheetViewportNavigation#extendColumn(SpreadsheetColumnReference)} etc rather than {@link SpreadsheetViewportNavigation#column(SpreadsheetColumnReference)}, navigations.
     */
    boolean shiftKeyDown;

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetViewportComponent setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.root.setCssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public SpreadsheetViewportComponent setCssProperty(final String name,
                                                       final String value) {
        this.root.setCssProperty(
            name,
            value
        );
        return this;
    }

    // IsElement........................................................................................................

    /**
     * The root DIV element holding the formula and TABLE holding all headers and cells.
     */
    @Override
    public HTMLDivElement element() {
        return this.root.element();
    }

    // formulaComponent.................................................................................................

    /**
     * Factory that creates the {@link SpreadsheetViewportFormulaComponent}
     */
    private SpreadsheetViewportFormulaComponent createFormula() {
        return SpreadsheetViewportFormulaComponent.with(this.context)
            .addContextMenu(this::onFormulaContextMenu);
    }

    /**
     * Getter that returns the embedded {@link SpreadsheetViewportFormulaComponent}.
     */
    public SpreadsheetViewportFormulaComponent formula() {
        return this.formula;
    }

    private final SpreadsheetViewportFormulaComponent formula;

    private void onFormulaContextMenu(final Event event) {
        event.preventDefault();

        final SpreadsheetViewportComponentContext context = this.context;

        context.pushHistoryToken(
            context.historyToken()
                .menu()
        );
    }

    /**
     * Place formula links at the bottom/right of formula component.
     */
    private SpreadsheetCellLinksComponent formulaCellLinks() {
        return SpreadsheetCellLinksComponent.empty(
            ID_PREFIX,
            this.context
        );
    }

    private final SpreadsheetCellLinksComponent formulaCellLinks;

    /**
     * Refreshes the value of {@link #formulaCellLinks} from the current {@link HistoryToken}.
     */
    private void formulaCellLinksRefresh() {
        final SpreadsheetCellLinksComponent formulaCellLinks = this.formulaCellLinks;

        final HistoryToken historyToken = this.context.historyToken();
        final boolean show = historyToken instanceof SpreadsheetCellFormulaHistoryToken;

        formulaCellLinks.setVisibility(show);

        if (show) {
            formulaCellLinks.setValue(
                historyToken.selection()
                    .flatMap(s ->
                        Optional.ofNullable(
                            s.isExternalReference() ?
                                s.toExpressionReference() :
                                null
                        )
                    )
            );
        }
    }

    // table container..................................................................................................

    private DivComponent tableContainer() {
        final DivComponent container = HtmlElementComponent.div();
        container.setCssText("position: relative; top: 0; left 0px; border: none; margin: 0px; padding: none; width:100%;");
        container.appendChild(this.table);

        container.appendChild(this.horizontalScrollbar);
        container.appendChild(this.horizontalScrollbarLeft());
        container.appendChild(this.horizontalScrollbarRight());

        container.appendChild(this.verticalScrollbar);
        container.appendChild(this.verticalScrollbarUp());
        container.appendChild(this.verticalScrollbarDown());

        return container;
    }

    private final DivComponent tableContainer;

    // table............................................................................................................

    private SpreadsheetViewportComponentTable table() {
        return SpreadsheetViewportComponentTable.empty(
                SpreadsheetViewportComponentSpreadsheetViewportComponentTableContext.with(
                    this
                )
            ).addClickListener(
                (event) -> onTableClickEvent(
                    Js.cast(event)
                )
            ).addContextMenuListener(this::onTableContextMenu)
            .addKeyDownListener(
                (event) -> onTableKeyDownEvent(
                    Js.cast(event)
                )
            ).addFocusInListener(
                (event) -> this.focused = true
            ).addFocusOutListener(
                (event) -> this.focused = false
            );
    }

    // focus ...........................................................................................................

    /**
     * When true indicates that some part of the viewport has FOCUS.
     */
    private boolean focused;

    // click ...........................................................................................................

    private void onTableClickEvent(final MouseEvent event) {
        event.preventDefault();

        final EventTarget eventTarget = event.target;
        if (eventTarget instanceof Element) {
            this.findSelectionAndNavigate(
                Js.cast(eventTarget),
                event.shiftKey
            );
        }
    }

    /**
     * Attempts to find the matching {@link SpreadsheetSelection} and adds to the navigations.
     */
    private void findSelectionAndNavigate(final Element element,
                                          final boolean shiftKeyDown) {
        final SpreadsheetViewportComponentContext context = this.context;

        Element walk = element;
        for (; ; ) {
            if (null == walk || walk.tagName.equalsIgnoreCase("TABLE")) {
                break;
            }

            final Optional<SpreadsheetSelection> maybeSelection = parseElementId(walk.id);
            if (maybeSelection.isPresent()) {
                final SpreadsheetSelection selection = maybeSelection.get();
                if (selection.isCell()) {
                    final SpreadsheetCellReference cell = selection.toCell();

                    this.onNavigation(
                        shiftKeyDown ?
                            SpreadsheetViewportNavigation.extendCell(
                                cell
                            ) :
                            SpreadsheetViewportNavigation.cell(
                                cell
                            ),
                        context
                    );
                    break;
                }
            }

            walk = walk.parentElement;
        }
    }

    // key down.........................................................................................................

    /**
     * Generic key event handler that handles any key events for cell/column OR row.
     */
    private void onTableKeyDownEvent(final KeyboardEvent event) {
        event.preventDefault();

        final boolean shifted = event.shiftKey;
        final SpreadsheetViewportComponentContext context = this.context;

        SpreadsheetViewportNavigation navigation = null;
        switch (Key.fromEvent(event)) {
            case ArrowLeft:
                navigation = shifted ?
                    SpreadsheetViewportNavigation.extendLeftColumn() :
                    SpreadsheetViewportNavigation.leftColumn();
                break;
            case ArrowUp:
                navigation = shifted ?
                    SpreadsheetViewportNavigation.extendUpRow() :
                    SpreadsheetViewportNavigation.upRow();
                break;
            case ArrowRight:
                navigation = shifted ?
                    SpreadsheetViewportNavigation.extendRightColumn() :
                    SpreadsheetViewportNavigation.rightColumn();
                break;
            case ArrowDown:
                navigation = shifted ?
                    SpreadsheetViewportNavigation.extendDownRow() :
                    SpreadsheetViewportNavigation.downRow();
                break;
            case Enter:
                // if cell then edit formula
                this.focused = false;
                context.pushHistoryToken(
                    context.historyToken()
                        .formula()
                );
                break;
            case Escape:
                // clear any selection
                context.pushHistoryToken(
                    context.historyToken()
                        .clearSelection()
                );
                break;
            default:
                // ignore other keys
                break;
        }

        if (null != navigation) {
            this.onNavigation(
                navigation,
                context
            );
        }
    }

    // context menu.....................................................................................................

    /**
     * First tries to find the outer parent element be it a column/row or cell trying to find an id and then extracting the
     * selection from the id. Once this is found, the context menu is updated with the {@link SpreadsheetSelection}.
     */
    private void onTableContextMenu(final Event event) {
        event.preventDefault();

        final EventTarget eventTarget = event.target;
        if (eventTarget instanceof Element) {
            Element element = Js.cast(eventTarget);

            for (; ; ) {
                if (null == element || element.tagName.equalsIgnoreCase("TABLE")) {
                    break;
                }

                final Optional<SpreadsheetSelection> maybeSelection = parseElementId(element.id);
                if (maybeSelection.isPresent()) {
                    final SpreadsheetSelection selection = maybeSelection.get();

                    final SpreadsheetViewportComponentContext context = this.context;

                    context.pushHistoryToken(
                        context.historyToken()
                            .menu(
                                Optional.of(selection),
                                context.spreadsheetViewportCache()
                            )
                    );
                    break;
                }

                // try again
                element = element.parentElement;
            }
        }
    }

    /**
     * Renders a drop down menu, provided the selection is in the current viewport TABLE.
     */
    private void renderContextMenu(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                   final RefreshContext context) {
        final Element element;

        if (historyToken instanceof SpreadsheetCellFormulaMenuHistoryToken) {
            element = this.formula.element();
        } else {
            final AnchoredSpreadsheetSelection anchored = historyToken.anchoredSelection();

            element = this.findElement(
                this.context.spreadsheetViewportCache()
                    .resolveIfLabelOrFail(
                        anchored.selection()
                    ).focused(anchored.anchor())
            ).orElse(null);
        }

        if (null != element) {
            final SpreadsheetContextMenu menu = SpreadsheetContextMenu.wrap(
                SpreadsheetContextMenuTargets.element(element),
                context
            );

            List<SpreadsheetFormatterMenu> spreadsheetFormatterSelectorMenus = this.spreadsheetFormatterSelectorMenus;
            if (null == spreadsheetFormatterSelectorMenus) {
                spreadsheetFormatterSelectorMenus = Lists.empty();
            }

            SpreadsheetSelectionMenu.build(
                historyToken,
                menu,
                SpreadsheetViewportComponentSpreadsheetSelectionMenuContext.with(
                    this.recentFormatterSelectors.values(),
                    spreadsheetFormatterSelectorMenus,
                    this.recentParserSelectors.values(),
                    this.recentTextStyleProperties.values(),
                    this.recentValidatorSelectors.values(),
                    this.context
                )
            );

            menu.focus();
        }
    }

    /**
     * Watches {@link HistoryToken} events for {@link SpreadsheetFormatterSelector} saves adding new entry.
     */
    private final HistoryTokenRecorder<SpreadsheetFormatterSelector> recentFormatterSelectors;

    /**
     * The {@link SpreadsheetExpressionReference} that was used to fetch {@link SpreadsheetFormatterMenu}.
     * This is used to detect selection changes,
     */
    private SpreadsheetExpressionReference spreadsheetFormatterSelectorSelection;

    /**
     * This should be updated each time the {@link SpreadsheetMetadataPropertyName#FORMATTERS} property changes.
     */
    private List<SpreadsheetFormatterMenu> spreadsheetFormatterSelectorMenus;

    /**
     * Watches {@link HistoryToken} events for {@link SpreadsheetParserSelector} saves adding new entry.
     */
    private final HistoryTokenRecorder<SpreadsheetParserSelector> recentParserSelectors;

    /**
     * Watches {@link HistoryToken} events for {@link TextStyleProperty} saves adding new entry.
     */
    private final HistoryTokenRecorder<TextStyleProperty<?>> recentTextStyleProperties;

    /**
     * Watches {@link HistoryToken} events for {@link ValidatorSelector} saves adding new entry.
     */
    private final HistoryTokenRecorder<ValidatorSelector> recentValidatorSelectors;

    /**
     * A TABLE that holds the grid of cells including the column and row headers.
     */
    private final SpreadsheetViewportComponentTable table;

    /**
     * The ID assigned to the container TABLE element.
     */
    final static String ID = "viewport";

    /**
     * Prefix for any ui within a viewport
     */
    final static String ID_PREFIX = ID + "-";

    // horizontal-scrollbar..............................................................................................

    private DivComponent horizontalScrollbar() {
        return this.scrollbar(
            "h-scrollbar",
            "left: 0px; bottom: 0px; width: calc(100% - " + (SCROLLBAR_LENGTH + BUTTON_LENGTH * 2 - 5) + "px);height:" + SCROLLBAR_LENGTH + "px; flex-flow: row; border-radius: 0 10px 10px 0;",
            this.horizontalScrollbarThumb,
            this::horizontalScrollbarOnClick
        );
    }

    private final DivComponent horizontalScrollbar;

    private DivComponent verticalScrollbar() {
        return this.scrollbar(
            "v-scrollbar",
            "top: 0px; right: 0px; height: calc(100% - " + (SCROLLBAR_LENGTH + BUTTON_LENGTH * 2 - 5) + "px); width: " + SCROLLBAR_LENGTH + "px; flex-flow: column; border-radius: 0 0 10px 10px;",
            this.verticalScrollbarThumb,
            this::verticalScrollbarOnClick
        );
    }

    private final DivComponent verticalScrollbar;

    private DivComponent scrollbar(final String idSuffix,
                                   final String cssText,
                                   final DivComponent thumb,
                                   final Consumer<MouseEvent> click) {
        final DivComponent scrollbar = HtmlElementComponent.div();

        scrollbar.setId(ID_PREFIX + idSuffix);
        scrollbar.setCssText("position: absolute; display: flex;" + cssText + "border-width: 2px;border-color: black;border-style: solid;padding: 2px;background-color: #aaa;");

        return scrollbar.appendChild(thumb)
            .addClickListener(
                e -> click.accept((MouseEvent) e)
            );
    }

    private void horizontalScrollbarOnClick(final MouseEvent event) {
        event.preventDefault();

        final int width = this.horizontalScrollbar.element()
            .offsetWidth;

        final double clientX = event.clientX;
        final double leftClientX = this.horizontalScrollbarThumbLeft * width * 0.01;
        final SpreadsheetViewportNavigation navigation = clientX < leftClientX ?
            SpreadsheetViewportNavigation.leftPixel(width) :
            SpreadsheetViewportNavigation.rightPixel(width);

        this.context.debug("SpreadsheetViewportComponent.horizontalScrollbarOnClick clientX: " + clientX + "< " + leftClientX + " " + navigation);

        this.onNavigation(
            navigation,
            this.context
        );
    }

    private void verticalScrollbarOnClick(final MouseEvent event) {
        event.preventDefault();

        final int height = this.horizontalScrollbar.element()
            .offsetHeight;

        final double clientY = event.clientY;
        final double topClientY = this.verticalScrollbarThumbTop * height * 0.01;
        final SpreadsheetViewportNavigation navigation = clientY < topClientY ?
            SpreadsheetViewportNavigation.upPixel(height) :
            SpreadsheetViewportNavigation.downPixel(height);

        this.context.debug("SpreadsheetViewportComponent.horizontalScrollbarOnClick clientY: " + clientY + "< " + topClientY + " " + navigation);

        this.onNavigation(
            navigation,
            this.context
        );
    }

    private DivComponent horizontalScrollbarThumb() {
        return scrollbarThumb(
            "h-scrollbar-thumb",
            "0%",
            "90%"
        );
    }

    private final DivComponent horizontalScrollbarThumb;

    private DivComponent verticalScrollbarThumb() {
        return scrollbarThumb(
            "v-scrollbar-thumb",
            "90%",
            "0%"
        );
    }

    private final DivComponent verticalScrollbarThumb;

    private DivComponent scrollbarThumb(final String idSuffix,
                                        final String width,
                                        final String height) {
        final DivComponent thumb = HtmlElementComponent.div();
        thumb.setId(ID_PREFIX + idSuffix);
        thumb.setCssText("position: absolute; width:" + width + "; height: " + height + "; box-sizing: border-box; border-color: black; border-style: solid; border-width: 1px; background-color: #fff; border-radius: " + (SCROLLBAR_LENGTH / 3) + "px");
        return thumb;
    }

    private HTMLElement horizontalScrollbarLeft() {
        return scrollbarArrow(
            "h-scrollbar-left",
            SpreadsheetIcons.arrowLeft(),
            "right: " + (SCROLLBAR_LENGTH + BUTTON_LENGTH - 8) + "px; bottom: -10px;",
            () -> SpreadsheetViewportNavigation.leftPixel(this.tableCellsWidth() - 1)
        );
    }

    private HTMLElement verticalScrollbarUp() {
        return scrollbarArrow(
            "v-scrollbar-up",
            SpreadsheetIcons.arrowUp(),
            "bottom: " + (SCROLLBAR_LENGTH + BUTTON_LENGTH - 3) + "px; right: -16px;",
            () -> SpreadsheetViewportNavigation.upPixel(this.tableCellsHeight() - 1)
        );
    }

    private HTMLElement horizontalScrollbarRight() {
        return scrollbarArrow(
            "h-scrollbar-right",
            SpreadsheetIcons.arrowRight(),
            "right: " + (SCROLLBAR_LENGTH - 8) + "px; bottom: -10px;",
            () -> SpreadsheetViewportNavigation.rightPixel(this.tableCellsWidth() - 1)
        );
    }

    private HTMLElement verticalScrollbarDown() {
        return scrollbarArrow(
            "v-scrollbar-down",
            SpreadsheetIcons.arrowDown(),
            "bottom: " + (SCROLLBAR_LENGTH - 3) + "px; right: -16px;",
            () -> SpreadsheetViewportNavigation.downPixel(this.tableCellsHeight() - 1)
        );
    }

    private HTMLElement scrollbarArrow(final String idSuffix,
                                       final MdiIcon icon,
                                       final String css,
                                       final Supplier<SpreadsheetViewportNavigation> navigation) {
        final Button button = Button.create(icon)
            .circle();

        final HTMLElement element = button.element();
        element.id = ID_PREFIX + idSuffix;
        element.tabIndex = 0;

        element.style.cssText = "position: absolute;" + css + "width: " + BUTTON_LENGTH + "px; height: " + BUTTON_LENGTH + "px";

        button.addClickListener(
            (e) -> this.onNavigation(
                navigation.get(),
                this.context
            )
        );

        return element;
    }

    /**
     * Updates the coordinates and dimensions of both the horizontal and vertical scrollbar thumbs. The calculations are
     * done using the last window.
     */
    private void scrollbarsRefresh() {
        final SpreadsheetViewportCache cache = this.spreadsheetViewportCache();
        final Optional<SpreadsheetCellRangeReference> maybeLast = cache.windows()
            .last();
        final OptionalInt maybeColumnCount = cache.columnCount();
        final OptionalInt maybeRowCount = cache.rowCount();

        final DivComponent horizontalScrollbarThumb = this.horizontalScrollbarThumb;
        final DivComponent verticalScrollbarThumb = this.verticalScrollbarThumb;

        if (maybeLast.isPresent() && maybeColumnCount.isPresent() && maybeRowCount.isPresent()) {
            final SpreadsheetCellRangeReference last = maybeLast.get();
            final SpreadsheetCellReference topLeft = last.begin();

            final int left = topLeft.column()
                .value();
            final int top = topLeft.row()
                .value();

            final int columnCount = maybeColumnCount.getAsInt();
            final int rowCount = maybeRowCount.getAsInt();

            final SpreadsheetViewportComponentThumbnails thumbnails = SpreadsheetViewportComponentThumbnails.compute(
                last,
                columnCount,
                rowCount
            );

            final float hLeft = thumbnails.left;
            final float hWidth = thumbnails.width;

            horizontalScrollbarThumb
                .setDisplay("visible")
                .setLeft(
                    hLeft + "%"
                )
                .setWidth(
                    hWidth + "%"
                );

            final float vTop = thumbnails.top;
            final float vHeight = thumbnails.height;

            verticalScrollbarThumb.setDisplay("visible")
                .setTop(
                    vTop + "%"
                )
                .setHeight(
                    vHeight + "%"
                );

            this.horizontalScrollbarThumbLeft = hLeft;
            this.verticalScrollbarThumbTop = vTop;

            context.debug("SpreadsheetViewportComponent.scrollbarsRefresh " + last + " left: " + left + " top: " + top + " hLeft: " + hLeft + " hWidth: " + hWidth + " vTop: " + vTop + " vHeight: " + vHeight);
        } else {
            horizontalScrollbarThumb.setDisplay("hidden");
            verticalScrollbarThumb.setDisplay("hidden");

            this.horizontalScrollbarThumbLeft = Integer.MAX_VALUE;
            this.verticalScrollbarThumbTop = Integer.MAX_VALUE;
        }
    }

    /**
     * The css position left percentage for the horizontal scrollbar thumb. This will be used to determine whether clicks on the horizontal scrollbar are before or after the thumb.
     */
    private float horizontalScrollbarThumbLeft;

    /**
     * The css position top percentage for the vertical scrollbar thumb. This will be used to determine whether clicks on the vertical scrollbar are before or after the thumb.
     */
    private float verticalScrollbarThumbTop;

    // misc.............................................................................................................

    public void setWidthAndHeight(final int width,
                                  final int height) {
        final boolean reload = width > this.width || height > this.height;

        final SpreadsheetViewportComponentContext context = this.context;
        context.debug("SpreadsheetViewportComponent.setWidthAndHeight " + width + "x" + height + " was " + this.width + "x" + this.height + " reload: " + reload);

        this.width = width;
        this.height = height;

        this.reload = reload;

        this.tableContainer.element()
            .style.cssText = "width: " + this.width() + "px; height: " + this.height() + "px; overflow: hidden; position: relative;";

        this.table.setWidth(this.tableWidth());
        this.table.setHeight(this.tableHeight());

        this.loadViewportCellsIfNecessary();
    }

    public SpreadsheetViewport viewport(final Optional<AnchoredSpreadsheetSelection> anchoredSelection) {
        final SpreadsheetCellReference home = this.context.spreadsheetMetadata()
            .getOrFail(SpreadsheetMetadataPropertyName.VIEWPORT)
            .rectangle()
            .home();
        return home.viewportRectangle(
                this.tableCellsWidth(),
                this.tableCellsHeight()
            ).viewport()
            .setAnchoredSelection(anchoredSelection);
    }

    private int width() {
        return this.width;
    }

    private int tableWidth() {
        return this.width() - SCROLLBAR_LENGTH;
    }

    private int tableCellsWidth() {
        return this.tableWidth() -
            (int) ROW_WIDTH.pixelValue() -
            SCROLLBAR_LENGTH;
    }

    private int height() {
        return this.height - this.formula.element()
            .offsetHeight;
    }

    private int tableHeight() {
        return this.height() -
            SCROLLBAR_LENGTH;
    }

    private int tableCellsHeight() {
        return this.tableHeight() -
            (int) COLUMN_HEIGHT.pixelValue();
    }

    final static Length<?> COLUMN_HEIGHT = Length.pixel(30.0);

    final static Length<?> ROW_WIDTH = Length.pixel(80.0);

    private final static int BUTTON_LENGTH = 50;

    private final static int SCROLLBAR_LENGTH = 32;

    /**
     * The width allocated to the widget.
     */
    private int width;

    /**
     * The height allocated to the widget.
     */
    private int height;

    // giveViewportSelectionFocus......................................................................................

    private void giveViewportSelectionFocus(final AnchoredSpreadsheetSelection selection,
                                            final RefreshContext context) {
        final SpreadsheetSelection nonLabelSelection = this.spreadsheetViewportCache()
            .resolveIfLabelOrFail(
                selection.selection()
            );
        final SpreadsheetSelection spreadsheetSelection = nonLabelSelection.focused(
            selection.anchor()
        );
        final Optional<Element> maybeElement = this.findElement(
            spreadsheetSelection
        );
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
                if (spreadsheetSelection.isColumn() || spreadsheetSelection.isRow()) {
                    element = element.firstElementChild;
                }

                context.debug("SpreadsheetViewportComponent.giveViewportSelectionFocus " + spreadsheetSelection + " focus element " + element);
                element.focus();
            }
        } else {
            context.debug("SpreadsheetViewportComponent.giveViewportSelectionFocus " + spreadsheetSelection + " element not found!");
        }
    }

    // HistoryTokenAwareComponentLifecycle..............................................................................................

    @Override
    public boolean isOpen() {
        return this.open;
    }

    @Override
    public void open(final RefreshContext context) {
        this.open = true;
        this.setVisibility(true);
    }

    @Override
    public void refresh(final RefreshContext context) {
        final HistoryToken historyToken = context.historyToken();
        final Optional<AnchoredSpreadsheetSelection> maybeAnchorSelection = historyToken.anchoredSelectionOrEmpty();

        // if selection changes, fetch the new formatter menus
        {
            SpreadsheetExpressionReference spreadsheetFormatterSelectorSelection = this.spreadsheetFormatterSelectorSelection;
            List<SpreadsheetFormatterMenu> spreadsheetFormatterSelectorMenus = this.spreadsheetFormatterSelectorMenus;

            if (historyToken instanceof SpreadsheetCellHistoryToken) {
                if (historyToken instanceof SpreadsheetCellSelectHistoryToken) {
                    final SpreadsheetCellSelectHistoryToken spreadsheetCellSelectHistoryToken = historyToken.cast(SpreadsheetCellSelectHistoryToken.class);
                    final SpreadsheetExpressionReference currentSelection = spreadsheetCellSelectHistoryToken.selection()
                        .map(SpreadsheetSelection::toExpressionReference)
                        .orElse(null);
                    if (null != currentSelection) {
                        if (false == currentSelection.equalsIgnoreReferenceKind(spreadsheetFormatterSelectorSelection)) {
                            spreadsheetFormatterSelectorSelection = currentSelection;
                            spreadsheetFormatterSelectorMenus = Lists.empty(); // will be replaced in #onSpreadsheetFormatterMenuList

                            this.context.spreadsheetFormatterFetcher()
                                .getCellFormatterMenu(
                                    spreadsheetCellSelectHistoryToken.id(),
                                    currentSelection
                                );
                        }
                    } else {
                        spreadsheetFormatterSelectorSelection = null;
                        spreadsheetFormatterSelectorMenus = null;
                    }
                }
            } else {
                spreadsheetFormatterSelectorSelection = null;
                spreadsheetFormatterSelectorMenus = null;
            }

            this.spreadsheetFormatterSelectorSelection = spreadsheetFormatterSelectorSelection;
            this.spreadsheetFormatterSelectorMenus = spreadsheetFormatterSelectorMenus;
        }

        this.refreshTable(maybeAnchorSelection);

        if (historyToken instanceof SpreadsheetCellSelectHistoryToken ||
            historyToken instanceof SpreadsheetColumnSelectHistoryToken ||
            historyToken instanceof SpreadsheetRowSelectHistoryToken) {
            this.giveViewportSelectionFocus(
                maybeAnchorSelection.get(),
                context
            );
        }

        if (historyToken instanceof SpreadsheetCellMenuHistoryToken ||
            historyToken instanceof SpreadsheetCellFormulaMenuHistoryToken ||
            historyToken instanceof SpreadsheetColumnMenuHistoryToken ||
            historyToken instanceof SpreadsheetRowMenuHistoryToken) {
            this.renderContextMenu(
                historyToken.cast(SpreadsheetAnchoredSelectionHistoryToken.class),
                context
            );
        }

        this.formulaCellLinksRefresh();
        this.scrollbarsRefresh();
    }

    private void refreshTable(final Optional<AnchoredSpreadsheetSelection> maybeAnchorSelection) {
        final SpreadsheetMetadata metadata = this.context.spreadsheetMetadata();
        final SpreadsheetViewportCache cache = this.context.spreadsheetViewportCache();
        final SpreadsheetViewportWindows windows = cache.windows();

        Predicate<SpreadsheetSelection> selected = Predicates.never();

        if (maybeAnchorSelection.isPresent()) {
            // special case for label
            final SpreadsheetSelection selectionNotLabel = cache.resolveIfLabelOrFail(
                maybeAnchorSelection.get()
                    .selection()
            );

            // is not cell-range required otherwise select-all-component will always be rendered as anchorSelection.
            selected = (s) -> selectionNotLabel.equalsIgnoreReferenceKind(s) ||
                (false == s.isCellRange() && selectionNotLabel.test(s));
        }

        this.hideZeroValues = metadata.getOrFail(SpreadsheetMetadataPropertyName.HIDE_ZERO_VALUES);
        this.defaultCellStyle = metadata.effectiveStyle()
            .merge(SpreadsheetViewportComponentTableCell.CELL_STYLE);
        this.mustRefresh = metadata.shouldViewRefresh(this.refreshMetadata);
        this.spreadsheetViewport = this.spreadsheetViewport();

        this.table.refresh(
            metadata.id()
                .get(),
            metadata.getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_NAME),
            windows,
            selected
        );
        this.refreshMetadata = metadata;
    }

    private SpreadsheetMetadata refreshMetadata;

    boolean hideZeroValues;

    TextStyle defaultCellStyle;

    boolean mustRefresh;

    /**
     * Cached {@link SpreadsheetViewport} shared by {@link SpreadsheetViewportComponentSpreadsheetViewportComponentTableContext}.
     */
    SpreadsheetViewport spreadsheetViewport;

    @Override
    public void openGiveFocus(final RefreshContext context) {
        // nop
    }

    @Override
    public void close(final RefreshContext context) {
        this.setVisibility(false);
        this.open = false;

        // need to "clear" cached metadata this will force loadViewportCells to happen when a new SpreadsheetMetadata.id appears
        this.metadata = SpreadsheetMetadata.EMPTY;
        this.refreshMetadata = SpreadsheetMetadata.EMPTY;
    }

    private boolean open;

    @Override
    public boolean shouldLogLifecycleChanges() {
        return true;
    }

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return false;
    }

    // SpreadsheetViewportComponentLifecycle............................................................................

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        return this.metadata;
    }

    @Override
    public SpreadsheetViewportCache spreadsheetViewportCache() {
        return this.context.spreadsheetViewportCache();
    }

    // Fetcher..........................................................................................................

    @Override
    public void onBegin(final HttpMethod method,
                        final Url url,
                        final Optional<FetcherRequestBody<?>> body,
                        final AppContext context) {
        // nop
    }

    @Override
    public void onFailure(final HttpMethod method,
                          final AbsoluteOrRelativeUrl url,
                          final HttpStatus status,
                          final Headers headers,
                          final String body,
                          final AppContext context) {
        // if loading SpreadsheetId failed, force a reload.
        if (HttpMethod.GET.equals(method)) {
            final Optional<SpreadsheetId> maybeId = SpreadsheetMetadataFetcher.extractSpreadsheetId(url);
            if (maybeId.isPresent()) {
                if (url.equals(SpreadsheetMetadataFetcher.url(maybeId.get()))) {
                    this.reload = true;

                }
            }
        }
    }

    @Override
    public void onError(final Object cause,
                        final AppContext context) {
        // nop
    }

    // delta............................................................................................................

    @Override
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta,
                                   final AppContext context) {
        Objects.requireNonNull(delta, "delta");

        final Optional<SpreadsheetViewport> maybeSpreadsheetViewport = delta.viewport();
        if (maybeSpreadsheetViewport.isPresent()) {
            this.synchronizeHistoryToken(
                maybeSpreadsheetViewport.get(),
                context
            );
        }
        this.componentLifecycleHistoryTokenQuery(context);
        this.loadViewportCellsIfNecessary();
    }

    /**
     * Reacts to new or different {@link AnchoredSpreadsheetSelection} from a {@link SpreadsheetDelta} response,
     * pushing the {@link AnchoredSpreadsheetSelection} to the {@link HistoryToken} which results in the navigation
     * being updated and followed.
     */
    private void synchronizeHistoryToken(final SpreadsheetViewport viewport,
                                         final AppContext context) {
        if (this.focused) {
            // before pushing history token need to update the AppContext.metadata
            final HistoryToken historyToken = context.historyToken();

            final HistoryToken withSelection = historyToken
                .clearSelection()
                .setAnchoredSelection(
                    viewport.anchoredSelection()
                );

            context.debug(
                this.getClass().getSimpleName() +
                    ".synchronizeHistoryToken different selection from history token @" +
                    withSelection
            );
            context.pushHistoryToken(withSelection);
        }
    }

    // SpreadsheetFormatterFetcherWatcher...............................................................................

    @Override
    public void onSpreadsheetFormatterSelectorEdit(final SpreadsheetId id,
                                                   final Optional<SpreadsheetExpressionReference> cellOrLabel,
                                                   final SpreadsheetFormatterSelectorEdit edit,
                                                   final AppContext context) {
        // nop
    }

    @Override
    public void onSpreadsheetFormatterMenuList(final SpreadsheetId id,
                                               final SpreadsheetExpressionReference cellOrLabel,
                                               final SpreadsheetFormatterMenuList menus,
                                               final AppContext context) {
        this.spreadsheetFormatterSelectorSelection = cellOrLabel;
        this.spreadsheetFormatterSelectorMenus = menus;
        this.refreshIfOpen(context);
    }

    // metadata.........................................................................................................

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas,
                                         final AppContext context) {
        // NOP
    }

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        Objects.requireNonNull(metadata, "metadata");

        if (metadata.shouldViewRefresh(this.metadata)) {
            this.reload = true;
        }

        boolean fetchSpreadsheetFormatterSelectorsMenu = false;

        // will be null initially
        if (null == this.spreadsheetFormatterSelectorMenus) {
            fetchSpreadsheetFormatterSelectorsMenu = true;
        } else {
            if (this.reload) {
                fetchSpreadsheetFormatterSelectorsMenu = true; // cant hurt to "reload"
            } else {
                // if formatters changed better reload formatter menu
                final Optional<SpreadsheetFormatterAliasSet> oldSpreadsheetFormatterSelectors = metadata.get(SpreadsheetMetadataPropertyName.FORMATTERS);
                final Optional<SpreadsheetFormatterAliasSet> newSpreadsheetFormatterSelectors = this.metadata.get(SpreadsheetMetadataPropertyName.FORMATTERS);
                fetchSpreadsheetFormatterSelectorsMenu = false == oldSpreadsheetFormatterSelectors.equals(newSpreadsheetFormatterSelectors);
            }
        }
        this.metadata = metadata;

        this.loadViewportCellsIfNecessary();

        // the returned metadata isnt any different from the current metadata skip rendering again.
        if (this.reload) {
            this.componentLifecycleHistoryTokenQuery(context);
        }

        if (fetchSpreadsheetFormatterSelectorsMenu) {
            this.spreadsheetFormatterSelectorSelection = null;
            this.spreadsheetFormatterSelectorMenus = null;

            this.refreshIfOpen(context);
        }
    }

    /**
     * Initial metadata is EMPTY or nothing.
     */
    private SpreadsheetMetadata metadata = SpreadsheetMetadata.EMPTY;

    /**
     * Tests if various requirements are ready and the viewport should be loaded again.
     */
    private void loadViewportCellsIfNecessary() {
        final SpreadsheetViewportComponentContext context = this.context;

        if (context.spreadsheetDeltaFetcher().waitingRequestCount() == 0) {
            final boolean reload = this.reload;
            final int width = this.width;
            final int height = this.height;

            final SpreadsheetMetadata metadata = context.spreadsheetMetadata();
            if (reload && width > 0 && height > 0 && metadata.isNotEmpty()) {
                if (metadata.isEmpty()) {
                    context.debug("SpreadsheetViewportComponent.loadViewportCellsIfNecessary waiting for metadata");
                } else {
                    this.loadViewportCells();
                }
            } else {
                context.debug("SpreadsheetViewportComponent.loadViewportCellsIfNecessary not ready, reload: " + reload + " width: " + width + " height: " + height + " metadata.isEmpty: " + metadata.isEmpty() + " open " + this.open);
            }
        }
    }

    /**
     * Unconditionally Loads all the cells to fill the viewport using the {@link #navigations} buffer. Assumes that a metadata with id is present.
     */
    public void loadViewportCells() {
        final SpreadsheetViewportComponentContext context = this.context;

        final SpreadsheetId id = context.spreadsheetMetadata()
            .getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_ID);

        final SpreadsheetViewport viewport = this.spreadsheetViewport()
            .setNavigations(this.navigations);

        context.debug("SpreadsheetViewportComponent.loadViewportCells id: " + id + " viewport: " + viewport);

        this.reload = false;
        this.navigations = SpreadsheetViewportNavigationList.EMPTY;

        context.spreadsheetDeltaFetcher()
            .getCells(
                id,
                viewport
            );
    }

    /**
     * Returns a {@link SpreadsheetViewport} with the currently active HOME and width/height.
     */
    private SpreadsheetViewport spreadsheetViewport() {
        SpreadsheetViewport viewport = this.context.spreadsheetMetadata()
            .getOrFail(SpreadsheetMetadataPropertyName.VIEWPORT);
        final SpreadsheetViewportRectangle rectangle = viewport.rectangle();
        return viewport.setRectangle(
            rectangle.setWidth(
                this.tableCellsWidth()
            ).setHeight(
                this.tableCellsHeight()
            )
        );
    }

    /**
     * Accepts and adds the given {@link SpreadsheetViewportNavigation} to the buffer and if possible
     * sends it to the server for actioning.
     */
    private void onNavigation(final SpreadsheetViewportNavigation navigation,
                              final SpreadsheetViewportComponentContext context) {
        Objects.requireNonNull(navigation, "navigation");
        Objects.requireNonNull(context, "context");

        this.navigations = this.navigations.concat(navigation);
        this.reload = true;
        this.loadViewportCellsIfNecessary();

    }

    /**
     * A buffer which fills up with {@link SpreadsheetViewportNavigation} entries such as keyboard cursor key movements
     * or clicking the horizontal or vertical scrollbars. This is useful so multiple navigation actions are batched
     * when outstanding fetches are in flight so they are sent once, rather than sending a fetch for each.
     */
    private SpreadsheetViewportNavigationList navigations = SpreadsheetViewportNavigationList.EMPTY;

    /**
     * Initially false, this will become true, when the metadata for a new spreadsheet is loaded and a resize event happens.
     */
    private boolean reload = false;

    /**
     * Helper that finds the {@link Element} for the given {@link SpreadsheetSelection}
     */
    private Optional<Element> findElement(final SpreadsheetSelection selection) {
        final Element element = DomGlobal.document
            .getElementById(
                SpreadsheetViewportComponent.id(
                    this.context.spreadsheetViewportCache()
                        .resolveIfLabelOrFail(selection)
                )
            );

        return Optional.ofNullable(element);
    }

    final SpreadsheetViewportComponentContext context;

    // helpers..........................................................................................................

    // viewport-column-A
    public static String id(final SpreadsheetSelection selection) {
        return ID_PREFIX +
            selection.textLabel().toLowerCase() +
            "-" +
            selection.toString().toUpperCase();
    }

    /**
     * Takes an id hopefully sourced from a {@link SpreadsheetViewportComponent} descendant element and tries to extract a {@link SpreadsheetSelection}.
     * <br>
     * This is the inverse of {@link #id(SpreadsheetSelection)}.
     */
    static Optional<SpreadsheetSelection> parseElementId(final String id) {
        SpreadsheetSelection selection = null;

        if (null != id && id.startsWith(ID_PREFIX)) {
            // viewport-cell-A1 -> cell-A1
            final String selectionTypeAndSelection = id.substring(ID_PREFIX.length());
            final int dash = selectionTypeAndSelection.indexOf('-');
            if (-1 != dash) {
                final Function<String, SpreadsheetSelection> parser;

                switch (selectionTypeAndSelection.substring(0, dash)) {
                    case "cell":
                        parser = SpreadsheetSelection::parseCell;
                        break;
                    case "column":
                        parser = SpreadsheetSelection::parseColumn;
                        break;
                    case "row":
                        parser = SpreadsheetSelection::parseRow;
                        break;
                    default:
                        parser = null;
                        break;
                }
                if (null != parser) {
                    try {
                        selection = parser.apply(
                            selectionTypeAndSelection.substring(dash + 1)
                        );
                    } catch (final RuntimeException ignore) {
                        // nop
                    }
                }
            }
        }

        return Optional.ofNullable(selection);
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        throw new UnsupportedOperationException();
    }
}
