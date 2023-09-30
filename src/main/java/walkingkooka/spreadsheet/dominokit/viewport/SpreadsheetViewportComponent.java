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
import elemental2.dom.HTMLTableCellElement;
import elemental2.dom.HTMLTableElement;
import elemental2.dom.HTMLTableRowElement;
import elemental2.dom.HTMLTableSectionElement;
import elemental2.dom.Headers;
import elemental2.dom.KeyboardEvent;
import jsinterop.base.Js;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.elements.DivElement;
import org.dominokit.domino.ui.elements.TBodyElement;
import org.dominokit.domino.ui.elements.TDElement;
import org.dominokit.domino.ui.elements.THElement;
import org.dominokit.domino.ui.elements.TableElement;
import org.dominokit.domino.ui.elements.TableRowElement;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.menu.Menu;
import org.dominokit.domino.ui.menu.direction.DropDirection;
import org.dominokit.domino.ui.menu.direction.MouseBestFitDirection;
import org.dominokit.domino.ui.popover.Tooltip;
import org.dominokit.domino.ui.utils.DominoElement;
import org.dominokit.domino.ui.utils.ElementsFactory;
import org.dominokit.domino.ui.utils.Separator;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.predicate.Predicates;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetError;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.dom.Doms;
import walkingkooka.spreadsheet.dominokit.dom.Key;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellMenuHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetColumnMenuHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetColumnSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowMenuHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetViewportSelectionHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionNavigation;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A component that displays a table holding the cells and headers for the columns and rows.
 */
