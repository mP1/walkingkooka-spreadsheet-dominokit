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

package walkingkooka.spreadsheet.dominokit.ui.viewport;

import elemental2.dom.DomGlobal;
import elemental2.dom.Element;
import elemental2.dom.Event;
import elemental2.dom.EventTarget;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLTableElement;
import elemental2.dom.Headers;
import elemental2.dom.KeyboardEvent;
import elemental2.dom.MouseEvent;
import jsinterop.base.Js;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.elements.DivElement;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.predicate.Predicates;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetViewportRectangle;
import walkingkooka.spreadsheet.SpreadsheetViewportWindows;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dom.Doms;
import walkingkooka.spreadsheet.dominokit.dom.Key;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellFindHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHighlightSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellMenuHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellPatternSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetColumnMenuHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetColumnSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowMenuHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.util.HistoryTokenRecorder;
import walkingkooka.spreadsheet.dominokit.net.NopNoResponseWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.ui.ComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetCellFind;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetViewportComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.ui.contextmenu.SpreadsheetContextMenuTargets;
import walkingkooka.spreadsheet.dominokit.ui.selectionmenu.SpreadsheetSelectionMenu;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportNavigation;
import walkingkooka.tree.text.Length;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A ui that displays a table holding the cells and headers for the columns and rows.
 */
