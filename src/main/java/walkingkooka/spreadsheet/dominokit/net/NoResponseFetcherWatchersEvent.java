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

/**
 * The event payload used by {@link FetcherWatcher#onNoResponse(AppContext)}.
 */
final class NoResponseFetcherWatchersEvent<W extends FetcherWatcher> extends FetcherWatchersEvent<W> {

    static <W extends FetcherWatcher> NoResponseFetcherWatchersEvent<W> with(final AppContext context) {
        return new NoResponseFetcherWatchersEvent<>(context);
    }

    private NoResponseFetcherWatchersEvent(final AppContext context) {
        super(context);
    }

    @Override
    public void accept(final W watcher) {
        try {
            watcher.onNoResponse(
                    this.context
            );
        } catch (final Exception cause) {
            this.context.error(
                    this.getClass().getSimpleName() + ".accept exception: " + cause.getMessage(),
                    cause
            );
        }
    }

    @Override
    public String toString() {
        return this.context.toString();
    }
}
