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

import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.compare.SpreadsheetComparator;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfoSet;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.server.SpreadsheetHttpServer;
import walkingkooka.text.CharSequences;

import java.util.Optional;

/**
 * Fetcher for {@link SpreadsheetComparator} end points.
 */
public final class SpreadsheetComparatorFetcher extends Fetcher<SpreadsheetComparatorFetcherWatcher> {

    static {
        SpreadsheetComparatorName.CASE_SENSITIVITY.toString(); // force json unmarshaller to register
    }

    // Fetcher..........................................................................................................

    public static SpreadsheetComparatorFetcher with(final SpreadsheetComparatorFetcherWatcher watcher,
                                                    final AppContext context) {
        return new SpreadsheetComparatorFetcher(
            watcher,
            context
        );
    }

    private SpreadsheetComparatorFetcher(final SpreadsheetComparatorFetcherWatcher watcher,
                                         final AppContext context) {
        super(
            watcher,
            context
        );
    }

    // GET /api/comparator/*
    public void getInfoSet() {
        this.get(URL);
    }

    private final static RelativeUrl URL = Url.EMPTY_RELATIVE_URL.appendPath(SpreadsheetHttpServer.API_COMPARATOR);

    @Override
    public void onSuccess(final HttpMethod method,
                          final AbsoluteOrRelativeUrl url,
                          final String contentTypeName,
                          final Optional<String> body) {
        final AppContext context = this.context;

        switch (CharSequences.nullToEmpty(contentTypeName).toString()) {
            case "":
                this.watcher.onEmptyResponse(context);
                break;
            case "SpreadsheetComparatorInfoSet":
                // GET http://server/api/comparator
                this.watcher.onSpreadsheetComparatorInfoSet(
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
}
