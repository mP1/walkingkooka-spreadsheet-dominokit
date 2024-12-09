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
import walkingkooka.spreadsheet.importer.SpreadsheetImporter;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterInfoSet;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterName;
import walkingkooka.text.CharSequences;

import java.util.Objects;

/**
 * Fetcher for {@link SpreadsheetImporter} end points.
 */
public final class SpreadsheetImporterFetcher extends Fetcher<SpreadsheetImporterFetcherWatcher> {

    static {
        SpreadsheetImporterName.CASE_SENSITIVITY.toString(); // force json unmarshaller to register
    }

    public static SpreadsheetImporterFetcher with(final SpreadsheetImporterFetcherWatcher watcher,
                                                  final AppContext context) {
        Objects.requireNonNull(watcher, "watcher");
        Objects.requireNonNull(context, "context");

        return new SpreadsheetImporterFetcher(
                watcher,
                context
        );
    }

    private SpreadsheetImporterFetcher(final SpreadsheetImporterFetcherWatcher watcher,
                                       final AppContext context) {
        super(
                watcher,
                context
        );
    }

    static RelativeUrl importer(final SpreadsheetId id) {
        return SpreadsheetMetadataFetcher.url(id)
                .appendPathName(
                        SpreadsheetImporterName.HATEOS_RESOURCE_NAME.toUrlPathName()
                );
    }

    // GET /api/spreadsheet/SpreadsheetId/importer/*
    public void infoSet(final SpreadsheetId id) {
        this.get(
                importer(id)
        );
    }

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
            case "SpreadsheetImporterInfoSet":
                // GET http://server/api/spreadsheet/1/importer
                this.watcher.onSpreadsheetImporterInfoSet(
                        SpreadsheetMetadataFetcher.extractSpreadsheetId(url)
                                .get(), // the request url
                        this.parse(
                                body,
                                SpreadsheetImporterInfoSet.class
                        ), // edit
                        context
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quote(contentTypeName));
        }
    }
}
