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

package walkingkooka.spreadsheet.dominokit.history;

import com.google.gwt.core.shared.GWT;
import walkingkooka.spreadsheet.dominokit.App;
import walkingkooka.spreadsheet.dominokit.AppContext;

import java.util.function.Consumer;

/**
 * The event payload used by {@link HistoryTokenWatchers}.
 */
final class HistoryTokenWatchersEvent implements Consumer<HistoryTokenWatcher> {

    static HistoryTokenWatchersEvent with(final HistoryToken previous, final AppContext context) {
        return new HistoryTokenWatchersEvent(previous, context);
    }

    private HistoryTokenWatchersEvent(final HistoryToken previous, final AppContext context) {
        this.previous = previous;
        this.context = context;
    }

    @Override
    public void accept(final HistoryTokenWatcher watcher) {
        final long before = System.currentTimeMillis();

        try {
            watcher.onHistoryTokenChange(
                this.previous,
                this.context
            );
        } catch (final Exception cause) {
            this.context.error(
                "HistoryTokenWatchersEvent.accept exception: " + cause.getMessage(),
                cause
            );
        } finally {
            // dont log too slow messages in JVM run mode (could be debugging etc).
            if (GWT.isClient()) {
                final long after = System.currentTimeMillis();
                final long millsTaken = after - before;
                if (millsTaken > App.SLOW_HISTORY_TOKEN_CHANGE) {
                    this.context.warn(watcher.getClass().getSimpleName() + ".onHistoryTokenChange is too slow, took " + millsTaken + " ms");
                }
            }
        }
    }

    private final HistoryToken previous;

    private final AppContext context;

    @Override
    public String toString() {
        return this.previous + " " + this.context;
    }
}
