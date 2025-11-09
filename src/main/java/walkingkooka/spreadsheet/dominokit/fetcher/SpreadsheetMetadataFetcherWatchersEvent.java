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

import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

/**
 * The event payload used by {@link SpreadsheetMetadataFetcherWatchers}.
 */
final class SpreadsheetMetadataFetcherWatchersEvent extends FetcherWatchersEvent<SpreadsheetMetadataFetcherWatcher> {

    static SpreadsheetMetadataFetcherWatchersEvent with(final SpreadsheetMetadata metadata) {
        return new SpreadsheetMetadataFetcherWatchersEvent(metadata);
    }

    private SpreadsheetMetadataFetcherWatchersEvent(final SpreadsheetMetadata metadata) {
        super();
        this.metadata = metadata;
    }

    @Override
    void fire(final SpreadsheetMetadataFetcherWatcher watcher) {
        watcher.onSpreadsheetMetadata(this.metadata);
    }

    private final SpreadsheetMetadata metadata;

    @Override
    public String toString() {
        return this.metadata.toString();
    }
}
