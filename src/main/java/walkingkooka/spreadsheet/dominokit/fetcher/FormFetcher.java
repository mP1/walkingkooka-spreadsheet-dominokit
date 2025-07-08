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
import walkingkooka.text.CharSequences;
import walkingkooka.validation.form.Form;
import walkingkooka.validation.form.FormName;

import java.util.Optional;

/**
 * Fetcher for {@link Form} end points.
 */
public final class FormFetcher extends Fetcher<FormFetcherWatcher> {

    static {
        FormName.CASE_SENSITIVITY.toString(); // force json unmarshaller to register
    }

    public static FormFetcher with(final FormFetcherWatcher watcher,
                                   final AppContext context) {
        return new FormFetcher(
            watcher,
            context
        );
    }

    private FormFetcher(final FormFetcherWatcher watcher,
                        final AppContext context) {
        super(
            watcher,
            context
        );
    }

    static RelativeUrl url(final SpreadsheetId id) {
        return SpreadsheetMetadataFetcher.url(id)
            .appendPathName(
                FormName.HATEOS_RESOURCE_NAME.toUrlPathName()
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
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quote(contentTypeName));
        }
    }
}
