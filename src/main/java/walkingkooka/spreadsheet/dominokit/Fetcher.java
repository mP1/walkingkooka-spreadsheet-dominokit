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

package walkingkooka.spreadsheet.dominokit;

import elemental2.dom.DomGlobal;
import elemental2.dom.Headers;
import elemental2.dom.RequestInit;
import walkingkooka.net.Url;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContexts;

import java.math.MathContext;
import java.util.Optional;

public interface Fetcher {

    JsonNodeUnmarshallContext UNMARSHALL_CONTEXT = JsonNodeUnmarshallContexts.basic(
            ExpressionNumberKind.BIG_DECIMAL,
            MathContext.DECIMAL32
    );

    default void delete(final Url url) {
        this.fetch(
                HttpMethod.DELETE,
                url,
                Optional.empty()
        );
    }

    default void get(final Url url) {
        this.fetch(
                HttpMethod.GET,
                url,
                Optional.empty()
        );
    }

    default void patch(final Url url,
                       final String body) {
        this.fetch(
                HttpMethod.PATCH,
                url,
                Optional.of(body)
        );
    }

    default void post(final Url url,
                      final String body) {
        this.fetch(
                HttpMethod.POST,
                url,
                Optional.of(body)
        );
    }

    default void put(final Url url,
                     final String body) {
        this.fetch(
                HttpMethod.PUT,
                url,
                Optional.of(body)
        );
    }

    default void fetch(final HttpMethod method,
                       final Url url,
                       final Optional<String> body) {
        final RequestInit requestInit = RequestInit.create();
        requestInit.setMethod(method.value());

        final Headers headers = new Headers();
        headers.append(
                HttpHeaderName.ACCEPT.value(),
                MediaType.APPLICATION_JSON.value()
        );

        if (body.isPresent()) {
            headers.append(
                    HttpHeaderName.CONTENT_TYPE.value(),
                    MediaType.APPLICATION_JSON.value()
            );

            requestInit.setBody(body.get());
        }

        requestInit.setHeaders(headers);

        this.fetchLog(method + " " + url + " " + body);

        DomGlobal.fetch(
                        url.value(),
                        requestInit
                ).then(response -> {
                    response.text()
                            .then(
                                    text -> {
                                        if (response.ok) {
                                            this.fetchLog("success " + text);
                                            this.onSuccess(text);
                                        } else {
                                            final HttpStatus status = HttpStatusCode.withCode(response.status)
                                                    .setMessage(response.statusText);
                                            this.fetchLog("failure " + status + " " + text);
                                            this.onFailure(
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
     * Opportunity for sub classes to log any fetches.
     */
    void fetchLog(final String message);

    /**
     * Success assumes a json response.
     */
    void onSuccess(final String body);

    /**
     * Parses the JSON String into the requested type.
     */
    default <T> T parse(final String json,
                        final Class<T> type) {
        return UNMARSHALL_CONTEXT.unmarshall(
                JsonNode.parse(
                        json
                ),
                type
        );
    }

    /**
     * This method is invoked for non OK responses.
     */
    void onFailure(final HttpStatus status,
                   final Headers headers,
                   final String body);

    void onError(final Object cause);
}
