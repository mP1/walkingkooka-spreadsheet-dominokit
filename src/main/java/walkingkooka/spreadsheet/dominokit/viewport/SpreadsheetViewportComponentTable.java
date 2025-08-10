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
import walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.dom.Key;
import walkingkooka.spreadsheet.dominokit.dom.TBodyComponent;
import walkingkooka.spreadsheet.dominokit.dom.THeadComponent;
import walkingkooka.spreadsheet.dominokit.dom.TableComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportNavigation;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportWindows;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A TABLE which holds all the ROWs that are displayed. It also contains and caches rows.
 */
final class SpreadsheetViewportComponentTable implements HtmlComponent<HTMLTableElement, SpreadsheetViewportComponentTable> {

    static SpreadsheetViewportComponentTable empty(final SpreadsheetViewportComponentTableContext context) {
        return new SpreadsheetViewportComponentTable(context);
    }

    private SpreadsheetViewportComponentTable(final SpreadsheetViewportComponentTableContext context) {
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
        thead.appendChild(this.columnHeaders);
        table.appendChild(thead);

        final TBodyComponent tbody = HtmlElementComponent.tbody();
        table.appendChild(tbody);

        this.table = table;
        this.tbody = tbody;

        this.id = null;
        this.name = null;

        this.rows = SortedSets.tree();
        this.rowsToTableRowCells = Maps.sorted();
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
    private void onKeyDownEvent(final KeyboardEvent event) {
        event.preventDefault();

        final boolean shifted = event.shiftKey;
        final SpreadsheetViewportComponentTableContext context = this.context;

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
                // TODO table.blur
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
            context.pushNavigation(navigation);
        }
    }

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

        context.debug(this.getClass().getSimpleName() + ".refresh " + (ended - started) + "ms, row rendering timings: " + String.join(", ", timings));
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

    @Override
    public SpreadsheetViewportComponentTable setCssText(final String css) {
        this.table.setCssText(css);
        return this;
    }

    @Override
    public SpreadsheetViewportComponentTable setCssProperty(final String name,
                                                            final String value) {
        this.table.setCssProperty(
            name,
            value
        );
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLTableElement element() {
        return this.table.element();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        this.table.printTree(printer);
    }

    private final TableComponent table;

    private final TBodyComponent tbody;

    private final SpreadsheetViewportComponentTableContext context;
}
