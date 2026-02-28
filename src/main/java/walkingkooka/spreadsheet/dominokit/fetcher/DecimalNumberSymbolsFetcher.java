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
import walkingkooka.spreadsheet.server.decimalnumbersymbols.DecimalNumberSymbolsHateosResource;
import walkingkooka.spreadsheet.server.decimalnumbersymbols.DecimalNumberSymbolsHateosResourceSet;
import walkingkooka.spreadsheet.server.locale.LocaleLanguageTag;
import walkingkooka.spreadsheet.server.net.SpreadsheetUrlPathTemplate;
import walkingkooka.template.TemplateValueName;
import walkingkooka.template.url.UrlPathTemplate;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * Fetcher for {@link DecimalNumberSymbolsHateosResource} end points.
 */
public final class DecimalNumberSymbolsFetcher extends Fetcher<DecimalNumberSymbolsFetcherWatcher> {

    // Fetcher..........................................................................................................

    public static DecimalNumberSymbolsFetcher with(final DecimalNumberSymbolsFetcherWatcher watcher,
                                                   final AppContext context) {
        return new DecimalNumberSymbolsFetcher(
            watcher,
            context
        );
    }

    private DecimalNumberSymbolsFetcher(final DecimalNumberSymbolsFetcherWatcher watcher,
                                        final AppContext context) {
        super(
            watcher,
            context
        );
    }

    // GET /api/decimalNumberSymbols/LocaleLanguageTag
    public void getDecimalNumberSymbols(final LocaleLanguageTag id) {
        Objects.requireNonNull(id, "id");

        get(
            URL.appendPathName(
                UrlPathName.with(id.toString())
            )
        );
    }

    private final static AbsoluteOrRelativeUrl URL = Url.EMPTY_RELATIVE_URL.appendPath(
        SpreadsheetHttpServer.API_DECIMAL_NUMBER_SYMBOLS
    );

    // GET /api/decimalNumberSymbols/*/localeStartsWith/startsWith
    public void getDecimalNumberSymbolsLocaleStartsWith(final String startsWith) {
        Objects.requireNonNull(startsWith, "startsWith");

        get(
            decimalNumberSymbolsLocaleStartsWithUrl(startsWith)
        );
    }

    static AbsoluteOrRelativeUrl decimalNumberSymbolsLocaleStartsWithUrl(final String startsWith) {
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
        switch (CharSequences.nullToEmpty(contentTypeName).toString()) {
            case "":
                this.watcher.onEmptyResponse();
                break;
            case "DecimalNumberSymbolsHateosResource":
                // GET http://server/api/locale/LocaleLanguageTagId
                this.watcher.onDecimalNumberSymbolsHateosResource(
                    LOCALE_TAG_TEMPLATE.localeTag(
                        url.path()
                    ),
                    this.parse(
                        body,
                        DecimalNumberSymbolsHateosResource.class
                    ) // edit
                );
                break;
            case "DecimalNumberSymbolsHateosResourceSet":
                // GET http://server/api/decimalNumberSymbols/*/localeStartsWith/STARTSWITH
                this.watcher.onDecimalNumberSymbolsHateosResourceSet(
                    extractLocaleStartsWith(url.path()), // the request url
                    this.parse(
                        body,
                        DecimalNumberSymbolsHateosResourceSet.class
                    )
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quote(contentTypeName));
        }
    }

    final static SpreadsheetUrlPathTemplate LOCALE_TAG_TEMPLATE = SpreadsheetUrlPathTemplate.parse("/api/decimalNumberSymbols/${LocaleLanguageTag}");

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

    private final static UrlPathTemplate STARTS_WITH_PATH_TEMPLATE = UrlPathTemplate.parse("/api/decimalNumberSymbols/*/localeStartsWith/${startsWith}");

    private final static TemplateValueName STARTS_WITH = TemplateValueName.with("startsWith");

    // Logging..........................................................................................................

    @Override
    boolean isDebugEnabled() {
        return DECIMAL_NUMBER_SYMBOLS_FETCHER;
    }
}
