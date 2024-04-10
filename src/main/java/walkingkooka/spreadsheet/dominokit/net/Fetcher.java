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
import walkingkooka.tree.json.JsonNode;

import java.util.Optional;

public interface Fetcher {

    default void delete(final AbsoluteOrRelativeUrl url) {
        this.fetch(
                HttpMethod.DELETE,
                url,
                Optional.empty()
        );
    }

    default void get(final AbsoluteOrRelativeUrl url) {
        this.fetch(
                HttpMethod.GET,
                url,
                Optional.empty()
        );
    }

    default void patch(final AbsoluteOrRelativeUrl url,
                       final String body) {
        this.fetch(
                HttpMethod.PATCH,
                url,
                Optional.of(body)
        );
    }

    default void post(final AbsoluteOrRelativeUrl url,
                      final String body) {
        this.fetch(
                HttpMethod.POST,
                url,
                Optional.of(body)
        );
    }

    default void put(final AbsoluteOrRelativeUrl url,
                     final String body) {
        this.fetch(
                HttpMethod.PUT,
                url,
                Optional.of(body)
        );
    }

    default void fetch(final HttpMethod method,
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

        DomGlobal.fetch(
                        url.value(),
                        requestInit
                ).then(response -> {
                    response.text()
                            .then(
                                    text -> {
                                        if (response.ok) {
                                            this.onSuccess(
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
                    this.onError(error);
                    return null;
                });
    }

    /**
     * Called just before a fetch begins.
     */
    void onBegin(final HttpMethod method,
                 final AbsoluteOrRelativeUrl url,
                 final Optional<String> body);

    /**
     * Success assumes a json response.
     */
    void onSuccess(final HttpMethod method,
                   final AbsoluteOrRelativeUrl url,
                   final String contentTypeName,
                   final String body);

    /**
     * Parses the JSON String into the requested type.
     */
    default <T> T parse(final String json,
                        final Class<T> type) {
        return this.context()
                .unmarshallContext()
                .unmarshall(
                        JsonNode.parse(
                                json
                        ),
                        type
                );
    }

    /**
     * Parses the JSON String into the requested type.
     */
    default String toJson(final Object value) {
        return this.context()
                .marshallContext()
                .marshall(
                        value
                ).toString();
    }

    AppContext context();

    /**
     * This method is invoked for non OK responses.
     */
    void onFailure(final HttpMethod method,
                   final AbsoluteOrRelativeUrl url,
                   final HttpStatus status,
                   final Headers headers,
                   final String body);

    void onError(final Object cause);
}
