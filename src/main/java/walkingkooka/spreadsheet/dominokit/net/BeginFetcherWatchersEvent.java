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

import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.dominokit.AppContext;

import java.util.Optional;

/**
 * The event payload used by {@link FetcherWatcher#onBegin(HttpMethod, Url, Optional, AppContext)}.
 */
final class BeginFetcherWatchersEvent<W extends FetcherWatcher> extends FetcherWatchersEvent<W> {

    static <W extends FetcherWatcher> BeginFetcherWatchersEvent<W> with(final HttpMethod method,
                                                                        final Url url,
                                                                        final Optional<String> body,
                                                                        final AppContext context) {
        return new BeginFetcherWatchersEvent<>(
                method,
                url,
                body,
                context
        );
    }

    private BeginFetcherWatchersEvent(final HttpMethod method,
                                      final Url url,
                                      final Optional<String> body,
                                      final AppContext context) {
        super(context);
        this.method = method;
        this.url = url;
        this.body = body;
    }

    @Override
    public void accept(final W watcher) {
        try {
            watcher.onBegin(
                    this.method,
                    this.url,
                    this.body,
                    this.context
            );
        } catch (final Exception cause) {
            this.context.error(
                    this.getClass().getSimpleName() + ".accept exception: " + cause.getMessage(),
                    cause
            );
        }
    }

    private final HttpMethod method;

    private final Url url;

    private final Optional<String> body;

    @Override
    public String toString() {
        return this.method + " " + this.url + " " + this.body + " " + this.context;
    }
}
