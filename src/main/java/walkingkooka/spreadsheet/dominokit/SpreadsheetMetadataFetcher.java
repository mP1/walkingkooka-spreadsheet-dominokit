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
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Objects;

public class SpreadsheetMetadataFetcher implements Fetcher {

    static SpreadsheetMetadataFetcher with(final SpreadsheetMetadataWatcher watcher,
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
     * Loads an existing spreadsheet
     */
    public void loadSpreadsheetMetadata(final SpreadsheetId id) {
        Objects.requireNonNull(id, "id");

        this.get(
                        Url.parseRelative("/api/spreadsheet/" + id)
        );
    }

    /**
     * Patches the provided {@link SpreadsheetMetadata} property.
     */
    <T> void patchMetadata(final SpreadsheetId id,
                           final SpreadsheetMetadataPropertyName<T> propertyName,
                           final T propertyValue) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(propertyName, "propertyName");

        this.patch(
                Url.parseRelative("/api/spreadsheet/" + id),
                this.toJson(
                        SpreadsheetMetadata.EMPTY.setOrRemove(
                                propertyName,
                                propertyValue
                        )
                )
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
