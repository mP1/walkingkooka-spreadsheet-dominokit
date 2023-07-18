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

public final class SpreadsheetDeltaWatchers implements SpreadsheetDeltaWatcher {

    public static SpreadsheetDeltaWatchers empty() {
        return new SpreadsheetDeltaWatchers();
    }

    /**
     * Adds a new {@link SpreadsheetDeltaWatcher} which will receive all events until removed using the returned {@link Runnable}.
     */
    public Runnable add(final SpreadsheetDeltaWatcher watcher) {
        return this.watchers.add(
                (e) -> e.accept(watcher)
        );
    }

    /**
     * Adds a {@link SpreadsheetDeltaWatcher} which will be removed after the first event is fired. This is unlike
     * {@link #add(SpreadsheetDeltaWatcher)} will continue to receive all events until the watcher is removed.
     */
    public void addOnce(final SpreadsheetDeltaWatcher watcher) {
        final Runnable[] remover = new Runnable[1];
        remover[0] = this.add(
                (d, c) -> {
                    try {
                        watcher.onSpreadsheetDelta(d, c);
                    } finally {
                        remover[0].run();
                    }
                }
        );
    }

    @Override
    public void onSpreadsheetDelta(final SpreadsheetDelta delta,
                                   final AppContext context) {
        this.watchers.accept(
                SpreadsheetDeltaWatchersEvent.with(
                        delta,
                        context
                )
        );
    }

    private final Watchers<SpreadsheetDeltaWatchersEvent> watchers = Watchers.create();

    @Override
    public String toString() {
        return this.watchers.toString();
    }
}
