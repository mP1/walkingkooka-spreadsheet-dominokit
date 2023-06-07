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
import walkingkooka.store.Watchers;

public final class SpreadsheetDeltaWatchers implements SpreadsheetDeltaWatcher {

    public static SpreadsheetDeltaWatchers empty() {
        return new SpreadsheetDeltaWatchers();
    }

    public Runnable add(final SpreadsheetDeltaWatcher watcher) {
        return this.watchers.addWatcher(
                (e) -> e.accept(watcher)
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
