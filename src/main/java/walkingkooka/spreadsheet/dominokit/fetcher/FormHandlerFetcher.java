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
import walkingkooka.validation.form.provider.FormHandlerInfo;
import walkingkooka.validation.form.provider.FormHandlerInfoSet;
import walkingkooka.validation.form.provider.FormHandlerName;

import java.util.Optional;

/**
 * Fetcher for {@link FormHandler} end points.
 */
public final class FormHandlerFetcher extends Fetcher<FormHandlerFetcherWatcher> {

    static {
        FormHandlerName.CASE_SENSITIVITY.toString(); // force json unmarshaller to register
        FormHandlerInfoSet.EMPTY.toString();
    }

    public static FormHandlerFetcher with(final FormHandlerFetcherWatcher watcher,
                                          final AppContext context) {
        return new FormHandlerFetcher(
            watcher,
            context
        );
    }

    private FormHandlerFetcher(final FormHandlerFetcherWatcher watcher,
                               final AppContext context) {
        super(
            watcher,
            context
        );
    }

    static RelativeUrl url(final SpreadsheetId id) {
        return SpreadsheetMetadataFetcher.url(id)
            .appendPathName(
                FormHandlerName.HATEOS_RESOURCE_NAME.toUrlPathName()
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
            case "FormHandlerInfo":
                // GET http://server/api/spreadsheet/1/formHandler/FormHandlerName
                this.watcher.onFormHandlerInfo(
                    SpreadsheetMetadataFetcher.extractSpreadsheetIdOrFail(url),
                    this.parse(
                        body.orElse(""),
                        FormHandlerInfo.class
                    ), // edit
                    context
                );
                break;
            case "FormHandlerInfoSet":
                // GET http://server/api/spreadsheet/1/formHandler/*
                this.watcher.onFormHandlerInfoSet(
                    SpreadsheetMetadataFetcher.extractSpreadsheetIdOrFail(url),
                    this.parse(
                        body.orElse(""),
                        FormHandlerInfoSet.class
                    ), // edit
                    context
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quote(contentTypeName));
        }
    }
}
