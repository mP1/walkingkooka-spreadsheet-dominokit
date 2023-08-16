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
import walkingkooka.net.UrlPath;
import walkingkooka.net.UrlPathName;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;

import java.util.Objects;
import java.util.Optional;

public final class SpreadsheetLabelMappingFetcher implements Fetcher {

    public static SpreadsheetLabelMappingFetcher with(final SpreadsheetLabelMappingWatcher watcher,
                                                      final AppContext context) {
        Objects.requireNonNull(watcher, "watcher");
        Objects.requireNonNull(context, "context");

        return new SpreadsheetLabelMappingFetcher(
                watcher,
                context
        );
    }

    private SpreadsheetLabelMappingFetcher(final SpreadsheetLabelMappingWatcher watcher,
                                           final AppContext context) {
        this.watcher = watcher;
        this.context = context;
    }

    /**
     * Loads the given {@link SpreadsheetLabelName}.
     */
    public void loadLabelMapping(final SpreadsheetId id,
                                 final SpreadsheetLabelName labelName) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(labelName, "labelName");

        this.get(
                this.url(
                        id,
                        labelName
                )
        );
    }

    /**
     * Saves the given {@link SpreadsheetLabelMapping}.
     */
    public void saveLabelMapping(final SpreadsheetId id,
                                 final SpreadsheetLabelMapping mapping) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(mapping, "mapping");

        this.post(
                this.url(
                        id,
                        mapping.label()
                ),
                this.context.marshallContext()
                        .marshall(mapping)
                        .toString()
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
                this.url(
                        id,
                        labelName
                )
        );
    }

    // GET http://localhost:3000/api/spreadsheet/1/label/Label123
    public RelativeUrl url(final SpreadsheetId id,
                           final SpreadsheetLabelName labelName) {
        return Url.EMPTY_RELATIVE_URL.setPath(
                API.append(
                                UrlPathName.with(id.toString())
                        ).append(LABEL)
                        .append(UrlPathName.with(labelName.value()))
        );
    }

    private final static UrlPath API = UrlPath.parse("/api/spreadsheet");

    private final static UrlPath LABEL = UrlPath.parse("/label");

    @Override
    public void fetchLog(final Object... values) {
        this.context.debug(values);
    }

    @Override
    public void onSuccess(final String body) {
        this.watcher.onSpreadsheetLabelMapping(
                Optional.ofNullable(
                        body.isEmpty() ?
                                null :
                                this.parse(
                                        body,
                                        SpreadsheetLabelMapping.class
                                )
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

    private final SpreadsheetLabelMappingWatcher watcher;

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
