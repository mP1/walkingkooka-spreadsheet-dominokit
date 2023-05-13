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
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetViewportWindows;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.engine.SpreadsheetEngineEvaluation;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionNavigation;
import walkingkooka.text.CaseKind;

import java.util.Objects;
import java.util.Optional;

public class SpreadsheetDeltaFetcher implements Fetcher {

    /**
     * Creates a {@link UrlQueryString} with parameters taken from the given method parameters.
     * <pre>
     * window=A1%3AP9&
     * selection=B1&
     * selectionType=cell
     * </pre>
     */
    public static UrlQueryString appendViewportSelectionAndWindow(final SpreadsheetViewportSelection viewportSelection,
                                                                  final SpreadsheetViewportWindows window,
                                                                  final UrlQueryString queryString) {
        return appendWindow(
                window,
                appendViewportSelection(
                        viewportSelection,
                        queryString
                )
        );
    }

    /**
     * Appends the given {@link SpreadsheetSelection} to the given {@link UrlQueryString}
     * <pre>
     * window=A1%3AP9&
     * selection=B1&
     * selectionType=cell
     * </pre>
     */
    public static UrlQueryString appendViewportSelection(final SpreadsheetViewportSelection viewportSelection,
                                                         final UrlQueryString queryString) {
        Objects.requireNonNull(viewportSelection, "viewportSelection");
        Objects.requireNonNull(queryString, "queryString");

        final SpreadsheetSelection selection = viewportSelection.selection();

        UrlQueryString result = queryString.addParameter(
                SELECTION,
                selection.toString()
        ).addParameter(
                SELECTION_TYPE,
                selection.selectionTypeName()
        );

        final SpreadsheetViewportSelectionAnchor anchor = viewportSelection.anchor();
        if (SpreadsheetViewportSelectionAnchor.NONE != anchor) {
            result = result.addParameter(
                    SELECTION_ANCHOR,
                    viewportSelection.anchor().kebabText()
            );
        }

        final Optional<SpreadsheetViewportSelectionNavigation> navigation = viewportSelection.navigation();
        if (navigation.isPresent()) {
            result = result.addParameter(
                    SELECTION_NAVIGATION,
                    navigation.get().kebabText()
            );
        }

        return result;
    }

    private final static UrlParameterName SELECTION = UrlParameterName.with("selection");

    private final static UrlParameterName SELECTION_TYPE = UrlParameterName.with("selectionType");

    private final static UrlParameterName SELECTION_ANCHOR = UrlParameterName.with("selectionAnchor");

    /**
     * Holds a direction resulting from entering an arrow key etc.
     */
    private final static UrlParameterName SELECTION_NAVIGATION = UrlParameterName.with("selectionNavigation");

    /**
     * Appends the given window to the given {@link UrlQueryString}
     */
    public static UrlQueryString appendWindow(final SpreadsheetViewportWindows window,
                                              final UrlQueryString queryString) {
        Objects.requireNonNull(window, "window");
        Objects.requireNonNull(queryString, "queryString");

        return window.isEmpty() ?
                queryString :
                queryString.addParameter(
                        WINDOW,
                        window.toString()
                );
    }

    private final static UrlParameterName WINDOW = UrlParameterName.with("window");

    public static SpreadsheetDeltaFetcher with(final SpreadsheetDeltaWatcher watcher,
                                               final AppContext context) {
        return new SpreadsheetDeltaFetcher(
                watcher,
                context
        );
    }

    private SpreadsheetDeltaFetcher(final SpreadsheetDeltaWatcher watcher,
                                    final AppContext context) {
        this.watcher = watcher;
        this.context = context;
    }

    /**
     * DELETEs the given {@link SpreadsheetViewportSelection} such as a cell/column/row.
     */
    public void deleteDelta(final SpreadsheetId id,
                            final SpreadsheetViewportSelection viewportSelection) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(viewportSelection, "viewportSelection");

        final SpreadsheetSelection selection = viewportSelection.selection();

        this.delete(
                this.url(
                        id,
                        selection,
                        Optional.empty()
                ).setQuery(
                        SpreadsheetDeltaFetcher.appendWindow(
                                this.context.viewportWindow(),
                                UrlQueryString.EMPTY
                        )
                )
        );
    }

    /**
     * Loads the cells to fill the given rectangular area typically a viewport.
     */
    public void loadCells(
            final SpreadsheetId id,
            final SpreadsheetCellReference home,
            final int width,
            final int height,
            final Optional<SpreadsheetViewportSelection> viewportSelection,
            final Optional<SpreadsheetViewportSelectionNavigation> navigation) {
        Objects.requireNonNull(navigation, "navigation");

        this.context.debug("SpreadsheetDeltaFetcher.loadCells " + home + " " + width + "x" + height);

        // load cells for the new window...
        //http://localhost:3000/api/spreadsheet/1f/cell/*/force-recompute?home=A1&width=1712&height=765&includeFrozenColumnsRows=true

        UrlQueryString queryString = UrlQueryString.EMPTY
                .addParameter(HOME, home.toString())
                .addParameter(WIDTH, String.valueOf(width))
                .addParameter(HEIGHT, String.valueOf(height))
                .addParameter(INCLUDE_FROZEN_COLUMNS_ROWS, Boolean.TRUE.toString());

        if (viewportSelection.isPresent()) {
            queryString = appendViewportSelection(
                    viewportSelection.get()
                            .setNavigation(navigation),
                    queryString
            );
        }

        this.get(
                Url.parseRelative(
                        "/api/spreadsheet/" +
                                id +
                                "/cell/*/" +
                                CaseKind.kebabEnumName(SpreadsheetEngineEvaluation.FORCE_RECOMPUTE)
                ).setQuery(queryString)
        );
    }

    private final static UrlParameterName HOME = UrlParameterName.with("home");
    private final static UrlParameterName WIDTH = UrlParameterName.with("width");
    private final static UrlParameterName HEIGHT = UrlParameterName.with("height");
    private final static UrlParameterName INCLUDE_FROZEN_COLUMNS_ROWS = UrlParameterName.with("includeFrozenColumnsRows");

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
        Objects.requireNonNull(selection, "selection");
        Objects.requireNonNull(path, "path");

        urlPath = urlPath.append(UrlPath.parse(selection.cellColumnOrRowText()))
                .append(UrlPath.parse(selection.toString()));

        if (path.isPresent()) {
            urlPath = urlPath.append(path.get());
        }

        return urlPath.addQueryString(
                UrlQueryString.EMPTY
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

    }

    @Override
    public void onError(final Object cause) {

    }

    private final SpreadsheetDeltaWatcher watcher;

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
