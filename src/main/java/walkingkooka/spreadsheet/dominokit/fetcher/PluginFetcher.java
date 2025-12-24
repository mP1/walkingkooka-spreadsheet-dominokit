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
import walkingkooka.net.header.Accept;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.plugin.PluginName;
import walkingkooka.plugin.store.Plugin;
import walkingkooka.plugin.store.PluginSet;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.server.SpreadsheetHttpServer;
import walkingkooka.spreadsheet.server.net.SpreadsheetServerLinkRelations;
import walkingkooka.spreadsheet.server.net.SpreadsheetServerMediaTypes;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoList;
import walkingkooka.spreadsheet.server.plugin.JarEntryInfoName;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * Fetcher for {@link walkingkooka.plugin.store.PluginStore} end points.
 */
public final class PluginFetcher extends Fetcher<PluginFetcherWatcher> {

    public static PluginFetcher with(final PluginFetcherWatcher watcher,
                                     final AppContext context) {
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
            url(pluginName)
        );
    }

    // GET /api/plugin/*/filter?query=XXX&offset=0&count=10
    public void getPluginFilter(final String query,
                                final OptionalInt offset,
                                final OptionalInt count) {
        Objects.requireNonNull(query, "query");

        this.get(
            URL.appendPathName(UrlPathName.WILDCARD)
                .appendPathName(
                    SpreadsheetServerLinkRelations.FILTER.toUrlPathName()
                        .get()
                ).setQuery(
                    offsetAndCountQueryString(
                        offset,
                        count
                    )
                )
        );
    }

    // GET /api/plugin/PluginName/list
    public void getPluginList(final PluginName pluginName) {
        this.get(
            url(pluginName)
                .appendPathName(
                    SpreadsheetServerLinkRelations.LIST.toUrlPathName()
                        .get()
                )
        );
    }

    /**
     * Loads a text files from the given {@link PluginName}.
     */
    public void getPluginJarTextFile(final PluginName pluginName,
                                     final JarEntryInfoName filename) {
        this.fetch(
            HttpMethod.GET,
            downloadUrl(
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

    /**
     * Uploads the request which is assumed to contain the file.
     */
    // POST /api/plugin/*/upload
    public void postPluginUpload(final FetcherRequestBody<?> body) {
        Objects.requireNonNull(body, "body");

        this.fetch(
            HttpMethod.POST,
            uploadUrl(),
            Maps.of(
                HttpHeaderName.ACCEPT,
                SpreadsheetServerMediaTypes.BINARY
            ),
            Optional.of(body)
        );
    }

    final static RelativeUrl URL = Url.EMPTY_RELATIVE_URL.appendPath(
        SpreadsheetHttpServer.API_PLUGIN
    );

    // api/plugin/PluginName
    static RelativeUrl url(final PluginName pluginName) {
        Objects.requireNonNull(pluginName, "pluginName");

        return URL.appendPathName(
            UrlPathName.with(pluginName.value())
        );
    }

    /**
     * Builds a URL to download a file within or the plugin archive.
     */
    // api/plugin/PluginName/download
    public static RelativeUrl downloadUrl(final PluginName pluginName,
                                          final Optional<JarEntryInfoName> file) {
        Objects.requireNonNull(pluginName, "pluginName");
        Objects.requireNonNull(file, "file");

        RelativeUrl url = url(pluginName)
            .appendPathName(
                SpreadsheetServerLinkRelations.DOWNLOAD.toUrlPathName()
                    .get()
            );
        if (file.isPresent()) {
            url = url.appendPath(
                UrlPath.parse(
                    file.get().value())
            );
        }

        return url;
    }

    // @VisibleForTesting
    static RelativeUrl uploadUrl() {
        return Url.EMPTY_RELATIVE_URL.appendPath(
            SpreadsheetHttpServer.API_PLUGIN.append(UrlPathName.WILDCARD)
                .append(UPLOAD)
        );
    }

    private final static UrlPathName UPLOAD = UrlPathName.with("upload");

    // Fetcher..........................................................................................................

    @Override
    public void onSuccess(final HttpMethod method,
                          final AbsoluteOrRelativeUrl url,
                          final String contentTypeName,
                          final Optional<String> body) {
        final String bodyText = body.orElse("");

        switch (CharSequences.nullToEmpty(contentTypeName).toString()) {
            case "":
                this.watcher.onEmptyResponse();
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
                    )
                );
                break;
            case "JarEntryInfoName":
                // GET https://server/api/plugin/PluginName/download/**
                this.watcher.onJarEntryInfoName(
                    this.extractPluginName(url),
                    JarEntryInfoName.pluginDownloadPathExtract(url.path()),
                    body
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
                    ) // edit
                );
                break;
            case "PluginSet":
                // GET http://server/api/plugin/PluginName/filter
                this.watcher.onPluginSet(
                    this.parse(
                        bodyText,
                        PluginSet.class
                    ) // edit
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

    // Logging..........................................................................................................

    @Override
    boolean isDebugEnabled() {
        return PLUGIN_FETCHER;
    }
}
