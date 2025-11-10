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
import walkingkooka.net.UrlPath;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserInfoSet;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserName;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.server.SpreadsheetHttpServer;
import walkingkooka.spreadsheet.server.SpreadsheetServerLinkRelations;
import walkingkooka.spreadsheet.server.parser.SpreadsheetParserSelectorEdit;
import walkingkooka.text.CharSequences;

import java.util.Optional;

/**
 * Fetcher for {@link SpreadsheetParserSelector} end points.
 */
public final class SpreadsheetParserFetcher extends Fetcher<SpreadsheetParserFetcherWatcher> {

    public static SpreadsheetParserFetcher with(final SpreadsheetParserFetcherWatcher watcher,
                                                final AppContext context) {
        return new SpreadsheetParserFetcher(
            watcher,
            context
        );
    }

    private SpreadsheetParserFetcher(final SpreadsheetParserFetcherWatcher watcher,
                                     final AppContext context) {
        super(
            watcher,
            context
        );
    }

    // GET /api/spreadsheet/SpreadsheetId/parser/*/edit/SpreadsheetParserSelector
    public void getEdit(final SpreadsheetId id,
                        final String selector) {
        this.get(
            editUrl(
                id,
                selector
            )
        );
    }

    // /api/spreadsheet/1/parser/*/edit/SpreadsheetParserSelector
    static AbsoluteOrRelativeUrl editUrl(final SpreadsheetId id,
                                         final String selector) {
        return url(id)
            .appendPath(EDIT)
            .appendPath(
                UrlPath.parse(UrlPath.SEPARATOR.string()
                    .concat(selector)
                )
            );
    }

    private final static UrlPath EDIT = UrlPath.parse(
        "/*/" + SpreadsheetServerLinkRelations.EDIT
    );

    // GET /api/parser/*
    public void getInfoSet() {
        this.get(
            GET_INFO_SET_URL
        );
    }

    private final static AbsoluteOrRelativeUrl GET_INFO_SET_URL = Url.EMPTY_RELATIVE_URL.appendPath(SpreadsheetHttpServer.API_PARSER);

    // /api/spreadsheet/1/parser

    static RelativeUrl url(final SpreadsheetId id) {
        return SpreadsheetMetadataFetcher.url(id)
            .appendPathName(
                SpreadsheetParserName.HATEOS_RESOURCE_NAME.toUrlPathName()
            );
    }

    // Fetcher..........................................................................................................

    @Override
    public void onSuccess(final HttpMethod method,
                          final AbsoluteOrRelativeUrl url,
                          final String contentTypeName,
                          final Optional<String> body) {
        switch (CharSequences.nullToEmpty(contentTypeName).toString()) {
            case "":
                this.watcher.onEmptyResponse();
                break;
            case "SpreadsheetParserInfoSet":
                // GET http://server/api/parser
                this.watcher.onSpreadsheetParserInfoSet(
                    this.parse(
                        body,
                        SpreadsheetParserInfoSet.class
                    ) // edit
                );
                break;
            case "SpreadsheetParserSelectorEdit":
                // http://server/api/spreadsheet/1/parser/*/edit
                this.watcher.onSpreadsheetParserSelectorEdit(
                    SpreadsheetMetadataFetcher.extractSpreadsheetIdOrFail(url),
                    this.parse(
                        body,
                        SpreadsheetParserSelectorEdit.class
                    ) // edit
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quote(contentTypeName));
        }
    }

    // Logging..........................................................................................................

    @Override
    boolean isDebugEnabled() {
        return SPREADSHEET_PARSER_FETCHER;
    }
}
