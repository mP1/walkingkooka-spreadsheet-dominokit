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

import walkingkooka.Context;
import walkingkooka.net.Url;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.tree.text.TextStyle;

import java.util.Objects;
import java.util.Optional;

public interface AppContext extends Context {

    void addSpreadsheetDeltaWatcher(final SpreadsheetDeltaWatcher watcher);

    SpreadsheetDeltaFetcher spreadsheetDeltaFetcher();

    void fireSpreadsheetDelta(final SpreadsheetDelta delta);

    void addSpreadsheetMetadataWatcher(final SpreadsheetMetadataWatcher watcher);

    SpreadsheetMetadataFetcher spreadsheetMetadataFetcher();

    void fireSpreadsheetMetadata(final SpreadsheetMetadata metadata);

    /**
     * Creates a new {@link SpreadsheetMetadata}.
     */
    default void createSpreadsheetMetadata() {
        this.spreadsheetMetadataFetcher()
                .post(
                        Url.parseRelative("/api/spreadsheet"),
                        ""
                );
    }

    /**
     * Loads an existing spreadsheet
     */
    default void loadSpreadsheetMetadata(final SpreadsheetId id) {
        Objects.requireNonNull(id, "id");

        this.spreadsheetMetadataFetcher()
                .get(
                        Url.parseRelative("/api/spreadsheet/" + id)
                );
    }

    default <T> void patchSpreadsheetMetadata(final SpreadsheetId id,
                                              final SpreadsheetMetadataPropertyName<T> propertyName,
                                              final T propertyValue) {
        this.spreadsheetMetadataFetcher()
                .patchMetadata(
                        id,
                        propertyName,
                        propertyValue
                );
    }

    /**
     * Returns the current or last loaded {@link SpreadsheetMetadata}.
     */
    SpreadsheetMetadata spreadsheetMetadata();

    /**
     * Parses the current history token.
     */
    Optional<HistoryToken> historyToken();

    void pushHistoryToken(final HistoryToken token);

    TextStyle viewportAll(final boolean selected);

    TextStyle viewportCell(final boolean selected);

    TextStyle viewportColumnHeader(final boolean selected);

    TextStyle viewportRowHeader(final boolean selected);

    void debug(final Object message);

    void error(final Object message);
}
