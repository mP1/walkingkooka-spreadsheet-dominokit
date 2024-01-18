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

package walkingkooka.spreadsheet.dominokit.net;

import elemental2.dom.Headers;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlParameterName;
import walkingkooka.net.UrlPath;
import walkingkooka.net.UrlQueryString;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetViewportRectangle;
import walkingkooka.spreadsheet.SpreadsheetViewportWindows;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetCellFind;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.engine.SpreadsheetEngineEvaluation;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangePath;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportNavigation;
import walkingkooka.text.CaseKind;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

public final class SpreadsheetDeltaFetcher implements Fetcher {

    /**
     * Appends any {@link SpreadsheetCellFind} values to the given {@link UrlQueryString}.
     */
    public static UrlQueryString cellFindQueryString(final SpreadsheetCellFind find) {
        Objects.requireNonNull(find, "find");

        UrlQueryString result = UrlQueryString.EMPTY;

        final Optional<SpreadsheetCellRangePath> path = find.path();
        final OptionalInt offset = find.offset();
        final OptionalInt max = find.max();
        final Optional<String> valueType = find.valueType();
        final Optional<String> query = find.query();

        if (path.isPresent()) {
            result = result.addParameter(
                    CELL_RANGE_PATH,
                    CaseKind.kebabEnumName(
                            path.get()
                    )
            );
        }
        if (max.isPresent()) {
            result = result.addParameter(
                    MAX,
                    String.valueOf(
                            max.getAsInt()
                    )
            );
        }
        if (offset.isPresent()) {
            result = result.addParameter(
                    OFFSET,
                    String.valueOf(
                            offset.getAsInt()
                    )
            );
        }
        if (query.isPresent()) {
            result = result.addParameter(
                    QUERY,
                    query.get()
            );
        }
        if (valueType.isPresent()) {
            result = result.addParameter(
                    VALUE_TYPE,
                    valueType.get()
            );
        }

        return result;
    }

    final static UrlParameterName CELL_RANGE_PATH = UrlParameterName.with("cell-range-path");
    final static UrlParameterName MAX = UrlParameterName.with("max");
    final static UrlParameterName OFFSET = UrlParameterName.with("offset");
    final static UrlParameterName QUERY = UrlParameterName.with("query");

    final static UrlParameterName VALUE_TYPE = UrlParameterName.with("value-type");

    public static UrlQueryString viewportAndWindowQueryString(final SpreadsheetViewport viewport,
                                                              final SpreadsheetViewportWindows window) {
        return viewportQueryString(viewport)
                .addParameters(
                        windowQueryString(window)
                );
    }

    /**
     * Returns a {@link UrlQueryString} with the {@link SpreadsheetSelection} as query parameters
     * <pre>
     * window=A1%3AP9&
     * selection=B1&
     * selectionType=cell
     * </pre>
     */
    public static UrlQueryString viewportQueryString(final SpreadsheetViewport viewport) {
        Objects.requireNonNull(viewport, "viewport");

        final SpreadsheetViewportRectangle rectangle = viewport.rectangle();

        UrlQueryString result = UrlQueryString.EMPTY
                .addParameter(HOME, rectangle.home().toString())
                .addParameter(WIDTH, String.valueOf(rectangle.width()))
                .addParameter(HEIGHT, String.valueOf(rectangle.height()))
                .addParameter(INCLUDE_FROZEN_COLUMNS_ROWS, Boolean.TRUE.toString());

        final Optional<AnchoredSpreadsheetSelection> maybeAnchored = viewport.selection();
        if (maybeAnchored.isPresent()) {
            final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = maybeAnchored.get();
            final SpreadsheetSelection selection = anchoredSpreadsheetSelection.selection();

            result = result.addParameter(
                    SELECTION,
                    selection.toString()
            ).addParameter(
                    SELECTION_TYPE,
                    selection.selectionTypeName()
            );

            final SpreadsheetViewportAnchor anchor = anchoredSpreadsheetSelection.anchor();
            if (SpreadsheetViewportAnchor.NONE != anchor) {
                result = result.addParameter(
                        SELECTION_ANCHOR,
                        anchor.kebabText()
                );
            }
        }

        final List<SpreadsheetViewportNavigation> navigations = viewport.navigations();
        if (false == navigations.isEmpty()) {
            result = result.addParameter(
                    NAVIGATION,
                    SpreadsheetViewport.SEPARATOR.toSeparatedString(
                            navigations,
                            SpreadsheetViewportNavigation::text
                    )
            );
        }

        return result;
    }

    private final static UrlParameterName HOME = UrlParameterName.with("home");

    private final static UrlParameterName WIDTH = UrlParameterName.with("width");

    private final static UrlParameterName HEIGHT = UrlParameterName.with("height");

    private final static UrlParameterName INCLUDE_FROZEN_COLUMNS_ROWS = UrlParameterName.with("includeFrozenColumnsRows");

    private final static UrlParameterName SELECTION = UrlParameterName.with("selection");

    private final static UrlParameterName SELECTION_TYPE = UrlParameterName.with("selectionType");