public final class SpreadsheetViewportComponent implements IsElement<HTMLDivElement>,
        SpreadsheetDeltaFetcherWatcher,
        SpreadsheetLabelMappingFetcherWatcher,
        SpreadsheetMetadataFetcherWatcher,
        ComponentLifecycle {

    public static SpreadsheetViewportComponent empty(final AppContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetViewportComponent(context);
    }

    private SpreadsheetViewportComponent(final AppContext context) {
        this.context = context;

        this.formulaComponent = this.formulaComponent();
        this.tableElement = this.table();
        this.root = this.root();

        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetMetadataWatcher(this);
        context.addSpreadsheetDeltaWatcher(this);
    }

    // root.............................................................................................................

    private DivElement root() {
        final DivElement root = ElementsFactory.elements.div();

        // overflow:hidden required to prevent scrollbars...
        root.style("width:100%; border: none; margin: 0px; padding: none; overflow: hidden");

        root.appendChild(this.formulaComponent);
        root.appendChild(this.tableElement);

        return root;
    }

    /**
     * The root or container that holds the {@link SpreadsheetFormulaComponent} and {@link #tableElement}.
     */
    private final DivElement root;

    // IsElement........................................................................................................

    /**
     * The root DIV element holding the formula and TABLE holding all headers and cells.
     */
    @Override
    public HTMLDivElement element() {
        return this.root.element();
    }

    // formulaComponent.................................................................................................

    private SpreadsheetFormulaComponent formulaComponent() {
        return SpreadsheetFormulaComponent.with(this.context);
    }

    private final SpreadsheetFormulaComponent formulaComponent;

    // table............................................................................................................

    /**
     * Creates an empty table with minimal styling including some placeholder text.
     */
    private TableElement table() {
        final TableElement tableElement = ElementsFactory.elements.table();
        tableElement.setId(VIEWPORT_ID);
        tableElement.style("width: 100%; height: 100%; overflow-x: hidden; overflow-y: hidden;");
        tableElement.appendChild(
                ElementsFactory.elements.tbody()
                        .appendChild(
                                ElementsFactory.elements.tr()
                                        .appendChild(
                                                ElementsFactory.elements.td()
                                                        .appendChild(
                                                                "spreadsheet here"
                                                        ).element()
                                        ).element()
                        ).element()
        );

        final HTMLTableElement element = tableElement.element();

        this.addClickEventListener(element);
        this.addKeyDownEventListener(element);
        this.addContextMenuEventListener(element);

        return tableElement;
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

    private void onClickEvent(final Event event) {
        event.preventDefault();

        final EventTarget eventTarget = event.target;
        if (eventTarget instanceof Element) {
            Element element = Js.cast(eventTarget);

            for (; ; ) {
                if (null == element || element.tagName.equalsIgnoreCase("TABLE")) {
                    break;
                }

                final String id = element.id;
                if (null == id) {
                    element = element.parentElement;
                    continue;
                }

                parseId(id)
                        .ifPresent(
                                selection -> {
                                    if (selection.isCellReference()) {
                                        final AppContext context = this.context;

                                        final SpreadsheetMetadata metadata = context.spreadsheetMetadata();
                                        final Optional<SpreadsheetName> spreadsheetName = metadata.name();
                                        final Optional<SpreadsheetId> spreadsheetId = metadata.id();

                                        if (spreadsheetId.isPresent() && spreadsheetName.isPresent()) {
                                            context.pushHistoryToken(
                                                    HistoryToken.cell(
                                                            spreadsheetId.get(),
                                                            spreadsheetName.get(),
                                                            selection.setDefaultAnchor()

                                                    )
                                            );
                                        }
                                    }
                                    ;
                                }
                        );
                break;
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

        SpreadsheetViewportSelectionNavigation navigation = null;
        switch (Key.fromEvent(event)) {
            case ArrowLeft:
                navigation = shifted ?
                        SpreadsheetViewportSelectionNavigation.extendLeftColumn() :
                        SpreadsheetViewportSelectionNavigation.leftColumn();
                break;
            case ArrowUp:
                navigation = shifted ?
                        SpreadsheetViewportSelectionNavigation.extendUpRow() :
                        SpreadsheetViewportSelectionNavigation.upRow();
                break;
            case ArrowRight:
                navigation = shifted ?
                        SpreadsheetViewportSelectionNavigation.extendRightColumn() :
                        SpreadsheetViewportSelectionNavigation.rightColumn();
                break;
            case ArrowDown:
                navigation = shifted ?
                        SpreadsheetViewportSelectionNavigation.extendDownRow() :
                        SpreadsheetViewportSelectionNavigation.downRow();
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
                                .setViewportSelection(
                                        Optional.empty()
                                )
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
                (event) -> this.onContextMenu(event)
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

                final String id = element.id;
                if (null == id) {
                    element = element.parentElement;
                    continue;
                }

                parseId(id).ifPresent(
                        selection -> {
                            final AppContext context = this.context;

                            context.pushHistoryToken(
                                    context.historyToken()
                                            .setMenu(
                                                    Optional.of(selection)
                                            )
                            );
                        }
                );
                break;
            }
        }
    }

    /**
     * Renders a drop down menu.
     */
    private void renderContextMenu(final SpreadsheetViewportSelectionHistoryToken historyToken,
                                   final AppContext context) {
        // show context setMenu1
        final SpreadsheetViewportSelection viewportSelection = historyToken.viewportSelection();
        final Optional<Element> maybeElement = this.findElement(
                viewportSelection.selection()
                        .focused(viewportSelection.anchor()),
                context
        );

        context.debug("SpreadsheetViewportComponent.renderContextMenu " + viewportSelection);

        if (maybeElement.isPresent()) {
            final DominoElement<?> element = new DominoElement<>(maybeElement.get());

            // CLEAR
            // DELETE
            // -------
            // FREEZE
            // UNFREEZE

            final Menu<Void> menu = Menu.<Void>create()
                    .setContextMenu(true)
                    .setDropDirection(new MouseBestFitDirection())
                    .setTargetElement(element)
                    .appendChild(
                            context.menuItem(
                                    "Clear",
                                    Optional.of(
                                            historyToken.setClear()
                                    )
                            )
                    ).appendChild(
                            context.menuItem(
                                    "Delete",
                                    Optional.of(
                                            historyToken.setDelete()
                                    )
                            )
                    ).appendChild(new Separator())
                    .appendChild(
                            context.menuItem(
                                    "Freeze",
                                    historyToken.freezeOrEmpty()
                            )
                    ).appendChild(
                            context.menuItem(
                                    "Unfreeze",
                                    historyToken.unfreezeOrEmpty()
                            )
                    );

            element.setDropMenu(menu);
            menu.open(true); // true = focus
        }
    }

    /**
     * A TABLE that holds the grid of cells including the column and row headers.
     */
    private final TableElement tableElement;

    /**
     * The ID assigned to the container TABLE element.
     */
    private final static String VIEWPORT_ID = "viewport";

    /**
     * Prefix for any component within a viewport
     */
    private final static String VIEWPORT_ID_PREFIX = VIEWPORT_ID + "-";

    // misc.............................................................................................................

    public void setWidthAndHeight(final int width,
                                  final int height) {
        final boolean reload = width > this.width || height > this.height;

        final AppContext context = this.context;
        context.debug("SpreadsheetViewportComponent.setWidthAndHeight " + width + "x" + height + " was " + this.width + "x" + this.height + " reload: " + reload);

        this.width = width;
        this.height = height;

        this.reload = reload;

        this.tableElement.element()
                .style.set(
                        "height",
                        (this.height - this.formulaComponent.element().offsetHeight) + "px"
                );

        this.loadViewportCellsIfNecessary(context);
    }

    /**
     * The width allocated to the widget.
     */
    private int width;

    /**
     * The height allocated to the widget.
     */
    private int height;

    // render..........................................................................................................

    /**
     * Renders the TABLE element again using its current state. Note no elements are cached or re-used, everything
     * is rendered again!
     */
    private void render(final AppContext context) {
        final HistoryToken historyToken = context.historyToken();
        final Optional<SpreadsheetViewportSelection> maybeViewportSelection = historyToken.viewportSelectionOrEmpty();
        this.setViewportSelection(
                maybeViewportSelection
        );

        final TableElement tableElement = this.tableElement;

        tableElement.clearElement();

        final SpreadsheetViewportCache cache = context.viewportCache();
        // "window": "A1:B12,WI1:WW12"
        //    A1:B12,
        //    WI1:WW12
        //
        // "window": "A1:B2,WI1:WX2,A3:B12,WI3:WX12"
        //   A1:B2
        //   WI1:WX2
        //   A3:B12
        //   WI3:WX12

        final Set<SpreadsheetColumnReference> columns = Sets.sorted();
        final Set<SpreadsheetRowReference> rows = Sets.sorted();

        // gather visible columns and rows.
        for (final SpreadsheetCellRange window : cache.windows().cellRanges()) {
            for (final SpreadsheetColumnReference column : window.columnRange()) {
                if (false == cache.isColumnHidden(column)) {
                    columns.add(column);
                }
            }

            for (final SpreadsheetRowReference row : window.rowRange()) {
                if (false == cache.isRowHidden(row)) {
                    rows.add(row);
                }
            }
        }

        // top row of column headers
        tableElement.appendChild(
                renderColumnHeaders(
                        columns,
                        context
                )
        );

        // render the rows and cells
        tableElement.appendChild(
                this.renderRows(
                        rows,
                        columns,
                        context
                )
        );

        if (historyToken instanceof SpreadsheetCellSelectHistoryToken ||
                historyToken instanceof SpreadsheetColumnSelectHistoryToken ||
                historyToken instanceof SpreadsheetRowSelectHistoryToken) {
            this.giveViewportSelectionFocus(
                    historyToken.cast(SpreadsheetViewportSelectionHistoryToken.class)
                            .viewportSelection(),
                    context
            );
        }

        if (historyToken instanceof SpreadsheetCellMenuHistoryToken ||
                historyToken instanceof SpreadsheetColumnMenuHistoryToken ||
                historyToken instanceof SpreadsheetRowMenuHistoryToken) {
            this.renderContextMenu(
                    historyToken.cast(SpreadsheetViewportSelectionHistoryToken.class),
                    context
            );
        }
    }

    /**
     * Creates a THEAD holding a TR with the SELECT ALL and COLUMN headers.
     */
    private HTMLTableSectionElement renderColumnHeaders(final Collection<SpreadsheetColumnReference> columns,
                                                        final AppContext context) {
        final TableRowElement tr = ElementsFactory.elements.tr()
                .appendChild(
                        this.renderSelectAll(context)
                );

        for (final SpreadsheetColumnReference column : columns) {
            tr.appendChild(
                    this.renderColumnHeader(
                            column,
                            context
                    )
            );
        }

        return ElementsFactory.elements.thead()
                .appendChild(tr.element())
                .element();
    }

    /**
     * Factory that creates the element that appears in the top left and may be used to select the entire spreadsheet.
     */
    // TODO add link
    private HTMLTableCellElement renderSelectAll(final AppContext context) {
        return ElementsFactory.elements.th()
                .id(VIEWPORT_SELECT_ALL_CELLS)
                .appendChild("ALL")
                .style(
                        context.viewportAllStyle(false)
                                .set(
                                        TextStylePropertyName.MIN_WIDTH,
                                        ROW_WIDTH
                                ).set(
                                        TextStylePropertyName.MIN_HEIGHT,
                                        COLUMN_HEIGHT
                                ).css() + "box-sizing: border-box;")
                .element();
    }

    private final static String VIEWPORT_SELECT_ALL_CELLS = VIEWPORT_ID_PREFIX + "select-all-cells";

    /**
     * Creates a TH with the column in UPPER CASE with column width.
     */
    private HTMLTableCellElement renderColumnHeader(final SpreadsheetColumnReference column,
                                                    final AppContext context) {
        final THElement th = ElementsFactory.elements.th()
                .id(id(column))
                .style(
                        context.viewportColumnHeaderStyle(this.isSelected(column))
                                .set(
                                        TextStylePropertyName.MIN_WIDTH,
                                        context.viewportCache()
                                                .columnWidth(column)
                                ).set(
                                        TextStylePropertyName.MIN_HEIGHT,
                                        COLUMN_HEIGHT
                                )
                                .css() + "box-sizing: border-box;"
                );

        th.appendChild(
                context.historyToken()
                        .setViewportSelection(
                                Optional.of(
                                        column.setDefaultAnchor()
                                )
                        ).link(
                                id(column)
                        ).setTabIndex(0)
                        .addPushHistoryToken(
                                context
                        ).setTextContent(
                                column.toString()
                                        .toUpperCase()
                        ).element()
        );

        return th.element();
    }

    private final static Length<?> COLUMN_HEIGHT = Length.pixel(25.0);

    /**
     * Factory that creates a TABLE CELL for the column header, including a link to select that column when clicked.
     */
    private HTMLTableSectionElement renderRows(final Set<SpreadsheetRowReference> rows,
                                               final Set<SpreadsheetColumnReference> columns,
                                               final AppContext context) {
        final TBodyElement tbody = ElementsFactory.elements.tbody();

        for (final SpreadsheetRowReference row : rows) {
            tbody.appendChild(
                    this.renderRow(
                            row,
                            columns,
                            context
                    )
            );
        }

        return tbody.element();
    }

    /**
     * Creates a TR which will hold the ROW and then cells.
     */
    private HTMLTableRowElement renderRow(final SpreadsheetRowReference row,
                                          final Collection<SpreadsheetColumnReference> columns,
                                          final AppContext context) {
        final TableRowElement tr = ElementsFactory.elements.tr()
                .appendChild(
                        this.renderRowHeader(
                                row,
                                context
                        )
                );

        for (final SpreadsheetColumnReference column : columns) {
            tr.appendChild(
                    this.renderCell(
                            column.setRow(row),
                            context
                    )
            );
        }

        return tr.element();
    }

    private HTMLTableCellElement renderRowHeader(final SpreadsheetRowReference row,
                                                 final AppContext context) {
        final TDElement td = ElementsFactory.elements.td()
                .id(id(row))
                .style(
                        context.viewportRowHeaderStyle(this.isSelected(row))
                                .set(
                                        TextStylePropertyName.MIN_WIDTH,
                                        ROW_WIDTH
                                ).set(
                                        TextStylePropertyName.MIN_HEIGHT,
                                        context.viewportCache()
                                                .rowHeight(row)
                                )
                                .css() + "box-sizing: border-box;"
                );

        td.appendChild(
                context.historyToken()
                        .setViewportSelection(
                                Optional.of(
                                        row.setDefaultAnchor()
                                )
                        ).link(
                                id(row)
                        ).setTabIndex(0)
                        .addPushHistoryToken(
                                context
                        ).setTextContent(
                                row.toString()
                                        .toUpperCase()
                        ).element()
        );

        return td.element();
    }

    private final static Length<?> ROW_WIDTH = Length.pixel(80.0);

    /**
     * Renders the given cell, reading the cell contents using {@link AppContext#viewportCache()}.
     */
    private HTMLTableCellElement renderCell(final SpreadsheetCellReference cellReference,
                                            final AppContext context) {
        final SpreadsheetViewportCache cache = context.viewportCache();
        final Optional<SpreadsheetCell> maybeCell = cache.cell(cellReference);

        TextStyle style = context.viewportCellStyle(
                this.isSelected(cellReference)
        );
        TextNode content = null;

        // if an error is present add a tooltip below the cell with the error message.
        Optional<SpreadsheetError> maybeError = Optional.empty();

        if (maybeCell.isPresent()) {
            final SpreadsheetCell cell = maybeCell.get();
            final Optional<TextNode> maybeFormatted = cell.formatted();
            if (maybeFormatted.isPresent()) {
                content = maybeFormatted.get();
            }
            style = cell.style()
                    .merge(style);

            maybeError = cell.formula()
                    .error();
        }

        style = style.set(
                TextStylePropertyName.WIDTH,
                cache.columnWidth(cellReference.column())
        ).set(
                TextStylePropertyName.HEIGHT,
                cache.rowHeight(cellReference.row())
        );

        // copy width/height to MIN to prevent table squashing cells to fit.
        style = style.set(
                TextStylePropertyName.MIN_WIDTH,
                style.getOrFail(TextStylePropertyName.WIDTH)
        ).set(
                TextStylePropertyName.MIN_HEIGHT,
                style.getOrFail(TextStylePropertyName.HEIGHT)
        );

        final TDElement td = ElementsFactory.elements.td()
                .id(
                        id(cellReference)
                ).setTabIndex(0)
                .style(
                        style.css() + "box-sizing: border-box;"
                );
        if (null != content) {
            td.appendChild(
                    Doms.node(content)
            );
        }

        final HTMLTableCellElement element = td.element();

        if (maybeError.isPresent()) {
            Tooltip.create(
                    element,
                    maybeError.get().message()
            ).setPosition(DropDirection.BOTTOM_MIDDLE);
        }

        return element;
    }

    private void giveViewportSelectionFocus(final SpreadsheetViewportSelection viewportSelection,
                                            final AppContext context) {
        final Optional<SpreadsheetSelection> maybeNonLabelSelection = context.viewportCache()
                .nonLabelSelection(
                        viewportSelection.selection()
                );
        if (maybeNonLabelSelection.isPresent()) {
            final SpreadsheetSelection nonLabelSelection = maybeNonLabelSelection.get();
            final SpreadsheetSelection selection = nonLabelSelection.focused(
                    viewportSelection.anchor()
            );
            final Optional<Element> maybeElement = this.findElement(
                    selection,
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
                    if (selection.isColumnReference() || selection.isRowReference()) {
                        element = element.firstElementChild;
                    }

                    context.debug("SpreadsheetViewportComponent " + selection + " focus element " + element);
                    element.focus();
                }
            } else {
                context.debug("SpreadsheetViewportComponent " + selection + " element not found!");
            }
        }
    }

    // history..........................................................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return false;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return true;
    }

    @Override
    public boolean isOpen() {
        return true; // always open
    }

    @Override
    public void open(final AppContext context) {
        // NOP
    }

    @Override
    public void refresh(final AppContext context) {
        context.debug("SpreadsheetViewportComponent.refresh BEGIN");

        this.render(
                context
        );

        context.debug("SpreadsheetViewportComponent.refresh END");
    }

    @Override
    public void close(final AppContext context) {
        // nop
    }

    // FetcherWatcher...................................................................................................

    @Override
    public void onBegin(final HttpMethod method,
                        final Url url,
                        final Optional<String> body,
                        final AppContext context) {
        this.outstandingFetches++;
    }

    @Override
    public void onFailure(final HttpStatus status,
                          final Headers headers,
                          final String body,
                          final AppContext context) {
        this.onFetchFinish(context);
    }

    @Override
    public void onError(final Object cause,
                        final AppContext context) {
        this.onFetchFinish(context);
    }

    /**
     * This is unconditionally called when a fetch to the server has completed, successfully, as a failure or error.
     */
    private void onFetchFinish(final AppContext context) {
        this.outstandingFetches--;
        if (this.outstandingFetches <= 0) {
            this.loadViewportCellsIfNecessary(context);
        }
    }

    /**
     * This incremented at the start of each fetch and decremented when they finish.
     */
    private int outstandingFetches;

    // delta............................................................................................................

    @Override
    public void onSpreadsheetDelta(final SpreadsheetDelta delta,
                                   final AppContext context) {
        Objects.requireNonNull(delta, "delta");

        this.refresh(context);
        this.onFetchFinish(context);
    }

    // SpreadsheetLabelMappingFetcherWatcher............................................................................

    /**
     * Any label mapping change should refresh the viewport cells.
     */
    @Override
    public void onSpreadsheetLabelMapping(final Optional<SpreadsheetLabelMapping> mapping,
                                          final AppContext context) {
        this.reload = true; // force a viewport reload.
        this.onFetchFinish(context);
    }

    // metadata.........................................................................................................

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        Objects.requireNonNull(metadata, "metadata");

        if (metadata.shouldViewRefresh(this.metadata)) {
            this.reload = true;
        }

        this.metadata = metadata;

        this.loadViewportCellsIfNecessary(context);

        final Optional<SpreadsheetId> spreadsheetId = metadata.id();
        if (spreadsheetId.isPresent()) {
            context.pushHistoryToken(
                    context.historyToken()
                            .setIdAndName(
                                    spreadsheetId.get(),
                                    metadata.getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_NAME)
                            )
            );
        }

        // the returned metadata isnt any different from the current metadata skip rendering again.
        if (this.reload) {
            this.refresh(context);
        }
        this.onFetchFinish(context);
    }

    /**
     * Initial metadata is EMPTY or nothing.
     */
    private SpreadsheetMetadata metadata = SpreadsheetMetadata.EMPTY;

    private void setViewportSelection(final Optional<SpreadsheetViewportSelection> maybeViewportSelection) {
        final AppContext context = this.context;
        context.debug(
                "SpreadsheetViewportComponent.setViewportSelection " + maybeViewportSelection.orElse(null)
        );

        Predicate<SpreadsheetSelection> predicate = null;

        if (maybeViewportSelection.isPresent()) {
            // special case for label
            final Optional<SpreadsheetSelection> maybeNotLabel = context.viewportCache()
                    .nonLabelSelection(maybeViewportSelection.get().selection());
            if (maybeNotLabel.isPresent()) {
                predicate = maybeNotLabel.get();
            }
        }

        this.selection = null != predicate ?
                predicate :
                Predicates.never();
    }

    /**
     * Tests if various requirements are ready and the viewport should be loaded again.
     */
    private void loadViewportCellsIfNecessary(final AppContext context) {
        final boolean reload = this.reload;

        final int width = this.width;
        final int height = this.height;

        final int outstandingFetches = this.outstandingFetches;

        if (reload && width > 0 && height > 0 && outstandingFetches <= 0) {
            if (context.spreadsheetMetadata().isEmpty()) {
                context.debug("SpreadsheetViewportComponent.loadViewportCellsIfNecessary waiting for metadata");
            } else {
                this.loadViewportCells(context);
            }
        } else {
            context.debug("SpreadsheetViewportComponent.loadViewportCellsIfNecessary not ready, reload: " + reload + " width: " + width + " height: " + height + " outstandingFetches: " + outstandingFetches);
        }
    }

    /**
     * Unconditionally Loads all the cells to fill the viewport using the {@link #navigations} buffer. Assumes that a metadata with id is present.
     */
    private void loadViewportCells(final AppContext context) {
        final int formulaOffsetHeight = this.formulaComponent.element()
                .offsetHeight;

        final SpreadsheetMetadata metadata = context.spreadsheetMetadata();

        final SpreadsheetId id = metadata.getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_ID);
        final SpreadsheetCellReference home = metadata.get(SpreadsheetMetadataPropertyName.VIEWPORT_CELL).orElse(SpreadsheetCellReference.A1);

        final int width = this.width;
        final int height = this.height - formulaOffsetHeight;

        final Optional<SpreadsheetViewportSelection> viewportSelection = metadata.get(SpreadsheetMetadataPropertyName.SELECTION);
        final List<SpreadsheetViewportSelectionNavigation> navigations = this.navigations;

        context.debug(
                "SpreadsheetViewportComponent.loadViewportCells id: " + id + " home: " + home + " width: " + width + " height: " + height + " navigations buffer: " +
                        SpreadsheetViewportSelection.SEPARATOR.toSeparatedString(
                                navigations,
                                SpreadsheetViewportSelectionNavigation::text
                        )
        );

        context.spreadsheetDeltaFetcher()
                .loadCells(
                        id, // id
                        home, // home
                        width,
                        height,
                        viewportSelection, // viewportSelection
                        navigations
                );

        context.viewportCache()
                .clear(); // clear all cached data.
        this.reload = false;
        navigations.clear();
    }

    /**
     * Accepts and adds the given {@link SpreadsheetViewportSelectionNavigation} to the buffer and if possible
     * sends it to the server for actioning.
     */
    private void onNavigation(final SpreadsheetViewportSelectionNavigation navigation,
                              final AppContext context) {
        Objects.requireNonNull(navigation, "navigation");
        Objects.requireNonNull(context, "context");

        this.navigations.add(navigation);
        this.reload = true;
        this.loadViewportCellsIfNecessary(context);

    }

    /**
     * A buffer which fills up with {@link SpreadsheetViewportSelectionNavigation} entries such as keyboard cursor key movements
     * or clicking the horizontal or vertical scrollbars. This is useful so multiple navigation actions are batched
     * when outstanding fetches are in flight so they are sent once, rather than sending a fetch for each.
     */
    private final List<SpreadsheetViewportSelectionNavigation> navigations = Lists.array();

    /**
     * Initially false, this will become true, when the metadata for a new spreadsheet is loaded and a resize event happens.
     */
    private boolean reload = false;

    /**
     * Tests if the given {@link SpreadsheetSelection} typically a cell, column or row is matched by the {@link SpreadsheetMetadataPropertyName#SELECTION}.
     * This is used during rendering of the viewport headers or cells if the item should be selected.
     */
    private boolean isSelected(final SpreadsheetSelection selection) {
        return this.selection.test(selection);
    }

    private Predicate<SpreadsheetSelection> selection = Predicates.never();

    /**
     * Helper that finds the {@link Element} for the given {@link SpreadsheetSelection}
     */
    private Optional<Element> findElement(final SpreadsheetSelection selection,
                                          final AppContext context) {
        Element element = null;

        final Optional<SpreadsheetSelection> maybeNotLabel = context.viewportCache()
                .nonLabelSelection(selection);

        if (maybeNotLabel.isPresent()) {
            element = DomGlobal.document
                    .getElementById(
                            SpreadsheetViewportComponent.id(
                                    selection
                            )
                    );
        }

        return Optional.ofNullable(element);
    }

    public Optional<SpreadsheetCell> viewportCell(final SpreadsheetSelection selection,
                                                  final AppContext context) {
        Optional<SpreadsheetCell> cell = Optional.empty();

        final SpreadsheetViewportCache cache = context.viewportCache();

        final Optional<SpreadsheetSelection> nonLabelSelection = cache.nonLabelSelection(selection);
        if (nonLabelSelection.isPresent()) {
            cell = cache.cell(nonLabelSelection.get().toCell());
        }

        return cell;
    }

    private final AppContext context;

    // helpers..........................................................................................................

    // viewport-column-A
    public static String id(final SpreadsheetSelection selection) {
        return VIEWPORT_ID_PREFIX +
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

        if (id.startsWith(VIEWPORT_ID_PREFIX)) {
            // viewport-cell-A1 -> cell-A1
            final String selectionTypeAndSelection = id.substring(VIEWPORT_ID_PREFIX.length());
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
