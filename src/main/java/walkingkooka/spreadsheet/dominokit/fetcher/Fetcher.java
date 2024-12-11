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

package walkingkooka.spreadsheet.dominokit.fetcher;

import elemental2.dom.DomGlobal;
import elemental2.dom.Headers;
import elemental2.dom.RequestInit;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.net.http.server.hateos.HateosResourceMapping;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;

import java.util.Optional;

abstract public class Fetcher<W extends FetcherWatcher> {

    Fetcher(final W watcher,
            final AppContext context) {
        this.watcher = watcher;
        this.context = context;
    }

    final void delete(final AbsoluteOrRelativeUrl url) {
        this.fetch(
                HttpMethod.DELETE,
                url,
                Optional.empty()
        );
    }

    final void get(final AbsoluteOrRelativeUrl url) {
        this.fetch(
                HttpMethod.GET,
                url,
                Optional.empty()
        );
    }

    final void patch(final AbsoluteOrRelativeUrl url,
                     final String body) {
        this.fetch(
                HttpMethod.PATCH,
                url,
                Optional.of(body)
        );
    }

    final void post(final AbsoluteOrRelativeUrl url,
                    final String body) {
        this.fetch(
                HttpMethod.POST,
                url,
                Optional.of(body)
        );
    }

    final void put(final AbsoluteOrRelativeUrl url,
                   final String body) {
        this.fetch(
                HttpMethod.PUT,
                url,
                Optional.of(body)
        );
    }

    final void fetch(final HttpMethod method,
                     final AbsoluteOrRelativeUrl url,
                     final Optional<String> body) {
        FetcherFetch.fetch(
                method,
                url,
                body,
                this
        );
    }

    final void nativeFetch(final HttpMethod method,
                           final AbsoluteOrRelativeUrl url,
                           final Optional<String> body) {
        final RequestInit requestInit = RequestInit.create();
        requestInit.setMethod(method.value());

        final Headers headers = new Headers();
        headers.append(
                HttpHeaderName.ACCEPT.value(),
                MediaType.APPLICATION_JSON.value()
        );

        // always send content-type: application/json except for GETs
        if (false == HttpMethod.GET.equals(method)) {
            headers.append(
                    HttpHeaderName.CONTENT_TYPE.value(),
                    MediaType.APPLICATION_JSON.value()
            );

            if (body.isPresent()) {
                requestInit.setBody(body.get());
            }
        }

        requestInit.setHeaders(headers);

        this.onBegin(
                method,
                url,
                body
        );

        this.setWaitingRequestCount(this.waitingRequestCount() + 1);

        DomGlobal.fetch(
                        url.value(),
                        requestInit
                ).then(response -> {
                    response.text()
                            .then(
                                    text -> {
                                        this.setWaitingRequestCount(this.waitingRequestCount() - 1);

                                        if (response.ok) {
                                            this.fireSuccess(
                                                    method,
                                                    url,
                                                    response.headers.get(
                                                            HateosResourceMapping.X_CONTENT_TYPE_NAME.value()
                                                    ),
                                                    text
                                            );
                                        } else {
                                            final HttpStatus status = HttpStatusCode.withCode(response.status)
                                                    .setMessage(response.statusText);
                                            this.onFailure(
                                                    method,
                                                    url,
                                                    status,
                                                    response.headers,
                                                    text
                                            );
                                        }
                                        return null;
                                    }
                            );

                    return null;
                })
                .catch_(error -> {
                    this.setWaitingRequestCount(this.waitingRequestCount() - 1);

                    this.onError(error);
                    return null;
                });
    }

    /**
     * Called just before a fetch begins.
     */
    private void onBegin(final HttpMethod method,
                         final AbsoluteOrRelativeUrl url,
                         final Optional<String> body) {
        this.watcher.onBegin(
                method,
                url,
                body,
                this.context
        );
    }

    /**
     * Logs a debug level message with the given parameters and then calls #onSuccess.
     */
    private void fireSuccess(final HttpMethod method,
                             final AbsoluteOrRelativeUrl url,
                             final String contentTypeName,
                             final String body) {
        String actualBodyLength = "";
        if (false == CharSequences.isNullOrEmpty(body)) {
            actualBodyLength = " " + body.length();
        }

        this.context.debug(this.getClass().getSimpleName() + ".onSuccess " + method + " " + url + " " + contentTypeName + actualBodyLength);

        this.onSuccess(
                method,
                url,
                contentTypeName,
                body
        );
    }

    /**
     * Success assumes a json response.
     */
    abstract void onSuccess(final HttpMethod method,
                            final AbsoluteOrRelativeUrl url,
                            final String contentTypeName,
                            final String body);

    /**
     * This method is invoked for non 2xx responses.
     */
    private void onFailure(final HttpMethod method,
                           final AbsoluteOrRelativeUrl url,
                           final HttpStatus status,
                           final Headers headers,
                           final String body) {
        this.watcher.onFailure(
                method,
                url,
                status,
                headers,
                body,
                this.context
        );
    }

    private void onError(final Object cause) {
        this.watcher.onError(
                cause,
                this.context
        );
    }

    final W watcher;

    /**
     * Parses the JSON String into the requested type.
     */
    final <T> T parse(final String json,
                      final Class<T> type) {
        return this.context.unmarshall(
                JsonNode.parse(
                        json
                ),
                type
        );
    }

    /**
     * Parses the JSON String into the requested type.
     */
    final String toJson(final Object value) {
        return this.context.marshall(
                value
        ).toString();
    }

    /**
     * {@link AppContext} used by final methods to retrieve marshall/unmarshall and log.
     */
    final AppContext context;

    /**
     * Returns the number of outstanding or inflight requests.
     */
    public final int waitingRequestCount() {
        return this.waitingRequestCount;
    }


    /**
     * This method is only intended to be called by {@link #fetch(HttpMethod, AbsoluteOrRelativeUrl, Optional)},
     * during various parts of the fetch lifecycle.
     */
    public final void setWaitingRequestCount(final int waitingRequestCount) {
        this.waitingRequestCount = waitingRequestCount;
    }

    private int waitingRequestCount;

    // Object..........................................................................................................

    @Override
    public final String toString() {
        return this.watcher.toString();
    }
}
