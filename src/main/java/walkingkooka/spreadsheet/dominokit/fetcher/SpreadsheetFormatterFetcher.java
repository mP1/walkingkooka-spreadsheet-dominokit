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

import walkingkooka.collect.list.Lists;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlPath;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterInfoSet;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.server.SpreadsheetHttpServer;
import walkingkooka.spreadsheet.server.SpreadsheetServerLinkRelations;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorEdit;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorMenu;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorMenuList;
import walkingkooka.text.CharSequences;

import java.util.Optional;

/**
 * Fetcher for {@link SpreadsheetFormatterSelector} end points.
 */
public final class SpreadsheetFormatterFetcher extends Fetcher<SpreadsheetFormatterFetcherWatcher> {

    static {
        try {
            SpreadsheetFormatterSelectorEdit.parse(
                null,
                null
            );
        } catch (final NullPointerException ignore) {
            // nop
        }

        // force json unmarshaller to register
        SpreadsheetFormatterSelectorMenuList.with(
            Lists.of(
                SpreadsheetFormatterSelectorMenu.with(
                    "Label",
                    SpreadsheetFormatterSelector.DEFAULT_TEXT_FORMAT
                )
            )
        ); // force JSON registry
    }

    public static SpreadsheetFormatterFetcher with(final SpreadsheetFormatterFetcherWatcher watcher,
                                                   final AppContext context) {
        return new SpreadsheetFormatterFetcher(
            watcher,
            context
        );
    }

    private SpreadsheetFormatterFetcher(final SpreadsheetFormatterFetcherWatcher watcher,
                                        final AppContext context) {
        super(
            watcher,
            context
        );
    }

    // GET /api/spreadsheet/SpreadsheetId/formatter/*/edit
    public void getEdit(final SpreadsheetId id,
                         final String selector) {
        this.get(
            editUrl(
                id,
                selector
            )
        );
    }

    static AbsoluteOrRelativeUrl editUrl(final SpreadsheetId id,
                                         final String selector) {
        return url(id)
            .appendPath(EDIT)
            .appendPath(
                UrlPath.parse(
                    UrlPath.SEPARATOR.string() + selector
                )
            );
    }

    private final static UrlPath EDIT = UrlPath.parse(
        "/*/" + SpreadsheetServerLinkRelations.EDIT
    );

    // GET /api/formatter/*
    public void getInfoSet() {
        this.get(
            GET_INFO_SET
        );
    }

    private final static AbsoluteOrRelativeUrl GET_INFO_SET = Url.EMPTY_RELATIVE_URL.appendPath(SpreadsheetHttpServer.API_FORMATTER);

    // GET /api/spreadsheet/SpreadsheetId/formatter/*/menu
    public void getMenu(final SpreadsheetId id) {
        this.get(
            url(id)
                .appendPath(MENU)
        );
    }

    private final static UrlPath MENU = UrlPath.parse(
        "/*/" + SpreadsheetServerLinkRelations.MENU
    );

    // api/spreadsheet/SpreadsheetId/formatter

    static RelativeUrl url(final SpreadsheetId id) {
        return SpreadsheetMetadataFetcher.url(id)
            .appendPathName(
                SpreadsheetFormatterName.HATEOS_RESOURCE_NAME.toUrlPathName()
            );
    }

    // Fetcher..........................................................................................................

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
            case "SpreadsheetFormatterInfoSet":
                // GET http://server/api/spreadsheet/1/formatter
                this.watcher.onSpreadsheetFormatterInfoSet(
                    this.parse(
                        body,
                        SpreadsheetFormatterInfoSet.class
                    ), // edit
                    context
                );
                break;
            case "SpreadsheetFormatterSelectorEdit":
                // http://server/api/spreadsheet/1/formatter/*/edit
                this.watcher.onSpreadsheetFormatterSelectorEdit(
                    SpreadsheetMetadataFetcher.extractSpreadsheetIdOrFail(url),
                    this.parse(
                        body,
                        SpreadsheetFormatterSelectorEdit.class
                    ), // edit
                    context
                );
                break;
            case "SpreadsheetFormatterSelectorMenuList":
                // http://server/api/spreadsheet/1/formatter/*/menu
                this.watcher.onSpreadsheetFormatterSelectorMenuList(
                    SpreadsheetMetadataFetcher.extractSpreadsheetIdOrFail(url),
                    this.parse(
                        body,
                        SpreadsheetFormatterSelectorMenuList.class
                    ), // menu
                    context
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quote(contentTypeName));
        }
    }
}
