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

import elemental2.dom.Headers;
import walkingkooka.net.Url;
import walkingkooka.net.UrlParameterName;
import walkingkooka.net.UrlPath;
import walkingkooka.net.UrlQueryString;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class SpreadsheetDeltaFetcher implements Fetcher {

    /**
     * Creates a {@link UrlQueryString} with parameters taken from the given method parameters.
     * <pre>
     * window=A1%3AP9&
     * selection=B1&
     * selectionType=cell
     * </pre>
     */
    public static UrlQueryString urlQueryString(final SpreadsheetSelection selection,
                                                final Set<SpreadsheetCellRange> window) {
        Objects.requireNonNull("window", "window");
        Objects.requireNonNull(selection, "selection");

        UrlQueryString queryString = UrlQueryString.EMPTY
                .addParameter(
                        SELECTION,
                        selection.toString()
                ).addParameter(
                        SELECTION_TYPE,
                        selection.selectionTypeName()
                );

        if (false == window.isEmpty()) {
            queryString = queryString.addParameter(
                    WINDOW,
                    SpreadsheetSelection.toStringWindow(window)
            );
        }

        return queryString;
    }

    private final static UrlParameterName SELECTION = UrlParameterName.with("selection");

    private final static UrlParameterName SELECTION_TYPE = UrlParameterName.with("selectionType");

    private final static UrlParameterName WINDOW = UrlParameterName.with("window");

    static SpreadsheetDeltaFetcher with(final SpreadsheetDeltaWatcher watcher,
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
     * Performs a POST using the parameters to build the url and the delta if present for the body.
     */
    public void post(final SpreadsheetId id,
                     final SpreadsheetSelection selection,
                     final Optional<UrlPath> path,
                     final UrlQueryString queryString,
                     final Optional<SpreadsheetDelta> delta) {
        this.post(
                this.url(
                        id,
                        selection,
                        path,
                        queryString
                ),
                delta.isPresent() ?
                        this.toJson(delta.get()) :
                        ""
        );
    }

    private Url url(final SpreadsheetId id,
                    final SpreadsheetSelection selection,
                    final Optional<UrlPath> path,
                    final UrlQueryString queryString) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(selection, "selection");
        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(queryString, "queryString");

        UrlPath urlPath = UrlPath.parse("/api/spreadsheet/")
                .append(UrlPath.parse(id.toString()));
        if (path.isPresent()) {
            urlPath = urlPath.append(path.get());
        }

        return urlPath.addQueryString(
                queryString
        );
    }

    @Override
    public void fetchLog(final String message) {
        this.context.debug(
                this.getClass().getSimpleName() +
                        " " +
                        message
        );
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

    private final AppContext context;

    @Override
    public String toString() {
        return this.watcher.toString();
    }
}
