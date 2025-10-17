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

package walkingkooka.spreadsheet.dominokit.fetcher;

import walkingkooka.collect.iterable.Iterables;
import walkingkooka.collect.set.Sets;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.UrlPath;
import walkingkooka.net.UrlPathName;
import walkingkooka.net.UrlQueryString;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetHateosResourceNames;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetValueType;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.cell.SpreadsheetCellValueDialogComponent;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportComponent;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportFormulaComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetCellFindQuery;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.engine.SpreadsheetEngineEvaluation;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.server.SpreadsheetServerLinkRelations;
import walkingkooka.spreadsheet.server.delta.SpreadsheetDeltaHttpMappings;
import walkingkooka.spreadsheet.server.delta.SpreadsheetDeltaUrlQueryParameters;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewport;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportAnchor;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportNavigation;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportRectangle;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportWindows;
import walkingkooka.text.CaseKind;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.validation.ValueTypeName;
import walkingkooka.validation.form.Form;
import walkingkooka.validation.form.FormName;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

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
public final class SpreadsheetDeltaFetcher extends Fetcher<SpreadsheetDeltaFetcherWatcher> {

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
                        match = UrlPathName.WILDCARD.equals(component);
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
     * Returns true if the URL is a load/save/delete label
     */
    public static boolean isLabel(final HttpMethod method,
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

                // http://server/api/spreadsheet/1/label
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
                    case 5: // label
                        match = "label".equals(component.value());
                        break;
                    case 6: // label-name
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
            .addParameter(SpreadsheetDeltaUrlQueryParameters.HOME, rectangle.home().toString())
            .addParameter(SpreadsheetDeltaUrlQueryParameters.WIDTH, String.valueOf(rectangle.width()))
            .addParameter(SpreadsheetDeltaUrlQueryParameters.HEIGHT, String.valueOf(rectangle.height()))
            .addParameter(SpreadsheetDeltaUrlQueryParameters.INCLUDE_FROZEN_COLUMNS_ROWS, Boolean.TRUE.toString());

        final Optional<AnchoredSpreadsheetSelection> maybeAnchored = viewport.anchoredSelection();
        if (maybeAnchored.isPresent()) {
            final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = maybeAnchored.get();
            final SpreadsheetSelection selection = anchoredSpreadsheetSelection.selection();

            result = result.addParameter(
                SpreadsheetDeltaUrlQueryParameters.SELECTION,
                selection.toStringMaybeStar()
            ).addParameter(
                SpreadsheetDeltaUrlQueryParameters.SELECTION_TYPE,
                selection.selectionTypeName()
            );

            final SpreadsheetViewportAnchor anchor = anchoredSpreadsheetSelection.anchor();
            if (SpreadsheetViewportAnchor.NONE != anchor) {
                result = result.addParameter(
                    SpreadsheetDeltaUrlQueryParameters.SELECTION_ANCHOR,
                    anchor.kebabText()
                );
            }
        }

        final List<SpreadsheetViewportNavigation> navigations = viewport.navigations();
        if (false == navigations.isEmpty()) {
            result = result.addParameter(
                SpreadsheetDeltaUrlQueryParameters.NAVIGATION,
                SpreadsheetViewport.SEPARATOR.toSeparatedString(
                    navigations,
                    SpreadsheetViewportNavigation::text
                )
            );
        }

        return result;
    }

    /**
     * Appends the given window to the given {@link UrlQueryString}
     */
    public static UrlQueryString windowQueryString(final SpreadsheetViewportWindows window) {
        Objects.requireNonNull(window, "window");

        return window.isEmpty() ?
            UrlQueryString.EMPTY :
            UrlQueryString.EMPTY.addParameter(
                SpreadsheetDeltaUrlQueryParameters.WINDOW,
                window.toString()
            );
    }

    public static SpreadsheetDeltaFetcher with(final SpreadsheetDeltaFetcherWatcher watcher,
                                               final AppContext context) {
        return new SpreadsheetDeltaFetcher(
            watcher,
            context
        );
    }

    private SpreadsheetDeltaFetcher(final SpreadsheetDeltaFetcherWatcher watcher,
                                    final AppContext context) {
        super(
            watcher,
            context
        );
    }

