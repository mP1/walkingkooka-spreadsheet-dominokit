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

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.watch.Watchers;

public final class HistoryTokenWatchers implements HistoryTokenWatcher {

    public static HistoryTokenWatchers empty() {
        return new HistoryTokenWatchers();
    }

    public Runnable add(final HistoryTokenWatcher watcher) {
        return this.watchers.add(
                (e) -> e.accept(watcher)
        );
    }

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        this.watchers.accept(
                HistoryTokenWatchersEvent.with(
                        previous,
                        context
                )
        );
    }

    private final Watchers<HistoryTokenWatchersEvent> watchers = Watchers.create();

    @Override
    public String toString() {
        return this.watchers.toString();
    }
}
