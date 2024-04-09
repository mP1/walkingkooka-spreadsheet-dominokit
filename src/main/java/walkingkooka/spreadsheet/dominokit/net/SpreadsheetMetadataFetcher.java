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
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlParameterName;
import walkingkooka.net.UrlPath;
import walkingkooka.net.UrlPathName;
import walkingkooka.net.UrlQueryString;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.server.context.SpreadsheetMetadataList;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * A specialised Fetcher that supports all typical CRUD operations relating to a {@link SpreadsheetMetadata}.
 * <br>
 * To create a new spreadsheet, simply call {@link SpreadsheetMetadataFetcher#createSpreadsheetMetadata()}. This will
 * POST an empty body to the server which will create a new spreadsheet with a new unique {@link SpreadsheetId}.
 * The locale in the request sent by the browser will be used as the {@link java.util.Locale} of the spreadsheet.
 * The response will contain a {@link SpreadsheetMetadata} in JSON form, and will include numerous default properties
 * of which there are many. These properties include:
 * <ul>
 *     <li>Audit info such as create/last updated author and timestamps</li>
 *     <li>Various mathematical properties which control precision and size of number calculations in expressions</li>
 *     <li>Default format patterns for all basic spreadsheet values</li>
 *     <li>Many more properties are available {@link SpreadsheetMetadataPropertyName}</li>
 * </ul>
 * <br>
 * It is possible to overwrite or set any of these properties by doing a PATCH with the new property name and value in
 * JSON form.
 * </br>
 * <br>
 * Loading a different spreadsheet is as simple as {@link SpreadsheetMetadataFetcher#loadSpreadsheetMetadata(SpreadsheetId)}.
 * </br>
 * <br>
 * Deletion of spreadsheets is accomplished by simplying call {@link SpreadsheetMetadataFetcher#deleteSpreadsheetMetadata(SpreadsheetId)}}.
 * </br>
 */
public final class SpreadsheetMetadataFetcher implements Fetcher {

    static {
        SpreadsheetMetadataList.empty(); // JSON register
    }

    private final static RelativeUrl API_BASE = Url.parseRelative("/api/spreadsheet");

    /**
     * Extracts the {@link SpreadsheetId} from a URL assumed to contain an endpoint.
     */
    static Optional<SpreadsheetId> extractSpreadsheetId(final AbsoluteOrRelativeUrl url) {
        SpreadsheetId id = null;

        final UrlPath path = url.path();

        int i = 0;
        for (final UrlPathName component : Iterables.iterator(path.iterator())) {
            i++;
            if (4 == i) {
                try {
                    id = SpreadsheetId.parse(component.value());
                } catch (final RuntimeException ignore) {
                    id = null;
                }
            }
        }

        return Optional.ofNullable(id);
    }

    public static SpreadsheetMetadataFetcher with(final SpreadsheetMetadataFetcherWatcher watcher,
                                                  final AppContext context) {
        Objects.requireNonNull(watcher, "watcher");
        Objects.requireNonNull(context, "context");

        return new SpreadsheetMetadataFetcher(
                watcher,
                context
        );
    }

    private SpreadsheetMetadataFetcher(final SpreadsheetMetadataFetcherWatcher watcher,
                                       final AppContext context) {
        this.watcher = watcher;
        this.context = context;
    }

    /**
     * Creates a new {@link SpreadsheetMetadata}.
     */
    public void createSpreadsheetMetadata() {
        this.post(
                API_BASE,
                ""
        );
    }

    /**
     * DELETE an existing spreadsheet
     */
    public void deleteSpreadsheetMetadata(final SpreadsheetId id) {
        this.delete(
                url(id)
        );
    }

    public void getSpreadsheetMetadatas(final OptionalInt from,
                                        final OptionalInt count) {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(count, "count");

        UrlQueryString query = UrlQueryString.EMPTY;

        if (from.isPresent()) {
            query = query.addParameter(
                    FROM,
                    String.valueOf(from.getAsInt())
            );
        }

        if (count.isPresent()) {
            query = query.addParameter(
                    COUNT,
                    String.valueOf(count.getAsInt())
            );
        }

        this.get(
                API_BASE.appendPath(STAR)
                        .setQuery(query)
        );
    }

    private final static UrlPath STAR = UrlPath.parse("/*");

    private final static UrlParameterName FROM = UrlParameterName.with("from");

    private final static UrlParameterName COUNT = UrlParameterName.with("count");


    /**
     * Loads an existing spreadsheet
     */
    public void loadSpreadsheetMetadata(final SpreadsheetId id) {
        this.get(
                url(id)
        );
    }

    /**
     * Patches the provided {@link SpreadsheetMetadata} property.
     */
    public <T> void patchMetadata(final SpreadsheetId id,
                                  final SpreadsheetMetadataPropertyName<T> propertyName,
                                  final T propertyValue) {
        this.patchMetadata(
                id,
                propertyName.patch(propertyValue)
        );
    }

    /**
     * Patches the {@link SpreadsheetMetadata}, with the patch created using {@link SpreadsheetMetadataPropertyName#patch(Object)}.
     */
    public void patchMetadata(final SpreadsheetId id,
                              final JsonNode node) {
        this.patch(
                url(id),
                this.toJson(
                        node
                )
        );
    }

    public <T> void patchMetadata(final SpreadsheetId id,
                                  final SpreadsheetMetadata metadata) {
        Objects.requireNonNull(metadata, "metadata");

        this.patch(
                url(id),
                this.toJson(
                        metadata
                )
        );
    }

    public static RelativeUrl url(final SpreadsheetId id) {
        Objects.requireNonNull(id, "id");

        return API_BASE.appendPath(
                UrlPath.parse(
                        id.toString()
                )
        );
    }

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
    public void onSuccess(final AbsoluteOrRelativeUrl url,
                          final String contentTypeName,
                          final String body) {
        final SpreadsheetMetadataFetcherWatcher watcher = this.watcher;
        final AppContext context = this.context;

        switch (CharSequences.nullToEmpty(contentTypeName).toString()) {
            case "":
                watcher.onNoResponse(
                        context
                );
                break;
            case "SpreadsheetMetadata":
                watcher.onSpreadsheetMetadata(
                        this.parse(
                                body,
                                SpreadsheetMetadata.class
                        ),
                        context
                );
                break;
            case "SpreadsheetMetadataList":
                watcher.onSpreadsheetMetadataList(
                        this.parse(
                                body,
                                SpreadsheetMetadataList.class
                        ),
                        context
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quoteAndEscape(contentTypeName));
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

    private final SpreadsheetMetadataFetcherWatcher watcher;

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
