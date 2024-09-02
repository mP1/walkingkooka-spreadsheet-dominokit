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
import walkingkooka.spreadsheet.compare.SpreadsheetComparator;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfoSet;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.server.comparator.SpreadsheetComparatorHateosResourceMappings;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * Fetcher for {@link SpreadsheetComparator} end points.
 */
public final class SpreadsheetComparatorFetcher implements Fetcher {

    private final static UrlPath COMPARATOR = UrlPath.parse(
            SpreadsheetComparatorHateosResourceMappings.COMPARATOR.value()
    );

    static {
        SpreadsheetComparatorName.CASE_SENSITIVITY.toString(); // force json unmarshaller to register
    }

    private final SpreadsheetComparatorFetcherWatcher watcher;
    private final AppContext context;

    // /api/spreadsheet/1/comparator
    private int waitingRequestCount;

    private SpreadsheetComparatorFetcher(final SpreadsheetComparatorFetcherWatcher watcher,
                                         final AppContext context) {
        this.watcher = watcher;
        this.context = context;
    }

    // Fetcher..........................................................................................................

    public static SpreadsheetComparatorFetcher with(final SpreadsheetComparatorFetcherWatcher watcher,
                                                    final AppContext context) {
        Objects.requireNonNull(watcher, "watcher");
        Objects.requireNonNull(context, "context");

        return new SpreadsheetComparatorFetcher(
                watcher,
                context
        );
    }

    static RelativeUrl comparator(final SpreadsheetId id) {
        return SpreadsheetMetadataFetcher.url(id)
                .appendPath(COMPARATOR);
    }

    // GET /api/spreadsheet/SpreadsheetId/comparator/*
    public void infoSet(final SpreadsheetId id) {
        this.get(
                comparator(id)
        );
    }

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
                break;
            case "SpreadsheetComparatorInfoSet":
                // GET http://server/api/spreadsheet/1/comparator
                this.watcher.onSpreadsheetComparatorInfoSet(
                        SpreadsheetMetadataFetcher.extractSpreadsheetId(url)
                                .get(), // the request url
                        this.parse(
                                body,
                                SpreadsheetComparatorInfoSet.class
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

    @Override
    public int waitingRequestCount() {
        return this.waitingRequestCount;
    }

    @Override
    public void setWaitingRequestCount(final int waitingRequestCount) {
        this.waitingRequestCount = waitingRequestCount;
    }

    @Override
    public AppContext context() {
        return this.context;
    }

    // Object..........................................................................................................

    @Override
    public String toString() {
        return this.watcher.toString();
    }
}