    public void postClear(final SpreadsheetId id,
                          final SpreadsheetSelection selection) {
        final AppContext context = this.context;

        this.postDelta(
            url(
                id,
                selection
            ).appendPathName(
                SpreadsheetServerLinkRelations.CLEAR.toUrlPathName()
                    .get()
            ).setQuery(
                context.viewportAndWindowQueryString()
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
                context.viewportAndWindowQueryString()
            )
        );
    }

    public void getFindCells(final SpreadsheetId id,
                             final SpreadsheetCellRangeReference cells,
                             final SpreadsheetCellFindQuery find) {
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
                                    final SpreadsheetCellFindQuery find) {
        return SpreadsheetMetadataFetcher.url(id)
            .appendPathName(SpreadsheetHateosResourceNames.CELL.toUrlPathName())
            .appendPathName(
                UrlPathName.with(
                    Objects.requireNonNull(cells, "cells")
                        .toStringMaybeStar()
                )
            ).appendPathName(
                SpreadsheetServerLinkRelations.FIND.toUrlPathName()
                    .get()
            ).setQuery(
                find.toUrlQueryString()
            );
    }

    public void postInsertAfterColumn(final SpreadsheetId id,
                                      final SpreadsheetSelection selection,
                                      final int count) {
        // http://localhost:3000/api/spreadsheet/1/column/ABC/insert-after?count=2&home=A1&width=1712&height=765&includeFrozenColumnsRows=true
        this.postInsertColumnOrRow(
            id,
            selection,
            SpreadsheetServerLinkRelations.INSERT_AFTER,
            count
        );
    }

    public void postInsertBeforeColumn(final SpreadsheetId id,
                                       final SpreadsheetSelection selection,
                                       final int count) {
        // http://localhost:3000/api/spreadsheet/1/column/ABC/insert-before?count=2&home=A1&width=1712&height=765&includeFrozenColumnsRows=true
        this.postInsertColumnOrRow(
            id,
            selection,
            SpreadsheetServerLinkRelations.INSERT_BEFORE,
            count
        );
    }

    public void postInsertAfterRow(final SpreadsheetId id,
                                   final SpreadsheetSelection selection,
                                   final int count) {
        // http://localhost:3000/api/spreadsheet/1/row/ABC/insert-after?count=2&home=A1&width=1712&height=765&includeFrozenRowsRows=true
        this.postInsertColumnOrRow(
            id,
            selection,
            SpreadsheetServerLinkRelations.INSERT_AFTER,
            count
        );
    }

    public void postInsertBeforeRow(final SpreadsheetId id,
                                    final SpreadsheetSelection selection,
                                    final int count) {
        // http://localhost:3000/api/spreadsheet/1/row/ABC/insert-before?count=2&home=A1&width=1712&height=765&includeFrozenRowsRows=true
        this.postInsertColumnOrRow(
            id,
            selection,
            SpreadsheetServerLinkRelations.INSERT_BEFORE,
            count
        );
    }

