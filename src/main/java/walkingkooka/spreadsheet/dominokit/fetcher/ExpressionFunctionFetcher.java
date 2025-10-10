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
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.server.SpreadsheetHttpServer;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfoSet;

import java.util.Optional;

/**
 * Fetcher for {@link ExpressionFunction} end points.
 */
public final class ExpressionFunctionFetcher extends Fetcher<ExpressionFunctionFetcherWatcher> {

    public static ExpressionFunctionFetcher with(final ExpressionFunctionFetcherWatcher watcher,
                                                 final AppContext context) {
        return new ExpressionFunctionFetcher(
            watcher,
            context
        );
    }

    private ExpressionFunctionFetcher(final ExpressionFunctionFetcherWatcher watcher,
                                      final AppContext context) {
        super(
            watcher,
            context
        );
    }

    // GET /api/function/*
    public void getInfoSet() {
        this.get(URL);
    }

    private final static AbsoluteOrRelativeUrl URL = AbsoluteOrRelativeUrl.EMPTY_RELATIVE_URL.appendPath(SpreadsheetHttpServer.API_FUNCTION);

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
            case "ExpressionFunctionInfoSet":
                // GET http://server/api/function
                this.watcher.onExpressionFunctionInfoSet(
                    this.parse(
                        body,
                        ExpressionFunctionInfoSet.class
                    ), // edit
                    context
                );
                break;
            default:
                throw new IllegalArgumentException("Unexpected content type " + CharSequences.quote(contentTypeName));
        }
    }
}
