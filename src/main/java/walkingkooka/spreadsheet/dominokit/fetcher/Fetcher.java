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
import walkingkooka.collect.map.Maps;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.UrlQueryString;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.net.http.server.hateos.HateosResourceMappings;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.log.Logging;
import walkingkooka.spreadsheet.server.SpreadsheetServerMediaTypes;
import walkingkooka.spreadsheet.server.net.SpreadsheetUrlQueryParameters;
import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.json.JsonNode;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * Base class for a variety of fetchers that target HateosResources and other end points.
 */
abstract public class Fetcher<W extends FetcherWatcher> implements Logging {

    Fetcher(final W watcher,
            final AppContext context) {
        this.watcher = Objects.requireNonNull(watcher, "watcher");
        this.context = Objects.requireNonNull(context, "context");
    }

    /**
     * Performs a DELETE to the given {@link AbsoluteOrRelativeUrl} without any body and with the header: Accept set to Json.
     */
    final void delete(final AbsoluteOrRelativeUrl url) {
        this.fetchJson(
            HttpMethod.DELETE,
            url,
            Optional.empty()
        );
    }

    /**
     * Performs a GET to the given {@link AbsoluteOrRelativeUrl} without any body and with the header: Accept set to Json.
     */
    final void get(final AbsoluteOrRelativeUrl url) {
        this.fetchJson(
            HttpMethod.GET,
            url,
            Optional.empty()
        );
    }

    /**
     * Performs a PATCH to the given {@link AbsoluteOrRelativeUrl} with the body and with the headers:
     * Content-Type and Accept set to Json.
     */
    final void patch(final AbsoluteOrRelativeUrl url,
                     final FetcherRequestBody<?> body) {
        this.fetchJson(
            HttpMethod.PATCH,
            url,
            Optional.of(body)
        );
    }

    /**
     * Performs a POST to the given {@link AbsoluteOrRelativeUrl} with the body and with the headers:
     * Content-Type and Accept set to Json.
     */
    final void post(final AbsoluteOrRelativeUrl url,
                    final FetcherRequestBody<?> body) {
        this.fetchJson(
            HttpMethod.POST,
            url,
            Optional.of(body)
        );
    }

    /**
     * Performs a PUT to the given {@link AbsoluteOrRelativeUrl} with the body and with the headers:
     * Content-Type and Accept set to Json.
     */
    final void put(final AbsoluteOrRelativeUrl url,
                   final FetcherRequestBody<?> body) {
        this.fetchJson(
            HttpMethod.PUT,
            url,
            Optional.of(body)
        );
    }

    /**
     * Performs a fetch using the given {@link HttpMethod} and url with the given body with two headers,
     * content-type and accept both set to JSON. To use other headers the {@link #fetch(HttpMethod, AbsoluteOrRelativeUrl, Map, Optional)},
     * must be used.
     */
    final void fetchJson(final HttpMethod method,
                         final AbsoluteOrRelativeUrl url,
                         final Optional<FetcherRequestBody<?>> body) {
        final Map<HttpHeaderName<?>, Object> headers = Maps.sorted();

        if (false == HttpMethod.GET.equals(method)) {
            headers.put(
                HttpHeaderName.CONTENT_TYPE,
                SpreadsheetServerMediaTypes.CONTENT_TYPE
            );
        }

        this.fetch(
            method,
            url,
            headers,//entity
            body
        );
    }

    /**
     * Performs a fetch using the provided parameters
     */
    final void fetch(final HttpMethod method,
                     final AbsoluteOrRelativeUrl url,
                     final Map<HttpHeaderName<?>, Object> headers,
                     final Optional<FetcherRequestBody<?>> body) {
        final RequestInit requestInit = RequestInit.create();
        requestInit.setMethod(method.value());

        final Headers nativeHeaders = new Headers();

        for (final Entry<HttpHeaderName<?>, Object> headerAndValues : headers.entrySet()) {
            final String headerName = headerAndValues.getKey()
                .value();
            final Object headerValue = headerAndValues.getValue();

            nativeHeaders.append(
                headerName,
                headerValue.toString()
            );
        }

        requestInit.setHeaders(nativeHeaders);

        final Runnable doFetch = () -> this.doFetch(
            method,
            url,
            headers,
            body,
            requestInit
        );

        if (body.isPresent()) {
            body.get()
                .handleFetch(
                    nativeHeaders,
                    requestInit,
                    doFetch
                );
        } else {
            doFetch.run();
        }
    }

