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

package walkingkooka.spreadsheet.dominokit.net;

import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.UrlPath;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.export.SpreadsheetExporter;
import walkingkooka.spreadsheet.export.SpreadsheetExporterInfoSet;
import walkingkooka.spreadsheet.export.SpreadsheetExporterName;
import walkingkooka.spreadsheet.server.export.SpreadsheetExporterHateosResourceMappings;
import walkingkooka.text.CharSequences;

import java.util.Objects;

/**
 * Fetcher for {@link SpreadsheetExporter} end points.
 */
public final class SpreadsheetExporterFetcher implements Fetcher {

    private final static UrlPath EXPORTER = UrlPath.parse(
            SpreadsheetExporterHateosResourceMappings.EXPORTER.value()
    );

    static {
        SpreadsheetExporterName.CASE_SENSITIVITY.toString(); // force json unmarshaller to register
    }

    private final SpreadsheetExporterFetcherWatcher watcher;

    // Fetcher..........................................................................................................
    private final AppContext context;
    private int waitingRequestCount;

    private SpreadsheetExporterFetcher(final SpreadsheetExporterFetcherWatcher watcher,
                                       final AppContext context) {
        this.watcher = watcher;
        this.context = context;
    }

    public static SpreadsheetExporterFetcher with(final SpreadsheetExporterFetcherWatcher watcher,
                                                  final AppContext context) {
        Objects.requireNonNull(watcher, "watcher");
        Objects.requireNonNull(context, "context");

        return new SpreadsheetExporterFetcher(
                watcher,
                context
        );
    }

    static RelativeUrl exporter(final SpreadsheetId id) {
        return SpreadsheetMetadataFetcher.url(id)
                .appendPath(EXPORTER);
    }

    // GET /api/spreadsheet/SpreadsheetId/exporter/*
    public void infoSet(final SpreadsheetId id) {
        this.get(
                exporter(id)
        );
    }

    @Override
    public void onSuccess(final HttpMethod method,
                          final AbsoluteOrRelativeUrl url,
                          final String contentTypeName,
                          final String body) {
        final AppContext context = this.context;

        this.logSuccess(
                method,
                url,
                contentTypeName,
                body,
                context
        );

        switch (CharSequences.nullToEmpty(contentTypeName).toString()) {
            case "":
                this.watcher.onEmptyResponse(context);
                break;
            case "SpreadsheetExporterInfoSet":
                // GET http://server/api/spreadsheet/1/exporter
                this.watcher.onSpreadsheetExporterInfoSet(
                        SpreadsheetMetadataFetcher.extractSpreadsheetId(url)
                                .get(), // the request url
                        this.parse(
                                body,
                                SpreadsheetExporterInfoSet.class
                        ), // edit
                        context
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quote(contentTypeName));
        }
    }

    @Override
    public SpreadsheetExporterFetcherWatcher watcher() {
        return this.watcher;
    }

    @Override
    public int waitingRequestCount() {
        return this.waitingRequestCount;
    }

    @Override
    public void setWaitingRequestCount(final int waitingRequestCount) {
        this.waitingRequestCount = waitingRequestCount;
    }

    @Override
    public AppContext context() {
        return this.context;
    }

    // Object..........................................................................................................

    @Override
    public String toString() {
        return this.watcher.toString();
    }
}
