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
import walkingkooka.net.UrlPath;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterInfoSet;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.server.SpreadsheetServerLinkRelations;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorEdit;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorMenu;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorMenuList;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;

import java.util.Objects;

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
        Objects.requireNonNull(watcher, "watcher");
        Objects.requireNonNull(context, "context");

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
    public void edit(final SpreadsheetId id,
                     final String selector) {
        this.post(
                formatter(id)
                        .appendPath(EDIT),
                JsonNode.string(selector)
                        .toString()
        );
    }

    private final static UrlPath EDIT = UrlPath.parse(
            "/*/" + SpreadsheetServerLinkRelations.EDIT
    );

    // GET /api/spreadsheet/SpreadsheetId/formatter/*
    public void infoSet(final SpreadsheetId id) {
        this.get(
                formatter(id)
        );
    }

    // GET /api/spreadsheet/SpreadsheetId/formatter/*/menu
    public void menu(final SpreadsheetId id) {
        this.get(
                formatter(id)
                        .appendPath(MENU)
        );
    }

    private final static UrlPath MENU = UrlPath.parse(
            "/*/" + SpreadsheetServerLinkRelations.MENU
    );

    // api/spreadsheet/SpreadsheetId/formatter

    static RelativeUrl formatter(final SpreadsheetId id) {
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
                          final String body) {
        final AppContext context = this.context;

        switch (CharSequences.nullToEmpty(contentTypeName).toString()) {
            case "":
                this.watcher.onEmptyResponse(context);
                break;
            case "SpreadsheetFormatterInfoSet":
                // GET http://server/api/spreadsheet/1/formatter
                this.watcher.onSpreadsheetFormatterInfoSet(
                        SpreadsheetMetadataFetcher.extractSpreadsheetId(url)
                                .get(), // the request formatter
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
                        SpreadsheetMetadataFetcher.extractSpreadsheetId(url)
                                .get(), // the request formatter
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
                        SpreadsheetMetadataFetcher.extractSpreadsheetId(url)
                                .get(), // the request formatter
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
