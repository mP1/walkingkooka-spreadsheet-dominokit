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
import walkingkooka.net.Url;
import walkingkooka.net.UrlPathName;
import walkingkooka.net.UrlQueryString;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.server.SpreadsheetHttpServer;
import walkingkooka.spreadsheet.server.currency.CurrencyCode;
import walkingkooka.spreadsheet.server.currency.CurrencyHateosResource;
import walkingkooka.spreadsheet.server.currency.CurrencyHateosResourceSet;
import walkingkooka.spreadsheet.server.net.SpreadsheetUrlPathTemplate;
import walkingkooka.spreadsheet.server.net.SpreadsheetUrlQueryParameters;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * Fetcher for {@link CurrencyHateosResource} end points.
 */
public final class CurrencyFetcher extends Fetcher<CurrencyFetcherWatcher> {

    // Fetcher..........................................................................................................

    public static CurrencyFetcher with(final CurrencyFetcherWatcher watcher,
                                       final AppContext context) {
        return new CurrencyFetcher(
            watcher,
            context
        );
    }

    private CurrencyFetcher(final CurrencyFetcherWatcher watcher,
                            final AppContext context) {
        super(
            watcher,
            context
        );
    }

    // GET /api/currency/CurrencyCode
    public void getCurrency(final CurrencyCode id) {
        Objects.requireNonNull(id, "id");

        get(
            URL.appendPathName(
                UrlPathName.with(id.toString())
            )
        );
    }

    // GET /api/currency/*
    public void getCurrencies(final int offset,
                              final int count) {
        if (offset < 0) {
            throw new IllegalArgumentException("Invalid offset " + offset + " < 0");
        }
        if (count < 0) {
            throw new IllegalArgumentException("Invalid count " + count + " < 0");
        }

        get(
            URL.appendPathName(
                UrlPathName.WILDCARD
            ).setQuery(
                UrlQueryString.EMPTY.addParameter(
                    SpreadsheetUrlQueryParameters.OFFSET,
                    String.valueOf(offset)
                ).addParameter(
                    SpreadsheetUrlQueryParameters.COUNT,
                    String.valueOf(count)
                )
            )
        );
    }

    private final static AbsoluteOrRelativeUrl URL = Url.EMPTY_RELATIVE_URL.appendPath(
        SpreadsheetHttpServer.API_CURRENCY
    );

    @Override
    public void onSuccess(final HttpMethod method,
                          final AbsoluteOrRelativeUrl url,
                          final String contentTypeName,
                          final Optional<String> body) {
        switch (CharSequences.nullToEmpty(contentTypeName).toString()) {
            case "":
                this.watcher.onEmptyResponse();
                break;
            case "CurrencyHateosResource":
                // GET http://server/api/currency/CurrencyCode
                this.watcher.onCurrencyHateosResource(
                    CURRENCY_CODE_TEMPLATE.currencyCode(
                        url.path()
                    ), // the request url
                    this.parse(
                        body,
                        CurrencyHateosResource.class
                    ) // edit
                );
                break;
            case "CurrencyHateosResourceSet":
                // GET http://server/api/currency/*
                this.watcher.onCurrencyHateosResourceSet(
                    this.parse(
                        body,
                        CurrencyHateosResourceSet.class
                    ) // edit
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quote(contentTypeName));
        }
    }

    final static SpreadsheetUrlPathTemplate CURRENCY_CODE_TEMPLATE = SpreadsheetUrlPathTemplate.parse("/api/currency/${CurrencyCode}");

    // Logging..........................................................................................................

    @Override
    boolean isDebugEnabled() {
        return CURRENCY_FETCHER;
    }
}
