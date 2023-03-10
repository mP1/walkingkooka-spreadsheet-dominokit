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
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

public class SpreadsheetMetadataFetcher implements Fetcher {

    static SpreadsheetMetadataFetcher with(final SpreadsheetMetadataWatcher watcher) {
        return new SpreadsheetMetadataFetcher(watcher);
    }

    private SpreadsheetMetadataFetcher(final SpreadsheetMetadataWatcher watcher) {
        this.watcher = watcher;
    }

    @Override
    public void onSuccess(final String body) {
        this.watcher.onSpreadsheetMetadata(
                this.parse(
                        body,
                        SpreadsheetMetadata.class
                )
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

    @Override
    public String toString() {
        return this.watcher.toString();
    }
}
