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

import elemental2.dom.Headers;
import org.junit.Test;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;

import java.util.Optional;

public final class SpreadsheetDeltaFetcherWatchersTest extends FetcherWatchersTestCase<SpreadsheetDeltaFetcherWatchers> {

    @Test
    public void testAddThenFire() {
        this.fired = 0;

        final SpreadsheetDelta spreadsheetDelta = SpreadsheetDelta.EMPTY;
        final AppContext appContext = new FakeAppContext();

        final SpreadsheetDeltaFetcherWatchers watchers = SpreadsheetDeltaFetcherWatchers.empty();
        watchers.add(
                new FakeSpreadsheetDeltaFetcherWatcher() {
                    @Override
                    public void onSpreadsheetDelta(final SpreadsheetDelta delta,
                                                   final AppContext context) {
                        SpreadsheetDeltaFetcherWatchersTest.this.checkEquals(spreadsheetDelta, delta);
                        SpreadsheetDeltaFetcherWatchersTest.this.checkEquals(appContext, context);

                        SpreadsheetDeltaFetcherWatchersTest.this.fired++;
                    }
                });
        watchers.onSpreadsheetDelta(spreadsheetDelta, appContext);

        this.checkEquals(1, this.fired);
    }

    @Test
    public void testAddOnce() {
        this.fired = 0;

        final SpreadsheetDelta spreadsheetDelta = SpreadsheetDelta.EMPTY;
        final AppContext appContext = new FakeAppContext();

        final SpreadsheetDeltaFetcherWatchers watchers = SpreadsheetDeltaFetcherWatchers.empty();
        watchers.addOnce(
                new FakeSpreadsheetDeltaFetcherWatcher() {
                    @Override
                    public void onSpreadsheetDelta(final SpreadsheetDelta delta,
                                                   final AppContext context) {
                        SpreadsheetDeltaFetcherWatchersTest.this.checkEquals(spreadsheetDelta, delta);
                        SpreadsheetDeltaFetcherWatchersTest.this.checkEquals(appContext, context);

                        SpreadsheetDeltaFetcherWatchersTest.this.fired++;
                    }
                });
        watchers.onSpreadsheetDelta(spreadsheetDelta, appContext);
        this.checkEquals(1, this.fired);

        watchers.onSpreadsheetDelta(spreadsheetDelta, appContext);
        this.checkEquals(1, this.fired);

        watchers.onSpreadsheetDelta(spreadsheetDelta, appContext);
        this.checkEquals(1, this.fired);
    }

    @Test
    public void testFireBegin() {
        this.fired = 0;

        final HttpMethod method = HttpMethod.with("CustomHttpMethod");
        final Url url = Url.parseAbsolute("https://example/");
        final Optional<String> body = Optional.of("Body123");
        final AppContext appContext = new FakeAppContext();

        final SpreadsheetDeltaFetcherWatchers watchers = SpreadsheetDeltaFetcherWatchers.empty();
        watchers.add(
                new FakeSpreadsheetDeltaFetcherWatcher() {

                    @Override
                    public void onBegin(final HttpMethod m,
                                        final Url u,
                                        final Optional<String> b,
                                        final AppContext context) {
                        SpreadsheetDeltaFetcherWatchersTest.this.checkEquals(method, m);
                        SpreadsheetDeltaFetcherWatchersTest.this.checkEquals(u, u);
                        SpreadsheetDeltaFetcherWatchersTest.this.checkEquals(b, b);
                        SpreadsheetDeltaFetcherWatchersTest.this.checkEquals(appContext, context);

                        SpreadsheetDeltaFetcherWatchersTest.this.fired++;
                    }
                });
        watchers.onBegin(method, url, body, appContext);
        this.checkEquals(1, this.fired);

        watchers.onBegin(method, url, body, appContext);
        this.checkEquals(2, this.fired);

        watchers.onBegin(method, url, body, appContext);
        this.checkEquals(3, this.fired);
    }

    @Test
    public void testFireError() {
        this.fired = 0;

        final Object cause = "cause-123";
        final AppContext appContext = new FakeAppContext();

        final SpreadsheetDeltaFetcherWatchers watchers = SpreadsheetDeltaFetcherWatchers.empty();
        watchers.add(
                new FakeSpreadsheetDeltaFetcherWatcher() {
                    @Override
                    public void onError(final Object c,
                                        final AppContext context) {
                        SpreadsheetDeltaFetcherWatchersTest.this.checkEquals(cause, c);
                        SpreadsheetDeltaFetcherWatchersTest.this.checkEquals(appContext, context);

                        SpreadsheetDeltaFetcherWatchersTest.this.fired++;
                    }
                });
        watchers.onError(cause, appContext);
        this.checkEquals(1, this.fired);

        watchers.onError(cause, appContext);
        this.checkEquals(2, this.fired);

        watchers.onError(cause, appContext);
        this.checkEquals(3, this.fired);
    }

    @Test
    public void testFireFailure() {
        this.fired = 0;

        final HttpStatus status = HttpStatusCode.withCode(123).setMessage("status message 456");
        final Headers headers = null;
        final String body = "Body-failure-789";
        final AppContext appContext = new FakeAppContext();

        final SpreadsheetDeltaFetcherWatchers watchers = SpreadsheetDeltaFetcherWatchers.empty();
        watchers.add(
                new FakeSpreadsheetDeltaFetcherWatcher() {

                    @Override
                    public void onFailure(final HttpStatus s,
                                          final Headers h,
                                          final String b,
                                          final AppContext context) {
                        SpreadsheetDeltaFetcherWatchersTest.this.checkEquals(status, s);
                        SpreadsheetDeltaFetcherWatchersTest.this.checkEquals(headers, h);
                        SpreadsheetDeltaFetcherWatchersTest.this.checkEquals(body, b);
                        SpreadsheetDeltaFetcherWatchersTest.this.checkEquals(appContext, context);

                        SpreadsheetDeltaFetcherWatchersTest.this.fired++;
                    }
                });
        watchers.onFailure(status, headers, body, appContext);
        this.checkEquals(1, this.fired);

        watchers.onFailure(status, headers, body, appContext);
        this.checkEquals(2, this.fired);

        watchers.onFailure(status, headers, body, appContext);
        this.checkEquals(3, this.fired);
    }

    private int fired = 0;

    // ClassTesting....................................................................................................

    @Override
    public Class<SpreadsheetDeltaFetcherWatchers> type() {
        return SpreadsheetDeltaFetcherWatchers.class;
    }
}
