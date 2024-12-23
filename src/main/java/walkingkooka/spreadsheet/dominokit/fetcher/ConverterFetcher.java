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

import walkingkooka.convert.Converter;
import walkingkooka.convert.provider.ConverterInfoSet;
import walkingkooka.convert.provider.ConverterName;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.UrlPath;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.server.convert.ConverterHateosResourceMappings;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * Fetcher for {@link Converter} end points.
 */
public final class ConverterFetcher extends Fetcher<ConverterFetcherWatcher> {

    private final static UrlPath CONVERTER = UrlPath.parse(
            ConverterHateosResourceMappings.CONVERTER.value()
    );

    static {
        ConverterName.CASE_SENSITIVITY.toString(); // force json unmarshaller to register
    }

    public static ConverterFetcher with(final ConverterFetcherWatcher watcher,
                                        final AppContext context) {
        Objects.requireNonNull(watcher, "watcher");
        Objects.requireNonNull(context, "context");

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

    static RelativeUrl converter(final SpreadsheetId id) {
        return SpreadsheetMetadataFetcher.url(id)
                .appendPath(CONVERTER);
    }

    // GET /api/spreadsheet/SpreadsheetId/converter/*
    public void infoSet(final SpreadsheetId id) {
        this.get(
                converter(id)
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
            case "ConverterInfoSet":
                // GET http://server/api/spreadsheet/1/converter
                this.watcher.onConverterInfoSet(
                        SpreadsheetMetadataFetcher.extractSpreadsheetId(url)
                                .get(), // the request url
                        this.parse(
                                body.orElse(""),
                                ConverterInfoSet.class
                        ), // edit
                        context
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quote(contentTypeName));
        }
    }
}