    private void postInsertColumnOrRow(final SpreadsheetId id,
                                       final SpreadsheetSelection selection,
                                       final LinkRelation<?> afterOrBefore,
                                       final int count) {
        final AppContext context = this.context;

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
            ).appendPathName(
                afterOrBefore.toUrlPathName()
                    .get()
            ).setQuery(
                offsetAndCountQueryString(
                    OptionalInt.empty(), // offset
                    OptionalInt.of(count)
                ).addParameters(
                    context.viewportAndWindowQueryString()
                )
            ),
            FetcherRequestBody.string("")
        );
    }

    /**
     * Invokes the end-point
     * <pre>
     * GET /api/spreadsheet/{SpreadsheetId}/cell/{@link SpreadsheetExpressionReference}/labels?offset=1&count=1
     * </pre>
     */
    public void getCellLabels(final SpreadsheetId id,
                              final SpreadsheetExpressionReference reference,
                              final OptionalInt offset,
                              final OptionalInt count) {
        // GET /api/spreadsheet/{SpreadsheetId}/cell/{SpreadsheetExpressionReference}/labels?offset=1&count=1
        this.get(
            SpreadsheetMetadataFetcher.url(
                id
            ).appendPath(
                UrlPath.parse(
                    reference.cellColumnOrRowText() +
                        UrlPath.SEPARATOR +
                        reference.toStringMaybeStar() +
                        UrlPath.SEPARATOR
                )
            ).appendPathName(
                SpreadsheetServerLinkRelations.LABELS.toUrlPathName()
                    .get()
            ).setQuery(
                offsetAndCountQueryString(
                    offset,
                    count
                )
            )
        );
    }

    /**
     * Invokes the end-point
     * <pre>
     * GET /api/spreadsheet/{@link SpreadsheetId}/cell/{@link SpreadsheetExpressionReference}/references?offset=1&count=1
     * </pre>
     */
    public void getCellReferences(final SpreadsheetId id,
                                  final SpreadsheetExpressionReference reference,
                                  final OptionalInt offset,
                                  final OptionalInt count) {
        // GET /api/spreadsheet/{SpreadsheetId}/cell/{SpreadsheetExpressionReference}/references?offset=1&count=1
        this.get(
            SpreadsheetMetadataFetcher.url(
                id
            ).appendPath(
                UrlPath.parse(
                    reference.cellColumnOrRowText() +
                        UrlPath.SEPARATOR +
                        reference.toStringMaybeStar() +
                        UrlPath.SEPARATOR
                )
            ).appendPathName(
                SpreadsheetServerLinkRelations.REFERENCES.toUrlPathName()
                    .get()
            ).setQuery(
                offsetAndCountQueryString(
                    offset,
                    count
                )
            )
        );
    }

    /**
     * Invokes the end-point
     * <pre>
     * GET /api/spreadsheet/{@link SpreadsheetId}/label/{reference}/references?offset=1&count=1
     * </pre>
     */
    public void getLabelReferences(final SpreadsheetId id,
                                   final SpreadsheetLabelName label,
                                   final OptionalInt offset,
                                   final OptionalInt count) {
        // GET /api/spreadsheet/{SpreadsheetId}/label/{label}/references?offset=1&count=1
        this.get(
            SpreadsheetMetadataFetcher.url(
                id
            ).appendPath(
                UrlPath.parse(
                    "" +
                        SpreadsheetHateosResourceNames.LABEL +
                        UrlPath.SEPARATOR +
                        label.toStringMaybeStar() +
                        UrlPath.SEPARATOR
                )
            ).appendPathName(
                SpreadsheetServerLinkRelations.REFERENCES.toUrlPathName()
                    .get()
            ).setQuery(
                offsetAndCountQueryString(
                    offset,
                    count
                )
            )
        );
    }

    /**
     * Deletes the cells also passing the {@link SpreadsheetViewport}.
     */
    public void deleteCells(final SpreadsheetId id,
                            final SpreadsheetViewport viewport) {
        // DELETE http://localhost:3000/api/spreadsheet/1f/cell/$cells
        this.delete(
            url(
                id,
                Objects.requireNonNull(viewport, "viewport")
                    .anchoredSelection()
                    .get()
                    .selection()
            ).setQuery(
                viewportQueryString(
                    viewport
                )
            )
        );
    }

    /**
     * Loads the cells to fill the given rectangular area typically a {@link SpreadsheetViewport}.
     */
    public void getCells(final SpreadsheetId id,
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
                )
            )
        );
    }

    private final static UrlPath FORCE_RECOMPUTE = UrlPath.parse(
        CaseKind.kebabEnumName(SpreadsheetEngineEvaluation.FORCE_RECOMPUTE)
    );

    /**
     * Loads the given {@link SpreadsheetLabelName}.
     */
    public void getLabelMapping(final SpreadsheetId id,
                                final SpreadsheetLabelName labelName) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(labelName, "labelName");

        this.get(
            url(
                id,
                labelName
            )
        );
    }

    /**
     * Invokes the label find by name API
     * <pre>
     * /api/spreadsheet/SpreadsheetId/label/&star;/findByName/query
     * </pre>.
     */
    public void getLabelMappingsFindByName(final SpreadsheetId id,
                                           final String query,
                                           final OptionalInt offset,
                                           final OptionalInt count) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(query, "query");

        this.get(
            url(
                id
            ).appendPathName(UrlPathName.WILDCARD)
                .appendPathName(SpreadsheetDeltaHttpMappings.FIND_BY_NAME.toUrlPathName().get())
                .appendPath(UrlPath.parse(query))
                .setQuery(
                    offsetAndCountQueryString(
                        offset,
                        count
                    )
                )
        );
    }

    // /api/spreadsheet/SpreadsheetId/label/*/findByName
    //  1   2           3             4     5 6
    public static boolean isGetLabelMappingsFindByName(final HttpMethod method,
                                                       final UrlPath path) {
        boolean match = HttpMethod.GET.equals(method);

        if (match) {
            final List<UrlPathName> names = path.namesList();
            match = names.size() >= 7 && names.get(6).equals(SpreadsheetDeltaHttpMappings.FIND_BY_NAME.toUrlPathName().get());
        }

        return match;
    }

    /**
     * Saves/Creates the given {@link SpreadsheetLabelMapping}.
     */
    public void postLabelMapping(final SpreadsheetId id,
                                 final SpreadsheetLabelMapping mapping) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(mapping, "mapping");

        this.post(
            url(
                id,
                mapping.label()
            ),
            FetcherRequestBody.string(
                this.context.marshall(
                    SpreadsheetDelta.EMPTY.setLabels(
                        Sets.of(mapping)
                    )
                ).toString()
            )
        );
    }

    /**
     * DELETEs the given {@link SpreadsheetLabelName}
     */
    public void deleteLabelMapping(final SpreadsheetId id,
                                   final SpreadsheetLabelName labelName) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(labelName, "labelName");

        this.delete(
            url(
                id,
                labelName
            )
        );
    }

    /**
     * Invokes the end-point
     * <pre>
     * GET /api/spreadsheet/{@link SpreadsheetId}/label/{reference}/references?offset=1&count=1
     * </pre>
     */
    public void getLabelMappings(final SpreadsheetId id,
                                 final OptionalInt offset,
                                 final OptionalInt count) {
        // GET /api/spreadsheet/{SpreadsheetId}/label/*?offset=1&count=1
        this.get(
            SpreadsheetMetadataFetcher.url(
                id
            ).appendPath(
                UrlPath.EMPTY.append(
                    SpreadsheetHateosResourceNames.LABEL.toUrlPathName()
                ).append(UrlPathName.WILDCARD)
            ).setQuery(
                offsetAndCountQueryString(
                    offset,
                    count
                )
            )
        );
    }

    public void patchCellsFormulaText(final SpreadsheetId id,
                                      final SpreadsheetSelection selection,
                                      final Map<SpreadsheetCellReference, String> cellToFormulaTexts) {
        this.patchDeltaWithViewportAndWindowQueryString(
            id,
            selection,
            SpreadsheetDelta.cellsFormulaTextPatch(cellToFormulaTexts)
        );
    }

    public void patchCellsDateTimeSymbols(final SpreadsheetId id,
                                          final SpreadsheetSelection selection,
                                          final Map<SpreadsheetCellReference, Optional<DateTimeSymbols>> cellToDateTimeSymbols) {
        this.patchDeltaWithViewportAndWindowQueryString(
            id,
            selection,
            SpreadsheetDelta.cellsDateTimeSymbolsPatch(
                cellToDateTimeSymbols,
                this.context
            )
        );
    }

    public void patchCellsDecimalNumberSymbols(final SpreadsheetId id,
                                               final SpreadsheetSelection selection,
                                               final Map<SpreadsheetCellReference, Optional<DecimalNumberSymbols>> cellToDecimalNumberSymbols) {
        this.patchDeltaWithViewportAndWindowQueryString(
            id,
            selection,
            SpreadsheetDelta.cellsDecimalNumberSymbolsPatch(
                cellToDecimalNumberSymbols,
                this.context
            )
        );
    }

    public void patchCellsLocale(final SpreadsheetId id,
                                 final SpreadsheetSelection selection,
                                 final Map<SpreadsheetCellReference, Optional<Locale>> cellToLocales) {
        this.patchDeltaWithViewportAndWindowQueryString(
            id,
            selection,
            SpreadsheetDelta.cellsLocalePatch(
                cellToLocales,
                this.context
            )
        );
    }

    public void patchCellsFormatter(final SpreadsheetId id,
                                    final SpreadsheetSelection selection,
                                    final Map<SpreadsheetCellReference, Optional<SpreadsheetFormatterSelector>> cellToFormatters) {
        this.patchDeltaWithViewportAndWindowQueryString(
            id,
            selection,
            SpreadsheetDelta.cellsFormatterPatch(
                cellToFormatters,
                this.context
            )
        );
    }

    public void patchCellsParser(final SpreadsheetId id,
                                 final SpreadsheetSelection selection,
                                 final Map<SpreadsheetCellReference, Optional<SpreadsheetParserSelector>> cellToParsers) {
        this.patchDeltaWithViewportAndWindowQueryString(
            id,
            selection,
            SpreadsheetDelta.cellsParserPatch(
                cellToParsers,
                this.context
            )
        );
    }

    public void patchCellsStyle(final SpreadsheetId id,
                                final SpreadsheetSelection selection,
                                final Map<SpreadsheetCellReference, TextStyle> cellToStyles) {
        this.patchDeltaWithViewportAndWindowQueryString(
            id,
            selection,
            SpreadsheetDelta.cellsStylePatch(
                cellToStyles,
                this.context
            )
        );
    }

    public void patchCellsValidator(final SpreadsheetId id,
                                    final SpreadsheetSelection selection,
                                    final Map<SpreadsheetCellReference, Optional<ValidatorSelector>> cellToValidators) {
        this.patchDeltaWithViewportAndWindowQueryString(
            id,
            selection,
            SpreadsheetDelta.cellsValidatorPatch(
                cellToValidators,
                this.context
            )
        );
    }

    public void patchCellsValue(final SpreadsheetId id,
                                final SpreadsheetSelection selection,
                                final Map<SpreadsheetCellReference, Optional<Object>> cellToValue) {
        this.patchDeltaWithViewportAndWindowQueryString(
            id,
            selection,
            SpreadsheetDelta.cellsValuePatch(
                cellToValue,
                this.context
            )
        );
    }

    public void patchCellsValueType(final SpreadsheetId id,
                                    final SpreadsheetSelection selection,
                                    final Map<SpreadsheetCellReference, Optional<ValueTypeName>> cellToValueTypes) {
        this.patchDeltaWithViewportAndWindowQueryString(
            id,
            selection,
            SpreadsheetDelta.cellsValueTypePatch(
                cellToValueTypes,
                this.context
            )
        );
    }

    public void patchDateTimeSymbols(final SpreadsheetId id,
                                     final SpreadsheetSelection selection,
                                     final Optional<DateTimeSymbols> dateTimeSymbols) {
        this.patchDeltaWithViewportAndWindowQueryString(
            id,
            selection,
            SpreadsheetDelta.dateTimeSymbolsPatch(
                dateTimeSymbols,
                this.context
            )
        );
    }

    public void patchDecimalNumberSymbols(final SpreadsheetId id,
                                          final SpreadsheetSelection selection,
                                          final Optional<DecimalNumberSymbols> decimalNumberSymbols) {
        this.patchDeltaWithViewportAndWindowQueryString(
            id,
            selection,
            SpreadsheetDelta.decimalNumberSymbolsPatch(
                decimalNumberSymbols,
                this.context
            )
        );
    }

    public void patchFormatter(final SpreadsheetId id,
                               final SpreadsheetSelection selection,
                               final Optional<SpreadsheetFormatterSelector> formatter) {
        this.patchDeltaWithViewportAndWindowQueryString(
            id,
            selection,
            SpreadsheetDelta.formatterPatch(
                formatter,
                this.context
            )
        );
    }

    public void patchFormula(final SpreadsheetId id,
                             final SpreadsheetSelection selection,
                             final JsonNode formula) {
        this.patchDeltaWithViewportAndWindowQueryString(
            id,
            selection,
            SpreadsheetDelta.formulaPatch(formula)
        );
    }

    public void patchLocale(final SpreadsheetId id,
                            final SpreadsheetSelection selection,
                            final Optional<Locale> locale) {
        this.patchDeltaWithViewportAndWindowQueryString(
            id,
            selection,
            SpreadsheetDelta.localePatch(
                locale,
                this.context
            )
        );
    }

    public void patchParser(final SpreadsheetId id,
                            final SpreadsheetSelection selection,
                            final Optional<SpreadsheetParserSelector> parser) {
        this.patchDeltaWithViewportAndWindowQueryString(
            id,
            selection,
            SpreadsheetDelta.parserPatch(
                parser,
                this.context
            )
        );
    }

    public void patchStyle(final SpreadsheetId id,
                           final SpreadsheetSelection selection,
                           final JsonNode style) {
        this.patchDeltaWithViewportAndWindowQueryString(
            id,
            selection,
            SpreadsheetDelta.stylePatch(
                style
            )
        );
    }

    public <T> void patchStyleProperty(final SpreadsheetId id,
                                       final SpreadsheetSelection selection,
                                       final TextStylePropertyName<T> name,
                                       final Optional<T> value) {
        this.patchDeltaWithViewportAndWindowQueryString(
            id,
            selection,
            SpreadsheetDelta.stylePatch(
                name.stylePatch(
                    value.orElse(null)
                )
            )
        );
    }

    public void patchValidator(final SpreadsheetId id,
                               final SpreadsheetSelection selection,
                               final Optional<ValidatorSelector> validator) {
        this.patchDeltaWithViewportAndWindowQueryString(
            id,
            selection,
            SpreadsheetDelta.validatorPatch(
                validator,
                this.context
            )
        );
    }

    /**
     * Performs a PATCH after building a FORMULA PATCH using the given value type and JSON.
     */
    public void patchValue(final SpreadsheetId id,
                           final SpreadsheetSelection selection,
                           final ValueTypeName valueType,
                           final Optional<?> value) {
        this.patchDeltaWithViewportAndWindowQueryString(
            id,
            selection,
            patchValuePatch(
                valueType,
                value
            )
        );
    }

    // @VisibleForTesting
    JsonNode patchValuePatch(final ValueTypeName valueType,
                             final Optional<?> value) {
        final AppContext context = this.context;

        Object valueOrNull = value.orElse(null);

        if (SpreadsheetValueType.DATE.equals(valueType) && SpreadsheetCellValueDialogComponent.TODAY_TEXT.equals(valueOrNull)) {
            valueOrNull = context.now()
                .toLocalDate();
        } else {
            if (SpreadsheetValueType.DATE_TIME.equals(valueType) && SpreadsheetCellValueDialogComponent.NOW_TEXT.equals(valueOrNull)) {
                valueOrNull = context.now();
            } else {
                if (SpreadsheetValueType.TIME.equals(valueType) && SpreadsheetCellValueDialogComponent.NOW_TEXT.equals(valueOrNull)) {
                    valueOrNull = context.now()
                        .toLocalTime();
                }
            }
        }

        return SpreadsheetDelta.formulaPatch(
            JsonNode.object()
                .set(
                    VALUE,
                    context.marshallWithType(valueOrNull)
                )
        );
    }

    /**
     * Used to construct a PATCH
     */
    private final static JsonPropertyName TYPE = JsonPropertyName.with("type");

    private final static JsonPropertyName VALUE = JsonPropertyName.with("value");

    public void patchValueType(final SpreadsheetId id,
                               final SpreadsheetSelection selection,
                               final Optional<ValueTypeName> valueType) {
        this.patchDeltaWithViewportAndWindowQueryString(
            id,
            selection,
            SpreadsheetDelta.valueTypePatch(
                valueType,
                this.context
            )
        );
    }

    public void postCells(final SpreadsheetId id,
                          final SpreadsheetSelection selection,
                          final Set<SpreadsheetCell> cells) {
        final AppContext context = this.context;

        this.postDelta(
            url(
                id,
                selection
            ).setQuery(
                context.viewportAndWindowQueryString()
            ),
            SpreadsheetDelta.EMPTY.setCells(cells)
        );
    }

    // GET http://localhost:12345/api/spreadsheet/1/cell/A1%3AB3/sort?comparators=A%3Dtext
    public void getSortCells(final SpreadsheetId id,
                             final SpreadsheetExpressionReference selection,
                             final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparators) {
        this.get(
            url(
                id,
                selection
            ).appendPathName(
                SpreadsheetServerLinkRelations.SORT.toUrlPathName()
                    .get()
            ).setQuery(
                this.context.viewportAndWindowQueryString()
                    .addParameter(
                        SpreadsheetDeltaUrlQueryParameters.COMPARATORS,
                        comparators.text()
                    )
            )
        );
    }

    // form.............................................................................................................

    // GET /api/spreadsheet/SpreadsheetId/form/FormName
    public void getForm(final SpreadsheetId id,
                        final FormName formName) {
        this.get(
            formUrl(
                id,
                Optional.of(formName)
            )
        );
    }

    // GET /api/spreadsheet/SpreadsheetId/form/*?offset=0&count=1
    public void getForms(final SpreadsheetId id,
                         final OptionalInt offset,
                         final OptionalInt count) {
        this.get(
            formUrl(
                id,
                Optional.empty()
            ).setQuery(
                offsetAndCountQueryString(
                    offset,
                    count
                )
            )
        );
    }

    // POST /api/spreadsheet/SpreadsheetId/form/FormName
    public void postForm(final SpreadsheetId id,
                         final FormName formName,
                         final Form<SpreadsheetExpressionReference> form) {
        this.postDelta(
            formUrl(
                id,
                Optional.of(formName)
            ),
            SpreadsheetDelta.EMPTY.setForms(
                Sets.of(form)
            )
        );
    }

    // DELETE /api/spreadsheet/SpreadsheetId/form/FormName
    public void deleteForm(final SpreadsheetId id,
                           final FormName formName) {
        this.delete(
            formUrl(
                id,
                Optional.of(formName)
            )
        );
    }

    // @VisibleForTesting
    static RelativeUrl formUrl(final SpreadsheetId id,
                               final Optional<FormName> formName) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(formName, "formName");

        return SpreadsheetMetadataFetcher.url(id)
            .appendPathName(FormName.HATEOS_RESOURCE_NAME.toUrlPathName())
            .appendPathName(
                formName.map(
                    (FormName f) -> UrlPathName.with(
                        f.value()
                    )
                ).orElse(UrlPathName.WILDCARD)
            ).normalize();
    }

    // helpers..........................................................................................................

    private void patchDeltaWithViewportAndWindowQueryString(final SpreadsheetId id,
                                                            final SpreadsheetSelection selection,
                                                            final JsonNode delta) {
        this.patch(
            url(
                id,
                selection
            ).setQuery(
                this.context.viewportAndWindowQueryString()
            ),
            this.requestBody(delta)
        );
    }

    private void postDelta(final AbsoluteOrRelativeUrl url,
                           final SpreadsheetDelta delta) {
        this.post(
            url,
            this.requestBody(delta)
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
                    Objects.requireNonNull(selection, "selection")
                        .cellColumnOrRowText()
                )
            ).appendPath(
                UrlPath.parse(
                    selection.toStringMaybeStar()
                )
            ).appendPath(path);
    }

    // GET http://localhost:3000/api/spreadsheet/1/label
    static RelativeUrl url(final SpreadsheetId id) {
        return SpreadsheetMetadataFetcher.url(id)
            .appendPathName(SpreadsheetHateosResourceNames.LABEL.toUrlPathName());
    }

    static RelativeUrl url(final SpreadsheetId id,
                           final SpreadsheetLabelName labelName) {
        return url(id)
            .appendPath(
                UrlPath.parse(
                    labelName.value()
                )
            );
    }

    // Fetcher.........................................................................................................

    @Override
    public void onSuccess(final HttpMethod method,
                          final AbsoluteOrRelativeUrl url,
                          final String contentTypeName,
                          final Optional<String> body) {
        final AppContext context = this.context;

        switch (CharSequences.nullToEmpty(contentTypeName).toString()) {
            case "":
                this.watcher.onEmptyResponse(context);
                break;
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
}
