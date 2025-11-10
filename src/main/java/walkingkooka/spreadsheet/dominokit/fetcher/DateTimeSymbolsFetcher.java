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

import walkingkooka.collect.map.Maps;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlPath;
import walkingkooka.net.UrlPathName;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.server.SpreadsheetHttpServer;
import walkingkooka.spreadsheet.server.datetimesymbols.DateTimeSymbolsHateosResource;
import walkingkooka.spreadsheet.server.datetimesymbols.DateTimeSymbolsHateosResourceSet;
import walkingkooka.spreadsheet.server.locale.LocaleTag;
import walkingkooka.spreadsheet.server.url.SpreadsheetUrlPathTemplate;
import walkingkooka.template.TemplateValueName;
import walkingkooka.template.url.UrlPathTemplate;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * Fetcher for {@link DateTimeSymbolsHateosResource} end points.
 */
public final class DateTimeSymbolsFetcher extends Fetcher<DateTimeSymbolsFetcherWatcher> {

    // Fetcher..........................................................................................................

    public static DateTimeSymbolsFetcher with(final DateTimeSymbolsFetcherWatcher watcher,
                                              final AppContext context) {
        return new DateTimeSymbolsFetcher(
            watcher,
            context
        );
    }

    private DateTimeSymbolsFetcher(final DateTimeSymbolsFetcherWatcher watcher,
                                   final AppContext context) {
        super(
            watcher,
            context
        );
    }

    // GET /api/dateTimeSymbols/LocaleTag
    public void getDateTimeSymbols(final LocaleTag id) {
        Objects.requireNonNull(id, "id");

        get(
            URL.appendPathName(
                UrlPathName.with(id.toString())
            )
        );
    }

    private final static AbsoluteOrRelativeUrl URL = Url.EMPTY_RELATIVE_URL.appendPath(
        SpreadsheetHttpServer.API_DATE_TIME_SYMBOLS
    );

    // GET /api/dateTimeSymbols/*/localeStartsWith/startsWith
    public void getDateTimeSymbolsLocaleStartsWith(final String startsWith) {
        Objects.requireNonNull(startsWith, "startsWith");

        get(
            dateTimeSymbolsLocaleStartsWithUrl(startsWith)
        );
    }

    static AbsoluteOrRelativeUrl dateTimeSymbolsLocaleStartsWithUrl(final String startsWith) {
        return Url.EMPTY_RELATIVE_URL.appendPath(
            STARTS_WITH_PATH_TEMPLATE.renderPathWithMap(
                Maps.of(
                    STARTS_WITH,
                    startsWith
                )
            )
        );
    }

    @Override
    public void onSuccess(final HttpMethod method,
                          final AbsoluteOrRelativeUrl url,
                          final String contentTypeName,
                          final Optional<String> body) {
        final AppContext context = this.context;

        switch (CharSequences.nullToEmpty(contentTypeName).toString()) {
            case "":
                this.watcher.onEmptyResponse();
                break;
            case "DateTimeSymbolsHateosResource":
                // GET http://server/api/dateTimeSymbols/LocaleTagId
                this.watcher.onDateTimeSymbolsHateosResource(
                    LOCALE_TAG_TEMPLATE.localeTag(
                        url.path()
                    ),
                    this.parse(
                        body,
                        DateTimeSymbolsHateosResource.class
                    ) // edit
                );
                break;
            case "DateTimeSymbolsHateosResourceSet":
                // GET http://server/api/dateTimeSymbols/*/localeStartsWith/STARTSWITH
                this.watcher.onDateTimeSymbolsHateosResourceSet(
                    extractLocaleStartsWith(url.path()), // the request url
                    this.parse(
                        body,
                        DateTimeSymbolsHateosResourceSet.class
                    )
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quote(contentTypeName));
        }
    }

    // /api/dateTimeSymbols/${LocaleTag}}
    final static SpreadsheetUrlPathTemplate LOCALE_TAG_TEMPLATE = SpreadsheetUrlPathTemplate.parse("/api/dateTimeSymbols/${LocaleTag}");

    // /api/dateTimeSymbols/*/localeStartsWith/STARTSWITH
    static String extractLocaleStartsWith(final UrlPath path) {
        return STARTS_WITH_PATH_TEMPLATE.tryPrepareValues(path)
            .flatMap(
                v -> v.get(
                    STARTS_WITH,
                    (s) -> s.startsWith(UrlPath.SEPARATOR.string()) ?
                        s.substring(UrlPath.SEPARATOR.string().length()) :
                        s
                )
            ).orElse("");
    }

    private final static UrlPathTemplate STARTS_WITH_PATH_TEMPLATE = UrlPathTemplate.parse("/api/dateTimeSymbols/*/localeStartsWith/${startsWith}");

    private final static TemplateValueName STARTS_WITH = TemplateValueName.with("startsWith");

    // Logging..........................................................................................................

    @Override
    boolean isDebugEnabled() {
        return DATE_TIME_SYMBOLS_FETCHER;
    }
}