    private void doFetch(final HttpMethod method,
                         final AbsoluteOrRelativeUrl url,
                         final Map<HttpHeaderName<?>, Object> headers,
                         final Optional<FetcherRequestBody<?>> body,
                         final RequestInit requestInit) {
        if (this.isDebugEnabled()) {
            if (body.isPresent()) {
                this.context.debug(method + " " + url, body.get());
            } else {
                this.context.debug(method + " " + url);
            }
        }

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
                                        HateosResourceMappings.X_CONTENT_TYPE_NAME.value()
                                    ),
                                    HttpStatusCode.NO_CONTENT.code() == response.status ?
                                        Optional.empty() :
                                        Optional.of(text)
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
                         final Optional<FetcherRequestBody<?>> body) {
        this.watcher.onBegin(
            method,
            url,
            body
        );
    }

    /**
     * Logs a debug level message with the given parameters and then calls #onSuccess.
     * Note if the response.status is NO_CONTENT the body will be {@link Optional#empty()}.
     */
    private void fireSuccess(final HttpMethod method,
                             final AbsoluteOrRelativeUrl url,
                             final String contentTypeName,
                             final Optional<String> body) {
        String actualBodyLength = "";
        if (body.isPresent()) {
            final String bodyText = body.get();
            if (false == CharSequences.isNullOrEmpty(bodyText)) {
                actualBodyLength = " " + bodyText.length();
            }
        }

        if(this.isDebugEnabled()) {
            this.context.debug(this.getClass().getSimpleName() + ".onSuccess " + method + " " + url + " " + contentTypeName + actualBodyLength);
        }

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
                            final Optional<String> body);

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
            body
        );
    }

    private void onError(final Object cause) {
        this.watcher.onError(
            cause
        );
    }

    final W watcher;

    /**
     * Helper that may be used to extract the message following an error response.
     * <pre>
     * java.lang.IllegalArgumentException: Expected at least 1 converter but got 0
     * 	at walkingkooka.convert.ConverterCollection.with(ConverterCollection.java:46)
     * 	at walkingkooka.convert.Converters.collection(Converters.java:132)
     * 	at walkingkooka.spreadsheet.convert.SpreadsheetConvertersConverterProvider.converter(SpreadsheetConvertersConverterProvider.java:88)
     * 	at walkingkooka.plugin.PluginSelector.evaluateValueText(PluginSelector.java:289)
     * </pre>
     */
    public static String errorMessage(final String message) {
        String error = "";

        final int colon = message.indexOf(':');
        if (-1 != colon) {
            int eol = message.length();
            final int cr = message.indexOf(LineEnding.CR.toString(), colon);
            if (-1 != cr) {
                eol = cr;
            }

            final int lf = message.indexOf(LineEnding.NL.toString(), colon);
            if (-1 != lf && cr < eol) {
                eol = lf;
            }

            error = message.substring(colon + 1, eol)
                .trim();
        }

        return error;
    }

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

    final <T> T parse(final Optional<String> json,
                      final Class<T> type) {
        return this.parse(
            json.orElse(""),
            type
        );
    }

    /**
     * Parses the JSON String into the requested type.
     */
    final FetcherRequestBody<String> requestBody(final Object value) {
        return FetcherRequestBody.string(
            this.context.marshall(
                value
            ).toString()
        );
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
     * This method is only intended to be called by {@link #fetch(HttpMethod, AbsoluteOrRelativeUrl, Map, Optional)} ,
     * during various parts of the fetch lifecycle.
     */
    public final void setWaitingRequestCount(final int waitingRequestCount) {
        this.waitingRequestCount = waitingRequestCount;
    }

    private int waitingRequestCount;

    // Logging..........................................................................................................

    abstract boolean isDebugEnabled();

    // Object..........................................................................................................

    @Override
    public final String toString() {
        return this.watcher.toString();
    }

    // helpers..........................................................................................................

    // @VisibleForTesting
    static UrlQueryString offsetAndCountQueryString(final OptionalInt offset,
                                                    final OptionalInt count) {
        UrlQueryString queryString = UrlQueryString.EMPTY;

        if (offset.isPresent()) {
            final int value = offset.getAsInt();

            if (value < 0) {
                throw new IllegalArgumentException("Invalid offset " + value + " < 0");
            }

            if (value > 0) {
                queryString = queryString.addParameter(
                    SpreadsheetUrlQueryParameters.OFFSET,
                    String.valueOf(value)
                );
            }
        }

        if (count.isPresent()) {
            final int value = count.getAsInt();

            if (value < 0) {
                throw new IllegalArgumentException("Invalid count " + value + " < 0");
            }

            if (value > 0) {
                queryString = queryString.addParameter(
                    SpreadsheetUrlQueryParameters.COUNT,
                    String.valueOf(value)
                );
            }
        }

        return queryString;
    }
}
