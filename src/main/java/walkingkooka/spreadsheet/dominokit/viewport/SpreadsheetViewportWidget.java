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

import elemental2.dom.Element;
import elemental2.dom.Event;
import elemental2.dom.HTMLAnchorElement;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLInputElement;
import elemental2.dom.HTMLTableCellElement;
import elemental2.dom.HTMLTableElement;
import elemental2.dom.HTMLTableRowElement;
import elemental2.dom.HTMLTableSectionElement;
import elemental2.dom.KeyboardEvent;
import jsinterop.base.Js;
import org.dominokit.domino.ui.forms.TextBox;
import org.gwtproject.safehtml.shared.SafeHtmlUtils;
import org.jboss.elemento.Elements;
import org.jboss.elemento.EventType;
import org.jboss.elemento.HtmlContentBuilder;
import org.jboss.elemento.InputBuilder;
import org.jboss.elemento.InputType;
import org.jboss.elemento.IsElement;
import org.jboss.elemento.Key;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.Url;
import walkingkooka.net.UrlParameterName;
import walkingkooka.net.UrlQueryString;
import walkingkooka.predicate.Predicates;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataWatcher;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.engine.SpreadsheetEngineEvaluation;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionNavigation;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public final class SpreadsheetViewportWidget implements IsElement<HTMLDivElement>,
        SpreadsheetDeltaWatcher,
        SpreadsheetMetadataWatcher,
        HistoryTokenWatcher {

    public static SpreadsheetViewportWidget empty(final AppContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetViewportWidget(context);
    }

    private SpreadsheetViewportWidget(final AppContext context) {
        this.context = context;

        this.formulaTextBox = this.createFormulaTextBox();
        this.tableElement = this.createTable();
        this.root = this.createRoot();

        context.addSpreadsheetMetadataWatcher(this);
        context.addSpreadsheetDeltaWatcher(this);
    }

    public void setWidthAndHeight(final int width,
                                  final int height) {
        final boolean reload = width > this.width || height > this.height;

        this.context.debug("SpreadsheetViewportWidget.setWidthAndHeight " + width + "x" + height + " was " + this.width + "x" + this.height + " reload: " + reload);

        this.width = width;
        this.height = height;

        this.reload = reload;
        this.loadViewportCellsIfNecessary();
    }

    /**
     * The width allocated to the widget.
     */
    private int width;

    /**
     * The height allocated to the widget.
     */
    private int height;

    private final AppContext context;

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        final HistoryToken historyToken = context.historyToken();

        final Optional<SpreadsheetViewportSelection> maybeViewportSelection = historyToken.viewportSelectionOrEmpty();
        this.setViewportSelection(maybeViewportSelection);

        this.render();
    }

    /**
     * A returned value of <code>true</code> indicates the formula should be enabled for editing.
     */
    private boolean isFormulaEnabled() {
        return this.context.historyToken() instanceof SpreadsheetCellHistoryToken;
    }

    @Override
    public void onSpreadsheetDelta(final SpreadsheetDelta delta,
                                   final AppContext context) {
        Objects.requireNonNull(delta, "delta");

        this.cache.onSpreadsheetDelta(delta, context);

        this.render();
    }

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        Objects.requireNonNull(metadata, "metadata");

        if (metadata.shouldViewRefresh(this.metadata)) {
            this.reload = true;
        }

        this.metadata = metadata;

        this.loadViewportCellsIfNecessary();

        this.cache.onSpreadsheetMetadata(
                metadata,
                context
        );

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

        this.render();
    }

    /**
     * Initial metadata is EMPTY or nothing.
     */
    private SpreadsheetMetadata metadata = SpreadsheetMetadata.EMPTY;

    private void setViewportSelection(final Optional<SpreadsheetViewportSelection> maybeViewportSelection) {
        final AppContext context = this.context;
        context.debug(
                "SpreadsheetViewportWidget.setViewportSelection " + maybeViewportSelection.orElse(null)
        );

        Predicate<SpreadsheetSelection> predicate = null;

        if (maybeViewportSelection.isPresent()) {
            // special case for label
            final Optional<SpreadsheetSelection> maybeNotLabel = this.cache.nonLabelSelection(maybeViewportSelection.get().selection());
            if (maybeNotLabel.isPresent()) {
                predicate = maybeNotLabel.get();
            }
        }

        this.selection = null != predicate ?
                predicate :
                Predicates.never();
    }

    /**
     * Tests if the given {@link SpreadsheetSelection} typically a cell, column or row is matched by the {@link SpreadsheetMetadataPropertyName#SELECTION}.
     */
    private boolean isSelected(final SpreadsheetSelection selection) {
        return this.selection.test(selection);
    }

    private Predicate<SpreadsheetSelection> selection = Predicates.never();

    private void loadViewportCellsIfNecessary() {
        if (this.reload) {
            if (this.metadata.isEmpty()) {
                this.context.debug("SpreadsheetViewportWidget.loadViewportCellsIfNecessary waiting for metadata");
            } else {
                this.loadViewportCells(
                        Optional.empty()
                );
            }
        }
    }

    /**
     * Loads all the cells to fill the viewport. Assumes that a metadata with id is present.
     */
    private void loadViewportCells(final Optional<SpreadsheetViewportSelectionNavigation> navigation) {
        Objects.requireNonNull(navigation, "navigation");

        this.cache.clear(); // equivalent to clearing all cached data.

        this.reload = false;

        final SpreadsheetMetadata metadata = this.metadata;
        final int width = this.width;
        final int height = this.height;
        final SpreadsheetCellReference home = metadata.get(SpreadsheetMetadataPropertyName.VIEWPORT_CELL)
                .orElse(SpreadsheetCellReference.A1);

        this.context.debug("SpreadsheetViewportWidget.loadViewportCells " + home + " " + width + "x" + height);


        // load cells for the new window...
        //http://localhost:3000/api/spreadsheet/1f/cell/*/force-recompute?home=A1&width=1712&height=765&includeFrozenColumnsRows=true

        UrlQueryString queryString = UrlQueryString.EMPTY
                .addParameter(HOME, home.toString())
                .addParameter(WIDTH, String.valueOf(width))
                .addParameter(HEIGHT, String.valueOf(height))
                .addParameter(INCLUDE_FROZEN_COLUMNS_ROWS, Boolean.TRUE.toString());

        final Optional<SpreadsheetViewportSelection> viewportSelection = metadata.get(SpreadsheetMetadataPropertyName.SELECTION);
        if (viewportSelection.isPresent()) {
            queryString = SpreadsheetDeltaFetcher.appendViewportSelection(
                    viewportSelection.get()
                            .setNavigation(navigation),
                    queryString
            );
        }

        this.context.spreadsheetDeltaFetcher()
                .get(
                        Url.parseRelative(
                                "/api/spreadsheet/" +
                                        metadata.getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_ID) +
                                        "/cell/*/" +
                                        CaseKind.kebabEnumName(SpreadsheetEngineEvaluation.FORCE_RECOMPUTE)
                        ).setQuery(queryString)
                );
    }

    private final static UrlParameterName HOME = UrlParameterName.with("home");
    private final static UrlParameterName WIDTH = UrlParameterName.with("width");
    private final static UrlParameterName HEIGHT = UrlParameterName.with("height");
    private final static UrlParameterName INCLUDE_FROZEN_COLUMNS_ROWS = UrlParameterName.with("includeFrozenColumnsRows");

    /**
     * Initially false, this will become true, when the metadata for a new spreadsheet is loaded and a resize event happens.
     */
    private boolean reload = false;

    /**
     * Handles resolving a {@link walkingkooka.spreadsheet.reference.SpreadsheetLabelName} into a {@link SpreadsheetSelection}
     * or just returns the selection.
     */
    public Optional<SpreadsheetSelection> nonLabelSelection(final SpreadsheetSelection selection) {
        return this.cache.nonLabelSelection(selection);
    }

    /**
     * Returns the window used by this viewport.
     */
    public Set<SpreadsheetCellRange> window() {
        return this.cache.windows;
    }

    /**
     * Cache that holds all the cells, labels etc displayed by this widget.
     */
    private final SpreadsheetViewportCache cache = SpreadsheetViewportCache.empty();

    // root.............................................................................................................

    /**
     * The root DIV element holding the formula and TABLE holding all headers and cells.
     */
    @Override
    public HTMLDivElement element() {
        return this.root;
    }

    // root.............................................................................................................

    private HTMLDivElement createRoot() {
        final HtmlContentBuilder<HTMLDivElement> root = Elements.div();
        root.style("border: none; margin: 0px; padding: none; width:100%");

        root.add(this.formulaTextBox);
        root.add(this.tableElement.element());

        return root.element();
    }

    /**
     * The root or container that holds the {@link #formulaTextBox} and {@link #tableElement}.
     */
    private final HTMLDivElement root;

    // formula..........................................................................................................

    /**
     * Creates a {@link TextBox} that holds the formula for editing.
     */
    private HTMLInputElement createFormulaTextBox() {
        final InputBuilder<HTMLInputElement> input = Elements.input(InputType.text);
        input.css("formulaTextBox");

        final HTMLInputElement element = input.element();
        element.addEventListener(
                EventType.focus.getName(),
                this::onFormulaTextBoxFocus
        );
        return element;
    }

    private void onFormulaTextBoxFocus(final Event event) {
        final AppContext context = this.context;
        final HistoryToken historyToken = context.historyToken();

        context.debug("SpreadsheetViewportWidget.onFormulaTextBoxFocus " + historyToken.viewportSelectionOrEmpty());

        context.pushHistoryToken(
                historyToken.formulaHistoryToken()
        );
    }

    /**
     * Only gives focus to the formula text box assumes that it is already shown and has been updated
     * with the formula every time a new {@link SpreadsheetDelta} is returned.
     */
    public void giveFcrmulaTextBoxFocus() {
        this.context.debug("SpreadsheetViewportWidget.giveFcrmulaTextBoxFocus");

        this.formulaTextBox.focus();
    }

    /**
     * Uses the {@link SpreadsheetSelection} which may be a label to fetch the {@link SpreadsheetCell} and updates
     * the formula text.
     */
    public void setFormula(final SpreadsheetSelection selection) {
        Objects.requireNonNull(selection, "selection");

        String text = "";

        final SpreadsheetViewportCache cache = this.cache;
        final Optional<SpreadsheetSelection> maybeNonLabel = cache.nonLabelSelection(selection);
        if (maybeNonLabel.isPresent()) {
            final SpreadsheetSelection nonLabel = maybeNonLabel.get();
            final Optional<SpreadsheetCell> maybeCell = cache.cell(nonLabel.toCell());

            if (maybeCell.isPresent()) {
                text = maybeCell.get().formula().text();
            }

            // TODO show error somewhere in formula ?
        }

        this.context.debug("SpreadsheetViewportWidget.setFormula text=" + CharSequences.quoteAndEscape(text));
        this.formulaTextBox.value = text;
    }

    /**
     * A {@link HTMLInputElement} that holds the selected cell formula for editing.
     */
    private final HTMLInputElement formulaTextBox;

    /**
     * The height of the formula textbox.
     */
    private final static int FORMULA_TEXTBOX_HEIGHT = 26;

    // table............................................................................................................

    /**
     * Creates an empty table with minimal styling including some placeholder text.
     */
    private HtmlContentBuilder<HTMLTableElement> createTable() {
        final HtmlContentBuilder<HTMLTableElement> tableElement = Elements.table();
        tableElement.id(VIEWPORT_ID);
        tableElement.style("width: 100%; height: 100%;");
        tableElement.add(
                Elements.tbody()
                        .add(
                                Elements.tr()
                                        .add(
                                                Elements.td()
                                                        .add(
                                                                "spreadsheet here"
                                                        ).element()
                                        ).element()
                        ).element()
        );

        return tableElement;
    }

    /**
     * A TABLE that holds the grid of cells including the column and row headers.
     */
    private final HtmlContentBuilder<HTMLTableElement> tableElement;

    /**
     * The ID assigned to the container TABLE element.
     */
    private final static String VIEWPORT_ID = "viewport";

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

        final Key key = Key.fromEvent(event);
        final boolean shifted = event.shiftKey;
        final AppContext context = this.context;

        SpreadsheetViewportSelectionNavigation navigation = null;

        switch (key) {
            case ArrowLeft:
                navigation = shifted ?
                        SpreadsheetViewportSelectionNavigation.EXTEND_LEFT :
                        SpreadsheetViewportSelectionNavigation.LEFT;
                break;
            case ArrowUp:
                navigation = shifted ?
                        SpreadsheetViewportSelectionNavigation.EXTEND_UP :
                        SpreadsheetViewportSelectionNavigation.UP;
                break;
            case ArrowRight:
                navigation = shifted ?
                        SpreadsheetViewportSelectionNavigation.EXTEND_RIGHT :
                        SpreadsheetViewportSelectionNavigation.RIGHT;
                break;
            case ArrowDown:
                navigation = shifted ?
                        SpreadsheetViewportSelectionNavigation.EXTEND_DOWN :
                        SpreadsheetViewportSelectionNavigation.DOWN;
                break;
            case Enter:
                // if cell then edit formula
                context.pushHistoryToken(
                        context.historyToken()
                                .formulaHistoryToken()
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
            this.loadViewportCells(
                    Optional.of(navigation)
            );
        }
    }

    /**
     * Renders the TABLE element again using its current state. Note no elements are cached or re-used, everything
     * is rendered again!
     */
    private void render() {
        final boolean shouldFormulaEnabled = this.isFormulaEnabled();
        final HtmlContentBuilder<HTMLTableElement> tableElement = this.tableElement;

        final AppContext context = this.context;
        context.debug("SpreadsheetViewportWidget.render isFormulaEnabled: " + shouldFormulaEnabled);

        Elements.removeChildrenFrom(tableElement.element());

        tableElement.element()
                .style.set(
                        "height",
                        (height -
                                (shouldFormulaEnabled ? FORMULA_TEXTBOX_HEIGHT : 0)
                        ) + "px"
                );
        this.formulaTextBox.style.set(
                "display",
                shouldFormulaEnabled ? "block" : "none"
        );

        final SpreadsheetViewportCache cache = this.cache;
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
        for(final SpreadsheetCellRange window : this.window()) {
            for(final SpreadsheetColumnReference column : window.columnRange()) {
                if(false == cache.isColumnHidden(column)) {
                    columns.add(column);
                }
            }

            for(final SpreadsheetRowReference row : window.rowRange()) {
                if(false == cache.isRowHidden(row)) {
                    rows.add(row);
                }
            }
        }

        // top row of column headers
        tableElement.add(
                renderColumnHeaders(columns)
        );

        // render the rows and cells
        tableElement.add(
                this.renderRows(
                        rows,
                        columns
                )
        );

        final HistoryToken historyToken = context.historyToken();
        if (historyToken instanceof SpreadsheetViewportWidgetWatcher) {
            final SpreadsheetViewportWidgetWatcher watcher = (SpreadsheetViewportWidgetWatcher) historyToken;
            watcher.onAfterSpreadsheetViewportWidgetRender(context);
        }
    }

    /**
     * Creates a THEAD holding a TR with the SELECT ALL and COLUMN headers.
     */
    private HTMLTableSectionElement renderColumnHeaders(final Collection<SpreadsheetColumnReference> columns) {
        final HtmlContentBuilder<HTMLTableRowElement> tr = Elements.tr()
                .add(
                        this.selectAll()
                );

        for (final SpreadsheetColumnReference column : columns) {
            tr.add(
                    this.renderColumnHeader(column)
            );
        }

        return Elements.thead()
                .add(tr.element())
                .element();
    }

    /**
     * Factory that creates the element that appears in the top left and may be used to select the entire spreadsheet.
     */
    // TODO add link
    private HTMLTableCellElement selectAll() {
        return Elements.th()
                .id(VIEWPORT_SELECT_ALL_CELLS)
                .add("ALL")
                .style(
                        this.context.viewportAllStyle(false)
                                .set(
                                        TextStylePropertyName.WIDTH,
                                        ROW_WIDTH
                                ).set(
                                        TextStylePropertyName.HEIGHT,
                                        COLUMN_HEIGHT
                                ).css() + "box-sizing: border-box;")
                .element();
    }

    private final static String VIEWPORT_SELECT_ALL_CELLS = "viewport-select-all-cells";

    /**
     * Creates a TH with the column in UPPER CASE with column width.
     */
    private HTMLTableCellElement renderColumnHeader(final SpreadsheetColumnReference column) {
        final HtmlContentBuilder<HTMLTableCellElement> td = Elements.th()
                .id(id(column))
                .style(
                        this.context.viewportColumnHeaderStyle(this.isSelected(column))
                                .set(
                                        TextStylePropertyName.WIDTH,
                                        this.cache.columnWidth(column)
                                ).set(
                                        TextStylePropertyName.HEIGHT,
                                        COLUMN_HEIGHT
                                )
                                .css() + "box-sizing: border-box;"
                );

        this.addLinkOrText(
                column,
                td
        );

        final HTMLTableCellElement element = td.element();
        this.addKeyDownEventListener(element);
        this.addContextMenuEventListener(
                element,
                column
        );
        return element;
    }

    private final static Length<?> COLUMN_HEIGHT = Length.pixel(25.0);

    /**
     * Factory that creates a TABLE CELL for the column header, including a link to select that column when clicked.
     */
    private HTMLTableSectionElement renderRows(final Set<SpreadsheetRowReference> rows,
                                               final Set<SpreadsheetColumnReference> columns) {
        final HtmlContentBuilder<HTMLTableSectionElement> tbody = Elements.tbody();

        for (final SpreadsheetRowReference row : rows) {
            tbody.add(
                    this.renderRow(
                            row,
                            columns
                    )
            );
        }

        return tbody.element();
    }

    /**
     * Creates a TR which will hold the ROW and then cells.
     */
    private HTMLTableRowElement renderRow(final SpreadsheetRowReference row,
                                          final Collection<SpreadsheetColumnReference> columns) {
        final HtmlContentBuilder<HTMLTableRowElement> tr = Elements.tr()
                .add(
                        this.renderRowHeader(row)
                );

        for (final SpreadsheetColumnReference column : columns) {
            tr.add(
                    this.renderCell(
                            column.setRow(row)
                    )
            );
        }

        return tr.element();
    }

    private HTMLTableCellElement renderRowHeader(final SpreadsheetRowReference row) {
        final HtmlContentBuilder<HTMLTableCellElement> td = Elements.td()
                .id(id(row))
                .style(
                        this.context.viewportRowHeaderStyle(this.isSelected(row))
                                .set(
                                        TextStylePropertyName.WIDTH,
                                        ROW_WIDTH
                                ).set(
                                        TextStylePropertyName.HEIGHT,
                                        this.cache.rowHeight(row)
                                )
                                .css() + "box-sizing: border-box;"
                );

        this.addLinkOrText(
                row,
                td
        );

        final HTMLTableCellElement element = td.element();
        this.addKeyDownEventListener(element);
        this.addContextMenuEventListener(
                element,
                row
        );
        return element;
    }

    private final static Length<?> ROW_WIDTH = Length.pixel(80.0);

    /**
     * If possible creates a link to the cell or row or simply the cow/row reference as text.
     */
    private void addLinkOrText(final SpreadsheetColumnOrRowReference columnOrRow,
                               final HtmlContentBuilder<HTMLTableCellElement> td) {
        if (null == this.context.historyToken()) {
            td.textContent(
                    columnOrRow.toString()
                            .toUpperCase()
            );
        } else {
            td.add(
                    this.link(columnOrRow)
            );
        }
    }

    /**
     * Creates an ANCHOR including an ID and TEXT in upper case of the given {@link SpreadsheetSelection}.
     */
    private HTMLAnchorElement link(final SpreadsheetSelection selection) {
        final HistoryToken token = this.context.historyToken()
                .setViewportSelection(
                        Optional.of(
                                selection.setDefaultAnchor()
                        )
                );

        return Elements.a()
                .id(id(selection) + "-link")
                .attr("href", "#" + token.urlFragment().value())
                .textContent(selection.toString().toUpperCase())
                .element();
    }

    /**
     * Renders the given cell, reading the cell contents from the {@link #cache}.
     */
    private HTMLTableCellElement renderCell(final SpreadsheetCellReference cellReference) {
        final AppContext context = this.context;
        final SpreadsheetViewportCache cache = this.cache;
        final Optional<SpreadsheetCell> maybeCell = cache.cell(cellReference);

        TextStyle style = this.metadata.effectiveStyle();
        String innerHtml = "";

        if (maybeCell.isPresent()) {
            final SpreadsheetCell cell = maybeCell.get();
            final Optional<TextNode> maybeFormatted = cell.formatted();
            if(maybeFormatted.isPresent()) {
                final TextNode formatted = maybeFormatted.get();

                innerHtml = formatted.toHtml();
            }
            style = cell.style()
                    .merge(style);
        }

        style = style.merge(
                context.viewportCellStyle(this.isSelected(cellReference))
                        .set(TextStylePropertyName.WIDTH, cache.columnWidth(cellReference.column()))
                        .set(TextStylePropertyName.HEIGHT, cache.rowHeight(cellReference.row()))
        );

        final HtmlContentBuilder<HTMLTableCellElement> td = Elements.td()
                .id(
                        id(cellReference)
                ).attr(
                        "tabindex",
                        "0"
                ).style(
                        style.css() + "box-sizing: border-box;"
                ).innerHtml(SafeHtmlUtils.fromTrustedString(innerHtml));

        final HTMLTableCellElement element = td.element();
        element.addEventListener(
                EventType.click.getName(),
                (e) -> this.selectCell(
                        cellReference, context
                )
        );
        this.addKeyDownEventListener(element);
        this.addContextMenuEventListener(
                element,
                cellReference
        );
        return element;
    }

    /**
     * Grab the id and name from {@link SpreadsheetMetadata} and push a new token including the selected cell.
     */
    private void selectCell(final SpreadsheetCellReference cell,
                            final AppContext context) {
        final SpreadsheetMetadata metadata = this.metadata;
        final Optional<SpreadsheetName> name = metadata.name();
        final Optional<SpreadsheetId> id = metadata.id();

        if (id.isPresent() && name.isPresent()) {
            context.pushHistoryToken(
                    HistoryToken.cell(
                            id.get(),
                            name.get(),
                            cell.setDefaultAnchor()

                    )
            );
        }
    }

    private void addContextMenuEventListener(final Element element,
                                             final SpreadsheetSelection selection) {
        element.addEventListener(
                EventType.contextmenu.getName(),
                (event) -> this.onContextMenu(event, selection)
        );
    }

    /**
     * Pushes the selection and menu history token. When the {@link HistoryToken#onHistoryTokenChange(HistoryToken, AppContext)}
     * is executed a context menu will be shown with items.
     */
    private void onContextMenu(final Event event,
                               final SpreadsheetSelection selection) {
        event.preventDefault();

        final AppContext context = this.context;

        context.pushHistoryToken(
                context.historyToken()
                        .menuHistoryToken(selection)
        );
    }

    // viewport-column-A
    public static String id(final SpreadsheetSelection selection) {
        return VIEWPORT_ID_PREFIX +
                selection.textLabel().toLowerCase() +
                "-" +
                selection.toString().toUpperCase();
    }

    /**
     * Takes an id hopefully sourced from a {@link SpreadsheetViewportWidget} descendant element and tries to extract a {@link SpreadsheetSelection}.
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

    /**
     * Prefix for any component within a viewport
     */
    private final static String VIEWPORT_ID_PREFIX = VIEWPORT_ID + "-";
}
