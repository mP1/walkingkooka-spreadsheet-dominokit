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

import java.util.function.Consumer;

/**
 * The event payload used by {@link SpreadsheetDeltaWatchers}.
 */
final class SpreadsheetDeltaWatchersEvent implements Consumer<SpreadsheetDeltaWatcher> {

    static SpreadsheetDeltaWatchersEvent with(final SpreadsheetDelta delta, final AppContext context) {
        return new SpreadsheetDeltaWatchersEvent(delta, context);
    }

    private SpreadsheetDeltaWatchersEvent(final SpreadsheetDelta delta, final AppContext context) {
        this.delta = delta;
        this.context = context;
    }

    @Override
    public void accept(final SpreadsheetDeltaWatcher watcher) {
        try {
            watcher.onSpreadsheetDelta(
                    this.delta,
                    this.context
            );
        } catch (final Exception cause) {
            this.context.error(
                    "SpreadsheetDeltaWatchersEvent.accept exception: " + cause.getMessage(),
                    cause
            );
        }
    }

    private final SpreadsheetDelta delta;

    private final AppContext context;

    @Override
    public String toString() {
        return this.delta + " " + this.context;
    }
}
