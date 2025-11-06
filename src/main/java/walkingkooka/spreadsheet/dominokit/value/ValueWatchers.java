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

package walkingkooka.spreadsheet.dominokit.value;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.watch.Watchers;

import java.util.Objects;
import java.util.Optional;

/**
 * A collection of {@link ValueWatcher} including support for firing new events.
 */
public final class ValueWatchers<T> implements ValueWatcher<T> {

    public static <T> ValueWatchers<T> empty() {
        return new ValueWatchers<>();
    }

    private ValueWatchers() {
        super();
    }

    /**
     * Adds a new {@link SpreadsheetMetadataFetcherWatcher} which will receive all events until removed using the returned {@link Runnable}.
     */
    public Runnable add(final ValueWatcher<T> watcher) {
        return this.addWatcher(
            Objects.requireNonNull(watcher, "watcher"),
            this.watchers
        );
    }

    private Runnable addWatcher(final ValueWatcher<T> watcher,
                                final Watchers<ValueWatcherEvent<T>> watchers) {
        return watchers.add(
            (e) -> e.accept(watcher)
        );
    }

    // ValueWatcher.....................................................................................................

    @Override
    public void onValue(final Optional<T> value,
                        final AppContext context) {
        this.fire(
            ValueWatcherEvent.with(
                value,
                context
            )
        );
    }

    private void fire(final ValueWatcherEvent<T> event) {
        this.watchers.accept(event);
    }

    private final Watchers<ValueWatcherEvent<T>> watchers = Watchers.create();

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.watchers.toString();
    }
}