    private final static UrlParameterName SELECTION_ANCHOR = UrlParameterName.with("selectionAnchor");

    /**
     * Holds a direction resulting from entering an arrow key etc.
     */
    private final static UrlParameterName NAVIGATION = UrlParameterName.with("navigation");

    /**
     * Appends the given window to the given {@link UrlQueryString}
     */
    public static UrlQueryString windowQueryString(final SpreadsheetViewportWindows window) {
        Objects.requireNonNull(window, "window");

        return window.isEmpty() ?
                UrlQueryString.EMPTY :
                UrlQueryString.EMPTY.addParameter(
                        WINDOW,
                        window.toString()
                );
    }

    private final static UrlParameterName WINDOW = UrlParameterName.with("window");

    public static SpreadsheetDeltaFetcher with(final SpreadsheetDeltaFetcherWatcher watcher,
                                               final AppContext context) {
        Objects.requireNonNull(watcher, "watcher");
        Objects.requireNonNull(context, "context");

        return new SpreadsheetDeltaFetcher(
                watcher,
                context
        );
    }

    private SpreadsheetDeltaFetcher(final SpreadsheetDeltaFetcherWatcher watcher,
                                    final AppContext context) {
        this.watcher = watcher;
        this.context = context;
    }

    /**
     * DELETEs the given {@link SpreadsheetViewport} such as a cell/column/row.
     */
    public void deleteDelta(final SpreadsheetId id,
                            final SpreadsheetSelection selection) {
        this.delete(
                this.url(
                        id,
                        selection,
                        Optional.empty()
                ).setQuery(
                        SpreadsheetDeltaFetcher.windowQueryString(
                                this.context.viewportCache()
                                        .windows()
                        )
                )
        );
    }

    public void findCells(final SpreadsheetId id,
                          final SpreadsheetCellRange cells,
                          final SpreadsheetCellFind find) {
        this.get(
                findCellsUrl(
                        id,
                        cells,
                        find
                )
        );
    }

    // @VisibleForTesting
    static RelativeUrl findCellsUrl(final SpreadsheetId id,
                                    final SpreadsheetCellRange cells,
                                    final SpreadsheetCellFind find) {
        checkId(id);
        Objects.requireNonNull(cells, "cells");
        Objects.requireNonNull(find, "find");

        return Url.parseRelative(
                "/api/spreadsheet/" +
                        id +
                        "/cell/" +
                        cells.toStringMaybeStar() +
                        "/find"
        ).setQuery(
                cellFindQueryString(
                        find
                )
        );
    }

    public void insertAfterColumn(final SpreadsheetId id,
                                  final SpreadsheetSelection selection,
                                  final int count,
                                  final Optional<SpreadsheetViewport> viewport) {
        checkId(id);
        checkColumnOrColumnRange(selection);
        checkViewport(viewport);

        this.context.debug("SpreadsheetDeltaFetcher.insertAfterColumn " + id + ", " + selection + ", " + count);

        // http://localhost:3000/api/spreadsheet/1/column/ABC/after?count=2&home=A1&width=1712&height=765&includeFrozenColumnsRows=true
        this.insertColumnOrRow(
                id,
                selection,
                "after",
                count,
                viewport
        );
    }

    public void insertBeforeColumn(final SpreadsheetId id,
                                   final SpreadsheetSelection selection,
                                   final int count,
                                   final Optional<SpreadsheetViewport> viewport) {
        checkId(id);
        checkColumnOrColumnRange(selection);
        checkViewport(viewport);

        this.context.debug("SpreadsheetDeltaFetcher.insertBeforeColumn " + id + ", " + selection + ", " + count);

        // http://localhost:3000/api/spreadsheet/1/column/ABC/before?count=2&home=A1&width=1712&height=765&includeFrozenColumnsRows=true
        this.insertColumnOrRow(
                id,
                selection,
                "before",
                count,
                viewport
        );
    }

    public void insertAfterRow(final SpreadsheetId id,
                               final SpreadsheetSelection selection,
                               final int count,
                               final Optional<SpreadsheetViewport> viewport) {
        checkId(id);
        checkRowOrRowRange(selection);
        checkViewport(viewport);

        this.context.debug("SpreadsheetDeltaFetcher.insertAfterRow " + id + ", " + selection + ", " + count);

        // http://localhost:3000/api/spreadsheet/1/row/ABC/after?count=2&home=A1&width=1712&height=765&includeFrozenRowsRows=true
        this.insertColumnOrRow(
                id,
                selection,
                "after",
                count,
                viewport
        );
    }

    public void insertBeforeRow(final SpreadsheetId id,
                                final SpreadsheetSelection selection,
                                final int count,
                                final Optional<SpreadsheetViewport> viewport) {
        checkId(id);
        checkRowOrRowRange(selection);
        checkViewport(viewport);

        this.context.debug("SpreadsheetDeltaFetcher.insertBeforeRow " + id + ", " + selection + ", " + count);

        // http://localhost:3000/api/spreadsheet/1/row/ABC/before?count=2&home=A1&width=1712&height=765&includeFrozenRowsRows=true
        this.insertColumnOrRow(
                id,
                selection,
                "before",
                count,
                viewport
        );
    }

