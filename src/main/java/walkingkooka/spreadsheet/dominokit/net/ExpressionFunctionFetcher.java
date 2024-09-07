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

package walkingkooka.spreadsheet.dominokit.net;


import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.UrlPath;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.server.function.ExpressionFunctionHateosResourceMappings;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfoSet;

import java.util.Objects;

/**
 * Fetcher for {@link ExpressionFunction} end points.
 */
public final class ExpressionFunctionFetcher extends Fetcher<ExpressionFunctionFetcherWatcher> {

    private final static UrlPath FUNCTION = UrlPath.parse(
            ExpressionFunctionHateosResourceMappings.FUNCTION.value()
    );

    static {
        ExpressionFunctionName.CASE_SENSITIVITY.toString(); // force json unmarshaller to register
    }

    private ExpressionFunctionFetcher(final ExpressionFunctionFetcherWatcher watcher,
                                      final AppContext context) {
        super(
                watcher,
                context
        );
    }

    public static ExpressionFunctionFetcher with(final ExpressionFunctionFetcherWatcher watcher,
                                                 final AppContext context) {
        Objects.requireNonNull(watcher, "watcher");
        Objects.requireNonNull(context, "context");

        return new ExpressionFunctionFetcher(
                watcher,
                context
        );
    }

    static RelativeUrl function(final SpreadsheetId id) {
        return SpreadsheetMetadataFetcher.url(id)
                .appendPath(FUNCTION);
    }

    // GET /api/spreadsheet/SpreadsheetId/function/*
    public void infoSet(final SpreadsheetId id) {
        this.get(
                function(id)
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
            case "ExpressionFunctionInfoSet":
                // GET http://server/api/spreadsheet/1/function
                this.watcher.onExpressionFunctionInfoSet(
                        SpreadsheetMetadataFetcher.extractSpreadsheetId(url)
                                .get(), // the request url
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
