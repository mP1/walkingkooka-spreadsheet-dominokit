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
import walkingkooka.collect.iterable.Iterables;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.UrlParameterName;
import walkingkooka.net.UrlPath;
import walkingkooka.net.UrlPathName;
import walkingkooka.net.UrlQueryString;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetViewportRectangle;
import walkingkooka.spreadsheet.SpreadsheetViewportWindows;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.find.SpreadsheetCellFind;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportComponent;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportFormulaComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.engine.SpreadsheetEngineEvaluation;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReferencePath;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportNavigation;
import walkingkooka.spreadsheet.server.delta.SpreadsheetDeltaHateosResourceMappings;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * A Fetcher (yes as in the WindowResizeWatcher's fetch object) that provides numerous CRUD and PATCH operations for a {@link SpreadsheetDelta}.
 * <br>
 * A {@link SpreadsheetDelta} is a payload representing cells belonging to a spreadsheet.
 * Deleting a cell, could trigger re-calculation of other cells that reference directly or indirectly the deleted cell.
 * The response for such an operation will contain the deleted cell in {@link SpreadsheetDelta#deletedCells()}, and the
 * updated cells in {@link SpreadsheetDelta#cells()}.
 * <br>
 * Note a window may be sent in the request which limits the items (cells, columns, rows etc) that appear in the response.
 * This is useful for example if the viewport only shows A1:D4, and A1 is updated, it might not be required to want cells
 * outside this viewport to be returned. They will be re-evaluated but their updated cells wont be returned.
 * Frozen columns and rows updates are implemented by the web-app by sending multiple windows.
 * <br>
 * Several components such as the {@link SpreadsheetViewportFormulaComponent} and
 * {@link SpreadsheetViewportComponent} are all {@link SpreadsheetDeltaFetcherWatcher},
 * so any new responses that concern them will trigger an update.
 * <br>
 * This means that only changes following an operation are returned, and not the entire viewport window or the entire spreadsheet.
 * <br>
 * Since a spreadsheet is mostly about updating cells in some way that means {@link SpreadsheetDelta} is the primary mechanism
 * to send payloads to the server and receiving the updates from that processing.
 */
public final class SpreadsheetDeltaFetcher implements Fetcher {

    static {
        SpreadsheetDelta.EMPTY.toString(); // force json unmarshaller to register
    }

    /**
     * Returns true if the URL is a GET all cells
     */
    public static boolean isGetAllCells(final HttpMethod method,
                                        final AbsoluteOrRelativeUrl url) {
        Objects.requireNonNull(method, "method");
        Objects.requireNonNull(url, "url");

        boolean match = false;

        if (method.isGetOrHead()) {
            final UrlPath path = url.path();

            int i = 0;
            match = true;

            for (final UrlPathName component : Iterables.iterator(path.iterator())) {
                i++;

                // http://server/api/spreadsheet/1/
                switch (i) {
                    case 1: // ROOT
                        match = true;
                        break;
                    case 2: // /api
                        match = "api".equals(component.value());
                        break;
                    case 3: // /spreadsheet
                        match = "spreadsheet".equals(component.value());
                        break;
                    case 4: // SPREADSHEETID
                        try {
                            SpreadsheetId.parse(component.value());
                        } catch (final RuntimeException ignore) {
                            match = false;
                        }
                        break;
                    case 5: // cells
                        match = "cells".equals(component.value());
                        break;
                    case 6: // *
                        match = "*".equals(component.value());
                        break;
                    default:
                        match = false; // too many components so NO
                        break;
                }

                if (false == match) {
                    break;
                }
            }

            match = 6 == i;
        }

        return match;
    }

    /**
     * Appends any {@link SpreadsheetCellFind} values to the given {@link UrlQueryString}.
     */
    public static UrlQueryString cellFindQueryString(final SpreadsheetCellFind find) {
        Objects.requireNonNull(find, "find");

        UrlQueryString result = UrlQueryString.EMPTY;

        final Optional<SpreadsheetCellRangeReferencePath> path = find.path();
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

        final Optional<AnchoredSpreadsheetSelection> maybeAnchored = viewport.anchoredSelection();
        if (maybeAnchored.isPresent()) {
            final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = maybeAnchored.get();
            final SpreadsheetSelection selection = anchoredSpreadsheetSelection.selection();

            result = result.addParameter(
                    SELECTION,
                    selection.toStringMaybeStar()
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

    public void clear(final SpreadsheetId id,
                      final SpreadsheetSelection selection) {
        final AppContext context = this.context;

        this.postDelta(
                url(
                        id,
                        selection
                ).appendPathName(
                        SpreadsheetDeltaHateosResourceMappings.CLEAR.toUrlPathName()
                ).setQuery(
                        context.lastCellFindAndViewportAndWindowQueryString()
                ),
                SpreadsheetDelta.EMPTY
        );
    }

    /**
     * DELETEs the given {@link SpreadsheetViewport} such as a cell/column/row.
     */
    public void deleteDelta(final SpreadsheetId id,
                            final SpreadsheetSelection selection) {
        final AppContext context = this.context;
        this.delete(
                url(
                        id,
                        selection
                ).setQuery(
                        context.lastCellFindAndViewportAndWindowQueryString()
                )
        );
    }

    public void findCells(final SpreadsheetId id,
                          final SpreadsheetCellRangeReference cells,
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
                                    final SpreadsheetCellRangeReference cells,
                                    final SpreadsheetCellFind find) {
        return SpreadsheetMetadataFetcher.url(id)
                .appendPathName(SpreadsheetDeltaHateosResourceMappings.CELL.toUrlPathName())
                .appendPathName(
                        UrlPathName.with(
                                Objects.requireNonNull(cells, "cells")
                                        .toStringMaybeStar()
                        )
                ).appendPathName(
                        SpreadsheetDeltaHateosResourceMappings.FIND.toUrlPathName()
                ).setQuery(
                        cellFindQueryString(
                                find
                        )
                );
    }

    public void insertAfterColumn(final SpreadsheetId id,
                                  final SpreadsheetSelection selection,
                                  final int count) {
        // http://localhost:3000/api/spreadsheet/1/column/ABC/after?count=2&home=A1&width=1712&height=765&includeFrozenColumnsRows=true
        this.insertColumnOrRow(
                id,
                selection,
                SpreadsheetDeltaHateosResourceMappings.AFTER,
                count
        );
    }

    public void insertBeforeColumn(final SpreadsheetId id,
                                   final SpreadsheetSelection selection,
                                   final int count) {
        // http://localhost:3000/api/spreadsheet/1/column/ABC/before?count=2&home=A1&width=1712&height=765&includeFrozenColumnsRows=true
        this.insertColumnOrRow(
                id,
                selection,
                SpreadsheetDeltaHateosResourceMappings.BEFORE,
                count
        );
    }

    public void insertAfterRow(final SpreadsheetId id,
                               final SpreadsheetSelection selection,
                               final int count) {
        // http://localhost:3000/api/spreadsheet/1/row/ABC/after?count=2&home=A1&width=1712&height=765&includeFrozenRowsRows=true
        this.insertColumnOrRow(
                id,
                selection,
                SpreadsheetDeltaHateosResourceMappings.AFTER,
                count
        );
    }

    public void insertBeforeRow(final SpreadsheetId id,
                                final SpreadsheetSelection selection,
                                final int count) {
        // http://localhost:3000/api/spreadsheet/1/row/ABC/before?count=2&home=A1&width=1712&height=765&includeFrozenRowsRows=true
        this.insertColumnOrRow(
                id,
                selection,
                SpreadsheetDeltaHateosResourceMappings.BEFORE,
                count
        );
    }

    private void insertColumnOrRow(final SpreadsheetId id,
                                   final SpreadsheetSelection selection,
                                   final LinkRelation<?> afterOrBefore,
                                   final int count) {
        final AppContext context = this.context;

        final UrlQueryString queryString = UrlQueryString.parse("count=" + count)
                .addParameters(
                        context.lastCellFindAndViewportAndWindowQueryString()
                );

        // /api/spreadsheet/SpreadsheetId/column/A:B/insertBefore?count=1
        this.post(
                SpreadsheetMetadataFetcher.url(
                        id
                ).appendPath(
                        UrlPath.parse(
                                selection.cellColumnOrRowText() +
                                        UrlPath.SEPARATOR +
                                        selection.toStringMaybeStar()
                        )
                        ).appendPathName(afterOrBefore.toUrlPathName())
                        .setQuery(queryString),
                ""
        );
    }

    /**
     * Deletes the cells also passing the viewport.
     */
    public void deleteCells(final SpreadsheetId id,
                            final SpreadsheetViewport viewport) {
        // DELETE http://localhost:3000/api/spreadsheet/1f/cell/$cells
        this.delete(
                url(
                        id,
                        checkViewport(viewport)
                                .anchoredSelection()
                                .get()
                                .selection()
                ).setQuery(
                        viewportQueryString(
                                viewport
                        ).addParameters(
                                this.context.lastCellFindQueryString()
                        )
                )
        );
    }

    /**
     * Loads the cells to fill the given rectangular area typically a viewport.
     */
    public void loadCells(final SpreadsheetId id,
                          final SpreadsheetViewport viewport) {
        // load cells for the new window...
        // http://localhost:3000/api/spreadsheet/1f/cell/*/force-recompute?home=A1&width=1712&height=765&includeFrozenColumnsRows=true
        this.get(
                url(
                        id,
                        SpreadsheetSelection.ALL_CELLS,
                        FORCE_RECOMPUTE // path
                ).setQuery(
                        viewportQueryString(
                                viewport
                        ).addParameters(
                                this.context.lastCellFindQueryString()
                        )
                )
        );
    }

    private final static UrlPath FORCE_RECOMPUTE = UrlPath.parse(
                    CaseKind.kebabEnumName(SpreadsheetEngineEvaluation.FORCE_RECOMPUTE)
    );

    public void patchCellsFormula(final SpreadsheetId id,
                                  final SpreadsheetSelection selection,
                                  final Map<SpreadsheetCellReference, SpreadsheetFormula> cellToFormulas) {
        this.patchCellsWithMap(
                id,
                selection,
                cellToFormulas,
                SpreadsheetDelta::cellsFormulaPatch
        );
    }

    public void patchCellsFormatter(final SpreadsheetId id,
                                    final SpreadsheetSelection selection,
                                    final Map<SpreadsheetCellReference, Optional<SpreadsheetFormatterSelector>> cellToFormatters) {
        this.patchCellsWithMap(
                id,
                selection,
                cellToFormatters,
                SpreadsheetDelta::cellsFormatterPatch
        );
    }

    public void patchCellsParser(final SpreadsheetId id,
                                 final SpreadsheetSelection selection,
                                 final Map<SpreadsheetCellReference, Optional<SpreadsheetParserSelector>> cellToParsers) {
        this.patchCellsWithMap(
                id,
                selection,
                cellToParsers,
                SpreadsheetDelta::cellsParserPatch
        );
    }

    public void patchCellsStyle(final SpreadsheetId id,
                                final SpreadsheetSelection selection,
                                final Map<SpreadsheetCellReference, TextStyle> cellToStyles) {
        this.patchCellsWithMap(
                id,
                selection,
                cellToStyles,
                SpreadsheetDelta::cellsStylePatch
        );
    }

    private <T> void patchCellsWithMap(final SpreadsheetId id,
                                       final SpreadsheetSelection selection,
                                       final Map<SpreadsheetCellReference, T> cellToStyles,
                                       final BiFunction<Map<SpreadsheetCellReference, T>, JsonNodeMarshallContext, JsonNode> patcher) {
        final AppContext context = this.context();

        this.post(
                url(
                        id,
                        selection
                ).setQuery(
                        context.lastCellFindAndViewportAndWindowQueryString()
                ),
                patcher.apply(
                        cellToStyles,
                        this.context
                ).toString()
        );
    }

    public void patchFormula(final SpreadsheetId id,
                             final SpreadsheetSelection selection,
                             final SpreadsheetFormula formula) {
        this.patchDelta(
                url(
                        id,
                        selection
                ),
                SpreadsheetDelta.formulaPatch(
                        formula,
                        this.context
                )
        );
    }

    public void patchFormatter(final SpreadsheetId id,
                               final SpreadsheetSelection selection,
                               final Optional<SpreadsheetFormatterSelector> formatter) {
        this.patchDelta(
                url(
                        id,
                        selection
                ),
                SpreadsheetDelta.formatterPatch(
                        formatter,
                        this.context
                )
        );
    }

    public void patchParser(final SpreadsheetId id,
                            final SpreadsheetSelection selection,
                            final Optional<SpreadsheetParserSelector> parser) {
        this.patchDelta(
                url(
                        id,
                        selection
                ),
                SpreadsheetDelta.parserPatch(
                        parser,
                        this.context
                )
        );
    }

    public void patchStyle(final SpreadsheetId id,
                           final SpreadsheetSelection selection,
                           final JsonNode style) {
        this.patchDelta(
                url(
                        id,
                        selection
                ),
                SpreadsheetDelta.stylePatch(
                        style
                )
        );
    }

    public void saveCells(final SpreadsheetId id,
                          final SpreadsheetSelection selection,
                          final Set<SpreadsheetCell> cells) {
        final AppContext context = this.context();

        this.postDelta(
                url(
                        id,
                        selection
                ).setQuery(
                        context.lastCellFindAndViewportAndWindowQueryString()
                ),
                SpreadsheetDelta.EMPTY.setCells(cells)
        );
    }

    public void saveFormulaText(final SpreadsheetId id,
                                final SpreadsheetSelection selection,
                                final String formulaText) {
        final AppContext context = this.context();

        // PATCH cell with new formula
        this.patchDelta(
                url(
                        id,
                        selection
                ).setQuery(
                        context.lastCellFindAndViewportAndWindowQueryString()
                ),
                SpreadsheetDelta.EMPTY.setCells(
                        Sets.of(
                                selection.toCell()
                                        .setFormula(
                                                SpreadsheetFormula.EMPTY.setText(
                                                        formulaText
                                                )
                                        )
                        )
                )
        );
    }

    public void saveFormatter(final SpreadsheetId id,
                              final SpreadsheetSelection selection,
                              final Optional<SpreadsheetFormatterSelector> formatter) {
        final AppContext context = this.context;

        this.patchDelta(
                url(
                        id,
                        selection
                ).setQuery(
                        context.lastCellFindAndViewportAndWindowQueryString()
                ),
                SpreadsheetDelta.formatterPatch(
                        formatter,
                        context
                )
        );
    }

    public void saveParser(final SpreadsheetId id,
                           final SpreadsheetSelection selection,
                           final Optional<SpreadsheetParserSelector> parser) {
        final AppContext context = this.context;

        this.patchDelta(
                url(
                        id,
                        selection
                ).setQuery(
                        context.lastCellFindAndViewportAndWindowQueryString()
                ),
                SpreadsheetDelta.parserPatch(
                        parser,
                        context
                )
        );
    }

    public <T> void saveStyleProperty(final SpreadsheetId id,
                                      final SpreadsheetSelection selection,
                                      final TextStylePropertyName<T> name,
                                      final Optional<T> value) {
        final AppContext context = this.context;

        this.patchDelta(
                url(
                        id,
                        selection
                ).setQuery(
                        context.lastCellFindAndViewportAndWindowQueryString()
                ),
                SpreadsheetDelta.stylePatch(
                        name.stylePatch(
                                value.orElse(null)
                        )
                )
        );
    }

    // GET http://localhost:12345/api/spreadsheet/1/cell/A1%3AB3/sort?comparators=A%3Dtext
    public void sortCells(final SpreadsheetId id,
                          final SpreadsheetExpressionReference selection,
                          final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparators) {
        this.get(
                url(
                        id,
                        selection
                ).appendPathName(
                        SpreadsheetDeltaHateosResourceMappings.SORT.toUrlPathName()
                ).setQuery(
                        this.context.lastCellFindAndViewportAndWindowQueryString()
                                .addParameter(
                                        COMPARATORS,
                                        comparators.text()
                                )
                )
        );
    }

    private final static UrlParameterName COMPARATORS = UrlParameterName.with("comparators");

    private void patchDelta(final AbsoluteOrRelativeUrl url,
                            final JsonNode delta) {
        this.patch(
                url,
                this.toJson(delta)
        );
    }

    private void patchDelta(final AbsoluteOrRelativeUrl url,
                            final SpreadsheetDelta delta) {
        this.patch(
                url,
                this.toJson(delta)
        );
    }

    private void postDelta(final AbsoluteOrRelativeUrl url,
                           final SpreadsheetDelta delta) {
        this.post(
                url,
                this.toJson(delta)
        );
    }

    // @VisibleForTesting
    static RelativeUrl url(final SpreadsheetId id,
                           final SpreadsheetSelection selection) {
        return url(
                id,
                selection,
                UrlPath.EMPTY // no patch
        );
    }

    // @VisibleForTesting
    static RelativeUrl url(final SpreadsheetId id,
                           final SpreadsheetSelection selection,
                           final UrlPath path) {
        return SpreadsheetMetadataFetcher.url(id)
                .appendPath(
                        UrlPath.parse(
                                checkSelection(selection)
                                        .cellColumnOrRowText()
                        )
                ).appendPath(
                        UrlPath.parse(
                                selection.toStringMaybeStar()
                        )
                ).appendPath(path);
    }

    // checkXXX.........................................................................................................

    private static SpreadsheetSelection checkSelection(final SpreadsheetSelection selection) {
        return Objects.requireNonNull(selection, "selection");
    }

    private static SpreadsheetViewport checkViewport(final SpreadsheetViewport viewport) {
        return Objects.requireNonNull(viewport, "viewport");
    }

    // Fetcher.........................................................................................................

    @Override
    public void onBegin(final HttpMethod method,
                        final AbsoluteOrRelativeUrl url,
                        final Optional<String> body) {
        this.watcher.onBegin(
                method,
                url,
                body,
                this.context
        );
    }

    @Override
    public void onSuccess(final HttpMethod method,
                          final AbsoluteOrRelativeUrl url,
                          final String contentTypeName,
                          final String body) {
        final AppContext context = this.context;

        this.logSuccess(
                method,
                url,
                contentTypeName,
                body,
                context
        );

        switch (CharSequences.nullToEmpty(contentTypeName).toString()) {
            case "":
                this.watcher.onEmptyResponse(context);
            case "SpreadsheetDelta":
                // http://server/api/spreadsheet/1/cell
                this.watcher.onSpreadsheetDelta(
                        method, // method
                        url, // the request url
                        this.parse(
                                body,
                                SpreadsheetDelta.class
                        ), // delta
                        context
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quote(contentTypeName));
        }
    }

    @Override
    public void onFailure(final HttpMethod method,
                          final AbsoluteOrRelativeUrl url,
                          final HttpStatus status,
                          final Headers headers,
                          final String body) {
        this.watcher.onFailure(
                method,
                url,
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
    public int waitingRequestCount() {
        return this.waitingRequestCount;
    }

    @Override
    public void setWaitingRequestCount(final int waitingRequestCount) {
        this.waitingRequestCount = waitingRequestCount;
    }

    private int waitingRequestCount;

    @Override
    public AppContext context() {
        return this.context;
    }

    private final AppContext context;

    // Object..........................................................................................................

    @Override
    public String toString() {
        return this.watcher.toString();
    }
}