    private void insertColumnOrRow(final SpreadsheetId id,
                                   final SpreadsheetSelection selection,
                                   final String afterOrBefore,
                                   final int count,
                                   final Optional<SpreadsheetViewport> viewport) {
        UrlQueryString queryString = UrlQueryString.parse("count=" + count);
        if (viewport.isPresent()) {
            queryString.addParameters(
                    viewportQueryString(
                            viewport.get()
                    )
            );
        }

        this.post(
                Url.parseRelative(
                        "/api/spreadsheet/" +
                                id +
                                "/" +
                                selection.cellColumnOrRowText() +
                                "/" +
                                selection.toStringMaybeStar() +
                                "/" +
                                afterOrBefore
                ).setQuery(queryString),
                ""
        );
    }

    /**
     * Loads the cells to fill the given rectangular area typically a viewport.
     */
    public void loadCells(final SpreadsheetId id,
                          final SpreadsheetViewport viewport) {
        checkId(id);
        checkViewport(viewport);

        this.context.debug("SpreadsheetDeltaFetcher.loadCells " + id + " " + viewport);

        // load cells for the new window...
        // http://localhost:3000/api/spreadsheet/1f/cell/*/force-recompute?home=A1&width=1712&height=765&includeFrozenColumnsRows=true
        final UrlQueryString queryString = viewportQueryString(
                viewport
        );

        this.get(
                Url.parseRelative(
                        "/api/spreadsheet/" +
                                id +
                                "/cell/*/" +
                                CaseKind.kebabEnumName(SpreadsheetEngineEvaluation.FORCE_RECOMPUTE)
                ).setQuery(queryString)
        );
    }

    public void patchDelta(final Url url,
                           final SpreadsheetDelta delta) {
        this.patch(
                url,
                this.toJson(delta)
        );
    }

    public void postDelta(final Url url,
                          final SpreadsheetDelta delta) {
        this.post(
                url,
                this.toJson(delta)
        );
    }

    public RelativeUrl url(final SpreadsheetId id,
                           final SpreadsheetSelection selection,
                           final Optional<UrlPath> path) {
        UrlPath urlPath = this.context.spreadsheetMetadataFetcher()
                .url(id)
                .path();

        checkSelection(selection);

        urlPath = urlPath.append(
                UrlPath.parse(
                        selection.cellColumnOrRowText()
                )
        ).append(
                UrlPath.parse(
                        selection.toStringMaybeStar()
                )
        );

        Objects.requireNonNull(path, "path");

        if (path.isPresent()) {
            urlPath = urlPath.append(path.get());
        }

        return urlPath.addQueryString(
                UrlQueryString.EMPTY
        );
    }

    private static SpreadsheetId checkId(final SpreadsheetId id) {
        return Objects.requireNonNull(id, "id");
    }

    private static SpreadsheetSelection checkSelection(final SpreadsheetSelection selection) {
        return Objects.requireNonNull(selection, "selection");
    }

    private static SpreadsheetSelection checkColumnOrColumnRange(final SpreadsheetSelection selection) {
        checkSelection(selection);

        if (false == selection.isColumnReference() && false == selection.isColumnReferenceRange()) {
            throw new IllegalArgumentException("Expected column or column range but got " + selection);
        }

        return selection;
    }

    private static SpreadsheetSelection checkRowOrRowRange(final SpreadsheetSelection selection) {
        checkSelection(selection);

        if (false == selection.isRowReference() && false == selection.isRowReferenceRange()) {
            throw new IllegalArgumentException("Expected row or row range but got " + selection);
        }

        return selection;
    }

    private Optional<SpreadsheetViewport> checkViewport(final Optional<SpreadsheetViewport> viewport) {
        return Objects.requireNonNull(viewport, "viewport");
    }

    private static SpreadsheetViewport checkViewport(final SpreadsheetViewport viewport) {
        return Objects.requireNonNull(viewport, "viewport");
    }

    @Override
    public void onBegin(final HttpMethod method,
                        final Url url,
                        final Optional<String> body) {
        this.watcher.onBegin(
                method,
                url,
                body,
                this.context
        );
    }

    @Override
    public void fetchLog(final Object... values) {
        this.context.debug(values);
    }

    @Override
    public void onSuccess(final String body) {
        this.watcher.onSpreadsheetDelta(
                this.parse(
                        body,
                        SpreadsheetDelta.class
                ),
                this.context
        );
    }

    @Override
    public void onFailure(final HttpStatus status,
                          final Headers headers,
                          final String body) {
        this.watcher.onFailure(
                status,
                headers,
                body,
                this.context
        );
    }

    @Override
    public void onError(final Object cause) {
        this.watcher.onError(
                cause,
                this.context
        );
    }

    private final SpreadsheetDeltaFetcherWatcher watcher;

    @Override
    public AppContext context() {
        return this.context;
    }

    private final AppContext context;

    @Override
    public String toString() {
        return this.watcher.toString();
    }
}
