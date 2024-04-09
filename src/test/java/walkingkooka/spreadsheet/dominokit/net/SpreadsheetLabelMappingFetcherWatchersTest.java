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

public final class SpreadsheetLabelMappingFetcherWatchersTest extends FetcherWatchersTestCase<SpreadsheetLabelMappingFetcherWatchers> {

    @Test
    public void testAddThenFire() {
        this.fired = 0;

        final SpreadsheetId spreadsheetId = SpreadsheetId.with(1);
        final Optional<SpreadsheetLabelMapping> spreadsheetLabelMapping = Optional.of(
                SpreadsheetSelection.labelName("Label123")
                        .mapping(SpreadsheetSelection.A1)
        );
        final AppContext appContext = AppContexts.fake();

        final SpreadsheetLabelMappingFetcherWatchers watchers = SpreadsheetLabelMappingFetcherWatchers.empty();
        watchers.add(
                new FakeSpreadsheetLabelMappingFetcherWatcher() {
                    @Override
                    public void onSpreadsheetLabelMapping(final SpreadsheetId id,
                                                          final Optional<SpreadsheetLabelMapping> mapping,
                                                          final AppContext context) {
                        SpreadsheetLabelMappingFetcherWatchersTest.this.checkEquals(spreadsheetId, id);
                        SpreadsheetLabelMappingFetcherWatchersTest.this.checkEquals(spreadsheetLabelMapping, mapping);
                        SpreadsheetLabelMappingFetcherWatchersTest.this.checkEquals(appContext, context);

                        SpreadsheetLabelMappingFetcherWatchersTest.this.fired++;
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

        final SpreadsheetLabelMappingFetcherWatchers watchers = SpreadsheetLabelMappingFetcherWatchers.empty();
        watchers.addOnce(
                new FakeSpreadsheetLabelMappingFetcherWatcher() {
                    @Override
                    public void onSpreadsheetLabelMapping(final SpreadsheetId id,
                                                          final Optional<SpreadsheetLabelMapping> mapping,
                                                          final AppContext context) {
                        SpreadsheetLabelMappingFetcherWatchersTest.this.checkEquals(spreadsheetId, id);
                        SpreadsheetLabelMappingFetcherWatchersTest.this.checkEquals(spreadsheetLabelMapping, mapping);
                        SpreadsheetLabelMappingFetcherWatchersTest.this.checkEquals(appContext, context);

                        SpreadsheetLabelMappingFetcherWatchersTest.this.fired++;
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

        final SpreadsheetLabelMappingFetcherWatchers watchers = SpreadsheetLabelMappingFetcherWatchers.empty();
        watchers.add(
                new FakeSpreadsheetLabelMappingFetcherWatcher() {

                    @Override
                    public void onBegin(final HttpMethod m,
                                        final Url u,
                                        final Optional<String> b,
                                        final AppContext context) {
                        SpreadsheetLabelMappingFetcherWatchersTest.this.checkEquals(method, m);
                        SpreadsheetLabelMappingFetcherWatchersTest.this.checkEquals(u, u);
                        SpreadsheetLabelMappingFetcherWatchersTest.this.checkEquals(b, b);
                        SpreadsheetLabelMappingFetcherWatchersTest.this.checkEquals(appContext, context);

                        SpreadsheetLabelMappingFetcherWatchersTest.this.fired++;
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

        final SpreadsheetLabelMappingFetcherWatchers watchers = SpreadsheetLabelMappingFetcherWatchers.empty();
        watchers.add(
                new FakeSpreadsheetLabelMappingFetcherWatcher() {
                    @Override
                    public void onError(final Object c,
                                        final AppContext context) {
                        SpreadsheetLabelMappingFetcherWatchersTest.this.checkEquals(cause, c);
                        SpreadsheetLabelMappingFetcherWatchersTest.this.checkEquals(appContext, context);

                        SpreadsheetLabelMappingFetcherWatchersTest.this.fired++;
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

        final SpreadsheetLabelMappingFetcherWatchers watchers = SpreadsheetLabelMappingFetcherWatchers.empty();
        watchers.add(
                new FakeSpreadsheetLabelMappingFetcherWatcher() {

                    @Override
                    public void onFailure(final HttpMethod m,
                                          final AbsoluteOrRelativeUrl u,
                                          final HttpStatus s,
                                          final Headers h,
                                          final String b,
                                          final AppContext context) {
                        SpreadsheetLabelMappingFetcherWatchersTest.this.checkEquals(method, m);
                        SpreadsheetLabelMappingFetcherWatchersTest.this.checkEquals(url, u);
                        SpreadsheetLabelMappingFetcherWatchersTest.this.checkEquals(status, s);
                        SpreadsheetLabelMappingFetcherWatchersTest.this.checkEquals(headers, h);
                        SpreadsheetLabelMappingFetcherWatchersTest.this.checkEquals(body, b);
                        SpreadsheetLabelMappingFetcherWatchersTest.this.checkEquals(appContext, context);

                        SpreadsheetLabelMappingFetcherWatchersTest.this.fired++;
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
    public Class<SpreadsheetLabelMappingFetcherWatchers> type() {
        return SpreadsheetLabelMappingFetcherWatchers.class;
    }
}
