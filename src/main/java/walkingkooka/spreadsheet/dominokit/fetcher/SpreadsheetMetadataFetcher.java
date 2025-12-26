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

import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlPath;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.server.SpreadsheetHttpServer;
import walkingkooka.spreadsheet.server.meta.SpreadsheetMetadataPropertyNameHateosResourceMappings;
import walkingkooka.spreadsheet.server.meta.SpreadsheetMetadataSet;
import walkingkooka.template.TemplateValueName;
import walkingkooka.template.url.UrlPathTemplate;
import walkingkooka.template.url.UrlPathTemplateValues;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * A specialised Fetcher that supports all typical CRUD operations relating to a {@link SpreadsheetMetadata}.
 * <br>
 * To create a new spreadsheet, simply call {@link SpreadsheetMetadataFetcher#postCreateSpreadsheetMetadata()}. This will
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
 * Loading a different spreadsheet is as simple as {@link SpreadsheetMetadataFetcher#getSpreadsheetMetadata(SpreadsheetId)}.
 * </br>
 * <br>
 * Deletion of spreadsheets is accomplished by simply call {@link SpreadsheetMetadataFetcher#deleteSpreadsheetMetadata(SpreadsheetId)}}.
 * </br>
 */
public final class SpreadsheetMetadataFetcher extends Fetcher<SpreadsheetMetadataFetcherWatcher> {

    private final static RelativeUrl API_BASE = Url.EMPTY_RELATIVE_URL.appendPath(SpreadsheetHttpServer.API_SPREADSHEET);

    /**
     * Extracts the {@link SpreadsheetId} from a URL assumed to contain an endpoint.
     * <pre>
     * /api/spreadsheet/SpreadsheetId
     * </pre>
     */
    public static Optional<SpreadsheetId> extractSpreadsheetId(final AbsoluteOrRelativeUrl url) {
        Optional<SpreadsheetId> id = Optional.empty();

        final Optional<UrlPathTemplateValues> pathValues = SPREADSHEET_ID_PATH_TEMPLATE.tryPrepareValues(url.path());
        if (pathValues.isPresent()) {
            id = pathValues.get()
                .get(
                    SPREADSHEET_ID,
                    SpreadsheetId::parse
                );
        }

        return id;
    }

    private final static UrlPathTemplate SPREADSHEET_ID_PATH_TEMPLATE = UrlPathTemplate.parse("/api/spreadsheet/${SpreadsheetId}/${any}");

    private final static TemplateValueName SPREADSHEET_ID = TemplateValueName.with("SpreadsheetId");

    public static SpreadsheetId extractSpreadsheetIdOrFail(final AbsoluteOrRelativeUrl url) {
        return extractSpreadsheetId(url)
            .orElseThrow(() -> new IllegalArgumentException("Missing SpreadsheetId from " + url));
    }

    public static SpreadsheetMetadataFetcher with(final SpreadsheetMetadataFetcherWatcher watcher,
                                                  final AppContext context) {
        return new SpreadsheetMetadataFetcher(
            watcher,
            context
        );
    }

    private SpreadsheetMetadataFetcher(final SpreadsheetMetadataFetcherWatcher watcher,
                                       final AppContext context) {
        super(
            watcher,
            context
        );
    }

    /**
     * Creates a new {@link SpreadsheetMetadata}.
     */
    public void postCreateSpreadsheetMetadata() {
        this.post(
            API_BASE,
            FetcherRequestBody.string("")
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

    public void getSpreadsheetMetadatas(final OptionalInt offset,
                                        final OptionalInt count) {
        this.get(
            API_BASE.appendPath(STAR)
                .setQuery(
                    offsetAndCountQueryString(
                        offset,
                        count
                    )
                )
        );
    }

    private final static UrlPath STAR = UrlPath.parse("/*");

    /**
     * Loads an existing spreadsheet
     */
    public void getSpreadsheetMetadata(final SpreadsheetId id) {
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
            this.requestBody(
                node
            )
        );
    }

    public <T> void patchMetadata(final SpreadsheetId id,
                                  final SpreadsheetMetadata metadata) {
        Objects.requireNonNull(metadata, "metadata");

        this.patch(
            url(id),
            this.requestBody(
                metadata
            )
        );
    }

    // /api/spreadsheet/SpreadsheetId/metadata/SpreadsheetMetadataPropertyName
    public static RelativeUrl propertyUrl(final SpreadsheetId id,
                                          final SpreadsheetMetadataPropertyName<?> propertyName) {
        return url(id)
            .appendPathName(SpreadsheetMetadataPropertyNameHateosResourceMappings.HATEOS_RESOURCE_NAME.toUrlPathName())
            .appendPathName(
                propertyName.toUrlPathName()
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
    public void onSuccess(final HttpMethod method,
                          final AbsoluteOrRelativeUrl url,
                          final String contentTypeName,
                          final Optional<String> body) {
        final SpreadsheetMetadataFetcherWatcher watcher = this.watcher;

        switch (CharSequences.nullToEmpty(contentTypeName).toString()) {
            case "":
                watcher.onEmptyResponse(
                );
                break;
            case "SpreadsheetMetadata":
                watcher.onSpreadsheetMetadata(
                    this.parse(
                        body,
                        SpreadsheetMetadata.class
                    )
                );
                break;
            case "SpreadsheetMetadataSet":
                watcher.onSpreadsheetMetadataSet(
                    this.parse(
                        body,
                        SpreadsheetMetadataSet.class
                    )
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quoteAndEscape(contentTypeName));
        }
    }

    // Logging..........................................................................................................

    @Override
    boolean isDebugEnabled() {
        return SPREADSHEET_METADATA_FETCHER;
    }
}
