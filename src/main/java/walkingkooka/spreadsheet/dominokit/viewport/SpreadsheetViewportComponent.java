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
import elemental2.dom.HTMLDivElement;
import elemental2.dom.Headers;
import elemental2.dom.KeyboardEvent;
import elemental2.dom.MouseEvent;
import jsinterop.base.Js;
import org.gwtproject.core.shared.GWT;
import walkingkooka.Cast;
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
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetDominoKitColor;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.cell.SpreadsheetCellLinksComponent;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuTargets;
import walkingkooka.spreadsheet.dominokit.dom.DivComponent;
import walkingkooka.spreadsheet.dominokit.dom.Doms;
import walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.fetcher.FetcherRequestBody;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopSpreadsheetFormatterInfoSetFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetFormatterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellFormulaHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellFormulaMenuHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellMenuHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetColumnMenuHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetColumnSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowMenuHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.recent.RecentValueSavesContext;
import walkingkooka.spreadsheet.dominokit.navigate.SpreadsheetNavigateLinkComponent;
import walkingkooka.spreadsheet.dominokit.reference.SpreadsheetSelectionMenu;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterAliasSet;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterMenu;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterMenuList;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorEdit;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewport;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportHomeNavigationList;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportNavigation;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportNavigationList;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportWindows;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextStyleProperty;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A ui that displays a table holding the cells and headers for the columns and rows.
 */
