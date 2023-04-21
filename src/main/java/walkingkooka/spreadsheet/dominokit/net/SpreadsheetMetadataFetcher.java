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
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.tree.json.JsonNode;

import java.util.Objects;

public class SpreadsheetMetadataFetcher implements Fetcher {

    public static SpreadsheetMetadataFetcher with(final SpreadsheetMetadataWatcher watcher,
                                                  final AppContext context) {
        return new SpreadsheetMetadataFetcher(
                watcher,
                context
        );
    }

    private SpreadsheetMetadataFetcher(final SpreadsheetMetadataWatcher watcher,
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
    public void fetchLog(final String message) {
        this.context.debug(
                this.getClass().getSimpleName() +
                        " " +
                        message
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

    }

    @Override
    public void onError(final Object cause) {

    }

    private final SpreadsheetMetadataWatcher watcher;

    private final AppContext context;

    @Override
    public String toString() {
        return this.watcher.toString();
    }
}
