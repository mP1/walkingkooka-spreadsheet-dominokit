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

import elemental2.dom.Headers;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.watch.Watchers;

import java.util.List;
import java.util.Optional;

abstract class FetcherWatchers<W extends FetcherWatcher> implements FetcherWatcher {

    FetcherWatchers() {
        super();
    }

    /**
     * Adds a new {@link SpreadsheetMetadataFetcherWatcher} which will receive all events until removed using the returned {@link Runnable}.
     */
    public final Runnable add(final W watcher) {
        return this.addWatcher(
                watcher,
                this.watchers
        );
    }

    /**
     * Added once {@link FetcherWatcher} are fired before any long term watchers added with {@link #add(FetcherWatcher)}.
     */
    public final Runnable addOnce(final W watcher) {
        final Runnable remover = this.addWatcher(
                watcher,
                this.onceWatchers
        );
        this.onceRemovers.add(remover);
        return remover;
    }

    private Runnable addWatcher(final W watcher,
                                final Watchers<FetcherWatchersEvent<W>> watchers) {
        return watchers.add(
                (e) -> e.accept(watcher)
        );
    }

    // FetcherWatcher..................................................................................................

    @Override
    public final void onBegin(final HttpMethod method,
                              final Url url,
                              final Optional<String> body,
                              final AppContext context) {
        // cant use fire because it removes one shot watchers...
        final FetcherWatchersEvent<W> event = FetcherWatchersEvent.begin(
                method,
                url,
                body,
                context
        );
        this.onceWatchers.accept(event);
        this.watchers.accept(event);
    }

    @Override
    public final void onFailure(final AbsoluteOrRelativeUrl url,
                                final HttpStatus status,
                                final Headers headers,
                                final String body,
                                final AppContext context) {
        this.fire(
                FetcherWatchersEvent.failure(
                        url,
                        status,
                        headers,
                        body,
                        context
                )
        );
    }

    @Override
    public final void onError(final Object cause,
                              final AppContext context) {
        this.fire(
                FetcherWatchersEvent.error(
                        cause,
                        context
                )
        );
    }

    @Override
    public void onNoResponse(final AppContext context) {
        this.fire(
                FetcherWatchersEvent.noResponse(
                        context
                )
        );
    }

    final void fire(final FetcherWatchersEvent<W> event) {
        try {
            this.onceWatchers.accept(event);
            this.watchers.accept(event);
        } finally {
            this.onceRemovers.forEach(Runnable::run);
            this.onceRemovers.clear();
        }
    }
    private final Watchers<FetcherWatchersEvent<W>> watchers = Watchers.create();

    private final Watchers<FetcherWatchersEvent<W>> onceWatchers = Watchers.create();

    /**
     * Cant use Watchers#addOnce because that will remove the watcher during #onBegin
     * meaning events afterward will never be received because watcher is gone by then.
     */
    private final List<Runnable> onceRemovers = Lists.copyOnWrite();

    @Override
    public final String toString() {
        return this.watchers.toString();
    }
}
