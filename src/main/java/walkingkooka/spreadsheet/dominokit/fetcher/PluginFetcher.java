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
import walkingkooka.collect.map.Maps;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlPath;
import walkingkooka.net.UrlPathName;
import walkingkooka.net.UrlQueryString;
import walkingkooka.net.header.Accept;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.plugin.PluginName;
import walkingkooka.plugin.store.Plugin;
import walkingkooka.plugin.store.PluginSet;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.server.SpreadsheetHttpServer;
import walkingkooka.spreadsheet.server.SpreadsheetServerLinkRelations;
import walkingkooka.spreadsheet.server.SpreadsheetUrlQueryParameters;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoList;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Fetcher for {@link walkingkooka.plugin.store.PluginStore} end points.
 */
public final class PluginFetcher extends Fetcher<PluginFetcherWatcher> {

    // force static initializers to register json marshaller/unmarshallers.
    static {
        Plugin.HATEOS_RESOURCE_NAME.toString();
        PluginSet.EMPTY.isEmpty();
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

    // DELETE /api/plugin/PluginName
    public void deletePlugin(final PluginName pluginName) {
        this.delete(
                pluginNameUrl(pluginName)
        );
    }

    // GET /api/plugin/*/filter?query=XXX&offset=0&count=10
    public void filter(final String query,
                       final int offset,
                       final int count) {
        Objects.requireNonNull(query, "query");
        if (offset < 0) {
            throw new IllegalArgumentException("Invalid offset " + offset + " < 0");
        }
        if (count < 0) {
            throw new IllegalArgumentException("Invalid count " + count + " < 0");
        }

        this.get(
                plugin()
                        .appendPathName(UrlPathName.WILDCARD)
                        .appendPathName(
                                SpreadsheetServerLinkRelations.FILTER.toUrlPathName()
                        ).setQuery(
                                UrlQueryString.EMPTY.addParameter(SpreadsheetUrlQueryParameters.QUERY, query)
                                        .addParameter(SpreadsheetUrlQueryParameters.OFFSET, String.valueOf(offset))
                                        .addParameter(SpreadsheetUrlQueryParameters.COUNT, String.valueOf(count))
                        )
        );
    }

    // GET /api/plugin/PluginName/list
    public void listJarEntries(final PluginName pluginName) {
        this.get(
                pluginNameUrl(pluginName)
                        .appendPathName(
                                SpreadsheetServerLinkRelations.LIST.toUrlPathName()
                        )
        );
    }

    /**
     * Loads a text files from the given {@link PluginName}.
     */
    public void loadJarTextFile(final PluginName pluginName,
                                final JarEntryInfoName filename) {
        this.fetch(
                HttpMethod.GET,
                pluginDownloadUrl(
                        pluginName,
                        Optional.of(filename)
                ),
                Maps.of(
                        HttpHeaderName.ACCEPT,
                        LOAD_TEXT_FILE_ACCEPT
                ),
                Optional.empty()
        );
    }

    private final static Accept LOAD_TEXT_FILE_ACCEPT = Accept.with(
            Lists.of(
                    MediaType.ANY_TEXT
            )
    );

    static RelativeUrl plugin() {
        return Url.EMPTY_RELATIVE_URL.appendPath(
                SpreadsheetHttpServer.API_PLUGIN
        );
    }

    // api/plugin/PluginName
    public static RelativeUrl pluginNameUrl(final PluginName pluginName) {
        Objects.requireNonNull(pluginName, "pluginName");

        return Url.EMPTY_RELATIVE_URL.appendPath(
                SpreadsheetHttpServer.API_PLUGIN.append(
                        UrlPathName.with(pluginName.value())
                )
        );
    }

    /**
     * Builds a URL to download a file within or the plugin archive.
     */
    // api/plugin/PluginName/download
    public static RelativeUrl pluginDownloadUrl(final PluginName pluginName,
                                                final Optional<JarEntryInfoName> file) {
        Objects.requireNonNull(pluginName, "pluginName");
        Objects.requireNonNull(file, "file");

        RelativeUrl url = pluginNameUrl(pluginName)
                .appendPathName(SpreadsheetServerLinkRelations.DOWNLOAD.toUrlPathName());
        if (file.isPresent()) {
            url = url.appendPath(
                    UrlPath.parse(
                            file.get().value())
            );
        }

        return url;
    }

    // Fetcher..........................................................................................................

    @Override
    public void onSuccess(final HttpMethod method,
                          final AbsoluteOrRelativeUrl url,
                          final String contentTypeName,
                          final Optional<String> body) {
        final AppContext context = this.context;

        final String bodyText = body.orElse("");

        switch (CharSequences.nullToEmpty(contentTypeName).toString()) {
            case "":
                this.watcher.onEmptyResponse(context);
                break;
            case "JarEntryInfoList":
                // GET https://server/api/plugin/Plugin/list
                this.watcher.onJarEntryInfoList(
                        this.extractPluginName(url),
                        Optional.ofNullable(
                                bodyText.trim()
                                        .isEmpty() ?
                                        null :
                                        this.parse(
                                                bodyText,
                                                JarEntryInfoList.class
                                        )
                        ),
                        this.context
                );
                break;
            case "JarEntryInfoName":
                // GET https://server/api/plugin/PluginName/download/**
                this.watcher.onJarEntryInfoName(
                        this.extractPluginName(url),
                        JarEntryInfoName.pluginDownloadPathExtract(url.path()),
                        body,
                        context
                );
                break;
            case "Plugin":
                // GET http://server/api/plugin/PluginName
                this.watcher.onPlugin(
                        this.extractPluginName(url),
                        Optional.ofNullable(
                                bodyText.trim()
                                        .isEmpty() ?
                                        null :
                                        this.parse(
                                                bodyText,
                                                Plugin.class
                                        )
                        ), // edit
                        context
                );
                break;
            case "PluginSet":
                // GET http://server/api/plugin/PluginName/filter
                this.watcher.onPluginSet(
                        this.parse(
                                bodyText,
                                PluginSet.class
                        ), // edit
                        context
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quote(contentTypeName));
        }
    }

    // extract pluginName from url / 1=api / 2=plugin / 3=PluginName
    private PluginName extractPluginName(final AbsoluteOrRelativeUrl url) {
        final List<UrlPathName> names = url.path()
                .namesList();
        if (names.size() < 3) {
            throw new IllegalStateException("Missing pluginName from url " + url);
        }
        return PluginName.with(
                names.get(3)
                        .value()
        );
    }
}