public final class SpreadsheetViewportComponent implements HtmlComponentDelegator<HTMLDivElement, SpreadsheetViewportComponent>,
    SpreadsheetDeltaFetcherWatcher,
    SpreadsheetFormatterFetcherWatcher,
    NopSpreadsheetFormatterInfoSetFetcherWatcher,
    SpreadsheetMetadataFetcherWatcher,
    HistoryTokenAwareComponentLifecycle,
    SpreadsheetViewportComponentLifecycle,
    LoadedSpreadsheetMetadataRequired,
    NopEmptyResponseFetcherWatcher {

    public static SpreadsheetViewportComponent empty(final SpreadsheetViewportComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetViewportComponent(context);
    }

    private SpreadsheetViewportComponent(final SpreadsheetViewportComponentContext context) {
        this.context = context;

        this.formula = this.createFormula();
        this.formulaCellLinks = this.formulaCellLinks();
        this.formulaContainer = HtmlElementComponent.div()
            .setCssText("position: relative;") // without this the formulaCellLinks will be positioned outside the formula editor
            .appendChild(this.formula)
            .appendChild(
                this.formulaCellLinks.setCssText("position:absolute; bottom: 0px; right: 15px; height: fit-content; display:flex;")
            );

        this.table = this.table(context);

        {
            final SpreadsheetViewportScrollbarComponentContext spreadsheetViewportScrollbarComponentContext = SpreadsheetViewportComponentSpreadsheetViewportScrollbarComponentContext.with(
                this,
                context // HistoryContext
            );
            this.horizontalScrollbar = this.horizontalScrollbar(spreadsheetViewportScrollbarComponentContext);
            this.verticalScrollbar = this.verticalScrollbar(spreadsheetViewportScrollbarComponentContext);
            this.navigateLink = SpreadsheetNavigateLinkComponent.with(context)
                .setId(ID_PREFIX + "navigate" + SpreadsheetElementIds.LINK);
            this.bottom = FlexLayoutComponent.row()
                .setCssProperty("position", "absolute")
                .setCssProperty("left", "0")
                .setCssProperty("bottom", "0")
                .setCssProperty("z-index", "1")
                .appendChild(this.horizontalScrollbar)
                .appendChild(this.navigateLink);
        }

        this.gridContainer = this.gridContainer();
        this.refreshMetadata = SpreadsheetMetadata.EMPTY;

        this.root = this.root();

        this.spreadsheetFormatterSelectorSelection = null;
        this.spreadsheetFormatterSelectorMenus = null;

        this.setVisibility(false);

        // SpreadsheetViewportComponent#addHistoryTokenWatcher must happen after recent HistoryTokenWatchers
        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetFormatterFetcherWatcher(this);
        context.addSpreadsheetMetadataFetcherWatcher(this);
        context.addSpreadsheetDeltaFetcherWatcher(this);
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
        root.appendChild(this.formulaContainer);
        root.appendChild(this.gridContainer);

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

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLDivElement, ?> htmlComponent() {
        return this.root;
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

    // formulaComponent.................................................................................................

    private final DivComponent formulaContainer;

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

    private DivComponent gridContainer() {
        final DivComponent container = HtmlElementComponent.div()
            .setCssText("position: relative; border: none; margin: 0px; padding: 0px;");

        container.appendChild(this.table);
        container.appendChild(this.verticalScrollbar);
        container.appendChild(this.bottom);

        return container;
    }

    private final DivComponent gridContainer;

    /**
     * Container that holds the {@link #horizontalScrollbar} and {@link #navigateLink}.
     */
    private final FlexLayoutComponent bottom;

    // table............................................................................................................

    private SpreadsheetViewportComponentTable table(final SpreadsheetViewportContext context) {
        return SpreadsheetViewportComponentTable.empty(
                SpreadsheetViewportComponentSpreadsheetViewportComponentTableContext.with(
                    this,
                    context
                )
            );
    }

    /**
     * A TABLE that holds the grid of cells including the column and row headers.
     */
    private final SpreadsheetViewportComponentTable table;

    // scrollbars.......................................................................................................

    SpreadsheetViewportScrollbarComponent<SpreadsheetColumnReference> horizontalScrollbar(final SpreadsheetViewportScrollbarComponentContext context) {
        return SpreadsheetViewportScrollbarComponent.columns(context)
            .setCssProperty("flex-grow", "2")
            .setCssProperty("height", SCROLLBAR_LENGTH + "px")
            .setCssProperty("border-color", SpreadsheetDominoKitColor.VIEWPORT_LINES_COLOR.toString())
            .setCssProperty("border-style", "solid")
            .setCssProperty("border-width", "1px")
            .setCssProperty("background-color", SpreadsheetDominoKitColor.VIEWPORT_HEADER_UNSELECTED_BACKGROUND_COLOR.toString())
            .addChangeListener(
                (Optional<SpreadsheetColumnReference> oldValue, Optional<SpreadsheetColumnReference> newValue) -> {
                    this.pushNewHome(
                        newValue.orElse(null)
                    );
                }
            );
    }

    private final SpreadsheetViewportScrollbarComponent<SpreadsheetColumnReference> horizontalScrollbar;

    SpreadsheetViewportScrollbarComponent<SpreadsheetRowReference> verticalScrollbar(final SpreadsheetViewportScrollbarComponentContext context) {
        return SpreadsheetViewportScrollbarComponent.rows(context)
            .setCssProperty("position", "absolute")
            .setCssProperty("top", "0")
            .setCssProperty("right", "0")
            .setCssProperty("z-index", "1")
            .setCssProperty("width", SCROLLBAR_LENGTH + "px")
            .setCssProperty("border-color", SpreadsheetDominoKitColor.VIEWPORT_LINES_COLOR.toString())
            .setCssProperty("border-style", "solid")
            .setCssProperty("border-width", "1px")
            .setCssProperty("background-color", SpreadsheetDominoKitColor.VIEWPORT_HEADER_UNSELECTED_BACKGROUND_COLOR.toString())
            .addChangeListener(
                (Optional<SpreadsheetRowReference> oldValue, Optional<SpreadsheetRowReference> newValue) -> {
                    this.pushNewHome(
                        newValue.orElse(null)
                    );
                }
            );
    }

    private final SpreadsheetViewportScrollbarComponent<SpreadsheetRowReference> verticalScrollbar;

    private void pushNewHome(final SpreadsheetColumnOrRowReference newHomeColumnOrRow) {
        if (null != newHomeColumnOrRow) {
            final SpreadsheetViewportComponentContext context = this.context;
            SpreadsheetCellReference home = context.home();
            if (newHomeColumnOrRow.isColumn()) {
                home = home.setColumn(
                    newHomeColumnOrRow.toColumn()
                );
            } else {
                home = home.setRow(
                    newHomeColumnOrRow.toRow()
                );
            }

            context.pushHistoryToken(
                context.historyToken()
                    .setNavigation(
                        Optional.of(
                            SpreadsheetViewportHomeNavigationList.with(home)
                        )
                    )
            );
        }
    }

    private final SpreadsheetNavigateLinkComponent navigateLink;

    // misc.............................................................................................................

    public void setWidthAndHeight(final int width,
                                  final int height) {
        final boolean reload = width > this.viewportGridWidth || height > this.viewportGridHeight;

        final SpreadsheetViewportComponentContext context = this.context;
        context.debug(this.getClass().getSimpleName() + ".setWidthAndHeight " + width + "x" + height + " was " + this.viewportGridWidth + "x" + this.viewportGridHeight + " reload: " + reload);

        this.width = width;
        this.height = height;
        this.viewportGridWidth = 0;
        this.viewportGridHeight = 0;

        this.reload = reload;

        if(this.isOpen()) {
            this.refresh(context);
        } else {
            this.refreshLayout();
        }

        this.loadViewportCellsIfNecessary();
    }

    /**
     * The width for the viewport including the headers.
     */
    int width;

    /**
     * The height for the viewport including the headers and formula editor
     */
    int height;

    /**
     * The width allocated to the grid, which includes the scrollbars but without the headers.
     */
    int viewportGridWidth;

    /**
     * The height allocated to the grid, which includes the scrollbars but without the headers.
     */
    int viewportGridHeight;

    /**
     * A snapshot of the {@link SpreadsheetMetadata} at the last refresh.
     */
    private SpreadsheetMetadata refreshMetadata;

    /**
     * Creates a {@link SpreadsheetViewport} using the current selection.
     */
    SpreadsheetViewport viewport() {
        return this.viewport(
            this.context.historyToken()
                .anchoredSelectionOrEmpty()
        );
    }

    public SpreadsheetViewport viewport(final Optional<AnchoredSpreadsheetSelection> anchoredSelection) {
        return this.context.home()
            .viewportRectangle(
            this.viewportGridWidth,
            this.viewportGridHeight
            ).viewport()
            .setAnchoredSelection(anchoredSelection);
    }

    /**
     * Refreshes the width and heights but does not refresh the actual content.
     */
    private void refreshLayout() {
        final SpreadsheetViewportComponentContext context = this.context;

        final SpreadsheetMetadata metadata = context.spreadsheetMetadata();

        this.mustRefresh = metadata.shouldViewRefresh(this.refreshMetadata);
        this.refreshMetadata = metadata;

        final HistoryToken historyToken = context.historyToken();

        final boolean autoHideScrollbars = metadata.get(SpreadsheetMetadataPropertyName.AUTO_HIDE_SCROLLBARS)
            .orElse(false);
        this.autoHideScrollbars = autoHideScrollbars;

        final boolean shouldShowFormulaEditor = metadata.get(SpreadsheetMetadataPropertyName.SHOW_FORMULA_EDITOR)
            .orElse(true) ||
            (historyToken instanceof SpreadsheetCellFormulaHistoryToken && false == historyToken.isSave());
        this.shouldShowFormulaEditor = shouldShowFormulaEditor;

        this.shouldHideZeroValues = metadata.get(SpreadsheetMetadataPropertyName.HIDE_ZERO_VALUES)
            .orElse(false);
        this.shouldShowFormulas = metadata.get(SpreadsheetMetadataPropertyName.SHOW_FORMULAS)
            .orElse(false);
        this.shouldShowHeaders = metadata.get(SpreadsheetMetadataPropertyName.SHOW_HEADINGS)
            .orElse(true);

        final int width = this.width;
        final int height = this.height;

        this.formulaContainer.setDisplay(
            shouldShowFormulaEditor ?
                "" :
                "none"
        );

        int gridWidth = width;
        int gridHeight = height;

        if(shouldShowFormulaEditor) {
            gridHeight = gridHeight -
                (
                    GWT.isClient() ?
                    this.formula.height() :
                    FORMULA_HEIGHT
                );
        }

        this.gridContainer.setWidth(
            gridWidth + "px"
        ).setHeight(
            gridHeight + "px"
        );

        int viewportGridWidth = gridWidth;
        int viewportGridHeight = gridHeight;

        if(this.shouldShowHeaders) {
            viewportGridWidth = viewportGridWidth - SpreadsheetViewportContext.ROW_HEADER_WIDTH_PIXELS;
            viewportGridHeight = viewportGridHeight - SpreadsheetViewportContext.COLUMN_HEADER_HEIGHT_PIXELS;
        }

        this.viewportGridWidth = viewportGridWidth;
        this.viewportGridHeight = viewportGridHeight;

        this.horizontalScrollbar.setAutoHideScrollbars(autoHideScrollbars);
        this.verticalScrollbar.setCssProperty(
            "height",
            (viewportGridHeight - SCROLLBAR_LENGTH) + "px"
        ).setAutoHideScrollbars(autoHideScrollbars);

        this.bottom.setCssProperty("width", width + "px");
    }

    // SpreadsheetViewportComponentSpreadsheetViewportScrollbarComponentContext.autoHideScrollbars()
    boolean autoHideScrollbars;

    boolean shouldShowFormulaEditor;

    boolean shouldHideZeroValues;

    boolean shouldShowFormulas;

    boolean shouldShowHeaders;

    boolean mustRefresh;

    private final static int SCROLLBAR_LENGTH = 32;

    final static int FORMULA_HEIGHT = 64;

    // HistoryTokenAwareComponentLifecycle..............................................................................

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
        this.refreshLayout();

        final HistoryToken historyToken = context.historyToken();
        final Optional<AnchoredSpreadsheetSelection> maybeAnchorSelection = historyToken.anchoredSelectionOrEmpty();

        this.formulaCellLinksRefresh();
        this.refreshTable(maybeAnchorSelection);
        this.horizontalScrollbar.refresh(context);
        this.verticalScrollbar.refresh(context);

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
    }

    private void refreshTable(final Optional<AnchoredSpreadsheetSelection> maybeAnchorSelection) {
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

        final SpreadsheetMetadata metadata = this.context.spreadsheetMetadata();

        this.table.refresh(
            metadata.id()
                .get(),
            metadata.getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_NAME),
            windows,
            selected
        );
    }

    private void giveViewportSelectionFocus(final AnchoredSpreadsheetSelection selection,
                                            final RefreshContext context) {
        if(GWT.isScript()) {
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

                    context.debug(this.getClass().getSimpleName() + ".giveViewportSelectionFocus " + spreadsheetSelection + " focus element " + element);
                    element.focus();
                }
            } else {
                context.debug(this.getClass().getSimpleName() + ".giveViewportSelectionFocus " + spreadsheetSelection + " element not found!");
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
            final SpreadsheetContextMenu<?> menu = SpreadsheetContextMenu.wrap(
                SpreadsheetContextMenuTargets.element(element),
                context
            );

            List<SpreadsheetFormatterMenu> spreadsheetFormatterSelectorMenus = this.spreadsheetFormatterSelectorMenus;
            if (null == spreadsheetFormatterSelectorMenus) {
                spreadsheetFormatterSelectorMenus = Lists.empty();
            }

            final RecentValueSavesContext recentValueSavesContext = this.context;

            SpreadsheetSelectionMenu.build(
                historyToken,
                menu,
                SpreadsheetViewportComponentSpreadsheetSelectionMenuContext.with(
                    recentValueSavesContext.recentValueSaves(SpreadsheetFormatterSelector.class),
                    spreadsheetFormatterSelectorMenus,
                    recentValueSavesContext.recentValueSaves(SpreadsheetParserSelector.class),
                    recentValueSavesContext.recentValueSaves(TEXT_STYLE_PROPERTY_CLASS),
                    recentValueSavesContext.recentValueSaves(ValidatorSelector.class),
                    this.context
                )
            );

            menu.focus();
        }
    }

    private final static Class<TextStyleProperty<?>> TEXT_STYLE_PROPERTY_CLASS = Cast.to(TextStyleProperty.class);

    /**
     * The {@link SpreadsheetExpressionReference} that was used to fetch {@link SpreadsheetFormatterMenu}.
     * This is used to detect selection changes,
     */
    // @VisibleForTesting
    SpreadsheetExpressionReference spreadsheetFormatterSelectorSelection;

    /**
     * This should be updated each time the {@link SpreadsheetMetadataPropertyName#FORMATTERS} property changes.
     */
    private List<SpreadsheetFormatterMenu> spreadsheetFormatterSelectorMenus;


    @Override
    public void openGiveFocus(final RefreshContext context) {
        // nop
    }

    @Override
    public void close(final RefreshContext context) {
        this.setVisibility(false);
        this.open = false;
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
            final SpreadsheetViewport viewport = maybeSpreadsheetViewport.get();
            this.synchronizeHistoryToken(
                viewport,
                context
            );
            this.reloadSpreadsheetMetadataIfViewportChanged(viewport);
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
        if (this.table.isEditing()) {
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

    /**
     * Because a {@link SpreadsheetDelta} response could contain a new {@link SpreadsheetViewport} the entire {@link SpreadsheetMetadata}
     * should be reloaded. When the {@link SpreadsheetMetadata} returns the UI viewport home and scrollbars will be
     * refreshed.
     */
    private void reloadSpreadsheetMetadataIfViewportChanged(final SpreadsheetViewport viewport) {
        if(false == viewport.rectangle().home().equalsIgnoreReferenceKind(this.context.home())) {
            this.context.spreadsheetMetadataFetcher()
                .getSpreadsheetMetadata(
                    this.metadata.getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_ID)
                );
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

        // the returned metadata isnt any different from the current metadata skip rendering again.
        if (this.reload) {
            this.componentLifecycleHistoryTokenQuery(context);
        }

        if (fetchSpreadsheetFormatterSelectorsMenu) {
            this.spreadsheetFormatterSelectorSelection = null;
            this.spreadsheetFormatterSelectorMenus = null;
        }

        this.refreshIfOpen(context);
        if(false == this.isOpen()) {
            this.refreshLayout();
        }

        this.loadViewportCellsIfNecessary();
    }

    /**
     * Initial metadata is EMPTY or nothing.
     */
    // @VisibleForTesting
    SpreadsheetMetadata metadata = SpreadsheetMetadata.EMPTY;

    /**
     * Tests if various requirements are ready and the viewport should be loaded again.
     */
    private void loadViewportCellsIfNecessary() {
        final SpreadsheetViewportComponentContext context = this.context;

        if (context.spreadsheetDeltaFetcher().waitingRequestCount() == 0) {
            final boolean reload = this.reload;
            final int width = this.viewportGridWidth;
            final int height = this.viewportGridHeight;

            final SpreadsheetMetadata metadata = context.spreadsheetMetadata();
            if (reload && width > 0 && height > 0 && metadata.isNotEmpty()) {
                if (metadata.isEmpty()) {
                    context.debug(this.getClass().getSimpleName() + ".loadViewportCellsIfNecessary waiting for metadata");
                } else {
                    this.loadViewportCells();
                }
            } else {
                context.debug(this.getClass().getSimpleName() + ".loadViewportCellsIfNecessary not ready, reload: " + reload + " width: " + width + " height: " + height + " metadata.isEmpty: " + metadata.isEmpty() + " open " + this.open);
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

        final SpreadsheetViewport viewport = this.viewport()
            .setNavigations(this.navigations);

        context.debug(this.getClass().getSimpleName() + ".loadViewportCells id: " + id + " viewport: " + viewport);

        this.reload = false;
        this.navigations = SpreadsheetViewportNavigationList.EMPTY;

        context.spreadsheetDeltaFetcher()
            .getCells(
                id,
                viewport
            );
    }

    /**
     * Accepts and adds the given {@link SpreadsheetViewportNavigation} to the buffer and if possible
     * sends it to the server for actioning.
     */
    void onNavigation(final SpreadsheetViewportNavigation navigation) {
        Objects.requireNonNull(navigation, "navigation");

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

    /**
     * The ID assigned to the container TABLE element.
     */
    final static String ID = "viewport";

    /**
     * Prefix for any ui within a viewport
     */
    final static String ID_PREFIX = ID + "-";

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
        printer.println(this.getClass().getSimpleName());
        printer.indent();

        {
            this.formula.printTree(printer);
            this.table.printTree(printer);
            this.verticalScrollbar.printTree(printer);
            this.horizontalScrollbar.printTree(printer);
            this.navigateLink.printTree(printer);
        }

        printer.outdent();
    }
}
