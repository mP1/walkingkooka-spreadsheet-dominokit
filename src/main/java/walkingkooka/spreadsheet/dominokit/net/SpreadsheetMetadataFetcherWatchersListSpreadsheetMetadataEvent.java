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

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

import java.util.List;

/**
 * The event payload used by {@link SpreadsheetMetadataFetcherWatchers}.
 */
final class SpreadsheetMetadataFetcherWatchersListSpreadsheetMetadataEvent extends FetcherWatchersEvent<SpreadsheetMetadataFetcherWatcher> {

    static SpreadsheetMetadataFetcherWatchersListSpreadsheetMetadataEvent with(final List<SpreadsheetMetadata> metadatas,
                                                                               final AppContext context) {
        return new SpreadsheetMetadataFetcherWatchersListSpreadsheetMetadataEvent(
                metadatas,
                context
        );
    }

    private SpreadsheetMetadataFetcherWatchersListSpreadsheetMetadataEvent(final List<SpreadsheetMetadata> metadatas,
                                                                           final AppContext context) {
        super(context);
        this.metadatas = metadatas;
    }

    @Override
    public void accept(final SpreadsheetMetadataFetcherWatcher watcher) {
        try {
            watcher.onSpreadsheetMetadataList(
                    this.metadatas,
                    this.context
            );
        } catch (final Exception cause) {
            this.context.error(
                    "SpreadsheetMetadataFetcherWatchersEvent.accept exception: " + cause.getMessage(),
                    cause
            );
        }
    }

    private final List<SpreadsheetMetadata> metadatas;

    @Override
    public String toString() {
        return this.metadatas + " " + this.context;
    }
}