public final class SpreadsheetViewportComponent implements HtmlElementComponent<HTMLDivElement, SpreadsheetViewportComponent>,
        SpreadsheetDeltaFetcherWatcher,
        SpreadsheetLabelMappingFetcherWatcher,
        SpreadsheetMetadataFetcherWatcher,
        ComponentLifecycle,
        SpreadsheetViewportComponentLifecycle,
        LoadedSpreadsheetMetadataRequired,
        NopNoResponseWatcher {

    /**
     * The maximum number of recent format / parse pattern saves.
     */
    final static int MAX_RECENT_COUNT = 3;

    public static SpreadsheetViewportComponent empty(final AppContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetViewportComponent(context);
    }

    private SpreadsheetViewportComponent(final AppContext context) {
        this.context = context;

        this.formulaComponent = this.formula();

        this.table = this.table();

        this.horizontalScrollbarThumb = this.horizontalScrollbarThumb();
        this.horizontalScrollbar = this.horizontalScrollbar();

        this.verticalScrollbarThumb = this.verticalScrollbarThumb();
        this.verticalScrollbar = this.verticalScrollbar();

        this.tableContainer = this.tableContainer();
        this.refreshMetadata = SpreadsheetMetadata.EMPTY;

        this.root = this.root();

        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetLabelMappingWatcher(this);
        context.addSpreadsheetMetadataWatcher(this);
        context.addSpreadsheetDeltaWatcher(this);

        this.recentFormatPatterns = this.recentPatternSaves(
                true, // is format true
                context
        );
        this.recentParsePatterns = this.recentPatternSaves(
                false, // is format false
                context
        );

        this.setVisibility(false);
    }

    /**
     * Creates a {@link HistoryTokenRecorder} which will keep the most recent saves of a {@link SpreadsheetPattern}.
     */
    private HistoryTokenRecorder recentPatternSaves(final boolean format,
                                                    final HistoryTokenContext context) {
        final HistoryTokenRecorder recorder = HistoryTokenRecorder.with(
                format ?
                        SpreadsheetViewportComponent::isFormatPatternSave :
                        SpreadsheetViewportComponent::isParsePatternSave,
                MAX_RECENT_COUNT
        );
        context.addHistoryTokenWatcher(recorder);
        return recorder;
    }

    private static boolean isFormatPatternSave(final HistoryToken token) {
        return isPatternSave(
                token,
                true // is format
        );
    }

    private static boolean isParsePatternSave(final HistoryToken token) {
        return isPatternSave(
                token,
                false // is parse
        );
    }

    /**
     * Because there are two {@link HistoryTokenRecorder} one for format patterns and the other for parse patterns,
     * a {@link Predicate} is required to filter just the one type for each recorder.
     */
    private static boolean isPatternSave(final HistoryToken token,
                                         final boolean format) {
        boolean keep = false;

        if (token instanceof SpreadsheetCellPatternSaveHistoryToken) {
            final SpreadsheetCellPatternSaveHistoryToken patternSaveHistoryToken = token.cast(SpreadsheetCellPatternSaveHistoryToken.class);

            final SpreadsheetPatternKind kind = patternSaveHistoryToken.patternKind().get();
            if (kind.isFormatPattern() == format) {
                if (patternSaveHistoryToken.pattern().isPresent()) {
                    keep = true;
                }
            }
        }

        return keep;
    }

    // root.............................................................................................................

    private DivElement root() {
        final DivElement root = ElementsFactory.elements.div();

        // overflow:hidden required to prevent scrollbars...
        root.style("width:100%; border: none; margin: 0px; padding: none; overflow: hidden");

        root.appendChild(this.formulaComponent);
        root.appendChild(this.tableContainer);

        root.addEventListener(
                EventType.mouseover.getName(),
                e -> this.onMouseEvent(
                        Js.cast(e)
                )
        );

        root.addEventListener(
                EventType.keydown.getName(),
                e -> this.onKeyEvent(
                        Js.cast(e)
                )
        );

        root.addEventListener(
                EventType.keyup.getName(),
                e -> this.onKeyEvent(
                        Js.cast(e)
                )
        );

        return root;
    }

    /**
     * The root or container that holds the {@link SpreadsheetViewportFormulaComponent} and {@link #table}.
     */
    private final DivElement root;

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
    private boolean shiftKeyDown;

    // IsElement........................................................................................................

    /**
     * The root DIV element holding the formula and TABLE holding all headers and cells.
     */
    @Override
    public HTMLDivElement element() {
        return this.root.element();
    }

    // formulaComponent.................................................................................................

    private SpreadsheetViewportFormulaComponent formula() {
        return SpreadsheetViewportFormulaComponent.with(this.context);
    }

    private final SpreadsheetViewportFormulaComponent formulaComponent;

    // table container..................................................................................................

    private DivElement tableContainer() {
        final DivElement container = ElementsFactory.elements.div();
        container.style("position: relative; top: 0; left 0px; border: none; margin: 0px; padding: none; width:100%;");
        container.appendChild(this.table);

        container.appendChild(this.horizontalScrollbar);
        container.appendChild(this.horizontalScrollbarLeft());
        container.appendChild(this.horizontalScrollbarRight());

        container.appendChild(this.verticalScrollbar);
        container.appendChild(this.verticalScrollbarUp());
        container.appendChild(this.verticalScrollbarDown());

        return container;
    }

    private final DivElement tableContainer;

    // table............................................................................................................

    private SpreadsheetViewportComponentTable table() {
        final SpreadsheetViewportComponentTable table = SpreadsheetViewportComponentTable.empty(this.context);

        final HTMLTableElement element = table.element();

        this.addClickEventListener(element);
        this.addKeyDownEventListener(element);
        this.addContextMenuEventListener(element);

        return table;
    }

    // click ...........................................................................................................

    /**
     * Registers a click event handler on the given {@link Element table} and computes the selected {@link SpreadsheetCellReference}.
     */
    private void addClickEventListener(final Element element) {
        element.addEventListener(
                EventType.click.getName(),
                (event) -> onClickEvent(
                        Js.cast(event)
                )
        );
    }

    private void onClickEvent(final MouseEvent event) {
        event.preventDefault();

        final EventTarget eventTarget = event.target;
        if (eventTarget instanceof Element) {
            Element element = Js.cast(eventTarget);

            for (; ; ) {
                if (null == element || element.tagName.equalsIgnoreCase("TABLE")) {
                    break;
                }

                final Optional<SpreadsheetSelection> maybeSelection = parseId(element.id);
                if (maybeSelection.isPresent()) {
                    final SpreadsheetSelection selection = maybeSelection.get();
                    if (selection.isCellReference()) {
                        final SpreadsheetCellReference cell = selection.toCell();

                        this.onNavigation(
                                event.shiftKey ?
                                        SpreadsheetViewportNavigation.extendCell(
                                                cell
                                        ) :
                                        SpreadsheetViewportNavigation.cell(
                                                cell
                                        ),
                                this.context
                        );
                        break;
                    }
                }

                element = element.parentElement;
            }
        }
    }

    // key down.........................................................................................................

    /**
     * Registers a keydown event handler on the given {@link Element}.
     */
    private void addKeyDownEventListener(final Element element) {
        element.addEventListener(
                EventType.keydown.getName(),
                (event) -> onKeyDownEvent(
                        Js.cast(event)
                )
        );
    }

    /**
     * Generic key event handler that handles any key events for cell/column OR row.
     */
    private void onKeyDownEvent(final KeyboardEvent event) {
        event.preventDefault();

        final boolean shifted = event.shiftKey;
        final AppContext context = this.context;

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
                context.pushHistoryToken(
                        context.historyToken()
                                .setFormula()
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

    private void addContextMenuEventListener(final Element element) {
        element.addEventListener(
                EventType.contextmenu.getName(),
                this::onContextMenu
        );
    }

    /**
     * First tries to find the outer parent element be it a column/row or cell trying to find an id and then extracting the
     * selection from the id. Once this is found, the context menu is updated with the {@link SpreadsheetSelection}.
     */
    private void onContextMenu(final Event event) {
        event.preventDefault();

        final EventTarget eventTarget = event.target;
        if (eventTarget instanceof Element) {
            Element element = Js.cast(eventTarget);

            for (; ; ) {
                if (null == element || element.tagName.equalsIgnoreCase("TABLE")) {
                    break;
                }

                final Optional<SpreadsheetSelection> maybeSelection = parseId(element.id);
                if (maybeSelection.isPresent()) {
                    final SpreadsheetSelection selection = maybeSelection.get();

                    final AppContext context = this.context;

                    context.pushHistoryToken(
                            context.historyToken()
                                    .setMenu(
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
                                   final AppContext context) {
        final AnchoredSpreadsheetSelection anchored = historyToken.anchoredSelection();
        final SpreadsheetSelection selection = anchored.selection();
        final Optional<Element> maybeElement = this.findElement(
                context.spreadsheetViewportCache()
                        .resolveIfLabel(
                                selection
                        ).focused(anchored.anchor()),
                context
        );

        if (maybeElement.isPresent()) {
            final SpreadsheetContextMenu menu = SpreadsheetContextMenu.wrap(
                    SpreadsheetContextMenuTargets.element(
                            maybeElement.get()
                    ),
                    context
            );

            SpreadsheetSelectionMenu.build(
                    historyToken,
                    menu,
                    SpreadsheetViewportComponentSpreadsheetSelectionMenuContext.with(
                            this.recentFormatPatterns.tokens(),
                            this.recentParsePatterns.tokens(),
                            context
                    )
            );

            menu.focus();
        }
    }

    private final HistoryTokenRecorder recentFormatPatterns;

    private final HistoryTokenRecorder recentParsePatterns;

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

    private DivElement horizontalScrollbar() {
        return this.scrollbar(
                "h-scrollbar",
                "left: 0px; bottom: 0px; width: calc(100% - " + (SCROLLBAR_LENGTH + BUTTON_LENGTH * 2 - 5) + "px);height:" + SCROLLBAR_LENGTH + "px; flex-flow: row; border-radius: 0 10px 10px 0;",
                this.horizontalScrollbarThumb,
                this::horizontalScrollbarOnClick
        );
    }

    private final DivElement horizontalScrollbar;

    private DivElement verticalScrollbar() {
        return this.scrollbar(
                "v-scrollbar",
                "top: 0px; right: 0px; height: calc(100% - " + (SCROLLBAR_LENGTH + BUTTON_LENGTH * 2 - 5) + "px); width: " + SCROLLBAR_LENGTH + "px; flex-flow: column; border-radius: 0 0 10px 10px;",
                this.verticalScrollbarThumb,
                this::verticalScrollbarOnClick
        );
    }

    private final DivElement verticalScrollbar;

    private DivElement scrollbar(final String idSuffix,
                                 final String cssText,
                                 final DivElement thumb,
                                 final Consumer<MouseEvent> click) {
        final DivElement scrollbar = ElementsFactory.elements.div();

        scrollbar.id(ID_PREFIX + idSuffix);
        scrollbar.style("position: absolute; display: flex;" + cssText + "border-width: 2px;border-color: black;border-style: solid;padding: 2px;background-color: #aaa;");

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

    private DivElement horizontalScrollbarThumb() {
        return scrollbarThumb(
                "h-scrollbar-thumb",
                "0%",
                "90%"
        );
    }

    private final DivElement horizontalScrollbarThumb;

    private DivElement verticalScrollbarThumb() {
        return scrollbarThumb(
                "v-scrollbar-thumb",
                "90%",
                "0%"
        );
    }

    private final DivElement verticalScrollbarThumb;

    private DivElement scrollbarThumb(final String idSuffix,
                                      final String width,
                                      final String height) {
        final DivElement thumb = ElementsFactory.elements.div();
        thumb.id(ID_PREFIX + idSuffix);
        thumb.style("position: absolute; width:" + width + "; height: " + height + "; box-sizing: border-box; border-color: black; border-style: solid; border-width: 1px; background-color: #fff; border-radius: " + (SCROLLBAR_LENGTH / 3) + "px");
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
        final AppContext context = this.context;
        final SpreadsheetViewportCache cache = context.spreadsheetViewportCache();
        final Optional<SpreadsheetCellRangeReference> maybeLast = cache.windows()
                .last();
        final OptionalInt maybeColumnCount = cache.columnCount();
        final OptionalInt maybeRowCount = cache.rowCount();

        final DivElement horizontalScrollbarThumb = this.horizontalScrollbarThumb;
        final DivElement verticalScrollbarThumb = this.verticalScrollbarThumb;

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

        final AppContext context = this.context;
        context.debug("SpreadsheetViewportComponent.setWidthAndHeight " + width + "x" + height + " was " + this.width + "x" + this.height + " reload: " + reload);

        this.width = width;
        this.height = height;

        this.reload = reload;

        this.tableContainer.element()
                .style.cssText = "width: " + this.width() + "px; height: " + this.height() + "px; overflow: hidden; position: relative;";

        this.table.setWidth(this.tableWidth());
        this.table.setHeight(this.tableHeight());

        this.loadViewportCellsIfNecessary(context);
    }

    public SpreadsheetViewport viewport(final Optional<AnchoredSpreadsheetSelection> anchoredSelection,
                                        final AppContext context) {
        final SpreadsheetCellReference home = context.spreadsheetMetadata()
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
        return this.height - this.formulaComponent.element()
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
                                            final AppContext context) {
        final SpreadsheetSelection nonLabelSelection = context.spreadsheetViewportCache()
                .resolveIfLabel(
                        selection.selection()
                );
        final SpreadsheetSelection spreadsheetSelection = nonLabelSelection.focused(
                selection.anchor()
        );
        final Optional<Element> maybeElement = this.findElement(
                spreadsheetSelection,
                context
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
                if (spreadsheetSelection.isColumnReference() || spreadsheetSelection.isRowReference()) {
                    element = element.firstElementChild;
                }

                context.debug("SpreadsheetViewportComponent " + spreadsheetSelection + " focus element " + element);
                element.focus();
            }
        } else {
            context.debug("SpreadsheetViewportComponent " + spreadsheetSelection + " element not found!");
        }
    }

    // ComponentLifecycle..............................................................................................

    @Override
    public boolean isOpen() {
        return this.open;
    }

    @Override
    public void open(final AppContext context) {
        this.open = true;
        this.setVisibility(true);
    }

    @Override
    public void refresh(final AppContext context) {
        final HistoryToken historyToken = context.historyToken();
        final Optional<AnchoredSpreadsheetSelection> maybeAnchorSelection = historyToken.anchoredSelectionOrEmpty();

        this.refreshTable(
                maybeAnchorSelection,
                context
        );

        if (historyToken instanceof SpreadsheetCellSelectHistoryToken ||
                historyToken instanceof SpreadsheetColumnSelectHistoryToken ||
                historyToken instanceof SpreadsheetRowSelectHistoryToken) {
            this.giveViewportSelectionFocus(
                    maybeAnchorSelection.get(),
                    context
            );
        }

        if (historyToken instanceof SpreadsheetCellMenuHistoryToken ||
                historyToken instanceof SpreadsheetColumnMenuHistoryToken ||
                historyToken instanceof SpreadsheetRowMenuHistoryToken) {
            this.renderContextMenu(
                    historyToken.cast(SpreadsheetAnchoredSelectionHistoryToken.class),
                    context
            );
        }

        if (historyToken instanceof SpreadsheetCellFindHistoryToken) {
            this.onSpreadsheetCellFindHistoryToken(
                    historyToken.cast(SpreadsheetCellFindHistoryToken.class),
                    context
            );
        }

        if (historyToken instanceof SpreadsheetCellHighlightSaveHistoryToken) {
            context.debug("SpreadsheetViewportComponent.refresh cell highlight save need to loadViewportCells");

            this.reload = true;
            this.loadViewportCells(context);
        }

        this.scrollbarsRefresh();
    }

    private void refreshTable(final Optional<AnchoredSpreadsheetSelection> maybeAnchorSelection,
                              final AppContext context) {
        final SpreadsheetMetadata metadata = context.spreadsheetMetadata();
        final SpreadsheetViewportCache cache = context.spreadsheetViewportCache();
        final SpreadsheetViewportWindows windows = cache.windows();

        Predicate<SpreadsheetSelection> selected = Predicates.never();

        if (maybeAnchorSelection.isPresent()) {
            // special case for label
            final SpreadsheetSelection selectionNotLabel = context.spreadsheetViewportCache()
                    .resolveIfLabel(
                            maybeAnchorSelection.get()
                                    .selection()
                    );

            // is not cell-range required otherwise select-all-component will always be rendered as anchorSelection.
            selected = (s) -> selectionNotLabel.equalsIgnoreReferenceKind(s) ||
                    (false == s.isCellRangeReference() && selectionNotLabel.test(s));
        }

        this.table.refresh(
                metadata.id().get(),
                metadata.getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_NAME),
                windows,
                selected,
                BasicSpreadsheetViewportComponentTableContext.with(
                        context,
                        cache,
                        metadata.getOrFail(SpreadsheetMetadataPropertyName.HIDE_ZERO_VALUES),
                        metadata.effectiveStyle()
                                .merge(SpreadsheetViewportComponentTableCell.CELL_STYLE),
                        metadata.shouldViewRefresh(this.refreshMetadata),
                        this.shiftKeyDown,
                        context
                )
        );
        this.refreshMetadata = metadata;
    }

    private SpreadsheetMetadata refreshMetadata;

    @Override
    public void openGiveFocus(final AppContext context) {
        // nop
    }

    @Override
    public void close(final AppContext context) {
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

    /**
     * Tries to skip reloading the viewport cells if the {@link AppContext#lastCellFind()} is the same or should include
     * all highlights for the current {@link SpreadsheetViewportCache#windows()}.
     */
    private void onSpreadsheetCellFindHistoryToken(final SpreadsheetCellFindHistoryToken historyToken,
                                                   final AppContext context) {
        final SpreadsheetCellFind spreadsheetCellFind = historyToken.find();

        String notRequired = null;

        final SpreadsheetCellFind last = context.lastCellFind();
        if (last.equals(spreadsheetCellFind)) {
            notRequired = " find unchanged " + last;
        } else {
            final SpreadsheetViewportCache cache = context.spreadsheetViewportCache();
            final SpreadsheetSelection selectionNotLabel = cache.resolveIfLabel(
                    historyToken.anchoredSelection()
                            .selection()
            );

            final SpreadsheetViewportWindows windows = cache.windows();

            String reload = "window " + windows + " not within " + selectionNotLabel.toStringMaybeStar();

            if (selectionNotLabel.containsAll(windows)) {
                reload = "offset not empty or 0";

                final OptionalInt offset = spreadsheetCellFind.offset();
                if (false == offset.isPresent() || offset.getAsInt() == 0) {

                    reload = "max not empty or less than window cell count";

                    final long windowsCellCount = windows.count();
                    final OptionalInt max = spreadsheetCellFind.max();

                    if (false == max.isPresent() || max.getAsInt() < windowsCellCount) {
                        reload = null;
                        notRequired = "";
                    }
                }
            }

            if (null != reload) {
                context.debug("SpreadsheetViewportComponent.onHistoryTokenChangeSpreadsheetCellFindHistoryToken load viewport required because " + reload);
                this.reload = true;
                this.loadViewportCells(context);
            }
        }

        if (null != notRequired) {
            context.debug("SpreadsheetViewportComponent.onHistoryTokenChangeSpreadsheetCellFindHistoryToken " + notRequired + " viewport load not required");
        }
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
                        final Optional<String> body,
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
    public void onSpreadsheetDelta(final SpreadsheetId id,
                                   final SpreadsheetDelta delta,
                                   final AppContext context) {
        Objects.requireNonNull(delta, "delta");

        this.componentLifecycleHistoryTokenQuery(context);
    }

    // SpreadsheetLabelMappingFetcherWatcher............................................................................

    /**
     * Any label mapping change should refresh the viewport cells.
     */
    @Override
    public void onSpreadsheetLabelMapping(final SpreadsheetId id,
                                          final Optional<SpreadsheetLabelMapping> mapping,
                                          final AppContext context) {
        this.reload = true; // force a viewport reload.
    }

    // metadata.........................................................................................................

    @Override
    public void onSpreadsheetMetadataList(final List<SpreadsheetMetadata> metadatas,
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

        this.metadata = metadata;

        this.loadViewportCellsIfNecessary(context);

        // the returned metadata isnt any different from the current metadata skip rendering again.
        if (this.reload) {
            this.componentLifecycleHistoryTokenQuery(context);
        }
    }

    /**
     * Initial metadata is EMPTY or nothing.
     */
    private SpreadsheetMetadata metadata = SpreadsheetMetadata.EMPTY;

    /**
     * Tests if various requirements are ready and the viewport should be loaded again.
     */
    private void loadViewportCellsIfNecessary(final AppContext context) {
        final boolean reload = this.reload;

        final int width = this.width;
        final int height = this.height;

        final SpreadsheetMetadata metadata = context.spreadsheetMetadata();
        if (reload && width > 0 && height > 0 && false == metadata.isEmpty()) {
            if (metadata.isEmpty()) {
                context.debug("SpreadsheetViewportComponent.loadViewportCellsIfNecessary waiting for metadata");
            } else {
                this.loadViewportCells(context);
            }
        } else {
            context.debug("SpreadsheetViewportComponent.loadViewportCellsIfNecessary not ready, reload: " + reload + " width: " + width + " height: " + height + " metadata.isEmpty: " + metadata.isEmpty() + " open " + this.open);
        }
    }

    /**
     * Unconditionally Loads all the cells to fill the viewport using the {@link #navigations} buffer. Assumes that a metadata with id is present.
     */
    public void loadViewportCells(final AppContext context) {
        final List<SpreadsheetViewportNavigation> navigations = this.navigations;

        final SpreadsheetId id = context.spreadsheetMetadata()
                .getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_ID);

        final SpreadsheetViewport viewport = this.spreadsheetViewport()
                .setNavigations(navigations);

        context.debug("SpreadsheetViewportComponent.loadViewportCells id: " + id + " viewport: " + viewport);

        context.spreadsheetViewportCache()
                .clear(); // clear all cached data.
        this.reload = false;
        navigations.clear();

        context.addSpreadsheetDeltaWatcherOnce(
                SpreadsheetDeltaFetcherWatchers.pushHistoryTokenViewportSelection()
        );
        context.spreadsheetDeltaFetcher()
                .loadCells(
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
                              final AppContext context) {
        Objects.requireNonNull(navigation, "navigation");
        Objects.requireNonNull(context, "context");

        this.navigations.add(navigation);
        this.reload = true;
        this.loadViewportCellsIfNecessary(context);

    }

    /**
     * A buffer which fills up with {@link SpreadsheetViewportNavigation} entries such as keyboard cursor key movements
     * or clicking the horizontal or vertical scrollbars. This is useful so multiple navigation actions are batched
     * when outstanding fetches are in flight so they are sent once, rather than sending a fetch for each.
     */
    private final List<SpreadsheetViewportNavigation> navigations = Lists.array();

    /**
     * Initially false, this will become true, when the metadata for a new spreadsheet is loaded and a resize event happens.
     */
    private boolean reload = false;

    /**
     * Helper that finds the {@link Element} for the given {@link SpreadsheetSelection}
     */
    private Optional<Element> findElement(final SpreadsheetSelection selection,
                                          final AppContext context) {
        final Element element = DomGlobal.document
                .getElementById(
                        SpreadsheetViewportComponent.id(
                                context.spreadsheetViewportCache()
                                        .resolveIfLabel(selection)
                        )
                );

        return Optional.ofNullable(element);
    }

    private final AppContext context;

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
    static Optional<SpreadsheetSelection> parseId(final String id) {
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
}
