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

import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.convert.Converter;
import walkingkooka.convert.provider.ConverterInfoSet;
import walkingkooka.convert.provider.ConverterName;
import walkingkooka.convert.provider.ConverterSelector;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlQueryString;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.convert.MissingConverterSet;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.server.SpreadsheetHttpServer;
import walkingkooka.spreadsheet.server.convert.ConverterHateosResourceMappings;
import walkingkooka.spreadsheet.server.url.SpreadsheetUrlPathTemplate;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;

import java.util.Optional;

/**
 * Fetcher for {@link Converter} end points.
 */
public final class ConverterFetcher extends Fetcher<ConverterFetcherWatcher> {

    static {
        ConverterName.CASE_SENSITIVITY.toString(); // force json unmarshaller to register
    }

    public static ConverterFetcher with(final ConverterFetcherWatcher watcher,
                                        final AppContext context) {
        return new ConverterFetcher(
            watcher,
            context
        );
    }

    private ConverterFetcher(final ConverterFetcherWatcher watcher,
                             final AppContext context) {
        super(
            watcher,
            context
        );
    }

    static RelativeUrl url(final SpreadsheetId id) {
        return SpreadsheetMetadataFetcher.url(id)
            .appendPathName(ConverterHateosResourceMappings.HATEOS_RESOURCE_NAME.toUrlPathName());
    }

    // GET /api/converterUrl/*
    public void getInfoSet() {
        this.get(URL);
    }

    private final static RelativeUrl URL = Url.EMPTY_RELATIVE_URL.appendPath(SpreadsheetHttpServer.API_CONVERTER);

    // POST /api/spreadsheet/SpreadsheetId/metadata/SpreadsheetMetadataPropertyName<ConverterSelector>/verify
    public void postVerify(final SpreadsheetId id,
                           final SpreadsheetMetadataPropertyName<ConverterSelector> converterMetadataProperty,
                           final String converterSelector) {
        this.post(
            VERIFY.render(
                Maps.of(
                    SpreadsheetUrlPathTemplate.SPREADSHEET_ID, id,
                    SpreadsheetUrlPathTemplate.SPREADSHEET_METADATA_PROPERTY_NAME, converterMetadataProperty
                )
            ).addQueryString(UrlQueryString.EMPTY),
            FetcherRequestBody.string(
                JsonNode.string(converterSelector)
                    .toString()
            )
        );
    }

    private final SpreadsheetUrlPathTemplate VERIFY = SpreadsheetUrlPathTemplate.parse("/api/spreadsheet/${SpreadsheetId}/metadata/${SpreadsheetMetadataPropertyName}/verify");

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
            case "ConverterInfoSet":
                // GET http://server/api/converter
                this.watcher.onConverterInfoSet(
                    this.parse(
                        body,
                        ConverterInfoSet.class
                    ), // edit
                    context
                );
                break;
            case "MissingConverterSet":
                // POST http://server/api/spreadsheet/1/converter/SpreadsheetMetadataPropertyName<ConverterSelector>/verify
                this.watcher.onVerify(
                    SpreadsheetMetadataFetcher.extractSpreadsheetIdOrFail(url),
                    verifyConverterSelector(url),
                    this.parse(
                        body,
                        MissingConverterSet.class
                    ), // MissingConverterSet
                    context
                );
                break;

            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quote(contentTypeName));
        }
    }

    // /api/spreadsheet/1/converter/SpreadsheetMetadataPropertyName<ConverterSelector>/verify
    // 01   2           3 4         5                                                  6
    static SpreadsheetMetadataPropertyName<ConverterSelector> verifyConverterSelector(final AbsoluteOrRelativeUrl url) {
        return Cast.to(
            SpreadsheetMetadataPropertyName.with(
                url.path()
                    .namesList()
                    .get(5)
                    .value()
            )
        );
    }
}
