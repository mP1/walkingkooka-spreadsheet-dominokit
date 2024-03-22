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
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.tree.json.JsonNode;

import java.util.Objects;
import java.util.Optional;

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
                Url.parseRelative("/api/spreadsheet"),
                ""
        );
    }

    /**
     * DELETE an existing spreadsheet
     */
    public void deleteSpreadsheetMetadata(final SpreadsheetId id) {
        this.delete(
                this.url(id)
        );
    }

    /**
     * Loads an existing spreadsheet
     */
    public void loadSpreadsheetMetadata(final SpreadsheetId id) {
        this.get(
                this.url(id)
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
                this.url(id),
                this.toJson(
                        node
                )
        );
    }

    public <T> void patchMetadata(final SpreadsheetId id,
                                  final SpreadsheetMetadata metadata) {
        Objects.requireNonNull(metadata, "metadata");

        this.patch(
                this.url(id),
                this.toJson(
                        metadata
                )
        );
    }

    public RelativeUrl url(final SpreadsheetId id) {
        Objects.requireNonNull(id, "id");

        return Url.parseRelative("/api/spreadsheet/" + id);
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
    public void onSuccess(final String body) {
        this.watcher.onSpreadsheetMetadata(
                this.parse(
                        body,
                        SpreadsheetMetadata.class
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
