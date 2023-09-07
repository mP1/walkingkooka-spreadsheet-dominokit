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
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.watch.Watchers;

public final class SpreadsheetDeltaFetcherWatchers implements SpreadsheetDeltaFetcherWatcher {

    public static SpreadsheetDeltaFetcherWatchers empty() {
        return new SpreadsheetDeltaFetcherWatchers();
    }

    /**
     * Adds a new {@link SpreadsheetDeltaFetcherWatcher} which will receive all events until removed using the returned {@link Runnable}.
     */
    public Runnable add(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.watchers.add(
                (e) -> e.accept(watcher)
        );
    }

    /**
     * Adds a {@link SpreadsheetDeltaFetcherWatcher} which will be removed after the first event is fired. This is unlike
     * {@link #add(SpreadsheetDeltaFetcherWatcher)} will continue to receive all events until the watcher is removed.
     */
    public Runnable addOnce(final SpreadsheetDeltaFetcherWatcher watcher) {
        return this.watchers.addOnce(
                (e) -> e.accept(watcher)
        );
    }

    @Override
    public void onSpreadsheetDelta(final SpreadsheetDelta delta,
                                   final AppContext context) {
        this.watchers.accept(
                SpreadsheetDeltaFetcherWatchersEvent.with(
                        delta,
                        context
                )
        );
    }

    private final Watchers<SpreadsheetDeltaFetcherWatchersEvent> watchers = Watchers.create();

    @Override
    public String toString() {
        return this.watchers.toString();
    }
}
