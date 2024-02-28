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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;

import java.util.Optional;

public final class SpreadsheetMetadataFetcherWatchersTest extends FetcherWatchersTestCase<SpreadsheetMetadataFetcherWatchers> {

    @Test
    public void testAddThenFire() {
        this.fired = 0;

        final SpreadsheetMetadata spreadsheetMetadata = SpreadsheetMetadata.EMPTY;
        final AppContext appContext = new FakeAppContext();

        final SpreadsheetMetadataFetcherWatchers watchers = SpreadsheetMetadataFetcherWatchers.empty();
        watchers.add(
                new FakeSpreadsheetMetadataFetcherWatcher() {
                    @Override
                    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                                      final AppContext context) {
                        SpreadsheetMetadataFetcherWatchersTest.this.checkEquals(spreadsheetMetadata, metadata);
                        SpreadsheetMetadataFetcherWatchersTest.this.checkEquals(appContext, context);

                        SpreadsheetMetadataFetcherWatchersTest.this.fired++;
                    }
                });
        watchers.onSpreadsheetMetadata(spreadsheetMetadata, appContext);

        this.checkEquals(1, this.fired);
    }

    @Test
    public void testFireBegin() {
        this.fired = 0;

        final HttpMethod method = HttpMethod.with("CustomHttpMethod");
        final Url url = Url.parseAbsolute("https://example/");
        final Optional<String> body = Optional.of("Body123");
        final AppContext appContext = new FakeAppContext();

        final SpreadsheetMetadataFetcherWatchers watchers = SpreadsheetMetadataFetcherWatchers.empty();
        watchers.add(
                new FakeSpreadsheetMetadataFetcherWatcher() {

                    @Override
                    public void onBegin(final HttpMethod m,
                                        final Url u,
                                        final Optional<String> b,
                                        final AppContext context) {
                        SpreadsheetMetadataFetcherWatchersTest.this.checkEquals(method, m);
                        SpreadsheetMetadataFetcherWatchersTest.this.checkEquals(u, u);
                        SpreadsheetMetadataFetcherWatchersTest.this.checkEquals(b, b);
                        SpreadsheetMetadataFetcherWatchersTest.this.checkEquals(appContext, context);

                        SpreadsheetMetadataFetcherWatchersTest.this.fired++;
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

        final SpreadsheetMetadataFetcherWatchers watchers = SpreadsheetMetadataFetcherWatchers.empty();
        watchers.add(
                new FakeSpreadsheetMetadataFetcherWatcher() {
                    @Override
                    public void onError(final Object c,
                                        final AppContext context) {
                        SpreadsheetMetadataFetcherWatchersTest.this.checkEquals(cause, c);
                        SpreadsheetMetadataFetcherWatchersTest.this.checkEquals(appContext, context);

                        SpreadsheetMetadataFetcherWatchersTest.this.fired++;
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

        final SpreadsheetMetadataFetcherWatchers watchers = SpreadsheetMetadataFetcherWatchers.empty();
        watchers.add(
                new FakeSpreadsheetMetadataFetcherWatcher() {

                    @Override
                    public void onFailure(final HttpStatus s,
                                          final Headers h,
                                          final String b,
                                          final AppContext context) {
                        SpreadsheetMetadataFetcherWatchersTest.this.checkEquals(status, s);
                        SpreadsheetMetadataFetcherWatchersTest.this.checkEquals(headers, h);
                        SpreadsheetMetadataFetcherWatchersTest.this.checkEquals(body, b);
                        SpreadsheetMetadataFetcherWatchersTest.this.checkEquals(appContext, context);

                        SpreadsheetMetadataFetcherWatchersTest.this.fired++;
                    }
                });
        watchers.onFailure(status, headers, body, appContext);
        this.checkEquals(1, this.fired);

        watchers.onFailure(status, headers, body, appContext);
        this.checkEquals(2, this.fired);

        watchers.onFailure(status, headers, body, appContext);
        this.checkEquals(3, this.fired);
    }

    private int fired;

    // ClassTesting....................................................................................................

    @Override
    public Class<SpreadsheetMetadataFetcherWatchers> type() {
        return SpreadsheetMetadataFetcherWatchers.class;
    }
}
