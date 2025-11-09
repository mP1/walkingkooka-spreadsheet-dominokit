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

import elemental2.dom.Headers;
import org.junit.jupiter.api.Test;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfoSet;

import java.util.Optional;

public final class ExpressionFunctionFetcherWatchersTest extends FetcherWatchersTestCase<ExpressionFunctionFetcherWatchers> {

    private final static HttpMethod METHOD = HttpMethod.GET;

    private final static AbsoluteOrRelativeUrl URL = Url.parseAbsoluteOrRelative("https://example.com/api/spreadsheet/1/cell");

    private final static SpreadsheetId ID = SpreadsheetId.with(2);

    @Test
    public void testAddThenFire() {
        this.fired = 0;

        final ExpressionFunctionInfoSet infos = SpreadsheetExpressionFunctions.EMPTY_INFO_SET;

        final ExpressionFunctionFetcherWatchers watchers = ExpressionFunctionFetcherWatchers.empty();
        watchers.add(
            new FakeExpressionFunctionFetcherWatcher() {

                @Override
                public void onExpressionFunctionInfoSet(final ExpressionFunctionInfoSet is) {
                    ExpressionFunctionFetcherWatchersTest.this.checkEquals(is, infos);

                    ExpressionFunctionFetcherWatchersTest.this.fired++;
                }
            });
        watchers.onExpressionFunctionInfoSet(
            infos
        );
        this.checkEquals(1, this.fired);
    }

    @Test
    public void testAddOnce() {
        this.fired = 0;

        final ExpressionFunctionInfoSet infos = SpreadsheetExpressionFunctions.EMPTY_INFO_SET;

        final ExpressionFunctionFetcherWatchers watchers = ExpressionFunctionFetcherWatchers.empty();
        watchers.addOnce(
            new FakeExpressionFunctionFetcherWatcher() {

                @Override
                public void onExpressionFunctionInfoSet(final ExpressionFunctionInfoSet is) {
                    ExpressionFunctionFetcherWatchersTest.this.checkEquals(is, infos);

                    ExpressionFunctionFetcherWatchersTest.this.fired++;
                }
            });
        watchers.onExpressionFunctionInfoSet(
            infos
        );
        this.checkEquals(1, this.fired);

        watchers.onExpressionFunctionInfoSet(
            infos
        );
        this.checkEquals(1, this.fired);
    }

    @Test
    public void testFireBegin() {
        this.fired = 0;

        final HttpMethod method = HttpMethod.with("CustomHttpMethod");
        final Url url = Url.parseAbsolute("https://example/");
        final Optional<FetcherRequestBody<?>> body = Optional.of(
            FetcherRequestBody.string("Body123")
        );

        final ExpressionFunctionFetcherWatchers watchers = ExpressionFunctionFetcherWatchers.empty();
        watchers.add(
            new FakeExpressionFunctionFetcherWatcher() {

                @Override
                public void onBegin(final HttpMethod m,
                                    final Url u,
                                    final Optional<FetcherRequestBody<?>> b) {
                    ExpressionFunctionFetcherWatchersTest.this.checkEquals(method, m);
                    ExpressionFunctionFetcherWatchersTest.this.checkEquals(u, u);
                    ExpressionFunctionFetcherWatchersTest.this.checkEquals(b, b);

                    ExpressionFunctionFetcherWatchersTest.this.fired++;
                }
            });
        watchers.onBegin(method, url, body);
        this.checkEquals(1, this.fired);

        watchers.onBegin(method, url, body);
        this.checkEquals(2, this.fired);

        watchers.onBegin(method, url, body);
        this.checkEquals(3, this.fired);
    }

    @Test
    public void testFireError() {
        this.fired = 0;

        final Object cause = "cause-123";

        final ExpressionFunctionFetcherWatchers watchers = ExpressionFunctionFetcherWatchers.empty();
        watchers.add(
            new FakeExpressionFunctionFetcherWatcher() {
                @Override
                public void onError(final Object c) {
                    ExpressionFunctionFetcherWatchersTest.this.checkEquals(cause, c);

                    ExpressionFunctionFetcherWatchersTest.this.fired++;
                }
            });
        watchers.onError(cause);
        this.checkEquals(1, this.fired);

        watchers.onError(cause);
        this.checkEquals(2, this.fired);

        watchers.onError(cause);
        this.checkEquals(3, this.fired);
    }

    @Test
    public void testFireFailure() {
        this.fired = 0;

        final HttpMethod method = HttpMethod.GET;
        final AbsoluteOrRelativeUrl url = Url.parseRelative("/something/api/");
        final HttpStatus status = HttpStatusCode.withCode(123).setMessage("status message 456");
        final Headers headers = null;
        final String body = "Body-failure-789";

        final ExpressionFunctionFetcherWatchers watchers = ExpressionFunctionFetcherWatchers.empty();
        watchers.add(
            new FakeExpressionFunctionFetcherWatcher() {

                @Override
                public void onFailure(final HttpMethod m,
                                      final AbsoluteOrRelativeUrl u,
                                      final HttpStatus s,
                                      final Headers h,
                                      final String b) {
                    ExpressionFunctionFetcherWatchersTest.this.checkEquals(method, m);
                    ExpressionFunctionFetcherWatchersTest.this.checkEquals(url, u);
                    ExpressionFunctionFetcherWatchersTest.this.checkEquals(status, s);
                    ExpressionFunctionFetcherWatchersTest.this.checkEquals(headers, h);
                    ExpressionFunctionFetcherWatchersTest.this.checkEquals(body, b);

                    ExpressionFunctionFetcherWatchersTest.this.fired++;
                }
            });
        watchers.onFailure(method, url, status, headers, body);
        this.checkEquals(1, this.fired);

        watchers.onFailure(method, url, status, headers, body);
        this.checkEquals(2, this.fired);

        watchers.onFailure(method, url, status, headers, body);
        this.checkEquals(3, this.fired);
    }

    private int fired = 0;

    // ClassTesting....................................................................................................

    @Override
    public Class<ExpressionFunctionFetcherWatchers> type() {
        return ExpressionFunctionFetcherWatchers.class;
    }
}
