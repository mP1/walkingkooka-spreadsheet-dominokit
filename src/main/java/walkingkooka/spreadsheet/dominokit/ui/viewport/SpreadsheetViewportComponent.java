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
import elemental2.dom.HTMLTableCellElement;
import elemental2.dom.HTMLTableElement;
import elemental2.dom.HTMLTableRowElement;
import elemental2.dom.HTMLTableSectionElement;
import elemental2.dom.Headers;
import elemental2.dom.KeyboardEvent;
import elemental2.dom.MouseEvent;
import jsinterop.base.Js;
import org.dominokit.domino.ui.badges.Badge;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.elements.DivElement;
import org.dominokit.domino.ui.elements.TBodyElement;
import org.dominokit.domino.ui.elements.TDElement;
import org.dominokit.domino.ui.elements.THElement;
import org.dominokit.domino.ui.elements.TableElement;
import org.dominokit.domino.ui.elements.TableRowElement;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.menu.direction.DropDirection;
import org.dominokit.domino.ui.popover.Tooltip;
import org.dominokit.domino.ui.utils.DominoElement;
import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.color.Color;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.predicate.Predicates;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetError;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.SpreadsheetViewportRectangle;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dom.Doms;
import walkingkooka.spreadsheet.dominokit.dom.Key;
import walkingkooka.spreadsheet.dominokit.history.AnchoredSpreadsheetSelectionHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHighlightSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellMenuHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetColumnMenuHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetColumnSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowMenuHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowSelectHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetLabelMappingFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.ui.Component;
import walkingkooka.spreadsheet.dominokit.ui.ComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.ui.metadatacolorpicker.SpreadsheetMetadataColorPickerComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportNavigation;
import walkingkooka.tree.text.BorderStyle;
import walkingkooka.tree.text.FontFamily;
import walkingkooka.tree.text.FontSize;
import walkingkooka.tree.text.FontStyle;
import walkingkooka.tree.text.FontVariant;
import walkingkooka.tree.text.FontWeight;
import walkingkooka.tree.text.Hyphens;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextDecorationLine;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.tree.text.VerticalAlign;
import walkingkooka.tree.text.WordBreak;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.dominokit.domino.ui.style.ColorsCss.dui_bg_orange;
import static org.dominokit.domino.ui.style.SpacingCss.dui_rounded_full;

/**
 * A ui that displays a table holding the cells and headers for the columns and rows.
 */
