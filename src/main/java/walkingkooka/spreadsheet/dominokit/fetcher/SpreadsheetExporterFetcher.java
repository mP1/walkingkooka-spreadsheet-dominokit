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
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.export.SpreadsheetExporter;
import walkingkooka.spreadsheet.export.SpreadsheetExporterInfoSet;
import walkingkooka.spreadsheet.export.SpreadsheetExporterName;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * Fetcher for {@link SpreadsheetExporter} end points.
 */
public final class SpreadsheetExporterFetcher extends Fetcher<SpreadsheetExporterFetcherWatcher> {

    static {
        SpreadsheetExporterName.CASE_SENSITIVITY.toString(); // force json unmarshaller to register
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

    private SpreadsheetExporterFetcher(final SpreadsheetExporterFetcherWatcher watcher,
                                       final AppContext context) {
        super(
            watcher,
            context
        );
    }

    static RelativeUrl exporter(final SpreadsheetId id) {
        return SpreadsheetMetadataFetcher.url(id)
            .appendPathName(SpreadsheetExporterName.HATEOS_RESOURCE_NAME.toUrlPathName());
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
                          final Optional<String> body) {
        final AppContext context = this.context;

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
                        body.orElse(""),
                        SpreadsheetExporterInfoSet.class
                    ), // edit
                    context
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quote(contentTypeName));
        }
    }
}
