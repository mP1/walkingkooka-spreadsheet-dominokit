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
import walkingkooka.spreadsheet.server.SpreadsheetUrlQueryParameters;
import walkingkooka.spreadsheet.server.locale.LocaleHateosResource;
import walkingkooka.spreadsheet.server.locale.LocaleHateosResourceSet;
import walkingkooka.spreadsheet.server.locale.LocaleTag;
import walkingkooka.spreadsheet.server.url.SpreadsheetUrlPathTemplate;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * Fetcher for {@link LocaleHateosResource} end points.
 */
public final class LocaleFetcher extends Fetcher<LocaleFetcherWatcher> {

    // Fetcher..........................................................................................................

    public static LocaleFetcher with(final LocaleFetcherWatcher watcher,
                                     final AppContext context) {
        return new LocaleFetcher(
            watcher,
            context
        );
    }

    private LocaleFetcher(final LocaleFetcherWatcher watcher,
                          final AppContext context) {
        super(
            watcher,
            context
        );
    }

    // GET /api/locale/LocaleTag
    public void getLocale(final LocaleTag id) {
        Objects.requireNonNull(id, "id");

        get(
            URL.appendPathName(
                UrlPathName.with(id.toString())
            )
        );
    }

    // GET /api/locale/*
    public void getLocales(final int offset,
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
        SpreadsheetHttpServer.API_LOCALE
    );

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
            case "LocaleHateosResource":
                // GET http://server/api/locale/LocaleTagId
                this.watcher.onLocaleHateosResource(
                    LOCALE_TAG_TEMPLATE.localeTag(
                        url.path()
                    ), // the request url
                    this.parse(
                        body,
                        LocaleHateosResource.class
                    ) // edit
                );
                break;
            case "LocaleHateosResourceSet":
                // GET http://server/api/locale/*
                this.watcher.onLocaleHateosResourceSet(
                    this.parse(
                        body,
                        LocaleHateosResourceSet.class
                    ) // edit
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quote(contentTypeName));
        }
    }

    final static SpreadsheetUrlPathTemplate LOCALE_TAG_TEMPLATE = SpreadsheetUrlPathTemplate.parse("/api/locale/${LocaleTag}");

    // Logging..........................................................................................................

    @Override
    boolean isDebugEnabled() {
        return LOCALE_FETCHER;
    }
}
