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
import elemental2.dom.EventTarget;
import elemental2.dom.HTMLTableElement;
import elemental2.dom.KeyboardEvent;
import elemental2.dom.MouseEvent;
import jsinterop.base.Js;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.dom.TBodyComponent;
import walkingkooka.spreadsheet.dominokit.dom.THeadComponent;
import walkingkooka.spreadsheet.dominokit.dom.TableComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.key.KeyBinding;
import walkingkooka.spreadsheet.dominokit.key.SpreadsheetKeyBindings;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportNavigation;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportWindows;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A TABLE which holds all the ROWs that are displayed. It also contains and caches rows.
 */
final class SpreadsheetViewportComponentTable implements HtmlComponentDelegator<HTMLTableElement, SpreadsheetViewportComponentTable> {

    static SpreadsheetViewportComponentTable empty(final SpreadsheetKeyBindings keyBindings,
                                                   final SpreadsheetViewportComponentTableContext context) {
        return new SpreadsheetViewportComponentTable(
            keyBindings,
            context
        );
    }

    private SpreadsheetViewportComponentTable(final SpreadsheetKeyBindings keyBindings,
                                              final SpreadsheetViewportComponentTableContext context) {
        this.bindingToKeyboardEventHandler = Maps.sorted();

        this.registerBindings(
            keyBindings.delete(),
            this::onDelete
        );

        this.registerBindings(
            keyBindings.exit(),
            this::onExit
        );

        this.registerBindings(
            keyBindings.select(),
            this::onSelect
        );
        
        this.registerBindings(
            keyBindings.selectionLeft(),
            this::onSelectionLeft
        );

        this.registerBindings(
            keyBindings.selectionRight(),
            this::onSelectionRight
        );

        this.registerBindings(
            keyBindings.selectionUp(),
            this::onSelectionUp
        );

        this.registerBindings(
            keyBindings.selectionDown(),
            this::onSelectionDown
        );

        this.registerBindings(
            keyBindings.extendSelectionLeft(),
            this::onExtendSelectionLeft
        );

        this.registerBindings(
            keyBindings.extendSelectionRight(),
            this::onExtendSelectionRight
        );

        this.registerBindings(
            keyBindings.extendSelectionUp(),
            this::onExtendSelectionUp
        );

        this.registerBindings(
            keyBindings.extendSelectionDown(),
            this::onExtendSelectionDown
        );

        this.registerBindings(
            keyBindings.screenLeft(),
            this::onScreenLeft
        );

        this.registerBindings(
            keyBindings.screenRight(),
            this::onScreenRight
        );

        this.registerBindings(
            keyBindings.screenUp(),
            this::onScreenUp
        );

        this.registerBindings(
            keyBindings.screenDown(),
            this::onScreenDown
        );

        this.registerBindings(
            keyBindings.extendScreenLeft(),
            this::onExtendScreenLeft
        );

        this.registerBindings(
            keyBindings.extendScreenRight(),
            this::onExtendScreenRight
        );

        this.registerBindings(
            keyBindings.extendScreenUp(),
            this::onExtendScreenUp
        );

        this.registerBindings(
            keyBindings.extendScreenDown(),
            this::onExtendScreenDown
        );

        this.context = context;

        final TableComponent table = HtmlElementComponent.table()
            .setId(SpreadsheetViewportComponent.ID)
            .setOverflow("hidden")
            .addClickListener(
                (event) -> this.onClickEvent(
                    Js.cast(event)
                )
            ).addContextMenuListener(this::onContextMenu)
            .addKeyDownListener(
                (event) -> this.onKeyDownEvent(
                    Js.cast(event)
                )
            );
        this.columnHeaders = SpreadsheetViewportComponentTableRowColumnHeaders.empty(context);

        final THeadComponent thead = HtmlElementComponent.thead();
        final TBodyComponent tbody = HtmlElementComponent.tbody();

        thead.appendChild(this.columnHeaders);
        table.appendChild(thead);
        table.appendChild(tbody);

        this.table = table;
        this.thead = thead;
        this.tbody = tbody;

        this.id = null;
        this.name = null;

        this.rows = SortedSets.tree();
        this.rowsToTableRowCells = Maps.sorted();
    }

    private void registerBindings(final Collection<KeyBinding> bindings,
                                  final Consumer<KeyboardEvent> handler) {
        for(KeyBinding binding : bindings) {
            this.bindingToKeyboardEventHandler.put(
                binding,
                handler
            );
        }
    }

    // click ...........................................................................................................

    private void onClickEvent(final MouseEvent event) {
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
        Element walk = element;
        for (; ; ) {
            if (null == walk || walk.tagName.equalsIgnoreCase("TABLE")) {
                break;
            }

            final Optional<SpreadsheetSelection> maybeSelection = SpreadsheetViewportComponent.parseElementId(walk.id);
            if (maybeSelection.isPresent()) {
                final SpreadsheetSelection selection = maybeSelection.get();
                if (selection.isCell()) {
                    final SpreadsheetCellReference cell = selection.toCell();

                    this.context.pushNavigation(
                        shiftKeyDown ?
                            SpreadsheetViewportNavigation.extendCell(
                                cell
                            ) :
                            SpreadsheetViewportNavigation.cell(
                                cell
                            )
                    );
                    break;
                }
            }

            walk = walk.parentElement;
        }
    }

