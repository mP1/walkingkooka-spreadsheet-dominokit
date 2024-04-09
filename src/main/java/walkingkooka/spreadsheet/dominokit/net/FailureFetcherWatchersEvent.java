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
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.dominokit.AppContext;

/**
 * The event payload used by {@link FetcherWatcher#onFailure(HttpStatus, Headers, String, AppContext)}.
 */
final class FailureFetcherWatchersEvent<W extends FetcherWatcher> extends FetcherWatchersEvent<W> {

    static <W extends FetcherWatcher> FailureFetcherWatchersEvent<W> with(final HttpMethod method,
                                                                          final AbsoluteOrRelativeUrl url,
                                                                          final HttpStatus status,
                                                                          final Headers headers,
                                                                          final String body,
                                                                          final AppContext context) {
        return new FailureFetcherWatchersEvent<>(
                method,
                url,
                status,
                headers,
                body,
                context
        );
    }

    private FailureFetcherWatchersEvent(final HttpMethod method,
                                        final AbsoluteOrRelativeUrl url,
                                        final HttpStatus status,
                                        final Headers headers,
                                        final String body,
                                        final AppContext context) {
        super(context);

        this.method = method;
        this.url = url;
        this.status = status;
        this.headers =headers;
        this.body = body;
    }

    @Override
    public void accept(final W watcher) {
        try {
            watcher.onFailure(
                    this.method,
                    this.url,
                    this.status,
                    this.headers,
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

    private final AbsoluteOrRelativeUrl url;

    private final HttpStatus status;

    private final Headers headers;

    private final String body;

    @Override
    public String toString() {
        return this.method + " " + this.url + " " + this.status + " " + this.headers + " " + this.body + " " + this.context;
    }
}
