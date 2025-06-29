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
import walkingkooka.net.UrlPathName;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.text.CharSequences;
import walkingkooka.validation.Validator;
import walkingkooka.validation.provider.ValidatorInfo;
import walkingkooka.validation.provider.ValidatorInfoSet;
import walkingkooka.validation.provider.ValidatorName;

import java.util.Optional;

/**
 * Fetcher for {@link Validator} end points.
 */
public final class ValidatorFetcher extends Fetcher<ValidatorFetcherWatcher> {

    static {
        ValidatorName.CASE_SENSITIVITY.toString(); // force json unmarshaller to register
        ValidatorInfoSet.EMPTY.toString();
    }

    public static ValidatorFetcher with(final ValidatorFetcherWatcher watcher,
                                        final AppContext context) {
        return new ValidatorFetcher(
            watcher,
            context
        );
    }

    private ValidatorFetcher(final ValidatorFetcherWatcher watcher,
                             final AppContext context) {
        super(
            watcher,
            context
        );
    }

    static RelativeUrl url(final SpreadsheetId id) {
        return SpreadsheetMetadataFetcher.url(id)
            .appendPathName(
                ValidatorName.HATEOS_RESOURCE_NAME.toUrlPathName()
            );
    }

    // GET /api/spreadsheet/SpreadsheetId/validator/ValidatorName
    public void getInfoSet(final SpreadsheetId id,
                           final ValidatorName name) {
        this.get(
            url(id)
                .appendPathName(
                    UrlPathName.with(name.value())
                )
        );
    }

    // GET /api/spreadsheet/SpreadsheetId/validator/*
    public void getInfoSet(final SpreadsheetId id) {
        this.get(
            url(id)
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
            case "ValidatorInfo":
                // GET http://server/api/spreadsheet/1/validator/ValidatorName
                this.watcher.onValidatorInfo(
                    SpreadsheetMetadataFetcher.extractSpreadsheetId(url)
                        .get(), // the request url
                    this.parse(
                        body.orElse(""),
                        ValidatorInfo.class
                    ), // edit
                    context
                );
                break;
            case "ValidatorInfoSet":
                // GET http://server/api/spreadsheet/1/validator
                this.watcher.onValidatorInfoSet(
                    SpreadsheetMetadataFetcher.extractSpreadsheetId(url)
                        .get(), // the request url
                    this.parse(
                        body.orElse(""),
                        ValidatorInfoSet.class
                    ), // edit
                    context
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quote(contentTypeName));
        }
    }
}
