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
import walkingkooka.net.UrlPathName;
import walkingkooka.net.UrlQueryString;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.plugin.PluginName;
import walkingkooka.plugin.PluginNameSet;
import walkingkooka.plugin.store.Plugin;
import walkingkooka.plugin.store.PluginSet;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.server.SpreadsheetHttpServer;
import walkingkooka.spreadsheet.server.SpreadsheetServerLinkRelations;
import walkingkooka.spreadsheet.server.SpreadsheetUrlQueryParameters;
import walkingkooka.text.CharSequences;

import java.util.Objects;

/**
 * Fetcher for {@link walkingkooka.plugin.store.PluginStore} end points.
 */
public final class PluginFetcher extends Fetcher<PluginFetcherWatcher> {

    static {
        Plugin.HATEOS_RESOURCE_NAME.toString();
        PluginNameSet.parse("");
    }

    public static PluginFetcher with(final PluginFetcherWatcher watcher,
                                     final AppContext context) {
        Objects.requireNonNull(watcher, "watcher");
        Objects.requireNonNull(context, "context");

        return new PluginFetcher(
                watcher,
                context
        );
    }

    private PluginFetcher(final PluginFetcherWatcher watcher,
                          final AppContext context) {
        super(
                watcher,
                context
        );
    }

    // GET /api/plugin/PluginName/filter?query=XXX&offset=0&count=10
    public void filter(final PluginName pluginName,
                       final String query,
                       final int offset,
                       final int count) {
        Objects.requireNonNull(pluginName, "pluginName");
        Objects.requireNonNull(query, "query");
        if (offset < 0) {
            throw new IllegalArgumentException("Invalid offset " + offset + " < 0");
        }
        if (count < 0) {
            throw new IllegalArgumentException("Invalid count " + count + " < 0");
        }

        this.get(
                plugin()
                        .appendPathName(
                                UrlPathName.with(pluginName.value())
                        ).appendPathName(
                                SpreadsheetServerLinkRelations.FILTER.toUrlPathName()
                        ).setQuery(
                                UrlQueryString.EMPTY.addParameter(SpreadsheetUrlQueryParameters.QUERY, query)
                                        .addParameter(SpreadsheetUrlQueryParameters.OFFSET, String.valueOf(offset))
                                        .addParameter(SpreadsheetUrlQueryParameters.COUNT, String.valueOf(count))
                        )
        );
    }

    static RelativeUrl plugin() {
        return Url.EMPTY_RELATIVE_URL.appendPath(
                SpreadsheetHttpServer.API_PLUGIN.append(
                        Plugin.HATEOS_RESOURCE_NAME.toUrlPathName()
                )
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
            case "PluginSet":
                // GET http://server/api/plugin/PluginName/filter
                this.watcher.onPluginSet(
                        this.parse(
                                body,
                                PluginSet.class
                        ), // edit
                        context
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quote(contentTypeName));
        }
    }
}
