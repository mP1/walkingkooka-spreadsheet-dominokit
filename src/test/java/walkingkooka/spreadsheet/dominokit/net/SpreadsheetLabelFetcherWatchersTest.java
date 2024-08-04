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
import org.junit.jupiter.api.Test;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.AppContexts;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public final class SpreadsheetLabelFetcherWatchersTest extends FetcherWatchersTestCase<SpreadsheetLabelFetcherWatchers> {

    @Test
    public void testAddThenFire() {
        this.fired = 0;

        final SpreadsheetId spreadsheetId = SpreadsheetId.with(1);
        final Optional<SpreadsheetLabelMapping> spreadsheetLabelMapping = Optional.of(
                SpreadsheetSelection.labelName("Label123")
                        .mapping(SpreadsheetSelection.A1)
        );
        final AppContext appContext = AppContexts.fake();

        final SpreadsheetLabelFetcherWatchers watchers = SpreadsheetLabelFetcherWatchers.empty();
        watchers.add(
                new FakeSpreadsheetLabelFetcherWatcher() {
                    @Override
                    public void onSpreadsheetLabelMapping(final SpreadsheetId id,
                                                          final Optional<SpreadsheetLabelMapping> mapping,
                                                          final AppContext context) {
                        SpreadsheetLabelFetcherWatchersTest.this.checkEquals(spreadsheetId, id);
                        SpreadsheetLabelFetcherWatchersTest.this.checkEquals(spreadsheetLabelMapping, mapping);
                        SpreadsheetLabelFetcherWatchersTest.this.checkEquals(appContext, context);

                        SpreadsheetLabelFetcherWatchersTest.this.fired++;
                    }
                });
        watchers.onSpreadsheetLabelMapping(spreadsheetId, spreadsheetLabelMapping, appContext);

        this.checkEquals(1, this.fired);
    }

    @Test
    public void testAddOnce() {
        this.fired = 0;

        final SpreadsheetId spreadsheetId = SpreadsheetId.with(1);
        final Optional<SpreadsheetLabelMapping> spreadsheetLabelMapping = Optional.of(
                SpreadsheetSelection.labelName("Label123")
                        .mapping(SpreadsheetSelection.A1)
        );
        final AppContext appContext = AppContexts.fake();

        final SpreadsheetLabelFetcherWatchers watchers = SpreadsheetLabelFetcherWatchers.empty();
        watchers.addOnce(
                new FakeSpreadsheetLabelFetcherWatcher() {
                    @Override
                    public void onSpreadsheetLabelMapping(final SpreadsheetId id,
                                                          final Optional<SpreadsheetLabelMapping> mapping,
                                                          final AppContext context) {
                        SpreadsheetLabelFetcherWatchersTest.this.checkEquals(spreadsheetId, id);
                        SpreadsheetLabelFetcherWatchersTest.this.checkEquals(spreadsheetLabelMapping, mapping);
                        SpreadsheetLabelFetcherWatchersTest.this.checkEquals(appContext, context);

                        SpreadsheetLabelFetcherWatchersTest.this.fired++;
                    }
                });
        watchers.onSpreadsheetLabelMapping(spreadsheetId, spreadsheetLabelMapping, appContext);
        this.checkEquals(1, this.fired);

        watchers.onSpreadsheetLabelMapping(spreadsheetId, spreadsheetLabelMapping, appContext);
        this.checkEquals(1, this.fired);

        watchers.onSpreadsheetLabelMapping(spreadsheetId, spreadsheetLabelMapping, appContext);
        this.checkEquals(1, this.fired);
    }

    @Test
    public void testFireBegin() {
        this.fired = 0;

        final HttpMethod method = HttpMethod.with("CustomHttpMethod");
        final Url url = Url.parseAbsolute("https://example/");
        final Optional<String> body = Optional.of("Body123");
        final AppContext appContext = AppContexts.fake();

        final SpreadsheetLabelFetcherWatchers watchers = SpreadsheetLabelFetcherWatchers.empty();
        watchers.add(
                new FakeSpreadsheetLabelFetcherWatcher() {

                    @Override
                    public void onBegin(final HttpMethod m,
                                        final Url u,
                                        final Optional<String> b,
                                        final AppContext context) {
                        SpreadsheetLabelFetcherWatchersTest.this.checkEquals(method, m);
                        SpreadsheetLabelFetcherWatchersTest.this.checkEquals(u, u);
                        SpreadsheetLabelFetcherWatchersTest.this.checkEquals(b, b);
                        SpreadsheetLabelFetcherWatchersTest.this.checkEquals(appContext, context);

                        SpreadsheetLabelFetcherWatchersTest.this.fired++;
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
        final AppContext appContext = AppContexts.fake();

        final SpreadsheetLabelFetcherWatchers watchers = SpreadsheetLabelFetcherWatchers.empty();
        watchers.add(
                new FakeSpreadsheetLabelFetcherWatcher() {
                    @Override
                    public void onError(final Object c,
                                        final AppContext context) {
                        SpreadsheetLabelFetcherWatchersTest.this.checkEquals(cause, c);
                        SpreadsheetLabelFetcherWatchersTest.this.checkEquals(appContext, context);

                        SpreadsheetLabelFetcherWatchersTest.this.fired++;
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

        final HttpMethod method = HttpMethod.GET;
        final AbsoluteOrRelativeUrl url = Url.parseRelative("/api/something/123");
        final HttpStatus status = HttpStatusCode.withCode(123).setMessage("status message 456");
        final Headers headers = null;
        final String body = "Body-failure-789";
        final AppContext appContext = AppContexts.fake();

        final SpreadsheetLabelFetcherWatchers watchers = SpreadsheetLabelFetcherWatchers.empty();
        watchers.add(
                new FakeSpreadsheetLabelFetcherWatcher() {

                    @Override
                    public void onFailure(final HttpMethod m,
                                          final AbsoluteOrRelativeUrl u,
                                          final HttpStatus s,
                                          final Headers h,
                                          final String b,
                                          final AppContext context) {
                        SpreadsheetLabelFetcherWatchersTest.this.checkEquals(method, m);
                        SpreadsheetLabelFetcherWatchersTest.this.checkEquals(url, u);
                        SpreadsheetLabelFetcherWatchersTest.this.checkEquals(status, s);
                        SpreadsheetLabelFetcherWatchersTest.this.checkEquals(headers, h);
                        SpreadsheetLabelFetcherWatchersTest.this.checkEquals(body, b);
                        SpreadsheetLabelFetcherWatchersTest.this.checkEquals(appContext, context);

                        SpreadsheetLabelFetcherWatchersTest.this.fired++;
                    }
                });
        watchers.onFailure(method, url, status, headers, body, appContext);
        this.checkEquals(1, this.fired);

        watchers.onFailure(method, url, status, headers, body, appContext);
        this.checkEquals(2, this.fired);

        watchers.onFailure(method, url, status, headers, body, appContext);
        this.checkEquals(3, this.fired);
    }

    private int fired = 0;

    // ClassTesting....................................................................................................

    @Override
    public Class<SpreadsheetLabelFetcherWatchers> type() {
        return SpreadsheetLabelFetcherWatchers.class;
    }
}
