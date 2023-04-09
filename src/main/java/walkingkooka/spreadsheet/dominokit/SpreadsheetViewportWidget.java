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

import elemental2.dom.HTMLAnchorElement;
import elemental2.dom.HTMLTableCellElement;
import elemental2.dom.HTMLTableElement;
import elemental2.dom.HTMLTableRowElement;
import elemental2.dom.HTMLTableSectionElement;
import org.gwtproject.safehtml.shared.SafeHtmlUtils;
import org.jboss.elemento.Elements;
import org.jboss.elemento.HtmlContentBuilder;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.Url;
import walkingkooka.net.UrlParameterName;
import walkingkooka.net.UrlQueryString;
import walkingkooka.predicate.Predicates;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetNameHistoryToken;
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
import walkingkooka.text.CaseKind;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextNode;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public final class SpreadsheetViewportWidget implements SpreadsheetDeltaWatcher, SpreadsheetMetadataWatcher {

    static SpreadsheetViewportWidget empty(final AppContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetViewportWidget(context);
    }

    private SpreadsheetViewportWidget(final AppContext context) {
        this.context = context;
        this.tableElement = Elements.table();
        this.initTable();

        context.addSpreadsheetMetadataWatcher(this);
        context.addSpreadsheetDeltaWatcher(this);
    }

    private void initTable() {
        final HtmlContentBuilder<HTMLTableElement> tableElement = this.tableElement;
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
    }

    public void setWidthAndHeight(final int width,
                                  final int height) {
        final boolean reload = width > this.width || height > this.height;

        this.context.debug("SpreadsheetViewport.setWidthAndHeight " + width + "x" + height + " was " + this.width + "x" + this.height + " reload: " + reload);

        this.width = width;
        this.height = height;

        this.tableElement.element()
                .style.set("height", height + "px");

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
    public void onSpreadsheetDelta(final SpreadsheetDelta delta,
                                   final AppContext context) {
        Objects.requireNonNull(delta, "delta");

        this.cache.onSpreadsheetDelta(delta, context);
        this.setViewportSelection(delta.viewportSelection());

        this.updateTable();
    }

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        Objects.requireNonNull(metadata, "metadata");

        if(metadata.shouldViewRefresh(this.metadata)) {
            this.reload = true;
        }

        this.metadata = metadata;
        this.setViewportSelection(
                metadata.get(SpreadsheetMetadataPropertyName.SELECTION)
        );

        this.loadViewportCellsIfNecessary();

        this.cache.onSpreadsheetMetadata(
                metadata,
                context
        );
        this.nameHistoryToken = metadata.id()
                .isPresent() ?
                SpreadsheetHistoryToken.spreadsheetSelect(
                        metadata.getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_ID),
                        metadata.getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_NAME)
                ) :
                null;
        this.updateTable();
    }

    /**
     * Initial metadata is EMPTY or nothing.
     */
    private SpreadsheetMetadata metadata = SpreadsheetMetadata.EMPTY;

    private void setViewportSelection(final Optional<SpreadsheetViewportSelection> selection) {
        this.context.debug(
                "SpreadsheetViewportWidget.setViewportSelection " + selection.orElse(null)
        );

        this.selection = selection.map(v -> (Predicate<SpreadsheetSelection>) v.selection())
                .orElse(Predicates.never());
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
            if(this.metadata.isEmpty()) {
                this.context.debug("SpreadsheetViewportWidget.loadViewportCellsIfNecessary waiting for metadata");
            } else {
                this.loadViewportCells();
            }
        }
    }

    /**
     * Loads all the cells to fill the viewport. Assumes that a metadata with id is present.
     */
    private void loadViewportCells() {
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
                    viewportSelection.get(),
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
     * Renders or updates the TABLE element holding the spreadsheet viewport.
     */
    // {
    //  "viewportSelection": {
    //    "selection": {
    //      "type": "spreadsheet-cell-reference",
    //      "value": "A1"
    //    }
    //  },
    //  "cells": {
    //    "A1": {
    //      "formula": {
    //        "token": {
    //          "type": "spreadsheet-expression-parser-token",
    //          "value": {
    //            "value": [
    //              {
    //                "type": "spreadsheet-equals-symbol-parser-token",
    //                "value": {
    //                  "value": "=",
    //                  "text": "="
    //                }
    //              },
    //              {
    //                "type": "spreadsheet-addition-parser-token",
    //                "value": {
    //                  "value": [
    //                    {
    //                      "type": "spreadsheet-number-parser-token",
    //                      "value": {
    //                        "value": [
    //                          {
    //                            "type": "spreadsheet-digits-parser-token",
    //                            "value": {
    //                              "value": "1",
    //                              "text": "1"
    //                            }
    //                          }
    //                        ],
    //                        "text": "1"
    //                      }
    //                    },
    //                    {
    //                      "type": "spreadsheet-plus-symbol-parser-token",
    //                      "value": {
    //                        "value": "+",
    //                        "text": "+"
    //                      }
    //                    },
    //                    {
    //                      "type": "spreadsheet-number-parser-token",
    //                      "value": {
    //                        "value": [
    //                          {
    //                            "type": "spreadsheet-digits-parser-token",
    //                            "value": {
    //                              "value": "2",
    //                              "text": "2"
    //                            }
    //                          }
    //                        ],
    //                        "text": "2"
    //                      }
    //                    }
    //                  ],
    //                  "text": "1+2"
    //                }
    //              }
    //            ],
    //            "text": "=1+2"
    //          }
    //        },
    //        "expression": {
    //          "type": "add-expression",
    //          "value": [
    //            {
    //              "type": "value-expression",
    //              "value": {
    //                "type": "expression-number",
    //                "value": "1"
    //              }
    //            },
    //            {
    //              "type": "value-expression",
    //              "value": {
    //                "type": "expression-number",
    //                "value": "2"
    //              }
    //            }
    //          ]
    //        },
    //        "value": {
    //          "type": "expression-number",
    //          "value": "3"
    //        }
    //      },
    //      "formatted": {
    //        "type": "text",
    //        "value": "3."
    //      }
    //    }
    //  },
    //  "columnWidths": {
    //    "A": 100
    //  },
    //  "rowHeights": {
    //    "1": 30
    //  },
    //  "window": "A1:B12,WI1:WW12"
    //}
    private void updateTable() {
        final HtmlContentBuilder<HTMLTableElement> tableElement = this.tableElement;
        Elements.removeChildrenFrom(tableElement.element());

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
                columnHeaders(columns)
        );

        // render the rows and cells
        tableElement.add(
                this.rows(
                        rows,
                        columns
                )
        );
    }

    /**
     * Creates a THEAD holding a TR with the SELECT ALL and COLUMN headers.
     */
    private HTMLTableSectionElement columnHeaders(final Collection<SpreadsheetColumnReference> columns) {
        final HtmlContentBuilder<HTMLTableRowElement> tr = Elements.tr()
                .add(
                        this.selectAll()
                );

        for(final SpreadsheetColumnReference column: columns) {
            tr.add(
                    this.columnHeader(column)
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
                        this.context.viewportAll(false)
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
    private HTMLTableCellElement columnHeader(final SpreadsheetColumnReference column) {
        final HtmlContentBuilder<HTMLTableCellElement> td = Elements.th()
                .id(id(column))
                .attr(
                        "tabindex",
                        "0"
                ).style(
                        this.context.viewportColumnHeader(this.isSelected(column))
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

        return td.element();
    }

    private final static Length<?> COLUMN_HEIGHT = Length.pixel(25.0);

    /**
     * Factory that creates a TABLE CELL for the column header, including a link to select that column when clicked.
     */
    private HTMLTableSectionElement rows(final Set<SpreadsheetRowReference> rows,
                                         final Set<SpreadsheetColumnReference> columns) {
        final HtmlContentBuilder<HTMLTableSectionElement> tbody = Elements.tbody();

        for(final SpreadsheetRowReference row: rows) {
            tbody.add(
                    this.row(
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
    private HTMLTableRowElement row(final SpreadsheetRowReference row,
                                        final Collection<SpreadsheetColumnReference> columns) {
        final HtmlContentBuilder<HTMLTableRowElement> tr = Elements.tr()
                .add(
                        this.rowHeader(row)
                );

        for(final SpreadsheetColumnReference column: columns) {
            tr.add(
                    this.cell(
                            column.setRow(row)
                    )
            );
        }

        return tr.element();
    }

    private HTMLTableCellElement rowHeader(final SpreadsheetRowReference row) {
        final HtmlContentBuilder<HTMLTableCellElement> td = Elements.td()
                .id(id(row))
                .attr(
                        "tabindex",
                        "0"
                ).style(
                        this.context.viewportRowHeader(this.isSelected(row))
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

        return td.element();
    }

    private final static Length<?> ROW_WIDTH = Length.pixel(80.0);

    /**
     * If possible creates a link to the cell or row or simply the cow/row reference as text.
     */
    private void addLinkOrText(final SpreadsheetColumnOrRowReference columnOrRow,
                               final HtmlContentBuilder<HTMLTableCellElement> td) {
        if(null == this.nameHistoryToken) {
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
        final SpreadsheetNameHistoryToken token = this.nameHistoryToken.viewportSelectionHistoryToken(
                selection.setDefaultAnchor()
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
    private HTMLTableCellElement cell(final SpreadsheetCellReference cellReference) {
        final AppContext context = this.context;
        final SpreadsheetViewportCache cache = this.cache;
        final Optional<SpreadsheetCell> maybeCell = cache.cell(cellReference);

        TextStyle style = this.metadata.effectiveStyle();
        String innerHtml = "";

        if(maybeCell.isPresent()) {
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
                context.viewportCell(this.isSelected(cellReference))
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

        td.element()
                .addEventListener(
                        "click",
                        (e) -> this.onCellClick(
                                cellReference, context
                        )
                );

        return td.element();
    }

    /**
     * Grab the id and name from {@link SpreadsheetMetadata} and push a new token including the selected cell.
     */
    private void onCellClick(final SpreadsheetCellReference cell,
                             final AppContext context) {
        final SpreadsheetMetadata metadata = this.metadata;
        final Optional<SpreadsheetName> name = metadata.name();
        final Optional<SpreadsheetId> id = metadata.id();

        if (id.isPresent() && name.isPresent()) {
            context.pushHistoryToken(
                    SpreadsheetHistoryToken.cell(
                            id.get(),
                            name.get(),
                            cell.setDefaultAnchor()

                    )
            );
        }
    }

    // viewport-column-A
    private String id(final SpreadsheetSelection selection) {
        return "viewport-" +
                selection.textLabel().toLowerCase() +
                "-" +
                selection.toString().toUpperCase();
    }

    /**
     * This is updated each time a new {@link SpreadsheetMetadata} arrives.
     */
    private SpreadsheetNameHistoryToken nameHistoryToken;

    /**
     * The root table element.
     */
    HTMLTableElement tableElement() {
        return this.tableElement.element();
    }

    private final HtmlContentBuilder<HTMLTableElement> tableElement;

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
}
