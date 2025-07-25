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
import walkingkooka.validation.form.FormHandler;
import walkingkooka.validation.form.provider.FormHandlerInfo;
import walkingkooka.validation.form.provider.FormHandlerInfoSet;
import walkingkooka.validation.form.provider.FormHandlerName;

import java.util.Objects;
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

    // GET /api/formHandler/FormHandlerName
    public void getInfo(final FormHandlerName name) {
        this.get(
            URL.appendPathName(
                Objects.requireNonNull(name, "name")
                    .toUrlPathName()
            )
        );
    }

    // GET /api/formHandler/*
    public void getInfoSet() {
        this.get(URL);
    }

    final static AbsoluteOrRelativeUrl URL = RelativeUrl.EMPTY_RELATIVE_URL.appendPath(SpreadsheetHttpServer.API_FORM_HANDLER);

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
                // GET http://server/api/formHandler/FormHandlerName
                this.watcher.onFormHandlerInfo(
                    this.parse(
                        body,
                        FormHandlerInfo.class
                    ), // edit
                    context
                );
                break;
            case "FormHandlerInfoSet":
                // GET http://server/api/formHandler/*
                this.watcher.onFormHandlerInfoSet(
                    this.parse(
                        body,
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
