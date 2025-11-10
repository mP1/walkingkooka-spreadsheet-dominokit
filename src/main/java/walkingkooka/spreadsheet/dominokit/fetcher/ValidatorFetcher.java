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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.server.SpreadsheetHttpServer;
import walkingkooka.text.CharSequences;
import walkingkooka.validation.Validator;
import walkingkooka.validation.provider.ValidatorInfo;
import walkingkooka.validation.provider.ValidatorInfoSet;
import walkingkooka.validation.provider.ValidatorName;

import java.util.Objects;
import java.util.Optional;

/**
 * Fetcher for {@link Validator} end points.
 */
public final class ValidatorFetcher extends Fetcher<ValidatorFetcherWatcher> {

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

    // GET /api/validator/ValidatorName
    public void getInfoSet(final ValidatorName name) {
        this.get(
            URL.appendPathName(
                Objects.requireNonNull(name, "name")
                    .toUrlPathName()
            )
        );
    }

    // GET /api/validator/*
    public void getInfoSet() {
        this.get(URL);
    }

    final static RelativeUrl URL = AbsoluteOrRelativeUrl.EMPTY_RELATIVE_URL.appendPath(SpreadsheetHttpServer.API_VALIDATOR);

    @Override
    public void onSuccess(final HttpMethod method,
                          final AbsoluteOrRelativeUrl url,
                          final String contentTypeName,
                          final Optional<String> body) {
        final AppContext context = this.context;

        switch (CharSequences.nullToEmpty(contentTypeName).toString()) {
            case "":
                this.watcher.onEmptyResponse();
                break;
            case "ValidatorInfo":
                // GET http://server/api/validator/ValidatorName
                this.watcher.onValidatorInfo(
                    this.parse(
                        body,
                        ValidatorInfo.class
                    ) // edit
                );
                break;
            case "ValidatorInfoSet":
                // GET http://server/api/validator
                this.watcher.onValidatorInfoSet(
                    this.parse(
                        body,
                        ValidatorInfoSet.class
                    ) // edit
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quote(contentTypeName));
        }
    }

    // Logging..........................................................................................................

    @Override
    boolean isDebugEnabled() {
        return VALIDATOR_FETCHER;
    }
}