    // context menu.....................................................................................................

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

                final Optional<SpreadsheetSelection> maybeSelection = SpreadsheetViewportComponent.parseElementId(element.id);
                if (maybeSelection.isPresent()) {
                    final SpreadsheetSelection selection = maybeSelection.get();

                    final SpreadsheetViewportComponentTableContext context = this.context;

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

    // key down.........................................................................................................

    /**
     * Generic key event handler that handles any key events for cell/column OR row.
     */
    // @VisibleForTesting
    void onKeyDownEvent(final KeyboardEvent event) {
        final KeyBinding binding = KeyBinding.fromKeyEvent(event);

        if (SPREADSHEET_KEYBOARD_EVENT_LISTENER) {
            this.context.debug(this.getClass().getSimpleName() + " handleKeyEvent " + binding);
        }

        final Consumer<KeyboardEvent> handler = this.bindingToKeyboardEventHandler.get(binding);
        if(null != handler) {
            handler.accept(event);
        }
    }

    private final Map<KeyBinding, Consumer<KeyboardEvent>> bindingToKeyboardEventHandler;

    private void onDelete(final KeyboardEvent event) {
        this.context.pushHistoryToken(
            this.context.historyToken()
                .clearAndFormula()
        );
    }

    private void onExit(final KeyboardEvent event) {
        this.context.pushHistoryToken(
            this.context.historyToken()
                .clearSelection()
        );
    }

    private void onSelect(final KeyboardEvent event) {
        this.context.pushHistoryToken(
            this.context.historyToken()
                .formula()
        );
    }

    private void onSelectionLeft(final KeyboardEvent event) {
        this.context.pushNavigation(
            SpreadsheetViewportNavigation.moveLeft()
        );
    }

    private void onSelectionRight(final KeyboardEvent event) {
        this.context.pushNavigation(
            SpreadsheetViewportNavigation.moveRight()
        );
    }

    private void onSelectionUp(final KeyboardEvent event) {
        this.context.pushNavigation(
            SpreadsheetViewportNavigation.moveUp()
        );
    }

    private void onSelectionDown(final KeyboardEvent event) {
        this.context.pushNavigation(
            SpreadsheetViewportNavigation.moveDown()
        );
    }

    private void onExtendSelectionLeft(final KeyboardEvent event) {
        this.context.pushNavigation(
            SpreadsheetViewportNavigation.extendLeftColumn()
        );
    }

    private void onExtendSelectionRight(final KeyboardEvent event) {
        this.context.pushNavigation(
            SpreadsheetViewportNavigation.extendRightColumn()
        );
    }

    private void onExtendSelectionUp(final KeyboardEvent event) {
        this.context.pushNavigation(
            SpreadsheetViewportNavigation.extendMoveUp()
        );
    }

    private void onExtendSelectionDown(final KeyboardEvent event) {
        this.context.pushNavigation(
            SpreadsheetViewportNavigation.extendDownRow()
        );
    }

    private void onScreenLeft(final KeyboardEvent event) {
        this.onScreenLeftRightUpDown(
            () -> SpreadsheetViewportNavigation.scrollLeft(
                this.context.spreadsheetViewportCache()
                    .lastWindowWidth()
            )
        );
    }

    private void onScreenRight(final KeyboardEvent event) {
        this.onScreenLeftRightUpDown(
            () -> SpreadsheetViewportNavigation.scrollRight(
                this.context.spreadsheetViewportCache()
                    .lastWindowWidth()
            )
        );
    }

    private void onScreenUp(final KeyboardEvent event) {
        this.onScreenLeftRightUpDown(
            () -> SpreadsheetViewportNavigation.scrollUp(
                this.context.spreadsheetViewportCache()
                    .lastWindowHeight()
            )
        );
    }

    private void onScreenDown(final KeyboardEvent event) {
        this.onScreenLeftRightUpDown(
            () -> SpreadsheetViewportNavigation.scrollDown(
                this.context.spreadsheetViewportCache()
                    .lastWindowHeight()
            )
        );
    }

    private void onExtendScreenLeft(final KeyboardEvent event) {
        this.onScreenLeftRightUpDown(
            () -> SpreadsheetViewportNavigation.extendScrollLeft(
                this.context.spreadsheetViewportCache()
                    .lastWindowWidth()
            )
        );
    }

    private void onExtendScreenRight(final KeyboardEvent event) {
        this.onScreenLeftRightUpDown(
            () -> SpreadsheetViewportNavigation.extendScrollRight(
                this.context.spreadsheetViewportCache()
                    .lastWindowWidth()
            )
        );
    }

    private void onExtendScreenUp(final KeyboardEvent event) {
        this.onScreenLeftRightUpDown(
            () -> SpreadsheetViewportNavigation.extendScrollUp(
                this.context.spreadsheetViewportCache()
                    .lastWindowHeight()
            )
        );
    }

    private void onExtendScreenDown(final KeyboardEvent event) {
        this.onScreenLeftRightUpDown(
            () -> SpreadsheetViewportNavigation.extendScrollDown(
                this.context.spreadsheetViewportCache()
                    .lastWindowHeight()
            )
        );
    }

    private void onScreenLeftRightUpDown(final Supplier<SpreadsheetViewportNavigation> navigation) {
        final SpreadsheetViewportComponentTableContext context = this.context;

        final HistoryToken historyToken = context.historyToken();
        final AnchoredSpreadsheetSelection anchoredSelection = historyToken.anchoredSelectionOrEmpty()
            .orElse(null);
        if (null != anchoredSelection) {
            final SpreadsheetSelection selection = anchoredSelection.selection();
            if (selection.isCell()) {
                context.pushNavigation(
                    navigation.get()
                );
            }
        }
    }

    // refresh..........................................................................................................

    void refresh(final SpreadsheetId id,
                 final SpreadsheetName name,
                 final SpreadsheetViewportWindows windows,
                 final Predicate<SpreadsheetSelection> selected) {
        Objects.requireNonNull(windows, "windows");

        final SpreadsheetViewportComponentTableContext context = this.context;

        if (false == id.equals(this.id) || false == name.equals(this.name)) {
            this.id = id;
            this.name = name;

            this.columnHeaders.setIdAndName(
                id,
                name
            );

            this.rowsToTableRowCells.values()
                .forEach(r -> r.setIdAndName(id, name));
        }

        // column headings
        final boolean showHeaders = context.shouldShowHeaders();
        {
            final THeadComponent thead = this.thead;
            final SpreadsheetViewportComponentTableRowColumnHeaders columnHeaders = this.columnHeaders;

            if (showHeaders) {
                if(false == thead.contains(columnHeaders)) {
                    thead.appendChild(columnHeaders);
                }
            } else {
                if(thead.contains(columnHeaders)) {
                    thead.removeChild(columnHeaders);
                }
            }
        }

        // rows
        final Set<SpreadsheetRowReference> rows = windows.rows();
        if (false == this.rows.equals(rows)) {
            this.rows = rows;

            // rows changed, create new rows
            final TBodyComponent tbody = this.tbody;
            tbody.clear();

            this.columnHeaders.refresh(
                windows,
                selected,
                context
            );

            final Map<SpreadsheetRowReference, SpreadsheetViewportComponentTableRowCells> oldRowsToTableRowCells = this.rowsToTableRowCells;
            final Map<SpreadsheetRowReference, SpreadsheetViewportComponentTableRowCells> newRowsToTableRowCells = Maps.sorted();

            double height = context.viewportGridHeight();

            // create new rows as necessary
            for (final SpreadsheetRowReference row : rows) {
                SpreadsheetViewportComponentTableRowCells tableRowCells = oldRowsToTableRowCells.get(row);
                if (null == tableRowCells) {
                    tableRowCells = SpreadsheetViewportComponentTableRowCells.empty(
                        row,
                        context
                    );
                }
                newRowsToTableRowCells.put(
                    row,
                    tableRowCells
                );
                tbody.appendChild(tableRowCells);

                height = height - context.spreadsheetViewportCache()
                    .rowHeight(row)
                    .pixelValue();
                if(height <= 0) {
                   break;
                }
            }

            this.rowsToTableRowCells = newRowsToTableRowCells;
        }

        final long started = System.currentTimeMillis();
        long ended = 0;

        final List<String> timings = Lists.array();
        {
            this.columnHeaders.refresh(
                windows,
                selected,
                context
            );
            timings.add(String.valueOf(System.currentTimeMillis() - started));
        }

        for (final SpreadsheetViewportComponentTableRowCells tableRowCells : this.rowsToTableRowCells.values()) {
            final long tableRowCellsStart = System.currentTimeMillis();

            tableRowCells.refresh(
                windows,
                selected,
                context
            );

            ended = System.currentTimeMillis();
            timings.add(String.valueOf(ended - tableRowCellsStart));
        }

        if(SPREADSHEET_VIEWPORT_COMPONENT_TABLE) {
            context.debug(this.getClass().getSimpleName() + ".refresh " + (ended - started) + "ms, row rendering timings: " + String.join(", ", timings));
        }
    }

    private SpreadsheetId id;

    private SpreadsheetName name;

    private final SpreadsheetViewportComponentTableRowColumnHeaders columnHeaders;

    private Set<SpreadsheetRowReference> rows;

    private Map<SpreadsheetRowReference, SpreadsheetViewportComponentTableRowCells> rowsToTableRowCells;

    /**
     * Tests if any column/cell/row within this table has focus, returning true.
     */
    @Override
    public boolean isEditing() {
        return HtmlComponent.hasFocus(this.element());
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLTableElement, ?> htmlComponent() {
        return this.table;
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        this.table.printTree(printer);
    }

    private final TableComponent table;

    private final THeadComponent thead;

    private final TBodyComponent tbody;

    private final SpreadsheetViewportComponentTableContext context;
}
