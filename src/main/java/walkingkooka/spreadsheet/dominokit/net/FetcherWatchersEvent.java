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
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.dominokit.AppContext;

import java.util.Optional;
import java.util.function.Consumer;

abstract class FetcherWatchersEvent<W extends FetcherWatcher> implements Consumer<W> {

    static <W extends FetcherWatcher> BeginFetcherWatchersEvent<W> begin(final HttpMethod method,
                                                                         final Url url,
                                                                         final Optional<String> body,
                                                                         final AppContext context) {
        return BeginFetcherWatchersEvent.with(
                method,
                url,
                body,
                context
        );
    }

    static <W extends FetcherWatcher> ErrorFetcherWatchersEvent<W> error(final Object cause,
                                                                         final AppContext context) {
        return ErrorFetcherWatchersEvent.with(cause, context);
    }

    static <W extends FetcherWatcher> FailureFetcherWatchersEvent<W> failure(final HttpStatus status,
                                                                             final Headers headers,
                                                                             final String body,
                                                                             final AppContext context) {
        return FailureFetcherWatchersEvent.with(
                status,
                headers,
                body,
                context
        );
    }

    FetcherWatchersEvent(final AppContext context) {
        this.context = context;
    }

    final AppContext context;

    @Override
    public abstract String toString();
}