public final class SpreadsheetViewportComponent implements Component<HTMLDivElement>,
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

        this.formulaComponent = this.formula();

        this.tableElement = this.table();

        this.horizontalScrollbarThumb = this.horizontalScrollbarThumb();
        this.horizontalScrollbar = this.horizontalScrollbar();

        this.verticalScrollbarThumb = this.verticalScrollbarThumb();
        this.verticalScrollbar = this.verticalScrollbar();

        this.tableContainer = this.tableContainer();

        this.root = this.root();

        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetMetadataWatcher(this);
        context.addSpreadsheetDeltaWatcher(this);

        context.addHistoryTokenWatcher(this::onHistoryTokenChangeSpreadsheetCellHighlightSaveHistoryToken);
    }

    // root.............................................................................................................

    private DivElement root() {
        final DivElement root = ElementsFactory.elements.div();

        // overflow:hidden required to prevent scrollbars...
        root.style("width:100%; border: none; margin: 0px; padding: none; overflow: hidden");

        root.appendChild(this.formulaComponent);
        root.appendChild(this.tableContainer);

        return root;
    }

    /**
     * The root or container that holds the {@link SpreadsheetViewportFormulaComponent} and {@link #tableElement}.
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

    private SpreadsheetViewportFormulaComponent formula() {
        return SpreadsheetViewportFormulaComponent.with(this.context);
                //.helperTextAlwaysExpanded();
    }

    private final SpreadsheetViewportFormulaComponent formulaComponent;

    // table container..................................................................................................

    private DivElement tableContainer() {
        final DivElement container = ElementsFactory.elements.div();
        container.style("position: relative; top: 0; left 0px; border: none; margin: 0px; padding: none; width:100%;");
        container.appendChild(this.tableElement);

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

                final Optional<SpreadsheetSelection> maybeSelection = parseId(element.id);
                if (maybeSelection.isPresent()) {
                    final SpreadsheetSelection selection = maybeSelection.get();
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
                                            Optional.of(selection)
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
     * Renders a drop down menu.
     */
    private void renderContextMenu(final AnchoredSpreadsheetSelectionHistoryToken historyToken,
                                   final AppContext context) {
        // show context setMenu1
        final AnchoredSpreadsheetSelection anchored = historyToken.selection();
        final SpreadsheetSelection selection = anchored.selection();
        final Optional<Element> maybeElement = this.findElement(
                selection.focused(anchored.anchor()),
                context
        );

        context.debug("SpreadsheetViewportComponent.renderContextMenu " + anchored);

        if (maybeElement.isPresent()) {
            final DominoElement<?> element = new DominoElement<>(maybeElement.get());

            // ALIGNMENT
            // COLOR
            // BACKGROUND COLOR
            // BOLD
            // ITALICS
            // STRIKE THRU
            // UNDERLINE
            // VERTICAL ALIGNMENT
            // CLEAR STYLE
            // ----
            // FORMAT
            // PARSE
            // -----
            // CLEAR
            // DELETE
            // -------
            // FREEZE
            // UNFREEZE
            // -------
            // LABELS
            final SpreadsheetContextMenu menu = SpreadsheetContextMenu.empty(
                    element,
                    context
            );

            // TODO add tick if already selected
            renderContextMenuAlignment(historyToken, menu);
            renderContextMenuColor(historyToken, menu, context);
            renderContextMenuBackgroundColor(historyToken, menu, context);
            renderContextMenuStyle(historyToken, menu);
            renderContextMenuVerticalAlignment(historyToken, menu);
            renderContextMenuClearStyle(historyToken, menu);

            menu.separator();
            {
                final Locale locale = context.spreadsheetMetadata()
                        .getOrFail(SpreadsheetMetadataPropertyName.LOCALE);

                renderContextMenuFormat(historyToken, locale, menu);
                renderContextMenuParse(historyToken, locale, menu);
            }
            menu.separator();

            renderContextMenuClearDelete(historyToken, menu);
            renderContextMenuInsertColumns(historyToken, selection, menu);
            renderContextMenuInsertRows(historyToken, selection, menu);
            renderContextMenuFreezeUnfreeze(historyToken, menu);

            menu.separator();
            renderContextMenuLabel(historyToken, selection, menu, context);

            menu.focus();
        }
    }

    private static void renderContextMenuParse(final HistoryToken historyToken,
                                               final Locale locale,
                                               final SpreadsheetContextMenu menu) {
        SpreadsheetViewportComponentPatternMenuParse.with(
                historyToken,
                locale
        ).build(
                menu.subMenu(
                        VIEWPORT_CONTEXT_MENU_ID_PREFIX + "parse",
                        "Parse",
                        SpreadsheetIcons.parsePattern()
                )
        );
    }

    private static void renderContextMenuFormat(final HistoryToken historyToken,
                                                final Locale locale,
                                                final SpreadsheetContextMenu menu) {
        SpreadsheetViewportComponentPatternMenuFormat.with(
                historyToken,
                locale
        ).build(
                menu.subMenu(
                        VIEWPORT_CONTEXT_MENU_ID_PREFIX + "format",
                        "Format",
                        SpreadsheetIcons.formatPattern()
                )
        );
    }

    private static void renderContextMenuColor(final HistoryToken historyToken,
                                               final SpreadsheetContextMenu menu,
                                               final AppContext context) {
        renderContextMenuColorItem(
                "color",
                "Color",
                historyToken.setStyle(TextStylePropertyName.COLOR),
                menu,
                context
        );
    }

    private static void renderContextMenuBackgroundColor(final HistoryToken historyToken,
                                                         final SpreadsheetContextMenu menu,
                                                         final AppContext context) {
        renderContextMenuColorItem(
                "background-color",
                "Background color",
                historyToken.setStyle(TextStylePropertyName.BACKGROUND_COLOR),
                menu,
                context
        );
    }

    private static void renderContextMenuColorItem(final String id,
                                                   final String text,
                                                   final HistoryToken historyToken,
                                                   final SpreadsheetContextMenu menu,
                                                   final AppContext context) {
        final SpreadsheetContextMenu sub = menu.subMenu(
                VIEWPORT_CONTEXT_MENU_ID_PREFIX + id + SpreadsheetIds.MENU_ITEM,
                text,
                SpreadsheetIcons.palette()
        );

        final SpreadsheetMetadataColorPickerComponent colors = SpreadsheetMetadataColorPickerComponent.with(historyToken);
        colors.element().className = "dui dui-menu-item";

        colors.refreshAll(
                historyToken,
                context.spreadsheetMetadata()
        );
        sub.item(colors);
    }

    private static void renderContextMenuClearStyle(final HistoryToken historyToken,
                                                    final SpreadsheetContextMenu menu) {
        menu.item(
                VIEWPORT_CONTEXT_MENU_ID_PREFIX + "clear-style" + SpreadsheetIds.MENU_ITEM,
                "Clear style",
                SpreadsheetIcons.clearStyle(),
                historyToken.setStyle(TextStylePropertyName.ALL)
                        .clearSave()
        );
    }

    private static void renderContextMenuClearDelete(final HistoryToken historyToken,
                                                     final SpreadsheetContextMenu menu) {
        menu.item(
                VIEWPORT_CONTEXT_MENU_ID_PREFIX + "clear" + SpreadsheetIds.MENU_ITEM,
                "Clear",
                historyToken.setClear()
        ).item(
                VIEWPORT_CONTEXT_MENU_ID_PREFIX + "delete" + SpreadsheetIds.MENU_ITEM,
                "Delete",
                historyToken.setDelete()
        ).separator();
    }

    private static void renderContextMenuStyle(final HistoryToken historyToken,
                                               final SpreadsheetContextMenu menu) {
        menu.item(
                VIEWPORT_CONTEXT_MENU_ID_PREFIX + "bold" + SpreadsheetIds.MENU_ITEM,
                "Bold",
                SpreadsheetIcons.bold(),
                historyToken.setStyle(TextStylePropertyName.FONT_WEIGHT)
                        .setSave(FontWeight.BOLD)
        );

        menu.item(
                VIEWPORT_CONTEXT_MENU_ID_PREFIX + "italics" + SpreadsheetIds.MENU_ITEM,
                "Italics",
                SpreadsheetIcons.italics(),
                historyToken.setStyle(TextStylePropertyName.FONT_STYLE)
                        .setSave(FontStyle.ITALIC)
        );

        menu.item(
                VIEWPORT_CONTEXT_MENU_ID_PREFIX + "strike-thru" + SpreadsheetIds.MENU_ITEM,
                "Strike-thru",
                SpreadsheetIcons.strikethrough(),
                historyToken.setStyle(TextStylePropertyName.TEXT_DECORATION_LINE)
                        .setSave(TextDecorationLine.LINE_THROUGH)
        );

        menu.item(
                VIEWPORT_CONTEXT_MENU_ID_PREFIX + "underline" + SpreadsheetIds.MENU_ITEM,
                "Underline",
                SpreadsheetIcons.underline(),
                historyToken.setStyle(TextStylePropertyName.TEXT_DECORATION_LINE)
                        .setSave(TextDecorationLine.UNDERLINE)
        );
    }

    private static void renderContextMenuAlignment(final HistoryToken historyToken,
                                                   final SpreadsheetContextMenu menu) {
        menu.subMenu(
                VIEWPORT_CONTEXT_MENU_ID_PREFIX + "alignment",
                "Alignment"
        ).item(
                VIEWPORT_CONTEXT_MENU_ID_PREFIX + "left" + SpreadsheetIds.MENU_ITEM,
                        "Left",
                        SpreadsheetIcons.alignLeft(),
                        historyToken.setStyle(
                                TextStylePropertyName.TEXT_ALIGN
                        ).setSave(TextAlign.LEFT)
                ).item(
                VIEWPORT_CONTEXT_MENU_ID_PREFIX + "center" + SpreadsheetIds.MENU_ITEM,
                        "Center",
                        SpreadsheetIcons.alignCenter(),
                        historyToken.setStyle(
                                TextStylePropertyName.TEXT_ALIGN
                        ).setSave(TextAlign.CENTER)
                ).item(
                VIEWPORT_CONTEXT_MENU_ID_PREFIX + "right" + SpreadsheetIds.MENU_ITEM,
                        "Right",
                        SpreadsheetIcons.alignRight(),
                        historyToken.setStyle(
                                TextStylePropertyName.TEXT_ALIGN
                        ).setSave(TextAlign.RIGHT)
                ).item(
                VIEWPORT_CONTEXT_MENU_ID_PREFIX + "justify" + SpreadsheetIds.MENU_ITEM,
                        "Justify",
                        SpreadsheetIcons.alignJustify(),
                        historyToken.setStyle(
                                TextStylePropertyName.TEXT_ALIGN
                        ).setSave(TextAlign.JUSTIFY)
                );
    }

    private static void renderContextMenuVerticalAlignment(final HistoryToken historyToken,
                                                           final SpreadsheetContextMenu menu) {
        menu.subMenu(
                VIEWPORT_CONTEXT_MENU_ID_PREFIX + "vertical-alignment",
                "Vertical Alignment"
        ).item(
                VIEWPORT_CONTEXT_MENU_ID_PREFIX + "top" + SpreadsheetIds.MENU_ITEM,
                        "Top",
                        SpreadsheetIcons.verticalAlignTop(),
                        historyToken.setStyle(
                                TextStylePropertyName.VERTICAL_ALIGN
                        ).setSave(VerticalAlign.TOP)
                ).item(
                VIEWPORT_CONTEXT_MENU_ID_PREFIX + "middle" + SpreadsheetIds.MENU_ITEM,
                        "Middle",
                        SpreadsheetIcons.verticalAlignMiddle(),
                        historyToken.setStyle(
                                TextStylePropertyName.VERTICAL_ALIGN
                        ).setSave(VerticalAlign.MIDDLE)
                ).item(
                VIEWPORT_CONTEXT_MENU_ID_PREFIX + "bottom" + SpreadsheetIds.MENU_ITEM,
                        "Bottom",
                        SpreadsheetIcons.verticalAlignBottom(),
                        historyToken.setStyle(
                                TextStylePropertyName.VERTICAL_ALIGN
                        ).setSave(VerticalAlign.BOTTOM)
                );
    }

    private void renderContextMenuInsertColumns(final HistoryToken historyToken,
                                                final SpreadsheetSelection selection,
                                                final SpreadsheetContextMenu menu) {
        if (selection.isColumnReference() | selection.isColumnReferenceRange() | selection.isCellReference() || selection.isCellRange()) {
            final HistoryToken columnHistoryToken = historyToken.setColumn(
                    selection.toColumnOrColumnRange()
            );
            this.insertSubMenu(
                    menu.subMenu(
                            VIEWPORT_CONTEXT_MENU_ID_PREFIX + "insert-before-column",
                            "Insert before column"
                    ),
                    VIEWPORT_CONTEXT_MENU_ID_PREFIX + "insert-before-column-",
                    columnHistoryToken::setInsertBefore
            );
            this.insertSubMenu(
                    menu.subMenu(
                            VIEWPORT_CONTEXT_MENU_ID_PREFIX + "insert-after-column",
                            "Insert after column"
                    ),
                    VIEWPORT_CONTEXT_MENU_ID_PREFIX + "insert-after-column-",
                    columnHistoryToken::setInsertAfter
            );
            menu.separator();
        }
    }

    private void renderContextMenuInsertRows(final HistoryToken historyToken,
                                             final SpreadsheetSelection selection,
                                             final SpreadsheetContextMenu menu) {
        if (selection.isRowReference() | selection.isRowReferenceRange() | selection.isCellReference() || selection.isCellRange()) {
            final HistoryToken rowHistoryToken = historyToken.setRow(
                    selection.toRowOrRowRange()
            );
            this.insertSubMenu(
                    menu.subMenu(
                            VIEWPORT_CONTEXT_MENU_ID_PREFIX + "insert-row-before",
                            "Insert before row"
                    ),
                    VIEWPORT_CONTEXT_MENU_ID_PREFIX + "insert-row-before-",
                    rowHistoryToken::setInsertBefore
            );
            this.insertSubMenu(
                    menu.subMenu(
                            VIEWPORT_CONTEXT_MENU_ID_PREFIX + "insert-row-before",
                            "Insert after row"
                    ),
                    VIEWPORT_CONTEXT_MENU_ID_PREFIX + "insert-row-before-",
                    rowHistoryToken::setInsertAfter
            );
            menu.separator();
        }
    }

    private static void renderContextMenuFreezeUnfreeze(final HistoryToken historyToken,
                                                        final SpreadsheetContextMenu menu) {
        menu.item(
                VIEWPORT_CONTEXT_MENU_ID_PREFIX + "freeze" + SpreadsheetIds.MENU_ITEM,
                "Freeze",
                historyToken.freezeOrEmpty()
        ).item(
                VIEWPORT_CONTEXT_MENU_ID_PREFIX + "unfreeze" + SpreadsheetIds.MENU_ITEM,
                "Unfreeze",
                historyToken.unfreezeOrEmpty()
        );

        menu.separator();
    }

    private static void renderContextMenuLabel(final HistoryToken historyToken,
                                               final SpreadsheetSelection selection,
                                               final SpreadsheetContextMenu menu,
                                               final AppContext context) {
        final Set<SpreadsheetLabelMapping> labelMappings = context.viewportCache().labelMappings(selection);

        SpreadsheetContextMenu sub = menu.subMenu(
                VIEWPORT_CONTEXT_MENU_ID_PREFIX + "label" + SpreadsheetIds.MENU_ITEM,
                "Labels",
                Badge.create(
                        String.valueOf(labelMappings.size())
                ).addCss(
                        dui_bg_orange,
                        dui_rounded_full
                )
        );

        int i = 0;

        // create items for each label
        for (final SpreadsheetLabelMapping mapping : labelMappings) {
            final SpreadsheetLabelName label = mapping.label();

            sub = sub.item(
                    VIEWPORT_CONTEXT_MENU_ID_PREFIX + "label-" + i + SpreadsheetIds.MENU_ITEM,
                    label + " (" + mapping.target() + ")",
                    historyToken.setLabelName(
                            Optional.of(label)
                    )
            );

            i++;
        }

        sub.item(
                VIEWPORT_CONTEXT_MENU_ID_PREFIX + "label-create" + SpreadsheetIds.MENU_ITEM,
                "Create...",
                historyToken.setLabelName(Optional.empty())
        );
    }

    private void insertSubMenu(final SpreadsheetContextMenu menu,
                               final String idPrefix,
                               final Function<Integer, HistoryToken> setCount) {
        for (int i = 1; i <= 3; i++) {
            menu.item(
                    idPrefix + "-" + i + SpreadsheetIds.MENU_ITEM,
                    String.valueOf(i),
                    setCount.apply(i)
            );
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
     * Prefix for any ui within a viewport
     */
    private final static String VIEWPORT_ID_PREFIX = VIEWPORT_ID + "-";

    final static String VIEWPORT_CONTEXT_MENU_ID_PREFIX = VIEWPORT_ID + "-context-menu-";

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

        scrollbar.id(VIEWPORT_ID_PREFIX + idSuffix);
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

        this.context.debug("SpreadsheetViewportComponent.horizontalScrollbarOnClick clientY: " + clientY + "< " +  topClientY + " " + navigation);

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
        thumb.id(VIEWPORT_ID_PREFIX + idSuffix);
        thumb.style("position: absolute; width:" + width + "; height: " + height + "; box-sizing: border-box; border-color: black; border-style: solid; border-width: 1px; background-color: #fff; border-radius: " + (SCROLLBAR_LENGTH / 3) + "px");
        return thumb;
    }

    private HTMLElement horizontalScrollbarLeft() {
        return scrollbarArrow(
                "h-scrollbar-left",
                SpreadsheetIcons.arrowLeft(),
                "right: " + (SCROLLBAR_LENGTH + BUTTON_LENGTH - 8) + "px; bottom: -10px;",
                () -> SpreadsheetViewportNavigation.leftPixel(this.viewportTableCellsWidth() - 1)
        );
    }

    private HTMLElement verticalScrollbarUp() {
        return scrollbarArrow(
                "v-scrollbar-up",
                SpreadsheetIcons.arrowUp(),
                "bottom: " + (SCROLLBAR_LENGTH + BUTTON_LENGTH - 3) + "px; right: -16px;",
                () -> SpreadsheetViewportNavigation.upPixel(this.viewportTableCellsHeight() - 1)
        );
    }

    private HTMLElement horizontalScrollbarRight() {
        return scrollbarArrow(
                "h-scrollbar-right",
                SpreadsheetIcons.arrowRight(),
                "right: " + (SCROLLBAR_LENGTH - 8) + "px; bottom: -10px;",
                () -> SpreadsheetViewportNavigation.rightPixel(this.viewportTableCellsWidth() - 1)
        );
    }

    private HTMLElement verticalScrollbarDown() {
        return scrollbarArrow(
                "v-scrollbar-down",
                SpreadsheetIcons.arrowDown(),
                "bottom: " + (SCROLLBAR_LENGTH - 3) + "px; right: -16px;",
                () -> SpreadsheetViewportNavigation.downPixel(this.viewportTableCellsHeight() - 1)
        );
    }

    private HTMLElement scrollbarArrow(final String idSuffix,
                                       final MdiIcon icon,
                                       final String css,
                                       final Supplier<SpreadsheetViewportNavigation> navigation) {
        final Button button = Button.create(icon)
                .circle();

        final HTMLElement element = button.element();
        element.id = VIEWPORT_ID_PREFIX + idSuffix;
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
     * Updates the coordinates and dimensions of both the horizonal and vertical scrollbar thumbs. The calculations are
     * done using the last window.
     */
    private void renderScrollbars() {
        final AppContext context = this.context;
        final SpreadsheetViewportCache cache = context.viewportCache();
        final Optional<SpreadsheetCellRange> maybeLast = cache.windows()
                .last();
        final OptionalInt maybeColumnCount = cache.columnCount();
        final OptionalInt maybeRowCount = cache.rowCount();

        final DivElement horizontalScrollbarThumb = this.horizontalScrollbarThumb;
        final DivElement verticalScrollbarThumb = this.verticalScrollbarThumb;

        if (maybeLast.isPresent() && maybeColumnCount.isPresent() && maybeRowCount.isPresent()) {
            final SpreadsheetCellRange last = maybeLast.get();
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

            context.debug("SpreadsheetViewportComponent.renderScrollbars " + last + " left: " + left + " top: " + top + " hLeft: " + hLeft + " hWidth: " + hWidth + " vTop: " + vTop + " vHeight: " + vHeight);
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
                .style.cssText = "width: " + this.viewportWidth()  + "px; height: " + this.viewportHeight() + "px; overflow: hidden; position: relative;";

        this.tableElement.element()
                .style.cssText = "width: " + this.viewportTableWidth() + "px; height: " + this.viewportTableHeight() + "px; overflow: hidden";

        this.loadViewportCellsIfNecessary(context);
    }

    public SpreadsheetViewport viewport(final Optional<AnchoredSpreadsheetSelection> selection,
                                        final AppContext context) {
        final SpreadsheetCellReference home = context.spreadsheetMetadata()
                .getOrFail(SpreadsheetMetadataPropertyName.VIEWPORT)
                .rectangle()
                .home();
        return home.viewportRectangle(
                        this.viewportTableCellsWidth(),
                        this.viewportTableCellsHeight()
                ).viewport()
                .setSelection(selection);
    }

    private int viewportWidth() {
        return this.width;
    }

    private int viewportTableWidth() {
        return this.viewportWidth() - SCROLLBAR_LENGTH;
    }

    private int viewportTableCellsWidth() {
        return this.viewportTableWidth() -
                (int) ROW_WIDTH.pixelValue() -
                SCROLLBAR_LENGTH;
    }

    private int viewportHeight() {
        return this.height - this.formulaComponent.element()
                .offsetHeight;
    }

    private int viewportTableHeight() {
        return this.viewportHeight() -
                SCROLLBAR_LENGTH;
    }

    private int viewportTableCellsHeight() {
        return this.viewportTableHeight() -
                (int) COLUMN_HEIGHT.pixelValue();
    }

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

    // render..........................................................................................................

    /**
     * Renders the TABLE element again using its current state. Note no elements are cached or re-used, everything
     * is rendered again!
     */
    private void render(final AppContext context) {
        final HistoryToken historyToken = context.historyToken();
        final Optional<AnchoredSpreadsheetSelection> maybeAnchored = historyToken.selectionOrEmpty();
        this.setSelected(maybeAnchored);

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
                    historyToken.cast(AnchoredSpreadsheetSelectionHistoryToken.class)
                            .selection(),
                    context
            );
        }

        if (historyToken instanceof SpreadsheetCellMenuHistoryToken ||
                historyToken instanceof SpreadsheetColumnMenuHistoryToken ||
                historyToken instanceof SpreadsheetRowMenuHistoryToken) {
            this.renderContextMenu(
                    historyToken.cast(AnchoredSpreadsheetSelectionHistoryToken.class),
                    context
            );
        }

        this.renderScrollbars();
    }

    /**
     * Creates a THEAD holding a TR with the SELECT ALL and COLUMN headers.
     */
    private HTMLTableSectionElement renderColumnHeaders(final Collection<SpreadsheetColumnReference> columns,
                                                        final AppContext context) {
        final TableRowElement tr = ElementsFactory.elements.tr()
                .appendChild(
                        this.renderSelectAllCells(context)
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
    private HTMLTableCellElement renderSelectAllCells(final AppContext context) {
        return ElementsFactory.elements.th()
                .id(VIEWPORT_SELECT_ALL_CELLS)
                .appendChild("ALL")
                .style(
                        this.selectAllCellsStyle()
                                .set(
                                        TextStylePropertyName.MIN_WIDTH,
                                        ROW_WIDTH
                                ).set(
                                        TextStylePropertyName.WIDTH,
                                        ROW_WIDTH
                                ).set(
                                        TextStylePropertyName.MIN_HEIGHT,
                                        COLUMN_HEIGHT
                                ).set(
                                        TextStylePropertyName.HEIGHT,
                                        COLUMN_HEIGHT
                                ).css() + "box-sizing: border-box;")
                .element();
    }

    private final static String VIEWPORT_SELECT_ALL_CELLS = VIEWPORT_ID_PREFIX + "select-all-cells";

    private TextStyle selectAllCellsStyle() {
        return this.columnRowHeaderStyle(SpreadsheetSelection.ALL_CELLS);
    }

    // renderColumnHeader | renderRowHeader.............................................................................

    /**
     * Creates a TH with the column in UPPER CASE with column width.
     */
    private HTMLTableCellElement renderColumnHeader(final SpreadsheetColumnReference column,
                                                    final AppContext context) {
        final Length<?> width = context.viewportCache()
                .columnWidth(column);

        final THElement th = ElementsFactory.elements.th()
                .id(id(column))
                .style(
                        this.columnHeaderStyle(column)
                                .set(
                                        TextStylePropertyName.MIN_WIDTH,
                                        width
                                ).set(
                                        TextStylePropertyName.WIDTH,
                                        width
                                ).set(
                                        TextStylePropertyName.MIN_HEIGHT,
                                        COLUMN_HEIGHT
                                ).set(
                                        TextStylePropertyName.HEIGHT,
                                        COLUMN_HEIGHT
                                ).css() + "box-sizing: border-box;"
                );

        th.appendChild(
                context.historyToken()
                        .clearAction()
                        .setSelection(
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

    private final static Length<?> COLUMN_HEIGHT = Length.pixel(30.0);

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
        final Length<?> height = context.viewportCache()
                .rowHeight(row);

        final TDElement td = ElementsFactory.elements.td()
                .id(id(row))
                .style(
                        this.rowHeaderStyle(row)
                                .set(
                                        TextStylePropertyName.MIN_WIDTH,
                                        ROW_WIDTH
                                ).set(
                                        TextStylePropertyName.WIDTH,
                                        ROW_WIDTH
                                ).set(
                                        TextStylePropertyName.MIN_HEIGHT,
                                        height
                                ).set(
                                        TextStylePropertyName.HEIGHT,
                                        height
                                ).css() + "box-sizing: border-box;"
                );

        td.appendChild(
                context.historyToken()
                        .clearAction()
                        .setSelection(
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

    private TextStyle columnHeaderStyle(final SpreadsheetColumnReference column) {
        return this.columnRowHeaderStyle(column);
    }

    private TextStyle rowHeaderStyle(final SpreadsheetRowReference row) {
        return this.columnRowHeaderStyle(row);
    }

    private TextStyle columnRowHeaderStyle(final SpreadsheetSelection selection) {
        return this.isSelected(selection) ?
                COLUMN_ROW_HEADER_SELECTED_STYLE :
                COLUMN_ROW_HEADER_UNSELECTED_STYLE;
    }

    private final static TextStyle COLUMN_ROW_HEADER_SELECTED_STYLE;
    private final static TextStyle COLUMN_ROW_HEADER_UNSELECTED_STYLE;

    private final static Color BORDER_COLOR = Color.BLACK;
    private final static BorderStyle BORDER_STYLE = BorderStyle.SOLID;
    private final static Length<?> BORDER_LENGTH = Length.pixel(1.0);

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

    // renderCell.......................................................................................................

    private void refreshCellStyle(final SpreadsheetMetadata metadata) {
        final TextStyle cellStyle = metadata.effectiveStyle();
        this.cellSelectedStyle = cellStyle.merge(CELL_SELECTED_STYLE);
        this.cellUnselectedStyle = cellStyle.merge(CELL_UNSELECTED_STYLE);
    }

    /**
     * Renders the given cell, reading the cell contents using {@link AppContext#viewportCache()}.
     */
    private HTMLTableCellElement renderCell(final SpreadsheetCellReference cellReference,
                                            final AppContext context) {
        final SpreadsheetViewportCache cache = context.viewportCache();
        final Optional<SpreadsheetCell> maybeCell = cache.cell(cellReference);

        TextStyle style = this.cellStyle(cellReference);
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

        // copy width/height to MIN to prevent table squashing cells to fit.
        style = style.setValues(
                Maps.of(
                        TextStylePropertyName.WIDTH,
                        cache.columnWidth(cellReference.column()),
                        TextStylePropertyName.HEIGHT,
                        cache.rowHeight(cellReference.row()),
                        TextStylePropertyName.MIN_WIDTH,
                        style.getOrFail(TextStylePropertyName.WIDTH),
                        TextStylePropertyName.MIN_HEIGHT,
                        style.getOrFail(TextStylePropertyName.HEIGHT)
                )
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

    private TextStyle cellStyle(final SpreadsheetSelection selection) {
        return this.isSelected(selection) ?
                this.cellSelectedStyle :
                this.cellUnselectedStyle;
    }

    private TextStyle cellSelectedStyle;
    private TextStyle cellUnselectedStyle;
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

    // giveViewportSelectionFocus......................................................................................

    private void giveViewportSelectionFocus(final AnchoredSpreadsheetSelection selection,
                                            final AppContext context) {
        final Optional<SpreadsheetSelection> maybeNonLabelSelection = context.viewportCache()
                .nonLabelSelection(
                        selection.selection()
                );
        if (maybeNonLabelSelection.isPresent()) {
            final SpreadsheetSelection nonLabelSelection = maybeNonLabelSelection.get();
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
    }

    // SpreadsheetCellHighlightSaveHistoryToken........................................................................

    private void onHistoryTokenChangeSpreadsheetCellHighlightSaveHistoryToken(final HistoryToken previous,
                                                                              final AppContext context) {
        if (context.historyToken() instanceof SpreadsheetCellHighlightSaveHistoryToken) {
            this.context.debug("SpreadsheetViewportComponent.onHistoryTokenChangeSpreadsheetCellHighlightSaveHistoryToken viewport reload necessary");

            this.reload = true;
            this.loadViewportCells(context);
        }
    }

    // ComponentLifecycle..............................................................................................

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
        context.debug("SpreadsheetViewportComponent.refresh BEGIN window: " + context.viewportCache().windows());

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

        this.refreshCellStyle(metadata);

        if (metadata.shouldViewRefresh(this.metadata)) {
            this.reload = true;
        }

        this.metadata = metadata;

        this.loadViewportCellsIfNecessary(context);

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

    private void setSelected(final Optional<AnchoredSpreadsheetSelection> selected) {
        final AppContext context = this.context;
        context.debug(
                "SpreadsheetViewportComponent.setSelection " + selected.orElse(null)
        );

        Predicate<SpreadsheetSelection> predicate = null;

        if (selected.isPresent()) {
            // special case for label
            final Optional<SpreadsheetSelection> maybeNotLabel = context.viewportCache()
                    .nonLabelSelection(selected.get().selection());
            if (maybeNotLabel.isPresent()) {
                predicate = maybeNotLabel.get();
            }
        }

        this.selected = null != predicate ?
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
        final List<SpreadsheetViewportNavigation> navigations = this.navigations;

        final SpreadsheetMetadata metadata = context.spreadsheetMetadata();

        final SpreadsheetId id = metadata.getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_ID);

        SpreadsheetViewport viewport = metadata.getOrFail(SpreadsheetMetadataPropertyName.VIEWPORT);
        final SpreadsheetViewportRectangle rectangle = viewport.rectangle();
        viewport = viewport.setRectangle(
                rectangle.setWidth(
                        this.viewportTableCellsWidth()
                ).setHeight(
                        this.viewportTableCellsHeight()
                )
        ).setNavigations(navigations);

        context.debug("SpreadsheetViewportComponent.loadViewportCells id: " + id + " viewport: " + viewport);

        context.viewportCache()
                .clear(); // clear all cached data.
        this.reload = false;
        navigations.clear();

        context.addSpreadsheetDeltaWatcherOnce(
                SpreadsheetDeltaFetcherWatchers.pushHistoryTokenResponseSpreadsheetDeltaSelection()
        );
        context.spreadsheetDeltaFetcher()
                .loadCells(
                        id,
                        viewport
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
     * Tests if the given {@link SpreadsheetSelection} typically a cell, column or row is matched by the {@link SpreadsheetMetadataPropertyName#VIEWPORT}.
     * This is used during rendering of the viewport headers or cells if the item should be selected.
     */
    private boolean isSelected(final SpreadsheetSelection selection) {
        return this.selected.test(selection);
    }

    /**
     * A {@link Predicate} that matches selected {@link SpreadsheetSelection} and will be used to highlight cells,
     * columns and rows.
     */
    private Predicate<SpreadsheetSelection> selected = Predicates.never();

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

        if (null != id && id.startsWith(VIEWPORT_ID_PREFIX)) {
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
