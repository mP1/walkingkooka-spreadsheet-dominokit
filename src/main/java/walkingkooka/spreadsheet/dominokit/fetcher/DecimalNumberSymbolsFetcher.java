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
import walkingkooka.net.UrlPath;
import walkingkooka.net.UrlPathName;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.server.SpreadsheetHttpServer;
import walkingkooka.spreadsheet.server.decimalnumbersymbols.DecimalNumberSymbolsHateosResource;
import walkingkooka.spreadsheet.server.decimalnumbersymbols.DecimalNumberSymbolsHateosResourceSet;
import walkingkooka.spreadsheet.server.locale.LocaleTag;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * Fetcher for {@link DecimalNumberSymbolsHateosResource} end points.
 */
public final class DecimalNumberSymbolsFetcher extends Fetcher<DecimalNumberSymbolsFetcherWatcher> {

    static {
        DecimalNumberSymbolsHateosResourceSet.EMPTY.toString(); // force json unmarshaller to register
    }

    // Fetcher..........................................................................................................

    public static DecimalNumberSymbolsFetcher with(final DecimalNumberSymbolsFetcherWatcher watcher,
                                                   final AppContext context) {
        Objects.requireNonNull(watcher, "watcher");
        Objects.requireNonNull(context, "context");

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

    // GET /api/decimalNumberSymbols/LocaleTag
    public void getDecimalNumberSymbols(final LocaleTag id) {
        Objects.requireNonNull(id, "id");

        get(
            Url.EMPTY_RELATIVE_URL.appendPath(
                SpreadsheetHttpServer.API_LOCALE.append(
                    UrlPathName.with(id.toString())
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
                this.watcher.onEmptyResponse(context);
                break;
            case "DecimalNumberSymbolsHateosResource":
                // GET http://server/api/locale/LocaleTagId
                this.watcher.onDecimalNumberSymbolsHateosResource(
                    parseLocaleTag(url.path()), // the request url
                    this.parse(
                        body.orElse(""),
                        DecimalNumberSymbolsHateosResource.class
                    ), // edit
                    context
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quote(contentTypeName));
        }
    }

    static LocaleTag parseLocaleTag(final UrlPath path) {
        return LocaleTag.parse(
            path.namesList()
                .get(3)
                .text()
        );
    }
}
