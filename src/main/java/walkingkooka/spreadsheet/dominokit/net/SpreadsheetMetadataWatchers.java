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
import walkingkooka.watch.Watchers;

public final class SpreadsheetMetadataWatchers implements SpreadsheetMetadataWatcher {

    public static SpreadsheetMetadataWatchers empty() {
        return new SpreadsheetMetadataWatchers();
    }

    /**
     * Adds a new {@link SpreadsheetMetadataWatcher} which will receive all events until removed using the returned {@link Runnable}.
     */
    public Runnable add(final SpreadsheetMetadataWatcher watcher) {
        return this.watchers.add(
                (e) -> e.accept(watcher)
        );
    }

    public Runnable addOnce(final SpreadsheetMetadataWatcher watcher) {
        return this.watchers.addOnce(
                (e) -> e.accept(watcher)
        );
    }

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        this.watchers.accept(
                SpreadsheetMetadataWatchersEvent.with(
                        metadata,
                        context
                )
        );
    }

    private final Watchers<SpreadsheetMetadataWatchersEvent> watchers = Watchers.create();

    @Override
    public String toString() {
        return this.watchers.toString();
    }
}
