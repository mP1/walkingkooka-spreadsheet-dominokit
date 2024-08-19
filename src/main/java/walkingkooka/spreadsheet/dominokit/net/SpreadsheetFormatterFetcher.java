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
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.UrlPath;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.server.SpreadsheetHttpServerLinkRelations;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterHateosResourceMappings;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorEdit;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;

import java.util.Objects;
import java.util.Optional;

/**
 * Fetcher for {@link SpreadsheetFormatterSelector} end points.
 */
public final class SpreadsheetFormatterFetcher implements Fetcher {

    static {
        SpreadsheetFormatterSelector.DEFAULT_TEXT_FORMAT.toString(); // force json unmarshaller to register

        try {
            SpreadsheetFormatterSelectorEdit.parse(
                    null,
                    null
            );
        } catch (final NullPointerException ignore) {
            // nop
        }
    }

    public static SpreadsheetFormatterFetcher with(final SpreadsheetFormatterFetcherWatcher watcher,
                                                   final AppContext context) {
        Objects.requireNonNull(watcher, "watcher");
        Objects.requireNonNull(context, "context");

        return new SpreadsheetFormatterFetcher(
                watcher,
                context
        );
    }

    private SpreadsheetFormatterFetcher(final SpreadsheetFormatterFetcherWatcher watcher,
                                        final AppContext context) {
        this.watcher = watcher;
        this.context = context;
    }

    // /api/spreadsheet/SpreadsheetId/formatter/*/edit
    public void edit(final SpreadsheetId id,
                     final String selector) {
        this.post(
                url(
                        id,
                        EDIT
                ),
                JsonNode.string(selector)
                        .toString()
        );
    }

    // /api/spreadsheet/SpreadsheetId/formatter/*/menu
    public void menu(final SpreadsheetId id) {
        this.get(
                url(
                        id,
                        MENU
                )
        );
    }

    // /api/spreadsheet/1/formatter/*/edit

    static RelativeUrl url(final SpreadsheetId id,
                           final UrlPath path) {
        return SpreadsheetMetadataFetcher.url(id)
                .appendPath(
                        FORMATTER
                ).appendPath(path);
    }

    private final static UrlPath FORMATTER = UrlPath.parse(
            SpreadsheetFormatterHateosResourceMappings.FORMATTER.value()
    );

    private final static UrlPath EDIT = UrlPath.parse(
            "/*/" + SpreadsheetHttpServerLinkRelations.EDIT
    );

    private final static UrlPath MENU = UrlPath.parse(
            "/*/" + SpreadsheetHttpServerLinkRelations.MENU
    );

    // Fetcher..........................................................................................................

    @Override
    public void onBegin(final HttpMethod method,
                        final AbsoluteOrRelativeUrl url,
                        final Optional<String> body) {
        this.watcher.onBegin(
                method,
                url,
                body,
                this.context
        );
    }

    @Override
    public void onSuccess(final HttpMethod method,
                          final AbsoluteOrRelativeUrl url,
                          final String contentTypeName,
                          final String body) {
        final AppContext context = this.context;

        this.logSuccess(
                method,
                url,
                contentTypeName,
                body,
                context
        );

        switch (CharSequences.nullToEmpty(contentTypeName).toString()) {
            case "":
                this.watcher.onEmptyResponse(context);
            case "SpreadsheetFormatterSelectorEdit":
                // http://server/api/spreadsheet/1/formatter/*/edit
                this.watcher.onSpreadsheetFormatterSelectorEdit(
                        SpreadsheetMetadataFetcher.extractSpreadsheetId(url)
                                .get(), // the request url
                        this.parse(
                                body,
                                SpreadsheetFormatterSelectorEdit.class
                        ), // edit
                        context
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quote(contentTypeName));
        }
    }

    @Override
    public void onFailure(final HttpMethod method,
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

    @Override
    public void onError(final Object cause) {
        this.watcher.onError(
                cause,
                this.context
        );
    }

    private final SpreadsheetFormatterFetcherWatcher watcher;

    @Override
    public int waitingRequestCount() {
        return this.waitingRequestCount;
    }

    @Override
    public void setWaitingRequestCount(final int waitingRequestCount) {
        this.waitingRequestCount = waitingRequestCount;
    }

    private int waitingRequestCount;

    @Override
    public AppContext context() {
        return this.context;
    }

    private final AppContext context;

    // Object..........................................................................................................

    @Override
    public String toString() {
        return this.watcher.toString();
    }
}
